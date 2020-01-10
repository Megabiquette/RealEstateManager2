package com.albanfontaine.realestatemanager2.controllers

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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

        // Check if the activity was called from the SearchFragment
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        if(!resources.getBoolean(R.bool.isTablet)){
            val editProperty = menu.findItem(R.id.toolbar_edit)
            editProperty?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> return false
            R.id.toolbar_add -> startActivity(Intent(this, AddActivity::class.java))
            R.id.toolbar_edit -> return false
            R.id.toolbar_search -> startActivity(Intent(this, SearchActivity::class.java))
            R.id.toolbar_map -> startMapActivity()
            else -> return true
        }
        return true
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
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_base_frame_layout, listFragment)
            .commit()
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
                .replace(R.id.activity_base_frame_layout_right, propertyCardFragment)
                .commit()
        }
    }

    private fun startMapActivity(){
        val cm: ConnectivityManager = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Utils.isInternetAvailable(cm)){
            startActivity(Intent(this, MapActivity::class.java))
        }else{
            Toast.makeText(applicationContext, resources.getString(R.string.no_internet), Toast.LENGTH_LONG).show()
        }
    }

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(false)
    }
}
