package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class ForgotPasswordRequester(val email: String) : Runnable {

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>? = null
        czResponse = ApiHelper.performForgotPassword(email) as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {

                EventBus.getDefault().post(EventObject(EventConstant.FORGOT_PASSWORD_SUCCESS, czResponse!!.getResponse().getMessage()))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.FORGOT_PASSWORD_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }

    }
}
