package com.idolmedia.yzymanager.model

/**
 *  时间：2019/6/25-17:56
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.net ApiConstant
 *  描述：
 */
object ApiConstant {

    //上线路径
//    const val BASEURL  = "https://www.withfans.com"
//    const val BASEURL_OVERSEAS = "https://www.withfans.cn"
//    const val SHARE_URL = "web"

    //测试路径
    const val BASEURL = "http://pay.withfans.com"
    const val BASEURL_OVERSEAS = "http://hktest.withfans.com"
    const val SHARE_URL = "webTest"

    //高利峰
    //const val BASEURL = "http://192.168.3.2:8280"
    //const val BASEURL_OVERSEAS = "http://192.168.3.2:8580"
    //波波
    //const val BASEURL = "http://192.168.3.3:8580/"
    //const val BASEURL_OVERSEAS = "http://192.168.3.3:8580"
    //老大本地
    //const val BASEURL = "http://192.168.3.8:8580"
    //线上测试
    //const val BASEURL = "http://106.14.161.229:8580"
    //张昊
    //const val BASEURL = "http://192.168.3.111:8280"

    const val PROT_NEW = "phonemanage"

    /**首页消息*/
    const val MESSAGE_HOME = "$PROT_NEW/statistics/info"
    const val MESSAGE_HOME_OVERSEAS = "$PROT_NEW/statistics/messageInfo"
    /**首页消息-列表*/
    const val MESSAGE_LIST = "$PROT_NEW/message/list"
    /**首页消息-读消息*/
    const val MESSAGE_READ = "$PROT_NEW/message/read"
    /**首页消息-消息状态*/
    const val MESSAGE_STATUS = "$PROT_NEW/message/getMessage"

    /**1发布商品*/
    const val COMMODITY_PUBLISH = "$PROT_NEW/product/saveProduct"
    /**1-1保存商品*/
    const val COMMODITY_SAVE = "$PROT_NEW/checkProduct/editProduct"

    /**2登陆*/
    const val LOGIN = "$PROT_NEW/account/login"
    /**2-1自动登陆*/
    const val LOGIN_AUTO = "$PROT_NEW/auto/login"
    /**2-2三方登陆-微信*/
    const val LOGIN_THREE_WX = "$PROT_NEW/wx/login"
    /**2-3三方登陆-微博*/
    const val LOGIN_THREE_WB = "$PROT_NEW/wb/login"
    /**2-4三方登陆-腾讯*/
    const val LOGIN_THREE_QQ = "$PROT_NEW/qq/login"
    /**2-5登陆*/
    const val LOGIN_PASSWORD_CHANGE = "$PROT_NEW/pwd/update"
    /**3图形验证码*/
    const val IMAGE_CODE = "$PROT_NEW/account/getVerifyCode"
    /**3-1验证码*/
    const val SMS_CODE = "$PROT_NEW/mainAcc/sendVCode"
    /**3-2更换账号*/
    const val ACCOUNT_CHANGE = "$PROT_NEW/mainAcc/update"

    /**4当前时间*/
    const val CURRENT_TIME = "$PROT_NEW/time/current"

    /**5上传图片*/
    const val UPLOAD_IMAGE = "$PROT_NEW/product/uploadPicture"

