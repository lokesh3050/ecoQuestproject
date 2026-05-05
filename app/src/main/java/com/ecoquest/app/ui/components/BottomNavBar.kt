package com.ecoquest.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecoquest.app.R
import com.ecoquest.app.ui.theme.EcoGreen
import com.ecoquest.app.ui.theme.EcoGreenLight
import com.ecoquest.app.ui.theme.MistGray

data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("home", R.string.nav_home, Icons.Filled.Home),
    BottomNavItem("courses", R.string.nav_courses, Icons.Filled.MenuBook),
    BottomNavItem("quizzes", R.string.nav_quizzes, Icons.Filled.Psychology),
    BottomNavItem("leaderboard", R.string.nav_ranks, Icons.Filled.EmojiEvents),
    BottomNavItem("profile", R.string.nav_profile, Icons.Filled.Person),
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            val label = stringResource(id = item.labelResId)
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EcoGreen,
                    selectedTextColor = EcoGreen,
                    unselectedIconColor = MistGray,
                    unselectedTextColor = MistGray,
                    indicatorColor = Color.White
                )
            )
        }
    }
}
