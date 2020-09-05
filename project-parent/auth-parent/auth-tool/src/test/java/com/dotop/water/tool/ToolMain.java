// package com.dotop.water.tool;
//
// import java.awt.Menu;
// import java.util.List;
//
// import com.alibaba.fastjson.JSON;
// import com.alibaba.fastjson.JSONObject;
// import
// com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
// import com.dotop.smartwater.project.module.core.auth.vo.MakeNumVo;
// import com.dotop.smartwater.project.module.core.water.vo.UserVo;
// import BusinessException;
// import AppInf;
// import BaseInf;
// import NumRuleSetInf;
// import DataUtil;
// import com.dotop.water.tool.util.JsonUtil;
//
// public class ToolMain {
// public static void main(String[] args) throws BusinessException {
// // WaterClientConfig.WaterCasUrl = "http://192.168.10.93:8088";
// WaterClientConfig.WaterCasUrl = "http://localhost:8888";
//
// // 以下为例子
// try {
// // 平台用户
// // String PlatformUser = BaseInf.WebLogin("admin","654321","192.168.10.93");
//
// // 水司用户
// String CompanyUser = BaseInf.WebLogin("kangjunrong", "123456",
// "localhost:4300");
// // 水司用户（权限比水司管理员小）
// // String CompanyUser1 =
// // BaseInf.WebLogin("kangjunrrr","123456","192.168.10.97");
//
// // System.out.println(PlatformUser);
// System.out.println(CompanyUser);
// // System.out.println(CompanyUser1);
//
// // 鉴权接口
// Integer flag = BaseInf.WebIsPass(JsonUtil.getString("userid", CompanyUser),
// JsonUtil.getString("ticket", CompanyUser), "30100000");
// System.out.println(flag);
//
// // 水司接口（可分页）
// Enterprise enterprise = new Enterprise();
// enterprise.setPage(1);
// enterprise.setPageCount(100);
// enterprise.setName(null);
// MsgEntityForList msgEntityForList =
// BaseInf.GetEnterprises(JsonUtil.getString("userid", CompanyUser),
// JsonUtil.getString("ticket", CompanyUser), enterprise);
// System.out.println("水司接口 :" + JsonUtil.serialize(msgEntityForList));
//
// // 平台用户权限
// // UserInfoVo pList =
// //
// BaseInf.GetUserInfo(Long.parseLong(JsonUtil.getString("userid",PlatformUser)),
// // JsonUtil.getString("ticket",PlatformUser),DmaCode.Platform_Dma);
//
// // 水司用户权限
// UserInfoVo cList = BaseInf.GetUserInfo(JsonUtil.getString("userid",
// CompanyUser),
// JsonUtil.getString("ticket", CompanyUser), DmaCode.Company_Dma);
//
// // UserInfoVo cList1=
// //
// BaseInf.GetUserInfo(Long.parseLong(JsonUtil.getString("userid",CompanyUser1)),
// // JsonUtil.getString("ticket",CompanyUser1),DmaCode.Company_Dma);
//
// // System.out.println(JsonUtil.serialize(pList));
// System.out.println(JsonUtil.serialize(cList));
// // System.out.println(JsonUtil.serialize(cList1));
//
// JSONObject parseObject = JSON.parseObject(CompanyUser);
// String userid = parseObject.getString("userid");
// String ticket = parseObject.getString("ticket");
//
// UserLogin userLogin = new UserLogin();
// userLogin.setUserid(userid);
// userLogin.setTicket(ticket);
// userLogin.setModelid("10000000");
// String generateCasKey = DataUtil.generateCasKey(userLogin);
// System.out.println("----");
// System.out.println(generateCasKey);
// System.out.println("----");
// System.out.println(JsonUtil.serialize(DataUtil.getUserLogin(generateCasKey)));
//
// List<AreaNode> list = BaseInf.getAreaList(userid, ticket);
// System.out.println("List<AreaNode> list: " + JsonUtil.serialize(list));
//
// List<Menu> menus = BaseInf.get_menu_child(userid, ticket,
// RevenueCode.Company_Revenue);
//
// System.out.println("List<Menu> menus: " + JsonUtil.serialize(menus));
//
// System.out.println("getAreaMaps: " + BaseInf.getAreaMaps(userid, ticket));
//
// System.out.println("getUserlora：" +
// JsonUtil.serialize(BaseInf.getUserLora("61")));
//
// System.out.println("getEnterpriseMaps: " +
// JsonUtil.serialize(BaseInf.getEnterpriseMaps(userid, ticket)));
//
// System.out.println("getSettlements: " +
// JsonUtil.serialize(BaseInf.GetSettlements()));
//
// System.out.println("GetCompanyInfo: " +
// JsonUtil.serialize(BaseInf.GetCompanyInfo(44L)));
//
// System.out.println("getAreaById: " +
// JsonUtil.serialize(BaseInf.getAreaById(106L)));
//
// System.out.println("NumRuleSetInf: ");
// MakeNumVo makeNumVo = NumRuleSetInf.getSerialNumber(userid, ticket, 1, 100);
// if (makeNumVo != null && makeNumVo.getStatus() == 1) {
// System.out.println(JsonUtil.serialize(makeNumVo.getNumbers()));
// } else {
// System.out.println("没有规则");
// }
//
// List<UserVo> uList = BaseInf.getUserList(userid, ticket, "61");
// if (uList != null) {
// System.out.println(JsonUtil.serialize(uList));
// }
//
// User user = AppInf.getUserBaseInfo(userid, ticket);
// System.out.println(JsonUtil.serialize(user));
//
// // System.out.println("登出：" + BaseInf.WebLogout(userid,ticket));
// } catch (BusinessException ex) {
// System.out.println(ex.getErrorCode());
// }
// }
// }
