package com.gultekinahmetabdullah.softedu.admin

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
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
        TopAppBar(
            colors = TopAppBarColors(
                MaterialTheme.colorScheme.onPrimary,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.outline,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            ),
            title = { Text("Admin Home") },
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
        Button(
            modifier = Modifier
                .padding(paddingValues)
                .border(
                    5.dp,
                    MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.large
                ),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.outline),
            elevation = ButtonDefaults.buttonElevation(5.dp, 5.dp, 5.dp, 5.dp, 5.dp),
            onClick = { navController.navigate(Screen.BottomScreen.FeedbackPanel.bRoute) }) {
            Text(
                "Check Feedbacks",
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                fontSize = TextUnit(40F, TextUnitType.Sp),
                textAlign = TextAlign.Center,
            )
        }

        Button(
            modifier = Modifier
                .padding(paddingValues)
                .border(
                    5.dp,
                    MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.large
                ),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.outline),
            elevation = ButtonDefaults.buttonElevation(5.dp, 5.dp, 5.dp, 5.dp, 5.dp),
            onClick = { navController.navigate(Screen.BottomScreen.AddQuestionPanel.bRoute) }) {
            Text(
                "Add Question",
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                fontSize = TextUnit(40F, TextUnitType.Sp),
                textAlign = TextAlign.Center
            )
        }
    }
}
