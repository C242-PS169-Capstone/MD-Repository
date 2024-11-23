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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.herehearteam.herehear.ui.components.ArticleCard
import com.herehearteam.herehear.ui.components.CustomTopAppBar
import com.herehearteam.herehear.ui.components.FilterBox
import com.herehearteam.herehear.ui.theme.HereHearTheme

@Composable
fun ArticleScreen(
    viewModel: ArticleViewModel = viewModel()
) {
    val articles by viewModel.articles.collectAsState()

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
            icon = Icons.AutoMirrored.Filled.ArrowBack
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(articles.size) { index ->
                val article = articles[index]
                val context = LocalContext.current

                ArticleCard(
                    title = article.title,
                    tags = article.tags,
                    date = article.date,
                    imagePainter = painterResource(id = article.imageRes),
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                        context.startActivity(intent)
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
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    HereHearTheme {
        ArticleScreen()
    }
}