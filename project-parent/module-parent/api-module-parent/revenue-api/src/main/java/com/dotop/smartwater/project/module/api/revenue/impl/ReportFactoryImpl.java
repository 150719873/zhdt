package com.dotop.smartwater.project.module.api.revenue.impl;

import static com.dotop.smartwater.project.module.api.revenue.impl.QueryFactoryImpl.checkQueryForm;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.IReportFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchRelationBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBookBindBo;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryParamBo;
import com.dotop.smartwater.project.module.core.water.constants.DeviceCode;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import com.dotop.smartwater.project.module.core.water.constants.PathCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.ExportFieldForm;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.utils.FileUtil;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcellException;
import com.dotop.smartwater.project.module.core.water.utils.file.ExportCreator;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookBindVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.ExportDeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;
import com.dotop.smartwater.project.module.service.device.IDeviceBookBindService;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.tool.IDeviceBatchService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import com.dotop.water.tool.service.BaseInf;

/**
 * 报表
 *

 * @date 2019年2月26日
 */
@Component
public class ReportFactoryImpl implements IReportFactory {

	private static final Logger logger = LoggerFactory.getLogger(ReportFactoryImpl.class);

	@Value("${param.revenue.excelTempUrl}")
	private String excelTempUrl;

	// excel导出最大条数,太大会卡死系统
	private static final Integer maxCount = 5000;
	// excel导出数据监控最大条数，因为数据监控列数太多
	private static final Integer maxMonitorCount = 2000;

	@Autowired
	private IOwnerService iOwnerService;

	@Autowired
	private IDeviceService iDeviceService;
	
	@Autowired
	private IDeviceBookBindService iDeviceBookBindService;

	@Autowired
	private IOrderService iOrderService;

	@Autowired
	private IDeviceUplinkService iDeviceUplinkService;
	
	@Autowired
	private IDeviceBatchService deviceBatchservice;
	
	@Autowired
    private IDictionaryChildService iDictionaryChildService;
	
	@Autowired
    private ISettlementService iSettlementService;
	
	@Override
	public String export_owner(OwnerForm owner) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		try {
			if (user == null) {
				throw new FrameworkRuntimeException(ResultCode.UserNotLogin, "用户没登录");
			}
			Map<String, AreaNodeVo> map = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
			if (map == null || map.size() == 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "没找到区域");
			}

			//查询水司下所有表册信息
			List<DeviceBookBindVo> bindList = new ArrayList<DeviceBookBindVo>();
			DeviceBookBindBo bindBo = new DeviceBookBindBo();
			bindBo.setEnterpriseid(user.getEnterpriseid());
			bindList = iDeviceBookBindService.listDeviceBookBind(bindBo);
			
			OwnerBo ownerBo = new OwnerBo();
			BeanUtils.copyProperties(owner, ownerBo);
			ownerBo.setPage(1);
			ownerBo.setPageCount(maxCount);
			ownerBo.setEnterpriseid(user.getEnterpriseid());

