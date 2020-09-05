package com.dotop.smartwater.project.third.server.meterread.client2.dao.third;

import com.dotop.smartwater.project.third.server.meterread.client2.core.third.bo.RemoteDataBo;
import com.dotop.smartwater.project.third.server.meterread.client2.core.third.vo.RemoteDataVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IThirdDao extends BaseDao<BaseDto, BaseVo> {

    /**
     * 根据业主编号获取最后更新
     */
    List<RemoteDataVo> getList(@Param("userCodes") List<String> userCodes);

    /**
     * 批量更新抄表数据
     *
     * @param remoteDataBos
     */
    void updates(@Param("remoteDatas") List<RemoteDataBo> remoteDataBos);

    /**
     * 批量插入抄表数据
     *
     * @param remoteDataBos
     */
    void inserts(@Param("remoteDatas") List<RemoteDataBo> remoteDataBos);

}
