package com.albanfontaine.realestatemanager2.controllers

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.models.SearchQuery
import com.albanfontaine.realestatemanager2.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar.*

class SearchActivity : BaseActivity() {

    // Form views
    private lateinit var mFlatForm: CheckBox
    private lateinit var mHouseForm: CheckBox
    private lateinit var mLoftForm: CheckBox
    private lateinit var mManorForm: CheckBox
    private lateinit var mPriceMinForm: EditText
    private lateinit var mPriceMaxForm: EditText
    private lateinit var mSurfaceMinForm: EditText
    private lateinit var mSurfaceMaxForm: EditText
    private lateinit var mNeighborhoodForm: EditText
    private lateinit var mPOIsForm: EditText
    private lateinit var mEntryDateFromForm: EditText
    private lateinit var mEntryDateToForm: EditText
    private lateinit var mSaleDateFromForm: EditText
    private lateinit var mSaleDateToForm: EditText
    private lateinit var mAvailableForm: CheckBox
    private lateinit var mSoldForm: CheckBox
    private lateinit var mAgentForm: EditText
    private lateinit var mMediasMinForm: EditText

    // Query values
    private lateinit var mTypes: List<String>
    private var mPriceMin: Int = 0
    private var mPriceMax: Int = 999999999
    private var mSurfaceMin: Int = 0
    private var mSurfaceMax: Int = 999999999
    private lateinit var mNeighborhood: String
    private lateinit var mPOIs: String
    private var mEntryDateFrom: Int = 0
    private var mEntryDateTo: Int = 999999999
    private var mSaleDateFrom: Int = 0
    private var mSaleDateTo: Int = 999999999
    private lateinit var mAvailable: List<Int>
    private lateinit var mAgent: String
    private var mMediasMin: Int = 0

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
            getForm()
            val intent = Intent(this, MainActivity::class.java)
            val gson = Gson()

            val searchQuery = SearchQuery(mTypes, mPriceMin, mPriceMax, mSurfaceMin, mSurfaceMax, mNeighborhood, mPOIs, mEntryDateFrom, mEntryDateTo, mSaleDateFrom, mSaleDateTo, mAvailable, mAgent, mMediasMin)
            val searchQueryType = object: TypeToken<SearchQuery>(){}.type

            val searchQueryJSON = gson.toJson(searchQuery, searchQueryType)
            intent.putExtra(Constants.SEARCH_QUERY, searchQueryJSON)

            startActivity(intent)
        }
    }

    private fun getForm(){
        val typeList = mutableListOf<String>()
        if(mFlatForm.isChecked){typeList.add(baseContext.resources.getString(R.string.flat))}
        if(mHouseForm.isChecked){typeList.add(baseContext.resources.getString(R.string.house))}
        if(mLoftForm.isChecked){typeList.add(baseContext.resources.getString(R.string.loft))}
        if(mManorForm.isChecked){typeList.add(baseContext.resources.getString(R.string.manor))}
        mTypes = typeList

        if(!mPriceMinForm.text.toString().trim().equals("")){mPriceMin = mPriceMinForm.text.toString().toInt()}
        if(!mPriceMaxForm.text.toString().trim().equals("")){mPriceMax = mPriceMaxForm.text.toString().toInt()}
        if(!mSurfaceMinForm.text.toString().trim().equals("")){mSurfaceMin = mSurfaceMinForm.text.toString().toInt()}
        if(!mSurfaceMaxForm.text.toString().trim().equals("")){mSurfaceMax = mSurfaceMaxForm.text.toString().toInt()}
        mNeighborhood = "%" + mNeighborhoodForm.text.toString().trim() + "%"
        mPOIs = "%" + mPOIsForm.text.toString().trim() + "%"
        if(!mEntryDateFromForm.text.toString().trim().equals("")){mEntryDateFrom = mEntryDateFromForm.text.toString().toInt()}
        if(!mEntryDateToForm.text.toString().trim().equals("")){mEntryDateTo = mEntryDateToForm.text.toString().toInt()}
        if(!mSaleDateFromForm.text.toString().trim().equals("")){mSaleDateFrom = mSaleDateFromForm.text.toString().toInt()}
        if(!mSaleDateToForm.text.toString().trim().equals("")){mSaleDateTo = mSaleDateToForm.text.toString().toInt()}

        val availableList = mutableListOf<Int>()
        if(mAvailableForm.isChecked){availableList.add(1)}
        if(mSoldForm.isChecked){availableList.add(0)}
        mAvailable = availableList

        mAgent = "%" + mAgentForm.text.toString().trim() + "%"
        if(!mMediasMinForm.text.toString().trim().equals("")){mMediasMin = mMediasMinForm.text.toString().toInt()}
    }

    private fun checkForm(): Boolean{
        if(!mFlatForm.isChecked && !mHouseForm.isChecked && !mLoftForm.isChecked && !mManorForm.isChecked){
            Toast.makeText(baseContext, baseContext.resources.getString(R.string.search_check_type), Toast.LENGTH_LONG).show()
            return false
        }
        if(!mAvailableForm.isChecked && !mSoldForm.isChecked){
            Toast.makeText(baseContext, baseContext.resources.getString(R.string.search_check_available), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun resetForm(){
        mFlatForm.isChecked = true
        mHouseForm.isChecked = true
        mLoftForm.isChecked = true
        mManorForm.isChecked = true
        mPriceMinForm.setText("")
        mPriceMaxForm.setText("")
        mSurfaceMinForm.setText("")
        mSurfaceMaxForm.setText("")
        mNeighborhoodForm.setText("")
        mPOIsForm.setText("")
        mEntryDateFromForm.setText("")
        mEntryDateToForm.setText("")
        mSaleDateFromForm.setText("")
        mSaleDateToForm.setText("")
        mAvailableForm.isChecked = true
        mSoldForm.isChecked = true
        mAgentForm.setText("")
        mMediasMinForm.setText("")
    }

    ///////////////////
    // CONFIGURATION //
    ///////////////////

    private fun configViews(){
        mFlatForm = search_flat
        mHouseForm = search_house
        mLoftForm = search_loft
        mManorForm = search_manor
        mPriceMinForm = search_price_min
        mPriceMaxForm = search_price_max
        mSurfaceMinForm = search_surface_min
        mSurfaceMaxForm = search_surface_max
        mNeighborhoodForm = search_neighborhood
        mPOIsForm = search_POIs
        mEntryDateFromForm = search_entry_date_from
        mEntryDateToForm = search_entry_date_to
        mSaleDateFromForm = search_sale_date_from
        mSaleDateToForm = search_sale_date_to
        mAvailableForm = search_available
        mSoldForm = search_sold
        mAgentForm = search_agent
        mMediasMinForm = search_min_medias
    }

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = getSupportActionBar()
        ab?.setDisplayHomeAsUpEnabled(true)
    }

}
