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
	<link rel="stylesheet" href="<%=basePath%>easyui/themes/metro-orange/easyui.css" type="text/css" />
	<link rel="stylesheet" href="<%=basePath%>easyui/themes/icon.css" type="text/css" />
	
	<!-- 
	<link rel="stylesheet" type="text/css" href="css/login/demo.css" />
	<link rel="stylesheet" type="text/css" href="css/login/style.css" />
	<link rel="stylesheet" type="text/css" href="css/login/animate-custom.css" /> -->
	<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" href="css/login/default.css" />
	<link rel="stylesheet" href="css/login/login-form-orange.css" />
	<link rel="stylesheet" href="<%=basePath%>css/common.css" type="text/css" />
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<script>
		var baseUrl="${ctx}/";
		var language = "${language}";
	</script>
	<script src="<%=basePath%>easyui/jquery.min.js" ></script>
	<script src="<%=basePath%>easyui/jquery.easyui.min.js" ></script>
	<script src="<%=basePath%>easyui/locale/easyui-lang-${language}.js" ></script>
	<script src="<%=basePath%>js/platform/locale/tomsLocale-${language}.js" ></script>
	<script src="<%=basePath%>js/login/prefixfree.min.js" ></script>
	<!--[if lt IE 9]>
		<script src="<%=basePath%>js/respond.min.js"></script>
		<script src="<%=basePath%>js/html5shiv.min.js"></script>
		<script src="<%=basePath%>js/css3-mediaqueries.js"></script>
	<![endif]-->
	<script src="<%=basePath%>js/common.jquery.js"></script>
	<script src="<%=basePath%>js/common.easyui.js"></script>
	<script src="<%=basePath%>js/login/login.js" ></script>
</head>
<body>
	<div class="container-fluid">
		<div class="codrops-top">
	        <span class="right" style="display:none;">
	            <select id="languageCheck">
	            </select>
	        </span>
	    </div>
	    <section>				
	        <div id="container_demo" >
	            <div id="wrapper">
	                <div id="login" class="animate form stark-login">
	                    <form autocomplete="on" class="form-horizontal"> 
                    		<div id="fade-box">
	                        <h4><s:text name="login.title" /></h4> 
	                        <div class="form-group">
	                            <label for="username" class="control-label uname" data-icon="u" > <s:text name="login.form.username" />:</label>
	                            <input id="username" class="form-control" name="username" required="required" type="text" placeholder="<s:text name="login.form.username.tips" />"/>
	                        </div>
	                        <div class="form-group"> 
	                            <label for="password" class="control-label youpasswd" data-icon="p"> <s:text name="login.form.password" />:</label>
	                            <input id="password" class="form-control" name="password" required="required" type="password" placeholder="<s:text name="login.form.password.tips" />" />
	                        </div>
	                        <div class="login button"> 
	                            <a id="loginBut" class="loginbtn"><s:text name="login.form.submit" /></a>
							</div>
							</div>
	                    </form>
	                </div>	
	                <div class="hexagons hidden-xs">
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <span>&#x2B22;</span>
				        <br>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <span>&#x2B22;</span>
				          <br>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span> 
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            <span>&#x2B22;</span>
			            
			            <br>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <span>&#x2B22;</span>
			              <br>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			                <span>&#x2B22;</span>
			              </div>      
				        </section> 
				    
				        <div id="circle1">
				          <div id="inner-cirlce1">
				            <h2> </h2>
				          </div>
				        </div>
	            </div>
	        </div>  
	    </section>
	</div>
</body>
</html>
