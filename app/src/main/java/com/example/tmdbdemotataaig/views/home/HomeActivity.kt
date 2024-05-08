package com.example.tmdbdemotataaig.views.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tmdbdemotataaig.R
import com.example.tmdbdemotataaig.utils.AppUtilsKotlin
import com.example.tmdbdemotataaig.utils.GlobalConfigs
import com.example.tmdbdemotataaig.views.home.adapter.HomeTabsAdapter
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {
    private val mTag = HomeActivity::class.java.simpleName.toString()
    private lateinit var mViewPager2: ViewPager2
    private lateinit var mTabLayout: TabLayout
    private lateinit var mHomeTabsAdapter: HomeTabsAdapter
    private val tabIconsUnfilled = arrayOf(
        R.drawable.ic_online_movies_unselected, R.drawable.ic_offline_movies_unselected
    )

    private val tabIconsFilled = arrayOf(
        R.drawable.ic_online_movies_selected, R.drawable.ic_offline_movies_selected
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.tabs_text_icon, theme)
        setContentView(R.layout.activity_home)
        initViews()
        setupViewPagerAdapter()
        initListeners()
    }

    private fun initListeners() {
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    mViewPager2.currentItem = tab.position
                    when (tab.position) {
                        0 -> {
                            tab.icon = ContextCompat.getDrawable(
                                this@HomeActivity, tabIconsFilled[0]
                            )
                        }

                        1 -> {
                            tab.icon = ContextCompat.getDrawable(
                                this@HomeActivity, tabIconsFilled[1]
                            )
                        }
                    }
                } else {
                    AppUtilsKotlin.showLog(mTag, "tab is null in onTabSelected()")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> {
                            tab.icon =
                                ContextCompat.getDrawable(this@HomeActivity, tabIconsUnfilled[0])
                        }

                        1 -> {
                            tab.icon =
                                ContextCompat.getDrawable(this@HomeActivity, tabIconsUnfilled[1])
                        }
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        mViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (AppUtilsKotlin.isNetworkAvailable(this@HomeActivity)) {
                    mTabLayout.selectTab(
                        mTabLayout.getTabAt(position)
                    )
                } else {
                    mTabLayout.selectTab(
                        mTabLayout.getTabAt(1)
                    )
                }

            }
        })
    }

    private fun initViews() {
        mTabLayout = findViewById(R.id.tab_layout_movies)
        mViewPager2 = findViewById(R.id.view_pager_movies)
        mViewPager2.isUserInputEnabled = false

    }

    private fun setupViewPagerAdapter() {
        mHomeTabsAdapter = HomeTabsAdapter(supportFragmentManager, lifecycle)
        mTabLayout.addTab(
            mTabLayout.newTab().setText("Popular Movies").setIcon(tabIconsUnfilled[0])
        )
        mTabLayout.addTab(
            mTabLayout.newTab().setText("Offline Movies").setIcon(tabIconsUnfilled[1])
        )
        mViewPager2.adapter = mHomeTabsAdapter
    }

    /**
     * method to PUSH the fragment
     */
    fun pushFragment(key: String, mFragment: Fragment?) {
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

    /**
     *  method to pop the fragment
     */
    private fun popFragment(mFragment: Fragment?, isNeedToRemoveFromStack: Boolean) {
        try {
            if (mFragment != null) {
                // Begin the transaction
                val childFm = mFragment.parentFragment?.childFragmentManager
                if (childFm != null && childFm.backStackEntryCount > 0) {
                    AppUtilsKotlin.showErrorLog(
                        mTag,
                        "Pop child fragment as child fragments count is ${childFm.backStackEntryCount}."
                    )
                    val ft = childFm.beginTransaction()
                    // remove fragment from view
                    ft.remove(mFragment)
                    ft.detach(mFragment)
                    ft.commit()

                    childFm.popBackStack()
                } else {
                    AppUtilsKotlin.showErrorLog(mTag, "Pop fragment : $mFragment")
                    // Begin the transaction
                    val ft = supportFragmentManager.beginTransaction()
                    // remove fragment from view
                    ft.remove(mFragment)
                    ft.detach(mFragment)
                    ft.commit()
                    supportFragmentManager.popBackStack()
                }
            } else AppUtilsKotlin.showErrorLog(mTag, "Pop fragment is null.")
        } catch (e: Exception) {
            // catch exception while removing fragment
            e.printStackTrace()
        }
    }
}