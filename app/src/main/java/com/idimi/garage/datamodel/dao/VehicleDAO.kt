package com.idimi.garage.datamodel.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.idimi.garage.datamodel.model.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vehicle: Vehicle): Long

    @Update
    suspend fun update(vehicle: Vehicle): Int

    @Query("SELECT * FROM vehicles")
    fun getAllVehicles(): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleByVin(id: Int): Vehicle
}