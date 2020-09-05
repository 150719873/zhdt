package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.water.bo.CompriseBo;
import com.dotop.smartwater.project.module.core.water.bo.LadderBo;
import com.dotop.smartwater.project.module.core.water.bo.PayTypeBo;
import com.dotop.smartwater.project.module.core.water.vo.CompriseVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceModelVo;
import com.dotop.smartwater.project.module.core.water.vo.LadderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;

/**

 */
public interface IPayTypeService extends BaseService<PayTypeBo, PayTypeVo> {

	LadderVo getMaxLadder(LadderBo ladderBo);

	List<PayTypeVo> noPagingFind(PayTypeBo payTypeBo);

	Pagination<PayTypeVo> find(PayTypeBo payTypeBo);

	void updatePayType(PayTypeBo payTypeBo);

	PayTypeVo addPayType(PayTypeBo payTypeBo);
	
	PayTypeVo getPayType(PayTypeBo payTypeBo);
	
	PayTypeVo savePayType(PayTypeBo payTypeBo);
	
	void editPayType(PayTypeBo payTypeBo);
	
	boolean checkNameIsExist(PayTypeBo payTypeBo);

	void updatePayTypeComprise(CompriseBo compriseBo);

	CompriseVo addPayTypeComprise(CompriseBo compriseBo);

	void saveLadder(LadderBo ladderBo);

	void editLadder(LadderBo ladderBo);

	List<CompriseVo> findComprise(CompriseBo compriseBo);

	List<LadderVo> findLadders(LadderBo ladderBo);

	int checkPayTypeIsUse(PayTypeBo payTypeBo);

	void delete(PayTypeBo payTypeBo);

	void deleteComprise(CompriseBo compriseBo);

	void deleteLadder(LadderBo ladderBo);

	PayTypeVo get(String payTypeId);

	List<LadderVo> getLadders(String typeId);

	Map<String, PayTypeVo> getPayTypeMap(String enterpriseid);

	Map<String, DeviceModelVo> getDeviceModelMap(String enterpriseid);

	@Deprecated
	Map<String, PurposeVo> getPurposeMap(String enterpriseid);

	Map<String, AreaNodeVo> getAreasForSchedule(String eId) throws FrameworkRuntimeException;
}
