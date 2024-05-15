package com.gultekinahmetabdullah.softedu.learning


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gultekinahmetabdullah.softedu.theme.SoftEduTheme


@Composable
fun Learn(){}



@Preview(name = "Learn", showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SoftEduTheme {
        Learn()
    }
}