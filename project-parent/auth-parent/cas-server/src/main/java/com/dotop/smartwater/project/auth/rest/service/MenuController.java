package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.auth.api.IMenuFactory;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.form.MenuForm;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 * 

 * @date 2019年3月5日 10:52
 *
 */
@RestController

@RequestMapping("/auth/menu")
public class MenuController extends FoundationController implements BaseController<MenuForm> {

	private static final Logger LOGGER = LogManager.getLogger(MenuController.class);

	@Autowired
	private IMenuFactory iMenuFatory;

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody MenuForm menuForm) {
		LOGGER.info(LogMsg.to("msg:", "新增功能开始", "menuForm", menuForm));

		String menuId = menuForm.getMenuid();
		String parentId = menuForm.getParentid();
		String name = menuForm.getName();
		Integer level = menuForm.getLevel();
		Integer type = menuForm.getType();
		String modelId = menuForm.getModelid();
		Integer flag = menuForm.getFlag();// 标识是操作谁的菜单(如运维/水司)

		// 校验
		VerificationUtils.string("menuId", menuId);
		VerificationUtils.string("parentId", parentId);
		VerificationUtils.string("name", name);
		VerificationUtils.integer("level", level);
		VerificationUtils.integer("type", type);
		VerificationUtils.string("modelId", modelId);
		VerificationUtils.integer("flag", flag);
		if (level == 1) {
			String systype = menuForm.getSystype();
			VerificationUtils.string("systype", systype);
		} else {
			menuForm.setSystype(null);
		}

		MenuVo menuVo = iMenuFatory.add(menuForm);
		auditLog(OperateTypeEnum.MENU_MANAGEMENT,"新增","名称",name);
		LOGGER.info(LogMsg.to("msg:", "新增功能结束", "dataBackupForm", menuForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, menuVo);
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody MenuForm menuForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "menuForm", menuForm));
		Integer page = menuForm.getPage();
		Integer pageCount = menuForm.getPageCount();
		Integer flag = menuForm.getFlag();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		VerificationUtils.integer("flag", flag);

		Pagination<MenuVo> pagination = iMenuFatory.page(menuForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody MenuForm menuForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", "menuForm", menuForm));
		// backupId
		String menuId = menuForm.getMenuid();
		Integer flag = menuForm.getFlag();
		// 校验
		VerificationUtils.string("menuId", menuId);
		VerificationUtils.integer("flag", flag);

		MenuVo menuVo = iMenuFatory.get(menuForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", "menuForm", menuForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, menuVo);
	}

	@PostMapping(value = "/revise", produces = GlobalContext.PRODUCES)
	public String revise(@RequestBody MenuForm menuForm) {
		LOGGER.info(LogMsg.to("msg:", "修改开始", "menuForm", menuForm));
		String menuId = menuForm.getMenuid();
		String parentId = menuForm.getParentid();
		String name = menuForm.getName();
		Integer level = menuForm.getLevel();
		Integer type = menuForm.getType();
		String modelId = menuForm.getMenuid();
		Integer flag = menuForm.getFlag();// 标识是操作谁的菜单(如运维/水司)

		// 校验
		VerificationUtils.string("menuId", menuId);
		VerificationUtils.string("parentId", parentId);
		VerificationUtils.string("name", name);
		VerificationUtils.integer("level", level);
		VerificationUtils.integer("type", type);
		VerificationUtils.string("modelId", modelId);
		VerificationUtils.integer("flag", flag);

		MenuVo menuVo = iMenuFatory.revise(menuForm);
		auditLog(OperateTypeEnum.MENU_MANAGEMENT,"编辑","名称",name);
		LOGGER.info(LogMsg.to("msg:", "修改结束", "menuForm", menuForm));
		return resp(ResultCode.Success, "SUCCESS", menuVo);
	}

	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody List<MenuForm> list) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", "List<MenuForm>", list));

		if (list.isEmpty()) {
			return resp(ResultCode.Fail, "获取菜单ID失败", null);
		}
		for (MenuForm menuForm : list) {
			String menuId = menuForm.getMenuid();
			Integer flag = menuForm.getFlag();
			// 校验
			VerificationUtils.string("menuId", menuId);
			VerificationUtils.integer("flag", flag);
		}
		// TODO 是否删除源文件
		iMenuFatory.del(list);
		auditLog(OperateTypeEnum.MENU_MANAGEMENT,"删除");
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "menuId", null));
		return resp(ResultCode.Success, "SUCCESS", null);
	}
}
