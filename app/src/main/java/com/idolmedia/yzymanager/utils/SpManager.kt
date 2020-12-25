package com.idolmedia.yzymanager.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idolmedia.yzymanager.model.entity.LoginEntity
import com.idolmedia.yzymanager.base.BaseApplication

/**
 *  时间：2019/4/23-13:45
 *  公司:北京爱豆文化传媒有限公司
 *  com.example.module_base.utils SpUserManager
 *  描述：SharedPreferences
 */
object SpManager{

    private val sp = BaseApplication.instance.getSharedPreferences("YZY_USER",Context.MODE_PRIVATE)
    private val spPhone = BaseApplication.instance.getSharedPreferences("YZY_PHONE",Context.MODE_PRIVATE)
    private var edit = sp.edit()
    private var editPhone = spPhone.edit()

    fun clearUserEntity(){
        edit.putInt("userIndex",0)
        edit.putString("user","")
        edit.apply()
    }

    /**获取用户信息全部信息*/
    fun getUserEntity(): LoginEntity?{
        val index = getUserIndex()
        getUserList()?.let {
            if (it.size>index){
                return it[index]
            }
        }

        return null
    }

    fun saveUserEntity(user: LoginEntity):Boolean{
        edit.putInt("userIndex",0).commit()
        val userList = getUserList()
        return if (userList.isNullOrEmpty()){
            val list = ArrayList<LoginEntity>()
            list.add(user)
            val editor = edit.putString("user",Gson().toJson(list))
            editor.commit()
        }else{
            val list = ArrayList<LoginEntity>()
            list.add(user)
            for(u in userList){
                if (u.datas.userId!=user.datas.userId){
                    list.add(u)
                }
            }
            val editor = edit.putString("user",Gson().toJson(list))
            editor.commit()
        }
    }

    fun refreshUserEntity(user: LoginEntity?){
        val userList = getUserList()
        userList?.let {
            val list = ArrayList<LoginEntity>()
            for(u in userList){
                if (u.datas.userId == user?.datas?.userId){
                    list.add(user)
                }else{
                    list.add(u)
                }
            }
            val editor = edit.putString("user",Gson().toJson(list))
            editor.commit()
        }
    }

    fun getUserEntity(index:Int): LoginEntity?{
        getUserList()?.let {
            if (it.size>index){
                val editor = edit.putInt("userIndex",index)
                editor.commit()
                return it[index]
            }
        }
        return null
    }

    fun getUserList():ArrayList<LoginEntity>?{

        val str = sp.getString("user","")
        str?.let {
            //存在字符串
            if (str.isNotEmpty()){
                //字符串不是空的
                val type = object : TypeToken<ArrayList<LoginEntity>>(){}.type
                val users = Gson().fromJson<ArrayList<LoginEntity>>(str, type)
                users?.let {
                    return users
                }
            }
        }

        return null
    }

    fun getUserIndex():Int{
        return sp.getInt("userIndex",0)
    }

    fun logout(){
        val index = getUserIndex()
        getUserList()?.let {
            it.removeAt(index)
            edit.putInt("userIndex",0)
            val editor = edit.putString("user",Gson().toJson(it))
            editor.commit()
        }
    }

    /**获取用户角色
     * OPERATIONAL_MANAGER（运营角色）
     * COMMON_PRODUCT_MERCHANT（商户管理-创建非会员类型商品）
     * VIP_PRODUCT_MERCHANT（商户管理-只创建会员类型商品）
     */
    fun getUserIdentity():String{
        val user  = getUserEntity()
        return user?.datas?.roleCode ?: ""
    }

    fun userIsManage():Boolean{
        return getUserIdentity() == "OPERATIONAL_MANAGER"
    }

    /**是否是海外系统*/
    fun isOverseasSystem():Boolean{
        return spPhone.getBoolean("isOverseas",false)
    }

    fun saveSystem(isOverseas:Boolean){
        editPhone.putBoolean("isOverseas",isOverseas)
        editPhone.apply()
    }

    /**是否是第一次打开app*/
    fun isFirstSplash():Boolean{
        return spPhone.getBoolean("firstSplash",true)
    }

    /**记录第一次打开app*/
    fun saveFirstSplash(){
        editPhone.putBoolean("firstSplash",false)
        editPhone.apply()
    }

    fun saveDeviceToken(value : String?){
        editPhone.putString("deviceToken",value)
        editPhone.apply()
    }

    fun getDeviceToken():String?{
        return spPhone.getString("deviceToken","")
    }

    fun getValueString(keyName:String):String?{
        return sp.getString(keyName,"")
    }

    fun getValueInt(keyName:String):Int{
        return sp.getInt(keyName,0)
    }

    fun getValueBoolean(keyName:String):Boolean{
        return sp.getBoolean(keyName,false)
    }

    fun saveValue(keyName: String,value : String?){
        edit.putString(keyName,value)
        edit.apply()
    }

    fun saveValue(keyName: String,value : Boolean){
        edit.putBoolean(keyName,value)
        edit.apply()
    }

    /**存储某个信息*/
    fun saveValue(keyName: String,value : Int){
        edit.putInt(keyName,value)
        edit.apply()
    }


    fun clear(){
        edit.clear()
        edit.commit()
    }

}