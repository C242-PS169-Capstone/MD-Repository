package com.herehearteam.herehear.ui.screens.article

import androidx.lifecycle.ViewModel
import com.herehearteam.herehear.R
import com.herehearteam.herehear.domain.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ArticleViewModel : ViewModel() {
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    init {
        loadDummyArticles()
    }

    private fun loadDummyArticles() {
        val dummyArticles = listOf(
            Article(
                id = "1",
                title = "9 Cara Mengatasi Anxiety Attack agar Kembali Tenang",
                tags = listOf("ADHD", "Mental Health"),
                date = "20 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article1"
            ),
            Article(
                id = "2",
                title = "9 Cara Mengatasi Anxiety Attack agar Kembali Tenang",
                tags = listOf("Anxiety", "Mental Health"),
                date = "20 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article2"
            ),
            Article(
                id = "3",
                title = "9 Cara Mengatasi Anxiety Attack agar Kembali Tenang",
                tags = listOf("ADHD", "Mental Health"),
                date = "20 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article1"
            ),
            Article(
                id = "4",
                title = "9 Cara Mengatasi Anxiety Attack agar Kembali Tenang",
                tags = listOf("Anxiety", "Mental Health"),
                date = "20 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article2"
            ),
            Article(
                id = "5",
                title = "9 Cara Mengatasi Anxiety Attack agar Kembali Tenang",
                tags = listOf("Anxiety", "Mental Health"),
                date = "20 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article2"
            ),

        )
        _articles.value = dummyArticles
    }
}