package com.idolmedia.yzymanager.utils.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 *  时间：2020/12/2-10:39
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.utils.animation OrderTipAnimation
 *  描述：
 */
object OrderTipAnimation {

    fun show(view:View?,endListener: AnimationEndListener){
        view?.let {
            val transXAnim = ObjectAnimator.ofFloat(view, "translationY", view.translationY, 0f)
            transXAnim.interpolator = LinearInterpolator()
            transXAnim.duration = 300
            transXAnim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    endListener.animationEnd()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
            transXAnim.start()
        }

    }

    fun hide(view:View?,endListener: AnimationEndListener){
        view?.let {
            val transXAnim = ObjectAnimator.ofFloat(view, "translationY", view.translationY, view.height.toFloat())
            transXAnim.interpolator = LinearInterpolator()
            transXAnim.duration = 300
            transXAnim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    endListener.animationEnd()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
            transXAnim.start()
        }
    }

    interface AnimationEndListener{
        fun animationEnd()
    }

}