 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String party = (String) request.getSession().getAttribute("loginUserId");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='sample.targetAch.title'/></title>
	<script>var my_login_id='<%=party%>';</script>
	
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/sellIn/Sales center SI performance.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/sellIn/jquery-ui.css"/>
	<script src="<%=basePath%>js/easyui1.4/jquery.easyui.min.js" ></script>
	<script src="<%=basePath%>js/platform/sellIn/sellInByHq.js" type="text/javascript" charset="utf-8"></script>

	 <script src="<%=basePath%>js/platform/sellIn/jquery-1.9.1.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=basePath%>js/platform/sellIn/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/sellIn/Sales center SI performance.js"></script>
	
	<link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/sellIn/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
	 
      
</head>
<body>
	<div class="main">
        	<p class="create"   tcl-text="permission.labelkey.sihq" ></p> 
        	<div class="search_SI">
				<%-- <div class="inb">
					<label for="" tcl-text="statement.center">业务中心</label>
					<select id="center"></select>
				</div>
				<div class="inb">
					<label for="" tcl-text="statement.country">国家</label>
					<select  id="country">  <!-- <option value='5'>flb</option> -->  </select>
				</div> --%>
				<div class="inb">
					<label for="" tcl-text="product.list.th.line" >产品系列</label>
			<select id="line" onchange="searchData()">
    	</select>
				</div>
<!-- 				<a href="#" onclick="searchData()" class="inb search">Search</a>
 -->			</div>
        	<!-- Tabs -->
			<div id="prizes">
				<ul>
					<li><a href="#tabs-1" tcl-text="statement.year">年</a></li>
					<li><a href="#tabs-2" tcl-text="statement.quarter">季度</a></li>
					<li><a href="#tabs-3" tcl-text="statement.month">月</a></li>
					<li><a href="#tabs-4" tcl-text="statement.customs">自定义</a></li>
				</ul>
				<div id="tabs-1">
					<div class="inb"  id="yearDate">
				<input onchange="getYear()"  id="reservationAnnual" name="reservatio" type="text"   style="width: 92%;"  maxlength="20"class="fl day-Custom form-control" 
				onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
					</div>
					<div style="overflow: auto;"  >
						<table  id="year" border="1" style="border-bottom-color: black; border-top-color: black; color: #000000; border-right-color: black; font-size: medium; border-left-color: black;border-collapse: collapse;">
						</table>
					</div>
					
				
				</div>
				<div id="tabs-2">
					<div>
						<div class="inb"  id="quarterDate">
							<input onchange="getQua()"  style="width: 92%;" class="fl day-Custom form-control" readonly id="reservationQuarterly" name="reservatio" 
         				 onclick="WdatePicker({dateFmt:'yyyy-QM', isQuarter:true, isShowOK:false,disabledDates:['....-0[5-9]-..','....-1[0-2]-..'], startDate:'%y-02-01' })" type="text" />
         			
						</div>
						
					</div>
					<div style="overflow: auto;">
						<table id="quarter" border="1" style="border-bottom-color: black; border-top-color: black;  color: #000000; border-right-color: black; font-size: medium; border-left-color: black;border-collapse: collapse;">
					</table>
					</div>
				</div>
				<div id="tabs-3">
					<div>
						<div class="inb" id="monthDate">
						<input   onchange="getMonth()" style="width: 92%;"  id="reservationMonthly"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="fl day-Custom form-control"/>

						</div>
						
					</div>
				<div style="overflow: auto;">
					<table id="month" border="1" style="border-bottom-color: black; border-top-color: black; color: #000000; border-right-color: black; font-size: medium; border-left-color: black;border-collapse: collapse;">
					</table>
				</div>
				</div>
				<div id="tabs-4">
					<div>
						<div class="inb">
						  <input type="text" onclick="bb()" readonly style="width: 100%;" name="reservation" id="reservationWeekly" class="fl day-Custom form-control" /> 
						</div>
						
					</div>
					
					<div style="overflow: auto;">
					<table id="week" border="1" style="border-bottom-color: black; border-top-color: black;  color: #000000; border-right-color: black; font-size: medium; border-left-color: black;border-collapse: collapse;">
					</table>
					</div>

				</div>
			</div>
       	</div>        
		
</body>
</html>