package com.moviemaker.extension

import android.app.Activity
import android.content.Intent
import com.moviemaker.R

fun Activity.showImageChooser(requestCode: Int) {
    val pickPhoto = Intent(
            Intent.ACTION_PICK
            ,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    )
    pickPhoto.type = "image/*"
//    pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    pickPhoto.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    val chooser = Intent.createChooser(
            pickPhoto,
            getString(R.string.choose_picture)
    )
    startActivityForResult(chooser, requestCode)
}