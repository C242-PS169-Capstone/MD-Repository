package com.herehearteam.herehear.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herehearteam.herehear.R
import com.herehearteam.herehear.ui.theme.ColorPrimary

@Composable
fun CustomButtonFilled(
    onClick: () -> Unit,
    text: String,
    icon: Any? = null,
    iconColor: Color = Color.Unspecified,
    textColor: Color = Color.White,
    backgroundColor: Color = ColorPrimary,
    isEnabled: Boolean = true,
    cornerRadius: Dp = 8.dp,
    height: Dp = 48.dp,
    letterSpacing: TextUnit = 1.sp,
    fontSize: TextUnit = 16.sp,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(cornerRadius),
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (icon != null) {
                    when (icon) {
                        is ImageVector -> {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        is Painter -> {
                            Icon(
                                painter = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = text,
                style = TextStyle(
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize,
                    letterSpacing = letterSpacing
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun CustomButtonOutlined(
    onClick: () -> Unit,
    text: String,
    icon: Any? = null,
    iconColor: Color = Color.Unspecified,
    outlineColor: Color = MaterialTheme.colors.primary,,
    textColor: Color = Color.Black,
    isEnabled: Boolean = true,
    cornerRadius: Dp = 8.dp,
    height: Dp = 48.dp,
    border: Dp = 1.dp,
    letterSpacing: TextUnit = 1.sp,
    fontSize: TextUnit = 16.sp
){
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor
        ),
        border = BorderStroke(border, outlineColor),
        enabled = isEnabled,
        shape = RoundedCornerShape(cornerRadius),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (icon != null) {
                    when (icon) {
                        is ImageVector -> {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        is Painter -> {
                            Icon(
                                painter = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = text,
                style = TextStyle(
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize,
                    letterSpacing = letterSpacing
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ButtonPreview(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            CustomButtonFilled(onClick = {}, text = "LANJUT", icon = Icons.Default.Phone, backgroundColor = Color.Cyan)

            Spacer(modifier = Modifier.height(16.dp))

            CustomButtonOutlined(onClick = {}, text = "LANJUT", painterResource(R.drawable.logo), outlineColor = Color.Cyan)
        }
    }
}
