package com.moviemaker.extension

import android.net.Uri

fun Uri.isImageUri() = this.toString().contains("images")