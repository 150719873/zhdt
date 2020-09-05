package com.dotop.smartwater.project.module.core.third.utils.wechat;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.module.core.third.bo.wechat.Article;
import com.dotop.smartwater.project.module.core.third.bo.wechat.ArticleListMessage;
import com.dotop.smartwater.project.module.core.third.bo.wechat.BaseMessage;
import com.dotop.smartwater.project.module.core.third.bo.wechat.TextMessage;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dotop.smartwater.project.module.core.third.bo.wechat.Article;
import com.dotop.smartwater.project.module.core.third.bo.wechat.ArticleListMessage;
import com.dotop.smartwater.project.module.core.third.bo.wechat.BaseMessage;
import com.dotop.smartwater.project.module.core.third.bo.wechat.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类 TODO 此类有什么用？
 */
public class MessageManager {

	public static final String SUCCESS = "SUCCESS";

	public static final String FAIL = "FAIL";

	public static final String RESP_RETURN_CODE = "return_code";

	public static final String RESP_RETURN_MSG = "return_msg";

	public static final String RESP_RESULT_CODE = "result_code";

	public static final String RESP_ERR_CODE = "err_code";

	// 用户支付中
	public static final String USERPAYING = "USERPAYING";
	// 银行系统异常
	public static final String BANKERROR = "BANKERROR";
	// 系统超时
	public static final String SYSTEMERROR = "SYSTEMERROR";

	public static final String RESP_ERR_CODE_DES = "err_code_des";

	public static final String RESP_PREPAY_ID = "prepay_id";

	public static final String ERR_CODE_OUT_TRADE_NO_USED = "OUT_TRADE_NO_USED";

	public static final String TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";

	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";

	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/** 点击事件 */
	public static final String EVENT_TYPE_SCAN = "SCAN";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 事件类型：VIEW(点击菜单跳转链接时的事件推送 )
	 */
	public static final String EVENT_TYPE_VIEW = "VIEW";

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(InputStream inputStream) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 客服消息转发
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String baseMessageToXml(BaseMessage baseMessage) {
		xstream.alias("xml", baseMessage.getClass());
		return xstream.toXML(baseMessage);
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String articleMessageToXml(ArticleListMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(textMessage);
	}

	public static String toXML(Object object) {
		xstream.alias("xml", object.getClass());
		return xstream.toXML(object);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}