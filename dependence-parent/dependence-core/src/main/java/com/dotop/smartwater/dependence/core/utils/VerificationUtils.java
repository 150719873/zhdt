package com.dotop.smartwater.dependence.core.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 * String reg = "^[a-z0-9]?$";
 */
public class VerificationUtils {

	private VerificationUtils() {
		super();
	}

	public static final int DEFAULT_MIN = 0;
	public static final int DEFAULT_MAX = Integer.MAX_VALUE;
	public static final long DEFAULT_MIN_LONG = 0L;
	public static final long DEFAULT_MAX_LONG = Long.MAX_VALUE;
	public static final double DEFAULT_MIN_DOUBLE = 0D;
	public static final double DEFAULT_MAX_DOUBLE = Double.MAX_VALUE;

	public static final BigDecimal DEFAULT_MIN_D = new BigDecimal(Long.MIN_VALUE);
	public static final BigDecimal DEFAULT_MAX_D = new BigDecimal(Long.MAX_VALUE);
	public static final int DEFAULT_MAX_LENGTH = 36;

	public static final String YYYYMMDD = "((\\d{2}(([02468][048])|([13579][26]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|(1[0-9])|(2[0-8]))))))";
	public static final String YYYYMMDDHHMMSS = "((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))";
	public static final String BLANK = "\\s*";
	public static final String ST = "(19|20)\\d{2}\\-[01]\\d\\-[0123]\\d\\s\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}";
	public static final String STRING_TIME = YYYYMMDD + "|" + YYYYMMDDHHMMSS;

	// 手机校验正则
	public static final String REG_PHONE = "^[1][3,4,5,7,8][0-9]{9}$";

	// sql检验正则
	public static final String REG_SQL = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
	public static final String REG_SQL_SELECT = "^select|^SELECT";

	// String
	public static final boolean string(String name, String obj) {
		return string(name, obj, false, DEFAULT_MAX_LENGTH);
	}

	public static final boolean string(String name, String obj, String regex) {
		return string(name, obj, false, DEFAULT_MAX_LENGTH, regex);
	}

	public static final boolean string(String name, String obj, boolean isNull) {
		return string(name, obj, isNull, DEFAULT_MAX_LENGTH, null);
	}

	public static final boolean string(String name, String obj, boolean isNull, int maxLength) {
		return string(name, obj, isNull, maxLength, null);
	}

