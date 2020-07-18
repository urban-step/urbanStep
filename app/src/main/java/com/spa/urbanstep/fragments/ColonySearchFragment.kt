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

import com.spa.urbanstep.R
import com.spa.urbanstep.adapter.ColonyAdapter
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.Colony
import com.spa.urbanstep.requester.ColonyListRequester
import kotlinx.android.synthetic.main.fragment_colony_search.*
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
class ColonySearchFragment : BaseFragment() {

    var adapter: ColonyAdapter? = null

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.COLONY_LIST_SUCCESS -> {

                    val colonyList = eventObject.`object` as ArrayList<Colony>

                    adapter = ColonyAdapter(activity as Context, colonyList)
                    rv_colony.layoutManager = LinearLayoutManager(context)
                    rv_colony.adapter = adapter

                }
                EventConstant.COLONY_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_colony_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_search.setOnClickListener {
            if (TextUtils.isEmpty(edt_search.text.toString())) {
                CommonMethods.showShortToast(context!!, "Please enter colony name")
            } else {
                Utility.showProgressBarSmall(rl_progress_bar)
                BackgroundExecutor().execute(ColonyListRequester(edt_search.text.toString().trim()))
            }
        }
    }

}
