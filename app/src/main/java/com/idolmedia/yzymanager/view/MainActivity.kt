package com.idolmedia.yzymanager.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.idolmedia.yzymanager.R
import com.idolmedia.yzymanager.adapter.ViewPager2Adapter
import com.idolmedia.yzymanager.base.BaseStateActivity
import com.idolmedia.yzymanager.base.BaseViewModel
import com.idolmedia.yzymanager.databinding.ActivityMainBinding
import com.idolmedia.yzymanager.view.main.HomeFragment
import com.idolmedia.yzymanager.view.main.MessageFragment
import com.idolmedia.yzymanager.view.main.MineFragment
import com.idolmedia.yzymanager.viewmodel.MainViewModel

class MainActivity : BaseStateActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): BaseViewModel {
       return ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun initTitleBar(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {

        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment())
        fragments.add(MessageFragment())
        fragments.add(MineFragment())
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.adapter = ViewPager2Adapter(this,fragments)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.viewModel?.currentItem?.set(position)
            }
        })

        binding.ivHome.setOnClickListener {
            binding.viewPager.currentItem = 0
        }

        binding.ivMessage.setOnClickListener {
            binding.viewPager.currentItem = 1
        }

        binding.ivMine.setOnClickListener {
            binding.viewPager.currentItem = 2
        }

        binding.tvHome.setOnClickListener {
            binding.viewPager.currentItem = 0
        }

        binding.tvMessage.setOnClickListener {
            binding.viewPager.currentItem = 1
        }

        binding.tvMine.setOnClickListener {
            binding.viewPager.currentItem = 2
        }

    }

}