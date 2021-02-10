package com.example.appcent.ui.pagination

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager


/**
 * Interface for views to implement when pagination of data is needed
 */
interface PaginatedView {
    /**
     * Animates a returned view based on dx, dy scroll
     *
     * @return: The view to animate
     */
    fun passOtherViewToAnimate(): View?

    /**
     * gets called when there is more data to load
     */
    fun loadMoreItems()

    /**
     * Check if this is the last page in the list
     *
     * @return: returns true if last page, false if last page hasn't been reached yet
     */
    val isLastPage: Boolean

    /**
     * @return: true if it should load, false if not
     */
    val isLoading: Boolean

    fun passLinearLayoutManager(): LinearLayoutManager?

    /**
     * Responsible for getting the scrollview to implement the scroll listener on
     *
     * @return: the passed scrollview
     */
    fun passScrollView(): View
}
