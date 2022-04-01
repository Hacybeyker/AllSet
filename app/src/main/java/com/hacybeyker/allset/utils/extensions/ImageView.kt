package com.hacybeyker.allset.utils.extensions

import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.hacybeyker.allset.R

/**
 * Created by Carlos Osorio on 16/06/2021
 */

fun ImageView.imageLoad(
    url: String,
    imagePlaceholder: Int = R.drawable.shape_placeholder,
    imageError: Int = R.drawable.shape_placeholder
) {
    if (url.isEmpty()) {
        return
    }

    this.imageClear()
    val uri = url.toUri().buildUpon().scheme("https").build()
    Glide.with(this.context)
        .asBitmap()
        .load(uri)
        .apply {
            placeholder(imagePlaceholder)
            error(imageError)
        }
        .into(this)
}

fun ImageView.imageClear() {
    Glide.with(this.context).clear(this)
}