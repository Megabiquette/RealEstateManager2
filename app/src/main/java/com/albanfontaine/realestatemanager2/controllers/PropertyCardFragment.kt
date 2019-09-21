package com.albanfontaine.realestatemanager2.controllers


import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Media
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.utils.Constants
import kotlinx.android.synthetic.main.fragment_property_card.*
import java.util.concurrent.Executors

class PropertyCardFragment : Fragment() {
    private lateinit var property: Property
    private lateinit var medias: List<Media>

    private lateinit var mDescription: TextView
    private lateinit var mSurface: TextView
    private lateinit var mNumberRooms: TextView
    private lateinit var mLocation: TextView
    private lateinit var mPOIs: TextView
    private lateinit var mEntryDate: TextView
    private lateinit var mAvailable: TextView
    private lateinit var mSaleDate: TextView
    private lateinit var mAgent: TextView
    private lateinit var mPrice: TextView
    private lateinit var mDescriptionLayout: LinearLayout
    private lateinit var mSurfaceLayout: LinearLayout
    private lateinit var mNumberRoomsLayout: LinearLayout
    private lateinit var mLocationLayout: LinearLayout
    private lateinit var mPOIsLayout: LinearLayout
    private lateinit var mEntryDateLayout: LinearLayout
    private lateinit var mSaleDateLayout: LinearLayout
    private lateinit var mAgentLayout: LinearLayout
    private lateinit var mPriceLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val id: Long? = arguments?.getLong(Constants.PROPERTY_ID)
        if(id != null){
            getProperty(id)
        }
        return inflater.inflate(R.layout.fragment_property_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configViews()
    }

    private fun getProperty(id: Long){
        val db = AppDatabase.getInstance(requireContext())
        val executor = Executors.newSingleThreadExecutor()
        executor.execute{
            property = db?.propertyDAO()?.getProperty(id)!!
            medias = db.mediaDAO().getMedias(id)
            activity?.runOnUiThread{
                setupPropertyCard()
            }
        }
    }

    private fun setupPropertyCard(){
        // Populate the fields. If a field is null, hide its layout
        if(property.description != null){ mDescription.text = property.description} else{ mDescriptionLayout.visibility = View.GONE}
        if(property.surface != null){ mSurface.text = property.surface} else{ mSurfaceLayout.visibility = View.GONE}
        if(property.roomNumber != null){ mNumberRooms.text = property.roomNumber} else{ mNumberRoomsLayout.visibility = View.GONE}
        if(property.address != null){ mLocation.text = property.address} else{ mLocationLayout.visibility = View.GONE}
        if(property.pointsOfInterest != null){ mPOIs.text = property.pointsOfInterest} else{ mPOIsLayout.visibility = View.GONE}
        if(property.marketEntryDate != null){ mEntryDate.text = property.marketEntryDate} else{ mEntryDateLayout.visibility = View.GONE}
        if(property.available){ mSaleDateLayout.visibility = View.GONE}else{ mAvailable.text = activity?.resources?.getString(R.string.property_sold)}
        if(property.sellDate != null){mSaleDate.text = property.sellDate} else{ mSaleDateLayout.visibility = View.GONE}
        if(property.agent != null){ mAgent.text = property.agent} else{ mAgentLayout.visibility = View.GONE}
        if(property.price != null){ mPrice.text = property.price} else{ mPriceLayout.visibility = View.GONE}
    }

    private fun configViews(){
        mDescription = property_card_description
        mSurface = property_card_surface
        mNumberRooms = property_card_numberRooms
        mLocation = property_card_location
        mPOIs = property_card_POIs
        mEntryDate = property_card_entry_date
        mAvailable = property_card_available
        mSaleDate = property_card_sale_date
        mAgent = property_agent
        mPrice = property_card_price
        mDescriptionLayout = property_card_description_layout
        mSurfaceLayout = property_card_surface_layout
        mNumberRoomsLayout = property_card_numberRooms_layout
        mLocationLayout = property_card_location_layout
        mPOIsLayout = property_card_POIs_layout
        mEntryDateLayout = property_card_entry_date_layout
        mSaleDateLayout = property_card_sale_date_layout
        mAgentLayout = property_card_agent_layout
        mPriceLayout = property_card_price_layout
    }
}
