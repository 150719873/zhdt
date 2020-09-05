package com.dotop.smartwater.project.module.dao.tool;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.ModeBindDto;
import com.dotop.smartwater.project.module.core.water.vo.ModeBindVo;

/**
 * 

 * @description 通讯方式配置
 * @date 2019-10-17 15:45
 *
 */
public interface IModeConfigureDao extends BaseDao<ModeBindDto, ModeBindVo> {
	
	Integer configureMode(@Param("list") List<ModeBindDto> list);
	
	List<ModeBindVo> listModeConfigure(ModeBindDto modeBindDto);
	List<ModeBindVo> defaultModeConfigure();
	
	Integer deleteModeConfigure(ModeBindDto modeBindDto);
}
