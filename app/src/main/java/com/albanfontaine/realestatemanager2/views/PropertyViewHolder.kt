package com.albanfontaine.realestatemanager2.views

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.realestatemanager2.database.AppDatabase
import com.albanfontaine.realestatemanager2.models.Property
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_list_item.view.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PropertyViewHolder(var view: View): RecyclerView.ViewHolder(view){
    private val mMedia: ImageView = view.list_item_media
    private val mType: TextView = view.list_item_type
    private val mLocation: TextView = view.list_item_location
    private val mPrice: TextView = view.list_item_price

    private var mDb: AppDatabase? = null

    fun updateWithProperty(property: Property?, context: Context, activity: Activity){
        if(property != null){
            Log.e("updateWithProperty", "property id = " + property.id.toString())
            mDb = AppDatabase.getInstance(context)
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute{
                val mediaURI: String = mDb?.mediaDAO()?.getMedias(property.id)?.get(0)!!.uri
                Log.e("mediaUri", mediaURI)
                activity.runOnUiThread{
                    val uri: Uri = Uri.parse(mediaURI)
                    Picasso.Builder(context).listener{_, _, e->e.printStackTrace()}.build().load(uri).fit().centerCrop().into(mMedia, object: Callback{
                        override fun onSuccess(){
                            Log.e("picasso", "success")
                        }

                        override fun onError() {
                            Log.e("picasso", "error")
                        }
                    })
                }
            }

            mType.text = property.type
            mLocation.text = property.address
            mPrice.text = "\$" + property.price
        }
    }
}