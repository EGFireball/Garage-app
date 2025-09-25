package com.idimi.garage.view.compose.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.idimi.garage.util.isNetworkAvailable
import com.idimi.garage.view.openNetworkSettings
import kotlinx.coroutines.launch

@Composable
fun MapElement(
    modifier: Modifier,
    markerTitle: String,
    lat: Double,
    lng: Double,
    onPinClicked: (Marker) -> Boolean
) {

//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    LaunchedEffect(Unit) {
//        if (!isNetworkAvailable(context)) {
//            coroutineScope.launch {
//                snackbarHostState.showSnackbar(
//                    message = "No internet connection",
//                    actionLabel = "Open settings"
//                ).let { result ->
//                    if (result == SnackbarResult.ActionPerformed) {
//                        context.openNetworkSettings()
//                    }
//                }
//            }
//        }
//    }

    LocationCheckScreen()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            //42.73402187319283, 23.29990188407221
            LatLng(lat, lng),
            14f
        )
    }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState) {
        Marker(
            state = remember {
                MarkerState(
                    position = LatLng(lat, lng),
                )
            },
            title = markerTitle,
            onClick = { marker ->
                onPinClicked(marker)
            }
        )
    }
}