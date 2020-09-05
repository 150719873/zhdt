package com.dotop.smartwater.project.server.water.rest.service.storage;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.store.IOutStorageFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.OutStorageForm;
import com.dotop.smartwater.project.module.core.water.form.StorageForm;
import com.dotop.smartwater.project.module.core.water.utils.FileUtil;
import com.dotop.smartwater.project.module.core.water.vo.OutStorageVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 入库管理
 *

 * @date 2018-11-30 下午 15:40
 */

@RestController

@RequestMapping("/store")
public class OutStorageController extends FoundationController implements BaseController<OutStorageForm> {

    private static final Logger LOGGER = LogManager.getLogger(OutStorageController.class);

    @Autowired
    private IOutStorageFactory iOutStorageFactory;

    private static final String RECORDNO = "recordNo";

    private static final String DATAFORMAT = "YYYYMMDDHHMMSS";

    private static final int MIN = 10;

    @PostMapping(value = "/getOutStorByCon", produces = GlobalContext.PRODUCES)
    public String getOutStorByCon(@RequestBody OutStorageForm outStorageForm) {
        LOGGER.info(LogMsg.to("msg:", " 分页查询开始", outStorageForm));
        Integer page = outStorageForm.getPage();
        Integer pageCount = outStorageForm.getPageCount();
        // 验证
        VerificationUtils.integer("page", page);
        VerificationUtils.integer("pageCount", pageCount);

        // 数据封装
        Pagination<OutStorageVo> pagination = iOutStorageFactory.getOutStorByCon(outStorageForm);
        LOGGER.info(LogMsg.to("msg:", " 分页查询结束"));
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    @PostMapping(value = "/getOutStorByNo", produces = GlobalContext.PRODUCES)
    public String getOutStorByNo(@RequestBody OutStorageForm outStorageForm) {
        LOGGER.info(LogMsg.to("msg:", " 查询出库产品详情开始", outStorageForm));
        String recordNo = outStorageForm.getRecordNo();
        VerificationUtils.string(RECORDNO, recordNo);

        // 数据封装
        OutStorageVo outStore = iOutStorageFactory.getOutStorByNo(outStorageForm);
        LOGGER.info(LogMsg.to("msg:", " 查询出库产品详情结束", outStorageForm));
        return resp(ResultCode.Success, ResultCode.SUCCESS, outStore);
    }

    @PostMapping(value = "/addOutStor", produces = GlobalContext.PRODUCES)
    public String addOutStor(@RequestBody OutStorageForm outStorageForm) {
        LOGGER.info(LogMsg.to("msg:", " 新增出库开始", outStorageForm));
        String recordNo = iOutStorageFactory.addOutStor(outStorageForm);
        auditLog(OperateTypeEnum.STORAGE_MANAGEMENT,"新增出库");
        return resp(ResultCode.Success, ResultCode.SUCCESS, recordNo);
    }

    @PostMapping(value = "/editOutStor", produces = GlobalContext.PRODUCES)
    public String editOutStor(@RequestBody OutStorageForm outStorageForm) {
        LOGGER.info(LogMsg.to("msg:", " 修改出库信息开始", outStorageForm));
        String recordNo = outStorageForm.getRecordNo();
        VerificationUtils.string(RECORDNO, recordNo);

        Integer num = iOutStorageFactory.editOutStor(outStorageForm);
        if (num == 1) {
            LOGGER.info(LogMsg.to("msg:", " 修改出库信息结束"));
            auditLog(OperateTypeEnum.STORAGE_MANAGEMENT,"修改出库信息","记录号",recordNo);
            return resp(ResultCode.Success, ResultCode.SUCCESS, null);
        } else {
            return resp(ResultCode.Fail, "编辑失败！", null);
        }
    }

    @PostMapping(value = "/confirmOutStor", produces = GlobalContext.PRODUCES)
    public String confirmOutStor(@RequestBody OutStorageForm outStorageForm) {
        LOGGER.info(LogMsg.to("msg:", " 确认出库开始", outStorageForm));
        String recordNo = outStorageForm.getRecordNo();
        VerificationUtils.string(RECORDNO, recordNo);

        Integer num = iOutStorageFactory.confirmOutStor(outStorageForm);
        if (num == 1) {
            LOGGER.info(LogMsg.to("msg:", " 确认出库结束"));
            return resp(ResultCode.Success, ResultCode.SUCCESS, null);
        } else {
            return resp(ResultCode.Fail, "确认出库失败！", null);
        }
    }

    @PostMapping(value = "/deleteOutStor", produces = GlobalContext.PRODUCES)
    public String deleteOutStor(@RequestBody OutStorageForm outStorageForm) {
        LOGGER.info(LogMsg.to("msg:", " 删除出库信息开始", outStorageForm));
        String recordNo = outStorageForm.getRecordNo();
        VerificationUtils.string(RECORDNO, recordNo);

        if (iOutStorageFactory.getOutStorByNo(outStorageForm).getStatus() == 0) {
            return resp(ResultCode.Fail, "已出库信息不可以删除", null);
        }
        Integer num = iOutStorageFactory.deleteOutStor(outStorageForm);
        if (num == 1) {
            LOGGER.info(LogMsg.to("msg:", " 删除出库信息结束"));
            auditLog(OperateTypeEnum.STORAGE_MANAGEMENT,"删除出库信息","记录号",recordNo);
            return resp(ResultCode.Success, ResultCode.SUCCESS, null);
        } else {
            return resp(ResultCode.Fail, "删除失败！", null);
        }
    }

    @PostMapping(value = "/getProCount", produces = GlobalContext.PRODUCES)
    public String getProCount(@RequestBody StorageForm storageForm) {
        LOGGER.info(LogMsg.to("msg:", " 获取入库产品数量开始", "storageForm", storageForm));
        // 数据校验
        String repoNo = storageForm.getRepoNo();
        String productNo = storageForm.getProductNo();
        VerificationUtils.string("repoNo", repoNo);
        VerificationUtils.string("productNo", productNo);

        Integer num = iOutStorageFactory.getProCount(storageForm);
        if (num == null) {
            LOGGER.info(LogMsg.to("msg:", " 获取入库产品数量结束"));
            return resp(ResultCode.Success, ResultCode.SUCCESS, -1);
        } else if (num >= 0) {
            LOGGER.info(LogMsg.to("msg:", " 获取入库产品数量结束"));
            return resp(ResultCode.Success, ResultCode.SUCCESS, num);
        } else {
            return resp(ResultCode.Fail, "获取库存数量失败！", null);
        }
    }

    @PostMapping(value = "/exportOutStor", produces = GlobalContext.PRODUCES)
    public void exportOutStor(HttpServletRequest request, HttpServletResponse response,
                              @RequestBody OutStorageForm outStorageForm) {
        LOGGER.info(LogMsg.to("msg:", "导出出库产品信息开始", outStorageForm));
        try (ServletOutputStream output = response.getOutputStream();
             BufferedOutputStream buff = new BufferedOutputStream(output)) {
	    	SimpleDateFormat format = new SimpleDateFormat(DATAFORMAT);
	        String date = format.format(new Date());
            String path = request.getServletContext().getRealPath("/");
            File baseDir = new File(path, "OutStorageExcel" + date);
            if (!baseDir.exists() && baseDir.mkdirs()) {
                FileUtil.deleteFiles(baseDir, MIN);
                String fileName = URLEncoder.encode("出库数据查询", StandardCharsets.UTF_8.name()) + "_" + date + ".xls";
                String tempFileName = File.separator + "OutStorageExcel" + date + File.separator + fileName;
                tempFileName = path + tempFileName;
                String filePath = tempFileName;
                response.setContentType(GlobalContext.OCTET);
                response.setHeader("Content-Disposition", "filename=" + fileName);
                iOutStorageFactory.exportOutStor(tempFileName, outStorageForm);
                buff.write(FileUtil.readFile(filePath));
                buff.flush();
                LOGGER.info(LogMsg.to("msg:", "导出出库产品信息结束"));
            }
        } catch (Exception e) {
            LOGGER.error("store/exportOutStor", e);
        } finally {

        }
    }
}
