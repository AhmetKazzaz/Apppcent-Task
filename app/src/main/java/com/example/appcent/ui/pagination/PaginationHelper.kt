package com.example.appcent.ui.pagination

import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference


/**
 * this class is a helper class that is responsible for implementing the scroll listener and animating extra passed views
 */
class PaginationHelper(paginated: PaginatedView) {
    private var paginate: Boolean = true
    private val paginatedView: WeakReference<PaginatedView?> = WeakReference(paginated)
    private var isFiltersBarHide = false

    /**
     * This method checks for type of passedview, if it is indeed a scrollable view of type
     * Recyclerview (in this case: in this challenge project all data is dynamic)
     * then adds a scroll listener to it that checks invokes the loadMoreItems to fetch more data [Pagination]
     */
    private fun onScrollListener(paginated: PaginatedView) {
        if (paginated.passScrollView() is RecyclerView) {
            (paginated.passScrollView() as RecyclerView).addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val pagination = paginatedView.get()
                    if (pagination != null && paginate) {
                        val visibleItemCount = pagination.passLinearLayoutManager()!!.childCount
                        val totalItemCount = pagination.passLinearLayoutManager()!!.itemCount
                        val firstVisibleItemPosition = pagination.passLinearLayoutManager()!!
                            .findFirstVisibleItemPosition()
                        if (!pagination.isLoading && !pagination.isLastPage) {
                            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                                && firstVisibleItemPosition >= 0
                            ) {
                                pagination.loadMoreItems()
                            }
                        }
                        if (dy > 0) {
                            animatePassedView(hide = true, isFast = false)
                        } else {
                            animatePassedView(hide = false, isFast = false)
                        }
                    }
                }
            })
        }
    }

    fun setPaginate(state: Boolean) {
        paginate = state
    }

    /**
     * Animates a view up and down
     *
     * @param hide:   Indicated whether the view should be animated to be shown or hidden
     * @param isFast: true if the animation should be fast, false if should be normal
     */
    fun animatePassedView(hide: Boolean, isFast: Boolean) {
        if (paginatedView.get() == null) return
        if (paginatedView.get()!!.passOtherViewToAnimate() == null) return
        if (isFiltersBarHide && hide || !isFiltersBarHide && !hide) return
        isFiltersBarHide = hide
        val moveY = if (hide) -(2 * paginatedView.get()!!.passOtherViewToAnimate()!!.height) else 0
        paginatedView.get()!!.passOtherViewToAnimate()!!.animate().translationY(moveY.toFloat())
            .setStartDelay(if (isFast) 0 else 100.toLong())
            .setDuration(if (isFast) 100 else 300.toLong()).start()
    }

    init {
        onScrollListener(paginated)
    }
}
