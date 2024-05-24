package com.gultekinahmetabdullah.softedu.admin

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.gultekinahmetabdullah.softedu.util.Screen

@Composable
fun AdminHome(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)


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
        Button(onClick = {
            // Save admin status in SharedPreferences
            with(sharedPreferences.edit()) {
                putBoolean("isAdmin", false)
                apply()
            }
            navController.navigate(Screen.LoginScreen.Login.lRoute)
        }) {
            Text("Exit")
        }
    }

}