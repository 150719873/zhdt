package com.dotop.pipe.auth.core.vo.enterprise;

import lombok.Data;

import java.util.List;

@Data
public class EnterpriseVo {

    public EnterpriseVo() {
        super();
    }

    public EnterpriseVo(String enterpriseId, String eid) {
        super();
        this.enterpriseId = enterpriseId;
        this.eid = eid;
    }

    private String enterpriseId;

    private String enterpriseName;

    private String eid;
    private String mapType;

    //支持的协议
    private List<String> protocols;

}
