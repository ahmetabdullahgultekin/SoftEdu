package com.gultekinahmetabdullah.softedu.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.R
import com.gultekinahmetabdullah.softedu.database.FirestoreConstants

@Composable
fun Home() { //TODO add announcement
    val listOfAnnouncements = remember { mutableStateOf(listOf<String>()) }

    GetAnnouncementsFB(listOfAnnouncements)
}

@Composable
private fun GetAnnouncementsFB(listOfAnnouncements: MutableState<List<String>>) {

    val db = Firebase.firestore
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {

        MottoCard()

        AnnouncementCard()

        HorizontalDivider()

        if (isLoading) {
            CircularProgressIndicator(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .size(50.dp)
            )
        }

        LaunchedEffect(key1 = Unit) {
            try {
                db.collection(FirestoreConstants.COLLECTION_FEEDBACKS)
                    .orderBy(FirestoreConstants.FIELD_TIMESTAMP, Query.Direction.DESCENDING)
                    .limit(FirestoreConstants.LIMIT_ANNOUNCEMENTS)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            listOfAnnouncements.value += document.data[FirestoreConstants.FIELD_FEEDBACK].toString()
                        }
                        println("feedback -> $listOfAnnouncements")

                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }

        if (!isLoading) {
            AnnouncementsColumn(listOfAnnouncements)
        }
    }
}

@Composable
fun MottoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(100.dp)
    ) {
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
fun AnnouncementsColumn(listOfAnnouncements: MutableState<List<String>>) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(listOfAnnouncements.value.size) { index ->
            HomeItem(listOfAnnouncements.value[index], drawable = R.drawable.ic_temporary)
        }
    }
}

@Composable
fun HomeItem(cat: String, drawable: Int) {
    //TODO add title and image
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = cat, modifier = Modifier.padding(10.dp))
            HorizontalDivider()
//            Image(
//                painter = painterResource(id = drawable),
//                contentDescription = cat, modifier = Modifier.padding(8.dp)
//            )
        }
    }
}