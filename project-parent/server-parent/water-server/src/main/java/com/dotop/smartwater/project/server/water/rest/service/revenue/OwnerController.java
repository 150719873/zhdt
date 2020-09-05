package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IOwnerFactory;
import com.dotop.smartwater.project.module.api.tool.IPrintBindFactory;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.OutStorageForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.vo.OwnerRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
 * 业主管理
 *

 * @date 2018-11-30 下午 15:40
 */
@RestController

@RequestMapping("/owner")
public class OwnerController extends FoundationController implements BaseController<OutStorageForm> {

	@Autowired
	private IOwnerFactory iOwnerFactory;
	@Autowired
	private IPrintBindFactory iPrintBindFactory;

	public static final String OWNERID = "ownerId";

	@PostMapping(value = "/add_owner", produces = GlobalContext.PRODUCES)
	public String addOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String communityId = ownerForm.getCommunityid();
		VerificationUtils.string("communityId", communityId);

		String userName = ownerForm.getUsername();
		VerificationUtils.string("userName", userName);

		String userNo = ownerForm.getUserno();
		VerificationUtils.string("userNo", userNo);

		String userAddress = ownerForm.getUseraddr();
		VerificationUtils.string("userAddress", userAddress);

		// 数据封装
		iOwnerFactory.add(ownerForm);
		auditLog(OperateTypeEnum.USER_FILE,"新增","业主编号",userNo);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	
	/**
     * 根据关键字查询业主信息
     *
     * @param deviceForm
     * @return
     */
    @PostMapping(value = "/getOwnerKeywords", produces = GlobalContext.PRODUCES)
    public String getOwnerKeywords(@RequestBody OwnerForm form) {
        // 校验
        VerificationUtils.string("keywords", form.getKeywords());
        OwnerVo vo = iOwnerFactory.getkeyWordOwner(form);
        return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
    }
	
	//直接开户
	@PostMapping(value = "/open_owner", produces = GlobalContext.PRODUCES)
	public String openOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		String devNo = ownerForm.getDevno();
		VerificationUtils.string("devNo", devNo);

		String payTypeId = ownerForm.getPaytypeid();
		VerificationUtils.string("payTypeId", payTypeId);

