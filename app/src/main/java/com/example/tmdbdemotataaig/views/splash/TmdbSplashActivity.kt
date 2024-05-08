package com.example.tmdbdemotataaig.views.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tmdbdemotataaig.R
import com.example.tmdbdemotataaig.utils.AppUtilsKotlin
import com.example.tmdbdemotataaig.utils.GlobalConfigs
import com.example.tmdbdemotataaig.views.home.HomeActivity

class TmdbSplashActivity : AppCompatActivity() {

    private lateinit var contLayoutOnlineView: ConstraintLayout
    private lateinit var contLayoutOfflineView: ConstraintLayout
    private lateinit var btnGoOfflineMode: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmdb_splash)
        contLayoutOnlineView = findViewById(R.id.cl_online_view)
        contLayoutOfflineView = findViewById(R.id.cl_offline_view)
        btnGoOfflineMode = findViewById(R.id.btn_offline_mode)
        startSplashHandler()
        initClickListeners()
    }

    private fun startSplashHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (AppUtilsKotlin.isNetworkAvailable(this)) {
                goToHomeScreen()
            } else {
                showOfflineView()
            }
        }, GlobalConfigs.SPLASH_TIMEOUT)
    }

    private fun initClickListeners() {
        btnGoOfflineMode.setOnClickListener {
            goToHomeScreen()
        }
    }

    private fun goToHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showOfflineView() {
        contLayoutOnlineView.visibility = View.GONE
        contLayoutOfflineView.visibility = View.VISIBLE
    }
}