package com.dotop.pipe.api.dao.third;

import com.dotop.pipe.core.dto.third.ThirdDataDto;
import com.dotop.pipe.core.vo.third.ThirdDataVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 */
public interface IThirdDataDao extends BaseDao<ThirdDataDto, ThirdDataVo> {

    /**
     * 查询上行数据中间表列表
     *
     * @param thirdDataDto
     * @return
     * @throws DataAccessException
     */
    @Override
    List<ThirdDataVo> list(ThirdDataDto thirdDataDto) throws DataAccessException;
    @Override
    ThirdDataVo get(ThirdDataDto thirdDataDto) throws DataAccessException;

    /**
     * 编辑上行数据中间表
     *
     * @param thirdDataDto
     * @return
     * @throws DataAccessException
     */
    @Override
    Integer edit(ThirdDataDto thirdDataDto) throws DataAccessException;

    /**
     * 新增上行数据到中间表
     *
     * @param thirdDataDto
     * @throws DataAccessException
     */
    @Override
    void add(ThirdDataDto thirdDataDto) throws DataAccessException;
}
