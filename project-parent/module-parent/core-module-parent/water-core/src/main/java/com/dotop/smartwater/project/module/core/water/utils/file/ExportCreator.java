package com.dotop.smartwater.project.module.core.water.utils.file;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.module.core.water.form.customize.ExportFieldForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.ExportDeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.project.module.core.water.form.customize.ExportFieldForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.ExportDeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;

import jxl.write.WritableCellFormat;

public class ExportCreator {

	private ExcelCreator creator;
	private List list;

	public ExportCreator(String tempFilename, List list) throws IOException {
		this.creator = new ExcelCreator(tempFilename);
		this.list = list;
	}

	/**
	 * 导出业主档案数据
	 *
	 * @param reportName
	 * @throws ExcellException
	 * @throws IOException
	 */
	public void OwnerCreate(String reportName) throws ExcellException, IOException {

		creator.CreateSheet(reportName);
		int line = 0;
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();

		creator.writeExcel("业主编号", 0, line, wcf);
		creator.writeExcel("业主名称", 1, line, wcf);
		creator.writeExcel("业主电话", 2, line, wcf);
		creator.writeExcel("证件类型", 3, line, wcf);
		creator.writeExcel("证件号码", 4, line, wcf);
		creator.writeExcel("账户余额", 5, line, wcf);
		creator.writeExcel("居住地址", 6, line, wcf);
		creator.writeExcel("收费种类", 7, line, wcf);
		creator.writeExcel("用水减免", 8, line, wcf);
		creator.writeExcel("水表用途", 9, line, wcf);
		creator.writeExcel("水表型号", 10, line, wcf);
		creator.writeExcel("水表表号", 11, line, wcf);
		creator.writeExcel("报装月份", 12, line, wcf);
		creator.writeExcel("报装读数", 13, line, wcf);
		creator.writeExcel("账户状态", 14, line, wcf);
		creator.writeExcel("备注", 15, line, wcf);

		// 设置 表格宽度
		creator.getSheet().setColumnView(0, 20);
		creator.getSheet().setColumnView(1, 20);
		creator.getSheet().setColumnView(2, 20);
		creator.getSheet().setColumnView(3, 20);
		creator.getSheet().setColumnView(4, 20);
		creator.getSheet().setColumnView(5, 20);
		creator.getSheet().setColumnView(6, 20);
		creator.getSheet().setColumnView(7, 20);
		creator.getSheet().setColumnView(8, 20);
		creator.getSheet().setColumnView(9, 20);
		creator.getSheet().setColumnView(10, 20);
		creator.getSheet().setColumnView(11, 20);
		creator.getSheet().setColumnView(12, 20);
		creator.getSheet().setColumnView(13, 20);
		creator.getSheet().setColumnView(14, 20);
		creator.getSheet().setColumnView(15, 20);

		// list 数据类型转换成 List<OwnerVo>
		@SuppressWarnings("unchecked")
		List<OwnerVo> querys = (List<OwnerVo>) list;

		if (querys != null && querys.size() > 0) {
			for (OwnerVo owner : querys) {
				line++;
				creator.writeExcel(owner.getUserno(), 0, line, wcfItem);
				creator.writeExcel(owner.getUsername(), 1, line, wcfItem);
				creator.writeExcel(owner.getUserphone(), 2, line, wcfItem);
				creator.writeExcel(owner.getCardtype(), 3, line, wcfItem);
				creator.writeExcel(owner.getCardid(), 4, line, wcfItem);
				creator.writeExcel(owner.getAlreadypay(), 5, line, wcfItem);
				creator.writeExcel(owner.getUseraddr(), 6, line, wcfItem);
				creator.writeExcel(owner.getPaytypename(), 7, line, wcfItem);
				creator.writeExcel(owner.getReducename(), 8, line, wcfItem);
				creator.writeExcel(owner.getPurposename(), 9, line, wcfItem);
				creator.writeExcel(owner.getModelname(), 10, line, wcfItem);
				creator.writeExcel(owner.getDevno(), 11, line, wcfItem);
				creator.writeExcel(owner.getInstallmonth(), 12, line, wcfItem);
				creator.writeExcel(owner.getBeginvalue(), 13, line, wcfItem);
				if (owner.getStatus() != null) {
					switch (owner.getStatus()) {
					case 0:
						creator.writeExcel("销户", 14, line, wcfItem);
						break;
					case 1:
						creator.writeExcel("开户", 14, line, wcfItem);
						break;
					case 2:
						creator.writeExcel("未开户", 14, line, wcfItem);
						break;
					default:
						break;
					}
				}
				creator.writeExcel(owner.getRemark(), 15, line, wcfItem);
			}
		}
		creator.close();
	}

