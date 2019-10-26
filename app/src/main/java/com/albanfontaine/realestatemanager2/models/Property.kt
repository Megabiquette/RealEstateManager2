package com.albanfontaine.realestatemanager2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Property(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "price") val price: Int?,
    @ColumnInfo(name = "surface") val surface: Int?,
    @ColumnInfo(name = "room_number") val roomNumber: Int?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "neighborhood") val neighborhood: String?,
    @ColumnInfo(name = "points_of_interest") val pointsOfInterest: String?,
    @ColumnInfo(name = "available") val available: Boolean = true,
    @ColumnInfo(name = "market_entry_date") val marketEntryDate: Int?,
    @ColumnInfo(name = "sell_date") val sellDate: Int? = null,
    @ColumnInfo(name = "agent") val agent: String? )