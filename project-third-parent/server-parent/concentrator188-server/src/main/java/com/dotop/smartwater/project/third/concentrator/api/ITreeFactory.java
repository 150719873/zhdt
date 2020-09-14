package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;
import java.util.Map;

public interface ITreeFactory {

    Map<String, List<ConcentratorVo>> tree() throws FrameworkRuntimeException;
}
