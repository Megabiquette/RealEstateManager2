package com.albanfontaine.realestatemanager2.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Property::class, parentColumns = arrayOf("property_id"), childColumns = arrayOf("associated_property_id"))])
data class Media(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="media_id") val id: Long,
    @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "associated_property_id", index = true) val associatedPropertyId: Long?)