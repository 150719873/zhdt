package com.dotop.smartwater.project.third.server.meter.rest.service.standard;


import com.dotop.smartwater.project.third.module.api.factory.*;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.OperationForm;
import com.dotop.smartwater.project.third.module.core.third.standard.form.AuthForm;
import com.dotop.smartwater.project.third.module.core.third.standard.form.DataForm;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardDeviceVo;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardOwnerVo;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.UplinkVo;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("/remote")
public class StandardRemoteController implements BaseController<BaseForm> {

    private static final Logger logger = LogManager.getLogger(StandardRemoteController.class);

    @Autowired
    private IThirdFactory iThirdFactory;

    @Autowired
    private IStandardDeviceUplinkFactory iStandardDeviceUplinkFactory;

    @Autowired
    private IStandardDeviceFactory iStandardDeviceFactory;

    @Autowired
    private IStandardOwnerFactory iStandardOwnerFactory;

    @Autowired
    private IStandardCommandFactory iStandardCommandFactory;


    @PostMapping(value = "/auth", produces = GlobalContext.PRODUCES)
    public String auth(@RequestBody AuthForm authForm) {
        try {

            logger.info(LogMsg.to("authForm", authForm));
            VerificationUtils.string("code", authForm.getCode());
            VerificationUtils.string("username", authForm.getUsername());
            VerificationUtils.string("password", authForm.getPassword());
            // TODO 登录权限校验和缓存
            UserVo userVo = iStandardDeviceUplinkFactory.cacheLoginInfo(authForm);
            if (userVo != null) {
                UserVo result = new UserVo();
                result.setTicket(userVo.getTicket());
                return resp(ResultCode.Success, ResultCode.SUCCESS, result);
            } else {
                return resp(ResultCode.Fail, "账号信息错误", null);
            }
        } catch (Exception e) {
            logger.error(e);
            return resp(ResultCode.Fail, "参数异常", null);
        }
    }


    @PostMapping(value = "/datas", produces = GlobalContext.PRODUCES)
    public String getDataBack(@RequestBody DataForm dataForm, @RequestHeader String ticket) {
        String enterpriseid = "";
        try {
            logger.info(LogMsg.to("dataForm", dataForm));
            VerificationUtils.string("month", dataForm.getMonth());
            VerificationUtils.string("devno", dataForm.getDevno(), true);
            DateUtils.parse(dataForm.getMonth(), DateUtils.YYYYMM);
        } catch (Exception e) {
            return resp(ResultCode.Fail, "参数异常", null);
        }

        try {
            UserVo login = iStandardDeviceUplinkFactory.isLogin(ticket);
            enterpriseid = login.getEnterpriseid();
        } catch (Exception e) {
            return resp(ResultCode.Fail, e.getMessage(), null);
        }

        try {
            if (dataForm.getPage() == null) {
                dataForm.setPage(1);
            }
            if (dataForm.getPageCount() == null) {
                dataForm.setPageCount(500);
            }
            dataForm.setEnterpriseid(enterpriseid);
            Pagination<UplinkVo> page = iStandardDeviceUplinkFactory.pageDeviceUplink(dataForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, page);
        } catch (Exception e) {
            logger.error(e);
            return resp(ResultCode.Success, ResultCode.SUCCESS, new Pagination<>(1, 1, new ArrayList<>(), 0));
        }

    }

    @PostMapping(value = "/devices", produces = GlobalContext.PRODUCES)
    public String getdevicesBack(@RequestBody DataForm dataForm, @RequestHeader String ticket) {
        String enterpriseid = "";
        try {

            logger.info(LogMsg.to("dataForm", dataForm));
            VerificationUtils.string("devno", dataForm.getDevno(), true);
        } catch (Exception e) {
            return resp(ResultCode.Fail, "参数异常", null);
        }

        try {
            UserVo login = iStandardDeviceUplinkFactory.isLogin(ticket);
            enterpriseid = login.getEnterpriseid();
        } catch (Exception e) {
            return resp(ResultCode.Fail, e.getMessage(), null);
        }

        try {
            if (dataForm.getPage() == null) {
                dataForm.setPage(1);
            }
            if (dataForm.getPageCount() == null) {
                dataForm.setPageCount(500);
            }
            dataForm.setEnterpriseid(enterpriseid);
            Pagination<StandardDeviceVo> page = iStandardDeviceFactory.pageDevice(dataForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, page);
        } catch (Exception e) {
            logger.error(e);
            return resp(ResultCode.Success, ResultCode.SUCCESS, new Pagination<>(1, 1, new ArrayList<>(), 0));
        }

    }

