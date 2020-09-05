package com.dotop.smartwater.project.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dotop.smartwater.project.auth.dao.IRoleDao;
import com.dotop.smartwater.project.auth.dao.IPlatformRoleDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.auth.dao.IMenuDao;
import com.dotop.smartwater.project.auth.service.IMenuService;
import com.dotop.smartwater.project.module.core.auth.bo.MenuBo;
import com.dotop.smartwater.project.module.core.auth.dto.MenuDto;
import com.dotop.smartwater.project.module.core.auth.vo.MenuVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 菜单管理
 *

 * @date 2019年3月5日 10:52
 */

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class MenuServiceImpl implements IMenuService {

    private static final Logger LOGGER = LogManager.getLogger(MenuServiceImpl.class);

    @Autowired
    private IMenuDao iMenuDao;

    @Autowired
    private IRoleDao iRoleDao;

    @Autowired
    private IPlatformRoleDao iPlatformRoleDao;

    @Override
    public MenuVo add(MenuBo menuBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            MenuDto menuDto = new MenuDto();
            BeanUtils.copyProperties(menuBo, menuDto);
            if (menuBo.getFlag() == 0) {
                MenuDto dto = new MenuDto();
                dto.setMenuid(menuBo.getMenuid());
                MenuVo vo = iMenuDao.adminGet(dto);
                while (vo != null){
                    isExistMenuId(menuBo);
                    dto.setMenuid(menuBo.getMenuid());
                    vo = iMenuDao.adminGet(dto);
                }
                menuDto.setMenuid(menuBo.getMenuid());
                iMenuDao.adminAdd(menuDto);
            } else if (menuBo.getFlag() == 1) {
                MenuDto dto = new MenuDto();
                dto.setMenuid(menuBo.getMenuid());
                MenuVo vo = iMenuDao.get(dto);
                while (vo != null){
                    isExistMenuId(menuBo);
                    dto.setMenuid(menuBo.getMenuid());
                    vo = iMenuDao.get(dto);
                }
                menuDto.setMenuid(menuBo.getMenuid());
                iMenuDao.add(menuDto);
                delParentPermission(menuDto);
            }
            MenuVo menuVo = iMenuDao.get(menuDto);
            return menuVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    private void isExistMenuId(MenuBo menuBo) {
        int menuId;
        String mid;
        switch (menuBo.getLevel()) {
            case 1:
                menuId = Integer.parseInt(menuBo.getMenuid().substring(0, 2)) + 1;
                if(menuId < 10){
                    mid = "0" + String.valueOf(menuId);
                }else{
                    mid = String.valueOf(menuId);
                }
                menuBo.setMenuid(mid + menuBo.getMenuid().substring(2, menuBo.getMenuid().length()));
                break;
            case 2:
                menuId = Integer.parseInt(menuBo.getMenuid().substring(2, 4)) + 1;
                if(menuId < 10){
                    mid = "0" + String.valueOf(menuId);
                }else{
                    mid = String.valueOf(menuId);
                }
                menuBo.setMenuid(menuBo.getMenuid().substring(0, 2) +
                        mid + menuBo.getMenuid().substring(4, menuBo.getMenuid().length()));
                break;
            case 3:
                menuId = Integer.parseInt(menuBo.getMenuid().substring(4, 6)) + 1;
                if(menuId < 10){
                    mid = "0" + String.valueOf(menuId);
                }else{
                    mid = String.valueOf(menuId);
                }
                menuBo.setMenuid(menuBo.getMenuid().substring(0, 4) +
                        mid + menuBo.getMenuid().substring(6, menuBo.getMenuid().length()));
                break;
            default:
                menuId = Integer.parseInt(menuBo.getMenuid()) + 1;
                menuBo.setMenuid(String.valueOf(menuId));
                break;
        }
    }


    private void delParentPermission(MenuDto menuDto) {
        if (menuDto != null && !"0".equals(menuDto.getParentid())) {

            iRoleDao.delPermission(menuDto.getParentid());
            iPlatformRoleDao.delPlatformRolePermission(menuDto.getParentid());

            MenuDto menuDto2 = new MenuDto();
            menuDto2.setMenuid(menuDto.getParentid());
            MenuVo parent = iMenuDao.get(menuDto2);

            MenuDto parentDto = new MenuDto();
            BeanUtils.copyProperties(parent, parentDto);

            delParentPermission(parentDto);
        }
    }

    @Override
    public Pagination<MenuVo> page(MenuBo menuBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            MenuDto menuDto = new MenuDto();
            BeanUtils.copyProperties(menuBo, menuDto);
            if (menuBo.getFlag() == 0) {
                Page<Object> pageHelper = PageHelper.startPage(menuBo.getPage(), menuBo.getPageCount());
                Pagination<MenuVo> pagination = new Pagination<MenuVo>(menuBo.getPageCount(), menuBo.getPage());
                List<MenuVo> list = iMenuDao.adminList(menuDto);
                pagination.setData(list);
                pagination.setTotalPageSize(pageHelper.getTotal());
                return pagination;
            } else if (menuBo.getFlag() == 1) {
                Page<Object> pageHelper = PageHelper.startPage(menuBo.getPage(), menuBo.getPageCount());
                Pagination<MenuVo> pagination = new Pagination<MenuVo>(menuBo.getPageCount(), menuBo.getPage());
                List<MenuVo> list = iMenuDao.list(menuDto);
                pagination.setData(list);
                pagination.setTotalPageSize(pageHelper.getTotal());
                return pagination;
            }
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public MenuVo get(MenuBo menuBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            MenuDto menuDto = new MenuDto();
            BeanUtils.copyProperties(menuBo, menuDto);
            MenuVo menuVo = new MenuVo();
            if (menuDto.getFlag() == 0) {
                menuVo = iMenuDao.adminGet(menuDto);
            } else if (menuDto.getFlag() == 1) {
                menuVo = iMenuDao.get(menuDto);
            }
            return menuVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public MenuVo revise(MenuBo menuBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            MenuDto menuDto = new MenuDto();
            BeanUtils.copyProperties(menuBo, menuDto);
            if (menuBo.getFlag() == 0) {
                iMenuDao.adminRevise(menuDto);
            } else if (menuBo.getFlag() == 1) {
                iMenuDao.revise(menuDto);
            }

            return iMenuDao.get(menuDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Integer del(List<MenuBo> list) throws FrameworkRuntimeException {
        try {
            List<MenuDto> l = new ArrayList<>();
            // 参数转换
            for (MenuBo menuBo : list) {
                MenuDto menuDto = new MenuDto();
                BeanUtils.copyProperties(menuBo, menuDto);
                l.add(menuDto);
            }
            if (l.get(0).getFlag() == 0) {
                return iMenuDao.adminDel(l);
            } else if (l.get(0).getFlag() == 1) {
                return iMenuDao.del(l);
            } else {
                return 0;
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
