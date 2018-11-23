<%@ page language="java" import="java.util.Locale,com.opensymphony.xwork2.ActionContext,cn.tcl.common.WebPageUtil" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	//设置当前语言环境
	String language = WebPageUtil.setLanguage(request);
	request.setAttribute("language", language);
%>
<!DOCTYPE HTML>
<html>
<head>
	<title><s:text name="login.title" /></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0">
	
	<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" href="<%=basePath%>css/login/login2.css" />
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
	
</head>
<body>
	<div class="container-fluid wrap">
		<!-- <div class="banner" style="background:#eaeaea url(<%=basePath%>/images/login/tcl_logo.png) 50px 0 no-repeat;display:none;">
			<h4>TCL终端运营平台</h4>
			<h4>TCL Terminal Operation Platform</h4>
		</div> -->
		<div class='row content'>
			<div class="hidden-xs col-sm-8 col-lg-9 big-bg">
				<img src="<%=basePath%>images/login/bg_1200_2.jpg" class="img-responsive big-image"/>
			</div>
			<div class="col-sm-4 col-lg-3 login-form">
				<h4>OBC零售管理系统</h4>
				<h4>TCL OBC Retail System</h4>
				<form autocomplete="on">
					<h4>USER LOGIN</h4>
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">
								<i class="glyphicon glyphicon-user"></i>
							</div>
							<input id="username" class="form-control" name="username"
								required="required" type="text"
								placeholder="<s:text name="login.form.username.tips" />" />
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<div class="input-group-addon">
								<i class="glyphicon glyphicon-lock"></i>
							</div>
							<input id="password" class="form-control" name="password"
								required="required" type="password"
								placeholder="<s:text name="login.form.password.tips" />" />
						</div>
					</div>
					<div class="login button">
						<a id="loginBut" class="btn btn-danger loginbtn"><s:text
								name="login.form.submit" /></a>
						<a class="login-status" style="display:none;"><i class="glyphicon glyphicon-repeat fa-spin"></i></a>
					</div>
				</form>

			</div>
		</div>
		<div style="display:hidden;">
		</div>
	</div>
	<script>
		var baseUrl="${ctx}/";var language = "${language}";
	</script>
	<script src="<%=basePath%>js/easyui1.4/jquery.min.js" ></script>
	<script src="<%=basePath%>js/platform/locale/tomsLocale-${language}.js" ></script>
	<script src="<%=basePath%>js/login/prefixfree.min.js" ></script>
	<!--[if lt IE 9]>
		<script src="<%=basePath%>js/respond.min.js"></script>
		<script src="<%=basePath%>js/html5shiv.min.js"></script>
		<script src="<%=basePath%>js/css3-mediaqueries.js"></script>
	<![endif]-->
	<script src="<%=basePath%>js/common.jquery.js"></script>
	<script src="<%=basePath%>js/common.easyui.js"></script>
	<script src="<%=basePath%>js/MD5.js" ></script>
	<script src="<%=basePath%>js/login/login2.js" ></script>
</body>
</html>
