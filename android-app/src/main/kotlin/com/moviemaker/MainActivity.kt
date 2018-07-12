package com.moviemaker

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.moviemaker.domain.Media
import com.moviemaker.extension.showImageChooser
import com.moviemaker.ui.media.MediaGridView
import kotterknife.bindView
import java.util.*


class MainActivity : AppCompatActivity() {

    // private members

    // views
    private val mediaGridView: MediaGridView by bindView(R.id.media_grid_view)
    private val addImageButton: FloatingActionButton by bindView(R.id.add_image_button)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)

        addImageButton.setOnClickListener { _ ->
            showImageChooser(IMAGE_CHOOSER_REQUEST_CODE)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data.data
            val media = getImageDetails(selectedImageUri)
            mediaGridView.addMedia(media)
        }
    }

    fun getImageDetails(contentUri: Uri): Media.Image {
        var fileSize = 0
        if (contentUri.scheme == ContentResolver.SCHEME_CONTENT) {
            try {
                val fileInputStream = applicationContext?.contentResolver
                        ?.openInputStream(contentUri)
                fileSize = fileInputStream?.available() ?: 0
            } catch (e: Exception) {
               e.printStackTrace()
            }
        }
        return Media.Image(
                contentUri.path,
                Date().time,
                fileSize.toLong()
                )
    }

    companion object {
        const val IMAGE_CHOOSER_REQUEST_CODE = 1
    }

}