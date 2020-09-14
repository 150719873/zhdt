package com.dotop.smartwater.project.third.concentrator.service.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorDeviceDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.concentrator.dao.IConcentratorDeviceDao;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorDeviceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 集中器设备数据获取层接口
 *
 *
 */
@Component("IConcentratorDeviceService")
public class ConcentratorDeviceServiceImpl implements IConcentratorDeviceService {

    private static final Logger LOGGER = LogManager.getLogger(ConcentratorDeviceServiceImpl.class);

    @Autowired
    private IConcentratorDeviceDao iConcentratorDeviceDao;

    /**
     * 分页查询集中器设备数据
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<ConcentratorDeviceVo> page(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(concentratorDeviceBo.getPage(), concentratorDeviceBo.getPageCount());
            List<ConcentratorDeviceVo> list = iConcentratorDeviceDao.list(concentratorDeviceDto);
            // 拼接数据返回
            return new Pagination<>(concentratorDeviceBo.getPage(), concentratorDeviceBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<ConcentratorDeviceVo> list(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
            return iConcentratorDeviceDao.list(concentratorDeviceDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 新增一条集中器设备数据
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public ConcentratorDeviceVo add(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
            concentratorDeviceDto.setIsDel(RootModel.NOT_DEL);
            iConcentratorDeviceDao.addConcentratorDevice(concentratorDeviceDto);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 更新一条集中器设备数据
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public ConcentratorDeviceVo edit(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
            Integer count = iConcentratorDeviceDao.updateConcentratorDevice(concentratorDeviceDto);
            if(count != 1) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "更新失败");
            }
            return BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceVo.class);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 获取一条集中器设备数据
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public ConcentratorDeviceVo get(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
            return iConcentratorDeviceDao.get(concentratorDeviceDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 根据code查询集中器设备
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public ConcentratorDeviceVo getByCode(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
            return iConcentratorDeviceDao.get(concentratorDeviceDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 查询集中器设备是否存在
     *
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public boolean isExist(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        // 参数转换
        ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
        ConcentratorDeviceVo concentratorDeviceVo = iConcentratorDeviceDao.get(concentratorDeviceDto);
        if (concentratorDeviceVo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除一条集中器设备数据
     * @param concentratorDeviceBo
     * @throws FrameworkRuntimeException
     */
    @Override
    public void delete(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
            int i = iConcentratorDeviceDao.delete(concentratorDeviceDto);
            if (i == 0) {
                Exception e = new Exception("删除失败");
                throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 根据企业id，集中器id，设备编号计算设备数量
     * @param enterpriseid
     * @param id
     * @param devno
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Integer countConcentratorDevice(String enterpriseid, String id, String devno) throws FrameworkRuntimeException {
        try {
            Integer count = iConcentratorDeviceDao.countConcentratorDevice(id, devno, enterpriseid);
            if (count == null) {
                return 0;
            } else {
                return count;
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 根据企业id，集中器id，集中器编号统计一个集中器下挂了多少个采集器通道
     * @param enterpriseid
     * @param id
     * @param code
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Integer countCollectorChannel(String enterpriseid, String id, String code) throws FrameworkRuntimeException {
        try {
            Integer count = iConcentratorDeviceDao.countCollectorChannel(id, code, enterpriseid);
            if (count == null) {
                return 0;
            } else {
                return count;
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 根据企业id， 采集器id，水表号统计一个采集器下挂了多少个水表
     * @param enterpriseid
     * @param id
     * @param devno
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Integer countCollectorDevice(String enterpriseid, String id, String devno) throws FrameworkRuntimeException {
        try {
            Integer count = iConcentratorDeviceDao.countCollectorDevice(id, devno, enterpriseid);
            if (count == null) {
                return 0;
            } else {
                return count;
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 查询集中器设备列表，并排序
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<ConcentratorDeviceVo> getConcentratorDeviceSort(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);
            return iConcentratorDeviceDao.list(concentratorDeviceDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 更新序号
     * @param concentratorDeviceBos
     * @throws FrameworkRuntimeException
     */
    @Override
    public void updateNo(List<ConcentratorDeviceBo> concentratorDeviceBos) throws FrameworkRuntimeException {
        try {
            // 参数转换
            if (concentratorDeviceBos != null && !concentratorDeviceBos.isEmpty()) {
                List<ConcentratorDeviceDto> concentratorDeviceDtos = BeanUtils.copy(concentratorDeviceBos, ConcentratorDeviceDto.class);
                iConcentratorDeviceDao.updateNo(concentratorDeviceDtos);
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 修改阀门状态
     * @param concentratorDeviceBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Integer setTapstatus(ConcentratorDeviceBo concentratorDeviceBo) throws FrameworkRuntimeException {
        try {
            ConcentratorDeviceDto concentratorDeviceDto = BeanUtils.copy(concentratorDeviceBo, ConcentratorDeviceDto.class);

            Integer count = iConcentratorDeviceDao.setTapstatus(concentratorDeviceDto);
            if (count == null) {
                return 0;
            } else {
                return count;
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 批量添加水表
     * @param concentratorDeviceBos
     * @throws FrameworkRuntimeException
     */
    @Override
    public void adds(List<ConcentratorDeviceBo> concentratorDeviceBos) throws FrameworkRuntimeException {
        try {
            // 参数转换
            List<ConcentratorDeviceDto> concentratorDeviceDtos = BeanUtils.copy(concentratorDeviceBos, ConcentratorDeviceDto.class);
            iConcentratorDeviceDao.adds(concentratorDeviceDtos);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
