package com.example.gym.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.LottieLoader
import com.example.gym.ui.theme.Brand500
import com.example.gym.ui.theme.Brand500Inactive
import com.example.gym.ui.theme.Error
import com.example.gym.ui.theme.GymFontFamily
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.Success
import com.example.gym.ui.theme.Warning
import com.example.gym.ui.theme.White

@Composable
fun GymButton(
    modifier: Modifier = Modifier,
    buttonType: ButtonType = ButtonType.Positive,
    buttonText: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    elevation: Dp = 0.dp,
    @DrawableRes iconStart: Int? = null,
    iconStartPadding: Dp = 8.dp,
    @DrawableRes iconEnd: Int? = null,
    iconEndPadding: Dp = 8.dp,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    textStyle: TextStyle = GymTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Normal,
        fontFamily = GymFontFamily,
        fontSize = 15.sp,
    ),
    onClick: () -> Unit
) {

    val textColor = when (buttonType) {
        is ButtonType.Positive -> {
            if (enabled) White else White
        }
        is ButtonType.Negative -> {
            if (enabled) Brand500 else Brand500Inactive
        }
        is ButtonType.Error -> {
            White
        }
        is ButtonType.Transparent -> {
            if (enabled) Brand500 else Brand500Inactive
        }

        is ButtonType.Warning -> {
            White
        }

        is ButtonType.Logout -> {
            if (enabled) Brand500 else Brand500Inactive
        }

        is ButtonType.Success -> {
            White
        }
    }
    val buttonColor = when (buttonType) {
        is ButtonType.Positive -> {
            if (enabled) Brand500 else Brand500Inactive
        }

        is ButtonType.Negative -> {
            Color.Transparent
        }

        is ButtonType.Warning -> {
            Warning
        }

        is ButtonType.Logout -> {
            Color.Transparent
        }
        is ButtonType.Error -> {
            if (enabled) Error else Error.copy(alpha = 0.5f)
        }
        is ButtonType.Transparent -> {
            Color.Transparent
        }
        is ButtonType.Success -> {
            Success
        }
    }
    val strokeColor = if (buttonType is ButtonType.Negative) Brand500 else Color.Transparent
    Button(
        modifier = modifier,
        onClick = { if (!isLoading) onClick() },
        elevation = ButtonDefaults.buttonElevation(
            pressedElevation = elevation,
            defaultElevation = elevation
        ),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(
            width = 1.dp,
            color = strokeColor
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = buttonColor,
            containerColor = buttonColor,
        ),
        contentPadding = contentPadding
    ) {
        if (isLoading) {
            LottieLoader(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp),
            )
        } else {
            iconStart?.let {
                Icon(
                    modifier = Modifier
                        .padding(end = iconStartPadding)
                        .align(CenterVertically),
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = textColor
                )
            }
            Text(
                color = textColor,
                modifier = Modifier
                    .wrapContentWidth()
                    .align(CenterVertically)
                    .padding(horizontal = 8.dp),
                text = buttonText,
                maxLines = 1,
                style = textStyle,
            )
            iconEnd?.let {
                Icon(
                    modifier = Modifier
                        .padding(start = iconEndPadding)
                        .align(CenterVertically),
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = textColor
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewButton() {
    GymTheme {
        Column {
            GymButton(modifier = Modifier.fillMaxWidth(), buttonText = "Submit") {}
            GymButton(buttonText = "Cancel", buttonType = ButtonType.Negative) {}
        }
    }
}

sealed class ButtonType {
   data object Positive : ButtonType()
   data object Negative : ButtonType()
   data object Success : ButtonType()
   data object Warning : ButtonType()
   data object Logout : ButtonType()
   data object Error : ButtonType()
   data object Transparent : ButtonType()
}