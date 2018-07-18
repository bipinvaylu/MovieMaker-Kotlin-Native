package com.moviemaker.ui.media

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyModel
import com.moviemaker.App
import com.moviemaker.R
import com.moviemaker.domain.Media
import com.moviemaker.extension.isImageUri
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
            Timber.d("Bipin - ImageView width: ${imageView.width}, height: ${imageView.height}")
        } else {
            val videoId = mediaUri.toString().split("/").last().toLong()
            val width = App.component.context().resources.getDimensionPixelSize(R.dimen.material_increment_9x)
            val height = App.component.context().resources.getDimensionPixelSize(R.dimen.material_increment_5x)
            App.component.picasso().load(
                    ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            videoId
                    )
            )
                    .resize(width, height)
                    .centerCrop()
                    .into(imageView)
//            Observable.create<String> {emitter->
//                val projection = arrayOf(MediaStore.Video.Media.DATA)
//                val videoId = mediaUri.toString().split("/").last()
//                val cursor = context.contentResolver.query(
//                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
//                        projection,
//                        MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
//                        arrayOf(videoId),
//                        null
//                )
//                cursor.moveToFirst()
//                val columnIndex = cursor.getColumnIndex(projection[0])
//                val thumbnailPath = cursor.getString(columnIndex)
//                cursor.close()
//                emitter.onNext(thumbnailPath)
//                emitter.onComplete()
//            }.observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .doOnNext { thumbnailPath ->
//                        App.component.picasso()
//                                .load(File(thumbnailPath))
//                                .into(imageView)
//                    }
//                    .subscribe()
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