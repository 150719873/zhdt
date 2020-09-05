package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.INoticeFactory;
import com.dotop.smartwater.project.module.client.third.IOssService;
import com.dotop.smartwater.project.module.core.third.utils.oss.OssUtil;
import com.dotop.smartwater.project.module.core.water.constants.OssPathCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.NoticeForm;
import com.dotop.smartwater.project.module.core.water.vo.NoticeVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
 * 通知管理
 * 

 * @date 2019-03-06 11:29
 *
 */
@RestController

@RequestMapping("/notice")
public class NoticeController extends FoundationController implements BaseController<NoticeForm> {

	private static final Logger LOGGER = LogManager.getLogger(NoticeController.class);

	private static final String NOTICEID = "noticeId";

	@Autowired
	private INoticeFactory iNoticeFactory;

	@Autowired
	private OssUtil ossUtil;

	@Autowired
	private IOssService iOssService;

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", noticeForm));
		Integer page = noticeForm.getPage();
		Integer pageCount = noticeForm.getPageCount();
		String type = noticeForm.getType();
		String status = noticeForm.getStatus();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		VerificationUtils.string("type", type);
		VerificationUtils.string("status", status, true);
		Pagination<NoticeVo> pagination = iNoticeFactory.page(noticeForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", "新增功能开始", noticeForm));
		// 数据校验
		String title = noticeForm.getTitle();
		String body = noticeForm.getBody();
		String sendWay = noticeForm.getSendWay();
		String receiveWay = noticeForm.getReceiveWay();

		VerificationUtils.string("title", title, true, 256);
		VerificationUtils.string("body", body, true, 1024);
		VerificationUtils.string("sendType", sendWay);
		VerificationUtils.string("receiveWay", receiveWay);

		if (noticeForm.getReceiveObjList() == null || noticeForm.getReceiveObjList().isEmpty()) {
			return resp(ResultCode.Fail, "接收对象receiveObjList不能为空", null);
		}
		auditLog(OperateTypeEnum.NOTICE_MANAGEMENT, "发送通知", "通知内容", noticeForm.getTitle());
		Integer count = iNoticeFactory.addNotice(noticeForm, "inner");
		if (count == 1) {
			LOGGER.info(LogMsg.to("msg:", "新增功能结束", noticeForm));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "发送失败", null);
		}
	}
	
	@PostMapping(value = "/sendSMS", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String sendSMS(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", "发送短信或邮件开始", noticeForm));
		// 数据校验
		String title = noticeForm.getTitle();
		String body = noticeForm.getBody();
		String sendWay = noticeForm.getSendWay();
		String receiveWay = noticeForm.getReceiveWay();
		String enterpriseid = noticeForm.getEnterpriseid();

		VerificationUtils.string("title", title);
		VerificationUtils.string("body", body);
		VerificationUtils.string("sendType", sendWay);
		VerificationUtils.string("receiveWay", receiveWay);
		VerificationUtils.string("enterpriseid", enterpriseid);
		
		if (noticeForm.getReceiveObjList().isEmpty()) {
			return resp(ResultCode.Fail, "接收对象receiveObjList不能为空", null);
		}

		Integer count = iNoticeFactory.addNotice(noticeForm, "");
		if (count == 1) {
			LOGGER.info(LogMsg.to("msg:", "发送短信或邮件结束", noticeForm));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "发送失败", null);
		}
	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", noticeForm));
		// backupId
		String noticeId = noticeForm.getNoticeId();
		// 校验
		VerificationUtils.string(NOTICEID, noticeId);
		NoticeVo notcieVo = iNoticeFactory.get(noticeForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", noticeForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, notcieVo);
	}

	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", noticeForm));
		// id
		String noticeId = noticeForm.getNoticeId();
		// 校验
		VerificationUtils.string(NOTICEID, noticeId);
		
		auditLog(OperateTypeEnum.NOTICE_MANAGEMENT, "删除通知", "通知ID", noticeId);
		String id = iNoticeFactory.del(noticeForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", NOTICEID, id));
		return resp(ResultCode.Success, ResultCode.SUCCESS, id);
	}

	@PostMapping(value = "/reviseStatus", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String reviseStatus(@RequestBody NoticeForm noticeForm) {
		LOGGER.info(LogMsg.to("msg:", "修改状态开始", noticeForm));
		// id
		String noticeId = noticeForm.getNoticeId();
		// 校验
		VerificationUtils.string(NOTICEID, noticeId);

		Integer count = iNoticeFactory.revise(noticeForm);
		if (count == 1) {
			LOGGER.info(LogMsg.to("msg:", "修改状态结束", "count", count));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "FAIL", null);
		}
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
