package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.Utility

import com.spa.urbanstep.R
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.requester.ForgotPasswordRequester
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.greenrobot.eventbus.Subscribe

class ForgotPasswordFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.FORGOT_PASSWORD_SUCCESS -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.FORGOT_PASSWORD_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_send.setOnClickListener {
            if (TextUtils.isEmpty(tv_email_address.text)) {
                Toast.makeText(context, getString(R.string.validation_email_empty), Toast.LENGTH_LONG).show()

            } else if (!CommonMethods.isEmailValid(tv_email_address.text.toString())) {
                Toast.makeText(context, getString(R.string.validation_email_format), Toast.LENGTH_LONG).show()

            } else {
                Utility.showProgressBarSmall(rl_progress_bar)
                BackgroundExecutor().execute(ForgotPasswordRequester(tv_email_address.text.toString()))
            }
        }
    }
}
