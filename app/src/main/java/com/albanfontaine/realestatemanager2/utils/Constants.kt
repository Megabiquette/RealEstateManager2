package com.albanfontaine.realestatemanager2.utils

class Constants{
    companion object{
        const val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE  = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        const val GALLERY_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 200
    }
}