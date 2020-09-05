package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @program: project-parent
 * @description: 收费区间

 * @create: 2019-02-26 09:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LadderVo extends BaseVo {
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

  private List<LadderPriceVo> ladderPrices;

  /* 用于接收参数 */
  private List<CompriseVo> comprise;
}
