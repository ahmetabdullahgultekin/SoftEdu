package com.gultekinahmetabdullah.softedu

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gultekinahmetabdullah.softedu.util.Screen

class MainViewModel:ViewModel() {

    private val startDestination = Screen.BottomScreen.Home

    private val _currentScreen: MutableState<Screen> = mutableStateOf(startDestination)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen){
        _currentScreen.value = screen
    }
}