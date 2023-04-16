package dev.souravdas.taaka

/**
 * Created by Sourav
 * On 4/15/2023 12:01 PM
 * For Taaka
 */
object Regex {
    val TXN_SMS = ".*Purchase txn (USD|BDT) .* from .* on .* Balance.* (?i)EBL Helpline .*".toRegex()
//    val TXN_SMS_PATTERN = ".*Purchase txn (USD|BDT)\\s+\\d+[\\d,.]* from [\\w. ]+\\. Card \\d+\\*{2}\\d+ on \\d{2}-[a-zA-Z]{3}-\\d{2} \\d{2}:\\d{2}:\\d{2} [AP]M [A-Z]{3}\\. Balance: (USD|BDT)\\s+\\d+[\\d,.]*.*".toRegex()
    val DEBIT_AMOUNT = "BDT ([\\d.]+)".toRegex()
    val DEBIT_TO = "from\\s+.*?\\.".toRegex()
    val DEBIT_FROM_CARD = "Card\\s+([\\d*]{12,19})".toRegex()
    val DEBIT_TIME = "on (\\d{2}-[A-Za-z]{3}-\\d{2} \\d{2}:\\d{2}:\\d{2} [AP]M [A-Z]{3})\\.".toRegex()
    val ACCOUNT_BALANCE = "Balance: BDT ([\\d.]+)\\.".toRegex()
    val CREDIT_SMS_PATTERN = ".*is credited with.*".toRegex()
//    val TXN_SMS_PATTERN_BOTH = "^Purchase txn\\sBDT\\s\\d+\\sfrom\\s[A-Z\\s]+\\.?\\sCard\\s\\d{6}\\*\\*\\d{4}\\son\\s\\d{2}-[A-Z][a-z]{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\s[AP]M\\s[A-Z]{3}\\.\\sYour\\sA\\/C\\s\\d{3}\\*\\*\\d{4}\\sBalance\\sBDT\\s\\d+\\.\\d{2}\\.\\sABC\\sHelpline\\s\\d{5}\$".toRegex()
}
