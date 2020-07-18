package com.spa.urbanstep.requester

import com.spa.carrythistoo.model.User
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus


class EditProfileRequester(val user: User) : Runnable {

    override fun run() {
        var czResponse: CZResponse<ResponseBean<Any>>? = null
        czResponse = ApiHelper.performEditProfile(user) as CZResponse<ResponseBean<Any>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            val userResponse = czResponse!!.getResponse().data
            if (czResponse!!.getResponse().getStatus() == 1) {

                EventBus.getDefault().post(EventObject(EventConstant.EDIT_PROFILE_SUCCESS,  czResponse!!.getResponse().getMessage()))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.EDIT_PROFILE_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }

    }
}