package com.gultekinahmetabdullah.softedu.learning.games


import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.updateAnsweredQuestions
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_error
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_errorContainer
import com.gultekinahmetabdullah.softedu.util.Screen
import com.gultekinahmetabdullah.softedu.util.fetchQuestion


@Composable
fun Quiz(navController: NavController, isTestScreen: Boolean, totalQuestions: Int) {
    val db = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    var correctAnswered by rememberSaveable { mutableIntStateOf(0) }

    var questionText by remember { mutableStateOf("") }
    var choices by remember { mutableStateOf(listOf<String>()) }
    var correctChoice by remember { mutableIntStateOf(- 1) }
    var questionCounter by remember { mutableIntStateOf(0) }
    var isAnswerSelected by remember { mutableStateOf(false) }
    var experienceLevel by remember { mutableIntStateOf(0) }
    var userId by remember { mutableStateOf(auth.currentUser?.uid) }
    var questionId by remember { mutableStateOf("") }
    var askedQuestionIds by remember { mutableStateOf(listOf<String>()) }
    var selectedChoice by remember { mutableIntStateOf(- 1) }
    var continueClicked by remember { mutableStateOf(false) }

    var buttonText by remember { mutableStateOf("Submit") }


    // Function to fetch the user's profile information from Firestore
    val triple = getUserInfo(userId, auth, db, experienceLevel)
    val fetchUserProfile = triple.first
    experienceLevel = triple.second
    userId = triple.third

    LaunchedEffect(key1 = Unit) {
        fetchUserProfile()
        fetchQuestion(userId, questionCounter, totalQuestions, askedQuestionIds, isTestScreen) { newQuestionId, newQuestionText, newChoices, newCorrectChoice ->
            questionId = newQuestionId
            questionText = newQuestionText
            choices = newChoices
            correctChoice = newCorrectChoice
            questionCounter ++
            isAnswerSelected = false
            askedQuestionIds += newQuestionId
        }
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
            modifier = Modifier.fillMaxWidth(1f)// Fill 100% of the width
                .padding(8.dp)
                .height(20.dp),
            color = md_theme_dark_error,
            trackColor = md_theme_dark_errorContainer,
        )

        Text(text = "QuestionText -> $questionText \n QuestionId -> $questionId")

        Spacer(modifier = Modifier.height(16.dp))

        choices.forEachIndexed { index, choice ->
            ChoiceBox(
                text = choice,
                selected = index == selectedChoice,
                correct = index == correctChoice,
                answered = continueClicked,
                onOptionSelected = {
                    selectedChoice = index
                    isAnswerSelected = true
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(enabled = selectedChoice != - 1,  // Disable the button if no choice is selected
               onClick = {
                   if (continueClicked) {
                       fetchQuestion(userId, questionCounter, totalQuestions, askedQuestionIds, false) { newQuestionId, newQuestionText, newChoices, newCorrectChoice ->
                           questionId = newQuestionId
                           questionText = newQuestionText
                           choices = newChoices
                           correctChoice = newCorrectChoice
                           questionCounter ++
                           isAnswerSelected = false
                           askedQuestionIds += newQuestionId
                           continueClicked = false
                           selectedChoice = - 1
                           buttonText = "Submit"
                       }

                       if (questionCounter >= totalQuestions) {
                           if (isTestScreen) {
                               val intervalSize = totalQuestions / 5.0
                               val newExperienceLevel = when (correctAnswered) {
                                   totalQuestions -> 5
                                   else -> (correctAnswered / intervalSize).toInt() + 1
                               }
                               auth.currentUser?.uid?.let { updateExperienceLevel(it, newExperienceLevel) }
                           }
                           navController.navigate(Screen.ResultScreen.Result.rRoute
                                   + ",${correctAnswered}" + ",${totalQuestions}")
                       }
                   } else {
                       if (selectedChoice == correctChoice) {
                           userId?.let { updateAnsweredQuestions(it, questionId, true) }
                           correctAnswered ++
                       } else {
                           userId?.let { updateAnsweredQuestions(it, questionId, false) }
                       }
                       isAnswerSelected = true
                       continueClicked = true
                       buttonText = "Next"
                   }
               }) {
            Text(buttonText)
        }
    }
}

@Composable
fun ChoiceBox(
    text: String,
    selected: Boolean,
    correct: Boolean,
    answered: Boolean,
    onOptionSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = when {
        answered && correct -> Color.Green
        answered && selected -> Color.Red
        selected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        answered && correct -> Color.Green
        answered && selected -> Color.Red
        selected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = color,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),

        modifier = modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = ! answered,
                    onClick = onOptionSelected
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


fun updateExperienceLevel(userId: String, experienceLevel: Int) {
    val db = Firebase.firestore
    db.collection("users").document(userId)
            .update("experienceLevel", experienceLevel)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User experience level updated")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating user experience level", e)
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
                            experienceLevel1 = document.getLong("experienceLevel")?.toInt() ?: 1
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


