package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.Utility

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.adapter.KnowAreaAdapter
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.Zone
import com.spa.urbanstep.model.response.HomeMapUrl
import com.spa.urbanstep.requester.KnowYourRequester
import kotlinx.android.synthetic.main.fragment_know_your_area.*
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
class KnowYourAreaFragment : BaseFragment(), KnowAreaAdapter.ClickListner {

    override fun onWardDetailClick(zone: Zone, isZone: Boolean) {
        if (isZone) {
            (activity as DashboardActivity).replaceFragmentWithTag(ZoneDetailFragment.newInstance(HOME_URL!!.area_url!!, "" + zone.id!!), "ZoneDetailFragment")
        }
    }

    var zoneList = ArrayList<Zone>()
    var wardList = ArrayList<Zone>()

    var adapter: KnowAreaAdapter? = null

    var OPEN_PAGE: Int? = null
    var HOME_URL: HomeMapUrl? = null

    companion

    object {
        var OPEN_PAGE: String = "open_type"
        var HOME_URL: String = "home_url"
        fun newInstance(homeMapUrl: HomeMapUrl): KnowYourAreaFragment {
            val fragment = KnowYourAreaFragment()
            val args = Bundle()
            //args.putInt(OPEN_PAGE, openType)
            args.putParcelable(HOME_URL, homeMapUrl)
            fragment.setArguments(args)
            return fragment
        }
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {

                EventConstant.KNOW_YOUR_LIST_SUCCESS -> {
                    zoneList = eventObject.`object` as ArrayList<Zone>

                    adapter = KnowAreaAdapter(activity as Context, zoneList, true, this)
                    rv_know_area.layoutManager = LinearLayoutManager(context)
                    rv_know_area.adapter = adapter
                }

                EventConstant.KNOW_YOUR_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_know_your_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // tv_description.setText(String.format(getString(R.string.delhi_desc)))

        tv_search_colony.setOnClickListener {
            (activity as DashboardActivity).replaceFragmentWithTag(ColonySearchFragment(), "ColonySearchFragment")
        }

        val args = arguments
        // OPEN_PAGE = args!!.getInt(KnowYourAreaFragment.OPEN_PAGE)
        HOME_URL = args!!.getParcelable(KnowYourAreaFragment.HOME_URL)

        //  if (OPEN_PAGE == 0) {
        //  rv_know_area.visibility = View.VISIBLE
        // card_view.visibility = View.GONE

        //  ll_map.visibility = View.VISIBLE

        webview_fragment.webChromeClient = WebChromeClient()
        webview_fragment.webViewClient = WebViewClient()
        webview_fragment.settings.javaScriptEnabled = true
        webview_fragment.settings.domStorageEnabled = true
        webview_fragment.loadUrl(HOME_URL!!.home_url)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(KnowYourRequester())
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_know_your_area), true)
        }
    }
}
