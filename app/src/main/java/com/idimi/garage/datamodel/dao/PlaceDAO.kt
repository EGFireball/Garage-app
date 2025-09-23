package com.idimi.garage.datamodel.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.idimi.garage.datamodel.model.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDAO {

    @Insert
    suspend fun insert(place: Place)

    @Update
    suspend fun update(place: Place)

    @Query("SELECT * FROM pois")
    fun getAllPois(): Flow<List<Place>>

    @Query("SELECT * FROM pois WHERE poiId = :poiId")
    suspend fun getPoiById(poiId: Int): Place
}