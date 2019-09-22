package com.albanfontaine.realestatemanager2.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.models.Media

class MediaAdapter(private val medias: List<Media>, val context: Context): RecyclerView.Adapter<MediaViewHolder>(){
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val view = inflater.inflate(R.layout.fragment_property_card_item, parent, false)
		return MediaViewHolder(view)
	}

	override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
		holder.updateWithMedia(medias.get(position), context)
	}

	override fun getItemCount(): Int{
		return medias.size
	}
}