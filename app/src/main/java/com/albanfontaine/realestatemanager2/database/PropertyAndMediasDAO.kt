package com.albanfontaine.realestatemanager2.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.albanfontaine.realestatemanager2.models.PropertyAndMedias

@Dao
interface PropertyAndMediasDAO {
	@Transaction
	@Query("SELECT * FROM Property")
	fun getProperties() : List<PropertyAndMedias>

	@Transaction
	@Query("SELECT * FROM Property WHERE property_id = :id")
	fun getProperty(id: Long): PropertyAndMedias

	@Transaction
	@Query("SELECT *, COUNT(media.associated_property_id) " +
			"FROM Property " +
			"INNER JOIN media ON media.associated_property_id = property.property_id " +
			"WHERE type IN (:type) " +
			"AND ((price BETWEEN :priceMin AND :priceMax) OR price IS NULL) " +
			"AND ((surface BETWEEN :surfaceMin AND :surfaceMax) OR surface IS NULL)" +
			"AND neighborhood LIKE :neighborhood " +
			"AND city LIKE :city " +
			"AND points_of_interest LIKE :POIs " +
			"AND ((market_entry_date BETWEEN :entryDateFrom AND :entryDateTo) OR market_entry_date IS NULL) " +
			"AND ((sell_date BETWEEN :sellDateFrom AND :sellDateTo) OR sell_date IS NULL) " +
			"AND available IN (:available) " +
			"AND agent LIKE :agent " +
			"GROUP BY property.property_id HAVING COUNT(media.associated_property_id) >= :mediaMin")
	fun searchProperties(type: List<String>, priceMin: Int, priceMax: Int, surfaceMin: Int, surfaceMax: Int, neighborhood: String, city: String, POIs: String,
						 entryDateFrom: Int, entryDateTo: Int, sellDateFrom: Int, sellDateTo: Int, available: List<Int>, agent: String, mediaMin: Int) : List<PropertyAndMedias>


}