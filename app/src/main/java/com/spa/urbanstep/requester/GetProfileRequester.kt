package com.spa.urbanstep.requester

import com.spa.carrythistoo.model.User
import com.spa.urbanstep.UserManager
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class GetProfileRequester : Runnable {

    override fun run() {
        var czResponse: CZResponse<ResponseBean<User>>? = null
        czResponse = ApiHelper.performGetProfile() as CZResponse<ResponseBean<User>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            val userResponse = czResponse!!.getResponse().data
            if (czResponse!!.getResponse().getStatus() == 1) {
                UserManager.getInstance().setUser(userResponse)
                EventBus.getDefault().post(EventObject(EventConstant.GET_PROFILE_SUCCESS, userResponse))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.GET_PROFILE_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}