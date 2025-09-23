package com.idimi.garage.datamodel

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idimi.garage.datamodel.dao.PlaceDAO
import com.idimi.garage.datamodel.dao.VehicleDAO
import com.idimi.garage.datamodel.model.Place
import com.idimi.garage.datamodel.model.Vehicle

@Database(entities = [Place::class, Vehicle::class], version = 1)
abstract class RoomDB: RoomDatabase() {
    abstract fun getVehicleDao(): VehicleDAO
    abstract fun getPlaceDao(): PlaceDAO
}