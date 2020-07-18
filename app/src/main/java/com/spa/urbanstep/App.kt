package com.spa.urbanstep

import android.app.Application
import android.content.Context
import com.mapbox.mapboxsdk.MapmyIndia
import com.mmi.services.account.MapmyIndiaAccountManager
import timber.log.Timber

class App : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = App.applicationContext()

        /* LicenceManager.getInstance().setRestAPIKey("your_rest_api_key");
         LicenceManager.getInstance().setMapSDKKey("your_java_script_key");*/


        Timber.plant(Timber.DebugTree())
        MapmyIndiaAccountManager.getInstance().restAPIKey = getRestAPIKey()
        MapmyIndiaAccountManager.getInstance().mapSDKKey = getMapSDKKey()
        MapmyIndiaAccountManager.getInstance().atlasClientId = getAtlasClientId()
        MapmyIndiaAccountManager.getInstance().atlasClientSecret = getAtlasClientSecret()
        MapmyIndiaAccountManager.getInstance().atlasGrantType = getAtlasGrantType()

        /*  LicenceManager.getInstance().setRestAPIKey("your_rest_api_key");
          LicenceManager.getInstance().setMapSDKKey("your_java_script_key");
  */
        MapmyIndia.getInstance(this)
    }

    fun getRestAPIKey(): String {
        return "6k4wzsa8jz9zebnihyh3ok2uso89ulhc"
    }


    fun getMapSDKKey(): String {
        return "5klgounjtvu8ih2zfu2fefl63tr9qkl7"
    }

    fun getAtlasClientId(): String {
        return "BvAaFHmSRhhnAyrJslhgTrlhNP2MsGEn5aN-ltATXDw4UjGKhA2ijrwQd7bCqI32cGMDV5x1kA4="
    }

    fun getAtlasClientSecret(): String {
        return "_fC0aBV9eBgBnzHw901YV2pJ2eVb9hMop5Koo7h59M0fLAg6TQ8wi8RyquzGolB9fFksjx_q2nM="
    }

    fun getAtlasGrantType(): String {
        return "client_credentials"
    }
}