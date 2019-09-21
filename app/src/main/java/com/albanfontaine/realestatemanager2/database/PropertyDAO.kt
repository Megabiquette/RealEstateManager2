package com.albanfontaine.realestatemanager2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.albanfontaine.realestatemanager2.models.Property

@Dao
interface PropertyDAO{
    @Query("SELECT * FROM Property")
    fun getProperties() : List<Property>

    @Query("SELECT * FROM Property WHERE id = :id")
    fun getProperty(id: Long): Property

    @Insert
    fun insertProperty(property: Property) : Long

    @Update
    fun updateProperty(property: Property) : Int
}