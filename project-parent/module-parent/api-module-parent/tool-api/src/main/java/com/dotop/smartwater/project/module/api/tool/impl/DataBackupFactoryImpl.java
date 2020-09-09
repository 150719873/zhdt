package com.dotop.smartwater.project.module.api.tool.impl;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.tool.IDataBackupFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DataBackupBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DataBackupForm;
import com.dotop.smartwater.project.module.core.water.vo.DataBackupVo;
import com.dotop.smartwater.project.module.service.tool.IDataBackupService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * 数据备份接口
 *

 * @date 2019年2月23日
 */

@Component
public class DataBackupFactoryImpl implements IDataBackupFactory {

    private static final Logger LOGGER = LogManager.getLogger(DataBackupFactoryImpl.class);

    @Value("${druid.dbbackup.outputpath}")
    private String downloadBasePath;

    @Value("${druid.dbbackup.host}")
    private String host;

    @Value("${druid.dbbackup.port}")
    private String port;

    @Value("${druid.datasource.username}")
    private String username;

    @Value("${druid.datasource.password}")
    private String password;

    @Value("${druid.dbbackup.dbname}")
    private String dbname;

    @Value("${druid.dbbackup.mysqldump}")
    private String mysqldump;

    @Value("${druid.dbbackup.outputpath}")
    private String outPutPath;

    @Autowired
    private StringValueCache svc;

    @Autowired
    private IDataBackupService iDataBackupService;

    @Override
    public Pagination<DataBackupVo> page(DataBackupForm dataBackupForm) {

        UserVo user = AuthCasClient.getUser();
        String operEid = user.getEnterpriseid();

        DataBackupBo dataBackupBo = new DataBackupBo();
        BeanUtils.copyProperties(dataBackupForm, dataBackupBo);
        if (operEid.equals(WaterConstants.ADMIN_ENTERPRISE_ID)) {
            dataBackupBo.setEnterpriseid(null);
        } else {
            dataBackupBo.setEnterpriseid(operEid);
        }
        return iDataBackupService.page(dataBackupBo);
    }

    @Override
    public DataBackupVo get(DataBackupForm dataBackupForm) {
        UserVo user = AuthCasClient.getUser();
        String operEid = user.getEnterpriseid();
        DataBackupBo dataBackupBo = new DataBackupBo();
        if (operEid.equals(WaterConstants.ADMIN_ENTERPRISE_ID)) {
            dataBackupBo.setEnterpriseid(null);
        } else {
            dataBackupBo.setEnterpriseid(operEid);
        }
        dataBackupBo.setBackupId(dataBackupForm.getBackupId());
        return iDataBackupService.get(dataBackupBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public DataBackupVo add(DataBackupForm dataBackupForm) {
        UserVo user = AuthCasClient.getUser();
        Date curr = new Date();
        String userBy = user.getName();
        String operEid = user.getEnterpriseid();
        // 复制属性
        DataBackupBo dataBackupBo = new DataBackupBo();
        BeanUtils.copyProperties(dataBackupForm, dataBackupBo);
        // 属性复制
        dataBackupBo.setEnterpriseid(operEid);
        dataBackupBo.setUserBy(userBy);
        dataBackupBo.setCurr(curr);
        return iDataBackupService.add(dataBackupBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(DataBackupForm dataBackupForm) {
        UserVo user = AuthCasClient.getUser();
        String operEid = user.getEnterpriseid();
        String backupId = dataBackupForm.getBackupId();
        DataBackupBo dataBackupBo = new DataBackupBo();
        dataBackupBo.setBackupId(backupId);
        dataBackupBo.setEnterpriseid(operEid);
        DataBackupVo dataBackupVo = iDataBackupService.get(dataBackupBo);
        String fileName = dataBackupVo.getFileName();
        String src = dataBackupVo.getFileSrc();
        if (fileName != null) {
            Path path = Paths.get(downloadBasePath, src, fileName);
            try {
                if (path.toFile().exists()) {
                    Files.delete(path);
                }
            } catch (IOException e) {
                LOGGER.debug("context", e);
            }
        }
        iDataBackupService.del(dataBackupBo);
        return backupId;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void backupDb() {

        UserVo userVo = AuthCasClient.getUser();

        String flag = svc.getVo(userVo.getEnterpriseid(), String.class);
        if (flag != null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "这个操作不能频繁");
        }

        // 一天只能按一次
        svc.setVo(userVo.getEnterpriseid(), "1", 3600 * 24L);
        Date curr = new Date();
        String filename = outPutPath + dbname + "_" + DateUtils.formatDate(curr) + ".sql";
        try (
                FileOutputStream font = new FileOutputStream(filename);
                OutputStreamWriter writer = new OutputStreamWriter(font, StandardCharsets.UTF_8)) {
            Runtime rt = Runtime.getRuntime();
//            String sqlCommand = String.format("%s -h %s -u%s -p%s -c --opt %s", mysqldump, host, username, password,
//                    dbname);
            //"cmd  /c \"E:\\Program Files\\Java\\mysql-5.7.25-winx64\\mysql-5.7.25-winx64\\bin\\mysqldump\" -uroot -p123456 --set-charset=utf8 1901"
            String sqlCommand=String.format("cmd /c \"%s\" -u%s -p%s --set-charset=utf8 water",mysqldump,username,password);
            Process child = rt.exec(sqlCommand);
            InputStream in = child.getInputStream();
            InputStreamReader xx = new InputStreamReader(in, StandardCharsets.UTF_8);

            String inStr;
            StringBuilder sb = new StringBuilder();
            String outStr;
            BufferedReader br = new BufferedReader(xx);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();

            writer.write(outStr);
            writer.flush();
            in.close();
            xx.close();
            br.close();

            String userBy = userVo.getName();
            String operEid = userVo.getEnterpriseid();
            // 复制属性
            DataBackupBo dataBackupBo = new DataBackupBo();
            dataBackupBo.setFileName(filename.replace(outPutPath, ""));
            dataBackupBo.setFileSrc(filename);

            if (WaterConstants.ADMIN_ENTERPRISE_ID.equals(userVo.getEnterpriseid())) {
                dataBackupBo.setBackupOwner("1");
            } else {
                dataBackupBo.setBackupOwner("0");
            }

            dataBackupBo.setBackupType("手动备份");
            dataBackupBo.setBackupDate(curr);

            // 属性复制
            dataBackupBo.setEnterpriseid(operEid);
            dataBackupBo.setUserBy(userBy);
            dataBackupBo.setCurr(curr);
            iDataBackupService.add(dataBackupBo);

        } catch (Exception e) {
            LOGGER.debug("context", e);
        }
    }

}
