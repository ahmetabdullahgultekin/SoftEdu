package com.gultekinahmetabdullah.softedu.learning

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gultekinahmetabdullah.softedu.util.screensInLearn

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Learn(navController: NavHostController) {

    val totalQuestions = 10

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(screensInLearn.size) { index ->
            Card(modifier = Modifier
                .size(200.dp, 200.dp)
                .padding(25.dp)
                .animateItemPlacement(),
                onClick = {
                    if (index == 0)
                        navController.navigate(screensInLearn[index].bRoute
                                + ",${false},${totalQuestions}")
                    else
                        navController.navigate(screensInLearn[index].bRoute)}) {

                Image(
                    painter = painterResource(id = screensInLearn[index].icon),
                    contentDescription = screensInLearn[index].bTitle,
                    modifier = Modifier
                        .fillMaxSize()
                )
/*
                Text(
                    text = screensInLearn[index].bTitle,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                )

 */
            }
        }
    }
}