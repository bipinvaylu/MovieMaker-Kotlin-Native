package com.moviemaker.ui.media

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyModel
import com.moviemaker.R
import com.moviemaker.domain.Media
import kotterknife.bindView


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
        val imageUri = Uri.parse(media.path)
        context.grantUriPermission(context.packageName, imageUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//        val bitmap = getBitmapFromUri(imageUri)
        imageView.setImageBitmap(bitmap)
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
        val image = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.fileDescriptor)
        parcelFileDescriptor.close()
        return image
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