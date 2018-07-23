package com.moviemaker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.coremedia.iso.IsoFile
import com.coremedia.iso.boxes.MetaBox
import com.googlecode.mp4parser.FileDataSourceImpl
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.Track
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.googlecode.mp4parser.authoring.tracks.AppendTrack
import com.jakewharton.rxbinding2.support.v7.widget.itemClicks
import com.jakewharton.rxbinding2.view.clicks
import com.moviemaker.domain.Media
import com.moviemaker.extension.isImageUri
import com.moviemaker.extension.showMediaChooser
import com.moviemaker.extension.showVideo
import com.moviemaker.ui.media.MediaGridView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotterknife.bindView
import timber.log.Timber
import java.io.File
import java.io.RandomAccessFile
import java.util.*


class MainActivity : AppCompatActivity() {

    // private members
    private val rxPermissions: RxPermissions by lazy {
        RxPermissions(this)
    }

    private val compositeDisposable = CompositeDisposable()

    // views
    private val mediaGridView: MediaGridView by bindView(R.id.media_grid_view)
    private val addImageButton: FloatingActionButton by bindView(R.id.add_image_button)
    private val toolbar: Toolbar by bindView(R.id.toolbar)


    // Override functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.main_menu)

        toolbar.menu.findItem(R.id.create_movie).isVisible = false

        toolbar.itemClicks()
                .subscribe { menuItem ->
                    when (menuItem.itemId) {
                        R.id.create_movie -> {
                            createMovie()
                        }
                        else -> {
                            //Do nothing
                        }
                    }
                }
                .addTo(compositeDisposable)

        addImageButton
                .clicks()
                .subscribe {
                    showMediaChooser(IMAGE_CHOOSER_REQUEST_CODE)
                }
                .addTo(compositeDisposable)


        rxPermissions.request(READ_PERMISSION, WRITE_PERMISSION)
                .filter { true }
                .subscribe {
                    mediaGridView.loadMediaList { mediaListSize ->
                        showCreatMovieMenu(mediaListSize >= 2)
                    }
                }
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
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
                        mediaGridView.hideLoading()
                        mediaGridView.addMedia(media) { mediaListSize ->
                            showCreatMovieMenu(mediaListSize >= 2)
                        }

                    }
                    .subscribe()
        }
    }

    private fun showCreatMovieMenu(visible: Boolean) {
        toolbar.menu
                .findItem(R.id.create_movie)
                .isVisible = visible
    }

    private fun retrieveMediaDetail(uri: Uri): Media? {
        val isVideo = !uri.isImageUri()

        val mediaColumns = arrayListOf(
                MediaStore.MediaColumns.DATA,
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
        val path = cursor.getString(cursor.getColumnIndex(mediaColumns[0]))
        val createdDate = cursor.getLong(cursor.getColumnIndex(mediaColumns[1]))
        val fileSize = cursor.getLong(cursor.getColumnIndex(mediaColumns[2]))
        val duration = if (isVideo) {
            cursor.getLong(cursor.getColumnIndex(mediaColumns[3]))
        } else {
            0
        }
        cursor.close()

        return Media(uri.toString(), path, createdDate, fileSize, duration)

    }

    private fun getMetaBoxVideos(): Observable<List<Media>> {
        return Observable.create<List<Media>> { emitter ->
            val mediaList = mediaGridView.getMediaList()
            val metaboxEnabledMedia = mutableListOf<Media>()
            mediaList.forEach { media ->
                val isoFile = IsoFile(media.path)
                val movieBox = isoFile.movieBox
                if (movieBox.getBoxes(MetaBox::class.java).isNotEmpty()) {
                    metaboxEnabledMedia.add(media)
                }
                isoFile.close()

            }
            emitter.onNext(metaboxEnabledMedia.toList())
        }

    }


    private fun createMovie() {
//         FIX: way to dismiss loading indicator when there is empty list
//        mediaGridView.showLoading()
        getMetaBoxVideos()
                .filter { it.isNotEmpty() }
                .map { mediaList ->
                    val videoTracks = mutableListOf<Track>()
                    val audioTracks = mutableListOf<Track>()
                    mediaList.forEach { media ->
                        Timber.d("Bipin - Uri: ${media.uriString}, path: ${media.path}")
                        val fileDataSourceImpl = FileDataSourceImpl(media.path)
                        val movie = MovieCreator.build(fileDataSourceImpl)
                        movie.tracks.forEach {
                            if (it.handler == "vide") {
                                videoTracks.add(it)
                            } else if (it.handler == "soun") {
                                audioTracks.add(it)
                            }
                        }
                    }
                    val finalMovie = Movie()

                    if (audioTracks.isNotEmpty()) finalMovie.addTrack(AppendTrack(*audioTracks.toTypedArray()))
                    if (videoTracks.isNotEmpty()) finalMovie.addTrack(AppendTrack(*videoTracks.toTypedArray()))

                    val container = DefaultMp4Builder().build(finalMovie)
                    val file = File(
                            Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_MOVIES
                            ).absolutePath, "MM-${Date().time}.mp4")
                    val fileChannel = RandomAccessFile(file, "rw").channel
                    container.writeContainer(fileChannel)
                    fileChannel.close()
                    file.absolutePath
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { filePath ->
                    mediaGridView.hideLoading()
                    showVideo(filePath)
                }
                .subscribe()
                .addTo(compositeDisposable)
    }

    companion object {

        private const val IMAGE_CHOOSER_REQUEST_CODE = 1

        private const val READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

        private const val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

    }
}