package com.example.appcent.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcent.R
import com.example.appcent.model.Game
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_item.view.*


class MySliderAdapter(
    var games: List<Game>
) :
    RecyclerView.Adapter<MySliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount(): Int {
        return games.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {
            Picasso.get()
                .load(game.imageUrl)
                .centerCrop()
                .fit()
                .into(itemView.imageViewMain)
        }
    }

}