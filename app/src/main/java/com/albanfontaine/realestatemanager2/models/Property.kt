package com.albanfontaine.realestatemanager2.models

data class Property(var type: String, var price: Int, var surface: Double, var roomNumber: Int, var description: String, var media: Array<Media>,
                    var address: String, var pointsOfInterest: String?, var available: Boolean = true, var marketEntryDate:String,
                    var sellDate: String? = null, var agent: String)