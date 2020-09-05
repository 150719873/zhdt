package com.dotop.smartwater.project.module.service.device;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceChangeBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.ExportDeviceVo;

import java.util.List;

/**

 * @date 2019/2/25.
 */
public interface IDeviceService extends BaseService<DeviceBo, DeviceVo> {

	DeviceVo findById(String id);

	DeviceVo getDevById(String id);

	Long getDeviceCount(String enterpriseid);
	
	Long traditionCount(String enterpriseid);
	
	Long remoteCount(String enterpriseid);
	
	Long offlineCount(String enterpriseid);
	
	Long offlineDeviceCount(String enterpriseid);
	
	Long noactiveCount(String enterpriseid);
	
	Long storageCount(String enterpriseid);
	
	Long scrapCount(String enterpriseid);
	
	Long unreportedsCount(String enterpriseid);

	int updateBeginValue(String id, String beginvalue);

	DeviceVo findByDevEUI(String devEui);

	DeviceVo getkeyWordDevice(DeviceBo deviceBo);
	
	DeviceVo findByNfcTagDev(DeviceBo deviceBo);

	void update(DeviceBo deviceBo);

	void updateStatus(String devid, Integer status, String explain);

	void updateBatchDeviceStatus(List<DeviceVo> devices);

	List<DeviceVo> findByCommunity(String id);

	DeviceVo findByDevNo(String devno);

	void deleteById(String devid);

	int insertDeviceChangeRecord(DeviceChangeBo bo);
	
	Pagination<DeviceChangeVo> replacePage(DeviceChangeBo bo);
	
	List<DeviceVo> findAll();

	@Override
	DeviceVo add(DeviceBo deviceBo);
	
	int batchAdd(List<DeviceBo> list);

	List<DeviceVo> getDevices(String enterpriseid);

	int unband(DeviceBo deviceBo);

	void updateByPrimaryKeySelective(DeviceBo deviceBo);

	Pagination<DeviceVo> findBypage(DeviceBo deviceBo);

	List<ExportDeviceVo> getExportDeviceList(DeviceBo deviceBo);

	void updateDeviceWaterV2(DeviceBo deviceBo);

	List<DeviceVo> findChildrenById(String devid, String enterpriseid);


	Long getDeviceCountByAreaIds(List<String> areaIds);

    List<DeviceVo> findByOrderPreviewList(List<OrderPreviewVo> orders);
    
    String updateByThird(DeviceBo deviceBo);
}
