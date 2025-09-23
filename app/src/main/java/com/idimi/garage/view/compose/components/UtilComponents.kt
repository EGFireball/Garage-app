package com.idimi.garage.view.compose.components

import android.icu.util.Calendar
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.idimi.garage.datamodel.model.FuelType
import com.idimi.garage.view.ui.theme.getTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildScreenTopBar(
    topBarTitle:String,
    topAppBarHeight:MutableState<Int>,
    topAppBarWidth:MutableState<Int>,
//    topLeftButton1Action:(()->Unit)? = null,
//    topLeftButton2Action:(()->Unit)? = null,
//    topRightButton1Action:(()->Unit)? = null,
//    topRightButton2Action:(()->Unit)? = null,
    viewBuilder: @Composable (pv:PaddingValues) -> Unit
){

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
            Text(
                text = topBarTitle,
                fontWeight = FontWeight.Bold,
                color = getTheme().onPrimary
            )
        },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    topAppBarHeight.value = it.size.height
                    topAppBarWidth.value = it.size.width
                }
                .onSizeChanged {
                    topAppBarHeight.value = it.height
                    topAppBarWidth.value = it.width
                }
            , colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = getTheme().primary)
//            , navigationIcon = {
//
//            }
//            , actions = {
//
//            }
        )
    }
    ) { paddingValues ->
        viewBuilder(paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearDropdown(
    label: String = "Year",
    startYear: Int = 1960,
    endYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    onYearSelected: (Int) -> Unit
) {
    val years = (startYear..endYear).toList().reversed()
    var expanded by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = getTheme().secondaryContainer, shape = RoundedCornerShape(8.dp)),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = selectedYear,
            onValueChange = { year ->
                selectedYear = year
            },
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            years.forEach { year ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = year.toString()
                        )
                    },
                    onClick = {
                        selectedYear = year.toString()
                        expanded = false
                        onYearSelected(year)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelTypeDropDown(
    label: String = "Fuel",
    onFuelSelected: (FuelType) -> Unit
) {

    val fuelTypes = FuelType.entries
    var expanded by remember { mutableStateOf(false) }
    var selectedFuelType by remember { mutableStateOf(FuelType.PETROL) }


    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = getTheme().secondaryContainer, shape = RoundedCornerShape(8.dp)),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = selectedFuelType.name.lowercase().capitalize(Locale.ROOT),
            onValueChange = { ft ->
                selectedFuelType = fuelTypes.first { it.name == ft }
            },
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            fuelTypes.forEach { ft ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = ft.name.lowercase().capitalize(Locale.ROOT)
                        )
                    },
                    onClick = {
                        selectedFuelType = ft
                        expanded = false
                        onFuelSelected(ft)
                    }
                )
            }
        }
    }
}

@Composable
fun ImagePicker(
    modifier: Modifier,
    onIconAdded: (Uri?) -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    // Launcher to pick image from gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        onIconAdded(imageUri)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        // Placeholder / Selected image
        if (imageUri != null) {
            AsyncImage( // From Coil
                model = imageUri,
                contentDescription = "Selected image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        launcher.launch("image/*")
                    },
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        launcher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Placeholder",
                    modifier = Modifier.size(180.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomIconSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "On",
            tint =  Color.Yellow
        )
    },
    uncheckedIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = "Off",
            tint = Color.Yellow
        )
    }
) {
    Switch(
        checked = checked,
        onCheckedChange = { isChecked ->
            onCheckedChange(isChecked)
        },
        modifier = modifier,
        thumbContent = {
            if (checked) checkedIcon?.invoke() else uncheckedIcon?.invoke()
        }
    )
}