package com.moviemaker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType.FIT_CENTER
import android.widget.LinearLayout
import com.moviemaker.datasource.LocalMediaDataSource
import com.moviemaker.domain.Media
import com.moviemaker.interactor.GetMediaList
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity() {

    private var imageContainer: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)

        imageContainer = findViewById(R.id.imageContainer)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { _ ->
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            val chooser = Intent.createChooser(pickPhoto, "Choose a Picture")
            startActivityForResult(chooser, 1)
        }

        val getMediaList = GetMediaList(LocalMediaDataSource())
        getMediaList.execute {
            it.mapIndexed { index: Int, media: Media ->
                Timber.d("Bipin - Index: $index, Media: $media")
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data.data
            val imageView = ImageView(this)
            imageView.scaleType = FIT_CENTER
            imageView.setImageURI(selectedImageUri)

            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            imageContainer?.setPadding(2,2,2,2)
            imageContainer?.addView(imageView, layoutParams)
            val media = getImageDetails(selectedImageUri)
            Timber.d("Bipin - Media: $media")
        }
    }

    fun getImageDetails(contentUri: Uri): Media.Image {
        // TODO: Find few more detail form Uri
        return Media.Image(
            contentUri.path,
            contentUri.encodedPath,
            Date().time,
            0
        )
    }
}