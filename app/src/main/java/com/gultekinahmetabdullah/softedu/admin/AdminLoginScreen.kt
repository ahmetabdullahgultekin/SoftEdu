package com.gultekinahmetabdullah.softedu.admin

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.softedu.util.Screen
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AdminLoginScreen(navController: NavController) {
    var accessKey by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = accessKey,
            onValueChange = { accessKey = it },
            label = { Text("Access Key") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                scope.launch {
                    if (checkAdminKey(accessKey)) {
                        Toast.makeText(context, "Access granted.", Toast.LENGTH_SHORT).show()
                        isAdmin = true
                        // Save admin status in SharedPreferences
                        with(sharedPreferences.edit()) {
                            putBoolean("isAdmin", true)
                            apply()
                        }
                        navController.navigate(Screen.BottomScreen.AdminHome.route)
                    } else {
                        // If the access key does not exist, show an error message
                        Toast.makeText(context, "Invalid access key.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Log in")
        }
    }
}


suspend fun checkAdminKey(key: String): Boolean {
    // Get a Firestore instance
    val db = FirebaseFirestore.getInstance()

    try {
        val querySnapshot = db.collection("accessKeys").whereEqualTo("key", key).get().await()
        return ! querySnapshot.isEmpty
    } catch (e: Exception) {
        // If the query fails, log the error and set keyExists to false
        Log.w(ContentValues.TAG, "Error checking admin key", e)
        return false
    }
}

fun checkAdminStatus(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isAdmin", false)
}