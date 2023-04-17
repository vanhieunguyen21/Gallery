package com.example.gallery.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gallery.R
import com.example.gallery.model.SortOrder
import com.example.gallery.model.SortType

@Composable
fun SortBar(
    sortType: SortType,
    sortOrder: SortOrder,
    onSortTypeChange: (SortType) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box {
            var dropdownExpanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .clickable { dropdownExpanded = !dropdownExpanded }
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(R.drawable.ic_sort),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.Gray,
                )
                Text(
                    text = when (sortType) {
                        SortType.Name -> stringResource(R.string.sort_type_name)
                        SortType.LastModified -> stringResource(R.string.sort_type_last_modified)
                        SortType.Size -> stringResource(R.string.sort_type_size)
                    },
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = 14.sp,
                        color = Color.Gray,
                    )
                )
            }
            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    onSortTypeChange(SortType.Name)
                    dropdownExpanded = false
                }) {
                    Text(stringResource(R.string.sort_type_name))
                }
                DropdownMenuItem(onClick = {
                    onSortTypeChange(SortType.LastModified)
                    dropdownExpanded = false
                }) {
                    Text(stringResource(R.string.sort_type_last_modified))
                }
                DropdownMenuItem(onClick = {
                    onSortTypeChange(SortType.Size)
                    dropdownExpanded = false
                }) {
                    Text(stringResource(R.string.sort_type_size))
                }
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = {
            onSortOrderChange(
                when (sortOrder) {
                    SortOrder.Ascending -> SortOrder.Descending
                    SortOrder.Descending -> SortOrder.Ascending
                }
            )
        }, modifier = Modifier.size(18.dp)) {
            Icon(
                when (sortOrder) {
                    SortOrder.Ascending -> painterResource(R.drawable.ic_arrow_upward)
                    SortOrder.Descending -> painterResource(R.drawable.ic_arrow_downward)
                },
                contentDescription = null,
                tint = Color.Gray,
            )
        }
    }
}