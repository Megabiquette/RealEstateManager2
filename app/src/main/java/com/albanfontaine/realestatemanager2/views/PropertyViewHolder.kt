package com.albanfontaine.realestatemanager2.views

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_list_item.view.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PropertyViewHolder(var view: View): RecyclerView.ViewHolder(view){
    private val mMedia: ImageView = view.list_item_media
    private val mType: TextView = view.list_item_type
    private val mNeighborhood: TextView = view.list_item_neighborhood
    private val mPrice: TextView = view.list_item_price

    private var mDb: AppDatabase? = null

    fun updateWithProperty(property: Property?, context: Context, activity: Activity){
        if(property != null){
            mDb = AppDatabase.getInstance(context)
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute{
                if(mDb?.mediaDAO()?.getMedias(property.id)?.size != 0){
                    val mediaURI: String = mDb?.mediaDAO()?.getMedias(property.id)?.get(0)!!.uri
                    activity.runOnUiThread{
                        val uri: Uri = Uri.parse(mediaURI)
                        Picasso.with(context).load(uri).fit().centerCrop().into(mMedia)
                    }
                }else{
                    // In case there is no media for this property
                    activity.runOnUiThread {
                        Picasso.with(context).load(R.drawable.home).fit().centerCrop().into(mMedia)
                    }
                }
            }

            mType.text = property.type
            if(!property.neighborhood.equals("")){
                mNeighborhood.text = property.neighborhood
            }else{
                mNeighborhood.text = activity.resources.getString(R.string.no_neighborhood)
                mNeighborhood.setTypeface(mNeighborhood.typeface, Typeface.ITALIC)
            }
            if(property.price != null){
                mPrice.text = Utils.formatPriceDollars(property.price!!)
            }else{
                mPrice.text = activity.resources.getString(R.string.no_price)
                mPrice.setTypeface(mNeighborhood.typeface, Typeface.ITALIC)
            }
        }
    }
}