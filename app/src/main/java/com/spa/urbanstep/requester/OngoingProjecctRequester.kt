package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.ProjectList
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class OngoingProjecctRequester : Runnable {

    override fun run() {
        var czResponse: CZResponse<ResponseBean<ProjectList>>? = null
        czResponse = ApiHelper.performOngoingProjectList() as CZResponse<ResponseBean<ProjectList>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {
                EventBus.getDefault().post(EventObject(EventConstant.ONGOING_PROJECT_LIST_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.ONGOING_PROJECT_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}