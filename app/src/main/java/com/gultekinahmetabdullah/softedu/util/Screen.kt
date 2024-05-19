package com.gultekinahmetabdullah.softedu.util
import androidx.annotation.DrawableRes
import com.gultekinahmetabdullah.softedu.R

sealed class Screen(val title: String, val route: String) {

    sealed class LoginScreen(
        private val lTitle: String, val lRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(lTitle, lRoute) {
        data object Login : LoginScreen(
            "Login", "login", R.drawable.baseline_login_24
        )

        data object UserInfo : LoginScreen(
            "UserInfo", "userInfo", R.drawable.baseline_login_24
        )
    }

    sealed class ResultScreen(
        private val rTitle: String, val rRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(rTitle, rRoute) {

        data object Result : ResultScreen(
            "Result", "result",//TODO might cause an error
            R.drawable.baseline_login_24
        )
    }

    sealed class BottomScreen(
        val bTitle: String, val bRoute: String,
        @DrawableRes open val icon: Int
    ) : Screen(bTitle, bRoute) {

        data object Home : BottomScreen(
            "Home", "home", R.drawable.baseline_home_24
        )

        data object Learn : BottomScreen(
            "Learn", "learn", R.drawable.baseline_school_24
        )

        sealed class Learnings(
            private val gTitle: String, private val gRoute: String,
            @DrawableRes override val icon: Int
        ) : BottomScreen(gTitle, gRoute, icon) {

            data object Quiz : Learnings(
                "Quiz",
                Learn.bRoute + "//" + "quiz",
                R.drawable.baseline_quiz_24
            )

            data object Puzzle : Learnings(
                "Puzzle", Learn.bRoute + "//" + "puzzle",
                R.drawable.baseline_widgets_24
            )

            data object Memory : Learnings(
                "Memory", Learn.bRoute + "//" + "memory",
                R.drawable.baseline_memory_24
            )

            data object Sliders : Learnings(
                "Sliders", Learn.bRoute + "//" + "sliders",
                R.drawable.sharp_swap_horizontal_circle_24
            )
        }

        data object Leaderboard : BottomScreen(
            "Leaderboard", "leaderboard", R.drawable.baseline_leaderboard_24
        )
    }


    sealed class AccountDrawerScreen(
        val dTitle: String, val dRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(dTitle, dRoute) {
        data object Account : AccountDrawerScreen(
            "Account",
            "account",
            R.drawable.ic_account
        )

        data object AdjustAccount : AccountDrawerScreen(
            "Adjust Account",
            "adjust_account",
            R.drawable.ic_account
        )

        data object Subscription : AccountDrawerScreen(
            "Subscription",
            "subscribe",
            R.drawable.ic_temporary
        )

        data object AddAccount : AccountDrawerScreen(
            "Add Account",
            "add_account",
            R.drawable.baseline_person_add_alt_1_24
        )
    }

    sealed class SettingsDrawerScreen(
        val dTitle: String, val dRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(dTitle, dRoute) {
        data object Settings : SettingsDrawerScreen(
            "Settings",
            "settings",
            R.drawable.baseline_settings_24
        )

        data object Feedback : SettingsDrawerScreen(
            "Feedback",
            "feedback",
            R.drawable.ic_feed_24
        )

        data object About : SettingsDrawerScreen(
            "About",
            "about",
            R.drawable.ic_about
        )
    }
}

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Learn,
    Screen.BottomScreen.Leaderboard
)

val screensInLearn = listOf(
    Screen.BottomScreen.Learnings.Quiz,
    Screen.BottomScreen.Learnings.Puzzle,
    Screen.BottomScreen.Learnings.Memory,
    Screen.BottomScreen.Learnings.Sliders
)

val screensInLeftDrawer = listOf(
    Screen.AccountDrawerScreen.Account,
    Screen.AccountDrawerScreen.Subscription,
    Screen.AccountDrawerScreen.AddAccount
)
val screensInRightDrawer = listOf(
    Screen.SettingsDrawerScreen.Settings,
    Screen.SettingsDrawerScreen.Feedback,
    Screen.SettingsDrawerScreen.About
)