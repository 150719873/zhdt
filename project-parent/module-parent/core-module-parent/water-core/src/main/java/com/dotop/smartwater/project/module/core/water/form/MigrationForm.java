package com.dotop.smartwater.project.module.core.water.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设别迁移(接收Form)

 * @date 2019-08-12
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MigrationForm extends BaseForm {
	
	//迁移设备列表
	private List<DeviceMigrationForm> list;
	//迁移历史Form
	private DeviceMigrationHistoryForm deviceMigrationHistoryForm;
}
