package com.tt.driver.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView

class NestedScrollPaginationView : NestedScrollView {
    var myScrollChangeListener: OnMyScrollChangeListener? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
    }

    private var currentPage = 1
    fun resetPageCounter() {
        currentPage = 1
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (myScrollChangeListener != null) { //            if (t > oldt) {
//                myScrollChangeListener.onScrollDown();
//            } else if (t < oldt) {
//                myScrollChangeListener.onScrollUp();
//            }
            val view = getChildAt(childCount - 1) as View
            val diff = view.bottom - (height + scrollY)
            if (diff == 0) {
                currentPage++
                myScrollChangeListener!!.onLoadMore(currentPage)
            }
        }
    }

    interface OnMyScrollChangeListener {
        //        public void onScrollUp();
//
//        public void onScrollDown();
        fun onLoadMore(currentPage: Int)
    }
}