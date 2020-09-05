package com.dotop.smartwater.project.server.water.rest.service.app;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.project.module.core.water.vo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceFactory;
import com.dotop.smartwater.project.module.api.device.IMeterReadingFactory;
import com.dotop.smartwater.project.module.api.revenue.IInstallAppointmentFactory;
import com.dotop.smartwater.project.module.api.revenue.IOrderFactory;
import com.dotop.smartwater.project.module.api.revenue.IReviewDeviceFactory;
import com.dotop.smartwater.project.module.api.tool.INoticeFactory;
import com.dotop.smartwater.project.module.client.third.IOssService;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterDataForm;
import com.dotop.smartwater.project.module.core.third.utils.oss.OssUtil;
import com.dotop.smartwater.project.module.core.water.constants.OssPathCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.InstallAcceptanceForm;
import com.dotop.smartwater.project.module.core.water.form.InstallSurveyForm;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingDetailForm;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingTaskForm;
import com.dotop.smartwater.project.module.core.water.form.NoticeForm;
import com.dotop.smartwater.project.module.core.water.form.ReviewDetailForm;
import com.dotop.smartwater.project.module.core.water.form.ReviewDeviceForm;
import com.dotop.smartwater.project.module.core.water.utils.IpAdrressUtil;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;

/**
 * @Date 2019年4月28日
 */
@RestController
@RequestMapping(value = "/app/workcenter")

public class AppWorkcenterController implements BaseController<BaseForm> {

	private static final Logger LOGGER = LogManager.getLogger(AppHandheldController.class);

	@Autowired
	private INoticeFactory iNoticeFactory;

	@Autowired
	private IOrderFactory iOrderFactory;

	@Autowired
	private IReviewDeviceFactory iReviewDeviceFactory;

	@Autowired
	private IInstallAppointmentFactory iInstallAppointmentFactory;

	@Autowired
	private IMeterReadingFactory iMeterReadingFactory;

	@Autowired
	private IDeviceFactory iDeviceFactory;

	@Autowired
	private IOrderService iOrderService;

	private static final String ISPRINT = "1";

	private static final String SUCCESS = "SUCCESS";

	private static final String NUMBER = "number";

	private static final String BATCHNO = "batchNo";

	@Autowired
	private OssUtil ossUtil;

	@Autowired
	private IOssService iOssService;

