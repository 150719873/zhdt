package com.dotop.pipe.api.dao.area;

import com.dotop.pipe.core.dto.area.AreaMapDto;
import com.dotop.pipe.core.vo.area.AreaMapVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

@Deprecated
// 区域与设备关系
public interface IAreaMapDao extends BaseDao<AreaMapDto, AreaMapVo> {

    // 新增区域与设备关系
    public void add(AreaMapDto areaMapDto) throws DataAccessException;

    // 删除区域与设备关系
    public Integer del(AreaMapDto areaMapDto) throws DataAccessException;

    // 设备是否存在区域关系
    public Boolean isExist(AreaMapDto areaMapDto) throws DataAccessException;

}
