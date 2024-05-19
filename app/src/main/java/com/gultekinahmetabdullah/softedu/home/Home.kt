package com.gultekinahmetabdullah.softedu.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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

    Column {
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

        LazyColumn(modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)) {
            item { HomeItem("New Update", drawable = R.drawable.ic_temporary) }
            item { HomeItem("New Features", drawable = R.drawable.ic_temporary) }
            item { HomeItem("Beta Release", drawable = R.drawable.ic_temporary) }
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
            .size(200.dp),
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
            Image(painter = painterResource(id = drawable), contentDescription = cat)
        }
    }
}