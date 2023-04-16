package dev.souravdas.taaka

import android.content.Context
import android.net.Uri
import android.provider.Telephony
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale


/**
 * Created by Sourav
 * On 4/15/2023 12:41 PM
 * For Taaka
 */
class MainVM : ViewModel() {

    private val _transactions  = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions = _transactions.asStateFlow()

    init {
        getAllTxns(BankType.EBL)
    }


    fun getAllTxns(bankType: BankType) {
        viewModelScope.launch {
            var id = 0L;
            val smsList = getAllSmsFromSpecificContact(Taaka.context, bankType.value)
            val txnList: MutableList<Transaction> = mutableListOf()

            smsList.forEach {
                if (matchesPatternForTxn(it)) {
                    txnList.add(getDebitTransaction(it, bankType, id))
                }
                id++
            }

            val map = txnList.groupBy { it.txnFrom }

            _transactions.value = txnList.sortedBy { it.txnDate }.reversed()
        }
    }

    fun getDebitTransaction(text: String, bankType: BankType, id:Long): Transaction {
        val amountMatch = Regex.DEBIT_AMOUNT.find(text)
        val senderNameMatch = Regex.DEBIT_TO.find(text)
        val cardNumberMatch = Regex.DEBIT_FROM_CARD.find(text)
        val dateMatch = Regex.DEBIT_TIME.find(text)
        val balanceMatch = Regex.ACCOUNT_BALANCE.find(text)

        val amount = try{amountMatch?.groupValues?.get(1)}catch (e: Exception){amountMatch?.groupValues?.get(0)}
        val senderName = try{senderNameMatch?.groupValues?.get(1)}catch (e: Exception){senderNameMatch?.groupValues?.get(0)}
        val cardNumber = cardNumberMatch?.groupValues?.get(1)
        val date = dateMatch?.groupValues?.get(1)
        val balance = balanceMatch?.groupValues?.get(1)

        return Transaction(
            id,
            Metadata(
                bankType, TxnType.TXN_PURCHASE, SourceType.DEBIT_CARD
            ),
            amount?.toDoubleOrNull(),
            senderName.formatToAccount(),
            cardNumber,
            toLocalDate(date),
            balance?.toDoubleOrNull()
        )
    }

    private fun toLocalDate(date: String?): LocalDateTime? {
        return try {
            val inputFormat = DateTimeFormatter.ofPattern("dd-MMM-yy hh:mm:ss a", Locale.ENGLISH)
            val formattedDate = date?.replace(" BST", "")
            LocalDateTime.parse(formattedDate,inputFormat)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    fun matchesPatternForTxn(text: String): Boolean {
        return Regex.TXN_SMS.matches(text)
    }

    private fun matchesPatternForCredit(text: String): Boolean {
        return Regex.CREDIT_SMS_PATTERN.matches(text)
    }

    private fun getAllSmsFromSpecificContact(context: Context, senderName: String): List<String> {
        val smsList = mutableListOf<String>()
        val cursor = context.contentResolver.query(
            Uri.parse("content://sms/inbox"), null,
            "address LIKE '%$senderName%'", null, null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                smsList.add(body)
            }
            cursor.close()
        }
        return smsList
    }

}

private fun String?.formatToAccount(): String? {
    return try {
        val formatted = this?.replace("from ","")?.replace(".", "")
        formatted!!.replaceFirstChar{it.uppercaseChar()}
    }catch (e: Exception){
        ""
    }

}

