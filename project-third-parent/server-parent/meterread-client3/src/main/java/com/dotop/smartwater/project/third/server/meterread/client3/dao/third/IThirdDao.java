package com.dotop.smartwater.project.third.server.meterread.client3.dao.third;

import com.dotop.smartwater.project.third.server.meterread.client3.core.third.bo.SbDtBo;
import com.dotop.smartwater.project.third.server.meterread.client3.core.third.vo.SbDtVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IThirdDao extends BaseDao<BaseDto, BaseVo> {

    /**
     * 根据水表编号和公司
     * @param userCodes
     * @return
     */
//    List<SbDtVo> getList(@Param("meterIds") String[] meterIds);
    List<SbDtVo> getList(@Param("userCodes") List<String> userCodes);

    /**
     * 批量插入抄表数据
     *
     * @param sbDtBos
     */
    void inserts(@Param("sbDtBos") List<SbDtBo> sbDtBos);

    /**
     * 批量更新抄表数据
     * @param sbDtBos
     */
    void updates(@Param("sbDtBos") List<SbDtBo> sbDtBos);


    /**
     * 查询开关阀控制列表
     * @param sbDtBo
     * @return
     */
    List<SbDtVo> getSbDtList(SbDtBo sbDtBo);

    /**
     * 更新下发
     * @param sbDtBo
     */
    void editSbDt(SbDtBo sbDtBo);
}
