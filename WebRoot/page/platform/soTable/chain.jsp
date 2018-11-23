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
	<title><s:text name='permission.labelkey.sohb'/></title>
	<script>var my_login_id='<%=party%>';
			var language='<%=language%>';
			var loginCountry='<%=loginCountry%>';
	</script>
	<script src="<%=basePath%>js/platform/soTable/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	
	 <script type="text/javascript" src="<%=basePath%>js/platform/soTable/TAB_Control/TAB_Country_RoleName.js"></script>
     <script type="text/javascript" src="<%=basePath%>js/platform/soTable/TAB_Control/set_chain_TAB_Country_RoleName.js"></script>
	

      
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/monthONmonth.js"></script>
	<script src="<%=basePath%>js/platform/soTable/tableStyles.js" type="text/javascript" charset="utf-8"></script>
	
	
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/chain.js"></script>
	<script src="<%=basePath%>js/platform/soTable/loadDataByChain.js" type="text/javascript" charset="utf-8"></script>
		<script src="<%=basePath%>js/platform/soTable/commonSO.js" type="text/javascript" charset="utf-8"></script>
	
	
	
       <link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/soTable/WdatePicker.js"></script>
     
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/jquery-ui.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/monthONmonth.css"/>
  
	
	<style type="text/css">
		.kwdn{
			border-bottom: 1px solid #dedede;
			border-top: 1px solid #dedede;
			background-color: #eeeeee;
		}
		.jsih{
			background-color: #eeeeee;
		}
		.ui-resizable {
		    position: absolute;
		}
	</style>
