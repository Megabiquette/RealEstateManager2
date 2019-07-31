package com.albanfontaine.realestatemanager2.Models

data class Property(var type: String, var price: Int, var surface: Double, var roomNumber: Int, var description: String, var photos: Array<Photo>,
                    var address: String, var pointsOfInterest: String?, var available: Boolean = true, var marketEntryDate:String,
                    var sellDate: String? = null, var agent: String)