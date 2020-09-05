package com.dotop.pipe.web.factory.mark;

import com.dotop.pipe.api.service.mark.IMarkService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.mark.MarkBo;
import com.dotop.pipe.core.form.MarkForm;
import com.dotop.pipe.core.vo.mark.MarkVo;
import com.dotop.pipe.web.api.factory.mark.IMarkFactory;
import com.dotop.pipe.web.api.factory.user.IUserFactory;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 
 */
@Component
public class MarkFactoryImpl implements IMarkFactory {

	private final static Logger logger = LogManager.getLogger(MarkFactoryImpl.class);

	@Autowired
	private IMarkService iMarkService;
	@Autowired
	private IAuthCasWeb iAuthCasApi;
	@Autowired
	private IUserFactory iUserFactory;


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MarkVo add(MarkForm markForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		MarkBo markBo = BeanUtils.copy(markForm, MarkBo.class);
		MarkVo markVo = get(markForm);
		if (markVo != null && markVo.getCode().equals(markForm.getCode())) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "编号已存在");
		}
		markBo.setUserId(loginCas.getUserId());
		markBo.setCurr(new Date());
		markBo.setUserBy(loginCas.getUserName());
		markBo.setEnterpriseId(loginCas.getEnterpriseId());
		markBo.setIsDel(0);
		markBo.setStatus("1");
		markBo.setId(UuidUtils.getUuid());
		return iMarkService.add(markBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MarkVo share(MarkForm markForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		MarkBo markBo = new MarkBo();
		markBo.setUserId(markForm.getUserId());
		markBo.setIsDel(0);
		markBo.setCode(markForm.getCode());
		MarkVo markVo = iMarkService.get(markBo);
		if (loginCas.getUserId().equals(markBo.getUserId())) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "不能分享给自己");
		}
		if (markVo != null && markVo.getCode().equals(markForm.getCode())) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "被分享用户已有该编号");
		}
		List<UserVo> userVos = iUserFactory.getUserList(markForm.getEnterpriseId());
		if(userVos.stream().filter(t->t.getUserid().equals(markForm.getUserId())).count() != 1) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该用户不存在");
		}
		markBo.setCurr(new Date());
		markBo.setUserBy(loginCas.getUserName());
		markBo.setEnterpriseId(loginCas.getEnterpriseId());
		markBo.setIsDel(0);
		markBo.setStatus("0");
		markBo.setId(UuidUtils.getUuid());
		return iMarkService.add(markBo);
	}

	@Override
	public MarkVo get(MarkForm markForm) throws FrameworkRuntimeException {
		MarkBo markBo = BeanUtils.copy(markForm, MarkBo.class);
		LoginCas loginCas = iAuthCasApi.get();
		markBo.setEnterpriseId(loginCas.getEnterpriseId());
		markBo.setIsDel(0);
		markBo.setUserId(loginCas.getUserId());
		return iMarkService.get(markBo);
	}

	@Override
	public Pagination<MarkVo> page(MarkForm markForm) throws FrameworkRuntimeException {
		MarkBo markBo = BeanUtils.copy(markForm, MarkBo.class);
		LoginCas loginCas = iAuthCasApi.get();
		markBo.setEnterpriseId(loginCas.getEnterpriseId());
		markBo.setIsDel(0);
		markBo.setUserId(loginCas.getUserId());
		return iMarkService.page(markBo);
	}

	@Override
	public List<MarkVo> list(MarkForm markForm) throws FrameworkRuntimeException {
		MarkBo markBo = BeanUtils.copy(markForm, MarkBo.class);
		LoginCas loginCas = iAuthCasApi.get();
		markBo.setEnterpriseId(loginCas.getEnterpriseId());
		markBo.setIsDel(0);
		markBo.setUserId(loginCas.getUserId());
		return iMarkService.list(markBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MarkVo edit(MarkForm markForm) throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		MarkForm markForm1 = new MarkForm();
		markForm1.setCode(markForm.getCode());
		markForm1.setUserId(loginCas.getUserId());
		MarkVo markVo = get(markForm1);
		if (markVo != null && !markVo.getId().equals(markForm.getId())) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "编号已存在");
		}
		MarkBo markBo = BeanUtils.copy(markForm, MarkBo.class);
		markBo.setCurr(new Date());
		markBo.setIsDel(0);
		markBo.setUserBy(loginCas.getUserName());
		markBo.setEnterpriseId(loginCas.getEnterpriseId());
		return iMarkService.edit(markBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(MarkForm markForm) throws FrameworkRuntimeException {
		MarkBo markBo = BeanUtils.copy(markForm, MarkBo.class);
		LoginCas loginCas = iAuthCasApi.get();
		markBo.setCurr(new Date());
		markBo.setUserBy(loginCas.getUserName());
		markBo.setEnterpriseId(loginCas.getEnterpriseId());
		return iMarkService.del(markBo);
	}
}
