package com.dotop.pipe.core.vo.product;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InventoryVo extends BasePipeVo {

    /**
     * 已入库总量
     */
    private Integer inTotal;
    /**
     * 已出库总量
     */
    private Integer outTotal;
    /**
     * 库存总量
     */
    private Integer stockTotal;

    private String productId;

    private String code;

}
