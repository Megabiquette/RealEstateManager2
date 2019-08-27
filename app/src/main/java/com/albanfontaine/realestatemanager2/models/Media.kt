package com.albanfontaine.realestatemanager2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Property::class, parentColumns = arrayOf("id"), childColumns = arrayOf("propertyId"))))
data class Media(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "uri") val url: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "property_id") val propertyId: Int)