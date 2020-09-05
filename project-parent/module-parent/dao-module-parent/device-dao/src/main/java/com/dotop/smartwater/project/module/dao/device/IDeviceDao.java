package com.dotop.smartwater.project.module.dao.device;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceExtDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceModelVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.ExportDeviceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 设备
 *

 * @date 2019/2/25.
 */
public interface IDeviceDao extends BaseDao<DeviceDto, DeviceVo> {

	/**
	 * 根据id查找
	 *
	 * @param id
	 * @return
	 */
	DeviceVo findById(@Param("id") String id);

	/**
	 * 根据id查找
	 *
	 * @param id
	 * @return
	 */
	DeviceVo getDevById(@Param("id") String id);

	Map<String, DeviceModelVo> getDeviceModelMap(@Param("enterpriseid") String enterpriseid);

	/**
	 * @param enterpriseid
	 * @return
	 */
	Long getDeviceCount(@Param("enterpriseid") String enterpriseid);

	/**
	 * 
	 * @param enterpriseid
	 * @return
	 */
	Long traditionCount(@Param("enterpriseid") String enterpriseid);
	
	/**
	 * 
	 * @param enterpriseid
	 * @return
	 */
	Long remoteCount(@Param("enterpriseid") String enterpriseid);

	/**
	 * @param enterpriseid
	 * @return
	 */
	Long offlineDeviceCount(@Param("enterpriseid") String enterpriseid);
	
	/**
	 * 
	 * @param enterpriseid
	 * @return
	 */
	Long offlineCount(@Param("enterpriseid") String enterpriseid);
	
	/**当天未上报数量
	 * @param enterpriseid
	 * @return
	 */
	Long unreportedsCount(@Param("enterpriseid") String enterpriseid);
	
	
	/**未激活数量
	 * @param enterpriseid
	 * @return
	 */
	Long noactiveCount(@Param("enterpriseid") String enterpriseid);
	
	/**已储存设备数量
	 * @param enterpriseid
	 * @return
	 */
	Long storageCount(@Param("enterpriseid") String enterpriseid);
	
	/**已报废设备数量
	 * @param enterpriseid
	 * @return
	 */
	Long scrapCount(@Param("enterpriseid") String enterpriseid);
	
	

	/**
	 * @param id
	 * @param beginvalue
	 * @return
	 */
	int updateBeginValue(@Param("id") String id, @Param("beginvalue") String beginvalue);

	/**
	 * @param devEui
	 * @return
	 */
	DeviceVo findByDevEUI(@Param("devEUI") String devEui);

	List<DeviceVo> getkeyWordDevice(DeviceDto deviceDto);
	
	/**
	 * 根据nfcTageID获取设备信息
	 * @param deviceDto
	 * @return
	 */
	DeviceVo findByNfcTagDev(DeviceDto deviceDto);

	/**
	 * 更新
	 *
	 * @param deviceDto
	 */
	void update(DeviceDto deviceDto);

	/**
	 * 更新状态
	 *
	 * @param devid
	 * @param status
	 * @param explain
	 */
	void updateStatus(@Param("devid") String devid, @Param("status") Integer status, @Param("explain") String explain);

	/**
	 * 批量更新状态
	 *
	 * @param list
	 */
	void updateBatchDeviceStatus(List<DeviceVo> list);

	/**
	 * @param id
	 * @return
	 */
	List<DeviceVo> findByCommunity(@Param("id") String id);

	/**
	 * @param devno
	 * @return
	 */
	DeviceVo findByDevNo(@Param("devno") String devno);

	/**
	 * 根据id删除
	 *
	 * @param devid
	 */
	void deleteById(@Param("devid") String devid);

	/**
	 * 查询所有
	 *
	 * @return
	 */
	List<DeviceVo> findAll();

	/**
	 * 添加
	 *
	 * @param deviceDto
	 */
	@Override
	void add(DeviceDto deviceDto);

	// 添加-附属表
	void addDeviceExt(DeviceExtDto extDto);
	
	/**
	 * 批量导入
	 * @param list
	 * @return
	 */
	int batchAdd(@Param("list") List<DeviceDto> list);
	
	/**
	 * 批量导入附属表
	 * @param list
	 * @return
	 */
	int batchAddExt(@Param("list") List<DeviceExtDto> list);
	
	/**
	 * @param enterpriseid
	 * @return
	 */
	List<DeviceVo> getDevices(@Param("enterpriseid") String enterpriseid);

	/**
	 * @param deviceDto
	 * @return
	 */
	int unband(DeviceDto deviceDto);

	/**
	 * 更新
	 *
	 * @param deviceDto
	 */
	void updateByPrimaryKeySelective(DeviceDto deviceDto);
	
	/**
	 * 更新附属表
	 * @param deviceDto
	 */
	void updateByPrimaryKeyExt(DeviceExtDto deviceDto);

	/**
	 * 业主模块要用 KJR
	 *
	 * @param devNoSet
	 */
	List<DeviceVo> findDevByDevNos(@Param("list") Set<String> devNoSet);

	/**
	 * 分页查询
	 *
	 * @param deviceDto
	 * @return
	 */
	List<DeviceVo> findBypage(DeviceDto deviceDto);

	@Override
	List<DeviceVo> list(DeviceDto deviceDto);

	/**
	 * 设备管理 导出水表信息 execl -- lsc
	 *
	 * @param deviceDto
	 * @return
	 * @
	 */
	List<ExportDeviceVo> getExportDeviceList(DeviceDto deviceDto);

	Long getDeviceCountByAreaIds(@Param("list") List<String> areaIds);

	List<DeviceVo> findByOrderPreviewList(@Param("list") List<OrderPreviewVo> orders);


    void batchUpdateWater(List<DeviceDto> deviceDtoList);

}
