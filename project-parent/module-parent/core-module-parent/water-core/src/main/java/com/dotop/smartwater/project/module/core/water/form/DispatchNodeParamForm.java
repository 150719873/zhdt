package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DispatchNodeParamForm extends BaseForm {
	private String id;
	
	/* 流水号 */
	private String flowno;
	/* 节点序号 */
	private Integer nodeno;
	/* 审核人ID */
	private String auditorid;
	/* 审核人 */
	private String auditorname;
	/* 审核时间 */
	private String etime;
	/* 审核结果 1-true 0-false*/
	private Integer result;
	/* 审核结果 文本*/
	private String rtext;
	/* 审核结果文本 */
	private String resulttext;
	/* 审核状态/节点状态 1、未查看 2、处理中 3、已完成 */
	private Integer status;
	/* 审核状态文本 */
	private String statustext;
	/* 是否填写审核意见 */
	private Integer isopinion;
	/* 审核意见 */
	private String opinion;
	/* 经度 */
	private String longitude;
	/* 纬度 */
	private String latitude;
	/* 地址 */
	private String addr;
	/* 是否需要拍照*/
	private String isphoto;
	/* 图片 */
	private String imgurl;
	/* 图片2 */
	private String imgurl2;
	/* 图片3 */
	private String imgurl3;
	/* 转发/抄送人ID */
	private String forwardid;
	/* 转发/抄送人 */
	private String forwardname;
	/* 通知类型 1-短信 2-邮件 3-短信邮件*/
	private Integer noticetype;
	/* 审核结果类型  如：通过/不通过 正常异常 */
	private Integer resulttype;
	/* 节点 1-流转 0-关闭*/
	private Integer fstatus;
}
