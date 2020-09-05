package com.dotop.smartwater.view.server.core.maintain.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MaintainLogForm extends BasePipeForm {

    /**
     * 设备id
     */
    private String id;

    /**
     * 产品类别
     */
    private String category;
    /**
     * 产品类型
     */
    private String type;

    private String content;

    private String address;

    private Integer isDel;

}