</head>
<body >
 <div class="main">
        	<p class="create">SO Month-to-month Growth</p> 
        	<!-- Tabs -->
			<div id="prizes" style="margin-top: 15px;">
				<ul class="">
					<li class="Year"><a href="#tabs-1" onclick="getYearCountryTotal()">Year</a></li>
					<li class="Month"><a href="#tabs-2" onclick="getMonthByCountryTotal()">Month</a></li>
				</ul>
				<!--年-->
				<div id="tabs-1" style="width: 100%;padding-top: 0;padding-left: 0;">
					<ul class="fl">
						<li><a href="#tabs_1_4" onclick="getYearCountryTotal()" id='year_county'></a></li>
						<li><a href="#tabs_1_1" onclick="getYearRegTotal()" id='year_regiHead'></a></li>
						<li><a href="#tabs_1_2" onclick="getYearSaleTotal()" id='year_sale'></a></li>
						<li><a href="#tabs_1_3" onclick="getYearAcfoTotal()" id='year_acfo'></a></li>
					</ul>
					<!--Regional Head-->
					<div class="fl" id="tabs_1_1">
						<ul>
							<li><a href="#tabs_1_1_1" onclick="getYearRegTotal()">ALL</a></li>
							<li><a href="#tabs_1_1_2" onclick="getYearRegUD()" >UD</a></li>
							<li><a href="#tabs_1_1_3" onclick="getYearRegXCP()">X/C/P</a></li>
							<li><a href="#tabs_1_1_4" onclick="getYearRegSmart()">Smart</a></li>
							<li><a href="#tabs_1_1_5" onclick="getYearRegBig()">Big</a></li>
							<li><a href="#tabs_1_1_6" onclick="getYearRegCurved()">Curved</a></li>
						</ul>
						<div id="tabs_1_1_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
								<input readonly  onchange="getYearRegTotal()" id="YearChainReg" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearRegTotal()" id="YearChainRegCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class=""  id="YearChainRegText"></p>
							</div>
							<table class="fl float_left" id="RegTotalByYearleft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="RegTotalByYearRight">
									
									
								</table>
							</div>
						
						</div>
						<!--UD-->
						<div id="tabs_1_1_2" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input readonly  id="YearUDReg" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select name="" id="RegUDModelByYear">
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' id="YearUDRegCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getYearRegUD()">Search</button>
				                <p class="" id="YearUDRegText"></p>
							</div>
							<table class="fl float_left"  id="RegUDByYearleft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="RegUDByYearRight">
									
								</table>
							</div>
						
						</div>
						<!--XCP单品-->
						<div id="tabs_1_1_3" class="clear">
							<div style="width: 100%;">
									<fieldset class="inb">
									<label  for="" style="margin-left: 0;">Year</label>
									<input readonly  id="YearXCPReg" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpRegYear" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpRegYear">
										
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input checked ='checked' id="YearXCPRegCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getYearRegXCP()">Search</button>
			                	<p  id="YearXCPRegText"></p>
							</div>
							<table class="fl float_left"  id="RegXCPByYearleft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="RegXCPByYearRight">
									
								</table>
							</div>
						
						</div>
						<!--Smart-->
						<div id="tabs_1_1_4" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
										<input readonly onchange="getYearRegSmart()"  id="YearSmartReg" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								</fieldset>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input onchange="getYearRegSmart()" checked ='checked' id="YearXCPRegCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
				                <p class=""  id="YearSmartRegText" ></p>
							</div>
							<table class="fl float_left"  id="RegSmartByYearleft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table  id="RegSmartByYearRight">
									
								</table>
							</div>
						
						</div>
						<!--Big-->
						<div id="tabs_1_1_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input  readonly  onchange="getYearRegBig()"  id="YearBigReg" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="RegBigSize"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getYearRegBig()"  checked ='checked' id="YearBigRegCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<!-- <button onclick="getYearRegBig()">Search</button> -->
				                <p class="" id="YearBigRegText" ></p>
							</div>
							<table class="fl float_left" id="RegBigByYearleft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table  id="RegBigByYearRight">
									
								</table>
							</div>
						
						</div>
						<!--Curve-->
						<div id="tabs_1_1_6" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for=""  >Year</label>
								<input readonly id="YearCurvedReg" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select name=""  id="CurvedModelRegYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' id="YearCurvedRegCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getYearRegCurved()">Search</button>
			                	<p class=""  id="YearCurvedRegText"></p>
							</div>
							<table class="fl float_left" id="RegCurvedByYearleft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="RegCurvedByYearRight">
								
								</table>
							</div>
						
						</div>
					</div>
					<!--Salesman-->
					<div class="fl" id="tabs_1_2">
						<ul>
							<li><a href="#tabs_1_2_1" onclick="getYearSaleTotal()">ALL</a></li>
							<li><a href="#tabs_1_2_2" onclick="getYearSaleUD()">UD</a></li>
							<li><a href="#tabs_1_2_3" onclick="getYearSaleXCP()">X/C/P</a></li>
							<li><a href="#tabs_1_2_4"  onclick="getYearSaleSmart()">Smart</a></li>
							<li><a href="#tabs_1_2_5" onclick="getYearSaleBig()">Big</a></li>
							<li><a href="#tabs_1_2_6" onclick="getYearSaleCurved()">Curve</a></li>
						</ul>
						<!--ALL-->
						<div id="tabs_1_2_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Year</label>
								<input readonly  onchange="getYearSaleTotal()" id="YearChainSale" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked'  onchange="getYearSaleTotal()" id="YearChainSaleCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                <p class="" id="YearChainSaleText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head1">
									<table style="border-right: 1px solid #bbb;" id="YearChainSaleLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right1">
									<table id="YearChainSaleRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left1" onScroll="moveLeft_Leftone();">
								    <table id="YearChainSaleRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<!--UD-->
						<div id="tabs_1_2_2" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for=""  >Year</label>
								<input readonly id="YearUDSale" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select name=""  id="SaleUDModelByYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' id="YearUDSaleCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getYearSaleUD()">Search</button>
			                	<p class=""  id="YearUDSaleText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head2">
									<table style="border-right: 1px solid #bbb;" id="SaleUDByYearleft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right2">
									<table id="SaleUDByYearRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left2" onScroll="moveLeft_Lefttwo();">
								    <table id="SaleUDByYearRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<!--XCP单品-->
						<div id="tabs_1_2_3" class="clear">
							<div style="width: 100%;">
									<fieldset class="inb">
									<label  for="" style="margin-left: 0;">Year</label>
									<input readonly id="YearXCPSale" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpSaleYear" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpSaleYear">
										
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input checked ='checked' id="YearXCPSaleCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getYearSaleXCP()">Search</button>
			                	<p  id="YearXCPSaleText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head3">
									<table style="border-right: 1px solid #bbb;" id="SaleXCPByYearleft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right3">
									<table  id="SaleXCPByYearRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left3" onScroll="moveLeft_Leftthir();">
								     <table  id="SaleXCPByYearRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<!--Smart-->
						<div id="tabs_1_2_4" class="clear">
								<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
										<input readonly onchange="getYearSaleSmart()"  id="YearSmartSale" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								</fieldset>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input onchange="getYearSaleSmart()" checked ='checked' id="YearXCPSaleCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
				                <p class=""  id="YearSmartSaleText" ></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head4">
									<table style="border-right: 1px solid #bbb;" id="SaleSmartByYearleft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right4">
									<table  id="SaleSmartByYearRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left4" onScroll="moveLeft_Leftfou();">
								    <table  id="SaleSmartByYearRightData">
								    	 
								    </table>
								</div>
							</div>
						</div>
						<!--Big-->
						<div id="tabs_1_2_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input readonly onchange="getYearSaleBig()"  id="YearBigSale" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="SaleBigSize"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getYearSaleBig()" checked ='checked' id="YearBigSaleCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<!-- <button onclick="getYearSaleBig()">Search</button> -->
				                <p class="" id="YearBigSaleText" ></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head5">
									<table style="border-right: 1px solid #bbb;" id="SaleBigByYearleft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right5">
									<table id="SaleBigByYearRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left5" onScroll="moveLeft_Leftfive();">
								    <table id="SaleBigByYearRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<!--Curve-->
						<div id="tabs_1_2_6" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for=""  >Year</label>
								<input readonly  id="YearCurvedSale" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select name=""  id="CurvedModelSaleYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' id="YearCurvedSaleCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getYearSaleCurved()">Search</button>
			                	<p class=""  id="YearCurvedSaleText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head6">
									<table style="border-right: 1px solid #bbb;" id="SaleCurvedByYearleft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right6">
									<table id="SaleCurvedByYearRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left6" onScroll="moveLeft_Leftsix();">
								    <table id="SaleCurvedByYearRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
					<!--Acfo-->
					<div class="fl" id="tabs_1_3">
						<ul>
							<li><a href="#tabs_1_3_1" onclick="getYearAcfoTotal()">ALL</a></li>
							<li><a href="#tabs_1_3_2" onclick="getYearAcfoUD()">UD</a></li>
							<li><a href="#tabs_1_3_3" onclick="getYearAcfoXCP()">X/C/P</a></li>
							<li><a href="#tabs_1_3_4" onclick="getYearAcfoSmart()">Smart</a></li>
							<li><a href="#tabs_1_3_5" onclick="getYearAcfoBig()">Big</a></li>
							<li><a href="#tabs_1_3_6" onclick="getYearAcfoCurved()">Curve</a></li>
						</ul>
						<!--ALL-->
						<div id="tabs_1_3_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
								<input readonly onchange="getYearAcfoTotal()" id="YearChainAcfo" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input checked ='checked' onchange="getYearAcfoTotal()" id="YearChainAcfoCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="YearChainAcfoText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head7">
									<table style="border-right: 1px solid #bbb;" id="YearChainAcfoLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right7">
									<table id="YearChainAcfoRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left7" onScroll="moveLeft_Leftseven();">
								    <table id="YearChainAcfoRightData">
								    	
								    </table>
								</div>
							</div>
						
						</div>
						<!--UD-->
						<div id="tabs_1_3_2" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for=""  >Year</label>
								<input  readonly id="YearUDAcfo" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select name=""  id="AcfoUDModelByYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' id="YearUDAcfoCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getYearAcfoUD()">Search</button>
			                	<p class=""  id="YearUDAcfoText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head8">
									<table style="border-right: 1px solid #bbb;"  id="AcfoUDByYearleft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right8">
									<table id="AcfoUDByYearRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left8" onScroll="moveLeft_Lefteight();">
								    <table id="AcfoUDByYearRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<!--XCP单品-->
						<div id="tabs_1_3_3" class="clear">
							<div style="width: 100%;">
									<fieldset class="inb">
									<label  for="" style="margin-left: 0;">Year</label>
									<input readonly id="YearXCPAcfo" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpAcfoYear" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpAcfoYear">
										
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input checked ='checked' id="YearXCPAcfoCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getYearAcfoXCP()">Search</button>
			                	<p  id="YearXCPAcfoText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head9">
									<table style="border-right: 1px solid #bbb;"  id="AcfoXCPByYearleft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right9">
									<table  id="AcfoXCPByYearRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left9" onScroll="moveLeft_Leftnine();">
								    <table  id="AcfoXCPByYearRightData">
								    
								    </table>
								</div>
							</div>
						</div>
						<!--Smart-->
						<div id="tabs_1_3_4" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
										<input readonly onchange="getYearAcfoSmart()"  id="YearSmartAcfo" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								</fieldset>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input onchange="getYearAcfoSmart()" checked ='checked' id="YearSmartAcfoCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
				                <p class=""  id="YearSmartAcfoText" ></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head10">
									<table style="border-right: 1px solid #bbb;"  id="AcfoSmartByYearleft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right10">
									<table   id="AcfoSmartByYearRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left10" onScroll="moveLeft_Leftten();">
								    <table   id="AcfoSmartByYearRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<!--Big-->
						<div id="tabs_1_3_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input readonly  onchange="getYearAcfoBig()"    id="YearBigAcfo" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								
							<%-- 	<fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="AcfoBigSize"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getYearAcfoBig()" checked ='checked' id="YearBigAcfoCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<!-- <button onclick="getYearAcfoBig()">Search</button> -->
				                <p class="" id="YearBigSaleText" ></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head11">
									<table style="border-right: 1px solid #bbb;"  id="AcfoBigByYearleft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right11">
									<table id="AcfoBigByYearRightHead">
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left11" onScroll="moveLeft_Left11();">
								    <table id="AcfoBigByYearRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<!--Curve-->
						<div id="tabs_1_3_6" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for=""  >Year</label>
								<input readonly id="YearCurvedAcfo" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select name=""  id="CurvedModelAcfoYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' id="YearCurvedAcfoCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getYearAcfoCurved()">Search</button>
			                	<p class=""  id="YearCurvedAcfoText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head12">
									<table style="border-right: 1px solid #bbb;"  id="AcfoCurvedByYearleft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right12">
									<table id="AcfoCurvedByYearRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left12" onScroll="moveLeft_Left12();">
								    <table  id="AcfoCurvedByYearRightData">
								    
								    </table>
								</div>
							</div>
						</div>
					</div>
					<!--Country-->
					<div class="fl" id="tabs_1_4">
						<ul>
							<li><a href="#tabs_1_4_1" onclick="getYearCountryTotal()">ALL</a></li>
							<li><a href="#tabs_1_4_2"  onclick="getYearCountryUD()">UD</a></li>
							<li><a href="#tabs_1_4_3"  onclick="getYearXCP()">X/C/P</a></li>
							<li><a href="#tabs_1_4_4"  onclick="getYearCountrySmart()">Smart</a></li>
							<li><a href="#tabs_1_4_5"  onclick="getYearCountryBig()">Big</a></li>
							<li><a href="#tabs_1_4_6"  onclick="getYearCountryCurved()">Curve</a></li>
						</ul>
						<!--ALL-->
						<div id="tabs_1_4_1" class="clear">
							<div style="width: 100%;">
							
								<fieldset class="inb">
										<label for=""  >Year</label>
								<input readonly onchange="getYearCountryTotal()"  id="YearChainCountry" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input checked ='checked' onchange="getYearCountryTotal()" id="YearChainCountryCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                	<p class="" id="YearChainCountryText"></p>
							</div>
							<div style="overflow: auto;">
								<table  id="YearChainCountryData">
									
								</table>
							</div>
						</div>
						<!--UD-->
						<div id="tabs_1_4_2" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for=""  >Year</label>
								<input readonly id="YearUDCountry" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select name=""  id="countryUDModelByYear">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' id="YearUDCountryCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getYearCountryUD()">Search</button>
			                	<p class=""  id="YearUDCountryText"></p>
							</div>
							<div style="overflow: auto;">
								<table  id="YearUDCountryData">
									
								</table>
							</div>
						</div>
						<!--XCP单品-->
						<div id="tabs_1_4_3" class="clear">
						<div style="width: 100%;">
								<fieldset class="inb">
									<label  for="" style="margin-left: 0;">Year</label>
									<input readonly id="reservationAnnualXCP" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="Xcp" onchange="getSubXcpGro(this)">
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
									<input checked ='checked' id="reservationAnnualXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getYearXCP()" style="background: #007fff;border: 1px solid #ddd!important;padding: 0 4px;border-radius: 4px;color: #fff;">Search</button>
			                	<p  id="yearXCPText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head13">
										<table style="border-right: 1px solid #bbb;" id="yearXCPLeft">
										<!--thead不动-->
										
										<!--tbooy动态生成-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right13">
									<table id="yearXCPRightHead" >
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left13" onScroll="moveLeft_Left13();">
								   <table id="yearXCPRight"> 
								    	
								    </table>
								</div>
							</div>
						</div>
						<!--Smart-->
						<div id="tabs_1_4_4" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input readonly onchange="getYearCountrySmart()"    id="YearCountrySmart" name="reservatio" type="text"   style="width:150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input checked ='checked' onchange="getYearCountrySmart()" id="YearCountrySmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                	<p class="" id="YearCountrySmartText"></p>
							</div>
							<div style="overflow: auto;">
								<table  id="YearCountrySmartData">
									
								</table>
							</div>
						</div>
						<!--big-->
						<div id="tabs_1_4_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input readonly onchange="getYearCountryBig()"   id="YearCountryBig" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="countryBigSize"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getYearCountryBig()" checked ='checked' id="YearCountryBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
