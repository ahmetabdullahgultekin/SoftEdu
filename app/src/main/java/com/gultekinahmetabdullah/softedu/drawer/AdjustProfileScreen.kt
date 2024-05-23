package com.gultekinahmetabdullah.softedu.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants
import java.util.Locale

@Composable
fun AdjustProfileScreen() {
    val auth = Firebase.auth
    val db = Firebase.firestore
    val context = LocalContext.current

    var selectedField by remember { mutableStateOf("") }
    var newValue by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }

    val fields = listOf("Name", "Surname", "Password")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        fields.forEach { field ->
            Button(onClick = { selectedField = field }) {
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
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,

                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    selectionColors = TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.onPrimary,
                        backgroundColor = MaterialTheme.colorScheme.onPrimary
                    )
                ),
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
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,

                cursorColor = MaterialTheme.colorScheme.onPrimary,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = MaterialTheme.colorScheme.onPrimary
                )
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val user = auth.currentUser
            if (user != null) {
                when (selectedField) {
                    "Name", "Surname" -> {
                        // Update name or surname in Firestore
                        val docRef = db.collection(FirestoreConstants.COLLECTION_USERS).document(user.uid)
                        docRef.update(selectedField.lowercase(Locale.getDefault()), newValue)
                            .addOnSuccessListener {
                                android.widget.Toast.makeText(context, "$selectedField updated", android.widget.Toast.LENGTH_SHORT).show()
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
                                                android.widget.Toast.makeText(context, "Password updated", android.widget.Toast.LENGTH_SHORT).show()
                                            } else {
                                                android.widget.Toast.makeText(context, "Error updating password", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    android.widget.Toast.makeText(context, "Incorrect old password", android.widget.Toast.LENGTH_SHORT).show()
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
