package com.example.tmdbdemotataaig.views.popular_movies_fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbdemotataaig.R
import com.example.tmdbdemotataaig.listeners.MovieItemClickListener
import com.example.tmdbdemotataaig.utils.AppUtilsKotlin
import com.example.tmdbdemotataaig.utils.GlobalConfigs
import com.example.tmdbdemotataaig.views.home.HomeActivity
import com.example.tmdbdemotataaig.views.movie_detailed_view.MovieDetailedViewFragment
import com.example.tmdbdemotataaig.views.popular_movies_fragment.adapter.PopularMoviesAdapter
import com.example.tmdbdemotataaig.views.popular_movies_fragment.model.MovieModel
import com.google.gson.Gson

class PopularMoviesFragment : Fragment(), MovieItemClickListener {
    private val mTAG = PopularMoviesFragment::class.java.simpleName.toString()
    private lateinit var mMoviesViewModel: PopularMoviesViewModel
    private lateinit var mPopularMoviesAdapter: PopularMoviesAdapter
    private lateinit var moviesListRecyclerView: RecyclerView
    private var isLastPage = false
    private var currentPage = 1

    /**
     * Activity instance
     */
    private var mActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mActivity = activity
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initData()
        initObservers()
        initMoviesListAdapter()
        initListeners()
    }

    private fun initListeners() {
        moviesListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (mMoviesViewModel.isLoadingMutable.value == false && !isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            currentPage++
                            fetchMoviesList(currentPage)
                        }
                    }
                }
            }
        })
    }

    private fun initViews(view: View) {
        moviesListRecyclerView = view.findViewById(R.id.rv_popular_movies)
    }

    private fun initData() {
        mMoviesViewModel = ViewModelProvider(this)[PopularMoviesViewModel::class.java]
    }

    private fun initObservers() {
        mMoviesViewModel.getPopularMoviesList()?.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                mMoviesViewModel.isLoadingMutable.value = false
                // add the data in the adapter
                mPopularMoviesAdapter.addMovies(it)
            } else {
                AppUtilsKotlin.showToast(requireActivity(), "The movies list is empty or null.")
            }
        }
    }

    private fun initMoviesListAdapter() {
        // putting span count as 2 as of now, can be configurable in style.xml
        moviesListRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        mPopularMoviesAdapter = PopularMoviesAdapter(
            requireActivity(), this
        )
        moviesListRecyclerView.adapter = mPopularMoviesAdapter
    }

    override fun onStart() {
        super.onStart()
        // calling the get movies api in onStart() as onResume will get invoke every time in tab switching
        if (::mMoviesViewModel.isInitialized) {
            fetchMoviesList(1)
        } else {
            AppUtilsKotlin.showLog(mTAG, "view model isn't initialized")
        }
    }

    private fun fetchMoviesList(pageNumber: Int) {
        mMoviesViewModel.getPopularMovies(requireActivity(), pageNumber)
    }

    override fun onMovieItemClick(movieModel: MovieModel) {
        if (mActivity != null) {
            val movieListItemModelBundle = Bundle()
            movieListItemModelBundle.putString(
                GlobalConfigs.MOVIE_LIST_ITEM_KEY,
                Gson().toJson(movieModel)
            )
            val movieDetailedFragment = MovieDetailedViewFragment().newInstance()
            movieDetailedFragment.arguments = movieListItemModelBundle
            (mActivity as HomeActivity).pushFragment(
                GlobalConfigs.MOVIE_DETAILS_SCREEN_KEY,
                movieDetailedFragment
            )
        } else {
            AppUtilsKotlin.showLog(mTAG, "mActivity is null")
        }
    }

}
