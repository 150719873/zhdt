package com.dotop.pipe.server.rest.service.auth;

import com.dotop.pipe.auth.core.form.AuthForm;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.auth.web.api.factory.auth.IAuthFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @date 2019年1月16日
 */
@RestController
@RequestMapping("/auth")
public class AuthController implements BaseController<AuthForm> {

    private final static Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private IAuthFactory iAuthFactory;

    /**
     * 鉴权
     *
     * @param authForm
     * @return
     */
    @PostMapping(value = "/authentication", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String authentication(@RequestBody AuthForm authForm) {
        logger.info(LogMsg.to("authForm", authForm));

        String cas = authForm.getCas();
        // String userId = authForm.getUserId();
        // String ticket = authForm.getTicket();
        // 验证
        VerificationUtils.string("cas", cas, false, 500);
        // VerificationUtils.string("userId", userId);
        // VerificationUtils.string("ticket", ticket);

        // TODO
        // cas =
        // "FGfuokzh8yh194c7C_NZnpMj1I9sUv0rZaGqfVsun4H2Q2P_jpRg5JrLMjVL1BWOGQId7fak-7XZ#UI6g96XKO8zJJXMMnEbeRBLRKUYVIUs=";
        // cas =
        // "FGfuokzh8yh194c7C_NZnm6vsharKpOnvIjrIRUV2EUCZFbmvj71st23WvKag1XWRgOXtOFaTPdR#72FagKPd-VqWcL55iIHui7h0MK3A6W0sW5rRBKrlwyFzxsT2IYHi";

        LoginCas loginCas = iAuthFactory.authentication(cas);
        return resp("loginCas", loginCas);
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping(value = "/loginOut", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String loginOut() {
        iAuthFactory.loginOut();
        return resp();
    }

    /**
     * 校验是否存在
     *
     * @return
     */
    @PostMapping(value = "/isOnline", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String isOnline() {
        return resp();
    }

    /**
     * 校验是否存在
     *
     * @param authForm
     * @return
     */
    @PostMapping(value = "/cas", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String cas(@RequestBody AuthForm authForm) {
        String modelId = authForm.getModelId();
        VerificationUtils.string("modelId", modelId);
        String cas = iAuthFactory.cas(modelId);
        return resp("cas", cas);
    }

    /**
     * 修改
     *
     * @param mapType
     * @return
     */
    @PutMapping(value = "/changeMap/{mapType}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editMap(@PathVariable("mapType") String mapType) {
        logger.info(LogMsg.to("mapType", mapType));
        // 校验
        VerificationUtils.string("mapType", mapType);
        iAuthFactory.editMap(mapType, null);
        return resp();
    }

    /**
     * 修改
     *
     * @param params
     * @return
     */
    @PutMapping(value = "/changeMap/protocols", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editMap(@RequestBody Map<String, Object> params) {
        Object protocols = params.get("protocols");
        logger.info(LogMsg.to(protocols, protocols));
        if (protocols != null && protocols instanceof List) {
            iAuthFactory.editMap(null, (List<String>) protocols);
        }
        return resp();
    }
}
