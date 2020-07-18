package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.adapter.ContactAdapter
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.Contact
import kotlinx.android.synthetic.main.fragment_emergency_contact.*
import org.greenrobot.eventbus.Subscribe

class EmergencyContactFragment : BaseFragment() {

    var adapter: ContactAdapter? = null
    var contactList = ArrayList<Contact>()

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactList.add(Contact())
        contactList.add(Contact())
        contactList.add(Contact())
        contactList.add(Contact())
        contactList.add(Contact())
        contactList.add(Contact())
        contactList.add(Contact())
        contactList.add(Contact())

        adapter = ContactAdapter(activity as Context, contactList)
        rv_contacts.layoutManager = LinearLayoutManager(context)
        rv_contacts.adapter = adapter

    }


    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_emergency_contact), false)
        }
    }

}
