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
  	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/jquery-ui.css"/>
	<!--正文-->
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/formForHeadquarters.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/soTable/headAndCustom.css"/>
	<script src="<%=basePath%>js/platform/soTable/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/soTable/formForHeadquarters.js"></script>
	<script src="<%=basePath%>js/platform/soTable/tableStyleHQ.js" type="text/javascript" charset="utf-8"></script>
    <script src="<%=basePath%>js/platform/BdscTable/hqAch.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=basePath%>js/platform/BdscTable/loadDataByHq.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/soTable/commonSO.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
    <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/soTable/daterangepicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/soTable/WdatePicker.js"></script>
    <style type="text/css">
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
				<li class="Year"><a href="#tabs-1" onclick="getYearTotal()">Year</a></li>
				<li class="Week"><a href="#tabs-3" onclick="getQuarterTotal()">Quarter</a></li>
			</ul>
			<!--年-->
			<div id="tabs-1">
				<ul class="fl">
					<li><a href="#tabs_1_1" onclick="getYearTotal()">Total</a></li>
					<li><a href="#tabs_1_2" onclick="getYearUD()">UD</a></li>
				
				</ul>
				<!--Total-->
				<div class="fl concreteness" id="tabs_1_1">
					<div class="selctPadTop">
						<fieldset class="inb">
							<label for="">Year</label>
							<input onchange="getYearTotal()"  readonly id="YearTotal" name="reservatio" type="text"   style="width:130px;padding:0;height: 22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
							onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
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
							<input onchange="getYearUD()"  id="YearUD"  readonly name="reservatio" type="text"   style="width:130px;padding:0;height: 22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
							onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
						</fieldset>
					
		                <p class=""  id="YearUDText" ></p>
					</div>
					<div class="tableArea">
						<table  id="YearUDData">
							
						</table>
					</div>
				</div>

		
			</div>
			<!--半年-->
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
							<input   readonly id="QuarterTotal" name="reservatio" type="text"   style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
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
							
							<input  readonly id="QuarterUD" name="reservatio" type="text"   style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
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
							
							<input   readonly id="QuarterXCP" name="reservatio" type="text"   style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
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
								<select  id="subXcpQuarter" onchange="selectXCPModel(this)">
									
								</select>
								</fieldset>
								
									<fieldset class="inb">
								<label  tcl-text="barcode.list.th.model" style="margin-left: 0;" for=""></label>
								<select  id="subModel" >
									
								</select>
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
							
							<input  readonly id="QuarterSmart" name="reservatio" type="text"   style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
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
							
							<input  readonly id="QuarterBig" name="reservatio" type="text"   style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
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
							
							<input  readonly id="QuarterCurved" name="reservatio" type="text"   style="width:130px;padding:0;height:22px;display:inline-block;"  maxlength="20"class="day-Custom form-control" 
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