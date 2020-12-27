package com.nelipa.moviedatabase.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.nelipa.moviedatabase.R
import com.nelipa.moviedatabase.base.BaseFragment
import com.nelipa.moviedatabase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyMainActivity : AppCompatActivity(), BaseFragment.NavigationCallback {

    private lateinit var mNavController: NavController

    private var mBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding?.root
        setContentView(view)

        initNavController()
    }


    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController
    }

    override fun showLoader() {
        mBinding?.llLoading?.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        mBinding?.llLoading?.visibility = View.GONE
    }

    override fun navigate(direction: NavDirections) {
        mNavController.navigate(direction)
    }

    override fun popFragment(destination: Int?) {
        destination?.let {
            mNavController.popBackStack(destination, false)
        } ?: mNavController.popBackStack()
    }
}