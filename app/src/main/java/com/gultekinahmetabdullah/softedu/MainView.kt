package com.gultekinahmetabdullah.softedu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.home.theme.AccountDialog
import com.gultekinahmetabdullah.softedu.signinsignup.LoginScreen
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_onSecondaryContainer
import com.gultekinahmetabdullah.softedu.util.Screen
import com.gultekinahmetabdullah.softedu.util.screensInBottom
import com.gultekinahmetabdullah.softedu.util.screensInDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(){//TODO Add Feedback operation

    //val scaffoldState = rememberState
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val isSheetFullScreen by remember{ mutableStateOf(false) }

    val modifier = if(isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    // Allow us to find out on which "View" we current are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dialogOpen = remember{
        mutableStateOf(false)
    }

    val currentScreen = remember{
        viewModel.currentScreen.value
    }

    val title = remember{
        mutableStateOf(currentScreen.title)
    }

    var isNavigationClicked by rememberSaveable { mutableStateOf(false) }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp
    val auth: FirebaseAuth = Firebase.auth
    var isUserLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    var bottomBar:  @Composable () -> Unit = {
        if(currentScreen is Screen.AccountDrawerScreen
            || currentScreen == Screen.BottomScreen.Home){
            BottomAppBar(
                Modifier.wrapContentSize(),
            ) {
                screensInBottom.forEach{
                    item ->
                    val isSelected = currentRoute == item.bRoute
                    Log.d("Navigation",
                        "Item: ${item.bTitle}, Current Route: $currentRoute," +
                                " Is Selected: $isSelected")
                    val tint = if(isSelected)Color.Cyan else Color.Red
                    NavigationBarItem(selected = currentRoute == item.bRoute,
                        onClick = { controller.navigate(item.bRoute)
                                  title.value = item.bTitle
                                  },
                        icon = {
                            Icon(tint= tint,
                                contentDescription = item.bTitle,
                                painter= painterResource(id = item.icon))
                        },
                            label = { Text(text = item.bTitle, color = tint )},
                    )
                }
            }
        }
    }

    //Here if user does not sign in so this if statement runs and it is login screen
    if (!isUserLoggedIn){// logged in now
        //Returns UI
        LoginScreen(navController = controller, isUserLoggedIn)//
        //TODO We have null variable controller
        //Other navigator uses composable
        //Eurake!
        //IF CURRENTUSER VALUE CHANGES THAN ELSE RUNS
    }

    //Here if user signs in so this else statement runs and it is main screen
    else {
        Scaffold(
        bottomBar = bottomBar,
        topBar = {
            TopAppBar(
                title = { Text(title.value) },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                //modalSheetState.expand()
                                openBottomSheet = true
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                },
                navigationIcon = { IconButton(onClick = {
                    // Open the drawer
                    scope.launch {
                        isNavigationClicked = true
                        openBottomSheet = true
                    }
                }) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu" )
                }}
            )
        },
        content = {
            //This returns the selected screen
            Navigation(navController = controller, viewModel = viewModel, pd = it)
            AccountDialog(dialogOpen = dialogOpen)
        }
    )

        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false; isNavigationClicked = false },
                sheetState = modalSheetState,
                shape = RoundedCornerShape(
                    topStart = roundedCornerRadius,
                    topEnd = roundedCornerRadius
                )

            ) {
                if (!isNavigationClicked) {
                    MoreBottomSheet(modifier = modifier)
                } else {
                    LazyColumn(Modifier.padding(16.dp)) {
                        items(screensInDrawer) { item ->
                            DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                                scope.launch {
                                    openBottomSheet = false
                                    isNavigationClicked = false
                                }
                                if (item.dRoute == "add_account") {
                                    dialogOpen.value = true
                                } else {
                                    controller.navigate(item.dRoute)
                                    title.value = item.dTitle
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItem(
    selected: Boolean,
    item: Screen.AccountDrawerScreen,
    onDrawerItemClicked : () -> Unit
){
    val background = if (selected) md_theme_dark_onSecondaryContainer
                    else Color.Transparent
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background)
            .clickable {
                onDrawerItemClicked()
            }) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(
            text = item.dTitle,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun MoreBottomSheet(modifier: Modifier){
    Box(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
    ){
        Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween){
            Row(modifier = modifier.padding(16.dp)){
                Icon(modifier = Modifier.padding(end = 8.dp),
                   painter =  painterResource(id = R.drawable.baseline_settings_24),
                    contentDescription = "Settings"
                )
                Text(
                    text = "Settings",
                    fontSize = 20.sp
                )
            }
            Row(modifier = modifier.padding(16.dp)) {
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
            Row(modifier = modifier.padding(16.dp)) {
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