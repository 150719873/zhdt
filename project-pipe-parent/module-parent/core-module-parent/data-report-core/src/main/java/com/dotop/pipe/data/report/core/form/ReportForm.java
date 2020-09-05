package com.dotop.pipe.data.report.core.form;

import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 * @date 2018/11/2.
 */
@Data
public class ReportForm {

    /**
     * 设备id集合
     */
    private List<String> deviceIds;
    /**
     * 日期类型
     */
    private DateTypeEnum dateType;
    /**
     * 数据类型
     */
    private FieldTypeEnum[] fieldType;
    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 同比环比
     */
    private String ratio;

}
