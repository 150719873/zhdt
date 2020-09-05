package com.dotop.pipe.web.factory.common;

import com.dotop.pipe.api.service.common.ICommonService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.form.DictionaryForm;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.common.NumRuleVo;
import com.dotop.pipe.web.api.factory.common.ICommonFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 */
@Component
public class CommonFactoryImpl implements ICommonFactory {

	private final static Logger logger = LogManager.getLogger(CommonFactoryImpl.class);

	private static Boolean lock = true;

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private ICommonService iCommonService;

	@Override
	public DictionaryVo getByType(String type) throws FrameworkRuntimeException {
		return iCommonService.getByType(type);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DictionaryVo add(DictionaryForm dictionaryForm) throws FrameworkRuntimeException {
		// Date curr = new Date();
		// LoginCas loginCas = iAuthCasApi.get();
		// String operEid = loginCas.getOperEid();
		// String userBy = loginCas.getUserName();
		// 所需参数

		String name = dictionaryForm.getName();
		String val = dictionaryForm.getVal();
		String des = dictionaryForm.getDes();
		String unit = dictionaryForm.getUnit();
		String type = dictionaryForm.getType();

		DictionaryVo result = new DictionaryVo();
		// 根据类型和名称检验是否唯一
		DictionaryVo dict = iCommonService.getByTypeAndName(type, name);
		if (dict != null) {
			// 若再该类型下已存字典，则判断是否已删除
			if (dict.getIsDel() == 1) {
				// 是已删除则取消删除状态并设置其他值
				iCommonService.edit(name, val, type, des, unit, dict.getId());
				result.setId(dict.getId());
			} else {
				// 不是已删除则新增失败
				logger.error(LogMsg.to("ex", PipeExceptionConstants.DICTIONARY_EXIST));
				throw new FrameworkRuntimeException(PipeExceptionConstants.DICTIONARY_EXIST,
						PipeExceptionConstants.getMessage(PipeExceptionConstants.DICTIONARY_EXIST));
			}
		} else {
			result = iCommonService.add(name, val, type, des, unit);
		}
		return result;
	}

	@Override
	public Pagination<DictionaryVo> page(DictionaryForm dictionaryForm) throws FrameworkRuntimeException {
		// LoginCas loginCas = iAuthCasApi.get();
		// String operEid = loginCas.getOperEid();
		// 所需参数
		Integer page = dictionaryForm.getPage();
		Integer pageSize = dictionaryForm.getPageSize();
		return iCommonService.page(dictionaryForm.getType(), page, pageSize);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(DictionaryForm dictionaryForm) throws FrameworkRuntimeException {
		// LoginCas loginCas = iAuthCasApi.get();
		// String operEid = loginCas.getOperEid();
		// String userBy = loginCas.getUserName();
		// 所需参数
		String id = dictionaryForm.getId();

		// DictionaryVo d = iCommonService.get(id);
		// String val = d.getVal();
		// List<DictionaryVo> list = iCommonService.getVo(val);
		// if (list != null && list.size() > 0) {
		// logger.error(LogMsg.to("ex", PipeExceptionConstants.DICTIONARY_USED));
		// throw new FrameworkRuntimeException(PipeExceptionConstants.DICTIONARY_USED,
		// PipeExceptionConstants.getMessage(PipeExceptionConstants.DICTIONARY_USED));
		// }
		iCommonService.del(id);
		return null;
	}

	@Override
	public Map<String, String> getMaxCode(Map<String, Object> queryParams) {
		System.out.println("打印编号规则");
		List<String> typeList = (List<String>) queryParams.get("type");
		Integer count = (Integer) queryParams.get("count");
		Map<String, String> map = new HashMap<>();
		synchronized (lock) {
			LoginCas loginCas = iAuthCasApi.get();
			String operEid = loginCas.getOperEid();
			String userby = loginCas.getUserName();
			for (String type : typeList) {
				NumRuleVo numRuleVo = iCommonService.getMaxCode(type, operEid);
				Integer maxValue = 10000;
				if (numRuleVo != null) {
					// 更新编号的最大值
					maxValue = numRuleVo.getMaxValue();
					iCommonService.editNumRule(numRuleVo.getId(), maxValue + count);
					map.put(type, numRuleVo.getTitle() + maxValue);
				} else {
					String title = type;
					if ("customize_device".equals(type)) {
						title = "customize";
					} else if ("water_factory".equals(type)) {
						title = "water";
					} else if ("slops_factory".equals(type)) {
						title = "slops";
					}
					// 新增一个编号
					iCommonService.addNumRule(type, title, maxValue + count, userby, new Date(), operEid);
					map.put(type, title + maxValue);
				}
				System.out.println("占用资源");
			}
		}
		return map;
	}
}
