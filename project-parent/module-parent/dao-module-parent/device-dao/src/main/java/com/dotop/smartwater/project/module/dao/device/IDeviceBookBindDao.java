package com.dotop.smartwater.project.module.dao.device;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBookBindDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookBindVo;

/**
 * 

 * @description 表册绑定抄表员/业主
 * @date 2019-10-22 16:39
 *
 */
public interface IDeviceBookBindDao extends BaseDao<DeviceBookBindDto, DeviceBookBindVo> {
	
	Integer insertDeviceBookBind(@Param("list") List<DeviceBookBindDto> list);
	
	Integer deleteDeviceBookBind(DeviceBookBindDto deviceBookBindDto);
	
	List<DeviceBookBindVo> listDeviceBookBind(DeviceBookBindDto deviceBookBindDto);
	
	List<DeviceBookBindVo> pageBindOwner(DeviceBookBindDto deviceBookBindDto);
	
	List<DeviceBookBindVo> listBindOwner(DeviceBookBindDto deviceBookBindDto);
	
	Integer deleteBindOwner(DeviceBookBindDto deviceBookBindDto);
	
	Integer deleteBindOwnerOne(DeviceBookBindDto deviceBookBindDto);
	
	Integer insertBindOwner(@Param("list") List<DeviceBookBindDto> deviceBookBindDtos, @Param("bookNum") String bookNum);
}
