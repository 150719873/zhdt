package com.dotop.smartwater.project.module.api.device.impl;

import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IMeterReadingFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.MeterReadingDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.MeterReadingTaskBo;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingDetailForm;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingTaskForm;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.MeterReadingTaskVo;
import com.dotop.smartwater.project.module.service.device.IDeviceBookManagementService;
import com.dotop.smartwater.project.module.service.device.IMeterReadingService;
import com.dotop.water.tool.service.BaseInf;

/**

 * @date 2019/2/22.
 */
@Component
public class MeterReadingFactoryImpl implements IMeterReadingFactory {

    @Autowired
    private IMeterReadingService iMeterReadingService;

    @Autowired
    private IDeviceBookManagementService iDeviceBookManagementService;

    @Override
    public Pagination<MeterReadingTaskVo> page(MeterReadingTaskForm meterReadingTaskForm) {
        UserVo user = AuthCasClient.getUser();

        MeterReadingTaskBo meterReadingTaskBo = new MeterReadingTaskBo();
        BeanUtils.copyProperties(meterReadingTaskForm, meterReadingTaskBo);

        meterReadingTaskBo.setEnterpriseid(user.getEnterpriseid());

        return iMeterReadingService.page(meterReadingTaskBo);
    }

    public Pagination<MeterReadingTaskVo> pageApp(MeterReadingTaskForm meterReadingTaskForm) {
        UserVo user = AuthCasClient.getUser();
        MeterReadingTaskBo meterReadingTaskBo = new MeterReadingTaskBo();
        BeanUtils.copyProperties(meterReadingTaskForm, meterReadingTaskBo);
        meterReadingTaskBo.setEnterpriseid(user.getEnterpriseid());

        // 根据用户ID获取表册信息,再根据表册信息获取可以抄表的区域
        /*DeviceBookManagementBo bo = new DeviceBookManagementBo();
        bo.setBookUserId(user.getUserid());
        bo.setPage(1);
        bo.setPageCount(1000);
        Pagination<DeviceBookManagementVo> books = iDeviceBookManagementService.page(bo);*/
        List<String> areaIds = iDeviceBookManagementService.getAreaIdsByUserid(user.getUserid(),user.getEnterpriseid());

        if (areaIds == null || areaIds.size() == 0) {
            return null;
        }

        return iMeterReadingService.pageApp(meterReadingTaskBo, areaIds);
    }

