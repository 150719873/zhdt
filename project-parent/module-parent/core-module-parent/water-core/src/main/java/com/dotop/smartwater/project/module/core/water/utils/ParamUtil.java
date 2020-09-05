package com.dotop.smartwater.project.module.core.water.utils;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: project-parent
 * @description: 参数校验

 * @create: 2019-11-28 11:25
 **/
public class ParamUtil {

    private static final String checkNumAndLittle = "^[A-Za-z0-9]*$";
    private static final String checkPhone = "^[0-9-]*$";

    public static final String WATER_TRADITION = "传统表";

    public static final String WATER_REMOTE = "远传表";

    public static String checkOwnerImportParam(int i, String[][] map, Map<String, String> areaNoteMap,
                                               Map<String, PayTypeVo> priceType,
                                               Map<String, String> devNoMap) {
        List<String> resultLi = new ArrayList<>();

        String areaNo = map[i][0].toUpperCase();
        String bookNum = map[i][1];
        String ownerNo = map[i][2];
        String ownerName = map[i][3];
        String ownerPhone = map[i][4];
        String ownerIdCard = map[i][5];
        String ownerAddress = map[i][6];
        String isOpen = map[i][7];
        String devNo = map[i][8];
        String payName = map[i][9];

        if (StringUtils.isBlank(areaNo)) {
            resultLi.add("[区域编号]列:不能为空");
        } else {
            if (areaNo.length() > 32 || !areaNo.matches(checkNumAndLittle)) {
                resultLi.add("[区域编号]列:格式不符合要求");
            }
            String areaID = areaNoteMap.get(areaNo);
            if (StringUtils.isBlank(areaID)) {
                resultLi.add("[区域编号]列:该区域编号不存在");
            }
        }

        if (StringUtils.isNotBlank(bookNum)) {
            if (bookNum.length() > 16 || !bookNum.matches(checkNumAndLittle)) {
                resultLi.add("[表册号]列:格式不符合要求");
            }
        }

        if (StringUtils.isBlank(ownerNo)) {
            resultLi.add("[业主编号]列:不能为空");
        } else {
            if (ownerNo.length() > 32 || !ownerNo.matches(checkNumAndLittle)) {
                resultLi.add("[业主编号]列:格式不符合要求");
            }
        }

        if (StringUtils.isBlank(ownerName)) {
            resultLi.add("[业主名称]列:不能为空");
        } else {
            if (ownerName.length() > 32) {
                resultLi.add("[业主名称]列:格式不符合要求");
            }
        }

        if (StringUtils.isNotBlank(ownerPhone)) {
            if (ownerPhone.length() > 16 || !ownerPhone.matches(checkPhone)) {
                resultLi.add("[电话号码]列:格式不符合要求");
            }
        }

        if (StringUtils.isNotBlank(ownerIdCard)) {
            if (ownerIdCard.length() > 18 || !ownerIdCard.matches(checkNumAndLittle)) {
                resultLi.add("[身份证]列:格式不符合要求");
            }
        }

        if (StringUtils.isBlank(ownerAddress)) {
            resultLi.add("[业主地址]列:不能为空");
        } else {
            if (ownerAddress.length() > 64) {
                resultLi.add("[业主地址]列:文字过长");
            }
        }
        
        if (StringUtils.isBlank(isOpen)) {
            resultLi.add("[是否开户]列:不能为空");
        } else {
            if (!isOpen.equals("是") && !isOpen.equals("否")) {
                resultLi.add("[是否开户]列:格式不符合要求");
            }
            
            if (isOpen.equals("是")) {
            	if (StringUtils.isNotBlank(devNo)) {
            		if (devNo.length() > 20 || !devNo.matches(checkNumAndLittle)) {
                        resultLi.add("[水表编号]列:格式不符合要求");
                    }
            		
            		String devid = devNoMap.get(devNo);
                    if (devid == null) {
                    	 resultLi.add("水表编号不存在");
                    }
            	} else {
            		resultLi.add("[水表编号]列:开户请填写水表编号");
            	}
            	
            	
            	if (priceType == null || priceType.size() == 0) {
            		resultLi.add("系统未添加收费种类");
            	} else {
            		if (StringUtils.isNotBlank(payName)) {
                		if (payName.length() > 20) {
                            resultLi.add("[收费种类]列:长度过长");
                        }
                        
                        PayTypeVo payType = priceType.get(payName);
                        if (payType == null) {
                        	 resultLi.add("系统没有录入该价格类型");
                        }
                	} else {
                		resultLi.add("[收费种类]列:开户请填写价格类型");
                	}
            	}
            } else {
            	if (StringUtils.isNotBlank(devNo)) {
            		if (devNo.length() > 20 || !devNo.matches(checkNumAndLittle)) {
                        resultLi.add("[水表编号]列:格式不符合要求");
                    }
            		
            		String devid = devNoMap.get(devNo);
                    if (devid == null) {
                    	 resultLi.add("水表编号不存在");
                    }
            	}
            	
            	if (StringUtils.isNotBlank(payName)) {
            		
            		if (priceType == null || priceType.size() == 0) {
                		resultLi.add("系统未添加收费种类");
                	}
            		
            		if (payName.length() > 20) {
                        resultLi.add("[收费种类]列:长度过长");
                    }
                    
                    PayTypeVo payType = priceType.get(payName);
                    if (payType == null) {
                    	 resultLi.add("系统没有录入该价格类型");
                    }
            	}
            }
        }

        
        if (resultLi.size() > 0) {
            return "第" + (i + 1) + "行错误:" + JSONUtils.toJSONString(resultLi);
        }

        return null;
    }

