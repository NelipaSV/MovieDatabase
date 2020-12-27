package com.nelipa.moviedatabase.utility

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

@BindingAdapter(value = ["url", "isCircle"], requireAll = false)

fun ImageView.bindImageUrl(url: String?, isCircle: Boolean = true) {
    if (url != null && url.isNotBlank()) {
        val fullURL = "https://image.tmdb.org/t/p/original/${url}"
        if (isCircle) {
            Glide.with(this)
                .load(fullURL)
                .transform(CircleCrop())
                .into(this)
        } else {
            Glide.with(this)
                .load(fullURL)
                .into(this)
        }
    }
}