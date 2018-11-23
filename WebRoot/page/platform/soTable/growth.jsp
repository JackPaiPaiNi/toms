 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="cn.tcl.common.WebPageUtil"%>
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
	<title><s:text name='permission.labelkey.sogrowth'/></title>
	<script>var my_login_id='<%=party%>';
			var language='<%=language%>';
			var loginCountry='<%=loginCountry%>';
	</script>
		
	   <script type="text/javascript" src="<%=basePath%>js/platform/soTable/TAB_Control/TAB_Country_RoleName.js"></script>
       <script type="text/javascript" src="<%=basePath%>js/platform/soTable/TAB_Control/set_grow_TAB_Country_RoleName.js"></script>
	
	
		<%-- <script src="<%=basePath%>js/platform/soTable/jquery-1.9.1.js" type="text/javascript" charset="utf-8"></script> --%>
	<script src="<%=basePath%>js/platform/soTable/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/year-on-year.js"></script>
		 <%-- <script type="text/javascript" src="<%=basePath%>js/platform/soTable/Grand Total.js"></script>  --%>
		 		<script type="text/javascript" src="<%=basePath%>js/platform/soTable/tableStyleGro.js"></script>
	<link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/commonSO.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/WdatePicker.js"></script>
       <script type="text/javascript" src="<%=basePath%>js/platform/soTable/ach.js"></script>
       <script type="text/javascript" src="<%=basePath%>js/platform/soTable/loadMonthReg.js"></script>
       <script type="text/javascript" src="<%=basePath%>js/platform/soTable/form_the_report_down.js"></script>
       
     <link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/jquery-ui.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/year-on-year.css"/>
	<style type="text/css">
		.kwdn{
			border-bottom: 1px solid #dedede;
			border-top: 1px solid #dedede;
			background-color: #eeeeee;
		}
		.jsih{
			background-color: #eeeeee;
		}
		th,td{
			font-size:12px!important;
		}
		#prizes>div>div>div p {
		    margin-bottom: 2px;
		    text-align: center;
		    font-size: 15px;
		    font-weight: 600;
		}
		.ui-resizable {
		    position: absolute;
		}
	</style>