	public void originalCreate(String reportName) throws ExcellException, IOException {

		creator.CreateSheet(reportName);
		int line = 0;
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();

		creator.writeExcel("水表编号", 0, line, wcf);
		creator.writeExcel("设备EUI", 1, line, wcf);
		creator.writeExcel("通讯方式", 2, line, wcf);
		creator.writeExcel("口径", 3, line, wcf);
		creator.writeExcel("读数", 4, line, wcf);
		creator.writeExcel("信号强度", 5, line, wcf);
		creator.writeExcel("信噪比", 6, line, wcf);
		creator.writeExcel("上行时间", 7, line, wcf);

		creator.getSheet().setColumnView(0, 20);
		creator.getSheet().setColumnView(1, 30);
		creator.getSheet().setColumnView(2, 20);
		creator.getSheet().setColumnView(3, 20);
		creator.getSheet().setColumnView(4, 20);
		creator.getSheet().setColumnView(5, 20);
		creator.getSheet().setColumnView(6, 20);
		creator.getSheet().setColumnView(7, 30);
		// 最多每次只能导出2000条,太大excel撑爆内存

		@SuppressWarnings("unchecked")
		List<OriginalVo> querys = (List<OriginalVo>) list;

		if (!CollectionUtils.isEmpty(querys)) {
			for (OriginalVo up : querys) {
				line++;
				creator.writeExcel(up.getDevno(), 0, line, wcfItem);
				creator.writeExcel(up.getDeveui(), 1, line, wcfItem);
				if (up.getMode() != null) {
					if (up.getMode().equals("28,300001,1")) {
						creator.writeExcel("Lora", 2, line, wcfItem);
					} else if (up.getMode().equals("28,300001,2")) {
						creator.writeExcel("NB移动", 2, line, wcfItem);
					} else if (up.getMode().equals("28,300001,3")) {
						creator.writeExcel("NB电信", 2, line, wcfItem);
					}
				} else {
					creator.writeExcel("", 2, line, wcfItem);
				}
				creator.writeExcel(up.getCaliber(), 3, line, wcfItem);
				creator.writeExcel(up.getWater(), 4, line, wcfItem);
				creator.writeExcel(up.getRssi(), 5, line, wcfItem);
				creator.writeExcel(up.getLsnr(), 6, line, wcfItem);
				creator.writeExcel(up.getRxtime(), 7, line, wcfItem);
			}
		}
		creator.close();

	}

	public void lowBatteryCreate() throws ExcellException, IOException {
		creator.CreateSheet("水表异常数据");
		int line = 0;
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();

		creator.writeExcel("水表号", 0, line, wcf);
		creator.writeExcel("开到位异常", 1, line, wcf);
		creator.writeExcel("关到位异常", 2, line, wcf);
		creator.writeExcel("阀电流异常", 3, line, wcf);
		creator.writeExcel("电量异常", 4, line, wcf);
		creator.writeExcel("磁暴攻击", 5, line, wcf);
		creator.writeExcel("状态", 6, line, wcf);
		creator.writeExcel("告警时间", 7, line, wcf);
		creator.writeExcel("处理人", 8, line, wcf);
		creator.writeExcel("处理时间", 9, line, wcf);

		creator.getSheet().setColumnView(0, 25);
		creator.getSheet().setColumnView(1, 13);
		creator.getSheet().setColumnView(2, 13);
		creator.getSheet().setColumnView(3, 13);
		creator.getSheet().setColumnView(4, 13);
		creator.getSheet().setColumnView(5, 13);
		creator.getSheet().setColumnView(6, 10);
		creator.getSheet().setColumnView(7, 25);
		creator.getSheet().setColumnView(8, 25);
		creator.getSheet().setColumnView(9, 25);

		@SuppressWarnings("unchecked")
		List<DeviceWarningVo> querys = (List<DeviceWarningVo>) list;

		if (!CollectionUtils.isEmpty(querys)) {
			for (DeviceWarningVo up : querys) {
				line++;
				creator.writeExcel(up.getDevno(), 0, line, wcfItem);
				creator.writeExcel("1".equals(up.getOpenException()) ? "异常" : "正常", 1, line, wcfItem);
				creator.writeExcel("1".equals(up.getCloseException()) ? "异常" : "正常", 2, line, wcfItem);
				creator.writeExcel("1".equals(up.getAbnormalCurrent()) ? "异常" : "正常", 3, line, wcfItem);
				creator.writeExcel("1".equals(up.getAbnormalPower()) ? "异常" : "正常", 4, line, wcfItem);
				creator.writeExcel("1".equals(up.getMagneticAttack()) ? "异常" : "正常", 5, line, wcfItem);
				creator.writeExcel(up.getStatus() == 1 ? "已处理" : "未处理", 6, line, wcfItem);
				creator.writeExcel(up.getCtime(), 7, line, wcfItem);
				creator.writeExcel(up.getHandler(), 8, line, wcfItem);
				creator.writeExcel(up.getHandletime(), 9, line, wcfItem);
			}
		}
		creator.close();

	}

