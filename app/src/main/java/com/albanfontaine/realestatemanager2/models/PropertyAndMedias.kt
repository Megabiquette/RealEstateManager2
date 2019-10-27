package com.albanfontaine.realestatemanager2.models

import androidx.room.Embedded
import androidx.room.Relation

class PropertyAndMedias{
	@Embedded
	var property: Property? = null

	@Relation(parentColumn = "property_id", entityColumn = "associated_property_id")
	var medias: List<Media> = ArrayList()
}