    @Override
    public MeterReadingTaskVo get(MeterReadingTaskForm meterReadingTaskForm) {
        UserVo user = AuthCasClient.getUser();

        MeterReadingTaskBo meterReadingTaskBo = new MeterReadingTaskBo();
        BeanUtils.copyProperties(meterReadingTaskForm, meterReadingTaskBo);

        meterReadingTaskBo.setEnterpriseid(user.getEnterpriseid());
        return iMeterReadingService.get(meterReadingTaskBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public MeterReadingTaskVo edit(MeterReadingTaskForm meterReadingTaskForm) {
        UserVo user = AuthCasClient.getUser();
        String userBy = user.getName();
        Date curr = new Date();

        MeterReadingTaskBo meterReadingTaskBo = new MeterReadingTaskBo();
        BeanUtils.copyProperties(meterReadingTaskForm, meterReadingTaskBo);

        if (StringUtils.isEmpty(meterReadingTaskForm.getId())) {
            meterReadingTaskBo.setId(UuidUtils.getUuid());
            meterReadingTaskBo.setCreateBy(userBy);
            meterReadingTaskBo.setCreateDate(curr);
        }

        meterReadingTaskBo.setEnterpriseid(user.getEnterpriseid());

        meterReadingTaskBo.setLastBy(userBy);
        meterReadingTaskBo.setLastDate(curr);

        //结束不改变详情状态
        /*if (meterReadingTaskBo.getStatus() != null && WaterConstants.METER_CLOSED == meterReadingTaskBo.getStatus()) {
            // 同步更新抄表详情状态
            MeterReadingDetailBo meterReadingDetailBo = new MeterReadingDetailBo();
            meterReadingDetailBo.setBatchId(meterReadingTaskBo.getBatchId());
            //meterReadingDetailBo.setStatus(meterReadingTaskBo.getStatus());
            meterReadingDetailBo.setLastBy(userBy);
            meterReadingDetailBo.setLastDate(curr);
            iMeterReadingService.editDetails(meterReadingDetailBo);
        }*/

        return iMeterReadingService.edit(meterReadingTaskBo);
    }

    @Override
    public Pagination<MeterReadingDetailVo> detailPage(MeterReadingDetailForm meterReadingDetailForm) {
        UserVo user = AuthCasClient.getUser();

        MeterReadingDetailBo meterReadingDetailBo = new MeterReadingDetailBo();
        BeanUtils.copyProperties(meterReadingDetailForm, meterReadingDetailBo);

        meterReadingDetailBo.setEnterpriseid(user.getEnterpriseid());
        return iMeterReadingService.detailPage(meterReadingDetailBo);
    }

    @Override
    public MeterReadingDetailVo deviceDetail(MeterReadingDetailForm meterReadingDetailForm) {
        UserVo user = AuthCasClient.getUser();
        MeterReadingDetailBo meterReadingDetailBo = new MeterReadingDetailBo();
        BeanUtils.copyProperties(meterReadingDetailForm, meterReadingDetailBo);
        meterReadingDetailBo.setEnterpriseid(user.getEnterpriseid());
        return iMeterReadingService.deviceDetail(meterReadingDetailBo);
    }

    @Override
    public boolean submitMeter(MeterReadingDetailForm meterReadingDetailForm) {
        UserVo user = AuthCasClient.getUser();
        MeterReadingDetailBo meterReadingDetailBo = new MeterReadingDetailBo();
        BeanUtils.copyProperties(meterReadingDetailForm, meterReadingDetailBo);
        meterReadingDetailBo.setEnterpriseid(user.getEnterpriseid());
        meterReadingDetailBo.setMeterReader(user.getName());
        meterReadingDetailBo.setLastBy(user.getName());
        meterReadingDetailBo.setReadTime(new Date());
        meterReadingDetailBo.setLastDate(new Date());
        meterReadingDetailBo.setStatus(1);
        return iMeterReadingService.submitMeter(meterReadingDetailBo) > 0 ? true : false;
    }

    @Override
    public List<AreaNodeVo> getTaskArea(MeterReadingTaskForm meterReadingTaskForm) {
        UserVo user = AuthCasClient.getUser();
        Map<String, AreaNodeVo> areaMap = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());

        MeterReadingTaskBo meterReadingTaskBo = new MeterReadingTaskBo();
        BeanUtils.copyProperties(meterReadingTaskForm, meterReadingTaskBo);

        List<String> areaIds = iMeterReadingService.getTaskAreaIds(meterReadingTaskBo);

        List<AreaNodeVo> areaList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(areaMap) && !CollectionUtils.isEmpty(areaIds)) {
            for (String areaId : areaIds) {
                AreaNodeVo areaNodeVo = areaMap.get(areaId);
                if (areaNodeVo != null && !areaList.contains(areaNodeVo)) {
                    areaList.add(areaNodeVo);
                }
            }
        }
        return areaList;
    }

    @Override
    public List<String> getMeterReaders(MeterReadingTaskForm meterReadingTaskForm) {
        UserVo user = AuthCasClient.getUser();
        MeterReadingTaskBo meterReadingTaskBo = new MeterReadingTaskBo();
        BeanUtils.copyProperties(meterReadingTaskForm, meterReadingTaskBo);
        MeterReadingTaskVo vo = iMeterReadingService.get(meterReadingTaskBo);

        String[] areas = vo.getAreaIds().split(",");
        if (areas.length == 0) {
            return null;
        }
        List<String> readers = iDeviceBookManagementService.findReadersbyAreas(Arrays.asList(areas),user.getEnterpriseid());
		/*if (vo != null) {

			for (String areaid : areas) {
				DeviceBookManagementBo bo = new DeviceBookManagementBo();
				bo.setAreaId(areaid);
				bo.setPage(1);
				bo.setPageCount(1000);
				Pagination<DeviceBookManagementVo> books = iDeviceBookManagementService.page(bo);
				for (DeviceBookManagementVo book : books.getData()) {
					readers.add(book.getUserName());
				}
			}
		}*/
        return readers;
    }

    @Override
    public boolean batchAdd(List<MeterReadingDetailForm> details) {
        List<MeterReadingDetailBo> list = new ArrayList<>();
        for (MeterReadingDetailForm form : details) {
            MeterReadingDetailBo bo = new MeterReadingDetailBo();
            BeanUtils.copyProperties(form, bo);
            list.add(bo);
        }
        return iMeterReadingService.batchAdd(list);
    }
}