		String installMonth = ownerForm.getInstallmonth();
		VerificationUtils.string("installMonth", installMonth);
		//不更新底数
		ownerForm.setIsUpdate("0");
		// 数据封装
		iOwnerFactory.openOwner(ownerForm);
		PrintBindVo printBindVo = iPrintBindFactory.getPrintStatus(AuthCasClient.getEnterpriseid(),
				SmsEnum.owner_open.intValue());
		return resp(ResultCode.Success, ResultCode.SUCCESS, printBindVo);
	}
	
	//更新底数并开户
	@PostMapping(value = "/update_and_open_owner", produces = GlobalContext.PRODUCES)
	public String updateAndOpenOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		String devNo = ownerForm.getDevno();
		VerificationUtils.string("devNo", devNo);

		String payTypeId = ownerForm.getPaytypeid();
		VerificationUtils.string("payTypeId", payTypeId);

		String installMonth = ownerForm.getInstallmonth();
		VerificationUtils.string("installMonth", installMonth);
		//更新底数
		ownerForm.setIsUpdate("1");
		// 数据封装
		iOwnerFactory.openOwner(ownerForm);
		PrintBindVo printBindVo = iPrintBindFactory.getPrintStatus(AuthCasClient.getEnterpriseid(),
				SmsEnum.owner_open.intValue());
		return resp(ResultCode.Success, ResultCode.SUCCESS, printBindVo);
	}
	

	@PostMapping(value = "/update_owner", produces = GlobalContext.PRODUCES)
	public String updateOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		// 数据封装
		iOwnerFactory.updateOwner(ownerForm);
		auditLog(OperateTypeEnum.USER_FILE,"编辑","业主ID",ownerId);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@PostMapping(value = "/del_owner", produces = GlobalContext.PRODUCES)
	public String delOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		// 数据封装
		iOwnerFactory.delOwner(ownerForm);
		auditLog(OperateTypeEnum.USER_FILE,"删除","业主ID",ownerId);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@PostMapping(value = "/checkopen", produces = GlobalContext.PRODUCES)
	public String checkOpen(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		// 数据封装
		iOwnerFactory.checkOpen(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	/**
	 * 业主销户
	 * 
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/cancel_owner", produces = GlobalContext.PRODUCES)
	public String cancelOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		// 数据封装
		iOwnerFactory.cancelOwner(ownerForm);
		PrintBindVo printBindVo = iPrintBindFactory.getPrintStatus(AuthCasClient.getEnterpriseid(),
				SmsEnum.owner_close.intValue());
		auditLog(OperateTypeEnum.USER_FILE,"销户","业主ID",ownerId);
		return resp(ResultCode.Success, ResultCode.SUCCESS, printBindVo);
	}

	/**
	 * 换水表
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/change_device", produces = GlobalContext.PRODUCES)
	public String changeDevice(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		String newDevNo = ownerForm.getNewdevno();
		VerificationUtils.string("newDevNo", newDevNo);

		String reason = ownerForm.getReason();
		VerificationUtils.string("reason", reason);

		// 数据封装
		iOwnerFactory.changeDevice(ownerForm);

		PrintBindVo printBindVo = iPrintBindFactory.getPrintStatus(AuthCasClient.getEnterpriseid(),
				SmsEnum.owner_change_meter.intValue());
		auditLog(OperateTypeEnum.USER_FILE,"换表","业主ID",ownerId,"新设备编号",newDevNo);
		return resp(ResultCode.Success, ResultCode.SUCCESS, printBindVo);
	}

	/**
	 * 过户
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/change_owner", produces = GlobalContext.PRODUCES)
	public String changeOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		String payTypeId = ownerForm.getTypeid();
		VerificationUtils.string("payTypeId", payTypeId);

		/*String purposeId = ownerForm.getPurposeid();
		VerificationUtils.string("purposeId", purposeId);*/

		String reason = ownerForm.getReason();
		VerificationUtils.string("reason", reason);

		String newUserNo = ownerForm.getNewuserno();
		VerificationUtils.string("newUserNo", newUserNo);

		// 数据封装
		iOwnerFactory.changeOwner(ownerForm);

		PrintBindVo printBindVo = iPrintBindFactory.getPrintStatus(AuthCasClient.getEnterpriseid(),
				SmsEnum.owner_transfer_ownership.intValue());
		auditLog(OperateTypeEnum.USER_FILE,"过户","新业主编号",newUserNo);
		return resp(ResultCode.Success, ResultCode.SUCCESS, printBindVo);
	}

	/**
	 * 获取业主信息（仅提供给水务后台App使用）
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/getOwners", produces = GlobalContext.PRODUCES)
	public String getOwners(@RequestBody OwnerForm ownerForm) {
		Pagination<OwnerVo> pagination = iOwnerFactory.getOwners(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 获取业主列表
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/getOwnerList", produces = GlobalContext.PRODUCES)
	public String getOwnerList(@RequestBody OwnerForm ownerForm) {
		Pagination<OwnerVo> pagination = iOwnerFactory.getOwnerList(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 加载单个业主
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/get_detail_owner", produces = GlobalContext.PRODUCES)
	public String getDetailOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		OwnerVo ownerVo = iOwnerFactory.getDetailOwner(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, ownerVo);
	}

	/**
	 * 加载单个对象
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/get_owner", produces = GlobalContext.PRODUCES)
	public String getOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		// 数据封装
		OwnerVo ownerVo = iOwnerFactory.getOwner(ownerForm);

		return resp(ResultCode.Success, ResultCode.SUCCESS, ownerVo);
	}

	@PostMapping(value = "/getUserNoOwner", produces = GlobalContext.PRODUCES)
	public String getUserNoOwner(@RequestBody OwnerForm ownerForm) {
		// 校验
		String userNo = ownerForm.getUserno();
		VerificationUtils.string("userNo", userNo);

		// 数据封装
		OwnerVo ownerVo = iOwnerFactory.getUserNoOwner(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, ownerVo);
	}

	/**
	 * 删除整个小区业主
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/del_owner_community", produces = GlobalContext.PRODUCES)
	public String delOwnerCommunity(@RequestBody OwnerForm ownerForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 生成最新账单
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/genNewOrder", produces = GlobalContext.PRODUCES)
	public String genNewOrder(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		// 数据封装
		iOwnerFactory.genNewOrder(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 业主账户变更操作查询
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/getRecordList", produces = GlobalContext.PRODUCES)
	public String getRecordList(@RequestBody OwnerForm ownerForm) {
		// 校验
		String ownerId = ownerForm.getOwnerid();
		VerificationUtils.string(OWNERID, ownerId);

		// 数据封装
		Pagination<OwnerRecordVo> pagination = iOwnerFactory.getRecordList(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 批量修改业主信息(收费种类、用途、型号等)
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/batchUpdateOwner", produces = GlobalContext.PRODUCES)
	public String batchUpdateOwner(@RequestBody OwnerForm ownerForm) {
		// 校验

		List<String> nodeIds = ownerForm.getNodeIds();
		VerificationUtils.strList("nodeIds", nodeIds);

		// 数据封装
		iOwnerFactory.batchUpdateOwner(ownerForm);
		auditLog(OperateTypeEnum.USER_FILE,"批量修改","区域IDS",nodeIds);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 营业厅-检测是否有查询动作是否正常或是否有查询权限
	 *
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "/getcheckSearch", produces = GlobalContext.PRODUCES)
	public String getCheckSearch(@RequestBody OwnerForm ownerForm) {
		// 数据封装
		iOwnerFactory.getCheckSearch(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 营业厅模糊查询出多个业主供用户点击选择
	 * @param ownerForm
	 * @return
	 */
	@PostMapping(value = "findBusinessHallOwners", produces = GlobalContext.PRODUCES)
	public String findBusinessHallOwners(@RequestBody OwnerForm ownerForm) {
		// 数据封装
		VerificationUtils.string("keywords", ownerForm.getKeywords());

		List<OwnerVo> list = iOwnerFactory.findBusinessHallOwners(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}
}
