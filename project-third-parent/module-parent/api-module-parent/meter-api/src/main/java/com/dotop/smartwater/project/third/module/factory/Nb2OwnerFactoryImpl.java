package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterOwnerFactory;
import com.dotop.smartwater.project.third.module.api.factory.INb2OwnerFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterOwnerService;
import com.dotop.smartwater.project.third.module.client.IWaterClientService;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.UserSynForm;
import com.dotop.smartwater.project.third.module.core.third.nb2.vo.UserSynVo;
import com.dotop.smartwater.project.third.module.core.utils.Nb2Utils;
import com.dotop.smartwater.project.third.module.core.utils.SaltUtils;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.third.module.core.water.bo.OwnerChangeBo;
import com.dotop.smartwater.project.third.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.dotop.smartwater.project.third.module.core.constants.DockingConstants.REMOTE_SUCCESS;

/**
 *
 */
@Component
public class Nb2OwnerFactoryImpl implements INb2OwnerFactory {

    private final static Logger LOGGER = LogManager.getLogger(Nb2OwnerFactoryImpl.class);

    @Autowired
    private IMeterDockingFactory iMeterDockingFactory;
    @Autowired
    private IWaterDeviceService iWaterDeviceService;
    @Autowired
    private IMeterOwnerFactory iMeterOwnerFactory;
    @Autowired
    private IMeterDeviceService iMeterDeviceService;
    @Autowired
    private IWaterOwnerService iWaterOwnerService;
    @Autowired
    private IWaterClientService iWaterClientService;

