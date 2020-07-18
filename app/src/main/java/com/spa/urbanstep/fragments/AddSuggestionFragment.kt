package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.adapter.SuggestionAdapter
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.Suggestion
import kotlinx.android.synthetic.main.fragment_add_suggestion.*
import org.greenrobot.eventbus.Subscribe

class AddSuggestionFragment : BaseFragment() {
    var adapter: SuggestionAdapter? = null
    var suggestions = ArrayList<Suggestion>()

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_suggestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        suggestions.add(Suggestion())
        suggestions.add(Suggestion())
        suggestions.add(Suggestion())
        suggestions.add(Suggestion())
        suggestions.add(Suggestion())
        suggestions.add(Suggestion())
        suggestions.add(Suggestion())
        suggestions.add(Suggestion())

        adapter = SuggestionAdapter(activity as Context, suggestions)
        rv_suggestions.layoutManager = LinearLayoutManager(context)
        rv_suggestions.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_suggestion), false)
        }
    }


}
