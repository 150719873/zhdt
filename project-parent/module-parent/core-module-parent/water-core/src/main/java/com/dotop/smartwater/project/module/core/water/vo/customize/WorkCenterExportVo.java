package com.dotop.smartwater.project.module.core.water.vo.customize;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterFormVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterExportVo extends BaseVo {

	private WorkCenterTmplVo tmpl;

	private WorkCenterFormVo form;

	private List<DictionaryVo> dictionarys;

	private Date exportDate;

	private String version;

	private String sign;

}
