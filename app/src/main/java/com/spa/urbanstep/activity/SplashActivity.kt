package com.spa.urbanstep.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.spa.urbanstep.R
import com.spa.urbanstep.prefrences.AppPreference


class SplashActivity : AppCompatActivity() {
    val SPLASH_TIMEOUT = 3000
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

/*        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions*/

        handler = Handler()
        handler!!.postDelayed(nextScreenRunnable, SPLASH_TIMEOUT.toLong())
    }

    private val nextScreenRunnable = Runnable {
        // startActivity(Intent(this, DashboardActivity::class.java))
        if (AppPreference.getInstance().isLogin) {
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}
