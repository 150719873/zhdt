package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.IUpLinkLogFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.form.UpLinkLogForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 上行历史日志查询入口层辑接口
 *
 *
 */
@RestController
@RequestMapping("/upLinkLog")
public class UpLinkLogController implements BaseController<UpLinkLogForm> {

    private static final Logger LOGGER = LogManager.getLogger(UpLinkLogController.class);

    @Autowired
    IUpLinkLogFactory iUpLinkLogFactory;

    /**
     * 获取设备上行记录并分页
     * @param upLinkLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iUpLinkLogFactory.page(upLinkLogForm));
    }

    /**
     * 导出设备上行记录详情
     * @param upLinkLogForm
     * @param response
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/outUplinkDetail", produces = GlobalContext.PRODUCES)
    public void outUplinkDetail(@RequestBody UpLinkLogForm upLinkLogForm, HttpServletResponse response) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("msg:", "导出 上行日志", "downLinkTaskLogForm", upLinkLogForm));
        try {
            response.setContentType("application/binary;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = URLEncoder.encode("上行日志", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iUpLinkLogFactory.export(upLinkLogForm);
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/outUplinkDetail", e);
        }
    }

    /**
     * 日跳变
     * @param upLinkLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/getUpLinkJump", produces = GlobalContext.PRODUCES)
    public String getUpLinkJump(@RequestBody UpLinkLogForm upLinkLogForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iUpLinkLogFactory.upLinkJump(upLinkLogForm));
    }

    @PostMapping(value = "/outUpLinkJumpFile", produces = GlobalContext.PRODUCES)
    public void outUpLinkJumpFile(@RequestBody UpLinkLogForm upLinkLogForm, HttpServletResponse response) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("msg:", "导出 水表跳变变化对比", "upLinkLogForm", upLinkLogForm));
        try {
            response.setContentType("application/binary;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = URLEncoder.encode("水表跳变变化对比", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iUpLinkLogFactory.exportUpLinkJump(upLinkLogForm);
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/outUpLinkJumpFile", e);
        }
    }

    @PostMapping(value = "/outRecordFile", produces = GlobalContext.PRODUCES)
    public void outRecordFile(@RequestBody UpLinkLogForm upLinkLogForm, HttpServletResponse response) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("msg:", "导出 上报记录", "upLinkLogForm", upLinkLogForm));
        try {
            response.setContentType("application/binary;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = URLEncoder.encode("上报记录", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iUpLinkLogFactory.exportRecord(upLinkLogForm);
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/outRecordFile", e);
        }
    }

}
