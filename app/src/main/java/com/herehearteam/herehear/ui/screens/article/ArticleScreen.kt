package com.herehearteam.herehear.ui.screens.article

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.data.remote.api.ApiConfig.getApiService
import com.herehearteam.herehear.domain.model.Article
import com.herehearteam.herehear.ui.components.ArticleCard
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.components.ExpandedArticleCard
import com.herehearteam.herehear.ui.components.FilterBox
import com.herehearteam.herehear.ui.theme.HereHearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    onNavigateBack: () -> Unit
) {
    val filters = listOf(
        "Anxiety" to Color(0xFF7E4AE2),
        "Depression" to Color(0xFF4A90E2),
        "Suicidal" to Color(0xFFE24A67),
        "Stress" to Color(0xFF4AE293),
        "Bi-Polar" to Color(0xFFE2C84A),
        "Personality Disorder" to Color(0xFFE24A4A)
    )
    val apiService = ApiConfig.getArticleService()

    val articleViewModel: ArticleViewModel = viewModel(factory = ArticleViewModelFactory(apiService))

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedArticle by remember { mutableStateOf<Article?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        articleViewModel.fetchArticles("mental health")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomTopAppBar(
            pageTitle = "Artikel",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            onIconClick = onNavigateBack
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { (query, color) ->
                Button(
                    onClick = { articleViewModel.fetchArticles(query) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = color
                    )
                ) {
                    Text(query)
                }
            }
        }

        articleViewModel.error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        if (articleViewModel.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(articleViewModel.articles) { article ->
                    ArticleCard(
                        title = article.title,
                        tag = article.sectionName,
                        date = article.publicationDate,
                        modifier = Modifier.clickable {
                            selectedArticle = article
                            showBottomSheet = true
                        }
                    )
                }
            }
        }

        if (showBottomSheet && selectedArticle != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    selectedArticle = null
                },
                sheetState = rememberModalBottomSheetState(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    ExpandedArticleCard(
                        title = selectedArticle!!.title,
                        tag = selectedArticle!!.sectionName,
                        date = selectedArticle!!.publicationDate,
                        description = "", // Tambahkan deskripsi jika tersedia dari API
                        source = "The Guardian",
                        onButtonClick = {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(selectedArticle!!.webUrl))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    HereHearTheme {
        val apiService = getApiService()
        ArticleScreen(
            onNavigateBack = {},
        )
    }
}