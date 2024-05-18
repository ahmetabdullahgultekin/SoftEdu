package com.gultekinahmetabdullah.softedu.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.CustomBottomAppBar
import com.gultekinahmetabdullah.softedu.CustomTopAppBar
import com.gultekinahmetabdullah.softedu.MainViewModel
import com.gultekinahmetabdullah.softedu.MoreBottomSheet
import com.gultekinahmetabdullah.softedu.R
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_tertiaryContainer
import com.gultekinahmetabdullah.softedu.util.Screen.AccountDrawerScreen.Account.title
import com.gultekinahmetabdullah.softedu.util.screensInDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) { //TODO Add Progress bar
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Home",
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("learn") }) {
                Text("Learn")
            }
            Button(onClick = {
                Firebase.auth.signOut()
                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                navController.navigate("login") // Navigate back to sign-in screen
            }) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun LearnerItem(cat: String, drawable: Int) {
    Card(modifier = Modifier
            .padding(16.dp)
            .size(200.dp),
         border = BorderStroke(2.dp, color = md_theme_dark_tertiaryContainer)) {
        Column(verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = cat)
            Image(painter = painterResource(id = drawable), contentDescription = cat)
        }
    }
}