    public static String checkDeviceImportParam(int i, String[][] data,
                                                Map<String, String> modeMap,
                                                Map<String, StoreProductVo> productVoMap) {
        List<String> result = new ArrayList<>();
        String factory = data[i][0];
        String productNo = data[i][1];
        String typeName = data[i][2];
        String devno = data[i][3];
        String deveui = data[i][4];
        String imsi = data[i][5];
        String modeName = data[i][6];
        String taptypeName = data[i][7];
        String beginvalue = data[i][8];
        String water = data[i][9];

        if (StringUtils.isBlank(factory)) {
            result.add("水表厂家名称不能为空");
        } else {
            if (factory.length() > 32) {
                result.add("水表厂家名称长度过长");
            }
        }
        if (StringUtils.isBlank(productNo)) {
            result.add("产品编号不能为空");
        } else {
            if (productNo.length() > 56 || !productNo.matches(checkNumAndLittle)) {
                result.add("产品编号格式不符合要求");
            } else if (productVoMap.get(productNo) == null) {
                result.add(productNo + " : 该产品编号没在系统中添加");
            }
        }
        if (StringUtils.isBlank(typeName)) {
            result.add("水表类型不能为空");
        } else {
            if (typeName.equals(WATER_TRADITION) || typeName.equals(WATER_REMOTE)) {

            } else {
                result.add("水表类型表不存在");
            }
        }

        if (StringUtils.isBlank(devno)) {
            result.add("水表编号不能为空");
        } else {
            if (devno.length() > 20 || !devno.matches(checkNumAndLittle)) {
                result.add("水表编号格式不符合要求");
            }
        }

        // 如果水表类型为远传表，则设备eui不能为空
        if (!StringUtils.isBlank(typeName) && typeName.equals(WATER_REMOTE) && StringUtils.isBlank(deveui)) {
            result.add("选择远传表时设备EUI不能为空");
        } else if (StringUtils.isNotBlank(deveui)) {
            if (deveui.length() > 36 || !deveui.matches(checkNumAndLittle)) {
                result.add("设备EUI格式不符合要求");
            }
        }

        if (StringUtils.isNotBlank(imsi)) {
            if (imsi.length() > 32 || !imsi.matches(checkNumAndLittle)) {
                result.add("imsi格式不符合要求");
            }
        }

        // 如果水表类型为远传表，则通讯方式不能为空
        if (!StringUtils.isBlank(typeName) && typeName.equals(WATER_REMOTE) && StringUtils.isBlank(modeName)) {
            result.add("选择远传表时通讯方式不能为空");
        } else if (StringUtils.isNotBlank(modeName)) {
            if (modeMap.get(modeName) == null) {
                result.add("该通讯方式在系统中不存在");
            }
        }

        if (StringUtils.isNotBlank(taptypeName)) {
            if ("是".equals(taptypeName) || "否".equals(taptypeName)) {

            } else {
                result.add("是否带阀参数错误,只能填 “是” 或者 “否”");
            }
        }

        try {
            Double.parseDouble(beginvalue);
        } catch (Exception e) {
            result.add("初始读数格式错误");
        }

        try {
            Double.parseDouble(water);
        } catch (Exception e) {
            result.add("最新读数格式错误");
        }

        if (result.size() > 0) {
            return "第" + (i + 1) + "行错误:" + JSONUtils.toJSONString(result);
        }
        return null;
    }

}
