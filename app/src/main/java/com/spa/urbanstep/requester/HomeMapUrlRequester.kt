package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.HomeMapUrl
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class HomeMapUrlRequester : Runnable {
    override fun run() {
        var czResponse: CZResponse<ResponseBean<HomeMapUrl>>? = null
        czResponse = ApiHelper.performHomeMapApi() as CZResponse<ResponseBean<HomeMapUrl>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {

                EventBus.getDefault().post(EventObject(EventConstant.HOME_MAP_URL_SUCCESS, czResponse!!.getResponse().data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.HOME_MAP_URL_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}