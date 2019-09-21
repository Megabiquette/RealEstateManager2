package com.albanfontaine.realestatemanager2.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView


class ItemClickSupport private constructor(
	private val mRecyclerView: RecyclerView,
	private val mItemID: Int
) {
	private var mOnItemClickListener: OnItemClickListener? = null

	private val mOnClickListener = View.OnClickListener { v ->
		if (mOnItemClickListener != null) {
			val holder = mRecyclerView.getChildViewHolder(v)
			mOnItemClickListener!!.onItemClicked(mRecyclerView, holder.adapterPosition, v)
		}
	}

	private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
		override fun onChildViewAttachedToWindow(view: View) {
			if (mOnItemClickListener != null) {
				view.setOnClickListener(mOnClickListener)
			}
		}

		override fun onChildViewDetachedFromWindow(view: View) {}
	}

	init {
		mRecyclerView.setTag(mItemID, this)
		mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener)
	}

	fun setOnItemClickListener(listener: OnItemClickListener): ItemClickSupport {
		mOnItemClickListener = listener
		return this
	}

	private fun detach(view: RecyclerView) {
		view.removeOnChildAttachStateChangeListener(mAttachListener)
		view.setTag(mItemID, null)
	}

	interface OnItemClickListener {
		fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View)
	}

	companion object {

		fun addTo(view: RecyclerView, itemID: Int): ItemClickSupport {
			var support: ItemClickSupport? = null
			if(view.getTag(itemID) != null){
				support = view.getTag(itemID) as ItemClickSupport
			}
			if (support == null) {
				support = ItemClickSupport(view, itemID)
			}
			return support
		}

		fun removeFrom(view: RecyclerView, itemID: Int): ItemClickSupport? {
			val support = view.getTag(itemID) as ItemClickSupport
			support?.detach(view)
			return support
		}
	}

}