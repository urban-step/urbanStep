package com.spa.urbanstep.requester

import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.SaveRecord
import com.spa.urbanstep.model.response.SubmitResponse
import com.spa.urbanstep.network.ApiHelper
import com.spa.urbanstep.network.CZResponse
import com.spa.urbanstep.network.ResponseBean
import org.greenrobot.eventbus.EventBus

class SaveRecordRequester(val recordType: Int, val record: SaveRecord) : Runnable {

    override fun run() {
        var czResponse: CZResponse<ResponseBean<SubmitResponse>>? = null
        czResponse = ApiHelper.performSaveRecord(recordType, record) as CZResponse<ResponseBean<SubmitResponse>>
        if (czResponse != null && czResponse!!.getResponse() != null) {
            if (czResponse!!.getResponse().getStatus() == 1) {
                EventBus.getDefault().post(EventObject(EventConstant.SAVE_RECORD_SUCCESS, czResponse.response.data))
            } else {
                EventBus.getDefault().post(EventObject(EventConstant.SAVE_RECORD_ERROR, czResponse!!.getResponse().getMessage()))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}