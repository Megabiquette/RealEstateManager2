package com.albanfontaine.realestatemanager2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Property(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "type") var type: String?,
    @ColumnInfo(name = "price") var price: String?,
    @ColumnInfo(name = "surface") var surface: String?,
    @ColumnInfo(name = "room_number") var roomNumber: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "address") var address: String?,
    @ColumnInfo(name = "points_of_interest") var pointsOfInterest: String?,
    @ColumnInfo(name = "available") var available: Boolean = true,
    @ColumnInfo(name = "market_entry_date") var marketEntryDate: String?,
    @ColumnInfo(name = "sell_date") var sellDate: String? = null,
    @ColumnInfo(name = "agent") var agent: String? )