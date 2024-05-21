package com.gultekinahmetabdullah.softedu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
            //val startDestination = Screen.BottomScreen.Home.bRoute
            val isUserSignedIn by remember {
                mutableStateOf(auth.currentUser != null)
            }

            val startDestination = if (isUserSignedIn)
                Screen.BottomScreen.Home.bRoute
            else
                Screen.LoginScreen.Login.lRoute

            SoftEduTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    MainView(startDestination, auth)
                }
            }
        }
    }
}