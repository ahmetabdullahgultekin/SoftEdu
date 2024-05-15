package com.gultekinahmetabdullah.softedu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import com.gultekinahmetabdullah.softedu.theme.SoftEduTheme

class
MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            SoftEduTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(PaddingValues())
                }
            }
        }
    }
}

/*
@Preview(name = "Welcome light theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Welcome dark theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun WelcomeScreenPreview() {
    SoftEduTheme {
        MainView()
    }
}
*/