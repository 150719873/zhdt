package com.dotop.smartwater.project.third.server.meterread.client2.service.impl;

import com.dotop.smartwater.project.third.server.meterread.client2.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.server.meterread.client2.core.third.vo.RemoteDataVo;
import com.dotop.smartwater.project.third.server.meterread.client2.dao.third.IThirdDao;
import com.dotop.smartwater.project.third.server.meterread.client2.service.IThirdDataService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
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
    public List<RemoteDataVo> list(List<RemoteDataBo> remoteDataBos) throws FrameworkRuntimeException {
        try {
            List<String> userCodes = new ArrayList<>();
            for (int i = 0; i < remoteDataBos.size(); i++) {
                userCodes.add(remoteDataBos.get(i).getUserCode());
            }
            return iThirdDao.getList(userCodes);
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
