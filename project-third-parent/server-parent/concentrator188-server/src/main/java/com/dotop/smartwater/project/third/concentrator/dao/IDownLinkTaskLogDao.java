package com.dotop.smartwater.project.third.concentrator.dao;

import com.dotop.smartwater.project.third.concentrator.core.dto.DownLinkTaskLogDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.DownLinkTaskLogVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;


import java.util.List;


public interface IDownLinkTaskLogDao extends BaseDao<DownLinkTaskLogDto, DownLinkTaskLogVo> {


    void recordSize(DownLinkTaskLogDto downLinkTaskLogDto);

    @Override
    void add(DownLinkTaskLogDto downLinkTaskLogDto);

    @Override
    Integer edit(DownLinkTaskLogDto downLinkTaskLogDto);

    /**
     * 获取下发任务列表
     * @param downLinkTaskLogDto
     * @return
     */
    @Override
    List<DownLinkTaskLogVo> list(DownLinkTaskLogDto downLinkTaskLogDto);
}
