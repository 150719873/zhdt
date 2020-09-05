package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.AccountingDto;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SummaryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @date 2019年2月25日
 */
public interface ISummaryAccountDao extends BaseDao<AccountingDto, AccountingVo> {
	SummaryVo summaryData(@Param("date") String date, @Param("enterpriseid") String enterpriseid);

	List<SummaryVo> summaryDetail(@Param("date") String date, @Param("enterpriseid") String enterpriseid);

	List<AccountingVo> summarySelfDetail(@Param("date") String date, @Param("enterpriseid") String enterpriseid,
	                                     @Param("userid") String userid);

	SummaryVo summarySelf(@Param("date") String date, @Param("enterpriseid") String enterpriseid,
	                      @Param("userid") String userid);
}
