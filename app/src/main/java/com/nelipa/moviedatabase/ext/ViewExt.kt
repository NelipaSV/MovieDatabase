package com.nelipa.moviedatabase.ext

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.snack(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG
) = Snackbar.make(this, message, duration)

fun View.snack(
    message: String,
    actionText: String,
    duration: Int = Snackbar.LENGTH_INDEFINITE,
    onClick: (View) -> Unit
) = Snackbar.make(this, message, duration)
    .setAction(actionText, onClick)

inline fun EditText?.afterTextChanged(crossinline changedText: (String?) -> Unit) {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            changedText.invoke(s?.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    this?.addTextChangedListener(textWatcher)
}

inline fun EditText?.onSearchClick(crossinline onSearchClick: () -> Unit) {
    val actionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onSearchClick.invoke()
            return@OnEditorActionListener true
        }
        false
    }

    this?.setOnEditorActionListener(actionListener)
}
