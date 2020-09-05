/** @author : KangJunRong
 *  @description : 
 *  @date : 2017年12月21日 上午10:08:48
 */
package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.List;

import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IManualFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.ImportFileForm;

/**
 * 批量导入抄表 导入导出
 * 

 * @date 2019年2月27日
 */
@RestController

@RequestMapping("/wechat/ManualController")
public class ManualController extends FoundationController implements BaseController<BaseForm> {
	private final static Logger LOGGER = LoggerFactory.getLogger(ManualController.class);

	@Autowired
	private IManualFactory iManualFactory;

	/**
	 * 批量导入抄表-导入
	 * 
	 * @return
	 */
	@PostMapping(value = "/import", produces = GlobalContext.PRODUCES)
	public String manualImport(@RequestBody ImportFileForm importfile) {
		LOGGER.info(LogMsg.to("msg:", "导入信息开始"));
		String fileName = importfile.getFilename();
		VerificationUtils.string("fileName", fileName);
		List<String> result = iManualFactory.manualImport(importfile);
		LOGGER.info(LogMsg.to("msg:", "导入信息结束"));
		auditLog(OperateTypeEnum.ARTIFICIAL_METER_READING,"批量导入","文件名称",fileName);
		return resp(ResultCode.Success, "SUCCESS", result);
	}

	/**
	 * 批量导入传统表 KJR
	 * 
	 * @return
	 */
	@PostMapping(value = "/deviceImport", produces = GlobalContext.PRODUCES)
	public String deviceImport(@RequestBody ImportFileForm importfile) {
		LOGGER.info(LogMsg.to("msg:", "批量导入传统表开始"));
		String fileName = importfile.getFilename();
		VerificationUtils.string("fileName", fileName);
		List<String> result = iManualFactory.deviceImport(importfile);
		LOGGER.info(LogMsg.to("msg:", "批量导入传统表结束"));
		auditLog(OperateTypeEnum.METER_MANAGEMENT,"批量导入","文件名称",fileName);
		return resp(ResultCode.Success, "SUCCESS", result);
	}
}