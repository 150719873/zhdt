package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.NoticeBo;
import com.dotop.smartwater.project.module.core.water.vo.NoticeVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.ReceiveObjectVo;

/**
 * 通知管理
 * 

 * @date 2019-03-06 11:29
 *
 */
public interface INoticeService extends BaseService<NoticeBo, NoticeVo> {

	@Override
	Pagination<NoticeVo> page(NoticeBo noticeBo);

	Integer addNotice(NoticeBo noticeBo);

	@Override
	NoticeVo get(NoticeBo noticeBo);

	@Override
	String del(NoticeBo noticeBo);

	Integer revise(NoticeBo noticeBo);

	List<OwnerVo> getOwners(List<ReceiveObjectVo> list, String enterpriseid);

	/**
	 * 获取用户信息
	 * 
	 * @param list
	 * @param enterpriseid
	 * @param type
	 *            根据用户ID或角色ID查询用户
	 * @return @
	 */
	List<UserVo> getUsers(List<ReceiveObjectVo> list, String enterpriseid, String type);
}
