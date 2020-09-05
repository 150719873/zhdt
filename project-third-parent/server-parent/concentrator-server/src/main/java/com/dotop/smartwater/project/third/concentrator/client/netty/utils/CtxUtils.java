package com.dotop.smartwater.project.third.concentrator.client.netty.utils;

import com.dotop.smartwater.project.third.concentrator.client.netty.business.StructureMsg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-11 16:15
 **/
@Service
public class CtxUtils {
    private static final Logger log = LoggerFactory.getLogger(CtxUtils.class);

    @Autowired
    private StructureMsg structureMsg;

    public static Map<String, ChannelHandlerContext> CTX_MAP = new ConcurrentHashMap<>();

    public static final String CONNECTION_CHANGE = "CONNECTION_CHANGE";

    public boolean login(String ctxName, ChannelHandlerContext ctx){
        boolean flag = false;
        ChannelHandlerContext oldCtx = CtxUtils.CTX_MAP.get(ctxName);
        String nameNum = ToolUtils.concentratorEncode(ctxName);
        if (oldCtx == null) {
            //把地址域放进map
            CtxUtils.CTX_MAP.put(ctxName, ctx);
            log.info("{} 登录成功", nameNum);
        } else {
            if(!ctx.equals(oldCtx)){
                log.info("{} 连接更新", nameNum);
                CtxUtils.CTX_MAP.remove(ctxName);
                oldCtx.close();

                //把新连接放进map
                CtxUtils.CTX_MAP.put(ctxName, ctx);

                flag = true;
            }
        }

        //发送回执
        PushUtils.sendMsg(ctx, structureMsg.makeLoginSussessMsg(ctxName));
        log.info("服务器 回复 [{}] 登录成功", nameNum);

        return flag;
    }

    public void heartBeat(String ctxName, ChannelHandlerContext ctx) {
        String nameNum = ToolUtils.concentratorEncode(ctxName);
        PushUtils.sendMsg(ctx, structureMsg.makeHeartBeatSussessMsg(ctxName));
        log.info("服务器 回复 [{}] 心跳成功", nameNum);
    }
}
