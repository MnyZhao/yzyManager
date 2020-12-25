package com.idolmedia.yzymanager.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * 时间：2019/2/28-10:29
 * 公司:北京爱豆文化传媒有限公司
 * idolmedia.com.picturedemo.com.andy CustomLoading
 * 描述：
 */

public class CustomLoading {
    private static CustomLoading sInstance;
    private ZLoadingDialog dialog;
    private MyHandler myHandler;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            closeLoad();
        }
    }

    public static CustomLoading getInstance() {
        if (null == sInstance) {
            synchronized (CustomLoading.class) {
                if (null == sInstance) {
                    sInstance = new CustomLoading();
                }
            }
        }
        return sInstance;
    }

    public void showLoad(Context context, int delayMillis) {
        dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.WHITE)//颜色
                .setHintText("加载中......")
                .setHintTextSize(12)
                .setHintTextColor(Color.WHITE)  // 设置字体颜色
                .setDurationTime(1) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                .show();
        myHandler = new MyHandler();
        myHandler.sendEmptyMessageDelayed(1,delayMillis);
    }

    public void showLoad(Context context) {
        if (context==null){
            return;
        }
        dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.WHITE)//颜色
                .setHintText("加载中......")
                .setHintTextSize(12)
                .setHintTextColor(Color.WHITE)  // 设置字体颜色
                .setDurationTime(1) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                .show();
        myHandler = new MyHandler();
        myHandler.sendEmptyMessageDelayed(1,30000);
    }

    public void showLoad(Context context,String hint) {
        if (context==null){
            return;
        }
        dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.WHITE)//颜色
                .setHintText(hint)
                .setHintTextSize(12)
                .setHintTextColor(Color.WHITE)  // 设置字体颜色
                .setDurationTime(1) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                .show();
        myHandler = new MyHandler();
        myHandler.sendEmptyMessageDelayed(1,30000);
    }

    /**持续显示*/
    public void showLoadCancelable(Context context,String hint) {
        if (context==null){
            return;
        }
        dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setLoadingColor(Color.WHITE)//颜色
                .setHintText(hint)
                .setHintTextSize(12)
                .setHintTextColor(Color.WHITE)  // 设置字体颜色
                .setDurationTime(1) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                .show();
    }

    public void closeLoad() {
        if (dialog != null) {
            dialog.cancel();
            dialog=null;
        }
    }
}
