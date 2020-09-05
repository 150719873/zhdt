package com.dotop.smartwater.project.third.server.meter.netty.decoder;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.server.meter.core.NumberUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 公共解码器
 *
 *
 */
public class CommonDecoder extends StringDecoder {

    private static final Logger logger = LogManager.getLogger(CommonDecoder.class);
    private static final String hexes = "0123456789ABCDEF";

    private Charset charset;

    public CommonDecoder(Charset charset) {
        super(charset);
        this.charset = charset;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        try {
            byte[] bytes = new byte[msg.readableBytes()];
            msg.readBytes(bytes);
            String data = NumberUtils.bytesToHex(bytes);
            String remoteAddress = String.valueOf(ctx.channel().remoteAddress());
            String localAddress = String.valueOf(ctx.channel().localAddress());
//        logger.info("common:{},address:{},bytes:{}", data, address, ("" + bytes[0] + bytes[1] + bytes[2] + bytes[3]));
            logger.info("common:{},remoteAddress:{},localAddress:{}", data, remoteAddress, localAddress);
            out.add(data);
        } catch (Exception e) {
            logger.error(LogMsg.to("decode发生错误", e));
        }
    }
}
