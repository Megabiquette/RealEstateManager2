package com.albanfontaine.realestatemanager2.Controllers

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.toolbar_add -> Log.e("menu", "add")
            R.id.toolbar_edit -> Log.e("menu", "edit")
            R.id.toolbar_search -> Log.e("menu", "search")
            else -> return true
        }
        return true
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
