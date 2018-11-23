package cn.tcl.strutsext;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareFilter;

import cn.tcl.common.WebPageUtil;

public class MyStrutsPrepareFilter extends StrutsPrepareFilter
{

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException
	{
		//国家化语言环境设置，如果当前session中语言环境为空，就拿客户端浏览器的语言环境设置到session中供struts调用。
		HttpServletRequest request = (HttpServletRequest)req;
		Locale.setDefault(Locale.ENGLISH);
		WebPageUtil.setLanguage(request);
		if(request.getRequestURI().contains("/WS")||request.getRequestURI().contains("/ueditor/jsp/"))
		{
			chain.doFilter(req,res);
		}
		else
		{
			super.doFilter(req, res, chain);
		}
	}
	
}
