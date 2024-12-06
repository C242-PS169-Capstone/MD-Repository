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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herehearteam.herehear.domain.model.Article
import com.herehearteam.herehear.ui.components.ArticleCard
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.components.ExpandedArticleCard
import com.herehearteam.herehear.ui.components.FilterBox
import com.herehearteam.herehear.ui.theme.HereHearTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    viewModel: ArticleViewModel = viewModel(),
    onNavigateBack: () -> Unit,
) {
    val articles by viewModel.articles.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedArticle by remember { mutableStateOf<Article?>(null) }
    val context = LocalContext.current

    val filters = listOf(
        "Depression" to Color(0xFF4A90E2),
        "Suicidal" to Color(0xFFE24A67),
        "Anxiety" to Color(0xFF7E4AE2),
        "Stress" to Color(0xFF4AE293),
        "Bi-Polar" to Color(0xFFE2C84A),
        "Personality Disorder" to Color(0xFFE24A4A)
    )

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(articles.size) { index ->
                val article = articles[index]

                ArticleCard(
                    title = article.title,
                    tag = article.tag,
                    date = article.date,
                    imagePainter = painterResource(id = article.imageRes),
                    modifier = Modifier.clickable {
                        selectedArticle = article
                        showBottomSheet = true
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // First Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            FilterBox(
                                title = "Depression",
                                backgroundColor = Color(0xFF4A90E2)
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            FilterBox(
                                title = "Suicidal",
                                backgroundColor = Color(0xFFE24A67)
                            )
                        }
                    }

                    // Second Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            FilterBox(
                                title = "Anxiety",
                                backgroundColor = Color(0xFF7E4AE2)
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            FilterBox(
                                title = "Stress",
                                backgroundColor = Color(0xFF4AE293)
                            )
                        }
                    }

                    // Third Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            FilterBox(
                                title = "Bi-Polar",
                                backgroundColor = Color(0xFFE2C84A)
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            FilterBox(
                                title = "Personality Disorder",
                                backgroundColor = Color(0xFFE24A4A)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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
                        tag = selectedArticle!!.tag,
                        date = selectedArticle!!.date,
                        description = selectedArticle!!.description,
                        source = selectedArticle!!.source,
                        imagePainter = painterResource(id = selectedArticle!!.imageRes),
                        onButtonClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedArticle!!.url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleBottomSheetContent(
    article: Article,
    onDismiss: () -> Unit,
    onReadMore: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        ExpandedArticleCard(
            title = article.title,
            tag = article.tag,
            date = article.date,
            description = article.description,
            source = article.source,
            imagePainter = painterResource(id = article.imageRes),
            onButtonClick = {
                onReadMore()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    HereHearTheme {
        ArticleScreen(
            onNavigateBack = {}
        )
    }
}