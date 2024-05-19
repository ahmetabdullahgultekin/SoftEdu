package com.gultekinahmetabdullah.softedu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.home.Home
import com.gultekinahmetabdullah.softedu.drawer.*
import com.gultekinahmetabdullah.softedu.leaderboard.Leaderboard
import com.gultekinahmetabdullah.softedu.learning.games.Quiz
import com.gultekinahmetabdullah.softedu.drawer.AdjustProfileScreen
import com.gultekinahmetabdullah.softedu.learning.Learn
import com.gultekinahmetabdullah.softedu.learning.ResultScreen
import com.gultekinahmetabdullah.softedu.learning.games.Memory
import com.gultekinahmetabdullah.softedu.learning.games.Puzzle
import com.gultekinahmetabdullah.softedu.learning.games.Sliders
import com.gultekinahmetabdullah.softedu.signinsignup.LoginScreen
import com.gultekinahmetabdullah.softedu.signinsignup.UserInfoScreen
import com.gultekinahmetabdullah.softedu.util.Screen


@Composable
fun Navigation(pd: PaddingValues, navController: NavController) {

    val auth: FirebaseAuth = Firebase.auth
    //val startDestination = Screen.BottomScreen.Home.bRoute
    val isUserSignedIn by remember {
        mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
    }

    val startDestination = if (isUserSignedIn)
        Screen.BottomScreen.Home.bRoute
    else
        Screen.LoginScreen.Login.lRoute


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
            FeedbackScreen(navController)
        }

        composable(Screen.SettingsDrawerScreen.About.route) {
            AboutScreen()
        }

        composable(Screen.LoginScreen.UserInfo.route) {
            UserInfoScreen(navController)
        }

        composable(Screen.ResultScreen.Result.rRoute + ",{correctAnswered}" + ",{totalQuestions}") { backStackEntry ->
            val correctAnswered = backStackEntry.arguments?.getString("correctAnswered")?.toIntOrNull() ?: 0
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            ResultScreen(navController, correctAnswered, totalQuestions)
        }
    }
}