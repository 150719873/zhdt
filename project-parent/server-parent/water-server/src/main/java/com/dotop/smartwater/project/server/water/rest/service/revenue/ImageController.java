/** @author : KangJunRong
 *  @description : 
 *  @date : 2017年12月21日 上午10:08:48
 */
package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.api.revenue.IImageFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;

@Controller
@RequestMapping("/ImageController")

public class ImageController implements BaseController<BaseForm> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private IImageFactory iImageFactory;

	/**
	 * 上传数据及保存文件
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/upload")
	@ResponseBody
	public String upload(HttpServletRequest request, String userid) {
		LOGGER.info(LogMsg.to("msg:", "上传功能开始--个人开始"));
		iImageFactory.logo(request, userid);
		return resp(ResultCode.Success, "SUCCESS", null);
	}

	@PostMapping("/getlogo")
	public void getlogo(HttpServletResponse response, String userid) {
		LOGGER.info(LogMsg.to("msg:", "查询logo功能开始"));
		iImageFactory.getLogo(response, userid);
		LOGGER.info(LogMsg.to("msg:", "查询logo功能结束"));
	}

}