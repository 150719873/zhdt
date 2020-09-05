package com.dotop.smartwater.dependence.core.log;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;

/**

 * @date 2019年4月19日
 * @description logger.info(" { \ " % s \ " : \ " % s \ " } ", " id ", id); 数组越界不处理
 */
public final class LogMsg {

    public static final int STEP = 2;

    private LogMsg() {
        super();
    }

    public static String to(String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        if (args != null && args.length > 0) {
            int len = (args.length % 2) == 0 ? args.length : (args.length - 1);
            for (int i = 0; i < len; i = i + STEP) {
                sb.append('\"');
                sb.append(args[i]);
                sb.append('\"');
                sb.append(':');
                sb.append('\"');
                sb.append(args[i + 1]);
                sb.append('\"');
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append('}');
        return sb.toString();
    }

    public static String to(Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        if (args != null && args.length > 0) {
            int len = (args.length % 2) == 0 ? args.length : (args.length - 1);
            for (int i = 0; i < len; i = i + STEP) {
                sb.append('\"');
                sb.append(args[i]);
                sb.append('\"');
                sb.append(':');
                if (args[i] instanceof String) {
                    sb.append('\"');
                    sb.append(args[i + 1]);
                    sb.append('\"');
                } else {
                    sb.append('\"');
                    sb.append(JSONUtils.toJSONString(args[i + 1]));
                    sb.append('\"');
                }
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append('}');
        return sb.toString();
    }

    public static String to(FrameworkRuntimeException e, String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"ex\"");
        sb.append(':');
        sb.append('\"');
        sb.append(e.toString());
        sb.append('\"');
        if (args != null && args.length > 0) {
            int len = (args.length % 2) == 0 ? args.length : (args.length - 1);
            for (int i = 0; i < len; i = i + STEP) {
                sb.append(',');
                sb.append('\"');
                sb.append(args[i]);
                sb.append('\"');
                sb.append(':');
                sb.append('\"');
                sb.append(args[i + 1]);
                sb.append('\"');
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public static String to(Throwable e, String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"ex\"");
        sb.append(':');
        sb.append('\"');
        sb.append(e.toString());
        sb.append('\"');
        if (args != null && args.length > 0) {
            int len = (args.length % 2) == 0 ? args.length : (args.length - 1);
            for (int i = 0; i < len; i = i + STEP) {
                sb.append(',');
                sb.append('\"');
                sb.append(args[i]);
                sb.append('\"');
                sb.append(':');
                sb.append('\"');
                sb.append(args[i + 1]);
                sb.append('\"');
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
