package com.dotop.smartwater.project.module.api.revenue.impl;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.IDesignPrintFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.BillBo;
import com.dotop.smartwater.project.module.core.water.bo.DesignPrintBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.PrintBindBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.BillForm;
import com.dotop.smartwater.project.module.core.water.form.DesignPrintForm;
import com.dotop.smartwater.project.module.core.water.form.PrintBindForm;
import com.dotop.smartwater.project.module.core.water.utils.PrintUtils;
import com.dotop.smartwater.project.module.core.water.vo.BillVo;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderExtVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.DesignFieldVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.service.revenue.IBillService;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.tool.IDesignPrintService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 票据管理
 *

 * @date 2019年2月25日
 */
@Component
public class DesignPrintFactoryImpl implements IDesignPrintFactory {

	private static final Logger logger = LoggerFactory.getLogger(DesignPrintFactoryImpl.class);

	@Autowired
	private IDesignPrintService iDesignPrintService;

	@Autowired
	private IOrderService iOrderService;
	@Autowired
	private IOwnerService iOwnerService;
	@Autowired
	private IBillService iBillService;

	@Override
	public List<PrintBindVo> templateList(PrintBindForm printBindForm) throws FrameworkRuntimeException {
		return iDesignPrintService.templateList(BeanUtils.copy(printBindForm, PrintBindBo.class));
	}

