package com.dotop.smartwater.project.auth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.auth.dao.IAreaDao;
import org.apache.commons.lang3.StringUtils;
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
import com.dotop.smartwater.project.auth.cache.AreaNodeMapCacheDao;
import com.dotop.smartwater.project.auth.cache.EnterpriseCacheDao;
import com.dotop.smartwater.project.auth.dao.IEnterpriseDao;
import com.dotop.smartwater.project.auth.dao.IUserDao;
import com.dotop.smartwater.project.auth.service.IAccountService;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.bo.SettlementBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserAreaBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.dto.EnterpriseDto;
import com.dotop.smartwater.project.module.core.auth.dto.UserDto;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class AccountServiceImpl implements IAccountService {

	private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);

	@Autowired
	private IUserDao iUserDao;

	@Autowired
	private IEnterpriseDao iEnterpriseDao;

	@Autowired
	private IAreaDao iAreaDao;

	@Autowired
	private AreaNodeMapCacheDao areaNodeMapCacheDao;

	@Autowired
	private EnterpriseCacheDao enterpriseCacheDao;

	@Override
	public UserVo login(UserBo user) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			UserVo userLogin = iUserDao.login(userDto);
			if (userLogin == null && StringUtils.isNotEmpty(user.getWorknum())) {
				userLogin = iUserDao.loginByWorkNum(userDto);
			}
			return userLogin;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int addUser(UserBo user) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);

			iUserDao.add(userDto);
			return 1;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public UserVo findUserByAccount(UserBo user) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);

			return iUserDao.findUserByAccount(userDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public UserVo findUserByWorknum(UserBo user) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);

			return iUserDao.findUserByWorknum(userDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public EnterpriseVo findEnterpriseById(String enterpriseid) throws FrameworkRuntimeException {
		try {
			EnterpriseVo enterprise = enterpriseCacheDao.getEnterprise(enterpriseid);
			if (enterprise == null) {
				enterprise = iEnterpriseDao.findById(enterpriseid);
			}
			return enterprise;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public EnterpriseVo findEnterpriseByWebsite(String website) throws FrameworkRuntimeException {
		try {
			return iEnterpriseDao.findEnterpriseByWebsite(website);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public SettlementVo getSettlement(String enterpriseid) throws FrameworkRuntimeException {
		try {
			return iEnterpriseDao.getSettlement(enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int editUser(UserBo user) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);

			return iUserDao.update(userDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	@Override
	public int editUserState(UserBo user) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);

			return iUserDao.update(userDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public UserVo findUserByAccountAndId(UserBo user) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);

			return iUserDao.findUserByAccountAndId(userDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public UserVo findUserByWorknumAndId(UserBo user) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);

			return iUserDao.findUserByWorknumAndId(userDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<UserVo> getUserList(UserBo user) throws FrameworkRuntimeException {
		try {
			//判断用户列表中用户是否存在已失效用户,超过失效时间置为失效状态
			list(user);
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(user.getPage(), user.getPageCount());
			List<UserVo> list = iUserDao.getUserList(userDto);
			// 拼接数据返回
			return new Pagination<>(user.getPage(), user.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<UserVo> list(UserBo user) throws FrameworkRuntimeException {
		try {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			List<UserVo> list = iUserDao.getUserList(userDto);
			Date curr = new Date();
			//判断用户列表中用户是否存在已失效用户,超过失效时间置为失效状态
			for(UserVo u: list) {
				if(u.getFailuretime() != null && (u.getFailuretime().getTime() <= curr.getTime()) && u.getFailurestate() != new Integer(UserVo.USER_FAILURESTATE_INVALID)) {
					UserDto data = new UserDto();
					data.setUserid(u.getUserid());
					data.setFailurestate(UserVo.USER_FAILURESTATE_INVALID);
					iUserDao.update(data);
				}
			}
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public UserVo findUserById(String userid) throws FrameworkRuntimeException {
		try {
			return iUserDao.findUserById(userid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int addEnterprise(EnterpriseBo enterprise) throws FrameworkRuntimeException {
		try {
			// 参数转换
			EnterpriseDto enterpriseDto = new EnterpriseDto();
			BeanUtils.copyProperties(enterprise, enterpriseDto);

			EnterpriseVo enterpriseVo = new EnterpriseVo();
			BeanUtils.copyProperties(enterprise, enterpriseVo);

			int flag = iEnterpriseDao.insert(enterpriseDto);
			if (flag > 0) {
				iEnterpriseDao.updateEnprno(enterpriseDto.getEnterpriseid());
				enterpriseCacheDao.setEnterprise(enterpriseVo);
			}
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int editEnterprise(EnterpriseBo enterprise) throws FrameworkRuntimeException {
		try {
			// 参数转换
			EnterpriseDto enterpriseDto = new EnterpriseDto();
			BeanUtils.copyProperties(enterprise, enterpriseDto);

			EnterpriseVo enterpriseVo = new EnterpriseVo();
			BeanUtils.copyProperties(enterprise, enterpriseVo);

			int flag = iEnterpriseDao.update(enterpriseDto);
			if (flag > 0) {
				enterpriseCacheDao.setEnterprise(enterpriseVo);
			}
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int delEnterprise(String enterpriseid) throws FrameworkRuntimeException {
		try {
			int flag = iEnterpriseDao.deleteById(enterpriseid);
			if (flag > 0) {
				iAreaDao.delAreaByEid(enterpriseid);
				enterpriseCacheDao.delEnterprise(enterpriseid);
			}
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<EnterpriseVo> getEnterpriseList(EnterpriseBo enterprise) throws FrameworkRuntimeException {
		try {
			// 参数转换
			EnterpriseDto enterpriseDto = new EnterpriseDto();
			BeanUtils.copyProperties(enterprise, enterpriseDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(enterprise.getPage(), enterprise.getPageCount());
			List<EnterpriseVo> list = iEnterpriseDao.getEnterpriseList(enterpriseDto);
			// 拼接数据返回
			return new Pagination<>(enterprise.getPage(), enterprise.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public EnterpriseVo findEnterpriseByName(EnterpriseBo enterprise) throws FrameworkRuntimeException {
		try {
			// 参数转换
			EnterpriseDto enterpriseDto = new EnterpriseDto();
			BeanUtils.copyProperties(enterprise, enterpriseDto);

			return iEnterpriseDao.findEnterpriseByName(enterpriseDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<UserVo> getUsers(String userIds) throws FrameworkRuntimeException {
		try {
			return iUserDao.getUsers(userIds);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public EnterpriseVo findEnterpriseByNameAndId(EnterpriseBo enterprise) throws FrameworkRuntimeException {
		try {
			// 参数转换
			EnterpriseDto enterpriseDto = new EnterpriseDto();
			BeanUtils.copyProperties(enterprise, enterpriseDto);

			return iEnterpriseDao.findEnterpriseByNameAndId(enterpriseDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int addUserArea(UserAreaBo userArea) throws FrameworkRuntimeException {
		try {
			areaNodeMapCacheDao.delAreaNodeMap(userArea.getUserid());
			int result = 0;
			result += iUserDao.delUserArea(userArea.getUserid());
			result += iUserDao.addUserArea(userArea.getUserid(), userArea.getAreaids());
			return result;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public LogoVo getLogo(String enterpriseid) throws FrameworkRuntimeException {
		return null;
	}

	@Override
	public int addSettlement(SettlementBo settlement) throws FrameworkRuntimeException {
		return 0;
	}

	@Override
	public int changePwd(String userid, String newPwd) throws FrameworkRuntimeException {
		return 0;
	}

	@Override
	public List<UserVo> getEnterpriseList(UserVo vo) throws FrameworkRuntimeException {
		return new ArrayList<>();
	}

}