	/**
	 * 获取通知（分页）
	 * 
	 * @param noticeForm
	 * @return
	 */
	@PostMapping(value = "/getNotices", produces = GlobalContext.PRODUCES)
	public String getNotices(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "noticeForm", noticeForm));
		Integer page = noticeForm.getPage();
		Integer pageCount = noticeForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);

		noticeForm.setType("RECEIVE");
		Pagination<NoticeVo> pagination = iNoticeFactory.page(noticeForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);

	}

	/**
	 * 获取通知详情
	 * 
	 * @param noticeForm
	 * @return
	 */
	@PostMapping(value = "/getNotice", produces = GlobalContext.PRODUCES)
	public String getNotice(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", "noticeForm", noticeForm));
		// backupId
		String noticeId = noticeForm.getNoticeId();
		// 校验
		VerificationUtils.string("noticeId", noticeId);
		NoticeVo notcieVo = iNoticeFactory.get(noticeForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", "noticeForm", noticeForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, notcieVo);
	}

	/**
	 * 修改通知状态
	 * 
	 * @param noticeForm
	 * @return
	 */
	@PostMapping(value = "/reviseStatus", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String reviseStatus(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", "修改状态开始", "noticeForm", noticeForm));
		// id
		String noticeId = noticeForm.getNoticeId();
		// 校验
		VerificationUtils.string("noticeId", noticeId);

		Integer count = iNoticeFactory.revise(noticeForm);
		if (count == 1) {
			LOGGER.info(LogMsg.to("msg:", "修改状态结束", "count", count));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "FAIL", null);
		}
	}

	/**
	 * 抄表任务分页
	 *
	 * @param agrs
	 * @return
	 */
	@PostMapping(value = "/meterTaskPage", produces = GlobalContext.PRODUCES)
	public String meterTaskPage(@RequestBody MeterReadingTaskForm agrs) {
		VerificationUtils.obj("MeterReadingTaskForm", agrs);
		Pagination<MeterReadingTaskVo> pagination = iMeterReadingFactory.pageApp(agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 抄表设备详情分页
	 *
	 * @param agrs
	 * @return
	 */
	@PostMapping(value = "/meterTaskDetail", produces = GlobalContext.PRODUCES)
	public String meterTaskDetail(@RequestBody MeterReadingDetailForm agrs) {
		VerificationUtils.obj("MeterReadingTaskForm", agrs);
		Pagination<MeterReadingDetailVo> pagination = iMeterReadingFactory.detailPage(agrs);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 设备详情信息
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/deviceDetail", produces = GlobalContext.PRODUCES)
	public String deviceDetail(@RequestBody MeterReadingDetailForm form) {
		VerificationUtils.string("id", form.getId());
		return resp(ResultCode.Success, ResultCode.SUCCESS, iMeterReadingFactory.deviceDetail(form));
	}

	/**
	 * 提交抄表信息
	 *
	 * @param meterDataForm
	 */
	@PostMapping(value = "/meterRead", produces = GlobalContext.PRODUCES)
	public String meterRead(HttpServletRequest request, @RequestBody MeterReadingDetailForm form) {
		LOGGER.info(LogMsg.to("msg:", " 提交抄表信息开始", "form", form));
		// 参数校验
		VerificationUtils.string("id", form.getId());
		VerificationUtils.string("userCode", form.getUserCode());
		VerificationUtils.string("meterCode", form.getMeterCode());
		VerificationUtils.string("readValue", form.getReadValue());
		UserVo user = AuthCasClient.getUser();
		DeviceVo device = iDeviceFactory.findByDevNo(form.getMeterCode());
		if (device != null && device.getDeveui() != null) {
			MeterDataForm meterDataForm = new MeterDataForm();
			meterDataForm.setDeveui(device.getDeveui());
			meterDataForm.setMeter(form.getReadValue());

			String qrCode = form.getQrCode();
			// 打印支付二维码
			if (qrCode != null && qrCode.equals(ISPRINT)) {
				// 抄表并生成账单
				iOrderFactory.readMeterAndBuildOrder(meterDataForm);

				// 修改抄表任务信息
				iMeterReadingFactory.submitMeter(form);

				// 查找生成的账单信息，并根据账单信息生成支付二维码
				Map<String, String> data = iOrderService.generatePayQrCode(user.getEnterpriseid(), form.getUserCode(),
						IpAdrressUtil.getIpAdrress(request));
				LOGGER.info(LogMsg.to("msg:", " 生成支付二维码", "data", data));
				if (data.get("result_code").equals(SUCCESS)) {
					return resp(ResultCode.Success, ResultCode.SUCCESS, data);
				} else {
					return resp(ResultCode.Fail, "支付二维码生成失败", data);
				}
			} else {
				// 抄表
				iOrderFactory.manualMeter(meterDataForm);

				// 修改抄表任务信息
				iMeterReadingFactory.submitMeter(form);
				return resp(ResultCode.Success, ResultCode.SUCCESS, null);
			}
		} else {
			return resp(ResultCode.NOTFINDWATER, ResultCode.getMessage(ResultCode.NOTFINDWATER), null);
		}
	}

	/**
	 * 提交设备复核
	 **/
	@PostMapping(value = "/addDeviceReview", produces = GlobalContext.PRODUCES)
	public String addDeviceReview(@RequestBody ReviewDeviceForm form) {

		VerificationUtils.string("title", form.getTitle());
		VerificationUtils.string("communityIds", form.getCommunityIds(), false, Integer.MAX_VALUE);
		VerificationUtils.string("communityNames", form.getCommunityNames(), false, Integer.MAX_VALUE);
		VerificationUtils.string("userIds", form.getUserIds(), false, Integer.MAX_VALUE);
		VerificationUtils.string("userNames", form.getUserNames(), false, Integer.MAX_VALUE);
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("endTime", form.getEndTime());
		VerificationUtils.string("diff", form.getDiff());
		VerificationUtils.string("devNumber", form.getDevNumber());

		iReviewDeviceFactory.addDeviceReview(form);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	/**
	 * 设备复核任务列表分页
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/getDeviceReviews", produces = GlobalContext.PRODUCES)
	public String getDeviceReviews(@RequestBody ReviewDeviceForm form) {
		LOGGER.info(LogMsg.to("msg:", " 设备复核分页查询", "form", form));
		Pagination<ReviewDeviceVo> pagination = iReviewDeviceFactory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 设备复核分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 设备复核详情分页
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/getDeviceReview", produces = GlobalContext.PRODUCES)
	public String getDeviceReview(@RequestBody ReviewDetailForm form) {
		LOGGER.info(LogMsg.to("msg:", " 设备复核详情分页查询开始", "form", form));
		VerificationUtils.string(BATCHNO, form.getBatchNo());
		Pagination<ReviewDetailVo> pagination = iReviewDeviceFactory.detailPage(form);
		LOGGER.info(LogMsg.to("msg:", " 设备复核详情分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 根据批次号、设备复核ID获取设备信息
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/getDevice", produces = GlobalContext.PRODUCES)
	public String getDevice(@RequestBody ReviewDetailForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取任务详情开始", "form", form));
		// 参数校验
		VerificationUtils.string(BATCHNO, form.getBatchNo());
		VerificationUtils.string("id", form.getId());
		ReviewDetailVo vo = iReviewDeviceFactory.getDevice(form);
		LOGGER.info(LogMsg.to("msg:", "获取任务情结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	/**
	 * 提交设备复核
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/submitReviewDevice", produces = GlobalContext.PRODUCES)
	public String submitReviewDevice(@RequestBody ReviewDetailForm form) {
		LOGGER.info(LogMsg.to("msg:", "提交设备复核开始", "form", form));
		// 参数校验
		VerificationUtils.string(BATCHNO, form.getBatchNo());
		VerificationUtils.string("id", form.getId());

		VerificationUtils.string("reviewWater", form.getReviewWater());
		VerificationUtils.string("reviewTime", form.getReviewTime());
		VerificationUtils.string("reviewStatus", form.getReviewStatus());
		VerificationUtils.string("submitStatus", form.getSubmitStatus());
		iReviewDeviceFactory.submitReviewDevice(form);
		LOGGER.info(LogMsg.to("msg:", "提交设备复核结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 获取勘测任务
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/surveyPage", produces = GlobalContext.PRODUCES)
	public String surveyPage(@RequestBody InstallSurveyForm form) {
		LOGGER.info(LogMsg.to("msg:", " 发起勘测任务开始", "form", form));
		// 参数校验
		Pagination<InstallSurveyVo> pagination = iInstallAppointmentFactory.surveyPage(form);
		LOGGER.info(LogMsg.to("msg:", " 发起勘测任务结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 提交勘测信息
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/submitSurvey", produces = GlobalContext.PRODUCES)
	public String submitSurvey(@RequestBody InstallSurveyForm form) {
		LOGGER.info(LogMsg.to("msg:", " 勘测人提交勘测信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("surveyId", form.getSurveyId());
		VerificationUtils.string("surveyTime", form.getSurveyTime());
		VerificationUtils.string("status", form.getStatus());
		VerificationUtils.string("place", form.getPlace());
		VerificationUtils.string("explan", form.getExplan());

		iInstallAppointmentFactory.survey(form);
		LOGGER.info(LogMsg.to("msg:", " 发起勘测任务结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 获取工程验收任务
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/acceptancePage", produces = GlobalContext.PRODUCES)
	public String acceptancePage(@RequestBody InstallAcceptanceForm form) {
		LOGGER.info(LogMsg.to("msg:", " 获取工程验收任务开始", "form", form));
		// 参数校验
		Pagination<InstallAcceptanceVo> pagination = iInstallAppointmentFactory.acceptancePage(form);
		LOGGER.info(LogMsg.to("msg:", " 获取工程验收任务结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 提交验收信息
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/submitAcceptance", produces = GlobalContext.PRODUCES)
	public String submitAcceptance(@RequestBody InstallAcceptanceForm form) {
		LOGGER.info(LogMsg.to("msg:", " 提交工程验收信息开始", "form", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string("acceptId", form.getAcceptId());
		VerificationUtils.string("acceptName", form.getAcceptName());
		VerificationUtils.string("status", form.getStatus());
		VerificationUtils.string("acceptTime", form.getAcceptTime());
		VerificationUtils.string("place", form.getPlace());
		VerificationUtils.string("explan", form.getExplan());

		iInstallAppointmentFactory.acceptance(form);
		LOGGER.info(LogMsg.to("msg:", "提交工程验收信息结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 上传数据及保存文件
	 */
	@PostMapping("/uploadFile")
	public String uploadFile(HttpServletRequest request, @RequestParam("userid") String userid) {
		LOGGER.info(LogMsg.to("msg:", " 上传附件开始", "userid", userid));
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		if (1 != files.size()) {
			return resp(ResultCode.ParamIllegal, "没有找到上传文件", null);
		}

		MultipartFile file;
		for (MultipartFile file1 : files) {
			file = file1;
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();

					String filename = file.getOriginalFilename();
					String contentType = ossUtil.getContentType(filename.substring(filename.lastIndexOf('.')));
					String url = iOssService.upLoadV2(bytes, OssPathCode.FILE, "appointment", filename, contentType);
					LOGGER.info(LogMsg.to("msg:", " 上传附件结束", "userid", userid));
					return resp(ResultCode.Success, ResultCode.SUCCESS, url);
				} catch (Exception e) {
					LOGGER.error("上传附件失败", e);
					return resp(ResultCode.Fail, e.getMessage(), null);
				}
			} else {
				return resp(ResultCode.Fail, "空文件！", null);
			}
		}
		return resp(ResultCode.Fail, "上传附件出现了未知的错误", null);

	}

}
