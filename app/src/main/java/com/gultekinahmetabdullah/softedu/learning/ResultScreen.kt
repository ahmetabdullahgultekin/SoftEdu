package com.gultekinahmetabdullah.softedu.learning

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gultekinahmetabdullah.softedu.util.Screen

@Composable
fun ResultScreen(navController: NavController, correctAnswered: Int, totalQuestions: Int) {
    val context = LocalContext.current
    val ratio = correctAnswered.toFloat() / totalQuestions.toFloat()

    Column(
        modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Your score: $correctAnswered/$totalQuestions", style = MaterialTheme.typography.titleLarge)
        Text(text = "Ratio: $ratio", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            Toast.makeText(context, "Navigating to Learn Screen", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.BottomScreen.Learn.bRoute)
        }) {
            Text("Continue")
        }
    }
}

