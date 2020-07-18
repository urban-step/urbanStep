package com.spa.urbanstep.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.spa.carrythistoo.executer.BackgroundExecutor
import com.spa.carrythistoo.utils.CommonMethods
import com.spa.carrythistoo.utils.Utility

import com.spa.urbanstep.R
import com.spa.urbanstep.activity.DashboardActivity
import com.spa.urbanstep.eventbus.EventConstant
import com.spa.urbanstep.eventbus.EventObject
import com.spa.urbanstep.model.response.HomeMapUrl
import com.spa.urbanstep.requester.HomeMapUrlRequester
import org.greenrobot.eventbus.Subscribe
import com.wfl.autolooppager.RecycleAdapter
import com.wfl.autolooppager.AutoLoopPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.row_banner.view.*


class HomeFragment : BaseFragment() {

    private var autoLoopPager: AutoLoopPager? = null
    private var homeMapUrl: HomeMapUrl? = null

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.HOME_MAP_URL_SUCCESS -> {
                    homeMapUrl = eventObject.`object` as HomeMapUrl
                    webview_fragment.loadUrl(homeMapUrl!!.home_url)
                }
                EventConstant.HOME_MAP_URL_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(HomeMapUrlRequester())

        autoLoopPager = autoPager as AutoLoopPager

        val banners = ArrayList<Int>()
        banners.add(R.drawable.banner_1)
        banners.add(R.drawable.banner_1)
        banners.add(R.drawable.banner_1)

        val adapter = MyAdapter()
        adapter.datas = banners
        autoLoopPager!!.setAdapter(adapter)

        webview_fragment.webChromeClient = WebChromeClient()
        webview_fragment.webViewClient = WebViewClient()
        webview_fragment.settings.javaScriptEnabled = true
        webview_fragment.settings.domStorageEnabled = true
        //webview_fragment.settings.setAppCacheEnabled(true)
        /* webview_fragment.settings.loadWithOverviewMode = true
         webview_fragment.settings.loadsImagesAutomatically = true
         webview_fragment.settings.useWideViewPort = true
         webview_fragment.settings.loadWithOverviewMode = true
         webview_fragment.settings.allowFileAccess = true
         webview_fragment.settings.allowFileAccessFromFileURLs = true
         webview_fragment.settings.allowUniversalAccessFromFileURLs = true
         webview_fragment.settings.setGeolocationEnabled(true)*/
        // webview_fragment.settings.userAgentString = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"
        // webview_fragment.settings.javaScriptCanOpenWindowsAutomatically = true


        /*  tv_ndmc.setOnClickListener(View.OnClickListener {
              tv_ndmc.isSelected = true
              tv_cdmc.isSelected = false
              tv_sdmc.isSelected = false
              tv_edmc.isSelected = false
          })

          tv_edmc.setOnClickListener(View.OnClickListener {
              tv_edmc.isSelected = true
              tv_cdmc.isSelected = false
              tv_sdmc.isSelected = false
              tv_ndmc.isSelected = false
          })

          tv_sdmc.setOnClickListener(View.OnClickListener {
              tv_sdmc.isSelected = true
              tv_cdmc.isSelected = false
              tv_ndmc.isSelected = false
              tv_edmc.isSelected = false
          })

          tv_cdmc.setOnClickListener(View.OnClickListener {
              tv_cdmc.isSelected = true
              tv_ndmc.isSelected = false
              tv_sdmc.isSelected = false
              tv_edmc.isSelected = false
          })*/

        tv_say_something.setOnClickListener {
            (activity as DashboardActivity).replaceFragment(DashboardFragment())
        }

        tv_know_area.setOnClickListener {

            (activity as DashboardActivity).replaceFragmentWithTag(KnowYourAreaFragment.newInstance(homeMapUrl!!), "KnowYourFragment")
            /* DialogUtil.openChooseKnowAreaDialog(context!!, object : DialogUtil.AlertDialogInterface.OpenchooserDialogListener {
                 override fun onColonySearchClick() {
                     (activity as DashboardActivity).replaceFragmentWithTag(KnowYourAreaFragment.newInstance(1,homeMapUrl!!), "KnowYourFragment")
                 }

                 override fun onZoneClick() {
                     (activity as DashboardActivity).replaceFragmentWithTag(KnowYourAreaFragment.newInstance(0,homeMapUrl!!), "KnowYourFragment")
                 }
             })*/

        }
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setToolbar(getString(R.string.title_we_the_change), false)
        }
    }

    private inner class MyAdapter : RecycleAdapter<MyAdapter.ViewHolder>() {

        var datas: ArrayList<Int>? = null

        override fun getCount(): Int {
            return datas!!.size
        }

        override fun onCreateViewHolder(container: ViewGroup?): ViewHolder {
            var itemView: View? = null

            itemView = LayoutInflater.from(container!!.context)
                    .inflate(R.layout.row_banner, container, false)

            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder!!.iv_image.setImageDrawable(context!!.resources.getDrawable(datas!!.get(position)))
        }

        override fun onRecycleViewHolder(holder: ViewHolder?) {
        }


        inner class ViewHolder(view: View?) : RecycleAdapter.ViewHolder(view!!) {
            var iv_image = view!!.iv_banner_image
        }
    }
}
