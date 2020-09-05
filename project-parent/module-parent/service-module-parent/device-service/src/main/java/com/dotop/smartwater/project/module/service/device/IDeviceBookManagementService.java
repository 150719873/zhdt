package com.dotop.smartwater.project.module.service.device;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBookManagementBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookManagementVo;

import java.util.List;

/**

 */
public interface IDeviceBookManagementService extends BaseService<DeviceBookManagementBo, DeviceBookManagementVo> {
	/**
	 * 判断该表册下是否存在抄表员
	 * @param deviceBookManagementBo
	 * @return
	 */
	Integer judgeIfExistWorker(DeviceBookManagementBo deviceBookManagementBo);
	/**
	 * 判断该表册下是否存在业主
	 * @param deviceBookManagementBo
	 * @return
	 */
	Integer judgeIfExistOwner(DeviceBookManagementBo deviceBookManagementBo);

    List<String> findReadersbyAreas(List<String> list,String enterpriseid);

    List<String> getAreaIdsByUserid(String userid, String enterpriseid);
}
