package com.albanfontaine.realestatemanager2.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import kotlinx.android.synthetic.main.toolbar.*

class SearchActivity : AppCompatActivity() {

    private var mFlat: Boolean = true
    private var mHouse: Boolean = true
    private var mLoft: Boolean = true
    private var mManor: Boolean = true
    private var mPriceMin: Int? = null
    private var mPriceMax: Int? = null
    private var mSurfaceMin: Int? = null
    private var mSurfaceMax: Int? = null
    private var mEntryDateFrom: Int? = null
    private var mEntryDateTo: Int? = null
    private var mSaleDateFrom: Int? = null
    private var mSaleDateTo: Int? = null
    private var mAvailable: Boolean = true
    private var mSold: Boolean = true
    private var mMediasMin: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureToolbar()

    }

    ///////////////////
    // CONFIGURATION //
    ///////////////////

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = getSupportActionBar()
        ab?.setDisplayHomeAsUpEnabled(true)
    }

}
