package com.dotop.pipe.core.vo.device;

import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceVo extends BasePipeVo {

    public DeviceVo() {
        super();
    }

    public DeviceVo(String deviceId, String code) {
        super();
        this.deviceId = deviceId;
        this.code = code;
    }

    // 主键
    private String deviceId;

    // 编码
    private String code;

    // 名字
    private String name;

    // 描述
    private String des;

    // 地址
    private String address;

    // 长度
    private String length;

    // 平均深度
    private String depth;

    // 管顶高程
    private String pipeElevation;

    // 地面高程
    private String groundElevation;

    // 敷设类型
    private DictionaryVo laying;

    // 安装时间
    private Date installDate;

    // 备注
    private String remark;

    // 版本
    private String version;

    // 占地面积
    private String acreage;

    private String scale;
    private String angle;


    private String deviceCount;

    // 企业id
    private String enterpriseId;

    // 产品
    private ProductVo product;

    // 坐标
    private AreaModelVo area;

    // 区域
    private List<PointVo> points;

    // 设备最新属性集合
    private List<DevicePropertyVo> devicePropertys;
    // 设备最新属性集合 (即一个设备的所有属性)
    private DevicePropertyVo deviceProperty;

    // 设备报警集合
    private List<AlarmVo> alarms;

    // 设备报警集合
    private Boolean isAlarm;
    //
    private boolean online;

    // 设备和设备绑定时的关联id
    private String deviceMapId;

    private List<AlarmSettingVo> alarmSettingVos;

    // 水质预警id
    private String alarmWMSettingId;

    // 收藏id
    private String collectionId;

    // 产品类别
    private String productCategory;
    // 产品类型
    private String productType;

	// 设备上下游关联关系
	private String upDownStreamId;
	// 设备下游设置的预测值
	private String alarmProperties;

    // 通信协议
    private String protocol;
}
