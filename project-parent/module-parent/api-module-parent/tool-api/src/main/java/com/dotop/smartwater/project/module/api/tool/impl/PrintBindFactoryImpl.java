package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.tool.IPrintBindFactory;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.water.bo.DesignPrintBo;
import com.dotop.smartwater.project.module.core.water.bo.PrintBindBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DesignPrintForm;
import com.dotop.smartwater.project.module.core.water.form.PrintBindForm;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;
import com.dotop.smartwater.project.module.service.tool.IPrintBindService;

/**
 * 打印绑定
 * 

 * @date 2019年2月23日
 */

@Component
public class PrintBindFactoryImpl implements IPrintBindFactory {

	@Autowired
	private IPrintBindService iPrintBindService;

	@Override
	public Pagination<PrintBindVo> page(PrintBindForm printBindForm)  {
		PrintBindBo printBindBo = new PrintBindBo();
		BeanUtils.copyProperties(printBindForm, printBindBo);
		return iPrintBindService.page(printBindBo);
	}

	@Override
	public List<PrintBindVo> listAll(PrintBindForm printBindForm)  {
		return iPrintBindService.listAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public PrintBindVo add(PrintBindForm printBindForm)  {
		Date curr = new Date();
		// 复制属性
		PrintBindBo printBindBo = new PrintBindBo();
		BeanUtils.copyProperties(printBindForm, printBindBo);
		// 校验是否存在
		Boolean flag = iPrintBindService.isExist(printBindBo);
		if (flag) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该类型已经存在在该水司下,请不要重复绑定");
		} else {
			printBindBo.setSmstypename(SmsEnum.getText(printBindBo.getSmstype()));
			printBindBo.setCurr(curr);
			iPrintBindService.add(printBindBo);
		}
		return null;
	}

	@Override
	public DesignPrintVo get(DesignPrintForm designPrintForm)  {
		DesignPrintBo designPrintBo = new DesignPrintBo();
		designPrintBo.setId(designPrintForm.getId());
		return iPrintBindService.get(designPrintBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(PrintBindForm printBindForm)  {
		String id = printBindForm.getId();
		PrintBindBo printBindBo = new PrintBindBo();
		printBindBo.setId(id);
		iPrintBindService.del(printBindBo);
		return id;
	}

	@Override
	public DesignPrintVo getRelationDesign(DesignPrintForm designPrintForm)  {
		DesignPrintBo designPrintBo = new DesignPrintBo();
		designPrintBo.setId(designPrintForm.getId());
		return iPrintBindService.getRelationDesign(designPrintBo);
	}

	@Override
	public PrintBindVo getPrintStatus(String enterpriseid, Integer intValue) {
		return iPrintBindService.getPrintStatus(enterpriseid, intValue);
	}

}
