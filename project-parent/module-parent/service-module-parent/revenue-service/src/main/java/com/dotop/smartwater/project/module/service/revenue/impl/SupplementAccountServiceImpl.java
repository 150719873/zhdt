package com.dotop.smartwater.project.module.service.revenue.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.customize.SupplementForm;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SupplementVo;
import com.dotop.smartwater.project.module.dao.revenue.ISupplementAccountDao;
import com.dotop.smartwater.project.module.service.revenue.ISupplementAccountService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 收银核算 -- 平账记录
 * 

 * @date 2019年2月25日
 */
@Service
public class SupplementAccountServiceImpl implements ISupplementAccountService {

	private static final Logger LOGGER = LogManager.getLogger(SupplementAccountServiceImpl.class);

	@Autowired
	private ISupplementAccountDao iSupplementAccountDao;

	@Override
	public List<AccountingVo> findMonthAccounting(SupplementForm sp) {
		try {
			return iSupplementAccountDao.summarySelfDetail(sp);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateSupplement(List<AccountingVo> list, UserVo u) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<SupplementVo> supps = new ArrayList<>();
			for (AccountingVo ating : list) {
				SupplementVo supp = new SupplementVo();
				supp.setSys(ating.getSys());
				supp.setArtificial(ating.getArtificial());
				supp.setDiffer(ating.getDiffer());
				supp.setUserid(String.valueOf(ating.getUserid()));
				supp.setAccount(ating.getAccount());
				supp.setUsername(ating.getUsername());
				supp.setAid(ating.getId());
				supp.setSupplement(CalUtil.sub(0, ating.getDiffer()));
				supp.setAtime(ating.getAtime());
				supp.setExplain("补差" + ating.getSupplement() + "￥");
				if (StringUtils.isBlank(ating.getRemark())) {
					supp.setRemark("");
				} else {
					supp.setRemark(ating.getRemark());
				}
				supp.setStatus(1);
				supp.setOperauserid(u.getUserid());
				supp.setOperausername(u.getName());
				supp.setOperatime(sf.format(new Date()));
				supp.setCreatetime(sf.format(new Date()));
				supp.setEnterpriseid(u.getEnterpriseid());
				supp.setId(UuidUtils.getUuid());
				supps.add(supp);
				// 修改收营员核算信息
				iSupplementAccountDao.updateAaccountStatus(ating.getId());
			}
			if (!supps.isEmpty()) {
				iSupplementAccountDao.batchSupplement(supps);
			}

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public Pagination<SupplementVo> page(SupplementForm sp, UserVo user) {
		try {
			Page<Object> pageHelper = PageHelper.startPage(sp.getPage(), sp.getPageCount());
			List<SupplementVo> list = iSupplementAccountDao.getList(sp, user);
			Pagination<SupplementVo> pagination = new Pagination<>(sp.getPageCount(), sp.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
