package com.example.appcent.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcent.R
import com.example.appcent.ui.ViewModel
import com.example.appcent.ui.adapter.GamesAdapter
import com.example.appcent.ui.adapter.MySliderAdapter
import com.example.appcent.ui.pagination.PaginatedView
import com.example.appcent.ui.pagination.PaginationHelper
import com.example.appcent.util.Util
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_games.*


class GamesFragment : Fragment(), PaginatedView, GamesAdapter.OnItemClickListener {

    private var isFirstInit: Boolean = true
    lateinit var viewModel: ViewModel

    private lateinit var adapter: GamesAdapter

    lateinit var paginationHelper: PaginationHelper
    private var page = 1 //first page to load
    private var loading = false

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: ViewModel by navGraphViewModels(R.id.nav_graph)
        this.viewModel = viewModel
        return inflater.inflate(R.layout.fragment_games, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paginationHelper = PaginationHelper(this)
        adapter = GamesAdapter(this)
        isFirstInit = true
        initRv()
        filterGames()
        getGames(page)
        observeData()
        handleTouchEventsForSearchBar()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleTouchEventsForSearchBar() {
        containerLayout.setOnTouchListener { p0, p1 ->
            etSearch.clearFocus()
            true
        }
        etSearch.setOnFocusChangeListener { _, hasFocus -> setViewPagerVisibility(if (hasFocus) View.GONE else View.VISIBLE) }
    }

    private fun getGames(page: Int) {
        loading = true
        adapter.addLoadingFooter()
        viewModel.getGames(page)
    }

    private fun filterGames() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.length > 2) {
                        Util.searchEvent(Firebase.analytics, it.toString())
                        viewModel.filterGames(it.toString())
                    }
                }
                if (s.isNullOrEmpty()) {
                    viewModel.filterGames("")
                }
            }
        })

    }

    private fun setViewPagerVisibility(visibility: Int) {
        tabLayout.visibility = visibility
        viewPagerMain.visibility = visibility
    }

    private fun observeData() {
        viewModel.games.observe(viewLifecycleOwner, {
            paginationHelper.setPaginate(true)
            it?.let {
                if (isFirstInit) {
                    viewPagerMain.adapter = MySliderAdapter(it.take(3))
                    TabLayoutMediator(tabLayout, viewPagerMain) { tab, position -> }.attach()
                }

                loading = false
                adapter.removeLoadingFooter()
                adapter.addAll(if (isFirstInit) it.drop(3) else it)
                viewModel.allGames.addAll(it)
                isFirstInit = false
            }
        })

        viewModel.filteredGames.observe(viewLifecycleOwner, {
            adapter.clear()
            adapter.removeLoadingFooter()
            if (etSearch.text.isEmpty()) {
                paginationHelper.setPaginate(true)
                adapter.addAll(viewModel.allGames)
            } else {
                paginationHelper.setPaginate(false)
                adapter.addAll(it)
            }
        })

        viewModel.onError.observe(viewLifecycleOwner, {
            loading = false
            adapter.removeLoadingFooter()
            Snackbar.make(requireView(), getString(R.string.unexpected_error), Snackbar.LENGTH_LONG)
                .show()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetData()
    }

    private fun initRv() {
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvGames.layoutManager = linearLayoutManager
        rvGames.adapter = adapter
    }

    override fun passOtherViewToAnimate(): View? {
        return null
    }

    override fun loadMoreItems() {
        page += 1
        getGames(page)
    }

    override val isLastPage: Boolean
        get() = viewModel.isLastPage


    override val isLoading: Boolean
        get() = loading

    override fun passLinearLayoutManager(): LinearLayoutManager? {
        return linearLayoutManager
    }

    override fun passScrollView(): View {
        return rvGames
    }

    override fun onItemClicked(
        id: Int,
    ) {
        findNavController().navigate(
            GamesFragmentDirections.actionGamesFragmentToGameDetailFragment(
                id
            )
        )
    }

}