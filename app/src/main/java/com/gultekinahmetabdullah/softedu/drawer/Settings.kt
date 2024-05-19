package com.gultekinahmetabdullah.softedu.drawer

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.softedu.MainActivity
import com.gultekinahmetabdullah.softedu.theme.DarkColors
import com.gultekinahmetabdullah.softedu.theme.LightColors
import com.gultekinahmetabdullah.softedu.theme.SoftEduTheme
import com.gultekinahmetabdullah.softedu.theme.Typography

@Composable
fun Settings() {
    //TODO add settings
    val settings = listOf("Dark Mode", "Notifications", "Privacy", "About", "Help")
    val systemInDarkTheme = isSystemInDarkTheme()
    val isDarkTheme = remember { mutableStateOf(systemInDarkTheme) }

    Column {
        SwitchButtonOnRow(settings[0])
    }
}

@Composable
fun SwitchButtonOnRow(btnText: String) {

    val systemInDarkTheme = isSystemInDarkTheme()
    val isDarkThemed = remember { mutableStateOf(systemInDarkTheme) }

    val colors = if (! isDarkThemed.value) {
        LightColors
    } else {
        DarkColors
    }


    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = btnText, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )
            Switch(modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically),
                checked = isDarkThemed.value, onCheckedChange = {
                    //TODO change theme
                    isDarkThemed.value = it
                })
        }
    }
}
