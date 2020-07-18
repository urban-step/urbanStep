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
import com.spa.urbanstep.model.Project
import com.spa.urbanstep.model.response.ListItem
import com.spa.urbanstep.model.response.SaveRecord
import com.spa.urbanstep.model.response.SubmitResponse
import com.spa.urbanstep.requester.ListRequester
import com.spa.urbanstep.requester.OngoingProjecctRequester
import com.spa.urbanstep.requester.SaveRecordRequester
import kotlinx.android.synthetic.main.fragment_feedback.*
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
class FeedbackFragment : BaseFragment(),ThanksFragment.DialogDismiss {
    override fun onDismiss() {
        (activity as DashboardActivity).onBackPressed()
    }

    var categoryList = ArrayList<ListItem>()
    var ratingList = ArrayList<ListItem>()
    var projectList: ArrayList<Project>? = null
    var authorityList: ArrayList<ListItem>? = null


    var type: Int? = null
    var projectId: Int? = null
    var rating: Int? = null
    var authorityId: Int? = null


    companion object {
        var TAG: String = FeedbackFragment::class.java.simpleName
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                ListType.AUTHORITY.ordinal -> {
                    authorityList = eventObject.`object` as ArrayList<ListItem>
                }
                EventConstant.ONGOING_PROJECT_LIST_SUCCESS -> {
                    projectList = eventObject.`object` as ArrayList<Project>
                }
                EventConstant.ONGOING_PROJECT_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
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
                    val dialogFragment = ThanksFragment.newInstance(DashboardType.FEEDBACK.ordinal,eventObject.`object` as SubmitResponse)
                    dialogFragment.setTargetFragment(this,100)
                    dialogFragment.show(ft, "dialog")
                   // (activity as DashboardActivity).replaceFragmentWithTag(ThanksFragment.newInstance(DashboardType.FEEDBACK.ordinal,eventObject.`object` as SubmitResponse), ThanksFragment.TAG)
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
            (context as DashboardActivity).setToolbar(getString(R.string.title_feedback), true)
        }
    }

    private fun resetView() {
        tv_select_category.text = ""
        tv_select_rating.text = ""
        tv_select_projects.text = ""
        tv_feedback.setText(" ")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryList.clear()
        ratingList.clear()

        categoryList.add(ListItem(1, "Feedback on Projects"))
        categoryList.add(ListItem(2, "Feedback on Authority"))
        categoryList.add(ListItem(3, "Feedback on Portal"))

        ratingList.add(ListItem(1, "1"))
        ratingList.add(ListItem(2, "2"))
        ratingList.add(ListItem(3, "3"))
        ratingList.add(ListItem(4, "4"))
        ratingList.add(ListItem(5, "5"))

        tv_select_category.setOnClickListener {
            DialogUtil.showListing(context!!, categoryList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String) {
                    type = id
                    tv_select_category.setText(name)
                    getList()
                }
            })
        }

        tv_select_projects.setOnClickListener {
            DialogUtil.showProjectList(context!!, projectList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String) {
                    projectId = id
                    tv_select_projects.setText(name)
                }
            })
        }

        tv_select_authority.setOnClickListener {
            DialogUtil.showListing(context!!, authorityList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String) {
                    authorityId = id
                    tv_select_authority.setText(name)
                }
            })
        }

        tv_select_rating.setOnClickListener {
            DialogUtil.showListing(context!!, ratingList!!, object : DialogUtil.AlertDialogInterface.ListItemDialogClickListener {
                override fun onItemSelect(id: Int, name: String) {
                    rating = id
                    tv_select_rating.setText(name)
                }
            })
        }

        tv_submit.setOnClickListener {
            sendSuggestionToServer()
        }
    }

    private fun getList() {

        if (type == 1) {
            Utility.showProgressBarSmall(rl_progress_bar)
            rl_select_projects.visibility = View.VISIBLE
            rl_select_authority.visibility = View.GONE
            rl_select_rating.visibility = View.GONE
            BackgroundExecutor().execute(OngoingProjecctRequester())

        } else if (type == 2) {
            Utility.showProgressBarSmall(rl_progress_bar)
            rl_select_authority.visibility = View.VISIBLE
            rl_select_rating.visibility = View.VISIBLE
            rl_select_projects.visibility = View.GONE
            BackgroundExecutor().execute(ListRequester(ListType.AUTHORITY.ordinal, 0, 0, 0, 0, 0))

        } else if (type == 3) {
            rl_select_rating.visibility = View.VISIBLE
            rl_select_projects.visibility = View.GONE
            rl_select_authority.visibility = View.GONE
        }
    }

    private fun sendSuggestionToServer() {
        if (valid()) {
            val grievance = SaveRecord()
            grievance.type = type
            grievance.user_id = UserManager.getInstance().userID
            if (type == 1) {
                grievance.project_id = projectId
                grievance.project_feedback = tv_feedback.text.toString()
            } else if (type == 2) {
                grievance.authority_id = authorityId
                grievance.authority_rating = "" + rating
                grievance.authority_feedback = tv_feedback.text.toString()

            } else if (type == 3) {
                grievance.portal_rating = "" + rating
                grievance.portal_feedback = tv_feedback.text.toString()
            }
            Utility.showProgressBarSmall(rl_progress_bar)
            BackgroundExecutor().execute(SaveRecordRequester(DashboardType.FEEDBACK.ordinal, grievance))
        }
    }

    private fun valid(): Boolean {
        if (TextUtils.isEmpty(tv_select_category.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_category_empty))
            return false
        } else if (type == 1 && TextUtils.isEmpty(tv_select_projects.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_projects))
            return false
        } else if (type == 2 && TextUtils.isEmpty(tv_select_authority.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_authority))
            return false
        } else if (type != 1 && TextUtils.isEmpty(tv_select_rating.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_rating))
            return false
        } else if (TextUtils.isEmpty(tv_feedback.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, getString(R.string.valid_address_empty))
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
