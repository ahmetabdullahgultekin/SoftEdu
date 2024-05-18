package com.gultekinahmetabdullah.softedu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_onSecondaryContainer
import com.gultekinahmetabdullah.softedu.util.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    onMoreClick: () -> Unit,
    onAccountClick: () -> Unit,
    navController: NavController
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(
                onClick = {
                    scope.launch {
                        openBottomSheet = true
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        },
        navigationIcon = {
            IconButton(onClick = onAccountClick) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu")
            }
        }
    )
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = modalSheetState,
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp
            )
        ) {
            MoreBottomSheet(modifier = Modifier.fillMaxWidth(), navController)
        }
    }
}

@Composable
fun CustomBottomAppBar(
    onHomeClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onAccountClick: () -> Unit
) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onHomeClick) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = onLeaderboardClick) {
                Icon(Icons.Default.Search, contentDescription = "Leaderboard")  // TODO add leaderbaord icon
            }
            IconButton(onClick = onAccountClick) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Account")
            }
        }
    }
}

@Composable
fun MoreBottomSheet(modifier: Modifier, navController: NavController) {
    Box(
        Modifier
                .fillMaxWidth()
                .wrapContentHeight()
    ) {
        Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("settings")
                    }) {
                Icon(modifier = Modifier
                        .padding(end = 8.dp),
                     painter = painterResource(id = R.drawable.baseline_settings_24),
                     contentDescription = "Settings"
                )
                Text(
                    text = "Settings",
                    fontSize = 20.sp
                )
            }
            Row(modifier = modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("feedback")
                    }) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_feed_24),
                    contentDescription = "Send Feedback"
                )
                Text(
                    text = "Send Feedback",
                    fontSize = 20.sp,
                )
            }
            Row(modifier = modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("about")
                    }) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_about),
                    contentDescription = "About"
                )
                Text(
                    text = "About",
                    fontSize = 20.sp
                )
            }
        }
    }
}