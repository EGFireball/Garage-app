package com.idimi.garage.datamodel.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idimi.garage.datamodel.RoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Entity(tableName = "pois")
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("poiId")
    val poiId: Int,
    val name: String,
    val url: String,
    @ColumnInfo(name = "primary_category_display_name")
    val primaryCategoryDisplayName: String,
    val rating: Float,
    @ColumnInfo(name = "v_320x320_url")
    val url320: String,
    @ColumnInfo(name = "v_145x145_url")
    val url145: String,
    val longtitude: Double,
    val latitude: Double,
    @ColumnInfo("is_favorite")
    var isFavorite: Boolean
) {

    @Throws(Exception::class)
    suspend fun update(db: RoomDB): Int = withContext(Dispatchers.IO) {
        if (id == 0) {
            db.getPlaceDao().insert(this@Place)
            this@Place.id
        } else {
            db.getPlaceDao().update(this@Place)
            this@Place.id
        }
    }
}