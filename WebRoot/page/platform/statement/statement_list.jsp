<%@page import="cn.tcl.platform.excel.actions.ImportExcelAction"%>
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
	int country =Integer.parseInt( WebPageUtil.getLoginedUser().getPartyId());
	String userPartyIds = "("+WebPageUtil.loadPartyIdsByUserId()+")";
	boolean isAdmin=WebPageUtil.isHAdmin();
	String roleName=WebPageUtil.getLoginedUser().getRoleId();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.bb'/></title>
	<link rel="stylesheet" href="<%=basePath%>css/redmond/jquery-ui-1.7.1.custom.css" type="text/css" title="ui-theme" />
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/sdmenu.js" ></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/css.css"/>
	<script>
		var my_login_id='<%=party%>';
		var isHQRole='<%=isHQRole%>';
		var loginCountry='<%=loginCountry%>';
		var country='<%=country%>';
		var partyList="<%=userPartyIds%>";
		var isAdmin="<%=isAdmin%>";
		var roleName="<%=roleName%>";
		
	</script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/baobiaozidingyi.css"/>
	<script src="<%=basePath%>js/echarts/echarts.js" ></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/UtilCommonFunction.js" ></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/statement.js" ></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/sta_common.js" ></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/excel.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/bootstrap-select.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>css/bootstrap-select.css">
	<link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
    <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/statement/daterangepicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/statement/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/platform/statement/3.3.7-bootstrap.min.js"></script>
    <script>
    	$(document).ready(function(){
    		$(".main .KeyUpload-DownloadTemplates .aDefineTheKey").click(function(){
    			$("#DefineKey-cover").css('display','block');			
    			});
    		$("#DefineKey-cover .DefineKey-fr").click(function(){
    			$("#DefineKey-cover").css("display","none");				
    			});	
    	})
	</script> 
</head>
<body>
	<!--右侧主要内容-->
         <div class="main">
         	<!--定义区间-->
         	<div class="DefineInterval">
         		<!--Business Center-->
         		<% if("true".equals(isHQRole)){ %>
         		<span class="fl BusinessCenter"  tcl-text="statement.center" ></span>
         		<span class="fl BusCen">
         			<select id="center"></select>
         		</span>
         		<!--Country-->
         		<span class="fl Country"  tcl-text="statement.country"></span>
         		<span class="fl country">
         			<select  id="country">  <!-- <option value='5'>flb</option> -->  </select>
         		</span>
         		<%} %>
         		<!--Region-->
         		<span class="fl Region"   tcl-text="statement.region"></span>
         		<span class="fl region">
         			<select id="region"></select>
         		</span>
         		<!--办事处-->
         		<span class="fl Office"  tcl-text="statement.office"><!--办事处--></span>
         		<span class="fl office">
         			<select  id="office"></select>
         		</span>
         		
         		<!-- 门店等级 -->
         		<span class="fl Office"  tcl-text="statement.store"><!-- 门店等级 --></span>
         		 	<span class="fl office">
         				<select  id="storeLevel"></select>
         		</span> 
         		<!--业务人员--><!--业务经理、业务员-->
         		<span class="fl ServiceManager"   tcl-text="statement.businessPer" ><!--业务人员--></span>
				<div  class="fl servicemanager">   	
         		 <input id="business_Mana" userId = "" value="" type="text" style="width:100px;"></input>		
         			<div id="my_menu" style="display:none;"  class="sdmenu">
						<div id = "partyManager" ><span tcl-text=" statement.regionHead"></span></div>
						<div id = "salesman"><span tcl-text="statement.salesMan"></span></div>
						
					</div><!--my_menu end-->
	         	 </div>          		
         		<!--督导-->
         		<span class="fl Steering" tcl-text="statement.supervisor" ><!--督导--></span>
         		<span class="fl steering">
         			<select id="supervisor">
