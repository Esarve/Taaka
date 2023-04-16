package dev.souravdas.taaka

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.threeten.bp.format.DateTimeFormatter
import java.time.temporal.TemporalAmount

/**
 * Created by Sourav
 * On 4/16/2023 3:01 PM
 * For Taaka
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: MainVM = viewModel()) {
    val txns = viewModel.transactions.collectAsState(initial = emptyList())

    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Accounts",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(txns.value.groupBy { it.txnFrom }.entries.toList()){index, item ->
                    AccountView(item.key, item.value[0].accountBalance ?: 0.00, index)
                }
            }

            Text(
                text = "Transactions",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                itemsIndexed(txns.value) { index, txn ->
                    TxnItem(txn, index)
                    Box(modifier = Modifier.padding(top = 4.dp).fillMaxWidth().height(1.dp).padding(horizontal = 8.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape))

                }
            }
        }
    }
}

@Composable
fun TxnItem(txn: Transaction, index: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp, 48.dp)
                    .clip(CircleShape)
                    .background(GenerateTextColor(index)),
                contentAlignment = Alignment.Center
            ) {
                txn.txnTo?.let {
                    Text(
                        text = it.substring(0, 1),
                        fontSize = 18.sp,
                        color = GenerateBG(index)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .padding(end = 8.dp, start = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = txn.txnTo.clip(),
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "- BDT ${txn.amount ?: 0.00}",
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Text(
                    text = "${
                        txn.txnDate?.toLocalDate()
                            ?.format(DateTimeFormatter.ofPattern("dd MMMM YYYY")) ?: ""
                    }",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

}

@Composable
fun GenerateTextColor(index: Int): Color {
    return when (index.mod(3)) {
        0 -> MaterialTheme.colorScheme.onPrimaryContainer
        1 -> MaterialTheme.colorScheme.onSecondaryContainer
        2 -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> {
            MaterialTheme.colorScheme.primary
        }
    }
}

@Composable
fun GenerateBG(value: Int): Color {
    return when (value.mod(3)) {
        0 -> MaterialTheme.colorScheme.primaryContainer
        1 -> MaterialTheme.colorScheme.secondaryContainer
        2 -> MaterialTheme.colorScheme.tertiaryContainer
        else -> {
            MaterialTheme.colorScheme.primary
        }
    }
}

private fun String?.clip(): String {
    if (this.isNullOrEmpty()) {
        return ""
    } else {
        return if (this.length > 16) {
            this.substring(0, 16).plus("...")
        } else {
            this
        }
    }
}

@Composable
fun AccountView(key: String?, amount: Double, index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = GenerateAccountColor(index))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${key!!}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "EDIT",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)) {
                Text(
                    text = "BDT $amount",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    }
}

@Composable
fun GenerateAccountColor(index: Int): Color {
    return when (index.mod(3)) {
        0 -> MaterialTheme.colorScheme.primary
        1 -> MaterialTheme.colorScheme.secondary
        2 -> MaterialTheme.colorScheme.tertiary
        else -> {
            MaterialTheme.colorScheme.primary
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AccountViewPreview() {
    AccountView(key = "123123**213", 69420.00,1)
}
