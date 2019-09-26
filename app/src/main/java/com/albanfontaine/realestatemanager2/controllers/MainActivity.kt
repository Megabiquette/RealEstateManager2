package com.albanfontaine.realestatemanager2.controllers

import android.content.Intent
import android.os.Bundle
import android.view.*
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
        showListFragment()
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu)
        if (!resources.getBoolean(R.bool.isTablet)){
            val editProperty = menu?.findItem(R.id.toolbar_edit)
            editProperty?.isVisible = false
        }
        return true
    }
    */

    /*
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val activityClass = when(item?.itemId){
            R.id.toolbar_add -> AddActivity::class.java
            R.id.toolbar_edit ->
            R.id.toolbar_search -> SearchActivity::class.java
            else -> return true
        }
        val intent = Intent(this, activityClass)
        startActivity(intent)
        return true
    }
     */

    private fun showListFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_base_frame_layout, ListFragment())
            .commit()
    }

/*
    private fun showCardFragment(id: Long){
        val bundle = Bundle()
        bundle.putLong(Constants.PROPERTY_ID, id)
        val propertyCardFragment = PropertyCardFragment()
        propertyCardFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_base_frame_layout, propertyCardFragment)
            .commit()
    } */

    ///////////////////
    // CONFIGURATION //
    ///////////////////

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = getSupportActionBar()
        ab?.setDisplayHomeAsUpEnabled(false)
    }
}