    /**6售中商品列表*/
    const val COMMODITY_LIST = "$PROT_NEW/product/inSaleProductList"
    /**6-1-1商品列表待审核*/
    const val COMMODITY_LIST_AUDIO_ADMIN = "$PROT_NEW/checkProduct/waitCheckProduct"
    /**6-1-2商品列表审核中*/
    const val COMMODITY_LIST_AUDIO_WAITING = "$PROT_NEW/checkProduct/checkingProduct"
    /**6-1商品详情-审核*/
    const val COMMODITY_DETAILS_MANAGE = "$PROT_NEW/checkProduct/details"
    /**6-1-1商品详情*/
    const val COMMODITY_DETAILS = "$PROT_NEW/product/details"
    /**6-2设置商品上下架*/
    const val COMMODITY_SET_PUTAWAY = "$PROT_NEW/product/onPutaway"
    /**6-3设置商品限购*/
    const val COMMODITY_SET_LIMIT = "$PROT_NEW/product/limitCountProduct"
    /**6-3设置商品限购*/
    const val COMMODITY_SET_LIMIT_AUDIT = "$PROT_NEW/checkProduct/limitCountProduct"
    /**6-4设置商品隐藏销量*/
    const val COMMODITY_SET_HIDDEN_SALE = "$PROT_NEW/product/secretProductSales"
    /**6-4设置商品隐藏销量_审核商品*/
    const val COMMODITY_SET_HIDDEN_SALE_AUDIT = "$PROT_NEW/checkProduct/secretProductSales"
    /**6-5设置商品隐藏商品*/
    const val COMMODITY_SET_HIDDEN_COMMODITY = "$PROT_NEW/product/onHide"
    /**6-5设置商品隐藏商品_审核商品*/
    const val COMMODITY_SET_HIDDEN_COMMODITY_AUDIT = "$PROT_NEW/checkProduct/onHide"
    /**6-6设置商品排行数量*/
    const val COMMODITY_SET_RANK_COUNT = "$PROT_NEW/product/updateShowBuyhistory"
    /**6-7设置商品附加信息与地址锁定*/
    const val COMMODITY_SET_LOCK = "$PROT_NEW/product/lockAddition"
    /**6-8商品规格*/
    const val COMMODITY_SPECIFICATION = "$PROT_NEW/product/lookShopStore"
    /**6-8-1审核中的尾款*/
    const val COMMODITY_SPECIFICATION_AUDIT = "$PROT_NEW/checkProduct/lookReserveFinal"
    /**6-7设置商品库存数量*/
    const val COMMODITY_SAVE_INVENTORY = "$PROT_NEW/product/editStore"
    /**6-8设置商品设置退款*/
    const val COMMODITY_SAVE_REFUND = "$PROT_NEW/product/editRefundReason"
    /**6-8-1设置审核商品设置退款*/
    const val COMMODITY_SAVE_REFUND_AUDIT = "$PROT_NEW/checkProduct/editRefundReason"
    /**6-9商品删除*/
    const val COMMODITY_DELETE = "$PROT_NEW/product/delete"
    /**6-9-1商品删除-审核*/
    const val COMMODITY_DELETE_AUDIT = "$PROT_NEW/checkProduct/delete"
    /**6-10撤销商品审核*/
    const val COMMODITY_REVOCATION = "$PROT_NEW/checkProduct/checkRecall"
    /**6-11通过审核*/
    const val COMMODITY_AUDIT_PASSED = "$PROT_NEW/checkProduct/checkSuccess"
    /**6-12拒绝审核*/
    const val COMMODITY_AUDIT_REFUSE = "$PROT_NEW/checkProduct/checkReject"
    /**6-13结束定金*/
    const val COMMODITY_OVER_RESERVE = "$PROT_NEW/product/endReserveProductEndTime"
    /**6-13-1结束定金*/
    const val COMMODITY_OVER_RESERVE_AUDIT = "$PROT_NEW/checkProduct/endReserveProductEndTime"
    /**6-14开启尾款-审核表*/
    const val COMMODITY_OPEN_FINAL_AUDIT = "$PROT_NEW/checkProduct/saveReserveFinal"
    /**6-14开启尾款*/
    const val COMMODITY_OPEN_FINAL = "$PROT_NEW/product/saveReserveFinal"
    /**6-15商品链接*/
    const val COMMODITY_LINK = "$PROT_NEW/product/shopCopyUrl"
    /**6-16商品测试链接*/
    const val COMMODITY_LINK_TEST = "$PROT_NEW/product/copyTestUrl"
    /**6-17发布商品预加载数据*/
    const val COMMODITY_PUBLISH_CACHE = "$PROT_NEW/product/savePrepose"
    /**6-18可排序的商品*/
    const val COMMODITY_LIST_SORT = "$PROT_NEW/sort/list"
    /**6-19商品当前的顺序*/
    const val COMMODITY_SORT_NO = "$PROT_NEW/sort/currSort"
    /**6-20保存商品的排序*/
    const val COMMODITY_SORT_SAVE = "$PROT_NEW/sort/move"
    /**6-21保存商品的排序*/
    const val COMMODITY_SORT_TOP = "$PROT_NEW/sort/onStick"

