 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="cn.tcl.common.WebPageUtil"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String party = (String) request.getSession().getAttribute("loginUserId");
	String language = WebPageUtil.getLanguage();
	String loginCountry = WebPageUtil.getLoginedUser().getPartyId();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.GrandTTL'/></title>
	<script>
			var my_login_id='<%=party%>';
			var language='<%=language%>';
			var loginCountry='<%=loginCountry%>';
	</script>
	
  	<!-- 选项卡显示设置  -->
    <script type="text/javascript" src="<%=basePath%>js/platform/soTable/TAB_Control/TAB_Country_RoleName.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/soTable/TAB_Control/set_TAB_Country_RoleName.js"></script>

	<script src="<%=basePath%>js/platform/soTable/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/vsSizeCatgory.js"></script>
	<script src="<%=basePath%>js/platform/soTable/tableStyle.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/jquery-ui.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/vsSizeCatgory.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/ach.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/form_the_report.js"></script>
       <script type="text/javascript" src="<%=basePath%>js/platform/soTable/loadMonthReg.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/commonSO.js"></script>
      
      <link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/WdatePicker.js"></script>
      
      <style type="text/css">
		.kwdn{
			border-bottom: 1px solid #dedede;
			border-top: 1px solid #dedede;
			background-color: #eeeeee;
		}
		.jsih{
			background-color: #eeeeee;
		}
		td,th{
			font-size:12px!important;
		}
		.ui-resizable {
		    position: absolute;
		}
	</style>
