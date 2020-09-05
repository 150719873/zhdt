package com.dotop.smartwater.project.third.server.meter.netty.encoder;

import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * 康宝莱tcp编码器
 *
 *
 */
public class CombineEncoder extends StringEncoder {

    public CombineEncoder(Charset charset) {
        super(charset);
    }
}
