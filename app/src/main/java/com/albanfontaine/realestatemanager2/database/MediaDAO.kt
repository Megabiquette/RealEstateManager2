package com.albanfontaine.realestatemanager2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.albanfontaine.realestatemanager2.models.Media

@Dao
interface MediaDAO {
    @Query("SELECT * FROM Media WHERE property_id = :propertyId")
    fun getMedias(propertyId : Long) : LiveData<List<Media>>

    @Insert
    fun insertMedia(media: Media) : Long
}