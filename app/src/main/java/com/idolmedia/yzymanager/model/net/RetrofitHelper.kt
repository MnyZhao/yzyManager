package com.idolmedia.yzymanager.model.net

import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.model.entity.*
import com.idolmedia.yzymanager.utils.SpManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class RetrofitHelper {

    companion object{
        private var helper : RetrofitHelper?= null
        fun instance(): RetrofitHelper {
            if (helper ==null){
                helper = RetrofitHelper()
            }
            return helper!!
        }
    }

    private fun <T: BaseEntity> subscribe(observable: Observable<T>, observer: BaseObserver<T>){
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    private fun onUpload(list: List<String>): List<MultipartBody.Part> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)//表单类型
        if (list.isNotEmpty()){
            for (i in list.indices) {
                val file = File(list[i])
                val body = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                builder.addFormDataPart("photo$i", file.name, body)
            }
        }

        return builder.build().parts
    }

    /**发布商品*/
    fun publishCommodity(map:HashMap<String,Any?>,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.commodityPublish(map),observer)
    }

    /**保存编辑后的商品*/
    fun saveCommodity(map:HashMap<String,Any?>,isAudit:Boolean,observer: BaseObserver<BaseEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.commoditySave(map),observer)
        }else{
            if (SpManager.userIsManage()){
                subscribe(RetrofitHttp.api.commodityPublish(map),observer)
            }else{
                subscribe(RetrofitHttp.api.commoditySave(map),observer)
            }
        }
    }

    /**发布商品的预加载数据*/
    fun queryCommodityPrecondition(observer: BaseObserver<CommodityPreconditionEntity>){
        subscribe(RetrofitHttp.api.queryCommodityPrecondition(),observer)
    }

    /**售中商品列表*/
    fun commodityList(map:HashMap<String,Any?>,observer: BaseObserver<CommodityListEntity>){
        subscribe(RetrofitHttp.api.commodityList(map),observer)
    }

    /**待审核商品-管理员*/
    fun commodityListAudioAdmin(map:HashMap<String,Any?>,observer: BaseObserver<CommodityListEntity>){
        subscribe(RetrofitHttp.api.commodityListAudioAdmin(map),observer)
    }

    /**审核中商品-商家*/
    fun commodityListAudioWaiting(map:HashMap<String,Any?>,observer: BaseObserver<CommodityListEntity>){
        subscribe(RetrofitHttp.api.commodityListAudioWaiting(map),observer)
    }

    fun commodityPutaway(putaway:Int,id:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.commodityPutaway(putaway,id),observer)
    }

    /**设置商品限购*/
    fun setCommodityLimit(isAudit: Boolean,limitCount:String,limitTimes:String,id:String,observer: BaseObserver<BaseEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.setCommodityLimitAudit(limitCount,limitTimes,id),observer)
        }else{
            subscribe(RetrofitHttp.api.setCommodityLimit(limitCount,limitTimes,id),observer)
        }
    }

    /**设置商品隐藏销量*/
    fun setCommodityHiddenSale(isAudit:Boolean,salesSecret:Int ,id:String,observer: BaseObserver<BaseEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.setCommodityHiddenSaleAudit(salesSecret ,id),observer)
        }else{
            subscribe(RetrofitHttp.api.setCommodityHiddenSale(salesSecret ,id),observer)
        }
    }

    /**设置商品隐藏*/
    fun setCommodityHiddenCommodity(isAudit:Boolean,isHide:Int ,id:String,observer: BaseObserver<BaseEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.setCommodityHiddenCommodityAudit(isHide ,id),observer)
        }else{
            subscribe(RetrofitHttp.api.setCommodityHiddenCommodity(isHide ,id),observer)
        }
    }

    /**设置商品隐藏*/
    fun setCommodityRankCount(count:String ,id:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.setCommodityRankCount(count ,id),observer)
    }

    /**设置商品附加信息与地址锁定 0-解锁地址；1-解锁附加信息；2-全部锁定*/
    fun setCommodityLock(lock:Int ,id:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.setCommodityLock(lock ,id),observer)
    }

    /**查询商品规格*/
    fun queryCommoditySpecification(isAudit: Boolean,id:String,observer: BaseObserver<CommoditySpecificationEntity>){
        if (isAudit){
            //审核中的尾款规格
            subscribe(RetrofitHttp.api.queryCommoditySpecificationAudit(id),observer)
        }else{
            subscribe(RetrofitHttp.api.queryCommoditySpecification(id),observer)
        }
    }

    /**调整库存数量*/
    fun saveInventory(operationStore:Int ,store:Int,ssid:String ,commodityId:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveInventory(operationStore ,store,ssid ,commodityId),observer)
    }

    /**调整库存数量*/
    fun saveCommodityRefund(isAudit: Boolean,refunds:String ,refundsReason:String,refundsTips:String ,commodityId:String,observer: BaseObserver<BaseEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.saveCommodityRefundAudit(refunds ,refundsReason,refundsTips ,commodityId),observer)
        }else{
            subscribe(RetrofitHttp.api.saveCommodityRefund(refunds ,refundsReason,refundsTips ,commodityId),observer)
        }
    }

    /**商品删除*/
    fun commodityDelete(id:String,isAudit: Boolean,observer: BaseObserver<BaseEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.commodityDeleteAudit(id),observer)
        }else{
            subscribe(RetrofitHttp.api.commodityDelete(id),observer)
        }
    }

    /**撤销商品审核*/
    fun commodityRevocation(id:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.commodityRevocation(id),observer)
    }

    /**查询分类商品*/
    fun queryCommodityList(sortMainType:String, pageNo:Int, observer: BaseObserver<CommodityListEntity>){
        subscribe(RetrofitHttp.api.queryCommodityList(sortMainType,pageNo),observer)
    }

    /**查询商品的排序位置*/
    fun queryCommoditySortNo(isStick:String,commodityId:String,sortMainType:String, sortNo:String,shoppingTo:String, observer: BaseObserver<SubjectSortEntity>){
        subscribe(RetrofitHttp.api.queryCommoditySortNo(isStick,commodityId, sortMainType, sortNo,shoppingTo),observer)
    }

    /**保存商品的排序位置*/
    fun saveCommoditySort(currPlace:String,mixtureId:String,commodityId:String,sortMainType:String,sortNum:String,shoppingTo:String, observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveCommoditySort(currPlace,mixtureId,commodityId,sortMainType,sortNum,shoppingTo),observer)
    }

    fun commodityStick(isStick:String,shoppingTo:String,shopcommonId:String,observer:BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.commodityStick(isStick,shoppingTo,shopcommonId),observer)
    }

    /**通过审核*/
    fun passedAudit(id:String,time:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.passedAudit(id,time),observer)
    }
    /**拒绝审核*/
    fun refuseAudit(id:String,reason:String,time:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.refuseAudit(id,reason,time),observer)
    }
    /**结束定金*/
    fun overReserve(isAudit : Boolean,id:String,shopType:String,observer: BaseObserver<BaseEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.overReserveAudit(id,shopType),observer)
        }else{
            subscribe(RetrofitHttp.api.overReserve(id,shopType),observer)
        }
    }

    /**开启尾款*/
    fun openFinal(isAudit: Boolean,map:HashMap<String,Any?>,observer: BaseObserver<BaseEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.openFinalAudit(map),observer)
        }else{
            subscribe(RetrofitHttp.api.openFinal(map),observer)
        }
    }

    /**商品详情*/
    fun commodityDetails(id:String,isAudit:Boolean,observer: BaseObserver<CommodityDetailsEntity>){
        if (isAudit){
            subscribe(RetrofitHttp.api.commodityDetailsManage(id),observer)
        }else{
            subscribe(RetrofitHttp.api.commodityDetails(id),observer)
        }
    }

    /**商品附加信息*/
    fun queryOrderAdditional(orderNum:String,observer: BaseObserver<OrderAdditionalEntity>){
        subscribe(RetrofitHttp.api.queryOrderAdditional(orderNum),observer)
    }

    /**商品附加信息-修改*/
    fun saveOrderAdditional(orderNum:String,additionContent:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveOrderAdditional(orderNum,additionContent),observer)
    }

    /**商品附加信息-修改*/
    fun saveOrderLogistics(orderNum:String,items:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveOrderLogistics(!SpManager.userIsManage(),items,orderNum),observer)
    }

    /**商品收货人信息-修改*/
    fun saveOrderConsignee(map:HashMap<String,Any?>,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveOrderConsignee(map),observer)
    }

    /**发送验证码*/
    fun sendSmsCode(accountNo:String,countryCode:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.sendSmsCode(accountNo, countryCode),observer)
    }

    /**更换主账号*/
    fun changeAccount(accountNo:String,countryCode:String,vfCode:String,observer: BaseObserver<BaseEntity>){
        val user = SpManager.getUserEntity()
        subscribe(RetrofitHttp.api.changeAccount(accountNo, countryCode,user?.datas?.username ?: "",vfCode),observer)
    }

    /**登陆-自动登录*/
    fun loginAuto(signTime:String,observer: BaseObserver<LoginEntity>){
        subscribe(RetrofitHttp.api.loginAuto(signTime,SpManager.getDeviceToken() ?: ""),observer)
    }

    /**登陆-三方登录*/
    fun loginThree(loginType:Int,map: HashMap<String, Any?>, observer: BaseObserver<LoginEntity>){
        if (loginType==0){
            subscribe(RetrofitHttp.api.loginThreeQQ(map),observer)
        }else if (loginType==1){
            subscribe(RetrofitHttp.api.loginThreeWX(map),observer)
        }else if (loginType==2){
            subscribe(RetrofitHttp.api.loginThreeWB(map),observer)
        }

    }

    /**登陆-修改密码*/
    fun passwordChange(map:HashMap<String,Any?>,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.passwordChange(map),observer)
    }

    /**获取当前时间按*/
    fun getCurrentTime(observer: BaseObserver<CurrentTimeEntity>){
        subscribe(RetrofitHttp.api.getCurrentTime(),observer)
    }

    /**获取运费地区信息*/
    fun getFreightAddress(observer: BaseObserver<FreightAddressEntity>){
        subscribe(RetrofitHttp.api.getFreightAddress(),observer)
    }

    /**创建/编辑运费模板*/
    fun saveFreightTemplate(map:HashMap<String,Any?>,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveFreightTemplate(map),observer)
    }

    /**删除运费模板*/
    fun deleteFreightTemplate(freightIds:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.deleteFreight(freightIds),observer)
    }

    /**商品链接*/
    fun getLink(commodityId:String,observer: BaseObserver<CommodityLinkEntity>){
        subscribe(RetrofitHttp.api.getLink(commodityId),observer)
    }

    /**商品测试链接*/
    fun getLinkTest(commodityId:String,observer: BaseObserver<CommodityLinkEntity>){
        subscribe(RetrofitHttp.api.getLinkTest(commodityId),observer)
    }

    /**获取订单列表*/
    fun getOrderList(map:HashMap<String,Any?>,observer: BaseObserver<OrderListEntity>){
        subscribe(RetrofitHttp.api.getOrderList(map),observer)
    }

    /**获取订单的商品列表*/
    fun getOrderCommodity(orderId:String,pageNo: Int,observer: BaseObserver<OrderListEntity>){
        subscribe(RetrofitHttp.api.getOrderCommodity(!SpManager.userIsManage(),orderId,pageNo),observer)
    }

    /**获取订单详情*/
    fun getOrderDetails(orderNum:String,observer: BaseObserver<OrderDetailsEntity>){
        subscribe(RetrofitHttp.api.getOrderDetails(!SpManager.userIsManage(),orderNum),observer)
    }

    /**查询关联爱豆*/
    fun queryIdoAssociated(name:String?,pageNo: Int,observer: BaseObserver<IdoAssociatedEntity>){
        subscribe(RetrofitHttp.api.queryIdoAssociated(name ?: "",pageNo),observer)
    }

    /**查询运费模板*/
    fun queryFreightList(pageNo:Int,fromId:String,observer: BaseObserver<FreightListEntity>){
        subscribe(RetrofitHttp.api.queryFreightList(pageNo,fromId),observer)
    }

    /**查询专题是运费模板*/
    fun querySubjectFreightList(pageNo:Int,isOverseas:Boolean,observer: BaseObserver<FreightListEntity>){
        subscribe(RetrofitHttp.api.querySubjectFreightList(pageNo,isOverseas),observer)
    }

    fun getHomeNote(observer: BaseObserver<HomeMessageEntity>){
        subscribe(RetrofitHttp.api.getHomeNote(),observer)
    }

    fun getHomeNoteOverseas(observer: BaseObserver<HomeMessageEntity>){
        subscribe(RetrofitHttp.api.getHomeNoteOverseas(),observer)
    }

    fun queryMessage(pageNo:Int,observer: BaseObserver<MessageListEntity>){
        subscribe(RetrofitHttp.api.queryMessage(pageNo),observer)
    }

    fun readMessage(id:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.readMessage(id),observer)
    }

    fun queryMessageStatus(id:String,timeBatch:String,observer: BaseObserver<MessageStatusEntity>){
        subscribe(RetrofitHttp.api.queryMessageStatus(id,timeBatch),observer)
    }

    fun querySubjectClassify(observer: BaseObserver<SubjectClassifyEntity>){
        subscribe(RetrofitHttp.api.querySubjectClassify(),observer)
    }

    /**专题选择商品*/
    fun querySubjectCommodity(map:HashMap<String,Any?>,observer: BaseObserver<CommodityListEntity>){
        subscribe(RetrofitHttp.api.querySubjectCommodity(map),observer)
    }

    /**专题选择商品*/
    fun querySubjectCommodity(id:String,observer: BaseObserver<CommodityListEntity>){
        subscribe(RetrofitHttp.api.querySubjectCommodity(id),observer)
    }

    /**专题选择商品*/
    fun queryBook(pageNo:Int,shopName:String,id:String,observer: BaseObserver<CommodityListEntity>){
        subscribe(RetrofitHttp.api.queryBook(pageNo,shopName, id),observer)
    }

    /**专题*/
    fun querySubject(pageNo:Int,isMergeExpress:String,isPutway:String,observer: BaseObserver<SubjectListEntity>){
        subscribe(RetrofitHttp.api.querySubject(pageNo,isMergeExpress, isPutway,"",""),observer)
    }

    /**专题*/
    fun querySubjectSearch(map:HashMap<String,Any?>,observer: BaseObserver<SubjectListEntity>){
        subscribe(RetrofitHttp.api.querySubjectSearch(map),observer)
    }

    /**专题的排序*/
    fun querySubjectSort(subjectId:String, sortNo:String, observer: BaseObserver<SubjectSortEntity>){
        subscribe(RetrofitHttp.api.querySubjectSort(subjectId,sortNo),observer)
    }

    /**保存编辑后的商品*/
    fun saveSubject(map:HashMap<String,Any?>,observer: BaseObserver<SubjectSaveEntity>){
        subscribe(RetrofitHttp.api.saveSubject(map),observer)
    }

    /**删除专题*/
    fun deleteSubject(subjectId:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.deleteSubject(subjectId),observer)
    }

    /**专题详情*/
    fun querySubjectDetails(id:String,observer: BaseObserver<SubjectDetailsEntity>){
        subscribe(RetrofitHttp.api.querySubjectDetails(id),observer)
    }

    /**专题上下架*/
    fun saveSubjectPutAway(id:String,putaway:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveSubjectPutAway(id,putaway),observer)
    }

    /**调整专题的商品顺序*/
    fun saveSubjectCommoditySort(sortProductList:String,deleteCommodity:String,subjectId:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveSubjectCommoditySort(sortProductList,deleteCommodity,subjectId),observer)
    }

    /**专题*/
    fun querySubjectList(pageNo:Int,observer: BaseObserver<SubjectListEntity>){
        subscribe(RetrofitHttp.api.querySubjectList(pageNo),observer)
    }

    /**调整专题顺序*/
    fun saveSubjectSort(currPlace:String,sortNum:String,subjectId:String,observer: BaseObserver<BaseEntity>){
        subscribe(RetrofitHttp.api.saveSubjectSort(currPlace, sortNum, subjectId),observer)
    }

    /**上传图片*/
    fun uploadImage(path:String,observer: BaseObserver<UploadImageEntity>){
        val list = ArrayList<String>()
        list.add(path)
        subscribe(RetrofitHttp.api.uploadImage(onUpload(list)),observer)
    }

    private fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}