    /**
     * 新增或者更新业主数据
     * @param userSynForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public UserSynVo addOwner(UserSynForm userSynForm) throws FrameworkRuntimeException {
        UserSynVo userSynVo = BeanUtils.copy(userSynForm, UserSynVo.class);
        try {
            DockingVo dockingVo = iMeterDockingFactory.get(Nb2Utils.getUserInfo(userSynForm));
            // 对比中间库设备是否存在
            DeviceBo deviceBo = new DeviceBo();
            deviceBo.setEnterpriseid(dockingVo.getEnterpriseid());
            deviceBo.setDevno(userSynForm.getMeterno());
            DeviceVo deviceVo = iMeterDeviceService.get(deviceBo);
            DeviceVo wDeviceVo = iWaterDeviceService.get(deviceBo);
            if (deviceVo == null) {
                //同步设备
                deviceVo = wDeviceVo;
                // TODO 若deviceVo为空，则水务库数据出现错误
                DeviceBo sync = BeanUtils.copy(deviceVo, DeviceBo.class);
                sync.setId(UuidUtils.getUuid());
                List<DeviceBo> deviceBos = new ArrayList<>();
                deviceBos.add(sync);
                iMeterDeviceService.adds(deviceBos);
            } else if (wDeviceVo != null &&
                    (!wDeviceVo.getDevid().equals(deviceVo.getDevid())
                            || !wDeviceVo.getDeveui().equals(deviceVo.getDeveui()))) {
                deviceVo.setDevid(wDeviceVo.getDevid());
                deviceVo.setDeveui(wDeviceVo.getDeveui());
                DeviceBo sync = BeanUtils.copy(deviceVo, DeviceBo.class);
                List<DeviceBo> deviceBos = new ArrayList<>();
                deviceBos.add(sync);
                iMeterDeviceService.edits(deviceBos);
            }
            //同步将要绑定该水表的业主信息
            OwnerForm ownerForm = new OwnerForm();
            ownerForm.setEnterpriseid(dockingVo.getEnterpriseid());
            ownerForm.setUserno(userSynForm.getUserid());
            OwnerVo ownerVo = iMeterOwnerFactory.get(ownerForm);
            OwnerVo wOwnerVo = iWaterOwnerService.get(BeanUtils.copy(ownerForm, OwnerBo.class));
            if (ownerVo == null) {
                //同步将要绑定该水表的业主信息
                ownerVo = wOwnerVo;
                if (ownerVo != null) {
                    OwnerForm sync = BeanUtils.copy(ownerVo, OwnerForm.class);
                    sync.setId(UuidUtils.getUuid());
                    List<OwnerForm> ownerForms = new ArrayList<>();
                    ownerForms.add(sync);
                    iMeterOwnerFactory.adds(ownerForms);
                }
            }
            //同步当前该水表的绑定者
            OwnerForm search = new OwnerForm();
            search.setEnterpriseid(dockingVo.getEnterpriseid());
            search.setDevid(deviceVo.getDevid());
            OwnerVo oldOwnerVo = iMeterOwnerFactory.get(search);
            OwnerVo wOldOwnerVo = iWaterOwnerService.get(BeanUtils.copy(search, OwnerBo.class));
            //如果当前水表不属于将要拥有的用户
            if(ownerVo != null && !deviceVo.getDevid().equals(ownerVo.getDevid())) {
                if (oldOwnerVo == null) {
                    //同步当前拥有该水表的业主信息
                    oldOwnerVo = wOldOwnerVo;
                    if (oldOwnerVo != null) {
                        //如果不是同一个用户
                        if (!oldOwnerVo.getUserno().equals(ownerVo.getUserno())) {
                            OwnerForm sync = BeanUtils.copy(oldOwnerVo, OwnerForm.class);
                            sync.setId(UuidUtils.getUuid());
                            List<OwnerForm> ownerForms = new ArrayList<>();
                            ownerForms.add(sync);
                            iMeterOwnerFactory.adds(ownerForms);
                        }
                    }
                }
            }
            //如果当前水表绑定者与将要绑定者不同，则抛出异常
            if (oldOwnerVo != null && !oldOwnerVo.getUserno().equals(userSynForm.getUserid())) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "需要解绑该水表后才能重新绑定");
            }
//            //如果将要绑定的业主已绑定了其他表
//            if (ownerVo != null && !ownerVo.getDevid().equals(deviceVo.getDevid())) {
//                throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "该用户已绑定了其他的表");
//            }
            //需要新增的业主
            List<OwnerForm> addOwnerForms = new ArrayList<>();
            //需要换表的业主
            List<OwnerChangeBo> ownerChangeBos = new ArrayList<>();
            if (ownerVo == null) {
                OwnerForm addOwnerForm = Nb2Utils.userSynToOwner(userSynForm);
                addOwnerForm.setEnterpriseid(dockingVo.getEnterpriseid());
                addOwnerForm.setId(UuidUtils.getUuid());
                addOwnerForm.setOwnerid(UuidUtils.getUuid());
                addOwnerForm.setDevid(deviceVo.getDevid());
                addOwnerForm.setJson(JSONUtils.toJSONString(userSynForm));
                addOwnerForms.add(addOwnerForm);
                iMeterOwnerFactory.adds(addOwnerForms);
                //执行换表操作
                OwnerChangeBo ownerChangeBo = new OwnerChangeBo();
                ownerChangeBo.setOwnerid(addOwnerForm.getOwnerid());
                ownerChangeBo.setDevid(addOwnerForm.getDevid());
                ownerChangeBo.setOldDevid(addOwnerForm.getDevid());
                ownerChangeBos.add(ownerChangeBo);

                userSynVo.setType(REMOTE_SUCCESS);
            } else {
                OwnerForm synForm = Nb2Utils.userSynToOwner(userSynForm);
                String synSalt = SaltUtils.getOwnerSalt(synForm.getUserno(), synForm.getUsername(), synForm.getUseraddr(), JSONUtils.toJSONString(userSynForm), deviceVo.getDevid());
                String meterSalt = SaltUtils.getOwnerSalt(ownerVo.getUserno(), ownerVo.getUsername(), ownerVo.getUseraddr(), ownerVo.getJson(), ownerVo.getDevid());
                List<OwnerForm> ownerForms = new ArrayList<>();
                OwnerForm syn = BeanUtils.copy(ownerVo, OwnerForm.class);
                syn.setUserno(synForm.getUserno());
                syn.setUsername(synForm.getUsername());
                syn.setUseraddr(synForm.getUseraddr());
                syn.setJson(JSONUtils.toJSONString(userSynForm));
                syn.setDevid(deviceVo.getDevid());
                ownerForms.add(syn);
                if (!synSalt.equals(meterSalt)) {
                    iMeterOwnerFactory.edits(ownerForms, dockingVo.getEnterpriseid());
                    if (wOwnerVo != null) {
                        if (wOwnerVo.getDevid().equals(syn.getDevid())) {
                            iWaterClientService.ownerEdits(BeanUtils.copy(ownerForms, OwnerBo.class), BeanUtils.copy(dockingVo, DockingBo.class));
                        } else {
                            OwnerChangeBo ownerChangeBo = new OwnerChangeBo();
                            ownerChangeBo.setOwnerid(syn.getOwnerid());
                            ownerChangeBo.setDevid(syn.getDevid());
                            ownerChangeBo.setOldDevid(wOwnerVo.getDevid());
                            ownerChangeBos.add(ownerChangeBo);
                        }
                    }
                }
                if (wOwnerVo == null) {
                    addOwnerForms.addAll(ownerForms);
                }
                userSynVo.setType(REMOTE_SUCCESS);
            }
            if (!addOwnerForms.isEmpty()) {
                iWaterClientService.ownerAdds(BeanUtils.copy(addOwnerForms, OwnerBo.class), BeanUtils.copy(dockingVo, DockingBo.class));
            }
            if (!ownerChangeBos.isEmpty()) {
                iWaterClientService.ownerChanges(ownerChangeBos, BeanUtils.copy(dockingVo, DockingBo.class));
            }
            return userSynVo;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "userSynForm:", userSynForm));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e.getMessage(), e);
        }
    }
}
