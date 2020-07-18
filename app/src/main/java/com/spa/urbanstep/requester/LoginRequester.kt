package com.spa.urbanstep.requester

import com.spa.carrythistoo.model.User
import com.spa.urbanstep.UserManager
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.LoginRequest
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import com.spa.urbanstep.prefrences.AppPreference
import org.greenrobot.eventbus.EventBus

class LoginRequester(val loginRequest: LoginRequest) : Runnable {

    override fun run() {
        var czResponse: CZResponse<ResponseBean<User>>? = null
        czResponse = ApiHelper.performLogin(loginRequest) as CZResponse<ResponseBean<User>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            val userResponse = czResponse!!.getResponse().data
            if (czResponse!!.getResponse().getStatus() == 1) {
                  UserManager.getInstance().setUser(userResponse)
                  AppPreference.getInstance().setLogin(true);

                EventBus.getDefault().post(EventObject(EventConstant.LOGIN_SUCCESS, userResponse))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.LOGIN_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}