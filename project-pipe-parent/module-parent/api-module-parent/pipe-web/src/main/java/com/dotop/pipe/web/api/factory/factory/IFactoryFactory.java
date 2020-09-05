//package com.dotop.pipe.web.api.factory.factory;
//
//import java.util.List;
//
//import com.dotop.pipe.core.form.FactoryForm;
//import com.dotop.pipe.core.vo.factory.FactoryVo;
//import com.dotop.smartwater.dependence.core.common.BaseFactory;
//import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
//import com.dotop.smartwater.dependence.core.pagination.Pagination;
//
///**
// *
// * @date 2018/10/25.
// */
//public interface IFactoryFactory extends BaseFactory<FactoryForm, FactoryVo> {
//
//	/**
//	 * 新增厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 * @throws FrameworkRuntimeException
//	 */
//	FactoryVo add(FactoryForm factoryForm) throws FrameworkRuntimeException;
//
//	/**
//	 * 获取厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 * @throws FrameworkRuntimeException
//	 */
//	FactoryVo get(FactoryForm factoryForm) throws FrameworkRuntimeException;
//
//	/**
//	 * 厂商分页
//	 *
//	 * @param factoryForm
//	 * @return
//	 * @throws FrameworkRuntimeException
//	 */
//	Pagination<FactoryVo> page(FactoryForm factoryForm) throws FrameworkRuntimeException;
//
//	/**
//	 * 列出厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 * @throws FrameworkRuntimeException
//	 */
//	List<FactoryVo> list(FactoryForm factoryForm) throws FrameworkRuntimeException;
//
//	/**
//	 * 编辑厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 * @throws FrameworkRuntimeException
//	 */
//	FactoryVo edit(FactoryForm factoryForm) throws FrameworkRuntimeException;
//
//	/**
//	 * 删除厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 * @throws FrameworkRuntimeException
//	 */
//	String del(FactoryForm factoryForm) throws FrameworkRuntimeException;
//}