	public void OwnerWaterCreate(String reportName) throws ExcellException, IOException {

		creator.CreateSheet(reportName);
		int line = 0;
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();

		creator.writeExcel("账单月份", 0, line, wcf);
		creator.writeExcel("账单流水号", 1, line, wcf);
		creator.writeExcel("区域名称", 2, line, wcf);
		creator.writeExcel("用户编号", 3, line, wcf);
		creator.writeExcel("用户名称", 4, line, wcf);
		creator.writeExcel("水表号", 5, line, wcf);

		creator.writeExcel("水表状态", 6, line, wcf);
		creator.writeExcel("上期抄表时间", 7, line, wcf);
		creator.writeExcel("上期抄表读数", 8, line, wcf);
		creator.writeExcel("本期抄表时间", 9, line, wcf);

		creator.writeExcel("本期抄表读数", 10, line, wcf);
		creator.writeExcel("区间天数", 11, line, wcf);
		creator.writeExcel("用水量", 12, line, wcf);
		creator.writeExcel("账单状态", 13, line, wcf);
		creator.writeExcel("说明", 14, line, wcf);

		creator.getSheet().setColumnView(0, 20);
		creator.getSheet().setColumnView(1, 20);
		creator.getSheet().setColumnView(2, 20);
		creator.getSheet().setColumnView(3, 20);
		creator.getSheet().setColumnView(4, 20);
		creator.getSheet().setColumnView(5, 20);
		creator.getSheet().setColumnView(6, 20);
		creator.getSheet().setColumnView(7, 20);
		creator.getSheet().setColumnView(8, 20);
		creator.getSheet().setColumnView(9, 20);

		creator.getSheet().setColumnView(10, 20);
		creator.getSheet().setColumnView(11, 20);
		creator.getSheet().setColumnView(12, 20);
		creator.getSheet().setColumnView(13, 20);
		creator.getSheet().setColumnView(14, 20);

		@SuppressWarnings("unchecked")
		List<OrderPreviewVo> querys = (List<OrderPreviewVo>) list;

		if (querys != null && querys.size() > 0) {
			for (OrderPreviewVo payVo : querys) {
				line++;
				creator.writeExcel(payVo.getYear() + "-" + payVo.getMonth(), 0, line, wcfItem);
				creator.writeExcel(payVo.getTradeno(), 1, line, wcfItem);
				creator.writeExcel(payVo.getCommunityname(), 2, line, wcfItem);
				creator.writeExcel(payVo.getUserno(), 3, line, wcfItem);
				creator.writeExcel(payVo.getUsername(), 4, line, wcfItem);
				creator.writeExcel(payVo.getDevno(), 5, line, wcfItem);

				if (payVo.getDevstatus() != null && payVo.getDevstatus() == 0) {
					creator.writeExcel("在线", 6, line, wcfItem);
				} else {
					creator.writeExcel("离线", 6, line, wcfItem);
				}
				creator.writeExcel(payVo.getUpreadtime(), 7, line, wcfItem);
				creator.writeExcel(payVo.getUpreadwater(), 8, line, wcfItem);
				creator.writeExcel(payVo.getReadtime(), 9, line, wcfItem);
				creator.writeExcel(payVo.getReadwater(), 10, line, wcfItem);
				creator.writeExcel(payVo.getDay(), 11, line, wcfItem);
				creator.writeExcel(payVo.getWater(), 12, line, wcfItem);
				if (payVo.getTradestatus() == 0) {
					creator.writeExcel("异常", 13, line, wcfItem);
				} else {
					creator.writeExcel("正常", 13, line, wcfItem);
				}
				creator.writeExcel(payVo.getDescribe(), 14, line, wcfItem);

			}
		}
		creator.close();
	}
	
