<%@page import="cn.tcl.platform.permission.service.IPermissionService"%>
<%@page import="cn.tcl.common.TreeNode"%>
<%@ page language="java" pageEncoding="UTF-8" import="java.util.*,com.opensymphony.xwork2.ActionContext,cn.tcl.common.WebPageUtil,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	//设置当前语言环境
	String language = WebPageUtil.setLanguage(request);
	request.setAttribute("language", language);
	ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(application);
	IPermissionService permissionService=(IPermissionService)context.getBean("permissionService");
	String userLoginId = session.getAttribute("loginUserId").toString();
	List<TreeNode> tnList = permissionService.selectPermissionByUserId(userLoginId);
	request.setAttribute("tnList", tnList);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta name="viewport" content="width=device-width,initial-scale=1">
		<title><sitemesh:write property='title'>Title goes here</sitemesh:write></title>
		<link rel="stylesheet" href="<%=basePath%>bootstrap/css/bootstrap.min.css"/>
		<link rel="stylesheet" href="<%=basePath%>font-awesome/css/font-awesome.min.css"/>
		<link rel="stylesheet" href="<%=basePath%>js/easyui1.4/themes/metro-orange/easyui.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>js/easyui1.4/themes/icon.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>easyui_deleted/themes/icon.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>css/common.css" type="text/css" />
		<link rel="shortcut icon" href="<%=basePath%>css/TCLico.ico" />
		<script src="<%=basePath%>js/easyui1.4/jquery.min.js" ></script>
		<script src="<%=basePath%>js/platform/locale/tomsLocale-${language}.js" ></script>
		<!-- common.jquery必须在jquery之后，因为它依赖于jquery -->
		<script src="<%=basePath%>js/common.jquery.js"></script>
		
		<!-- bootstrap必须在easyui前面，否则easyui的form会不正常 -->
		<script src="<%=basePath%>bootstrap/js/bootstrap.min.js"></script>
		
		<script src="<%=basePath%>js/easyui1.4/jquery.easyui.min.js" ></script>
		<script src="<%=basePath%>js/easyui1.4/jquery.easyui.panelOnMove.js" ></script>
		
		<!-- common.easyui必须在easyui之后，因为它依赖于easyui -->
		<script src="<%=basePath%>js/common.easyui.js"></script>
		
		<script src="<%=basePath%>js/easyui1.4/locale/easyui-lang-${language}.js" ></script>
		<script src="<%=basePath%>js/MD5.js" ></script>
		<!--[if lt IE 9]>
			<script src="<%=basePath%>js/respond.min.js"></script>
			<script src="<%=basePath%>js/html5shiv.min.js"></script>
			<script src="<%=basePath%>js/css3-mediaqueries.js"></script>
		<![endif]-->
		<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>
		<c:set var="canEdit" value="${canEdit}" ></c:set>
		
		<script>
	
		
			var baseUrl="${ctx}/";
			var language = "${language}";
			var canEdit = "${canEdit}";
		
			/**
			 * 提交导入表单
			 */
			 
			function uploadFormSubmit()
			{
				var uploadExcel = $("#uploadExcel").filebox("getValue");
				if(null==uploadExcel || ""==uploadExcel)
				{
					$("#errorMsgPanel").html("<s:text name='import.error.notcheckfile' />");
					return;
				}else{
					var excelSuffix = uploadExcel.split(".")[1];
					if("xlsx" != excelSuffix && "xls" != excelSuffix){
						$("#errorMsgPanel").html("<s:text name='import.error.excelFile' />");
						return;
					}
				}
				var submitUrl = $("#uploadForm").attr("action");
				//$(document.body).append("<div id='loadingImport' style='width:5rem;height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 9005;'><div><table border='0'><tr><td><img src='"+baseUrl+"images/statement/import.gif' style='width: 100px;height: 100px;z-index: 10000;'/></td></tr></table></div></div>" ); 
				$("#loadingImport").show();
				$("#uploadForm").form('submit', {
				    url:submitUrl,    
				    success:function(data){
				    $("#loadingImport").hide();
				        if("success"==data)
			        	{
			        		$("#errorMsgPanel").html("<s:text name='import.success' />");
				        	$("table[name=gridTable]").datagrid("reload");
				        	$("#uploadDialogWin").window('close');
				        	alert("Uploaded successfully");
			        	}
				        else
			        	{
				        	if(data.indexOf('id="username"')>-1)
			        		{
				        		top.location = baseUrl + "loginPage.action";
			        		}
				        	else
			        		{
				        		var _data = ReplaceAll(ReplaceAll(data,"&lt;","<"),"&gt;",">")
					        	$("#errorMsgPanel").html(_data);
			        		}
			        	}
				    }
				});
			}

			/**
			 * 打开导入窗口
			 * title：导入的标题，submitUrl:导入功能的action
			 */
			function showImportWin(title,submitUrl)
			{
			$("#uploadForm").attr("action",submitUrl);
				if(!$("#uploadDialog") || $("#uploadDialog").length<1)
				{
					var tips = "<s:text name='import.form.title.checkexcel.tips' />";
					$(document.body).append("<div id='uploadDialog'><div id='uploadDialogWin' class='easyui-window' minimizable='false' maximizable='false' collapsible='false' title='"+title+"' style='width:750px;height:265px;padding:20px 40px;'>"+
												"<form id='uploadForm'  method='post' enctype='multipart/form-data' action="+submitUrl+">"+
										   			"<label for='uploadExcel'><s:text name='import.form.title.checkexcel' /></label>"+
										   			"<input id='uploadExcel' name='uploadExcel'  class='easyui-filebox' buttonText=\"<s:text name='import.form.checkexcelbut' />\" style='width:400px' data-options=\"prompt:'"+tips+"'\"/>"+
										            "&nbsp;<a id='uploadBut' class='easyui-linkbutton' data-options='iconCls:\"icon-save\"' href='#' style='width:70px' onclick='uploadFormSubmit()' ><s:text name='import.form.impbut' /></a>"+
												"</form>"+
												"<div id='errorMsgPanel' style='background-color:#f2f6f8;overflow-y:auto;max-height: 150px;color: red;text-align: left; letter-spacing:2px;line-height:20px;padding-left: 30px;margin-top: 10px;'>"+
													
												"</div>"
												+"<div id='loadingImport' style='width:5rem; height:5rem;display:none;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 9005;'> "
								+"<div>" 
							+"<table border='0'> " 
								+"<tr>" 
								+"	<td><img src='"+baseUrl+"images/statement/import.gif' style='width: 100px;height: 100px;z-index: 10000;'/></td>" 
								+"</tr>"  	 
						
							+"</table> " 
						+"</div>"  
				+"	</div>"
										  +"</div></div>");
					
				}
				$.parser.parse("#uploadDialog");
				$("#uploadDialogWin").window('open');
				clearErrorMsg();
				
			}
			
			/**
			 * 打开目标导入窗口
			 * title：导入的标题，submitUrl:导入功能的action
			 * val：目标类型
			 */		
			function showTargetImportWin(title,submitUrl)
			{
			$("#uploadForm").attr("action",submitUrl);
				if(!$("#uploadDialog") || $("#uploadDialog").length<1)
				{
					var tips = "<s:text name='import.form.title.checkexcel.tips' />";
					$(document.body).append("<div id='uploadDialog'><div id='uploadDialogWin' class='easyui-window' minimizable='false' maximizable='false' collapsible='false' title='"+title+"' style='width:750px;height:265px;padding:20px 40px;'>"+
												"<form id='uploadForm'  method='post' enctype='multipart/form-data' action="+submitUrl+">"+
										   			"<label for='uploadExcel'><s:text name='import.form.title.checkexcel' /></label>"+
										   			"<input id='uploadExcel' name='uploadExcel'  class='easyui-filebox' buttonText=\"<s:text name='import.form.checkexcelbut' />\" style='width:400px' data-options=\"prompt:'"+tips+"'\"/>"+
										            "&nbsp;<a id='uploadBut' class='easyui-linkbutton' data-options='iconCls:\"icon-save\"' href='#' style='width:70px' onclick='uploadFormSubmit()' ><s:text name='import.form.impbut' /></a>"+
										        "</form>"+
												"<div id='errorMsgPanel' style='background-color:#f2f6f8;overflow-y:auto;max-height: 150px;color: red;text-align: left; letter-spacing:2px;line-height:20px;padding-left: 30px;margin-top: 10px;'>"+
													
												"</div>"
												+"<div id='loadingImport' style='width:5rem; height:5rem;display:none;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 9005;'> "
								+"<div>" 
							+"<table border='0'> " 
								+"<tr>" 
								+"	<td><img src='"+baseUrl+"images/statement/import.gif' style='width: 100px;height: 100px;z-index: 10000;'/></td>" 
								+"</tr>"  	 
						
							+"</table> " 
						+"</div>"  
				+"	</div>"
										  +"</div></div>");
					
				}
				$.parser.parse("#uploadDialog");
				$("#uploadDialogWin").window('open');
				clearErrorMsg();
				
			}
			
			
			
			//20160531 add huangqitai 
			function clearErrorMsg(){
				var errorMsg = $("#errorMsgPanel").html();
				if(errorMsg != ""){
					$("#errorMsgPanel").html("");//清空提示信息
				}
			}
			
			/**
		     * 按钮权限初始化控制--临时方案
		     */
			$(function(){
				//按钮权限控制部分开始
				if(typeof(canEdit)!="undefined" && "true"==canEdit)
				{
					//系统管理员或分公司管理员，拥有编辑权限，不做处理
				}
				else
				{
					//不拥有编辑权限的人员，统一去掉：添加，删除，编辑，导入，启用，禁用等按钮
					//根据linkbutton样式，初始化页面即调用
					$(".easyui-linkbutton").each(function(){
						var iconcls = $(this).attr("iconcls");
						if("icon-search"!=iconcls && "icon-redo"!=iconcls)
						{
							$(this).remove();
						}
					});
				}
				//按钮权限控制部分结束
				
				//Ajax请求全局设置
				$(document).ajaxError(function(event,jqxhr,settings,exception){
					if(jqxhr.status)
					{
						top.location = baseUrl + "loginPage.action";
					}
				});
			});
			/**
		     * 表格toolbar，要在表格加载完成后调用，初始化
		     */
			function initDataGridToolBar()
			{
				if(typeof(canEdit)!="undefined" && "true"==canEdit)
				{
					//系统管理员或分公司管理员，拥有编辑权限，不做处理
				}
				else
				{
					//根据datagrid-toolbar样式
					$(".datagrid-toolbar").each(function(){
						$(this).remove();
					});
				}
			}
			/**
		     * 表格单元格操作，要在表格加载完成后调用，初始化
		     */
			function initDataGridCells()
			{
				if(typeof(canEdit)!="undefined" && "true"==canEdit)
				{
					//系统管理员或分公司管理员，拥有编辑权限，不做处理
				}
				else
				{
					//根据tcl-btn样式
					$(".tcl-btn").each(function(){
						$(this).remove();
					});
				}
			}
			/* $(document).ready(function(){
				prime();
				function oneList(){
		            $(".easyui-layout").layout('panel', 'west').panel("resize",{width:150});
		            $(".easyui-layout").layout("resize");
		        };
				function twoList(){
		            $(".easyui-layout").layout('panel', 'west').panel("resize",{width:300});
		            $(".easyui-layout").layout("resize");
		        };
				$('#side-menu>li>a').click(function(){
					//debugger
					//二级菜单
					//prime();
					$('#side-menu>li>ul').each(function(){
						$(this).removeClass('in');
						$(this).css('display','none');
					})
					$(this).next('ul').css('display','block');
					//被点击li的样式
					$('#side-menu>li').each(function(){
						$(this).css('background','transparent').css('border','0');
					})
					$(this).parent('li').css('background','#eee').css('border-top','1px solid #DDD').css('border-bottom','1px solid #DDD');
					//被点击a
					$('#side-menu>li>a').each(function(){
						$(this).css('color','#ddd');
					})
					$(this).css('color','#337ab7');
					var ulDisplay = $(this).next('ul').css('display');
					if(ulDisplay == 'block'){
						twoList()
					}else{ 
						oneList();
					};
				});
				function prime(){
					var ulDisplayt = $('ul.in').css('display');
					if(ulDisplayt == 'block'){
						twoList();
					};
					$('.active').parent('ul').parent('li').css('background','#eee').css('border-top','1px solid #DDD').css('border-bottom','1px solid #DDD');
					$('.active').parent('ul').siblings('a').css('color','#337ab7');
					$('.active>a').css('color','#337ab7');
				};
			}) */ 
			
			$(document).ready(function(){
				prime();
				function oneList(){
		            $("#customBady").layout('panel', 'west').panel("resize",{width:150});
		            $("#customBady").layout("resize");
		        };
				function twoList(){
		            $("#customBady").layout('panel', 'west').panel("resize",{width:300});
		            $("#customBady").layout("resize");
		        };
				$('#side-menu>li>a').click(function(){
					var p = $(this).attr('href');
					//alert(p);
					//debugger
					//二级菜单
					//prime();
					$('#side-menu>li').each(function(){
						$(this).css('background','rgba(48,57,73,1)').css('border-top','0').css('border-bottom','0');
						$('#side-menu>li>a').css('color','#ddd');
					})
					$('#side-menu>li>ul').each(function(){
						$(this).removeClass('in');
						$(this).css('display','none');
					})
					if(p == '' || p == '#'){
						twoList();
						$(this).next('ul').css('display','block');
						$(this).parent('li').css('background','#eee').css('border-top','1px solid #DDD').css('border-bottom','1px solid #DDD');
						$(this).css('color','#337ab7');
					}else{
						oneList();
					}
				});
				$('.secondary-menu>li>a').click(function(){
					$('.secondary-menu>li').each(function(){
						$(this).css('background','#f8f8f8').css('border-top','0').css('border-bottom','0');
					})
					$('.secondary-menu>li>ul').each(function(){
						$(this).removeClass('in');
						$(this).css('display','none');
					})
					$(this).next('ul').css('display','block');
				});
				function prime(){
					var ulDisplayt = $('.Level3-menu.in').css('display');
					var ulDisplayt2 = $('.secondary-menu.in').css('display');
					$('.active>a').css('color','#337ab7');
					if(ulDisplayt == 'block'){
						twoList();
						$('.Level3-menu.in').parent('li').parent('.secondary-menu').css('display','block');
						$('.active').parent('ul').parent('li').parent('ul').parent('li').css('background','#eee').css('border-top','1px solid #DDD').css('border-bottom','1px solid #DDD');
						$('.active').parent('ul').parent('li').parent('ul').siblings('a').css('color','#337ab7');
						$('.active').parent('ul').parent('li').css('background','#eee').css('border-top','1px solid #DDD').css('border-bottom','1px solid #DDD');
						$('.active').parent('ul').siblings('a').css('color','#337ab7');
						$('.active').css('background','#fff');
						$('.active>a').css('color','#d4a375');
					};
					if(ulDisplayt2 == 'block'){
						twoList();
						$('.active').parent('ul').parent('li').css('background','#eee').css('border-top','1px solid #DDD').css('border-bottom','1px solid #DDD');
						$('.active').parent('ul').siblings('a').css('color','#337ab7');
						$('.active>a').css('color','#337ab7');
					};
				};
			})
		</script>
		
		<sitemesh:write property='head'/>
	</head>
	<body id="customBady" class="easyui-layout fixed-navigation" fit="true">
	
	
		<fmt:setLocale value="${language}"/>           <!--指定区域语言-->
       	<fmt:bundle basename="cn/tcl/message"> 
			<div region="north" border="false" style="height:60px;background: #f3f3f4;">
				<div class="col-xs-12 col-sm-8 col-lg-9">
					<h3 class="text-primary"><s:text name="system.name" /><span class="badge">alpha</span></h3>
				</div>
				<div class="col-xs-12 col-sm-4 col-lg-3 welcome">
					<s:text name="system.welcome" />${loginUser.userName}
					<span>|</span>
					<a href="<%=basePath%>loginOut.action"  target="_self" class="logout" ><s:text name="system.logout" /></a> 
				</div>
			</div>
			<div region="west" border="false" split="true" id="customWest" width="150" style="background: rgba(48,57,73,1);border-top-width:1px;overflow-x: hidden;">
				<nav class="navbar-default navbar-static-side" role="navigation">
		            <div class="sidebar-collapse">
		                <ul class="nav tclmenu" id="side-menu">
		                <!-- 99999999999999 -->
		                	<c:forEach items="${tnList}" var="tn" varStatus="status">
		                		<li dataid="${tn.id}">
			                		<c:if test="${not empty tn.attributes.url}">
				                        <a href="${tn.attributes.url}">
				                        	<i class="fa ${tn.attributes.code}"></i> 
				                        	<span class="nav-label"><fmt:message key="${tn.attributes.labelKey}"/></span>
				                        </a>
			                		</c:if>
			                		<c:if test="${empty tn.attributes.url && not empty tn.children}">
			                			<a href="#" data-toggle="collapse" class="collapsed" data-target="#submenu_${tn.id}">
				                        	<i class="fa ${tn.attributes.code}"></i>
				                        	<span class="nav-label"><fmt:message key="${tn.attributes.labelKey}"/></span>
				                        	<span class="fa fa-angle-right pull-right"></span>
				                        </a>
				                        <ul class="nav nav-second-level collapse secondary-menu" id="submenu_${tn.id}">
				                        	<c:forEach items="${tn.children}" var="tnChild" varStatus="statusChild">
				                        		<li>
					                        		<c:if test="${not empty tnChild.attributes.url}">
								                        <a href="${tnChild.attributes.url}">
								                        	<span class="nav-label"><fmt:message key="${tnChild.attributes.labelKey}"/></span>
								                        </a>
							                		</c:if>
							                		<c:if test="${empty tnChild.attributes.url && not empty tnChild.children}">
						                            	<a href="#" data-toggle="collapse" class="collapsed" data-target="#submenu_${tnChild.id}">
							                            	<span class="nav-label"><fmt:message key="${tnChild.attributes.labelKey}"/></span>
						                        			<span class="fa fa-angle-right pull-right"></span>  
						                            	</a>
					                             		<ul class="nav nav-second-level collapse Level3-menu" id="submenu_${tnChild.id}">
					                            			<c:forEach items="${tnChild.children}" var="tnChildren" varStatus="statusChildren">
								                        		<li>
									                            	<a href="${tnChildren.attributes.url}"><fmt:message key="${tnChildren.attributes.labelKey}"/></a>
									                            </li>
								                    		</c:forEach>
								                    	</ul>
					                            	</c:if>
					                            </li>
				                        	</c:forEach>
			                        	</ul>
			                		</c:if>
			                	</li>
		                	</c:forEach>
		                	<!-- 测试 -->
		                </ul>
		            </div>
		        </nav>
			</div>
			<div region="center" border="false" split="true" id="customId">
				<sitemesh:write property='body'>这里是内容</sitemesh:write>
			</div>
			
		</fmt:bundle>
		
		
		
		
	</body>
</html>