package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 *  库存盘点VO

* @date 2018-12-05 下午 19:09
*
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryVo extends BaseVo {

	private String inventoryId; //库存盘点主键
	
	private Integer inTotal; //已入库总量
	
	private Integer outTotal; //已出库总量
	
	private Integer stockTotal; //库存总量
	
	private String productNo;
	private String name;
	
	private StoreProductVo product; //产品类
}
