package com.dotop.pipe.server.rest.service.common;

import com.dotop.pipe.core.form.CommonUploadForm;
import com.dotop.pipe.core.vo.common.CommonUploadVo;
import com.dotop.pipe.web.api.factory.common.ICommonUploadFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @date 2019年1月16日
 */
@RestController()
@RequestMapping("/commonupload")
public class CommonUploadController implements BaseController<CommonUploadForm> {

    private final static Logger logger = LogManager.getLogger(CommonUploadController.class);

    @Autowired
    private ICommonUploadFactory iCommonUploadFactory;

    // 上传文件
    @PostMapping(value = {"/uploadImg"}, consumes = "multipart/form-data")
    public String upload(@RequestParam("uploadFile") MultipartFile file) {
        CommonUploadVo commonUploadVo = this.iCommonUploadFactory.uploadImg(file);
        return resp(commonUploadVo);
    }

    // 删除图片文件
    @PostMapping(value = "/del")
    public String delete(HttpServletRequest request, @RequestBody CommonUploadForm commonUploadForm) {
        return resp(this.iCommonUploadFactory.delete(commonUploadForm));
    }

    // 加载
    @GetMapping(value = {"get/{thirdId}"}, produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@PathVariable(name = "thirdId", required = false) String thirdId) {
        CommonUploadForm commonUploadForm = new CommonUploadForm();
        commonUploadForm.setThirdId(thirdId);
        return resp(this.iCommonUploadFactory.get(commonUploadForm));
    }
}
