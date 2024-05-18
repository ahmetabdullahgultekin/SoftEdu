package com.gultekinahmetabdullah.softedu

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.home.Home
import com.gultekinahmetabdullah.softedu.home.theme.AccountView
import com.gultekinahmetabdullah.softedu.home.theme.Subscription
import com.gultekinahmetabdullah.softedu.leaderboard.Leaderboard
import com.gultekinahmetabdullah.softedu.learning.Learn
import com.gultekinahmetabdullah.softedu.settings.SettingsScreen
import com.gultekinahmetabdullah.softedu.signinsignup.LoginScreen
import com.gultekinahmetabdullah.softedu.util.Screen


@Composable
fun Navigation() {
    val auth: FirebaseAuth = Firebase.auth
    val startDestination = if (auth.currentUser != null) "home" else "login"
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") { Home(navController) }
        composable("learn") { Learn(navController) }
        composable("leaderboard") { Leaderboard(navController, auth) }
        composable("accountView") { AccountView(navController) }
        composable("login") { LoginScreen(navController) }
        composable("settings") { SettingsScreen() }
        composable("feedback") { FeedbackScreen(navController) }
        composable("about") { AboutScreen() }
        composable("result/{correctAnswered}/{totalQuestions}") { backStackEntry ->
            val correctAnswered = backStackEntry.arguments?.getString("correctAnswered")?.toIntOrNull() ?: 0
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            ResultScreen(navController, correctAnswered, totalQuestions)
        }

    }
}