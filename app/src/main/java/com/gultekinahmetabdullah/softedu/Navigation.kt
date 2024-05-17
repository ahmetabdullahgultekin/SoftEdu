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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.home.Home
import com.gultekinahmetabdullah.softedu.home.theme.AccountView
import com.gultekinahmetabdullah.softedu.home.theme.Subscription
import com.gultekinahmetabdullah.softedu.leaderboard.Leaderboard
import com.gultekinahmetabdullah.softedu.learning.Learn
import com.gultekinahmetabdullah.softedu.util.Screen


@Composable
fun Navigation(pd: PaddingValues, navController: NavController, viewModel: MainViewModel){

    val auth: FirebaseAuth = Firebase.auth
    val startDestination = Screen.BottomScreen.Home.bRoute

    NavHost(navController = navController as NavHostController,
        startDestination = startDestination, modifier = Modifier.padding(pd) ){

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