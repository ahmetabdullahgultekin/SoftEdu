package com.gultekinahmetabdullah.softedu.drawer

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.softedu.theme.DarkColors
import com.gultekinahmetabdullah.softedu.theme.LightColors

@Composable
fun Settings() {
    //TODO add settings
    val settings = listOf("Dark Mode", "Notifications", "Privacy", "About", "Help")
    val systemInDarkTheme = isSystemInDarkTheme()
    val isDarkTheme = remember { mutableStateOf(systemInDarkTheme) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        SwitchButtonOnRow(settings[0])
        CardWithUpdateLink()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardWithUpdateLink() {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ClickableText(text = AnnotatedString(text = "Check for Update"),
                onHover = { },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .fillMaxWidth(),
                style = TextStyle.Default.copy(
                    color = Color.Cyan,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                ),
                onClick = {
                    Toast.makeText(context, "Checking for update", Toast.LENGTH_SHORT).show()
                }
            )

            Text(
                text = "Beta v1.0.0", modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
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