</head>
<body >
   <div class="main">
        	<p class="create">SO Year-to-year Growth</p> 
        	<!-- Tabs -->
			<div id="prizes" style="margin-top: 15px;">
				<ul class="">
					<li class="Year"><a href="#tabs-1"  onclick="getGroCountry()">Year</a></li>
					<li class="Month" onclick='queryMonthDataByRegi();'><a href="#tabs-2">Month</a></li>
					<li class="Week" onclick='queryWeekDataByRegi();'><a href="#tabs-3">Week</a></li>
				</ul>
				<!--年-->
				<div id="tabs-1" style="width: 100%;padding-top: 0;padding-left: 0;">
					<ul class="fl">
						<li><a href="#tabs_1_4" id='year_regiHead'>Regional Head</a></li>
						<li><a href="#tabs_1_3" onclick="getYearGrowthBySale()" id='year_sale'>Salesman</a></li>
						<li onclick="qeryYearDataByACFO();"><a href="#tabs_1_6" id='year_acfo'>Acfo</a></li>
						<li><a href="#tabs_1_1" id='year_county' onclick="getGroCountry()"></a></li>
					</ul>
				
					<div class="fl" id="tabs_1_6">
						<ul id='year_acfo_head_TAB'>
							<li><a href="#tabs_1_6_1">Total</a></li>
							<li><a href="#tabs_1_6_2">UD</a></li>
							<li><a href="#tabs_1_6_3">X/C/P</a></li>
							<li><a href="#tabs_1_6_4">Smart</a></li>
							<li><a href="#tabs_1_6_5">Big</a></li>
							<li><a href="#tabs_1_6_6">Curved</a></li>
						</ul>
						<div id="tabs_1_6_1">
							<fieldset class="">
									<label for="">Year</label>
									<input readonly  onchange="qeryYearDataByACFO();" style=""  id="selectDateQueryAcfoTotal_year" name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control"/>
								
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryYearDataByACFO()" id="acfo_total_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="ACFOTotalHeadDiv_year">
									<table id='ACFOTotalHead_year' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" style="display:none;" id="ACFOTotalRightDiv_year">
									<table id='ACFOTotalRight_year'>
										
									</table>
								</div>
								<div class="fl Right_Body" style="display:none;" id="ACFOTotalLeftDiv_year" 
								onScroll="moveLeft_LeftAcfo('ACFOTotalLeftDiv_year','ACFOTotalRightDiv_year','ACFOTotalHeadDiv_year');">
								    <table id='ACFOTotalLeft_year'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_1_6_2">
							<fieldset class="">
									<label for="">Year</label>
									<input readonly onchange="qeryYearDataByACFO('UD');" style=""  id="selectDateQueryAcfoUD_year" name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control"/>
								
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryYearDataByACFO('UD');" id="acfo_UD_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>UD Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOUDHeadDiv_year" style="display:none;">
									<table id='ACFOUDHead_year' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOUDRightDiv_year" style="display:none;">
									<table id='ACFOUDRight_year'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOUDLeftDiv_year"  style="display:none;"
								onScroll="moveLeft_LeftAcfo('ACFOUDLeftDiv_year','ACFOUDRightDiv_year','ACFOUDHeadDiv_year');">
								    <table id='ACFOUDLeft_year'>
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_1_6_3">
							<fieldset class="">
									<label for="">Year</label>
									<input readonly style=""  id="selectDateQueryAcfoXCP_year"  name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control"/>
									
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesACFOXcp_year" onchange="SeriesACFOXcp(this,'_year');">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subACFOXcp_year">
									</select>
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryYearDataByACFO('X/C/P');" id="acfo_XCP_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
									<button onclick="qeryYearDataByACFO('X/C/P');">Search</button>
								</fieldset>
							<p>X/C/P Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="ACFOXCPHeadDiv_year">
									<table id='ACFOXCPHead_year' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" style="display:none;" id="ACFOXCPRightDiv_year">
									<table id='ACFOXCPRight_year'>
										
									</table>
								</div>
								<div class="fl Right_Body" style="display:none;" id="ACFOXCPLeftDiv_year" 
								onScroll="moveLeft_LeftAcfo('ACFOXCPLeftDiv_year','ACFOXCPRightDiv_year','ACFOXCPHeadDiv_year');">
								    <table id='ACFOXCPLeft_year'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_1_6_4">
							<fieldset class="">
									<label for="">Year</label>
									<input   onchange="qeryYearDataByACFO('Smart');" style=""  id="selectDateQueryAcfoSmart_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control"/>
								
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryYearDataByACFO('Smart');" id="acfo_Smart_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Smart Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="ACFOSmartHeadDiv_year">
									<table id='ACFOSmartHead_year' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" style="display:none;" id="ACFOSmartRightDiv_year">
									<table id='ACFOSmartRight_year'>
										
									</table>
								</div>
								<div class="fl Right_Body" style="display:none;" id="ACFOSmartLeftDiv_year" 
								onScroll="moveLeft_LeftAcfo('ACFOSmartLeftDiv_year','ACFOSmartRightDiv_year','ACFOSmartHeadDiv_year');">
								    <table id='ACFOSmartLeft_year'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_1_6_5">
							<fieldset class="">
									<label for="">Year</label>
									<input   onchange="qeryYearDataByACFO('Big');" style=""  id="selectDateQueryAcfoBig_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control"/>
								
								<%--<label for="">Size</label>
										
									 <select id='ACFOMonthBigAttr_year' onchange="qeryYearDataByACFO('Big');">
										<option>48</option>
										<option>55</option>
									</select> --%>
									
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryYearDataByACFO('Big')" id="acfo_Big_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								
								</fieldset>
								
							<p>Big Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="ACFOBigHeadDiv_year">
									<table id='ACFOBigHead_year' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" style="display:none;" id="ACFOBigRightDiv_year">
									<table id='ACFOBigRight_year'>
										
									</table>
								</div>
								<div class="fl Right_Body" style="display:none;" id="ACFOBigLeftDiv_year" 
								onScroll="moveLeft_LeftAcfo('ACFOBigLeftDiv_year','ACFOBigRightDiv_year','ACFOBigHeadDiv_year');">
								    <table id='ACFOBigLeft_year'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_1_6_6">
							<fieldset class="">
									<label for="">Year</label>
									<input   onchange="qeryYearDataByACFO('Curved');" style=""  id="selectDateQueryAcfoCurved_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control"/>
								
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryYearDataByACFO('Curved')" id="acfo_Curved_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Curved Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="ACFOCurvedHeadDiv_year">
									<table id='ACFOCurvedHead_year' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" style="display:none;" id="ACFOCurvedRightDiv_year">
									<table id='ACFOCurvedRight_year'>
										
									</table>
								</div>
								<div class="fl Right_Body" style="display:none;" id="ACFOCurvedLeftDiv_year" 
								onScroll="moveLeft_LeftAcfo('ACFOCurvedLeftDiv_year','ACFOCurvedRightDiv_year','ACFOCurvedHeadDiv_year');">
								    <table id='ACFOCurvedLeft_year'>
								    </table>
								</div>
								
							</div>
						</div>
					</div>
					<div class="fl" id="tabs_1_1">
						<ul>
							<li><a href="#tabs_1_1_1" onclick="getGroCountry()">Total</a></li>
							<li><a href="#tabs_1_1_2" onclick="getYearByCountryUD()">UD</a></li>
							<li><a href="#tabs_1_1_3" onclick="getYearByCountryXCP()">X/C/P</a></li>
							<li><a href="#tabs_1_1_4" onclick="getYearByCountrySmart()">Smart</a></li>
							<li><a href="#tabs_1_1_5" onclick="getYearByCountryBig()">Big</a></li>
							<li><a href="#tabs_1_1_6" onclick="getYearByCountryCurved()">Curved</a></li>
						</ul>
						 <div id="tabs_1_1_1">
							<div>
								<fieldset class="inb">
										<label for="" style="margin-left: 0;">Year</label>
										<input readonly onchange="getGroCountry()" id="reservationAnnualGro" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								
									</fieldset>
									<fieldset class="inb">
										<label for="" style="color:#aaa;">Reduction Coefficient</label>
										<input  checked ='checked' onchange="getGroCountry()" id="GroCountryCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
									</fieldset>
							</div>
							
								
							<table class="fl"  id="growthByCountryLeft">
								
							</table>
							<div class="fl" style="overflow: auto;width: -webkit-calc(100% - 100px);width: -moz-calc(100% - 100px);width: calc(100% - 100px);">
								<table id="growthByCountryRight">
									
								
									</tbody>
								</table>
							</div>
						</div> 
						<div id="tabs_1_1_2">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Year</label>
								<input     style="width:150px;display: inline-block;"  id="YearCountryUD"  readonly name="reservatio" type="text"onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="CountryUDModelByYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked' id="YearCountryUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  
								<button onclick="getYearByCountryUD()">Search</button>
								<p class="" id="YearCountryUDText">UD Growth performances </p>
								
							</div>
								
							<table class="fl"  id="UDgrowthByCountryLeft">
								
							</table>
							<div class="fl" style="overflow: auto;width: -webkit-calc(100% - 100px);width: -moz-calc(100% - 100px);width: calc(100% - 100px);">
								<table id="UDgrowthByCountryRight">
									
								
									</tbody>
								</table>
						</div>
						</div>
						<div id="tabs_1_1_3">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Year</label>
								<input  style="width:150px;display: inline-block;"  id="YearCountryXCP"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpCountryYear" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset style="text-indent: 5px;">
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpCountryYear">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="YearCountryXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getYearByCountryXCP()" style="margin-left: 10px;">Search</button>
			                	<p class=""  id="YearCountryXCPText">X/C/P Growth performances</p>
							</div>
							
							<table class="fl"  id="XCPgrowthByCountryLeft">
								
							</table>
							<div class="fl" style="overflow: auto;width: -webkit-calc(100% - 100px);width: -moz-calc(100% - 100px);width: calc(100% - 100px);">
								<table id="XCPgrowthByCountryRight">
									
								
									</tbody>
								</table>
						</div>
						</div>
						<div id="tabs_1_1_4">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Year</label>
								<input    onchange="getYearByCountrySmart()" style="width:150px;display: inline-block;"  id="YearCountrySmart"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getYearByCountrySmart()"  checked ='checked' id="YearCountrySmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="YearCountrySmartText">Smart Growth performances</p>
							</div>
								
							<table class="fl"  id="SmartgrowthByCountryLeft">
								
							</table>
							<div class="fl" style="overflow: auto;width: -webkit-calc(100% - 100px);width: -moz-calc(100% - 100px);width: calc(100% - 100px);">
								<table id="SmartgrowthByCountryRight">
									
								
									</tbody>
								</table>
						</div>
						</div>
						<div id="tabs_1_1_5">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
								<input  onchange="getYearByCountryBig()" style="width:150px;display: inline-block;"  id="YearCountryBig"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" class="day-Custom form-control"/>

								</fieldset>
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="CountryBigSizeByYear"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
							<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="YearCountryBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getYearByCountryBig()">Search</button>
			                	<p class=""  id="YearCountryBigText">Big Growth performances </p>
							</div>
							
							<table class="fl"  id="BiggrowthByCountryLeft">
								
							</table>
							<div class="fl" style="overflow: auto;width: -webkit-calc(100% - 100px);width: -moz-calc(100% - 100px);width: calc(100% - 100px);">
								<table id="BiggrowthByCountryRight">
									
								
									</tbody>
								</table>
						</div>
						</div>
						<div id="tabs_1_1_6">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input   style="width:150px;display: inline-block;"  id="YearCountryCurved"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});"  class="day-Custom form-control"/>
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="CurvedModelCountryYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked' id="YearCountryCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getYearByCountryCurved()">Search</button>
			                	<p class="" id="YearCountryCurvedText">Curved Growth performances </p>
							</div>
								
							<table class="fl"  id="CurvedgrowthByCountryLeft">
								
							</table>
							<div class="fl" style="overflow: auto;width: -webkit-calc(100% - 100px);width: -moz-calc(100% - 100px);width: calc(100% - 100px);">
								<table id="CurvedgrowthByCountryRight">
									
								
									</tbody>
								</table>
						</div>
						</div>
					</div>
					<div class="fl" id="tabs_1_3">
						<ul>
							<li><a href="#tabs_1_3_1" onclick="getYearGrowthBySale()">ALL</a></li>
							<li><a href="#tabs_1_3_2" onclick="getYearBySaleUD()">UD</a></li>
							<li><a href="#tabs_1_3_3" onclick="getYearBySaleXCP()">X/C/P</a></li>
							<li><a href="#tabs_1_3_4" onclick="getYearBySaleSmart()">Smart</a></li>
							<li><a href="#tabs_1_3_5" onclick="getYearBySaleBig()">Big</a></li>
							<li><a href="#tabs_1_3_6" onclick="getYearBySaleCurved()">Curved</a></li>
						</ul>
						<div id="tabs_1_3_1">
							<div style="width: 100%;">
									<fieldset class="inb">
						<label >Year</label>
						<input   onchange="getYearGrowthBySale()" style="width:150px;display: inline-block;" 
						 id="reservationYearlySaleGro"  readonly name="reservatio" type="text"
						 onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});"   class="day-Custom form-control"/>
						</fieldset>
						<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearGrowthBySale()" id="YearGrowthBySaleCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
							<p class="pTitl">Growth performances in every Salesman/Account</p>
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head48" style="display:none;">
										<table style="border-right: 1px solid #bbb;"  id="growthSaleYearLeft">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right48" style="display:none;">
										<table id="growthSaleYearRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left48" onScroll="moveLeft_Left48();" style="display:none;">
								    <table    id="growthSaleYearRight">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_1_3_2">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Year</label>
								<input     style="width:150px;display: inline-block;"  
								id="YearSaleUD"  readonly name="reservatio" type="text"
							onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});"  class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="SaleUDModelByYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked' id="YearSaleUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  
								<button onclick="getYearBySaleUD()">Search</button>
								<p class="" id="YearSaleUDText">UD Growth performances in every Salesman/Account</p>
								
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head49" style="display:none;">
										<table style="border-right: 1px solid #bbb;"  id="YearSaleUDLeft">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right49" style="display:none;">
									<table  id="YearSaleUDRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left49" style="display:none;" onScroll="moveLeft_Left49();">
								   <table id="YearSaleUDRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_1_3_3">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Year</label>
								<input  style="width:150px;display: inline-block;"  id="YearSaleXCP"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});"  class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpSaleYear" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset >
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpSaleYear">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="YearSaleXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getYearBySaleXCP()" style="margin-left: 10px;">Search</button>
			                	<p class=""  id="YearSaleXCPText"></p>
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head50" style="display:none;">
										<table style="border-right: 1px solid #bbb;" id="YearSaleXCPLeft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right50" style="display:none;">
									<table id="YearSaleXCPRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left50" onScroll="moveLeft_Left50();" style="display:none;">
								    <table id="YearSaleXCPRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_1_3_4">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Year</label>
								<input    onchange="getYearBySaleSmart()" style="width:150px;display: inline-block;"  id="YearSaleSmart"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});"  class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getYearBySaleSmart()"  checked ='checked' id="YearSaleSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="YearSaleSmartText">Smart Growth performances in every Salesman/Account</p>
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head51" style="display:none;">
									<table style="border-right: 1px solid #bbb;"  id="YearSaleSmartLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right51" style="display:none;">
										<table  id="YearSaleSmartRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left51" style="display:none;" onScroll="moveLeft_Left51();">
								    <table  id="YearSaleSmartRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_1_3_5">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
								<input  onchange="getYearBySaleBig()" style="width: 150px;display: inline-block;"  id="YearSaleBig"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});"  class="day-Custom form-control"/>

								</fieldset>
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="SaleBigSizeByYear"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
							<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="YearSaleBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
