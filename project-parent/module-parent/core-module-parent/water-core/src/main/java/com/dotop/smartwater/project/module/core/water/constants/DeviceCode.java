package com.dotop.smartwater.project.module.core.water.constants;

/**

 * @date 2019/2/26.
 */
public class DeviceCode {

    /**
     * 使用中
     */
    public static final int DEVICE_STATUS_ON_USE = 0;

    /**
     * 离线
     */
    public static final int DEVICE_STATUS_OFFLINE = 2;

    public static final int DEVICE_FLAG_NEW = 2;
    public static final int DEVICE_FLAG_EDIT = 3;
    public static final int DEVICE_FLAG_DELETE = 4;
    public static final int DEVICE_FLAG_DELETE_TOTALLY = 6;

    /**
     * 阀门 开
     */
    public static final int DEVICE_TAP_STATUS_ON = 1;

    /**
     * 阀门 关
     */
    public static final int DEVICE_TAP_STATUS_OFF = 0;


    /**
     * 阀门类型 带阀
     */
    public static final int DEVICE_TAP_TYPE_WITH_TAP = 1;
    /**
     * 阀门类型 不带阀
     */
    public static final int DEVICE_TAP_TYPE_NO_TAP = 0;

    /**
     * 电池状态 电量正常
     */
    public static final int DEVICE_BATTERY_NORMAL = 0;
    /**
     * 电池状态 低电量
     */
    public static final int DEVICE_BATTERY_LOW = 1;

    /**
     * 水表通讯方式 1LORA 2移动NB 3电信NB 4全网通  5联通NB
     */
    public static final int DEVICE_MODE_LORA = 1;
    public static final int DEVICE_MODE_YDNB = 2;
    public static final int DEVICE_MODE_DXNB = 3;
    public static final int DEVICE_MODE_QWT = 4;
    public static final int DEVICE_MODE_LTNB = 5;

    /**
     * 水表类型 1=传统表 2=电子表
     */
    public static final String DEVICE_MACHINE_EUI = "machine_";
    public static final String DEVICE_FAKE_EUI = "fake_";

    /**
     * 水表类型 1=传统表 2=电子表
     */
    public static final String DEVICE_TYPE_TRADITIONAL = "1";
    public static final String DEVICE_TYPE_ELECTRONIC = "2";

    /**
     * 水表种类 real=实表 fake虚表
     */
    public static final String DEVICE_KIND_REAL = "real";
    public static final String DEVICE_KIND_FAKE = "fake";

    /**
     * 绑定方式 product-生产 sample-样品
     */
    public static final String DEVICE_BIND_PRODUCT = "product";
    public static final String DEVICE_BIND_SAMPLE = "sample";

    /**
     * 过程状态 product-生产 online-上线
     */
    public static final String DEVICE_PROCESS_STATUS_PRODUCT = "product";
    public static final String DEVICE_PROCESS_STATUS_ONLINE = "online";

    /**
     * 总表
     */
    public static final String DEVICE_PARENT = "0";

    /**
     * 订阅异常码
     */
    /**
     * 该水表已订阅，不要重复订阅
     **/
    public static final String DEVICE_SUBSCRIBE = "1001";
    /**
     * 水务系统没有该水表
     **/
    public static final String DEVICE_NOT_EXIST = "1002";
    /**
     * 输入的水司ID和水务系统绑定的不匹配
     **/
    public static final String DEVICE_MISMATCH = "1003";

}
