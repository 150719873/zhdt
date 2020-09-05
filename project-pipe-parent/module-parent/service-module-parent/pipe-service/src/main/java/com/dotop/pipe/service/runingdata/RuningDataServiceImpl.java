package com.dotop.pipe.service.runingdata;

import com.dotop.pipe.api.dao.runingdata.IRuningDataDao;
import com.dotop.pipe.api.service.runingdata.IRuningDataService;
import com.dotop.pipe.core.bo.runingdata.RuningDataBo;
import com.dotop.pipe.core.dto.runingdata.RuningDataDto;
import com.dotop.pipe.core.vo.runingdata.RuningDataVo;
import com.dotop.pipe.service.device.DeviceServiceImpl;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class RuningDataServiceImpl implements IRuningDataService {

    private final static Logger logger = LogManager.getLogger(DeviceServiceImpl.class);

    @Autowired
    private IRuningDataDao iRuningDataDao;

    @Resource(name = "druidDataSource")
    private DataSource dataSource;


    @Override
    public RuningDataVo add(RuningDataBo runingDataBo) throws FrameworkRuntimeException {

        try {
            Integer isDel = RootModel.NOT_DEL;
            String taskId = UuidUtils.getUuid();
            RuningDataDto runingDataDto = BeanUtils.copyProperties(runingDataBo, RuningDataDto.class);
            runingDataDto.setTaskId(taskId);
            runingDataDto.setIsDel(isDel);
            runingDataDto.setStatus("0"); // 未开始
            iRuningDataDao.add(runingDataDto);
            RuningDataVo runingDataVo = BeanUtils.copyProperties(runingDataDto, RuningDataVo.class);
            return runingDataVo;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }

    }

    @Override
    public Pagination<RuningDataVo> page(RuningDataBo runingDataBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            RuningDataDto runingDataDto = BeanUtils.copyProperties(runingDataBo, RuningDataDto.class);
            runingDataDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(runingDataBo.getPage(), runingDataBo.getPageSize());
            // 请求产品
//            Map<String, ProductVo> productVoMap = iWaterProductClient.storeMap(deviceBo.getProductCategory());
//            deviceDto.setProductIds(new ArrayList<>(productVoMap.keySet()));
            List<RuningDataVo> list = iRuningDataDao.list(runingDataDto);
            // 组装产品
//            WaterProductUtils.build(productVoMap, list, deviceBo.getProductCategory());
            Pagination<RuningDataVo> pagination = new Pagination<>(runingDataBo.getPageSize(), runingDataBo.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            return pagination;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void makeData(RuningDataBo runingDataBo) throws FrameworkRuntimeException {
        CallableStatement proc = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String storedProc = "{ call runing_data_p(?,?) }"; // 调用存储过程sql
            proc = connection.prepareCall(storedProc);
            proc.setString(1, runingDataBo.getTaskId()); // 企业id
            proc.setString(2, runingDataBo.getProductCategory()); // 产品类型
            proc.execute();
            proc.close();
            connection.close();
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        } finally {
            if (proc != null) {
                try {
                    proc.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String del(RuningDataBo runingDataBo) throws FrameworkRuntimeException {
        try {
            RuningDataDto runingDataDto = BeanUtils.copyProperties(runingDataBo, RuningDataDto.class);
            runingDataDto.setIsDel(RootModel.DEL);
            iRuningDataDao.del(runingDataDto);
            return null;
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public RuningDataVo edit(RuningDataBo runingDataBo) throws FrameworkRuntimeException {
        try {
            RuningDataDto runingDataDto = BeanUtils.copyProperties(runingDataBo, RuningDataDto.class);
            iRuningDataDao.edit(runingDataDto);
            return new RuningDataVo();
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }


    @Override
    public List<RuningDataVo> getRuningTask(RuningDataBo runingDataBo) throws FrameworkRuntimeException {
        try {
            RuningDataDto runingDataDto = BeanUtils.copyProperties(runingDataBo, RuningDataDto.class);
            runingDataDto.setStatus("1"); // 状态是运行中
            runingDataDto.setIsDel(RootModel.NOT_DEL);// 未删除的
            return iRuningDataDao.getRuningTask(runingDataDto);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }
}
