package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.RecordDetail
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class RecordDetailRequester(val recordType: Int) : Runnable {

    override fun run() {
        var czResponse: CZResponse<ResponseBean<List<RecordDetail>>>? = null
        czResponse = ApiHelper.performRecordList(recordType) as CZResponse<ResponseBean<List<RecordDetail>>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {
                EventBus.getDefault().post(EventObject(EventConstant.RECORD_LIST_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.RECORD_LIST_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}