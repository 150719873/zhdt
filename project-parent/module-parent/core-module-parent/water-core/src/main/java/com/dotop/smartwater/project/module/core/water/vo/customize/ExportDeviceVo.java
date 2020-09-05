package com.dotop.smartwater.project.module.core.water.vo.customize;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: ExportDevice
 * @Description: 水表信息导出辅助类

 * @date 2018年11月27日 下午16:10:36
 * 
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ExportDeviceVo extends BaseVo {

	private String id;

	private String devid;

	private String name;

	private String deveui;

	private String devaddr;

	private String devno;

	private String typeid;

	private Double water;

	private Integer status;
	
	private String statusText;
	// 状态说明
	private String explain;

	private Integer flag;

	private Long rssi;

	private Double lsnr;

	private Double beginvalue;

	private Date accesstime;

	private String accesstimeText;
	
	private String uplinktime;
	
	/**1-已上报 0-未上报*/
	private String uplinkStatus;

	private Integer tapstatus;

	private Integer taptype;

	private Integer battery;
	
	/**是否带阀-解释语言*/
	private String isValve;

	private String version;

	private String communityid;
	
	private String communityName;

	private String period;

	private Integer tapcycle; // 洗阀周期

	private String ownerid;
	
	private String userno;

	private String username;
	
	private String useraddr;

	/**阀门状态*/
	private String tap;

	private String phone;

	private String actdevType;

	private Integer abnormalCurrent;

	private Integer timeSync;

	private Integer activeStatus;

	// 通信方式 1LORA 2移动NB 3电信NB
	private String mode;
	private String modeName;

	// 识别码，供应商为 NB-移动为必填
	private String imsi;

	// 最近抄表时间与当前时间间隔
	private Integer interval;

	private Integer confirmed;
	// 水表用途
	private String keywords;
	// 水表用途
	private String purposename;

	/**
	 * 此参数用于列表查询时根据分配区域读取
	 */
	private List<String> nodeIds;

	// 批次号
	private String batchNumber;
	// 下发绑定参数ID
	private String deviceParId;
	
	// 产品ID
	private String productId;
	// 产品名称
	private String productName;

	private String productNo;
	
	//上级水表号
	private String parentDevNo;

	// 水表种类
	private String kind;

	private String pid;

	private List<DeviceVo> children;

	private String devicestatus;
	private String devicetaptype;
	private String devicemode;
	private String devicetapstatus;

	// 口径
	private String caliber;
	
	/**NFC初始密码*/
	private String nfcComPwd;
	/**NFC通讯密码*/
	private String nfcInitPwd;
	/**NFC-tag*/
	private String nfcTag;

	// 生命状态：0，初始状态、1，贮存状态、2，运行状态、3，报废状态
	private Integer lifeStatus;

	// 基站id
	private String cellId;

	// 小区局域id
	private String pci;

	// 版本
	private String ver;

	// 集中器编号
	private String concentratorCode;
	// 中继器编号
	private String collectorCode;
	
	// 上行数据
	private List<OriginalVo> originals;
	
	//计量单位(字典)
	private String unit;
	//计量单位名称
	private String unitName;
	//传感器类型(字典)
	private String sensorType;
	//传感器类型名称
	private String sensorTypeName;

	private String iccid;
}