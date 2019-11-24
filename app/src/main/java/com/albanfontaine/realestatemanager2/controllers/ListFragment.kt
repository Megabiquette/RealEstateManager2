package com.albanfontaine.realestatemanager2.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
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
		Log.e("onCreateView", "onCreateView")
        setHasOptionsMenu(true)
        if (arguments != null){
            val gson = Gson()
            val searchQueryType = object: TypeToken<SearchQuery>(){}.type
            mSearchQuery = gson.fromJson(arguments?.getString(Constants.SEARCH_QUERY), searchQueryType)
        }
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		Log.e("onViewCreated", "onViewCreated")
		getProperties()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        if(!resources.getBoolean(R.bool.isTablet)){
            val editProperty = menu.findItem(R.id.toolbar_edit)
            editProperty?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_add -> startActivity(Intent(activity,AddActivity::class.java))
            //R.id.toolbar_edit ->
            R.id.toolbar_search -> startActivity(Intent(activity,SearchActivity::class.java))
            R.id.toolbar_map -> startActivity(Intent(activity,MapActivity::class.java))
            else -> return true
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if(mAdapter != null){
            getProperties()
            mAdapter?.notifyDataSetChanged()
        }
    }

    private fun getProperties(){
		Log.e("getProps", "getProps")
		mDb = AppDatabase.getInstance(requireContext())
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute{
            if(mSearchQuery != null){
                mPropertiesAndMedias = mDb?.propertyAndMediasDAO()?.searchProperties(mSearchQuery?.types!!, mSearchQuery?.priceMin!!, mSearchQuery?.priceMax!!, mSearchQuery?.surfaceMin!!, mSearchQuery?.surfaceMax!!,
                    mSearchQuery?.neighborhood!!, mSearchQuery?.POIs!!, mSearchQuery?.entryDateFrom!!, mSearchQuery?.entryDateTo!!, mSearchQuery?.saleDateFrom!!, mSearchQuery?.saleDateTo!!,
                    mSearchQuery?.available!!, mSearchQuery?.agent!!, mSearchQuery?.mediaMin!!)!!

            }else{
                mPropertiesAndMedias = mDb?.propertyAndMediasDAO()?.getProperties()!!
            }
            activity?.runOnUiThread{
                configureRecyclerView()
                configureOnClickRecyclerView()
            }
        }
    }

    private fun configureRecyclerView(){
		Log.e("configRV", "configRV")
		mRecyclerView = fragment_list_recycler_view
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
						activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.activity_base_frame_layout_right, propertyCardFragment)?.addToBackStack(null)?.commit()
					}
                }
            })
    }
}
