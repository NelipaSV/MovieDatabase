package com.nelipa.moviedatabase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelipa.moviedatabase.constants.Constants.MOVIE_ADAPTER_LOADER
import com.nelipa.moviedatabase.constants.Constants.MOVIE_ADAPTER_MOVIE
import com.nelipa.moviedatabase.databinding.ItemLoadingBinding
import com.nelipa.moviedatabase.databinding.ItemMovieBinding
import com.nelipa.moviedatabase.models.movie.MovieDB
import javax.inject.Inject
import kotlin.collections.ArrayList

class MoviesAdapter @Inject constructor(
    private val onMovieSelected:(MovieDB) -> Unit,
    private val onLoadMore:() -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var moviesList = ArrayList<MovieDB?>()
    private var layoutInflater: LayoutInflater? = null
    private lateinit var diffCall: DiffCallbackMovies
    private lateinit var diffResult: DiffUtil.DiffResult

    private val visibleThreshold = 1
    private var loading: Boolean = false

    fun makeDiffAnim(newList: List<MovieDB>) {
        diffCall = DiffCallbackMovies(moviesList, newList)
        diffResult = DiffUtil.calculateDiff(diffCall)
        this.moviesList.apply {
            clear()
            addAll(newList)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            moviesList[position] == null -> {
                MOVIE_ADAPTER_LOADER
            }
            else -> {
                MOVIE_ADAPTER_MOVIE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.context)
        return when(type){
            MOVIE_ADAPTER_MOVIE -> MovieViewHolder(
                ItemMovieBinding.inflate(layoutInflater!!, parent, false), onMovieSelected = onMovieSelected
            )
            MOVIE_ADAPTER_LOADER -> ProgressBarViewHolder(
                ItemLoadingBinding.inflate(layoutInflater!!, parent, false)
            )
            else -> MovieViewHolder(
                ItemMovieBinding.inflate(layoutInflater!!, parent, false), onMovieSelected = onMovieSelected
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            MOVIE_ADAPTER_MOVIE -> {
                val movie = moviesList[position]
                movie?.let {
                    (holder as MovieViewHolder).bind(it)
                }
            }
            MOVIE_ADAPTER_LOADER -> (holder as ProgressBarViewHolder)
        }
    }


    inner class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieSelected:(MovieDB) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var movieDB: MovieDB

        init {
            binding.root.setOnClickListener {
                onMovieSelected.invoke(movieDB)
            }
        }

        fun bind(movie: MovieDB) {
            movieDB = movie

            binding.movie = movie
            binding.executePendingBindings()
        }
    }

    inner class ProgressBarViewHolder(
        binding: ItemLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.progressBarLoading.isIndeterminate = true
        }
    }

    fun setOnLoadListener(recyclerView: RecyclerView?) {
        var lastVisibleItem: Int
        var totalItemCount: Int
        if (recyclerView?.layoutManager is LinearLayoutManager) {

            val linearLayoutManager = recyclerView
                .layoutManager as LinearLayoutManager

            recyclerView
                .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView,
                                            dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        totalItemCount = linearLayoutManager.itemCount
                        lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition()
                        if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold && moviesList.size > 0) {
                            onLoadMore.invoke()
                            loading = true
                        }
                    }
                })
        }
    }

    fun setLoaded() {
        loading = false
    }

    fun addLoadProgressBar() {
        moviesList.add(null)
        notifyItemInserted(moviesList.size - 1)
    }

    fun removeLoadProgressBar() {
        if (moviesList.size > 0) {
            if (moviesList[moviesList.size - 1] == null) {
                moviesList.removeAt(moviesList.size - 1)
                notifyItemRemoved(moviesList.size)
            }
        }
    }

    class DiffCallbackMovies(
        private val oldList: List<MovieDB?>,
        private val newList: List<MovieDB?>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldList[oldItemPosition]?.id == newList[newItemPosition]?.id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }
    }
}