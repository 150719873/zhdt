package com.dotop.smartwater.project.third.server.meterread.client3.service.impl;

import com.dotop.smartwater.project.third.server.meterread.client3.service.IThirdCommandService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.server.meterread.client3.core.third.bo.SbDtBo;
import com.dotop.smartwater.project.third.server.meterread.client3.core.third.vo.SbDtVo;
import com.dotop.smartwater.project.third.server.meterread.client3.dao.third.IThirdDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThirdCommandServiceImpl implements IThirdCommandService {

    @Autowired
    IThirdDao iThirdDao;

    /**
     * 过滤判断ProcessFlag是否存在没处理的开关阀控制
     *
     * @param sbDtBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<SbDtVo> list(SbDtBo sbDtBo) throws FrameworkRuntimeException {
        try {
            return iThirdDao.getSbDtList(sbDtBo);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<SbDtVo> list(List<SbDtBo> sbDtBos) throws FrameworkRuntimeException {
        try {
            List<String> userCodes = new ArrayList<>();
            for (int i = 0; i < sbDtBos.size(); i++) {
                userCodes.add(sbDtBos.get(i).getUserCode());
            }
            return iThirdDao.getList(userCodes);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
//        try {
//            String[] meterIds = new String[sbDtBos.size()];
//            for (int i = 0; i < sbDtBos.size(); i++) {
//                meterIds[i] = sbDtBos.get(i).getMeterId();
//            }
//            return iThirdDao.getList(meterIds);
//        } catch (DataAccessException e) {
//            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
//        }
    }

    /**
     * 编辑结果
     *
     * @param sbDtBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public void editSbDt(SbDtBo sbDtBo) throws FrameworkRuntimeException {
        try {
            iThirdDao.editSbDt(sbDtBo);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    /***
     * 批量添加抄表数据
     * @param sbDtBos
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<SbDtBo> sbDtBos) throws FrameworkRuntimeException {
        try {
            if (!sbDtBos.isEmpty()) {
                iThirdDao.inserts(sbDtBos);
            }
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
    /**
     * 批量编辑抄表数据
     *
     * @param sbDtBos
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void edits(List<SbDtBo> sbDtBos) throws FrameworkRuntimeException {
        try {
            if (!sbDtBos.isEmpty()) {
                iThirdDao.updates(sbDtBos);
            }
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
