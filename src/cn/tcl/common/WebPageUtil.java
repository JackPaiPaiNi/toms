package cn.tcl.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.tcl.platform.role.service.IRoleService;
import cn.tcl.platform.role.vo.Role;
import cn.tcl.platform.user.service.IUserLoginService;
import cn.tcl.platform.user.vo.UserLogin;

import com.novell.ldap.LDAPConnection;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * 页面工具类
 * 
 * @author luliezhi
 * @since 2010-07-26
 * 
 */
public class WebPageUtil {
	public static Integer p2Int(String p){
		return p2Int(p,null);
	}
	public static Integer p2Int(String p,Integer defval){
		if(p==null ||"".equals(p)){
			return defval;
		}else{
			try {
				return Integer.parseInt(p);
			} catch (Exception e) {
				return defval;
			}
		}
	}
	public static Double p2Double(String p){
		return p2Double(p,null);
	}
	public static Double p2Double(String p,Double defval){
		if(p==null ||"".equals(p)){
			return defval;
		}else{
			try {
				return Double.parseDouble(p);
			} catch (Exception e) {
				return defval;
			}
		}
	}
	public static Date p2Date(String p){
		return p2Date(p,null,"yyyy-MM-dd");
	}
	public static Date p2Date(String p,Date defval){
		return p2Date(p,defval,"yyyy-MM-dd");
	}
	public static Date p2Date(String p,Date defval,String format){
		if(p==null ||"".equals(p)){
			return defval;
		}else{
			try {
				return strToDate(p,format);
			} catch (Exception e) {
				return defval;
			}
		}
	}
	private static Logger log = Logger.getLogger(WebPageUtil.class);
	
