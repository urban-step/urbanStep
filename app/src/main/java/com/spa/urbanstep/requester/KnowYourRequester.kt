package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.Zone
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class KnowYourRequester :Runnable{

    override fun run() {
        var czResponse: CZResponse<ResponseBean<List<Zone>>>? = null
        czResponse = ApiHelper.performKnowYourAreaList() as CZResponse<ResponseBean<List<Zone>>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {

                EventBus.getDefault().post(EventObject(EventConstant.KNOW_YOUR_LIST_SUCCESS,  czResponse!!.getResponse().data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.KNOW_YOUR_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }

    }
}