<!-- 								<button  onclick="getYearCountryBig()" style="background: #007fff;border: 1px solid #ddd!important;padding: 0 4px;border-radius: 4px;color: #fff;">Search</button>
 -->								
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head14">
									<table style="border-right: 1px solid #bbb;"  id="YearCountryBigLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right14">
									<table  id="YearCountryBigRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left14" onScroll="moveLeft_Left14();">
								    <table id="YearCountryBigRightData">
								    	
								    </table>
								</div>
							
							</div>
						</div>
						<!--Curve-->
						<div id="tabs_1_4_6" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Year</label>
									<input  readonly  id="YearCountryCurved" name="reservatio" type="text"   style="width: 150px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select name="" id="countryCurvedModelByYear">

									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked' id="YearCountryCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getYearCountryCurved()">Search</button>
			                	<p class="" id="YearCurvedCountryText"></p>
							</div>
							<div style="overflow: auto;">
								<table  id="YearCountryCurvedData">
								
								</table>
							</div>
						</div>
					</div>
					
				</div>
				<!--月-->
				<div id="tabs-2" style="width: 100%;padding-top: 0;padding-left: 0;">
					<ul class="fl">
						<li><a href="#tabs_2_4" onclick="getMonthByCountryTotal()" id='month_county'></a></li>
						<li><a href="#tabs_2_1"  onclick="getMonthByRegTotal()" id='month_regiHead'></a></li>
						<li><a href="#tabs_2_2"  onclick="getMonthBySaleTotal()" id='month_sale'></a></li>
						<li><a href="#tabs_2_3"  onclick="getMonthByAcfoTotal()" id='month_acfo'></a></li>
						
					</ul>
					<!--Regional Head-->
					<div class="fl" id="tabs_2_1">
						<ul>
							<li><a href="#tabs_2_1_1" onclick="getMonthByRegTotal()">ALL</a></li>
							<li><a href="#tabs_2_1_2" onclick="getMonthByRegUD()">UD</a></li>
							<li><a href="#tabs_2_1_3" onclick="getMonthByRegXCP()">X/C/P</a></li>
							<li><a href="#tabs_2_1_4" onclick="getMonthByRegSmart()">Smart</a></li>
							<li><a href="#tabs_2_1_5" onclick="getMonthByRegBig()">Big</a></li>
							<li><a href="#tabs_2_1_6" onclick="getMonthByRegCurved()">Curved</a></li>
						</ul>
						<div id="tabs_2_1_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input    onchange="getMonthByRegTotal()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthRegTotal"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getMonthByRegTotal()"  checked ='checked' id="monthRegTotalCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="monthRegTotalText"></p>
							</div>
							<table class="fl float_left"  id="monthRegTotalLeft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="monthRegTotalRight">
									
								</table>
							</div>
						</div>
						<div id="tabs_2_1_2" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input     style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthRegUD"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="RegUDModelByMonth">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked' id="monthRegUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  
								<button onclick="getMonthByRegUD()">Search</button>
								<p class="" id="monthRegUDText"></p>
								
							</div>
							<table class="fl float_left"  id="monthRegUDLeft">
							
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table  id="monthRegUDRight">
									
								</table>
							</div>
						</div>
						<div id="tabs_2_1_3" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input  style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthRegXCP"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpRegMonth" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpRegMonth">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="monthRegXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getMonthByRegXCP()" style="margin-left:10px;">Search</button>
			                	<p class=""  id="monthRegXCPText"></p>
							</div>
							<table class="fl float_left" id="monthRegXCPLeft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="monthRegXCPRight">
									
								</table>
							</div>
						</div>
						<div id="tabs_2_1_4" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input    onchange="getMonthByRegSmart()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthRegSmart"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getMonthByRegSmart()"  checked ='checked' id="monthRegSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="monthRegSmartText"></p>
							</div>
							<table class="fl float_left" id="monthRegSmartLeft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="monthRegSmartRight">
								
								</table>
							</div>
						</div>
						<div id="tabs_2_1_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
								<input onchange="getMonthByRegBig()" onchange="getMonthByRegBig()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthRegBig"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="RegBigSizeByMonth"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
							<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input  onchange="getMonthByRegBig()" checked ='checked' id="monthRegBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
