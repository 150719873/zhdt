package com.dotop.smartwater.project.module.api.revenue.impl;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.IOrderFactory;
import com.dotop.smartwater.project.module.api.revenue.IOwnerFactory;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.client.third.http.IHttpTransfer;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.constants.*;
import com.dotop.smartwater.project.module.core.water.enums.ErrorType;
import com.dotop.smartwater.project.module.core.water.form.OwnerChangeForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerExtForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LastUpLinkVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.service.tool.IUserLoraService;
import com.dotop.water.tool.service.BaseInf;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**

 */
@Component
public class OwnerFactoryImpl implements IOwnerFactory {

    private static final Logger LOGGER = LogManager.getLogger(OwnerFactoryImpl.class);

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IPayDetailService iPayDetailService;

    @Autowired
    private IWrongAccountService iWrongAccountService;

    @Autowired
    private IPayTypeService iPayTypeService;

    @Autowired
    private IOrderFactory iOrderFactory;

    @Autowired
    private IUserLoraService iUserLoraService;

    @Autowired
    private IPaymentTradeOrderService iPaymentTradeOrderService;

    @Autowired
    private IPaymentTradeOrderExtService iPaymentTradeOrderExtService;
    
    @Autowired
    private AbstractValueCache<String> avc;

    @Autowired
    private IHttpTransfer iHttpTransfer;
    
    public static final String OWNER_ORDINARY_TYPE = "28,100002,2";