<!--             			<option value='jeffreynadera'>Test</option>      -->
         			</select>
         		</span>        		
         		<a href="#" id="searchs" class="fl search"  tcl-text="toolbar.search"></a>
         	</div><!--定义区间完结--> 
         	
         	<!--一键上传-->
         	<div class="KeyUpload-DownloadTemplates"  >
         		<div class="inb" id="adBu">
         			<span class="DownloadTemplates "  tcl-text="statement.dtf"><!--下载模板--></span>
	         		 <a id="flip" href="#" class="downloadtemplates" ></a>
         		</div>
         		<div class="inb">
         			<span class="DefineTheKey"  tcl-text="statement.corePC"><!--定义重点产品--></span>
	         		<a class = "aDefineTheKey"  onclick="seletCore()"></a> 
         		</div>
         		<div class="inb">
         			<span class="Bulkimport" tcl-text="statement.bulk" ><!--定义重点产品--></span>
	         		<a class="aBulkimport" href="#"></a>
         		</div>
         		<!-- <p id="upTe" class="inb" style="display: inline-block;text-indent: 0;"></p> -->
         	</div>
         	<ul id="panel">
     	
     			</ul>
         	<!--重点产品-->
         	<div id="DefineKey-cover" id="">
         		<div id="DefineKey">
	         		<p><span class="fl DefineKey-fl"  tcl-text="statement.coreProducts"></span><span class="fr DefineKey-fr">
	         		<button style="width:70px;color:#000000;" onclick="setCore()">OK<!-- <a href="#" onclick="setCore()"  tcl-text="statement.ok" ></a> --></button>   
	         		<button style="width:70px;color:#000000;">CLOSE</button>   
	         		</span></p >
				<div id="seriesA" style="margin-top: 32px;">
	         			<label   tcl-text="statement.seriesA" ></label>
					
				</div> 
				<hr />
				<div id="seriesB">
					<label tcl-text="statement.seriesB"> B：</label>
					
				</div>
				<hr />
				<div id="seriesC">
					<label  tcl-text="statement.seriesC">Series C：</label>
					
				</div>
				<hr />
				<div id="seriesD">
					<label  tcl-text="statement.seriesD">Series D：</label>
					
				</div>
				<hr />
				<div id="seriesE">
					<label  tcl-text="statement.seriesE">Series E：</label>
					
				</div>
				<hr />
				<div id="seriesF">
					<label  tcl-text="statement.seriesF">Series F：</label>
					
				</div>
				<hr />
				<div id="seriesG">
					<label tcl-text="statement.seriesG" >Series G：</label>
					
				</div>
				<hr />
				<div id="seriesH">
					<label  tcl-text="statement.seriesH">Series H：</label>
				</div>
				<hr />
				<div id="seriesI">
					<label tcl-text="statement.seriesI">Series I：</label>
				</div>
				<hr />
				<div id="seriesJ">
					<label tcl-text="statement.seriesJ">Series J：</label>
				</div>
				<hr />
				<div id="seriesK">
					<label tcl-text="statement.seriesK">Series K：</label>
				</div>
				<hr />
				<div id="seriesL">
					<label tcl-text="statement.seriesL">Series L：</label>
				</div>
				<hr />
				<div id="seriesM">
					<label tcl-text="statement.seriesM">Series M：</label>
				</div>
				<hr />
				<div id="seriesN">
					<label tcl-text="statement.seriesN">Series N：</label>
				</div>
				<hr />
				<div id="seriesO">
					<label tcl-text="statement.seriesO">Series O：</label>
				</div>
				<hr />
				<div id="seriesP">
					<label tcl-text="statement.seriesP">Series P：</label>
				</div>
				<hr />
				<div id="seriesQ">
					<label tcl-text="statement.seriesQ">Series Q：</label>
				</div>
				<hr />
				<div id="seriesR">
					<label tcl-text="statement.seriesR">Series R：</label>
				</div>
				<hr />
				<div id="seriesS">
					<label tcl-text="statement.seriesS">Series S：</label>
				</div>
				<hr />
				<div id="seriesT">
					<label tcl-text="statement.seriesT">Series T：</label>
				</div>
				<hr />
				<div id="seriesU">
					<label tcl-text="statement.seriesU">Series U：</label>
				</div>
				<hr />
				<div id="seriesV">
					<label tcl-text="statement.seriesV">Series V：</label>
				</div>
				<hr />
				<div id="seriesW">
					<label tcl-text="statement.seriesW">Series W：</label>
				</div>
				<hr />
				<div id="seriesX">
					<label tcl-text="statement.seriesX">Series X：</label>
				</div>
				<hr />
				<div id="seriesY">
					<label tcl-text="statement.serieY">Series Y：</label>
				</div>
				<hr />
				<div id="seriesZ">
					<label tcl-text="statement.seriesZ">Series Z：</label>
				</div>
				<hr />
	         	</div>
         	</div>
         	<!--报表下载-->
     		<p class="reportdownload"   tcl-text="statement.downloadStatements"><!--报表下载-->Download Statements：</p>         	
         	<div class="ReportDownload">         	
         		
         		<!--日报-->
         		<div class="ReportDownload-two" id="dayRemove">         			
         			<div>
     					<img src="images/statement/icon-day.png"/>
     					<p tcl-text="statement.daily"><!--日报-->Daily Reports</p>	 
     					<!-- <input class="fl day-Custom" type="text" id="rangeA" value="" /> -->
