package com.gultekinahmetabdullah.softedu.drawer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.leaderboard.User
import com.gultekinahmetabdullah.softedu.util.Screen

data class UserRowItem(
    val isIconDrawable: Boolean,
    @DrawableRes val iconPainterID: Int,
    val imageVector: ImageVector?,
    val iconDescription: String,
    val userInfoLabel: String,
    val userInfoValue: String,
    val isRowTitle: Boolean
)

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
                        rank =
                            users.indexOfFirst { it.name == user.name && it.surname == user.surname } + 1
                    }
                }
        }
    }

    val userPInfoRowItems = listOf(
        UserRowItem(
            false, 0, Icons.Filled.Person,
            "Info", "Personal Info", "", true
        ),
        UserRowItem(
            false, 0, Icons.Filled.Email,
            "Mail: ", "", email, false
        ),
        UserRowItem(
            true, com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary,
            null, "", "Name : ", user.name, false
        ),
        UserRowItem(
            true, com.gultekinahmetabdullah.softedu.R.drawable.ic_temporary,
            null, "", "Surname : ",
            user.surname, false
        ),
    )

    val userSInfoRowItems = listOf(
        UserRowItem(
            false, 0, Icons.Filled.Face,
            "Face", "Student Info", "", true
        ),
        UserRowItem(
            false, 0, Icons.Filled.PlayArrow,
            "Face", "Nickname : ",
            "nickname", false
        ),
        UserRowItem(
            false, 0, Icons.Filled.Build,
            "Face", "Experience Level : ",
            user.experienceLevel.toString(),
            false
        ),
        UserRowItem(
            false, 0, Icons.Filled.AddCircle,
            "Face", "Score : ", user.score.toString(), false
        ),
        UserRowItem(
            false, 0, Icons.Filled.Star,
            "Face", "Rank : ", rank.toString(), false
        ),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(userPInfoRowItems.size) { index ->
            AccountScreenRowItem(
                userPInfoRowItems[index].isIconDrawable,
                userPInfoRowItems[index].iconPainterID,
                userPInfoRowItems[index].imageVector,
                userPInfoRowItems[index].iconDescription,
                userPInfoRowItems[index].userInfoLabel,
                userPInfoRowItems[index].userInfoValue,
                userPInfoRowItems[index].isRowTitle
            )
        }

        item { Spacer(modifier = Modifier.padding(50.dp)) }

        items(userSInfoRowItems.size) { index ->
            AccountScreenRowItem(
                userSInfoRowItems[index].isIconDrawable,
                userSInfoRowItems[index].iconPainterID,
                userSInfoRowItems[index].imageVector,
                userSInfoRowItems[index].iconDescription,
                userSInfoRowItems[index].userInfoLabel,
                userSInfoRowItems[index].userInfoValue,
                userSInfoRowItems[index].isRowTitle
            )
        }
    }
}

@Composable
fun AccountScreenRowItem(
    isIconDrawable: Boolean, @DrawableRes iconPainterID: Int,
    imageVector: ImageVector?, iconDescription: String,
    userInfoLabel: String, userInfoValue: String, isRowTitle: Boolean
) {
    Row(modifier = Modifier.padding(top = 16.dp)) {
        if (isIconDrawable) {
            Icon(
                painter = painterResource(
                    id = iconPainterID
                ),
                contentDescription = iconDescription,
                modifier = Modifier.padding(end = 10.dp)
            )
        } else {
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = iconDescription,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        }
        Text(text = userInfoLabel + userInfoValue)
    }
    if (!isRowTitle) {
        HorizontalDivider()
    } else {
        Spacer(modifier = Modifier.padding(10.dp))
    }
}
