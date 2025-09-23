package com.idimi.garage

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.idimi.garage.api.GarageAPI
import com.idimi.garage.api.pojo.PoisPojo
import com.idimi.garage.datamodel.RoomDB
import com.idimi.garage.datamodel.dao.PlaceDAO
import com.idimi.garage.datamodel.model.Place
import com.idimi.garage.repo.GarageRepo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import retrofit2.Response

class PlaceServiceTests {

    val api: GarageAPI = mockk()
    val db: RoomDB = mockk()
    val placeDao = mockk<PlaceDAO>()

    @Test
    fun placeServiceTest() {
        runBlocking {
            val repo = GarageRepo(garageAPI = api, db = db)
            val mockPlace = Place(
                id = 0,
                poiId = 34567,
                name = "My Place",
                url = "",
                primaryCategoryDisplayName = "",
                rating = 5f,
                url320 = "",
                url145 = "",
                latitude = 35.0,
                longtitude = 75.0,
                isFavorite = false
            )
            val mockPlacePojo = PoisPojo(
                id = 34567,
                name = "My Place",
                url = "",
                primaryCategoryDisplayName = "",
                rating = 5.0,
                v320x320Url = "",
                v145x145Url = "",
                loc = arrayListOf(75.0, 35.0)
            )

            every { db.getPlaceDao() } returns placeDao

            val jsonArray = JsonArray()
            val jsonItem = Gson().toJsonTree(mockPlacePojo).asJsonObject
            jsonArray.add(jsonItem)
            val responseJson = JsonObject()
            responseJson.add("pois", jsonArray)

            coEvery { api.getPois() } returns Response.success(responseJson)
            coEvery { placeDao.insert(any()) } returns Unit

            val results = api.getPois()
            val placesJSONArray = results.body()!!.get("pois").asJsonArray
            val places = repo.parsePoisAndPersistItIntoDatabase(placesJSONArray)

            assertThat(places.size).isEqualTo(1)
            assertThat(places[0].poiId).isEqualTo(mockPlace.poiId)
            assertThat(places[0].latitude).isEqualTo(mockPlace.latitude)
            assertThat(places[0].longtitude).isEqualTo(mockPlace.longtitude)
        }
    }
}