    /**8订单列表*/
    const val ORDER_LIST = "$PROT_NEW/order/orderList"
    /**8-1订单状态数量*/
    const val ORDER_HOME_DETAILS = "$PROT_NEW/order/statusCountList"
    /**8-1-1首页订单状态数量*/
    const val ORDER_HOME_NUM = "$PROT_NEW/manageHome/orderCount"
    /**8-2订单详情*/
    const val ORDER_DETAILS = "$PROT_NEW/order/orderDetail"
    /**8-2-1订单商品列表*/
    const val ORDER_COMMODITY_LIST = "$PROT_NEW/order/mergeOrderList"
    /**8-3订单物流公司*/
    const val ORDER_EXPRESS_COMPANY = "$PROT_NEW/order/expressCompanyList"
    /**8-4订单确认发货*/
    const val ORDER_CONFIRM_DELIVERY = "$PROT_NEW/order/confirmExpress"
    /**8-5订单的物流信息*/
    const val ORDER_LOGISTICS = "$PROT_NEW/order/seeExpressage"
    /**8-6订单的运单号*/
    const val ORDER_LOGISTICS_NUMBER = "$PROT_NEW/order/queryExpressInfo"
    /**8-7同意退款*/
    const val ORDER_REFUND_AGREE = "$PROT_NEW/orderRefundInfo/addOrderRefundInfo"
    /**8-8订单附加信息*/
    const val ORDER_ADDITIONAL = "$PROT_NEW/order/orderAdditionInfo"
    /**8-8修改订单附加信息*/
    const val ORDER_ADDITIONAL_EDIT = "$PROT_NEW/order/orderEditAdditionInfo"
    /**8-8修改订单物流信息*/
    const val ORDER_EDIT_LOGISTICS = "$PROT_NEW/order/orderEditWuliuInfo"
    /**8-9订单收货人信息修改*/
    const val ORDER_CONSIGNEE_EDIT = "$PROT_NEW/order/orderEditConsigneeInfo"
    /**9运费地区城市信息*/
    const val FREIGHT_AREA = "$PROT_NEW/freight/getAreaCodeOrName"

    /**10-1运费模板列表*/
    const val FREIGHT_LIST = "$PROT_NEW/freight/freightList"
    /**10-1-1运费模板列表*/
    const val FREIGHT_LIST_SUBJECT = "$PROT_NEW/subject/mergeFrightList"
    /**10-2创建运费模板*/
    const val FREIGHT_CREATE = "$PROT_NEW/freight/saveFreight"
    /**10-4删除运费模板*/
    const val FREIGHT_DELETE = "$PROT_NEW/freight/deleteFreight"
    /**10-5复制运费模板*/
    const val FREIGHT_COPY = "$PROT_NEW/freight/copyFreight"

    /**15上下架*/
    const val COMMODITY_OPERATION_DOWN = "$PROT_NEW/product/batchPutaway"
    /**16商品删除*/
    const val COMMODITY_OPERATION_DELETE = "$PROT_NEW/product/batchDeleteShop"

    /**17关联爱豆*/
    const val IDO_ASSOCIATED = "$PROT_NEW/product/productIdol"
    /**18-1附加信息*/
    const val ADDITIONAL_LIST = "$PROT_NEW/addition/list"
    /**18-1-1附加信息*/
    const val ADDITIONAL_LIST_EDIT = "$PROT_NEW/addition/editShopAdditionList"
    /**18-2附加信息-删除*/
    const val ADDITIONAL_DELETE = "$PROT_NEW/addition/delete"
    /**18-3附加信息-添加编辑*/
    const val ADDITIONAL_ADD_EDIT = "$PROT_NEW/addition/save"


    /**20-1专题分类*/
    const val SUBJECT_CLASSIFY = "$PROT_NEW/subject/catalogList"
    /**18-3专题发布-选择商品*/
    const val SUBJECT_COMMODITY = "$PROT_NEW/subject/productList"
    /**18-4专题发布-选择电子刊*/
    const val SUBJECT_BOOK = "$PROT_NEW/subject/eleBookList"
    /**18-5发专题*/
    const val SUBJECT_SAVE = "$PROT_NEW/subject/save"
    /**18-6专题*/
    const val SUBJECT_LIST = "$PROT_NEW/subject/list"
    /**18-7专题删除*/
    const val SUBJECT_DELETE = "$PROT_NEW/subject/deleteSubject"
    /**18-8专题详情*/
    const val SUBJECT_DETAILS = "$PROT_NEW/subject/detail"
    /**18-9专题上下架*/
    const val SUBJECT_PUT_AWAY = "$PROT_NEW/subject/putaway"
    /**18-9专题的商品*/
    const val SUBJECT_COMMODITY_LIST = "$PROT_NEW/subject/sortProduct"
    /**18-9专题的商品排序*/
    const val SUBJECT_COMMODITY_SORT = "$PROT_NEW/subject/editSortProduct"
    /**18-9专题排序用列表*/
    const val SUBJECT_SORT_LIST = "$PROT_NEW/subject/subjectSortList"
    /**18-9专题排序保存*/
    const val SUBJECT_SORT_SAVE = "$PROT_NEW/subject/editSort"
    /**18-9专题的排序*/
    const val SUBJECT_SORT = "$PROT_NEW/subject/subjectCurrPlace"

}