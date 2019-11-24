package com.albanfontaine.realestatemanager2.controllers

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albanfontaine.realestatemanager2.R

open class BaseActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if (resources.getBoolean(R.bool.isTablet)){
			this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
		}else{
			this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
		}
	}
}
