package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.requester.RegisterRequester
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.DialogUtil
import com.spa.carrythistoo.utils.Utility
import com.spa.urbanstep.ListType

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.LoginActivity
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.request.RegisterRequest
import com.spa.urbanstep.model.response.ListItem
import com.spa.urbanstep.requester.ListRequester
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 *
 */
class RegisterFragment : BaseFragment() {

    var headList: ArrayList<ListItem>? = null
    var subHeadList = ArrayList<ListItem>()
    var headID: Int? = null
    var subHeadID: Int? = null

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                ListType.HEAD_LIST.ordinal -> {
                    headList = eventObject.`object` as ArrayList<ListItem>

                    if (headList != null && !headList!!.isEmpty()) {
                        tv_select_sub_head.visibility = View.VISIBLE
                    }

                }
                ListType.SUB_HEAD_LIST.ordinal -> {
                    subHeadList = eventObject.`object` as ArrayList<ListItem>
                    if (subHeadList != null && !subHeadList!!.isEmpty()) {
                        tv_select_sub_head.visibility = View.VISIBLE
                    } else {
                        tv_select_sub_head.visibility = View.GONE
                    }
                }
                EventConstant.ALL_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.REGISTER_SUCCESS -> {
                    (activity as LoginActivity).replaceFragment(LoginFragment())
                    /*startActivity(Intent(context, DashboardActivity::class.java))
                    activity!!.finish()*/
                }
                EventConstant.REGISTER_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(ListRequester(ListType.HEAD_LIST.ordinal, 0, 0, 0, 0, 0))

        tv_select_head.setOnClickListener {
            DialogUtil.showListing(context!!, headList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String) {
                    headID = id
                    tv_select_head.setText(name)
                    subHeadList!!.clear()
                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().execute(ListRequester(ListType.SUB_HEAD_LIST.ordinal, headID!!, 0, 0, 0, 0))
                }
            })
        }

        tv_select_sub_head.setOnClickListener {
            if (TextUtils.isEmpty(tv_select_head.text.toString())) {
                CommonMethods.showShortToast(context!!, "Please select Head")
            } else {
                DialogUtil.showListing(context!!, subHeadList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                    override fun onItemSelect(id: Int, name: String) {
                        subHeadID = id
                        tv_select_sub_head.text = name
                    }
                })
            }
        }

        tv_sign_up.setOnClickListener {
            if (TextUtils.isEmpty(tv_select_head.text)) {
                Toast.makeText(context, getString(R.string.valid_head_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(tv_select_sub_head.text)) {
                Toast.makeText(context, getString(R.string.valid_sub_head_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_username.text)) {
                Toast.makeText(context, getString(R.string.valid_username), Toast.LENGTH_LONG).show()

            } else if (!CommonMethods.isEmailValid(edt_email.text.toString())) {
                Toast.makeText(context, getString(R.string.validation_email_format), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_password.text)) {
                Toast.makeText(context, getString(R.string.validation_password_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_confirm_password.text)) {
                Toast.makeText(context, getString(R.string.valid_confirm_pwd_empty), Toast.LENGTH_LONG).show()

            } else if (edt_password.text.toString() != edt_confirm_password.text.toString()) {
                Toast.makeText(context, getString(R.string.valid_mismatch_pwd_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_full_name.text)) {
                Toast.makeText(context, getString(R.string.valid_full_name_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_resident_address.text)) {
                Toast.makeText(context, getString(R.string.valid_resident_address_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_work_office.text)) {
                Toast.makeText(context, getString(R.string.valid_work_office_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_country.text)) {
                Toast.makeText(context, getString(R.string.valid_country_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_state.text)) {
                Toast.makeText(context, getString(R.string.valid_state_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_mobile_number.text)) {
                Toast.makeText(context, getString(R.string.valid_mobile_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_organization.text)) {
                Toast.makeText(context, getString(R.string.valid_organization_empty), Toast.LENGTH_LONG).show()

            } else if (TextUtils.isEmpty(edt_designation.text)) {
                Toast.makeText(context, getString(R.string.valid_designation_empty), Toast.LENGTH_LONG).show()

            } else {
                Utility.showProgressBarSmall(rl_progress_bar)
                BackgroundExecutor().execute(RegisterRequester(RegisterRequest(headID, subHeadID, edt_username.text.toString(), edt_email.text.toString(), edt_password.text.toString(), edt_full_name.text.toString(), edt_resident_address.text.toString(), edt_work_office.text.toString(), edt_mobile_number.text.toString(), edt_country.text.toString(), edt_state.text.toString(), edt_organization.text.toString(), edt_designation.text.toString())))
            }
        }

        tv_already_registered.setOnClickListener {
            (activity as LoginActivity).replaceFragment(LoginFragment())
        }
    }
}
