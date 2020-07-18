package com.spa.urbanstep.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.Utility

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.activity.LoginActivity
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.requester.LoginRequester
import com.spa.urbanstep.model.LoginRequest
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : BaseFragment() {

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.LOGIN_SUCCESS -> {
                    startActivity(Intent(context, DashboardActivity::class.java))
                    activity!!.finish()
                }
                EventConstant.LOGIN_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_not_a_member.setOnClickListener {
            (activity as LoginActivity).replaceFragmentWithTag(RegisterFragment(), "Register")
        }

        tv_login.setOnClickListener {

            if (TextUtils.isEmpty(tv_email.text)) {
                Toast.makeText(context, getString(R.string.validation_email_empty), Toast.LENGTH_LONG).show()

            } else if (!CommonMethods.isEmailValid(tv_email.text.toString())) {
                Toast.makeText(context, getString(R.string.validation_email_format), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(tv_password.text)) {
                Toast.makeText(context, getString(R.string.validation_email_empty), Toast.LENGTH_LONG).show()

            } else {
                Utility.showProgressBarSmall(rl_progress_bar)
                BackgroundExecutor().execute(LoginRequester(LoginRequest(tv_email.text.toString(), tv_password.text.toString())))
            }
        }

        tv_forgot_password.setOnClickListener {
            (activity as LoginActivity).replaceFragmentWithTag(ForgotPasswordFragment(), "Forget Password")
        }
    }
}
