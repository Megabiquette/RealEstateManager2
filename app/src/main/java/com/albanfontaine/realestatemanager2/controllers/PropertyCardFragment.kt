package com.albanfontaine.realestatemanager2.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Media
import com.albanfontaine.realestatemanager2.models.PropertyAndMedias
import com.albanfontaine.realestatemanager2.utils.Constants
import com.albanfontaine.realestatemanager2.utils.Utils
import com.albanfontaine.realestatemanager2.views.MediaAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_property_card.*
import java.util.concurrent.Executors

class PropertyCardFragment : Fragment() {
    private lateinit var mPropertyAndMedias: PropertyAndMedias
    private lateinit var mMedias: List<Media>

    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: MediaAdapter? = null

    private lateinit var mType: TextView
    private lateinit var mNeighborhood: TextView
    private lateinit var mDescription: TextView
    private lateinit var mSurface: TextView
    private lateinit var mNumberRooms: TextView
    private lateinit var mAddress: TextView
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
    private lateinit var mAddressLayout: LinearLayout
    private lateinit var mPOIsLayout: LinearLayout
    private lateinit var mEntryDateLayout: LinearLayout
    private lateinit var mSaleDateLayout: LinearLayout
    private lateinit var mAgentLayout: LinearLayout
    private lateinit var mPriceLayout: LinearLayout
    private lateinit var mLoanButton : Button

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
        if (!resources.getBoolean(R.bool.isTablet)){
            val activity = activity as AppCompatActivity
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.toolbar_menu, menu)
        if (!resources.getBoolean(R.bool.isTablet)){
            val addProperty = menu.findItem(R.id.toolbar_add)
            addProperty?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> activity?.supportFragmentManager?.popBackStack()
            R.id.toolbar_add -> return false
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
            mPropertyAndMedias = db?.propertyAndMediasDAO()?.getProperty(id)!!
            mMedias = mPropertyAndMedias.medias
            activity?.runOnUiThread{
                setupPropertyCard()
                configureRecyclerView()
            }
        }
    }

    private fun changePriceCurrency(){
        if (mPrice.text.toString()[0] == '$'){
            mPrice.text = Utils.formatPriceEuros(Utils.convertDollarToEuro(mPropertyAndMedias.property?.price!!))
        }else{
            mPrice.text = Utils.formatPriceDollars(mPropertyAndMedias.property?.price!!)
        }
    }

    private fun editProperty(){
        val intent = Intent(activity, AddActivity::class.java)
        intent.putExtra(Constants.PROPERTY_ID, mPropertyAndMedias.property?.id)
        startActivity(intent)
    }

    private fun simulateLoan(){
        val intent = Intent(activity, LoanActivity::class.java)
        intent.putExtra(Constants.PROPERTY_PRICE, mPropertyAndMedias.property?.price)
        startActivity(intent)
    }

    // CONFIGURATION

    private fun setupPropertyCard(){
        // Populate the fields. If a field is null, hide its layout
        mType.text = mPropertyAndMedias.property?.type
        if(!mPropertyAndMedias.property?.neighborhood.equals("")){ mNeighborhood.text = mPropertyAndMedias.property?.neighborhood} else{ mNeighborhood.visibility = View.GONE}
        if(!mPropertyAndMedias.property?.description.equals("")){ mDescription.text = mPropertyAndMedias.property?.description} else{ mDescriptionLayout.visibility = View.GONE}
        if(mPropertyAndMedias.property?.surface != null){ mSurface.text = mPropertyAndMedias.property?.surface.toString()} else{ mSurfaceLayout.visibility = View.GONE}
        if(mPropertyAndMedias.property?.roomNumber != null){ mNumberRooms.text = mPropertyAndMedias.property?.roomNumber.toString()} else{ mNumberRoomsLayout.visibility = View.GONE}
        if(!mPropertyAndMedias.property?.pointsOfInterest.equals("")){ mPOIs.text = mPropertyAndMedias.property?.pointsOfInterest} else{ mPOIsLayout.visibility = View.GONE}
        if(mPropertyAndMedias.property?.marketEntryDate != null){ mEntryDate.text = Utils.formatDateToText(mPropertyAndMedias.property?.marketEntryDate!!)} else{ mEntryDateLayout.visibility = View.GONE}
        if(mPropertyAndMedias.property?.available!!){ mSaleDateLayout.visibility = View.GONE}else{ mAvailable.text = activity?.resources?.getString(R.string.property_sold)}
        if(mPropertyAndMedias.property?.sellDate != null){mSaleDate.text = Utils.formatDateToText(mPropertyAndMedias.property?.sellDate!!)} else{ mSaleDateLayout.visibility = View.GONE}
        if(!mPropertyAndMedias.property?.agent.equals("")){ mAgent.text = mPropertyAndMedias.property?.agent} else{ mAgentLayout.visibility = View.GONE}
        if(mPropertyAndMedias.property?.price != null){
            mPrice.text =  Utils.formatPriceDollars(mPropertyAndMedias.property?.price!!)
        } else{
            mPriceLayout.visibility = View.GONE
            mLoanButton.visibility = View.GONE
        }
        if(!mPropertyAndMedias.property?.address.equals("")){
            mAddress.text = mPropertyAndMedias.property?.address
            val mapUrl: String = Utils.getMapUrl(mPropertyAndMedias.property?.address)
            Picasso.with(context).load(mapUrl).fit().into(mMap)
        } else{ mAddressLayout.visibility = View.GONE}
    }

    private fun configViews(){
        mType = property_card_type
        mNeighborhood = property_card_neighborhood
        mDescription = property_card_description
        mSurface = property_card_surface
        mNumberRooms = property_card_numberRooms
        mAddress = property_card_address
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
        mAddressLayout = property_card_address_layout
        mPOIsLayout = property_card_POIs_layout
        mEntryDateLayout = property_card_entry_date_layout
        mSaleDateLayout = property_card_sale_date_layout
        mAgentLayout = property_card_agent_layout
        mPriceLayout = property_card_price_layout
        mLoanButton = property_card_loan

        mPrice.setOnClickListener{ changePriceCurrency() }
        mLoanButton.setOnClickListener{ simulateLoan()}

        mRecyclerView = property_card_recycler_view

    }

    private fun configureRecyclerView(){
        mAdapter = MediaAdapter(mMedias, requireContext())
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }
}
