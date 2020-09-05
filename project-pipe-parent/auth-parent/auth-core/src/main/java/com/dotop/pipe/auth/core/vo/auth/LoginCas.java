package com.dotop.pipe.auth.core.vo.auth;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LoginCas {

	private String userId;

	private String ticket;

	// 票据
	private String token;

	// cas企业id
	private String eid;

	// 用户类型(cas企业类型)
	private String type;

	// 用户所属企业id
	private String enterpriseId;

	// 用户所属企业名称
	private String enterpriseName;

	// 用户名称
	private String userName;

	// 用户账号
	private String account;

	// 用户操作企业id
	private String operEid;

	// 权限资源
	private List<PermissionCas> permissions;

	// center地图中心经纬度，格式：经度,纬度(例如：114.0800, 22.5431)
	private BigDecimal[] center;
	// extent地图边界，格式：最小经度,最小纬度,最大经度,最大纬度(例如：114.06, 22.5, 114.10, 22.6)
	private BigDecimal[] extent;
	// 地图license
	private String license;

	// 地图类型
	private String mapType;

	// 头部信息
	private List<TopInfoCas> topInfos;

	// 企业别名
	private String alias;
	// 企业logo
	private String logoUrl;

	//支持的协议
	private List<String> protocols;

}
