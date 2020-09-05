package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.project.module.core.water.model.SortModel;
import com.dotop.smartwater.project.module.core.water.model.SortModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: Device
 * @Description: TODO

 * @date 2018年4月24日 下午2:10:36
 * 
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBo extends BaseBo {

	private String id;

	private String devid;

	private String name;

	private String deveui;

	private String devaddr;

	private String devno;

	private String typeid;

	private Double water;

	private Integer status;
	// 状态说明
	private String explain;

	private Integer flag;

	private Long rssi;

	private Double lsnr;

	private Double beginvalue;

	private Date accesstime;

	private String uplinktime;

	private Integer tapstatus;

	private Integer taptype;

	private Integer battery;

	private String version;

	private String communityid;
	
	private String communityName;

	private String period;

	private Integer tapcycle; // 洗阀周期

	private String userno;

	private String username;

	private String tap;

	private String phone;

	private String actdevType;

	private Integer abnormalCurrent;

	private Integer timeSync;

	private Integer activeStatus;
	// 水表用途
	private String keywords;

	// 通信方式 1LORA 2移动NB 3电信NB
	private String mode;

	// 识别码，供应商为 NB-移动为必填
	private String imsi;

	// 最近抄表时间与当前时间间隔
	private Integer interval;

	private Integer confirmed;

	// 水表用途
	private String purposename;

	/** 此参数用于列表查询时根据分配区域读取 */
	private List<String> nodeIds;

	// 批次号
	private String batchNumber;
	// 下发绑定参数ID
	private String deviceParId;
	// 产品ID
	private String productId;
	// 产品名称
	private String productName;

	// 水表种类
	private String kind;

	private String pid;

	// 口径
	private String caliber;

	/**NFC初始密码*/
	private String nfcComPwd;
	/**NFC通讯密码*/
	private String nfcInitPwd;
	/**NFC-tag*/
	private String nfcTag;
	/**iccid*/
	private String iccid;
	
	// 生命状态：0，初始状态、1，贮存状态、2，运行状态、3，报废状态
	private Integer lifeStatus;

	// 基站id
	private String cellId;

	// 小区局域id
	private String pci;

	//厂家
	private String factory;

	// 版本
	private String ver;
	
	//计量单位(字典)
	private String unit;
	//传感器类型(字典)
	private String sensorType;
	/**绑定方式  生产：product 样品:sample*/
	private String bindType;

	/**生产过程的状态  生产：product 上线:online*/
	private String processStatus;

	/**上报周期  单位分钟*/
	private String reportingCycle;

	/**流量计生产厂家*/
	private String factoryCode;

	/**用户状态*/
	private String userStatus;
	
	/**排序实体类*/
	private List<SortModel> sortList;
}