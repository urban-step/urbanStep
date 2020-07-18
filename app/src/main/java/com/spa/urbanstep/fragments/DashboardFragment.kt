package com.spa.urbanstep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.spa.urbanstep.R
import com.spa.urbanstep.UserManager
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.eventbus.EventObject
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DashboardFragment : BaseFragment() {

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_username.setText(String.format(getString(R.string.welcome_user), UserManager.getInstance().user.name))

        ll_grievance.setOnClickListener {
            openFragment(GrievanceFragment(), GrievanceFragment.TAG)
        }

        ll_suggestion.setOnClickListener {
            openFragment(SuggestionFragment(), SuggestionFragment.TAG)
        }

        ll_feedback.setOnClickListener {
            openFragment(FeedbackFragment(), FeedbackFragment.TAG)
        }

        ll_query.setOnClickListener {
            openFragment(QueryFragment(), QueryFragment.TAG)
        }

        ll_update_us.setOnClickListener {
            openFragment(UpdateUsFragment(), UpdateUsFragment.TAG)
        }
        ll_discussion.setOnClickListener {
            openFragment(DiscussionFragment(), DiscussionFragment.TAG)
        }
    }

    private fun openFragment(fragment: BaseFragment, TAG: String) {
        (activity as DashboardActivity).replaceFragmentWithTag(fragment, TAG)
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_my_account), false)
        }
    }


}
