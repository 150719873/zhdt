package com.dotop.smartwater.project.third.module.core.third.video.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerVo extends BaseVo {

    /**
     * 客户资料记录id
     */
    private Integer id;

    /**
     *客户编号
     */
    private String ccid;

    /**
     *客户名称
     */
    private String cname;

    /**
     * 客户地址
     */
    private String caddress;

    /**
     * 客户备注信息
     */
    private String cinfo;

    /**
     * 客户资料所属册号
     */
    private String bookname;

    /**
     * 客户使用水表信息
     */
    private List<IMeterVo> imeter;

}
