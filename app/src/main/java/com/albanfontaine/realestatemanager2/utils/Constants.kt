package com.albanfontaine.realestatemanager2.utils

class Constants{
    companion object{
        const val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE  = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        const val GALLERY_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 200

        const val PROPERTY_ID = "property_id"
        
        const val MAP_URL_BEGINNING = "https://maps.googleapis.com/maps/api/staticmap?markers="
        const val MAP_URL_END = "&zoom=15&size=300x300&scale=3&key=AIzaSyADBmQ_3NHCQjEFjqwO4R8YSe3WUc5AwcI"
    }
}