package com.dotop.smartwater.project.third.meterread.client.service.impl;

import com.dotop.smartwater.project.third.meterread.client.core.third.bo.RemoteValveBo;
import com.dotop.smartwater.project.third.meterread.client.core.third.vo.RemoteValveVo;
import com.dotop.smartwater.project.third.meterread.client.dao.third.IThirdDao;
import com.dotop.smartwater.project.third.meterread.client.service.IThirdCommandService;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdCommandServiceImpl implements IThirdCommandService {

    @Autowired
    IThirdDao iThirdDao;

    /**
     * 过滤判断IfProcess是否存在没处理的开关阀控制
     *
     * @param remoteValveBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<RemoteValveVo> list(RemoteValveBo remoteValveBo) throws FrameworkRuntimeException{
        try {
            return iThirdDao.getRemoteValveList(remoteValveBo);
        }catch (DataAccessException e){
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 下发成功后【成聪库】记录返回的device_downlink的clientid到扩展字段，【成聪库】的IfProcess修改为【正在处理】和处理时间等等
     *
     * @param remoteValveBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public void editRemoteValve(RemoteValveBo remoteValveBo) throws FrameworkRuntimeException{
        try {
            iThirdDao.editRemoteValve(remoteValveBo);
        }catch (DataAccessException e){
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    /**
     * 根据查询【成聪库】的device_downlink的clientid，并查询【水务库】的status结果。如果结果为【成功】或【失败】，则更新【成聪库】的IfProcess修改为【已处理】等等；如果没有结果则不处理，并将执行次数增加1，如果执行超过【3次】，则标记为【失败】，其他的等待下一次轮询
     *
     * @param remoteValveBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public void editResult(RemoteValveBo remoteValveBo) throws FrameworkRuntimeException{
        try {
            iThirdDao.editRemoteValve(remoteValveBo);
        }catch (DataAccessException e){
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
