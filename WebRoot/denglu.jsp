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
<!DOCTYPE html>
<html>
<head>
	<title><s:text name="login.title" /></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0">	
	<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" href="<%=basePath%>css/login/denglu.css" />
	<link rel="stylesheet" href="<%=basePath%>css/login/mystyle.css" />	
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
</head>
<body>
	<div class="head">
		<div class="head-middle auto">
			<!--<p class="TCL-Chinese">创意感动生活</p>-->			
			<!-- <p class="TCL-Chinese">The Creative Life</p> -->
		</div>	
	</div>
	<div class="body">
		<div class="blank"></div>
		<div class="body-middle auto">
			<!--登录框-->
			<div class="loginBox">
				<!--<p class="loginBox-Chinese">TCL OBC Retail System</p>-->
				<p class="loginBox-English">TCL OBC Retail System</p>
				<p class="id">
					<span>ID:</span>
					<input name="username" id="username" required="required" type="text"
								placeholder="" />	
					<div class="login button">
						<a class="login-status" style="display:none;"><i class="glyphicon glyphicon-repeat fa-spin"></i></a>
					</div>				
				</p>
				<p class="password">
					<span>Password:</span>
					<input name="password" id="password" required="required" type="password"
								placeholder=""/>							
					<button id="loginBut">Log In</button>				
				</p>
				
			</div>
			<!--输入错误提示信息-->
	        <div class="Errormessage" style="display:none;">Account or password is incorrect, please enter again</div>
			<!--展示图片-->
			<div class="picture1">
				<img src="images/login/tu1nei_04.jpg"/>
			</div>
			<div class="picture2">
				<img src="images/login/tunei_17.png"/>
			</div>
			<div class="picture3">
				<img src="images/login/tunei_14.png"/>
			</div>
			<div class="picture4">
				<img src="images/login/tubiaoneirong_03.png"/>
			</div>
			<div class="picture5"></div>
			<div class="picture6"></div>
			<div class="picture7"></div>
			<div class="picture8"></div>
			<img src="images/login/tubiaoneirong_06.png"/ class="chart1">
			<img src="images/login/tubiaoneirong_09.png"/ class="chart3">			
			<!--文字-->
			<p class="HighEfficiency">High Efficiency</p>
			<p class="Comprehensive">Comprehensive</p>
			<p class="Easytouse">Easy to use</p>
			<!--提效率快增长求改变-->
			<div class="bottomCenter">
				<div class="bottomCenter-middle">
					<div class="bottomCenter-middle-fi">
						<img src="images/login/tixiaolv_03.png"/>
						<p class="moudle-text">
							<span class="moudle-text1">To Improve Efficiency</span>							
						</p>
					</div>
					<div class="bottomCenter-middle-se">
						<img src="images/login/kuaizengzhang_03.png"/>
						<p class="moudle-text">
							<span class="moudle-text1">To Speed Up The Growth</span>							
						</p>
					</div>
					<div class="bottomCenter-middle-th">
						<img src="images/login/qiugaibian_03.png"/>
						<p class="moudle-text">
							<span class="moudle-text1">The Pursuit Of Change</span>							
						</p>
					</div>
				</div>
			</div> 
		</div>			
	</div>
	<!--批号-->
	<div class="bottom">
		<p class="ct">2017. TCL OBC Retail System</p>
	</div>
	<script>
		var baseUrl="${ctx}/";var language = "${language}";
	</script>
	<script src="<%=basePath%>js/easyui1.4/jquery.min.js" ></script>
	<script src="<%=basePath%>js/platform/locale/tomsLocale-${language}.js" ></script>
	<script src="<%=basePath%>js/MD5.js" ></script>
	<script src="<%=basePath%>js/login/login3.js" ></script>
	<script src="<%=basePath%>js/login/cookie.js" ></script>
</body>
</html>