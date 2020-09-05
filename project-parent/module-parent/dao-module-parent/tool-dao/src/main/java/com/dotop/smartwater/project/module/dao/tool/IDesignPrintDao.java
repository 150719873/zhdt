package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DesignPrintDto;
import com.dotop.smartwater.project.module.core.water.dto.PrintBindDto;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.DesignFieldVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * 打印设计
 *

 * @date 2019-03-07 10:02
 */

public interface IDesignPrintDao extends BaseDao<PrintBindDto, PrintBindVo> {

	List<PrintBindVo> templateList(PrintBindDto printBindDto);

	List<DesignPrintVo> getDesignPrintList(DesignPrintDto designPrintDto);

	/**
	 * 查询详情
	 *
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	DesignPrintVo get(String id);

	/**
	 * 删除打印设计
	 *
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	int deleteDesignPrint(String id);

	/**
	 * 查询字段
	 *
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	List<DesignFieldVo> getFields(String id);

	Boolean saveDesignPrint(DesignPrintDto designPrintDto);

	Boolean deleteDesignPrint(DesignPrintDto designPrintDto);

	int deleteDesignFields(String id);

	void saveField(List<DesignFieldVo> list);

	void updateDesignPrint(DesignPrintDto designPrintDto);

	Map<String, String> getView(@Param("value") String sql);

	List<Map<String, String>> getSqlView(@Param("value") String sql);

	void updateTemplate(DesignPrintDto designPrintDto);

	void addTemplate(DesignPrintDto designPrintDto);

	DesignPrintVo getPrintStatus(@Param("enterpriseid") String enterpriseid, @Param("smstype") Integer smstype);

}
