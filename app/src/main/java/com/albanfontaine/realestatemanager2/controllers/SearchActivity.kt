package com.albanfontaine.realestatemanager2.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Property
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.StringBuilder
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SearchActivity : AppCompatActivity() {

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
    private lateinit var mMediasMinForm: EditText

    // Query values
    /*private var mFlat: Boolean = true
    private var mHouse: Boolean = true
    private var mLoft: Boolean = true
    private var mManor: Boolean = true*/
    private lateinit var mTypes: String
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
    private lateinit var mAvailable: String
    //private var mSold: Boolean = true
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
            val db = AppDatabase.getInstance(baseContext)
            var properties: List<Property>
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute{
                properties = db?.propertyDAO()?.searchProperties(mTypes, mPriceMin, mPriceMax, mSurfaceMin, mSurfaceMax, mNeighborhood, mPOIs,
                    mEntryDateFrom, mEntryDateTo, mSaleDateFrom, mSaleDateTo, mAvailable, mMediasMin)!!
                Log.e("type", mTypes)
                Log.e("available", mAvailable)
                Log.e("quartier", mNeighborhood)
                Log.e("POIs", mPOIs)
                for(property: Property in properties){
                    Log.e("result", property.toString())
                }
            }
        }
    }

    private fun getForm(){
        val typesArray = ArrayList<String>()
        if(mFlatForm.isChecked){typesArray.add(baseContext.resources.getString(R.string.flat))}
        if(mHouseForm.isChecked){typesArray.add(baseContext.resources.getString(R.string.house))}
        if(mLoftForm.isChecked){typesArray.add(baseContext.resources.getString(R.string.loft))}
        if(mManorForm.isChecked){typesArray.add(baseContext.resources.getString(R.string.manor))}
        mTypes = typesArray.joinToString(",")

        if(!mPriceMinForm.text.toString().trim().equals("")){mPriceMin = mPriceMinForm.text.toString().toInt()}
        if(!mPriceMaxForm.text.toString().trim().equals("")){mPriceMax = mPriceMaxForm.text.toString().toInt()}
        if(!mSurfaceMinForm.text.toString().trim().equals("")){mSurfaceMin = mSurfaceMinForm.text.toString().toInt()}
        if(!mSurfaceMaxForm.text.toString().trim().equals("")){mSurfaceMax = mSurfaceMaxForm.text.toString().toInt()}
        mNeighborhood = "%" + mNeighborhoodForm.text.toString().trim() + "%"
        mPOIs = "%" + mPOIsForm.text.toString().trim() + "%"
        //if(!mNeighborhoodForm.text.toString().trim().equals("")){mNeighborhood = mNeighborhoodForm.text.toString()}
        //if(!mPOIsForm.text.toString().trim().equals("")){mPOIs = mPOIsForm.text.toString()}
        if(!mEntryDateFromForm.text.toString().trim().equals("")){mEntryDateFrom = mEntryDateFromForm.text.toString().toInt()}
        if(!mEntryDateToForm.text.toString().trim().equals("")){mEntryDateTo = mEntryDateToForm.text.toString().toInt()}
        if(!mSaleDateFromForm.text.toString().trim().equals("")){mSaleDateFrom = mSaleDateFromForm.text.toString().toInt()}
        if(!mSaleDateToForm.text.toString().trim().equals("")){mSaleDateTo = mSaleDateToForm.text.toString().toInt()}

        val availableArray = ArrayList<String>()
        if(mAvailableForm.isChecked){availableArray.add(baseContext.resources.getString(R.string.property_available))}
        if(mSoldForm.isChecked){availableArray.add(baseContext.resources.getString(R.string.property_sold))}
        mAvailable = availableArray.joinToString(",")

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
        mMediasMinForm = search_min_medias
    }

    private fun configureToolbar(){
        setSupportActionBar(toolbar as Toolbar)
        val ab : ActionBar? = getSupportActionBar()
        ab?.setDisplayHomeAsUpEnabled(true)
    }

}
