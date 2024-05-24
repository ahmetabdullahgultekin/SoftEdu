package com.gultekinahmetabdullah.softedu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.softedu.admin.AddQuestionPanel
import com.gultekinahmetabdullah.softedu.admin.AdminHome
import com.gultekinahmetabdullah.softedu.admin.AdminLoginScreen
import com.gultekinahmetabdullah.softedu.admin.FeedbackPanel
import com.gultekinahmetabdullah.softedu.drawer.AboutScreen
import com.gultekinahmetabdullah.softedu.drawer.AccountView
import com.gultekinahmetabdullah.softedu.drawer.AdjustProfileScreen
import com.gultekinahmetabdullah.softedu.drawer.FeedbackScreen
import com.gultekinahmetabdullah.softedu.drawer.Settings
import com.gultekinahmetabdullah.softedu.drawer.Subscription
import com.gultekinahmetabdullah.softedu.home.Home
import com.gultekinahmetabdullah.softedu.leaderboard.Leaderboard
import com.gultekinahmetabdullah.softedu.learning.Learn
import com.gultekinahmetabdullah.softedu.learning.ResultScreen
import com.gultekinahmetabdullah.softedu.learning.games.Memory
import com.gultekinahmetabdullah.softedu.learning.games.Puzzle
import com.gultekinahmetabdullah.softedu.learning.games.Quiz
import com.gultekinahmetabdullah.softedu.learning.games.Sliders
import com.gultekinahmetabdullah.softedu.signinsignup.LoginScreen
import com.gultekinahmetabdullah.softedu.signinsignup.UserInfoScreen
import com.gultekinahmetabdullah.softedu.util.Screen


@Composable
fun Navigation(
    pd: PaddingValues,
    navController: NavController,
    startDestination: String,
    auth: FirebaseAuth
) {

    NavHost(
        navController = navController as NavHostController,
        startDestination = startDestination, modifier = Modifier.padding(pd)
    ) {

        composable(Screen.LoginScreen.Login.lRoute) {
            LoginScreen(auth = auth, navController)
        }

        composable(Screen.BottomScreen.Home.bRoute) {
            Home()
        }

        composable(Screen.BottomScreen.Learn.bRoute) {
            Learn(navController)
        }

        composable(Screen.BottomScreen.Learnings.Quiz.bRoute + ",{isTestScreen}"+ ",{totalQuestions}") {
            backStackEntry ->
            val isTestScreen = backStackEntry.arguments?.getString("isTestScreen").toBoolean()
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            Quiz(navController, isTestScreen, totalQuestions)
        }

        composable(Screen.BottomScreen.Learnings.Memory.route) {
            Memory()
        }

        composable(Screen.BottomScreen.Learnings.Puzzle.route) {
            Puzzle()
        }

        composable(Screen.BottomScreen.Learnings.Sliders.route) {
            Sliders()
        }

        composable(Screen.BottomScreen.Leaderboard.bRoute) {
            Leaderboard(auth = auth)
        }

        composable(Screen.AccountDrawerScreen.Account.route) {
            AccountView(auth, navController)
        }

        composable(Screen.AccountDrawerScreen.AdjustAccount.route) {
            AdjustProfileScreen()
        }

        composable(Screen.AccountDrawerScreen.Subscription.route) {
            Subscription()
        }

        composable(Screen.SettingsDrawerScreen.Settings.route) {
            Settings()
        }

        composable(Screen.SettingsDrawerScreen.Feedback.route) {
            FeedbackScreen()
        }

        composable(Screen.SettingsDrawerScreen.About.route) {
            AboutScreen()
        }

        composable(Screen.LoginScreen.UserInfo.route) {
            UserInfoScreen(navController)
        }
        composable(Screen.BottomScreen.AdminHome.route) {
            AdminHome(navController)
        }
        composable(Screen.LoginScreen.AdminLogin.route) {
            AdminLoginScreen(navController)
        }
        composable(Screen.BottomScreen.FeedbackPanel.route) {
            FeedbackPanel()
        }
        composable(Screen.BottomScreen.AddQuestionPanel.route) {
            AddQuestionPanel()
        }

        composable(Screen.ResultScreen.Result.rRoute + ",{correctAnswered}" + ",{totalQuestions}") { backStackEntry ->
            val correctAnswered = backStackEntry.arguments?.getString("correctAnswered")?.toIntOrNull() ?: 0
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            ResultScreen(navController, correctAnswered, totalQuestions)
        }
    }
}