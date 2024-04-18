package com.example.gym.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePickerBottomSheet(
    query: String,
    exercises: List<ExerciseUi>,
    sheetState: SheetState,
    onClose: () -> Unit,
    onPicked: (ExerciseUi) -> Unit,
    onQueryChanged: (String) -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        sheetState = sheetState,
        containerColor = Color.White,
        contentColor = White,
        onDismissRequest = onClose,
        scrimColor = Color(0xFFEAEFF8),
        dragHandle = {
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .width(40.dp),
                thickness = 3.dp,
                color = GymTheme.colors.uiBorder
            )
        }
    ) {
        GymTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = query,
            hint = "Enter name",
            keyboardActions = KeyboardActions(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            onValueChange = onQueryChanged,
            onTrailingIconClick = { onQueryChanged("") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(exercises) {
                ExerciseListItem(
                    exerciseUi = it,
                    onClick = onPicked,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}