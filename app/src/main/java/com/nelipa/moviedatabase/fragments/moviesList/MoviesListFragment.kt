package com.nelipa.moviedatabase.fragments.moviesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nelipa.moviedatabase.adapters.MoviesAdapter
import com.nelipa.moviedatabase.utility.Validator
import com.google.android.material.snackbar.Snackbar
import com.nelipa.moviedatabase.R
import com.nelipa.moviedatabase.base.BaseFragment
import com.nelipa.moviedatabase.databinding.FragmentMoviesListBinding
import com.nelipa.moviedatabase.ext.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MoviesListFragment : BaseFragment() {

    //viewModels
    private val mMainVM: MovieListVM by viewModels()

    //adapters
    private var moviesAdapter: MoviesAdapter? = null

    @Inject
    lateinit var mValidator: Validator
    private var mBinding: FragmentMoviesListBinding? = null
    private var snack: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, createBackPressCallback())
        showLoader()
        mMainVM.loadPageTrendingMovies()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = FragmentMoviesListBinding.inflate(inflater, container, false).also {
        mBinding = it
    }.root

    override fun onStart() {
        super.onStart()

        initUI()
        initAdapter()

        observeListMovies()
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
        createClickListener()?.let { listener ->
            mBinding?.ivSearchQuery?.setOnClickListener(listener)
            mBinding?.ivClearQuery?.setOnClickListener(listener)
            mBinding?.mlMoviesList?.setOnClickListener(listener)
        }

        mBinding?.tietSearchText?.afterTextChanged { queryText ->
            if (queryText.isNullOrBlank()) {
                if (mBinding?.mlMoviesList?.currentState != mBinding?.mlMoviesList?.startState) {
                    mBinding?.mlMoviesList?.transitionToStart()
                }
            } else {
                if (mBinding?.mlMoviesList?.currentState != mBinding?.mlMoviesList?.endState) {
                    mBinding?.mlMoviesList?.transitionToEnd()
                }
            }
            if (mBinding?.tilSearchText?.error != null)
                mBinding?.tilSearchText?.error = null
        }

        mBinding?.tietSearchText?.onSearchClick {
            searchQuery()
        }
    }

    private fun initAdapter() {
        moviesAdapter = MoviesAdapter(
                onMovieSelected = { movie ->
                    movie.id?.let { movieID ->
                        navigate(
                                MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(
                                        movieID
                                )
                        )
                    } ?: context?.makeToast("Movie ID is null, chose another")
                },
                onLoadMore = {
                    mMainVM.nextPage()
                    if (mMainVM.getCurrentPage() <= mMainVM.getLastPage()) {
                        moviesAdapter?.addLoadProgressBar()
                        mMainVM.loadNextPage()
                    }
                }
        )
        mBinding?.rvSearchResult?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moviesAdapter
        }
        moviesAdapter?.setOnLoadListener(mBinding?.rvSearchResult)
    }

    private fun createClickListener(): View.OnClickListener? {
        mBinding?.let { binding ->
            return View.OnClickListener {
                when (it?.id) {
                    binding.ivSearchQuery.id -> {
                        searchQuery()
                    }
                    binding.ivClearQuery.id -> {
                        clearQuery()
                    }
                    binding.mlMoviesList.id -> {
                        binding.mlMoviesList.hideKeyboard()
                    }
                }
            }
        } ?: return null
    }

    private fun searchQuery() {
        if (isValidQuery())
            mBinding?.tietSearchText?.apply {
                getTextFromQueryField()?.let { query ->
                    hideKeyboard()
                    clearFocus()
                    mMainVM.setStartingPage()
                    showLoader()
                    mMainVM.loadPageSearchMovies(query)
                }
            }
    }

    private fun clearQuery() {
        mBinding?.tietSearchText?.apply {
            hideKeyboard()
            clearFocus()
            mMainVM.setStartingPage()
            showLoader()
            mMainVM.loadPageTrendingMovies()
            setText("")
        }
    }

    private fun isValidQuery(): Boolean {
        val error = mValidator.validateNotBlank(getTextFromQueryField())
        mBinding?.tilSearchText?.error = error
        return error == null
    }

    private fun getTextFromQueryField() = mBinding?.tietSearchText?.text?.toString()

    private fun createBackPressCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(
                true
        ) {
            override fun handleOnBackPressed() {
                mBinding?.tietSearchText?.snack(
                        message = getString(R.string.fragment_movies_list_exit_text),
                        actionText = getString(R.string.fragment_movies_list_exit),
                        onClick = {
                            this.isEnabled = false
                            activity?.onBackPressed()
                        }
                )?.apply {
                    show()
                }
            }
        }
    }

    private fun observeListMovies() {
        mMainVM.getListMovies().observe(viewLifecycleOwner) { pairWithListMovies ->
            pairWithListMovies?.let {
                lifecycleScope.launch {
                    moviesAdapter?.apply {
                        removeLoadProgressBar()
                        if (it.second && mBinding?.mlMoviesList?.currentState != mBinding?.mlMoviesList?.startState) {
                            mBinding?.mlMoviesList?.awaitTransitionComplete(R.id.start_anim)
                        } else if (!it.second && mBinding?.mlMoviesList?.currentState != mBinding?.mlMoviesList?.endState) {
                            mBinding?.mlMoviesList?.awaitTransitionComplete(R.id.end_anim)
                        }
                        hideLoader()
                        makeDiffAnim(it.first)
                        setLoaded()
                    }
                }
            }
        }
    }

    private fun observeForError() {
        mMainVM.getLoadError().observe(this) { event ->
            event?.getContentIfNotHandled()?.let { error ->
                hideLoader()
                snack = view?.snack(
                        message = getString(
                                R.string.fragment_movies_list_load_fail,
                                error
                        )
                )?.apply {
                    show()
                }
            }
        }
    }
}
