package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.DialogUtil
import com.spa.carrythistoo.utils.Utility
import com.spa.urbanstep.DashboardType
import com.spa.urbanstep.ListType

import com.spa.urbanstep.R
import com.spa.urbanstep.UserManager
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.ListItem
import com.spa.urbanstep.model.response.SaveRecord
import com.spa.urbanstep.model.response.SubmitResponse
import com.spa.urbanstep.requester.ListRequester
import com.spa.urbanstep.requester.SaveRecordRequester
import kotlinx.android.synthetic.main.fragment_query.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class QueryFragment : BaseFragment(), ThanksFragment.DialogDismiss {
    override fun onDismiss() {
        (activity as DashboardActivity).onBackPressed()
    }

    var categoryList: ArrayList<ListItem>? = null
    var subCategoryList: ArrayList<ListItem>? = null

    var catId: Int? = null
    var subCatId: Int? = null

    companion object {
        var TAG: String = QueryFragment::class.java.simpleName
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                ListType.CATEGORY.ordinal -> {
                    categoryList = eventObject.`object` as ArrayList<ListItem>

                    if (categoryList != null && !categoryList!!.isEmpty())
                        rl_select_sub_category.visibility = View.VISIBLE

                }
                ListType.SUB_CATEGORY.ordinal -> {
                    subCategoryList = eventObject.`object` as ArrayList<ListItem>

                    if (subCategoryList == null || subCategoryList!!.isEmpty())
                        rl_select_sub_category.visibility = View.GONE
                    else {
                        rl_select_sub_category.visibility = View.VISIBLE
                    }
                }

                EventConstant.ALL_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.SAVE_RECORD_SUCCESS -> {
                    val ft = fragmentManager!!.beginTransaction()
                    val prev = fragmentManager!!.findFragmentByTag("dialog")
                    if (prev != null) {
                        ft.remove(prev)
                    }
                    ft.addToBackStack(null)
                    val dialogFragment = ThanksFragment.newInstance(DashboardType.QUERY.ordinal, eventObject.`object` as SubmitResponse)
                    dialogFragment.setTargetFragment(this, 100)
                    dialogFragment.show(ft, "dialog")
                    //(activity as DashboardActivity).replaceFragmentWithTag(ThanksFragment.newInstance(DashboardType.QUERY.ordinal,eventObject.`object` as SubmitResponse), ThanksFragment.TAG)
                }
                EventConstant.SAVE_RECORD_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_query), true)
        }
    }

    private fun resetView() {
        tv_select_category.text = ""
        tv_select_sub_category.text = ""
        tv_query.setText(" ")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_query, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(ListRequester(ListType.CATEGORY.ordinal, 0, 0, 0, 0, 0))

        tv_select_category.setOnClickListener {
            DialogUtil.showListing(context!!, categoryList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String) {
                    catId = id
                    tv_select_category.setText(name)

                    if (name.equals("others", true)) {
                        rl_select_sub_category.visibility = View.GONE
                        rl_select_others.visibility = View.VISIBLE
                    } else {
                        rl_select_sub_category.visibility = View.VISIBLE
                        rl_select_others.visibility = View.GONE

                        Utility.showProgressBarSmall(rl_progress_bar)
                        BackgroundExecutor().execute(ListRequester(ListType.SUB_CATEGORY.ordinal, 0, catId!!, 0, 0, 0))
                    }
                }
            })
        }

        tv_select_sub_category.setOnClickListener {
            if (TextUtils.isEmpty(tv_select_category.text.toString())) {
                CommonMethods.showShortToast(context!!, "Please select Category")
            } else {
                if (subCategoryList == null || subCategoryList!!.isEmpty()) {
                    rl_select_sub_category.visibility = View.GONE

                } else {
                    DialogUtil.showListing(context!!, subCategoryList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                        override fun onItemSelect(id: Int, name: String) {
                            subCatId = id
                            tv_select_sub_category.setText(name)
                        }
                    })
                }
            }
        }

        tv_submit.setOnClickListener {
            sendSuggestionToServer()
        }
    }

    private fun sendSuggestionToServer() {
        if (valid()) {
            val grievance = SaveRecord()
            grievance.user_id = UserManager.getInstance().userID

            grievance.cat_id = catId

            if (catId == 1000) {
                grievance.others = tv_select_others.text.toString()
            } else {

                grievance.sub_cat_id = subCatId
            }
            grievance.query = tv_query.text.toString()

            Utility.showProgressBarSmall(rl_progress_bar)
            BackgroundExecutor().execute(SaveRecordRequester(DashboardType.QUERY.ordinal, grievance))
        }
    }

    private fun valid(): Boolean {
        if (TextUtils.isEmpty(tv_select_category.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_category_empty))
            return false
        } else if (rl_select_sub_category.visibility == View.VISIBLE && TextUtils.isEmpty(tv_select_sub_category.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_subcategory))
            return false
        } else if (rl_select_others.visibility == View.VISIBLE && TextUtils.isEmpty(tv_select_others.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_other))
            return false
        } else if (TextUtils.isEmpty(tv_query.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_suggestion_empty))
            return false
        } else {
            return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetView()
    }
}
