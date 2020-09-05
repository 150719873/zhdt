package com.dotop.smartwater.project.module.dao.operation;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.UserOperationRecordDto;
import com.dotop.smartwater.project.module.core.water.vo.UserOperationRecordVo;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**

 * @date 2019/2/25.
 */
public interface IUserOperationRecordDao extends BaseDao<UserOperationRecordDto, UserOperationRecordVo> {

	/**
	 * 添加用户操作记录
	 *
	 * @param userOperationRecordDto
	 * @throws DataAccessException
	 */
	@Override
	void add(UserOperationRecordDto userOperationRecordDto);

	/**
	 * 查询用户操作记录
	 *
	 * @param userOperationRecordDto
	 * @return
	 */
	@Override
	List<UserOperationRecordVo> list(UserOperationRecordDto userOperationRecordDto);
}
