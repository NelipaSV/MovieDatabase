package com.nelipa.moviedatabase.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import java.lang.ref.SoftReference

abstract class BaseFragment : Fragment() {

    lateinit var mNavigationCallback: SoftReference<NavigationCallback>

    interface NavigationCallback {
        fun navigate(direction: NavDirections)
        fun popFragment(destination: Int? = null)
        fun showLoader()
        fun hideLoader()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mNavigationCallback = SoftReference(context as NavigationCallback)
    }

    protected fun navigate(direction: NavDirections) {
        mNavigationCallback.get()?.navigate(direction)
    }

    protected fun popFragment(destination: Int? = null) {
        mNavigationCallback.get()?.popFragment(destination)
    }

    protected fun showLoader() {
        mNavigationCallback.get()?.showLoader()
    }

    protected fun hideLoader() {
        mNavigationCallback.get()?.hideLoader()
    }
}