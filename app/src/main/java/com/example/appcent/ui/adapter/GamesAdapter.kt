package com.example.appcent.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appcent.R
import com.example.appcent.model.Game
import com.example.appcent.ui.view.LoadingVH
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.games_list_item.view.*

class GamesAdapter(onItemClickListener: OnItemClickListener) : BasePaginationAdapter<Game>() {
    private var mListener: OnItemClickListener? = null


    init {
        this.mListener = onItemClickListener
    }

    override fun onBindData(holder: RecyclerView.ViewHolder?, item: Game, position: Int) {
        if (getItemViewType(position) != LOADING) {
            (holder as ViewHolder).bind(item)
        }
    }


    override fun setViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        when (viewType) {
            ITEM -> {
                val view: View = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.games_list_item, parent, false)
                viewHolder = ViewHolder(view)
            }
            LOADING -> {
                val v2: View = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_loading, parent, false)
                viewHolder = LoadingVH(v2)
            }
        }
        return viewHolder!!
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {
            itemView.rating.rating = game.rating
            itemView.name.text = game.name
            itemView.released.text = game.released
            Picasso.get().load(game.imageUrl).resize(40, 40).onlyScaleDown()
                .into(itemView.image)

            itemView.setOnClickListener {
                mListener?.onItemClicked(game.id)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: Int)
    }
}