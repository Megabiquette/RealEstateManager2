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
import com.albanfontaine.realestatemanager2.models.Property
import com.albanfontaine.realestatemanager2.models.PropertyAndMedias
import com.albanfontaine.realestatemanager2.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_list_item.view.*

class PropertyViewHolder(var view: View): RecyclerView.ViewHolder(view){
    private val mMedia: ImageView = view.list_item_media
    private val mType: TextView = view.list_item_type
    private val mNeighborhood: TextView = view.list_item_neighborhood
    private val mPrice: TextView = view.list_item_price

    //private var mDb: AppDatabase? = null

    fun updateWithProperty(propertyAndMedias: PropertyAndMedias?, context: Context, activity: Activity){
        if(propertyAndMedias != null){
            if(!propertyAndMedias.medias.isEmpty()){
                val mediaUri: Uri = Uri.parse(propertyAndMedias.medias[0].uri)
                Picasso.with(context).load(mediaUri).fit().centerCrop().into(mMedia)
            }else{
                Picasso.with(context).load(R.drawable.home).fit().centerCrop().into(mMedia)
            }

            mType.text = propertyAndMedias.property?.type

            if(!propertyAndMedias.property?.neighborhood.equals("")){
                mNeighborhood.text = propertyAndMedias.property?.neighborhood
            }else if(!propertyAndMedias.property?.city.equals("")){
                mNeighborhood.text = propertyAndMedias.property?.city
            } else{
                mNeighborhood.text = activity.resources.getString(R.string.no_neighborhood)
                mNeighborhood.setTypeface(mNeighborhood.typeface, Typeface.ITALIC)
            }

            if(propertyAndMedias.property?.price != null){
                mPrice.text = Utils.formatPriceDollars(propertyAndMedias.property?.price!!)
            }else{
                mPrice.text = activity.resources.getString(R.string.no_price)
                mPrice.setTypeface(mPrice.typeface, Typeface.ITALIC)
            }
        }
    }
}