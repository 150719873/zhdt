package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.dto.AccountingDto;
import com.dotop.smartwater.project.module.core.water.form.customize.SupplementForm;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SupplementVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @date 2019年2月25日
 */
public interface ISupplementAccountDao extends BaseDao<AccountingDto, AccountingVo> {

	void updateAaccountStatus(@Param("id") String id);

	void batchSupplement(@Param("list") List<SupplementVo> supps);


	List<AccountingVo> summarySelfDetail(SupplementForm sp);

	List<SupplementVo> getList(@Param("sp") SupplementForm sp, @Param("user") UserVo user);

}
