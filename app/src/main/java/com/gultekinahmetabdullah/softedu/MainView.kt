package com.gultekinahmetabdullah.softedu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRowScope
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gultekinahmetabdullah.softedu.drawer.AccountDialog
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_inverseOnSurface
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_onSecondary
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_onSecondaryContainer
import com.gultekinahmetabdullah.softedu.theme.md_theme_dark_onTertiaryContainer
import com.gultekinahmetabdullah.softedu.util.Screen
import com.gultekinahmetabdullah.softedu.util.screensInBottom
import com.gultekinahmetabdullah.softedu.util.screensInLeftDrawer
import com.gultekinahmetabdullah.softedu.util.screensInRightDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {

    //val scaffoldState = rememberState
    val totalQuestions = 5
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val isSheetFullScreen by remember { mutableStateOf(false) }
    val auth: FirebaseAuth = Firebase.auth


    // Allow us to find out on which "View" we current are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
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

    var isUserInSignInScreen by remember {
        mutableStateOf(currentRoute == Screen.LoginScreen.Login.lRoute
                || currentRoute?.contains(Screen.BottomScreen.Learn.bRoute) ?: true)//TODO Correct bars
    }

    LaunchedEffect(navBackStackEntry) {
        //currentScreen = currentRoute.toString()
        //title.value = Screen.BottomScreen.Home.bTitle
        isUserInSignInScreen = currentRoute == Screen.LoginScreen.Login.lRoute
    }

    val topBar: @Composable () -> Unit = {
        if (!isUserInSignInScreen) {
            TopAppBar(colors = topAppBarColors(
                containerColor = md_theme_dark_onSecondaryContainer
            ),
                title = { Text(title.value, color = md_theme_dark_onSecondary) },
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
                            contentDescription = null, tint = md_theme_dark_onSecondary
                        )
                    }
                    //Sign Out button
                    IconButton(
                        onClick = {
                            auth.signOut()
                            // Navigate back to login screen after signing out
                            controller.navigate(Screen.LoginScreen.Login.lRoute)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null, tint = md_theme_dark_onSecondary
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
                            contentDescription = "Menu", tint = md_theme_dark_onSecondary
                        )
                    }
                }
            )
        }
    }

    val bottomBar: @Composable () -> Unit = {
        if (!isUserInSignInScreen) {
            BottomAppBar(
                Modifier.wrapContentSize(), containerColor = md_theme_dark_onTertiaryContainer
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
                        md_theme_dark_onSecondary
                    else
                        md_theme_dark_inverseOnSurface

                    NavigationBarItem(
                        selected = isSelected,//currentRoute == item.bRoute,
                        onClick = {
                            controller.navigate(item.bRoute)
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
                    )
                }
            }
        }
    }

    Scaffold(
        bottomBar = bottomBar,
        topBar = topBar,
        content = {
            //This returns the selected screen
            Navigation(pd = it, navController = controller)
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
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            if (isNavigationClicked) {
                //RightBottomSheet(modifier = modifier)
                LazyColumn(Modifier.padding(16.dp)) {
                    items(screensInRightDrawer) { item ->
                        SettingsDrawerItem(selected = currentRoute == item.dRoute, item = item) {
                            scope.launch {
                                openBottomSheet = false
                                isNavigationClicked = true
                            }
                            controller.navigate(item.dRoute)
                            title.value = item.dTitle
                        }
                        HorizontalDivider()
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
                                controller.navigate(item.dRoute)
                                title.value = item.dTitle
                            }
                        }
                        HorizontalDivider()
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
    val background = if (selected) md_theme_dark_onSecondaryContainer
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
    val background = if (selected) md_theme_dark_onSecondaryContainer
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