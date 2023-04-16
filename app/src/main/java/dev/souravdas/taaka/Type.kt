package dev.souravdas.taaka

/**
 * Created by Sourav
 * On 4/15/2023 12:32 PM
 * For Taaka
 */
enum class TxnType(val value: String) {
    TXN_DEBIT ("IN"),
    TXN_CREDIT ("OUT"),
    TXN_PURCHASE("Purchase")
}

enum class SourceType{
    DEBIT_CARD,
    CREDIT_CARD,
    ACCOUNT
}

enum class BankType(val value: String){
    EBL("EBL")
}