</head>
<body >
 <div class="main">
        	<p class="create">SO Ach</p> 
        	<!-- Tabs -->
			<div id="prizes" style="margin-top: 15px;">
				<ul class="">
					<li class="Year"><a href="#tabs-1" onclick="loadYearMenu();">Year</a></li>
					<li class="Month"><a href="#tabs-2" onclick="getMonthByReg()">Month</a></li>
					<li class="Week"><a href="#tabs-3" onclick="getWeekByReg()">Week</a></li>
				</ul>
				<!--年-->
				<div class="" id="tabs-1" style="width: 100%;padding-top: 0;padding-left: 0;">
					<ul class="fl">
					    <li><a href='#tabs_1_1' onclick='getYearByReg()' id='year_reg_head'></a></li> 
						<li><a href='#tabs_1_2' onclick='getYearSale()' id='year_sale'></a></li> 
						<li><a href='#tabs_1_3' onclick='getYearByAcfo()' id='year_acfo'></a></li> 
						<li><a href='#tabs_1_4' onclick='getYearXCP()' id='year_country'></a></li> 
						<li onclick='setYearByCPU();'><a href='#tabs_1_5' id='year_region'></a></li> 
					</ul>
					<div class="fl" id="tabs_1_1">
						<ul>
							<li><a href="#tabs_1_1_1">Total</a></li>
							<!--<li><a href="#tabs_1_1_2">UD</a></li>-->
							<!--<li><a href="#tabs_1_1_3">X/C/P单品</a></li>-->
							<!--<li><a href="#tabs_1_1_4">Smart整体</a></li>-->
							<!--<li><a href="#tabs_1_1_5">Big</a></li>-->
							<!--<li><a href="#tabs_1_1_6">Curve</a></li>-->
							<!--<li><a href="#tabs_1_1_7">Basic</a></li>-->
							<!--<li><a href="#tabs_1_1_8">Size</a></li>-->
							<!--<li><a href="#tabs_1_1_9">结构</a></li>-->
						</ul>
						<div id="tabs_1_1_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for=""  >Year</label>
									<input readonly onchange="getYearByReg()"  id="reservationAnnualReg" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearByReg()" id="regByYearCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
							</div>
							<p class="pTitl" id="regByYearText"></p>
							<div style="height: -webkit-calc(100vh - 280px);height: -moz-calc(100vh - 280px);height: calc(100vh - 280px);overflow-y: auto;">
								<table style="width: 100%;" id="regByYear">
								</table>
							</div>
						</div>
					</div>
					<div class="fl" id="tabs_1_2">
						<ul>
							<li><a href="#tabs_1_2_1">Total</a></li>
							<!--<li><a href="#tabs_1_2_2">UD</a></li>-->
							<!--<li><a href="#tabs_1_2_3">X/C/P单品</a></li>-->
							<!--<li><a href="#tabs_1_2_4">Smart整体</a></li>
							<li><a href="#tabs_1_2_5">Big</a></li>
							<li><a href="#tabs_1_2_6">Curve</a></li>
							<li><a href="#tabs_1_2_7">Basic</a></li>
							<li><a href="#tabs_1_2_8">Size</a></li>
							<li><a href="#tabs_1_2_9">结构</a></li>-->
						</ul>
						<div id="tabs_1_2_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb" >
								<label for="" >Year</label>
									<input readonly onchange="getYearSale()"  id="reservationAnnualSale" name="reservatio" type="text"   style="width:150px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
							onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearSale()" id="saleByYearCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
				                  <p class="pTitl" id="saleByYearText"></p>
							</div>
							<div style="overflow-x: auto;">
								<table  id="saleByYear">
									
									
								</table>
							</div>
						</div>
					</div>
					<div class="fl" id="tabs_1_3">
						<ul>
							<li><a href="#tabs_1_3_1">Total</a></li>
							<!--<li><a href="#tabs_1_3_2">UD</a></li>-->
							<!--<li><a href="#tabs_1_3_3">X/C/P单品</a></li>-->
							<!--<li><a href="#tabs_1_2_4">Smart整体</a></li>
							<li><a href="#tabs_1_2_5">Big</a></li>
							<li><a href="#tabs_1_2_6">Curve</a></li>
							<li><a href="#tabs_1_2_7">Basic</a></li>
							<li><a href="#tabs_1_2_8">Size</a></li>
							<li><a href="#tabs_1_2_9">结构</a></li>-->
						</ul>
						<div id="tabs_1_3_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="" >Year</label>
									<input readonly  onchange="getYearByAcfo()" id="reservationAnnualAcfo" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearByAcfo()" id="acfoByYearCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="pTitl" id="AcfoByYearText"></p>
							</div>
							<div style="overflow-x: auto;">
								<table id="AcfoByYear">
									
									
								</table>
							</div>
						</div>
					</div>
					<div class="fl" id="tabs_1_4">
						<ul>
							<!--<li><a href="#tabs_1_4_1">Total</a></li>
							<li><a href="#tabs_1_4_2">UD</a></li>-->
							<li><a href="#tabs_1_4_3">X/C/P</a></li>
							<!--<li><a href="#tabs_1_4_4">Smart整体</a></li>-->
							<li onclick = 'queryYearByBig();'><a href="#tabs_1_4_5">Big</a></li>
							<!--<li><a href="#tabs_1_4_6">Curve</a></li>-->
							<!--<li><a href="#tabs_1_4_7">Basic</a></li>-->
							<!--<li><a href="#tabs_1_4_8">Size</a></li>-->
							<!--<li><a href="#tabs_1_4_9">结构</a></li>-->
						</ul>
						<div id="tabs_1_4_3" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label  for="" style="margin-left: 0;">Year</label>
									<input readonly  id="reservationAnnualXCP" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="xcp" onchange="getSubXcp()">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for="">Series</label>
									<select  id="subXcp">
										
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked'  id="countryXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getYearXCP()" style="background: #007fff;border: 1px solid #ddd!important;padding: 0 4px;border-radius: 4px;color: #fff;">Search</button>
			                	<p class="pTitl" id="yearXCPText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="test_Head4">
									<table style="border-right: 1px solid #bbb;" id="yearXCPLeft">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" style="display:none;" id="test_Right4">
									<table id="yearXCPRightHead" >
										
									</table>
								</div>
								<div  class="fl Right_Body" style="display:none;" id="test_Left4" onScroll="moveLeft_Leftfou();">
								    <table id="yearXCPRight"> 
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_1_4_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
										<input readonly onchange="queryYearByBig()"  id="YearByBigSelectDate" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								</fieldset>
								<fieldset class="inb">
								 <label for="" style="color:#aaa;">Reduction Coefficient</label>
										<input  checked ='checked' onchange="queryYearByBig()" id="year_coun_big" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
			
								</fieldset>
								
								<p class="pTitl">Big Size Sell-out Performance</p>
							</div>
							<div style="overflow: auto;height: -webkit-calc(100vh - 255px);height: -moz-calc(100vh - 255px);height: calc(100vh - 255px);">
								<table id='bigYearInfo' style="width: 100%;">
									<!-- <thead>
										<tr>
											<th>SIZE</th>
											<th>TV TYPE</th>
											<th>MODEL</th>
											<th>May</th>
											<th>June</th>
											<th>July</th>
											<th>Aug</th>
											<th>Sep</th>
											<th>TTL</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>49</td>
											<td>FLAT UHD TV</td>
											<td>49C2US</td>
											<td>12</td>
											<td>14</td>
											<td>23</td>
											<td>123</td>
											<td>56</td>
											<td>123</td>
										</tr>
										<tr>
											<td>55</td>
											<td>FLAT UHD TV</td>
											<td>55C2US</td>
											<td>12</td>
											<td>14</td>
											<td>23</td>
											<td>123</td>
											<td>56</td>
											<td>123</td>
										</tr>
										<tr>
											<td>75</td>
											<td>FLAT UHD TV</td>
											<td>75C2US</td>
											<td>12</td>
											<td>14</td>
											<td>23</td>
											<td>123</td>
											<td>56</td>
											<td>123</td>
										</tr>
										<tr>
											<td colspan="3">TOTAL</td>
											<td>12</td>
											<td>14</td>
											<td>23</td>
											<td>123</td>
											<td>56</td>
											<td>123</td>
										</tr>
										<tr class="bluegrey">
											<td colspan="3">TTL SELLOUT/MOS.</td>
											<td>1223</td>
											<td>1434</td>
											<td>2334</td>
											<td>1233</td>
											<td>5656</td>
											<td></td>
										</tr>
										<tr>
											<td colspan="3">RATIO of C2</td>
											<td>1.2%</td>
											<td>1.4%</td>
											<td>2.3%</td>
											<td>1.23%</td>
											<td>5.6%</td>
											<td></td>
										</tr>
									</tbody> -->
								</table>
							</div>
						</div>
					</div>
					<div class="fl" id="tabs_1_5">
						<ul>
							<!--<li><a href="#tabs_1_5_1">Total</a></li>
							<li><a href="#tabs_1_5_2">UD</a></li>-->
							<!--<li><a href="#tabs_1_5_3">X/C/P单品</a></li>-->
							<!--<li><a href="#tabs_1_5_4">Smart整体</a></li>-->
							<li><a href="#tabs_1_5_3">X/C/P</a></li>
							<!--<li><a href="#tabs_1_5_6">Curve</a></li>-->
							<!--<li><a href="#tabs_1_5_7">Basic</a></li>-->
							<!--<li><a href="#tabs_1_5_8">Size</a></li>-->
							<!--<li><a href="#tabs_1_5_9">结构</a></li>-->
						</ul>
						<div id="tabs_1_5_3" class="clear">
							<div style="width: 100%;">
								<%-- <fieldset class="inb">
									<label for="">Year</label>
									<select name="">
										<option value=""></option>
									</select>
								</fieldset> --%>
								<fieldset class="inb">
									<label for="">Year</label>
										<input readonly  id="reservationAnnualCpu" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
										onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
										
								</fieldset>
				                <label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
				                <select id='xcpsYear' onchange='getSubXcpYear();'>
									<option value=''>All</option>
									<option value='X'>X</option>
									<option value='C'>C</option>
									<option value='P'>P</option>
								</select>
								
								<select id='subYearXcpWeek'>
								</select>
				                <label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input  checked ='checked' onchange="setYearByCPU();" id="year_region_xpc_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								<button onclick='setYearByCPU();' style="background: #007fff;border: 1px solid #ddd!important;padding: 0 4px;border-radius: 4px;color: #fff;">Search</button>
				                <p id="year_Region" class="pTitl"></p>
							</div>
							<div style="overflow-x: auto;">
								<table id='yearByCPU'>
									
								</table>
							</div>
						</div>
					</div>
				</div>
				<!--月-->
				<div id="tabs-2" style="width: 100%;padding-top: 0;padding-left: 0;">
					<ul  class="fl">
						<li><a href='#tabs_2_1' onclick='getMonthByReg()' id='month_regin_head'></a></li>
						<li onclick='loadSalesmanMonthQuarterly()'><a href='#tabs_2_2' id='month_sale'></a></li>
						<li><a href='#tabs_2_3'  onclick='getMonthByAcfo()' id="month_acfo"></a></li>
						<!--<li><a href="#tabs_2_1">Country</a></li>-->
						<!--<li><a href="#tabs_2_2">Area</a></li>-->
						<!--<li><a href="#tabs_2_3">客户</a></li>-->
						<!--<li><a href="#tabs_2_4">门店</a></li>
						<li><a href="#tabs_2_5">业务人员</a></li>
						<li><a href="#tabs_2_6">督导</a></li>
						<li><a href="#tabs_2_7">促销员</a></li>-->
					</ul>
					<!--Regional Head-->
					<div class="fl" id="tabs_2_1">
						<ul>
							<li><a href="#tabs_2_1_1">Total</a></li>
							<!-- <li><a href="#tabs_2_1_2">UD</a></li>
							<li><a href="#tabs_2_1_3">X/C/P</a></li>
							<li><a href="#tabs_2_1_4">Smart</a></li>
							<li><a href="#tabs_2_1_5">Big</a></li>
							<li><a href="#tabs_2_1_6">Curved</a></li> -->
							<!--<li><a href="#tabs_2_1_7">Basic</a></li>-->
							<!--<li><a href="#tabs_2_1_8">Size</a></li>-->
							<!--<li><a href="#tabs_2_1_9">结构</a></li>-->
						</ul>
						<div id="tabs_2_1_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label  for="">Month</label>
									<input   onchange="getMonthByReg()" style="width:150px;display: inline-block;"  id="reservationMonthlyReg"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getMonthByReg()" id="regByMonthCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="pTitl" id="monthRegText"></p>
							</div>
							<table class="fl float_left" id="monthRegLeft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="monthRegRight">
									
								</table>
							</div>
						</div>
					</div>
					<!--Saleman-->
					<div class="fl" id="tabs_2_2">
						<ul>
							<li><a href="#tabs_2_2_1">Total</a></li>
							<!--<li><a href="#tabs_2_2_2">UD</a></li>
							<li><a href="#tabs_2_2_3">X/C/P单品</a></li>
							<li><a href="#tabs_2_2_4">Smart整体</a></li>
							<li><a href="#tabs_2_2_5">Big</a></li>
							<li><a href="#tabs_2_2_6">Curve</a></li>
							<li><a href="#tabs_2_2_7">Basic</a></li>
							<li><a href="#tabs_2_2_8">Size</a></li>
							<li><a href="#tabs_2_2_9">结构</a></li>-->
						</ul>
						<div id="tabs_2_2_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
									<input onchange="loadSalesmanMonthQuarterly()"   style="width:150px"  id="salesmanMonthQuarterlyLeftDate"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control inb"/>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="loadSalesmanMonthQuarterly()" id="month_sale_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                <p class="pTitl" id='month_sale_title'></p>
							</div>
							<div style="overflow: auto;">
								
							
								<div class="fl Left_Head" id="test_Head1">
									<table style="border-right: 1px solid #bbb;"  id="salesmanMonthQuarterlyLeftData">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right1">
									
								</div>
								<div class="fl Right_Body" id="test_Left1" onScroll="moveLeft_Leftone();">
								    <table id="salesmanMonthQuarterlyRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
					<!--Acfo-->
					<div class="fl" id="tabs_2_3">
						<ul>
							<li><a href="#tabs_2_3_1">Total</a></li>
							<!-- <li><a href="#tabs_2_3_2">UD</a></li> -->
							<!--<li><a href="#tabs_2_3_3">X/C/P单品</a></li>
							<li><a href="#tabs_2_3_4">Smart整体</a></li>
							<li><a href="#tabs_2_3_5">Big</a></li>
							<li><a href="#tabs_2_3_6">Curve</a></li>
							<li><a href="#tabs_2_3_7">Basic</a></li>
							<li><a href="#tabs_2_3_8">Size</a></li>
							<li><a href="#tabs_2_3_9">结构</a></li>-->
						</ul>
						<div id="tabs_2_3_1" class="clear">
							<div style="width: 100%;">
								<fieldset  class="inb">
									<label for="">Month</label>
									<input   onchange="getMonthByAcfo()" style="width:150px;display: inline-block;"  id="reservationMonthlyAcfo"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getMonthByAcfo()" id="acfoByMonthCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                    <p class="pTitl" id="monthAcfoText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="test_Head">
									<table style="border-right: 1px solid #bbb;" id="monthAcfoLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" style="display:none;" id="test_Right">
									<table id="monthAcfoRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" style="display:none;" id="test_Left" onScroll="moveLeft_Left();">
								    <table id="monthAcfoRight">
								  
								    </table>
								</div>
							</div>
						</div>
						<%-- <div id="tabs_2_3_2">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
									<select name="">
										<option value=""></option>
									</select>
								</fieldset>
				                  <p class="pTitl">CPU sellout results in August 2017</p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head5">
									<table style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<thead>
											<tr>
												<th rowspan="2">RANK</th>
												<th rowspan="2">Acfo</th>
												<th rowspan="2">AREA</th>
											</tr>
											<tr></tr>
										</thead>
										<!--tbooy动态生成-->
										<tbody>
											<tr>
												<td>1</td>
												<td>Ruel Buentiempo</td>
												<td>MLA-Soutd</td>
											</tr>
											<tr>
												<td>2</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>3</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>4</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>5</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>6</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>7</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>8</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>9</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>10</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>11</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>12</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>13</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>14</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td>15</td>
												<td>Mr. Philip Xia</td>
												<td>MIN</td>
											</tr>
											<tr>
												<td></td>
												<td>Exhibit</td>
												<td></td>
											</tr>
											<tr>
												<td></td>
												<td>TOTAL</td>
												<td></td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right5">
									<table>
										<thead>
											<tr>
												<th class="lightcyan" rowspan="2">C1/C2 TTL Sellout</th>
												<th rowspan="2">TARGET</th>
												<th rowspan="2">ACH</th>
												<th class="" colspan="2" style="width: 200px;">P1/P3 sellout</th>
												<th class="lightcyan" rowspan="2">P1/P3 TTL Sellout</th>
												<th class="" rowspan="2">TARGET</th>
												<th class="" rowspan="2">ACH</th>
												<th class="lightcyan" rowspan="2">UHD TTL Sellout</th>
												<th class="" rowspan="2">TARGET</th>
												<th class="" rowspan="2">ACH</th>
												<th class="lightcyan" rowspan="2">TTL Sellout</th>
												<th class="" rowspan="2">TTL Target</th>
												<th class="" rowspan="2">ACH</th>
											</tr>
											<tr>
												<th class="">C48P1FS</th>
												<th class="">C49P3FS</th>
											</tr>
										</thead>
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left5" onScroll="moveLeft_Leftfive();">
								    <table>
								    	<tbody>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="red">78%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
											<tr>
												<td>0</td>
												<td>5</td>
												<td>0%</td>
												<td>4</td>
												<td>1</td>
												<td>5</td>
												<td>6</td>
												<td class="yellow">89%</td>
												<td>42</td>
												<td>23</td>
												<td class="green">185%</td>
												<td>47</td>
												<td>33</td>
												<td class="green">141%</td>
											</tr>
										</tbody>
								    </table>
								</div>
							</div>
						</div> --%>
					</div>
				</div>
				<!--周-->
				<div id="tabs-3"  style="width: 100%;padding-top: 0;padding-left: 0;">
					<ul class="fl">
					    <li><a href='#tabs_3_1'  onclick='getWeekByReg()' id='week_region_head'></a></li>
						<li onclick='queryWeekBySale();'><a href='#tabs_3_2' id='week_sale'></a></li>
						<li><a href='#tabs_3_3' onclick='getWeekByAcfo()' id='week_acfo'></a></li>
						<li onclick='querySeriesOfSales();'><a href='#tabs_3_4' id='week_region'></a></li>
					</ul>
					<!--Regional Head-->
					<div class="fl" id="tabs_3_1">
						<ul>
							<li><a href="#tabs_3_1_1">Total</a></li>
							
						</ul>
						<div id="tabs_3_1_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label tcl-text="sample.list.th.datadate"></label>
									<input type="text" onclick="bb(this)" readonly style="width:150px;display: inline-block;" name="reservation" id="reservationWeeklyReg" class="day-Custom form-control" /> 
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekByReg()" id="regByWeekCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                <p class="pTitl" id="weekRegText"></p>
							</div>
							<table class="fl float_left" id="weekRegLeft">
								
							</table>
							<div class="fl WRH" style="overflow: auto;">
								<table id="weekRegRight">
								
								</table>
							</div>
						</div>
					</div>
					<!--Saleman-->
					<div class="fl" id="tabs_3_2">
						<ul>
							<li><a href="#tabs_3_2_1">Total</a></li>
						</ul>
						<div id="tabs_3_2_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label tcl-text="sample.list.th.datadate" >Week：</label>
									<input type="text" onclick="bb(this)" readonly style="width:150px;display: inline-block;" name="reservation" id="reservationWeeklySale" class="day-Custom form-control" /> 
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryWeekBySale()" id="week_sale_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                <p id='week_sale_title' class="pTitl"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head2">
									<table id='salesmanWeekQuarterlyLeftData' style="border-right: 1px solid #bbb;" >
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right2">
									
								</div>
								<div class="fl Right_Body" id="test_Left2" onScroll="moveLeft_Lefttwo();">
								    <table id="salesmanWeekQuarterlyRightData">
								     
								    </table>
								</div>
							</div>
						</div>
					</div>
					<!--Acfo-->
					<div class="fl" id="tabs_3_3">
						<ul>
							<li><a href="#tabs_3_3_1">Total</a></li>
						</ul>
						<div id="tabs_3_3_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label tcl-text="sample.list.th.datadate"></label>
									<input type="text" onclick="bb(this)" readonly style="width:150px;display: inline-block;" name="reservation" id="reservationWeeklyAcfo" class="day-Custom form-control" /> 
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekByAcfo()" id="acfoByWeekCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="pTitl" id="weekAcfoText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="test_Head3">
									<table style="border-right: 1px solid #bbb;"  id="weekAcfoLeft">
										<!--thead不动-->
											
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right3" style="display:none;">
									<table id="weekAcfoRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left3" style="display:none;" onScroll="moveLeft_Leftthir();">
								    <table  id="weekAcfoRight">
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
					<div class="fl" id="tabs_3_4">
						<ul>
							<!--<li><a href="#tabs_3_4_1">Total</a></li>
							<li><a href="#tabs_3_4_2">UD</a></li>-->
							<li><a href="#tabs_3_4_3">X/C/P</a></li>
							
						</ul>
						<div id="tabs_3_4_3" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label  tcl-text="sample.list.th.datadate" style="margin-left: 0;">Week：</label>
									<input type="text" onclick="bb(this)" readonly style="width:150px;" name="reservation" id="WeekByCP" class="day-Custom form-control inb" /> 
								</fieldset>
								<fieldset class="inb">
									<label style="margin-left: 0;">X/C/P Series</label>
									<select id='xcps' onchange='getSubXcpWeek();'>
										<option value=''>All</option>
										<option value='X'>X</option>
										<option value='C'>C</option>
										<option value='P'>P</option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label style="margin-left: 0;">Sub-series</label>
									<select id='subXcpWeek'>
									</select>
								</fieldset>
								<fieldset class="inb">
				                    <label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="querySeriesOfSales()" id="week_xcp_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick='querySeriesOfSales();' style="background: #007fff;border: 1px solid #ddd!important;padding: 0 4px;border-radius: 4px;color: #fff;">Search</button>
				                  <p id='week_series_title' class="pTitl"></p>
							</div>
							
							<div style="overflow-x: auto;">
								<table id='WeekByCPU'>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
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
        </div>        
</body>
</html>