<!-- 								<button  onclick="getYearBySaleBig()">Search</button> -->
			                	<p class=""  id="YearSaleBigText">Big Growth performances in every Salesman/Account</p>
							</div>
							
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head52" style="display:none;">
									<table style="border-right: 1px solid #bbb;"  id="YearSaleBigLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right52" style="display:none;">
									<table id="YearSaleBigRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left52" style="display:none;" onScroll="moveLeft_Left52();">
								   <table  id="YearSaleBigRightData">
								    
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_1_3_6">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input   style="width:150px;display: inline-block;"  id="YearSaleCurved"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});"  class="day-Custom form-control"/>
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="CurvedModelSaleYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked' id="YearSaleCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getYearBySaleCurved()">Search</button>
			                	<p class="" id="YearSaleCurvedText">Curve Growth performances in every Salesman/Account</p>
							</div>
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head53" style="display:none;">
									<table style="border-right: 1px solid #bbb;" id="YearSaleCurvedLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right53" style="display:none;">
										<table id="YearSaleCurvedRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left53" style="display:none;" onScroll="moveLeft_Left53();">
								      <table id="YearSaleCurvedRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
					<div class="fl" id="tabs_1_4">
						<ul id='year_region_head_TAB'>
							<li><a href="#tabs_1_4_1">Total</a></li>
							<li><a href="#tabs_1_4_2">UD</a></li>
							<li><a href="#tabs_1_4_3">X/C/P</a></li>
							<li><a href="#tabs_1_4_4">Smart</a></li>
							<li><a href="#tabs_1_4_5">Big</a></li>
							<li><a href="#tabs_1_4_6">Curved</a></li>
						</ul>
						<div id="tabs_1_4_1">
							<fieldset class="inb">
								<label for="">Year</label>
								<input   onchange="queryYearDataByRegi()" style="width:150px;"  id="queryYearDataByregin_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control inb"/>
								 <label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input  checked ='checked' onchange="queryYearDataByRegi()" id="region_head_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<p>Growth performances in every Region</p>
							
							<table id='leftDataHtml_year' class="fl">
							<!-- 区域年度（left） -->
							</table>
							<div class="fl rightDataHtmlDiv" style="overflow: auto;">
								<table id='rightDataHtml_year'>
								<!-- 区域年度（right） -->
								</table>
							</div>
						</div>
						<div id="tabs_1_4_2">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									<input   onchange="queryYearDataByRegi('UD');" style="width:150px;"  id="queRegHeUDDate_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control inb"/>
		
			
									 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryYearDataByRegi('UD');" id="region_head_UD_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>

								</fieldset>
								<p>UD Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeUDLeft_year'>
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeUDRight_year'>
								</table>
							</div>
						</div>
						<div id="tabs_1_4_3">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									<input style="width:150px;"  id="queRegHeXCPDate_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control inb"/>
								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesXcp_year" onchange="SeriesXcp(this,'_year')">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subXcp_year">
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryYearDataByRegi('X/C/P');" id="region_head_xcp_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="queryYearDataByRegi('X/C/P');">Search</button>
								<p>X/C/P Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeXCPLeft_year'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeXCPRight_year'>
									
								</table>
							</div>
						</div>
						<div id="tabs_1_4_4">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									<input   onchange="queryYearDataByRegi('Smart');" style="width:150px;"  id="queRegHeSmartDate_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control inb"/>
								
									
									 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									 <input  checked ='checked' onchange="queryYearDataByRegi('Smart');" id="region_head_smart_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
									
								</fieldset>
								<p>Smart Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeSmartLeft_year'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeSmartRight_year'>
									
								</table>
							</div>
						</div>
						<div id="tabs_1_4_5">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									
									<input onchange="queryYearDataByRegi('Big');" style="width:150px;"  id="queRegHeBigDate_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control inb"/>
									
								</fieldset>
								<%-- <fieldset class="inb">
									<label for="">Big</label>
										
									<select id='regiHeadBigAttr_year' onchange="queryYearDataByRegi('Big');">
										<option>48</option>
										<option>55</option>
									</select>
									
								</fieldset> --%>
								
								 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryYearDataByRegi('Big');" id="region_head_big_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								<p>Big Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeBigLeft_year'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeBigRight_year'>
								</table>
							</div>
						</div>
						<div id="tabs_1_4_6">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									<input   onchange="queryYearDataByRegi('Curved');" style="width:150px;"  id="queRegHeCurvedDate_year"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="day-Custom form-control inb"/>
		
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryYearDataByRegi('Curved');" id="region_head_Curved_Check_year" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<p>Curved Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeCurvedLeft_year'>
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeCurvedRight_year'>
									
								</table>
							</div>
						</div>
					</div>
				</div>
				<!--月-->
				<div id="tabs-2" style="width: 100%;padding-top: 0;padding-left: 0;">
					<ul class="fl">
						<li><a href="#tabs_2_1" onclick='queryMonthDataByRegi();' id='month_region_head'></a></li>
						<li><a href="#tabs_2_2"  onclick="getMonthGrowthBySale()" id='month_sale'></a></li>
						<li onclick="qeryMonthDataByACFO();"><a href="#tabs_2_3" id='mongth_acfo'></a></li>
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
						<ul id='month_region_head_TAB'>
							<li><a href="#tabs_2_1_1">Total</a></li>
							<li><a href="#tabs_2_1_2">UD</a></li>
							<li><a href="#tabs_2_1_3">X/C/P</a></li>
							<li><a href="#tabs_2_1_4">Smart</a></li>
							<li><a href="#tabs_2_1_5">Big</a></li>
							<li><a href="#tabs_2_1_6">Curved</a></li>
							<!--<li><a href="#tabs_2_1_7">Basic</a></li>
							<li><a href="#tabs_2_1_8">Size</a></li>
							<li><a href="#tabs_2_1_9">结构</a></li>-->
						</ul>
						<div id="tabs_2_1_1">
								<fieldset class="inb">
									<label for="">Month</label>
									<input   onchange="queryMonthDataByRegi()" style="width:150px;"  id="queryYearDataByregin"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control inb"/>
								
									
									 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryMonthDataByRegi()" id="mongth_region_head_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Growth performances in every Region</p>
							
							<table id='leftDataHtml' class="fl">
							<!-- 区域月度（left） -->
							</table>
							<div class="fl rightDataHtmlDiv" style="overflow: auto;">
								<table id='rightDataHtml'>
								<!-- 区域月度（right） -->
								</table>
							</div>
						</div>
						<div id="tabs_2_1_2">
							<div>
								<fieldset class="inb">
									<label for="">Month</label>
									<input   onchange="queryMonthDataByRegi('UD');" style="width:150px;"  id="queMonRegHeUDDate"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control inb"/>
		
			
									 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryMonthDataByRegi('UD');" id="mongth_region_head_UD_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>

								</fieldset>
								<p>UD Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queMonRegHeUDLeft'>
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queMonRegHeUDRight'>
								</table>
							</div>
						</div>
						<div id="tabs_2_1_3">
							<div>
								<fieldset class="inb">
									<label for="">Month</label>
									<input style="width:150px;"  id="queMonRegHeXCPDate"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control inb"/>
								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesXcp" onchange="SeriesXcp(this,'')">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subXcp">
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryMonthDataByRegi('X/C/P');" id="mongth_region_head_xcp_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="queryMonthDataByRegi('X/C/P');">Search</button>
								<p>X/C/P Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queMonRegHeXCPLeft'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queMonRegHeXCPRight'>
									
								</table>
							</div>
						</div>
						<div id="tabs_2_1_4">
							<div>
								<fieldset class="inb">
									<label for="">Month</label>
									<input   onchange="queryMonthDataByRegi('Smart');" style="width:150px;"  id="queMonRegHeSmartDatedd"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control inb"/>
								
									
									 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									 <input  checked ='checked' onchange="queryMonthDataByRegi('Smart');" id="mongth_region_head_smart_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
									
								</fieldset>
								<p>Smart Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queMonRegHeSmartLeft'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queMonRegHeSmartRight'>
									
								</table>
							</div>
						</div>
						<div id="tabs_2_1_5">
							<div>
								<fieldset class="inb">
									<label for="">Month</label>
									
									<input onchange="queryMonthDataByRegi('Big');" style="width:150px;"  id="queMonRegHeBigDate"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control inb"/>
									
								</fieldset>
								<%-- <fieldset class="inb">
									<label for="">Size</label>
										
									<select id='regiHeadMonthBigAttr' onchange="queryMonthDataByRegi('Big');">
										<option>48</option>
										<option>55</option>
									</select>
									
								</fieldset> --%>
								
								 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryMonthDataByRegi('Big');" id="mongth_region_head_big_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								
								
