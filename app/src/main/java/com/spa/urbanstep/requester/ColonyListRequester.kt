package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.Colony
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class ColonyListRequester(var colcny: String): Runnable{
    override fun run() {
        var czResponse: CZResponse<ResponseBean<List<Colony>>>? = null
        czResponse = ApiHelper.performColonyList(colcny) as CZResponse<ResponseBean<List<Colony>>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {

                EventBus.getDefault().post(EventObject(EventConstant.COLONY_LIST_SUCCESS,  czResponse!!.getResponse().data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.COLONY_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }

    }
}