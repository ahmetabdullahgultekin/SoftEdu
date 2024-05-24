package com.gultekinahmetabdullah.softedu.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.gultekinahmetabdullah.softedu.util.Screen

@Composable
fun AdminHome(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(Screen.BottomScreen.FeedbackPanel.bRoute) }) {
            Text("Go to Feedback Panel")
        }

        Button(onClick = { navController.navigate(Screen.BottomScreen.AddQuestionPanel.bRoute) }) {
            Text("Go to Add Question Panel")
        }
    }

}