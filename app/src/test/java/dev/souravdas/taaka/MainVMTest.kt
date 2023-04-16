package dev.souravdas.taaka

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Created by Sourav
 * On 4/15/2023 8:09 PM
 * For Taaka
 */


class MainVMTest {

    private lateinit var viewmodel: MainVM

    @Before
    fun setup(){
        viewmodel = MainVM()
    }

    @Test
    fun parse_text_data(){
        val item = "Purchase txn BDT 690 from bKash Limited Dhak.Card 539280**0870 on 02-Apr-23 11:19:33 AM BST.Your A/C 122**3607 Balance BDT 95853.17. EBL Helpline 16230"
        val txn = viewmodel.getDebitTransaction(item, BankType.EBL)

        assertThat(txn.txnFrom).isEqualTo("539280**0870")
    }

    @Test
    fun matches_pattern(){
        val item = "Purchase txn BDT 690 from bKash Limited Dhak.Card 123456**1234 on 02-Apr-23 11:19:33 AM BST.Your A/C 122**3607 Balance BDT 12345.17. EBL Helpline 16230"
        val result = viewmodel.matchesPatternForTxn(item)

        assertThat(result).isEqualTo(true)
    }
}