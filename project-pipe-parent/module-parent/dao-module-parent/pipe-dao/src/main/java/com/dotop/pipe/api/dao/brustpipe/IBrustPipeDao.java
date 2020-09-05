package com.dotop.pipe.api.dao.brustpipe;

import com.dotop.pipe.core.dto.brustpipe.BrustPipeDto;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

import java.util.List;

public interface IBrustPipeDao  extends BaseDao<BrustPipeDto, BrustPipeVo> {
    /**
     * 修改状态
     *
     * @param brustPipeDto
     */
    void editStatus(BrustPipeDto brustPipeDto);


    /**
     * 新增
     *
     * @param brustPipeDto
     */
    @Override
    void add(BrustPipeDto brustPipeDto);

    /**
     * 查询全部数据
     *
     * @param brustPipeDto
     * @return
     */
    @Override
    List<BrustPipeVo> list(BrustPipeDto brustPipeDto);

    /**
     * 删除
     *
     * @param brustPipeDto
     * @return
     */
    @Override
    Integer del(BrustPipeDto brustPipeDto);

    /**
     * 更新
     *
     * @param brustPipeDto
     * @return
     */
    @Override
    Integer edit(BrustPipeDto brustPipeDto);

    /**
     * 获取某一条数据
     *
     * @param brustPipeDto
     * @return
     */
    @Override
    BrustPipeVo get(BrustPipeDto brustPipeDto);

}
