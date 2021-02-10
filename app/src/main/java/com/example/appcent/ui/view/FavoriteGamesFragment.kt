package com.example.appcent.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcent.R
import com.example.appcent.ui.ViewModel
import com.example.appcent.ui.adapter.GamesAdapter
import kotlinx.android.synthetic.main.fragment_favorite_games.*

class FavoriteGamesFragment : Fragment(), GamesAdapter.OnItemClickListener {

    private lateinit var adapter: GamesAdapter
    lateinit var viewModel: ViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: ViewModel by navGraphViewModels(R.id.nav_graph)
        this.viewModel = viewModel
        return inflater.inflate(
            R.layout.fragment_favorite_games,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GamesAdapter(this)
        initRv()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetData()
        adapter.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetData()
        adapter.clear()
    }

    private fun observeData() {
        viewModel.favGames.observe(viewLifecycleOwner, {
            it?.let {
                adapter.addAll(it)
            }
        })
    }

    private fun initRv() {
        rvFavs.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvFavs.adapter = adapter
        viewModel.getFavGames()
    }

    override fun onItemClicked(id: Int) {
        findNavController().navigate(
            FavoriteGamesFragmentDirections.actionFavoriteGamesFragmentToGameDetailFragment(
                id
            )
        )
    }

}