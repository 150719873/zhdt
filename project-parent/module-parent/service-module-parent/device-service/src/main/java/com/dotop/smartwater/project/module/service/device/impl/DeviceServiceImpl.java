package com.dotop.smartwater.project.module.service.device.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceChangeBo;
import com.dotop.smartwater.project.module.core.water.constants.DeviceCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.dto.DeviceChangeDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceExtDto;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.ExportDeviceVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceChangeDao;
import com.dotop.smartwater.project.module.dao.device.IDeviceDao;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 * @date 2019/2/25.
 */
@Service
public class DeviceServiceImpl implements IDeviceService {

    private static final Logger LOGGER = LogManager.getLogger(DeviceServiceImpl.class);

    @Resource
    private IDeviceDao iDeviceDao;
    
    @Resource
    private IDeviceChangeDao changeDao;

    @Override
    public DeviceVo findById(String id) {
        try {
            return iDeviceDao.findById(id);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceVo getDevById(String id) {
        try {
            return iDeviceDao.getDevById(id);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Long getDeviceCount(String enterpriseid) {
        try {
            return iDeviceDao.getDeviceCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public Long traditionCount(String enterpriseid) {
    	try {
            return iDeviceDao.traditionCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Long remoteCount(String enterpriseid) {
    	try {
            return iDeviceDao.remoteCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public Long offlineDeviceCount(String enterpriseid) {
        try {
            return iDeviceDao.offlineDeviceCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public Long offlineCount(String enterpriseid) {
    	try {
            return iDeviceDao.offlineCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    
    @Override
    public Long noactiveCount(String enterpriseid) {
        try {
            return iDeviceDao.noactiveCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public Long storageCount(String enterpriseid) {
        try {
            return iDeviceDao.storageCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public Long scrapCount(String enterpriseid) {
        try {
            return iDeviceDao.scrapCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public Long unreportedsCount(String enterpriseid) {
    	try {
    		return iDeviceDao.unreportedsCount(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public int updateBeginValue(String id, String beginvalue) {
        try {
            return iDeviceDao.updateBeginValue(id, beginvalue);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceVo findByDevEUI(String devEui) {
        try {
            return iDeviceDao.findByDevEUI(devEui);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    public DeviceVo getkeyWordDevice(DeviceBo deviceBo) {
        try {
            DeviceDto dto = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(deviceBo, DeviceDto.class);
            List<DeviceVo> list = iDeviceDao.getkeyWordDevice(dto);
            if (list != null && list.size() == 1) {
                return list.get(0);
            } else {
                if(list.size() == 0){
                    return null;
                }
                throw new FrameworkRuntimeException(ResultCode.Fail, "根据条件无法检索到唯一设备");
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    
    public DeviceVo findByNfcTagDev(DeviceBo deviceBo) {
    	try {
            DeviceDto dto = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(deviceBo, DeviceDto.class);
            return iDeviceDao.findByNfcTagDev(dto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void update(DeviceBo deviceBo) {
        try {
            updateDeviceWaterV2(deviceBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateStatus(String devid, Integer status, String explain) {
        try {
            iDeviceDao.updateStatus(devid, status, explain);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateBatchDeviceStatus(List<DeviceVo> devices) {
        try {
            iDeviceDao.updateBatchDeviceStatus(devices);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> findByCommunity(String id) {
        try {
            return iDeviceDao.findByCommunity(id);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceVo findByDevNo(String devno) {
        try {
            return iDeviceDao.findByDevNo(devno);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void deleteById(String devid) {
        try {
            iDeviceDao.deleteById(devid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    

    @Override
    public List<DeviceVo> findAll() {
        try {
            return iDeviceDao.findAll();
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public DeviceVo add(DeviceBo deviceBo) {
        try {
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            if(deviceDto.getDevid() == null || "".equals(deviceDto.getDevid())){
                deviceDto.setDevid(UuidUtils.getUuid());
            }
            iDeviceDao.add(deviceDto);
            
            // 保存关联信息
            DeviceExtDto extDto = new DeviceExtDto();
            BeanUtils.copyProperties(deviceDto, extDto);
            iDeviceDao.addDeviceExt(extDto);
            
            DeviceVo deviceVo = new DeviceVo();
            BeanUtils.copyProperties(deviceDto, deviceVo);
            return deviceVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    
    public int insertDeviceChangeRecord(DeviceChangeBo bo) {
    	try {
            // 参数转换
    		DeviceChangeDto dto = new DeviceChangeDto();
            BeanUtils.copyProperties(bo, dto);
            dto.setId(UuidUtils.getUuid());
            return changeDao.insertDeviceChangeRecord(dto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    
    public Pagination<DeviceChangeVo> replacePage(DeviceChangeBo bo) {
    	try {
            // 操作数据
    		DeviceChangeDto dto = new DeviceChangeDto();
            BeanUtils.copyProperties(bo, dto);

            Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
            List<DeviceChangeVo> list = changeDao.getList(dto);
            // 拼接数据返回
            return new Pagination<>(bo.getPage(), bo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    
    public int batchAdd(List<DeviceBo> list) {
    	try {
            // 参数转换
            List<DeviceDto> devices = new ArrayList<DeviceDto>();
            List<DeviceExtDto> exts = new ArrayList<DeviceExtDto>();
            for(DeviceBo bo : list) {
            	DeviceDto deviceDto = new DeviceDto();
            	BeanUtils.copyProperties(bo, deviceDto);
            	devices.add(deviceDto);
            	
            	DeviceExtDto extDto = new DeviceExtDto();
                BeanUtils.copyProperties(deviceDto, extDto);
                exts.add(extDto);
            }
            iDeviceDao.batchAdd(devices);
            iDeviceDao.batchAddExt(exts);
            return 0; 
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    

    @Override
    public List<DeviceVo> getDevices(String enterpriseid) {
        try {
            return iDeviceDao.getDevices(enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public int unband(DeviceBo deviceBo) {
        try {
            // 参数转换
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            return iDeviceDao.unband(deviceDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateByPrimaryKeySelective(DeviceBo deviceBo) {
        updateDeviceWaterV2(deviceBo);
    }

    @Override
    public Pagination<DeviceVo> findBypage(DeviceBo deviceBo) {
        try {
            // 操作数据
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            
            //如果排序数据为空，则将其过滤
            if(deviceDto.getSortList() != null && deviceDto.getSortList().isEmpty()) {
            	deviceDto.setSortList(null);
            }
            Page<Object> pageHelper = PageHelper.startPage(deviceBo.getPage(), deviceBo.getPageCount());
            List<DeviceVo> list = iDeviceDao.findBypage(deviceDto);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            for (DeviceVo vo : list) {
            	if (!StringUtils.isEmpty(vo.getUplinktime())) {
            		try {
						if (sf.parse(vo.getUplinktime()).getTime() < sf.parse(sf.format(new Date())).getTime()) {
							vo.setUplinkStatus("0");	
						} else {
							vo.setUplinkStatus("1");
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
            	} else {
            		vo.setUplinkStatus("0");
            	}
            }
            
            // 拼接数据返回
            return new Pagination<>(deviceBo.getPage(), deviceBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> list(DeviceBo deviceBo) {
        try {
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            return iDeviceDao.list(deviceDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<ExportDeviceVo> getExportDeviceList(DeviceBo deviceBo) {
        try {
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            return iDeviceDao.getExportDeviceList(deviceDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 当实表数据更新时,更新所有父级虚表的值 (事务级别的处理)
     *
     * @param deviceBo
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateDeviceWaterV2(DeviceBo deviceBo) {
        try {
            DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);

            DeviceVo deviceVo = iDeviceDao.findById(deviceBo.getDevid());
            Double waterAdd = 0.0D;
            if (deviceBo.getWater() != null && deviceVo.getWater() != null) {
                // 计算增加的用水量
                LOGGER.info(LogMsg.to("msg", "更新上级水表读数", "water0", deviceBo.getWater(), "water1", deviceVo.getWater()));
                waterAdd = CalUtil.sub(deviceBo.getWater(), deviceVo.getWater());
            }

            // 先更新自己水表
            iDeviceDao.updateByPrimaryKeySelective(deviceDto);

            // 更新附属表
            DeviceExtDto extDto = new DeviceExtDto();
            BeanUtils.copyProperties(deviceDto, extDto);
            iDeviceDao.updateByPrimaryKeyExt(extDto);
            
            // 读数有变化才这样做 // 不是总表要更新上级虚表的读数（一级一级递归）
            if (waterAdd > 0.0D && !DeviceCode.DEVICE_PARENT.equals(deviceVo.getPid())) {
                deviceVo = iDeviceDao.getDevById(deviceVo.getPid());
                while (deviceVo != null && !DeviceCode.DEVICE_PARENT.equals(deviceVo.getPid())
                        && DeviceCode.DEVICE_KIND_FAKE.equals(deviceVo.getKind())) {
                    deviceVo.setWater(CalUtil.add(deviceVo.getWater(), waterAdd));
                    BeanUtils.copyProperties(deviceVo, deviceDto);
                    iDeviceDao.updateByPrimaryKeySelective(deviceDto);
                    deviceVo = iDeviceDao.getDevById(deviceVo.getPid());
                }

                // 总表是虚表也要更新读数
                if (deviceVo != null && DeviceCode.DEVICE_PARENT.equals(deviceVo.getPid())
                        && DeviceCode.DEVICE_KIND_FAKE.equals(deviceVo.getKind())) {
                    deviceVo.setWater(CalUtil.add(deviceVo.getWater(), waterAdd));
                    BeanUtils.copyProperties(deviceVo, deviceDto);
                    iDeviceDao.updateByPrimaryKeySelective(deviceDto);
                }
            }

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> findChildrenById(String devid, String enterpriseid) {
        try {
            DeviceDto deviceDto = new DeviceDto();
            deviceDto.setPid(devid);
            deviceDto.setEnterpriseid(enterpriseid);
            return iDeviceDao.list(deviceDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Long getDeviceCountByAreaIds(List<String> areaIds) {
        try {
            return iDeviceDao.getDeviceCountByAreaIds(areaIds);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<DeviceVo> findByOrderPreviewList(List<OrderPreviewVo> orders) {
        try {
            if(orders!=null&&orders.size()>0){
                return iDeviceDao.findByOrderPreviewList(orders);
            }
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    
    @Override
    public String updateByThird(DeviceBo deviceBo) {
        try {
        	DeviceDto deviceDto = new DeviceDto();
            BeanUtils.copyProperties(deviceBo, deviceDto);
            iDeviceDao.update(deviceDto);
            return deviceBo.getDevid();
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
