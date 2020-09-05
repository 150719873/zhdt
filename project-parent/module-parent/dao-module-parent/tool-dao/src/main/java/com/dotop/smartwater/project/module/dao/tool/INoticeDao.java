package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.dto.NoticeDto;
import com.dotop.smartwater.project.module.core.water.vo.NoticeVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.ReceiveObjectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 通知管理
 *

 * @date 2019-03-06 11:29
 */

public interface INoticeDao extends BaseDao<NoticeDto, NoticeVo> {

	@Override
	List<NoticeVo> list(NoticeDto noticeDto);

	Integer addNotice(NoticeDto noticeDto);

	@Override
	NoticeVo get(NoticeDto noticeDto);

	@Override
	Integer del(NoticeDto noticeDto);

	/*
	 * 修改通知状态
	 */
	Integer revise(NoticeDto noticeDto);

	List<OwnerVo> getOwners(@Param("list") List<ReceiveObjectVo> list, @Param("enterpriseid") String enterpriseid);

	List<UserVo> getUsers(@Param("list") List<ReceiveObjectVo> list,
	                      @Param("enterpriseid") String enterpriseid, @Param("type") String type);
}
