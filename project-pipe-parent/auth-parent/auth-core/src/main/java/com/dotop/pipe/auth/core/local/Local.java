package com.dotop.pipe.auth.core.local;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;

import java.util.Date;

public class Local {

    private final static ThreadLocal<LocalObj> threadSession = new ThreadLocal<LocalObj>();

    public final static void add(LoginCas loginCas) {
        LocalObj obj = new LocalObj();
        obj.setToken(loginCas.getToken());
        obj.setLoginCas(loginCas);
        obj.setCurr(new Date());
        threadSession.set(obj);
    }

    public final static LocalObj get() {
        return threadSession.get();
    }

    public final static String getToken() {
        if (threadSession.get() != null) {
            return threadSession.get().getToken();
        }
        return null;
    }

    public final static LoginCas getLoginCas() {
        if (threadSession.get() != null) {
            return threadSession.get().getLoginCas();
        }
        return null;
    }

    public final static Date getCurr() {
        if (threadSession.get() != null) {
            return threadSession.get().getCurr();
        }
        return null;
    }

}
