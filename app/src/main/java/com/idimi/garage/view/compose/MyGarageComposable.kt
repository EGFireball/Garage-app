package com.idimi.garage.view.compose

import android.net.Uri
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.idimi.garage.R
import com.idimi.garage.datamodel.model.FuelType
import com.idimi.garage.datamodel.model.Vehicle
import com.idimi.garage.view.compose.components.FuelTypeDropDown
import com.idimi.garage.view.compose.components.ImagePicker
import com.idimi.garage.view.compose.components.YearDropdown
import com.idimi.garage.view.ui.theme.getTheme
import com.idimi.garage.view.viewmodel.GarageViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

@Composable
fun MyGarage(
    modifier: Modifier,
    garageViewModel: GarageViewModel
) {

    val vehicles = garageViewModel.vehicleStateFlow.collectAsStateWithLifecycle()

    var showAddVehiclePopup by remember {
        mutableStateOf(false)
    }

    val selectedVehicle = remember {
        mutableStateOf<Vehicle?>(null)
    }

    LaunchedEffect(Unit) {
        garageViewModel.getAllVehicles()
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (vehicles.value.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .clickable {
                        showAddVehiclePopup = true
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No vehicles in the garage.\nAdd new now.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add vehicle to garage."
                )
            }
        } else {
            LazyColumn {
                items(vehicles.value) { vehicle ->
                    VehicleItem(
                        vehicle = vehicle,
                    ) { chosenVehicle ->
                        selectedVehicle.value = chosenVehicle
                        showAddVehiclePopup = true
                    }
                }
            }
            FloatingActionButton(
                modifier =
                    Modifier
                        .size(80.dp)
                        .padding(end = 12.dp, bottom = 12.dp)
                        .background(color = getTheme().primary, shape = CircleShape)
                        .align(Alignment.BottomEnd),
                onClick = {
                    showAddVehiclePopup = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Vehicle"
                )
            }
        }
    }
    if (showAddVehiclePopup) {
        AddVehiclePopup(
            garageViewModel = garageViewModel,
            vehicleForEdit = selectedVehicle.value
        ) {
            selectedVehicle.value = null
            showAddVehiclePopup = false
        }
    }
}

@Composable
fun VehicleItem(
    modifier: Modifier = Modifier,
    vehicle: Vehicle,
    onEditClicked: (Vehicle) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            if (vehicle.iconURL != null) {
                AsyncImage(
                    model = File(vehicle.iconURL!!),
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_car),
                    contentDescription = ""
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Text(text = "Vehicle name: ")
                    Text(text = vehicle.name)
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(text = "Manufacturer: ")
                    Text(text = vehicle.make)
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(text = "Model: ")
                    Text(text = vehicle.model)
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(text = "Year: ")
                    Text(text = vehicle.year.toString())
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(text = "VIN: ")
                    Text(text = vehicle.vin)
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(text = "Fuel: ")
                    Text(text = vehicle.fuelType.name.lowercase().capitalize(Locale.ROOT))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Edit,
                modifier = Modifier.size(32.dp)
                    .clickable {
                        onEditClicked(vehicle)
                    },
                tint = getTheme().onPrimary,
                contentDescription = "Edit Vehicle Data",
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun AddVehiclePopup(
    garageViewModel: GarageViewModel,
    vehicleForEdit: Vehicle?,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val vehicleName = remember {
        mutableStateOf(
            vehicleForEdit?.name ?: ""
        )
    }

    val modelState = remember {
        mutableStateOf(
            vehicleForEdit?.model ?: ""
        )
    }

    val makeState = remember {
        mutableStateOf(
            vehicleForEdit?.make ?: ""
        )
    }

    val vinState = remember {
        mutableStateOf(
            vehicleForEdit?.vin ?: ""
        )
    }

    val yearState = remember {
        mutableIntStateOf(
            vehicleForEdit?.year ?: -1
        )
    }

    val fuelType = remember {
        mutableStateOf(
            vehicleForEdit?.fuelType ?: FuelType.PETROL
        )
    }

    val iconUri = remember {
        mutableStateOf<Uri?>(
            vehicleForEdit?.iconURL?.toUri()
        )
    }

    Popup(
        alignment = Alignment.TopCenter,
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            focusable = true
        ),
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface (
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            modifier = Modifier
                .wrapContentSize()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Add Vacation Vehicle",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    if (iconUri.value != null) {
                        AsyncImage(
                            model = iconUri.value!!,
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        ImagePicker(modifier = Modifier.size(64.dp)) { uri ->
                            iconUri.value = uri
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        text = "Vehicle Icon",
                        fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    value = vehicleName.value,
                    shape = RoundedCornerShape(24.dp),
                    onValueChange = { name ->
                        vehicleName.value = name
                    },
                    placeholder = {
                        Text(
                            text = "Set Vehicle Name",
                        )
                    },
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    value = makeState.value,
                    shape = RoundedCornerShape(24.dp),
                    onValueChange = { make ->
                        makeState.value = make
                    },
                    placeholder = {
                        Text(
                            text = "Set Vehicle manufacturer",
                        )
                    },
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    value = modelState.value,
                    shape = RoundedCornerShape(24.dp),
                    onValueChange = { model ->
                        modelState.value = model
                    },
                    placeholder = {
                        Text(
                            text = "Set Model",
                        )
                    },
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    value = vinState.value,
                    shape = RoundedCornerShape(24.dp),
                    onValueChange = { vin ->
                        vinState.value = vin
                    },
                    placeholder = {
                        Text(
                            text = "Set VIN",
                        )
                    },
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    YearDropdown(
                        label = if (yearState.intValue == -1) "Year of production" else yearState.intValue.toString()
                    ) { year ->
                        yearState.intValue = year
                    }
                }
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    FuelTypeDropDown(
                        label = "Fuel"
                    ) { ft ->
                        fuelType.value = ft
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                ) {
                    Spacer (Modifier.weight(1f))
                    Button(
                        modifier = Modifier.wrapContentWidth().height(45.dp),
                        enabled = vehicleName.value.isNotEmpty() &&
                                makeState.value.isNotEmpty() &&
                                vinState.value.isNotEmpty() &&
                                yearState.intValue != -1,
                        onClick = {
                            coroutineScope.launch {
//                            val path = garageViewModel.saveImageToAppStorage(
//                                context,
//                                iconUri.value!!,
//                                vehicleName.value
//                            )

                                val vehicle = Vehicle(
                                    id = vehicleForEdit?.id ?: 0,
                                    name = vehicleName.value,
                                    model = modelState.value,
                                    make = makeState.value,
                                    vin = vinState.value,
                                    year = yearState.intValue,
                                    fuelType = fuelType.value,
                                    iconURL = iconUri.value?.toString()
                                )
                                garageViewModel.addVehicleToGarage(
                                    vehicle,
                                    forEdit = vehicleForEdit != null
                                )
                                onDismiss()
                            }
                        }
                    ) {
                        Text(text = "Submit")
                    }
                    Spacer (Modifier.weight(1f))
                    Button(
                        modifier = Modifier.wrapContentWidth().height(45.dp),
                        enabled = true,
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer (Modifier.weight(1f))
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}