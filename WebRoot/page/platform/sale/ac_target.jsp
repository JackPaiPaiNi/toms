<%@page import="cn.tcl.common.WebPageUtil"%>
<%@page import="cn.tcl.platform.sale.vo.SaleTarget"%>
<%@page import="cn.tcl.platform.excel.actions.ImportExcelAction"%>
<%@page import="cn.tcl.platform.excel.service.impl.ImportPHExcelServiceImpl"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String countryId=WebPageUtil.getLoginedUser().getPartyId();
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String party = (String) request.getSession().getAttribute("loginUserId");
%>

<html>
<head>
	<meta charset="UTF-8">
	<title><s:text name='permission.labelkey.xsmbgl'/></title>
	<%--  <script type="text/javascript" src="<%=basePath%>js/platform/sale/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/sale/jquery-1.4.2.min.js"></script> --%>
	<script type="text/javascript" src="<%=basePath%>js/platform/sale/mubiaoshedingzdy.js"></script>
	
	 <script type="text/javascript" src="<%=basePath%>js/platform/sale/jquery.page.js"></script> 
	 <script type="text/javascript" src="<%=basePath%>js/platform/sale/actarget.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/mubiaosheding.css"/>
	
	<script>var my_login_id='<%=party%>';</script>
	<style type="text/css">
		tr.highlight{background:#fff7d6!important;}
		tr.selected {background:#f6cd8f!important;}
		a{ text-decoration:none;}
		.tcdPageCode{width: -webkit-calc(100% - 300px);width: -moz-calc(100% - 300px);width: calc(100% - 300px); text-align: center;color: #ccc;position: fixed;bottom: 0;background-color: #fafafa;padding: 5px 0;}
		.tcdPageCode a{display: inline-block;color: #428bca;display: inline-block;height: 24px;	line-height: 24px;	padding: 0 10px;border: 1px solid #ddd;	margin: 0 2px;border-radius: 4px;vertical-align: middle;}
		.tcdPageCode a:hover{text-decoration: none;border: 1px solid #428bca;}
		.tcdPageCode span.current{display: inline-block;height: 24px;line-height: 24px;padding: 0 10px;margin: 0 2px;color: #fff;background-color: #428bca;	border: 1px solid #428bca;border-radius: 4px;vertical-align: middle;}
		.tcdPageCode span.disabled{	display: inline-block;height: 24px;line-height: 24px;padding: 0 10px;margin: 0 2px;	color: #bfbfbf;background: #f2f2f2;border: 1px solid #bfbfbf;border-radius: 4px;vertical-align: middle;}

	</style>
</head>
<body>
        <!--主要内容-->
        <div class="main">       	
         	 <p class="DefineInterval">
         	 	<span tcl-text="saletarget.list.title" style="font-weight: 600;"></span>
         	 </p>
         	<a href="<%=basePath%>download/AC Targets.xlsx" class="Download" >Download Template File</a><!--下载模板-->
         	
    		<%
    		  ImportPHExcelServiceImpl p = (ImportPHExcelServiceImpl)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("importPHExcelService");
    		  String type = p.selectSoType(countryId);
    		  if("admin".equals(type)){
    		%>
    		<a href="#" class="Up"  onclick="importACtarget(4)" plain="true"><span tcl-text="shop_target.upload"></span></a>
    		<a href="#" class="Up"  onclick="importACtarget(3)" plain="true"><span tcl-text="channel_target.upload"></span></a>
    		<a href="#" class="Up"  onclick="importACtarget(1)" plain="true"><span tcl-text="country_target.upload"></span></a>
			<%
    		  }else if("0".equals(type)){
			%>
			<a href="#" class="Up"  onclick="importACtarget(1)" plain="true"><span tcl-text="country_target.upload"></span></a>
			<%
    		  }else if("1".equals(type)){
			%>
			<a href="#" class="Up"  onclick="importACtarget(3)" plain="true"><span tcl-text="channel_target.upload"></span></a>
			<%
    		  }else if("2".equals(type)){
			%>
			<a href="#" class="Up"  onclick="importACtarget(4)" plain="true"><span tcl-text="shop_target.upload"></span></a>
			<%}%>
			
			<!--分公司目标-->
     		<p class="Headers-unified" id="BranchTarget" style="margin:0;"><span tcl-text="target.country"></span>
     		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>Year</span>
     			<select id="year" style="font-size: 14px;"></select>
     		
     		<span>Month</span>
     			<select id="month" style="font-size: 14px;">
     			
     			</select>	
     		</p>
     		<!--表格-->
     		<div id="table-Supervision" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="branch">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;">Country</td>
	         				<td colspan="4">So Target</td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2">Basic Target</td>
	         				<td colspan="2">Challenge Target</td>
	         			
	         			</tr>
	         			<tr>         					
         					<th class="branch_sum">Volume</th>
	         				<th class="branch_sum">Revenue</th>
	         				<th class="branch_sum">Volume</th>
	         				<th class="branch_sum">Revenue</th> 
         				</tr>   
         			</thead>
         			<tbody id="bran">          				
         				<tr id="branchlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	     	       				   				
         			</tbody>
         		
         		</table>
     			<!-- <a href="" onclick="hideBranch()" id="table-branch-id" class="table-hidden" target="_self"></a>
     			<a href="#" onclick="showBranch()" id="table-branch-show" class="table-show" target="_self"></a> -->
     		</div>
       	</div>        
	
</body>
</html>
