package com.dotop.pipe.core.bo.product;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductBo extends BasePipeBo {


    public ProductBo() {
    }

    public ProductBo(String category, String productId, String code) {
        this.category = category;
        this.productId = productId;
        this.code = code;
    }

    public ProductBo(String category, String productId, String code, String enterpriseId) {
        this.category = category;
        this.productId = productId;
        this.code = code;
        setEnterpriseId(enterpriseId);
    }

    /**
     * 产品ID
     */
    private String productId;
    /**
     * 产品类别
     */
    private String category;
    /**
     * 产品编码
     */
    private String code;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 地址
     */
    private String address;
    /**
     * 产品描述
     */
    private String des;

}
