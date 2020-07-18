package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.Utility
import com.spa.urbanstep.DashboardType

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.adapter.ShowRecordAdapter
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.RecordDetail
import com.spa.urbanstep.requester.RecordDetailRequester
import kotlinx.android.synthetic.main.fragment_show_record.*
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
class ShowRecordFragment : BaseFragment() {

    var adapter: ShowRecordAdapter? = null
    var recordList = ArrayList<RecordDetail>()

    var OPEN_PAGE: Int? = null

    companion

    object {
        var OPEN_PAGE: String = "open_type"
        fun newInstance(openType: Int): ShowRecordFragment {
            val fragment = ShowRecordFragment()
            val args = Bundle()
            args.putInt(OPEN_PAGE, openType)
            fragment.setArguments(args)
            return fragment
        }
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.RECORD_LIST_SUCCESS -> {
                    recordList = eventObject.`object` as ArrayList<RecordDetail>

                    adapter = ShowRecordAdapter(activity as Context, recordList, OPEN_PAGE!!)
                    rv_projects.layoutManager = LinearLayoutManager(context)
                    rv_projects.adapter = adapter
                }
                EventConstant.RECORD_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        OPEN_PAGE = args!!.getInt(WebviewFragment.OPEN_PAGE)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(RecordDetailRequester(OPEN_PAGE!!))

        tv_view.setOnClickListener {
            if (!TextUtils.isEmpty(tv_area.text.toString().trim())) {
                if (!recordList.isEmpty()) {
                    var fiterList = recordList!!.filter { it.unique_id == tv_area.text.toString() } as ArrayList<RecordDetail>

                    if (fiterList.isEmpty()) {
                        CommonMethods.showShortToast(context!!, "Not Found")
                    } else {
                        adapter = ShowRecordAdapter(activity as Context, fiterList, OPEN_PAGE!!)
                        rv_projects.layoutManager = LinearLayoutManager(context)
                        rv_projects.adapter = adapter
                    }
                }
            } else {
                CommonMethods.showShortToast(context!!, "Please enter ID")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            if (OPEN_PAGE == DashboardType.GRIEVANCE.ordinal) {
                (context as DashboardActivity).setToolbar(getString(R.string.title_grievance_details), false)
                tv_area.setHint("Enter Grievance Id")

            } else if (OPEN_PAGE == DashboardType.SUGGESTION.ordinal) {
                (context as DashboardActivity).setToolbar(getString(R.string.title_suggestion_details), false)
                tv_area.setHint("Enter Suggestion Id")

            } else if (OPEN_PAGE == DashboardType.QUERY.ordinal) {
                (context as DashboardActivity).setToolbar(getString(R.string.title_query_details), false)
                tv_area.setHint("Enter Query Id")

            } else if (OPEN_PAGE == DashboardType.UPDATE_US.ordinal) {
                (context as DashboardActivity).setToolbar(getString(R.string.title_update_us_details), false)
                tv_area.setHint("Enter UpdateUS Id")

            } else if (OPEN_PAGE == DashboardType.DISCUSSION.ordinal) {
                (context as DashboardActivity).setToolbar(getString(R.string.title_discussion_details), false)
                tv_area.setHint("Enter Discussion Id")
            }
        }
    }
}
