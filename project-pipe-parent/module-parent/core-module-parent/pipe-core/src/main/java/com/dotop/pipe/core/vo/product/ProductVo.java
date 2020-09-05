package com.dotop.pipe.core.vo.product;

import com.dotop.pipe.core.vo.factory.FactoryVo;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/25.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductVo extends BasePipeVo {

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 厂家
     */
    private FactoryVo factory;

    /**
     * 产品类别
     */
    private DictionaryVo category;

    /**
     * 产品类型
     */
    private DictionaryVo type;

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

    /**
     * 口径
     */
    private DictionaryVo caliber;

    /**
     * 材质
     */
    private DictionaryVo material;

    /**
     * 设备数量
     */
    private Integer deviceCount;

    /**
     * 水务平台库存
     */
    private InventoryVo inventory;

    /**
     * 设施一对一地图设备
     */
    private DeviceVo device;
}
