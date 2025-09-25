package com.idimi.garage.view.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.idimi.garage.datamodel.model.Place
import com.idimi.garage.datamodel.model.Vehicle
import com.idimi.garage.repo.GarageRepo
import com.idimi.garage.sysflows.collectPlacesAsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Modifier.PRIVATE
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GarageViewModel @Inject constructor(
    private val garageRepo: GarageRepo
): ViewModel() {
    val allPlaces: MutableList<Place> = mutableListOf()

    private val _placesStateFlow = MutableStateFlow<List<Place>>(emptyList())
    val placesStateFlow = _placesStateFlow.asStateFlow()

    private val _vehicleStateFlow = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicleStateFlow = _vehicleStateFlow.asStateFlow()

    private var currentVehicleImage: MutableState<String?> = mutableStateOf(null)

    private fun clearCurrentVehicleImage() {
        currentVehicleImage.value = null
    }

    suspend fun getAllPlaces() = withContext(Dispatchers.IO) {
        collectPlacesAsFlow(garageRepo).collect { result ->
            if (result.data != null && result.data.isNotEmpty()) {
                allPlaces.addAll(result.data)
                _placesStateFlow.emit(result.data)
            }
        }
        allPlaces
    }

    fun showOnlyFavoritePlaces() {
        val favorites = allPlaces.filter { it.isFavorite }.toSet()
        _placesStateFlow.value = favorites.toList()
    }

    fun showAllPlaces() {
        _placesStateFlow.value = allPlaces
    }

    suspend fun getAllVehicles() = withContext(Dispatchers.IO) {
        garageRepo.getAllVehicles().collect {
            _vehicleStateFlow.emit(it)
        }
    }

    suspend fun addVehicleToGarage(vehicle: Vehicle, forEdit: Boolean) {
        if (currentVehicleImage.value != null) {
            vehicle.iconURL = currentVehicleImage.value
            clearCurrentVehicleImage()
        }
        garageRepo.setVehicle(vehicle = vehicle, forEdit)
    }

    fun saveImageToAppStorage(context: Context, uri: Uri): String {
        val file = File(context.filesDir, "${UUID.randomUUID()}.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)

        inputStream.use { input ->
            FileOutputStream(file).use { outputStream ->
                input?.copyTo(outputStream)
            }
        }

        currentVehicleImage.value = file.absolutePath

        return file.absolutePath
    }

    suspend fun changeFavoriteStateForPlace(isFavorite: Boolean, place: Place, updateUi: (Boolean) -> Unit) {
        garageRepo.changeFavoriteStateForPlace(
            isFavorite, place
        ) { favorite ->
            updateUi(favorite)
        }
    }

    fun isItemFavorite(place: Place, isFavorite: (Boolean) -> Unit) {
        isFavorite(_placesStateFlow.value.firstOrNull { it.poiId == place.poiId }?.isFavorite == true)
    }
}