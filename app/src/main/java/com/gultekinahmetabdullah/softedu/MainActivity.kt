package com.gultekinahmetabdullah.softedu

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import com.gultekinahmetabdullah.softedu.FirebaseMessagingService.Companion.getMessageToken
import com.gultekinahmetabdullah.softedu.theme.SoftEduTheme

class
MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        getMessageToken(this)

        setContent {
            SoftEduTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    MainView()
                }
            }
        }
    }
}