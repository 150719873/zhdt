package com.dotop.pipe.api.client;

import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 *
 */
public interface IWaterDictClient {

    DictionaryVo get(String category) throws FrameworkRuntimeException;
}
