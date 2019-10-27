package com.albanfontaine.realestatemanager2.database

import androidx.room.*
import com.albanfontaine.realestatemanager2.models.Property

@Dao
interface PropertyDAO{
    @Query("SELECT * FROM Property")
    fun getProperties() : List<Property>

    @Query("SELECT * FROM Property WHERE property_id = :id")
    fun getProperty(id: Long): Property

    @Insert
    fun insertProperty(property: Property) : Long

    @Update
    fun updateProperty(property: Property) : Int

}