			Pagination<OwnerVo> pagination = iOwnerService.getOwnerList(ownerBo);
			setOwnerAreaName(map, pagination, bindList);
			List<OwnerVo> list = pagination.getData();
			if (pagination.getTotalPageSize() > maxCount) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "数据总条数大于" + maxCount + ",不可导出");
			}
			if (pagination.getTotalPageSize() == 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "没有数据,不可导出");
			}
			String filePath = BuildExcelAndReUrl(list, "业主档案", PathCode.OwnerExcel ,owner.getFields());
			return filePath;
		} catch (Exception e) {
			logger.error("export_owner", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}
	
	
	public String export_device_batch(DeviceBatchForm form) {
		UserVo user = AuthCasClient.getUser();
		try {
			if (user == null) {
				throw new FrameworkRuntimeException(ResultCode.UserNotLogin, "用户没登录");
			}
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			DeviceBatchBo bo = new DeviceBatchBo();
			BeanUtils.copyProperties(form, bo);
			bo.setEnterpriseid(user.getEnterpriseid());
			DeviceBatchVo batchVo= deviceBatchservice.get(bo);
			
			DeviceBatchRelationBo relationBo = new DeviceBatchRelationBo();
			BeanUtils.copyProperties(form, relationBo);
			relationBo.setBatchId(bo.getId());
			relationBo.setPage(1);
			relationBo.setPageCount(maxCount);
			relationBo.setEnterpriseid(user.getEnterpriseid());
			Pagination<DeviceVo> pagination = deviceBatchservice.detailPage(relationBo);
			List<DeviceVo> list = pagination.getData();
			if (pagination.getTotalPageSize() > maxCount) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "数据总条数大于" + maxCount + ",不可导出");
			}
			if (pagination.getTotalPageSize() == 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "没有数据,不可导出");
			}
			
			for (DeviceVo vo : list) {
				if (vo.getStatus() != null) {
					switch (vo.getStatus()) {
					case 0:
						vo.setStatusText("在线");
						break;
					case 2:
						vo.setStatusText("离线");
						break;	
					case 3:
						vo.setStatusText("未激活");
						break;
					case 4:
						vo.setStatusText("已贮存");
						break;	
					case 5:
						vo.setStatusText("已报废");
						break;	
					default:
						break;
					}
				}
				vo.setBindTime(sf.format(vo.getAccesstime()));
			}
			
			String filePath = exportDeviceBatchExcel(list, "设备批次", PathCode.OwnerExcel ,form.getFields() ,batchVo);
			return filePath;
		} catch (Exception e) {
			logger.error("export_owner", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}
	

	private void setOwnerAreaName(Map<String, AreaNodeVo> map, Pagination<OwnerVo> pagination,
			List<DeviceBookBindVo> bindList) {
		if (pagination.getData() != null && pagination.getData().size() > 0) {
			for (OwnerVo o : pagination.getData()) {
				switch (o.getStatus()) {
				case 0:
					o.setStatusText("已销户");
					break;
				case 1:
					o.setStatusText("已开户");			
					break;
				case 2:
					o.setStatusText("未开户");
					break;
				default:
					break;
				}
				
				//所属区域
				AreaNodeVo areaNode = map.get(String.valueOf(o.getCommunityid()));
				if (areaNode == null) {
					continue;
				}
				o.setCommunityCode(areaNode.getCode());
				o.setCommunityname(areaNode.getTitle());
				
				//如果抄表员列表不为空,则将抄表员工号拼接
				if (bindList != null && !bindList.isEmpty() && StringUtils.isNotBlank(o.getBookNum())) {
					StringBuffer userCodes = new StringBuffer("");
					for (DeviceBookBindVo bind: bindList) {
						if (o.getBookNum().equals(bind.getBookNum())) {
							if (userCodes.toString().equals("")) {
								userCodes.append(bind.getWorkNum());	
							} else {
								userCodes.append("," + bind.getWorkNum());	
							}
						}
					}
					o.setMeterUserCode(userCodes.toString());
				}
				
				//计算欠费金额
				if (o.getArrears() == null || o.getArrears() == 0) {
					o.setOwe(0.0);
				} 
				
				if (o.getArrears() != null && o.getArrears() != 0) {
					if (o.getAlreadypay() == null || o.getArrears() == 0) {
						o.setOwe(o.getArrears());
					} else if (o.getAlreadypay() >= o.getArrears()){
						o.setOwe(0.0);	
					} else if (o.getAlreadypay() < o.getArrears()) {
						o.setOwe(CalUtil.sub(o.getArrears(),o.getAlreadypay()));	
					}
				}
				
			}
		}
	}
	
	public String exportDeviceBatchExcel (List list, String reportName, String type ,
			List<ExportFieldForm> fields , DeviceBatchVo batchVo) throws IOException, ExcellException {
		String path = excelTempUrl;
		File baseDir = new File(path, type);
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		FileUtil.deleteFiles(baseDir, 10);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = format.format(new Date());

		Random r = new Random();
		String fileName = reportName + "_" + date + r.nextInt(10000) + ".xls";
		String tempFilename = File.separator + type + File.separator + fileName;
		tempFilename = path + tempFilename;
		ExportCreator creator = new ExportCreator(tempFilename, list);
		creator.generateDeviceBatchFile(reportName,fields , batchVo);
		return tempFilename;
	}
	
	
	// 生成Excel文件并得到地址
	private String BuildExcelAndReUrl(List list, String reportName, String type ,List<ExportFieldForm> fields) throws IOException, ExcellException {
		String path = excelTempUrl;
		File baseDir = new File(path, type);
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		FileUtil.deleteFiles(baseDir, 10);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = format.format(new Date());

		Random r = new Random();
		String fileName = reportName + "_" + date + r.nextInt(10000) + ".xls";
		String tempFilename = File.separator + type + File.separator + fileName;
		tempFilename = path + tempFilename;
		ExportCreator creator = new ExportCreator(tempFilename, list);
		creator.generateFile(reportName,fields);
		return tempFilename;
	}

	@Override
	public String exportOwnerwater(PreviewForm view) throws FrameworkRuntimeException {

		UserVo user = AuthCasClient.getUser();
		if (user == null) {
			throw new FrameworkRuntimeException(ResultCode.TimeOut, "登录超时,请重新登录");
		}
		if (StringUtils.isBlank(view.getCommunityIds())) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请先选择区域");
		}

		if (StringUtils.isBlank(view.getTradeStatus())) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "账单状态未选定");
		}

		try {
			view.setEnterpriseid(user.getEnterpriseid());
			view.setPage(1);
			view.setPageCount(maxCount);
			Pagination<OrderPreviewVo> pagination = iOrderService.auditingOrderPreviewList(view);
			
			if (pagination.getTotalPageSize() > maxCount) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "数据总条数大于" + maxCount + ",不可导出");
			}
			if (pagination.getTotalPageSize() == 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "没有数据,不可导出");
			}

			List<OrderPreviewVo> list = pagination.getData();
			handleOrderData(list);
			String filePath = BuildExcelAndReUrl(list, "业主账单", PathCode.PaymentExcel,view.getFields());
			return filePath;

		} catch (Exception e) {
			logger.error("export_ownerwater", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}
	
	private void handleOrderData(List<OrderPreviewVo> list) {
		for (OrderPreviewVo vo : list) {
			if (vo.getDevstatus() != null) {
				switch (vo.getDevstatus()) {
				case 0:
					vo.setDevstatusText("在线");
					break;
				case 2:
					vo.setDevstatusText("离线");
					break;	
				case 3:
					vo.setDevstatusText("未激活");
					break;
				case 4:
					vo.setDevstatusText("已贮存");
					break;	
				case 5:
					vo.setDevstatusText("已报废");
					break;	
				default:
					break;
				}
			}
			
			if (vo.getTradestatus() != null) {
				switch (vo.getTradestatus()) {
				case 0:
					vo.setTradestatusText("异常");
					break;
				case 1:
					vo.setTradestatusText("正常");
					break;	
				default:
					break;
				}
			}
			
			
			if (vo.getApproval_status() != null) {
				switch (vo.getApproval_status()) {
				case "0":
					vo.setApprovalStatusText("未审核");
					break;
				case "1":
					vo.setApprovalStatusText("审核中");
					break;	
				case "2":
					vo.setApprovalStatusText("审核完成");
					break;		
				default:
					break;
				}
			}
			
			if (vo.getApproval_result() != null) {
				switch (vo.getApproval_result()) {
				case "0":
					vo.setApprovalResultText("不通过");
					break;
				case "1":
					vo.setApprovalResultText("通过");
					break;	
				default:
					break;
				}
			}
			
		}
	}
	

	@Override
	public String exportWaterInfo(DeviceForm deviceForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		try {
			if (deviceForm.getCommunityid() == null) {
				//不是最高级管理员,获取分配的区域
				/*if(WaterConstants.USER_TYPE_ENTERPRISE_NORMAL == user.getType()){
					List<AreaNodeVo> list = BaseInf.getAreaList(user.getUserid(), user.getTicket());
					if(list.size() == 0){
						throw new FrameworkRuntimeException(ResultCode.Fail, "用户角色没分配区域");
					}else{
						List<String> areaIds = new ArrayList<>();
						for(AreaNodeVo area : list){
							areaIds.add(area.getKey());
						}
						deviceForm.setNodeIds(areaIds);
					}
				}else{
					deviceForm.setNodeIds(null);
				}*/
				deviceForm.setNodeIds(null);
			}

			deviceForm.setCommunityid(null);
			deviceForm.setEnterpriseid(user.getEnterpriseid());

			DeviceBo deviceBo = new DeviceBo();
			BeanUtils.copyProperties(deviceForm, deviceBo);
			deviceBo.setPid(DeviceCode.DEVICE_PARENT);
			
			// TODO 查询列表
			List<ExportDeviceVo> list = iDeviceService.getExportDeviceList(deviceBo);
			handleDeviceData(list);
			if (list.size() > maxCount) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "数据总条数大于" + maxCount + ",不可导出");
			}
			if (list.size() == 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "没有数据,不可导出");
			}

			String filePath = BuildExcelAndReUrl(list, "水表数据", PathCode.WaterInfoExcel , deviceForm.getFields());
			return filePath;
		} catch (Exception e) {
			logger.error("exportWaterInfo", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}
	
	@Override
	public String exportDeviceWater(DeviceForm deviceForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		try {
			if (deviceForm.getCommunityid() == null) {
				//不是最高级管理员,获取分配的区域
				deviceForm.setNodeIds(null);
			}

			deviceForm.setCommunityid(null);
			deviceForm.setEnterpriseid(user.getEnterpriseid());

			DeviceBo deviceBo = new DeviceBo();
			BeanUtils.copyProperties(deviceForm, deviceBo);
			deviceBo.setPid(DeviceCode.DEVICE_PARENT);
			
			// 查询水表列表
			List<ExportDeviceVo> list = iDeviceService.getExportDeviceList(deviceBo);
			handleDeviceData(list);
			if (list.size() > maxCount) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "数据总条数大于" + maxCount + ",不可导出");
			}
			if (list.size() == 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "没有数据,不可导出");
			}
			//获取水司设置的水表离线天数,默认为5天
			Integer offDay = 5;
			SettlementVo settlement = iSettlementService.getSettlement(user.getEnterpriseid());
			if(settlement != null) {
				if(settlement.getOffday() != null) {
					offDay = settlement.getOffday();
				}
			}else {
				throw new FrameworkRuntimeException(ResultCode.Fail, "获取该水司水表离线天数失败");
			}
			//抄表时间,这里把抄表时间存到了accesstime中
			Date meterDate = deviceForm.getAccesstime();
			
			List<String> deveuis = new ArrayList<String>();
			for(ExportDeviceVo item: list) {
				deveuis.add(item.getDeveui());
				item.setWater(null);
				item.setUplinktime(null);
			}
			Calendar calendar = Calendar.getInstance();
			Date tempDate = meterDate;
			for(int i=0;i < offDay;i++) {
				if(i != 0) {
					calendar.setTime(tempDate);
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					tempDate = calendar.getTime();
				}
				List<DeviceUplinkVo> uplinks = new ArrayList<DeviceUplinkVo>();
				if(deveuis != null && !deveuis.isEmpty()) {
					//批量查询用水量
					uplinks = iDeviceUplinkService.batchFindWaterByDeveuis(generateString(deveuis), DateUtils.format(tempDate, "yyyyMM"), DateUtils.format(tempDate, "yyyy-MM-dd"));
				}
				if(uplinks != null && !uplinks.isEmpty()) {
					for(ExportDeviceVo item: list) {
						for(int j=0;j < uplinks.size();j++) {
							DeviceUplinkVo uplink = uplinks.get(j);
							if(item.getDeveui().equalsIgnoreCase(uplink.getDeveui())) {
								item.setWater(uplink.getWater() != null && StringUtils.isNotEmpty(uplink.getWater()) ?Double.valueOf(uplink.getWater()) : null);
								item.setUplinktime(DateUtils.formatDatetime(uplink.getRxtime()));
								deveuis.remove(item.getDeveui());
								uplinks.remove(j);
								break;
							}
						}
					}
				}
			}
			
			String filePath = BuildExcelAndReUrl(list, "抄表数据", PathCode.DeviceWaterExcel , deviceForm.getFields());
			return filePath;
		} catch (Exception e) {
			logger.error("exportWaterInfo", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}
	
	private String generateString(List<String> deveuis) {
		String result = "(";
		if(deveuis != null && !deveuis.isEmpty()) {
			for(int i=0;i<deveuis.size();i++) {
				if(i+1 == deveuis.size()) {
					result = result + "'" + deveuis.get(i) + "'";
				}else {
					result = result + "'" + deveuis.get(i) + "'" + ",";
				}
			}
		}
		result += ")";
		return result;
	}
	
	private void handleDeviceData(List<ExportDeviceVo> list) {
		for (ExportDeviceVo vo : list) {
			if (!StringUtils.isEmpty(vo.getMode()) && vo.getStatus() != null) {
				switch (vo.getStatus()) {
				case 0:
					vo.setStatusText("在线");
					break;
				case 2:
					vo.setStatusText("离线");
					break;	
				case 3:
					vo.setStatusText("未激活");
					break;
				case 4:
					vo.setStatusText("已贮存");
					break;	
				case 5:
					vo.setStatusText("已报废");
					break;	
				default:
					break;
				}
			}
			//taptype 0:不带阀 1:带阀
			if(vo.getTaptype() != null && vo.getTaptype() == 0) {
				vo.setTap("-");
			}
		}
	}
	
	@Override
	public String exportOriginal(QueryForm queryForm) throws FrameworkRuntimeException {
		try {
			QueryParamBo param = checkQueryForm(queryForm);
			param.setDeveui(queryForm.getDeveui());
			param.setPage(1);
			param.setPageCount(2000);

			Pagination<OriginalVo> pagination;
			// 当月
			if (param.getStartMonth().equals(param.getEndMonth())) {
				pagination = iDeviceUplinkService.exportFindOriginal(param);
			} else {
				// 跨月
				pagination = iDeviceUplinkService.exportFindOriginalCrossMonth(param);
			}
			
			if (pagination.getTotalPageSize() > maxCount) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "数据总条数大于" + maxCount + ",不可导出");
			}
			if (pagination.getTotalPageSize() == 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "没有数据,不可导出");
			}

			List<OriginalVo> list = pagination.getData();
			DictionaryChildBo bo = new DictionaryChildBo();
            bo.setDictionaryId(DictionaryCode.DEVICE_UPLOAD_REASON);
            List<DictionaryChildVo> listDict = iDictionaryChildService.list(bo);
            //处理数据中需要转换的属性
			handleHistoryData(list, listDict);

			String filePath = BuildExcelAndReUrl(list, "上报历史数据", PathCode.OriginalExcel , queryForm.getFields());
			return filePath;

		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}
	
	private void handleHistoryData(List<OriginalVo> list, List<DictionaryChildVo> listDict) {
		for (OriginalVo vo : list) {
			if(!StringUtils.isEmpty(vo.getRssi())) {
				Integer val = Integer.valueOf(vo.getRssi());
				if ("28,300001,2".equals(vo.getMode()) || "28,300001,3".equals(vo.getMode())
						 || "28,300001,7".equals(vo.getMode()) || "28,300001,10".equals(vo.getMode())) {   //NB
		            if (val >= 15) {
		                vo.setRssi("好" + "(" + vo.getRssi() + ")");
		            }
		            if (10 <= val && val < 15) {
		            	vo.setRssi("一般" + "(" + vo.getRssi() + ")");
		            }
		            if (val < 10) {
		            	vo.setRssi("差" + "(" + vo.getRssi() + ")");
		            }
		        }
				if ("28,300001,1".equals(vo.getMode()) || "28,300001,6".equals(vo.getMode())
						 || "28,300001,8".equals(vo.getMode()) || "28,300001,9".equals(vo.getMode())
						 || "28,300001,11".equals(vo.getMode())) {   //Lora
		            if (val >= -80) {
		            	vo.setRssi("好" + "(" + vo.getRssi() + ")");
		            }
		            if (-100 <= val && val < -80) {
		            	vo.setRssi("一般" + "(" + vo.getRssi() + ")");
		            }
		            if (val < -100) {
		            	vo.setRssi("差" + "(" + vo.getRssi() + ")");
		            }
		        }
			}
//			信噪比类型为double，无法进行转换
			/*if(vo.getLsnr() != null) {
				Double val = vo.getLsnr();
				if ("28,300001,2".equals(vo.getMode()) || "28,300001,3".equals(vo.getMode())
						 || "28,300001,7".equals(vo.getMode()) || "28,300001,10".equals(vo.getMode())) {   //NB
		            if (val >= 15) {
		                vo.setLsnr("好" + "(" + vo.getLsnr() + ")");
		            }
		            if (10 <= val && val < 15) {
		            	vo.setRssi("一般" + "(" + vo.getLsnr() + ")");
		            }
		            if (val < 10) {
		            	vo.setRssi("差" + "(" + vo.getLsnr() + ")");
		            }
		        }
				if ("28,300001,1".equals(vo.getMode()) || "28,300001,6".equals(vo.getMode())
						 || "28,300001,8".equals(vo.getMode()) || "28,300001,9".equals(vo.getMode())
						 || "28,300001,11".equals(vo.getMode())) {   //Lora
		            if (val >= 8) {
		            	vo.setRssi("好" + "(" + vo.getLsnr() + ")");
		            }
		            if (0 <= val && val < 8) {
		            	vo.setRssi("一般" + "(" + vo.getLsnr() + ")");
		            }
		            if (val < 0) {
		            	vo.setRssi("差" + "(" + vo.getLsnr() + ")");
		            }
		        }
			}*/
			//taptype 0:不带阀 1:带阀
			if(vo.getTapType() == null || vo.getTapType() == 0) {
				vo.setStatus("-");
			}
			//转换上报类型
			if(!StringUtils.isEmpty(vo.getReason())) {
				if (listDict != null && listDict.size() > 0) {
	                Map<String, String> map = listDict.stream()
	                        .collect(Collectors.toMap(x -> x.getChildValue(), x -> x.getChildName(), (oldValue, newValue) -> oldValue));
	                String reason = map.get(vo.getReason());
                    if (reason != null) {
                        vo.setReason(reason);
                    }
	            }
			}
			if (!StringUtils.isEmpty(vo.getModeName())) {
				vo.setMode(vo.getModeName());
			}
			if(!StringUtils.isEmpty(vo.getMeasureMethod())) {
				String val = vo.getMeasureMethod();
				if("0".equals(val)) {
					vo.setMeasureMethod("不变");
				}else if("1".equals(val)) {
					vo.setMeasureMethod("相对计量");
				}else if("2".equals(val)) {
					vo.setMeasureMethod("结对计量");
				}
			}
			if(!StringUtils.isEmpty(vo.getMeasureType())) {
				String val = vo.getMeasureType();
				if("0".equals(val)) {
					vo.setMeasureType("不变");
				}else if("1".equals(val)) {
					vo.setMeasureType("霍尔");
				}else if("2".equals(val)) {
					vo.setMeasureType("干簧管");
				}
			}
			if(!StringUtils.isEmpty(vo.getMeasureUnit())) {
				String val = vo.getMeasureUnit();
				if("0".equals(val)) {
					vo.setMeasureUnit("不变");
				}else if("1".equals(val)) {
					vo.setMeasureUnit("1L");
				}else if("2".equals(val)) {
					vo.setMeasureUnit("10L");
				}else if("3".equals(val)) {
					vo.setMeasureUnit("100L");
				}else if("4".equals(val)) {
					vo.setMeasureUnit("1000L");
				}
			}
			if(!StringUtils.isEmpty(vo.getNetworkInterval())) {
				String val = vo.getNetworkInterval();
				if("0".equals(val)) {
					vo.setNetworkInterval("不变");
				}else if("1".equals(val)) {
					vo.setNetworkInterval("24h");
				}else if("2".equals(val)) {
					vo.setNetworkInterval("48h");
				}else if("3".equals(val)) {
					vo.setNetworkInterval("72h");
				}else if("4".equals(val)) {
					vo.setNetworkInterval("96h");
				}else if("4".equals(val)) {
					vo.setNetworkInterval("120h");
				}
			}
			if(!StringUtils.isEmpty(vo.getWaterConsumption())) {
				vo.setWaterConsumption(vo.getWaterConsumption() + "L");
			}
			if(!StringUtils.isEmpty(vo.getResetPeriod())) {
				vo.setResetPeriod(vo.getResetPeriod() + "天");
			}
			//阀电流异常
			if("0".equals(vo.getAbnormalCurrent())) {
				vo.setAbnormalCurrent("正常");
			}else if("1".equals(vo.getAbnormalCurrent())) {
				vo.setAbnormalCurrent("异常");
			}else {
				vo.setAbnormalCurrent("-");
			}
			//时间同步
			if("0".equals(vo.getTimeSync())) {
				vo.setTimeSync("忽略");
			}else if("1".equals(vo.getTimeSync())) {
				vo.setTimeSync("同步");
			}else {
				vo.setTimeSync("-");
			}
			//电量异常
			if("0".equals(vo.getAbnormalPower())) {
				vo.setAbnormalPower("正常");
			}else if("1".equals(vo.getAbnormalPower())) {
				vo.setAbnormalPower("异常");
			}else {
				vo.setAbnormalPower("-");
			}
			//磁暴攻击
			if("0".equals(vo.getMagneticAttack())) {
				vo.setMagneticAttack("正常");
			}else if("1".equals(vo.getMagneticAttack())) {
				vo.setMagneticAttack("异常");
			}else {
				vo.setMagneticAttack("-");
			}
			//串口异常
			if("0".equals(vo.getSerialAbnormal())) {
				vo.setSerialAbnormal("正常");
			}else if("1".equals(vo.getSerialAbnormal())) {
				vo.setSerialAbnormal("异常");
			}else {
				vo.setSerialAbnormal("-");
			}
			//复位类型
			if("0".equals(vo.getResetType())) {
				vo.setResetType("电源");
			}else if("1".equals(vo.getResetType())) {
				vo.setResetType("看门狗");
			}else {
				vo.setResetType("-");
			}
			//生命状态
			if("0".equals(vo.getLifeStatus())) {
				vo.setLifeStatus("初始状态");
			}else if("1".equals(vo.getLifeStatus())) {
				vo.setLifeStatus("贮存状态");
			}else if("2".equals(vo.getLifeStatus())) {
				vo.setLifeStatus("运行状态");
			}else if("3".equals(vo.getLifeStatus())) {
				vo.setLifeStatus("报废状态");
			}else {
				vo.setLifeStatus("-");
			}
		}
	}

	public String exportMonitor(Pagination<DeviceVo> pagination, List<String> dates) throws FrameworkRuntimeException {
		try {
			List<DeviceVo> list = pagination.getData();
			if (list.size() > maxMonitorCount) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "数据总条数大于" + maxMonitorCount + ",不可导出");
			}
			if (list.size() == 0) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "没有数据,不可导出");
			}
			
			//生成excel表头
			List<String> headList = new ArrayList<String>();
			headList.add("水表编号");
			headList.add("水表EUI");
			headList.add("通讯方式");
			for(String date: dates) {
				if(date != null && date.length() == 4) {
					headList.add(date.substring(0, 2) + "月" + date.substring(2, 4) + "号(m³)");
				}else {
					headList.add(date);
				}
			}
            //处理数据
			List<List<String>> dataList = handleMonitorData(list, dates);

			//生成路径
			String reportName = "数据监控数据";
			String type = PathCode.MonitorExcel;
			String path = excelTempUrl;
			File baseDir = new File(path, type);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}
			FileUtil.deleteFiles(baseDir, 10);

			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = format.format(new Date());

			Random r = new Random();
			String fileName = reportName + "_" + date + r.nextInt(10000) + ".xls";
			String tempFilename = File.separator + type + File.separator + fileName;
			tempFilename = path + tempFilename;

			ExportCreator creator = new ExportCreator(tempFilename, null);
			creator.generateMonitorFile(reportName, headList, dataList);
			String filePath = tempFilename;
			return filePath;
		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}
	
	//处理数据-返回处理好的数据
	private List<List<String>> handleMonitorData(List<DeviceVo> list, List<String> dates){
		List<List<String>> result = new ArrayList<List<String>>();
		if(list != null && !list.isEmpty() && dates != null && !dates.isEmpty()) {
			for(DeviceVo item: list) {
				List<String> tempList = new ArrayList<>();
				tempList.add(item.getDevno());
				tempList.add(item.getDeveui());
				tempList.add(item.getModeName());
				if(item.getOriginals() == null || item.getOriginals().isEmpty()) {
					for(int i=0;i<dates.size();i++) {
						tempList.add("未上报");
					}
				}else {
					for(String date: dates) {
						boolean flag = false;
						for(OriginalVo or: item.getOriginals()) {
							if(date.equals(or.getMonthDay())) {
								tempList.add(or.getWater());
								flag = true;
								break;
							}
						}
						if(!flag) {
							tempList.add("未上报");
						}
					}
				}
				result.add(tempList);
			}
		}
		return result;
	}

}
