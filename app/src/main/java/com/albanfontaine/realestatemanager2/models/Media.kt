package com.albanfontaine.realestatemanager2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Property::class, parentColumns = arrayOf("id"), childColumns = arrayOf("property_id"))])
data class Media(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id") val id: Long,
    @ColumnInfo(name = "uri") val url: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "property_id", index = true) var propertyId: Int?)