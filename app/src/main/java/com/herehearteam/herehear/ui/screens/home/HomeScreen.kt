package com.herehearteam.herehear.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.herehearteam.herehear.R
import com.herehearteam.herehear.data.local.repository.PredictionRepository
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.di.AppDependencies
import com.herehearteam.herehear.domain.model.Article
import com.herehearteam.herehear.ui.components.ArticleCard
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.components.DailyQuestionCard
import com.herehearteam.herehear.ui.components.ExpandedArticleCard
import com.herehearteam.herehear.ui.components.FeatureCardComponent
import com.herehearteam.herehear.ui.components.HotlineAlertDialog
import com.herehearteam.herehear.ui.components.LocalGoogleAuthUiClient
import com.herehearteam.herehear.ui.components.UserGreetingCard
import com.herehearteam.herehear.ui.components.WeeklyMoodCard
import com.herehearteam.herehear.ui.screens.article.ArticleViewModel
import com.herehearteam.herehear.ui.screens.predict.PredictionViewModel
import com.herehearteam.herehear.ui.theme.ColorPrimary
import com.herehearteam.herehear.ui.theme.HereHearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(LocalGoogleAuthUiClient.current)
    ),
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showHotlineAlert by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedArticle by remember { mutableStateOf<Article?>(null) }

    val apiService = ApiConfig.getApiService()
    val predictionRepository = PredictionRepository(apiService)

    if (showHotlineAlert) {
        HotlineAlertDialog(
            onDismiss = { showHotlineAlert = false }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
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
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-40).dp)
                ) {
                    UserGreetingCard(
                        userName = uiState.userName,
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                ) {
                    Grid(
                        columns = 4,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FeatureCardComponent(
                            title = "Streak", onClick = {}
                        )
                        FeatureCardComponent(
                            title = "Forum", onClick = {}
                        )
                        FeatureCardComponent(
                            title = "Psikolog", onClick = {}
                        )
                        FeatureCardComponent(
                            title = "Hotline", onClick = {
                                showHotlineAlert = true
                            }
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp)
                ) {
                    DailyQuestionCard(
                        question = uiState.dailyQuestion,
                        onClick = viewModel::onDailyQuestionClick
                    )
                }
            }

//            item {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp)
//                        .padding(top = 24.dp)
//                ) {
//                    Text(
//                        text = "Artikel Terbaru",
//                        style = MaterialTheme.typography.titleMedium,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//
//                    articles.take(5).forEach { article ->
//                        ArticleCard(
//                            title = article.title,
//                            tag = article.tag,
//                            date = article.date,
//                            imagePainter = painterResource(id = article.imageRes),
//                            modifier = Modifier
//                                .padding(vertical = 8.dp)
//                                .clickable {
//                                    selectedArticle = article
//                                    showBottomSheet = true
//                                }
//                        )
//                    }
//                }
//            }
//
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//        if (showBottomSheet && selectedArticle != null) {
//            ModalBottomSheet(
//                onDismissRequest = {
//                    showBottomSheet = false
//                    selectedArticle = null
//                },
//                sheetState = rememberModalBottomSheetState(),
//                containerColor = MaterialTheme.colorScheme.surface,
//                contentColor = MaterialTheme.colorScheme.onSurface
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    ExpandedArticleCard(
//                        title = selectedArticle!!.title,
//                        tag = selectedArticle!!.tag,
//                        date = selectedArticle!!.date,
//                        description = selectedArticle!!.description,
//                        source = selectedArticle!!.source,
//                        imagePainter = painterResource(id = selectedArticle!!.imageRes),
//                        onButtonClick = {
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedArticle!!.url))
//                            context.startActivity(intent)
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            }
        }
    }
}

@Composable
private fun Grid(
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemWidth = (constraints.maxWidth - (columns - 1)) / columns
        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth
        )

        val placeables = measurables.map { it.measure(itemConstraints) }
        val height = placeables.chunked(columns).fold(0) { acc, row ->
            acc + (row.maxOfOrNull { it.height } ?: 0)
        }

        layout(constraints.maxWidth, height) {
            var y = 0
            placeables.chunked(columns).forEach { row ->
                var x = 0
                row.forEach { placeable ->
                    placeable.place(x, y)
                    x += itemWidth
                }
                y += (row.maxOfOrNull { it.height } ?: 0)
            }
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
