package com.herehearteam.herehear.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herehearteam.herehear.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    pageTitle: String? = null,
    icon: Any? = null,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    actions: @Composable RowScope.() -> Unit = {},
    height: Dp = 56.dp,
    scrollable: Boolean = false,
    onIconClick: (() -> Unit)? = null
) {
    val scrollBehavior = if (scrollable) {
        TopAppBarDefaults.enterAlwaysScrollBehavior()
    } else {
        null
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(height)
            .let { modifier ->
                if (scrollable && scrollBehavior != null) {
                    modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                } else {
                    modifier
                }
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                when (icon) {
                    is Painter -> Image(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable(
                                onClick = { onIconClick?.invoke() },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                    is ImageVector -> IconButton(
                        onClick = { onIconClick?.invoke() },
                        modifier = Modifier
                            .offset(x = (-10).dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = contentColor,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.weight(2f),
                contentAlignment = Alignment.Center
            ) {
                if (pageTitle != null) {
                    Text(
                        text = pageTitle,
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = contentColor
                        ),
                        fontSize = 18.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
//                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    content = actions
                )
            }
        }
    }
}

@Preview()
@Composable
fun MyScreen() {
    Box(
        modifier = Modifier.padding(top = 100.dp)
    ) {
        CustomTopAppBar(
            pageTitle = "My Screen",
//            icon = painterResource(id = R.drawable.logo),
            icon = Icons.Filled.ArrowBack,
            actions = {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }
        )
    }

}