package com.moviemaker.ui.media

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.airbnb.epoxy.EpoxyModel
import com.moviemaker.App
import com.moviemaker.R
import com.moviemaker.domain.Media
import com.moviemaker.extension.isImageUri
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotterknife.bindView
import timber.log.Timber


class MediaItemView : FrameLayout {

    // private members

    // views
    private val imageView: ImageView by bindView(R.id.image_view)


    // constructors
    @JvmOverloads
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)


    // public functions
    fun bind(media: Media) {
        val mediaUri = Uri.parse(media.path)
        if(mediaUri.isImageUri()) {
            App.component.picasso()
                    .load(mediaUri)
                    .into(imageView)
        } else {
            Observable.create<Bitmap> {emitter->
                val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
                val cursor = context.contentResolver.query(mediaUri, filePathColumn, null, null, null)
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                cursor.close()
                Timber.d("Bipin - picturePath: $picturePath")
                val bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)
                emitter.onNext(bitmap)
                emitter.onComplete()
            }.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnNext { bitmap ->
                        imageView.scaleType = ScaleType.FIT_XY
                        imageView.setImageBitmap(bitmap)
                    }
                    .subscribe()
        }
    }

    // Model
    data class Model(
            val media: Media
    ) : EpoxyModel<MediaItemView>() {

        private var mediaItemView: MediaItemView? = null

        override fun getDefaultLayout() = R.layout.media_item_view

        override fun bind(view: MediaItemView) {
            mediaItemView = view
            view.bind(media)
        }

    }

}