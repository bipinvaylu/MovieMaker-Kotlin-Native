package com.moviemaker.ui

import com.moviemaker.base.BasePresenter
import com.moviemaker.interactor.GetMediaList

class GalleryPresenter(
private val getMediaList: GetMediaList
): BasePresenter<GalleryView>() {

    override fun onViewAttached() {
        super.onViewAttached()
    }

}