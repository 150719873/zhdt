package com.dotop.smartwater.project.auth.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.MdDockingEnterpriseDto;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingEnterpriseVo;

import java.util.List;

/**

 * @date 2019年5月9日
 * @description
 */
public interface IMdDockingEnterpriseDao extends BaseDao<MdDockingEnterpriseDto, MdDockingEnterpriseVo> {

    @Override
    void add(MdDockingEnterpriseDto dto);

    @Override
    Integer edit(MdDockingEnterpriseDto dto);

    @Override
    Integer del(MdDockingEnterpriseDto dto);

    @Override
    List<MdDockingEnterpriseVo> list(MdDockingEnterpriseDto dto);

    @Override
    MdDockingEnterpriseVo get(MdDockingEnterpriseDto dto);

    List<EnterpriseVo> enterpriseList();
}
