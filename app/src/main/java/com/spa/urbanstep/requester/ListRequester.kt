package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.ListItem
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class ListRequester(private val listType: Int, private val headId: Int, private val catId: Int, private val zoneId: Int, private val wardId: Int, private val subCatId: Int) : Runnable {

    override fun run() {
        var czResponse: CZResponse<ResponseBean<List<ListItem>>>? = null
        czResponse = ApiHelper.performList(listType!!, headId, catId, zoneId, wardId, subCatId) as CZResponse<ResponseBean<List<ListItem>>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {
                EventBus.getDefault().post(EventObject(listType, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.ALL_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}