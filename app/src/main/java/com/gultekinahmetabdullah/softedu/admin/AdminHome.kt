package com.gultekinahmetabdullah.softedu.admin

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gultekinahmetabdullah.softedu.R
import com.gultekinahmetabdullah.softedu.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHome(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

    Scaffold(topBar = {
        TopAppBar(title = { Text("Admin Home") },
            actions = {
                IconButton(modifier = Modifier.padding(10.dp), onClick = {
                    // Save admin status in SharedPreferences
                    with(sharedPreferences.edit()) {
                        putBoolean("isAdmin", false)
                        apply()
                    }
                    navController.navigate(Screen.LoginScreen.Login.lRoute)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_logout_24),
                        contentDescription = "Logout"
                    )

                }
            }
        )
    }, content = {
        AdminHomeContent(navController = navController, it)
    })
}

@Composable
fun AdminHomeContent(
    navController: NavController,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(modifier = Modifier.padding(paddingValues),
            onClick = { navController.navigate(Screen.BottomScreen.FeedbackPanel.bRoute) }) {
            Text("Feedback Panel")
        }

        Button(modifier = Modifier.padding(paddingValues),
            onClick = { navController.navigate(Screen.BottomScreen.AddQuestionPanel.bRoute) }) {
            Text("Add Question Panel")
        }
    }
}
