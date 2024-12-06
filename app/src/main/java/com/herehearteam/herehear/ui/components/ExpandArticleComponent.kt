package com.herehearteam.herehear.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herehearteam.herehear.R

@Composable
fun ExpandedArticleCard(
    title: String,
    tag: String,
    date: String,
    description: String,
    source: String,
    onButtonClick: () -> Unit,
    imagePainter: Painter = painterResource(id = R.drawable.placeholder_image_article),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ArticleCard(
            title = title,
            tag = tag,
            date = date,
            imagePainter = imagePainter
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Source: $source",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButtonFilled(
            onClick = onButtonClick,
            text = "SELENGKAPNYA"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandedArticleCardPreview() {
    ExpandedArticleCard(
        title = "9 Cara Mengatasi Anxiety Attack agar Kembali Tenang",
        tag = "Anxiety",
        date = "21 Agu 2024",
        description = "Ini adalah deskripsi artikel tentang cara mengatasi anxiety attack agar Anda bisa kembali tenang.",
        source = "Siloam Hospital",
        onButtonClick = { /* Handle button click */ }
    )
}


