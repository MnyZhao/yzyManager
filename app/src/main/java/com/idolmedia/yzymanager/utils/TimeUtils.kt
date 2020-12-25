package com.idolmedia.yzymanager.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 *  时间：2020/10/20-17:45
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.yzymanager.utils TimeUtils
 *  描述：
 */
@SuppressLint("SimpleDateFormat")
object TimeUtils{

    /**date转换成yyyy-MM-dd格式的时间*/
    fun getTime(date: Date): String {
        //可根据需要自行截取数据显示
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return format.format(date)
    }

    /**获取半年前的时间*/
    fun getOrderHalfYear(): String {
        val nowTime = System.currentTimeMillis()
        val c = 6*30*24*60*60*1000L
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(Date(nowTime-c))
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式的时间
     * 比较结束时间是否小于当前时间
     */
    fun compareOvertime(nowTime: String?, endTime: String?): Boolean {
        Log.d("time",nowTime+"  "+endTime)
        if (nowTime.isNullOrEmpty() || endTime.isNullOrEmpty()){
            return false
        }

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val dataNow = format.parse(nowTime)
            val dataEnd = format.parse(endTime)
            return if(dataNow!=null&&dataEnd!=null){
                dataEnd.time<dataNow.time
            }else{
                false
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    fun getTimeFormatText(time: String?): String? {

        if (time==null){
            return ""
        }
        val dateTime = stringToDate(time,"yyyy-MM-dd HH:mm:ss")
        dateTime?.let {

            val c = Calendar.getInstance()
            val nowYear = c.get(Calendar.YEAR)
            val nowMonth = c.get(Calendar.MONTH)+1
            val nowDay = c.get(Calendar.DATE)

            val year = it.year + 1900
            val month = it.month+1
            val day = it.date

            if (nowYear == year && nowMonth == month){
                val h = if (it.hours<10) "0${it.hours}" else it.hours.toString()
                val m = if (it.minutes<10) "0${it.minutes}" else it.minutes.toString()
                val s = if (it.seconds<10) "0${it.seconds}" else it.seconds.toString()
                if (day == nowDay){
                    return "${h}:${m}:${s}"
                }
                else if(day+1 == nowDay){
                    return "昨天${h}:${m}:${s}"
                }
            }

            return time
        }

        return time
    }

    private fun stringToDate(strTime: String, formatType: String): Date? {
        var date: Date? = null
        try {
            val formatter = SimpleDateFormat(formatType)
            date = formatter.parse(strTime)
        } catch (e: Exception) {

        }

        return date
    }

    fun getJson(context: Context, fileName: String?): String? {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName!!)
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

}