<!--      					<input type="text" readonly style="width: 65%;" name="reservation" id="reservationDaily" class="fl day-Custom form-control" /> 
 -->         			 <input  id="reservationDaily"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="fl day-Custom form-control"/>
         			
         				<a href="#" class="fl download" id="daily" onclick="getdata(this)"></a>
         			</div>         			
         		</div>	
         		<!--周报-->
         		<div class="ReportDownload-two" id="weekRemove">         			
         			<div>
     					<img src="images/statement/icon-week.png"/>
     					<p  tcl-text="statement.weekly"><!--周报-->Weekly Reports</p>
     					<input type="text" onclick="bb()" readonly style="width: 65%;" name="reservation" id="reservationWeekly" class="fl day-Custom form-control" /> 
         				<a href="#" class="fl download" id="weekly" onclick="weekV(this)" ></a>
         			</div>         			
         		</div>	
         		<!--月报-->
         		<div class="ReportDownload-two">         			
         			<div>
     					<img src="images/statement/icon-month.png"/>
     					<p tcl-text="statement.monthly"><!--月报-->Monthly Reports</p>
     					 <input  id="reservationMonthly"  readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="fl day-Custom form-control"/>

         				<a href="#" class="fl download"  id="monthly" onclick="getdata(this)"></a>
         			</div>         			
         		</div>	
         		<!--季报-->
         		<div class="ReportDownload-two">         			
         			<div>
     					<img src="images/statement/icon-jidu.png"/>
     					<p  tcl-text="statement.quarterly"><!--季报-->Quarterly Reports</p>	
     					<input class="fl day-Custom form-control" readonly id="reservationQuarterly" name="reservatio" 
         				 onclick="WdatePicker({dateFmt:'yyyy-QM', isQuarter:true, isShowOK:false,disabledDates:['....-0[5-9]-..','....-1[0-2]-..'], startDate:'%y-02-01' })" type="text" />
         				<a href="#" class="fl download"  id="quarterly"  onclick="getdata(this)"></a>
         			</div>         			
         		</div>	
         		<!--年报-->
         		<div class="ReportDownload-two">         			
         			<div>
     					<img src="images/statement/icon-year.png"/>
     					<p  tcl-text="statement.annual"><!--年报-->Annual Reports</p>	
     				<!-- 	<input type="text" readonly style="width: 65%;" name="reservation" id="reservationAnnual" class="fl day-Custom form-control" />  -->
         			<input id="reservationAnnual" name="reservatio" type="text"  readonly style="width: 65%;"  maxlength="20"class="fl day-Custom form-control" 
				onfocus="WdatePicker({dateFmt:'yyyy',isShowClear:true});" />
         			
         				<a href="#" class="fl download"  id="annual" onclick="getdata(this)"></a>
         			</div>         			
         		</div>	
         	
         		<!--自定义报表-->
         		<div class="ReportDownload-two">         			
         			<div>
     					<img src="images/statement/PCbaobiao_07.png"/>
     					<p  tcl-text="statement.custom"><!--自定义报表-->Custom Download</p>
     					<input onclick="aa()" type="text" readonly style="width: 65%;" name="reservation" id="reservationGeneral" class="fl day-Custom form-control" /> 
         				<a href="#" class="fl download"  onclick="getdata(this)"  id="custom" ></a>
         			</div>         			
         		</div>	
         		
         	</div><!--报表下载完结--> 
         	
         	<!--总览图-->
