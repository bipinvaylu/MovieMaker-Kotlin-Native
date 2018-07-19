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


    // Override functions
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
    }

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
                    val media = retrieveMediaDetail(it)
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

    private fun retrieveMediaDetail(uri: Uri): Media? {
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


    companion object {

        private const val IMAGE_CHOOSER_REQUEST_CODE = 1

        private const val READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        
        private const val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

    }
}