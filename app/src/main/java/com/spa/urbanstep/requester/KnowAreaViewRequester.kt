package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.Zone
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class KnowAreaViewRequester(var zone_id: Int) : Runnable {
    override fun run() {
        var czResponse: CZResponse<ResponseBean<List<Zone>>>? = null
        czResponse = ApiHelper.performKnowYourAreaViewList(zone_id) as CZResponse<ResponseBean<List<Zone>>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {

                EventBus.getDefault().post(EventObject(EventConstant.KNOW_YOUR_VIEW_LIST_SUCCESS, czResponse!!.getResponse().data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.KNOW_YOUR_VIEW_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}