<!-- 								<button>Search</button> -->
								<p>Big Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queMonRegHeBigLeft'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queMonRegHeBigRight'>
								</table>
							</div>
						</div>
						<div id="tabs_2_1_6">
							<div>
								<fieldset class="inb">
									<label for="">Month</label>
									<input   onchange="queryMonthDataByRegi('Curved');" style="width: 150px;"  id="queMonRegHeCurvedDate"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control inb"/>
		
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryMonthDataByRegi('Curved');" id="mongth_region_head_Curved_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<p>Curved Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queMonRegHeCurvedLeft'>
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queMonRegHeCurvedRight'>
									
								</table>
							</div>
						</div>
					</div>
					<!--Saleman-->
					<div class="fl" id="tabs_2_2">
							<ul>
							<li><a href="#tabs_2_2_1" onclick="getMonthGrowthBySale()">ALL</a></li>
							<li><a href="#tabs_2_2_2" onclick="getMonthBySaleUD()">UD</a></li>
							<li><a href="#tabs_2_2_3" onclick="getMonthBySaleXCP()">X/C/P</a></li>
							<li><a href="#tabs_2_2_4" onclick="getMonthBySaleSmart()">Smart</a></li>
							<li><a href="#tabs_2_2_5" onclick="getMonthBySaleBig()">Big</a></li>
							<li><a href="#tabs_2_2_6" onclick="getMonthBySaleCurved()">Curved</a></li>
						</ul>
						<div id="tabs_2_2_1">
						
						<fieldset class="inb">
						<label >Month</label>
						<input   onchange="getMonthGrowthBySale()" style="width:150px;display: inline-block;"  id="reservationMonthlySaleGro"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
						</fieldset>
						<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getMonthGrowthBySale()" id="MonthGrowthBySaleCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
							<p class="pTitl">Growth performances in every Salesman/Account</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head1" style="display:none;">
									<table style="border-right: 1px solid #bbb;"  id="growthSaleMonthLeft">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right1" style="display:none;">
									<table id="growthSaleMonthRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left1" style="display:none;" onScroll="moveLeft_Leftone();">
								    <table id="growthSaleMonthRight">
								    
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_2">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input     style="width:150px;display: inline-block;"  id="monthSaleUD"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="SaleUDModelByMonth">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked' id="monthSaleUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  
								<button onclick="getMonthBySaleUD()">Search</button>
								<p class="" id="monthSaleUDText">UD Growth performances in every Salesman/Account</p>
								
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head5" style="display:none;">
									<table style="border-right: 1px solid #bbb;"  id="monthSaleUDLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right5" style="display:none;">
									<table  id="monthSaleUDRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left5" style="display:none;" onScroll="moveLeft_Left5();">
								       <table id="monthSaleUDRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_3">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input  style="width:150px;display: inline-block;"  id="monthSaleXCP"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpSaleMonth" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpSaleMonth">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="monthSaleXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getMonthBySaleXCP()" style="margin-left: 10px;">Search</button>
			                	<p class=""  id="monthSaleXCPText">X/C/P Growth performances in every Salesman/Account</p>
							</div>
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head6" style="display:none;">
										<table style="border-right: 1px solid #bbb;" id="monthSaleXCPLeft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right6" style="display:none;">
										<table id="monthSaleXCPRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left6" style="display:none;" onScroll="moveLeft_Left6();">
								    <table id="monthSaleXCPRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_4">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input    onchange="getMonthBySaleSmart()" style="width:150px;display: inline-block;"  id="monthSaleSmart"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getMonthBySaleSmart()"  checked ='checked' id="monthSaleSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="monthSaleSmartText">Smart Growth performances in every Salesman/Account</p>
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" style="display:none;" id="test_Head7">
										<table style="border-right: 1px solid #bbb;"  id="monthSaleSmartLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right7" style="display:none;">
										<table  id="monthSaleSmartRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left7" style="display:none;" onScroll="moveLeft_Left7();">
								       <table  id="monthSaleSmartRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_5">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
								<input  onchange="getMonthBySaleBig()" style="width:150px;display: inline-block;"  id="monthSaleBig"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="SaleBigSizeByMonth"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
							<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="monthSaleBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getMonthBySaleBig()">Search</button>
			                	<p class=""  id="monthSaleBigText">Big Growth performances in every Salesman/Account</p>
							</div>
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head8" style="display:none;">
										<table style="border-right: 1px solid #bbb;"  id="monthSaleBigLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right8" style="display:none;">
										<table id="monthSaleBigRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left8" style="display:none;" onScroll="moveLeft_Left8();">
								     <table  id="monthSaleBigRightData">
								    
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_6">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input   style="width:150px;display: inline-block;"  id="monthSaleCurved"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="CurvedModelSaleMonth">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked' id="monthSaleCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getMonthBySaleCurved()">Search</button>
			                	<p class="" id="monthSaleCurvedText">Curved Growth performances in every Salesman/Account</p>
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head9" style="display:none;">
										<table style="border-right: 1px solid #bbb;" id="monthSaleCurvedLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right9" style="display:none;">
										<table id="monthSaleCurvedRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left9" style="display:none;" onScroll="moveLeft_Left9();">
								 <table id="monthSaleCurvedRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
					<!--Acfo-->
					<div class="fl" id="tabs_2_3">
						<ul id='month_acfo_head_TAB'>
							<li><a href="#tabs_2_3_1">Total</a></li>
							<li><a href="#tabs_2_3_2">UD</a></li>
							<li><a href="#tabs_2_3_3">X/C/P</a></li>
							<li><a href="#tabs_2_3_4">Smart</a></li>
							<li><a href="#tabs_2_3_5">Big</a></li>
							<li><a href="#tabs_2_3_6">Curved</a></li>
						</ul>
						<div id="tabs_2_3_1">
							<fieldset class="">
									<label for="">Month</label>
									<input   onchange="qeryMonthDataByACFO();" style=""  id="selectDateQueryAcfoTotal_month"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryMonthDataByACFO()" id="acfo_total_Check_month" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOTotalHeadDiv_month">
									<table id='ACFOTotalHead_month' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOTotalRightDiv_month">
									<table id='ACFOTotalRight_month'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOTotalLeftDiv_month" 
								onScroll="moveLeft_LeftAcfo('ACFOTotalLeftDiv_month','ACFOTotalRightDiv_month','ACFOTotalHeadDiv_month');">
								    <table id='ACFOTotalLeft_month'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_2_3_2">
							<fieldset class="">
									<label for="">Month</label>
									<input   onchange="qeryMonthDataByACFO('UD');" style=""  id="selectDateQueryAcfoUD_month"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryMonthDataByACFO('UD');" id="acfo_UD_Check_month" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>UD Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOUDHeadDiv_month">
									<table id='ACFOUDHead_month' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOUDRightDiv_month">
									<table id='ACFOUDRight_month'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOUDLeftDiv_month" 
								onScroll="moveLeft_LeftAcfo('ACFOUDLeftDiv_month','ACFOUDRightDiv_month','ACFOUDHeadDiv_month');">
								    <table id='ACFOUDLeft_month'>
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_3_3">
							<fieldset>
								<fieldset class="inb">
									<label for="">Month</label>
									<input style=""  id="selectDateQueryAcfoXCP_month"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								</fieldset>
								<fieldset class="inb">	
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesACFOXcp_month" onchange="SeriesACFOXcp(this,'_month');">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subACFOXcp_month">
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryMonthDataByACFO('X/C/P');" id="acfo_XCP_Check_month" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="qeryMonthDataByACFO('X/C/P');">Search</button>
							</fieldset>
							<p>X/C/P Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOXCPHeadDiv_month">
									<table id='ACFOXCPHead_month' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOXCPRightDiv_month">
									<table id='ACFOXCPRight_month'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOXCPLeftDiv_month" 
								onScroll="moveLeft_LeftAcfo('ACFOXCPLeftDiv_month','ACFOXCPRightDiv_month','ACFOXCPHeadDiv_month');">
								    <table id='ACFOXCPLeft_month'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_2_3_4">
							<fieldset class="">
									<label for="">Month</label>
									<input   onchange="qeryMonthDataByACFO('Smart');" style=""  id="selectDateQueryAcfoSmart_month"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryMonthDataByACFO('Smart');" id="acfo_Smart_Check_month" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Smart Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOSmartHeadDiv_month">
									<table id='ACFOSmartHead_month' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOSmartRightDiv_month">
									<table id='ACFOSmartRight_month'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOSmartLeftDiv_month" 
								onScroll="moveLeft_LeftAcfo('ACFOSmartLeftDiv_month','ACFOSmartRightDiv_month','ACFOSmartHeadDiv_month');">
								    <table id='ACFOSmartLeft_month'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_2_3_5">
							<fieldset class="">
									<label for="">Month</label>
									<input   onchange="qeryMonthDataByACFO('Big');" style=""  id="selectDateQueryAcfoBig_month"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								
								<%-- <label for="">Size</label>
										
									<select id='ACFOMonthBigAttr_month' onchange="qeryMonthDataByACFO('Big');">
										<option>48</option>
										<option>55</option>
									</select> --%>
									
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryMonthDataByACFO('Big')" id="acfo_Big_Check_month" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								
								</fieldset>
								
							<p>Big Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOBigHeadDiv_month">
									<table id='ACFOBigHead_month' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOBigRightDiv_month">
									<table id='ACFOBigRight_month'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOBigLeftDiv_month" 
								onScroll="moveLeft_LeftAcfo('ACFOBigLeftDiv_month','ACFOBigRightDiv_month','ACFOBigHeadDiv_month');">
								    <table id='ACFOBigLeft_month'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_2_3_6">
							<fieldset class="">
									<label for="">Month</label>
									<input   onchange="qeryMonthDataByACFO('Curved');" style=""  id="selectDateQueryAcfoCurved_month"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryMonthDataByACFO('Curved')" id="acfo_Curved_Check_month" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Curved Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOCurvedHeadDiv_month">
									<table id='ACFOCurvedHead_month' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOCurvedRightDiv_month">
									<table id='ACFOCurvedRight_month'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOCurvedLeftDiv_month" 
								onScroll="moveLeft_LeftAcfo('ACFOCurvedLeftDiv_month','ACFOCurvedRightDiv_month','ACFOCurvedHeadDiv_month');">
								    <table id='ACFOCurvedLeft_month'>
								    </table>
								</div>
								
							</div>
						</div>
					</div>
				</div>
				<!--周-->
				<div id="tabs-3" style="width: 100%;padding-top: 0;padding-left: 0;">
					<ul class="fl">
						<li onclick="queryWeekDataByRegi();"><a href="#tabs_3_1" id='week_regio_head'></a></li>
						<li><a href="#tabs_3_2" onclick="getWeekGrowthBySale()" id='week_sale'></a></li>
						<li onclick="qeryWeekDataByACFO();"><a href="#tabs_3_3" id='week_acfo'></a></li>
						<li><a href="#tabs_3_4" onclick="getWeekGrowthByCustomer()" >DEALER</a></li>
					</ul>
					<!--Regional Head-->
					<div class="fl" id="tabs_3_1">
						<ul id='chou_region_head_TAB'>
							<li><a href="#tabs_3_1_1">Total</a></li>
							<li><a href="#tabs_3_1_2">UD</a></li>
							<li><a href="#tabs_3_1_3">X/C/P</a></li>
							<li><a href="#tabs_3_1_4">Smart</a></li>
							<li><a href="#tabs_3_1_5">Big</a></li>
							<li><a href="#tabs_3_1_6">Curve</a></li>
						</ul>
						<div id="tabs_3_1_1">
							<fieldset class="inb">
								<label   tcl-text="sample.list.th.datadate" for="">Month</label>
									<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="regionWeekData" class="day-Custom form-control inb" /> 
							
							
								<label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input  checked ='checked' onchange="queryWeekDataByRegi()" id="week_region_head_Check" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Growth performances in every Region</p>
						
							<table class="fl" id="weekRegLeftGro">
								
							</table>
							<div class="fl weekRegRightGroone" style="overflow: auto;">
								<table  id="weekRegRightGro">
									
								</table>
							</div>
						</div>
						<div id="tabs_3_1_2">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									 <input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="queRegHeUDDate_week" class="day-Custom form-control inb" /> 
									 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryChouDataByRegi('UD');" id="region_head_UD_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>

								</fieldset>
								<p>UD Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeUDLeft_week'>
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeUDRight_week'>
								</table>
							</div>
						</div>
						<div id="tabs_3_1_3">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="queRegHeXCPDate_week" class="day-Custom form-control inb" /> 
								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesXcp_week" onchange="SeriesXcp(this,'_week')">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subXcp_week">
									</select>
								</fieldset>
								 <label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input  checked ='checked' onchange="queryChouDataByRegi('X/C/P');" id="mongth_region_head_xcp_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								<button onclick="queryChouDataByRegi('X/C/P');">Search</button>
								<p>X/C/P Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeXCPLeft_week'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeXCPRight_week'>
									
								</table>
							</div>
						</div>
						<div id="tabs_3_1_4">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									 <input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="queRegHeSmartDate_week" class="day-Custom form-control inb" /> 
									 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									 <input  checked ='checked' onchange="queryChouDataByRegi('Smart');" id="region_head_smart_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
									
								</fieldset>
								<p>Smart Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeSmartLeft_week'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeSmartRight_week'>
									
								</table>
							</div>
						</div>
						<div id="tabs_3_1_5">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="queRegHeBigDate_week" class="day-Custom form-control inb" /> 
								</fieldset>
								<%-- <fieldset class="inb">
									<label for="">Big</label>
										
									<select id='regiHeadBigAttr_week' onchange="queryChouDataByRegi('Big');">
										<option>48</option>
										<option>55</option>
									</select>
									
								</fieldset> --%>
								
								 <label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryChouDataByRegi('Big');" id="region_head_big_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								<p>Big Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeBigLeft_week'>
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeBigRight_week'>
								</table>
							</div>
						</div>
						<div id="tabs_3_1_6">
							<div>
								<fieldset class="inb">
									<label for="">Year</label>
									<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="queRegHeCurvedDate_week" class="day-Custom form-control inb" /> 
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="queryChouDataByRegi('Curved');" id="region_head_Curved_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<p>Curved Growth performances in every Regional Head</p>
							</div>
							<table class="fl fixation" id='queRegHeCurvedLeft_week'>
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id='queRegHeCurvedRight_week'>
									
								</table>
							</div>
						</div>
					</div>
					<!--Saleman-->
					<div class="fl" id="tabs_3_2">
						<ul>
							<li><a href="#tabs_3_2_1" onclick="getWeekGrowthBySale()">ALL</a></li>
							<li><a href="#tabs_3_2_2" onclick="getWeekBySaleUD()">UD</a></li>
							<li><a href="#tabs_3_2_3" onclick="getWeekBySaleXCP()">X/C/P</a></li>
							<li><a href="#tabs_3_2_4" onclick="getWeekBySaleSmart()">Smart</a></li>
							<li><a href="#tabs_3_2_5" onclick="getWeekBySaleBig()">Big</a></li>
							<li><a href="#tabs_3_2_6" onclick="getWeekBySaleCurved()">Curved</a></li>
						</ul>
						<div id="tabs_3_2_1">
						<fieldset class="inb">
						<label tcl-text="sample.list.th.datadate"></label>
						<input type="text" onclick="bb(this)" readonly style="width: 170px;display: inline-block;" name="reservation" id="reservationWeeklySaleGro" class="day-Custom form-control" /> 
						
						</fieldset>
						<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekGrowthBySale()" id="GrowthBySaleWeekCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
						
							<!--<p>Growth performances in every Salesman/Account</p>-->
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head2" style="display:none;">
									<table style="border-right: 1px solid #bbb;"  id="growthSaleWeekLeft">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right2" style="display:none;">
									<table id="growthSaleWeekRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left2" style="display:none;" onScroll="moveLeft_Lefttwo();">
								    <table id="growthSaleWeekRight">
								    
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_2_2">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label  tcl-text="sample.list.th.datadate" for="">Week</label>
								<input     style="width: 170px;display: inline-block;"  
								id="WeekSaleUD"  readonly name="reservatio" type="text"
							 onclick="bb(this)"  class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="SaleUDModelByWeek">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked' id="WeekSaleUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  
								<button onclick="getWeekBySaleUD()">Search</button>
								<p class="" id="WeekSaleUDText">UD Growth performances in every Salesman/Account</p>
								
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head21" style="display:none;">
										<table style="border-right: 1px solid #bbb;"  id="WeekSaleUDLeft">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right21" style="display:none;">
									<table  id="WeekSaleUDRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left21" style="display:none;" onScroll="moveLeft_Left21();">
								   <table id="WeekSaleUDRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_2_3">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label   tcl-text="sample.list.th.datadate" for="">Week</label>
								<input  style="width: 170px;display: inline-block;"  id="WeekSaleXCP"  readonly name="reservatio" type="text"  onclick="bb(this)"  class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpSaleWeek" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpSaleWeek">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="WeekSaleXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getWeekBySaleXCP()" style="margin-left: 10px;">Search</button>
			                	<p class=""  id="WeekSaleXCPText"></p>
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head22" style="display:none;">
										<table style="border-right: 1px solid #bbb;" id="WeekSaleXCPLeft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right22" style="display:none;">
									<table id="WeekSaleXCPRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left22" style="display:none;" onScroll="moveLeft_Left22();">
								    <table id="WeekSaleXCPRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_2_4">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label  tcl-text="sample.list.th.datadate" for="">Week</label>
								<input    style="width: 170px;display: inline-block;"  id="WeekSaleSmart"  readonly name="reservatio" type="text" onclick="bb(this)" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getWeekBySaleSmart()"  checked ='checked' id="WeekSaleSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="WeekSaleSmartText">Smart Growth performances in every Salesman/Account</p>
							</div>
								
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head23" style="display:none;">
									<table style="border-right: 1px solid #bbb;"  id="WeekSaleSmartLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right23" style="display:none;">
										<table  id="WeekSaleSmartRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left23" style="display:none;" onScroll="moveLeft_Left23();">
								    <table  id="WeekSaleSmartRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_2_5">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label  tcl-text="sample.list.th.datadate" for="">Week</label>
								<input  onchange="getWeekBySaleBig()" style="width: 170px;display: inline-block;"  id="WeekSaleBig"  readonly name="reservatio" type="text"  onclick="bb(this)" class="day-Custom form-control"/>

								</fieldset>
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="SaleBigSizeByWeek"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
							<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="WeekSaleBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getWeekBySaleBig()">Search</button>
			                	<p class=""  id="WeekSaleBigText">Big Growth performances in every Salesman/Account</p>
							</div>
							
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head24" style="display:none;">
									<table style="border-right: 1px solid #bbb;"  id="WeekSaleBigLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right24" style="display:none;">
									<table id="WeekSaleBigRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left24" style="display:none;" onScroll="moveLeft_Left24();">
								   <table  id="WeekSaleBigRightData">
								    
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_2_6">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label  tcl-text="sample.list.th.datadate" for="">Week</label>
									<input   style="width: 170px;display: inline-block;"  id="WeekSaleCurved"  readonly name="reservatio" type="text" onclick="bb(this)"  class="day-Custom form-control"/>
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="CurvedModelSaleWeek">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input checked ='checked' id="WeekSaleCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getWeekBySaleCurved()">Search</button>
			                	<p class="" id="WeekSaleCurvedText">Curved Growth performances in every Salesman/Account</p>
							</div>
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head25" style="display:none;">
									<table style="border-right: 1px solid #bbb;" id="WeekSaleCurvedLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right25" style="display:none;">
										<table id="WeekSaleCurvedRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left25" style="display:none;" onScroll="moveLeft_Left25();">
								      <table id="WeekSaleCurvedRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
					<!--Acfo-->
					<div class="fl" id="tabs_3_3">
						<ul id='week_acfo_head_TAB'>
							<li><a href="#tabs_3_3_1">Total</a></li>
							<li><a href="#tabs_3_3_2">UD</a></li>
							<li><a href="#tabs_3_3_3">X/C/P</a></li>
							<li><a href="#tabs_3_3_4">Smart</a></li>
							<li><a href="#tabs_3_3_5">Big</a></li>
							<li><a href="#tabs_3_3_6">Curved</a></li>
						</ul>
						<div id="tabs_3_3_1">
							<fieldset class="">
									<label for="">Month</label>
									<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="selectDateQueryAcfoTotal_week" class="day-Custom form-control inb" /> 
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryWeekDataByACFO()" id="acfo_total_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOTotalHeadDiv_week">
									<table id='ACFOTotalHead_week' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOTotalRightDiv_week">
									<table id='ACFOTotalRight_week'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOTotalLeftDiv_week" 
								onScroll="moveLeft_LeftAcfo('ACFOTotalLeftDiv_week','ACFOTotalRightDiv_week','ACFOTotalHeadDiv_week');">
								    <table id='ACFOTotalLeft_week'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_3_3_2">
							<fieldset class="">
									<label for="">Month</label>
								<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="selectDateQueryAcfoUD_week" class="day-Custom form-control inb" /> 
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryWeekDataByACFO('UD');" id="acfo_UD_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>UD Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOUDHeadDiv_week">
									<table id='ACFOUDHead_week' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOUDRightDiv_week">
									<table id='ACFOUDRight_week'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOUDLeftDiv_week" 
								onScroll="moveLeft_LeftAcfo('ACFOUDLeftDiv_week','ACFOUDRightDiv_week','ACFOUDHeadDiv_week');">
								    <table id='ACFOUDLeft_week'>
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_3_3">
							<fieldset>
								<fieldset class="inb">
									<label for="">Month</label>
									<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="selectDateQueryAcfoXCP_week" class="day-Custom form-control inb" /> 
								</fieldset>	
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesACFOXcp_week" onchange="SeriesACFOXcp(this,'_week');">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subACFOXcp_week">
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryWeekDataByACFO('X/C/P');" id="acfo_XCP_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="qeryWeekDataByACFO('X/C/P');">Search</button>
							</fieldset>
							<p>X/C/P Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOXCPHeadDiv_week">
									<table id='ACFOXCPHead_week' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOXCPRightDiv_week">
									<table id='ACFOXCPRight_week'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOXCPLeftDiv_week" 
								onScroll="moveLeft_LeftAcfo('ACFOXCPLeftDiv_week','ACFOXCPRightDiv_week','ACFOXCPHeadDiv_week');">
								    <table id='ACFOXCPLeft_week'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_3_3_4">
							<fieldset class="">
									<label for="">Month</label>
								<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="selectDateQueryAcfoSmart_week" class="day-Custom form-control inb" /> 
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryWeekDataByACFO('Smart');" id="acfo_Smart_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Smart Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOSmartHeadDiv_week">
									<table id='ACFOSmartHead_week' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOSmartRightDiv_week">
									<table id='ACFOSmartRight_week'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOSmartLeftDiv_week" 
								onScroll="moveLeft_LeftAcfo('ACFOSmartLeftDiv_week','ACFOSmartRightDiv_week','ACFOSmartHeadDiv_week');">
								    <table id='ACFOSmartLeft_week'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_3_3_5">
							<fieldset class="">
									<label for="">Month</label>
								<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="selectDateQueryAcfoBig_week" class="day-Custom form-control inb" /> 
								<%-- <label for="">Size</label>
										
									<select id='ACFOMonthBigAttr_week' onchange="qeryWeekDataByACFO('Big');">
										<option>48</option>
										<option>55</option>
									</select> --%>
									
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryWeekDataByACFO('Big')" id="acfo_Big_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								
								</fieldset>
								
							<p>Big Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOBigHeadDiv_week">
									<table id='ACFOBigHead_week' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOBigRightDiv_week">
									<table id='ACFOBigRight_week'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOBigLeftDiv_week" 
								onScroll="moveLeft_LeftAcfo('ACFOBigLeftDiv_week','ACFOBigRightDiv_week','ACFOBigHeadDiv_week');">
								    <table id='ACFOBigLeft_week'>
								    </table>
								</div>
								
							</div>
						</div>
						<div id="tabs_3_3_6">
							<fieldset class="">
									<label for="">Month</label>
									<input type="text" onclick="bb(this)" readonly style="width: 170px;" name="reservation" id="selectDateQueryAcfoCurved_week" class="day-Custom form-control inb" /> 
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="qeryWeekDataByACFO('Curved')" id="acfo_Curved_Check_week" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
	
								</fieldset>
							<p>Curved Growth performances in every Acfo/Area</p>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="ACFOCurvedHeadDiv_week">
									<table id='ACFOCurvedHead_week' style="border-right: 1px solid #bbb;">
										<!--thead不动-->
										<!--tbooy动态生成-->
									</table>
								</div>
								<div class="fl Right_Head" id="ACFOCurvedRightDiv_week">
									<table id='ACFOCurvedRight_week'>
										
									</table>
								</div>
								<div class="fl Right_Body" id="ACFOCurvedLeftDiv_week" 
								onScroll="moveLeft_LeftAcfo('ACFOCurvedLeftDiv_week','ACFOCurvedRightDiv_week','ACFOCurvedHeadDiv_week');">
								    <table id='ACFOCurvedLeft_week'>
								    </table>
								</div>
								
							</div>
						</div>
					</div>
					<!--DEALER-->
					<div class="fl" id="tabs_3_4">
						<ul id='channel_tab_wher'>
							<li><a href="#tabs_3_4_1">Total</a></li>
							<li><a href="#tabs_3_4_2">UD</a></li>
							<li><a href="#tabs_3_4_3">X/C/P</a></li>
							<li><a href="#tabs_3_4_4">Smart</a></li>
							<li><a href="#tabs_3_4_5">Big</a></li>
							<li><a href="#tabs_3_4_6">Curved</a></li>
						</ul>
						<div id="tabs_3_4_1">
							<fieldset class="inb">
							<label tcl-text="sample.list.th.datadate" for="" style="margin-left: 0;">Year</label>
							<input type="text" onclick="bb(this)" readonly style="width: 170px;display: inline-block;" name="reservation" id="reservationWeeklyCustomerGro" class="day-Custom form-control" /> 
							
							<label for="">Region</label>
								<select name="" style="margin-right: 20px;" id="DealerName"  onchange="getWeekGrowthByCustomer()">
									
								</select>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekGrowthByCustomer()" id="GrowthByCustomerCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
						</fieldset>
						
							<!-- 	<p class="inb"></p> -->
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head4">
									<table style="border-right: 1px solid #bbb;"  id="growthCustomerWeekLeft">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right4">
									<table id="growthCustomerWeekRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left4" onScroll="moveLeft_Leftfour();">
								    <table id="growthCustomerWeekRight">
								    	
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_4_2">
							<fieldset class="inb">
							<label tcl-text="sample.list.th.datadate" for="" style="margin-left: 0;">Year</label>
							<input type="text" onclick="bb(this)" readonly style="width: 170px;display: inline-block;" name="reservation" id="reservationWeeklyCustomerGro_UD" class="day-Custom form-control" /> 
							
							<label for="">Region</label>
								<select name="" style="margin-right: 20px;" id="DealerName_UD"  onchange="getWeekGrowthByCustomers('UD');">
									
								</select>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekGrowthByCustomers('UD');" id="GrowthByCustomerCheck_UD" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
						</fieldset>
						
							<!-- 	<p class="inb"></p> -->
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head4_UD">
									<table style="border-right: 1px solid #bbb;"  id="growthCustomerWeekLeft_UD">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right4_UD">
									<table id="growthCustomerWeekRightHead_UD">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left4_UD" onScroll="moveLeft_LeftAcfo('test_Left4_UD','test_Right4_UD','test_Head4_UD');">
								    <table id="growthCustomerWeekRight_UD">
								    	
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_4_3">
							<fieldset class="inb">
							<label tcl-text="sample.list.th.datadate" for="" style="margin-left: 0;">Year</label>
							<input type="text" onclick="bb(this)" readonly style="width: 170px;display: inline-block;" name="reservation" id="reservationWeeklyCustomerGro_XCP" class="day-Custom form-control" /> 
							
							<label for="">Region</label>
								<select name="" style="margin-right: 20px;" id="DealerName_XCP" >
									
								</select>
								
								<div class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesXcpChannelWeek" onchange="SeriesXcpchannel_week(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subXcpChannelWeek">
									</select>
								</div>
								
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekGrowthByCustomers('X/C/P');" id="GrowthByCustomerCheck_XCP" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getWeekGrowthByCustomers('X/C/P');">Search</button>
						</fieldset>
						
						
						<!-- <fieldset class="inb">
									
								</fieldset> -->
						
							<!-- 	<p class="inb"></p> -->
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head4_XCP">
									<table style="border-right: 1px solid #bbb;"  id="growthCustomerWeekLeft_XCP">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right4_XCP">
									<table id="growthCustomerWeekRightHead_XCP">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left4_XCP" onScroll="moveLeft_LeftAcfo('test_Left4_XCP','test_Right4_XCP','test_Head4_XCP');">
								    <table id="growthCustomerWeekRight_XCP">
								    	
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_4_4">
							<fieldset class="inb">
							<label tcl-text="sample.list.th.datadate" for="" style="margin-left: 0;">Year</label>
							<input type="text" onclick="bb(this)" readonly style="width: 170px;display: inline-block;" name="reservation" id="reservationWeeklyCustomerGro_Smart" class="day-Custom form-control" /> 
							
							<label for="">Region</label>
								<select name="" style="margin-right: 20px;" id="DealerName_Smart"  onchange="getWeekGrowthByCustomers('Smart');">
									
								</select>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekGrowthByCustomers('Smart');" id="GrowthByCustomerCheck_Smart" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
						</fieldset>
						
							<!-- 	<p class="inb"></p> -->
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head4_Smart">
									<table style="border-right: 1px solid #bbb;"  id="growthCustomerWeekLeft_Smart">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right4_Smart">
									<table id="growthCustomerWeekRightHead_Smart">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left4_Smart" onScroll="moveLeft_LeftAcfo('test_Left4_Smart','test_Right4_Smart','test_Head4_Smart');">
								    <table id="growthCustomerWeekRight_Smart">
								    	
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_4_5">
							<fieldset class="inb">
							<label tcl-text="sample.list.th.datadate" for="" style="margin-left: 0;">Year</label>
							<input type="text" onclick="bb(this)" readonly style="width: 170px;display: inline-block;" name="reservation" id="reservationWeeklyCustomerGro_Big" class="day-Custom form-control" /> 
							
							<label for="">Region</label>
								<select name="" style="margin-right: 20px;" id="DealerName_Big"  onchange="getWeekGrowthByCustomers('Big');">
									
								</select>
							<%-- <label for="">Size</label>
								<select name="" style="margin-right: 20px;" id="together_Channel_big_week" onchange="getWeekGrowthByCustomers('Big');">
									<option value='48'>48</option>
									<option value='55'>55</option>
								</select> --%>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekGrowthByCustomers('Big');" id="GrowthByCustomerCheck_Big" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
						</fieldset>
						
							<!-- 	<p class="inb"></p> -->
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head4_Big">
									<table style="border-right: 1px solid #bbb;"  id="growthCustomerWeekLeft_Big">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right4_Big">
									<table id="growthCustomerWeekRightHead_Big">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left4_Big" onScroll="moveLeft_LeftAcfo('test_Left4_Big','test_Right4_Big','test_Head4_Big');">
								    <table id="growthCustomerWeekRight_Big">
								    	
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_3_4_6">
							<fieldset class="inb">
							<label tcl-text="sample.list.th.datadate" for="" style="margin-left: 0;">Year</label>
							<input type="text" onclick="bb(this)" readonly style="width: 170px;display: inline-block;" name="reservation" id="reservationWeeklyCustomerGro_Curved" class="day-Custom form-control" /> 
							
							<label for="">Region</label>
								<select name="" style="margin-right: 20px;" id="DealerName_Curved"  onchange="getWeekGrowthByCustomers('Curved');">
									
								</select>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getWeekGrowthByCustomers('Curved');" id="GrowthByCustomerCheck_Curved" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
						</fieldset>
						
							<!-- 	<p class="inb"></p> -->
							
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head4_Curved">
									<table style="border-right: 1px solid #bbb;"  id="growthCustomerWeekLeft_Curved">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right4_Curved">
									<table id="growthCustomerWeekRightHead_Curved">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left4_Curved" onScroll="moveLeft_LeftAcfo('test_Left4_Curved','test_Right4_Curved','test_Head4_Curved');">
								    <table id="growthCustomerWeekRight_Curved">
								    	
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id='loadingImportOne' style='width:5rem; height:5rem;display:none;position: absolute;left: -webkit-calc(50% - 50px);left: -moz-calc(50% - 50px);left: calc(50% - 50px);
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