<%--      		<p class="overviewmap" > <span tcl-text="statement.overview"></span>
     			<select id="instrument">
     			<option value="week"  tcl-text="statement.week">Week</option>
					<option value="month" tcl-text="statement.month" >Month</option>
					<option value="quarter" tcl-text="statement.quarter">Quarter</option>
					<option value="year"  tcl-text="statement.year">Year</option>		
     			</select>
     		</p>         	
         	<div class="OverviewMap">
         		<div class="OverviewMap-one">         			
         			<div>
     					<p  tcl-text="statement.ofVolume"><!--销售数量总览图-->Overview Map Of Sales Volume
         					<a class="fr" href="#" id="volumeAch"  onclick="exportVolumeAch()"></a>     						
     					</p>						
         				<p>
         				<!--计划目标--><span tcl-text="statement.pt" >Planned Target: </span>
         				<span id="volumeSat">
         				</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!--实际完成-->
         				<span tcl-text="statement.ach">Actual Achievement：
         				</span><span id="volumeSale"></span>
         				</p>
     					<!-- <div id="myChart">
     						<img class="" src="images/statement/zonglan_05.png"/ style="width: 100%;height: 100%;">
     					</div>  -->  
     					<div id="myChart" name="amount"></div>

						<!-- 加载gif -->
						<div id="loadingAllSaleVolume" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
							<div> 
								<table border="0"> 
									<tr>
										<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
									</tr> 	 
								</table> 
							</div> 
						</div> 
						 	   					
         			</div>         			
         		</div>	
         		<div class="OverviewMap-two">
         			<div>
         				<p tcl-text="statement.ofValue" ><!--销售金额总览图-->Overview Map Of Sales Revenue<a class="fr" href="#"  onclick="exportValueAch()"></a></p>
         				
         				<p>
         				<!--计划目标--><span tcl-text="statement.pt" >Planned Target: </span>
         				<span id="revenueSat">
         				</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!--实际完成-->
         				<span tcl-text="statement.ach">Actual Achievement：
         				</span><span id="revenueSale"></span>
         				</p>
         				       				
     					<!-- <div id="myChart">
     						<img class="myChart" src="images/statement/zonglan_051.png"/ style="width: 100%;height: 100%;">
     					</div> -->
     					<div id="myChart" name="money"></div>
     					
     					<!-- 加载gif -->
						<div id="loadingAllSaleRevenue" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
							<div> 
								<table border="0"> 
									<tr>
										<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
									</tr> 	 
							
								</table> 
							</div> 
						</div> 
         			</div>   
         		</div>         		
         	</div><!--总览图完结-->
         	
         	<!--销售排名-->
         	<p  class="Salesranking">
         	<span tcl-text="statement.ofRate"></span>
         		<select id = 'sales_rank'>
     				<option  tcl-text="statement.regionHead"  value="businessManager"><!--业务经理-->Regional head</option>
					<option tcl-text="statement.salesMan"  value="salesman"><!--业务员-->Salesman</option>
					<option  tcl-text="statement.supervisor"  value="supervisor"><!--督导-->Supervisor</option>
     			</select>
         	</p>
         	<div class="Salesranking-manager">         			
     			<div id="myChart" name="sales_rank">
					<!-- <img class="myChart" src="images/statement/xiaoshoupaiming (2).png"/> -->
					
					<!-- 加载gif -->
					<div id="loadingSalesRank" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
						<div> 
							<table border="0"> 
								<tr>
									<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
								</tr> 	 
						
							</table> 
						</div> 
					</div> 
     			</div>
     			
     			
     			
     			