<!-- 								<button  onclick="getMonthByRegBig()">Search</button>
 -->			                	<p class=""  id="monthRegBigText"></p>
							</div>
							<table class="fl float_left"  id="monthRegBigLeft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table id="monthRegBigRight">
									
								</table>
							</div>
						</div>
						<div id="tabs_2_1_6" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
									<input   style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthRegCurved"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="CurvedModelRegMonth">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked' id="monthRegCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getMonthByRegCurved()">Search</button>
			                	<p class="" id="monthRegCurvedText"></p>
							</div>
							<table class="fl float_left"  id="monthRegCurvedLeft">
								
							</table>
							<div class="fl MRH" style="overflow: auto;">
								<table   id="monthRegCurvedRight">
									
								</table>
							</div>
						</div>
					</div>
					<!--Salesman-->
					<div class="fl" id="tabs_2_2">
						<ul>
							<li><a href="#tabs_2_2_1" onclick="getMonthBySaleTotal()">ALL</a></li>
							<li><a href="#tabs_2_2_2" onclick="getMonthBySaleUD()">UD</a></li>
							<li><a href="#tabs_2_2_3" onclick="getMonthBySaleXCP()">X/C/P</a></li>
							<li><a href="#tabs_2_2_4" onclick="getMonthBySaleSmart()">Smart</a></li>
							<li><a href="#tabs_2_2_5" onclick="getMonthBySaleBig()">Big</a></li>
							<li><a href="#tabs_2_2_6" onclick="getMonthBySaleCurved()">Curved</a></li>
						</ul>
						<div id="tabs_2_2_1" class="clear">
							<div style="width: 100%;">
									<fieldset class="inb">
								<label for="">Month</label>
								<input    onchange="getMonthBySaleTotal()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthSaleTotal"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getMonthBySaleTotal()"  checked ='checked' id="monthSaleTotalCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="monthSaleTotalText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head21">
									<table style="border-right: 1px solid #bbb;"  id="monthSaleTotalLeft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right21">
									<table id="monthSaleTotalRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left21" onScroll="moveLeft_Left21();">
								    <table id="monthSaleTotalRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_2" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input     style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthSaleUD"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

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
								<p class="" id="monthSaleUDText"></p>
								
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head22">
									<table style="border-right: 1px solid #bbb;" id="monthSaleUDLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right22">
									<table  id="monthSaleUDRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left22" onScroll="moveLeft_Left22();">
								    <table id="monthSaleUDRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_3" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input  style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthSaleXCP"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpSaleMonth" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset style="text-indent: 5px;">
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
			                	<p class=""  id="monthSaleXCPText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head23">
									<table style="border-right: 1px solid #bbb;" id="monthSaleXCPLeft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right23">
									<table id="monthSaleXCPRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left23" onScroll="moveLeft_Left23();">
								   <table id="monthSaleXCPRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_4" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input    onchange="getMonthBySaleSmart()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthSaleSmart"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getMonthBySaleSmart()"  checked ='checked' id="monthSaleSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="monthSaleSmartText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head24">
									<table style="border-right: 1px solid #bbb;"  id="monthSaleSmartLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right24">
									<table  id="monthSaleSmartRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left24" onScroll="moveLeft_Left24();">
								    <table  id="monthSaleSmartRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
								<input  onchange="getMonthBySaleBig()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthSaleBig"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

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
									<input  onchange="getMonthBySaleBig()" checked ='checked' id="monthSaleBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
