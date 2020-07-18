package com.spa.urbanstep.fragments


import android.content.Context
import android.support.v4.app.Fragment
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    @Subscribe
    abstract fun onEvent(eventObject: EventObject)

    fun onHandleBaseEvent(eventObject: EventObject?) {
        if (eventObject == null) {
            // Log.d(TAG, "on event method called but event object is null")
            return
        }
        when (eventObject!!.getId()) {
         /* EventConstant.NETWORK_ERROR -> DialogUtil.showOneButtonDialog(context!!, getString(R.string.no_internet_connection), getString(R.string.ok))
          EventConstant.SERVER_ERROR -> DialogUtil.showOneButtonDialog(context!!, getString(R.string.msg_server_error), getString(R.string.ok))*/
            EventConstant.NETWORK_ERROR -> CommonMethods.showShortToast(context!!,"No Internet Connection")
            EventConstant.SERVER_ERROR -> CommonMethods.showShortToast(context!!,"Server Error")
        }
    }

/*    private fun openLoginActivity() {
        startActivity(LoginActivity.newIntent(context))
    }

    fun changeFragmentWithTag(fragment: Fragment, tag: String) {
        ChildFragmentFactory.replaceFragment(fragment, R.id.container, this, tag)
    }

    fun changeFragment(fragment: Fragment) {
        ChildFragmentFactory.replaceFragment(fragment, R.id.container, this)
    }

    fun changeFragment(fragment: Fragment, container: Int) {
        ChildFragmentFactory.replaceFragment(fragment, container, this)
    }

    fun addFragment(fragment: Fragment, tag: String) {
        ChildFragmentFactory.addFragment(fragment, R.id.container, this, tag)
    }

    fun addFragment(fragment: Fragment) {
        ChildFragmentFactory.addFragment(fragment, R.id.container, this)
    }*/

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}// Required empty public constructor