<!--      			<a class="" href="#" onclick="exportAchievingRate()" ></a>			     			       			 -->
     		</div><!--销售排名结束-->
     		<!--渠道销售进度-->
     		<p     class="Salesprogress">
     		<span tcl-text="statement.progress"></span>
     			<select id="area">
<!--      				<option tcl-text="statement.channels" value="channels">渠道Channels</option> -->
					<option tcl-text="statement.stores" value="stores"><!--门店-->Stores</option>			
					<option tcl-text="statement.region" value="region" selected = selected><!--区域-->Region</option>				
     			</select>
     			
				<select id='salesRanking' >
					<option value="week" tcl-text="statement.week"></option>
					<option value="month" tcl-text="statement.month"></option>
					<option value="quarter" tcl-text="statement.quarter"></option>
					<option value="year" tcl-text="statement.year"></option>
     			</select>     		
     		</p>
     		<div class="salesprogress">
         		<div class="salesprogress-channel" id="myChart" name="channel_store">          			    					
<!--  					 <img class="myChart" src="images/statement/qudaoxiaoshoujindu.png"/>   -->

					<!-- 加载gif -->
					<div id="loadingAreaSales" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
						<div> 
							<table border="0"> 
								<tr>
									<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
								</tr> 	 
						
							</table> 
						</div> 
					</div> 
         		</div>
<!--          		<a class="" href="#"></a>	         		       		 -->
       		</div><!--销售进度结束-->
       		
       		<!--核心产品-->
     		<p class="Coreproduct"> 
     		<span tcl-text="statement.coreProducts"></span>
     			<select id='codeTdate' name='1'>
					<option value="month" tcl-text="statement.month">Month</option>
					<option value="quarter" tcl-text="statement.quarter">Quarter</option>
     			</select>
     			
     			<select id = 'majorFunction'><!-- 功能 --></select> 
     			<select id="majorSeries"><!-- 系列 --></select> 
     			<select id='majorSize'><!-- 尺寸 --></select> 
     			<select id="id_select" class="selectpicker bla bla bli" multiple data-live-search="true">
	     			<!-- <option>cow</option>
				    <option>bull</option>
				    <option>ASD</option>
				    <option selected>Bla</option>
				    <option>Ble</option>
				    </optgroup> -->
				 </select>
				<button onclick='loadItemSaleInfo(1)'>Query</button>
     		</p>
     		
     		<div class="Coreproduct-Chart">         			
     			<div id="myChart">
				<div class="myChart" name="code" style="width: 100%;height: 100%;"></div>
				<!-- 加载gif -->
					<div id="loadingCodeSalesRank" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
						<div> 
							<table border="0"> 
								<tr>
									<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
								</tr> 	 
						
							</table> 
						</div> 
					</div> 
     			</div>
     			<!-- <a class="" href="#"></a> -->			     			       			
     		</div><!--核心产品结束-->
       		
       		<!--销售趋势-->
       		<p class="Salestrends">
       		<span tcl-text="statement.trends"></span>
       		<div class="salestrends">
         		<div class="OverviewMap-Singprod">         			
         			<div>
     					<p><span tcl-text="statement.singleProduct"></span>
     						<select id='single' name='2'>
								<option value="month" tcl-text="statement.month">Month</option>
								<option value="quarter" tcl-text="statement.quarter">Quarter</option>
			     			</select>
			     			<select id="id_item" class="selectItem bla bla bli" multiple data-live-search="true">
			     			</select>
			     			<button onclick='loadItemSaleInfo(2)'>Query</button>
         					<!-- <a class="fr" href="#"></a> -->			     			
     					</p>
     					<div id="myChart">
