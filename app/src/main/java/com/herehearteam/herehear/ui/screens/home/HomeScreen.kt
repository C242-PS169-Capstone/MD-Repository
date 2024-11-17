package com.herehearteam.herehear.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.R
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.components.DailyQuestionCard
import com.herehearteam.herehear.ui.components.FeatureCardComponent
import com.herehearteam.herehear.ui.components.UserGreetingCard
import com.herehearteam.herehear.ui.components.WeeklyMoodCard
import com.herehearteam.herehear.ui.theme.ColorPrimary
import com.herehearteam.herehear.ui.theme.HereHearTheme


@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Upper Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .background(ColorPrimary)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CustomTopAppBar(
                    pageTitle = "",
                    icon = painterResource(R.drawable.ic_herehear),
                    contentColor = Color.White,
                    backgroundColor = Color.Transparent,
                    actions = {
                        IconButton(onClick = { viewModel.onNotificationClick() }) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { viewModel.onSettingsClick() }) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    }
                )

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = 40.dp),
                ) {
                    UserGreetingCard(
                        userName = uiState.userName,
                    )
                }

            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 60.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                item { FeatureCardComponent(title = "Streak", onClick = {}) }
                item { FeatureCardComponent(title = "Forum", onClick = {}) }
                item { FeatureCardComponent(title = "Psikolog", onClick = {}) }
                item { FeatureCardComponent(title = "Hotline", onClick = {}) }
            }

            Spacer(modifier = Modifier.height(24.dp))

            DailyQuestionCard(
                question = uiState.dailyQuestion,
                onClick = viewModel::onDailyQuestionClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            WeeklyMoodCard(
                moodData = uiState.weeklyMoods,
                onMoodClick = viewModel::onMoodClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HereHearTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val navController = rememberNavController()

            HomeScreen(navController)
        }
    }
}
