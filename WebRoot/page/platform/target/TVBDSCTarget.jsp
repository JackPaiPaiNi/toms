<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="cn.tcl.common.WebPageUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String party = (String) request.getSession().getAttribute("loginUserId");									
	String isHQRole = WebPageUtil.isHQRole()+"";
	String loginCountry = WebPageUtil.getLoginedUser().getPartyId();
	int country =Integer.parseInt( WebPageUtil.getLoginedUser().getPartyId());;
	String userPartyIds = "("+WebPageUtil.loadPartyIdsByUserId()+")";
	boolean isAdmin=WebPageUtil.isHAdmin();
	String roleName=WebPageUtil.getLoginedUser().getRoleId();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.bdscTarget'/></title>
		
	
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/bootstrap-select.js"></script>
      <link rel="stylesheet" type="text/css" href="<%=basePath%>css/bootstrap-select.css">
	 <link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <link rel="stylesheet" type="text/css" href="<%=basePath%>css/target/BDSC_content.css"/> 
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/target/BDSC_country_target.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/target/BDSCTarget.js"></script>
	<script>
		var my_login_id='<%=party%>';
		var isHQRole='<%=isHQRole%>';
		var loginCountry='<%=loginCountry%>';
		var country='<%=country%>';
		var partyList="<%=userPartyIds%>";
		var isAdmin="<%=isAdmin%>";
		var roleName="<%=roleName%>";
		
	</script>
	
	
</head>
<body >
	<!--右侧主要内容-->
       <div class="main">
        	<!--内容标题-->
         	<div class="content_title">         		
         		<span class="">Sell-out Goal Data</span>
         	</div>
         	<input type="hidden" id='type' value="1"/>
         	<!--内容正文-->
         	<div class="content_body">
         		<!--门店目标-->
	     	
	     		<div class="easyui-tabs" id="addTabsMine">
	     			<div title="Month" style="padding:10px">
	     				<p class="StoresTarget" id="StoresTarget">
			         		<a href="<%=basePath%>download/BD Targets.xlsx" class="Download">Download Template File</a> 
			         		<a href="#" class="Up" onclick="importSales()">Upload  target</a> <br />
			     		</p>
	     				<div style="margin-bottom:6px;">
	     					<span class="monthTitle">Month</span>
	         		 		<input  id="date"  onchange="selectBDSCTarget('1')" readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" />
	     				</div>
	     				<!--表格-->
			     		<div class="form_overview"  onresize="parent.location.reload();">
			     			<div class="fl Left_Head" id="test_Head">
				     			<table border="0" cellspacing="0" cellpadding="0">
				         			<thead>
				         				<tr style="border-top: 1px solid #bbb;">
				         					<th rowspan="2">Country</th>
				         					<th rowspan="2">Date</th>
				         					<th rowspan="2">Type</th>
				         					<th id="bloOrNo" style="border-right: 0;"> </th>
					         			</tr>
					         			<tr>
				         					<th style="border-right:2px solid #bbb;">Total Target</th>
					         			</tr>
				         			</thead>
				         			<tbody id="tbody" >
				         			          				
				     				</tbody>
				         		</table>
				     		</div>
				     		<div class="fl Right_Head" id="test_Right">
								<table border="0" cellspacing="0" cellpadding="0">
									<thead  id="lineMap">
										
									</thead>
									<tbody id="rightBody" onscroll="moveTopOrLeft('test_Right','test_Head')">
									
									</tbody>
								</table>
							</div>
			     		</div>
	     			</div>
	     			<div title="Year" style="padding:10px">
	     					<p class="StoresTarget" id="StoresTarget">
				         		<a href="<%=basePath%>download/BD Targets-Year.xlsx" class="Download">Download Template File</a> 
				         		<a href="#" class="Up" onclick="importSalesYear()">Upload  target</a> <br />
				     		</p>
	     					<div style="margin-bottom:6px;" onresize="parent.location.reload();">
		     					<span class="monthTitle">Year</span>
		         		 		<input  id="dateYear"  onchange="selectBDSCTargetYear('1')" readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" />
		     				</div>
	     			
	     				
	     				<div class="form_overview">
			     			<div class="fl Left_HeadY" id="test_HeadY">
				     			<table border="0" cellspacing="0" cellpadding="0">
				         			<thead>
				         				<tr style="border-top: 1px solid #bbb;">
				         					<th>Country</th>
				         					<th>Date</th>
				         					<th>Type</th>
				         					<th>Total Target</th>
				         					<th>UD Target</th>
					         			</tr>
				         			</thead>
				         			<tbody id="leftBodyYear" >
				         			          				
				     				</tbody>
				         		</table>
				     		</div>
			     		</div>
	     			</div>
	     		</div>
		     		
         	</div>
		</div>  
		<div id="lalala"></div>     
	<div id='loadingImport' style='width:5rem; height:5rem;display:none;position: absolute;left: -webkit-calc(50% - 50px);left: -moz-calc(50% - 50px);left: calc(50% - 50px);
					top: -webkit-calc(50% - 30px);top: -moz-calc(50% - 30px);top: calc(50% - 30px);z-index: 9005;'>
							<div>
								<table border='0'> 
									<tr>
										<td><img src='<%=basePath%>images/statement/import.gif' style='width: 100px;height: 100px;z-index: 10000;'/></td>
										</tr> 	 
								
									</table> 
								</div>
							</div>
</body>
</html>