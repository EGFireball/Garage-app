package com.idimi.garage.datamodel.model

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.idimi.garage.datamodel.RoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

//name, make, model, year, VIN, fuel type).
@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val model: String,
    val make: String,
    val year: Int,
    @ColumnInfo(name = "VIN")
    val vin: String,
    val fuelType: FuelType,
    var iconURL: String? = null
)

@Serializable
enum class FuelType(type: Int) {
    NONE(-1), PETROL(0), DIESEL(1), ELECTRIC(2), HYBRID(3)
}