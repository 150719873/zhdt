package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.project.auth.api.IAccountFactory;
import com.dotop.smartwater.project.auth.api.IInfFactory;
import com.dotop.smartwater.project.auth.api.IRoleFactory;
import com.dotop.smartwater.project.auth.cache.CBaseDao;
import com.dotop.smartwater.project.auth.cache.EnterpriseRoleCacheDao;
import com.dotop.smartwater.project.auth.cache.PlatformRoleCacheDao;
import com.dotop.smartwater.project.auth.dao.ILogoDao;
import com.dotop.smartwater.project.auth.api.IDataPermissionFactory;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.form.DataTypeForm;
import com.dotop.smartwater.project.module.core.auth.form.EnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.*;
import com.dotop.smartwater.project.module.core.auth.vo.select.Obj;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.utils.MenuUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**

 * @date 2019年5月9日
 * @description
 */
@RestController

@RequestMapping("/auth/api")
public class InterfaceController implements BaseController<UserForm> {

    private static final Logger LOGGER = LogManager.getLogger(InterfaceController.class);

    @Resource
    private ILogoDao iLogoDao;

    @Resource
    private EnterpriseRoleCacheDao enterpriseRoleCacheDao;

    @Resource
    private PlatformRoleCacheDao platformRoleCacheDao;

    @Resource
    private IRoleFactory iRoleFactory;

    @Resource
    private IAccountFactory iAccountFactory;

    @Resource
    private IInfFactory iInfFactory;

    @Autowired
    private IDataPermissionFactory iDataPermissionFactory;

    @Autowired
    private IDistributedLock iDistributedLock;

    @Resource
    private CBaseDao baseDao;

    private static final int Pass = 1;
    private static final int Refuse = 0;
    private int i = 0;
    private int j = 0;

    @PostMapping(value = "/redisLock", produces = GlobalContext.PRODUCES)
    public String redisLock() {
        // 获取分布式锁
        boolean flag = iDistributedLock.lock("test", 2, 50L);

        try {
            if (flag) {
                i = i + 1;
                LOGGER.info(i);
                return resp(ResultCode.Success, "Success", null);
            }
            // false表示已经锁住
            else {
                j = j + 1;
                LOGGER.info(j);
                return resp(ResultCode.Fail, "locking", null);
            }
        } finally {
            // 减少访问一次redis
            if (flag) {
                iDistributedLock.releaseLock("test");
            }
        }
    }

    @PostMapping(value = "/permission", produces = GlobalContext.PRODUCES)
    public String permission(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                             @RequestHeader("mid") String mid) {
        UserVo user = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || user == null) {
            return resp(ResultCode.TimeOut, "用户没登录,请登录", null);
        }

