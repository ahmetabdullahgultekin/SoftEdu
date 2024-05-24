package com.gultekinahmetabdullah.softedu.admin

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.softedu.database.addQuestionToFirestore

@Composable
fun AddQuestionPanel() {
    val context = LocalContext.current
    var difficultyLevel by remember { mutableStateOf("") }
    var questionText by remember { mutableStateOf("") }
    var choice1 by remember { mutableStateOf("") }
    var choice2 by remember { mutableStateOf("") }
    var choice3 by remember { mutableStateOf("") }
    var choice4 by remember { mutableStateOf("") }
    var correctChoice by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Add New Question",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = questionText,
            onValueChange = { questionText = it },
            label = { Text("Question Text") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = choice1,
            onValueChange = { choice1 = it },
            label = { Text("Choice 1") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = choice2,
            onValueChange = { choice2 = it },
            label = { Text("Choice 2") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = choice3,
            onValueChange = { choice3 = it },
            label = { Text("Choice 3") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = choice4,
            onValueChange = { choice4 = it },
            label = { Text("Choice 4") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = correctChoice,
            onValueChange = { correctChoice = it },
            label = { Text("Correct Choice Index") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = difficultyLevel,
            onValueChange = { difficultyLevel = it },
            label = { Text("Difficulty Level") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (addQuestionToFirestore(
                        difficultyLevel.toIntOrNull() ?: 0,
                        questionText,
                        listOf(choice1, choice2, choice3, choice4),
                        correctChoice.toIntOrNull() ?: 0
                    )) {
                    Toast.makeText(context, "Question added!", Toast.LENGTH_SHORT).show()
                    difficultyLevel = ""
                    questionText = ""
                    choice1 = ""
                    choice2 = ""
                    choice3 = ""
                    choice4 = ""
                    correctChoice = ""
                } else {
                    Toast.makeText(context, "Question could not be added!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Add Question")
        }
    }
}