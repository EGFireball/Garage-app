package com.idimi.garage.repo

import com.google.gson.JsonArray
import com.idimi.garage.api.GarageAPI
import com.idimi.garage.datamodel.RoomDB
import com.idimi.garage.datamodel.model.Place
import com.idimi.garage.datamodel.model.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GarageRepo @Inject constructor(
    val garageAPI: GarageAPI,
    val db: RoomDB
) {

    suspend fun updatePoi(place: Place) {
        place.update(db)
    }

    suspend fun getAllVehicles(): Flow<List<Vehicle>> = withContext(Dispatchers.IO) {
        db.getVehicleDao().getAllVehicles()
    }

    suspend fun setVehicle(vehicle: Vehicle, forEdit: Boolean) = withContext(Dispatchers.IO) {
        if (forEdit) {
            db.getVehicleDao().update(vehicle)
        } else {
            db.getVehicleDao().insert(vehicle)
        }
    }

    suspend fun parsePoisAndPersistItIntoDatabase(poisJSONArray: JsonArray): List<Place> {
        val pois: MutableList<Place> = mutableListOf()
        for (i in 0 until poisJSONArray.size()) {
            val p = poisJSONArray[i].asJsonObject
            val locArray = p.get("loc").asJsonArray
            val place = Place(
                poiId = p.get("id").asInt,
                name = p.get("name").asString,
                url = p.get("url").asString,
                rating = if (!p.get("rating").isJsonNull) p.get("rating").asDouble.toFloat() else 0.0f,
                primaryCategoryDisplayName = p.get("primary_category_display_name").asString,
                url145 = p.get("v_145x145_url").asString,
                url320 = p.get("v_320x320_url").asString,
                longtitude = locArray[0].asDouble,
                latitude = locArray[1].asDouble,
                isFavorite = false
            )
            pois.add(place)
            updatePoi(place)
        }
        return pois
    }

    suspend fun changeFavoriteStateForPlace(isFavorite: Boolean, place: Place, updateUi: (Boolean) -> Unit) = withContext(Dispatchers.IO) {
        place.isFavorite = isFavorite
        place.update(db)
        val dbPoi = db.getPlaceDao().getPoiById(place.poiId)
        updateUi(dbPoi.isFavorite)
    }
}