package com.example.tmdbdemotataaig.views.movie_detailed_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.example.tmdbdemotataaig.R
import com.example.tmdbdemotataaig.utils.AppUtilsKotlin
import com.example.tmdbdemotataaig.utils.GlobalConfigs
import com.example.tmdbdemotataaig.utils.MoviesUrls
import com.example.tmdbdemotataaig.views.home.model.MovieModel
import com.google.gson.Gson
import kotlin.math.roundToInt

class MovieDetailedViewFragment : Fragment() {


    private val mTag = MovieDetailedViewFragment::class.java.simpleName
    private var mMovieItemModel: MovieModel? = null
    private lateinit var imgViewMovieThumbnail: AppCompatImageView
    private lateinit var tvMovieTitle: AppCompatTextView
    private lateinit var tvMovieLanguage: AppCompatTextView
    private lateinit var tvMovieRatings: AppCompatTextView
    private lateinit var tvMovieReleaseDate: AppCompatTextView
    private lateinit var tvMovieOverView: AppCompatTextView

    /**
     * Fragment constructor*/
    fun newInstance(): MovieDetailedViewFragment {
        return MovieDetailedViewFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detailed_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        getBundleData()
    }

    private fun getBundleData() {
        val bundle = this.arguments
        if (bundle != null) {
            val movieListItemModelJson = bundle.getString(GlobalConfigs.MOVIE_LIST_ITEM_KEY, null)
            mMovieItemModel =
                Gson().fromJson(movieListItemModelJson, MovieModel::class.java)
            mMovieItemModel?.let { initDataInViews(it) }
        } else {
            AppUtilsKotlin.showLog(mTag, "Bundle is null")
        }
    }

    private fun initViews(view: View) {
        imgViewMovieThumbnail = view.findViewById(R.id.img_movie_thumbnail)
        tvMovieTitle = view.findViewById(R.id.tv_movie_title)
        tvMovieLanguage = view.findViewById(R.id.tv_movie_language_value)
        tvMovieRatings = view.findViewById(R.id.tv_movie_rating_value)
        tvMovieReleaseDate = view.findViewById(R.id.tv_movie_release_date_value)
        tvMovieOverView = view.findViewById(R.id.tv_movie_overview_value)
    }

    private fun initDataInViews(movieModel: MovieModel) {
        try {
            Glide.with(requireContext())
                .load("${MoviesUrls.BASE_URL_FOR_IMAGES}${movieModel.posterPath}")
                .into(imgViewMovieThumbnail)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        tvMovieTitle.text = movieModel.title.toString()
        tvMovieLanguage.text = movieModel.originalLanguage.toString()
        tvMovieRatings.text = (movieModel.voteAverage?.times(10)?.roundToInt()).toString() + "%"
        tvMovieReleaseDate.text = movieModel.releaseDate.toString()
        tvMovieOverView.text = movieModel.overview
    }
}