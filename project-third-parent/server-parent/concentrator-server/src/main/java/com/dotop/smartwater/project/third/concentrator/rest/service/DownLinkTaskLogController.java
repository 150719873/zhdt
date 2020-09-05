package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.IDownLinkTaskLogFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.form.DownLinkTaskLogForm;
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
 * 下发命令任务日志查询入口层辑接口
 *
 *
 */
@RestController
@RequestMapping("/downLinkTaskLog")
public class DownLinkTaskLogController implements BaseController<DownLinkTaskLogForm> {

    private static final Logger LOGGER = LogManager.getLogger(DownLinkTaskLogController.class);

    @Autowired
    IDownLinkTaskLogFactory iDownLinkTaskLogFactory;

    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody DownLinkTaskLogForm downLinkTaskLogForm) throws FrameworkRuntimeException {
        return null;
    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody DownLinkTaskLogForm downLinkTaskLogForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iDownLinkTaskLogFactory.page(downLinkTaskLogForm));
    }

    /**
     * 导出集中器时间范围内抄表结果
     * @param downLinkTaskLogForm
     * @param response
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/outConcentratorRecordFile", produces = GlobalContext.PRODUCES)
    public void outConcentratorRecordFile(@RequestBody DownLinkTaskLogForm downLinkTaskLogForm, HttpServletResponse response) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("msg:", "导出 集中器时间范围内抄表结果", "downLinkTaskLogForm", downLinkTaskLogForm));
        try {
            response.setContentType("application/binary;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = URLEncoder.encode("集中器时间范围内抄表结果", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iDownLinkTaskLogFactory.exportConcentratorRecord(downLinkTaskLogForm);
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/outConcentratorRecordFile", e);
        }
    }
}
