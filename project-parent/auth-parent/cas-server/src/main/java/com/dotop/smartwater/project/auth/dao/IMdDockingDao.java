package com.dotop.smartwater.project.auth.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.MdDockingDto;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingVo;
import com.dotop.smartwater.project.module.core.auth.vo.md.ConfigListBaseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**

 * @date 2019年5月9日
 * @description
 */
public interface IMdDockingDao extends BaseDao<MdDockingDto, MdDockingVo> {

    @Override
    void add(MdDockingDto dto);

    @Override
    Integer edit(MdDockingDto dto);

    @Override
    Integer del(MdDockingDto dto);

    @Override
    List<MdDockingVo> list(MdDockingDto dto);

    @Override
    MdDockingVo get(MdDockingDto dto);

    void delByMdeId(@Param("mdeId") String mdeId);

    List<ConfigListBaseVo> getDictionaryList(@Param("dictionaryId") String dictionaryId);

    List<ConfigListBaseVo> getSystemList(@Param("dictionaryId") String dictionaryId);

    List<ConfigListBaseVo> getProductList(@Param("enterpriseId") String enterpriseId);

    void updateByMdeId(@Param("mdeId")String mdeId,@Param("isDel")Boolean isDel);

    void delByFactoryAndType(MdDockingDto dto);

    void delByMdeIdAndFactory(@Param("mdeId")String mdeId, @Param("factory")String factory);
}
