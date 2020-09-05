package com.dotop.deyang.filter;
import com.dotop.deyang.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AccessLogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest) req);
        long start = System.currentTimeMillis();
        try {
            chain.doFilter(req, res);
        }
        finally {
            long cost = System.currentTimeMillis() - start;
            String realIP = request.getHeader("X-Real-IP");
            if(StrUtil.isBlank(realIP)) {
                realIP = request.getRemoteAddr();
            }
            log.info(String.format("%s %s %s %s ----> %d ms", request.getMethod(), request.getRequestURI(), request.getProtocol(), realIP, cost));
        }
    }

}