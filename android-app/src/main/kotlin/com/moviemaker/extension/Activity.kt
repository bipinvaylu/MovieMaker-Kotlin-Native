package com.moviemaker.extension

import android.app.Activity
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.PicassoEngine
import timber.log.Timber


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
            .choose(MimeType.ofAll())
            .countable(false)
//            TODO: Fix camera crash
//            .capture(true)
//            .captureStrategy(
//                    CaptureStrategy(true, "com.moviemaker.fileprovider")
//            )
            .maxSelectable(10)
            .thumbnailScale(0.85f)
            .imageEngine(PicassoEngine())
            .originalEnable(true)
            .maxOriginalSize(10)
            .setOnSelectedListener { uriList, pathList ->
                Timber.d("Bipin - onSelectedLister uriList:$uriList, pathList: $pathList")
            }
            .setOnCheckedListener {
                Timber.d("Bipin - onCheckedListener called")
            }
            .forResult(requestCode)
}