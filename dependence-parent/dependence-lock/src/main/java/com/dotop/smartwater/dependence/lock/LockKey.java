package com.dotop.smartwater.dependence.lock;

public class LockKey {

    public final static String add(String enterpriseId, String code) {
        return "lock_add" + ":" + enterpriseId + ":" + code;
    }

    public final static String addPipe(String enterpriseId, String code) {
        return "lock_pipe_add" + ":" + enterpriseId + ":" + code;
    }

    public static String addEnterprise(String eid) {
        return "lock_enterprise_add" + ":" + eid;
    }
}