<!-- 								<button  onclick="getMonthBySaleBig()">Search</button>
 -->			                	<p class=""  id="monthSaleBigText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head25">
									<table style="border-right: 1px solid #bbb;"  id="monthSaleBigLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right25">
									<table id="monthSaleBigRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left25" onScroll="moveLeft_Left25();">
								    <table  id="monthSaleBigRightData">
								    
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_2_6" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input   style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthSaleCurved"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

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
			                	<p class="" id="monthSaleCurvedText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head26">
									<table style="border-right: 1px solid #bbb;" id="monthSaleCurvedLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right26">
									<table id="monthSaleCurvedRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left26" onScroll="moveLeft_Left26();">
								    <table id="monthSaleCurvedRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
					<!--Acfo-->
					<div class="fl" id="tabs_2_3">
						<ul>
							<li><a href="#tabs_2_3_1" onclick="getMonthByAcfoTotal()">ALL</a></li>
							<li><a href="#tabs_2_3_2" onclick="getMonthByAcfoUD()">UD</a></li>
							<li><a href="#tabs_2_3_3" onclick="getMonthByAcfoXCP()">X/C/P</a></li>
							<li><a href="#tabs_2_3_4" onclick="getMonthByAcfoSmart()">Smart</a></li>
							<li><a href="#tabs_2_3_5" onclick="getMonthByAcfoBig()">Big</a></li>
							<li><a href="#tabs_2_3_6" onclick="getMonthByAcfoCurved()">Curved</a></li>
						</ul>
						<div id="tabs_2_3_1" class="clear">
							<div style="width: 100%;">
									<fieldset class="inb">
								<label for="">Month</label>
								<input    onchange="getMonthByAcfoTotal()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthAcfoTotal"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getMonthByAcfoTotal()"  checked ='checked' id="monthAcfoTotalCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="monthAcfoTotalText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head27">
									<table style="border-right: 1px solid #bbb;" id="monthAcfoTotalLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right27">
									<table  id="monthAcfoTotalRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left27" onScroll="moveLeft_Left27();">
								    <table id="monthAcfoTotalRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_3_2" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthAcfoUD"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="AcfoUDModelByMonth">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked' id="monthAcfoUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  
								<button onclick="getMonthByAcfoUD()">Search</button>
								<p class="" id="monthAcfoUDText"></p>
								
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head28">
									<table style="border-right: 1px solid #bbb;"  id="monthAcfoUDLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right28">
									<table  id="monthAcfoUDRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left28" onScroll="moveLeft_Left28();">
								    <table id="monthAcfoUDRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_3_3" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input  style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthAcfoXCP"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpAcfoMonth" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpAcfoMonth">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="monthAcfoXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getMonthByAcfoXCP()" style="margin-left:10px;">Search</button>
			                	<p class=""  id="monthAcfoXCPText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head29">
									<table style="border-right: 1px solid #bbb;" id="monthAcfoXCPLeft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right29">
									<table id="monthAcfoXCPRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left29" onScroll="moveLeft_Left29();">
								    <table id="monthAcfoXCPRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_3_4" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input    onchange="getMonthByAcfoSmart()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthAcfoSmart"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getMonthByAcfoSmart()"  checked ='checked' id="monthAcfoSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
				                  <p class="" id="monthAcfoSmartText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head30">
									<table style="border-right: 1px solid #bbb;" id="monthAcfoSmartLeft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right30">
									<table id="monthAcfoSmartRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left30" onScroll="moveLeft_Left30();">
								    <table id="monthAcfoSmartRightData">
											
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_3_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
								<input  onchange="getMonthByAcfoBig()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthAcfoBig"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="AcfoBigSizeByMonth"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
							<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input  onchange="getMonthByAcfoBig()"  checked ='checked' id="monthAcfoBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
