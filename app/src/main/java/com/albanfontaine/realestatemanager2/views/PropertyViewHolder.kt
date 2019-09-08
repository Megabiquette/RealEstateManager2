package com.albanfontaine.realestatemanager2.views

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Property
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_list_item.view.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PropertyViewHolder(var view: View): RecyclerView.ViewHolder(view){
    private var mMedia: ImageView = view.list_item_media
    private var mType: TextView = view.list_item_type
    private var mLocation: TextView = view.list_item_location
    private var mPrice: TextView = view.list_item_price

    private var mDb: AppDatabase? = null

    fun updateWithProperty(property: Property?, context: Context, activity: Activity){
        Log.e("updateWithProperty", "updateWithProperty")
        if(property != null){
            mDb = AppDatabase.getInstance(context)
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute{
                val mediaURI: String = mDb?.mediaDAO()?.getMedias(property.id)?.get(0)!!.uri
                activity.runOnUiThread{
                    Picasso.with(context).load(mediaURI).into(mMedia)
                }
            }

            mType.text = property.type
            mLocation.text = property.address
            mPrice.text = property.price.toString()
        }
    }
}