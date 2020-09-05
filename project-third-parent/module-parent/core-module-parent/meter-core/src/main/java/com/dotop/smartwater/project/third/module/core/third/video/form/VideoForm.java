package com.dotop.smartwater.project.third.module.core.third.video.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class VideoForm extends BaseForm {

    /**
     * id
     */
    private Integer id;

    /**
     * 从第几条记录开始获取客户列，从0开始
     */
    private Integer from;

    /**
     * 每次获取多少条数据
     */
    private Integer size;

    /**
     * 开始时间
     */
    private Date starttime;

    /**
     * 结束时间
     */
    private Date endtime;

    /**
     * 水表编号
     */
    private String meterid;

    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 客户编号
     */
    private String ccid;

    /**
     * 是否已核对
     */
    private String checked;

    /**
     * 默认为readtime按读表时间排序
     */
    private String order;

    /**
     * 升序或降序
     */
    private String sort;


}
