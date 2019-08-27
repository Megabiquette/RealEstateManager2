package com.albanfontaine.realestatemanager2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Property(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "surface") var surface: Double,
    @ColumnInfo(name = "room_number") var roomNumber: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "media") var media: Array<Media>,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "points_of_interest") var pointsOfInterest: String?,
    @ColumnInfo(name = "available") var available: Boolean = true,
    @ColumnInfo(name = "market_entry_date") var marketEntryDate:String,
    @ColumnInfo(name = "sell_date") var sellDate: String? = null,
    @ColumnInfo(name = "agent") var agent: String)