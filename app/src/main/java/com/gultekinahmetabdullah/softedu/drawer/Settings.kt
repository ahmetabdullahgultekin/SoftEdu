package com.gultekinahmetabdullah.softedu.drawer

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants
import java.io.FileInputStream
import java.security.MessageDigest

@Composable
fun Settings(isDarkTheme: MutableState<Boolean>) {
    //TODO add settings
    val settings = listOf("Dark Mode", "Notifications", "Privacy", "About", "Help")
    val showDialog = remember { mutableStateOf(false) }
    val shortUrl = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        SwitchButtonOnRow(settings[0], isDarkTheme)
        CardWithUpdateLink(showDialog, shortUrl)
        if (showDialog.value) {
            val uriHandler = LocalUriHandler.current
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Update Available") },
                text = {
                    Text(
                        "A new version of the app is available. " +
                                "Would you like to download it?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        uriHandler.openUri(shortUrl.value)
                        showDialog.value = false
                    }) {
                        Text("Download")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardWithUpdateLink(showDialog: MutableState<Boolean>, shortUrl: MutableState<String>) {

    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ClickableText(text = AnnotatedString(text = "Check for Update"),
                onHover = { },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .fillMaxWidth(),
                style = TextStyle.Default.copy(
                    color = Color.Cyan,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                ),
                onClick = {
                    Toast.makeText(context, "Checking for update!", Toast.LENGTH_SHORT).show()
                    checkForUpdate(context, showDialog, shortUrl)
                }
            )

            Text(
                text = "v.${versionName}",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

fun checkForUpdate(
    context: Context,
    showDialog: MutableState<Boolean>,
    shortUrl: MutableState<String>
) {
    try {
        val buffer = ByteArray(1024)
        val md = MessageDigest.getInstance("SHA-256")
        val fis = FileInputStream(context.applicationContext.packageResourcePath)
        var numRead: Int

        while (fis.read(buffer).also { numRead = it } != -1) {
            md.update(buffer, 0, numRead)
        }
        fis.close()

        val hashBytes = md.digest()
        val sb = StringBuilder()
        for (byte in hashBytes) {
            sb.append(String.format("%02x", byte))
        }
        val localHash = sb.toString()

        // Get Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Get the document
        db.collection(FirestoreConstants.COLLECTION_APKS).document(FirestoreConstants.FIELD_LATEST)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val firestoreHash = document.getString(FirestoreConstants.FIELD_HASH_CODE)
                    val firestoreShortUrl = document.getString(FirestoreConstants.FIELD_SHORT_LINK)

                    if (firestoreHash != null && firestoreShortUrl != null) {
                        if (firestoreHash != localHash) {
                            shortUrl.value = firestoreShortUrl
                            showDialog.value = true
                        } else {
                            Toast.makeText(
                                context,
                                "You are using the latest version!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                } else {
                    Log.d("Firestore", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Firestore", "get failed with ", exception)
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun SwitchButtonOnRow(btnText: String, isDarkTheme: MutableState<Boolean>) {

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = btnText, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )
            Switch(modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically),
                checked = isDarkTheme.value, onCheckedChange = { isDarkTheme.value = it },
                thumbContent = {
                    Text(
                        text = if (isDarkTheme.value) "🌙" else "☀️",
                        style = TextStyle.Default.copy(
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    )
                }
            )
        }
    }
}
