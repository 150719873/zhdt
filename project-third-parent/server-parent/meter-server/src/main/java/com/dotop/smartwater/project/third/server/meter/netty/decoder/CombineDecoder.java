package com.dotop.smartwater.project.third.server.meter.netty.decoder;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 康宝莱tcp解码器
 *
 *
 */
@ChannelHandler.Sharable
public class CombineDecoder extends MessageToMessageDecoder<String> {

    private static final Logger logger = LogManager.getLogger(CombineDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) {
//        logger.info("data:{}", msg);
        Map<String, String> params = new HashMap<>();
        params.put("key", "combine");
        params.put("data", msg);
        out.add(JSONUtils.toJSONString(params));
    }
}
