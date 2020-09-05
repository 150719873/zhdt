package com.dotop.smartwater.project.auth.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dotop.smartwater.project.auth.api.IAccountFactory;
import com.dotop.smartwater.project.auth.api.IAreaFactory;
import com.dotop.smartwater.project.auth.api.IRoleFactory;
import com.dotop.smartwater.project.auth.cache.CBaseDao;
import com.dotop.smartwater.project.auth.cache.RedisDao;
import com.dotop.smartwater.project.auth.cache.UserCacheDao;
import com.dotop.smartwater.project.auth.dao.IAreaDao;
import com.dotop.smartwater.project.auth.dao.IBaseDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.dto.AreaDto;
import com.dotop.smartwater.project.module.core.auth.form.EnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.UserAreaForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaListVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.RoleVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoginVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;

/**

 * @date 2019年5月9日
 * @description
 */
@RestController

@RequestMapping("/auth/UserController")
public class UserController extends FoundationController implements BaseController<UserForm> {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    private IBaseDao iBaseDao;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private UserCacheDao userCacheDao;


    @Autowired
    private IRoleFactory iRoleFactory;

    @Autowired
    private IAccountFactory iAccountFactory;

    @Autowired
    private IAreaFactory iAreaFactory;

    @Autowired
    private CBaseDao baseDao;

    @Autowired
    private ISettlementService iSettlementService;

    @Autowired
    private IAreaDao iAreaDao;
    
    @Value("${param.initSetting.offday}")
    private Integer offday;
    @Value("${param.initSetting.alarmday}")
    private Integer alarmday;
    @Value("${param.initSetting.appointmentDay}")
    private Integer appointmentDay;
    @Value("${param.initSetting.appointmentNumber}")
    private Integer appointmentNumber;
    @Value("${aliyundns.defaultDoMain}")
    private String DEFAULTDOMAIN;

