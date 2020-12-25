package com.idolmedia.yzymanager.viewmodel.commodity

import android.content.Intent
import android.util.Log
import android.webkit.WebView
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.utils.UnicodeUtil
import com.idolmedia.yzymanager.view.WebActivity

class CommodityDetailsEditViewModel : BaseViewModel() {

    fun onClickPreview(web:WebView){
        web.evaluateJavascript("javascript:test()") {
            Log.d("====js", it)
            if (it == "\"\"") return@evaluateJavascript
            val u = it.replace("\"","").replace("\\\\","\\")
            val str = UnicodeUtil.decode(u)
            Log.d("====js 解码",str)
            val intent = Intent(web.context, WebActivity::class.java)
            intent.putExtra("title","预览")
            intent.putExtra("url",str)
            web.context.startActivity(intent)
        }
    }

}