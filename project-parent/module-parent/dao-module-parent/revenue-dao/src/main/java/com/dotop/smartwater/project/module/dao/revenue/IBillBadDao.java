package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.dto.BillBadDto;
import com.dotop.smartwater.project.module.core.water.vo.BillBadVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账单坏账
 *

 * @date 2019年2月23日
 */
public interface IBillBadDao extends BaseDao<BillBadDto, BillBadVo> {

	@Override
	List<BillBadVo> list(BillBadDto billBadDto);

	@Override
	Integer edit(BillBadDto billBadDto);

	@Override
	Boolean isExist(BillBadDto billBadDto);

	/**
	 * 批量新增
	 *
	 * @param list
	 * @return
	 * @
	 */
	Integer addList(@Param("list") List<BillBadDto> list);

	List<BillBadVo> getBillBadList(BillBadDto billBadDto);

	/**
	 * 标记为坏账
	 *
	 * @param billBadDto
	 * @return
	 * @
	 */
	Integer markBadBill(BillBadDto billBadDto);

	/**
	 * 将标记为坏账的账单 在流程审批结束后 更新坏账的流程审批状态
	 *
	 * @param billBadDto
	 * @throws FrameworkRuntimeException
	 */
	void editProcessOver(BillBadDto billBadDto);
}
