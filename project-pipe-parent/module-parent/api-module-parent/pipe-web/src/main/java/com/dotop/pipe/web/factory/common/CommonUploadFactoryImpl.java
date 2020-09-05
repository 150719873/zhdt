package com.dotop.pipe.web.factory.common;

import com.dotop.pipe.api.service.common.ICommonUploadService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.common.CommonUploadBo;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.form.CommonUploadForm;
import com.dotop.pipe.core.vo.common.CommonUploadVo;
import com.dotop.pipe.web.api.factory.common.ICommonUploadFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
public class CommonUploadFactoryImpl implements ICommonUploadFactory {

    private final static Logger logger = LogManager.getLogger(CommonUploadFactoryImpl.class);

    @Value("${upload.base.path}")
    private String path;

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private ICommonUploadService iCommonUploadService;

    /**
     * 上传图片文件到服务器(单个文件)
     *
     * @param file
     * @return
     */
    @Override
    public CommonUploadVo uploadImg(MultipartFile file) {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            // 获取文件名后缀
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            // 源文件名称
            String originalName = file.getOriginalFilename();
            String filePath = this.path + "img//";
            String fileId = UuidUtils.getUuid();
            String fileName = fileId + suffix;
            File toFile = new File(filePath + fileName);
            if (!toFile.getParentFile().exists()) {
                toFile.mkdirs();
            }
            file.transferTo(toFile);
            CommonUploadVo commonUploadVo = new CommonUploadVo();
            commonUploadVo.setId(fileId);
            commonUploadVo.setFileName(fileName);
            commonUploadVo.setOriginalName(originalName);
            commonUploadVo.setFilePath(filePath);
            commonUploadVo.setCreateBy(loginCas.getUserName());
            commonUploadVo.setLastBy(loginCas.getUserName());
            commonUploadVo.setCreateDate(new Date());
            commonUploadVo.setLastDate(new Date());
            // TODO 上传数据库
            CommonUploadBo commonUploadBo = BeanUtils.copyProperties(commonUploadVo, CommonUploadBo.class);
            this.iCommonUploadService.add(commonUploadBo);
            return commonUploadVo;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.COMMON_UPLOAD_ERR, "msg",
                    PipeExceptionConstants.getMessage(PipeExceptionConstants.COMMON_UPLOAD_ERR)));
            throw new FrameworkRuntimeException(PipeExceptionConstants.COMMON_UPLOAD_ERR,
                    PipeExceptionConstants.getMessage(PipeExceptionConstants.COMMON_UPLOAD_ERR));
        }
        return new CommonUploadVo();
    }


    /**
     * 删除文件( 上传后删除 未保存到表中)
     *
     * @param commonUploadForm
     * @return
     */
    @Override
    public boolean delete(CommonUploadForm commonUploadForm) {
        // 数据库中获取数据
        if (commonUploadForm.getIds() != null && !commonUploadForm.getIds().isEmpty()) {
            List<CommonUploadVo> list = this.iCommonUploadService.get(commonUploadForm.getId(), commonUploadForm.getIds(), commonUploadForm.getThirdId());
            for (CommonUploadVo commonUploadVo : list) {
                File file = new File(commonUploadVo.getFilePath() + commonUploadVo.getFileName());
                file.delete();
            }
        }
        // 删除数据库
        this.iCommonUploadService.del(commonUploadForm.getId(), commonUploadForm.getIds(), commonUploadForm.getThirdId());
        return true;
    }

    @Override
    public List<CommonUploadVo> get(CommonUploadForm commonUploadForm) {
        InputStream fis = null;
        try {
            List<CommonUploadVo> list = this.iCommonUploadService.get(commonUploadForm.getId(), commonUploadForm.getIds(), commonUploadForm.getThirdId());
            for (CommonUploadVo commonUploadVo : list) {
                String filepath = commonUploadVo.getFilePath() + commonUploadVo.getFileName();
                if ("\"".equals(filepath.substring(0))) {
                    fis = new BufferedInputStream(new FileInputStream(filepath.split("\"")[1]));
                } else {
                    File file = new File(filepath);
                    if (file.exists()) {
                        fis = new BufferedInputStream(new FileInputStream(filepath));
                    } else {
                        continue;
                    }
                }
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                commonUploadVo.setContent(buffer);
            }
            return list;
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return null;
    }
}
