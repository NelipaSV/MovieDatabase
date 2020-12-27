package com.nelipa.moviedatabase.fragments.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nelipa.moviedatabase.R
import com.nelipa.moviedatabase.base.BaseFragment
import com.nelipa.moviedatabase.databinding.FragmentMovieDetailsBinding
import com.nelipa.moviedatabase.ext.snack
import com.nelipa.moviedatabase.utility.ConvertingUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    //viewModels
    private val mMovieDetailsVM: MovieDetailsVM by viewModels()

    @Inject
    lateinit var convertingUtils: ConvertingUtils

    private var snack: Snackbar? = null

    private var mBinding: FragmentMovieDetailsBinding? = null

    private val mArgs: MovieDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoader()
        mMovieDetailsVM.loadMovieDetails(mArgs.movieID)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = FragmentMovieDetailsBinding.inflate(inflater, container, false).also {
        mBinding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        observeMovie()
        observeForError()
    }

    override fun onPause() {
        super.onPause()
        snack?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun initUI() {
        mBinding?.ivBackArrow?.setOnClickListener {
            popFragment()
        }
    }

    private fun observeMovie() {
        mMovieDetailsVM.getMovieDetails().observe(viewLifecycleOwner) { movie ->
            movie?.let {
                hideLoader()
                mBinding?.movie = movie
                mBinding?.convertingUtils = convertingUtils
            }
        }
    }

    private fun observeForError() {
        mMovieDetailsVM.getLoadError().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled().let { error ->
                hideLoader()
                snack = view?.snack(
                        message = getString(
                                R.string.fragment_movies_details_load_fail,
                                error
                        ),
                        actionText = getString(R.string.fragment_movies_details_retry),
                        onClick = {
                            mMovieDetailsVM.loadMovieDetails(mArgs.movieID)
                            showLoader()
                        }
                )?.also {
                    it.show()
                }
            }
        }
    }
}