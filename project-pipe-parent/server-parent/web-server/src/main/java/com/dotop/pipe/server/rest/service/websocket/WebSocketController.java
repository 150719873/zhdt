//package com.dotop.pipe.server.rest.service.websocket;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.socket.TextMessage;
//
//import com.dotop.pipe.server.socketHandler.PipeSocketHandler;
//
//@Controller
//@RequestMapping("/socket")
//public class WebSocketController {
//
//	@Resource
//	private PipeSocketHandler myHandler;
//
//	/**
//	 * 发送消息给指定人
//	 *
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/send")
//	public Map<String, Object> send(HttpServletRequest request) {
//		System.out.println("发送给某个人");
//		Map<String, Object> result = new HashMap<>();
//		try {
//			// 假设用户jack登录,存储到session中
//			String id = request.getParameter("id");
//			String message = request.getParameter("message");
//			this.myHandler.sendMessageToUser(id, new TextMessage(message));
//		} catch (Exception e) {
//			result.put("success", false);
//			result.put("message", e.getMessage());
//		}
//		return result;
//	}
//
//	/**
//	 * 指定id下线
//	 *
//	 * @date 2019/1/22
//	 */
//	@RequestMapping("logout")
//	public Map<String, Object> logout(HttpServletRequest request) {
//		Map<String, Object> result = new HashMap<>();
//		try {
//			String id = request.getParameter("id");
//			this.myHandler.logout(id);
//			result.put("success", true);
//		} catch (Exception e) {
//			result.put("success", false);
//			result.put("message", e.getMessage());
//		}
//		return result;
//	}
//}