package com.dotop.pipe.api.dao.common;

import com.dotop.pipe.core.dto.common.CommonUploadDto;
import com.dotop.pipe.core.vo.common.CommonUploadVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ICommonUploadDao {

    void add(CommonUploadDto commonUploadDto) throws DataAccessException;

    Integer del(@Param("ids") List<String> ids, @Param("thirdId") String thirdId) throws DataAccessException;

    List<CommonUploadVo> get(@Param("ids") List<String> ids, @Param("thirdId") String thirdId) throws DataAccessException;

    void updateThirdId(@Param("fileIdList") List<String> fileIdList, @Param("thirdId") String thirdId) throws DataAccessException;
}
