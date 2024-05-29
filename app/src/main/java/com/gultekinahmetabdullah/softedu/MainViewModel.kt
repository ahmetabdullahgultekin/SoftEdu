package com.gultekinahmetabdullah.softedu

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.util.Screen

class MainViewModel:ViewModel() {

    private val startDestination = Firebase.auth.currentUser?.let {
        Screen.BottomScreen.Home
    } ?: Screen.LoginScreen.Login

    private val _currentScreen: MutableState<Screen> = mutableStateOf(startDestination)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen){
        _currentScreen.value = screen
    }
}