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

    @Query("SELECT *, COUNT(media.property_id) FROM Property INNER JOIN media ON media.property_id = property.id WHERE type IN (:type) AND price BETWEEN :priceMin AND :priceMax " +
            "AND surface BETWEEN :surfaceMin AND :surfaceMax AND neighborhood LIKE :neighborhood AND points_of_interest LIKE :POIs " +
            "AND market_entry_date BETWEEN :entryDateMin AND :entryDateMax AND sell_date BETWEEN :sellDateMin AND :sellDateMax " +
            "AND available IN (:available) GROUP BY media.property_id HAVING COUNT(media.property_id) > :mediaMin")
    fun searchProperties(type: List<String>, priceMin: Int, priceMax: Int, surfaceMin: Int, surfaceMax: Int, neighborhood: String, POIs: String,
                         entryDateMin: Int, entryDateMax: Int, sellDateMin: Int, sellDateMax: Int, available: List<Int>, mediaMin: Int) : List<Property>

    @Query("SELECT * FROM Property WHERE neighborhood LIKE :neighborhood")
    fun searchTest(neighborhood: String): List<Property>
}