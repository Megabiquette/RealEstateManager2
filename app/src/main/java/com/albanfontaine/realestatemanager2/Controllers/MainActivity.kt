package com.albanfontaine.realestatemanager2.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureToolbar()
        showFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val activityClass = when(item?.itemId){
            R.id.toolbar_add -> AddActivity::class.java
            R.id.toolbar_edit -> EditActivity::class.java
            R.id.toolbar_search -> SearchActivity::class.java
            else -> return true
        }
        val intent = Intent(this, activityClass)
        startActivity(intent)
        return true
    }

    private fun showFragment(){
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
