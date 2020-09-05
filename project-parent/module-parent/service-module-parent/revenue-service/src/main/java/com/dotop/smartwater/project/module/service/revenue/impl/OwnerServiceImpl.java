package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerChangeBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerRecordBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerChangeDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerExtDto;
import com.dotop.smartwater.project.module.core.water.dto.OwnerRecordDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerExtVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OwnerDeviceVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceDao;
import com.dotop.smartwater.project.module.dao.revenue.IOwnerDao;
import com.dotop.smartwater.project.module.dao.revenue.IOwnerExtDao;
import com.dotop.smartwater.project.module.dao.revenue.IOwnerRecordDao;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 */
@Service
public class OwnerServiceImpl implements IOwnerService {

    private static final Logger LOGGER = LogManager.getLogger(OwnerServiceImpl.class);
    //导入时插入普通用户类型
    private static final String OWNER_TYPE = "28,100002,2";
    @Resource
    private IOwnerDao iOwnerDao;

    @Resource
    private IOwnerExtDao iOwnerExtDao;

    @Resource
    private IDeviceDao iDeviceDao;

    @Resource
    private IOwnerRecordDao iOwnerRecordDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public OwnerVo add(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            ownerDto.setOwnerid(UuidUtils.getUuid());

            iOwnerDao.add(ownerDto);

            if (ownerBo.getOwnerExtForm() != null) {
                OwnerExtDto ownerExtDto = BeanUtils.copy(ownerBo.getOwnerExtForm(), OwnerExtDto.class);
                ownerExtDto.setOwnerId(ownerDto.getOwnerid());
                iOwnerExtDao.add(ownerExtDto);
            }

            OwnerVo ownerVo = new OwnerVo();
            ownerVo.setOwnerid(ownerDto.getOwnerid());
            return ownerVo;

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public boolean batchAdd(List<OwnerBo> list) {
        try {
            // 参数转换
            List<OwnerDto> owners = new ArrayList<OwnerDto>();
            List<OwnerExtDto> exts = new ArrayList<OwnerExtDto>();
            for (OwnerBo bo : list) {
                OwnerDto dto = BeanUtils.copy(bo, OwnerDto.class);
                OwnerExtDto ext = BeanUtils.copy(bo.getOwnerExtForm(), OwnerExtDto.class);
                owners.add(dto);
                exts.add(ext);
            }
            iOwnerDao.batchAdd(owners);
            iOwnerExtDao.batchAddExt(exts);
            return true;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean batchEdit(List<OwnerBo> list) {
        try {
            // 参数转换
            List<OwnerDto> owners = new ArrayList<OwnerDto>();
            for (OwnerBo bo : list) {
                OwnerDto dto = BeanUtils.copy(bo, OwnerDto.class);
                owners.add(dto);
            }
            iOwnerDao.batchEdit(owners);
            return true;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean batchChange(List<OwnerChangeBo> list) {
        try {
            // 参数转换
            List<OwnerChangeDto> changes = new ArrayList<OwnerChangeDto>();
            for (OwnerChangeBo bo : list) {
                OwnerChangeDto dto = BeanUtils.copy(bo, OwnerChangeDto.class);
                changes.add(dto);
            }
            iOwnerDao.batchChange(changes);
            return true;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }


    @Override
    public int checkByOwnerNo(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = new OwnerDto();

            ownerDto.setOwnerid(ownerBo.getOwnerid());
            ownerDto.setUserno(ownerBo.getUserno());
            return iOwnerDao.checkByOwnerNo(ownerDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    
    public int checkByBarCode(OwnerBo ownerBo) {
    	try {
            OwnerDto ownerDto = new OwnerDto();
            BeanUtils.copy(ownerBo, ownerDto);
            return iOwnerDao.checkBybarCode(ownerDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo findOwnerByDevNo(OwnerBo ownerBo) {
        try {
            return iOwnerDao.findOwnerByDevNo(ownerBo.getDevno());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    public List<String> userNoCheck(Set<String> userNoSet) {
    	try {
            StringBuilder userNos = new StringBuilder();
            for (String userNo : userNoSet) {
                if (StringUtils.isNotBlank(userNo)) {
                	userNos.append("'").append(userNo).append("',");
                }
            }

            if (userNos.length() != 0) {
                String userNostr = userNos.toString().substring(0, userNos.length() - 1);
                return iOwnerDao.userNoCheck(userNostr);
            }

            return new ArrayList<>();

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    

    @Override
    public String findDevIdByDevNo(OwnerBo owner) {
        try {
            return iOwnerDao.findDevIdByDevNo(owner.getDevno());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceVo findDevByDevNo(OwnerBo owner) {
        try {
            return iDeviceDao.findByDevNo(owner.getDevno());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void openOwner(OwnerBo owner) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(owner, OwnerDto.class);

            updateDeviceEnterpriseId(ownerDto);

            iOwnerDao.openOwner(ownerDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    public int createAndopenOwner(OwnerBo owner) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(owner, OwnerDto.class);
            updateDeviceEnterpriseId(ownerDto);
            return iOwnerDao.createAndopenOwner(ownerDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo findByOwnerId(OwnerBo ownerBo) {
        try {
            OwnerExtDto ownerExtDto = BeanUtils.copy(ownerBo, OwnerExtDto.class);
            ownerExtDto.setOwnerId(ownerBo.getOwnerid());
            OwnerVo ownerVo = iOwnerDao.findByOwnerId(ownerExtDto);
            if (ownerVo != null) {
                ownerExtDto.setOwnerId(ownerVo.getOwnerid());
                OwnerExtVo ownerExtVo = iOwnerExtDao.get(ownerExtDto);
                ownerVo.setOwnerExtVo(ownerExtVo);
            }

            return ownerVo;

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public OwnerVo getOwnerById(OwnerBo ownerBo) {
        try {
            OwnerExtDto ownerExtDto = BeanUtils.copy(ownerBo, OwnerExtDto.class);
            ownerExtDto.setOwnerId(ownerBo.getOwnerid());
            OwnerVo ownerVo = iOwnerDao.getOwnerById(ownerExtDto);
            if (ownerVo != null) {
                ownerExtDto.setOwnerId(ownerVo.getOwnerid());
                OwnerExtVo ownerExtVo = iOwnerExtDao.get(ownerExtDto);
                ownerVo.setOwnerExtVo(ownerExtVo);
            }

            return ownerVo;

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void webUpdate(OwnerBo ownerBo) {
        try {
            OwnerDto o = BeanUtils.copy(ownerBo, OwnerDto.class);

            if (ownerBo.getOwnerExtForm() != null) {
                OwnerExtDto ownerExtDto = BeanUtils.copy(ownerBo.getOwnerExtForm(), OwnerExtDto.class);
                ownerExtDto.setOwnerId(ownerBo.getOwnerid());
                if (iOwnerExtDao.get(ownerExtDto) != null) {
                    if (StringUtils.isNotBlank(ownerExtDto.getOwnerType())) {
                        iOwnerExtDao.edit(ownerExtDto);
                    }
                } else {
                    ownerExtDto.setOwnerId(ownerBo.getOwnerid());
                    iOwnerExtDao.add(ownerExtDto);
                }
            }

            if (ownerBo.getStatus() == WaterConstants.OWNER_STATUS_UNOPRN) {
                iOwnerDao.updateForNotOpen(o);
                return;
            }
            iOwnerDao.webUpdate(o);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void delete(OwnerBo ownerBo) {
        try {
            iOwnerExtDao.del(BeanUtils.copy(ownerBo, OwnerExtDto.class));
            iOwnerDao.delete(ownerBo.getOwnerid());

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void addOwnerRecord(OwnerRecordBo ownerRecord) {
        try {

            OwnerRecordDto ownerRecordDto = BeanUtils.copy(ownerRecord, OwnerRecordDto.class);
            ownerRecordDto.setId(UuidUtils.getUuid());
            iOwnerRecordDao.addOwnerRecord(ownerRecordDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void cancelOwner(OwnerBo ownerBo) {
        try {
            if (ownerBo == null) {
                return;
            }
            ownerBo.setAlreadypay(0.0);
            ownerBo.setStatus(WaterConstants.OWNER_STATUS_DELETE);
            ownerBo.setDevid(null);
            ownerBo.setModelid(null);

            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            iOwnerDao.updateStatusAndAlreadyPay(ownerDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void updateDevid(OwnerVo owner) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(owner, OwnerDto.class);

            updateDeviceEnterpriseId(ownerDto);

            iOwnerDao.updateDevId(ownerDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    private void updateDeviceEnterpriseId(OwnerDto ownerDto) {
        if (StringUtils.isNotBlank(ownerDto.getEnterpriseid())) {
            // 更新水表所属水司
            DeviceDto deviceDto = new DeviceDto();
            deviceDto.setDevid(ownerDto.getDevid());
            deviceDto.setEnterpriseid(ownerDto.getEnterpriseid());
            iDeviceDao.update(deviceDto);
        }
    }

    @Override
    public OwnerVo findByOwnerUserno(OwnerBo ownerBo) {
        try {
            return iOwnerDao.findByOwnerUserNo(ownerBo.getUserno(), ownerBo.getOwnerid());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<OwnerVo> getOwners(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = new OwnerDto();
            if (StringUtils.isNotBlank(ownerBo.getKeywords())) {
                ownerDto.setDevno("%" + ownerBo.getKeywords() + "%");
                ownerDto.setUsername("%" + ownerBo.getKeywords() + "%");
                ownerDto.setUserphone("%" + ownerBo.getKeywords() + "%");
            }

            if (ownerBo.getIsonline() != null) {
                ownerDto.setIsonline(ownerBo.getIsonline());
            }

            if (ownerBo.getNodeIds() != null && !ownerBo.getNodeIds().isEmpty()) {
                ownerDto.setNodeIds(ownerBo.getNodeIds());
            }

            ownerDto.setStatus(WaterConstants.OWNER_STATUS_CREATE);

            Page<Object> pageHelper = PageHelper.startPage(ownerBo.getPage(), ownerBo.getPageCount());

            List<OwnerVo> list = iOwnerDao.getOwners(ownerDto);

            // 拼接数据返回
            return new Pagination<>(ownerBo.getPage(), ownerBo.getPageCount(), list, pageHelper.getTotal());

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<OwnerVo> getCommunityOwner(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = new OwnerDto();
            BeanUtils.copy(ownerBo, ownerDto);
            return iOwnerDao.getCommunityOwner(ownerDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<OwnerVo> getOwnerList(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);

            Page<Object> pageHelper = PageHelper.startPage(ownerBo.getPage(), ownerBo.getPageCount());

            //修复选择区域为空的SQL错误 nodeIds
            if (ownerDto.getNodeIds() != null && ownerDto.getNodeIds().size() == 0) {
                ownerDto.setNodeIds(null);
            }
            //如果排序数据为空，则将其过滤
            if(ownerDto.getSortList() != null && ownerDto.getSortList().isEmpty()) {
            	ownerDto.setSortList(null);
            }
            List<OwnerVo> list = iOwnerDao.getOwnerList(ownerDto);

            // 拼接数据返回
            return new Pagination<>(ownerBo.getPage(), ownerBo.getPageCount(), list, pageHelper.getTotal());

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo findByOwnerDetail(OwnerBo ownerBo) {
        try {
        	OwnerVo ownerVo = iOwnerDao.findByOwnerDetail(ownerBo.getOwnerid());
        	if (ownerVo != null) {
        		OwnerExtDto ownerExtDto = new OwnerExtDto();
                ownerExtDto.setOwnerId(ownerVo.getOwnerid());
                OwnerExtVo ownerExtVo = iOwnerExtDao.get(ownerExtDto);
                ownerVo.setOwnerExtVo(ownerExtVo);
            }
            return ownerVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<OwnerRecordVo> getRecordList(OwnerBo ownerBo) {
        try {
            OwnerRecordDto ownerRecordDto = new OwnerRecordDto();

            ownerRecordDto.setEnterpriseid(ownerBo.getEnterpriseid());
            ownerRecordDto.setOwnerid(ownerBo.getOwnerid());

            Page<Object> pageHelper = PageHelper.startPage(ownerBo.getPage(), ownerBo.getPageCount());

            List<OwnerRecordVo> list = iOwnerRecordDao.getRecordList(ownerRecordDto);

            return new Pagination<>(ownerBo.getPage(), ownerBo.getPageCount(), list, pageHelper.getTotal());

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void batchUpdateOwner(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            iOwnerDao.batchUpdateOwner(ownerDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public int getOwnerCount(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            ownerDto.setStatus(WaterConstants.OWNER_STATUS_CREATE);

            return iOwnerDao.getOwnerCount(ownerDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo getOwnerUser(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            return iOwnerDao.getOwnerUser(ownerDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo getOwnerUserByDevId(String devid) {
        try {
            return iOwnerDao.getOwnerUserByDevId(devid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<OwnerVo> findByCommunity(String areaId) {
        try {
            return iOwnerDao.findOwnerByCommunity(areaId);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Long getUserCount(String enterpriseId) {
        try {
            return iOwnerDao.getUserCount(enterpriseId);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Long userOpenCount(String enterpriseId) {
    	try {
            return iOwnerDao.userOpenCount(enterpriseId);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public Long areaCount(String enterpriseId) {
    	try {
            return iOwnerDao.areaCount(enterpriseId);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public List<OwnerDeviceVo> findOwnerByCommunityIds(String communityIds, String enterpriseid) {
        try {
            StringBuilder str = new StringBuilder();
            for (String id : communityIds.split(",")) {
                if (str.length() == 0) {
                    str.append("'").append(id).append("'");
                } else {
                    str.append(",'").append(id).append("'");
                }
            }
            return iOwnerDao.findOwnerByCommunityIds(str.toString(), enterpriseid);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<OwnerDeviceVo> findOwnerByOwnernos(String usernos, String enterpriseid) {
        try {
            StringBuilder str = new StringBuilder();
            for (String id : usernos.split(",")) {
                if (str.length() == 0) {
                    str.append("'").append(id).append("'");
                } else {
                    str.append(",'").append(id).append("'");
                }
            }
            return iOwnerDao.findOwnerByOwnernos(str.toString(), enterpriseid);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo findByDevId(String devId) {
        try {
            return iOwnerDao.findByDevId(devId);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo getByOwner(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            return iOwnerDao.getByOwner(ownerDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> findDevByDevNos(Set<String> devNoSet) {
        try {
            if (devNoSet == null || devNoSet.size() == 0) {
                return new ArrayList<>();
            }

            return iDeviceDao.findDevByDevNos(devNoSet);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<String> checkOwnerNo(Set<String> list) {
        try {

            if (list.size() == 0) {
                return new ArrayList<>();
            }

            return iOwnerDao.checkOwnerNo(list);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<String> devNoCheck(Set<String> devNoSet) {
        try {
            StringBuilder devNos = new StringBuilder();
            for (String devNo : devNoSet) {
                if (StringUtils.isNotBlank(devNo)) {
                    devNos.append("'").append(devNo).append("',");
                }
            }

            if (devNos.length() != 0) {
                String devNostr = devNos.toString().substring(0, devNos.length() - 1);
                return iOwnerDao.devNoCheck(devNostr);
            }

            return new ArrayList<>();

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<OwnerVo> getByIds(List<String> ownerids) {
        try {
            if (ownerids == null || ownerids.isEmpty()) {
                return new ArrayList<>();
            }
            return iOwnerDao.getByIds(ownerids);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo checkOwnerIsExist(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            return iOwnerDao.checkOwnerIsExist(ownerDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo getkeyWordOwner(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            List<OwnerVo> list = iOwnerDao.getkeyWordOwner(ownerDto);
            if (list != null && list.size() == 1) {
                return list.get(0);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public OwnerVo getUserNoOwner(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            return iOwnerDao.getUserNoOwner(ownerDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateOwnerAccount(Double alreadypay, String ownerid) {
        try {
            iOwnerDao.updateOwnerAccount(alreadypay, ownerid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }

    }

    @Override
    public void webaddList(List<OwnerBo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        List<OwnerDto> listDto = new ArrayList<>();
        try {
            for (OwnerBo owner : list) {
                owner.setOwnerid(UuidUtils.getUuid());
                OwnerDto dto = BeanUtils.copy(owner, OwnerDto.class);
                dto.setOwnerType(OWNER_TYPE);
                listDto.add(dto);
            }
            iOwnerDao.batchAddOwnerExt(listDto);
            iOwnerDao.batchAdd(listDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public int updateStatusAndAlreadypay(OwnerBo ownerBo) {
        try {
            OwnerDto ownerDto = BeanUtils.copy(ownerBo, OwnerDto.class);
            return iOwnerDao.updateStatusAndAlreadypay(ownerDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<OwnerVo> findBusinessHallOwners(String enterpriseid, List<String> keyWords) {
        try {
            return iOwnerDao.findBusinessHallOwners(enterpriseid,keyWords);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<String> getOwnersByTypeId(String typeid, Integer ownerStatusCreate) {
        try {
            return iOwnerDao.getOwnersByTypeId(typeid, ownerStatusCreate);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
