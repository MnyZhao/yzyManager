package com.idolmedia.yzymanager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *  时间：2020/9/28-18:36
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.adapter ViewPager2FragmentAdaoter
 *  描述：
 */
class ViewPager2Adapter(manager: FragmentManager, lifecycle: Lifecycle, private val fragments: ArrayList<Fragment>) : FragmentStateAdapter(manager,lifecycle) {

    constructor(activity:FragmentActivity, fragments: ArrayList<Fragment>):this(activity.supportFragmentManager,activity.lifecycle,fragments)

    constructor(fragment:Fragment, fragments: ArrayList<Fragment>):this(fragment.childFragmentManager,fragment.lifecycle,fragments)

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
       return fragments[position]
    }

}