package com.albanfontaine.realestatemanager2.controllers

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.utils.Constants
import com.albanfontaine.realestatemanager2.utils.Utils
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utils.verifyStoragePermissions(this)
        configureToolbar()

        // Check if the activity was called from the MapFragment
        val extras: Bundle? = intent.extras

        if(extras?.getString(Constants.SEARCH_QUERY) != null){
            // Show properties from search query
            val searchQueryJSON = intent.extras!!.getString(Constants.SEARCH_QUERY)
            showListFragmentFromSearch(searchQueryJSON!!)
        }else if(extras?.getLong(Constants.PROPERTY_ID) != null && extras.getLong(Constants.PROPERTY_ID) != 0.toLong()){
            // Show property card
            showPropertyCardFragment(extras.getLong(Constants.PROPERTY_ID))
        }else{
            // Show all properties
            showListFragment()
        }
    }

    private fun showListFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_base_frame_layout, ListFragment())
            .commit()
    }

    private fun showListFragmentFromSearch(searchQueryJSON: String){
        val bundle = Bundle()
        bundle.putString(Constants.SEARCH_QUERY, searchQueryJSON)
        val listFragment = ListFragment()
        listFragment.arguments = bundle
    }

    private fun showPropertyCardFragment(id: Long){
        val bundle = Bundle()
        bundle.putLong(Constants.PROPERTY_ID, id)
        val propertyCardFragment = PropertyCardFragment()
        propertyCardFragment.arguments = bundle
        if (!resources.getBoolean(R.bool.isTablet)){
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_base_frame_layout, propertyCardFragment)
                .commit()
        }else{
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_base_frame_layout_right, propertyCardFragment)
                .commit()
        }
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
