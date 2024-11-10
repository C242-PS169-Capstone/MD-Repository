package com.herehearteam.herehear.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.R
import com.herehearteam.herehear.navigation.Screen
import com.herehearteam.herehear.ui.theme.ColorPrimary

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home Icon
                IconButton(
                    onClick = { navController.navigate(Screen.Home.route) }
                ) {
                    Icon(
                        imageVector = if (currentRoute == Screen.Home.route)
                            Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                        tint = if (currentRoute == Screen.Home.route)
                            ColorPrimary else Color.Gray
                    )
                }

                // Article Icon
                IconButton(
                    onClick = { navController.navigate(Screen.Article.route) }
                ) {
                    CustomIcon (
                        icon = if (currentRoute == Screen.Article.route)
                            painterResource(R.drawable.ic_article_filled) else painterResource(R.drawable.ic_article),
                        contentDescription = "Article",
                        tint = if (currentRoute == Screen.Article.route)
                            ColorPrimary else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(72.dp))

                // Archive Icon
                IconButton(
                    onClick = { navController.navigate(Screen.Archive.route) }
                ) {
                    CustomIcon(
                        icon = if (currentRoute == Screen.Archive.route)
                            painterResource(R.drawable.ic_archive_filled) else painterResource(R.drawable.ic_archive),
                        contentDescription = "Archive",
                        tint = if (currentRoute == Screen.Archive.route)
                            ColorPrimary else Color.Gray
                    )
                }

                // Profile Icon
                IconButton(
                    onClick = { navController.navigate(Screen.Profile.route) }
                ) {
                    Icon(
                        imageVector = if (currentRoute == Screen.Profile.route)
                            Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Profile",
                        tint = if (currentRoute == Screen.Profile.route)
                            ColorPrimary else Color.Gray
                    )
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { navController.navigate(Screen.Journal.route) },
            modifier = Modifier
                .offset(y = (-40).dp)
                .size(56.dp),
            containerColor = ColorPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Journal",
                tint = Color.White
            )
        }
    }
}

@Composable
fun CustomIcon(
    icon: Any,
    contentDescription: String? = null,
    tint: Color = Color.Unspecified
) {
    when (icon) {
        is ImageVector -> {
            Icon(imageVector = icon, contentDescription = contentDescription, tint = tint)
        }
        is Painter -> {
            Icon(painter = icon, contentDescription = contentDescription, tint = tint)
        }
        else -> {
            throw IllegalArgumentException("Invalid icon type")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavPreview(){
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}