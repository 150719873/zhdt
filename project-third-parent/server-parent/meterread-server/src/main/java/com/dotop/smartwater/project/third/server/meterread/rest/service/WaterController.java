package com.dotop.smartwater.project.third.server.meterread.rest.service;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.form.EnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.OperationBo;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.server.meterread.api.IThirdFactory;
import com.dotop.smartwater.project.third.server.meterread.api.IWaterFactory;
import com.dotop.smartwater.project.third.server.meterread.client.fegin.ICasFeginClient;
import com.dotop.smartwater.project.third.server.meterread.client.fegin.IWaterDownLinkClient;
import com.dotop.smartwater.project.third.server.meterread.core.third.WaterDownLoadForm;
import com.dotop.smartwater.project.third.server.meterread.dao.water.IWaterDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 水务系统业务入口层，主要用生成主动触发
 *
 *
 */
@RestController
@RequestMapping("/water")
public class WaterController implements BaseController<BaseForm> {

    private final static Logger LOGGER = LogManager.getLogger(WaterController.class);

    @Autowired
    private IWaterFactory iWaterFactory;
    @Autowired
    private IWaterDownLinkClient iWaterDownLinkClient;
    @Autowired
    private IThirdFactory iThirdFactory;

    // TODO
    private static final long DEFAULT_CACHE_TIMEOUT = 1800L;
    public static final String CACHE_KEY = "meter:water:login:";
    @Autowired
    private StringValueCache svc;
    @Autowired
    private IWaterDao iWaterDao;
    @Autowired
    private ICasFeginClient iCasFeginClient;

    @Value("${param.config.enterpriseid}")
    private String enterpriseid;
    @Value("${param.config.website}")
    private String website;
    @Value("${param.config.account}")
    private String account;
    @Value("${param.config.password}")
    private String password;


    /**
     * 查询最新抄表数据并分页
     *
     * @param deviceUplinkForm
     * @return
     */
    @PostMapping(value = "/getUplinkPage", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody DeviceUplinkForm deviceUplinkForm) {
        LOGGER.info(LogMsg.to("deviceUplinkForm", deviceUplinkForm));
        return resp(ResultCode.Success, ResultCode.SUCCESS, iWaterFactory.page(deviceUplinkForm));
    }


    /**
     * 查询最新业主信息并分页
     *
     * @param ownerForm
     * @return
     */
    @PostMapping(value = "/getOwnerPage", produces = GlobalContext.PRODUCES)
    public String getOwnerPage(@RequestBody OwnerForm ownerForm) {
        LOGGER.info(LogMsg.to("ownerForm", ownerForm));
        return resp(ResultCode.Success, ResultCode.SUCCESS, iWaterFactory.getOwnerPage(ownerForm));
    }

    /**
     * 下发命令
     *
     * @param operationBo
     * @return
     */
    @PostMapping(value = "/downlink", produces = GlobalContext.PRODUCES)
    public String downlink(@RequestBody OperationBo operationBo) {
        LOGGER.info(LogMsg.to("operationBo", operationBo));
//        return resp(ResultCode.Success, ResultCode.SUCCESS, iWaterDownLinkClient.downlink(operationBo));
        DeviceVo device = iWaterDao.findByDevNo(operationBo.getDevNo());
        if (device == null) {
            return resp(ResultCode.NOTFINDWATER, ResultCode.NOTFINDWATER, null);
        }
        LOGGER.info(LogMsg.to("device", device));
        WaterDownLoadForm form = new WaterDownLoadForm();
        form.setDevid(device.getDevid());
        //  水务系统1关阀，2开阀
        if (operationBo.getType() == 2) {
            form.setCommand(TxCode.OpenCommand);
        } else if (operationBo.getType() == 1) {
            form.setCommand(TxCode.CloseCommand);
        } else {
            form.setCommand(TxCode.OpenCommand);
        }
        LOGGER.info(LogMsg.to("form", form));
        UserVo user = login(enterpriseid, website, account, password);
        String downlink = iWaterDownLinkClient.downlink(form, user.getTicket(), user.getUserid());
        LOGGER.info(LogMsg.to("downlink", downlink));
        JSONObjects jsonObjects = JSONUtils.parseObject(downlink);
        String code = jsonObjects.getString(ModeConstants.RESULT_CODE);
        if (!ResultCode.Success.equals(code)) {
            return resp(ResultCode.Fail, ResultCode.Fail, code);
        }
        String data = jsonObjects.getString("data");
        String clientid = JSONUtils.parseObject(data).getString(ModeConstants.RESULT_CLIENT_ID);
        Map<Object, Object> params = new HashMap(3);
        params.put("code", ResultCode.Success);
        params.put("msg", ResultCode.SUCCESS);
        params.put("data", clientid);
        LOGGER.info(LogMsg.to("params", params));
        return resp(ResultCode.Success, ResultCode.SUCCESS, JSONUtils.toJSONString(params));
    }

    private UserVo login(String enterpriseid, String website, String account, String password) throws FrameworkRuntimeException {
        String str = svc.get(CACHE_KEY + enterpriseid + ":" + account);
        if (StringUtils.isNotBlank(str)) {
            return JSONUtils.parseObject(str, UserVo.class);
        }
        UserForm userForm = new UserForm();
        userForm.setWebsite(website);
        userForm.setAccount(account);
        userForm.setPassword(password);
        String result = iCasFeginClient.login(userForm);
        // 结果校验
        String data = getData(result);
        JSONObjects map = JSONUtils.parseObject(data);
        String userid = map.getString("userid");
        String ticket = map.getString("ticket");
        // 组装缓存参数
        UserVo userVo = new UserVo();
        userVo.setUserid(userid);
        userVo.setTicket(ticket);
        userVo.setAccount(account);
        svc.set(CACHE_KEY + enterpriseid + ":" + account, JSONUtils.toJSONString(userVo), DEFAULT_CACHE_TIMEOUT);
        return userVo;
    }

    private String getData(String result) throws FrameworkRuntimeException {
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
        if (ResultCode.Success.equals(code)) {
            return jsonObjects.getString("data");
        }
        String msg = jsonObjects.getString("msg");
        throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, msg);
    }

    /**
     * 查询下发命令列表
     *
     * @param deviceDownlinkBos
     * @return
     */
    @PostMapping(value = "/downlinkList", produces = GlobalContext.PRODUCES)
    public String downlinkList(@RequestBody List<DeviceDownlinkBo> deviceDownlinkBos) {
        LOGGER.info(LogMsg.to("deviceDownlinkBos", deviceDownlinkBos));
        return resp(ResultCode.Success, ResultCode.SUCCESS, iThirdFactory.getDownlink(deviceDownlinkBos));
    }

    @PostMapping(value = "/checkEnterpriseId", produces = GlobalContext.PRODUCES)
    public String checkEnterpriseId(@RequestBody EnterpriseForm enterpriseForm) {
        LOGGER.info(LogMsg.to("enterpriseForm", enterpriseForm));
        return resp(ResultCode.Success, ResultCode.SUCCESS, iWaterFactory.checkEnterpriseId(enterpriseForm));
    }
}