    @Override
    public OwnerVo add(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        
        // 校验字段
        ownerBo.setEnterpriseid(user.getEnterpriseid());
        ownerBo.setUserBy(userBy);
        ownerBo.setCurr(curr);
        
        // 验证用户编号的唯一性
        if (iOwnerService.checkByOwnerNo(ownerBo) > 0) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OWNERBIANHAOUSE), "业主编号已被使用");
        }
        
        // 验证条码唯一性
        if (StringUtils.isNotBlank(ownerBo.getBarCode()) && iOwnerService.checkByBarCode(ownerBo) > 0) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OWNERBIANHAOUSE), "条码已被使用");
        }
        
        if (StringUtils.isNotBlank(ownerForm.getDevno())) {
        	OwnerVo owner = iOwnerService.findOwnerByDevNo(ownerBo);
            if (owner != null) {
                throw new FrameworkRuntimeException(ResultCode.DeviceIsUsed, "水表编号已经被使用");
            }

            String devId = iOwnerService.findDevIdByDevNo(ownerBo);
            if (devId == null) {
                throw new FrameworkRuntimeException(ResultCode.DevnoNotExist, "水表编号不存在对应的设备");
            }
            ownerBo.setDevid(devId);
        }
        
        ownerBo.setStatus(WaterConstants.OWNER_STATUS_UNOPRN);
        ownerBo.setAlreadypay(0.0);
        ownerBo.setCreatetime(new Date());
        ownerBo.setCreateuser(user.getUserid());

        OwnerVo ownerVo = iOwnerService.add(ownerBo);
        return ownerVo;
    }

    @Override
    public boolean batchAdd(List<OwnerForm> owners) {
        UserVo user = AuthCasClient.getUser();
        Date curr = new Date();
        List<OwnerBo> list = new ArrayList<OwnerBo>();

        List<AreaNodeVo> areas = BaseInf.getAreaList(user.getUserid(), user.getTicket());
        if (areas == null || areas.isEmpty()) {
            throw new FrameworkRuntimeException(ResultCode.NO_SET_COMMUNITY, ResultCode.getMessage(ResultCode.NO_SET_COMMUNITY));
        }

        //获取顶级节点
        AreaNodeVo areaVo = new AreaNodeVo();
        for (AreaNodeVo area : areas) {
            if (area.getPId() != null && area.getPId().equals("0")) {
                areaVo = area;
                break;
            }
        }

        // 重组封装数据
        for (OwnerForm owner : owners) {
            OwnerBo bo = new OwnerBo();
            BeanUtils.copy(owner, bo);

            // 如果所属区域ID未传，则取顶级区域ID
            if (StringUtils.isBlank(bo.getCommunityid())) {
                bo.setCommunityid(areaVo.getKey());
            }

            if (bo.getOwnerExtForm() == null) {
                bo.setOwnerExtForm(new OwnerExtForm());
            }
            bo.getOwnerExtForm().setOwnerId(bo.getOwnerid());
            bo.getOwnerExtForm().setOwnerType(OWNER_ORDINARY_TYPE);
            bo.setCreatetime(curr);
            bo.setEnterpriseid(user.getEnterpriseid());
            bo.setStatus(WaterConstants.OWNER_STATUS_UNOPRN);
            bo.setAlreadypay(0.0);
            bo.setIschargebacks(0);
            list.add(bo);
        }

        if (!list.isEmpty()) {
            iOwnerService.batchAdd(list);
        }
        return false;
    }

    @Override
    public boolean batchEdit(List<OwnerForm> owners) {
        UserVo user = AuthCasClient.getUser();
        List<OwnerBo> list = new ArrayList<OwnerBo>();

        // 重组封装数据
        for (OwnerForm owner : owners) {
            OwnerBo bo = new OwnerBo();
            BeanUtils.copy(owner, bo);
            bo.setEnterpriseid(user.getEnterpriseid());
            list.add(bo);
        }

        if (!list.isEmpty()) {
            iOwnerService.batchEdit(list);
            return true;
        }
        return false;
    }

    @Override
    public boolean batchChange(List<OwnerChangeForm> changes) {
        UserVo user = AuthCasClient.getUser();
        List<OwnerChangeBo> list = new ArrayList<OwnerChangeBo>();
        for (OwnerChangeForm change : changes) {
            OwnerChangeBo bo = new OwnerChangeBo();
            BeanUtils.copy(change, bo);
            bo.setEnterpriseid(user.getEnterpriseid());
            list.add(bo);
        }

        if (!list.isEmpty()) {
            iOwnerService.batchChange(list);
            return true;
        }

        return false;
    }


    @Override
    public void openOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        OwnerVo owner = iOwnerService.findOwnerByDevNo(ownerBo);
        if (owner != null) {
            throw new FrameworkRuntimeException(ResultCode.DeviceIsUsed, "水表编号已经被使用");
        }

        String devId = iOwnerService.findDevIdByDevNo(ownerBo);
        if (devId == null) {
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist, "水表编号不存在对应的设备");
        }
        ownerBo.setDevid(devId);
        ownerBo.setStatus(WaterConstants.OWNER_STATUS_CREATE);
        DeviceVo device = iOwnerService.findDevByDevNo(ownerBo);
        if (!DeviceCode.DEVICE_PARENT.equals(device.getPid())) {
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist, "该水表不是总表,不能开户");
        }

        // 如果设备水司ID与当前用户水司ID不相同，则重新注册设备
        if (!device.getEnterpriseid().equals(user.getEnterpriseid())) {
            UserLoraVo ul = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
            if (ul == null || StringUtils.isBlank(ul.getAccount())) {
                throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                        device.getDevno() + "未找到当前水司对应IOT账号");
            }

            // 开户时若EnterpriseId不同,先在旧账号IOT解绑,再新绑定新账号下的IOT平台,让IOT与LPC做关联
            String token = avc.get(CacheKey.WaterIotToken + device.getEnterpriseid());
            UserLoraVo ulOld = iUserLoraService.findByEnterpriseId(device.getEnterpriseid());
            if (ulOld == null || StringUtils.isBlank(ulOld.getAccount())) {
                throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                        device.getDevno() + "未找到原水司IOT对应账号");
            }
            
            //IOT账号不相同，则解绑设备后重新注册
            if (!ul.getAccount().equals(ulOld.getAccount())) {
            	// 自己的通讯方式则需要切换IOT
            	if (device.getMode() != null && (device.getMode().equals("28,300001,1") || device.getMode().equals("28,300001,2")
                  		 || device.getMode().equals("28,300001,3") || device.getMode().equals("28,300001,10"))) {
            		
            		UserLoraBo userLoraBo = new UserLoraBo();
                    BeanUtils.copy(ulOld, userLoraBo);
                    
                    if (token == null) {
                        IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                        if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                            Map map1 = (Map) JSON.parse(authResult.getData().toString());
                            token = (String) (map1.get("token"));
                            avc.set(CacheKey.WaterIotToken + device.getEnterpriseid(), token);
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                                    device.getDevno() + "获取旧IOT Token错误");
                        }
                    }

                    DeviceBo deviceBo = new DeviceBo();
                    BeanUtils.copy(device, deviceBo);
                    
                    IotMsgEntityVo iotMsgEntity = null;
                    // Lora表在IOT平台智能调用删除接口，NB表调解绑接口
                    if (device.getMode().equals("28,300001,1")) {
                    	iotMsgEntity = iHttpTransfer.cleanDevice(token, deviceBo, userLoraBo);	
                    } else {
                    	iotMsgEntity = iHttpTransfer.delDevice(token, deviceBo, userLoraBo);
                    }
                    
                    if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                        LOGGER.info("[开户]:{} 从IOT平台删除", device.getName());
                    } else {
                        throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, device.getDevno()
                                + "设备不能从IOT平台删除[开户]:" + iotMsgEntity.getCode());
                    }

                    userLoraBo = new UserLoraBo();
                    BeanUtils.copy(ul, userLoraBo);

                    // 再在IOT绑定
                    String tokenNew = avc.get(CacheKey.WaterIotToken + ul.getEnterpriseid());
                    if (tokenNew == null) {
                        IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                        if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                            Map map1 = (Map) JSON.parse(authResult.getData().toString());
                            tokenNew = (String) (map1.get("token"));
                            avc.set(CacheKey.WaterIotToken + ul.getEnterpriseid(), tokenNew);
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                                    device.getDevno() + "获取新IOT Token错误");
                        }
                    }

                    // 创建id
                    deviceBo.setDevid(UUID.randomUUID().toString());
                    deviceBo.setName("auto"+deviceBo.getDevno());
                    iotMsgEntity = iHttpTransfer.addDevice(tokenNew, deviceBo, userLoraBo);
                    if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                    	LOGGER.info("{} 成功添加进IOT平台[开户导入]", device.getDevno());
                    } else {
                        throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, device.getDevno()
                                + "不能添加进IOT平台[开户导入],错误码:" + iotMsgEntity.getCode());
                    }
            		
            	} 
            	
            }

            // 更新水表公司ID
            device.setEnterpriseid(user.getEnterpriseid());
            DeviceBo deviceBo1 = new DeviceBo();
            BeanUtils.copy(device, deviceBo1);
            
            iDeviceService.updateByPrimaryKeySelective(deviceBo1);
        }
        
        //验证是否将最新读数更新到初始读数中，1表示更新
        if (ownerForm.getIsUpdate() != null && ownerForm.getIsUpdate().equals("1")) {
        	 DeviceBo deviceBo1 = new DeviceBo();
             BeanUtils.copy(device, deviceBo1);
             deviceBo1.setBeginvalue(deviceBo1.getWater());
             iDeviceService.updateByPrimaryKeySelective(deviceBo1);
        }
         
        iOwnerService.openOwner(ownerBo);
    }

    public int createOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        iOwnerService.createAndopenOwner(ownerBo);
        return 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        
        OwnerVo owner = iOwnerService.getOwnerById(ownerBo);
        if (owner == null) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "不存在的业主ID");
        }
        
        ownerBo.setEnterpriseid(user.getEnterpriseid());
        if (iOwnerService.checkByOwnerNo(ownerBo) > 0) {
            throw new FrameworkRuntimeException(ResultCode.OWNERBIANHAOUSE, "业主编号已被使用");
        }
        
        // 验证条码唯一性
        if (StringUtils.isNotBlank(ownerBo.getBarCode()) && iOwnerService.checkByBarCode(ownerBo) > 0) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.OWNERBIANHAOUSE), "条码已被使用");
        }
        
        if (StringUtils.isNotBlank(ownerForm.getDevno())) {
        	OwnerVo ownervo = iOwnerService.findOwnerByDevNo(ownerBo);
            if (ownervo != null && !ownervo.getUserno().equals(ownerForm.getUserno())) {
                throw new FrameworkRuntimeException(ResultCode.DeviceIsUsed, "水表编号已经被使用");
            }

            String devId = iOwnerService.findDevIdByDevNo(ownerBo);
            if (devId == null) {
                throw new FrameworkRuntimeException(ResultCode.DevnoNotExist, "水表编号不存在对应的设备");
            }
            ownerBo.setDevid(devId);
            
            if (StringUtils.isBlank(owner.getDevid()) || (StringUtils.isNotBlank(owner.getDevid()) && !owner.getDevid().equals(devId)) ) {
            	OwnerRecordBo ownerRecord = new OwnerRecordBo();
            	ownerRecord.setEnterpriseid(user.getEnterpriseid());
            	ownerRecord.setCommunityid(ownerBo.getCommunityid());
            	ownerRecord.setType(WaterConstants.ONWER_RECORD_TYPE_NO_OPEN_UPDATE);
            	ownerRecord.setOwnerid(ownerBo.getOwnerid());
            	ownerRecord.setUsername(ownerBo.getUsername());
            	ownerRecord.setOldownerid(ownerBo.getOwnerid());
            	ownerRecord.setOldusername(ownerBo.getUsername());
            	ownerRecord.setOlduserphone(ownerBo.getUserphone());
            	ownerRecord.setDevid(devId);
            	ownerRecord.setDevno(ownerForm.getDevno());
            	
            	ownerRecord.setOlddevid(owner.getDevid());
            	ownerRecord.setOlddevno(owner.getDevno());
            	
            	ownerRecord.setReason("修改水表编号");
            	ownerRecord.setDescr("未开户修改水表编号");
            	ownerRecord.setOperatetime(new Date());
            	ownerRecord.setOperateuser(user.getUserid());
            	ownerRecord.setOperatename(user.getName());
            	iOwnerService.addOwnerRecord(ownerRecord);
            }
        }

        if (!owner.getUsername().equals(ownerForm.getUsername())) {
            OwnerRecordBo ownerRecord = new OwnerRecordBo();
            ownerRecord.setOwnerid(owner.getOwnerid());
            ownerRecord.setEnterpriseid(owner.getEnterpriseid());

            ownerRecord.setType(WaterConstants.OWNER_RECORD_TYPE_OWNERNAME_CHANGE);
            ownerRecord.setUsername(ownerForm.getUsername());
            ownerRecord.setOldusername(owner.getUsername());

            ownerRecord.setOperateuser(user.getUserid());
            ownerRecord.setOperatename(user.getName());
            ownerRecord.setOperatetime(new Date());

            ownerRecord.setReason("-");
            if(owner.getStatus() == 1){
                ownerRecord.setDescr("业主已开户,但名称录入错误,需要修改名称");
            }else{
                ownerRecord.setDescr("业主名称录入错误,需要修改名称");
            }

            iOwnerService.addOwnerRecord(ownerRecord);
        }

        // 根据status判断能修改哪些内容
        ownerBo.setStatus(owner.getStatus());
        iOwnerService.webUpdate(ownerBo);

        // TODO 修改业主操作日志

    }

    @Override
    public void delOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);

        if (owner == null || owner.getStatus() == WaterConstants.OWNER_STATUS_CREATE) {
            throw new FrameworkRuntimeException(ResultCode.NOOPENACCOUNTISDELETE, "只有未开户的业主才可以删除");
        }

        iOwnerService.delete(ownerBo);

        // TODO 删除业主操作日志
    }

    @Override
    public OwnerVo checkOwnerIsExist(OwnerForm ownerForm) throws FrameworkRuntimeException {
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        return iOwnerService.checkOwnerIsExist(ownerBo);
    }

    @Override
    public OwnerVo getUserNoOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());

        OwnerVo owner = iOwnerService.getUserNoOwner(ownerBo);
        if (owner != null) {
            if (map != null && map.size() != 0) {
                AreaNodeVo areaNode = map.get(owner.getCommunityid());
                if (areaNode != null) {
                    owner.setCommunityname(areaNode.getTitle());
                }
            }
        }
        return owner;
    }

    @Override
    public OwnerVo getkeyWordOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        ownerBo.setEnterpriseid(user.getEnterpriseid());
        return iOwnerService.getkeyWordOwner(ownerBo);
    }

    @Override
    public void checkOpen(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);

        if (owner == null || owner.getStatus() == WaterConstants.OWNER_STATUS_UNOPRN) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "业主未开户，不能进行销户！");
        } else if (owner.getStatus() == WaterConstants.OWNER_STATUS_CREATE) {
            // throw new FrameworkRuntimeException(ResultCode.Success, "Success");
        } else if (owner.getStatus() == WaterConstants.OWNER_STATUS_DELETE) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "业主未开户，不能进行销户！");
        }
    }

    @Override
    public void cancelOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);
        if (owner == null) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "不存在的业主ID");
        }

        if (WaterConstants.OWNER_STATUS_CREATE != owner.getStatus()) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "未开户的业主不能销户,只能删除");
        }

        OrderBo order = new OrderBo();
        order.setEnterpriseid(owner.getEnterpriseid());
        order.setUserno(owner.getUserno());
        order.setPaystatus(0);
        List<OrderVo> noPayOrderList = iOrderService.orderList(order);
        if (noPayOrderList.size() > 0) {
            // 判断业主申报错账审批之后账单已经处理完成，可以销户
            WrongAccountBo wrongAccountBo = new WrongAccountBo();
            wrongAccountBo.setOwnerid(ownerForm.getOwnerid());
            List<WrongAccountVo> ownerWrongList = iWrongAccountService.getStatus(wrongAccountBo);
            if (ownerWrongList == null || ownerWrongList.size() == 0) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "业主存在未处理账单，不能销户");

            } else {
                outer:
                for (WrongAccountVo wrongAccount : ownerWrongList) {
                    if (wrongAccount.getStatus().intValue() == WaterConstants.WRONG_ACCOUNT_STATUS_SHENQING
                            || wrongAccount.getStatus().intValue() == WaterConstants.WRONG_ACCOUNT_STATUS_CHULI) {
                        throw new FrameworkRuntimeException(ResultCode.Fail, "业主有错账在申请处理中，不能销户");
                    }
                    for (int i = 0; i < noPayOrderList.size(); i++) {
                        if (noPayOrderList.get(i).getTradeno().equals(wrongAccount.getTradeno())) {
                            continue outer;
                        }
                        if (i == (noPayOrderList.size() - 1)) {
                            throw new FrameworkRuntimeException(ResultCode.Fail, "业主存在未处理账单，不能销户");
                        }
                    }
                }
            }
        }
        // 生成支付记录
        PayDetailBo payDetail = new PayDetailBo();
        payDetail.setOwnerid(owner.getOwnerid());
        payDetail.setOwnerno(owner.getUserno());
        payDetail.setOwnername(owner.getUsername());
        payDetail.setPaymoney(BigDecimal.valueOf(owner.getAlreadypay()));
        payDetail.setCreatetime(new Date());
        payDetail.setCreateuser(user.getUserid());
        payDetail.setUsername(user.getName());
        payDetail.setType(WaterConstants.PAY_DETAIL_TYPE_OUT);
        payDetail.setBeforemoney(BigDecimal.valueOf(owner.getAlreadypay()));
        payDetail.setAftermoney(BigDecimal.valueOf(0.0));
        payDetail.setRemark("销户余额退还");

        iPayDetailService.addPayDetail(payDetail, owner.getAlreadypay());

        // 插入销户记录
        // new OwnerRecordBo(ownerBo);
        OwnerRecordBo ownerRecord = BeanUtils.copy(ownerBo, OwnerRecordBo.class);
        ownerRecord.setType(WaterConstants.OWNER_RECORD_TYPE_CANCEL);
        ownerRecord.setUsername(owner.getUsername());
        ownerRecord.setDevid(owner.getDevid());
        ownerRecord.setDevno(owner.getDevno());
        ownerRecord.setOperateuser(user.getUserid());
        ownerRecord.setOperatename(user.getName());
        ownerRecord.setOperatetime(new Date());
        ownerRecord.setReason(ownerForm.getReason());
        ownerRecord.setDescr(ownerForm.getDescr());
        ownerRecord.setOldalreadypay(owner.getAlreadypay());
        iOwnerService.addOwnerRecord(ownerRecord);

        // 余额清零
        iOwnerService.cancelOwner(ownerBo);

        // TODO 改变水表初始读数,不然缴费会算错 (这方法可以在device那里实现)
        /*
         * Freeze freeze = freezeRecordMapper.findByOwnerid(owner.getOwnerid()); if
         * (freeze != null) { deviceSrvImpl.updateBeginValue(owner.getDevid(),
         * freeze.getWater()); }
         */

        // TODO 销户操作日志

        // TODO 发短信和微信消息 (新增业主)
    }

    @Override
    public void changeDevice(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        // 获取业主信息
        OwnerVo ownerVo = iOwnerService.findByOwnerId(ownerBo);
        if (ownerVo == null) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "不存在的业主ID");
        }
        if (WaterConstants.OWNER_STATUS_DELETE == ownerVo.getStatus()) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "已经销户了,不能换水表");
        }

        OwnerBo queryOwner = new OwnerBo();
        // 先判断新水表水表是否被使用,根据devno
        queryOwner.setDevno(ownerForm.getNewdevno());
        if (iOwnerService.findOwnerByDevNo(queryOwner) != null) {
            throw new FrameworkRuntimeException(ResultCode.DeviceIsUsed, "新水表编号已经被使用");
        }
        // 根据devno获取新水表的devid
        DeviceVo device = iOwnerService.findDevByDevNo(queryOwner);
        if (device == null) {
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist, "新水表编号不存在对应的设备");
        }

        // 新水表是否在本水司，不在则更改在IOT的账号
        UserLoraVo ul = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
        if (ul == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "没在系统设置水司关联的IOT账号");
        }
        // if (device.getEnterpriseid() != ul.getEnterpriseid()) {
        // // 开户时若EnterpriseId不同,先在旧账号IOT解绑,再新绑定新账号下的IOT平台,让IOT与LPC做关联
        // String token = redisSrv.get(CacheKey.WaterIotToken +
        // device.getEnterpriseid());
        // UserLora ulOld = BaseInf.getUserLora(device.getEnterpriseid());
        //
        // if (token == null) {
        // IotMsgEntity authResult = HttpTransfer.getLoginInfo(ulOld);
        // if (authResult != null &&
        // AppCode.IotSucceccCode.equals(authResult.getCode())) {
        // JSONObject data = (JSONObject) authResult.getData();
        // token = (String) (data.get("token"));
        // redisSrv.setTtl(CacheKey.WaterIotToken + device.getEnterpriseid(), token,
        // 1800L);
        // } else {
        // throw new FrameworkRuntimeException(ResultCode.GETTOKENERROR, "获取旧IOT
        // Token错误");
        // }
        // }
        //
        // // 先在IOT解绑
        // IotMsgEntity iotMsgEntity = HttpTransfer.delDevice(token, device, ulOld);
        // if (iotMsgEntity != null &&
        // AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
        // log.info("[开户]:" + device.getName() + "从IOT平台删除");
        // } else {
        // throw new FrameworkRuntimeException(ResultCode.Fail, device.getName() +
        // "设备不能从IOT平台删除[开户]:" + iotMsgEntity.getCode(),
        // null);
        // }
        //
        // // 再在IOT绑定
        // String tokenNew = redisSrv.get(CacheKey.WaterIotToken +
        // ul.getEnterpriseid());
        // if (tokenNew == null) {
        // IotMsgEntity authResult = HttpTransfer.getLoginInfo(ul);
        // if (authResult != null &&
        // AppCode.IotSucceccCode.equals(authResult.getCode())) {
        // JSONObject data = (JSONObject) authResult.getData();
        // tokenNew = (String) (data.get("token"));
        // redisSrv.setTtl(CacheKey.WaterIotToken + ul.getEnterpriseid(), tokenNew,
        // 1800L);
        // } else {
        // throw new FrameworkRuntimeException(ResultCode.GETTOKENERROR, "获取新IOT
        // Token错误");
        // }
        // }
        //
        // iotMsgEntity = HttpTransfer.addDevice(tokenNew, device, ul);
        // if (iotMsgEntity != null &&
        // AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
        // log.info(device.getName() + "成功添加进IOT平台[开户]");
        // } else {
        // throw new FrameworkRuntimeException(ResultCode.DeviceIsUsed,
        // device.getName() + "不能添加进IOT平台[开户],错误码:" + iotMsgEntity.getCode());
        // }
        //
        // device.setEnterpriseid(user.getEnterpriseid());
        //
        // // 更新水表所属公司
        // deviceSrvImpl.updateByPrimaryKeySelective(device);
        // }

        // 获取旧水表数据
        ownerBo.setDevno(ownerVo.getDevno());
        DeviceVo oldDevice = iOwnerService.findDevByDevNo(ownerBo);
        if (oldDevice == null) {
            throw new FrameworkRuntimeException(ResultCode.DevnoNotExist, "旧水表编号不存在对应的设备");
        }

        if (!oldDevice.getDevid().equals(ownerVo.getDevid())) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "旧水表Id错误");
        }

        Double newWater = oldDevice.getWater() == null ? 0.0 : oldDevice.getWater();

        if (newWater < 0.0) {
            throw new FrameworkRuntimeException(ResultCode.OLDWATERMETERRECORDERROR, "旧水表记录错误,请联系管理员");
        }

        OwnerRecordBo ownerRecord = BeanUtils.copy(ownerBo, OwnerRecordBo.class);
        ownerRecord.setType(WaterConstants.OWNER_RECORD_TYPE_CHANGE_DEV);
        ownerRecord.setUsername(ownerVo.getUsername());
        ownerRecord.setDevid(device.getDevid());
        ownerRecord.setDevno(device.getDevno());
        ownerRecord.setOlddevid(oldDevice.getDevid());
        ownerRecord.setOlddevno(oldDevice.getDevno());
        ownerRecord.setOlddevnum(oldDevice.getWater());
        ownerRecord.setOperateuser(user.getUserid());
        ownerRecord.setOperatename(user.getName());
        ownerRecord.setOperatetime(new Date());
        ownerRecord.setReason(ownerForm.getReason());
        ownerRecord.setDescr(ownerForm.getDescr());
        iOwnerService.addOwnerRecord(ownerRecord);

        // 更新业主表devid为新的devid

        ownerBo.setDevid(device.getDevid());
        ownerBo.setEnterpriseid(user.getEnterpriseid());
        iOwnerService.updateDevid(BeanUtils.copy(ownerBo, OwnerVo.class));

        // TODO 操作日志(业主换表)

        // TODO 发短信和微信消息 (业主换表)

    }

    @Override
    public void changeOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        // 获取原业主数据
        OwnerVo oldOwner = iOwnerService.findByOwnerId(ownerBo);
        if (oldOwner == null) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "不存在的业主ID", null);
        }
        if (WaterConstants.OWNER_STATUS_DELETE == oldOwner.getStatus()) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "该用户已经销户了,不能过户", null);
        }
        // 获取新业主数据
        ownerBo.setOwnerid(null);
        ownerBo.setUserno(ownerForm.getNewuserno());
        OwnerVo newOwner = iOwnerService.findByOwnerUserno(ownerBo);
        if (newOwner == null) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "新业主编号不存在", null);
        }
        // 如果新业主已经开户，则不能过户到新业主中
        if (WaterConstants.OWNER_STATUS_CREATE == newOwner.getStatus()) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "新业主已开户，不能过户", null);
        }
        // 获取水表数据
        DeviceVo device = iDeviceService.findById(oldOwner.getDevid());
        String devid = oldOwner.getDevid();
        String modelid = oldOwner.getModelid();
        String devno = oldOwner.getDevno();
        // 对原业主进行销户
        OrderBo order = new OrderBo();
        order.setEnterpriseid(oldOwner.getEnterpriseid());
        order.setUserno(oldOwner.getUserno());
        order.setPaystatus(0);
        List<OrderVo> noPayOrderList = iOrderService.orderList(order);
        if (noPayOrderList.size() > 0) {
            // throw new FrameworkRuntimeException(ResultCode.Fail, "原业主存在未处理账单，不能过户",
            // null);
            // 判断业主申报错账审批之后账单已经处理完成，可以销户
            WrongAccountBo wrongAccountBo = new WrongAccountBo();
            wrongAccountBo.setOwnerid(ownerForm.getOwnerid());
            List<WrongAccountVo> ownerWrongList = iWrongAccountService.getStatus(wrongAccountBo);
            if (ownerWrongList == null || ownerWrongList.size() == 0) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "业主存在未处理账单，不能销户", null);

            } else {
                outer:
                for (WrongAccountVo wrongAccount : ownerWrongList) {
                    if (wrongAccount.getStatus().intValue() == WaterConstants.WRONG_ACCOUNT_STATUS_SHENQING
                            || wrongAccount.getStatus().intValue() == WaterConstants.WRONG_ACCOUNT_STATUS_CHULI) {
                        throw new FrameworkRuntimeException(ResultCode.Fail, "业主有错账在申请处理中，不能销户", null);
                    }
                    for (int i = 0; i < noPayOrderList.size(); i++) {
                        if (noPayOrderList.get(i).getTradeno().equals(wrongAccount.getTradeno())) {
                            continue outer;
                        }
                        if (i == (noPayOrderList.size() - 1)) {
                            throw new FrameworkRuntimeException(ResultCode.Fail, "业主存在未处理账单，不能销户", null);
                        }
                    }
                }
            }
        }
        double oldownerAlreadypay = oldOwner.getAlreadypay();
        // 生成支付记录
        PayDetailBo payDetail = new PayDetailBo();
        payDetail.setOwnerid(oldOwner.getOwnerid());
        payDetail.setOwnerno(oldOwner.getUserno());
        payDetail.setOwnername(oldOwner.getUsername());
        payDetail.setPaymoney(BigDecimal.valueOf(oldOwner.getAlreadypay()));
        payDetail.setCreatetime(new Date());
        payDetail.setCreateuser(user.getUserid());
        payDetail.setUsername(user.getName());
        payDetail.setType(WaterConstants.PAY_DETAIL_TYPE_OUT);
        payDetail.setBeforemoney(BigDecimal.valueOf(oldOwner.getAlreadypay()));
        payDetail.setAftermoney(BigDecimal.valueOf(0.0));
        payDetail.setRemark("过户余额退还");
        iPayDetailService.addPayDetail(payDetail, oldOwner.getAlreadypay());

        // 余额清零 同时把原来业主设置为没开户，没绑定水表状态
        ownerBo.setOwnerid(oldOwner.getOwnerid());
        iOwnerService.cancelOwner(ownerBo);

        // 将水表添加到新业主中，并为新业主开户
        ownerBo = new OwnerBo();
        ownerBo.setOwnerid(newOwner.getOwnerid());
        ownerBo.setDevid(devid);
        ownerBo.setModelid(modelid);
        ownerBo.setInstallmonth(ownerForm.getInstallmonth());
        ownerBo.setPurposeid(ownerForm.getPurposeid());
        ownerBo.setPaytypeid(ownerForm.getTypeid());
        ownerBo.setReduceid(ownerForm.getReduceid());
        ownerBo.setStatus(WaterConstants.OWNER_STATUS_CREATE);
        ownerBo.setEnterpriseid(user.getEnterpriseid());
        iOwnerService.openOwner(ownerBo);

        // 插入记录表
        OwnerRecordBo ownerRecord = new OwnerRecordBo();
        ownerRecord.setEnterpriseid(user.getEnterpriseid());
        ownerRecord.setCommunityid(oldOwner.getCommunityid());
        ownerRecord.setType(WaterConstants.OWNER_RECORD_TYPE_CHANGE_OWNER);
        ownerRecord.setOwnerid(newOwner.getOwnerid());
        ownerRecord.setUsername(newOwner.getUsername());
        ownerRecord.setOldownerid(oldOwner.getOwnerid());
        ownerRecord.setOldusername(oldOwner.getUsername());
        ownerRecord.setOlduserphone(oldOwner.getUserphone());
        ownerRecord.setDevid(newOwner.getDevid());
        if (device != null) {
            ownerRecord.setDevno(device.getDevno());
            ownerRecord.setDevnum(device.getWater());
        }
        ownerRecord.setReason(ownerForm.getReason());
        ownerRecord.setDescr(ownerForm.getDescr());
        ownerRecord.setOperatetime(new Date());
        ownerRecord.setOperateuser(user.getUserid());
        ownerRecord.setOperatename(user.getName());
        ownerRecord.setOldalreadypay(oldownerAlreadypay);
        iOwnerService.addOwnerRecord(ownerRecord);

        // TODO 操作日志(业主换过户)

        // TODO 发短信和微信消息 (业主过户)
    }

    @Override
    public Pagination<OwnerVo> getOwners(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map == null || map.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "没找到区域", null);
        }

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        ownerBo.setEnterpriseid(user.getEnterpriseid());
        ownerBo.setUserBy(userBy);
        ownerBo.setCurr(curr);

        Pagination<OwnerVo> pagination = iOwnerService.getOwners(ownerBo);
        setOwnerAreaName(map, pagination);

        return iOwnerService.getOwners(ownerBo);
    }

    @Override
    public List<OwnerVo> getCommunityOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        ownerBo.setEnterpriseid(user.getEnterpriseid());
        return iOwnerService.getCommunityOwner(ownerBo);
    }

    @Override
    public Pagination<OwnerVo> getOwnerList(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map == null || map.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "没找到区域", null);
        }

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        ownerBo.setEnterpriseid(user.getEnterpriseid());
        ownerBo.setUserBy(userBy);
        ownerBo.setCurr(curr);

        Pagination<OwnerVo> pagination = iOwnerService.getOwnerList(ownerBo);
        setOwnerAreaName(map, pagination);
        return pagination;
    }

    private void setOwnerAreaName(Map<String, AreaNodeVo> map, Pagination<OwnerVo> pagination) {
        if (pagination.getData() != null && pagination.getData().size() > 0) {
            for (OwnerVo o : pagination.getData()) {
                AreaNodeVo areaNode = map.get(String.valueOf(o.getCommunityid()));
                if (areaNode == null) {
                    continue;
                }
                o.setCommunityname(areaNode.getTitle());
            }
        }
    }

    @Override
    public OwnerVo getDetailOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());

        OwnerVo owner = iOwnerService.findByOwnerDetail(ownerBo);
        if (owner == null) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "不存在的业主ID", null);
        } else {
            if (map != null && map.size() != 0) {
                AreaNodeVo areaNode = map.get(owner.getCommunityid());
                if (areaNode != null) {
                    owner.setCommunityname(areaNode.getTitle());
                }
            }
            
            // 统计未缴账单数量和欠费金额
            PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
            paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
            paymentTradeOrderBo.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
            paymentTradeOrderBo.setUserid(owner.getOwnerid());
            List<PaymentTradeOrderVo> list = iPaymentTradeOrderService.list(paymentTradeOrderBo);
            LOGGER.info(owner.getUsername() + "共[" + list.size() + "]个未缴账单...");
            double arrears = 0.0;
            for (PaymentTradeOrderVo noPayOrder : list) {
                arrears = CalUtil.add(CalUtil.add(noPayOrder.getPenalty(), noPayOrder.getAmount()).doubleValue(), arrears);
            }
            owner.setArrears(arrears);
            owner.setNoPayNumber((list != null && list.size() > 0) ? list.size() : 0 );
            return owner;
        }
    }

    @Override
    public OwnerVo getOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);

        OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);
        if (owner == null) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "不存在的业主ID", null);
        }

        // 获取业主最新一条账单
        OrderVo order = iOrderService.findByUserNo(owner.getEnterpriseid(), owner.getUserno());
        if (order != null) {
            owner.setTradeno(order.getTradeno());
            owner.setOrderdate(order.getYear() + order.getMonth());
            owner.setReadtime(order.getReadtime());
            if (owner.getReadtime() != null) {
                Date genDate = DateUtils.parseDatetime(order.getReadtime());
                long diff = System.currentTimeMillis() - genDate.getTime();
                long day = diff / (1000 * 60 * 60 * 24);
                owner.setTimeinterval((int) (day < 1 ? 0 : day));
            }
            // 统计业主欠费数据(根据营业厅账单不是业务账单)   edit By KJR
            List<PaymentTradeOrderVo> list;
            PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
            paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
            paymentTradeOrderBo.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
            paymentTradeOrderBo.setUserid(owner.getOwnerid());
            list = iPaymentTradeOrderService.list(paymentTradeOrderBo);
            LOGGER.info(owner.getUsername() + "共[" + list.size() + "]个未缴账单...");
            // 拖欠费用
            double arrears = 0.0;
            for (PaymentTradeOrderVo noPayOrder : list) {
                arrears = CalUtil.add(CalUtil.add(noPayOrder.getPenalty(), noPayOrder.getAmount()).doubleValue(), arrears);
            }
            owner.setArrears(arrears);
        }

        return owner;
    }

    @Override
    public void genNewOrder(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);

        if (owner == null) {
            throw new FrameworkRuntimeException(ResultCode.OwnerNotExist, "不存在的业主ID", null);
        }
        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map == null || map.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "操作人没有分配区域,没有操作权限", null);
        }
        AreaNodeVo community = map.get(String.valueOf(owner.getCommunityid()));
        if (community == null) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "操作人没有操作该区域的权限", null);
        }
        // 获取离当前时间最近的一条上行记录
        String metertime = DateUtils.formatDate(new Date());
        String[] data = metertime.split("-");
        // 得到一个Calendar的实例
        Calendar ca = Calendar.getInstance();
        // 月份是从0开始的，所以11表示12月
        ca.set(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2]));
        // 月份减1
        ca.add(Calendar.MONTH, -1);
        // 结果
        Date lastMonth = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
        String CurrentMonth = data[0] + data[1];
        String LastMonth = sf.format(lastMonth);
        metertime = metertime + " 23:59:59";

        LastUpLinkVo lastUplink = iOrderService.findLastUplink(owner.getDevid(), CurrentMonth, LastMonth, metertime);
        if (lastUplink == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "水表未进行过抄表，不能生成账单", null);
        }

        DeviceVo device = iDeviceService.getDevById(owner.getDevid());

        PayTypeVo payType = iPayTypeService.get(owner.getPaytypeid());
        Integer errorType = iOrderFactory.singleBuildOrder(owner, community, user, device, payType);
        if (errorType != ErrorType.SUCCESS.intValue()) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "error", ErrorType.getCodeText(errorType));
        }
    }

    @Override
    public Pagination<OwnerRecordVo> getRecordList(OwnerForm ownerForm) throws FrameworkRuntimeException {
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        return iOwnerService.getRecordList(ownerBo);
    }

    @Override
    public void batchUpdateOwner(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        iOwnerService.batchUpdateOwner(ownerBo);
    }

    @Override
    public void getCheckSearch(OwnerForm ownerForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        String userBy = user.getAccount();
        Date curr = new Date();

        Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (map == null || map.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "系统未给您分配区域", null);
        }

        List<String> nodeIds = new ArrayList<>();
        for (String key : map.keySet()) {
            nodeIds.add(key);
        }

        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        ownerBo.setNodeIds(nodeIds);
        if (iOwnerService.getOwnerCount(ownerBo) != 1) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "根据条件未检索到或检索到多个业主，请完善查询条件", null);
        }
    }

    @Override
    public OwnerVo findByOwnerId(OwnerForm ownerForm) throws FrameworkRuntimeException {
        OwnerBo ownerBo = new OwnerBo();
        BeanUtils.copy(ownerForm, ownerBo);
        return iOwnerService.findByOwnerId(ownerBo);
    }

    @Override
    public List<OwnerVo> findBusinessHallOwners(OwnerForm ownerForm) {
        UserVo user = AuthCasClient.getUser();
        String[] keys = ownerForm.getKeywords().split(" ");
        List<String> keyWords =  Arrays.asList(keys);

        return iOwnerService.findBusinessHallOwners(user.getEnterpriseid(),keyWords);
    }
}
