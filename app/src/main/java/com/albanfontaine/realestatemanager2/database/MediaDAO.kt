package com.albanfontaine.realestatemanager2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.albanfontaine.realestatemanager2.models.Media

@Dao
interface MediaDAO {
    @Query("SELECT * FROM Media WHERE associated_property_id = :propertyId")
    fun getMedias(propertyId : Long) : List<Media>

    @Insert
    fun insertMedia(media: Media) : Long

    @Query("DELETE FROM Media WHERE associated_property_id = :propertyId")
    fun deleteAllMedias(propertyId: Long): Int
}