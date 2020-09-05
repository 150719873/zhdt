package com.dotop.pipe.api.dao.mark;

import com.dotop.pipe.core.dto.mark.MarkDto;
import com.dotop.pipe.core.vo.mark.MarkVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IMarkDao extends BaseDao<MarkDto, MarkVo> {

    @Override
    void add(MarkDto markDto) throws DataAccessException;

    @Override
    MarkVo get(MarkDto markDto) throws DataAccessException;

    @Override
    List<MarkVo> list(MarkDto markDto) throws DataAccessException;

    @Override
    Integer edit(MarkDto markDto) throws DataAccessException;

    @Override
    Integer del(MarkDto markDto) throws DataAccessException;
}
