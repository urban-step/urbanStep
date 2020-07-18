package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.adapter.FaqAdapter
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.FAQ
import kotlinx.android.synthetic.main.fragment_faq.*
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that m atch
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FaqFragment : BaseFragment() {

    var adapter: FaqAdapter? = null
    var projectList = ArrayList<FAQ>()

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        projectList.add(FAQ())
        projectList.add(FAQ())
        projectList.add(FAQ())
        projectList.add(FAQ())
        projectList.add(FAQ())
        projectList.add(FAQ())
        projectList.add(FAQ())
        projectList.add(FAQ())
        projectList.add(FAQ())

        adapter = FaqAdapter(activity as Context, projectList)
        rv_faq.layoutManager = LinearLayoutManager(context)
        rv_faq.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_faq), false)
        }
    }
}
