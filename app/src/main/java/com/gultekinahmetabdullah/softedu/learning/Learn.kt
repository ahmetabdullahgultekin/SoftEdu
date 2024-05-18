package com.gultekinahmetabdullah.softedu.learning


import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
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
import com.gultekinahmetabdullah.softedu.database.updateAnsweredQuestions
import com.gultekinahmetabdullah.softedu.theme.SoftEduTheme
import java.util.UUID


data class Choice(val choiceStr: String)

const val totalQuestions = 10
val possibleAnswers: List<Choice>? = null
val selectedAnswer: Choice? = null

@Composable
fun Learn(navController: NavController) {
    val context = LocalContext.current
    val db = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    var correctAnswered = 0

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
            db.collection("users").document(userId).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {  // If the user document exists
                            // Get the correctQuestions array from the user's document
                            val correctQuestions = document.get("correctQuestions") as? List<String> ?: emptyList()

                            // Fetch each question that is not in the correctQuestions array
                            db.collection("questions")
                                    .whereGreaterThanOrEqualTo("difficultyLevel", experienceLevel)
                                    .get()
                                    .addOnSuccessListener { result ->
                                        val documents = result.filter { document ->
                                            ! correctQuestions.contains(document.id) && ! askedQuestionIds.contains(document.id)
                                        }
                                        if (documents.isNotEmpty()) {
                                            val document = documents.random()
                                            questionId = document.id
                                            questionText = document.getString("questionText") ?: ""
                                            choices = document.get("choices") as List<String>
                                            correctChoice = document.getLong("correctChoice")?.toInt() ?: - 1
                                            questionCounter ++
                                            isAnswerSelected = false
                                            askedQuestionIds += document.id
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w(ContentValues.TAG, "Error getting documents.", exception)
                                    }
                        } else {
                            Log.d(ContentValues.TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
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
            progress = { (questionCounter - 1) / (totalQuestions).toFloat() },
            modifier = Modifier.fillMaxWidth(0.80f),  // Fill 80% of the width
            color = MaterialTheme.colorScheme.primary

        )

        Text(text = questionText + "\n" + questionId)

        Spacer(modifier = Modifier.height(16.dp))

        choices.forEachIndexed { index, choice ->  //TODO change buttons with choice boxes
            Button(onClick = {
                if (index == correctChoice) {
                    userId?.let { updateAnsweredQuestions(it, questionId, true) }
                    correctAnswered ++
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
                if (questionCounter >= totalQuestions) {
                    navController.navigate("result/${correctAnswered}/${totalQuestions}")
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
            db.collection("users")
                    .document(it)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            experienceLevel1 = document.getLong("experienceLevel")?.toInt() ?: 0
                        } else {
                            Log.d(ContentValues.TAG, "No such user document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
                    }
        }
    }
    return Triple(function, experienceLevel1, userId1)
}


@Composable
fun ChoiceBox(
    text: String,
    selected: Boolean,
    onOptionSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .selectable(
                    selected,
                    onClick = onOptionSelected,
                    role = Role.RadioButton
                )
    ) {
        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))

            Text(text, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            Box(Modifier.padding(8.dp)) {
                RadioButton(selected, onClick = null)
            }
        }
    }
}


