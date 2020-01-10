package com.albanfontaine.realestatemanager2.models

class SearchQuery (val types: List<String>, val priceMin: Int, val priceMax: Int, val surfaceMin: Int, val surfaceMax: Int, val neighborhood: String, val city: String, val POIs: String,
				   val entryDateFrom: Int, val entryDateTo: Int, val saleDateFrom: Int, val saleDateTo: Int, val available: List<Int>, val agent: String, val mediaMin: Int)
