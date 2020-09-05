package com.dotop.smartwater.project.server.water.rest.service.device;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.project.server.water.rest.service.tool.AppVersionController;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceMigrationFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.DeviceMigrationForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceMigrationHistoryForm;
import com.dotop.smartwater.project.module.core.water.form.MigrationForm;
import com.dotop.smartwater.project.module.core.water.utils.ExcelUtil;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationHistoryVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import com.dotop.smartwater.project.server.water.rest.service.tool.AppVersionController;

/**
 * 设备迁移
 * @date 2019-08-08
 *
 */
@RestController

@RequestMapping("/deviceMigration")
public class DeviceMigrationController extends FoundationController implements BaseController<DeviceMigrationForm> {
	
	private static final Logger LOGGER = LogManager.getLogger(AppVersionController.class);

	@Autowired
	private IDeviceMigrationFactory iDeviceMigrationFactory;
	
	/**
	 * 获取临时表中的所有设备迁移记录
	 * @param deviceMigrationForm
	 * @return
	 */
	@PostMapping(value = "/tempPage", produces = GlobalContext.PRODUCES)
	public String tempPage(@RequestBody DeviceMigrationForm deviceMigrationForm) {
		LOGGER.info(LogMsg.to("msg:", "查询临时表开始", "deviceMigrationForm", deviceMigrationForm));
		
		List<DeviceMigrationVo> list = iDeviceMigrationFactory.tempPage(deviceMigrationForm);
		LOGGER.info(LogMsg.to("msg:", "查询查询临时表结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}
	/**
	 * 查询设备
	 * @param deviceMigrationForm
	 * @return
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DeviceMigrationForm deviceMigrationForm) {
		LOGGER.info(LogMsg.to("msg:", " 查询迁移设备信息开始", "deviceMigrationForm", deviceMigrationForm));
		String enterpriseid = deviceMigrationForm.getEnterpriseid();
		// 验证
		VerificationUtils.string("enterpriseid", enterpriseid);
		List<DeviceMigrationVo> list = iDeviceMigrationFactory.pageMigration(deviceMigrationForm);
		LOGGER.info(LogMsg.to("msg:", "查询迁移设备信息结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}
	/**
	 * 获取迁移历史详情
	 * @param deviceMigrationForm
	 * @return
	 */
	@PostMapping(value = "/detail", produces = GlobalContext.PRODUCES)
	public String detail(@RequestBody DeviceMigrationForm deviceMigrationForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceMigrationForm", deviceMigrationForm));
		Integer page = deviceMigrationForm.getPage();
		Integer pageCount = deviceMigrationForm.getPageCount();
		String migrationHistoryId = deviceMigrationForm.getMigrationHistoryId();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		VerificationUtils.string("migrationHistoryId", migrationHistoryId);
		Pagination<DeviceMigrationVo> pagination = iDeviceMigrationFactory.detail(deviceMigrationForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
	/**
	 * 清除设备列表缓存
	 * @param deviceMigrationForm
	 * @return
	 */
	@PostMapping(value = "/clearTemp", produces = GlobalContext.PRODUCES)
	public String clearTemp(@RequestBody DeviceMigrationForm deviceMigrationForm) {
		LOGGER.info(LogMsg.to("msg:", "清除设备列表开始", "deviceMigrationForm", deviceMigrationForm));
		
		iDeviceMigrationFactory.clearTemp(deviceMigrationForm);
		LOGGER.info(LogMsg.to("msg:", "清除设备列表结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	/**
	 * 获取迁移历史分页
	 * @param deviceMigrationHistoryForm
	 * @return
	 */
	@PostMapping(value = "/pageHistory", produces = GlobalContext.PRODUCES)
	public String pageHistory(@RequestBody DeviceMigrationHistoryForm deviceMigrationHistoryForm) {
		LOGGER.info(LogMsg.to("msg:", "获取迁移历史分页开始", "deviceMigrationHistoryForm", deviceMigrationHistoryForm));
		
		Pagination<DeviceMigrationHistoryVo> pagination = iDeviceMigrationFactory.pageHistory(deviceMigrationHistoryForm);
		LOGGER.info(LogMsg.to("msg:", "获取迁移历史分页结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
	/**
	 * 迁移设备
	 * @param migrationForm
	 * @return
	 */
	@PostMapping(value = "/migrationDevice", produces = GlobalContext.PRODUCES)
	public String migrationDevice(@RequestBody MigrationForm migrationForm) {
		LOGGER.info(LogMsg.to("msg:", "迁移设备开始", "migrationForm", migrationForm));
		if(migrationForm.getDeviceMigrationHistoryForm() == null) {
			return resp(ResultCode.Fail, "迁移原水司和目标水司不能为空", null);
		}
		String initialId = migrationForm.getDeviceMigrationHistoryForm().getInitialId();
		String targetId = migrationForm.getDeviceMigrationHistoryForm().getTargetId();
		// 验证
		VerificationUtils.string("initialId", initialId);
		VerificationUtils.string("targetId", targetId);
		
		iDeviceMigrationFactory.migrationDevice(migrationForm.getDeviceMigrationHistoryForm());
		LOGGER.info(LogMsg.to("msg:", "迁移设备结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	@PostMapping(value = "/deleteHistory", produces = GlobalContext.PRODUCES)
	public String deleteHistory(@RequestBody DeviceMigrationHistoryForm deviceMigrationHistoryForm) {
		LOGGER.info(LogMsg.to("msg:", "删除迁移历史设备开始", "deviceMigrationHistoryForm", deviceMigrationHistoryForm));
		String id = deviceMigrationHistoryForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		auditLog(OperateTypeEnum.DEVICE_MIGRATION, "删除设备迁移历史开始", "迁移ID", id);
		iDeviceMigrationFactory.deleteHistory(deviceMigrationHistoryForm);
		LOGGER.info(LogMsg.to("msg:", "删除迁移历史结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	/**
	 * 更新缓存列表数据状态
	 * @param deviceMigrationForm
	 * @return
	 */
	@PostMapping(value = "/updateTemp", produces = GlobalContext.PRODUCES)
	public String updateTemp(@RequestBody DeviceMigrationForm deviceMigrationForm) {
		LOGGER.info(LogMsg.to("msg:", "更新缓存设备列表开始", "deviceMigrationForm", deviceMigrationForm));
		
		Integer count = iDeviceMigrationFactory.updateTemp(deviceMigrationForm);
		LOGGER.info(LogMsg.to("msg:", "更新缓存设备列表结束"));
		if(count == 1) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, count);
		}else {
			return resp(ResultCode.Fail, "更新缓存设备迁移列表失败", null);
		}
	}
	
	@PostMapping(value = "/importExcel", produces = GlobalContext.PRODUCES)
	public String importExcel(HttpServletRequest request) {
		LOGGER.info(LogMsg.to("msg:", "导入水表号开始"));
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		if (1 != files.size()) {
			return resp(ResultCode.ParamIllegal, "没有找到上传文件", null);
		}
		MultipartFile file;
		for (MultipartFile file1 : files) {
			file = file1;
			if (!file.isEmpty()) {
				try {
					List<String> devnoList = new ArrayList<String>();
					byte[] bytes = file.getBytes();
					ByteArrayInputStream is = new ByteArrayInputStream(bytes);	
	                String[][] map = ExcelUtil.readExcelContent(is);

	                if (map == null || map.length <= 1) {
	                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "excel里面没有水表号等数据");
	                }
	                System.out.println("******map.length: " + map.length);
	                for (int i = 0; i < 10; i++) {
	                	for(int j = 0; j < map.length;j++) {
	                		// 过滤空行
		                    if (StringUtils.isBlank(map[j][i])) {
		                        continue;
		                    }
//		                    System.out.println("*******map["+j+"]["+i+"]: " + map[j][i]);
		                    devnoList.add(map[j][i]);
	                	}
	                }
	                System.out.println("*******devnoList: " + JSONUtils.toJSONString(devnoList));
	                LOGGER.info(LogMsg.to("msg:", "导入水表号结束"));
					return resp(ResultCode.Success, ResultCode.SUCCESS, devnoList);
				} catch (Exception e) {
					LOGGER.error("获取导入文件失败", e);
					return resp(ResultCode.Fail, e.getMessage(), null);
				}
			} else {
				return resp(ResultCode.Fail, "空文件！", null);
			}
		}
		return resp(ResultCode.Fail, "文件解析出现了未知的错误", null);
	}
}
