package com.spa.urbanstep.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.spa.carrythistoo.utils.FragmentFactory
import com.spa.urbanstep.R
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.fragments.LoginFragment
import org.greenrobot.eventbus.Subscribe

class LoginActivity : BaseActivity() {

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FragmentFactory.replaceFragment(LoginFragment(), R.id.fragment, this)

    }

    fun replaceFragment(fragment: Fragment) {
        FragmentFactory.replaceFragment(fragment, R.id.fragment, this)
    }


    fun replaceFragmentWithTag(fragment: Fragment, tag: String) {
        FragmentFactory.replaceFragment(fragment, R.id.fragment, this, tag)
    }
}
