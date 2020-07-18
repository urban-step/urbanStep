package com.spa.urbanstep.fragments


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.urbanstep.DashboardType

import com.spa.urbanstep.R
import com.spa.urbanstep.model.response.SubmitResponse
import kotlinx.android.synthetic.main.fragment_thanks.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ThanksFragment : DialogFragment() {

    interface DialogDismiss {
        fun onDismiss()
    }

    var OPEN_PAGE: Int? = null
    var response: SubmitResponse? = null

    companion

    object {
        var TAG: String = ThanksFragment.javaClass.simpleName
        var OPEN_PAGE: String = "open_type"
        var SUBMIT_RESPONSE: String = "submit"
        fun newInstance(openType: Int, response: SubmitResponse): ThanksFragment {
            val fragment = ThanksFragment()
            val args = Bundle()
            args.putInt(OPEN_PAGE, openType)
            args.putParcelable(SUBMIT_RESPONSE, response)
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        OPEN_PAGE = args!!.getInt(ThanksFragment.OPEN_PAGE)
        response = args!!.getParcelable(ThanksFragment.SUBMIT_RESPONSE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thanks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (OPEN_PAGE == DashboardType.GRIEVANCE.ordinal) {
            tv_success_message.setText(String.format(getString(R.string.submit_message), "Grievance"))
            tv_submittion_number.setText(String.format(getString(R.string.submit_number), "Grievance"))
            tv_unique_number.setText(response!!.unique_no)
            tv_authority.setText(response!!.authority_name + "\n" + response!!.authority_email + "\n" + response!!.authority_title)

        } else if (OPEN_PAGE == DashboardType.SUGGESTION.ordinal) {
            tv_success_message.setText(String.format(getString(R.string.submit_message), "Suggestion"))
            tv_submittion_number.setText(String.format(getString(R.string.submit_number), "Suggestion"))
            tv_unique_number.setText(response!!.unique_no)
            tv_authority.setText(response!!.authority_name + "\n" + response!!.authority_email + "\n" + response!!.authority_title)

        } else if (OPEN_PAGE == DashboardType.QUERY.ordinal) {
            tv_success_message.setText(String.format(getString(R.string.submit_message), "Query"))
            tv_submittion_number.setText(String.format(getString(R.string.submit_number), "Query"))
            tv_unique_number.setText(response!!.unique_no)
            tv_authority.setText(response!!.authority_name + "\n" + response!!.authority_email + "\n" + response!!.authority_title)

        } else if (OPEN_PAGE == DashboardType.UPDATE_US.ordinal) {
            tv_success_message.setText(String.format(getString(R.string.submit_message), "Update Us"))
            rl_submit_number.visibility = View.GONE
            rl_concerned_authority.visibility = View.GONE

        } else if (OPEN_PAGE == DashboardType.DISCUSSION.ordinal) {
            tv_success_message.setText(String.format(getString(R.string.submit_message), "Discussion"))
            rl_concerned_authority.visibility = View.GONE
            rl_submit_number.visibility = View.GONE

        } else if (OPEN_PAGE == DashboardType.FEEDBACK.ordinal) {
            tv_success_message.setText(String.format(getString(R.string.submit_message), "Feedback"))
            rl_concerned_authority.visibility = View.GONE
            rl_submit_number.visibility = View.GONE
        }

        tv_ok.setOnClickListener {
            dismiss()
            val listener = targetFragment as DialogDismiss?
            listener!!.onDismiss()
        }
    }

}
