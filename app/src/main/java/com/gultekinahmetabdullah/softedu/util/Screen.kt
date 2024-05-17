package com.gultekinahmetabdullah.softedu.util

import androidx.annotation.DrawableRes
import com.gultekinahmetabdullah.softedu.R

sealed class Screen(val title: String, val route: String) {

    sealed class LoginScreen(
        private val lTitle: String, val lRoute: String,
        @DrawableRes val icon: Int) : Screen(lTitle, lRoute){
            data object Login : LoginScreen(
                "Login", "login", R.drawable.baseline_login_24
            )
    }

    sealed class MainScreen(
        private val mTitle: String, val mRoute: String,
        @DrawableRes val icon: Int) : Screen(mTitle, mRoute){
        data object Main : MainScreen(
            "Main", "main", R.drawable.baseline_login_24
        )
    }

    sealed class BottomScreen(val bTitle: String, val bRoute: String,
                              @DrawableRes val icon: Int): Screen(bTitle,bRoute){
        data object Home : BottomScreen(
            "Home", "home", R.drawable.baseline_home_24
        )

        data object Learn: BottomScreen(
            "Learn", "play", R.drawable.baseline_school_24
        )

        data object Leaderboard : BottomScreen(
            "Leaderboard", "leaderboard", R.drawable.baseline_leaderboard_24
        )
    }

    sealed class  AccountDrawerScreen(val dTitle: String, val dRoute: String,
        @DrawableRes val icon: Int) : Screen(dTitle, dRoute){
            data object Account: AccountDrawerScreen(
                "Account",
                "account",
                R.drawable.ic_account
            )
            data object Subscription: AccountDrawerScreen(
                "Subscription",
                "subscribe",
                R.drawable.ic_temporary
            )

            data object AddAccount: AccountDrawerScreen(
                "Add Account",
                "add_account",
                R.drawable.baseline_person_add_alt_1_24
            )
        }

    sealed class SettingsDrawerScreen(
        val dTitle: String, val dRoute: String,
        @DrawableRes val icon: Int) : Screen(dTitle, dRoute){
        data object Account: AccountDrawerScreen(
            "Account",
            "account",
            R.drawable.ic_account
        )
        data object Subscription: AccountDrawerScreen(
            "Subscription",
            "subscribe",
            R.drawable.ic_temporary
        )

        data object AddAccount: AccountDrawerScreen(
            "Add Account",
            "add_account",
            R.drawable.baseline_person_add_alt_1_24
        )
    }
}

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Learn,
    Screen.BottomScreen.Leaderboard
)

val screensInDrawer = listOf(
    Screen.AccountDrawerScreen.Account,
    Screen.AccountDrawerScreen.Subscription,
    Screen.AccountDrawerScreen.AddAccount
)