<!-- 								<button  onclick="getMonthByAcfoBig()">Search</button>
 -->			                	<p class=""  id="monthAcfoBigText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head31">
									<table style="border-right: 1px solid #bbb;"  id="monthAcfoBigLeft">
										<!--thead不动-->
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right31">
									<table id="monthAcfoBigRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left31" onScroll="moveLeft_Left31();">
								    <table id="monthAcfoBigRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
						<div id="tabs_2_3_6" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
									<input style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthAcfoCurved"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="CurvedModelAcfoMonth">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked' id="monthAcfoCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getMonthByAcfoCurved()">Search</button>
			                	<p class="" id="monthAcfoCurvedText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head32">
									<table style="border-right: 1px solid #bbb;" id="monthAcfoCurvedLeft">
									
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right32">
									<table id="monthAcfoCurvedRightHead">
									
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left32" onScroll="moveLeft_Left32();">
								    <table id="monthAcfoCurvedRightData">
								    	
								    </table>
								</div>
							</div>
						</div>
					</div>
					<!--Country-->
					<div class="fl" id="tabs_2_4">
						<ul>
							<li><a href="#tabs_2_4_1" onclick="getMonthByCountryTotal()">ALL</a></li>
							<li><a href="#tabs_2_4_2" onclick="getMonthByCountryUD()">UD</a></li>
							<li><a href="#tabs_2_4_3" onclick="getMonthByCountryXCP()">X/C/P</a></li>
							<li><a href="#tabs_2_4_4" onclick="getMonthByCountrySmart()">Smart</a></li>
							<li><a href="#tabs_2_4_5" onclick="getMonthByCountryBig()">Big</a></li>
							<li><a href="#tabs_2_4_6" onclick="getMonthByCountryCurved()">Curved</a></li>
						</ul>
						<!--ALL-->
						<div id="tabs_2_4_1" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
									<input  onchange="getMonthByCountryTotal()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthCountryTotal"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  onchange="getMonthByCountryTotal()"  checked ='checked' id="monthCountryTotalCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                	<p class="" id="monthCountryTotalText"></p>
							</div>
							<div style="overflow: auto;">
								<table style="width: 100%;"  id="monthCountryTotalData">
									
								</table>
							</div>
						</div>
						<!--UD-->
						<div id="tabs_2_4_2" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input  style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthCountryUD"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								
			                	
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="countryUDModelByMonth">
										<option value=""></option>
									</select>
								</fieldset>
								
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked' id="monthCountryUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getMonthByCountryUD()">Search</button>
			                	<p class="" id="monthCountryUDText"></p>
							</div>
							<div style="overflow: auto;">
								<table style="width: 100%;"  id="monthCountryUDData">
									
								</table>
							</div>
						</div>
						<!--XCP单品-->
						<div id="tabs_2_4_3" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input  style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthCountryXCP"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpCountryMonth" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset>
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpCountryMonth">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="monthCountryXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getMonthByCountryXCP()" style="margin-left:10px;">Search</button>
			                	<p class=""  id="monthCountryXCPText"></p>
							</div>
							<div style="overflow: auto;">
								<div class="fl Left_Head" id="test_Head33">
									<table style="border-right: 1px solid #bbb;"  id="monthCountryXCPleft">
										
									</table>
								</div>
								<div class="fl Right_Head" id="test_Right33">
									<table id="monthCountryXCPRightHead">
										
									</table>
								</div>
								<div class="fl Right_Body" id="test_Left33" onScroll="moveLeft_Left33();">
								    <table id="monthCountryXCPRightData">
								    	
								    </table>
								
								</div>
							</div>
						</div>
						<!--Smart-->
						<div id="tabs_2_4_4" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input  onchange="getMonthByCountrySmart()" style="padding:0;height:24px;width:150px;display: inline-block;"  id="monthCountrySmart"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input onchange="getMonthByCountrySmart()"   checked ='checked' id="monthCountrySmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                	<p class="" id="monthCountrySmartText"></p>
							</div>
							<div style="overflow: auto;">
								<table style="width: 100%;"  id="monthCountrySmartData">
									
								</table>
							</div>
						</div>
						<!--big-->
						<div id="tabs_2_4_5" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
									<label for="">Month</label>
								<input  onchange="getMonthByCountryBig()" style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthCountryBig"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<%-- <fieldset class="inb">
									<label for="" style="color:#aaa;">Size(&gt;)</label>
									<select  id="CountryBigSizeByMonth"  style="width:100px">
									<option value="48"> 48 </option>
									<option value="55"> 55 </option>
									</select>
								</fieldset> --%>
							<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   onchange="getMonthByCountryBig()"  checked ='checked' id="monthCountryBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
