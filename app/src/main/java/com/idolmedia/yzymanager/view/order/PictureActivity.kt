package com.idolmedia.yzymanager.view.order

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ktx.immersionBar
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.BaseAdapter
import com.idolmedia.yzymanager.adapter.ViewPager2Adapter
import com.idolmedia.yzymanager.base.BaseBean
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityPictureBinding
import com.idolmedia.yzymanager.viewmodel.order.ItemPictureBean
import com.idolmedia.yzymanager.viewmodel.order.PictureViewModel

/**
 *  时间：2020/12/2-11:14
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.view.order ImgActivity
 *  描述：
 */
class PictureActivity : BaseStateActivity<ActivityPictureBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_picture
    }

    override fun getViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get(PictureViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {
        binding.tvTitle.setOnClickListener { finish() }
        immersionBar{
            statusBarColor(R.color.black)
            navigationBarColor(R.color.black)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val json = intent.getStringExtra("json") ?: ""
        if (json.isNotEmpty()){
            val type =object : TypeToken<ArrayList<String>>(){}.type
            val list = Gson().fromJson<ArrayList<String>>(json,type)
            list?.let {
                val l = ArrayList<BaseBean>()
                for(path in it){
                    l.add(ItemPictureBean(path))
                }
                binding.viewPager.offscreenPageLimit = 10
                binding.viewPager.adapter = BaseAdapter(this,l)
                binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        binding.viewModel?.strTitleCount?.set("×  ${position+1}/${it.size}")
                    }
                })

            }
        }
    }

}