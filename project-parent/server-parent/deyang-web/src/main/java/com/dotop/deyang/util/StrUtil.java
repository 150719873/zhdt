package com.dotop.deyang.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

	public static boolean isBlank(String value) {
		return value == null || value.trim().length() == 0;
	}
	
	public static boolean isBlank(Integer value) {
		return value == null;
	}
	
	public static boolean isBlank(Long value) {
		return value == null;
	}
	
	public static boolean isBlank(Double value) {
		return value == null;
	}
	
	public static boolean isNotBlank(String value) {
		return !isBlank(value);
	}
	
	public static boolean isNotBlank(Double value) {
		return !isBlank(value);
	}
	
	public static boolean isNotBlank(Integer value) {
		return !isBlank(value);
	}
	
	public static boolean isNotBlank(Long value) {
		return !isBlank(value);
	}
	
	public static boolean isDigit(String str) {
		  return str.matches("[0-9]+");
	}
	
	public static String newGuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}


	private static Pattern pMobile = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
	private static Pattern pWeChatParams = Pattern.compile("\\{\\{[^\\}]*\\}\\}");
	
	public static String join(String delimiter, String[] values) {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < values.length; i++) {
			if(i > 0) {
				sb.append(delimiter);
			}
			sb.append(values[i]);
		}
		return sb.toString();
	}
	
	/**
	   * 手机号验证
	   * @author ：KJR
	   * 2016年12月5日下午4:34:46
	   * @param  str
	   * @return 验证通过返回true
	   */
	  public static boolean isMobile(String str) {
		  if(isBlank(str)){
			  return false;
		  }

		  return pMobile.matcher(str).matches();
	  }
	
	//重载一个join方法 ,操作list
	//内部不锁定,效率最高,但是当写多线程时要考虑并发操作的问题。
	public static String join(String delimiter, List<String> values) {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < values.size(); i++) {
			if(i > 0) {
				sb.append(delimiter);
			}
			sb.append(values.get(i));
		}
		return sb.toString();
	}
	
	//生成随机数 KJR
	public static String GetRandomNumberString(int weishu) {
		  String str = "";
		  str += (int)(Math.random()*9+1);
		  for(int i = 1; i < weishu; i++){
		  str += (int)(Math.random()*10);
		  }
		  return str;
	}
	
	
    //生成文件夹 
	public static void makeDir(String path){
		File file =new File(path);    
		//如果文件夹不存在则创建    
		if  (!file .exists()  && !file .isDirectory()){        
		    file .mkdir();    
		} 
		file = null;
	}
	
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public static boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}
	
	//判断session是否存在
	public static void IsNewSession(HttpServletRequest request){
		 HttpSession session = request.getSession();
	     //将数据存储到session中
		 //session.setAttribute("data", "KJR");
		 //获取session的Id
	     String sessionId = session.getId();
	    //判断session是不是新创建的
	    if (session.isNew()) {
		  System.out.println("session创建成功，session的id是："+sessionId);
	    }else {
	    	System.out.println("服务器已经存在该session了，session的id是："+sessionId);
	  }
	}
	
	//判断数组时候是递增
	public static boolean JadgeIncreseArrayWithRecursion(Double[] array){
	        return JadgeIncreseArrayWithRecursion(array, 0);
	}

    private static boolean JadgeIncreseArrayWithRecursion(Double[] array, int begin){
        if (begin == array.length - 1){
            return true;
        }else{
            return array[begin] < array[begin + 1] &&  JadgeIncreseArrayWithRecursion(array, begin + 1);
        }
    }
    
    public static boolean compare_date(String date1,String date2) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Date dt1=null;  
        Date dt2=null;  
        
        dt1 = sdf.parse(date1);  
        dt2=sdf.parse(date2);  
         
        if(dt1.getTime()<dt2.getTime()){  
            return true;  
        }
        return false;  
    }
    
    public static String getPageContidion(Long page,Long pageCount){
    	Long first = (page-1)*pageCount;
    	return String.format(" limit %s,%s", String.valueOf(first),String.valueOf(pageCount));
    } 
    
    /**
     * 获得两个日期相差的月份按照自然月
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonthSpace(Date date1, Date date2){
		int result = 0;
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(date1);
		c2.setTime(date2);
		
		result = (c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR))*12+c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		return Math.abs(result);
	}

	public static List<String> getWechatParams(String contant){
		if(isBlank(contant)){
			return null;
		}

		List<String> list= new ArrayList<>();
		Matcher m = pWeChatParams.matcher(contant);
		while(m.find()){
			list.add(m.group().substring(2, m.group().length()-2));
		}
		return list;
	}

	/**
	 * 替换微信模板中参数
	 * @param template
	 * @param map
	 * @return
	 */
	public static String replaceWechatKey(String template, Map<String, String> map) {
		try {
			final String reg = "\\{\\{[^\\}]*\\}\\}";
			Pattern regex = Pattern.compile(reg);
			Matcher matcher = regex.matcher(template);
			while (matcher.find()) {
				String key = matcher.group();
				if(key.contains("-")){
					String mapkey = key.replaceAll("\\{","").replaceAll("\\}","").split("-")[1];
					if (map.containsKey(mapkey)) {
						template = template.replaceAll(key.replaceAll("\\{","\\\\{").replaceAll("\\}","\\\\}"), map.get(mapkey));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return template;
	}

	/**
	 * 替换模板中参数
	 * @param template
	 * @param map
	 * @return
	 */
	public static String replaceKey(String template, Map<String, String> map) {
		try {
			final String reg = "\\$\\{([^}]*)\\}";
			Pattern regex = Pattern.compile(reg);
			Matcher matcher = regex.matcher(template);
			while (matcher.find()) {
				String key = matcher.group(1);
				if (map.containsKey(key)) {
					template = template.replaceAll("\\$\\{" + key + "\\}", map.get(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return template;
	}
}
