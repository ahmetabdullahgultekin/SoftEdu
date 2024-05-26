package com.gultekinahmetabdullah.softedu.signinsignup

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants
import com.gultekinahmetabdullah.softedu.theme.getCustomOutlinedTextFieldColors
import com.gultekinahmetabdullah.softedu.util.Screen

@Composable
fun UserInfoScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    val db = Firebase.firestore
    val context = LocalContext.current
    val totalQuestions = 10


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            colors = getCustomOutlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
            colors = getCustomOutlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("Nickname") },
            colors = getCustomOutlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                // Navigate to the test screen
                if (saveProfileInfo(name, surname, nickname, db, context)) {
                    navController.navigate(Screen.BottomScreen.Learnings.Quiz.bRoute + ",${true},${totalQuestions}")
                }
            }) {
            Text("Continue")
        }
    }
}


private fun saveProfileInfo(name: String,
                            surname: String,
                            nickname: String,
                            db: FirebaseFirestore,
                            context: Context): Boolean {

    // Check if name, surname or nickname is empty
    if (name.isBlank() || surname.isBlank() || nickname.isBlank()) {
        Toast.makeText(context, "Name, surname and nickname cannot be empty.", Toast.LENGTH_SHORT).show()
        return false
    }

    // Check if the nickname already exists in the database
    db.collection(FirestoreConstants.COLLECTION_USERS)
        .whereEqualTo(FirestoreConstants.FIELD_NICKNAME, nickname)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                // The nickname does not exist, proceed with saving the profile information
                val user = hashMapOf(
                    FirestoreConstants.FIELD_NAME to name,
                    FirestoreConstants.FIELD_SURNAME to surname,
                    FirestoreConstants.FIELD_NICKNAME to nickname,
                    FirestoreConstants.FIELD_EXPERIENCE_LEVEL to 0,
                    FirestoreConstants.FIELD_SCORE to 0
                )

                val userId = FirebaseAuth.getInstance().currentUser?.uid
                userId?.let {
                    db.collection(FirestoreConstants.COLLECTION_USERS)
                        .document(it) // Use auth.uid as the document ID
                        .set(user) // Use set instead of add
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile information saved!", Toast.LENGTH_SHORT).show()
                            //                    navController.navigate("home") // Navigate to "home" screen
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error saving profile information.", Toast.LENGTH_SHORT).show()
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }
                }
            } else {
                // The nickname already exists, show a Toast message
                Toast.makeText(context, "This nickname is already taken. Please choose another one.", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error checking the nickname.", Toast.LENGTH_SHORT).show()
            Log.w(ContentValues.TAG, "Error checking the nickname", e)
        }

    return true
}
