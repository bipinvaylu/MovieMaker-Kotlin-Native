package com.moviemaker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.moviemaker.domain.Media
import com.moviemaker.extension.isImageUri
import com.moviemaker.extension.showMediaChooser
import com.moviemaker.ui.media.MediaGridView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotterknife.bindView
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    // private members
    private val rxPermissions: RxPermissions by lazy {
        RxPermissions(this)
    }

    // views
    private val mediaGridView: MediaGridView by bindView(R.id.media_grid_view)
    private val addImageButton: FloatingActionButton by bindView(R.id.add_image_button)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val mainView: View by bindView(R.id.media_grid_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle(R.string.app_name)

        addImageButton.setOnClickListener { _ ->
            showMediaChooser(IMAGE_CHOOSER_REQUEST_CODE)
        }



        rxPermissions.request(READ_PERMISSION, WRITE_PERMISSION)
                .filter { true }
                .subscribe {
                    mediaGridView.loadMediaList()
                }
//        if (ContextCompat.checkSelfPermission(this, READ_PERMISSION) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, WRITE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(READ_PERMISSION, WRITE_PERMISSION),
//                    READ_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            mediaGridView.loadMediaList()
//        }

    }

//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>,
//                                            grantResults: IntArray
//    ) {
//        when (requestCode) {
//            READ_PERMISSION_REQUEST_CODE,
//            WRITE_PERMISSION_REQUEST_CODE -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    mediaGridView.loadMediaList()
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
                && requestCode == IMAGE_CHOOSER_REQUEST_CODE
                && data != null) {
            mediaGridView.showLoading()
            Observable.create<Media> { emitter ->
                val selected: List<Uri> = Matisse.obtainResult(data)
                Timber.d("selected: $selected")
                selected.forEach {
                    val media = retriveMediaDetail(it)
                    media?.let {
                        emitter.onNext(it)
                    }
                }
                emitter.onComplete()
            }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnNext { media ->
                        mediaGridView.addMedia(media)
                        mediaGridView.hideLoading()
                    }
                    .subscribe()
        }
    }

    fun retriveMediaDetail(uri: Uri): Media? {
        val isVideo = !uri.isImageUri()

        val mediaColumns = arrayListOf(
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.SIZE
        )
        if (isVideo) {
            mediaColumns.add(MediaStore.Video.Media.DURATION)
        }

        val cursor = contentResolver.query(
                uri,
                mediaColumns.toTypedArray(),
                null,
                null,
                null
        )
        cursor.moveToFirst()
        val createdDate = cursor.getLong(cursor.getColumnIndex(mediaColumns[0]))
        val fileSize = cursor.getLong(cursor.getColumnIndex(mediaColumns[1]))
        val duration = if (isVideo) {
            cursor.getLong(cursor.getColumnIndex(mediaColumns[2]))
        } else {
            0
        }
        cursor.close()
        return Media(uri.toString(), createdDate, fileSize, duration)
    }

//    private fun getMedia(uri: Uri): Media {
//        return if (uri.toString().contains("images")) {
//            Media.Image(
//                    uri.toString(),
//                    Date().time,
//                    0L
//            )
//        } else {
//            Media.Video(
//                    uri.toString(),
//                    Date().time,
//                    0L
//            )
//        }
//    }

//    private fun getImageDetails(imageUri: Uri, fileDirectory: File): Media {
//
//        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//
//        // Get the cursor
//        val cursor = contentResolver.query(imageUri,
//                filePathColumn, null, null, null)
//        // Move to first row
//        cursor.moveToFirst()
//
//        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//        val imgDecodableString = cursor.getString(columnIndex)
//
//        val fileGallery = File(imgDecodableString)
//        val bitmap = BitmapFactory.decodeFile(fileGallery.absolutePath)
//
//        cursor.close()
//
//        val imageName = UUID.randomUUID().toString() + ".PNG"
//
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
//        val b = baos.toByteArray()
//
//        val file = File(fileDirectory, imageName)
//
//        try {
//            val out = FileOutputStream(file)
//            out.write(b)
//            out.flush()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//
//        val uri = Uri.fromFile(file)
//        val fileSize = file.inputStream().available()
//        return Media.Image(
//                uri.toString(),
//                Date().time,
//                fileSize.toLong()
//        )
//    }

    companion object {

        private const val IMAGE_CHOOSER_REQUEST_CODE = 1

        private const val READ_PERMISSION_REQUEST_CODE = 100
        private const val READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

        private const val WRITE_PERMISSION_REQUEST_CODE = 101
        private const val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

    }
}