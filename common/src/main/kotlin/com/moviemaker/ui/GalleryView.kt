package com.moviemaker.ui

import com.moviemaker.base.BaseView
import com.moviemaker.domain.Media

interface GalleryView : BaseView {

    fun showMediaList(mediaItem: List<Media>)
    fun showLoading(loading: Boolean)
    fun showError(errorMessage: String)

}