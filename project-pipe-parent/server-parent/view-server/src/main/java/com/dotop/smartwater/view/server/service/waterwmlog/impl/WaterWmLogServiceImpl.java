package com.dotop.smartwater.view.server.service.waterwmlog.impl;

import com.dotop.smartwater.view.server.core.device.form.WaterWmLogForm;
import com.dotop.smartwater.view.server.core.device.vo.WaterWmLogVo;
import com.dotop.smartwater.view.server.dao.pipe.waterwmlog.IWaterWmLogDao;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.service.waterwmlog.IWaterWmLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WaterWmLogServiceImpl implements IWaterWmLogService {

    @Autowired
    private IWaterWmLogDao iWaterWmLogDao;

    @Override
    public void adds(List<WaterWmLogForm> waterWmLogForms) throws FrameworkRuntimeException {
        Integer count = iWaterWmLogDao.adds(waterWmLogForms);
    }
    
    @Override
    public Pagination<WaterWmLogVo> page(WaterWmLogForm waterWmLogForm) throws FrameworkRuntimeException {
        Page<Object> pageHelper = PageHelper.startPage(waterWmLogForm.getPage(), waterWmLogForm.getPageSize());
        List<WaterWmLogVo> list = iWaterWmLogDao.list(waterWmLogForm);
        Pagination<WaterWmLogVo> pagination = new Pagination<>(waterWmLogForm.getPageSize(), waterWmLogForm.getPage());
        pagination.setData(list);
        pagination.setTotalPageSize(pageHelper.getTotal());
        return pagination;
    }
}
