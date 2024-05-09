package com.example.tmdbdemotataaig.views.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbdemotataaig.R
import com.example.tmdbdemotataaig.listeners.MovieItemClickListener
import com.example.tmdbdemotataaig.utils.AppUtilsKotlin
import com.example.tmdbdemotataaig.utils.GlobalConfigs
import com.example.tmdbdemotataaig.views.home.adapter.PopularMoviesAdapter
import com.example.tmdbdemotataaig.views.home.model.MovieModel
import com.example.tmdbdemotataaig.views.movie_detailed_view.MovieDetailedViewFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), MovieItemClickListener {
    private val mTag = HomeActivity::class.java.simpleName.toString()
    private lateinit var mMoviesViewModel: PopularMoviesViewModel
    private lateinit var mPopularMoviesAdapter: PopularMoviesAdapter
    private lateinit var moviesListRecyclerView: RecyclerView
    private lateinit var offlineModeTv: AppCompatTextView
    private lateinit var customToolbar: Toolbar
    private var isLastPage = false
    private var currentPage = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.tabs_text_icon, theme)
        setContentView(R.layout.activity_home)
        initViews()
        initListeners()
        initData()
        initObservers()
        initMoviesListAdapter()
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
                            if (AppUtilsKotlin.isNetworkAvailable(this@HomeActivity)) {
                                fetchMoviesList(currentPage)
                            } else {
                                fetchMoviesFromDatabase(currentPage)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun initData() {
        mMoviesViewModel = ViewModelProvider(this)[PopularMoviesViewModel::class.java]
    }

    private fun initViews() {
        moviesListRecyclerView = findViewById(R.id.rv_popular_movies)
        customToolbar = findViewById(R.id.custom_toolbar)
        offlineModeTv = findViewById(R.id.txt_offline_mode_message)
        setSupportActionBar(customToolbar)
        supportActionBar?.apply {
            title = "T M D B"
            setDisplayHomeAsUpEnabled(false) // hide back button
        }
    }

    private fun initObservers() {
        mMoviesViewModel.getPopularMoviesList()?.observe(this) {
            if (it != null && it.isNotEmpty()) {
                mMoviesViewModel.isLoadingMutable.value = false
                // add the data in the adapter
                mPopularMoviesAdapter.addMovies(it)
                hideOfflineViewShowMoviesView()
            } else {
                AppUtilsKotlin.showLog(mTag, "The movies list is empty or null.")
            }
        }

        mMoviesViewModel.getInternetConnectionStatus().observe(this) {
            if (it == false) {
                mPopularMoviesAdapter.addMovies(arrayListOf())
                showOfflineViewHideMoviesView()
                AppUtilsKotlin.showLog(mTag, "No internet connection.")
            } else {
                hideOfflineViewShowMoviesView()
            }
        }
    }

    private fun showOfflineViewHideMoviesView() {
        offlineModeTv.visibility = View.VISIBLE
        moviesListRecyclerView.visibility = View.GONE
    }

    private fun hideOfflineViewShowMoviesView() {
        offlineModeTv.visibility = View.GONE
        moviesListRecyclerView.visibility = View.VISIBLE
    }

    private fun initMoviesListAdapter() {
        // putting span count as 3 as of now, can be configurable in style.xml
        moviesListRecyclerView.layoutManager = GridLayoutManager(this, 3)
        mPopularMoviesAdapter = PopularMoviesAdapter(
            this, this
        )
        moviesListRecyclerView.adapter = mPopularMoviesAdapter
    }

    private fun fetchMoviesList(pageNumber: Int) {
        mMoviesViewModel.getPopularMovies(this, pageNumber)
    }

    override fun onResume() {
        super.onResume()
        if (::mMoviesViewModel.isInitialized) {
            fetchMoviesList(1)
        } else {
            AppUtilsKotlin.showLog(mTag, "view model isn't initialized")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.offline_mode_item -> {
                if (AppUtilsKotlin.isNetworkAvailable(this)) {
                    AppUtilsKotlin.showToast(this, "Internet is Available, showing movies Online")
                } else {
                    mPopularMoviesAdapter.addMovies(arrayListOf())
                    fetchMoviesFromDatabase(1)
                }
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }


    override fun onMovieItemClick(movieModel: MovieModel) {
        val movieListItemModelBundle = Bundle()
        movieListItemModelBundle.putString(
            GlobalConfigs.MOVIE_LIST_ITEM_KEY,
            Gson().toJson(movieModel)
        )
        val movieDetailedFragment = MovieDetailedViewFragment().newInstance()
        movieDetailedFragment.arguments = movieListItemModelBundle
        pushFragment(
            GlobalConfigs.MOVIE_DETAILS_SCREEN_KEY,
            movieDetailedFragment
        )
    }

    /**
     * method to PUSH the fragment
     */
    private fun pushFragment(key: String, mFragment: Fragment?) {
        try {
            if (mFragment != null) {
                // Begin the transaction
                val transaction = supportFragmentManager.beginTransaction()
                // add fragment on frame-layout
                transaction.add(R.id.tile_fragment_container, mFragment)
                transaction.addToBackStack(null)
                // Commit the transaction
                transaction.commitAllowingStateLoss()
                // add fragment into flow stack
            } else AppUtilsKotlin.showErrorLog(
                mTag, "Push Fragment is null or fragment already pushed."
            )
        } catch (e: Exception) {
            // catch exception while pushing fragment
            e.printStackTrace()
        }
    }

    private fun fetchMoviesFromDatabase(pageSize: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            mMoviesViewModel.getMoviesFromDatabase(this@HomeActivity, pageSize)
        }
    }
}