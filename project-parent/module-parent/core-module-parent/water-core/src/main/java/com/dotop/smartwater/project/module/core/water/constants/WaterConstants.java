package com.dotop.smartwater.project.module.core.water.constants;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class WaterConstants {

    // 运维的企业ID
    public static final String ADMIN_ENTERPRISE_ID = "28";
    // 东信的企业ID
    public static final String DX_ENTERPRISE_ID = "44";

    public static final String SELECT_TYPE_ROLE = "role";
    public static final String SELECT_TYPE_USER = "user";

    public static final String SELECT_TYPE_DEFAULT_ROLE_ID = "-1";
    public static final String SELECT_TYPE_DEFAULT_ROLE_NAME = "水司最高级管理员";
    public static final String SELECT_TYPE_ADMIN_ROLE_NAME = "系统最高级管理员";

    // 流水号每次生成的最大个数
    public static final int NUM_RULE_SET_MAX_VALUE = 10000;

    /**
     * 最高级系统管理员
     */
    public static final int USER_TYPE_ADMIN = 0;
    /**
     * 水司最高级系统管理员
     */
    public static final int USER_TYPE_ADMIN_ENTERPRISE = 1;
    /**
     * 水司普通用户
     */
    public static final int USER_TYPE_ENTERPRISE_NORMAL = 2;

    // 已交费
    public static final int PAY_STATUS_YES = 1;
    // 未交费
    public static final int PAY_STATUS_NO = 0;

    // 已保存
    public static final int OUTSTORAGE_STATUS_SAVE = 1;
    // 已出库
    public static final int OUTSTORAGE_STATUS_OUT = 0;

    /**
     * 业主常量
     */
    // 销户
    public static final int OWNER_STATUS_DELETE = 0;
    // 开户
    public static final int OWNER_STATUS_CREATE = 1;
    // 未开户
    public static final int OWNER_STATUS_UNOPRN = 2;
    // 过户
    public static final int OWNER_STATUS_TRANSFER = 3;
    // 换表
    public static final int OWNER_STATUS_CHANGE = 4;

    /**
     * 优惠券 Coupon
     */
    public static final int COUPON_STATUS_NORMAL = 0;
    public static final int COUPON_STATUS_UNAVAILABLE = 1;
    public static final int COUPON_STATUS_DISABLE = 2;
    public static final int COUPON_STATUS_USED = 3;
    public static final int COUPON_STATUS_EXPIRE = 4;
    public static final int COUPON_TYPE_DJQ = 1;
    public static final int COUPON_TYPE_DKQ = 2;

    /**
     * 打印 DesignPrint 启用
     */
    public static final int DESIGN_PRINT_STATUS_ENABLE = 0;
    /**
     * 打印 DesignPrint 禁用
     */
    public static final int DESIGN_PRINT_STATUS_DISABLE = 1;

    /**
     * 设备 Device
     */
    public static final String TAG_WATER_CONFIG = "04";
    public static final String TAG_WATER_TIME = "05";
    /**
     * 疏通阀门周期
     */
    public static final String TAG_WATER_TAP_PERIOD = "0B";

    /**
     * 在线
     */
    public static final int DEVICE_STATUS_ON_USE = 0;
    /**
     * 弃用
     */
    public static final int DEVICE_STATUS_NO_USE = 1;
    /**
     * 离线
     */
    public static final int DEVICE_STATUS_OFFLINE = 2;
    /**
     * 未激活
     */
    public static final int DEVICE_STATUS_NOACTIVE = 3;
    /**
     * 储存
     */
    public static final int DEVICE_STATUS_STORAGE = 4;
    /**
     * 已报废
     */
    public static final int DEVICE_STATUS_SCRAP = 5;

    /**
     * 未开始
     */
    public static final int PRODUCTION_NO_START = 0;
    /**
     * 生产中
     */
    public static final int PRODUCTION_STARTING = 1;
    /**
     * 已结束
     */
    public static final int PRODUCTION_END = 2;

    /**
     * 正常
     */
    public static final int DEVICE_FLAG_OK = 1;
    /**
     * 新增
     */
    public static final int DEVICE_FLAG_NEW = 2;
    /**
     * 修改
     */
    public static final int DEVICE_FLAG_EDIT = 3;
    /**
     * 删除
     */
    public static final int DEVICE_FLAG_DELETE = 4;


    /**
     * 阀门 开
     */
    public static final int DEVICE_TAP_STATUS_ON = 1;
    /**
     * 阀门 关
     */
    public static final int DEVICE_TAP_STATUS_OFF = 0;

    /**
     * 疏通阀门周期设置 一周
     */
    public static final int DEVICE_TAP_PERIOD_WEEK = 1;
    public static final int DEVICE_TAP_PERIOD_MONTH = 2;// 一个月
    public static final int DEVICE_TAP_PERIOD_QUARTER = 3;// 一个季度
    public static final int DEVICE_TAP_PERIOD_NO = 4;// 无周期
    public static final int DEVICE_TAP_PERIOD_CANCEL = 5;// 取消周期设置

    // 阀门类型
    public static final int DEVICE_TAP_TYPE_WITH_TAP = 1;// 带阀
    public static final int DEVICE_TAP_TYPE_NO_TAP = 0;// 不带阀

    // 电池状态
    public static final int DEVICE_BATTERY_NORMAL = 0;// 电量正常
    public static final int DEVICE_BATTERY_LOW = 1;// 低电量

    // 水表通讯方式 1LORA 2移动NB 3电信NB 4全网通 5联通NB
    public static final int DEVICE_MODE_LORA = 1;
    public static final int DEVICE_MODE_YDNB = 2;
    public static final int DEVICE_MODE_DXNB = 3;
    public static final int DEVICE_MODE_QWT = 4;
    public static final int DEVICE_MODE_LTNB = 10;
    public static final int DEVICE_MODE_MBUS = 6;

    /**
     * 设备 DeviceRecord
     */
    public static final int DEVICE_RECORD_FLAG_OK = 1;
    public static final int DEVICE_RECORD_FLAG_NEW = 2;

    public static final int OWE_STATUS_FREEZE = 1;
    public static final int OWE_STATUS_NOT_FREEZE = 0;

    /**
     * 设备 DeviceWarning
     */
    public static final int DEVICE_WARNING_HANDLE_STATUS_OK = 1;
    public static final int DEVICE_WARNING_HANDLE_STATUS_UNTREATED = 0;

    /**
     * 账单 Order
     */
    // public static final String Sms_PayOrder = "JFTZ";//缴费通知
    // public static final String Sms_MakeOrder = "SCZDTZ";//生成账单通知
    public static final String ORDER_WEIXIN_PAYORDER = "4";// 缴费通知
    public static final String ORDER_WEIXIN_MAKEORDER = "6";// 生成账单通知

    public static final int ORDER_TRADESTATUS_NORMAL = 1;// 账单状态正常
    public static final int ORDER_TRADESTATUS_ABNORMAL = 0;// 账单状态异常

    public static final int ORDER_PAYSTATUS_PAID = 1;// 已支付
    public static final int ORDER_PAYSTATUS_NOTPAID = 0;// 未支付
    public static final int ORDER_PAYSTATUS_PAYING = 2; // 支付中
    public static final int ORDER_PAYSTATUS_PAYFAIL = 4; // 支付失败

    public static final int ORDER_PAYTYPE_MONEY = 1;// 现金
    public static final int ORDER_PAYTYPE_WEIXIN = 2;// 微信
    public static final int ORDER_PAYTYPE_ALIPAY = 3;// 支付宝
    public static final int ORDER_PAYTYPE_PAYCARD = 4; // 微信刷卡支付
    public static final int ORDER_PAYTYPE_QRCODE = 5; // 微信二维码支付

    public static final int ORDER_PRINT_YES = 1;// 已打印
    public static final int ORDER_PRINT_NO = 0;// 没打印

    public static final int ORDER_ISSUB_YES = 1;// 是子账单
    public static final int ORDER_ISSUB_NO = 0;// 不是子账单

    /**
     * OwnerRecord
     */
    public static final int OWNER_RECORD_TYPE_CANCEL = 1;
    public static final int OWNER_RECORD_TYPE_CHANGE_OWNER = 2;
    public static final int OWNER_RECORD_TYPE_CHANGE_DEV = 3;
    public static final int ONWER_RECORD_TYPE_NO_OPEN_UPDATE = 4;
    public static final int OWNER_RECORD_TYPE_OWNERNAME_CHANGE = 5;

    /**
     * PayDetail
     */
    public static final int PAY_DETAIL_TYPE_IN = 1;// 入账(充值)
    public static final int PAY_DETAIL_TYPE_OUT = 0;// 出账(抵扣)

    /**
     * 库存管理 Storage
     */
    public static final int STORAGE_STATUS_IN = 2; // 已入库
    public static final int STORAGE_STATUS_SAVE = 1; // 已保存
    public static final int STORAGE_STATUS_delete = -1; // 已删除

    /**
     * 库存产品 StoreProduct
     */
    public static final int STORE_PRODUCT_STATUS_DELETE = -1;// 删除
    public static final int STORE_PRODUCT_STATUS_DOWN = 0;// 下线
    public static final int STORE_PRODUCT_STATUS_SAVE = 1;// 保存
    public static final int STORE_PRODUCT_STATUS_UP = 2;// 上线

    /**
     * 交易 TradePay
     */
    // 支付成功
    public static final int TRADE_PAYSTATUS_SUCCESS = 1;
    // 支付失败
    public static final int TRADE_PAYSTATUS_ERROR = 2;
    // 支付中
    public static final int TRADE_PAYSTATUS_IN = 0;

    /**
     * 错账 WrongAccount
     */
    // 已撤销
    public static final int WRONG_ACCOUNT_STATUS_CHEXIAO = -1;
    // 已申请
    public static final int WRONG_ACCOUNT_STATUS_SHENQING = 0;
    // 处理中
    public static final int WRONG_ACCOUNT_STATUS_CHULI = 1;
    // 已解决
    public static final int WRONG_ACCOUNT_STATUS_JIEJUE = 2;

    // 电话
    public static final int WRONG_ACCOUNT_TYPE_DIANHUA = 1;
    // 微信
    public static final int WRONG_ACCOUNT_TYPE_WEIXIN = 2;

    /**
     * FreezeRecord
     */
    public static final int FREEZE_TYPE_MONTH = 1;// 按月冻结

    public static final int FREEZE_TYPE_YEAR = 2;// 按年冻结

    /**
     * 工作中心-业务系统类型
     */
    // 测试系统
    public static final String WORK_CENTER_BUSINESS_TYPE_TEST = "TEST";
    // 营收系统-账单审核
    public static final String WORK_CENTER_BUSINESS_TYPE_REVENUE_ORDER_AUDITING = "REVENUE_ORDER_AUDITING";
    // 设备管理
    public static final String WORK_CENTER_BUSINESS_TYPE_DEVICE = "DEVICE";
    // 设备管理水表报警
    public static final String WORK_CENTER_BUSINESS_TYPE_DEVICE_ALARM = "DEVICE_ALARM";
    // 库存管理-入库
    public static final String WORK_CENTER_BUSINESS_TYPE_STORAGE = "STORAGE";
    // 库存管理-出库
    public static final String WORK_CENTER_BUSINESS_TYPE_STORAGE_OUT = "STORAGE_OUT";
    // 财务系统
    public static final String WORK_CENTER_BUSINESS_TYPE_FINANCIAL = "FINANCIAL";
    // 运维系统
    public static final String WORK_CENTER_BUSINESS_TYPE_OPERATION = "OPERATION";
    // 管漏系统
    public static final String WORK_CENTER_BUSINESS_TYPE_PIPE = "PIPE";
    
    public static final String WORK_CENTER_BUSINESS_TYPE_OA = "OA";
    
    /**
     * 工作中心-节点
     */
    public static final String WORK_CENTER_NODE_PARENT = "0";
    public static final String WORK_CENTER_NODE_SORT = "0";
    public static final String WORK_CENTER_NODE_NAME_APPLY = "申请";
    public static final String WORK_CENTER_NODE_TYPE_APPLY = "APPLY";
    public static final String WORK_CENTER_NODE_NAME_FIRST = "开始";
    public static final String WORK_CENTER_NODE_TYPE_FIRST = "FIRST";
    public static final String WORK_CENTER_NODE_NAME_END = "结束";
    public static final String WORK_CENTER_NODE_TYPE_END = "END";
    public static final String WORK_CENTER_NODE_TYPE_MIDDLE = "MIDDLE";


    /**
     * 流程图节点形状
     */
    public static final String WORK_CENTER_NODE_POINT_SHAPE = "flow-circle";

    /**
     * 流程图节点尺寸
     */
    public static final String WORK_CENTER_NODE_POINT_SIZE = "72*72";

    /**
     * 流程图节点类型
     */
    public static final String WORK_CENTER_NODE_POINT_TYPE = "node";

    /**
     * 流程图开始节点颜色
     */
    public static final String WORK_CENTER_NODE_POINT_START_COLOR = "#20B360";

    /**
     * 流程图结束节点颜色
     */
    public static final String WORK_CENTER_NODE_POINT_END_COLOR = "#EE6A6A";


    /**
     * 工作中心-流程状态
     */
    // 未申请
    public static final String WORK_CENTER_PROCESS_NO_APPLY = "NOAPPLY";
    // 已申请
    public static final String WORK_CENTER_PROCESS_APPLY = "APPLY";
    // 处理中
    public static final String WORK_CENTER_PROCESS_HANDLE = "HANDLE";
    // 已结束
    public static final String WORK_CENTER_PROCESS_OVER = "OVER";
    // 已挂起
    public static final String WORK_CENTER_PROCESS_HANG = "HANG";
    // 已退回
    public static final String WORK_CENTER_PROCESS_RETURN = "RETURN";
    // 已关闭
    public static final String WORK_CENTER_PROCESS_CLOSE = "CLOSE";
    // 已归档
    public static final String WORK_CENTER_PROCESS_ARCHIVE = "ARCHIVE";

    // 用于标记审批通过的结束 lsc
    public static final String WORK_CENTER_PROCESS_SUCCESS_OVER = "SUCCESSOVER";
    // 用于标记审批不通过的结束 lsc
    public static final String WORK_CENTER_PROCESS_ERROR_OVER = "ERROROVER";

    /**
     * 工作中心-数据源
     */
    // 数据源类型自动加载
    public static final String WORK_CENTER_DB_LOAD_TYPE_AUTO = "AUTO";
    // 数据源类型外部加载
    public static final String WORK_CENTER_DB_LOAD_TYPE_EXTERNAL = "EXTERNAL";
    // 数据源加载
    public static final String WORK_CENTER_DB_LOAD_STATUS_LOAD = "LOAD";
    // 数据源不加载
    public static final String WORK_CENTER_DB_LOAD_STATUS_NO_LOAD = "NO_LOAD";
    /**
     * 工作中心-数据源字段
     */
    public static final String WORK_CENTER_DB_FIELD_TYPE_COMMON = "COMMON";
    public static final String WORK_CENTER_DB_FIELD_TYPE_RELATION = "RELATION";
    /**
     * 工作中心-模板节点
     */
    // 使用
    public static final String WORK_CENTER_NODE_USE = "USE";
    // 不使用
    public static final String WORK_CENTER_NODE_NO_USE = "NO_USE";
    /**
     * 工作中心-模板、表单、表单字段是否有效
     */
    // 使用
    public static final String WORK_CENTER_EFFECT = "EFFECT";
    // 不使用
    public static final String WORK_CENTER_NO_EFFECT = "NO_EFFECT";

    /**
     * 否定、肯定
     */
    public static final String DICTIONARY_0 = "0";
    public static final String DICTIONARY_1 = "1";

    /**
     * 坏账账单标记 坏账1 不是还账 0
     */
    public static final String BILL_BAD_IS_BAD = "1";
    public static final String BILL_BAD_ISNOT_BAD = "0";

    public static final int WECHAT_PAY_AMOUNT = 0;
    public static final int ALREADY_PAY_AMOUNT = 1;

    // 电话
    public static final int TYPE_DIANHUA = 1;
    // 微信
    public static final int TYPE_WEIXIN = 2;

    // ** 可正常支付 **//*
    public static final int CAN_NORMAL_PAY = 2;
    // ** 已经完成支付 **//*
    public static final int HAD_PAID = 5;
    // ** 生成支付请求异常 **//*
    public static final int EXCEPTION = -1;

    /**
     * 微信模板中使用
     */
    public static final int WECHAT_TEMPLATE_STATUS_ENABLE = 0;
    public static final int WECHAT_TEMPLATE_STATUS_DISABLE = 1;
    /**
     * 默认用户
     */
    public static final int WECHAT_OWNER_IS_DEFAULT = 1;
    /**
     * 微信非默认用户
     */
    public static final int WECHAT_OWNER_IS_NOT_DEFAULT = 0;

    public static final String WECHAT_UNIFIEDORDERURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String WECHAT_ORDERQUERYURL = "https://api.mch.weixin.qq.com/pay/orderquery";
    public static final String WECHAT_GATEWAYAUTHORIZECODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state=jiutong#wechat_redirect";
    public static final String WECHAT_GATEWAYOPENIDBYCODE = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
    public static final int PAGECOUNT = 5;

    public static final int PRINT_TEMPLATE_STATUS_ENABLE = 0;
    public static final int PRINT_TEMPLATE_STATUS_DISABLE = 1;

    /**
     * 抄表任务完成状态：0未开始
     */
    public static final int METER_NOT_STARTED = 0;
    /**
     * 抄表任务完成状态：1抄表中
     */
    public static final int METER_READING = 1;
    /**
     * 抄表任务完成状态：2已完成
     */
    public static final int METER_FINISHED = 2;
    /**
     * 抄表任务完成状态：3已结束
     */
    public static final int METER_CLOSED = 3;

    /**
     * 编号规则id 微信充值编号
     */
    public static final int NUM_RULE_CHARGE = 21;
    public static final int NUM_RULE_PAY = 22;

    /**
     * 预警异常类型
     */
    public static final Map<Integer, String> WARNING_TYPE_MAP = new HashMap<>();

    public static final Map<String, Integer> WARNING_TYPE_VALUE_MAP = new HashMap<>();

    static {
        WARNING_TYPE_MAP.put(1, "开到位异常");
        WARNING_TYPE_MAP.put(2, "关到位异常");
        WARNING_TYPE_MAP.put(3, "阀电流异常");
        WARNING_TYPE_MAP.put(4, "电量异常");
        WARNING_TYPE_MAP.put(5, "磁暴攻击");

        WARNING_TYPE_VALUE_MAP.put("开到位异常", 1);
        WARNING_TYPE_VALUE_MAP.put("关到位异常", 2);
        WARNING_TYPE_VALUE_MAP.put("阀电流异常", 3);
        WARNING_TYPE_VALUE_MAP.put("电量异常", 4);
        WARNING_TYPE_VALUE_MAP.put("磁暴攻击", 5);
    }

    public static final long USER_LORA_CACHE_TIMEOUT = 60 * 60 * 24 * 30;

    /**
     * 登陆超过7天自动失效
     */
    public static final long LOGIN_TIME = 24 * 60 * 60 * 1000 * 7;


    /**
     * 自动关阀标识
     */
    public static final String WATER_CLOSE_FLAG = "water:close:flag:";


}
