package com.albanfontaine.realestatemanager2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Property::class, parentColumns = arrayOf("id"), childColumns = arrayOf("propertyId"))))
data class Media(
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "property_id") val propertyId: Int)