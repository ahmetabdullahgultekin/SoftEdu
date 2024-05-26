package com.gultekinahmetabdullah.softedu.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFFFF9800)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFEEDCFF)
val md_theme_light_onPrimaryContainer = Color(0xFFFF5722)
val md_theme_light_secondary = Color(0xFF655A6F)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFEBDDF7)
val md_theme_light_onSecondaryContainer = Color(0xFF20182A)
val md_theme_light_tertiary = Color(0xFF0091EA)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFD9DE)
val md_theme_light_onTertiaryContainer = Color(0xFF321018)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF1D1B1E)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF1D1B1E)
val md_theme_light_surfaceVariant = Color(0xFFE8E0EB)
val md_theme_light_onSurfaceVariant = Color(0xFF4A454E)
val md_theme_light_outline = Color(0xFF7B757F)
val md_theme_light_inverseOnSurface = Color(0xFFF5EFF4)
val md_theme_light_inverseSurface = Color(0xFF1D1B1E)
val md_theme_light_inversePrimary = Color(0xFFD9B9FF)
val md_theme_light_surfaceTint = Color(0xFF00C853)


//Bars Surface
val md_theme_dark_primary = Color(0xFF000000)

//TopBarItems
val md_theme_dark_onPrimary = Color(0xFFABCDEF)

//Bottom Bar Selected Box
val md_theme_dark_primaryContainer = Color(0xFFABCDEF)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFFFFF)

//DrawerSurface
val md_theme_dark_secondary = Color(0xFF000000)

//Drawer Items
val md_theme_dark_onSecondary = Color(0xFFFFFFFF)

//Current Screen In Drawer
val md_theme_dark_secondaryContainer = Color(0xFF000000)

//BottomSheetSurface
val md_theme_dark_onSecondaryContainer = Color(0xFF00BFA5)
val md_theme_dark_tertiary = Color(0xFFFFD600)
val md_theme_dark_onTertiary = Color(0xFFFFFFFF)

//GridBorderColourOfHome
val md_theme_dark_tertiaryContainer = Color(0xFFFFFFFF)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFFFFF)
val md_theme_dark_error = Color(0xFFFFD600)
val md_theme_dark_errorContainer = Color(0xFFD50000)
val md_theme_dark_onError = Color(0xFFD50000)
val md_theme_dark_onErrorContainer = Color(0xFFD50000)

//Background
val md_theme_dark_background = Color(0xFF070707)

//Account Screen Text
val md_theme_dark_onBackground = Color(0xFFABCDEF)

//Card Surface
val md_theme_dark_surface = Color(0xFFABCDEF)

//Card Content
val md_theme_dark_onSurface = Color(0xFF000000)
val md_theme_dark_surfaceVariant = Color(0xFF000000)

//NavigationIconColour
val md_theme_dark_onSurfaceVariant = Color(0xFFFFFFFF)

//Horizontal Divider
val md_theme_dark_outline = Color(0xFFFFFFFF)

//Leaderboard Current User
val md_theme_dark_inverseOnSurface = Color(0xFFFF6D00)
val md_theme_dark_inverseSurface = Color(0xFFC51162)

//Bottom Bar Selected Icon
val md_theme_dark_inversePrimary = Color(0xFFFFFFFF)
val md_theme_dark_surfaceTint = Color(0xFFD50000)


@Composable
fun getCustomOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    unfocusedLabelColor = MaterialTheme.colorScheme.outline,
    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
    unfocusedTextColor = MaterialTheme.colorScheme.outline,
    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
    focusedContainerColor = MaterialTheme.colorScheme.primary,
    focusedTextColor = MaterialTheme.colorScheme.onPrimary,

    cursorColor = MaterialTheme.colorScheme.onPrimary,
    selectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.onPrimary,
        backgroundColor = MaterialTheme.colorScheme.onPrimary
    )
)