<!--      						<img class="myChart" src="images/statement/danpinqushizhou.jpg"/ style="width: 100%;height: 100%;">     						 -->
     						<div class="myChart" name="item" style="width: 100%;height: 100%;"></div>
     						
     				<!-- 加载gif -->
					<div id="loadingSModelSalealesRank" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
						<div> 
							<table border="0"> 
								<tr>
									<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
								</tr> 	 
						
							</table> 
						</div> 
					</div> 
     					</div>
         			</div>         			
         		</div>	
         		<div class="OverviewMap-Differentsize">
         			<div>
         				<p><!--不同尺寸段电视销售趋势--><!-- sales trends of TVs with different sizes -->
         				<span tcl-text="statement.trendsOfSize"></span>
         					<select id="sizeSale">
								<option value="year" tcl-text="statement.month" >Month</option>
								<option value="quarter" tcl-text="statement.quarter">Quarter</option>
			     			</select>
			     			<select id="id_size" class="selectSize bla bla bli" multiple data-live-search="true">
			     			</select>
							<input type="text" style="width:130px" id="id_size"  data-options="multiple:true"/>
			     			<button onclick = 'differentSizeTV();'>Query</button>
<!--          					<a class="fr" href="#"></a>			     			 -->
         				</p>
         				<div id="myChart">
       						<div class="myChart" name="size"  style="width: 100%;height: 100%;"></div>
       						
       						<div id="loadingSize" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
								<div> 
									<table border="0"> 
										<tr>
											<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
										</tr> 	 
								
									</table> 
								</div> 
							</div> 
							         					  
         				</div>
         			</div>   
         		</div>   
         		<div class="OverviewMap-function">
         			<div>
         				<p><!--不同功能电视销售趋势--><!-- Different Functions -->
         				<span tcl-text="statement.differentFunctions"></span>
         					<select id="selectFunctionSaleTime">
								<option value="year"  tcl-text="statement.month">Month</option>
								<option value="quarter" tcl-text="statement.quarter">Quarter</option>
			     			</select>
<!--          					<a class="fr" href="#"></a>			     			 -->
         				</p>
         				<div id="myChart" >
      						<div class="myChart" name="function1" style="width: 100%;height: 100%;"></div>
      						
      						<div id="loadingFunction" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
							<div> 
								<table border="0"> 
									<tr>
										<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
									</tr> 	 
							
								</table> 
							</div> 
							
						</div> 
      					</div>
						
         			</div>   
         		</div>  
<!--          		<div class="OverviewMap-growthrate"> -->
<!--          			<div> -->
<!--          				<p  tcl-text="statement.trendsOfRate">增长率趋势图Tendency Charts Of Growth Rates</p> -->
<!--      					<img class="myChart" src="images/statement/zengzahnglv.png" style="width: 100%;height: 100%;">  -->
<!--          			</div>    -->
<!--          		</div>   -->
    		</div><!--销售趋势结束-->
         
     		<!--对比分析-->
     		<p class="ComparativeAnalysis"><!--对比分析--><!-- Contrast Analysis -->
     		<span tcl-text="statement.contrastAnalysis"></span>
     			<select id = 'analysis'>
     				<!-- <option value="week">Week</option>  -->
					<option value="year" tcl-text="statement.month">Month</option>
					<option value="quarter"  tcl-text="statement.quarter">Quarter</option>
     			</select>
     			<select id="volOrRev">     				
					<option value="Volume"  tcl-text="statement.volume">Volume</option>
					<option value="Revenue" tcl-text="statement.revenue">Revenue</option>
     			</select>
     			
     			<select id = 'contrastRole'>
<!--      				<option value="region" tcl-text="statement.regionHead">业务经理Region head</option> -->
<!-- 					<option value="salesman" tcl-text="statement.salesMan">业务员Salesman</option> -->
<!-- 					<option value="supervisor"  tcl-text="statement.supervisor">督导Supervisor</option> -->
     			</select>
     		</p>
     		
	     	<div class="comparan">         		
         		<div class="comparan-Differentregions">         			
         			
     					<p  tcl-text="statement.timeInRegion"><!--同区域不同时间段对比-->Different&nbsp;Time&nbsp;Periods&nbsp;Contrast&nbsp;In&nbsp;A&nbsp;Region</p>	
     					<div id="myChart" name="analysis" style="width:100%;height:80%"></div>
     					
     					<div id="loadingContrast" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
							<div> 
								<table border="0"> 
									<tr>
										<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
									</tr> 	 
							
								</table> 
							</div> 
						</div> 
