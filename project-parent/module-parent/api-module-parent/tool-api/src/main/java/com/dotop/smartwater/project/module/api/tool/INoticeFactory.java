package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.NoticeBo;
import com.dotop.smartwater.project.module.core.water.form.NoticeForm;
import com.dotop.smartwater.project.module.core.water.vo.NoticeVo;

/**
 * 通知管理
 *

 * @date 2019-03-06 11:29
 */
public interface INoticeFactory extends BaseFactory<NoticeForm, NoticeVo> {

  @Override
  Pagination<NoticeVo> page(NoticeForm noticeForm);

  Integer addNotice(NoticeForm noticeForm, String type);

  @Override
  NoticeVo get(NoticeForm noticeForm);

  @Override
  String del(NoticeForm noticeForm);

  /**
   * 修改消息状态
   *
   * @param noticeForm
   * @return @
   */
  Integer revise(NoticeForm noticeForm);

  /**
   * 只用于后台调用发送通知
   *
   * @param noticeBo
   * @param enterpriseid
   * @param userBy
   * @param name
   * @return @
   */
  Integer sendNotice(NoticeBo noticeBo, String enterpriseid, String userBy, String name);
}
