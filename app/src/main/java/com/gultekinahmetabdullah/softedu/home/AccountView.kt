package com.gultekinahmetabdullah.softedu.home.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.CustomBottomAppBar
import com.gultekinahmetabdullah.softedu.CustomTopAppBar
import com.gultekinahmetabdullah.softedu.leaderboard.User
import com.gultekinahmetabdullah.softedu.theme.SoftEduTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountView(navController: NavController) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val db = Firebase.firestore
    val auth = Firebase.auth
    val userId = auth.currentUser?.uid
    val context = LocalContext.current
    val email = auth.currentUser?.email ?: ""
    var user by remember { mutableStateOf(User("", "", "", 0)) }
    var users by remember { mutableStateOf(listOf<User>()) }
    var rank by remember { mutableIntStateOf(0) }
    var totalQuestions by remember { mutableStateOf(0) }
    var solvedQuestions by remember { mutableStateOf(0) }
    var progress by remember { mutableFloatStateOf(0f) }

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
                            user = User(document.id, name, surname, score)
                            rank = users.indexOfFirst { it.name == user.name && it.surname == user.surname } + 1
                            solvedQuestions = document.get("correctQuestions")?.let { (it as List<*>).size } ?: 0
                        }
                    }

            db.collection("questions")
                    .get()
                    .addOnSuccessListener { result ->
                        totalQuestions = result.size()
                        progress = if (totalQuestions > 0) solvedQuestions.toFloat() / totalQuestions.toFloat() else 0f
                    }
        }
    }




    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Account",
                onMoreClick = { showBottomSheet = true },
                onAccountClick = { /* Handle account button click */ },
                navController = navController
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                onHomeClick = { navController.navigate("home") },
                onLeaderboardClick = { navController.navigate("leaderboard") },
                onAccountClick = { navController.navigate("accountView") }
            )
        }
    ) {
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
                        Text("agaoglum@gmail.com")
                    }
                }
                IconButton(onClick = {
                    //TODO : Logout

                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                         contentDescription = null)
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
                Text(text = user.name)
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
                Text(text = user.surname)
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
                Text(text = email)
            }
            HorizontalDivider()
        }
    }
}