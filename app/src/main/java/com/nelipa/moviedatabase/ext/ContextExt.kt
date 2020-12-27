package com.nelipa.moviedatabase.ext

import android.content.Context
import android.widget.Toast

fun Context.makeToast(textToToast: String) {
    Toast.makeText(this, textToToast, Toast.LENGTH_LONG).show()
}