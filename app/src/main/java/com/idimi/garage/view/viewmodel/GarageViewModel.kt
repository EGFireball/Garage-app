package com.idimi.garage.view.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.idimi.garage.datamodel.model.Place
import com.idimi.garage.datamodel.model.Vehicle
import com.idimi.garage.repo.GarageRepo
import com.idimi.garage.sysflows.collectPoisAsFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GarageViewModel @Inject constructor(
    private val garageRepo: GarageRepo
): ViewModel() {

    private val allPois: MutableList<Place> = mutableListOf()

    private val _poisStateFlow = MutableStateFlow<List<Place>>(emptyList())
    val poisStateFlow = _poisStateFlow.asStateFlow()

    private val _vehicleStateFlow = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicleStateFlow = _vehicleStateFlow.asStateFlow()

    private var currentVehicleImage: MutableState<String?> = mutableStateOf(null)

    private fun clearCurrentVehicleImage() {
        currentVehicleImage.value = null
    }

    suspend fun getAllPois() = withContext(Dispatchers.IO) {
        collectPoisAsFlow(garageRepo, this).collect { result ->
            if (result.data != null && result.data.isNotEmpty()) {
                allPois.addAll(result.data)
                _poisStateFlow.emit(result.data)
            }
        }
    }

    fun showOnlyFavoritePlaces() {
        val temp = allPois
        val favorites = temp.filter { it.isFavorite }
        _poisStateFlow.value = favorites
    }

    fun showAllPlaces() {
        _poisStateFlow.value = allPois
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
        isFavorite(_poisStateFlow.value.first { it.poiId == place.poiId }.isFavorite)
    }
}