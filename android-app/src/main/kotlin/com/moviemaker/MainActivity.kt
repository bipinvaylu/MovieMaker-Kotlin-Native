package com.moviemaker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import java.util.Date

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helloTextView = findViewById<TextView>(R.id.hello_txt)
        helloTextView.text = Hello().get()

        val image = Media.Image("PATH", "THMPath", Date().time, 12345)
        helloTextView.text = image.toString()
    }

}