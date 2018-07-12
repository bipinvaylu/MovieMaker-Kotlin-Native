package com.moviemaker.extension

import android.app.Activity
import android.content.Intent
import com.moviemaker.R

fun Activity.showImageChooser(requestCode: Int) {
    val pickPhoto = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    )
    val chooser = Intent.createChooser(
            pickPhoto,
            getString(R.string.choose_picture)
    )
    startActivityForResult(chooser, requestCode)
}