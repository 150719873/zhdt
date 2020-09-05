package com.dotop.smartwater.project.module.service.tool.impl;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.NoticeBo;
import com.dotop.smartwater.project.module.core.water.dto.NoticeDto;
import com.dotop.smartwater.project.module.core.water.vo.NoticeVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.ReceiveObjectVo;
import com.dotop.smartwater.project.module.dao.tool.INoticeDao;
import com.dotop.smartwater.project.module.service.tool.INoticeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知管理
 *

 * @date 2019-03-06 11:29
 */
@Service
public class NoticeServiceImpl implements INoticeService {

  private static final Logger LOGGER = LogManager.getLogger(NoticeServiceImpl.class);

  @Autowired private INoticeDao iNoticeDao;

  @Override
  public Pagination<NoticeVo> page(NoticeBo noticeBo) {
    try {
      // 参数转换
      NoticeDto noticeDto = new NoticeDto();
      BeanUtils.copyProperties(noticeBo, noticeDto);
      Page<Object> pageHelper = PageHelper.startPage(noticeBo.getPage(), noticeBo.getPageCount());
      List<NoticeVo> list = iNoticeDao.list(noticeDto);
      for (NoticeVo nv : list) { // 封装数据
        nv.setSendWayList(JSONUtils.parseArray(nv.getSendWay(), String.class));
        nv.setReceiveObjList(JSONUtils.parseArray(nv.getReceiveObj(), ReceiveObjectVo.class));
        if (nv.getAccess() == null || "".equals(nv.getAccess())) {
          nv.setAccess("{\"name\":\"\",\"url\":\"\"}");
        }
        nv.setAccessMap(JSONUtils.parseObject(nv.getAccess(), String.class, String.class));
      }
      Pagination<NoticeVo> pagination =
          new Pagination<>(noticeBo.getPageCount(), noticeBo.getPage());
      pagination.setData(list);
      pagination.setTotalPageSize(pageHelper.getTotal());
      return pagination;
    } catch (DataAccessException e) {
      LOGGER.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(
          BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
    }
  }

  @Override
  public Integer addNotice(NoticeBo noticeBo) {
    try {

      Integer isNotDel = RootModel.NOT_DEL;
      // 参数转换
      NoticeDto noticeDto = new NoticeDto();
      BeanUtils.copyProperties(noticeBo, noticeDto);
      noticeDto.setIsDel(isNotDel);
      return iNoticeDao.addNotice(noticeDto);
    } catch (DataAccessException e) {
      LOGGER.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(
          BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
    }
  }

  @Override
  public NoticeVo get(NoticeBo noticeBo) {
    try {
      Integer isNotDel = RootModel.NOT_DEL;
      // 参数转换
      NoticeDto noticeDto = new NoticeDto();
      BeanUtils.copyProperties(noticeBo, noticeDto);
      noticeDto.setIsDel(isNotDel);

      return iNoticeDao.get(noticeDto);
    } catch (DataAccessException e) {
      LOGGER.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(
          BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
    }
  }

  @Override
  public String del(NoticeBo noticeBo) {
    try {
      Integer isDel = RootModel.DEL;
      // 参数转换
      NoticeDto noticeDto = new NoticeDto();
      BeanUtils.copyProperties(noticeBo, noticeDto);
      noticeDto.setIsDel(isDel);
      Integer count = iNoticeDao.del(noticeDto);
      if (count > 0) {
        return "success";
      } else {
        return "fail";
      }
    } catch (DataAccessException e) {
      LOGGER.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(
          BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
    }
  }

  @Override
  public Integer revise(NoticeBo noticeBo) {
    try {
      Integer isNotDel = RootModel.NOT_DEL;
      // 参数转换
      NoticeDto noticeDto = new NoticeDto();
      BeanUtils.copyProperties(noticeBo, noticeDto);
      noticeDto.setIsDel(isNotDel);

      return iNoticeDao.revise(noticeDto);
    } catch (DataAccessException e) {
      LOGGER.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(
          BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
    }
  }

  @Override
  public List<OwnerVo> getOwners(List<ReceiveObjectVo> list, String enterpriseid) {
    try {
      return iNoticeDao.getOwners(list, enterpriseid);
    } catch (DataAccessException e) {
      LOGGER.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(
          BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
    }
  }

  @Override
  public List<UserVo> getUsers(List<ReceiveObjectVo> list, String enterpriseid, String type) {
    try {
      return iNoticeDao.getUsers(list, enterpriseid, type);
    } catch (DataAccessException e) {
      LOGGER.error(LogMsg.to(e));
      throw new FrameworkRuntimeException(
          BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
    }
  }
}
