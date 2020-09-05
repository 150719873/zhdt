package com.dotop.smartwater.project.third.concentrator.core.constants;

/**
 *
 */
public final class LockKey {

    public static String asyncPool(String enterpriseid, String concentratorCode) {
        return "lock_cd_async_pool:" + enterpriseid + ":" + concentratorCode;
    }
}
