package com.idolmedia.yzymanager.view.commodity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.lifecycle.ViewModelProvider
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.databinding.ActivityCommodityDetailsEditWebBinding
import com.idolmedia.yzymanager.model.entity.UploadImageEntity
import com.idolmedia.yzymanager.model.net.BaseObserver
import com.idolmedia.yzymanager.model.net.RetrofitHelper
import com.idolmedia.yzymanager.utils.Dp2PxUtils
import com.idolmedia.yzymanager.utils.PhotoUtils
import com.idolmedia.yzymanager.utils.ToastUtil
import com.idolmedia.yzymanager.utils.UnicodeUtil
import com.idolmedia.yzymanager.viewmodel.TitleBarViewModel
import com.idolmedia.yzymanager.viewmodel.commodity.CommodityDetailsEditViewModel
import com.idolmedia.yzymanager.widget.CustomLoading
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia

class CommodityDetailsEditActivity : BaseStateActivity<ActivityCommodityDetailsEditWebBinding>() {
    override fun getLayoutId() = R.layout.activity_commodity_details_edit_web

    override fun getViewModel() = ViewModelProvider(this).get(CommodityDetailsEditViewModel::class.java)

    override fun initTitleBar(savedInstanceState: Bundle?) {

        binding.viewModel?.titleBar?.set(TitleBarViewModel("商品详情","完成",object : TitleBarViewModel.OnClickListener{
            override fun onBackClick(view: View) {
                finish()
            }

            override fun onRightClick(view: View) {
                binding.webView.evaluateJavascript("javascript:test()") {
                    Log.d("====js", it)
                    val u = it.replace("\"","").replace("\\\\","\\")
                    //   val s= u.subSequence(1,u.length-1).toString()
                    val str = UnicodeUtil.decode(u)
                    Log.d("====js 解码：",str)
                    val intent = Intent()
                    intent.putExtra("info",str)
                    setResult(1,intent)
                    finish()
                }
            }
        }))
    }

    var uploadMessageAboveL: ValueCallback<Array<Uri>>? = null
    var isLoad = true
    override fun initView(savedInstanceState: Bundle?) {

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

        binding.webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d("====加载进度","$newProgress")
                if (newProgress>=95){
                    CustomLoading.getInstance().closeLoad()
                }

                if (newProgress==100&&isLoad){
                    isLoad = false

                    val height = Dp2PxUtils.getScreenHeightToWeb(Dp2PxUtils.dip2px(120).toInt())
                    binding.webView.evaluateJavascript("javascript:setHeight('${height}')") {

                    }

                    val h = intent.getStringExtra("info") ?: ""
                    binding.webView.evaluateJavascript("javascript:getSummernote('${h}')") {

                    }
                }

            }

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                uploadMessageAboveL = filePathCallback
                PhotoUtils.onCameraClick(this@CommodityDetailsEditActivity)
                return true
            }

        }

        binding.webView.loadUrl("file:///android_asset/www/index.html")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==PhotoUtils.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK){

            val list = PictureSelector.obtainMultipleResult(data) as ArrayList<LocalMedia>
            if (list.isNotEmpty()){
                uploadMessageAboveL?.onReceiveValue(null)
                uploadMessageAboveL = null
                CustomLoading.getInstance().showLoad(this)
                RetrofitHelper.instance().uploadImage(list[0].realPath,object : BaseObserver<UploadImageEntity>(){
                    override fun onSuccess(t: UploadImageEntity) {
                        if (t.resultCode==1){
                            binding.webView.evaluateJavascript("javascript:appImg('${t.imageUrl}')") {

                            }
                        }
                    }

                    override fun onError(msg: String) {
                        ToastUtil.show(msg)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        CustomLoading.getInstance().closeLoad()
                    }

                })
            }else{
                uploadMessageAboveL?.onReceiveValue(null)
                uploadMessageAboveL = null
            }

        }else{
            uploadMessageAboveL?.onReceiveValue(null)
            uploadMessageAboveL = null
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