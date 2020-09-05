package com.dotop.pipe.web.factory.point;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.dto.point.PointDto;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.service.point.IPointMapService;
import com.dotop.pipe.api.service.point.IPointService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.pipe.web.api.factory.point.IPointFactory;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.dependence.lock.LockKey;

/**
 * 
 */
@Component
public class PointFactoryImpl implements IPointFactory {

	private final static Logger logger = LogManager.getLogger(PointFactoryImpl.class);

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private IDistributedLock iDistributedLock;

	@Autowired
	private IPointService iPointService;

	@Autowired
	private IPointMapService iPointMapService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public PointVo add(PointForm pointForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		// 所需参数
		String code = pointForm.getCode();
		String name = pointForm.getName();
		String des = pointForm.getDes();
		BigDecimal longitude = pointForm.getLongitude();
		BigDecimal latitude = pointForm.getLatitude();
		String remark = pointForm.getRemark();

		String dk = null;
		try {
			dk = LockKey.addPipe(operEid, code);
			if (iDistributedLock.lock(dk)) {

				// 检验code是否唯一
				PointVo point = iPointService.getByCode(operEid, code);
				if (point != null) {
					logger.error(LogMsg.to("ex", PipeExceptionConstants.POINT_CODE_EXIST, "code", code));
					throw new FrameworkRuntimeException(PipeExceptionConstants.POINT_CODE_EXIST);
				}

				// 新增坐标
				PointVo pointVo = iPointService.add(operEid, code, name, des, longitude, latitude, remark, curr,
						userBy);
				return pointVo;
			} else {
				throw new FrameworkRuntimeException(BaseExceptionConstants.LOCK_TIMEOUT);
			}
		} finally {
			iDistributedLock.releaseLock(dk);
		}
	}

	@Override
	public PointVo get(PointForm pointForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 所需参数
		String pointId = pointForm.getPointId();
		PointVo point = iPointService.get(operEid, pointId);
		return point;
	}

	@Override
	public Pagination<PointVo> page(PointForm pointForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 所需参数
		Integer page = pointForm.getPage();
		Integer pageSize = pointForm.getPageSize();
		Pagination<PointVo> pagination = iPointService.page(operEid, page, pageSize);
		return pagination;
	}

	@Override
	public List<PointVo> list(PointForm pointForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		// 所需参数
		Integer limit = pointForm.getLimit();
		List<PointVo> list = iPointService.page(operEid, limit);
		return list;
	}

	@Override
	public List<PointVo> realList(PointForm pointForm) throws FrameworkRuntimeException {
		PointDto pointDto = BeanUtils.copy(pointForm, PointDto.class);
		return iPointService.realList(pointDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public PointVo edit(PointForm pointForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		// 所需参数
		String pointId = pointForm.getPointId();
		String code = pointForm.getCode();
		String name = pointForm.getName();
		String des = pointForm.getDes();
		BigDecimal longitude = pointForm.getLongitude();
		BigDecimal latitude = pointForm.getLatitude();
		String remark = pointForm.getRemark();
		// 更新节点
		iPointService.edit(operEid, pointId, code, name, des, longitude, latitude, remark, curr, userBy);
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(PointForm pointForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userBy = loginCas.getUserName();
		// 所需参数
		String pointId = pointForm.getPointId();
		// 检验是否存在使用中的坐标，如果存在不允许删除
		Boolean isExist = iPointMapService.isExist(operEid, pointId);
		if (isExist == null || isExist) {
			logger.error(LogMsg.to("ex", PipeExceptionConstants.POINT_USE, "pointId", pointId));
			throw new FrameworkRuntimeException(PipeExceptionConstants.POINT_USE,
					PipeExceptionConstants.getMessage(PipeExceptionConstants.POINT_USE));
		}
		// 删除节点
		iPointService.del(operEid, pointId, curr, userBy);
		return null;
	}
}
