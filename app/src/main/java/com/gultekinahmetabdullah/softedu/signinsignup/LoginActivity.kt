package com.gultekinahmetabdullah.softedu.signinsignup

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants
import com.gultekinahmetabdullah.softedu.database.updateAnsweredQuestions
import com.gultekinahmetabdullah.softedu.leaderboard.User
import com.gultekinahmetabdullah.softedu.util.Screen
import java.util.UUID

@Composable
fun ProfileInfoScreen(navController: NavController) {
    val context = LocalContext.current
    val db = Firebase.firestore
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var experienceLevel by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
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

        Spacer(modifier = Modifier.height(16.dp))

        Text("Experience Level: ${experienceLevel.toInt()}")
        Slider(
            value = experienceLevel,
            onValueChange = { experienceLevel = it },
            valueRange = 0f .. 5f,
            steps = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            saveProfileInfo(name, surname, experienceLevel, db, context, navController)
        }) {
            Text("Submit")
        }
    }
}


private fun saveProfileInfo(name: String,
                            surname: String,
                            experienceLevel: Float,
                            db: FirebaseFirestore,
                            context: Context,
                            navController: NavController) {
    val user = hashMapOf(
        FirestoreConstants.FIELD_NAME to name,
        FirestoreConstants.FIELD_SURNAME to surname,
        FirestoreConstants.FIELD_EXPERIENCE_LEVEL to experienceLevel.toInt(),
        FirestoreConstants.FIELD_SCORE to 0
    )
    val auth: FirebaseAuth = Firebase.auth
    val userId = auth.currentUser?.uid

    userId?.let {
        db.collection(FirestoreConstants.COLLECTION_USERS)
            .document(it) // Use auth.uid as the document ID
            .set(user) // Use set instead of add
            .addOnSuccessListener {
                Toast.makeText(context, "Profile information saved!", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.BottomScreen.Home.bRoute)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error saving profile information.", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error adding document", e)
            }
    }
}


