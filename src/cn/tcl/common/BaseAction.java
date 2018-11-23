package cn.tcl.common;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.json.annotations.JSON;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, ServletContextAware{
	
	protected Logger log = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

	protected HttpServletRequest request;
	
	protected HttpServletResponse response;
	
	protected ServletContext servletContext;
	
	protected File uploadExcel;
	protected String uploadExcelFileName;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	protected static String toString(String[] strs) {
		if(strs == null) {
			return "";
		}
		if(strs.length <= 0) {
			return "";
		}
		String str = Arrays.toString(strs);
		return str.substring(1, str.length() - 1);
	}
	
	protected static Timestamp toTimestamp(String s) throws Exception{
		if(s == null || "".equals(s)) {
			return null;
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return new Timestamp(f.parse(s).getTime());
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public void setUploadExcel(File uploadExcel) {
		this.uploadExcel = uploadExcel;
	}

	public void setUploadExcelFileName(String uploadExcelFileName) {
		this.uploadExcelFileName = uploadExcelFileName;
	}
	
	public String getClassRealPath()
	{
		//默认windows
		String realPath = Thread.currentThread().getContextClassLoader().getResource("").toString().replaceAll("%20", " ").replaceAll("file:/", "");
		if(!WebPageUtil.isWindowsOS())
		{
			realPath = File.separatorChar + realPath;
		}
		return realPath;
	}
	
	/**
	 * 界面下拉菜单更改语言环境
	 */
	@JSON
	public void setLanguage()
	{
		String language = request.getParameter("language");
		Locale locale=new Locale(language);
		request.getSession().setAttribute(Contents.LANGUAGE_SESSION, locale);
	}
}
