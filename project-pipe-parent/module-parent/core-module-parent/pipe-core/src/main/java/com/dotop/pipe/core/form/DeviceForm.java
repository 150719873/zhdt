package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceForm extends BasePipeForm {

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

	// 版本
	private String version;

	// 产品id
	private String productId;

	// 面积
	private String acreage;
	// 比例尺
	private String scale;
	// 角度
	private String angle;
	// 设备数量
	private String deviceCount;

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

	// 坐标
	private List<PointForm> points;

	// 分割管道id
	private List<String> breakPipeIds;

	// 合并管道
	private List<String> mergePipeIds;

	// 批量添加管道时调用
	private List<DeviceExtForm> deviceExts;

	// 设备关联表 otherId 字段
	private String otherId;
	// 设备关联表 map_id 字段
	private String deviceMapId;

	// 设备绑定状态
	private String bindStatus;

	/**
	 * device的id的集合, 用户爆管查询
	 */
	private List<String> deviceIds;

	private String collectionId;

	// 通信协议
	private String protocol;

}
