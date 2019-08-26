package com.albanfontaine.realestatemanager2.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.models.Property

class PropertyAdapter(var properties: List<Property>, var context: Context): RecyclerView.Adapter<PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_list_item, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.updateWithProperty(properties.get(position), context)
    }

    override fun getItemCount(): Int = properties.size

}
