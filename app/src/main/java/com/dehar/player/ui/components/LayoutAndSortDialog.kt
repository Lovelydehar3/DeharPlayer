package com.dehar.player.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dehar.player.data.FolderLayoutSettings
import com.dehar.player.data.SortOrder
import com.dehar.player.ui.theme.DeharAccent

@Composable
fun LayoutAndSortDialog(
    initialSettings: FolderLayoutSettings,
    onDismiss: () -> Unit,
    onConfirm: (FolderLayoutSettings) -> Unit
) {
    var settings by remember { mutableStateOf(initialSettings) }
    
    // Collapsible accordion states
    var fieldsExpanded by remember { mutableStateOf(false) }
    var advancedExpanded by remember { mutableStateOf(false) }
    
    // Helper to get sort field and direction
    val isAscending = remember(settings.sortOrder) {
        settings.sortOrder == SortOrder.NAME_ASC ||
        settings.sortOrder == SortOrder.DATE_ASC ||
        settings.sortOrder == SortOrder.SIZE_ASC ||
        settings.sortOrder == SortOrder.DURATION_ASC
    }
    
    val currentSortField = remember(settings.sortOrder) {
        when (settings.sortOrder) {
            SortOrder.NAME_ASC, SortOrder.NAME_DESC -> "TITLE"
            SortOrder.DATE_ASC, SortOrder.DATE_DESC -> "DATE"
            SortOrder.SIZE_ASC, SortOrder.SIZE_DESC -> "SIZE"
            SortOrder.DURATION_ASC, SortOrder.DURATION_DESC -> "LENGTH"
        }
    }

    fun updateSortOrder(field: String, ascending: Boolean) {
        val newOrder = when (field) {
            "TITLE" -> if (ascending) SortOrder.NAME_ASC else SortOrder.NAME_DESC
            "DATE" -> if (ascending) SortOrder.DATE_ASC else SortOrder.DATE_DESC
            "SIZE" -> if (ascending) SortOrder.SIZE_ASC else SortOrder.SIZE_DESC
            "LENGTH" -> if (ascending) SortOrder.DURATION_ASC else SortOrder.DURATION_DESC
            else -> if (ascending) SortOrder.NAME_ASC else SortOrder.NAME_DESC
        }
        settings = settings.copy(sortOrder = newOrder)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF172230) // Premium dark slate dialog background
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(vertical = 16.dp),
            border = BorderStroke(1.dp, Color(0xFF263544))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Main Dialog Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // --- 1. VIEW MODE ---
                    Text(
                        text = "View Mode",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ViewModeChip(
                            icon = Icons.Default.FolderSpecial,
                            label = "All folders",
                            isSelected = settings.viewMode == "ALL_FOLDERS",
                            onClick = { settings = settings.copy(viewMode = "ALL_FOLDERS") },
                            modifier = Modifier.weight(1f)
                        )
                        ViewModeChip(
                            icon = Icons.Default.Description,
                            label = "Files",
                            isSelected = settings.viewMode == "FILES",
                            onClick = { settings = settings.copy(viewMode = "FILES") },
                            modifier = Modifier.weight(1f)
                        )
                        ViewModeChip(
                            icon = Icons.Default.Folder,
                            label = "Folders",
                            isSelected = settings.viewMode == "FOLDERS",
                            onClick = { settings = settings.copy(viewMode = "FOLDERS") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // --- 2. LAYOUT ---
                    Text(
                        text = "Layout",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ViewModeChip(
                            icon = Icons.AutoMirrored.Filled.List,
                            label = "List",
                            isSelected = settings.layoutType == "LIST",
                            onClick = { settings = settings.copy(layoutType = "LIST") },
                            modifier = Modifier.weight(1f)
                        )
                        ViewModeChip(
                            icon = Icons.Default.GridView,
                            label = "Grid",
                            isSelected = settings.layoutType == "GRID",
                            onClick = { settings = settings.copy(layoutType = "GRID") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // --- 3. SORT ---
                    Text(
                        text = "Sort",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Grid of 10 sorting buttons (2 rows of 5)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            SortGridItem(
                                icon = Icons.Default.SortByAlpha,
                                label = "Title",
                                isSelected = currentSortField == "TITLE",
                                onClick = { updateSortOrder("TITLE", isAscending) }
                            )
                            SortGridItem(
                                icon = Icons.Default.CalendarToday,
                                label = "Date",
                                isSelected = currentSortField == "DATE",
                                onClick = { updateSortOrder("DATE", isAscending) }
                            )
                            SortGridItem(
                                icon = Icons.Default.AccessTime,
                                label = "Played time",
                                isSelected = false, // Mock/visual alignment
                                onClick = {}
                            )
                            SortGridItem(
                                icon = Icons.Default.CheckCircleOutline,
                                label = "Status",
                                isSelected = false, // Mock
                                onClick = {}
                            )
                            SortGridItem(
                                icon = Icons.Default.HourglassEmpty,
                                label = "Length",
                                isSelected = currentSortField == "LENGTH",
                                onClick = { updateSortOrder("LENGTH", isAscending) }
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            SortGridItem(
                                icon = Icons.Default.SdStorage,
                                label = "Size",
                                isSelected = currentSortField == "SIZE",
                                onClick = { updateSortOrder("SIZE", isAscending) }
                            )
                            SortGridItem(
                                icon = Icons.Default.Hd,
                                label = "Resolution",
                                isSelected = false, // Mock
                                onClick = {}
                            )
                            SortGridItem(
                                icon = Icons.Default.FolderOpen,
                                label = "Path",
                                isSelected = false, // Mock
                                onClick = {}
                            )
                            SortGridItem(
                                icon = Icons.Default.Speed,
                                label = "Frame rate",
                                isSelected = false, // Mock
                                onClick = {}
                            )
                            SortGridItem(
                                icon = Icons.Default.Description,
                                label = "Type",
                                isSelected = false, // Mock
                                onClick = {}
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ascending / Descending Direction Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF263544)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(if (isAscending) DeharAccent else Color.Transparent)
                                .clickable { updateSortOrder(currentSortField, true) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "↑ A to Z",
                                color = if (isAscending) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(if (!isAscending) DeharAccent else Color.Transparent)
                                .clickable { updateSortOrder(currentSortField, false) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "↓ Z to A",
                                color = if (!isAscending) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- 4. FIELDS ACCORDION ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { fieldsExpanded = !fieldsExpanded }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fields",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = if (fieldsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand Fields",
                            tint = Color.Gray
                        )
                    }

                    AnimatedVisibility(visible = fieldsExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                FieldCheckbox("Thumbnail", settings.showThumbnail, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showThumbnail = it)
                                }
                                FieldCheckbox("Length", settings.showLength, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showLength = it)
                                }
                                FieldCheckbox("File extension", settings.showExtension, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showExtension = it)
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                FieldCheckbox("Played time", settings.showPlayedTime, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showPlayedTime = it)
                                }
                                FieldCheckbox("Resolution", settings.showResolution, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showResolution = it)
                                }
                                FieldCheckbox("Frame rate", settings.showFrameRate, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showFrameRate = it)
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                FieldCheckbox("Path", settings.showPath, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showPath = it)
                                }
                                FieldCheckbox("Size", settings.showSize, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showSize = it)
                                }
                                FieldCheckbox("Date", settings.showDate, modifier = Modifier.weight(1f)) {
                                    settings = settings.copy(showDate = it)
                                }
                            }
                        }
                    }

                    Divider(color = Color(0xFF263544), thickness = 1.dp)

                    // --- 5. ADVANCED ACCORDION ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { advancedExpanded = !advancedExpanded }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Advanced",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = if (advancedExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand Advanced",
                            tint = Color.Gray
                        )
                    }

                    AnimatedVisibility(visible = advancedExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AdvancedSwitchRow(
                                label = "Display length over thumbnail",
                                isChecked = settings.displayLengthOverThumbnail
                            ) {
                                settings = settings.copy(displayLengthOverThumbnail = it)
                            }
                            AdvancedSwitchRow(
                                label = "Show hidden files and folders",
                                isChecked = settings.showHidden
                            ) {
                                settings = settings.copy(showHidden = it)
                            }
                            AdvancedSwitchRow(
                                label = "Recognize .nomedia",
                                isChecked = settings.recognizeNoMedia
                            ) {
                                settings = settings.copy(recognizeNoMedia = it)
                            }
                        }
                    }
                }

                // --- BOTTOM BUTTONS ---
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = { onConfirm(settings) }) {
                        Text(text = "Done", color = DeharAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ViewModeChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) DeharAccent.copy(alpha = 0.15f) else Color(0xFF263544),
        border = BorderStroke(1.dp, if (isSelected) DeharAccent else Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(58.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) DeharAccent else Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = if (isSelected) DeharAccent else Color.LightGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SortGridItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(62.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    if (isSelected) DeharAccent.copy(alpha = 0.2f) else Color(0xFF263544),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) DeharAccent else Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) DeharAccent else Color.LightGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 11.sp
        )
    }
}

@Composable
private fun FieldCheckbox(
    label: String,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = DeharAccent,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.Black
            ),
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = Color.LightGray,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun AdvancedSwitchRow(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.LightGray,
            fontSize = 13.sp
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = DeharAccent,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color(0xFF263544)
            )
        )
    }
}