	/**
	 * 项目配置文件
	 */
	public static String configFileName = "config.properties";

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @author sutao
	 * @return
	 */
	public static Date strToDate(String strDate) 
	{
		if (strDate.trim().length() != 10)
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setLenient(false);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate.trim(), pos);
		return strtodate;
	}
	
	/**
	 * 将短时间格式字符串转换为时间 MM/dd/YYYY
	 * @param strDate
	 * @author sutao
	 * @return
	 */
	public static Date strToDateMMDDYYYY(String strDate) 
	{
		if (strDate.trim().length() != 10)
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");
		formatter.setLenient(false);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate.trim(), pos);
		return strtodate;
	}

	/**
	 * 将短时间格式字符串转换为时间 dd/MM/yyyy
	 * @param strDate
	 * @author sutao
	 * @return
	 */
	public static Date strToDateDDMMYYYY(String strDate) 
	{
		if (strDate.trim().length() != 10)
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		formatter.setLenient(false);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate.trim(), pos);
		return strtodate;
	}
	
	/**
	 * 根据自定义格式，转换日期字符串 成 日期对象
	 * @param strDate 日期字符串
	 * @param formatStr 日期格式化字符串
	 * @return
	 */
	public static Date strToDate(String strDate,String formatStr)
	{
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
			Date date = formatter.parse(strDate);
			return date;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * @param dateDate
	 * @return
	 * @author sutao
	 * @since 2010-8-31下午02:57:45
	 * @see
	 */
	public static String dateToStr(java.util.Date date) 
	{
		try 
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(date);
			return dateString;
		} 
		catch (Exception ex) 
		{
			return "";
		}
	}

	/**
	 * 将短时间格式时间转换为字符串MM/dd/yyyy
	 * @param dateDate
	 * @return
	 * @author sutao
	 * @since 2010-8-31下午02:57:45
	 * @see
	 */
	public static String dateToStrMMDDYYYY(java.util.Date date) 
	{
		try 
		{
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dateString = formatter.format(date);
			return dateString;
		} 
		catch (Exception ex) 
		{
			return "";
		}
	}

	/**
	 * 将短时间格式时间转换为字符串dd/MM/yyyy
	 * @param dateDate
	 * @return
	 * @author sutao
	 * @since 2010-8-31下午02:57:45
	 * @see
	 */
	public static String dateToStrDDMMYYYY(java.util.Date date) 
	{
		try 
		{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String dateString = formatter.format(date);
			return dateString;
		} 
		catch (Exception ex) 
		{
			return "";
		}
	}

	/**
	 * 将短时间格式时间转换为字符串
	 * @param dateDate
	 *            转换的日期
	 * @param formatStr
	 *            转换字符串格式
	 * @return
	 * @author sutao
	 * @since 2010-8-31下午02:57:45
	 * @see
	 */
	public static String dateToStr(java.util.Date date, String formatStr) 
	{
		try 
		{
			SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
			String dateString = formatter.format(date);
			return dateString;
		} 
		catch (Exception ex) 
		{
			return "";
		}
	}

	/**
	 * 比较两个日期的先后
	 * @param startDate yyyy-MM-dd 格式的日期字符串
	 * @param endDate yyyy-MM-dd 格式的日期字符串
	 * @return true:startDate小于endDate<br>
	 *         false:startDate大于endDate
	 * @author sutao
	 * @since 2010-7-29下午03:53:36
	 */
	public static boolean compareDate(String startDate, String endDate)
	{
		Date sDate = strToDate(startDate);
		Date eDate = strToDate(endDate);
		if (eDate.compareTo(sDate) > 0)
			return true;
		else
			return false;
	}

	/**
	 * 获取现在时间
	 * @return 返回格式 yyyy-MM-dd HH:mm:ss
	 * @author sutao
	 * @since 2010-9-15上午11:55:50
	 * @see
	 */
	public static Date getLongDate() 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(System.currentTimeMillis());
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(dateString, pos);
		return strtodate;
	}
	
	/**
	 * 获取日期字符串所处的日期是周几
	 * @return 返回周几的数字
	 */
	public static int getWeekByDate(String dateStr)
	{
		Date date = strToDate(dateStr, "yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
		{
			w = 7;
		}
        return w;  
	}

	/**
	 * 此函数用于判断当前变量是否为数字
	 * @param str
	 * @return
	 * @author baosg
	 * @since 2011-09-05 11:30:00
	 * @see
	 */
	public static boolean isValidNum(String str) 
	{
		String sflag = "no";
		for (int i = 0; i < str.length(); i++) 
		{
			if (!String.valueOf(str.charAt(i)).matches("\\d *")) 
			{
				sflag = "yes";
			}
		}
		if (sflag.toString().trim().equals("yes")) 
		{
			return false;
		} 
		else 
		{
			return true;
		}

	}

	/**
	 * 根据properties文件路径获取配置对象
	 * @param fileName
	 * @return
	 */
	public static Properties getPropertiesByFileName(String fileName) {
		Properties props = new java.util.Properties();
		try 
		{
			InputStream stream = new WebPageUtil().getClass().getClassLoader().getResourceAsStream(fileName);
			props.load(stream);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return props;
	}
	
	/**
	 * 获取配置文件的内容 config.properties
	 * @param key
	 * @return
	 */
	public static String getConfigValueByKey(String key)
	{
		Properties props = getPropertiesByFileName(configFileName);
		return props.getProperty(key);
	}

	/**
	 * 获取LDAP连接
	 * @return
	 */
	public static LDAPConnection getLDAPConnection() 
	{
		LDAPConnection lc = new LDAPConnection();
		try 
		{
			Properties props = getPropertiesByFileName(configFileName);
			int ldapPort = LDAPConnection.DEFAULT_PORT; // 端口号,如389
			int ldapVersion = LDAPConnection.LDAP_V3;

			String ldapHost = props.getProperty("ldapHost");
			String loginDN = props.getProperty("loginDN");
			String password = props.getProperty("password");

			lc.connect(ldapHost, ldapPort);
			lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		}
		return lc;
	}

	/**
	 * 回写数据
	 * @param str
	 */
	public static void writeBack(String str) 
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try 
		{
			out = response.getWriter();
			out.print(str);
			out.flush();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage(),e);
		} 
		finally 
		{
			if (null != out) 
			{
				out.close();
			}
		}
	}

	/**
	 * 获取当前登录的用户ID
	 * 
	 * @return
	 */
	public static String getLoginedUserId() 
	{
		ActionContext context = ActionContext.getContext();
//		String userId = ServletActionContext.getContext().getSession().get("loginUserId").toString();
		
		String userId = context.getSession().get("loginUserId").toString();
		return userId;
	}

	/**
	 * 判断A字符串是否是B字符串中的元素 str1 ：A字符串 str2 ：B字符串
	 * @return
	 * 
	 */
	public static boolean getFlag(String str1, String str2) 
	{
		String aStr2[] = new String[20];
		aStr2 = str2.split(";");
		boolean sflag = false;
		if (str1.trim().equals("") || str1.trim().isEmpty()) 
		{
			return sflag;
		} 
		else 
		{
			for (int i = 0; i < aStr2.length; i++) 
			{
				if (aStr2[i].toString().trim().equals(str1.trim())) 
				{
					sflag = true;
					break;
				}
			}
		}
		return sflag;
	}

	/**
	 * 根据value查询出MAP的key
	 * 
	 * @author yan 2012-2-7
	 * @param paramMap
	 * @return
	 */
	public static Object getKeyByValue(String value, Map paramMap) 
	{
		Object key = null;
		Set<Entry> stEntry = paramMap.entrySet();
		for (Entry entry : stEntry) 
		{
			String v = (String) entry.getValue();
			if (value.equals(v)) 
			{
				key = entry.getKey();
			}
		}
		return key;
	}

	/**
	 * 复制文件夹
	 * @param sourceDir
	 * @param targetDir
	 * @throws IOException
	 */
	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException 
	{
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		if (file != null && file.length > 0) 
		{
			for (int i = 0; i < file.length; i++) 
			{
				if (file[i].isFile()) 
				{
					// 源文件
					File sourceFile = file[i];
					// 目标文件
					File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
					copyFile(sourceFile, targetFile);
				}
				if (file[i].isDirectory()) 
				{
					// 准备复制的源文件夹
					String dir1 = sourceDir + "/" + file[i].getName();
					// 准备复制的目标文件夹
					String dir2 = targetDir + "/" + file[i].getName();
					copyDirectiory(dir1, dir2);
				}
			}
		}
	}

	/**
	 * 拷贝文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException 
	{
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) 
		{
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();
		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
	
	/**
	 * 获取当前应用的基础路径
	 * @return
	 */
	public static String getBasePath()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		return basePath;
	}
	
	/**
	 * 是否是中文
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是英文
	 * @param c
	 * @return
	 */
	public static boolean isEnglish(String charaString) {
		return charaString.matches("^[a-zA-Z]*");
	}
	
	/**
	 * 是否是中文
	 * @param c
	 * @return
	 */
	public static boolean isChinese(String str) {
		String regEx = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find())
			return true;
		else
			return false;
	}
	
	/**
	 * 转换java字段为数据库字段
	 * @return
	 */
	public static String tansBeanFiledToDbField(String beanField)
	{
		String dbField = "";
		char[] chars = beanField.toCharArray();
		for(char _char : chars)
		{
			if(_char>='A' && _char<='Z')
			{
				dbField += "_"+(char)(_char+32);
			}
			else
			{
				dbField += _char;
			}
		}
		return dbField;
	}
	
	/**
	 * 根据日期获取星期几
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	/**
	 * 把日期字符串加减N天再返回日期字符串
	 * @param dateStr
	 * @param format
	 * @param days
	 * @return
	 */
	public static String loadDateStrAddDays(String dateStr,String format,int days)
	{
		Date date = strToDate(dateStr, format);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DATE,days);
		Date rtDate = ca.getTime();
		String rt = dateToStr(rtDate, format);
		return rt;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isStringNullAvaliable(String str)
	{
		return str!=null && !"".equals(str);
	}
	
	/**
	 * 设置语言环境公用方法
	 */
	public static String setLanguage(HttpServletRequest request)
	{
		Locale locale = (Locale)request.getSession().getAttribute(Contents.LANGUAGE_SESSION);
		if(null==locale)
		{
			locale = request.getLocale();
		}
		String language = locale.getLanguage();
		if(language.indexOf("_")>-1)
		{
			language = language.substring(0, language.indexOf("_"));
		}
		Locale _locale = new Locale(language);
		request.getSession().setAttribute(Contents.LANGUAGE_SESSION, _locale);
		return language;
	}
	
	/**
	 * 用作登录后，设置session和cookie信息
	 */
	public static void setSessionAndCookie(UserLogin user,HttpServletRequest request,HttpServletResponse response)
	{
		String loginUserId = user.getUserLoginId();
		request.getSession().setAttribute("loginUserId", loginUserId);
		request.getSession().setAttribute("loginUser", user);
		String language = user.getUserLocale();
		Locale locale = new Locale(language);
		request.getSession().setAttribute(Contents.LANGUAGE_SESSION, locale);
		request.getSession().setAttribute("canEdit", canEdit());
		
		Cookie cookie=new Cookie("loginUserId",loginUserId);
		cookie.setPath("/");
		cookie.setMaxAge(60*60*24);
		response.addCookie(cookie);
	}
	
	
	/**
	 * 获取当前登录的用户对象
	 * @return
	 */
	public static UserLogin getLoginedUser()
	{
		ActionContext context = ActionContext.getContext();
		UserLogin user = (UserLogin) context.getSession().get("loginUser");
		return user;
	}
	
	
	/**
	 * 判断是否超级管理员
	 */
	public static boolean isHAdmin()
	{
		boolean result = false;
		UserLogin user = getLoginedUser();
		String roleId = user.getRoleId();
		if(null!=roleId && !"".equals(roleId) && roleId.indexOf(Contents.ROLE_TYPE_HADMIN)>-1)
		{
			result = true;
		}
		return result;
	}
	
	/**
	 * 判断是否总部角色
	 */
	public static boolean isHQRole()
	{
		boolean result = false;
		UserLogin user = getLoginedUser();
		String roleId = user.getRoleId();
		if(null!=roleId && !"".equals(roleId) && roleId.indexOf("H")==0)
		{
			result = true;
		}
		return result;
	}
	
	
	/**
	 * 判断是否具有编辑类型权限(只有总部管理员(总部领导)，分公司管理员（分公司领导）可以编辑[增加、修改、删除、导入]数据)
	 * @return
	 */
	public static boolean canEdit()
	{
		boolean result = false;
		UserLogin user = getLoginedUser();
		String roleId = user.getRoleId();
		if(null!=roleId && !"".equals(roleId) && (roleId.indexOf(Contents.ROLE_TYPE_HLEADER)>-1 || roleId.indexOf(Contents.ROLE_TYPE_BLEADER)>-1 || 
				roleId.indexOf(Contents.ROLE_TYPE_HADMIN)>-1 || roleId.indexOf(Contents.ROLE_TYPE_BADMIN)>-1))
		{
			result = true;
		}
		return result;
	}
	

	/**
	 * 获取当前的国际化语言环境
	 * @return
	 */
	public static String getLanguage()
	{
		ActionContext context = ActionContext.getContext();
		String language = context.getSession().get(Contents.LANGUAGE_SESSION)+"";
		return language;
	}
	
	
	/**
	 * 组装数据权限控制sql
	 * @param conditionFiled
	 * @param permissionType
	 * @return
	 */
	/*public static String buildDataPermissionSql(String conditionFiled,String permissionType)
	{
		StringBuffer sql = new StringBuffer();
		
		//机构权限
		if(Contents.ROLE_DATA_PERMISSION_PARTY.equals(permissionType))
		{
			sql.append(conditionFiled);
			sql.append(" IN ( SELECT z1.PERMISSION_VALUE FROM role_data_permission z1,user_role_mapping z2 WHERE z1.PERMISSION_TYPE = '");
			sql.append(Contents.ROLE_DATA_PERMISSION_PARTY);
			sql.append("' AND z1.ROLE_ID = z2.ROLE_ID AND z2.USER_LOGIN_ID = '");
			sql.append(getLoginedUserId());
			sql.append("')");
		}
		
		return sql.toString();
	}*/
	
	
	/**
	 * 获取当前用户所能查看到的所有机构ID
	 * @return 返回所有机构组装成的字符串：返回的字符串格式     'partyId01','partyId02','partyId03','partyId04'
	 */
	public static String loadPartyIdsByUserId()
	{
		String result = "";
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		ServletContext sc = request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		IUserLoginService userLoginService = (IUserLoginService) ac.getBean("userLoginService");
		String userLoginId = getLoginedUserId();
		String permissionType = Contents.ROLE_DATA_PERMISSION_PARTY;
		try
		{
			List<String> userPartyIds = userLoginService.selectDataPermissionValues(permissionType, userLoginId);
			if(null!=userPartyIds && !userPartyIds.isEmpty())
			{
				for(String userPartyId : userPartyIds)
				{
					result +="'" + userPartyId + "',";
				}
				if(null!=result && !"".equals(result))
				{
					result = result.substring(0, result.length()-1);
				}
			}
		}
		catch (Exception e) 
		{
			log.error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 判断是否是windows操作系统
	 * @return
	 */
	public static boolean isWindowsOS()
	{
	    boolean isWindowsOS = false;
	    String osName = System.getProperty("os.name");
	    if(osName.toLowerCase().indexOf("windows")>-1)
	    {
	      isWindowsOS = true;
	    }
	    return isWindowsOS;
	 }
	
	/**
	 * 判断是否是分公司领导或分公司管理员
	 * @return
	 */
	public static boolean isBranchRole(){
		boolean result = false;
		UserLogin user = getLoginedUser();
		String roleId = user.getRoleId();
		if(null!=roleId && !"".equals(roleId) && (roleId.indexOf(Contents.ROLE_TYPE_BLEADER)>-1 || roleId.indexOf(Contents.ROLE_TYPE_BADMIN)>-1))
		{
			result = true;
		}
		return result;
	}
	
	/**
	 * 判断是否是业务中心"SC"
	 * @return
	 */
	public static boolean isSaleCenter(){
		
		boolean result = false;
		UserLogin user = getLoginedUser();
		String getRoleId = user.getRoleId();
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		ServletContext sc = request.getSession().getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
		IRoleService IRoleService = (IRoleService) ac.getBean("roleService");
		try {
			Role roleName = IRoleService.getRoleName(getRoleId);
				if(roleName!=null && !roleName.equals("")){
					if(roleName.getRoleName()!=null && !roleName.getRoleName().equals("") && (roleName.getRoleName().indexOf(Contents.Role_Name)>-1 || getRoleId.indexOf(Contents.Role_Name)>-1)){
						result = true;
					}
				}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;		
	}
}
