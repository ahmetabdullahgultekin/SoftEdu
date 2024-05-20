package com.gultekinahmetabdullah.softedu.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_inverseOnSurface
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_onTertiaryContainer
import kotlinx.coroutines.tasks.await

@Composable
fun Leaderboard(auth: FirebaseAuth) {
    val db = Firebase.firestore
    var users by remember { mutableStateOf(listOf<User>()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        LaunchedEffect(key1 = Unit) {
            try {
                val result = db.collection(FirestoreConstants.COLLECTION_USERS)
                        .orderBy(FirestoreConstants.FIELD_SCORE, Query.Direction.DESCENDING)
                        .get()
                        .await()
                users = result.documents.mapNotNull { document ->
                    val user = document.toObject(User::class.java)
                    user?.id = document.id
                    user
                }
            } catch (e: Exception) {
                // Handle any errors that may occur
                e.printStackTrace()
            }
        }

        LazyColumn {
            items(users) { user ->
                LeaderboardItem(user, user.id == auth.currentUser?.uid)
            }
        }
    }
}

@Composable
fun LeaderboardItem(user: User, isCurrentUser: Boolean) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser)
                                md_theme_dark_inverseOnSurface
                            else
                                md_theme_dark_onTertiaryContainer
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.name,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                text = user.score.toString(),
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}