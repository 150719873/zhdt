package com.dotop.smartwater.project.server.water.rest.service.tool;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.api.tool.ILogoFactory;
import com.dotop.smartwater.project.module.api.tool.ISettlementFactory;
import com.dotop.smartwater.project.module.api.tool.IUserLoraFactory;
import com.dotop.smartwater.project.module.core.auth.form.EnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.SettlementForm;
import com.dotop.smartwater.project.module.core.auth.form.UserLoraForm;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tool/Setting")
public class SettingController implements BaseController<BaseForm>, IAuthCasClient {

	@Resource
	private IUserLoraFactory iUserLoraFactory;

	@Resource
	private ISettlementFactory iSettlementFactory;

	private static final long MAXSIZE = 2 * 1024 * 1024;

	@Resource
	private ILogoFactory iLogoFactory;

	@PostMapping(value = "/getUserlora", produces = GlobalContext.PRODUCES)
	public String getUserlora(@RequestBody EnterpriseForm etp) {
		UserLoraVo userlora = iUserLoraFactory.findByEnterpriseId(etp.getEnterpriseid());
		return resp(ResultCode.Success, ResultCode.SUCCESS, userlora);

	}
	
	@PostMapping(value = "/getTestIot", produces = GlobalContext.PRODUCES)
	public String getTestIot(@RequestBody EnterpriseForm etp) {
		SettlementVo settlement = iSettlementFactory.getSettlement(etp.getEnterpriseid());
		return resp(ResultCode.Success, ResultCode.SUCCESS, settlement);
	}
	

	@PostMapping(value = "/saveUserlora", produces = GlobalContext.PRODUCES)
	public String saveUserlora(@RequestBody UserLoraForm userlora) {
		iUserLoraFactory.saveUserlora(userlora);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	@PostMapping(value = "/saveTestIot", produces = GlobalContext.PRODUCES)
	public String saveTestIot(@RequestBody SettlementForm settlement) {
		iSettlementFactory.saveTestIot(settlement);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	

	// 获取水司基本配置
	@PostMapping(value = "/getCompanyConfig", produces = GlobalContext.PRODUCES)
	public String getCompanyConfig() {
		SettlementVo settlement = iSettlementFactory.getSettlement(getEnterpriseid());
		return resp(ResultCode.Success, ResultCode.SUCCESS, settlement);
	}

	// 水司基本配置
	@PostMapping(value = "/setCompanyConfig", produces = GlobalContext.PRODUCES)
	public String setCompanyConfig(@RequestBody SettlementForm settlement) {
		settlement.setEnterpriseid(getEnterpriseid());
		/*if(settlement.getIsHelp() == null) {
			settlement.setIsHelp(1);
		}*/
		if (StringUtils.isBlank(settlement.getAlias())) {
			settlement.setAlias("");
		}
		if (settlement.getStatus() == null) {
			settlement.setStatus(0);
		}
		if (settlement.getOffday() == null) {
			settlement.setOffday(0);
		}
		if (settlement.getAlarmday() == null) {
			settlement.setAlarmday(0);
		}
		iSettlementFactory.editSettlement(settlement);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 获取水司Logo
	@PostMapping(value = "/getCompanyLogo", produces = GlobalContext.PRODUCES)
	public String getCompanyLogo() {
		LogoVo logo = iLogoFactory.getLogo();
		return resp(ResultCode.Success, ResultCode.SUCCESS, logo);
	}

	@PostMapping(value = "/uploadCompanyLogo", produces = GlobalContext.PRODUCES)
	public String uploadCompanyLogo(@RequestParam("file") MultipartFile file) {
		if (file.getSize() > MAXSIZE) { // 2M
			return resp(ResultCode.Fail, "上传的文件过大", null);
		}
		String url = iLogoFactory.uploadCompanyLogo(file);
		return resp(ResultCode.Success, ResultCode.SUCCESS, url);
	}

	@PostMapping(value = "/delCompanyLogo", produces = GlobalContext.PRODUCES)
	public String delCompanyLogo() {
		iLogoFactory.delCompanyLogo();
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * @param userid 用户ID
	 * @param ticket 票据
	 * @return 获取系统定义的消息统一码（短信，微信消息，工单，打印等）
	 */
	@PostMapping(value = "/codeType", produces = GlobalContext.PRODUCES)
	public String codeType(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iSettlementFactory.codeTypeList());
	}

}
