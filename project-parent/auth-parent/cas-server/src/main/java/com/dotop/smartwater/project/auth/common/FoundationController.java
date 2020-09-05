package com.dotop.smartwater.project.auth.common;

import com.dotop.smartwater.project.module.api.operation.IUserOperationRecordFactory;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.UserOperationRecordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**

 * @date 2019/7/11.
 */
public class FoundationController {

	@Autowired
	private IUserOperationRecordFactory iUserOperationRecordFactory;

	private static final String UNKNOWN = "unknown";

	protected void auditLog(OperateTypeEnum operateTypeEnum , Object... datas) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = request.getHeader("X-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		UserOperationRecordForm userOperationRecordForm = new UserOperationRecordForm();
		userOperationRecordForm.setIp(ip);
		if(operateTypeEnum != null){
			userOperationRecordForm.setOperatetype(operateTypeEnum.getValue());
		}
		String content = formatData(datas);
		userOperationRecordForm.setOperate(content);
		iUserOperationRecordFactory.add(userOperationRecordForm);
	}

	private String formatData(Object... datas) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < datas.length; i++) {
			if (i != 0) {
				sb.append(" - ");
			}
			sb.append("【").append(String.valueOf(datas[i])).append("】");
		}
		return sb.toString();
	}

}
