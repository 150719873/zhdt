package com.dotop.smartwater.project.module.service.revenue.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;
import com.dotop.smartwater.project.module.dao.revenue.IPurposeDao;
import com.dotop.smartwater.project.module.service.revenue.IPurposeService;

/**
 * Purpose 操作
 * 

 * @date 2019年2月25日
 */
@Service
public class PurposeServiceImpl implements IPurposeService {

	@Autowired
	private IPurposeDao iPurposeDao;

	@Override
	public PurposeVo findById(String purposeId) {
		return iPurposeDao.findById(purposeId);
	}
}
