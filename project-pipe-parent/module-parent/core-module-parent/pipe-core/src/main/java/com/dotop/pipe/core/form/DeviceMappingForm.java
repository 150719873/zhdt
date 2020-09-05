package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceMappingForm extends BasePipeForm {

    private String mapId;
    private String deviceId;
    private String otherId;
    private String direction;
    private String level;
    private List<String> deviceIds;
    private List<String> otherIds;

    // 正向新增
    private List<String> addPositiveOtherIds;
    // 反向新增
    private List<String> addReverseOtherIds;

    // 反向删除
    private List<String> delMapIds;
}
