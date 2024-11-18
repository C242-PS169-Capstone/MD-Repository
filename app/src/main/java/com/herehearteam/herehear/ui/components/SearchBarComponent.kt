package com.herehearteam.herehear.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.herehearteam.herehear.ui.theme.ColorPrimary
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalContentColor


@Composable
fun CustomSearchBar(
    onValueChange: (String) -> Unit,
    placeholder: String = "Search",
    iconColor: Color = Color.DarkGray,
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = iconColor)
    },
    trailingIcon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = Color.LightGray,
    textColor: Color = Color.Black,
    placeholderColor: Color = Color.Gray,
){
    var searchQuery by remember { mutableStateOf("") }

    TextField(
        value = searchQuery,
        onValueChange = { newValue ->
            searchQuery = newValue
            onValueChange(newValue)
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor),
        leadingIcon = leadingIcon,
        trailingIcon = if (trailingIcon != null) {
            {
                CompositionLocalProvider(LocalContentColor provides iconColor) {
                    trailingIcon()
                }
            }
        } else null,
        placeholder = {
            androidx.compose.material3.Text(
                text = placeholder,
                color = placeholderColor
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            cursorColor = textColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchBarPreview(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        CustomSearchBar(
            iconColor = ColorPrimary,
            onValueChange = {},
            trailingIcon = {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date Filter")
            },
        )
    }
}