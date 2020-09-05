package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.IConcentratorDeviceFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorDeviceForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 集中器设备入口层辑接口
 *
 *
 */
@RestController
@RequestMapping("/concentrator/device")
public class ConcentratorDeviceController implements BaseController<ConcentratorDeviceForm> {

    private static final Logger LOGGER = LogManager.getLogger(ConcentratorDeviceController.class);

    @Autowired
    private IConcentratorDeviceFactory iConcentratorDeviceFactory;

    private static final int MIN = 10;
    private static final String DATAFORMAT = "YYYYMMDDHHMMSS";


    /**
     * 获取一条集中器设备信息
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.get(concentratorDeviceForm));
    }

    /**
     * 查询集中器设备并分页
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.page(concentratorDeviceForm));
    }

    /**
     * 查询集中器设备
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.list(concentratorDeviceForm));
    }

    /**
     * 添加一条集中器设备信息
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.add(concentratorDeviceForm));
    }

    /**
     * 删除一个集中器设备
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        iConcentratorDeviceFactory.del(concentratorDeviceForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, ResultCode.SUCCESS);
    }

    /**
     * 编辑集中器设备信息
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.edit(concentratorDeviceForm));
    }

    /**
     * 设备档案分页。条件：采集器id
     */
    @PostMapping(value = "/page/archive", produces = GlobalContext.PRODUCES)
    public String pageArchive(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.page(concentratorDeviceForm));
    }

    /**
     * 设备档案分页导出
     */
    @PostMapping(value = "/archive/excel", produces = GlobalContext.PRODUCES)
    public void excel(@RequestBody ConcentratorDeviceForm concentratorDeviceForm, HttpServletResponse response, HttpServletRequest request) {
        LOGGER.info(LogMsg.to("msg:", "导出 设备档案", "concentratorDeviceForm", concentratorDeviceForm));
        try {
            response.setContentType("application/vnd.ms-excel; charset=UTF-8");
            response.setHeader("Content-disposition", "  filename=" + URLEncoder.encode("集中器：" + concentratorDeviceForm.getConcentrator().getCode() + "设备档案", "UTF-8") + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iConcentratorDeviceFactory.export(concentratorDeviceForm);
            ServletOutputStream outputStream = response.getOutputStream();
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/archive/excel", e);
        }

    }

    /**
     * 根据水表devid修改阀门状态
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/setTapstatus", produces = GlobalContext.PRODUCES)
    public String reordering(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.setTapstatus(concentratorDeviceForm));
    }

    /**
     * 查询设备日用水量
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/page/upLinkLogDate", produces = GlobalContext.PRODUCES)
    public String pageUpLinkLogDate(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.pageUpLinkLogDate(concentratorDeviceForm));
    }

    /**
     * 查询设备日用水量
     *
     * @param concentratorDeviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/page/upLinkLogMonth", produces = GlobalContext.PRODUCES)
    public String pageUpLinkLogMonth(@RequestBody ConcentratorDeviceForm concentratorDeviceForm) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorDeviceFactory.pageUpLinkLogMonth(concentratorDeviceForm));
    }

    @PostMapping(value = "/outUpLinkLogDateFile", produces = GlobalContext.PRODUCES)
    public void outUpLinkLogDateFile(@RequestBody ConcentratorDeviceForm concentratorDeviceForm, HttpServletResponse response) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("msg:", "导出水表报表日用图表格", "concentratorDeviceForm", concentratorDeviceForm));
        try {
            response.setContentType("application/binary;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = URLEncoder.encode("水表报表日用图表格", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iConcentratorDeviceFactory.exportUpLinkLogDate(concentratorDeviceForm);
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/outUpLinkLogDateFile", e);
        }
    }

    @PostMapping(value = "/outUpLinkLogMonthFile", produces = GlobalContext.PRODUCES)
    public void outUpLinkLogMonthFile(@RequestBody ConcentratorDeviceForm concentratorDeviceForm, HttpServletResponse response) throws FrameworkRuntimeException {
        LOGGER.info(LogMsg.to("msg:", "导出水表报表月用图表格", "concentratorDeviceForm", concentratorDeviceForm));
        try {
            response.setContentType("application/binary;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            String fileName = URLEncoder.encode("水表报表月用图表格", "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iConcentratorDeviceFactory.exportUpLinkLogMonth(concentratorDeviceForm);
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/outUpLinkLogMonthFile", e);
        }
    }

}
