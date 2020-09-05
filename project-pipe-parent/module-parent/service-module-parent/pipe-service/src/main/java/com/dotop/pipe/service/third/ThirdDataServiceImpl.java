package com.dotop.pipe.service.third;

import com.dotop.pipe.api.dao.third.IThirdDataDao;
import com.dotop.pipe.api.service.third.IThirdDataService;
import com.dotop.pipe.core.bo.third.ThirdDataBo;
import com.dotop.pipe.core.dto.third.ThirdDataDto;
import com.dotop.pipe.core.vo.third.ThirdDataVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 */
@Service
public class ThirdDataServiceImpl implements IThirdDataService {

    private final static Logger logger = LogManager.getLogger(ThirdDataServiceImpl.class);

    @Autowired
    private IThirdDataDao iThirdDataDao;

    @Override
    public List<ThirdDataVo> list(ThirdDataBo thirdDataBo) throws FrameworkRuntimeException {
        ThirdDataDto thirdDataDto = BeanUtils.copy(thirdDataBo, ThirdDataDto.class);
        thirdDataDto.setIsDel(RootModel.NOT_DEL);
        return iThirdDataDao.list(thirdDataDto);
    }

    @Override
    public ThirdDataVo get(String enterpriseId, String deviceId, String deviceCode) throws FrameworkRuntimeException {
        ThirdDataDto thirdDataDto = new ThirdDataDto();
        thirdDataDto.setEnterpriseId(enterpriseId);
        thirdDataDto.setDeviceId(deviceId);
        thirdDataDto.setDeviceCode(deviceCode);
        thirdDataDto.setIsDel(RootModel.NOT_DEL);
        return iThirdDataDao.get(thirdDataDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ThirdDataVo add(ThirdDataBo thirdDataBo) throws FrameworkRuntimeException {
        ThirdDataDto thirdDataDto = BeanUtils.copy(thirdDataBo, ThirdDataDto.class);
        thirdDataDto.setIsDel(RootModel.NOT_DEL);
        thirdDataDto.setCurr(new Date());
        thirdDataDto.setUserBy("system");
        if (StringUtils.isBlank(thirdDataDto.getId())) {
            thirdDataDto.setId(UuidUtils.getUuid());
        }
        iThirdDataDao.add(thirdDataDto);
        return BeanUtils.copy(thirdDataDto, ThirdDataVo.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public ThirdDataVo edit(ThirdDataBo thirdDataBo) throws FrameworkRuntimeException {
        ThirdDataDto thirdDataDto = BeanUtils.copy(thirdDataBo, ThirdDataDto.class);
        thirdDataDto.setIsDel(RootModel.NOT_DEL);
        thirdDataDto.setCurr(new Date());
        thirdDataDto.setUserBy("system");
        if (iThirdDataDao.edit(thirdDataDto) != 1) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "更新中间库错误");
        }
        return BeanUtils.copy(thirdDataDto, ThirdDataVo.class);
    }
}
