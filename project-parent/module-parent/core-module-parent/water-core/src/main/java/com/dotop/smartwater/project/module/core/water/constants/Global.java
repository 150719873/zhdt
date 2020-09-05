package com.dotop.smartwater.project.module.core.water.constants;

/**
 * 外部链接请求地址 错误返回结果
 *
 *
 */
// 此类需要重构
@Deprecated
public class Global {

	/** 获取打印设置信息 */
	public final static String GETPRINTSETTING = "GETPRINTSETTING";

	public final static String ERROR = "error";

	/** 报装 */
	public final static int INSTALLATION = 10;
	/** 报修 */
	public final static int MAINTENANCE = 11;
	/** 巡检 */
	public final static int INSPECTION = 12;

	/** 成功 */
	public final static int Success = 0;

	public final static String SUCCESS = "success";

	/** 用户ID不能为空 */
	public final static int ERRORUSERIDNULL = 10001;

	/** 用户token不能为空 */
	public final static int ERRORTOKENNULL = 10002;

	/** 用户未登陆 */
	public final static int ERRORNOTLOGIN = 100000;

	/** 标题信息不能为空 */
	public final static int ERRORTITLEISNOTNULL = 100001;

	/** 工单来源信息不能为空 */
	public final static int ERRORSYSISNOTNULL = 100002;

	/** 工单类型不能为空 */
	public final static int ERRORTYPEISNOTNULL = 100003;

	/** 申请人不能为空 */
	public final static int ERRORAPPLICANTISNOTNULL = 100004;

	/** 申请时间格式错误 */
	public final static int ERRORTIMEFORMAT = 100005;

	/** 所属区域不能为空 */
	public final static int ERRORREGIONISNOTNULL = 100006;

	/** 工单本体不能为空 */
	public final static int ERRORDESCRIBEISNOTNULL = 100007;

	/** 企业信息不能为空 */
	public final static int ERRORENTERPRISEISNOTNULL = 100008;

	/** 工单号已存在 */
	public final static int ERRORFLOWNOEXIST = 100009;

	/** 工单号不能为空 */
	public final static int ERRORFLOWNOISNULL = 100010;

	/** 未找到流程信息 */
	public final static int ERRORNOFINDFLOW = 100011;

	/** 未找到工单信息 */
	public final static int ERRORNOFINDBILL = 100012;

	/** 是否拍照不能为空 */
	public final static int ERRORISPHOTOISNULL = 100013;

	/** 处理结果类型不能为空 */
	public final static int ERRORRESULTTYPEISNULL = 100014;

	/** 是否填写处理意见不能为空 */
	public final static int ERROROPINIONISNULL = 100015;

	/** 处理人不能为空 */
	public final static int ERRORAUDITORUSERISNULL = 100016;

	/** 节点不能为空 */
	public final static int ERRORNODEISNULL = 100017;

	/** 工单已撤销 */
	public final static int ERRORISREVOKE = 100018;

	/** 工单已关闭 */
	public final static int ERRORISCLOSE = 100019;

	/** 工单已归档 */
	public final static int ERRORISFILE = 100020;

	/** 未找到打印模板类型 */
	public final static int NOTFINDPRINTDESIGNTYPE = 100021;

	/** 未找到打印模板 */
	public final static int NOTFINDPRINTDESIGN = 100022;

	/** 工单处理中不能撤回 */
	public final static int NOREVOKEWORK = 100023;

	/** 当前处理人处理完毕无法撤回 */
	public final static int NOWITHDRAW = 100024;

	/** 工单未派出 */
	public final static int NONODEWITHDRAW = 100025;

	/** 工单处理中不能归档 */
	public final static int NOAWAY = 100026;

	/** 工单已派出 */
	public final static int WORKDISPATCH = 100027;

	/** 未找到节点信息 */
	public final static int NOFINDNODEINFO = 100028;

	/** 处理意见不能为空 */
	public final static int OPINIONNONULL = 100029;

	/** 图片不能为空 */
	public final static int PHOTONONULL = 100030;

	/** 坐标不能为空 */
	public final static int COORDINATENONULL = 100031;

	/** 处理状态不能为空 */
	public final static int STATUSNONULL = 100032;

	/** 系统异常 */
	public final static int ERRORRUNSYSTEM = 999999;
}
