package com.dotop.pipe.api.dao.historylog;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.historylog.ChangeDto;
import com.dotop.pipe.core.dto.historylog.HistoryLogDto;
import com.dotop.pipe.core.vo.historylog.HistoryLogVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

public interface IHistoryLogDao extends BaseDao<HistoryLogDto, HistoryLogVo> {

	public List<HistoryLogVo> list(HistoryLogDto historyLogDto) throws DataAccessException;

	public HistoryLogVo get(HistoryLogDto historyLogDto) throws DataAccessException;

	public void add(HistoryLogDto historyLogDto) throws DataAccessException;

	public void addList(@Param("list") List<ChangeDto> list, @Param("operEid") String operEid,
			@Param("userBy") String userBy, @Param("isDel") Integer isDel, @Param("createDate") Date date,
			@Param("deviceId") String deviceId);
}
