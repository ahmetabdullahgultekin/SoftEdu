package com.gultekinahmetabdullah.softedu

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.gultekinahmetabdullah.softedu.drawer.AccountDialog
import com.gultekinahmetabdullah.softedu.util.Screen
import com.gultekinahmetabdullah.softedu.util.screensInBottom
import com.gultekinahmetabdullah.softedu.util.screensInLeftDrawer
import com.gultekinahmetabdullah.softedu.util.screensInRightDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(startDestination: String, auth: FirebaseAuth) {

    //val scaffoldState = rememberState
    val totalQuestions = 5
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val isSheetFullScreen by remember { mutableStateOf(false) }
    //val auth: FirebaseAuth = Firebase.auth


    // Allow us to find out on which "View" we current are
    val navController: NavController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        mutableStateOf(currentScreen.title) //.title
    }

    var isNavigationClicked by rememberSaveable { mutableStateOf(false) }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp

    val routes = listOf(
        Screen.LoginScreen.Login.lRoute,
        Screen.BottomScreen.Learnings.Quiz.bRoute + ",{isTestScreen},{totalQuestions}",
        Screen.BottomScreen.Learnings.Memory.bRoute,
        Screen.BottomScreen.Learnings.Puzzle.bRoute,
        Screen.BottomScreen.Learnings.Sliders.bRoute,
        Screen.LoginScreen.UserInfo.lRoute,
        Screen.ResultScreen.Result.rRoute + ",{correctAnswered}" + ",{totalQuestions}"
    )

    var isUserInSignInScreen by remember {
        mutableStateOf(currentRoute in routes)
    }


    DisposableEffect(navBackStackEntry) {
        isUserInSignInScreen = currentRoute in routes
        onDispose { }
    }

    LaunchedEffect(currentRoute) {
        title.value = when (currentRoute) {
            Screen.BottomScreen.Home.bRoute -> Screen.BottomScreen.Home.bTitle
            Screen.BottomScreen.Learn.bRoute -> Screen.BottomScreen.Learn.bTitle
            Screen.BottomScreen.Leaderboard.bRoute -> Screen.BottomScreen.Leaderboard.bTitle
            else -> title.value
        }
    }


    val topBar: @Composable () -> Unit = {
        if (!isUserInSignInScreen) {
            TopAppBar(
                colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = { Text(title.value, color = MaterialTheme.colorScheme.onPrimary) },
                actions = {
                    //More button opens Bottom sheet
                    IconButton(
                        onClick = {
                            scope.launch {
                                //modalSheetState.expand()
                                openBottomSheet = true
                                isNavigationClicked = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Sign Out button
                    IconButton(
                        onClick = {
                            auth.signOut()
                            isUserInSignInScreen = true
                            while (navController.currentBackStack.value.isNotEmpty()) {
                                navController.popBackStack()
                            }
                            // Navigate back to login screen after signing out
                            navController.navigate(Screen.LoginScreen.Login.lRoute)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                //Account sheet button
                navigationIcon = {
                    IconButton(onClick = {
                        // Open the drawer
                        scope.launch {
                            isNavigationClicked = false
                            openBottomSheet = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    }

    val bottomBar: @Composable () -> Unit = {
        if (!isUserInSignInScreen) {
            BottomAppBar(
                Modifier.wrapContentSize(),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                screensInBottom.forEach { item ->

                    val isSelected = (currentRoute == item.bRoute)
                    //|| (currentRoute?.contains(item.bRoute) == true)
                    Log.d(
                        "Navigation",
                        "Item: ${item.bTitle}, Current Route: $currentRoute," +
                                " Is Selected: $isSelected"
                    )
                    val tint = if (isSelected)
                        MaterialTheme.colorScheme.inversePrimary
                    else
                        MaterialTheme.colorScheme.onPrimary

                    NavigationBarItem(
                        modifier = Modifier.weight(1f),
                        //.background(MaterialTheme.colorScheme.primaryContainer),
                        selected = isSelected, //currentRoute == item.bRoute,
                        onClick = {
                            navController.navigate(item.bRoute)
                            title.value = item.bTitle
                        },
                        icon = {
                            Icon(
                                tint = tint,
                                contentDescription = item.bTitle,
                                painter = painterResource(id = item.icon)
                            )
                        },
                        label = { Text(text = item.bTitle, color = tint) },
                        colors = NavigationBarItemDefaults.colors(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.onPrimary,
                            MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                }
            }
        }
    }

    if (isUserInSignInScreen || auth.currentUser == null) {
        // Display the LoginScreen without the Scaffold
        Navigation(pd = PaddingValues(), navController = navController, startDestination, auth)
    } else {
        Scaffold(
            bottomBar = bottomBar,
            topBar = topBar,
            content = {
                //This returns the selected screen
                Navigation(pd = it, navController = navController, startDestination, auth)
                AccountDialog(dialogOpen = dialogOpen)
            }
        )
    }

    if (openBottomSheet) {
        /*
        ModalNavigationDrawer(drawerContent = { /*TODO*/ }) {

        }
        */
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false; isNavigationClicked = false },
            sheetState = modalSheetState,
            shape = RoundedCornerShape(
                topStart = roundedCornerRadius,
                topEnd = roundedCornerRadius
            ),
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            if (isNavigationClicked) {
                //RightBottomSheet(modifier = modifier)

                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outline)
                LazyColumn(Modifier.padding(16.dp)) {
                    items(screensInRightDrawer) { item ->
                        SettingsDrawerItem(selected = currentRoute == item.dRoute, item = item) {
                            scope.launch {
                                openBottomSheet = false
                                isNavigationClicked = true
                            }
                            navController.navigate(item.dRoute)
                            title.value = item.dTitle
                        }
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                LazyColumn(Modifier.padding(16.dp)) {
                    items(screensInLeftDrawer) { item ->
                        AccountDrawerItem(selected = currentRoute == item.dRoute, item = item) {
                            scope.launch {
                                openBottomSheet = false
                                isNavigationClicked = false
                            }
                            if (item.dRoute == "add_account") {
                                dialogOpen.value = true
                            } else {
                                navController.navigate(item.dRoute)
                                title.value = item.dTitle
                            }
                        }
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountDrawerItem(
    selected: Boolean,
    item: Screen.AccountDrawerScreen,
    onDrawerItemClicked: () -> Unit
) {
    val background = if (selected) MaterialTheme.colorScheme.secondaryContainer
    else Color.Transparent
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background, RoundedCornerShape(15.dp))
            .clickable {
                onDrawerItemClicked()
            }) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
        )
        Text(
            text = item.dTitle,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
        )
    }
}

@Composable
fun SettingsDrawerItem(
    selected: Boolean,
    item: Screen.SettingsDrawerScreen,
    onDrawerItemClicked: () -> Unit
) {
    val background = if (selected)
        MaterialTheme.colorScheme.secondaryContainer
    else Color.Transparent
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background, RoundedCornerShape(15.dp))
            .clickable {
                onDrawerItemClicked()
            }) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
        )
        Text(
            text = item.dTitle,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
        )
    }
}

@Composable
fun RightBottomSheet(modifier: Modifier) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = modifier
                .padding(16.dp)
                .clickable {
                    //controller.navigate(Screen.SettingsDrawerScreen.Settings.dRoute)
                }) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.baseline_settings_24),
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