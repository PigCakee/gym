package com.example.gym.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gym.R
import com.example.gym.ui.theme.GymTheme
import com.example.gym.ui.theme.White
import com.example.gym.util.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownBox(
    modifier: Modifier = Modifier,
    items: List<DropdownUiItem>,
    defaultSelection: DropdownUiItem,
    onItemSelected: (DropdownUiItem) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        Surface(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0xFFEAEFF8),
                    shape = RoundedCornerShape(size = 8.dp)
                ),
            color = White,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = modifier.menuAnchor(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                defaultSelection.iconId?.let {
                    Image(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(18.dp),
                        painter = painterResource(id = it),
                        contentDescription = null
                    )
                }
                defaultSelection.imageUrl?.let {
                    GymAsyncImage(
                        model = it,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(24.dp),
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .weight(1f),
                    text = defaultSelection.title.asString(context),
                    style = GymTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        lineHeight = 22.5.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF1B1B1B),
                    )
                )
                Image(
                    modifier = Modifier.padding(end = 12.dp),
                    painter = painterResource(id = R.drawable.ic_chevron_down),
                    contentDescription = null
                )
            }
        }

        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                val name = item.title.asString(context)
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White),
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = White),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            item.iconId?.let {
                                Image(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource(id = item.iconId),
                                    contentDescription = null
                                )
                            }
                            item.imageUrl?.let {
                                GymAsyncImage(
                                    model = it,
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.size(24.dp),
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = name,
                                style = GymTheme.typography.bodyLarge.copy(
                                    color = Color(0xFF1B1B1B),
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp,
                                    lineHeight = 22.5.sp
                                )
                            )
                        }
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

data class DropdownUiItem(
    val title: UiText,
    val iconId: Int? = null,
    val imageUrl: String? = null,
)