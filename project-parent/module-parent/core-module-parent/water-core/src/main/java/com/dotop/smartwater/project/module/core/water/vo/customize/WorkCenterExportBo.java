package com.dotop.smartwater.project.module.core.water.vo.customize;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterFormBo;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterExportBo extends BaseVo {

	private WorkCenterTmplBo tmpl;

	private WorkCenterFormBo form;

	private List<DictionaryBo> dictionarys;

	private Date exportDate;

	private String version;

	private String sign;

}
