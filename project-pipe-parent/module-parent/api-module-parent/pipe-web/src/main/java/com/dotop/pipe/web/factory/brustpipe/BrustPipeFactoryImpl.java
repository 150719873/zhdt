package com.dotop.pipe.web.factory.brustpipe;

import com.dotop.pipe.web.api.factory.brustpipe.IBrustPipeFactory;
import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.pipe.api.service.brustpipe.IBrustPipeService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.brustpipe.BrustPipeBo;
import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.pipe.core.form.BrustPipeForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class BrustPipeFactoryImpl implements IBrustPipeFactory {
    private final static Logger logger = LogManager.getLogger(BrustPipeFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IBrustPipeService iBrustPipeService;

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Override
    public BrustPipeVo add(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            String operEid = loginCas.getOperEid();
            String userId = loginCas.getUserId();
            String userBy = loginCas.getUserName();
            BrustPipeBo brustPipeBo = BeanUtils.copyProperties(brustPipeForm,
                    BrustPipeBo.class);
            brustPipeBo.setStatus(PipeConstants.PIPE_UNTREATED_STATUS);
            brustPipeBo.setCurr(new Date());
            brustPipeBo.setUserBy(userBy);
            brustPipeBo.setEnterpriseId(operEid);
            brustPipeBo.setIsDel(RootModel.NOT_DEL);
            return iBrustPipeService.add(brustPipeBo);
        }
        catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public BrustPipeVo get(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        try {
            BrustPipeBo brustPipeBo = BeanUtils.copy(brustPipeForm, BrustPipeBo.class);
            brustPipeBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iBrustPipeService.get(brustPipeBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<BrustPipeVo> page(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        try {
            BrustPipeBo brustPipeBo = BeanUtils.copy(brustPipeForm, BrustPipeBo.class);
            brustPipeBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            Pagination<BrustPipeVo> page = iBrustPipeService.page(brustPipeBo);
            List<BrustPipeVo> data = page.getData();
            for (BrustPipeVo brustPipeVo : data) {
                List<String> deviceIds = brustPipeVo.getDeviceIds();
                if ( deviceIds != null && deviceIds.size() > 0) {
                    DeviceForm deviceForm = new DeviceForm();
                    deviceForm.setDeviceIds(deviceIds);
                    deviceForm.setProductType("pipe");
                    deviceForm.setProductCategory("pipe");
                    deviceForm.setLimit(5000);
                    List<DeviceVo> list = iDeviceFactory.list(deviceForm);
                    brustPipeVo.setDeviceList(list);
                }
            }
            return page;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<BrustPipeVo> list(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        try {
            BrustPipeBo brustPipeBo = BeanUtils.copy(brustPipeForm, BrustPipeBo.class);
            brustPipeBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iBrustPipeService.list(brustPipeBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public BrustPipeVo edit(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        try {
            BrustPipeBo brustPipeBo = BeanUtils.copy(brustPipeForm, BrustPipeBo.class);
            brustPipeBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            brustPipeBo.setUserBy(iAuthCasApi.get().getUserName());
            brustPipeBo.setCurr(new Date());
            return iBrustPipeService.edit(brustPipeBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        try {
            BrustPipeBo brustPipeBo = BeanUtils.copy(brustPipeForm, BrustPipeBo.class);
            brustPipeBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            brustPipeBo.setIsDel(RootModel.DEL);
            return iBrustPipeService.del(brustPipeBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public BrustPipeVo editStatus(BrustPipeForm brustPipeForm) {
        try {
            if (PipeConstants.PIPE_UNTREATED_STATUS.equals(brustPipeForm.getStatus())) {
                brustPipeForm.setStatus(PipeConstants.PIPE_HANDLED_STATUS);
            }
            BrustPipeBo brustPipeBo = BeanUtils.copy(brustPipeForm, BrustPipeBo.class);
            brustPipeBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iBrustPipeService.editStatus(brustPipeBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
