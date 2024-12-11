package com.herehearteam.herehear.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.herehearteam.herehear.R
import com.herehearteam.herehear.ui.theme.ColorPrimary

@Composable
fun FeatureCardComponent(
    title: String,
    icon: Painter? = null,
    onClick: () -> Unit,
    backgroundColor: Color = ColorPrimary
) {
    Column(
        modifier = Modifier
            .size(85.dp)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = backgroundColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeatureCardPreview(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FeatureCardComponent(
            title = "Streak",
            onClick = {},
            icon = painterResource(R.drawable.ic_hotline)
        )
    }

}

