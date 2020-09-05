package com.dotop.smartwater.project.auth.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.MdDockingTempDto;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;

import java.util.List;

/**

 * @date 2019年5月9日
 * @description
 */
public interface IMdDockingTempDao extends BaseDao<MdDockingTempDto, MdDockingTempVo> {

    @Override
    void add(MdDockingTempDto dto);

    @Override
    Integer edit(MdDockingTempDto dto);

    @Override
    Integer del(MdDockingTempDto dto);

    @Override
    List<MdDockingTempVo> list(MdDockingTempDto dto);

    @Override
    MdDockingTempVo get(MdDockingTempDto dto);

}
