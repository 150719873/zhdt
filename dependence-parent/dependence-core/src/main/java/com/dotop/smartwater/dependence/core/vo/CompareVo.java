package com.dotop.smartwater.dependence.core.vo;

import lombok.Data;

@Data
public class CompareVo {

    public CompareVo() {
        super();
    }

    public CompareVo(String key, Object beforeValue, Object afterValue) {
        super();
        this.key = key;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
    }

    private String key;

    private Object beforeValue;

    private Object afterValue;
}
