package com.herehearteam.herehear.ui.screens.article

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herehearteam.herehear.R
import com.herehearteam.herehear.data.remote.api.ApiConfig
import com.herehearteam.herehear.data.remote.api.ApiService
import com.herehearteam.herehear.domain.model.Article
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val apiService: ApiService
) : ViewModel() {
    var articles by mutableStateOf<List<Article>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun fetchArticles(query: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = apiService.searchArticles(
                    query = query,
                    apiKey = ApiConfig.GUARDIAN_API_KEY
                )
                articles = response.response.results.map { result ->
                    Article(
                        id = result.id,
                        title = result.webTitle,
                        sectionName = result.sectionName,
                        publicationDate = result.webPublicationDate,
                        webUrl = result.webUrl,
                        description = "",
                        imageRes = R.drawable.placeholder_image_article
                    )
                }
            } catch (e: Exception) {
                error = "Gagal memuat artikel: ${e.localizedMessage}"
                articles = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}