@Composable
fun MultipleChoiceQuestionScreen(navController: NavController) {
    val context = LocalContext.current
    val db = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    var questionText by remember { mutableStateOf("") }
    var choices by remember { mutableStateOf(listOf<String>()) }
    var correctChoice by remember { mutableIntStateOf(- 1) }
    var questionCounter by remember { mutableIntStateOf(0) }
    var isAnswerSelected by remember { mutableStateOf(false) }
    var experienceLevel by remember { mutableIntStateOf(0) }
    var userId by remember { mutableStateOf(auth.currentUser?.uid) }
    var questionId by remember { mutableStateOf("") }
    var askedQuestionIds by remember { mutableStateOf(listOf<String>()) }


    // Function to fetch the user's profile information from Firestore
    val triple = getUserInfo(userId, auth, db, experienceLevel)
    val fetchUserProfile = triple.first
    experienceLevel = triple.second
    userId = triple.third

    // Function to fetch a question from Firestore
    val fetchQuestion = {
        userId?.let { userId ->
            db.collection(FirestoreConstants.COLLECTION_USERS).document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {  // If the user document exists
                        // Get the correctQuestions array from the user's document
                        val correctQuestions = document.get(FirestoreConstants.FIELD_CORRECT_QUESTIONS) as? List<String> ?: emptyList()
                        // Generate a random string
                        val randomString = UUID.randomUUID().toString()

                        // Fetch each question that is not in the correctQuestions array
                        db.collection(FirestoreConstants.COLLECTION_QUESTIONS)
                            .whereGreaterThanOrEqualTo(FieldPath.documentId(), randomString) // fetch random question
                            .whereGreaterThanOrEqualTo(FirestoreConstants.FIELD_DIFFICULTY_LEVEL, experienceLevel)
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    if (! correctQuestions.contains(document.id) && ! askedQuestionIds.contains(document.id)) {
                                        questionId = document.id
                                        questionText = document.getString(FirestoreConstants.FIELD_QUESTION_TEXT) ?: ""
                                        choices = document.get(FirestoreConstants.FIELD_CHOICES) as List<String>
                                        correctChoice = document.getLong(FirestoreConstants.FIELD_CORRECT_CHOICE)?.toInt() ?: - 1
                                        questionCounter ++
                                        isAnswerSelected = false
                                        askedQuestionIds += document.id
                                        break
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error getting documents.", exception)
                            }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }
    LaunchedEffect(key1 = Unit) {
        fetchUserProfile()
        fetchQuestion()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Progress bar
        LinearProgressIndicator(
            progress = { questionCounter / 10f },
            modifier = Modifier.fillMaxWidth(),
        )

        Text(text = questionText + "\n" + questionId)

        Spacer(modifier = Modifier.height(16.dp))

        choices.forEachIndexed { index, choice ->
            Button(onClick = {
                if (index == correctChoice) {
                    userId?.let { updateAnsweredQuestions(it, questionId, true) }
                    Toast.makeText(context, "Correct choice!", Toast.LENGTH_SHORT).show()
                } else {
                    userId?.let { updateAnsweredQuestions(it, questionId, false) }
                    Toast.makeText(context, "Incorrect choice! The correct answer is $correctChoice.", Toast.LENGTH_SHORT).show()
                }
                isAnswerSelected = true
            },
                   enabled = ! isAnswerSelected
            ) {
                Text(choice)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
        if (isAnswerSelected) {
            // Continue button
            Button(onClick = {
                fetchQuestion()
                if (questionCounter >= 10) {
                    navController.navigate(Screen.BottomScreen.Home.bRoute)
                }
            }) {
                Text("Continue")
            }
        }
    }
}

@Composable
private fun getUserInfo(userId: String?,
                        auth: FirebaseAuth,
                        db: FirebaseFirestore,
                        experienceLevel: Int): Triple<() -> Task<DocumentSnapshot>?, Int, String?> {
    var userId1 = userId
    var experienceLevel1 = experienceLevel
    val function = {
        userId1 = auth.currentUser?.uid
        userId1?.let {
            db.collection(FirestoreConstants.COLLECTION_USERS)
                .document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        experienceLevel1 = document.getLong(FirestoreConstants.FIELD_EXPERIENCE_LEVEL)?.toInt() ?: 0
                    } else {
                        Log.d(TAG, "No such user document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }
    return Triple(function, experienceLevel1, userId1)
}


@Composable
fun MainScreen(navController: NavController, auth: FirebaseAuth) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to the Main Screen!")

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("multipleChoiceQuestion") }) {
            Text("Multiple Choice Question")
        }
        Button(onClick = { navController.navigate("leaderboard") }) {
            Text("Leaderboard")
        }
        Button(onClick = { navController.navigate("profile") }) {
            Text("Profile")
        }

    }
}

@Composable
fun ProfileScreen(navController: NavController, auth: FirebaseAuth) {
    val db = Firebase.firestore
    val userId = auth.currentUser?.uid
    val context = LocalContext.current
    var user by remember { mutableStateOf(User("", "", "", 0)) }
    var users by remember { mutableStateOf(listOf<User>()) }
    var rank by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = userId) {
        userId?.let { it ->
            db.collection(FirestoreConstants.COLLECTION_USERS)
                .get()
                .addOnSuccessListener { result ->
                    users = result.documents.mapNotNull { it.toObject(User::class.java) }
                    users = users.sortedByDescending { it.score }
                }

            db.collection(FirestoreConstants.COLLECTION_USERS)
                .document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString(FirestoreConstants.FIELD_NAME) ?: ""
                        val surname = document.getString(FirestoreConstants.FIELD_SURNAME) ?: ""
                        val score = document.getLong(FirestoreConstants.FIELD_SCORE)?.toInt() ?: 0
                        user = User(document.id, name, surname, score)
                        rank = users.indexOfFirst { it.name == user.name && it.surname == user.surname } + 1
                    }
                }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Profile", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Name: ${user.name}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Surname: ${user.surname}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Score: ${user.score}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Rank: $rank", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                auth.signOut()
                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("login") // Navigate back to sign-in screen
            }) {
                Text("Logout")
            }
        }
    }
}
