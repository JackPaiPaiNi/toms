package cn.tcl.platform.user.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.tcl.platform.user.service.IUserLoginService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthorityInterceptor extends AbstractInterceptor
{
	@Autowired(required = false)
	@Qualifier("userLoginService")
	private IUserLoginService userLoginService;
	
	/**
	 * 判断是否是Ajax请求类型
	 * @param request
	 * @return
	 */
	public boolean isAjaxRequest(HttpServletRequest request) 
	{
		String header = request.getHeader("X-Requested-With");
		if (header != null && "XMLHttpRequest".equals(header))
		    return true;
		else
		    return false;
	}
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception 
	{
		ActionContext context = invocation.getInvocationContext();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		
		Map<String, Object> session = context.getSession();
		String loginUserId = (String)session.get("loginUserId");
		if(null != loginUserId && !"".equals(loginUserId))
		{
			response.setHeader("expires", "0");
			response.setHeader("pragma", "no-cache");
			return invocation.invoke();
		}
		else
		{
			//Ajax请求
			if(isAjaxRequest(request))
			{
				response.sendError(408);
			}
			//非Ajax请求
			else
			{
				context.put("tip", "您还未登陆！");
			}
			return "loginPage";
		}
	}
}