<!--      					<a class="fr" href="#"></a>     					 -->
         			         			
         		</div>		         		
			</div><!--对比分析结束-->
			
			<!--人员效率-->
			<p class="PersonnelEfficiency">
			<span tcl-text="statement.personnelEfficiency"></span><!--人员效率--><!-- Personnel Efficiency -->
				<select id="per_eff_date">
					<option value="month"  tcl-text="statement.month">Month</option>
					<option value="quarter" tcl-text="statement.quarter">Quarter</option>
     			</select>
     			<select id="per_eff_voOrRe">
     				<option value="Volume"  tcl-text="statement.volume">Volume</option>
					<option value="Revenue" tcl-text="statement.revenue">Revenue</option>
     			</select>
     			<select id='autoNamesAttr'></select>
     			<select id='sizeFunction'></select>
			</p>
			<div class="PersonnelEfficiency-rank">         			
     			<div id="myChart" >     				
     			
     			<!-- 加载gif -->
					<div id="loadingStaffEfficiency" style="width:5rem; height:5rem;position: absolute;left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99;"> 
						<div> 
							<table border="0"> 
								<tr>
									<td><img src="<%=basePath%>images/statement/loading.gif" style="width: 100px;height: 100px;z-index: 9999;"/></td>
								</tr> 	 
						
							</table> 
						</div> 
					</div> 
					
					<div class="myChart" name="role_sale_info" style="width: 100%;height: 100%;"></div>
					<span class="fr-1"  onclick="selectUserSaleNumber('Region head')"></span> 
					<span class="fr-1-1"  tcl-text="statement.regionHead">Regional head</span>
					<span class="fr-2" onclick="selectUserSaleNumber('Supervisor')"></span> 
					<span class="fr-2-1"  tcl-text="statement.supervisor">Supervisor</span>
					<span class="fr-3" onclick="selectUserSaleNumber('Salesman')"></span> 
					<span class="fr-3-1" tcl-text="statement.salesMan">Salesman</span>   				 
<!--  					<a class="fr" href="#"></a>						 -->
     			</div>		     			       			
    		</div><!--人员效率结束-->     	
         	
         </div><!--右侧内容完结-->
	</div>
	<input id='isSorS' type="hidden"></input><!-- 用于储存业务员下拉选择的是业务经理还是业务员 -->
	<div class="Bulk_upload_content">
		<p   ><span  tcl-text="statement.bulk"></span><i class="lead_in" onclick="importByPH()"></i> <i class="Add"></i> <i class="Shut_down fr"></i></p>
		<div class="Bulk_upload_tb" >
			<table>
				<thead>
					<tr>
						<th>Country</th>
						<th>Shop</th>
						<th>Date</th>
						<th>Type</th>
						<th>Model</th>
						<th>Quantity</th>
						<th>Price</th>
						
						<th style="width: 20px;"></th>
					</tr>
				</thead>
				<tbody  id="tbody">
				
				</tbody>
			</table>
		</div>
		<div class="Error_message" style="display: none">
			<p class="Error_message_title"  ><span  tcl-text="statement.error" ></span><i></i></p >
						<hr />
			
			<div id="msg">
			</div>
		</div> --%>
		<div id='loadingImportByInput' style='width:5rem; height:5rem;position: absolute;display:none; left: calc(50% - 50px);top: calc(50% - 30px);z-index: 99999;'>
					<div> 
							<table border='0'>  
								<tr> 
								<td><img src='<%=basePath%>/images/statement/loading.gif' style='width: 100px;height: 100px;z-index: 10000;'/></td>
								</tr>  	 
						
							</table> 
						</div>
				</div>
	</div>
	
	
	
</body>
</html>