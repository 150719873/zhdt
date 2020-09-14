package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.IConcentratorFactory;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 集中器/中继器入口层辑接口
 *
 *
 */
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/concentrator")
public class ConcentratorController implements BaseController<ConcentratorForm> {

    private static final Logger LOGGER = LogManager.getLogger(ConcentratorController.class);

    @Autowired
    private IConcentratorFactory iConcentratorFactory;

    //输入集中器编号等信息拿到集中器信息
    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        ConcentratorVo concentratorVo = iConcentratorFactory.get(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, concentratorVo);
    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        Pagination<ConcentratorVo> page = iConcentratorFactory.page(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        ConcentratorVo concentratorVo = iConcentratorFactory.add(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, concentratorVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        String del = iConcentratorFactory.del(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        ConcentratorVo concentratorVo = iConcentratorFactory.edit(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, concentratorVo);
    }

    @PostMapping(value = "/edit/status", produces = GlobalContext.PRODUCES)
    public String editStatus(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        ConcentratorVo concentratorVo = iConcentratorFactory.editStatus(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, concentratorVo);
    }

    /**
     * 集中器档案分页。
     */
    @PostMapping(value = "/page/archive", produces = GlobalContext.PRODUCES)
    public String pageArchive(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        Pagination<ConcentratorVo> page = iConcentratorFactory.pageArchive(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, page);
    }

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        List<ConcentratorVo> list = iConcentratorFactory.list(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }

    @PostMapping(value = "/outFile")
    public void outFile(@RequestBody ConcentratorForm concentratorForm, HttpServletResponse response) {
        LOGGER.info(LogMsg.to("msg:", "导出读取档案", "concentratorForm", concentratorForm));
        try {
            response.setContentType("application/vnd.ms-excel; charset=UTF-8");
            response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode("集中器：" + concentratorForm.getCode() + "档案", "UTF-8") + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iConcentratorFactory.export(concentratorForm);
            OutputStream outputStream = response.getOutputStream();
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/outUplinkDetail", e);
        }
    }

    /**
     * 导入设备档案
     *
     * @param multipartFile
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/device/archive/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) throws FrameworkRuntimeException {
        return resp(ResultCode.Success, ResultCode.SUCCESS, iConcentratorFactory.analyseFile(multipartFile));
    }

    /**
     * 集中器在线状态列表
     */
    @PostMapping(value = "/page/online")
    public String onlinePage(@RequestBody ConcentratorForm concentratorForm) throws FrameworkRuntimeException {
        Pagination<ConcentratorVo> page = iConcentratorFactory.onlinePage(concentratorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, page);
    }

    @PostMapping(value = "/outFileOnline")
    public void outFileOnline(@RequestBody ConcentratorForm concentratorForm, HttpServletResponse response) {
        LOGGER.info(LogMsg.to("msg:", "集中器是否在线全览图", "concentratorForm", concentratorForm));
        try {
            response.setContentType("application/vnd.ms-excel; charset=UTF-8");
            response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode("集中器是否在线全览图", "UTF-8") + DateUtils.formatDatetime(new Date()) + ".xlsx");
            XSSFWorkbook workBook = iConcentratorFactory.exportOnline(concentratorForm);
            OutputStream outputStream = response.getOutputStream();
            workBook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("/outFileOnline", e);
        }
    }

}
