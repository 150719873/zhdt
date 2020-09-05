package com.dotop.smartwater.project.module.api.device;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.SettlementForm;
import com.dotop.smartwater.project.module.core.water.bo.customize.DownLinkDataBo;
import com.dotop.smartwater.project.module.core.water.form.DeviceDownlinkForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.customize.OperationForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;

/**
 * 设备接口
 *

 * @date 2019/2/25.
 */
public interface IDeviceFactory extends BaseFactory<DeviceForm, DeviceVo> {

	/**
	 * 根据devno查找
	 *
	 * @param devid 水表号
	 * @return 设备对象
	 */
	DeviceVo findByDevId(String devid);

	/**
	 * 第三方开关阀
	 * @param operationForm 参数对象
	 * @return 下发命令Id
	 */
	String thirdOpenOrClose(OperationForm operationForm);

	/**
	 * 开关阀
	 *
	 * @param command    TxCode
	 * @param deviceForm 设备对象
	 */
	void openOrClose(DeviceForm deviceForm, int command);

	/**
	 * 实时抄表
	 *
	 * @param deviceForm 设备对象
	 */
	void getDeviceWater(DeviceForm deviceForm);

	/**
	 * 水表信息查询
	 *
	 * @param deviceForm 设备对象
	 * @return 设备对象
	 */
	DeviceVo getDeviceInfo(DeviceForm deviceForm);

	/**
	 * 关键字查询
	 *
	 * @param deviceForm 设备对象
	 * @return 对象
	 */
	DeviceVo getkeyWordDevice(DeviceForm deviceForm);
	
	/**
	 * 根据nfcTagId查询设备信息
	 * @param deviceForm
	 * @return
	 */
	DeviceVo findByNfcTagDev(DeviceForm deviceForm);

	/**
	 * 校准上行历史查询
	 *
	 * @param params 参数
	 * @return 分页
	 */
	Pagination<OriginalVo> getUpCorrectionDatas(Map<String, String> params);

	/**
	 * 校准下行历史查询
	 *
	 * @param params 参数
	 * @return 分页
	 */
	Pagination<DeviceUplinkVo> getDownCorrectionDatas(Map<String, String> params);

	/**
	 * 校准下发
	 *
	 * @param params 参数
	 */
	void downCorrectionOper(DeviceDownlinkForm params);

	/**
	 * 复位下发
	 *
	 * @param params 参数
	 */
	void downRest(DeviceDownlinkForm params);


	/**
	 * 获取设备数
	 * @param areaIds 区域ID
	 * @return 设备数
	 */
	Long getDeviceCountByAreaIds(List<String> areaIds);

	/**
	 * 执行命令
	 *
	 * @param deviceForm 设备对象
	 * @param command 命令
	 * @param value 值
	 * @param expire 过期时间
	 * @param downLinkDataBo 下发对象
	 * @return 结果Map
	 */
	Map<Boolean, String> txNew(DeviceForm deviceForm, int command, String value, Integer expire,
	                           DownLinkDataBo downLinkDataBo);

	/**
	 * 修改设备
	 * @param deviceForm 设备对象
	 */
	void update(DeviceForm deviceForm);

	/**
	 * 根据devno查找
	 *
	 * @param devno 水表号
	 * @return 设备对象
	 */
	DeviceVo findByDevNo(String devno);

	/**
	 * @param deviceForm 对象参数
	 * @return 设备对象
	 */
	@Override
	DeviceVo add(DeviceForm deviceForm);

	/**
	 * 页面添加设备
	 * @param deviceForm 设备对象
	 */
	void addDeviceByWeb(DeviceForm deviceForm);

	/**
	 * 离线检查
	 * @param settlementForm 参数对象
	 */
	void offLineCheck(SettlementForm settlementForm);

	/**
	 *
	 * 重置生命周期
	 * @param params 下行对象
	 */
	void setLifeStatus(DeviceDownlinkForm params);

	/**
	 * 重置周期
	 * @param params 下行对象
	 */
	void resetPeriod(DeviceDownlinkForm params);
	
	/**
	 * 第三方添加设备
	 * @param deviceForm 设备对象
	 * @return
	 */
	Integer addDeviceByThird(List<DeviceForm> deviceForms);
	
	/**
	 * 第三方修改设备
	 * @param deviceForm 设备对象
	 * @return 设备ID
	 */
	List<String> editDeviceByThird(List<DeviceForm> deviceForms);
	
	/**
	 * 第三方查询设备
	 * @param deviceForm
	 * @return
	 */
	Pagination<DeviceVo> pageByThird(DeviceForm deviceForm);
	
	/**
	 * 第三方平台删除设备
	 * @param deviceForm
	 * @return devid
	 */
	String deleteByThird(DeviceForm deviceForm);

	/**
	 * 第三方平台上行用水量
	 * @param deviceUplinkForm
	 * @return
	 */
	DeviceUplinkVo uplinkByThird(DeviceUplinkForm deviceUplinkForm);

    String verificationData(DeviceVo deviceVo, Integer command);

	DeviceVo findByDevEui(String deveui);
}
