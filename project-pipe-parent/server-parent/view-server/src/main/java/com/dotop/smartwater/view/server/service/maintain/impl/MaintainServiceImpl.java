package com.dotop.smartwater.view.server.service.maintain.impl;

import com.dotop.smartwater.view.server.service.maintain.IMaintainService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.view.server.core.maintain.form.MaintainLogForm;
import com.dotop.smartwater.view.server.core.maintain.vo.MaintainLogVo;
import com.dotop.smartwater.view.server.dao.pipe.maintain.IMaintainDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintainServiceImpl implements IMaintainService {
    private final static Logger logger = LogManager.getLogger(MaintainServiceImpl.class);

    @Autowired
    private IMaintainDao iMaintainDao;


    @Override
    public Pagination<MaintainLogVo> pageMaintain(MaintainLogForm maintainLogForm) throws FrameworkRuntimeException {
        Page<Object> pageHelper = PageHelper.startPage(maintainLogForm.getPage(), maintainLogForm.getPageSize());
        List<MaintainLogVo> list = iMaintainDao.list(maintainLogForm);
        Pagination<MaintainLogVo> pagination = new Pagination<>(maintainLogForm.getPageSize(), maintainLogForm.getPage());
        pagination.setData(list);
        pagination.setTotalPageSize(pageHelper.getTotal());
        return pagination;
    }
}