	@Override
	public String view(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		try {
			DesignPrintVo template = iDesignPrintService.get(designPrintForm.getTempid());
			template.setContent(PrintUtils.changeContent(template.getContent(), false));
			template.setSqlstr(PrintUtils.changeContent(template.getSqlstr(), false));
			Map<String, String> data = getView(template.getSqlstr(), designPrintForm);

			String content = "";
			if (data == null) {
				content = "LODOP.ADD_PRINT_TEXT(62,222,253,39,\"未查询到打印数据！！\"); "
						+ "LODOP.SET_PRINT_STYLEA(0,\"FontSize\",18); LODOP.SET_PRINT_STYLEA(0,\"FontColor\",\"#FF0000\");";
			} else {
				content = PrintUtils.matcherData(data, template.getContent(), template);
			}
			return content;
		} catch (Exception e) {
			logger.error("save", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}

	/**
	 * 水务调打印详情
	 */
	@Override
	public String getPrintView(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		try {
			UserVo user = AuthCasClient.getUser();
			DesignPrintVo template = iDesignPrintService.get(designPrintForm.getTempid());
			Map<String, String> data = null;
			if (template != null) {
				template.setContent(PrintUtils.changeContent(template.getContent(), false));
				template.setSqlstr(PrintUtils.changeContent(template.getSqlstr(), false));
				data = getView(template.getSqlstr(), designPrintForm);
			}
			String content = "";
			if (data == null) {
				content = "LODOP.ADD_PRINT_TEXT(62,222,253,39,\"未查询到打印数据！！\"); "
						+ "LODOP.SET_PRINT_STYLEA(0,\"FontSize\",18); LODOP.SET_PRINT_STYLEA(0,\"FontColor\",\"#FF0000\");";
			} else {
				content = PrintUtils.matcherData(data, template.getContent(), template);
				if (user != null) {
					// 记录打印数据
					BillBo bill = new BillBo();
					bill.setOperauserid(user.getUserid());
					bill.setOperaname(user.getName());
					bill.setPrintTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					bill.setEnterpriseid(user.getEnterpriseid());
					bill.setTempid(designPrintForm.getTempid());

					if (StringUtils.isNotBlank(designPrintForm.getOwnerId())) {
						OwnerBo ownerBo = new OwnerBo();
						ownerBo.setOwnerid(designPrintForm.getOwnerId());
						OwnerVo owner = iOwnerService.findByOwnerId(ownerBo);
						bill.setUserno(owner.getUserno());
						bill.setUsername(owner.getUsername());
						bill.setOwnerid(owner.getOwnerid());
						bill.setStatus(1);
						bill.setTempcontent(content);
						bill.setDatacontent(content);
						bill.setBusinessid(designPrintForm.getBussinessId());
						bill.setTempid(designPrintForm.getTempid());
						bill.setTempname(template.getName());
						// 解决汉字乱码问题
						String typename = new String(designPrintForm.getTypename().getBytes("ISO-8859-1"), "UTF-8");
						bill.setType(typename);
						iBillService.add(bill);
					}
				}
			}
			return content;
		} catch (Exception e) {
			logger.error("save", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}

	public Map<String, String> getView(String sql, DesignPrintForm designPrintForm) {
		Map<String, String> map = null;
		try {
			if (StringUtils.isBlank(designPrintForm.getVal()) || designPrintForm.getVal().equals("undefined")) {
				sql += " limit 1";
				map = iDesignPrintService.getView(sql);
			} else {
				sql += " where " + designPrintForm.getName() + " = " + "'" + designPrintForm.getVal() + "' limit 1";

				if (designPrintForm.getPaytype() != null && designPrintForm.getPaytype() == 1) {
					OrderExtVo orderExt = iOrderService.findOrderExtByTradeno(designPrintForm.getVal());
					List<LadderPriceDetailVo> list = JSON.parseArray(orderExt.getChargeinfo(),
							LadderPriceDetailVo.class);
					String paydetail = "";
					if (list != null && list.size() > 0) {
						for (LadderPriceDetailVo p : list) {
							paydetail += p.getName() + ":" + p.getAmount() + "元 ";
						}
					}
					sql = sql.replaceAll("%s", paydetail);
				}
				map = iDesignPrintService.getView(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, String> preview(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		try {
			if (designPrintForm == null || StringUtils.isBlank(designPrintForm.getSqlstr())) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "Sql不能为空");
			}
			List<Map<String, String>> list = iDesignPrintService
					.getSqlView(designPrintForm.getSqlstr() + " limit " + WaterConstants.PAGECOUNT);
			if (list != null && list.size() > 0) {
				Map<String, String> map = list.get(0);
				List<String> propertys = new ArrayList<String>();
				Iterator<String> iter = map.keySet().iterator();
				while (iter.hasNext()) {
					propertys.add(iter.next());
				}

				Map<String, String> data = new HashMap<String, String>();
				data.put("data", JSON.toJSONString(list));
				data.put("propertys", JSON.toJSONString(propertys));
				data.put("total", "" + list.size());
				return data;
			} else {
				throw new FrameworkRuntimeException(ResultCode.Fail, "查询结果至少需要一条数据");
			}
		} catch (Exception e) {
			logger.error("preview", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, "SQL运行异常，请检查");
		}
	}

	@Override
	public Pagination<DesignPrintVo> getDesignPrintList(DesignPrintForm designPrintForm)
			throws FrameworkRuntimeException {
		DesignPrintBo designPrintBo = BeanUtils.copyProperties(designPrintForm, DesignPrintBo.class);
		return iDesignPrintService.getDesignPrintList(designPrintBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void saveDesignPrint(DesignPrintForm design) throws FrameworkRuntimeException {

		if (design.getId() != null && !"".equals(design.getId())) {
			design.setContent(PrintUtils.changeContent(design.getContent(), true));
			design.setSqlstr(PrintUtils.changeContent(design.getSqlstr(), true));
			DesignPrintBo designPrintBo = BeanUtils.copyProperties(design, DesignPrintBo.class);
			iDesignPrintService.updateDesignPrint(designPrintBo);
		} else {
			design.setContent(PrintUtils.changeContent(design.getContent(), true));
			design.setSqlstr(PrintUtils.changeContent(design.getSqlstr(), true));
			design.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			DesignPrintBo designPrintBo = BeanUtils.copyProperties(design, DesignPrintBo.class);
			iDesignPrintService.saveDesignPrint(designPrintBo);
		}

	}

	@Override
	public String viewDetail(BillForm bill) throws FrameworkRuntimeException {
		try {
			BillVo bl = iBillService.getById(bill.getId());
			if (bl != null) {
				return bl.getTempcontent();
			} else {
				String content = "LODOP.ADD_PRINT_TEXT(62,222,253,39,\"未查询到打印数据！！\"); "
						+ "LODOP.SET_PRINT_STYLEA(0,\"FontSize\",18); LODOP.SET_PRINT_STYLEA(0,\"FontColor\",\"#FF0000\");";
				return content;
			}
		} catch (Exception e) {
			logger.error("save", e);
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
		}
	}

	@Override
	public DesignPrintVo get(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		DesignPrintVo print = iDesignPrintService.get(designPrintForm.getId());
		List<DesignFieldVo> fields = iDesignPrintService.getFields(designPrintForm.getId());
		if (fields != null && fields.size() > 0) {
			StringBuffer sf = new StringBuffer();
			for (DesignFieldVo df : fields) {
				if (sf.toString().equals("")) {
					sf.append(df.getField());
				} else {
					sf.append("," + df.getField());
				}
			}
			print.setFields(sf.toString());
		}
		return print;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void delete(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		iDesignPrintService.deleteDesignPrint(designPrintForm.getId());
		iDesignPrintService.deleteDesignFields(designPrintForm.getId());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addTemplate(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		designPrintForm.setStatus(WaterConstants.PRINT_TEMPLATE_STATUS_ENABLE);
		designPrintForm.setCreateTime(DateUtils.formatDate(new Date()));
		DesignPrintBo designPrintbo = BeanUtils.copy(designPrintForm, DesignPrintBo.class);
		iDesignPrintService.addTemplate(designPrintbo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateTemplate(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		DesignPrintBo designPrintbo = BeanUtils.copy(designPrintForm, DesignPrintBo.class);
		iDesignPrintService.updateTemplate(designPrintbo);
	}

	@Override
	public DesignPrintVo getPrintStatus(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		return iDesignPrintService.getPrintStatus(user.getEnterpriseid(), designPrintForm.getType());
	}

	@Override
	public String getPreview(DesignPrintForm designPrintForm) throws FrameworkRuntimeException {
		BillVo billvo = iBillService.getById(designPrintForm.getId());
		if (billvo != null) {
			return billvo.getDatacontent();
		} else {
			String content = "LODOP.ADD_PRINT_TEXT(62,222,253,39,\"未查询到打印数据！！\"); "
					+ "LODOP.SET_PRINT_STYLEA(0,\"FontSize\",18); LODOP.SET_PRINT_STYLEA(0,\"FontColor\",\"#FF0000\");";
			return content;
		}

	}
}
