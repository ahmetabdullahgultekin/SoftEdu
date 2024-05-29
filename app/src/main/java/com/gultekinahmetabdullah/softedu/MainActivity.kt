package com.gultekinahmetabdullah.softedu

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirebaseMessagingService.Companion.getMessageToken
import com.gultekinahmetabdullah.softedu.theme.SoftEduTheme
import com.gultekinahmetabdullah.softedu.util.Screen

class
MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        getMessageToken(this)

        setContent {
            val auth: FirebaseAuth = Firebase.auth
            val context = LocalContext.current
            val sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val isAdmin = sharedPreferences.getBoolean("isAdmin", false)
            val startDestination = if (auth.currentUser != null && ! isAdmin)
                Screen.BottomScreen.Home.bRoute
            else if (auth.currentUser == null && isAdmin)
                Screen.BottomScreen.AdminHome.route
            else
                Screen.LoginScreen.Login.lRoute
            val isSystemInDarkTheme = isSystemInDarkTheme()
            val isDarkTheme = remember { mutableStateOf(isSystemInDarkTheme) }

            SoftEduTheme(isDarkTheme.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    MainView(startDestination, auth, isDarkTheme)
                }
            }
        }
    }
}