<!-- 								<button  onclick="getMonthByCountryBig()">Search</button>
 -->			                	<p class=""  id="monthCountryBigText"></p>
			                	</div>
							<div class="fl Left_Head" id="test_Head34">
								<table style="border-right: 1px solid #bbb;" id="monthCountryBigleft">
								
								</table>
							</div>
							<div class="fl Right_Head" id="test_Right34">
								<table  id="monthCountryBigRightHead">
									
								</table>
							</div>
							<div class="fl Right_Body" id="test_Left34" onScroll="moveLeft_Left34();">
							    <table  id="monthCountryBigRightData">
							    	
							    </table>
							</div>
						</div>
						<!--Curve-->
						<div id="tabs_2_4_6" class="clear">
							<div style="width: 100%;">
								<fieldset class="inb">
								<label for="">Month</label>
								<input style="width:150px;display: inline-block;padding:0;height:24px;"  id="monthCountryCurved"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label for="">Model</label>
									<select id="countryCurvedModelByMonth">
										<option value=""></option>
									</select>
								</fieldset>
								<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked' id="monthCountryCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button  onclick="getMonthByCountryCurved()">Search</button>
			                	<p class="" id="monthCountryCurvedText"></p>
							</div>
							<div style="overflow: auto;">
								<table style="width: 100%;" id="monthCountryCurvedData">
									
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