        try {
            Map<String, MenuVo> rolePermission;
            Integer pass = Refuse;
            switch (user.getType()) {
                case UserVo.USER_TYPE_ADMIN:
                    pass = Pass;
                    break;
                case UserVo.USER_TYPE_ADMIN_ENTERPRISE:
                    // 获取水司拥有的平台权限
                    rolePermission = iInfFactory.getPermissions(user, mid);
                    if(rolePermission.get(mid) != null){
                        pass = Pass;
                    }

                    break;
                case UserVo.USER_TYPE_ENTERPRISE_NORMAL:
                    // 获取用户角色拥有的权限
                    if (user.getRoleid() != null) {
                        rolePermission = iInfFactory.getPermissions(user, mid);
                        if(rolePermission.get(mid) != null){
                            pass = Pass;
                        }
                    }
                    break;
                default:
                    break;
            }
            return resp(ResultCode.Success, "Success", pass);
        } catch (Exception e) {
            LOGGER.error("permission", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/enterprises", produces = GlobalContext.PRODUCES)
    public String enterprises(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                              @RequestBody EnterpriseForm etp) {
        try {
            Pagination<EnterpriseVo> pagination;
            UserVo user = baseDao.getRedisUser(userid);
            if (!baseDao.webAuth(userid, ticket) || user == null) {
                return resp(ResultCode.TimeOut, "账号失效,请重新登录", null);
            }
            pagination = iAccountFactory.getEnterpriseList(etp);
            return resp(ResultCode.Success, "SUCCESS", pagination);
        } catch (Exception e) {
            LOGGER.error("enterprises", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/getUsers", produces = GlobalContext.PRODUCES)
    public String getUsers(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                           @RequestHeader("userIds") String userIds) {
        try {
            if (!baseDao.webAuth(userid, ticket)) {
                return resp(ResultCode.TimeOut, "账号失效,请重新登录", null);
            }

            List<UserVo> list = iAccountFactory.getUsers(userIds);

            return resp(ResultCode.Success, "SUCCESS", list);
        } catch (Exception e) {
            LOGGER.error("enterprises", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/enterpriseMap", produces = GlobalContext.PRODUCES)
    public String enterpriseMap(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
        try {
            UserVo user = baseDao.getRedisUser(userid);
            if (!baseDao.webAuth(userid, ticket) || user == null) {
                return resp(ResultCode.TimeOut, "账号失效,请重新登录", null);
            }
            Map<String, EnterpriseVo> map = iInfFactory.getEnterpriseMap();

            return resp(ResultCode.Success, "Success", map);
        } catch (Exception e) {
            LOGGER.error("enterpriseMap", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/user", produces = GlobalContext.PRODUCES)
    public String getUserInfo(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
                              @RequestHeader("modelid") String modelid) {
        UserVo user = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || user == null) {
            return resp(ResultCode.TimeOut, "验证失败,请重新登录", null);
        }

        try {
            if (StringUtils.isBlank(modelid)) {
                return resp(ResultCode.ParamIllegal, "modelid 为空", null);
            }

            UserParamVo userParamVo = new UserParamVo();
            BeanUtils.copyProperties(user, userParamVo);
            userParamVo.setTicket(ticket);

            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setUserParamVo(userParamVo);
            userInfoVo.setPermission(iInfFactory.getPermissions(user, modelid));

            // 系统管理员
            List<MenuVo> list = new ArrayList<>();
            if (user.getType() == 0) {
                list = iRoleFactory.getAdminMenuByParentid("0");
            }

            // 水司管理员
            if (user.getType() == UserVo.USER_TYPE_ADMIN_ENTERPRISE) {
                // 只获取一级
                String proleId = null;
                if (user.getEnterprise() != null) {
                    if (user.getEnterprise().getProleid() != null) {
                        proleId = user.getEnterprise().getProleid();
                    }
                }
                if (proleId == null) {
                    return resp(ResultCode.ParamIllegal, "该水司没有开通服务权限", null);
                }
                list = MenuUtils.filterChild(getMenus(iRoleFactory.getMenuByParentidAndProleid(null, proleId)));
            }

            // 普通系统用户,跟权限挂钩
            if (user.getType() == 2) {
                list = MenuUtils.filterChild(getMenus(iRoleFactory.getMenuByRoleid(user.getRoleid(), null)));
            }

            userInfoVo.setEnterprise(user.getEnterprise());
            userInfoVo.setTopMenuList(list);

            if (user.getSettlement() != null && user.getSettlement().getAlias() != null) {
                userInfoVo.setAlias(user.getSettlement().getAlias());
            }

            LogoVo logo = iLogoDao.getLogo(user.getEnterpriseid());
            if (logo != null && logo.getOssurl() != null) {
                userInfoVo.setLogoUrl(logo.getOssurl());
            }

            //业务权限
            switch (user.getType()) {
                case WaterConstants.USER_TYPE_ADMIN:
                case WaterConstants.USER_TYPE_ADMIN_ENTERPRISE:
                    userInfoVo.setBusinessPermission("pass");
                    break;
                default:
                    DataTypeForm dataTypeForm = new DataTypeForm();
                    dataTypeForm.setId(user.getRoleid());
                    List<CheckboxOptionVo> vos = iDataPermissionFactory.loadPermissionById(dataTypeForm);
                    if (vos == null || vos.size() == 0) {
                        userInfoVo.setBusinessPermission("deny");
                    } else {
                        String data = "";
                        for (CheckboxOptionVo vo : vos) {
                            if (vo.isChecked()) {
                                data += vo.getValue() + ",";
                            }
                        }
                        userInfoVo.setBusinessPermission("".equals(data) ? "deny" : data);
                    }

            }
            return resp(ResultCode.Success, "Success", userInfoVo);
        } catch (Exception e) {
            LOGGER.error("getUserInfo", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    private List<MenuVo> getMenus(List<MenuVo> menus){
        if(CollectionUtils.isEmpty(menus)){
            return Collections.emptyList();
        }
        List<String> mids = new ArrayList<>();
        for(MenuVo m:menus){
            mids.add(m.getMenuid());
        }

        List<String> ids = new ArrayList<>();
        for(MenuVo m:menus){
            if(!"0".equals(m.getParentid()) && !mids.contains(m.getParentid())){
                ids.add(m.getParentid());
            }
        }

        if(!CollectionUtils.isEmpty(ids)){
            List<MenuVo> menuVoList =  iRoleFactory.getMenus(ids);
            menuVoList.addAll(menus);
            return getMenus(menuVoList);
        }else{
            return menus;
        }
    }

    @PostMapping(value = "/getCasKey", produces = GlobalContext.PRODUCES)
    public String getCasKey(@RequestBody UserLoginVo userLogin) {
        UserVo user = baseDao.getRedisUser(userLogin.getUserid());
        if (!baseDao.webAuth(userLogin.getUserid(), userLogin.getTicket()) || user == null) {
            return resp(ResultCode.AccountInvalid, "验证失败,请重新登录", null);
        }
        try {
            if (StringUtils.isBlank(userLogin.getModelid())) {
                return resp(ResultCode.ParamIllegal, "modelid 为空", null);
            }

            return resp(ResultCode.Success, "Success", CBaseDao.generateCasKey(userLogin));
        } catch (Exception e) {
            LOGGER.error("getCasKey", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/getCompanyAreas", produces = GlobalContext.PRODUCES)
    public String getCompanyAreas(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
        UserVo user = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || user == null) {
            return resp(ResultCode.AccountInvalid, "验证失败,请重新登录", null);
        }
        try {
            Map<String, AreaNodeVo> map = null;
            List<AreaNodeVo> list;
            // 主账号 返回全部区域
            if (UserVo.USER_TYPE_ADMIN_ENTERPRISE == user.getType()) {
                map = iInfFactory.getAreas(user.getEnterpriseid());
            }

            // 子账号 返回部分区域
            if (UserVo.USER_TYPE_ENTERPRISE_NORMAL == user.getType()) {
                map = iInfFactory.getAreasByUserId(user.getUserid());
            }
            list = mapToListTree(map);
            return resp(ResultCode.Success, "Success", list);
        } catch (Exception e) {
            LOGGER.error("getCompanyAreas", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    public static List<AreaNodeVo> mapToListTree(Map<String, AreaNodeVo> treeMap) {
        List<AreaNodeVo> nodeTrees = new ArrayList<>(treeMap.size());
        if (treeMap == null || treeMap.isEmpty()) {
            return nodeTrees;
        }
        for (Entry<String, AreaNodeVo> entry : treeMap.entrySet()) {
            AreaNodeVo treeNode = entry.getValue();
            String pid = treeNode.getPId();
            if (pid == null || pid.length() == 0 || !treeMap.containsKey(pid)) {
                treeNode.setPId("0");
                nodeTrees.add(treeNode);
            } else {
                AreaNodeVo parentTreeNode = treeMap.get(pid);
                if (parentTreeNode.getChildren() == null) {
                    parentTreeNode.setChildren(new ArrayList<>());
                }
                parentTreeNode.setIsLeaf(false);
                parentTreeNode.getChildren().add(treeNode);
            }
        }
        return nodeTrees;
    }

    @PostMapping(value = "/getAreaMaps", produces = GlobalContext.PRODUCES)
    public String getAreaMaps(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
        UserVo user = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || user == null) {
            return resp(ResultCode.AccountInvalid, "验证失败,请重新登录", null);
        }
        try {
            Map<String, AreaNodeVo> map = null;
            // 主账号 返回全部区域
            if (UserVo.USER_TYPE_ADMIN_ENTERPRISE == user.getType()) {
                map = iInfFactory.getAreas(user.getEnterpriseid());
            }

            // 子账号 返回部分区域
            if (UserVo.USER_TYPE_ENTERPRISE_NORMAL == user.getType()) {
                map = iInfFactory.getAreasByUserId(user.getUserid());
            }
            return resp(ResultCode.Success, "Success", map);
        } catch (Exception e) {
            LOGGER.error("getAreaMaps", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    // @PostMapping(value = "/getUserLora", produces = GlobalContext.PRODUCES)
    // public String getUserLora(@RequestHeader("enterpriseid") String enterpriseid)
    // {
    // LOGGER.info("getUserLora --> enterpriseid :" + enterpriseid);
    // String eId = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1,
    // WaterClientConfig.WaterCasKey2,
    // enterpriseid);
    // if (eId == null) {
    // return null;
    // }
    // return resp(ResultCode.Success, "Success",
    // iUserLoraFactory.findByEnterpriseId(eId));
    // }

    // @PostMapping(value = "/getSettlements", produces = GlobalContext.PRODUCES)
    // public String getSettlements(@RequestHeader("key") String key,
    // @RequestHeader("value") String value) {
    // String dKey = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1,
    // WaterClientConfig.WaterCasKey2, value);
    //
    // if (!key.equals(JSONUtils.parseObject(dKey, String.class))) {
    // return resp(ResultCode.Fail, "不是合法请求", null);
    // }
    // try {
    //
    // return resp(ResultCode.Success, "Success", iInfFactory.getSettlements());
    // } catch (Exception e) {
    // LOGGER.error("getSettlements", e);
    // return resp(ResultCode.Fail, e.getMessage(), null);
    // }
    // }

    // @PostMapping(value = "/getSettlement", produces = GlobalContext.PRODUCES)
    // public String getSettlement(@RequestHeader("key") String key,
    // @RequestHeader("value") String value,
    // @RequestHeader("userid") String userid, @RequestHeader("ticket") String
    // ticket) {
    // String dKey = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1,
    // WaterClientConfig.WaterCasKey2, value);
    // UserVo user = baseDao.getRedisUser(userid);
    // if (!key.equals(JSONUtils.parseObject(dKey, String.class))) {
    // return resp(ResultCode.Fail, "不是合法请求", null);
    // }
    // try {
    // return resp(ResultCode.Success, "Success",
    // iSettlementFactory.getSettlement(user.getEnterpriseid()));
    // } catch (Exception e) {
    // LOGGER.error("getSettlements", e);
    // return resp(ResultCode.Fail, e.getMessage(), null);
    // }
    // }

    // @PostMapping(value = "/getEnterpriseSettlement", produces =
    // GlobalContext.PRODUCES)
    // public String getEnterpriseSettlement(@RequestHeader("enterpriseid") String
    // enterpriseid) {
    // try {
    // return resp(ResultCode.Success, "Success",
    // iSettlementFactory.getSettlement(enterpriseid));
    // } catch (Exception e) {
    // LOGGER.error("getSettlements", e);
    // return resp(ResultCode.Fail, e.getMessage(), null);
    // }
    // }

    @PostMapping(value = "/getCompanyInfo", produces = GlobalContext.PRODUCES)
    public String getCompanyInfo(@RequestHeader("key") String key, @RequestHeader("value") String value,
                                 @RequestHeader("eid") String eid) {
        String dKey = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2, value);

        if (!key.equals(JSONUtils.parseObject(dKey, String.class))) {
            return resp(ResultCode.Fail, "不是合法请求", null);
        }
        try {

            return resp(ResultCode.Success, "Success", iAccountFactory.findEnterpriseById(eid));
        } catch (Exception e) {
            LOGGER.error("getCompanyInfo", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/getAreaById", produces = GlobalContext.PRODUCES)
    public String getAreaById(@RequestHeader("key") String key, @RequestHeader("value") String value,
                              @RequestHeader("cid") String cid) {
        String dKey = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2, value);

        if (!key.equals(JSONUtils.parseObject(dKey, String.class))) {
            return resp(ResultCode.Fail, "不是合法请求", null);
        }
        try {
            return resp(ResultCode.Success, "Success", iInfFactory.getAreaById(cid));
        } catch (Exception e) {
            LOGGER.error("getAreaById", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

    /**
     * 获取人员组织架构名称
     */
    @PostMapping(value = "/getOrganizationChart", produces = GlobalContext.PRODUCES)
    public String getOrganizationChart(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
        UserVo user = baseDao.getRedisUser(userid);
        if (!baseDao.webAuth(userid, ticket) || user == null) {
            return resp(ResultCode.TimeOut, "验证失败,请重新登录", null);
        }
        try {
            Map<String, Obj> map = iInfFactory.getOrganizationChartMap(user.getEnterpriseid());
            return resp(ResultCode.Success, "Success", map);
        } catch (Exception e) {
            LOGGER.error("getOrganizationChart", e);
            return resp(ResultCode.Fail, e.getMessage(), null);
        }
    }

}