    @PostMapping(value = "/owners", produces = GlobalContext.PRODUCES)
    public String getOwnerssBack(@RequestBody DataForm dataForm, @RequestHeader String ticket) {
        String enterpriseid = "";
        try {
            logger.info(LogMsg.to("dataForm", dataForm));
            VerificationUtils.string("startMonth", dataForm.getStartMonth(), true);
            VerificationUtils.string("endMonth", dataForm.getEndMonth(), true);
            if (dataForm.getStartMonth() != null) {
                DateUtils.parse(dataForm.getStartMonth(), DateUtils.YYYYMM);
            }
            if (dataForm.getEndMonth() != null) {
                DateUtils.parse(dataForm.getEndMonth(), DateUtils.YYYYMM);
            }
        } catch (Exception e) {
            return resp(ResultCode.Fail, "参数异常", null);
        }

        try {
            UserVo login = iStandardDeviceUplinkFactory.isLogin(ticket);
            enterpriseid = login.getEnterpriseid();
        } catch (Exception e) {
            return resp(ResultCode.Fail, e.getMessage(), null);
        }

        try {
            if (dataForm.getPage() == null) {
                dataForm.setPage(1);
            }
            if (dataForm.getPageCount() == null) {
                dataForm.setPageCount(500);
            }
            if (dataForm.getStartMonth() == null && dataForm.getEndMonth() == null) {
                DateTime starDate = new DateTime(System.currentTimeMillis());
                DateTime startTemp = new DateTime(DateUtils.month(starDate.toDate(), -2));
                DateTime endTemp = new DateTime(DateUtils.month(starDate.toDate(), 0));
                dataForm.setStartMonth(DateUtils.format(startTemp.toDate(), DateUtils.YYYYMM));
                dataForm.setEndMonth(DateUtils.format(endTemp.toDate(), DateUtils.YYYYMM));

            } else if (dataForm.getStartMonth() != null && dataForm.getEndMonth() == null) {
                DateTime starDate = new DateTime(DateUtils.parse(dataForm.getStartMonth(), DateUtils.YYYYMM).getTime());
                DateTime startTemp = new DateTime(DateUtils.month(starDate.toDate(), 0));
                DateTime endTemp = new DateTime(DateUtils.month(starDate.toDate(), 2));
                dataForm.setStartMonth(DateUtils.format(startTemp.toDate(), DateUtils.YYYYMM));
                dataForm.setEndMonth(DateUtils.format(endTemp.toDate(), DateUtils.YYYYMM));

            } else if (dataForm.getStartMonth() == null && dataForm.getEndMonth() != null) {
                DateTime endDate = new DateTime(DateUtils.parse(dataForm.getEndMonth(), DateUtils.YYYYMM).getTime());
                DateTime endTemp = new DateTime(DateUtils.month(endDate.toDate(), 0));
                DateTime startTemp = new DateTime(DateUtils.month(endDate.toDate(), -2));
                dataForm.setStartMonth(DateUtils.format(startTemp.toDate(), DateUtils.YYYYMM));
                dataForm.setEndMonth(DateUtils.format(endTemp.toDate(), DateUtils.YYYYMM));
            }
            dataForm.setEnterpriseid(enterpriseid);
            Pagination<StandardOwnerVo> page = iStandardOwnerFactory.pageOwner(dataForm);
            return resp(ResultCode.Success, ResultCode.SUCCESS, page);
        } catch (Exception e) {
            logger.error(e);
            return resp(ResultCode.Success, ResultCode.SUCCESS, new Pagination<>(1, 1, new ArrayList<>(), 0));
        }

    }

    /**
     * 下发命令
     *
     * @return
     */
    @PostMapping(value = "/downlink", produces = GlobalContext.PRODUCES)
    public String downlink(@RequestBody OperationForm operationForm, @RequestHeader String ticket) {
        logger.info(LogMsg.to("operationForm", operationForm));
        try {
            UserVo login = iStandardDeviceUplinkFactory.isLogin(ticket);
            operationForm.setEnterpriseid(login.getEnterpriseid());
            operationForm.setUsername(login.getAccount());
            CommandVo commandVo = iStandardCommandFactory.sendCommand(operationForm);
            Map<String, String> params = new HashMap<>();
            params.put("clientId", commandVo.getClientid());
            return resp(ResultCode.Success, ResultCode.SUCCESS, params);
        } catch (Exception e) {
            logger.error(e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }

    }

    /**
     * 查询下发命令列表
     *
     * @param clientIds
     * @return
     */
    @PostMapping(value = "/downlinkList", produces = GlobalContext.PRODUCES)
    public String downlinkList(@RequestBody List<String> clientIds, @RequestHeader String ticket) {
        logger.info(LogMsg.to("clientIds", clientIds));
        try {
            UserVo login = iStandardDeviceUplinkFactory.isLogin(ticket);
        } catch (Exception e) {
            logger.error(e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
        return resp(ResultCode.Success, ResultCode.SUCCESS, iThirdFactory.getDownlink(clientIds));
    }


}
