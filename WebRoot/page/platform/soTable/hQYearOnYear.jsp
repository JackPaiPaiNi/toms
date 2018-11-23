 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="cn.tcl.common.WebPageUtil"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	/* String party = (String) request.getSession().getAttribute("loginUserId");
	String language = WebPageUtil.getLanguage();
	String loginCountry = WebPageUtil.getLoginedUser().getPartyId(); */
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><s:text name='permission.labelkey.sogrowth'/></title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/jquery-ui.css"/>
	<!--定义文件 不可删除-->
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/headAndCustom.css"/>
	<!--定义文件结束-->
	<!--正文-->
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/HQYearOnYear.css"/>
	<!-- 时间插件 -->
    <link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
    
	<script src="<%=basePath%>js/platform/soTable/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/hQYearOnYear.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/hq_growth.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/hq_growth_html_joint.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/soTable/daterangepicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/soTable/WdatePicker.js"></script>
     <style type="text/css">
    	.ui-resizable {
		    position: absolute;
		}
    </style>
</head>
<body>
	  <!--主要内容-->
        <div class="main">
        	<%-- <p class="create"><s:text name='permission.labelkey.sogrowth'/></p>  --%>
        	<p class="create">SO Year-to-year Growth</p> 
        	<!-- Tabs -->
			<div id="prizes" style="margin-top: 15px;">
				<ul class="">
					<li onclick="queryYearGrowth();" class="Year"><a href="#tabs-1">Year</a></li>
					<li onclick="halfAYearTabFunctionCut();" class="Month"><a href="#tabs-2">Half a year</a></li>
					<li onclick="quarterTabFunctionCut();" class="Week"><a href="#tabs-3">Quarter</a></li>
				</ul>
				<!--年-->
				<div id="tabs-1">
					<ul class="fl" id='year_tab'>
						<li><a href="#tabs_1_1">Total</a></li>
						<li><a href="#tabs_1_2">UD</a></li>
						<li><a href="#tabs_1_3">X/C/P</a></li>
						<li><a href="#tabs_1_4">Smart</a></li>
						<li><a href="#tabs_1_5">Big</a></li>
						<li><a href="#tabs_1_6">Curved</a></li>
					</ul>
					<!--Total-->
					<div class="fl concreteness" id="tabs_1_1">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="totalGrowthDate_year" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								<label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input id="totalGrowthCoeff_year" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<button onclick="queryYearGrowth('Total');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea clear">
							<div  class="leftTabel fl" id="YearTotalLeft">
								<!-- year-total-left -->
							</div>
							<div class="rightTabel fl" id="YearTotalRight" onscroll="moveTop('YearTotalRight','YearTotalLeft')">
								<!-- year-total-right -->
							</div>	
						</div>
					</div>
					<!--UD-->
					<div class="fl concreteness" id="tabs_1_2">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="uDGrowthDate_year" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								<label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input id="uDrowthCoeff_year" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<button onclick="queryYearGrowth('UD');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="YearUdLeft">
								<!-- year-ud-left -->
							</div>
							<div class="rightTabel fl" id="YearUdRight" onscroll="moveTop('YearUdRight','YearUdLeft')">
								<!-- year-ud-right -->
							</div>	
						</div>
					</div>
					<!--X/C/P-->
					<div class="fl concreteness" id="tabs_1_3">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="xCPGrowthDate_year" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
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
								<input id="xCPGrowthCoeff_year" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<button onclick="queryYearGrowth('X/C/P');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="YearXCPLeft">
								<!-- year-X/C/P-left -->
							</div>
							<div class="rightTabel fl" id="YearXCPRight" onscroll="moveTop('YearXCPRight','YearXCPLeft')">
								<!-- year-X/C/P-right -->
							</div>	
						</div>
					</div>
					<!--Smart-->
					<div class="fl concreteness" id="tabs_1_4">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="smartGrowthDate_year" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								<label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input id="smartGrowthCoeff_year" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<button onclick="queryYearGrowth('Smart');">Search</button>
		                	<p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="YearSmartLeft">
								<!-- year-Smart-left -->
							</div>
							<div class="rightTabel fl" id="YearSmartRight" onscroll="moveTop('YearSmartRight','YearSmartLeft')">
								<!-- year-Smart-right -->
							</div>	
						</div>
					</div>
					<!--Big-->
					<div class="fl concreteness" id="tabs_1_5">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="bigGrowthDate_year" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								<label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input id="bigGrowthCoeff_year" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<button onclick="queryYearGrowth('Big');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="YearBigLeft">
								<!-- year-Big-left -->
							</div>
							<div class="rightTabel fl" id="YearBigRight" onscroll="moveTop('YearBigRight','YearBigLeft')">
								<!-- year-Big-right -->
							</div>	
						</div>
					</div>
					<!--Curved-->
					<div class="fl concreteness" id="tabs_1_6">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="curvedGrowthDate_year" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
								<label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input id="curvedGrowthCoeff_year" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<button onclick="queryYearGrowth('Curved');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="YearCurvedLeft">
								<!-- year-Curved-left -->
							</div>
							<div class="rightTabel fl" id="YearCurvedRight" onscroll="moveTop('YearCurvedRight','YearCurvedLeft')">
								<!-- year-Curved-right -->
							</div>
						</div>
					</div>
				</div>
				<!--半年-->
				<div id="tabs-2">
					<ul class="fl" id='half_a_year_tab'>
						<li><a href="#tabs_2_1">Total</a></li>
						<li><a href="#tabs_2_2">UD</a></li>
						<li><a href="#tabs_2_3">X/C/P</a></li>
						<li><a href="#tabs_2_4">Smart</a></li>
						<li><a href="#tabs_2_5">Big</a></li>
						<li><a href="#tabs_2_6">Curved</a></li>
					</ul>
					<!--Total-->
					<div class="fl concreteness" id="tabs_2_1">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="totalGrowthDate_halfAyear" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select id="total_halfAyear_select" name="" style="width: 130px;">
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="totalGrowthCoeff_halfAyear" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryHalfAYearGrowth('Total');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="HalfyearTotalLeft">
								<!-- halfAyear-total-left -->
							</div>
							<div class="rightTabel fl" id="HalfyearTotalRight" onscroll="moveTop('HalfyearTotalRight','HalfyearTotalLeft')">
								<!-- halfAyear-total-right -->
							</div>
						</div>
					</div>
					<!--UD-->
					<div class="fl concreteness" id="tabs_2_2">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="uDGrowthDate_halfAyear" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select id="ud_halfAyear_select" name="" style="width: 130px;">
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="uDrowthCoeff_halfAyear" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryHalfAYearGrowth('UD');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="HalfyearUDLeft">
								
							</div>
							<div class="rightTabel fl" id="HalfyearUDRight" onscroll="moveTop('HalfyearUDRight','HalfyearUDLeft')">
								
							</div>
						</div>
					</div>
					<!--X/C/P-->
					<div class="fl concreteness" id="tabs_2_3">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="xCPGrowthDate_halfAyear" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select id="xcp_halfAyear_select" name="" style="width: 130px;">
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							
							<fieldset class="inb">
								<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesXcp_halfAyear" onchange="SeriesXcp(this,'_halfAyear')">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subXcp_halfAyear">
									</select>
							</fieldset>
							<fieldset class="inb">
								<label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input id="xCPGrowthCoeff_halfAyear" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<button onclick="queryHalfAYearGrowth('X/C/P');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="HalfyearXCPLeft">
								
							</div>
							<div class="rightTabel fl" id="HalfyearXCPRight" onscroll="moveTop('HalfyearXCPRight','HalfyearXCPLeft')">
								
							</div>
						</div>
					</div>
					<!--Smart-->
					<div class="fl concreteness" id="tabs_2_4">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="smartGrowthDate_halfAyear" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select id="smart_halfAyear_select" name="" style="width: 130px;">
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="smartGrowthCoeff_halfAyear" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryHalfAYearGrowth('Smart');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="HalfyearSmartLeft">
							
							</div>
							<div class="rightTabel fl" id="HalfyearSmartRight" onscroll="moveTop('HalfyearSmartRight','HalfyearSmartLeft')">
								
							</div>
						</div>
					</div>
					<!--Big-->
					<div class="fl concreteness" id="tabs_2_5">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="bigGrowthDate_halfAyear" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select id="big_halfAyear_select" name="" style="width: 130px;">
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="bigGrowthCoeff_halfAyear" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryHalfAYearGrowth('Big');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="HalfyearBigLeft">
								
							</div>
							<div class="rightTabel fl" id="HalfyearBigRight" onscroll="moveTop('HalfyearBigRight','HalfyearBigLeft')">
								
							</div>
						</div>
					</div>
					<!--Curved-->
					<div class="fl concreteness" id="tabs_2_6">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="curvedGrowthDate_halfAyear" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Half a year</label>
								<select id="curved_halfAyear_select" name="" style="width: 130px;">
									<option value="H1">H1</option>
									<option value="H2">H2</option>
								</select>
							</fieldset>
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="curvedGrowthCoeff_halfAyear" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryHalfAYearGrowth('Curved');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="HalfyearCurvedLeft">
								
							</div>
							<div class="rightTabel fl" id="HalfyearCurvedRight" onscroll="moveTop('HalfyearCurvedRight','HalfyearCurvedLeft')">
								
							</div>
						</div>
					</div>
				</div>
				<!--季度-->
				<div id="tabs-3">
					<ul class="fl" id= 'quarter_tab'>
						<li><a href="#tabs_3_1">Total</a></li>
						<li><a href="#tabs_3_2">UD</a></li>
						<li><a href="#tabs_3_3">X/C/P</a></li>
						<li><a href="#tabs_3_4">Smart</a></li>
						<li><a href="#tabs_3_5">Big</a></li>
						<li><a href="#tabs_3_6">Curved</a></li>
					</ul>
					<!--Total-->
					<div class="fl concreteness" id="tabs_3_1">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="totalGrowthDate_quarter" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id='total_quarter_select' name="">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
							</fieldset>
							
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="totalGrowthCoeff_quarter" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryQuarterGrowth('Total');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="QuarterTotalLeft">
							
							</div>
							<div class="rightTabel fl" id="QuarterTotalRight" onscroll="moveTop('QuarterTotalRight','QuarterTotalLeft')">
								
							</div>
						</div>
					</div>
					<!--UD-->
					<div class="fl concreteness" id="tabs_3_2">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="uDGrowthDate_quarter" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id='ud_quarter_select' name="">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
							</fieldset>
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="uDrowthCoeff_quarter" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryQuarterGrowth('UD');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div class="leftTabel fl" id="QuarterUDLeft">
								
							</div>
							<div class="rightTabel fl" id="QuarterUDRight" onscroll="moveTop('QuarterUDRight','QuarterUDLeft')">
								
							</div>
						</div>
					</div>
					<!--X/C/P-->
					<div class="fl concreteness" id="tabs_3_3">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="xCPGrowthDate_quarter" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id='xcp_quarter_select' name="">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
							</fieldset>
							<fieldset class="inb">
								<label tcl-text="soAch.xcp" for="" style="margin-left: 0;">Series</label>
									<select id="SeriesXcp_quarter" onchange="SeriesXcp(this,'_quarter')">
										<option value="">All</option>
										<option value="X">X</option>
										<option value="C">C</option>
										<option value="P">P</option>
									</select>
									<select id="subXcp_quarter">
									</select>
							</fieldset>
							<fieldset class="inb">
								<label for="" style="color:#aaa;">Reduction Coefficient</label>
								<input id="xCPGrowthCoeff_quarter" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							</fieldset>
							<button onclick="queryQuarterGrowth('X/C/P');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="QuarterXCPLeft">
								
							</div>
							<div class="rightTabel fl" id="QuarterXCPRight" onscroll="moveTop('QuarterXCPRight','QuarterXCPLeft')">
								
							</div>
						</div>
					</div>
					<!--Smart-->
					<div class="fl concreteness" id="tabs_3_4">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="smartGrowthDate_quarter" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id='smart_quarter_select' name="">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
							</fieldset>
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="smartGrowthCoeff_quarter" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryQuarterGrowth('Smart');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="QuarterSmartLeft">
								
							</div>
							<div class="rightTabel fl" id="QuarterSmartRight" onscroll="moveTop('QuarterSmartRight','QuarterSmartLeft')">
								
							</div>
						</div>
					</div>
					<!--Big-->
					<div class="fl concreteness" id="tabs_3_5">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="bigGrowthDate_quarter" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id='big_quarter_select' name="">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
							</fieldset>
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="bigGrowthCoeff_quarter" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryQuarterGrowth('Big');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="QuarterBigLeft">
								
							</div>
							<div class="rightTabel fl" id="QuarterBigRight" onscroll="moveTop('QuarterBigRight','QuarterBigLeft')">
								
							</div>
						</div>
					</div>
					<!--Curved-->
					<div class="fl concreteness" id="tabs_3_6">
						<div class="selctPadTop">
							<fieldset class="inb">
								<label for="">Year</label>
									<input readonly id="curvedGrowthDate_quarter" name="reservatio" type="text"   style="width: 130px;padding: 0;height: 24px;display: inline-block;"  maxlength="20"class="day-Custom form-control" 
									onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
							</fieldset>
							<fieldset class="inb">
								<label for="">Quarter</label>
								<select id='curved_quarter_select' name="">
									<option value="Q1">Q1</option>
									<option value="Q2">Q2</option>
									<option value="Q3">Q3</option>
									<option value="Q4">Q4</option>
								</select>
							</fieldset>
							<label for="" style="color:#aaa;">Reduction Coefficient</label>
							<input id="curvedGrowthCoeff_quarter" checked ='checked' name="reservatio" type="checkbox" style="width:16px;height: 16px;margin-top: 0;vertical-align: middle;"/>
							<button onclick="queryQuarterGrowth('Curved');">Search</button>
			                <p class="tableHeader"></p>
						</div>
						<div class="tableArea">
							<div  class="leftTabel fl" id="QuarterCurvedLeft">
								
							</div>
							<div class="rightTabel fl" id="QuarterCurvedRight" onscroll="moveTop('QuarterCurvedRight','QuarterCurvedLeft')">
								
							</div>
						</div>
					</div>
				</div>
			</div>
        </div>        
	</div>
	
	<div id='loadingHQGrandImport' style='width:5rem; height:5rem;display:none;position: absolute;left: -webkit-calc(50% - 50px);left: -moz-calc(50% - 50px);left: calc(50% - 50px);
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
