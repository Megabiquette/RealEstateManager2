package com.albanfontaine.realestatemanager2.controllers

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.PropertyAndMedias
import com.albanfontaine.realestatemanager2.models.SearchQuery
import com.albanfontaine.realestatemanager2.utils.Constants
import com.albanfontaine.realestatemanager2.utils.ItemClickSupport
import com.albanfontaine.realestatemanager2.views.PropertyAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ListFragment : Fragment() {

	private lateinit var mPropertiesAndMedias: List<PropertyAndMedias>
    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: PropertyAdapter? = null
    private var mSearchQuery: SearchQuery? = null

    private var mDb: AppDatabase? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        if (arguments != null){
			// Activity was called from search
            val gson = Gson()
            val searchQueryType = object: TypeToken<SearchQuery>(){}.type
            mSearchQuery = gson.fromJson(arguments?.getString(Constants.SEARCH_QUERY), searchQueryType)
        }
		return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		mRecyclerView = fragment_list_recycler_view
		getProperties()
		val activity = activity as AppCompatActivity
		activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        if(mAdapter != null){
            getProperties()
            mAdapter?.notifyDataSetChanged()
			checkListSize()
        }
    }

    private fun getProperties(){
		mDb = AppDatabase.getInstance(requireContext())
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute{
            if(mSearchQuery != null){
                mPropertiesAndMedias = mDb?.propertyAndMediasDAO()?.searchProperties(mSearchQuery?.types!!, mSearchQuery?.priceMin!!, mSearchQuery?.priceMax!!, mSearchQuery?.surfaceMin!!, mSearchQuery?.surfaceMax!!,
                    mSearchQuery?.neighborhood!!, mSearchQuery?.city!!, mSearchQuery?.POIs!!, mSearchQuery?.entryDateFrom!!, mSearchQuery?.entryDateTo!!, mSearchQuery?.saleDateFrom!!, mSearchQuery?.saleDateTo!!,
                    mSearchQuery?.available!!, mSearchQuery?.agent!!, mSearchQuery?.mediaMin!!)!!

            }else{
                mPropertiesAndMedias = mDb?.propertyAndMediasDAO()?.getProperties()!!
            }
            activity?.runOnUiThread{
				checkListSize()
                configureRecyclerView()
                configureOnClickRecyclerView()
				// If device is a tablet, show first property card in right fragment
				if(resources.getBoolean(R.bool.isTablet) && !mPropertiesAndMedias.isEmpty()){
					val id = mPropertiesAndMedias[0].property?.id
					val bundle = Bundle()
					bundle.putLong(Constants.PROPERTY_ID, id!!)
					val propertyCardFragment = PropertyCardFragment()
					propertyCardFragment.arguments = bundle
					activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.activity_base_frame_layout_right, propertyCardFragment)?.addToBackStack(null)?.commit()
				}
            }
        }
    }

	private fun checkListSize(){
		val noPropertyTextView: TextView = fragment_list_no_property_found
		if(mPropertiesAndMedias.isEmpty()){
			noPropertyTextView.visibility = View.VISIBLE
		}else{
			noPropertyTextView.visibility = View.GONE
		}
	}

    private fun configureRecyclerView(){
        mAdapter = PropertyAdapter(mPropertiesAndMedias, requireContext(), requireActivity())
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_list_item)
            .setOnItemClickListener(object: ItemClickSupport.OnItemClickListener{
                override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                    val id = mPropertiesAndMedias[position].property?.id
                    val bundle = Bundle()
                    bundle.putLong(Constants.PROPERTY_ID, id!!)
                    val propertyCardFragment = PropertyCardFragment()
                    propertyCardFragment.arguments = bundle
					if (!resources.getBoolean(R.bool.isTablet)){
                    	activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.activity_base_frame_layout, propertyCardFragment)?.addToBackStack(null)?.commit()
					}else{
						// Highlight selected property in the recycler view
						for(i in 0 until recyclerView.childCount){
							val view:View? = recyclerView.layoutManager?.findViewByPosition(i)
							if(i == position){
								view!!.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
							}else{
								view!!.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.quantum_white_100))
							}
						}
						activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.activity_base_frame_layout_right, propertyCardFragment)?.addToBackStack(null)?.commit()
					}
                }
            })
    }
}
