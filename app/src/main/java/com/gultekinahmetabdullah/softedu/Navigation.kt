package com.gultekinahmetabdullah.softedu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.home.Home
import com.gultekinahmetabdullah.softedu.home.theme.AccountView
import com.gultekinahmetabdullah.softedu.home.theme.MainView
import com.gultekinahmetabdullah.softedu.home.theme.Subscription
import com.gultekinahmetabdullah.softedu.leaderboard.Leaderboard
import com.gultekinahmetabdullah.softedu.learning.Learn
import com.gultekinahmetabdullah.softedu.signinsignup.LoginScreen
import com.gultekinahmetabdullah.softedu.util.Screen


@Composable
fun Navigation(pd: PaddingValues){

    val auth: FirebaseAuth = Firebase.auth
    val startDestination =
        if (auth.currentUser != null)
            //Screen.BottomScreen.Home.bRoute
            Screen.BottomScreen.Learn.bRoute
            //Screen.MainScreen.Main.mRoute//TODO Starter Screen not working for MainView but Home
        else
            Screen.LoginScreen.Login.lRoute

    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(pd) ){

        composable(Screen.LoginScreen.Login.lRoute){
            LoginScreen(navController = navController)
        }

        composable(Screen.MainScreen.Main.mRoute){
            MainView(controller = navController)
        }

        composable(Screen.BottomScreen.Home.bRoute){
            Home()
        }

        composable(Screen.BottomScreen.Learn.bRoute){
            Learn()
        }

        composable(Screen.BottomScreen.Leaderboard.bRoute){
            Leaderboard(auth = auth)
        }

        composable(Screen.AccountDrawerScreen.Account.route){
            AccountView()
        }

        composable(Screen.AccountDrawerScreen.Subscription.route){
            Subscription()
        }
    }
}