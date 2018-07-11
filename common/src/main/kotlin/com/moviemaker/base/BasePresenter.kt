package com.moviemaker.base

abstract class BasePresenter<V : BaseView> {

    private var view: V? = null
        private set


    fun attach(view: V) {
        this.view = view
        onViewAttached()
    }

    fun detach(view: V) {
        this.view = view
        onViewDetached()
    }

    open fun onViewAttached() {

    }

    open fun onViewDetached() {

    }

}