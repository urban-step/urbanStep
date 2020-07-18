package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.Utility

import com.spa.urbanstep.R
import com.spa.urbanstep.adapter.FeedbackAuthAdapter
import com.spa.urbanstep.adapter.FeedbackPortalAdapter
import com.spa.urbanstep.adapter.FeedbackrojectAdapter
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.FeedbackResponse
import com.spa.urbanstep.requester.FeedbackRequester
import kotlinx.android.synthetic.main.fragment_past_feedback.*
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
class PastFeedbackFragment : BaseFragment() {

    var projectAdapter: FeedbackrojectAdapter? = null
    var authAdapter: FeedbackAuthAdapter? = null
    var portalAdapter: FeedbackPortalAdapter? = null

    var feedbackResponse: FeedbackResponse? = null


    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.FEEDBACK_SUCCESS -> {
                    feedbackResponse = eventObject.`object` as FeedbackResponse

                    projectAdapter = FeedbackrojectAdapter(activity as Context, feedbackResponse!!.project_feedback_data!!)
                    rv_feedback_project.layoutManager = LinearLayoutManager(context)
                    rv_feedback_project.setNestedScrollingEnabled(false);
                    rv_feedback_project.adapter = projectAdapter

                    authAdapter = FeedbackAuthAdapter(activity as Context, feedbackResponse!!.authority_feedback_data!!)
                    rv_feedback_auth.layoutManager = LinearLayoutManager(context)
                    rv_feedback_auth.setNestedScrollingEnabled(false);
                    rv_feedback_auth.adapter = authAdapter

                    portalAdapter = FeedbackPortalAdapter(activity as Context, feedbackResponse!!.portal_feedback_data!!)
                    rv_feedback_portal.layoutManager = LinearLayoutManager(context)
                    rv_feedback_portal.setNestedScrollingEnabled(false);
                    rv_feedback_portal.adapter = portalAdapter

                }
                EventConstant.FEEDBACK_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(FeedbackRequester())
    }

}
