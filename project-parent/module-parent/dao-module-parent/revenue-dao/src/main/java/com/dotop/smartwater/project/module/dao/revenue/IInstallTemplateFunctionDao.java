package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallFunctionDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallFunctionVo;

import java.util.List;

public interface IInstallTemplateFunctionDao extends BaseDao<InstallFunctionDto, InstallFunctionVo> {

	List<InstallFunctionVo> getFuncs();

}
