package com.gultekinahmetabdullah.softedu.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.softedu.R
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_tertiaryContainer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(){
    val categories = listOf("Hits", "Happy", "Workout", "Running", "TGIF", "Yoga")
    val grouped = listOf<String>("New Release","Favorites","Top  Rated").groupBy { it[0] }
    LazyColumn{
        grouped.forEach{
            stickyHeader {
                Text(text = it.value[0], modifier = Modifier.padding(16.dp))
                LazyRow{
                    items(categories){
                        cat->
                        LearnerItem(cat= cat, drawable = R.drawable.baseline_apps_24)
                    }
                }
            }
        }
    }
}
@Composable
fun LearnerItem(cat: String, drawable:Int){
    Card(modifier = Modifier
        .padding(16.dp)
        .size(200.dp),
        border = BorderStroke(2.dp, color = md_theme_dark_tertiaryContainer)){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = cat)
            Image(painter = painterResource(id = drawable), contentDescription = cat)
        }
    }
}