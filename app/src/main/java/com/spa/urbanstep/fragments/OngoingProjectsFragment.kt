package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.Utility

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.adapter.ProjectsAdapter
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.Project
import com.spa.urbanstep.requester.OngoingProjecctRequester
import kotlinx.android.synthetic.main.fragment_onging_projects.*
import kotlinx.android.synthetic.main.progress_bar.*
import org.greenrobot.eventbus.Subscribe

class OngoingProjectsFragment : BaseFragment() {

    var adapter: ProjectsAdapter? = null
    var projectList = ArrayList<Project>()

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.ONGOING_PROJECT_LIST_SUCCESS -> {
                    projectList = eventObject.`object` as ArrayList<Project>
                    adapter = ProjectsAdapter(activity as Context, projectList)
                    rv_projects.layoutManager = LinearLayoutManager(context)
                    rv_projects.adapter = adapter
                }
                EventConstant.ONGOING_PROJECT_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onging_projects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(OngoingProjecctRequester())
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_ongoing_projects), false)
        }
    }
}
