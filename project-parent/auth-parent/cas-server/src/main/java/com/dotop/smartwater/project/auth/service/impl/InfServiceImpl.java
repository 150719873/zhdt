package com.dotop.smartwater.project.auth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.dotop.smartwater.project.auth.dao.IAreaDao;
import com.dotop.smartwater.project.auth.dao.IEnterpriseDao;
import com.dotop.smartwater.project.auth.dao.IMenuDao;
import com.dotop.smartwater.project.auth.dao.IRoleDao;
import com.dotop.smartwater.project.auth.service.IInfService;
import com.dotop.smartwater.project.module.core.auth.dto.MenuDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.auth.cache.AreaNodeMapCacheDao;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.auth.vo.select.Obj;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class InfServiceImpl implements IInfService {

    private static final Logger LOGGER = LogManager.getLogger(InfServiceImpl.class);

    @Resource
    private IEnterpriseDao iEnterpriseDao;

    @Resource
    private IRoleDao iRoleDao;

    @Resource
    private IAreaDao iAreaDao;

    @Resource
    private IMenuDao iMenuDao;

    @Resource
    private AreaNodeMapCacheDao areaNodeMapCacheDao;

    @Override
    public List<EnterpriseVo> getErpList() throws FrameworkRuntimeException {
        try {
            return iEnterpriseDao.getErpList();
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, EnterpriseVo> getEnterpriseMap() throws FrameworkRuntimeException {
        try {
            return iEnterpriseDao.getEnterpriseMap();
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, MenuVo> getPermissions(UserVo user, String modelId) throws FrameworkRuntimeException {
        List<MenuVo> list = null;
        Map<String, MenuVo> map = new HashMap<>();
        try {
            // 最高管理员
            if (user.getType() == UserVo.USER_TYPE_ADMIN) {
                list = iRoleDao.getAdminTreeByModelid(modelId);
            }

            // 水司管理员
            if (user.getType() == UserVo.USER_TYPE_ADMIN_ENTERPRISE) {
                String proleId = null;
                if (user.getEnterprise() != null && user.getEnterprise().getProleid() != null) {
                    proleId = user.getEnterprise().getProleid();
                }
                if (proleId == null) {
                    return map;
                }
                // 二级
                list = iRoleDao.getMenuByModelidAndProleid(modelId, proleId);
                //证明二级菜单下有权限，要把一级菜单ID带上  //修复 by KJR
                if (list.size() > 0) {
                    map = list.stream().collect(Collectors.toMap(MenuVo::getMenuid, p -> p));
                    MenuVo menuVo = map.get(modelId);
                    if (menuVo == null) {
                        MenuDto menuDto = new MenuDto();
                        menuDto.setMenuid(modelId);
                        menuVo = iMenuDao.get(menuDto);
                        if (menuVo != null) {
                            map.put(modelId, menuVo);
                        }
                    }
                    return map;
                }
            }
            // 普通系统用户,跟权限挂钩
            if (user.getType() == UserVo.USER_TYPE_ENTERPRISE_NORMAL) {
                // 二级
                list = iRoleDao.getMenuByRoleidAndModelid(user.getRoleid(), modelId);
                //证明二级菜单下有权限，要把一级菜单ID带上  //修复 by KJR
                if (list.size() > 0) {
                    map = list.stream().collect(Collectors.toMap(MenuVo::getMenuid, p -> p));
                    MenuVo menuVo = map.get(modelId);
                    if (menuVo == null) {
                        MenuDto menuDto = new MenuDto();
                        menuDto.setMenuid(modelId);
                        menuVo = iMenuDao.get(menuDto);
                        if (menuVo != null) {
                            map.put(modelId, menuVo);
                        }
                    }
                    return map;
                }
            }

            if (list != null && !list.isEmpty()) {
                map = list.stream().collect(Collectors.toMap(MenuVo::getMenuid, p -> p));
            }
            return map;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, AreaNodeVo> getAreas(String eId) throws FrameworkRuntimeException {
        try {
            Map<String, AreaNodeVo> map = areaNodeMapCacheDao.getAreaNodeMap(eId);
            if (map == null) {
                map = iAreaDao.getAreaMapByEid(eId);
                areaNodeMapCacheDao.setAreaNodeMap(eId, map);
            }
            return map;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, AreaNodeVo> getAreasByUserId(String userId) throws FrameworkRuntimeException {
        try {
            Map<String, AreaNodeVo> map = areaNodeMapCacheDao.getAreaNodeMap(userId);
            if (map == null || map.isEmpty()) {
                map = iAreaDao.getAreaMapByUserid(userId);
                areaNodeMapCacheDao.setAreaNodeMap(userId, map);
            }
            return map;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<SettlementVo> getSettlements() throws FrameworkRuntimeException {
        try {
            return iEnterpriseDao.getSettlements();
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public AreaVo getAreaById(String id) throws FrameworkRuntimeException {
        try {
            return iAreaDao.getAreaById(id);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Obj> getOrganizationChartMap(String enterpriseid) throws FrameworkRuntimeException {
        try {
            Map<String, Obj> map = iEnterpriseDao.getOrganizationChartMap(enterpriseid);
            Obj obj = new Obj();
            obj.setId(WaterConstants.SELECT_TYPE_DEFAULT_ROLE_ID);
            obj.setName(WaterConstants.SELECT_TYPE_DEFAULT_ROLE_NAME);
            map.put(WaterConstants.SELECT_TYPE_DEFAULT_ROLE_ID, obj);
            return map;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

}
