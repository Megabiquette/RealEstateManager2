package com.albanfontaine.realestatemanager2.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import kotlinx.android.synthetic.main.toolbar.*

class MapActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_map)

		configureToolbar()
	}

	private fun configureToolbar(){
		setSupportActionBar(toolbar as Toolbar)
		val ab : ActionBar? = getSupportActionBar()
		ab?.setDisplayHomeAsUpEnabled(true)
	}
}
