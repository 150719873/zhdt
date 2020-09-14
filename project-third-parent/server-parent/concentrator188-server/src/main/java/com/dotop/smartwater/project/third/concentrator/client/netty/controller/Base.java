package com.dotop.smartwater.project.third.concentrator.client.netty.controller;


import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.MsgEntity;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.code.ResultCode;

public class Base {
	public String respString(Object object){
		return JSONUtils.toJSONString(object);
	}

	public String respString(int code,String detail,Object object){
		MsgEntity msgEntity = new MsgEntity();
		msgEntity.setCode(String.valueOf(code));
		msgEntity.setMsg(detail);
		msgEntity.setData(object);
		return JSONUtils.toJSONString(msgEntity);
	}

	public String respStringOk(){
		MsgEntity msgEntity = new MsgEntity();
		msgEntity.setCode(String.valueOf(ResultCode.Success));
		msgEntity.setMsg(ResultCode.SUCCESS);
		msgEntity.setData(null);
		return JSONUtils.toJSONString(msgEntity);
	}

	public String respStringOk(Object obj){
		MsgEntity msgEntity = new MsgEntity();
		msgEntity.setCode(String.valueOf(ResultCode.Success));
		msgEntity.setMsg(ResultCode.SUCCESS);
		msgEntity.setData(obj);
		return JSONUtils.toJSONString(msgEntity);
	}
}
