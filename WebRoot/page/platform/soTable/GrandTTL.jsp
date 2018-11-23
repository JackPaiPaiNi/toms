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
	
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/Grand Total.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/jquery-ui.css"/>
	<script src="<%=basePath%>js/easyui1.4/jquery.easyui.min.js" ></script>

	 <%-- <script src="<%=basePath%>js/platform/soTable/jquery-1.9.1.js" type="text/javascript" charset="utf-8"></script> --%>
	<script src="<%=basePath%>js/platform/soTable/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/Grand Total.js"></script>
 <script type="text/javascript" src="<%=basePath%>js/platform/soTable/GrandTTL.js"></script> 
 	 <script type="text/javascript" src="<%=basePath%>js/platform/soTable/loadData.js"></script> 
	<link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
	 <style type="text/css">
		.ui-resizable {
		    position: absolute;
		}
	</style>
      
</head>
<body>
	 <div class="main">
        	<p class="create">Grand Total</p> 
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
					<div style="height: -webkit-calc(100% - 40px);height: -moz-calc(100% - 40px);height: calc(100% - 40px);">
						<div class="fl Left_Head" id="test_Head">
							<table id="fixationYear" style="border-right: 1px solid #bbb;">
								<!--固定数据-->
							</table>
						</div>
						<div class="fl Right_Head" id="test_Right">
							<table>
								<thead id="headrYear">
									<!--数据生成表头-->
								</thead>
							</table>
						</div>
						<div class="fl Right_Body" id="test_Left" onScroll="moveLeft_Left();">
						    <table id="bydataYear">
						    	<!--数据生成底部-->
						    </table>
						</div>
					</div>
				</div>
				<div id="tabs-2">
						
						<div class="inb"  id="quarterDate">
							<input onchange="getQua()"  style="width: 92%;" class="fl day-Custom form-control" readonly id="reservationQuarterly" name="reservatio" 
         				 onclick="WdatePicker({dateFmt:'yyyy-QM', isQuarter:true, isShowOK:false,disabledDates:['....-0[5-9]-..','....-1[0-2]-..'], startDate:'%y-02-01' })" type="text" />
         			
						</div>
						<div style="height: -webkit-calc(100% - 40px);height: -moz-calc(100% - 40px);height: calc(100% - 40px);">
						<div class="fl Left_Head" id="test_Head2">
							<table id="fixationQua" style="border-right: 1px solid #bbb;">
								<!--固定数据-->
							</table>
						</div>
						<div class="fl Right_Head" id="test_Right2">
							<table>
								<thead id="headrQua">
									<!--数据生成表头-->
								</thead>
							</table>
						</div>
						<div class="fl Right_Body" id="test_Left2" onScroll="moveLeft_Lefttwo()">
						    <table id="bydataQua">
						    	<!--数据生成底部-->
						    </table>
						</div>
					</div>
				
				</div>
				<div id="tabs-3">
						<div class="inb" id="monthDate">
						<input   onchange="getMonth()" style="width: 92%;"  id="reservationMonthly"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="fl day-Custom form-control"/>
						</div>
					<div style="height: -webkit-calc(100% - 40px);height: -moz-calc(100% - 40px);height: calc(100% - 40px);">
						<div class="fl Left_Head" id="test_Head3">
							<table id="fixationMonth" style="border-right: 1px solid #bbb;">
								<!--固定数据-->
							</table>
						</div>
						<div class="fl Right_Head" id="test_Right3">
							<table>
								<thead id="headrMonth">
									<!--数据生成表头-->
								</thead>
							</table>
						</div>
						<div class="fl Right_Body" id="test_Left3" onScroll="moveLeft_Lefttre()">
						    <table id="bydataMonth">
						    	<!--数据生成底部-->
						    </table>
						</div>
					</div>
					</div>
				<div id="tabs-4">
				<div class="inb">
						  <input type="text" onclick="bb()" readonly style="width: 100%;" name="reservation" id="reservationWeekly" class="fl day-Custom form-control" /> 
					</div>
					<div style="height: -webkit-calc(100% - 40px);height: -moz-calc(100% - 40px);height: calc(100% - 40px);">
						<div class="fl Left_Head" id="test_Head4">
							<table id="fixationWeek" style="border-right: 1px solid #bbb;">
								<!--固定数据-->
							</table>
						</div>
						<div class="fl Right_Head" id="test_Right4">
							<table>
								<thead id="headrWeek">
									<!--数据生成表头-->
								</thead>
							</table>
						</div>
						<div class="fl Right_Body" id="test_Left4" onScroll="moveLeft_Leftfor();">
						    <table id="bydataWeek">
						    	<!--数据生成底部-->
						    </table>
						</div>
					</div>
					</div>
					
	</div>
	</div>
</body>
</html>