package com.gultekinahmetabdullah.softedu.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants
import com.gultekinahmetabdullah.softedu.theme.getCustomButtonColors
import com.gultekinahmetabdullah.softedu.theme.getCustomOutlinedTextFieldColors
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

@Composable
fun AdjustProfileScreen() {
    val auth = Firebase.auth
    val db = Firebase.firestore
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedField by remember { mutableStateOf("") }
    var newValue by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val fields = listOf("Nickname", "Name", "Surname", "Password")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        fields.forEach { field ->
            Button(colors = getCustomButtonColors(),
                onClick = {
                    selectedField = field
                    newValue = ""
                    oldPassword = ""
                    errorMessage = ""
                }) {
                Text(field)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectedField == "Password") {
            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = { Text("Old Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = getCustomOutlinedTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = newValue,
            onValueChange = { newValue = it },
            label = { Text("New $selectedField") },
            visualTransformation = if (selectedField == "Password")
                PasswordVisualTransformation()
            else VisualTransformation.None,
            colors = getCustomOutlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        // Display the error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(colors = getCustomButtonColors(),
            onClick = {
                scope.launch {
                    val user = auth.currentUser
                    if (user != null) {
                        when (selectedField) {

                            "Name", "Surname" -> {
                                // Update name or surname in Firestore
                                if (newValue.trim().isNotEmpty()) {
                                    val docRef = db.collection(FirestoreConstants.COLLECTION_USERS)
                                        .document(user.uid)
                                    docRef.update(
                                        selectedField.lowercase(Locale.getDefault()),
                                        newValue
                                    )
                                        .addOnSuccessListener {
                                            android.widget.Toast.makeText(
                                                context,
                                                "$selectedField updated",
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                } else {
                                    // The new value is empty, show an error message
                                    errorMessage = "$selectedField cannot be empty"
                                }
                            }

                            "Password" -> {
                                // Sign in with email and old password
                                auth.signInWithEmailAndPassword(user.email ?: "", oldPassword)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Update password in FirebaseAuth
                                            user.updatePassword(newValue)
                                                .addOnCompleteListener { updateTask ->
                                                    if (updateTask.isSuccessful) {
                                                        android.widget.Toast.makeText(
                                                            context,
                                                            "Password updated",
                                                            android.widget.Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        errorMessage =
                                                            "Password enter a valid password"
                                                    }
                                                }
                                        } else {
                                            errorMessage = "Please check your old password"
                                        }
                                    }
                            }

                            "Nickname" -> {
                                if (newValue.trim().isNotEmpty()) {
                                    // Check if a user with the same nickname already exists
                                    val result = db.collection(FirestoreConstants.COLLECTION_USERS)
                                        .whereEqualTo(FirestoreConstants.FIELD_NICKNAME, newValue)
                                        .get()
                                        .await()

                                    if (result.isEmpty) {
                                        // No user with the same nickname exists, update the nickname
                                        val docRef =
                                            db.collection(FirestoreConstants.COLLECTION_USERS)
                                                .document(user.uid)
                                        docRef.update(FirestoreConstants.FIELD_NICKNAME, newValue)
                                            .addOnSuccessListener {
                                                errorMessage = ""
                                                android.widget.Toast.makeText(
                                                    context,
                                                    "$selectedField updated",
                                                    android.widget.Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    } else {
                                        // A user with the same nickname exists, show an error message
                                        errorMessage = "Nickname already exists"
                                    }

                                } else {
                                    // The new value is empty, show an error message
                                    errorMessage = "$selectedField cannot be empty"
                                }
                            }
                        }
                    }
                }
            }) {
            Text("Update Info")
        }
    }
}
