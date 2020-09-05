package com.dotop.pipe.api.client.core;

import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.device.DeviceVo;

import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public final class WaterDictUtils {

    public static void laying(DictionaryVo dictionary, DeviceVo device) {
        DictionaryVo laying = device.getLaying();
        if (laying != null && dictionary != null && dictionary.getChildren() != null && !dictionary.getChildren().isEmpty()) {
            String id = laying.getId();
            Map<String, DictionaryVo> childrenMap = dictionary.getChildren().stream().collect(Collectors.toMap(DictionaryVo::getId, a -> a, (k1, k2) -> k1));
            DictionaryVo child = childrenMap.get(id);
            if (child != null) {
                device.setLaying(child);
            }
        }
    }

}
