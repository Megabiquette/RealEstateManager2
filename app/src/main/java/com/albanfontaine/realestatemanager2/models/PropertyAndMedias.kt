package com.albanfontaine.realestatemanager2.models

import androidx.room.Embedded
import androidx.room.Relation

class PropertyAndMedias{
	@Embedded
	var property: Property? = null

	@Relation(parentColumn = "id", entityColumn = "property_id")
	var medias: List<Media> = ArrayList()
}