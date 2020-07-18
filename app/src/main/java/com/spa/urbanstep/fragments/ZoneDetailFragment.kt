package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
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
import com.spa.urbanstep.requester.KnowAreaViewRequester
import kotlinx.android.synthetic.main.fragment_zone_detail.*
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
class ZoneDetailFragment : BaseFragment(), KnowAreaAdapter.ClickListner {
    override fun onWardDetailClick(zone: Zone, isZone: Boolean) {
        if (!isZone) {
            (activity as DashboardActivity).replaceFragmentWithTag(WardDetailFragment.newInstance("" + zone.url
            !!), "WardDetailFragment")
        }
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.KNOW_YOUR_VIEW_LIST_SUCCESS -> {
                    wardList = eventObject.`object` as ArrayList<Zone>

                    if (!wardList.isEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            tv_description.setText(Html.fromHtml(wardList.get(0).decription, Html.FROM_HTML_MODE_COMPACT))
                        } else {
                            tv_description.setText(Html.fromHtml(wardList.get(0).decription))
                        }
                    }

                    adapter = KnowAreaAdapter(activity as Context, wardList, false, this)
                    rv_know_area.layoutManager = LinearLayoutManager(context)
                    rv_know_area.adapter = adapter
                }

                EventConstant.KNOW_YOUR_VIEW_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }

    var ZONE_ID: String? = null
    var MAP_URL: String? = null

    var wardList = ArrayList<Zone>()

    var adapter: KnowAreaAdapter? = null

    companion

    object {
        var OPEN_PAGE: String = "open_type"
        var ZONE_ID: String = "zone_id"
        var MAP_URL: String = "map_url"
        fun newInstance(mapUrl: String, zoneId: String): ZoneDetailFragment {
            val fragment = ZoneDetailFragment()
            val args = Bundle()
            //args.putInt(OPEN_PAGE, openType)
            args.putString(ZONE_ID, zoneId)
            args.putString(MAP_URL, mapUrl)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zone_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        // OPEN_PAGE = args!!.getInt(KnowYourAreaFragment.OPEN_PAGE)
        ZONE_ID = args!!.getString(ZoneDetailFragment.ZONE_ID)
        MAP_URL = args!!.getString(ZoneDetailFragment.MAP_URL)

        //  if (OPEN_PAGE == 0) {
        //  rv_know_area.visibility = View.VISIBLE
        // card_view.visibility = View.GONE

        //  ll_map.visibility = View.VISIBLE

        webview_fragment.webChromeClient = WebChromeClient()
        webview_fragment.webViewClient = WebViewClient()
        webview_fragment.settings.javaScriptEnabled = true
        webview_fragment.settings.domStorageEnabled = true
        webview_fragment.settings.javaScriptCanOpenWindowsAutomatically = true

        webview_fragment.loadUrl(MAP_URL)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(KnowAreaViewRequester(ZONE_ID!!.toInt()))
    }
}
