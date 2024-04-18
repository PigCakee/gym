package com.example.gym.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.R
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.Mono400

@Composable
fun GymTextField(
    value: String,
    hint: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit,
    showTrailingIcon: Boolean = true,
    @DrawableRes trailingIcon: Int = R.drawable.ic_close,
    maxLines: Int = 1,
) {
    // without error height = 56, width = 80
    Column(modifier = Modifier) {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = GymTheme.colors.primary,
            backgroundColor = Color.Transparent
        )
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            OutlinedTextField(
                modifier = modifier,
                value = value,
                onValueChange = onValueChange,
                shape = RoundedCornerShape(8.dp),
                maxLines = maxLines,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    errorContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    cursorColor = GymTheme.colors.primary,
                    errorCursorColor = GymTheme.colors.primary,
                    focusedBorderColor = GymTheme.colors.primary,
                    unfocusedBorderColor = GymTheme.colors.uiBorder,
                    errorBorderColor = GymTheme.colors.error,
                ),
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                isError = !error.isNullOrEmpty(),
                // Body 2/Medium
                textStyle = GymTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = GymTheme.colors.textPrimary,
                ),
                placeholder = {
                    Text(
                        text = hint,
                        style = GymTheme.typography.bodySmall.copy(
                            fontSize = 15.sp,
                            lineHeight = 22.5.sp,
                            fontWeight = FontWeight.Thin,
                            color = Color(0xFF788BAC)
                        )
                    )
                },
//        label = {
//            Text(
//                text = hint,
//                // Body 3/Semibold
//                style = TextStyle(
//                    fontSize = 14.sp,
//                    lineHeight = 21.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    color = GymTheme.colors.textPrimary,
//                )
//            )
//        },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = value.isNotBlank() && showTrailingIcon,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(
                            modifier = Modifier,
                            onClick = onTrailingIconClick
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = trailingIcon),
                                contentDescription = null,
                                tint = Mono400
                            )
                        }
                    }
                }
            )
        }

        AnimatedVisibility(
            modifier = modifier,
            visible = error != null,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = error ?: "",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = GymTheme.colors.error,
                )
            )
        }
    }

}

@Preview
@Composable
fun PreviewTextField() {
    GymTheme {
        Surface(color = GymTheme.colors.uiBackground) {
            GymTextField(
                value = "driver1",
                onValueChange = {},
                hint = "Username",
                onTrailingIconClick = {},
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions()
            )
        }
    }
}

@Preview
@Composable
fun PreviewTextFieldEmpty() {
    GymTheme {
        Surface(color = GymTheme.colors.uiBackground) {
            GymTextField(value = "", onValueChange = {}, hint = "Username", onTrailingIconClick = {},
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions()
            )
        }
    }
}