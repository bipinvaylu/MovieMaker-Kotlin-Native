package com.moviemaker

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.moviemaker.domain.Media
import com.moviemaker.extension.showImageChooser
import com.moviemaker.ui.media.MediaGridView
import io.reactivex.Observable
import kotterknife.bindView
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.UUID


class MainActivity : AppCompatActivity() {

    // private members


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
            showImageChooser(IMAGE_CHOOSER_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(this, READ_PERMISSION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, WRITE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_PERMISSION, WRITE_PERMISSION),
                READ_PERMISSION_REQUEST_CODE
            )
        } else {
            mediaGridView.loadMediaList()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray
    ) {
        when (requestCode) {
            READ_PERMISSION_REQUEST_CODE,
            WRITE_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mediaGridView.loadMediaList()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CHOOSER_REQUEST_CODE
            && resultCode == Activity.RESULT_OK) {
            Observable.create<Media> {

            }

            val fileDirectory = File(Environment.getExternalStorageDirectory().absolutePath + "/media/")
            Timber.d("FileDir: $fileDirectory")
            if (!fileDirectory.exists()) {
                fileDirectory.mkdirs()
            }

            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            // Get the cursor
            val cursor = contentResolver.query(selectedImage,
                filePathColumn, null, null, null)
            // Move to first row
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val imgDecodableString = cursor.getString(columnIndex)

            val fileGallery = File(imgDecodableString)
            val bitmap = BitmapFactory.decodeFile(fileGallery.absolutePath)


            cursor.close()

            val imageName = UUID.randomUUID().toString() + ".PNG"

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
            val b = baos.toByteArray()

            val file = File(fileDirectory, imageName)

            try {
                val out = FileOutputStream(file)
                out.write(b)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val uri = Uri.fromFile(file)
            Timber.d("URI: ${uri.toString()}")
            val media = getImageDetails(uri)
//            Timber.d("Image URI: $selectedImageUri")
            mediaGridView.addMedia(media)
        }
    }

    fun getImageDetails(contentUri: Uri): Media {
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
        return Media(
            contentUri.toString(),
            Date().time,
            fileSize.toLong()
        )
    }

    companion object {

        private const val IMAGE_CHOOSER_REQUEST_CODE = 1

        private const val READ_PERMISSION_REQUEST_CODE = 100
        private const val READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

        private const val WRITE_PERMISSION_REQUEST_CODE = 101
        private const val WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

    }

}