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
import com.gultekinahmetabdullah.softedu.learning.Learn
import com.gultekinahmetabdullah.softedu.drawer.SettingsScreen
import com.gultekinahmetabdullah.softedu.learning.ResultScreen
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

        composable(Screen.BottomScreen.Learn.bRoute + ",{isTestScreen}" + ",{totalQuestions}") {
            backStackEntry ->
//        composable(Screen.BottomScreen.Learn.bRoute + "/{isTestScreen}/{totalQuestions}") { backStackEntry ->
            val isTestScreen = backStackEntry.arguments?.getString("isTestScreen").toBoolean()
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            Learn(navController, isTestScreen, totalQuestions)
        }

        composable(Screen.BottomScreen.Leaderboard.bRoute) {
            Leaderboard(auth = auth)
        }

        composable(Screen.AccountDrawerScreen.Account.route) {
            AccountView(auth)
        }

        composable(Screen.AccountDrawerScreen.Subscription.route) {
            Subscription()
        }

        composable(Screen.SettingsDrawerScreen.Settings.route) {
            SettingsScreen()
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

        composable(Screen.ResultScreen.Result.rRoute + "/{correctAnswered}/{totalQuestions}") { backStackEntry ->
            val correctAnswered = backStackEntry.arguments?.getString("correctAnswered")?.toIntOrNull() ?: 0
            val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toIntOrNull() ?: 0
            ResultScreen(navController, correctAnswered, totalQuestions)
        }
    }
}