package com.dotop.smartwater.project.module.core.water.vo.customize;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MakeNumVo extends BaseVo {

	private Integer status;
	private List<String> numbers;

}
