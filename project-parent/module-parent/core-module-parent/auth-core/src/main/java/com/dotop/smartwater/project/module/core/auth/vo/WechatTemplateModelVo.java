package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WechatTemplateModelVo extends BaseVo {
    /**获取模板ID*/
    private String template_id;
    /**发送用户编号*/
    private String touser;
    /**点击跳转URL*/
    private String url;
    /**文字颜色*/
    private String topcolor;
    /**模板内容*/
    private Map<String, TemplateDataVo> data;
}