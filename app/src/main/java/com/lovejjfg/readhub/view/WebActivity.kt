/*
 *
 *   Copyright (c) 2017.  Joe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.lovejjfg.readhub.view

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.lovejjfg.readhub.BuildConfig
import com.lovejjfg.readhub.R
import com.lovejjfg.readhub.base.BaseActivity
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.databinding.ActivityWebBinding
import com.lovejjfg.readhub.utils.UIUtil

class WebActivity : BaseActivity() {

    private lateinit var mWeb: WebView
    private lateinit var loading: ProgressBar
    private lateinit var toolbar: Toolbar

    @SuppressLint("SetJavaScriptEnabled")
    override fun afterCreatedView(savedInstanceState: Bundle?) {
        super.afterCreatedView(savedInstanceState)
        val viewBind = DataBindingUtil.setContentView<ActivityWebBinding>(this, R.layout.activity_web)
        mWeb = viewBind.web
        loading = viewBind.pb
        toolbar = viewBind.toolbar
        toolbar.setOnClickListener {
            if (UIUtil.doubleClick()) {
                onTitleDoubleClick()
            }
        }

        val url = intent.getStringExtra(Constants.URL)
        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }
        mWeb.isVerticalScrollBarEnabled = false
        mWeb.isHorizontalScrollBarEnabled = false
        WebView.setWebContentsDebuggingEnabled(BuildConfig.IS_DEBUG)
        val webSettings = mWeb.settings
        webSettings!!.javaScriptEnabled = true
        //        webSettings.setUseWideViewPort(true);
        //        webSettings.setLoadWithOverviewMode(true);
        mWeb.isClickable = true
        webSettings.domStorageEnabled = true
        webSettings.loadsImagesAutomatically = true
        webSettings.builtInZoomControls = true
        webSettings.blockNetworkImage = false
        webSettings.displayZoomControls = false
        //        mWeb.setWebViewClient(new WebViewClient());
        mWeb.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(webView: WebView, s: String) {
                super.onReceivedTitle(webView, s)
                toolbar.title = s
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                loading.progress = newProgress
                if (newProgress == 100)
                    loading.visibility = View.GONE
            }
        }
        mWeb.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                loading.show()
                loading.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loading.visibility = View.GONE
//                loading.dismiss()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                loading.visibility = View.GONE
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                loading.visibility = View.GONE
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                loading.visibility = View.GONE
            }
        }

        mWeb.loadUrl(url)
    }

    override fun onDestroy() {
        super.onDestroy()
        mWeb.destroy()
    }

    override fun onBackPressed() {
        if (mWeb.canGoBack()) {
            mWeb.goBack()
            return
        }
        super.onBackPressed()
    }

    private fun onTitleDoubleClick() {
        mWeb.scrollTo(0, 0)
    }
}
