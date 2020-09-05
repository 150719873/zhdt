package com.dotop.smartwater.project.module.core.third.bo.wechat;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文本消息
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleListMessage extends BaseMessage {
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles = new ArrayList<Article>();
}