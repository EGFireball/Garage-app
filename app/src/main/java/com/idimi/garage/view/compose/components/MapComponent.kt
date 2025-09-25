package com.idimi.garage.view.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.idimi.garage.datamodel.model.Place

@Composable
fun MapElement(
    modifier: Modifier,
    places: List<Place>,
    onPinClicked: (Place) -> Boolean
) {
    LocationCheckScreen()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            //42.73402187319283, 23.29990188407221
            LatLng(places[0].latitude, places[0].longtitude),
            if (places.size == 1) 14f else 13f
        )
    }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState) {
        places.forEach { place ->
            Marker(
                state = remember {
                    MarkerState(
                        position = LatLng(place.latitude, place.longtitude),
                    )
                },
                title = place.name,
                onClick = { _ ->
                    onPinClicked(place)
                }
            )
        }
    }
}