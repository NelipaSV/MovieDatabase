package com.nelipa.moviedatabase.utility

import android.content.res.Resources
import com.nelipa.moviedatabase.R
import javax.inject.Inject

class Validator @Inject constructor(private val resources: Resources) {

    fun validateNotBlank(text: String?): String? {
        return with(resources){
            when {
                text.isNullOrBlank() -> getString(R.string.fragment_movies_list_search_error_blank)
                else -> null
            }
        }
    }
}