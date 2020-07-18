package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.adapter.GovernanceAdapter
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.Governance
import kotlinx.android.synthetic.main.fragment_governance.*
import org.greenrobot.eventbus.Subscribe

class GovernanceFragment : BaseFragment() {

    var adapter: GovernanceAdapter? = null
    var governanceList = ArrayList<Governance>()

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_governance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        governanceList.add(Governance())
        governanceList.add(Governance())
        governanceList.add(Governance())
        governanceList.add(Governance())
        governanceList.add(Governance())
        governanceList.add(Governance())
        governanceList.add(Governance())
        governanceList.add(Governance())

        adapter = GovernanceAdapter(activity as Context, governanceList)
        rv_governance.layoutManager = LinearLayoutManager(context)
        rv_governance.adapter = adapter

    }


    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_governance), false)
        }
    }
}
