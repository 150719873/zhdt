package com.dotop.smartwater.view.server.constants;

import com.dotop.smartwater.project.module.core.auth.constants.AuthDMACode;

public class PathConstants {

	public final static String AUTH = "/auth";
	public final static String POINT = "/point";
	public final static String PIPE = "/pipe";
	public final static String AREA = "/area";
	public final static String SENEOR = "/sensor";
	public final static String COMMON = "/common";
	public final static String ALARM = "/alarm";
	public final static String REPORT = "/report";
	public final static String AREAREPORT = "/areaReport";
	public final static String NODE = "/node";
	public final static String PLUG = "/plug";
	public final static String HYDRANT = "/hydrant";
	public final static String VALVE = "/valve";
	public final static String DEVICE = "/device";
	public final static String HISTORYLOG = "/historyLog";
	public final static String ROOT = "/";

	public final static boolean isAuth(String contextPath, String requestURI, String method) {
		if (requestURI.contains(contextPath + AUTH + "/authentication")) {
			return true;
		} else if (requestURI.contains(contextPath + "/test")) {
			return true;
		} else if (requestURI.contains(contextPath + "/druid")) {
			return true;
		} else if (requestURI.contains(contextPath + "/util/update-enterprise")) {
			return true;
		}
		return false;
	}

	public final static boolean isCommon(String contextPath, String requestURI, String method) {
		switch (method) {
		case "GET":
			if (requestURI.contains(contextPath + COMMON + ROOT)) {
				return true;
			}
		case "POST":
			if (requestURI.contains(contextPath + AUTH + "/loginOut")) {
				return true;
			}
		default:
			break;
		}
		return false;
	}

	public final static String whichAuthAdmin(String contextPath, String requestURI, String method) {
		return AuthDMACode.Platform_Dma;
	}

	public final static String whichAuthUser(String contextPath, String requestURI, String method) {

		/*
		 * switch (method) { case "GET": if (requestURI.contains(contextPath + POINT +
		 * ROOT)) { return DmaCode.Company_Dma; } else if
		 * (requestURI.contains(contextPath + PIPE + ROOT)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + AREA +
		 * ROOT)) { return DmaCode.Company_Dma_Btn_Area_Search; } else if
		 * (requestURI.contains(contextPath + SENEOR + ROOT)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + ALARM +
		 * ROOT)) { return DmaCode.Company_Dma; }else if
		 * (requestURI.contains(contextPath + NODE + ROOT)) { return
		 * DmaCode.Company_Dma; }else if (requestURI.contains(contextPath + PLUG +
		 * ROOT)) { return DmaCode.Company_Dma; }else if
		 * (requestURI.contains(contextPath + VALVE + ROOT)) { return
		 * DmaCode.Company_Dma; }else if (requestURI.contains(contextPath + HYDRANT +
		 * ROOT)) { return DmaCode.Company_Dma; } break; case "POST": if
		 * (requestURI.contains(contextPath + POINT)) { return DmaCode.Company_Dma; }
		 * else if (requestURI.contains(contextPath + PIPE)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + AREA +
		 * ROOT)) { return DmaCode.Company_Dma_Btn_Area_Add; } else if
		 * (requestURI.contains(contextPath + SENEOR + ROOT)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + ALARM +
		 * ROOT)) { return DmaCode.Company_Dma; } else if
		 * (requestURI.contains(contextPath + REPORT + ROOT)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + AREAREPORT
		 * + ROOT)) { return DmaCode.Company_Dma; } else if
		 * (requestURI.contains(contextPath + AUTH + ROOT + "isOnline")) { // 校验是否在线
		 * return DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + AUTH
		 * + ROOT + "cas")) { // 生成cas return DmaCode.Company_Dma; }else if
		 * (requestURI.contains(contextPath + NODE + ROOT)) { return
		 * DmaCode.Company_Dma; }else if (requestURI.contains(contextPath + PLUG +
		 * ROOT)) { return DmaCode.Company_Dma; }else if
		 * (requestURI.contains(contextPath + VALVE + ROOT)) { return
		 * DmaCode.Company_Dma; }else if (requestURI.contains(contextPath + HYDRANT +
		 * ROOT)) { return DmaCode.Company_Dma; } break; case "PUT": if
		 * (requestURI.contains(contextPath + POINT)) { return DmaCode.Company_Dma; }
		 * else if (requestURI.contains(contextPath + PIPE)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + AREA +
		 * ROOT)) { return DmaCode.Company_Dma_Btn_Area_Edit; } else if
		 * (requestURI.contains(contextPath + SENEOR + ROOT)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + ALARM +
		 * ROOT)) { return DmaCode.Company_Dma; }else if
		 * (requestURI.contains(contextPath + NODE + ROOT)) { return
		 * DmaCode.Company_Dma; }else if (requestURI.contains(contextPath + PLUG +
		 * ROOT)) { return DmaCode.Company_Dma; }else if
		 * (requestURI.contains(contextPath + VALVE + ROOT)) { return
		 * DmaCode.Company_Dma; }else if (requestURI.contains(contextPath + HYDRANT +
		 * ROOT)) { return DmaCode.Company_Dma; } break; case "DELETE": if
		 * (requestURI.contains(contextPath + POINT + ROOT)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + PIPE +
		 * ROOT)) { return DmaCode.Company_Dma; } else if
		 * (requestURI.contains(contextPath + AREA + ROOT)) { return
		 * DmaCode.Company_Dma_Btn_Area_Delete; } else if
		 * (requestURI.contains(contextPath + SENEOR + ROOT)) { return
		 * DmaCode.Company_Dma; } else if (requestURI.contains(contextPath + ALARM +
		 * ROOT)) { return DmaCode.Company_Dma; }else if
		 * (requestURI.contains(contextPath + NODE + ROOT)) { return
		 * DmaCode.Company_Dma; }else if (requestURI.contains(contextPath + PLUG +
		 * ROOT)) { return DmaCode.Company_Dma; }else if
		 * (requestURI.contains(contextPath + VALVE + ROOT)) { return
		 * DmaCode.Company_Dma; }else if (requestURI.contains(contextPath + HYDRANT +
		 * ROOT)) { return DmaCode.Company_Dma; } break; }
		 */

		return AuthDMACode.Company_Dma;

	}

}