	public static final boolean string(String name, String obj, boolean isNull, int maxLength, String regex) {
		if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(regex)) {

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(obj);
			boolean rs = matcher.find();
			if (!rs) {
				throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, new String[] { name });
			}
		}
		if (!isNull && StringUtils.isEmpty(obj)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
		if (obj != null && obj.length() > maxLength) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, new String[] { name });
		}
		return true;
	}

	public static final boolean stringTime(String name, String obj, String regex) {
		if (StringUtils.isEmpty(obj)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(obj);
		boolean rs = matcher.find();
		if (!rs) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, new String[] { name });
		}
		return true;
	}

	// 字符串校验
	public static final boolean regex(String str, String regex) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	// Integer
	public static final boolean integer(String name, Integer obj) {
		return integer(name, obj, false, DEFAULT_MIN, DEFAULT_MAX);
	}

	public static final boolean integer(String name, Integer obj, boolean isNull) {
		return integer(name, obj, isNull, DEFAULT_MIN, DEFAULT_MAX);
	}

	public static final boolean integer(String name, Integer obj, boolean isNull, int min, int max) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
		if (obj != null && (obj < min || obj > max)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, new String[] { name });
		}
		return true;
	}

	// long
	public static final boolean longs(String name, Long obj) {
		return longs(name, obj, false, DEFAULT_MIN_LONG, DEFAULT_MAX_LONG);
	}

	public static final boolean longs(String name, Long obj, boolean isNull) {
		return longs(name, obj, isNull, DEFAULT_MIN_LONG, DEFAULT_MAX_LONG);
	}

	public static final boolean longs(String name, Long obj, boolean isNull, long min, long max) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
		if (obj != null && (obj < min || obj > max)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, new String[] { name });
		}
		return true;
	}

	// double
	public static final boolean doubles(String name, Double obj) {
		return doubles(name, obj, false, DEFAULT_MIN_DOUBLE, DEFAULT_MAX_DOUBLE);
	}

	public static final boolean doubles(String name, Double obj, boolean isNull) {
		return doubles(name, obj, isNull, DEFAULT_MIN_DOUBLE, DEFAULT_MAX_DOUBLE);
	}

	public static final boolean doubles(String name, Double obj, boolean isNull, Double min, Double max) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
		if (obj != null && (obj < min || obj > max)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, new String[] { name });
		}
		return true;
	}

	// BigDecimal
	public static final boolean bigDecimal(String name, BigDecimal obj) {
		return bigDecimal(name, obj, false, DEFAULT_MIN_D, DEFAULT_MAX_D);
	}

	public static final boolean bigDecimal(String name, BigDecimal obj, boolean isNull) {
		return bigDecimal(name, obj, isNull, DEFAULT_MIN_D, DEFAULT_MAX_D);
	}

	public static final boolean bigDecimal(String name, BigDecimal obj, boolean isNull, BigDecimal min,
			BigDecimal max) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
		if (obj != null && (obj.compareTo(min) < 0 || obj.compareTo(max) > 0)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, new String[] { name });
		}
		return true;
	}

	// string ary
	public static final void strAry(String name, String[] arys) {
		if (arys == null || arys.length == 0) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
	}

	// obj ary
	public static final void objAry(String name, Object[] arys) {
		if (arys == null || arys.length == 0) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
	}

	// string list
	public static final void strList(String name, List<String> arys) {
		if (arys == null || arys.isEmpty()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
	}

	// obj list
	public static final void objList(String name, List<?> arys) {
		if (arys == null || arys.isEmpty()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
	}

	// obj
	public static final void obj(String name, Object obj) {
		if (obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
	}

	// date
	public static final void date(String name, Date obj) {
		date(name, obj, false);
	}

	public static final void date(String name, Date obj, boolean isNull) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, new String[] { name });
		}
	}

	// String
	public static final boolean toString(String msg, String obj) {
		return toString(msg, obj, false, DEFAULT_MAX_LENGTH);
	}

	public static final boolean toString(String msg, String obj, String regex) {
		return toString(msg, obj, false, DEFAULT_MAX_LENGTH, regex);
	}

	public static final boolean toString(String msg, String obj, boolean isNull) {
		return toString(msg, obj, isNull, DEFAULT_MAX_LENGTH, null);
	}

	public static final boolean toString(String msg, String obj, boolean isNull, int maxLength) {
		return toString(msg, obj, isNull, maxLength, null);
	}

	public static final boolean toString(String msg, String obj, boolean isNull, int maxLength, String regex) {
		if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(regex)) {

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(obj);
			boolean rs = matcher.find();
			if (!rs) {
				throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, msg);
			}
		}
		if (!isNull && StringUtils.isEmpty(obj)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
		if (obj != null && obj.length() > maxLength) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, msg);
		}
		return true;
	}

	public static final boolean toStringTime(String msg, String obj, String regex) {
		if (StringUtils.isEmpty(obj)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(obj);
		boolean rs = matcher.find();
		if (!rs) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_FORMAT_ERROR, msg);
		}
		return true;
	}

	// Integer
	public static final boolean toInteger(String msg, Integer obj) {
		return toInteger(msg, obj, false, DEFAULT_MIN, DEFAULT_MAX);
	}

	public static final boolean toInteger(String msg, Integer obj, boolean isNull) {
		return toInteger(msg, obj, isNull, DEFAULT_MIN, DEFAULT_MAX);
	}

	public static final boolean toInteger(String msg, Integer obj, boolean isNull, int min, int max) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
		if (obj != null && (obj < min || obj > max)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, msg);
		}
		return true;
	}

	// long
	public static final boolean toLong(String msg, Long obj) {
		return toLong(msg, obj, false, DEFAULT_MIN_LONG, DEFAULT_MAX_LONG);
	}

	public static final boolean toLong(String msg, Long obj, boolean isNull) {
		return toLong(msg, obj, isNull, DEFAULT_MIN_LONG, DEFAULT_MAX_LONG);
	}

	public static final boolean toLong(String msg, Long obj, boolean isNull, long min, long max) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
		if (obj != null && (obj < min || obj > max)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, msg);
		}
		return true;
	}

	// double
	public static final boolean toDouble(String msg, Double obj) {
		return toDouble(msg, obj, false, DEFAULT_MIN_DOUBLE, DEFAULT_MAX_DOUBLE);
	}

	public static final boolean toDouble(String msg, Double obj, boolean isNull) {
		return toDouble(msg, obj, isNull, DEFAULT_MIN_DOUBLE, DEFAULT_MAX_DOUBLE);
	}

	public static final boolean toDouble(String msg, Double obj, boolean isNull, Double min, Double max) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
		if (obj != null && (obj < min || obj > max)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, msg);
		}
		return true;
	}

	// BigDecimal
	public static final boolean toBigDecimal(String msg, BigDecimal obj) {
		return toBigDecimal(msg, obj, false, DEFAULT_MIN_D, DEFAULT_MAX_D);
	}

	public static final boolean toBigDecimal(String msg, BigDecimal obj, boolean isNull) {
		return toBigDecimal(msg, obj, isNull, DEFAULT_MIN_D, DEFAULT_MAX_D);
	}

	public static final boolean toBigDecimal(String msg, BigDecimal obj, boolean isNull, BigDecimal min,
			BigDecimal max) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
		if (obj != null && (obj.compareTo(min) < 0 || obj.compareTo(max) > 0)) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_TOO_MAX_LENGTH, msg);
		}
		return true;
	}

	// string ary
	public static final void toStrAry(String msg, String[] arys) {
		if (arys == null || arys.length == 0) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
	}

	// obj ary
	public static final void toObjAry(String msg, Object[] arys) {
		if (arys == null || arys.length == 0) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
	}

	// string list
	public static final void toStrList(String msg, List<String> arys) {
		if (arys == null || arys.isEmpty()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
	}

	// obj list
	public static final void toObjList(String msg, List<?> arys) {
		if (arys == null || arys.isEmpty()) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
	}

	// obj
	public static final void toObj(String msg, Object obj) {
		if (obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
	}

	// date
	public static final void toDate(String msg, Date obj) {
		toDate(msg, obj, false);
	}

	public static final void toDate(String msg, Date obj, boolean isNull) {
		if (!isNull && obj == null) {
			throw new FrameworkRuntimeException(BaseExceptionConstants.PARAM_EMPTY, msg);
		}
	}

}
