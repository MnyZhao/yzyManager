package com.idolmedia.yzymanager.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.lifecycle.ViewModelProvider
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.databinding.ActivityWebBinding
import com.idolmedia.yzymanager.utils.WebCss
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.WebViewModel
import com.idolmedia.yzymanager.widget.CustomLoading

/**
 *  时间：2020/4/13-17:35
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmanage.yzy.view.activity WebActivity
 *  描述：
 */
class WebActivity : BaseStateActivity<ActivityWebBinding>() {
    override fun getLayoutId() = R.layout.activity_web

    override fun getViewModel() = ViewModelProvider(this).get(WebViewModel::class.java)

    override fun initTitleBar(savedInstanceState: Bundle?) {
        val title = intent.getStringExtra("title") ?: ""
        binding.viewModel?.titleBar?.set(TitleBarViewModel(title))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(savedInstanceState: Bundle?) {
        val url = intent.getStringExtra("url") ?: ""

        val setting = binding.webView.settings
        setting.javaScriptEnabled = true
        //设置自适应屏幕，两者合用
        setting.useWideViewPort = true //将图片调整到适合webview的大小
        setting.loadWithOverviewMode = true // 缩放至屏幕的大小
        setting.defaultTextEncodingName = "utf-8"//设置编码格式
        // 开启 DOM storage API 功能
        setting.domStorageEnabled = true
        setting.allowFileAccess = true
        setting.setAppCacheEnabled(true)
        setting.cacheMode = WebSettings.LOAD_DEFAULT
        setting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        CustomLoading.getInstance().showLoad(this)
        binding.webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d("====加载进度","$newProgress")
                if (newProgress>=95){
                    CustomLoading.getInstance().closeLoad()
                }
            }
        }

        if (url.startsWith("www") || url.startsWith("http")){
            binding.webView.loadUrl(url)
        }else{
            binding.webView.setBackgroundColor(0)
            binding.webView.loadDataWithBaseURL(null, WebCss.getHtml(url),"text/html", "utf-8", null)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.webView.destroy()
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()){
            binding.webView.goBack()
        }else{
            super.onBackPressed()
        }
    }

}