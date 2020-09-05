package com.dotop.pipe.auth.core.constants;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;

public class CasConstants {

	// dotop水务平台-系统管理员
	public final static String CAS_TYPE_0 = "0";
	// dotop水司平台-水司管理员
	public final static String CAS_TYPE_1 = "1";
	// dotop水司平台-水司员工
	public final static String CAS_TYPE_2 = "2";

	public final static String casType(Integer type) {
		switch (type) {
		case 0:
			return CAS_TYPE_0;
		case 1:
			return CAS_TYPE_1;
		case 2:
			return CAS_TYPE_2;
		}
		return null;
	}

	public final static boolean isAdmin(LoginCas loginCas) {
		String type = loginCas.getType();
		if (CAS_TYPE_0.equals(type)) {
			return true;
		}
		return false;
	}

}
