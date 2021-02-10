package com.example.appcent.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*


/**
 * This adapter class is responsible for changing the item type when new paginated data is being fetched from the API
 *
 * @param <T> : This is a generic object that represents the type of data that is used by the extending adapters
</T> */
abstract class BasePaginationAdapter<T> internal constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = ArrayList<T>()
    private var isLoadingAdded = false
    abstract fun setViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder
    abstract fun onBindData(holder: RecyclerView.ViewHolder?, `val`: T, position: Int)
    override fun getItemViewType(position: Int): Int {
        return if (position == items.size && isLoadingAdded) {
            LOADING
        } else ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return setViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Ignore binding for loading footer.
        if (isLoadingAdded && position == items.size) {
            return
        }
        onBindData(holder, items[position], position)
    }

    override fun getItemCount(): Int {
        return if (isLoadingAdded) {
            items.size + 1
        } else items.size
    }

    private fun getItem(position: Int): T {
        return items[position]
    }

    private fun add(mc: T) {
        items.add(mc)
        notifyItemInserted(items.size - 1)
    }

    fun addAll(mcList: List<T>) {
        for (mc in mcList) {
            add(mc)
        }
    }

    private fun remove(city: T) {
        val position = items.indexOf(city)
        if (position > -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        removeLoadingFooter()
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun addLoadingFooter() {
        if (!isLoadingAdded) {
            isLoadingAdded = true
            notifyItemInserted(items.size)
        }
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false
            notifyItemRemoved(items.size)
        }
    }

    companion object {
        const val LOADING = 2
        const val ITEM = 1
    }
}