    @PostMapping(value = "/logout", produces = GlobalContext.PRODUCES)
    public String logout(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
        UserVo user = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || user == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        } else {
            redisDao.delWebUserTicket(userid);
            auditLog(OperateTypeEnum.USER_ACCOUNT, "用户登出", "账号", user.getAccount());
            return resp(ResultCode.Success, "用户登出成功", null);
        }
    }

    @PostMapping(value = "/login", produces = GlobalContext.PRODUCES)
    public String login(@RequestBody UserForm user) {
        try {
            if (StringUtils.isBlank(user.getAccount()) || StringUtils.isBlank(user.getPassword())) {
                return resp(ResultCode.ParamIllegal, "账号或密码为空", null);
            }
            System.out.println("网址为："+user.getWebsite());
            EnterpriseVo enterprise = iAccountFactory.findEnterpriseByWebsite(user.getWebsite());
            if (enterprise == null) {
                return resp(ResultCode.ParamIllegal, "域名没有配置", null);
            }
            if (enterprise.getFailureState() != null && enterprise.getFailureState() == 1) {
                return resp(ResultCode.ParamIllegal, "域名已失效", null);
            }
            if (enterprise.getFailureTime() != null && System.currentTimeMillis() > enterprise.getFailureTime().getTime()) {
                return resp(ResultCode.ParamIllegal, "域名已失效", null);
            }
            user.setEnterpriseid(enterprise.getEnterpriseid());
            UserVo userLogin = iAccountFactory.login(enterprise.getEnterpriseid(), user.getAccount(),
                    user.getPassword());
            if (userLogin == null) {
                return resp(ResultCode.AccountOrPasswordError, "账号或密码错误", null);
            }

            if (userLogin.getFailurestate() != null && userLogin.getFailurestate() == UserVo.USER_FAILURESTATE_INVALID) {
                return resp(ResultCode.AccountInvalid, "账户已失效", null);
            } else if ((userLogin.getFailurestate() != null && userLogin.getFailurestate() == UserVo.USER_FAILURESTATE_VALID)
                    || userLogin.getFailurestate() == null) {
                if (userLogin.getFailuretime() != null) {
                    Date date = new Date();
                    if (date.getTime() > userLogin.getFailuretime().getTime()) {
                        UserForm u = new UserForm();
                        u.setUserid(userLogin.getUserid());
                        u.setFailurestate(UserVo.USER_FAILURESTATE_INVALID);
                        iAccountFactory.editUser(u);
                        return resp(ResultCode.AccountInvalid, "账户已失效", null);
                    }
                }
            }

            if (userLogin.getType() != UserVo.USER_TYPE_ADMIN) {
                if (enterprise.getProleid() == null) {
                    return resp(ResultCode.ParamIllegal, "该水司没有初始化平台角色权限,请联系平台管理员配置", null);
                }
            }

            // 更新权限
            iRoleFactory.updateRolePermission(BeanUtils.copy(userLogin, UserForm.class), enterprise.getProleid());

            SettlementVo settlement = iSettlementService.getSettlement(user.getEnterpriseid());

            // 踢人，不能同时登陆
            String ticket = UuidUtils.getUuid();
            redisDao.setWebUserTicket(userLogin.getUserid(), ticket);

            userLogin.setEnterprise(enterprise);
            userLogin.setSettlement(settlement);
            userCacheDao.setUser(userLogin);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userid", userLogin.getUserid());
            resultMap.put("ticket", ticket);
            resultMap.put("username", userLogin.getAccount());
            resultMap.put("name", userLogin.getName());
            resultMap.put("invalidtime", System.currentTimeMillis() + WaterConstants.LOGIN_TIME);
            resultMap.put("companyName", enterprise.getName());
            resultMap.put("enterpriseid", enterprise.getEnterpriseid());
            resultMap.put("description", enterprise.getDescription());
            resultMap.put("calibration", enterprise.getCalibration());
            resultMap.put("syschoice", userLogin.getSyschoice());
            resultMap.put("type", userLogin.getType());
            resultMap.put("bindpermission", userLogin.getBindpermission());
            resultMap.put("userType", userLogin.getUsertype());
            resultMap.put("userManual", enterprise.getUserManual());
            resultMap.put("workApp", enterprise.getWorkApp());
            resultMap.put("bindApp", enterprise.getBindApp());
            
            if (settlement != null) {
            	if (StringUtils.isNotBlank(settlement.getAlias())) {
            		resultMap.put("title", settlement.getAlias());
            	}
            	
            	if (settlement.getDefaultDay() != null) {
            		resultMap.put("defaultDay", settlement.getDefaultDay());
            	}
            } else {
            	resultMap.put("title", "");
            }

            getRoleName(userLogin.getType(),resultMap,userLogin.getRoleid());
            return resp(ResultCode.Success, AuthResultCode.SUCCESS, resultMap);
        } catch (Exception e) {
            LOGGER.error("login", e);
            return resp(ResultCode.Fail, "服务繁忙,请稍候再试", null);
        }
    }

    private void getRoleName(Integer userType,Map<String, Object> resultMap,String roleId) {
        switch (userType) {
            case WaterConstants.USER_TYPE_ADMIN:
                resultMap.put("roleName", WaterConstants.SELECT_TYPE_ADMIN_ROLE_NAME);
                break;
            case WaterConstants.USER_TYPE_ADMIN_ENTERPRISE:
                resultMap.put("roleName", WaterConstants.SELECT_TYPE_DEFAULT_ROLE_NAME);
                break;
            default:
                RoleVo vo = iRoleFactory.findRoleById(roleId);
                if(vo != null){
                    resultMap.put("roleName", vo.getName());
                }else{
                    resultMap.put("roleName", "");
                }
                break;
        }
    }

    @PostMapping(value = "/children_login", produces = GlobalContext.PRODUCES)
    public String childrenLogin(@RequestBody Map<String, String> map) {
        try {
            String data = map.get(WaterClientConfig.CasKey);
            if (StringUtils.isBlank(data)) {
                return resp(ResultCode.Fail, "缺少Key!", null);
            }

            UserLoginVo noLoginUser = CBaseDao.getUserLogin(data);
            if (noLoginUser == null) {
                return resp(ResultCode.Fail, "验证Key失败!", null);
            }

            UserVo user = baseDao.getRedisUser(noLoginUser.getUserid());
            if (!baseDao.webAuth(noLoginUser.getUserid(), noLoginUser.getTicket()) || user == null) {
                return resp(ResultCode.TimeOut, "登陆失败,请重新登录", null);
            }
            // TODO 测试ticket不改变
            // String.valueOf(UUID.randomUUID());
            String ticket = noLoginUser.getTicket();
            redisDao.setWebUserTicket(user.getUserid(), ticket);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userid", user.getUserid());
            resultMap.put("ticket", ticket);
            resultMap.put("username", user.getAccount());
            resultMap.put("name", user.getName());
            resultMap.put("invalidtime", System.currentTimeMillis() + WaterConstants.LOGIN_TIME);
            resultMap.put("menuid", noLoginUser.getModelid());
            EnterpriseVo enterprise = user.getEnterprise();
            if (enterprise != null) {
                resultMap.put("companyName", enterprise.getName());
                resultMap.put("enterpriseid", enterprise.getEnterpriseid());
            }

            SettlementVo settlement = user.getSettlement();
            if (settlement != null) {
                if (StringUtils.isNotBlank(settlement.getAlias())) {
                    resultMap.put("title", settlement.getAlias());
                }
            }
            getRoleName(user.getType(),resultMap,user.getRoleid());
            return resp(ResultCode.Success, AuthResultCode.SUCCESS, resultMap);
        } catch (Exception e) {
            LOGGER.error("children_login", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/check_user_invalid", produces = GlobalContext.PRODUCES)
    public String checkUserInvalid(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
        try {
            if (!baseDao.webAuth(userid, ticket)) {
                return resp(ResultCode.AccountInvalid, "用户已失效", null);
            } else {
                return resp(ResultCode.Success, AuthResultCode.SUCCESS, null);
            }
        } catch (Exception e) {
            LOGGER.error("check_user_invalid", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    /**
     * 传对象例子 (表单提交)
     *
     * @param userid
     * @param ticket
     * @param user
     * @return
     */
    @PostMapping(value = "/add_user", produces = GlobalContext.PRODUCES)
    public String addUser(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                          @RequestBody UserForm user) {
        UserVo createUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || createUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            if (StringUtils.isBlank(user.getAccount()) || StringUtils.isBlank(user.getPassword())
                    || StringUtils.isBlank(user.getEnterpriseid()) || StringUtils.isBlank(user.getRoleid())) {
                return resp(ResultCode.ParamIllegal, "必填参数不能为空", null);
            }

            if (!createUser.getType().equals(UserVo.USER_TYPE_ADMIN)) {
                user.setEnterpriseid(createUser.getEnterpriseid());
            }

            UserVo tempUser = iAccountFactory.findUserByAccount(user);

            if (tempUser != null) {
                return resp(ResultCode.AccountExist, "账号已经存在", null);
            }

            tempUser = iAccountFactory.findUserByWorknum(user);

            if (tempUser != null) {
                return resp(ResultCode.AccountExist, "工号已经存在", null);
            }

            if (createUser.getType() < 2) {
                user.setType(createUser.getType() + 1);
            } else {
                user.setType(UserVo.USER_TYPE_ENTERPRISE_NORMAL);
            }

            user.setCreatetime(new Date());
            // STATUS_OK = 0
            user.setStatus(0);
            user.setCreateuser(createUser.getUserid());
            user.setUserid(UuidUtils.getUuid());
            if (iAccountFactory.addUser(user) > 0) {
                auditLog(OperateTypeEnum.USER_ACCOUNT, "新增", "账号", user.getAccount());
                return resp(ResultCode.Success, AuthResultCode.SUCCESS, null);
            }
            return resp(ResultCode.Fail, "unknow error", null);
        } catch (Exception e) {
            LOGGER.error("add_user", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/edit_user", produces = GlobalContext.PRODUCES)
    public String editUser(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                           @RequestBody UserForm user) {
        UserVo createUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || createUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            if (StringUtils.isBlank(user.getAccount()) || StringUtils.isBlank(user.getPassword())
                    || StringUtils.isBlank(user.getRoleid()) || StringUtils.isBlank(user.getUserid())) {
                return resp(ResultCode.ParamIllegal, "必填参数不能为空", null);
            }

            UserVo tempUser = iAccountFactory.findUserById(user.getUserid());
            if (tempUser == null) {
                return resp(ResultCode.Fail, "找不到该用户", null);
            }

            if (UserVo.USER_TYPE_ADMIN == createUser.getType()) {
                user.setEnterpriseid(tempUser.getEnterpriseid());
            } else {
                user.setEnterpriseid(createUser.getEnterpriseid());
            }

            tempUser = iAccountFactory.findUserByAccountAndId(user);

            if (tempUser != null) {
                return resp(ResultCode.AccountExist, "账号已经存在", null);
            }

            tempUser = iAccountFactory.findUserByWorknumAndId(user);

            if (tempUser != null) {
                return resp(ResultCode.AccountExist, "工号已经存在", null);
            }
            Date curr = new Date();
            user.setCreatetime(curr);
            user.setStatus(0);// STATUS_OK = 0
            user.setCreateuser(createUser.getUserid());
            if (user.getFailuretime() != null && user.getFailuretime().getTime() > curr.getTime()) {
                user.setFailurestate(UserVo.USER_FAILURESTATE_VALID);
            } else if (user.getFailuretime() != null && user.getFailuretime().getTime() <= curr.getTime()) {
                user.setFailurestate(UserVo.USER_FAILURESTATE_INVALID);
            }


            if (iAccountFactory.editUser(user) > 0) {
                auditLog(OperateTypeEnum.USER_ACCOUNT, "编辑", "账号", user.getAccount());
                return resp(ResultCode.Success, AuthResultCode.SUCCESS, null);
            }

            return resp(ResultCode.Fail, "unknow error", null);
        } catch (Exception e) {
            LOGGER.error("edit_user", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/edit_user_state", produces = GlobalContext.PRODUCES)
    public String editUserState(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                                @RequestBody UserForm user) {
        UserVo createUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || createUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            if (user.getFailurestate() == null) {
                return resp(ResultCode.ParamIllegal, "必填参数不能为空", null);
            }

            UserVo tempUser = iAccountFactory.findUserById(user.getUserid());
            if (tempUser == null) {
                return resp(ResultCode.Fail, "找不到该用户", null);
            }

            if (UserVo.USER_TYPE_ADMIN == createUser.getType()) {
                user.setEnterpriseid(tempUser.getEnterpriseid());
            } else {
                user.setEnterpriseid(createUser.getEnterpriseid());
            }

            if (iAccountFactory.editUserState(user) > 0) {
                auditLog(OperateTypeEnum.USER_ACCOUNT, "修改帐号失效状态", "账号", user.getAccount());
                return resp(ResultCode.Success, AuthResultCode.SUCCESS, null);
            }

            return resp(ResultCode.Fail, "unknow error", null);
        } catch (Exception e) {
            LOGGER.error("edit_user", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/get_user", produces = GlobalContext.PRODUCES)
    public String getUser(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                          @RequestBody UserForm user) {
        UserVo createUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || createUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            
            if (!createUser.getType().equals(0)) {
            	if(StringUtils.isEmpty(user.getEnterpriseid()) || !createUser.getEnterpriseid().equals(user.getEnterpriseid())) {
            		user.setEnterpriseid(createUser.getEnterpriseid());
            	}
            }
            Pagination<UserVo> pagination = iAccountFactory.getUserList(user);

            return resp(ResultCode.Success, "success", pagination);
        } catch (Exception e) {
            LOGGER.error("get_user", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/getUserList", produces = GlobalContext.PRODUCES)
    public String getUserList(@RequestBody UserForm user) {
        try {
            Pagination<UserVo> pagination = iAccountFactory.getUserList(user);
            return resp(ResultCode.Success, "success", pagination);
        } catch (Exception e) {
            LOGGER.error("get_user", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/find_user", produces = GlobalContext.PRODUCES)
    public String findUser(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                           @RequestBody UserVo user) {
        UserVo createUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || createUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            if (StringUtils.isBlank(user.getUserid())) {
                return resp(ResultCode.ParamIllegal, "必填参数不能为空", null);
            }
            return resp(ResultCode.Success, AuthResultCode.SUCCESS, iAccountFactory.findUserById(user.getUserid()));
        } catch (Exception e) {
            LOGGER.error("find_user", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/add_enterprise", produces = GlobalContext.PRODUCES)
    public String addEnterprise(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                                @RequestBody EnterpriseForm enterprise) {
        UserVo user = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || user == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }

        try {
            if (StringUtils.isBlank(enterprise.getName())) {
                return resp(ResultCode.ParamIllegal, "必填参数不能为空", null);
            }

            EnterpriseVo tempEnterprise = iAccountFactory.findEnterpriseByName(enterprise);
            if (tempEnterprise != null) {
                return resp(ResultCode.EnterpriseExist, "水司已经存在", null);
            }

            enterprise.setCreatetime(new Date());
            enterprise.setCreateuser(user.getUserid());
            if (iAccountFactory.addEnterprise(enterprise) > 0) {
                auditLog(OperateTypeEnum.USER_ACCOUNT, "新增水司", "名称", enterprise.getName());
                // 增加一条记录进去区域表
                AreaListVo areaVo = new AreaListVo();
                areaVo.setEnterpriseid(enterprise.getEnterpriseid());
                AreaVo area = new AreaVo();
                area.setEnterpriseid(enterprise.getEnterpriseid());
                area.setId(enterprise.getEnterpriseid());
                area.setName(enterprise.getName());
                area.setPId("0");
                List<AreaVo> list = new ArrayList<>();

                
                // 动态开通域名（只支持阿里云DNS）  如果域名后缀不为空切等于默认后缀域名
                if (StringUtils.isNotBlank(enterprise.getWebsiteSuffix()) 
                		&& enterprise.getWebsiteSuffix().contains(DEFAULTDOMAIN)) {
                	iAccountFactory.openDoMain(enterprise);	
        		}
                
                // 写入水司配置数据
                SettlementBo setBo = new SettlementBo();
                setBo.setEnterpriseid(enterprise.getEnterpriseid());
                setBo.setAlias(enterprise.getName());
                setBo.setOffday(offday);
                setBo.setAlarmday(alarmday);
                setBo.setAppointmentDay(appointmentDay);
                setBo.setAppointmentNumber(appointmentNumber);
                setBo.setStatus(0);
                iSettlementService.addSettlement(setBo);
                
                AreaDto areaDto = new AreaDto();
                areaDto.setId(area.getId());
                boolean flag = true;
                for (; flag;) {
                    String code = generateCode();
                    areaDto.setCode(code);
                    //判断生成的编号是否与数据库中重复
                    if (iAreaDao.checkAreaCode(areaDto) == 0) {
                        area.setCode(code);
                        list.add(area);
                        areaVo.setList(list);
                        iAreaFactory.saveCompanyArea(areaVo);
                        flag = false;
                    }
                }
                return resp(ResultCode.Success, AuthResultCode.SUCCESS, null);
            }

            return resp(ResultCode.Fail, "unknow error", null);
        } catch (Exception e) {
            LOGGER.error("add_enterprise", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/edit_enterprise", produces = GlobalContext.PRODUCES)
    public String editEnterprise(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                                 @RequestBody EnterpriseForm enterprise) {
        UserVo createUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || createUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            if (StringUtils.isBlank(enterprise.getName())) {
                return resp(ResultCode.ParamIllegal, "必填参数不能为空", null);
            }

            EnterpriseVo tempEnterprise = iAccountFactory.findEnterpriseByNameAndId(enterprise);

            if (tempEnterprise != null) {
                return resp(ResultCode.EnterpriseExist, "水司已经存在", null);
            }

            enterprise.setCreatetime(new Date());
            enterprise.setCreateuser(createUser.getUserid());

            // 修改前的记录
            EnterpriseVo e = iAccountFactory.findEnterpriseById(enterprise.getEnterpriseid());
            if (iAccountFactory.editEnterprise(enterprise) > 0) {
            	
            	// 如果修改前的域名和修改后的域名不一致
            	if (StringUtils.isNotBlank(enterprise.getWebsite()) && 
            			!enterprise.getWebsite().equals(e.getWebsite())) {
            		
            		if (e.getWebsiteSuffix().contains(DEFAULTDOMAIN) && !enterprise.getWebsiteSuffix().contains(DEFAULTDOMAIN)) {
                		// 旧的包含，新的不包含 删除旧域名
            			iAccountFactory.deleteDoMain(BeanUtils.copy(e, EnterpriseForm.class));
            		} else if (e.getWebsiteSuffix().contains(DEFAULTDOMAIN) && enterprise.getWebsiteSuffix().contains(DEFAULTDOMAIN)) {
            			//旧的包含，新的包含 删除旧域名 新增新域名
            			iAccountFactory.deleteDoMain(BeanUtils.copy(e, EnterpriseForm.class));
            			iAccountFactory.openDoMain(enterprise);
            		} else if (!e.getWebsiteSuffix().contains(DEFAULTDOMAIN) && enterprise.getWebsiteSuffix().contains(DEFAULTDOMAIN)) {
            			//旧的不包含，新的包含 新增新域名
            			iAccountFactory.openDoMain(enterprise);
            		} else if (!e.getWebsiteSuffix().contains(DEFAULTDOMAIN) && enterprise.getWebsiteSuffix().contains(DEFAULTDOMAIN)) {
            			//旧的不包含，新的不包含 不处理
            		}
            	}
            	
                auditLog(OperateTypeEnum.USER_ACCOUNT, "编辑水司", "名称", enterprise.getName());
                return resp(ResultCode.Success, AuthResultCode.SUCCESS, null);
            }

            return resp(ResultCode.Fail, "unknow error", null);
        } catch (Exception e) {
            LOGGER.error("edit_enterprise", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/del_enterprise", produces = GlobalContext.PRODUCES)
    public String delEnterprise(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                                @RequestBody EnterpriseVo enterprise) {
        UserVo createUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || createUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            if (StringUtils.isBlank(enterprise.getEnterpriseid())) {
                return resp(ResultCode.ParamIllegal, "必填参数不能为空", null);
            }

            if (WaterConstants.ADMIN_ENTERPRISE_ID.equals(enterprise.getEnterpriseid())) {
                return resp(ResultCode.ParamIllegal, "系统基本配置,不能删除", null);
            }

            // 先判断有没有该水司有没有用户,有的话不能删
            if (iBaseDao.getUserCountByEnterpriseid(enterprise.getEnterpriseid()) > 0) {
                return resp(ResultCode.Fail, "该水司已有用户存在,不能删除水司", null);
            }

            EnterpriseVo e = iAccountFactory.findEnterpriseById(enterprise.getEnterpriseid());
            
            if (iAccountFactory.delEnterprise(enterprise.getEnterpriseid()) > 0) {
            	
            	//验证当前域名是否为阿里域名，如果是则远程调用删除
            	if (StringUtils.isNotBlank(e.getWebsiteSuffix()) 
                		&& e.getWebsiteSuffix().contains(DEFAULTDOMAIN)) {
            		iAccountFactory.deleteDoMain(BeanUtils.copy(e, EnterpriseForm.class));	
        		}
            	
                return resp(ResultCode.Success, AuthResultCode.SUCCESS, null);
            }

            auditLog(OperateTypeEnum.USER_ACCOUNT, "删除水司", "名称", e.getName());
            return resp(ResultCode.Fail, "unknow error", null);
        } catch (Exception e) {
            LOGGER.error("del_enterprise", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/get_enterprise", produces = GlobalContext.PRODUCES)
    public String getEnterprise(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                                @RequestBody EnterpriseForm etp) {
        try {
            Pagination<EnterpriseVo> pagination;
            UserVo user = baseDao.getRedisUser(userid);
            if (!baseDao.webAuth(userid, ticket) || user == null) {
                return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
            } else {
                if (user.getType().equals(UserVo.USER_TYPE_ADMIN)) {
                    pagination = iAccountFactory.getEnterpriseList(etp);
                } else {
                    etp.setEnterpriseid(user.getEnterpriseid());
                    pagination = iAccountFactory.getEnterpriseList(etp);
                }
            }
            return resp(ResultCode.Success, "success", pagination);
        } catch (Exception e) {
            LOGGER.error("get_enterprise", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/enterprise", produces = GlobalContext.PRODUCES)
    public String enterprise(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                             @RequestBody EnterpriseVo etp) {
        if (!baseDao.webAuth(userid, ticket)) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            return resp(ResultCode.Success, AuthResultCode.SUCCESS,
                    iAccountFactory.findEnterpriseById(etp.getEnterpriseid()));
        } catch (Exception e) {
            LOGGER.error("enterprise", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/get_user_area", produces = GlobalContext.PRODUCES)
    public String getUserArea(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                              @RequestBody UserVo user) {
        UserVo loginUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || loginUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            if (user.getUserid() == null) {
                return resp(ResultCode.ParamIllegal, "userid 为空", null);
            }
            return resp(ResultCode.Success, AuthResultCode.SUCCESS,
                    iAreaFactory.findAreasByEidAndUseId(loginUser.getEnterpriseid(), user.getUserid()));
        } catch (Exception e) {
            LOGGER.error("get_user_area", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/edit_user_area", produces = GlobalContext.PRODUCES)
    public String editUserArea(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                               @RequestBody UserAreaForm userArea) {
        UserVo loginUser = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || loginUser == null) {
            return resp(ResultCode.TimeOut, "登录超时,请重新登录", null);
        }
        try {
            if (StringUtils.isBlank(userArea.getUserid())) {
                return resp(ResultCode.ParamIllegal, "userid 为空", null);
            }

            List<String> areaIds = userArea.getAreaids();
            if (CollectionUtils.isEmpty(areaIds)) {
                return resp(ResultCode.ParamIllegal, "请选择一个区域", null);
            }

            UserVo user = iAccountFactory.findUserById(userArea.getUserid());
            if (user == null) {
                return resp(ResultCode.ParamIllegal, "该userid的账号不存在 ", null);
            }

            auditLog(OperateTypeEnum.USER_ACCOUNT, "分配区域权限");
            iAccountFactory.addUserArea(userArea);
            return resp(ResultCode.Success, AuthResultCode.SUCCESS, null);
        } catch (Exception e) {
            LOGGER.error("edit_user_area", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    private String generateCode() {
        Random random = new Random();
        String result = "";
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(26);
            num += 65;
            boolean flag = false;
            for (int item : list) {
                if (num == item) {
                    flag = true;
                    i--;
                    break;
                }
            }
            if (!flag) {
                list.add(num);
                String temp = (char) num + "";
                result += temp;
            }
        }
        return result;
    }
}
