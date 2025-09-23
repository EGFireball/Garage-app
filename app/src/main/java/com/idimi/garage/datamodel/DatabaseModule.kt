package com.idimi.garage.datamodel

import android.content.Context
import androidx.room.Room
import com.idimi.garage.datamodel.dao.PlaceDAO
import com.idimi.garage.datamodel.dao.VehicleDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext appContext: Context): RoomDB =
        Room.databaseBuilder(
            appContext,
            RoomDB::class.java,
            "garage_db"
        ).build()

    @Provides
    fun provideVehicleDao(db: RoomDB): VehicleDAO =
        db.getVehicleDao()

    @Provides
    fun providePoiDao(db: RoomDB): PlaceDAO =
        db.getPlaceDao()
}