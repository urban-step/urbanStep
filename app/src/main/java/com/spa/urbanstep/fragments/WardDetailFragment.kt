package com.spa.urbanstep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient

import com.spa.urbanstep.R
import com.spa.urbanstep.eventbus.EventObject
import kotlinx.android.synthetic.main.fragment_ward_detail.*
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class WardDetailFragment : BaseFragment() {

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

    var MAP_URL: String? = null

    companion

    object {
        var MAP_URL: String = "home_url"
        fun newInstance(mapUrl: String): WardDetailFragment {
            val fragment = WardDetailFragment()
            val args = Bundle()
            //args.putInt(OPEN_PAGE, openType)
            args.putString(MAP_URL, mapUrl)
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ward_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        // OPEN_PAGE = args!!.getInt(KnowYourAreaFragment.OPEN_PAGE)
        MAP_URL = args!!.getString(WardDetailFragment.MAP_URL)

        //  if (OPEN_PAGE == 0) {
        //  rv_know_area.visibility = View.VISIBLE
        // card_view.visibility = View.GONE

        //  ll_map.visibility = View.VISIBLE

        webview_fragment.webChromeClient = WebChromeClient()
        webview_fragment.webViewClient = WebViewClient()
        webview_fragment.settings.javaScriptEnabled = true
        webview_fragment.settings.domStorageEnabled = true
        webview_fragment.loadUrl(MAP_URL!!)

        /* tv_know_ward.setOnClickListener {
             (activity as DashboardActivity).replaceFragmentWithTag(WebviewFragment.newInstance("MAP_URL", MAP_URL!!), "WebviewFragment")
         }*/
        //}
    }

}
