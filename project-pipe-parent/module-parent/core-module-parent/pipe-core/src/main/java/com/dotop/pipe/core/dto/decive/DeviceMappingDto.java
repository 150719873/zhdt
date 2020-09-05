package com.dotop.pipe.core.dto.decive;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 * @date 2018/11/12.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceMappingDto extends BasePipeDto {
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
