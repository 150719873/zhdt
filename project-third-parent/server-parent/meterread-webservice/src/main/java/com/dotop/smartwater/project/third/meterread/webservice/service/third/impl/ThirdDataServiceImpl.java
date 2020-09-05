package com.dotop.smartwater.project.third.meterread.webservice.service.third.impl;

import com.dotop.smartwater.project.third.meterread.webservice.dao.third.IThirdDao;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.vo.RemoteDataVo;
import com.dotop.smartwater.project.third.meterread.webservice.service.third.IThirdDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThirdDataServiceImpl implements IThirdDataService {

    @Autowired
    private IThirdDao iThirdDao;

    @Override
    public List<RemoteDataVo> list(List<RemoteDataBo> remoteDataBos, String enterpriseid, Integer factoryId) throws FrameworkRuntimeException {
        try {
            List<String> deviceIds = new ArrayList();
            for (int i = 0; i < remoteDataBos.size(); i++) {
                deviceIds.add(remoteDataBos.get(i).getDeviceId());
            }
            return iThirdDao.getList(deviceIds, enterpriseid, factoryId);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /***
     * 批量添加抄表数据
     * @param remoteDataBos
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public void adds(List<RemoteDataBo> remoteDataBos) throws FrameworkRuntimeException {
        try {
            if (!remoteDataBos.isEmpty()) {
                iThirdDao.inserts(remoteDataBos);
            }
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 批量编辑抄表数据
     *
     * @param remoteDataBos
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public void edits(List<RemoteDataBo> remoteDataBos) throws FrameworkRuntimeException {
        try {
            if (!remoteDataBos.isEmpty()) {
                iThirdDao.updates(remoteDataBos);
            }
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