	public void generateFile(String reportName ,List<ExportFieldForm> fields) throws ExcellException, IOException {
		creator.CreateSheet(reportName);
		int line = 0;
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();
		
		for (int i = 0 ; i < fields.size(); i++) {
			creator.writeExcel(fields.get(i).getName(), i, line, wcf);
			creator.getSheet().setColumnView(i, 20);
		}
		
		if (list != null && list.size() > 0) {
			for (int i = 0 ; i < list.size(); i++) {
				line++;
				Map map = JSONObject.parseObject(JSON.toJSONString(list.get(i)));
				for (int j = 0 ; j < fields.size(); j++) {
					creator.writeExcel(map.get(fields.get(j).getKey()), j, line, wcfItem);	
				}
			}
		}
		creator.close();
	}
	
	
	public void generateDeviceBatchFile(String reportName ,List<ExportFieldForm> fields , DeviceBatchVo batchVo) throws ExcellException, IOException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		creator.CreateSheet(reportName);
		int line = 0;
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();
		
		//批次信息
		creator.writeExcel("批次号：", 0, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getSerialNumber(), 1, line, wcfItem);
		creator.getSheet().setColumnView(0, 30);
		creator.writeExcel("生产数量：", 2, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getSurplus()+"/"+batchVo.getQuantity()+"个", 3, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel("起止日期：", 4, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getStartTime().substring(0,10)+"~"+batchVo.getEndTime().substring(0,10), 5, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel("状态：", 6, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		String statusText = "";
		switch (batchVo.getStatus()) {
		case "0":
			statusText ="未开始";
			break;
		case "1":
			statusText ="生产中";
			break;
		case "2":
			statusText ="已完成";
			break;
		}
		creator.writeExcel(statusText, 7, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		
		// 产品信息
		line++;
		creator.writeExcel("产品名称：", 0, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getProductName(), 1, line, wcfItem);
		creator.getSheet().setColumnView(0, 30);
		creator.writeExcel("产品型号：", 2, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getProductModel(), 3, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel("产品口径：", 4, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getProductCaliber()+"mm", 5, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel("生产厂家：", 6, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getProductVender(), 7, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		
		// 设备参数
		line++;
		creator.writeExcel("通讯方式：", 0, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getMode(), 1, line, wcfItem);
		creator.getSheet().setColumnView(0, 30);
		creator.writeExcel("是否带阀：", 2, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		String tapTypeText = "";
		if (batchVo.getTaptype() != null && batchVo.getTaptype().equals("0")) {
			tapTypeText = "不带阀";
		} else if (batchVo.getTaptype() != null && batchVo.getTaptype().equals("1")) {
			tapTypeText = "带阀";
		}
		creator.writeExcel(tapTypeText, 3, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel("计量单位：", 4, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getUnit(), 5, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel("传感器类型：", 6, line, wcf);
		creator.getSheet().setColumnView(0, 15);
		creator.writeExcel(batchVo.getSensorType(), 7, line, wcfItem);
		creator.getSheet().setColumnView(0, 15);
		
		line++;
		line++;
		for (int i = 0 ; i < fields.size(); i++) {
			creator.writeExcel(fields.get(i).getName(), i, line, wcf);
			creator.getSheet().setColumnView(i, 20);
		}
		
		if (list != null && list.size() > 0) {
			for (int i = 0 ; i < list.size(); i++) {
				line++;
				Map map = JSONObject.parseObject(JSON.toJSONString(list.get(i)));
				for (int j = 0 ; j < fields.size(); j++) {
					creator.writeExcel(map.get(fields.get(j).getKey()), j, line, wcfItem);	
				}
			}
		}
		creator.close();
	}
	

	public void WaterInfoCreate(String reportName) throws ExcellException, IOException {
		creator.CreateSheet(reportName);
		int line = 0;
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();

		creator.writeExcel("批次号", 0, line, wcf);
		creator.writeExcel("设备EUI", 1, line, wcf);
		creator.writeExcel("水表号", 2, line, wcf);
		creator.writeExcel("水表状态", 3, line, wcf);
		creator.writeExcel("当前读数(m³)", 4, line, wcf);
		creator.writeExcel("读数上传时间", 5, line, wcf);
		creator.writeExcel("水表类型", 6, line, wcf);
		creator.writeExcel("阀门状态", 7, line, wcf);
		creator.writeExcel("是否带阀", 8, line, wcf);
		creator.writeExcel("通讯方式", 9, line, wcf);
		creator.writeExcel("总用水量(m³)", 10, line, wcf);

		creator.writeExcel("业主编号", 11, line, wcf);
		creator.writeExcel("业主名称", 12, line, wcf);
		creator.writeExcel("电话", 13, line, wcf);
		creator.writeExcel("身份证号", 14, line, wcf);
		creator.writeExcel("地址", 15, line, wcf);
		creator.writeExcel("报装月份", 16, line, wcf);
		creator.writeExcel("余额", 17, line, wcf);
		creator.writeExcel("状态", 18, line, wcf);
		creator.writeExcel("imsi", 19, line, wcf);
		
		

		creator.getSheet().setColumnView(0, 20);
		creator.getSheet().setColumnView(1, 20);
		creator.getSheet().setColumnView(2, 20);
		creator.getSheet().setColumnView(3, 20);
		creator.getSheet().setColumnView(4, 20);
		creator.getSheet().setColumnView(5, 20);
		creator.getSheet().setColumnView(6, 20);
		creator.getSheet().setColumnView(7, 20);
		creator.getSheet().setColumnView(8, 20);
		creator.getSheet().setColumnView(9, 20);
		creator.getSheet().setColumnView(10, 20);
		creator.getSheet().setColumnView(11, 20);
		creator.getSheet().setColumnView(12, 20);
		creator.getSheet().setColumnView(13, 20);
		creator.getSheet().setColumnView(14, 20);
		creator.getSheet().setColumnView(15, 20);
		creator.getSheet().setColumnView(16, 20);
		creator.getSheet().setColumnView(17, 20);
		creator.getSheet().setColumnView(18, 20);
		creator.getSheet().setColumnView(19, 20);

		List<ExportDeviceVo> datas = (List<ExportDeviceVo>) list;
		if (datas != null && datas.size() > 0) {
			for (ExportDeviceVo d : datas) {
				line++;
				creator.writeExcel(d.getBatchNumber(), 0, line, wcfItem);
				creator.writeExcel(d.getDeveui(), 1, line, wcfItem);
				creator.writeExcel(d.getDevno(), 2, line, wcfItem);
				creator.writeExcel(d.getDevicestatus(), 3, line, wcfItem);
				creator.writeExcel(d.getWater(), 4, line, wcfItem);
				creator.writeExcel(d.getUplinktime(), 5, line, wcfItem);

				creator.writeExcel(d.getTypeid(), 6, line, wcfItem);
				creator.writeExcel(d.getDevicetapstatus(), 7, line, wcfItem);
				creator.writeExcel(d.getDevicetaptype(), 8, line, wcfItem);
				creator.writeExcel(d.getDevicemode(), 9, line, wcfItem);
				creator.writeExcel(d.getUserno(), 11, line, wcfItem);
				creator.writeExcel(d.getUsername(), 12, line, wcfItem);
				creator.writeExcel(d.getUseraddr(), 15, line, wcfItem);
				if(d.getMode() != null && !d.getMode().equals(1)) {
					creator.writeExcel(d.getImsi(), 19, line, wcfItem);
				}
			}
			
		}
		creator.close();
	}

	public void generateMonitorFile(String reportName, List<String> headList, List<List<String>> dataList) throws ExcellException, IOException {
		creator.CreateSheet(reportName);
		int line = 0;
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();
		
		for (int i = 0 ; i < headList.size(); i++) {
			creator.writeExcel(headList.get(i), i, line, wcf);
			creator.getSheet().setColumnView(i, 15);
		}
		if (dataList != null && dataList.size() > 0) {
			for (int i = 0 ; i < dataList.size(); i++) {
				line++;
				for (int j = 0; j < dataList.get(i).size(); j++) {
					creator.writeExcel(dataList.get(i).get(j), j, line, wcfItem);
				}
			}
		}
		creator.close();
	}
}
