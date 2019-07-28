package com.albanfontaine.realestatemanager2.Models

data class Property(val type: String, val price: Int, val surface: Double, val roomNumber: Int, val description: String, val photos: Array<Photo>,
                    val address: String, val pointsOfInterest: String, var available: Boolean, val marketEntryDate:String,
                    val sellDate: String, val agent: User)