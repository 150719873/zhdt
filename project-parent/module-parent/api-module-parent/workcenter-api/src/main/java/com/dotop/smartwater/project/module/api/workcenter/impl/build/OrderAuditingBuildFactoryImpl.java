package com.dotop.smartwater.project.module.api.workcenter.impl.build;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.workcenter.IWorkCenterBuildFactory;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.customize.WorkCenterBuildBo;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**

 */
@Component("OrderAuditingBuildFactoryImpl")
public class OrderAuditingBuildFactoryImpl implements IWorkCenterBuildFactory, IAuthCasClient {


    @Autowired
    private IDictionaryChildService iDictionaryChildService;

    @Autowired
    private AbstractValueCache<WorkCenterBuildBo> avc;


    @Override
    public AbstractValueCache<WorkCenterBuildBo> getCache() throws FrameworkRuntimeException {
        return avc;
    }

    @Override
    public void build(WorkCenterBuildBo buildBo) throws FrameworkRuntimeException {

        System.out.println(JSONUtils.toJSONString(buildBo));
    }
}
