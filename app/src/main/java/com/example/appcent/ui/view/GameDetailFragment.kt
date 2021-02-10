package com.example.appcent.ui.view

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.ImageViewCompat
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.appcent.R
import com.example.appcent.ui.ViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_game_detail.*

class GameDetailFragment : Fragment() {

    lateinit var viewModel: ViewModel
    private val args: GameDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: ViewModel by navGraphViewModels(R.id.nav_graph)
        this.viewModel = viewModel

        return inflater.inflate(R.layout.fragment_game_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.let {
            viewModel.getGameDetail(it.id)
        }
        observeData()
        setFavorite()
    }

    private fun observeData() {
        viewModel.gameDetail.observe(viewLifecycleOwner, {
            name.text = it.name
            releasd.text = it.released
            Picasso.get().load(it.imageUrl).centerCrop().fit().into(image)
            description.text =
                HtmlCompat.fromHtml(it.description!!, HtmlCompat.FROM_HTML_MODE_LEGACY);
            setFavIconTint(it.isFav)
            (activity as AppCompatActivity).supportActionBar?.title = it.name
        })
    }

    private fun setFavIconTint(isFav: Boolean) {
        ImageViewCompat.setImageTintList(
            favIcon,
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    if (isFav) R.color.purple_500 else R.color.purple_200
                )
            )
        )
    }

    private fun setFavorite() {
        favIcon.setOnClickListener {
            viewModel.gameDetail.value?.let {
                if (!it.isFav) {
                    viewModel.addToFavorites(it)
                    setFavIconTint(true)
                } else {
                    viewModel.removeFromFavorites(it.id)
                    setFavIconTint(false)
                }
            }
        }
    }

}