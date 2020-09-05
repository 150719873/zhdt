package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IDictionaryFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DictionaryForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;

/**
 * @program: project-parent
 * @description: 字典管理

 * @create: 2019-03-04 09:05
 **/
@RestController

@RequestMapping("/dictionary")
public class DictionaryController implements BaseController<DictionaryForm> {

	@Autowired
	private IDictionaryFactory iDictionaryFactory;

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody DictionaryForm dictionaryForm) {
		iDictionaryFactory.add(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody DictionaryForm dictionaryForm) {
		iDictionaryFactory.edit(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody DictionaryForm dictionaryForm) {
		iDictionaryFactory.del(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody DictionaryForm dictionaryForm) {
		return resp(ResultCode.Success, ResultCode.SUCCESS, iDictionaryFactory.get(dictionaryForm));
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DictionaryForm dictionaryForm) {
		Pagination<DictionaryVo> pagination = iDictionaryFactory.page(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody DictionaryForm dictionaryForm) {
		List<DictionaryVo> list = iDictionaryFactory.list(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	@PostMapping(value = "/getChildren", produces = GlobalContext.PRODUCES)
	public String getChildren(@RequestBody DictionaryForm dictionaryForm) {

		VerificationUtils.string("dictionaryCode", dictionaryForm.getDictionaryCode());

		List<DictionaryChildVo> list = iDictionaryFactory.getChildren(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	@PostMapping(value = "/sync/list", produces = GlobalContext.PRODUCES)
	public String syncList(@RequestBody DictionaryForm dictionaryForm) {
		List<DictionaryVo> list = iDictionaryFactory.syncList(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}
	
	@PostMapping(value = "/sync", produces = GlobalContext.PRODUCES)
	public String sync(@RequestBody DictionaryForm dictionaryForm) {

		VerificationUtils.strList("dictionaryIds", dictionaryForm.getDictionaryIds());

		iDictionaryFactory.sync(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
	
	@PostMapping(value = "/initialize", produces = GlobalContext.PRODUCES)
	public String initialize(@RequestBody DictionaryForm dictionaryForm) {

		VerificationUtils.strList("dictionaryIds", dictionaryForm.getDictionaryIds());

		iDictionaryFactory.initialize(dictionaryForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
}
