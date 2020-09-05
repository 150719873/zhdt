package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 收费区间
 *

 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class LadderForm extends BaseForm {

  private String id;

  /* 收费种类ID */
  private String typeid;
  /* 阶梯编号 */
  private int ladderno;
  /*区间最小值*/
  private Double minval;
  /* 最大用水量 */
  private Double maxval;
  /* 单价 */
  private Double price;
  /* 创建时间 */
  private String createtime;

  private List<LadderPriceForm> ladderPrices;

  /* 用于接收参数 */
  private List<CompriseForm> comprise;
}
