/**

 * @description :
 * @date : 2017年12月21日 上午10:08:48
 */
package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.io.BufferedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.IInformationFactory;
import com.dotop.smartwater.project.module.api.revenue.IReportFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.utils.FileUtil;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

@Controller

@RequestMapping("/ReportController")
public class ReportController implements BaseController<BaseForm> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private IReportFactory iReportFactory;
	
	@Autowired
	private IInformationFactory iInformationFactory;

	private static final String FILENAME = "filename=";

	private static final String DATEFORMAT = "yyyyMMddHHmmss";

	private static final String DISPOSITION = "Content-Disposition";

	private static final SimpleDateFormat SF = new SimpleDateFormat(DATEFORMAT);
	
	/**
	 * 导出 设备批次信息
	 *
	 * @param owner
	 * @return
	 */
	@PostMapping(value = "/exportDeviceBatch", produces = GlobalContext.PRODUCES)
	public void exportDeviceBatch(HttpServletResponse response, @RequestBody DeviceBatchForm form)
			throws UnsupportedEncodingException {
		LOGGER.info(LogMsg.to("msg:", "导出设备批次信息开始"));
		
		if (form.getFields() == null || form.getFields().size() == 0) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "导出列不能为空");
		}
		
		String filePath = iReportFactory.export_device_batch(form);
		String fileName = URLEncoder.encode("设备批次", StandardCharsets.UTF_8.name()) + "-" + SF.format(new Date())
				+ ".xls";
		response.setContentType(GlobalContext.OCTET);
		response.setHeader(DISPOSITION, FILENAME + fileName);
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			buff.write(FileUtil.readFile(filePath));
			buff.flush();
		} catch (Exception e) {
			LOGGER.error("导出:{}", e);
		}
		LOGGER.info(LogMsg.to("msg:", "导出设备批次信息结束"));

	}
	
	
	/**
	 * 导出 业主报装
	 *
	 * @param owner
	 * @return
	 */
	@PostMapping(value = "/export_owner", produces = GlobalContext.PRODUCES)
	public void exportOwner(HttpServletResponse response, @RequestBody OwnerForm owner)
			throws UnsupportedEncodingException {
		LOGGER.info(LogMsg.to("msg:", "导出 业主报装息开始"));
		
		if (owner.getFields() == null || owner.getFields().size() == 0) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "导出列不能为空");
		}
		
		String filePath = iReportFactory.export_owner(owner);
		String fileName = URLEncoder.encode("业主信息", StandardCharsets.UTF_8.name()) + "-" + SF.format(new Date())
				+ ".xls";
		response.setContentType(GlobalContext.OCTET);
		response.setHeader(DISPOSITION, FILENAME + fileName);
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			buff.write(FileUtil.readFile(filePath));
			buff.flush();
		} catch (Exception e) {
			LOGGER.error("导出:{}", e);
		}
		LOGGER.info(LogMsg.to("msg:", "导出 业主报装结束"));

	}

	/**
	 * 导出业主账单
	 * 
	 * @param view
	 * @return
	 */
	@PostMapping(value = "/export_ownerwater", produces = GlobalContext.PRODUCES)
	public void exportOwnerwater(HttpServletResponse response, @RequestBody PreviewForm view)
			throws UnsupportedEncodingException {
		LOGGER.info(LogMsg.to("msg:", "导出业主账单开始"));
		
		if (view.getFields() == null || view.getFields().size() == 0) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "导出列不能为空");
		}
		
		String filePath = iReportFactory.exportOwnerwater(view);
		String fileName = URLEncoder.encode("业主账单", StandardCharsets.UTF_8.name()) + "-" + SF.format(new Date())
				+ ".xls";
		response.setContentType(GlobalContext.OCTET);
		response.setHeader(DISPOSITION, FILENAME + fileName);
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			buff.write(FileUtil.readFile(filePath));
			buff.flush();
		} catch (Exception e) {
			LOGGER.error("导出:{}", e);
		}

		LOGGER.info(LogMsg.to("msg:", "导出业主账单结束"));
	}

	/**
	 * 导出水表信息
	 */
	@PostMapping(value = "/exportWaterInfo", produces = GlobalContext.PRODUCES)
	public void exportWaterInfo(HttpServletResponse response, @RequestBody DeviceForm deviceForm)
			throws UnsupportedEncodingException {
		LOGGER.info(LogMsg.to("msg:", "导出水表信息开始", deviceForm));
		if (deviceForm.getFields() == null || deviceForm.getFields().size() == 0) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "导出列不能为空");
		}

		String filePath = iReportFactory.exportWaterInfo(deviceForm);
		String fileName = URLEncoder.encode("水表信息", StandardCharsets.UTF_8.name()) + "-" + SF.format(new Date())
				+ ".xls";
		response.setContentType(GlobalContext.OCTET);
		response.setHeader(DISPOSITION, FILENAME + fileName);
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			buff.write(FileUtil.readFile(filePath));
			buff.flush();
		} catch (Exception e) {
			LOGGER.error("导出:{}", e);
		}
		LOGGER.info(LogMsg.to("msg:", "导出水表信息结束"));
	}
	
	/**
	 * 导出抄表信息
	 */
	@PostMapping(value = "/exportDeviceWater", produces = GlobalContext.PRODUCES)
	public void exportDeviceWater(HttpServletResponse response, @RequestBody DeviceForm deviceForm)
			throws UnsupportedEncodingException {
		LOGGER.info(LogMsg.to("msg:", "导出抄表数据开始", deviceForm));
		if (deviceForm.getFields() == null || deviceForm.getFields().size() == 0) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "导出列不能为空");
		}
		if (deviceForm.getAccesstime() == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "抄表时间不能为空");
		}
		String filePath = iReportFactory.exportDeviceWater(deviceForm);
		String fileName = URLEncoder.encode("抄表信息", StandardCharsets.UTF_8.name()) + "-" + SF.format(new Date()) + ".xls";
		response.setContentType(GlobalContext.OCTET);
		response.setHeader(DISPOSITION, FILENAME + fileName);
		
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			buff.write(FileUtil.readFile(filePath));
			buff.flush();
		} catch (Exception e) {
			LOGGER.error("导出:{}", e);
		}
		LOGGER.info(LogMsg.to("msg:", "导出抄表数据结束"));
	}
	
	/**
	 * 导出原始数据查询
	 * 
	 * @param queryForm the query form
	 * @return string
	 */
	@PostMapping(value = "/exportOriginal", produces = GlobalContext.PRODUCES)
	public void exportOriginal(HttpServletResponse response, @RequestBody QueryForm queryForm)
			throws UnsupportedEncodingException {
		LOGGER.info(LogMsg.to("msg:", "导出历史上行数据开始", queryForm));
		
		if (queryForm.getFields() == null || queryForm.getFields().size() == 0) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "导出列不能为空");
		}
		
		String filePath = iReportFactory.exportOriginal(queryForm);
		String fileName = URLEncoder.encode("历史上行数据", StandardCharsets.UTF_8.name()) + "-" + SF.format(new Date())
				+ ".xls";
		response.setContentType(GlobalContext.OCTET);
		response.setHeader(DISPOSITION, FILENAME + fileName);
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			buff.write(FileUtil.readFile(filePath));
			buff.flush();
		} catch (Exception e) {
			LOGGER.error("导出:{}", e);
		}
		LOGGER.info(LogMsg.to("msg:", "导出历史上传数据结束"));
	}
	
	/**
	 * 导出 数据监控信息
	 * 
	 * @param queryForm the query form
	 * @return string
	 */
	@PostMapping(value = "/exportMonitor", produces = GlobalContext.PRODUCES)
	public void exportMonitor(HttpServletResponse response, @RequestBody DeviceForm deviceForm)
			throws UnsupportedEncodingException {
		LOGGER.info(LogMsg.to("msg:", "导出数据监控数据开始", deviceForm));
		deviceForm.setPage(1);
		deviceForm.setPageCount(2000);
		
		//获取数据监控信息
		Pagination<DeviceVo> pagination = iInformationFactory.getMonitor(deviceForm);
		//设备管理-水表监控-获取时间间隔中的每一天
		List<String> dates = iInformationFactory.getDate(deviceForm);
		//导出信息
		String filePath = iReportFactory.exportMonitor(pagination, dates);
		String fileName = URLEncoder.encode("数据监控数据", StandardCharsets.UTF_8.name()) + "-" + SF.format(new Date()) + ".xls";
		response.setContentType(GlobalContext.OCTET);
		response.setHeader(DISPOSITION, FILENAME + fileName);
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			buff.write(FileUtil.readFile(filePath));
			buff.flush();
		} catch (Exception e) {
			LOGGER.error("导出:{}", e);
		}
		LOGGER.info(LogMsg.to("msg:", "导出数据监控数据结束"));
	}
}
