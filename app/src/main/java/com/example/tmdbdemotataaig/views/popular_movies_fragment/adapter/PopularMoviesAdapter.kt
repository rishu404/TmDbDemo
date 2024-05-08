package com.example.tmdbdemotataaig.views.popular_movies_fragment.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tmdbdemotataaig.R
import com.example.tmdbdemotataaig.listeners.MovieItemClickListener
import com.example.tmdbdemotataaig.utils.MoviesUrls
import com.example.tmdbdemotataaig.views.popular_movies_fragment.model.MovieModel

class PopularMoviesAdapter(
    private val activity: Activity,
    private val movieItemClickListener: MovieItemClickListener
) :
    RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder>() {
    private val moviesList: ArrayList<MovieModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_list_item, parent, false)
        return PopularMoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        val movieItem = moviesList[position]
        holder.movieTitle.text = truncateText(movieItem.title.toString())
        holder.movieReleaseDate.text = movieItem.releaseDate.toString()
        try {
            Glide.with(activity.applicationContext)
                .load("${MoviesUrls.BASE_URL_FOR_IMAGES}${movieItem.posterPath}")
                .into(holder.movieThumbnail)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initClickListeners(holder, movieItem)
    }

    private fun initClickListeners(
        holder: PopularMoviesAdapter.PopularMoviesViewHolder,
        movieItem: MovieModel
    ) {
        holder.itemView.setOnClickListener {
            movieItemClickListener.onMovieItemClick(movieItem)
        }
    }

    fun addMovies(moviesArrayList: ArrayList<MovieModel>) {
        var isAlreadyInList = false
        for (k in moviesArrayList.indices) {
            for (j in moviesList.indices) {
                if (moviesArrayList[k].id?.equals(moviesList[j].id) == true) {
                    isAlreadyInList = true
                    break
                }
            }
            if (!isAlreadyInList) {
                moviesList.add(moviesArrayList[k])
            }
        }
        notifyDataSetChanged()
    }

    /**
     * Description: Truncates the given text if it exceeds the length 15 and appends
     * ellipsis.
     */
    private fun truncateText(text: String): String {
        return if (text.length >= 12) {
            text.substring(0, 12) + "..."
        } else {
            text
        }
    }

    inner class PopularMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieThumbnail: AppCompatImageView = itemView.findViewById(R.id.img_movie_thumbnail)
        val movieTitle: AppCompatTextView = itemView.findViewById(R.id.tv_movie_title)
        var movieReleaseDate: AppCompatTextView = itemView.findViewById(R.id.tv_movie_release_date)
    }

}

