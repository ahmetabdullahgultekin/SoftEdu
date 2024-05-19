package com.gultekinahmetabdullah.softedu.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.softedu.R
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_tertiaryContainer

@Composable
fun Home() {//TODO add announcement

    val listOfAnnouncements = listOf(
        "Nickname System",
        "Friendship System",
        "Optimise Home Screen",
        "Optimise Learn Screen",
        "Learn Screen should show game selection at first",
        "Add new learning mode",
        "Optimise Leaderboard Screen",
        "Add new settings features",
        "Add new details to the Feedback Screen",
        "Log in Screen must be improved",
        "Log in text field show hide password",
        "Add share feature",
        "Add version info"
    )

    Column {
        MottoCard()

        AnnouncementCard()

        AnnouncementsColumn(listOfAnnouncements)
    }
}

@Composable
fun MottoCard() {
    Card( modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .size(100.dp)) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Welcome to SoftEdu",
                modifier = Modifier.padding(8.dp)
            )
            HorizontalDivider()
            Text(
                text = "Learn, Share, Grow",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun AnnouncementCard() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Announcements",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}


@Composable
fun AnnouncementsColumn(listOfAnnouncements: List<String>) {

    LazyColumn(modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)) {

        items(listOfAnnouncements.size) { index ->
            HomeItem(listOfAnnouncements[index], drawable = R.drawable.ic_temporary)
        }
    }
}

@Composable
fun HomeItem(cat: String, drawable: Int) {
    //TODO add title and image
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        border = BorderStroke(2.dp, color = md_theme_dark_tertiaryContainer),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = cat, modifier = Modifier.padding(8.dp))
            HorizontalDivider()
            Image(painter = painterResource(id = drawable),
                contentDescription = cat, modifier = Modifier.padding(8.dp))
        }
    }
}