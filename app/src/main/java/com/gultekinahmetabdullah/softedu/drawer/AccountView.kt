package com.gultekinahmetabdullah.softedu.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.leaderboard.User
import com.gultekinahmetabdullah.softedu.util.Screen

@Composable
fun AccountView(auth: FirebaseAuth, navController: NavController) {
    val db = Firebase.firestore
    val userId = auth.currentUser?.uid
    var user by remember { mutableStateOf(User("", "", "", 0)) }
    var users by remember { mutableStateOf(listOf<User>()) }
    var rank by remember { mutableIntStateOf(0) }
    val email = auth.currentUser?.email.toString()

    LaunchedEffect(key1 = userId) {
        userId?.let { it ->
            db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        users = result.documents.mapNotNull { it.toObject(User::class.java) }
                        users = users.sortedByDescending { it.score }
                    }

            db.collection("users")
                    .document(it)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val name = document.getString("name") ?: ""
                            val surname = document.getString("surname") ?: ""
                            val score = document.getLong("score")?.toInt() ?: 0
                            val experienceLevel = document.getLong("experienceLevel")?.toInt() ?: 1
                            user = User(document.id, name, surname, score)
                            rank = users.indexOfFirst { it.name == user.name && it.surname == user.surname } + 1
                        }
                    }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                Icon(imageVector = Icons.Filled.AccountCircle,
                     contentDescription = "Account",
                     modifier = Modifier.padding(end = 8.dp)
                )
                Column {
                    Text(email)
                }
            }
        }

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Icon(
                painter = painterResource(
                    id = com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary
                ),
                contentDescription = "User Information Here",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Name: " + user.name)
        }
        HorizontalDivider()

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Icon(
                painter = painterResource(
                    id = com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary
                ),
                contentDescription = "User Information Here",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Surname: " + user.surname)
        }
        HorizontalDivider()

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Icon(
                painter = painterResource(
                    id = com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary
                ),
                contentDescription = "User Information Here",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Experience Level: " + user.experienceLevel.toString())
        }
        HorizontalDivider()

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Icon(
                painter = painterResource(
                    id = com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary
                ),
                contentDescription = "User Information Here",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Score: " + user.score.toString())
        }

        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = "Manage", modifier = Modifier
                .align(Alignment.CenterVertically))
            IconButton(onClick = {
                //TODO : Something can be made
                navController.navigate(Screen.SettingsDrawerScreen.Settings.dRoute)
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null)
            }
        }
    }
}
