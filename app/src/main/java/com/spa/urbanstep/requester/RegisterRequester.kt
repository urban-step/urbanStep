package com.spa.carrythistoo.requester

import com.spa.carrythistoo.model.User
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.request.RegisterRequest
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus


class RegisterRequester(val register: RegisterRequest) : Runnable {
    var TAG = RegisterRequester::class.java.name

    override fun run() {
        var czResponse: CZResponse<ResponseBean<User>>? = null
        czResponse = ApiHelper.performRegister(register) as CZResponse<ResponseBean<User>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().status == 1) {
                /*   val userResponse = czResponse!!.getResponse().data
                   UserManager.getInstance().setUser(userResponse)
                   AppPreference.getInstance().setLogin(true);*/
                EventBus.getDefault().post(EventObject(EventConstant.REGISTER_SUCCESS, czResponse!!.getResponse().getMessage()))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.REGISTER_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}
