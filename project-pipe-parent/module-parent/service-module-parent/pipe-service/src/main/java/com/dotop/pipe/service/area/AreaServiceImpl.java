package com.dotop.pipe.service.area;

import com.dotop.pipe.api.dao.area.IAreaDao;
import com.dotop.pipe.api.service.area.IAreaService;
import com.dotop.pipe.core.bo.area.AreaBo;
import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.pipe.core.dto.area.AreaDto;
import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.point.PointVo;
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

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AreaServiceImpl implements IAreaService {

    private static final Logger logger = LogManager.getLogger(AreaServiceImpl.class);

    @Autowired
    private IAreaDao iAreaDao;

    @Override
    public String selectMaxAreaCodeByParentCode(String parentCode) throws FrameworkRuntimeException {
        try {
            return iAreaDao.selectMaxAreaCodeByParentCode(parentCode);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AreaModelVo add(AreaForm areaForm, String operEid, String userBy, Date date)
            throws FrameworkRuntimeException {
        try {
            String uuid = UuidUtils.getUuid();
            Integer isDel = RootModel.NOT_DEL;
            Integer isParent = PipeConstants.AREA_ISNOT_PARENT;
            iAreaDao.add(areaForm.getAreaCode(), areaForm.getName(), areaForm.getDes(), areaForm.getIsLeaf(),
                    areaForm.getParentCode(), areaForm.getAreaColorNum(), areaForm.getScale(), userBy, date, isDel,
                    operEid, isParent, uuid);
            AreaModelVo newAreaModelVo = new AreaModelVo();
            newAreaModelVo.setAreaId(uuid);
            newAreaModelVo.setAreaCode(areaForm.getAreaCode());
            return newAreaModelVo;
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public int updateIsParentByAreaCode(String parentCode, String enterpriseId, Date curr) throws FrameworkRuntimeException {
        try {
            Integer isParent = PipeConstants.AREA_IS_PARENT;
            return iAreaDao.updateIsParentByAreaCode(parentCode, isParent, enterpriseId, curr);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void addAreaRoot(String areaCode, String name, String operEid, String userBy, String des, Date date)
            throws FrameworkRuntimeException {
        try {
            String uuid = UuidUtils.getUuid();
            Integer isParent = PipeConstants.AREA_ISNOT_PARENT;
            Integer isDel = RootModel.NOT_DEL;
            Integer isLeaf = PipeConstants.AREA_IS_LEAF;
            String parentCode = PipeConstants.AREA_PARENT_CODE;
            // 默认水司颜色是模板是1  比例尺 5000
            iAreaDao.add(areaCode, name, des, isLeaf, parentCode, "1", "5000", userBy, date, isDel, operEid, isParent,
                    uuid);

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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public int del(String areaId, String userBy, Date date, String operEid) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.DEL;
            return iAreaDao.del(areaId, isDel, userBy, date, operEid);
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
    public List<AreaModelVo> list(String operEid) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            String parentCode = PipeConstants.AREA_PARENT_CODE;
            List<AreaModelVo> list = iAreaDao.list(operEid, isDel, parentCode);
            return list;
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
    public AreaModelVo getNodeDetails(String operEid, String areaId) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AreaModelVo areaModelVo = iAreaDao.getNodeDetails(operEid, areaId, isDel);
            if (areaModelVo.getPoints() != null) {
                areaModelVo.getPoints().sort(Comparator.comparing(PointVo::sortBycode));
            }
            return areaModelVo;
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public int edit(AreaBo areabo) {
        try {
            AreaDto areaDto = BeanUtils.copyProperties(areabo, AreaDto.class);
            int i = iAreaDao.edit(areaDto);
            return i;
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
    public int selectCountByParentCode(String parentCode, String operEid) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            int i = iAreaDao.selectCountByParentCode(parentCode, isDel, operEid);
            return i;
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public int updateIsParentByAreaId(String parentCode, String operEid,Date date) {
        try {
            Integer isNotParent = PipeConstants.AREA_ISNOT_PARENT;
            int i = iAreaDao.updateIsParentByAreaId(parentCode, isNotParent, operEid,date);
            return i;
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
    public List<AreaModelVo> listAll(String enterpriseId) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AreaDto areaDto = new AreaDto();
            areaDto.setIsDel(isDel);
            areaDto.setEnterpriseId(enterpriseId);
            return iAreaDao.listAll(areaDto);
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
    public Map<String, AreaModelVo> mapAll(String enterpriseId) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iAreaDao.mapAll(enterpriseId, isDel);
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
    public boolean isExist(String newAreaCode, String operEid) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iAreaDao.isExist(newAreaCode, operEid, isDel);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public int editNodeParent(List<AreaModelVo> updateList, String enterpriseId, String userBy, Date date) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iAreaDao.editNodeParent(updateList, enterpriseId, userBy, date, isDel);
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
    public List drawAreaList(AreaBo areaBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            AreaDto areaDto = BeanUtils.copyProperties(areaBo, AreaDto.class);
            areaDto.setIsDel(isDel);
            return iAreaDao.drawAreaList(areaDto);
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
    public Pagination<AreaModelVo> page(AreaBo areaBo) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            // 参数转换
            AreaDto areaDto = BeanUtils.copyProperties(areaBo, AreaDto.class);
            areaDto.setIsDel(isDel);
            Page<Object> pageHelper = PageHelper.startPage(areaBo.getPage(), areaBo.getPageSize());
            List<AreaModelVo> list = iAreaDao.listAll(areaDto);
            Pagination<AreaModelVo> pagination = new Pagination<>(areaBo.getPageSize(), areaBo.getPage());
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
    public boolean isExistChild(String operEid, String areaId) {
        try {
            Integer isDel = RootModel.NOT_DEL;
            return iAreaDao.isExistChild(areaId, operEid, isDel);
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
