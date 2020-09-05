package com.dotop.pipe.core.bo.device;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceBo extends BasePipeBo {

    // Id
    private String deviceId;

    // 编号
    private String code;

    // 名称
    private String name;

    // 描述
    private String des;

    // 地址
    private String address;

    // 长度
    private String length;
    private String acreage;
    // 比例尺
    private String scale;
    // 角度
    private String angle;
    // 设备数量
    private String deviceCount;

    // 平均深度
    private String depth;

    // 管顶高程
    private String pipeElevation;

    // 地面高程
    private String groundElevation;

    // 敷设类型
    private String laying;

    // 安装时间
    private Date installDate;

    // 备注
    private String remark;

    // 产品id
    private String productId;

    // 产品类别
    private String productCategory;
    // 产品类型
    private String productType;

    // 所属企业
    // private String enterpriseId;

    // 区域id
    private String areaId;
    // 区域id集合
    private List<String> areaIds;

    // 坐标ids
    private List<String> pointIds;

    private List<String> deviceIds;
    // 版本

    private String version;

    // 设备关联表 otherId 字段
    private String otherId;
    // 设备关联表 map_id 字段
    private String deviceMapId;

    private String bindStatus;

    private String collectionId;


    // 产品id集合
    private List<String> productIds;

    // 通信协议
    private String protocol;
}
