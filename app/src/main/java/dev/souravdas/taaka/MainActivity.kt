package dev.souravdas.taaka

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.souravdas.taaka.ui.theme.TaakaTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaakaTheme {
                Home()
            }
        }

        if (checkSmsPermission(this)) {
            val smsList = getAllSmsFromSpecificContact(applicationContext, "EBL")


        } else {
            // Permission is not granted, request it at runtime
            requestSmsPermission(this)
        }
    }

    private val TAG: String = "RESULT"
    private val PERMISSION_REQUEST_CODE = 123

    fun checkSmsPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestSmsPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_SMS),
            PERMISSION_REQUEST_CODE
        )
    }

    fun getAllSmsFromSpecificContact(context: Context, senderName: String): List<String> {
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaakaTheme {
        Greeting("Android")
    }
}