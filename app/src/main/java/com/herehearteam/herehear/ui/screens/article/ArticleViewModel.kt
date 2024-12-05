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
                tag = "Anxiety",
                date = "20 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article1",
                description = "Pelajari cara-cara efektif untuk mengatasi serangan kecemasan, seperti teknik pernapasan dan pengelolaan stres.",
                source = "Mental Health News"
            ),
            Article(
                id = "2",
                title = "Manfaat Meditasi untuk Kesehatan Mental",
                tag = "Mental Health",
                date = "15 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article2",
                description = "Meditasi memiliki banyak manfaat, termasuk meningkatkan ketenangan dan mengurangi tingkat kecemasan.",
                source = "Healthy Life"
            ),
            Article(
                id = "3",
                title = "Pentingnya Dukungan Sosial dalam Mengatasi Depresi",
                tag = "Depression",
                date = "10 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article3",
                description = "Artikel ini membahas bagaimana dukungan sosial dapat membantu seseorang yang mengalami depresi.",
                source = "Supportive Health Journal"
            ),
            Article(
                id = "4",
                title = "Cara Menangani Pikiran Suicidal secara Aman",
                tag = "Suicidal",
                date = "5 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article4",
                description = "Pikiran suicidal bisa diatasi dengan bantuan profesional dan membangun sistem pendukung yang kuat.",
                source = "Mental Wellness Institute"
            ),
            Article(
                id = "5",
                title = "Mengelola Stres di Tempat Kerja dengan Teknik Relaksasi",
                tag = "Stress",
                date = "1 Agu 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article5",
                description = "Temukan teknik-teknik relaksasi untuk membantu mengurangi stres di tempat kerja.",
                source = "Work Life Balance"
            ),
            Article(
                id = "6",
                title = "Mengenal Gejala Bipolar dan Cara Mengelolanya",
                tag = "Bi-Polar",
                date = "25 Jul 2024",
                imageRes = R.drawable.placeholder_image_article,
                url = "https://example.com/article6",
                description = "Bipolar adalah gangguan mental yang membutuhkan pemahaman mendalam untuk mengelola gejalanya dengan baik.",
                source = "Mental Health Awareness"
            )
        )
        _articles.value = dummyArticles
    }
}