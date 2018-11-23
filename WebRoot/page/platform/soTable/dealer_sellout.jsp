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
	<meta charset="UTF-8">
	<title>Grand Total</title>
	<link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
	 
	<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/jquery-ui.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/Dealer Sellout.css"/>
	<style type="text/css">
		.kwdn{
			border-bottom: 1px solid #dedede;
			border-top: 1px solid #dedede;
			background-color: #eeeeee;
		}
		.jsih{
			background-color: #eeeeee;
		}
		.panel-body {
		    padding: 0px; 
		}
		.main {
		    position: absolute;
		    left: 0px;
		    right: 0;
		    top: 0px;
		    bottom: 0px;
		}
	</style>
</head>
<body>
	
	<div id='test_Head1_loadingImport' style='width:5rem; height:5rem;display:none;position: absolute;left: -webkit-calc(50% - 50px);left: -moz-calc(50% - 50px);left: calc(50% - 50px);
	top: -webkit-calc(50% - 30px);top: -moz-calc(50% - 30px);top: calc(50% - 30px);z-index: 9005;'>
		<div>
			<table border='0'> 
				<tr>
					<td><img src='<%=basePath%>images/statement/import.gif' style='width: 100px;height: 100px;z-index: 10000;'/></td>
				</tr> 	 
		
			</table> 
		</div>
	</div>
				
	<div class="parent">
        <!--ä¸»è¦åå®¹-->
        <div class="main">
        	<p class="create">Grand Total</p> 
        	<!-- Tabs -->
			<div id="prizes">
				<div id="tabs-1">
					<div class="">
						<label class="inb" for="">Date：</label>
						<input class="inb"  onchange="getSelectMmodel()" style="width: 150px;"  id="reservationMonthly"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="fl day-Custom form-control"/>
					</div>
					
					<div style="height: -webkit-calc(100% - 40px);height: -moz-calc(100% - 40px);height: calc(100% - 40px);">
						<div class="fl Left_Head" id="test_Head">
							<table id= "fixedInformation" style="border-right: 1px solid #bbb;">
								<!-- 固定信息生成 -->
							</table>
						</div>
						<div class="fl Right_Head" id="test_Right">
							<table>
								<thead id = 'mobileHeadData'>
									<!-- 根据数据生成表头 -->
									<!-- <tr class="graphite">
										2015
										<th>539</th>
										<th>475</th>
										<th>16995</th>
										<th>246029440</th>
										2016
										<th>585</th>
										<th>502</th>
										<th>18960</th>
										<th>295043100</th>
										<th>828</th>
										<th>12782015</th>
										<th>19788</th>
										<th>307829115</th>
										
										<th>390071305</th>
										<th>76%</th>
										<th>100%</th>
										
										<th>14.8%</th>
										<th>19.9%</th>
										
										<th>35</th>
										<th>517957</th>
										
										<th>35</th>
										<th>517957</th>
										
										<th>35</th>
										<th>517957</th>
									</tr> -->
								</thead>
							</table>
						</div>
						<div class="fl Right_Body" id="test_Left" onScroll="moveLeft_Left();">
						    <table id='mobileData'>
						    	<!-- 根据数据生成底部信息 -->
						    </table>
						</div>
					</div>
				</div>
			</div>
        </div>        
	</div>
	
	<script src="<%=basePath%>js/platform/soTable/jquery-1.9.1.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=basePath%>js/platform/soTable/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/common.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/dealer_sellout.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/dealer_sellout_index.js"></script>
</body>
</html>
