package com.idolmedia.yzymanager.model

import com.idolmedia.yzymanager.base.BaseEntity
import com.idolmedia.yzymanager.model.entity.*
import com.idolmedia.yzymanager.model.net.BaseObserver
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface Api {

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_PUBLISH)
    fun commodityPublish(@FieldMap map: HashMap<String, Any?>): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SAVE)
    fun commoditySave(@FieldMap map: HashMap<String, Any?>): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_LIST)
    fun commodityList(@FieldMap map: HashMap<String, Any?>): Observable<CommodityListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_LIST_AUDIO_ADMIN)
    fun commodityListAudioAdmin(@FieldMap map: HashMap<String, Any?>): Observable<CommodityListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_LIST_AUDIO_WAITING)
    fun commodityListAudioWaiting(@FieldMap map: HashMap<String, Any?>): Observable<CommodityListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_PUTAWAY)
    fun commodityPutaway(@Field("putaway") putaway:Int,@Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_OVER_RESERVE)
    fun overReserve(@Field("shopcommonId") id:String,@Field("shopType") shopType:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_OVER_RESERVE_AUDIT)
    fun overReserveAudit(@Field("shopcommonId") id:String,@Field("shopType") shopType:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_LIMIT)
    fun setCommodityLimit(@Field("limitCount") limitCount:String,
                          @Field("limitTimes") limitTimes:String,
                          @Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_LIMIT_AUDIT)
    fun setCommodityLimitAudit(@Field("limitCount") limitCount:String,
                               @Field("limitTimes") limitTimes:String,
                               @Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_LINK)
    fun getLink(@Field("shopcommonId") id:String): Observable<CommodityLinkEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_LINK_TEST)
    fun getLinkTest(@Field("shopcommonId") id:String): Observable<CommodityLinkEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_HIDDEN_SALE)
    fun setCommodityHiddenSale(@Field("salesSecret") salesSecret:Int,
                               @Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_HIDDEN_SALE_AUDIT)
    fun setCommodityHiddenSaleAudit(@Field("salesSecret") salesSecret:Int,
                                    @Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_HIDDEN_COMMODITY)
    fun setCommodityHiddenCommodity(@Field("isHide") isHide:Int,
                                    @Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_HIDDEN_COMMODITY_AUDIT)
    fun setCommodityHiddenCommodityAudit(@Field("isHide") isHide:Int,
                                         @Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_RANK_COUNT)
    fun setCommodityRankCount(@Field("showBuyhistory") count:String, @Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SET_LOCK)
    fun setCommodityLock(@Field("interventionType") lock:Int, @Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SAVE_INVENTORY)
    fun saveInventory(@Field("operationStore") operationStore:Int,
                      @Field("store") store:Int,
                      @Field("catalogId") catalogId:String,
                      @Field("shopcommonId") shopcommonId:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SAVE_REFUND)
    fun saveCommodityRefund(@Field("refunds") operationStore:String,
                            @Field("refundsReason") store:String,
                            @Field("refundsTips") catalogId:String,
                            @Field("shopcommonId") shopcommonId:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SAVE_REFUND_AUDIT)
    fun saveCommodityRefundAudit(@Field("refunds") operationStore:String,
                                 @Field("refundsReason") store:String,
                                 @Field("refundsTips") catalogId:String,
                                 @Field("shopcommonId") shopcommonId:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_DELETE)
    fun commodityDelete(@Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_DELETE_AUDIT)
    fun commodityDeleteAudit(@Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_DETAILS)
    fun commodityDetails(@Field("shopcommonId") id:String): Observable<CommodityDetailsEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_DETAILS_MANAGE)
    fun commodityDetailsManage(@Field("shopcommonId") id:String): Observable<CommodityDetailsEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_REVOCATION)
    fun commodityRevocation(@Field("shopcommonId") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_LIST_SORT)
    fun queryCommodityList(@Field("sortMainType") sortMainType:String,
                           @Field("pageNo") pageNo:Int): Observable<CommodityListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SORT_NO)
    fun queryCommoditySortNo(@Field("isStick") isStick:String,
                             @Field("shopcommonId") commodityId:String,
                             @Field("sortMainType") sortMainType:String,
                             @Field("sortNo") sortNo:String,
                             @Field("shoppingTo") shoppingTo:String): Observable<SubjectSortEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SORT_SAVE)
    fun saveCommoditySort(@Field("currPlace") currPlace:String,
                          @Field("mixtureId") mixtureId:String,
                          @Field("shopcommonId") commodityId:String,
                          @Field("sortMainType") sortMainType:String,
                          @Field("sortNum") sortNo:String,
                          @Field("shoppingTo") shoppingTo:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SORT_TOP)
    fun commodityStick(@Field("isStick") currPlace:String,
                       @Field("shoppingTo") mixtureId:String,
                       @Field("shopcommonId") commodityId:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_AUDIT_PASSED)
    fun passedAudit(@Field("shopcommonId") id:String,
                    @Field("timesTamp") time:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_AUDIT_REFUSE)
    fun refuseAudit(@Field("shopcommonId") id:String,
                    @Field("checkReason") reason:String,
                    @Field("timesTamp") time:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_OPEN_FINAL)
    fun openFinal(@FieldMap map: HashMap<String, Any?>): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_OPEN_FINAL_AUDIT)
    fun openFinalAudit(@FieldMap map: HashMap<String, Any?>): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SPECIFICATION)
    fun queryCommoditySpecification(@Field("shopcommonId") id:String): Observable<CommoditySpecificationEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.COMMODITY_SPECIFICATION_AUDIT)
    fun queryCommoditySpecificationAudit(@Field("shopcommonId") id:String): Observable<CommoditySpecificationEntity>

    @POST(value= ApiConstant.COMMODITY_PUBLISH_CACHE)
    fun queryCommodityPrecondition(): Observable<CommodityPreconditionEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SMS_CODE)
    fun sendSmsCode(@Field("accountNo") accountNo:String,
                    @Field("countryCode") countryCode:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ACCOUNT_CHANGE)
    fun changeAccount(@Field("newAccountNo") accountNo:String,
                      @Field("countryCode") countryCode:String,
                      @Field("oldAccountNo") oldAccountNo:String,
                      @Field("vfCode") vfCode:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.LOGIN_PASSWORD_CHANGE)
    fun passwordChange(@FieldMap map: HashMap<String, Any?>): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.LOGIN_THREE_WX)
    fun loginThreeWX(@FieldMap map: HashMap<String, Any?>): Observable<LoginEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.LOGIN_THREE_WB)
    fun loginThreeWB(@FieldMap map: HashMap<String, Any?>): Observable<LoginEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.LOGIN_THREE_QQ)
    fun loginThreeQQ(@FieldMap map: HashMap<String, Any?>): Observable<LoginEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.LOGIN_AUTO)
    fun loginAuto(@Field("signTime") signTime:String,
                  @Field("deviceToken") deviceToken:String): Observable<LoginEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.LOGIN_AUTO)
    suspend fun loginAutoCoroutine(@Field("signTime") signTime:String,
                           @Field("deviceToken") deviceToken:String): Call<LoginEntity>

    @POST(value= ApiConstant.CURRENT_TIME)
    fun getCurrentTime(): Observable<CurrentTimeEntity>

    @POST(value= ApiConstant.CURRENT_TIME)
    suspend fun getCoroutinesCurrentTime():CurrentTimeEntity

    @FormUrlEncoded
    @POST(value= ApiConstant.LOGIN)
    suspend fun loginPasswordCoroutines(@FieldMap map: HashMap<String, Any?>): LoginEntity

    @POST(value= ApiConstant.FREIGHT_AREA)
    fun getFreightAddress(): Observable<FreightAddressEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.FREIGHT_CREATE)
    fun saveFreightTemplate(@FieldMap map: HashMap<String, Any?>): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.FREIGHT_DELETE)
    fun deleteFreight(@Field("freightIds") freightIds:String): Observable<BaseEntity>

    @Multipart
    @POST(value= ApiConstant.UPLOAD_IMAGE)
    fun uploadImage(@Part partList: List<MultipartBody.Part>): Observable<UploadImageEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_LIST)
    fun getOrderList(@FieldMap map: HashMap<String, Any?>): Observable<OrderListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_COMMODITY_LIST)
    fun getOrderCommodity(@Field("isBusiness") isBusiness:Boolean,
                          @Field("mergeOrderNum") orderNum:String,
                          @Field("pageNo") pageNo:Int): Observable<OrderListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_DETAILS)
    fun getOrderDetails(@Field("isBusiness") isBusiness:Boolean,
                        @Field("mergeOrderNum") orderNum:String): Observable<OrderDetailsEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_ADDITIONAL)
    fun queryOrderAdditional(@Field("orderNum") orderNum:String): Observable<OrderAdditionalEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_ADDITIONAL_EDIT)
    fun saveOrderAdditional(@Field("orderNum") orderNum:String,
                            @Field("additionContent") additional:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_EDIT_LOGISTICS)
    fun saveOrderLogistics(@Field("isBusiness") isBusiness:Boolean,
                           @Field("items") items:String,
                           @Field("mergeOrderNum") mergeOrderNum:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_CONSIGNEE_EDIT)
    fun saveOrderConsignee(@FieldMap map: HashMap<String, Any?>): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_HOME_DETAILS)
    fun getHomeOrderDetails(@FieldMap map: HashMap<String, Any?>): Observable<OrderStatusNumEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ORDER_HOME_NUM)
    fun getHomeOrderNum(@FieldMap map: HashMap<String, Any?>): Observable<OrderStatusNumEntity>


    @FormUrlEncoded
    @POST(value= ApiConstant.IDO_ASSOCIATED)
    fun queryIdoAssociated(@Field("idoName") idoName:String,
                           @Field("pageNo") pageNo:Int): Observable<IdoAssociatedEntity>

    @POST(value= ApiConstant.ADDITIONAL_LIST)
    fun queryAdditional(): Observable<AdditionalEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ADDITIONAL_ADD_EDIT)
    fun saveAdditional(@FieldMap map: HashMap<String, Any?>): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.ADDITIONAL_DELETE)
    fun deleteAdditional(@Field("attitionId") attitionId:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.FREIGHT_LIST)
    fun queryFreightList(@Field("pageNo") pageNo:Int,
                         @Field("fromId") fromId: String): Observable<FreightListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.FREIGHT_LIST_SUBJECT)
    fun querySubjectFreightList(@Field("pageNo") pageNo:Int,
                                @Field("isOverseas") isOverseas:Boolean): Observable<FreightListEntity>

    @POST(value= ApiConstant.MESSAGE_HOME)
    fun getHomeNote(): Observable<HomeMessageEntity>

    @POST(value= ApiConstant.MESSAGE_HOME_OVERSEAS)
    fun getHomeNoteOverseas(): Observable<HomeMessageEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.MESSAGE_LIST)
    fun queryMessage(@Field("pageNo") pageNo:Int): Observable<MessageListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.MESSAGE_READ)
    fun readMessage(@Field("id") id:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.MESSAGE_STATUS)
    fun queryMessageStatus(@Field("id") id:String,
                           @Field("timeBatch") timeBatch:String): Observable<MessageStatusEntity>

    @POST(value= ApiConstant.SUBJECT_CLASSIFY)
    fun querySubjectClassify(): Observable<SubjectClassifyEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_COMMODITY)
    fun querySubjectCommodity(@FieldMap map: HashMap<String, Any?>): Observable<CommodityListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_COMMODITY_LIST)
    fun querySubjectCommodity(@Field("subjectId") id:String): Observable<CommodityListEntity>


    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_BOOK)
    fun queryBook(@Field("pageNo") pageNo:Int,
                  @Field("shopName") shopName:String,
                  @Field("shopcommonId") id:String): Observable<CommodityListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_LIST)
    fun querySubject(@Field("pageNo") pageNo:Int,
                     @Field("isMergeExpress") isMergeExpress:String,
                     @Field("isPutway") isPutway:String,
                     @Field("subjectId") subjectId:String,
                     @Field("subjectName") subjectName:String): Observable<SubjectListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_LIST)
    fun querySubjectSearch(@FieldMap map: HashMap<String, Any?>): Observable<SubjectListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_SORT)
    fun querySubjectSort(@Field("subjectId") subjectId:String,
                         @Field("sortNo") sortNo:String): Observable<SubjectSortEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_SAVE)
    fun saveSubject(@FieldMap map: HashMap<String, Any?>): Observable<SubjectSaveEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_DELETE)
    fun deleteSubject(@Field("subjectId") subjectId:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_DETAILS)
    fun querySubjectDetails(@Field("subjectId") subjectId:String): Observable<SubjectDetailsEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_PUT_AWAY)
    fun saveSubjectPutAway(@Field("subjectId") subjectId:String,
                           @Field("putaway") putaway:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_COMMODITY_SORT)
    fun saveSubjectCommoditySort(@Field("sortProductList") sortProductList:String,
                                 @Field("deleteSubjectProduct") deleteSubjectProduct:String,
                                 @Field("subjectId") subjectId:String): Observable<BaseEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_SORT_LIST)
    fun querySubjectList(@Field("pageNo") pageNo:Int): Observable<SubjectListEntity>

    @FormUrlEncoded
    @POST(value= ApiConstant.SUBJECT_SORT_SAVE)
    fun saveSubjectSort(@Field("currPlace") currPlace:String,
                        @Field("sortNum") sortNum:String,
                        @Field("subjectId") subjectId:String): Observable<BaseEntity>

}