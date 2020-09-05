package com.dotop.smartwater.project.module.core.water.utils;

import com.dotop.smartwater.project.module.core.water.model.BodyMap;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryChildBo;
import com.dotop.smartwater.project.module.core.water.model.BodyMap;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**

 * @date 2019年3月20日
 * @description xxx为属性 输入 {${xxx}$} 选择 {#{xxx}#} 日期 {%{xxx}%} 文本 {^{xxx}^} sql参数 {&{xxx}&}
 */
public class WorkCenterTmplUtils {

  public static String inputs(String body, List<String> inputs) {
    if (inputs == null || inputs.isEmpty()) {
      return body;
    }
    for (String input : inputs) {
      String regex = "\\{\\$\\{" + input + "\\}\\$\\}";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(body);
      if (matcher.find()) {
        String sb = "<input style='width:100%;' id='" + input + "' name='" + input + "' />";
        body = matcher.replaceAll(sb);
      }
    }
    return body;
  }

  public static String selects(String body, Map<String, List<DictionaryChildBo>> selects) {
    if (selects == null || selects.isEmpty()) {
      return body;
    }
    for (String select : selects.keySet()) {
      String regex = "\\{\\#\\{" + select + "\\}\\#\\}";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(body);
      if (matcher.find()) {
        StringBuilder sb = new StringBuilder();
        sb.append("<select style='width:100%;' id='");
        sb.append(select);
        sb.append("' name='");
        sb.append(select);
        sb.append("'>");
        for (DictionaryChildBo dc : selects.get(select)) {
          sb.append("<option value='");
          sb.append(dc.getChildId());
          sb.append("'>");
          sb.append(dc.getChildName());
          sb.append("</option>");
        }
        sb.append("</select>");
        body = matcher.replaceAll(sb.toString());
      }
    }
    return body;
  }

  public static String dates(String body, List<String> dates) {
    if (dates == null || dates.isEmpty()) {
      return body;
    }
    for (String date : dates) {
      String regex = "\\{\\%\\{" + date + "\\}\\%\\}";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(body);
      if (matcher.find()) {
        String sb = "<input style='width:100%;' id='" + date + "' name='" + date + "' />";
        body = matcher.replaceAll(sb);
      }
    }
    return body;
  }

  public static String inputs(String body, List<String> inputs, Map<String, String> fillParams) {
    if (inputs == null || inputs.isEmpty()) {
      return body;
    }
    for (String input : inputs) {
      String regex = "\\{\\$\\{" + input + "\\}\\$\\}";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(body);
      if (matcher.find()) {
        StringBuilder sb = new StringBuilder();
        if (fillParams == null || fillParams.size() == 0) {
          sb.append("<span>" + "</span>");
        } else {
          String val = fillParams.get(input);
          if (StringUtils.isBlank(val)) {
            sb.append("<span>" + "</span>");
          } else {
            sb.append("<span>").append(val).append("</span>");
          }
        }
        body = matcher.replaceAll(sb.toString());
      }
    }
    return body;
  }

  public static String selects(
      String body, Map<String, List<DictionaryChildBo>> selects, Map<String, String> fillParams) {
    if (selects == null || selects.isEmpty()) {
      return body;
    }
    for (String select : selects.keySet()) {
      String regex = "\\{\\#\\{" + select + "\\}\\#\\}";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(body);
      if (matcher.find()) {
        StringBuilder sb = new StringBuilder();
        String selectChildId = fillParams.get(select);
        for (DictionaryChildBo dc : selects.get(select)) {
          if (dc.getChildId().equals(selectChildId)) {
            sb.append("<span>").append(dc.getChildName()).append("</span>");
            break;
          }
        }
        if (sb.length() == 0) {
          sb.append("<span>" + "</span>");
        }
        body = matcher.replaceAll(sb.toString());
      }
    }
    return body;
  }

  public static String dates(String body, List<String> dates, Map<String, String> fillParams) {
    if (dates == null || dates.isEmpty()) {
      return body;
    }
    for (String date : dates) {
      String regex = "\\{\\%\\{" + date + "\\}\\%\\}";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(body);
      if (matcher.find()) {
        StringBuilder sb = new StringBuilder();
        if (fillParams == null || fillParams.size() == 0) {
          sb.append("<span>" + "</span>");
        } else {
          String val = fillParams.get(date);
          if (StringUtils.isBlank(val)) {
            sb.append("<span>" + "</span>");
          } else {
            sb.append("<span>").append(val).append("</span>");
          }
        }
        body = matcher.replaceAll(sb.toString());
      }
    }
    return body;
  }

  public static String texts(String body, List<String> texts, Map<String, String> showParams) {
    if (texts == null || texts.isEmpty()) {
      return body;
    }
    for (String text : texts) {
      String regex = "\\{\\^\\{" + text + "\\}\\^\\}";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(body);
      if (matcher.find()) {
        StringBuilder sb = new StringBuilder();
        if (showParams == null || showParams.size() == 0) {
          sb.append("<span>" + "</span>");
        } else {
          String val = showParams.get(text);
          if (StringUtils.isBlank(val)) {
            sb.append("<span>" + "</span>");
          } else {
            sb.append("<span>").append(val).append("</span>");
          }
        }
        body = matcher.replaceAll(sb.toString());
      }
    }
    return body;
  }

  public static List<BodyMap> bodyMap(
      List<BodyMap> bodyMap,
      List<String> inputs,
      Map<String, List<DictionaryChildBo>> selects,
      List<String> dates,
      List<String> texts,
      Map<String, String> fillParams,
      Map<String, String> showParams) {
    List<BodyMap> list = new ArrayList<>();
    if (bodyMap == null || bodyMap.isEmpty()) {
      return list;
    }
    for (BodyMap map : bodyMap) {
      String key = map.getKey();
      String val = map.getVal();
      if (StringUtils.isBlank(key) || StringUtils.isBlank(val)) {
        continue;
      }
      boolean flag = false;
      if (!flag && inputs != null && !inputs.isEmpty()) {
        for (String input : inputs) {
          String regex = "\\{\\$\\{" + input + "\\}\\$\\}";
          Pattern pattern = Pattern.compile(regex);
          Matcher matcher = pattern.matcher(val);
          if (matcher.find()) {
            list.add(new BodyMap(key, fillParams.get(input)));
            flag = true;
            break;
          }
        }
      }
      if (!flag && selects != null && !selects.isEmpty()) {
        for (String select : selects.keySet()) {
          String regex = "\\{\\#\\{" + select + "\\}\\#\\}";
          Pattern pattern = Pattern.compile(regex);
          Matcher matcher = pattern.matcher(val);
          if (matcher.find()) {
            String selectChildId = fillParams.get(select);
            for (DictionaryChildBo dc : selects.get(select)) {
              if (dc.getChildId().equals(selectChildId)) {
                list.add(new BodyMap(key, dc.getChildName()));
                flag = true;
                break;
              }
            }
            break;
          }
        }
      }
      if (!flag && dates != null && !dates.isEmpty()) {
        for (String date : dates) {
          String regex = "\\{\\%\\{" + date + "\\}\\%\\}";
          Pattern pattern = Pattern.compile(regex);
          Matcher matcher = pattern.matcher(val);
          if (matcher.find()) {
            list.add(new BodyMap(key, fillParams.get(date)));
            flag = true;
            break;
          }
        }
      }
      if (!flag && texts != null && !texts.isEmpty()) {
        String valClone = new String(val);
        for (String text : texts) {
          String regex = "\\{\\^\\{" + text + "\\}\\^\\}";
          Pattern pattern = Pattern.compile(regex);
          Matcher matcher = pattern.matcher(valClone);
          if (matcher.find()) {
            valClone = matcher.replaceAll(showParams.get(text));
            flag = true;
          }
        }
        if (flag) {
          list.add(new BodyMap(key, valClone));
        }
      }
    }
    return list;
  }
}
