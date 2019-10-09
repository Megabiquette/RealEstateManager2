package com.albanfontaine.realestatemanager2.controllers

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.utils.Constants
import com.albanfontaine.realestatemanager2.utils.Utils
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utils.verifyStoragePermissions(this)
        configureToolbar()

        // Check if the activity was called from the MapFragment
        val extras: Bundle? = intent.extras
        if(extras != null){
            showPropertyCardFragment(extras.getLong(Constants.PROPERTY_ID))
        }else{
            showListFragment()
        }
    }

    private fun showListFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_base_frame_layout, ListFragment())
            .commit()
    }

    private fun showPropertyCardFragment(id: Long){
        val bundle = Bundle()
        bundle.putLong(Constants.PROPERTY_ID, id)
        val propertyCardFragment = PropertyCardFragment()
        propertyCardFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_base_frame_layout, propertyCardFragment)
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
