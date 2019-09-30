package com.albanfontaine.realestatemanager2.controllers

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.utils.Utils
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utils.verifyStoragePermissions(this)
        configureToolbar()
        showListFragment()
    }

    private fun showListFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_base_frame_layout, ListFragment())
            .commit()
    }

    ///////////////////
    // CONFIGURATION //
    ///////////////////

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = getSupportActionBar()
        ab?.setDisplayHomeAsUpEnabled(false)
    }
}
