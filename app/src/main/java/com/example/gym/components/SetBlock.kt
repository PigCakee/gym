package com.example.gym.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.data.Set
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.Mono400

@Composable
fun SetBlock(
    set: Set,
    setNum: Int,
    onModify: (Set) -> Unit,
    modifier: Modifier = Modifier,
) {
    var valueWeight by remember {
        mutableStateOf(set.actualWeight?.toString())
    }
    var valueReps by remember {
        mutableStateOf(set.actualReps?.toString())
    }
    val repsFocus = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GymTheme.colors.uiBorder),
        color = Color.White
    ) {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = GymTheme.colors.primary,
            backgroundColor = Color.Transparent
        )
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set $setNum",
                style = GymTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GymTheme.colors.textPrimary
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sug. weight (kg): ${set.sugWeight} | Actual (kg):",
                    style = GymTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = GymTheme.colors.textPrimary
                    )
                )
                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                    OutlinedTextField(
                        modifier = Modifier
                            .width(64.dp)
                            .height(48.dp)
                            .onFocusChanged {
                                if (valueWeight != null && !it.hasFocus) {
                                    onModify(set.copy(actualWeight = valueWeight?.toDouble()))
                                }
                            },
                        value = valueWeight ?: "",
                        onValueChange = {
                            valueWeight = it
                        },
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 1,
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
                        keyboardActions = KeyboardActions(
                            onNext = {
                                onModify(set.copy(actualWeight = valueWeight?.toDouble()))
                                repsFocus.requestFocus()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Decimal
                        ),
                        textStyle = GymTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = GymTheme.colors.textPrimary,
                        ),
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Sug. reps: ${set.sugReps} | Actual:",
                    style = GymTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = GymTheme.colors.textPrimary
                    )
                )
                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                    OutlinedTextField(
                        modifier = Modifier
                            .width(64.dp)
                            .height(48.dp)
                            .focusRequester(repsFocus)
                            .onFocusChanged {
                                if (valueReps != null && !it.hasFocus) {
                                    onModify(set.copy(actualReps = valueReps?.toDouble()))
                                }
                            },
                        value = valueReps ?: "",
                        onValueChange = {
                            valueReps = it
                        },
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 1,
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
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onModify(set.copy(actualReps = valueReps?.toDouble()))
                                keyboardController?.hide()
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Decimal
                        ),
                        textStyle = GymTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = GymTheme.colors.textPrimary,
                        ),
                    )
                }
            }
        }
    }
}