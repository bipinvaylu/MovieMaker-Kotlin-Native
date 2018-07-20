package com.moviemaker.extension

import android.app.Activity
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.PicassoEngine


fun Activity.showMediaChooser(requestCode: Int) {
//    val pickPhoto = Intent(
//            Intent.ACTION_PICK
//            ,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//    )
//    pickPhoto.type = "video/*"
//    pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//    pickPhoto.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
//    val chooser = Intent.createChooser(
//            pickPhoto,
//            getString(R.string.choose_media)
//    )
//    startActivityForResult(chooser, requestCode)
    Matisse.from(this)
            .choose(MimeType.ofVideo())
            .countable(false)
            .maxSelectable(10)
            .thumbnailScale(0.85f)
            .imageEngine(PicassoEngine())
            .forResult(requestCode)
}