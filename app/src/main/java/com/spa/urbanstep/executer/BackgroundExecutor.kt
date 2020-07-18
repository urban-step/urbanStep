package com.spa.carrythistoo.executer

import com.spa.carrythistoo.utils.ConnectivityController.isNetworkAvailable
import com.spa.urbanstep.App
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by shweta on 1/6/18.
 */
class BackgroundExecutor() {

    private var _bgExecutor: BackgroundExecutor? = null
    private var _exService: ExecutorService? = null

    fun getInstance(): BackgroundExecutor {
        if (_bgExecutor == null) {
            _bgExecutor = BackgroundExecutor()
        }
        if (_exService == null) {
            _exService = Executors.newFixedThreadPool(8)
        }
        return _bgExecutor as BackgroundExecutor
    }

    fun execute(runnable: Runnable) {
        if (!isNetworkAvailable(App.applicationContext())) {
            EventBus.getDefault().post(EventObject(EventConstant.NETWORK_ERROR, ""))
            return
        }
        if (_exService == null) {
            _exService = Executors.newFixedThreadPool(8)
        }
        _exService!!.submit(runnable)
    }

    fun stop() {
        if (_exService != null) {
            _exService!!.shutdownNow()
            _exService = null
        }
    }
}