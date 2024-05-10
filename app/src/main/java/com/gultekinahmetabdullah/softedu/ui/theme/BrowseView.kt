package com.gultekinahmetabdullah.softedu.ui.theme

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import com.gultekinahmetabdullah.softedu.R

@Composable
fun Learn(){
    val categories = listOf("Hits", "Happy", "Workout", "Running", "TGIF", "Yoga")
    LazyVerticalGrid(GridCells.Fixed(2)) {
        items(categories) { cat ->
            LearnerItem(cat = cat, drawable = R.drawable.baseline_apps_24)
        }
    }
}