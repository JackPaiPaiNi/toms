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
   	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/jquery-ui.css"/>
  	<!-- 定义文件不可删 -->
   	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/headAndCustom.css"/>
	<!--正文-->
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/formForHeadquarters.css"/>
	<link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
	
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/formForHeadquarters.js"></script>
	<script src="<%=basePath%>js/platform/soTable/tableStyleHQ.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=basePath%>js/platform/soTable/HqChain.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=basePath%>js/platform/soTable/loadDataByHqChain.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=basePath%>js/platform/soTable/commonSO.js" type="text/javascript" charset="utf-8"></script>
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
					<li class="Year"><a href="#tabs-1" onclick="getYearTotal()">Year</a></li>
					<li class="Month"><a href="#tabs-2" onclick="getHalfTotal()">Half a year</a></li>
					<li class="Week"><a href="#tabs-3" onclick="getQuarterTotal()">Quarter</a></li>
				</ul>
				<!--年-->
				<div id="tabs-1">
					<ul class="fl">
						<li><a href="#tabs_1_1" onclick="getYearTotal()">Total</a></li>
						<li><a href="#tabs_1_2" onclick="getYearUD()">UD</a></li>
						<li><a href="#tabs_1_3" onclick="getYearXCP()">X/C/P</a></li>
						<li><a href="#tabs_1_4" onclick="getYearSmart()">Smart</a></li>
						<li><a href="#tabs_1_5" onclick="getYearBig()">Big</a></li>
						<li><a href="#tabs_1_6" onclick="getYearCurved()">Curved</a></li>
					</ul>
					<!--Total-->
					<div class="fl concreteness" id="tabs_1_1">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input onchange="getYearTotal()"  readonly id="YearTotal" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearTotal()" id="YearTotalCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                <p class=""  id="YearTotalText" ></p>
						</div>
						<div class="tableArea">
							<table  id="YearTotalData">
								
								
							</table>
						</div>
					</div>
					<!--UD-->
					<div class="fl concreteness" id="tabs_1_2">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input onchange="getYearUD()"  id="YearUD"  readonly name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearUD()" id="YearUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                <p class=""  id="YearUDText" ></p>
						</div>
						<div class="tableArea">
							<table  id="YearUDData">
								
							</table>
						</div>
					</div>
					<!--X/C/P-->
					<div class="fl concreteness" id="tabs_1_3">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input  style="width:130px;display:inline-block;height:22px;padding:0;"  readonly id="YearXCP"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" class="day-Custom form-control"/>

								</fieldset>
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpYear" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset >
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpYear">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="YearXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button  onclick="getYearXCP()" style="">Search</button>
			                	<p class=""  id="YearXCPText"></p>
						</div>
						<div class="tableArea">
							<table id="YearXCPData">
								
							</table>
						</div>
					</div>
					<!--Smart-->
					<div class="fl concreteness" id="tabs_1_4">
						<div class="selctPadTop">
						<fieldset class="inb">
								<label for="">Year</label>
								<input onchange="getYearSmart()"   readonly id="YearSmart" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearSmart()" id="YearSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                <p class=""  id="YearSmartText" ></p>
						</div>
						<div class="tableArea">
							<table id="YearSmartData">
								
							</table>
						</div>
					</div>
					<!--Big-->
					<div class="fl concreteness" id="tabs_1_5">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input onchange="getYearBig()"  readonly id="YearBig" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearBig()" id="YearBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                <p class=""  id="YearBigText" ></p>
						</div>
						<div class="tableArea">
							<table id="YearBigData">
								
							</table>
						</div>
					</div>
					<!--Curved-->
					<div class="fl concreteness" id="tabs_1_6">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input onchange="getYearCurved()"   readonly id="YearCurved" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked' onchange="getYearCurved()" id="YearCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
			                <p class=""  id="YearCurvedText" ></p>
						</div>
						<div class="tableArea">
							<table  id="YearCurvedData">
								
							</table>
						</div>
					</div>
				</div>
				<!--半年-->
				<div id="tabs-2">
					<ul class="fl">
						<li><a href="#tabs_2_1" onclick="getHalfTotal()">Total</a></li>
						<li><a href="#tabs_2_2" onclick="getHalfUD()">UD</a></li>
						<li><a href="#tabs_2_3" onclick="getHalfXCP()">X/C/P</a></li>
						<li><a href="#tabs_2_4" onclick="getHalfSmart()">Smart</a></li>
						<li><a href="#tabs_2_5" onclick="getHalfBig()">Big</a></li>
						<li><a href="#tabs_2_6" onclick="getHalfCurved()">Curved</a></li>
					</ul>
					<!--Total-->
					<div class="fl concreteness" id="tabs_2_1">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input   id="HalfTotal" readonly name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select name=""  id="HalfTotalWhat">
								  <option value=""> </option>
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked'  id="HalfTotalCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
							<button onclick="getHalfTotal()">Search</button>
			              
			                <p class=""  id="HalfTotalText" ></p>
						</div>
						<div class="tableArea">
							<table id="HalfTotalData">
								
							</table>
						</div>
					</div>
					<!--UD-->
					<div class="fl concreteness" id="tabs_2_2">
						<div class="selctPadTop">
							
							<fieldset class="inb">
								<label for="">Half</label>
								<input   id="HalfUD" readonly name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							
								<fieldset class="inb">
								<label for="">Half a year</label>
								<select name=""  id="HalfUDWhat">
								  <option value=""> </option>
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							
							<fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked'  id="HalfUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button onclick="getHalfUD()">Search</button>
								
			                <p class=""  id="HalfUDText" ></p>
						</div>
						<div class="tableArea">
							<table  id="HalfUDData">
								
							</table>
							
							
							
							
						</div>
					</div>
					<!--X/C/P-->
					<div class="fl concreteness" id="tabs_2_3">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Half</label>
								<input readonly style="width:130px;padding:0;height:22px;display:inline-block;"  id="HalfXCP"  readonly name="reservatio" type="text" onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" class="day-Custom form-control"/>

								</fieldset>
								
								<fieldset class="inb">
								<label for="">Half a year</label>
								<select name=""  id="HalfXCPWhat">
								  <option value=""> </option>
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							
								<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpHalf" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset >
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpHalf">
										
									</select>
									</fieldset>
									<fieldset class="inb">
									<label for="" style="color:#aaa;margin-left: 0;" >Reduction Coefficient</label>
									<input   checked ='checked' id="HalfXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								
								<button onclick="getHalfXCP()">Search</button>
			                	<p class=""  id="HalfXCPText"></p>
						</div>
						<div class="tableArea">
							<table id="HalfXCPData">
								
							</table>
						</div>
					</div>
					<!--Smart-->
					<div class="fl concreteness" id="tabs_2_4">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input   id="HalfSmart" readonly name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select name=""  id="HalfSmartWhat">
								  <option value=""> </option>
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked'  id="HalfSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
							<button onclick="getHalfSmart()">Search</button>
			              
			                <p class=""  id="HalfSmartText" ></p>
						</div>
						<div class="tableArea">
							<table id="HalfSmartData">
								
							</table>
						</div>
					</div>
					<!--Big-->
					<div class="fl concreteness" id="tabs_2_5">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input   id="HalfBig" name="reservatio" readonly type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select name=""  id="HalfBigWhat">
								  <option value=""> </option>
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked'  id="HalfBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
							<button onclick="getHalfBig()">Search</button>
			              
			                <p class=""  id="HalfBigText" ></p>
						</div>
						<div class="tableArea">
							<table id="HalfBigData">
								
							</table>
						</div>
					</div>
					<!--Curved-->
					<div class="fl concreteness" id="tabs_2_6">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input   id="HalfCurved" name="reservatio"  readonly type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select name=""  id="HalfCurvedWhat">
								  <option value=""> </option>
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input  checked ='checked'  id="HalfCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
							<button onclick="getHalfCurved()">Search</button>
			              
			                <p class=""  id="HalfCurvedText" ></p>
						</div>
						<div class="tableArea">
							<table id="HalfCurvedData">
								
							</table>
						</div>
					</div>
				</div>
				<!--季度-->
				<div id="tabs-3">
					<ul class="fl">
						<li><a href="#tabs_3_1" onclick="getQuarterTotal()">Total</a></li>
						<li><a href="#tabs_3_2" onclick="getQuarterUD()">UD</a></li>
						<li><a href="#tabs_3_3" onclick="getQuarterXCP()">X/C/P</a></li>
						<li><a href="#tabs_3_4" onclick="getQuarterSmart()">Smart</a></li>
						<li><a href="#tabs_3_5" onclick="getQuarterBig()">Big</a></li>
						<li><a href="#tabs_3_6" onclick="getQuarterCurved()">Curved</a></li>
					</ul>
					<!--Total-->
					<div class="fl concreteness" id="tabs_3_1">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								<input   readonly id="QuarterTotal" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
									</fieldset>
							
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id="QuarterTotalQ">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
									</fieldset>
							
							 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked'  id="QuarterTotalCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getQuarterTotal()">Search</button>
			                <p  id="QuarterTotalText"></p>
						</div>
						<div class="tableArea">
							<table  id="QuarterTotalData">
								
							</table>
						</div>
					</div>
					<!--UD-->
					<div class="fl concreteness" id="tabs_3_2">
						<div class="selctPadTop">
							
							
							<fieldset class="inb">
								<label for="">Year</label>
								
								<input  readonly id="QuarterUD" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								
									</fieldset>
							
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id="QuarterUDQ">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
									</fieldset>
							
							 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input   checked ='checked'  id="QuarterUDCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getQuarterUD()">Search</button>
			                <p class="" id="QuarterUDText"></p>
						</div>
						<div class="tableArea">
							<table  id="QuarterUDData">
								
							</table>
						</div>
					</div>
					
									
					<!--X/C/P-->
					<div class="fl concreteness" id="tabs_3_3">
						<div class="selctPadTop">
						
						<fieldset class="inb">
							<label for="">Year</label>
							<input   readonly id="QuarterXCP" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
							onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
						</fieldset>
						<fieldset class="inb">
							<label for="">Quarter</label>
							<select id="QuarterXCPQ">
								<option value="Q1">Q1</option>
								<option value="Q2">Q2</option>
								<option value="Q3">Q3</option>
								<option value="Q4">Q4</option>
							</select>
						</fieldset>
						<fieldset class="inb">
									<label tcl-text="soAch.xcp" for="" style="margin-left: 0;"></label>
									<select id="XcpQuarter" onchange="getSubXcpGro(this)">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
								</fieldset >
									<fieldset class="inb">
									<label  tcl-text="soAch.subxcp" style="margin-left: 0;" for=""></label>
									<select  id="subXcpQuarter">
										
									</select>
									</fieldset>
							 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked'  id="QuarterXCPCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getQuarterXCP()">Search</button>
								
			                <p class="" id="QuarterXCPText"></p>
						</div>
						<div class="tableArea">
							<table  id="QuarterXCPData">
								
							</table>
						</div>
					</div>
					<!--Smart-->
					<div class="fl concreteness" id="tabs_3_4">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
								
								<input  readonly id="QuarterSmart" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								
									</fieldset>
							
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id="QuarterSmartQ">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
									</fieldset>
						
						 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked'  id="QuarterSmartCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getQuarterSmart()">Search</button>
			                <p class="" id="QuarterSmartText"></p>
						</div>
						<div class="tableArea">
							<table  id="QuarterSmartData">
								
							</table>
						</div>
					</div>
					<!--Big-->
					<div class="fl concreteness" id="tabs_3_5">
						<div class="selctPadTop">
							
								<fieldset class="inb">
								<label for="">Year</label>
								
								<input  readonly id="QuarterBig" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								
									</fieldset>
							
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id="QuarterBigQ">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
									</fieldset>
						
						
						 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked'  id="QuarterBigCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getQuarterBig()">Search</button>
			                <p class="" id="QuarterBigText"></p>
						</div>
						<div class="tableArea">
							<table  id="QuarterBigData">
								
							</table>
						</div>
					</div>
					<!--Curved-->
					<div class="fl concreteness" id="tabs_3_6">
						<div class="selctPadTop">
							
								<fieldset class="inb">
								<label for="">Year</label>
								
								<input  readonly id="QuarterCurved" name="reservatio" type="text" style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
								onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								
									</fieldset>
							
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id="QuarterCurvedQ">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
									</fieldset>
						
						
							 <fieldset class="inb">
									<label for="" style="color:#aaa;">Reduction Coefficient</label>
									<input    checked ='checked'  id="QuarterCurvedCheck" name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
								</fieldset>
								<button onclick="getQuarterCurved()">Search</button>
			                <p class="" id="QuarterCurvedText"></p>
						</div>
						<div class="tableArea">
							<table  id="QuarterCurvedData">
								
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
			
</body>
</html>