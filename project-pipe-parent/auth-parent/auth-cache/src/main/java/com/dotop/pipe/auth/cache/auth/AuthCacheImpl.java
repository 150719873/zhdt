package com.dotop.pipe.auth.cache.auth;

import com.dotop.pipe.auth.api.cache.auth.IAuthCache;
import com.dotop.pipe.auth.core.constants.CacheKey;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class AuthCacheImpl extends IAuthCache { // BaseCache implements

    @Override
    public String set(LoginCas loginCas) throws FrameworkRuntimeException {
        String userId = loginCas.getUserId();

        // 删除旧缓存
        String ot = cacheOpsForValue.get(CacheKey.ut(userId));
        if (StringUtils.isNoneBlank(ot)) {
            cacheRedisTemplate.delete(new ArrayList<String>() {
                private static final long serialVersionUID = 3514837515809187420L;
                {
                    add(CacheKey.ut(userId));
                    add(CacheKey.tu(ot));
                }
            });
        }
        // 设置新缓存
//		String token = UuidUtils.getUuid();
//		loginCas.setToken(token);
//		cacheOpsForValue.set(CacheKey.tu(token), JSONUtils.toJSONString(loginCas), authCasExpire, TimeUnit.SECONDS);
//		cacheOpsForValue.set(CacheKey.ut(userId), token, authCasExpire, TimeUnit.SECONDS);
        cacheOpsForValue.set(CacheKey.ut(userId), JSONUtils.toJSONString(loginCas), authCasExpire, TimeUnit.SECONDS);
        return null;
    }

    @Override
    public LoginCas get(String userId) throws FrameworkRuntimeException {
        String ut = cacheOpsForValue.get(CacheKey.ut(userId));
        if (StringUtils.isNoneBlank(ut)) {
            LoginCas loginCas = JSONUtils.parseObject(ut, LoginCas.class);
            cacheRedisTemplate.expire(CacheKey.ut(userId), authCasExpire, TimeUnit.SECONDS);
            return loginCas;
        }
        return null;
    }

    @Override
    public void del(LoginCas loginCas) throws FrameworkRuntimeException {
        String userId = loginCas.getUserId();
        cacheRedisTemplate.delete(CacheKey.ut(userId));
//        String ot = cacheOpsForValue.get(CacheKey.ut(userId));
//        if (StringUtils.isNoneBlank(ot)) {
//            cacheRedisTemplate.delete(new ArrayList<String>() {
//                private static final long serialVersionUID = 3514837515809187420L;
//                {
//                    add(CacheKey.ut(userId));
//                    add(CacheKey.tu(ot));
//                }
//            });
//        }
    }

}
