package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.form.BookForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 表册档案入口层辑接口
 *
 *
 */
@RestController
@RequestMapping("/book")
public class BookController implements BaseController<BookForm> {

    private static final Logger LOGGER = LogManager.getLogger(BookController.class);

    @PostMapping(value = "/export")
    public void exports(@RequestBody BookForm bookForm, HttpServletResponse response)
            throws UnsupportedEncodingException {
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public String imports(@RequestParam("file") MultipartFile file) {
        LOGGER.info(LogMsg.to("file", file));
        if (file.getSize() > 1048576) {
            return resp(ResultCode.Fail, "上传的文件过大", null);
        }
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }
}
