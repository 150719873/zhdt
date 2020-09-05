package com.dotop.pipe.core.utils;

import com.dotop.pipe.core.dto.historylog.ChangeDto;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.CompareUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

// TODO 对比优化修改
public class HistoryLogUtils {

	private final static Logger logger = LogManager.getLogger(HistoryLogUtils.class);

	public final static List<ChangeDto> compareObj(DeviceVo deviceVo, DeviceForm deviceForm) {

		/*
		 * DeviceVo deviceVo = new DeviceVo();
		 * 
		 * deviceVo.setDeviceId("1234567"); deviceVo.setCode("1235");
		 * deviceVo.setName("nameddd"); deviceVo.setDes("maiou");
		 * deviceVo.setAddress("1125"); deviceVo.setLength("1252");
		 * deviceVo.setDepth("152525"); deviceVo.setPipeElevation("dddd");
		 * deviceVo.setGroundElevation("122"); deviceVo.setInstallDate(new Date());
		 * 
		 * DeviceForm deviceForm = new DeviceForm(); deviceForm.setDeviceId("1234567");
		 * deviceForm.setCode("1235"); deviceForm.setName("nameddd");
		 * deviceForm.setDes("maioshu"); deviceForm.setAddress("12225");
		 * deviceForm.setLength("1252"); deviceForm.setDepth("12");
		 * deviceForm.setPipeElevation("dddd"); deviceForm.setGroundElevation("122");
		 * deviceForm.setInstallDate(new Date()); deviceForm.setProductId("123522");
		 * deviceForm.setAreaId("ddd");
		 */

		// 获取参数类
		Class deviceFormClass = deviceForm.getClass();
		// 将参数类转换为对应属性数量的Field类型数组（即该类有多少个属性字段 N 转换后的数组长度即为 N）
		Field[] formfields = deviceFormClass.getDeclaredFields();
		Map formFieldMap = new HashMap<String, String>();

		Class deviceVoClass = deviceVo.getClass();
		Field[] vofields = deviceVoClass.getDeclaredFields();
		Map voFieldMap = new HashMap<String, Field>();

		for (int i = 0; i < vofields.length; i++) {
			Field vofield = vofields[i];
			vofield.setAccessible(true);
			try {
				voFieldMap.put(vofield.getName(), vofield);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		List<ChangeDto> changelist = new ArrayList<ChangeDto>();
		for (int i = 0; i < formfields.length; i++) {
			Field formfield = formfields[i];
			formfield.setAccessible(true);
			try {
				// 1 form表单的字段名称
				String fieldName = formfield.getName();
				// 2 form表单的字段值
				Object formFieldVal = formfield.get(deviceForm);
				// 3 form表单的字段类型
				String fieldType = formfield.getGenericType().getTypeName();
				// 4 判断vo 是否有相同的字段名称
				if (voFieldMap.containsKey(fieldName) && formFieldVal != null) {
					Field voField = (Field) voFieldMap.get(fieldName);
					// 5 判断 两个字段类型是否相同
					if (fieldType.equals(voField.getGenericType().getTypeName())) {
						Object voFieldVal = voField.get(deviceVo);
						// 6 类型相等
						String fieldOldVal = null;
						String fieldNewVal = null;

						switch (fieldType) {
						case "java.util.Date":
							DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							fieldNewVal = format.format(formFieldVal);
							fieldOldVal = format.format(voFieldVal);
							break;
						case "java.util.Integer":
							fieldNewVal = String.valueOf(formFieldVal);
							fieldOldVal = String.valueOf(voFieldVal);
						default:
							fieldNewVal = String.valueOf(formFieldVal);
							fieldOldVal = String.valueOf(voFieldVal);
							break;
						}

						if (StringUtils.isNotBlank(fieldNewVal) && !fieldNewVal.equals(fieldOldVal)) {
							ChangeDto changeDto = new ChangeDto();
							changeDto.setFieldName(fieldName);
							changeDto.setFieldNewVal(fieldNewVal);
							changeDto.setFieldOldVal(fieldOldVal);
							changelist.add(changeDto);
						}
					}
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println(formFieldMap.toString());
		System.out.println(changelist.toString());
		return changelist;
	}

	public final static <T> List<ChangeDto> compareForDevice(T obj1, T obj2, Set<String> filters)
			throws FrameworkRuntimeException {
		try {
			List<ChangeDto> result = new ArrayList<ChangeDto>();
			Map<String, Object> map1 = CompareUtils.objectToMap(obj1);// 修改后的值
			Map<String, Object> map2 = CompareUtils.objectToMap(obj2);// 修改之前的

			// 遍历字段
			Iterator<Entry<String, Object>> iter1 = map1.entrySet().iterator();
			while (iter1.hasNext()) {
				Map.Entry<String, Object> entry1 = (Entry<String, Object>) iter1.next();
				String key = entry1.getKey();
				// 修改后值
				Object m1value = entry1.getValue();
				Object m2value = null;
				if (filters != null && filters.contains(key)) {
					continue;
				}

				if (m1value != null) {

					// 1 vo里面 这些字段是对象 不能和对象比较 要和对象中的某个字段比较
					// 2 字段的属性名称有的不一样 points pointIds 等 要做特殊处理
					switch (entry1.getKey()) {
					case "pointIds": // 坐标改变
						// form 表单中的是 pointIds vo 中对应的时area 对象
						// Object m2value = map2.get(entry1.getKey());
						;
						break;
					case "productId": // 产品id
						ProductVo productVo = (ProductVo) map2.get("product");
						m2value = productVo.getProductId();
						break;
					case "area":
						AreaModelVo areaModelVo = (AreaModelVo) map2.get("area"); // 修改之前的
						// m2value = areaModelVo.getAreaCode();
						m2value = areaModelVo.getName();
						AreaModelVo newAreaModelVo = (AreaModelVo) m1value;// 修改之后的
						// m1value = newAreaModelVo.getAreaCode();
						m1value = newAreaModelVo.getName();
						// key = "areaName";
						break;
					case "laying":
						DictionaryVo dictionaryVo = (DictionaryVo) map2.get("laying");
						m2value = dictionaryVo.getVal();
						break;
					default:
						m2value = map2.get(entry1.getKey());
						break;
					}

					boolean flag = CompareUtils.equals(m1value, m2value);
					if (!flag) {
						ChangeDto changeDto = new ChangeDto(key, m2value, m1value);
						result.add(changeDto);
					}
				}
			}

			return result;
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION);
		}
	}

}
