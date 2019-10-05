package com.albanfontaine.realestatemanager2.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.StringBuilder

class SearchActivity : AppCompatActivity() {

    private lateinit var mFlat: CheckBox
    private lateinit var mHouse: CheckBox
    private lateinit var mLoft: CheckBox
    private lateinit var mManor: CheckBox
    private lateinit var mPriceMin: EditText
    private lateinit var mPriceMax: EditText
    private lateinit var mSurfaceMin: EditText
    private lateinit var mSurfaceMax: EditText
    private lateinit var mNeighborhood: EditText
    private lateinit var mPOIs: EditText
    private lateinit var mEntryDateFrom: EditText
    private lateinit var mEntryDateTo: EditText
    private lateinit var mSaleDateFrom: EditText
    private lateinit var mSaleDateTo: EditText
    private lateinit var mAvailable: CheckBox
    private lateinit var mSold: CheckBox
    private lateinit var mMediasMin: EditText

    /*
    private var mFlat: Boolean = true
    private var mHouse: Boolean = true
    private var mLoft: Boolean = true
    private var mManor: Boolean = true
    private var mPriceMin: Int? = null
    private var mPriceMax: Int? = null
    private var mSurfaceMin: Int? = null
    private var mSurfaceMax: Int? = null
    private var mNeighborhood: String? = null
    private var mPOIs: String? = null
    private var mEntryDateFrom: Int? = null
    private var mEntryDateTo: Int? = null
    private var mSaleDateFrom: Int? = null
    private var mSaleDateTo: Int? = null
    private var mAvailable: Boolean = true
    private var mSold: Boolean = true
    private var mMediasMin: Int? = null
    */

    private var mQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search_button_reset.setOnClickListener{ resetForm() }
        search_button_search.setOnClickListener{ launchSearch() }

        configViews()
        configureToolbar()
    }

    private fun launchSearch(){
        if(checkForm()){
            buildQuery()
        }
    }

    private fun buildQuery(){
        var sb: StringBuilder = StringBuilder()
        // Type
        //var listType: ArrayList<String>
        //if(mFlat.isChecked) { sb.append("type = Appartement") }
    }

    private fun checkForm(): Boolean{
        if(!mFlat.isChecked && !mHouse.isChecked && !mLoft.isChecked && !mManor.isChecked){
            Toast.makeText(baseContext, baseContext.resources.getString(R.string.search_check_type), Toast.LENGTH_LONG).show()
            return false
        }
        if(!mAvailable.isChecked && !mSold.isChecked){
            Toast.makeText(baseContext, baseContext.resources.getString(R.string.search_check_available), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun resetForm(){
        mFlat.isChecked = true
        mHouse.isChecked = true
        mLoft.isChecked = true
        mManor.isChecked = true
        mPriceMin.setText("")
        mPriceMax.setText("")
        mSurfaceMin.setText("")
        mSurfaceMax.setText("")
        mNeighborhood.setText("")
        mPOIs.setText("")
        mEntryDateFrom.setText("")
        mEntryDateTo.setText("")
        mSaleDateFrom.setText("")
        mSaleDateTo.setText("")
        mAvailable.isChecked = true
        mSold.isChecked = true
        mMediasMin.setText("")
    }

    ///////////////////
    // CONFIGURATION //
    ///////////////////

    private fun configViews(){
        mFlat = search_flat
        mHouse = search_house
        mLoft = search_loft
        mManor = search_manor
        mPriceMin = search_price_min
        mPriceMax = search_price_max
        mSurfaceMin = search_surface_min
        mSurfaceMax = search_surface_max
        mNeighborhood = search_neighborhood
        mPOIs = search_POIs
        mEntryDateFrom = search_entry_date_from
        mEntryDateTo = search_entry_date_to
        mSaleDateFrom = search_sale_date_from
        mSaleDateTo = search_sale_date_to
        mAvailable = search_available
        mSold = search_sold
        mMediasMin = search_min_medias
    }

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = getSupportActionBar()
        ab?.setDisplayHomeAsUpEnabled(true)
    }

}
