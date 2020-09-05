package com.dotop.smartwater.project.server.wechat;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;

public class TestAA {

	public static void main(String[] args) {

		// String str = "oG4dAwwugz3ymitrP7mtm-kSKu1M";
		//Object str = "a";
		 String str = "22323";
		String v = JSONUtils.parseObject(str, String.class);
		System.out.println(v);

	}
}
