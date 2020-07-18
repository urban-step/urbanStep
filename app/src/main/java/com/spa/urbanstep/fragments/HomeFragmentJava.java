package com.spa.urbanstep.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.spa.urbanstep.R;
import com.spa.urbanstep.activity.DashboardActivity;
import com.spa.urbanstep.eventbus.EventObject;
import com.wfl.autolooppager.RecycleAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragmentJava extends BaseFragment {

    private Context context;

    public HomeFragmentJava() {
        // Required empty public constructor
    }

    @Override
    @Subscribe
    public void onEvent(@NotNull EventObject eventObject) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_fragment_java, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView webView = view.findViewById(R.id.webview_f);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl("http://urbanstep.in/homemap");

    }

    @Override
    public void onResume() {
        super.onResume();
        if (context instanceof DashboardActivity) {
            ((DashboardActivity) context).setToolbar(getString(R.string.title_we_the_change), false);
        }
    }

   /* private static class MyAdapter extends RecycleAdapter<MyAdapter.ViewHolder> {

        private ArrayList<Integer> datas = new ArrayList<Integer>();

        override fun

        getCount():Int

        {
            return datas !!.size
        }

        override fun

        onCreateViewHolder(container:ViewGroup?):ViewHolder

        {
            var itemView:View ? = null

            itemView = LayoutInflater.from(container !!.context)
                    .inflate(R.layout.row_banner, container, false)

            return ViewHolder(itemView)
        }

        override fun

        onBindViewHolder(holder:ViewHolder?, position:Int) {
            holder !!.iv_image.setImageDrawable(context !!.resources.getDrawable(datas !!.
            get(position)))
        }


        @Override
        protected int getCount() {
            return datas.size();
        }

        @Override
        protected ViewHolder onCreateViewHolder(ViewGroup container) {
            View itemView = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.row_banner, container, false);

            return new ViewHolder(itemView);
        }

        @Override
        protected void onBindViewHolder(ViewHolder holder, int position) {
            holder.iv_image.setImageDrawable(context.getResources().getDrawable(datas.get(position)));
        }

        @Override
        protected void onRecycleViewHolder(ViewHolder holder) {

        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView iv_image;

            public ViewHolder(View itemView) {
                super(itemView);
                this.iv_image = (ImageView) itemView.findViewById(R.id.iv_banner_image);
            }
        }
    }*/
}
