package com.example.tmdbdemotataaig.views.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tmdbdemotataaig.utils.AppUtilsKotlin
import com.example.tmdbdemotataaig.views.offline_movies_fragment.OfflineMoviesFragment
import com.example.tmdbdemotataaig.views.popular_movies_fragment.PopularMoviesFragment

class HomeTabsAdapter(
    fragmentManager: FragmentManager,
    lifeCycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifeCycle) {

    private val mTag = HomeTabsAdapter::class.java.simpleName.toString()
    override fun getItemCount(): Int {
        return 2 // Number of tabs/fragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AppUtilsKotlin.showLog(mTag, "returning the Popular Movies fragment")
                PopularMoviesFragment()
            }

            1 -> {
                AppUtilsKotlin.showLog(mTag, "returning the Offline Movies Tab fragment")
                OfflineMoviesFragment()

            }

            else -> {
                AppUtilsKotlin.showLog(mTag, "returning the Popular Movies fragment")
                PopularMoviesFragment()
            }
        }
    }


}