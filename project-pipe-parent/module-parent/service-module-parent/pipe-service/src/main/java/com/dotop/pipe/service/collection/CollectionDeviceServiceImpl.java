package com.dotop.pipe.service.collection;

import com.dotop.pipe.api.dao.collection.ICollectionDeviceDao;
import com.dotop.pipe.api.service.collection.ICollectionDeviceService;
import com.dotop.pipe.core.bo.collection.CollectionDeviceBo;
import com.dotop.pipe.core.dto.collection.CollectionDeviceDto;
import com.dotop.pipe.core.vo.collection.CollectionDeviceVo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollectionDeviceServiceImpl implements ICollectionDeviceService {

    private static final Logger logger = LogManager.getLogger(CollectionDeviceServiceImpl.class);

    @Autowired
    private ICollectionDeviceDao iCollectionDeviceDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public CollectionDeviceVo add(CollectionDeviceBo alarmSettingBo) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            CollectionDeviceDto collectionDeviceDto = BeanUtils.copyProperties(alarmSettingBo,
                    CollectionDeviceDto.class);
            collectionDeviceDto.setIsDel(isDel);
            collectionDeviceDto.setId(UuidUtils.getUuid());
            // 在插入
            iCollectionDeviceDao.add(collectionDeviceDto);
            // 返回
            CollectionDeviceVo collectionDeviceVo = new CollectionDeviceVo();
            collectionDeviceVo.setId(collectionDeviceDto.getId());
            return collectionDeviceVo;
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
    public Pagination<CollectionDeviceVo> page(CollectionDeviceBo collectionDeviceBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            CollectionDeviceDto alarmWMSettingDto = BeanUtils.copyProperties(collectionDeviceBo,
                    CollectionDeviceDto.class);
            alarmWMSettingDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(collectionDeviceBo.getPage(),
                    collectionDeviceBo.getPageSize());
            List<CollectionDeviceVo> list = iCollectionDeviceDao.page(alarmWMSettingDto);
            Pagination<CollectionDeviceVo> pagination = new Pagination<>(collectionDeviceBo.getPageSize(),
                    collectionDeviceBo.getPage());
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
    public String del(CollectionDeviceBo collectionDeviceBo) {
        try {
            Integer isDel = RootModel.DEL;
            CollectionDeviceDto collectionDeviceDto = BeanUtils.copyProperties(collectionDeviceBo,
                    CollectionDeviceDto.class);
            collectionDeviceDto.setIsDel(isDel);
            iCollectionDeviceDao.del(collectionDeviceDto);
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

}
