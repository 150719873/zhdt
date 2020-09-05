package com.dotop.pipe.web.factory.water;

import com.dotop.pipe.api.client.IWaterDeviceClient;
import com.dotop.pipe.api.client.core.WaterDeviceForm;
import com.dotop.pipe.api.client.core.WaterDeviceVo;
import com.dotop.pipe.web.api.factory.water.IWaterDeviceFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterDeviceFactoryImpl implements IWaterDeviceFactory {

    private final static Logger logger = LogManager.getLogger(WaterDeviceFactoryImpl.class);

    @Autowired
    private IWaterDeviceClient iWaterDeviceClient;


    @Override
    public Pagination<WaterDeviceVo> page(WaterDeviceForm waterDeviceForm) throws FrameworkRuntimeException {
        Integer page = waterDeviceForm.getPage();
        Integer pageSize = waterDeviceForm.getPageSize();
        String deveui = waterDeviceForm.getDeveui();
        String devno = waterDeviceForm.getDevno();
        return iWaterDeviceClient.page(page, pageSize, devno, deveui);
    }


}
