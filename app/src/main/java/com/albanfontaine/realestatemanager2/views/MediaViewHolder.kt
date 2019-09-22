package com.albanfontaine.realestatemanager2.views

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.realestatemanager2.models.Media
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_property_card_item.view.*

class MediaViewHolder(var view: View): RecyclerView.ViewHolder(view){
	private val mImage: ImageView = view.fragment_property_card_item_imageView
	private val mDescription: TextView = view.fragment_property_card_item_description

	fun updateWithMedia(media: Media?, context: Context){
		if(media != null){
			Picasso.with(context).load(Uri.parse(media.uri)).fit().into(mImage)
			mDescription.text = media.description
		}
	}
}