package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.ReduceForm;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;

import java.util.List;

/**

 * @date 2019/2/26.
 */
public interface IReduceFactory extends BaseFactory<ReduceForm, ReduceVo> {
	/**
	 * 获取列表
	 *
	 * @param reduceForm
	 * @return
	 * @
	 */
	List<ReduceVo> getReduces(ReduceForm reduceForm) ;

	/**
	 * 添加
	 *
	 * @param reduceForm
	 * @return
	 * @
	 */
	int addReduce(ReduceForm reduceForm) ;

	/**
	 * 删除
	 *
	 * @param reduceForm
	 * @return
	 * @
	 */
	int delReduce(ReduceForm reduceForm) ;

	/**
	 * 编辑
	 *
	 * @param reduceForm
	 * @return
	 * @
	 */
	int editReduce(ReduceForm reduceForm) ;

	/**
	 * 根据id查找
	 *
	 * @param reduceForm
	 * @return
	 * @
	 */
	ReduceVo findById(ReduceForm reduceForm) ;
}
