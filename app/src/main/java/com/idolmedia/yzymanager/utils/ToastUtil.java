package com.idolmedia.yzymanager.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.idolmedia.yzymanager.base.BaseApplication;

/**
 * Toast统一管理类
 */
public class ToastUtil {

    private static Toast toast;

    private static void initToast(CharSequence message, int duration) {
        Message msg = new Message();
        msg.obj = message;
        msg.arg1=duration;
        if (toast == null) {
            msg.what=0;
        } else {
            msg.what=1;
        }
        handler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    private static Handler handler = new Handler(Looper.getMainLooper()){
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try {
                if (msg.what==0){
                    toast = Toast.makeText(BaseApplication.instance, msg.obj.toString(), msg.arg1);
                    toast.setGravity(Gravity.CENTER,0,0);
                }else{
                    toast.setText(msg.obj.toString());
                    toast.setDuration(msg.arg1);
                }
                toast.show();
            } catch (Exception e){

            }
        }
    };

    /**
     * 自定义显示Toast时间
     *
     * @param message 内容
     * @param duration 时间（毫秒）
     */
    public static void show(CharSequence message, int duration) {
        initToast(message, duration);
    }

    public static void show(CharSequence message) {
        initToast(message, 1000);
    }





}
