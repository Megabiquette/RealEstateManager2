package com.albanfontaine.realestatemanager2.controllers

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Media
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.utils.Constants
import com.albanfontaine.realestatemanager2.utils.Utils
import com.albanfontaine.realestatemanager2.views.MediaAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_property_card.*
import java.util.concurrent.Executors

class PropertyCardFragment : Fragment() {
    private lateinit var mProperty: Property
    private lateinit var mMedias: List<Media>

    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: MediaAdapter? = null

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
    private lateinit var mMap: ImageView
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
        setHasOptionsMenu(true)
        val id: Long? = arguments?.getLong(Constants.PROPERTY_ID)
        if(id != null){
            getProperty(id)
        }
        return inflater.inflate(R.layout.fragment_property_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        if (!resources.getBoolean(R.bool.isTablet)){
            val addProperty = menu.findItem(R.id.toolbar_add)
            addProperty?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //R.id.toolbar_add -> AddActivity::class.java
            R.id.toolbar_edit -> editProperty()
            R.id.toolbar_search -> startActivity(Intent(activity,SearchActivity::class.java))
            R.id.toolbar_map -> startActivity(Intent(activity,MapActivity::class.java))
            else -> return true
        }
        return true
    }

    private fun getProperty(id: Long){
        val db = AppDatabase.getInstance(requireContext())
        val executor = Executors.newSingleThreadExecutor()
        executor.execute{
            mProperty = db?.propertyDAO()?.getProperty(id)!!
            mMedias = db.mediaDAO().getMedias(id)
            activity?.runOnUiThread{
                setupPropertyCard()
            }
            activity?.runOnUiThread{
                configureRecyclerView()
            }
        }
    }

    private fun changePriceCurrency(){
        if (mPrice.text.toString()[0] == '$'){
            mPrice.text = activity?.resources?.getString(R.string.price_euros, Utils.formatPriceToEuros(mProperty.price))
        }else{
            mPrice.text = activity?.resources?.getString(R.string.price_dollars, mProperty.price)
        }
    }

    private fun editProperty(){
        val intent = Intent(activity, AddActivity::class.java)
        intent.putExtra(Constants.PROPERTY_ID, mProperty.id)
        startActivity(intent)
    }

    // CONFIGURATION

    private fun setupPropertyCard(){
        // Populate the fields. If a field is null, hide its layout
        if(!mProperty.description.equals("")){ mDescription.text = mProperty.description} else{ mDescriptionLayout.visibility = View.GONE}
        if(!mProperty.surface.equals("")){ mSurface.text = mProperty.surface} else{ mSurfaceLayout.visibility = View.GONE}
        if(!mProperty.roomNumber.equals("")){ mNumberRooms.text = mProperty.roomNumber} else{ mNumberRoomsLayout.visibility = View.GONE}
        if(!mProperty.pointsOfInterest.equals("")){ mPOIs.text = mProperty.pointsOfInterest} else{ mPOIsLayout.visibility = View.GONE}
        if(!mProperty.marketEntryDate.equals("")){ mEntryDate.text = mProperty.marketEntryDate} else{ mEntryDateLayout.visibility = View.GONE}
        if(mProperty.available){ mSaleDateLayout.visibility = View.GONE}else{ mAvailable.text = activity?.resources?.getString(R.string.property_sold)}
        if(!mProperty.sellDate.equals("")){mSaleDate.text = mProperty.sellDate} else{ mSaleDateLayout.visibility = View.GONE}
        if(!mProperty.agent.equals("")){ mAgent.text = mProperty.agent} else{ mAgentLayout.visibility = View.GONE}
        if(!mProperty.price.equals("")){ mPrice.text = activity?.resources?.getString(R.string.price_dollars, mProperty.price)} else{ mPriceLayout.visibility = View.GONE}
        if(!mProperty.address.equals("")){
            mLocation.text = mProperty.address
            val mapUrl: String = Utils.getMapUrl(mProperty.address)
            Picasso.with(context).load(mapUrl).fit().into(mMap)
        } else{ mLocationLayout.visibility = View.GONE}
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
        mMap = property_card_map
        mDescriptionLayout = property_card_description_layout
        mSurfaceLayout = property_card_surface_layout
        mNumberRoomsLayout = property_card_numberRooms_layout
        mLocationLayout = property_card_location_layout
        mPOIsLayout = property_card_POIs_layout
        mEntryDateLayout = property_card_entry_date_layout
        mSaleDateLayout = property_card_sale_date_layout
        mAgentLayout = property_card_agent_layout
        mPriceLayout = property_card_price_layout

        mPrice.setOnClickListener{ changePriceCurrency() }
    }

    private fun configureRecyclerView(){
        mRecyclerView = property_card_recycler_view
        mAdapter = MediaAdapter(mMedias, requireContext())
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }
}
