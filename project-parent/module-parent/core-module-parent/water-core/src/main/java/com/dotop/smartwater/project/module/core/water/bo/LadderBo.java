package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
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
public class LadderBo extends BaseBo {

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

  private List<LadderPriceBo> ladderPrices;

  /* 用于接收参数 */
  private List<CompriseBo> comprises;
}
