package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeEdgeVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodePointVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeEdgeVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodePointVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**

 * @date 2019/7/26.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterTmplNodeGraphVo extends BaseVo {

	private List<WorkCenterTmplNodePointVo> nodes;

	private List<WorkCenterTmplNodeEdgeVo> edges;
}
