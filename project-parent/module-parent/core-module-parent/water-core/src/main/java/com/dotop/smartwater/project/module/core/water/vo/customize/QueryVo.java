package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**

 * @date 2019/2/27.
 */
//表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class QueryVo extends BaseVo {
	private Long communityid;
	private String userno;
	private String username;
	private String devno;
	private String userphone;

	// yyyy-MM-dd-yyyy-MM-dd
	private String timerange;

	// 冻结年或月
	// when 1 then '月冻结数据'
	// when 2 then '年冻结数据'
	private int type;
	private String year;
	private String month;

	// 水表、燃气表、热表
	// when 0 then '水表'
	// when 1 then '燃气表'
	// when 2 then '热表'
	private String typeid;

	// yyyy-MM-dd
	private String time;

	// 旧表号
	private String olddevno;

	// 旧用户名
	private String oldusername;

	// 用户账号
	private String useraccount;

	/**
	 * 此参数用于列表查询时根据分配区域读取
	 */
	private List<String> nodeIds;
}
