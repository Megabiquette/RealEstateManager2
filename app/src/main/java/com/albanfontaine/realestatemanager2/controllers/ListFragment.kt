package com.albanfontaine.realestatemanager2.controllers

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.views.PropertyAdapter
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ListFragment : Fragment() {

    private lateinit var mProperties: List<Property>
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: PropertyAdapter

    private var mDb: AppDatabase? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getProperties()
    }

    private fun configureRecyclerView(){

        mRecyclerView = fragment_list_recycler_view
        mAdapter = PropertyAdapter(mProperties, requireContext(), requireActivity())
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun getProperties(){
        mDb = AppDatabase.getInstance(requireContext())
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute{
            mProperties = mDb?.propertyDAO()?.getProperties()!!
            if(!mProperties.isNullOrEmpty()){
                mProperties?.forEach {
                    Log.e("property", it.toString())
                }
            }else{
                Log.e("getProps", "empty")
            }
            activity?.runOnUiThread{
                configureRecyclerView()
            }
        }
    }
}
