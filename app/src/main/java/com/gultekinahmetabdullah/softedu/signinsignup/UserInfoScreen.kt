package com.gultekinahmetabdullah.softedu.signinsignup

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants
import com.gultekinahmetabdullah.softedu.theme.getCustomOutlinedTextFieldColors
import com.gultekinahmetabdullah.softedu.util.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun UserInfoScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    val db = Firebase.firestore
    val context = LocalContext.current
    val totalQuestions = 10
    val coroutineScope = rememberCoroutineScope()



    Column(
        modifier = Modifier
            .fillMaxSize()
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
                // Launch a new coroutine
                coroutineScope.launch {
                    // Navigate to the test screen
                    val pair = saveProfileInfo(name, surname, nickname, db, context)
                    val result = pair.first
                    val message = pair.second
                    if (result) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.BottomScreen.Learnings.Quiz.bRoute + ",${true},${totalQuestions}")
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
            Text("Continue")
        }
    }
}


private suspend fun saveProfileInfo(name: String,
                                    surname: String,
                                    nickname: String,
                                    db: FirebaseFirestore,
                                    context: Context): Pair<Boolean, String> = withContext(Dispatchers.IO) {

    // Check if name, surname or nickname is empty
    if (name.isBlank() || surname.isBlank() || nickname.isBlank()) {
        return@withContext Pair(false, "Name, surname and nickname cannot be empty.")
    }

    // Check if the nickname already exists in the database
    val documents = db.collection(FirestoreConstants.COLLECTION_USERS)
        .whereEqualTo(FirestoreConstants.FIELD_NICKNAME, nickname)
        .get()
        .await()

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
            try {
                db.collection(FirestoreConstants.COLLECTION_USERS)
                    .document(it) // Use auth.uid as the document ID
                    .set(user) // Use set instead of add
                    .await()
                return@withContext Pair(true, "Profile information saved!")
            } catch (e: Exception) {
                return@withContext Pair(false, "Error saving profile information.")
            }
        }
    } else {
        // The nickname already exists, show a Toast message
        return@withContext Pair(false, "This nickname is already taken. Please choose another one.")
    }

    return@withContext Pair(false, "An unknown error occurred.")
}