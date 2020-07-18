package com.spa.urbanstep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.eventbus.EventObject
import kotlinx.android.synthetic.main.fragment_webview.*
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class WebviewFragment : BaseFragment() {
    var OPEN_PAGE: String? = null
    var URL: String? = null

    companion

    object {
        var OPEN_PAGE: String = "open_type"
        var URL: String = "url"
        fun newInstance(openType: String, url: String): WebviewFragment {
            val fragment = WebviewFragment()
            val args = Bundle()
            args.putString(OPEN_PAGE, openType)
            args.putString(URL, url)
            fragment.setArguments(args)
            return fragment
        }
    }


    @Subscribe
    override fun onEvent(eventObject: EventObject) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = arguments
        OPEN_PAGE = args!!.getString(WebviewFragment.OPEN_PAGE)
        URL = args!!.getString(WebviewFragment.URL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webview.webChromeClient = WebChromeClient()
        webview.webViewClient = WebViewClient()
        webview.settings.javaScriptEnabled = true
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.allowFileAccess = true
        webview.settings.allowFileAccessFromFileURLs = true
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            if (OPEN_PAGE.equals("PRIVACY_POLICY")) {
                (context as DashboardActivity).setToolbar(getString(R.string.title_privacy_policy), false)
            } else if (OPEN_PAGE.equals("TERM_AND_CONDITION")) {
                (context as DashboardActivity).setToolbar(getString(R.string.title_term_and_conditions), false)
            } else if (OPEN_PAGE.equals("MAP_URL")) {
                (context as DashboardActivity).setToolbar("KNOW YOUR AREA", true)
                webview.loadUrl(URL);
            }
        }
    }
}
