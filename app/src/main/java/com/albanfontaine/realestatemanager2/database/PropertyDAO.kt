package com.albanfontaine.realestatemanager2.database

import androidx.room.*
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

    @Query("SELECT *, COUNT(media.property_id) " +
            "FROM Property " +
            "INNER JOIN media ON media.property_id = property.id " +
            "WHERE type IN (:type) " +
            "AND (price BETWEEN :priceMin AND :priceMax OR (CASE WHEN :priceMin = 0 AND :priceMax = 999999999 THEN price IS NULL ELSE price IS NOT NULL END)) " +
            "AND (surface BETWEEN :surfaceMin AND :surfaceMax OR (CASE WHEN :surfaceMin = 0 AND :surfaceMax = 999999999 THEN surface IS NULL ELSE surface IS NOT NULL END)) " +
			"AND neighborhood LIKE :neighborhood " +
			"AND points_of_interest LIKE :POIs " +
            "AND (market_entry_date BETWEEN :entryDateFrom AND :entryDateTo OR (CASE WHEN :entryDateFrom = 0 AND :entryDateTo = 999999999 THEN market_entry_date IS NULL ELSE market_entry_date IS NOT NULL END)) " +
			"AND (sell_date BETWEEN :sellDateFrom AND :sellDateTo OR (CASE WHEN :sellDateFrom = 0 AND :sellDateTo = 999999999 THEN sell_date IS NULL ELSE sell_date IS NOT NULL END)) " +
            "AND available IN (:available) " +
			"AND agent LIKE :agent " +
			"GROUP BY media.property_id HAVING COUNT(media.property_id) >= :mediaMin")
    fun searchProperties(type: List<String>, priceMin: Int, priceMax: Int, surfaceMin: Int, surfaceMax: Int, neighborhood: String, POIs: String,
                          entryDateFrom: Int, entryDateTo: Int, sellDateFrom: Int, sellDateTo: Int, available: List<Int>, agent: String, mediaMin: Int) : List<Property>
	@Transaction
	@Query("SELECT *, COUNT(media.property_id) FROM Property INNER JOIN media ON media.property_id = property.id WHERE type IN (:type) AND (price BETWEEN :priceMin AND :priceMax OR (CASE WHEN :priceMin = 0 AND :priceMax = 999999999 THEN price IS NULL ELSE price IS NOT NULL END)) AND (surface BETWEEN :surfaceMin AND :surfaceMax OR (CASE WHEN :surfaceMin = 0 AND :surfaceMax = 999999999 THEN surface IS NULL ELSE surface IS NOT NULL END)) AND neighborhood LIKE :neighborhood AND points_of_interest LIKE :POIs AND (market_entry_date BETWEEN :entryDateFrom AND :entryDateTo OR (CASE WHEN :entryDateFrom = 0 AND :entryDateTo = 999999999 THEN market_entry_date IS NULL ELSE market_entry_date IS NOT NULL END)) AND (sell_date BETWEEN :sellDateFrom AND :sellDateTo OR (CASE WHEN :sellDateFrom = 0 AND :sellDateTo = 999999999 THEN sell_date IS NULL ELSE sell_date IS NOT NULL END)) AND available IN (:available) AND agent LIKE :agent GROUP BY property.id HAVING COUNT(media.property_id) >= :mediaMin")
	fun searchProperties2(type: List<String>, priceMin: Int, priceMax: Int, surfaceMin: Int, surfaceMax: Int, neighborhood: String, POIs: String,
						  entryDateFrom: Int, entryDateTo: Int, sellDateFrom: Int, sellDateTo: Int, available: List<Int>, agent: String, mediaMin: Int) : List<Property>
}