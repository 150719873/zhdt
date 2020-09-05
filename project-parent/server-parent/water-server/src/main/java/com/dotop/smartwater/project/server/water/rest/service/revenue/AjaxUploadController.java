package com.dotop.smartwater.project.server.water.rest.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IAjaxUploadFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.ImportFileForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 统一导入导出
 *

 * @date 2019年4月10日
 */
@RestController

@RequestMapping("/AjaxUploadController")
public class AjaxUploadController implements BaseController<BaseForm> {

    private static final Logger LOGGER = LogManager.getLogger(AjaxUploadController.class);

    // 2MB
    private static final int MAX_REQUEST_SIZE = 2097152;

    @Autowired
    private IAjaxUploadFactory iAjaxUploadFactory;

    /**
     * 上传数据及保存文件
     *
     * @param request
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(HttpServletRequest request) {
        LOGGER.info(LogMsg.to("msg:", "上传功能开始"));
        String fileName = iAjaxUploadFactory.upload(request);
        LOGGER.info(LogMsg.to("msg:", "上传功能完成", "fileName", fileName));
        return resp(ResultCode.Success, "SUCCESS", fileName);
    }

    /**
     * 验证文件大小
     *
     * @throws IOException
     */
    @PostMapping(value = "/ajaxvc", produces = GlobalContext.PRODUCES)
    public String ajaxvc(HttpServletRequest request) {

        String filesize = request.getParameter("filesize");
        VerificationUtils.string("filesize", filesize);
        int size = Integer.parseInt(filesize);
        if (size > MAX_REQUEST_SIZE) {
            return resp(ResultCode.Fail, "文件过大", null);
        }
        return resp(ResultCode.Success, "Success", null);
    }

    /**
     * 报装管理-业主报装-导入
     *
     * @param request
     * @param importfile
     * @return
     */
    @PostMapping(value = "/import_owner", produces = GlobalContext.PRODUCES)
    public String importOwner(HttpServletRequest request, @RequestBody ImportFileForm importfile) {
        String filename = importfile.getFilename();
        VerificationUtils.string("filename", filename ,true , 200);

        iAjaxUploadFactory.importOwner(request, importfile);
        return resp(ResultCode.Success, "Success", null);

    }
}
