<%@page import="cn.tcl.platform.sale.service.ISaleService"%>

<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.tcl.platform.sale.dao.ISaleTargetDao"%>
<%@page import="cn.tcl.common.WebPageUtil"%>
<%@page import="cn.tcl.platform.sale.vo.SaleTarget"%>
<%@page import="cn.tcl.platform.excel.service.impl.ImportPHExcelServiceImpl"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String countryId=WebPageUtil.getLoginedUser().getPartyId();
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String party = (String) request.getSession().getAttribute("loginUserId");
String partyId=WebPageUtil.getLoginedUser().getPartyId();
ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());

ISaleService saleDao=(ISaleService)context.getBean("saleService");
String targetType=saleDao.selectSOType(partyId);

%>

<html>
<head>
	<meta charset="UTF-8">
	<title><s:text name='permission.labelkey.tvmb'/></title>
	<%--  <script type="text/javascript" src="<%=basePath%>js/platform/sale/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/sale/jquery-1.4.2.min.js"></script> --%>
	<script type="text/javascript" src="<%=basePath%>js/platform/sale/mubiaoshedingzdy.js"></script>
	
	 <script type="text/javascript" src="<%=basePath%>js/platform/sale/jquery.page.js"></script> 
	 <script type="text/javascript" src="<%=basePath%>js/platform/sale/newtargetlist.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/mubiaosheding.css"/>
	
	<script>var my_login_id='<%=party%>';</script>
	<script>var partyId='<%=partyId%>';</script>
	<script type="text/javascript">var Type_target='<%=targetType%>';</script>


	<style type="text/css">
		tr.highlight{background:#fff7d6!important;}
		tr.selected {background:#f6cd8f!important;}
		a{ text-decoration:none;}
		.tcdPageCode{width: -webkit-calc(100% - 300px);width: -moz-calc(100% - 300px);width: calc(100% - 300px); text-align: center;color: #ccc;position: fixed;bottom: 0;background-color: #fafafa;padding: 5px 0;}
		.tcdPageCode a{display: inline-block;color: #428bca;display: inline-block;height: 24px;	line-height: 24px;	padding: 0 10px;border: 1px solid #ddd;	margin: 0 2px;border-radius: 4px;vertical-align: middle;}
		.tcdPageCode a:hover{text-decoration: none;border: 1px solid #428bca;}
		.tcdPageCode span.current{display: inline-block;height: 24px;line-height: 24px;padding: 0 10px;margin: 0 2px;color: #fff;background-color: #428bca;	border: 1px solid #428bca;border-radius: 4px;vertical-align: middle;}
		.tcdPageCode span.disabled{	display: inline-block;height: 24px;line-height: 24px;padding: 0 10px;margin: 0 2px;	color: #bfbfbf;background: #f2f2f2;border: 1px solid #bfbfbf;border-radius: 4px;vertical-align: middle;}

	</style>
</head>
<body>
        <!--主要内容-->
        <div class="main">
         	 <p class="DefineInterval">
         	 	<span tcl-text="saletarget.list.title" style="font-weight: 600;"></span>
         	 </p>
         	<a href="<%=basePath%>download/TV Targets.xlsx" class="Download" ><span tcl-text="target.downloadtemplate"></span></a><!--下载模板-->
    	
			<%
    		   ImportPHExcelServiceImpl p = (ImportPHExcelServiceImpl)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("importPHExcelService");
			   String type = p.selectSoType(countryId);
    		  if("admin".equals(type)){
    		%>
    		<a href="#" class="Up"  onclick="importStoreTarget(4)" plain="true"><span tcl-text="shop_target.upload"></span></a>
    		<a href="#" class="Up"  onclick="importStoreTarget(3)" plain="true"><span tcl-text="channel_target.upload"></span></a>
    		<a href="#" class="Up"  onclick="importStoreTarget(1)" plain="true"><span tcl-text="country_target.upload"></span></a>
			<%
    		  }else if("0".equals(type)){
			%>
			<a href="#" class="Up"  onclick="importStoreTarget(1)" plain="true"><span tcl-text="country_target.upload"></span></a>
			<%
    		  }else if("1".equals(type)){
			%>
			<a href="#" class="Up"  onclick="importStoreTarget(3)" plain="true"><span tcl-text="channel_target.upload"></span></a>
			<%
    		  }else if("2".equals(type)){
			%>
			<a href="#" class="Up"  onclick="importStoreTarget(4)" plain="true"><span tcl-text="shop_target.upload"></span></a>
			<%}%>      		
    		<%if(targetType.equals("1")){%>
    			
     		
			<!--渠道目标-->
     		<p class="Headers-unified" id="ChannelTarget" style="margin:0;"><span tcl-text="target.channel"></span>
     		<span>Year</span>
     			<select id="year" style="font-size: 14px;"></select>
     		
     		<span>Month</span>
     			<select id="month" style="font-size: 14px;">
     			
     			</select>	
     			<%-- <a href="<%=basePath%>download/channelTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importChannelTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-item" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="channel">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.channels"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         				
	         			</tr>
	         			<tr>         					
         					        					
         					<th class=channel-sum><span tcl-text="target.volume"></span></th>
	         				<th class=channel-sum><span tcl-text="target.revenue"></span></th>
	         				<th class=channel-sum><span tcl-text="target.volume"></span></th>
	         				<th class=channel-sum><span tcl-text="target.revenue"></span></th> 	
         				</tr>   
         			</thead>
         			<tbody  id="chan">
						<tr id="channellist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
     				</tbody>
         		</table>
     			<a  onclick="hideChan()" id="table-item-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showChan()" id="table-item-show" class="table-show" target="_self"></a>         		
     		</div> 
     		

			<!--分公司目标-->
     		<p class="Headers-unified" id="BranchTarget" style="margin:0;"><span tcl-text="target.country"></span>
     			<%-- <a href="<%=basePath%>download/branchTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importBranchTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Supervision" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="branch">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.countrys"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         			
	         			</tr>
	         			<tr>         					
         					<th class="branch_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.revenue"></span></th> 
         				</tr>   
         			</thead>
         			<tbody id="bran">          				
         				<tr id="branchlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	     	       				   				
         			</tbody>
         		
         		</table>
     			<a  onclick="hideBranch()" id="table-branch-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showBranch()" id="table-branch-show" class="table-show" target="_self"></a>
     		</div>
     		
			<!--导购员目标-->
 <%--     		<p class="Headers-unified" id="PromoterTarget" style="margin:0;">Promoter's Target
     			<a href="<%=basePath%>download/promoterTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importPromoterTarget()" plain="true"></a>
     		</p>
     		<!--表格-->
     		<div id="table-Busimana" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="promoter">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;">Promoter's</td>
	         				<td colspan="4">So Target</td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2">Basic Target</td>
	         				<td colspan="2">Challenge Target</td>
	         				
	         			</tr>
	         			<tr>         					
         					<th class="promoter_sum">Volume</th>
	         				<th class="promoter_sum">Revenue</th>
	         				<th class="promoter_sum">Volume</th>
	         				<th class="promoter_sum">Revenue</th> 		
         				</tr>   
         			</thead>
         			<tbody id="pro">          				
         				<tr id="promoterlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	   		   				
         			</tbody>
         			
         		</table>
     			<a href="" onclick="hidePromoter()" id="table-promoter-id" class="table-hidden" target="_self"></a>
     			<a href="#" onclick="showPromoter()" id="table-promoter-show" class="table-show" target="_self"></a>
     		</div> --%>
     		
			<!--督导目标-->
     		<p class="Headers-unified" id="MarketingSupervisorTarget" style="margin:0;"><span tcl-text="target.supervisor"></span>
     			<%-- <a href="<%=basePath%>download/supervisorTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importSupervisorTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Dealer" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="supervisor">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.supervisors"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				       					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         				
	         			</tr>
	         			<tr>         					
         					<th class="supervisor_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="supervisor_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="supervisor_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="supervisor_sum"><span tcl-text="target.revenue"></span></th> 		
         				</tr>   
         			</thead>
         			<tbody id="sup">          				
         				<tr id="supervisorlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	   				    				
         			</tbody>
         			
         		</table>
     			<a  onclick="hideSupervisor()" id="table-supervisor-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showSupervisor()" id="table-supervisor-show" class="table-show" target="_self"></a>
     		</div>
     		
			<!--业务员目标-->
     		<p class="Headers-unified" id="SalesmanTarget" style="margin:0;"><span tcl-text="target.salesman"></span>
     			<%-- <a href="<%=basePath%>download/salesmanTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importSalesmanTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Office" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="salesman">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.salesmans"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         				
	         			</tr>
	         			<tr>         					
         					<th class="salesman_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="salesman_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="salesman_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="salesman_sum"><span tcl-text="target.revenue"></span></th> 		
         				</tr>   
         			</thead>
         			<tbody id="sale">          				
         				<tr id="salesmanlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	   				    				
         			</tbody>
         			
         		</table>
     			<a  onclick="hideSalesman()" id="table-salesman-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showSalesman()" id="table-salesman-show" class="table-show" target="_self"></a>
     		</div>
     		
     		<!--业务经理目标-->
     		<p class="Headers-unified" id="BusinessManagerTarget" style="margin:0;"><span tcl-text="target.manager"></span>
     			<%-- <a href="<%=basePath%>download/businessManagerTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importManagerTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Branch" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="managers">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.managers"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         			       					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         			
	         			</tr>
	         			<tr>         					
         					<th class="managers_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="managers_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="managers_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="managers_sum"><span tcl-text="target.revenue"></span></th> 			
         				</tr>   
         			</thead>
         			<tbody id="man">          				
         				<tr id="managerslist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	   			    				
         			</tbody>
         				
         		</table>
     			<a  onclick="hideManager()" id="table-manager-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showManager()" id="table-manager-show" class="table-show" target="_self"></a>
     		</div>
    		<% }else if(targetType.equals("2")){ %>
         	<!--门店目标-->
     		<p class="StoresTarget" id="StoresTarget" style="margin-bottom:0;"><span tcl-text="target.store"></span>
     		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>Year</span>
     			<select id="year" style="font-size: 14px;"></select>
     		
     		<span>Month</span>
     			<select id="month" style="font-size: 14px;">
     			
     			</select>	
     		</p>
     		<!--表格-->
     		<div class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="store">
         			<thead >
         				<tr >
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.stores"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         				
	         			</tr>
	         			<tr > 
	         				        					
         					<th class=store-sum><span tcl-text="target.volume"></span></th>
	         				<th class=store-sum><span tcl-text="target.revenue"></span></th>
	         				<th class=store-sum><span tcl-text="target.volume"></span></th>
	         				<th class=store-sum><span tcl-text="target.revenue"></span></th> 
	         				
	         				        				
         				</tr>   
         			</thead >
         			<tbody  id="tbody">
         				<tr id="storelist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr >
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
     				</tbody>
     			
         		</table>
     			<a  onclick="hid()" id="table-store-id" class="table-hidden" target="_self"></a>
     			<a  onclick="sho()" id="table-store-show" class="table-show" target="_self"></a>
     		</div>
     		
			<!--渠道目标-->
     		<p class="Headers-unified" id="ChannelTarget" style="margin:0;"><span tcl-text="target.channel"></span>
     			<%-- <a href="<%=basePath%>download/channelTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importChannelTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-item" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="channel">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.channels"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         				
	         			</tr>
	         			<tr>         					
         					        					
         					<th class=channel-sum><span tcl-text="target.volume"></span></th>
	         				<th class=channel-sum><span tcl-text="target.revenue"></span></th>
	         				<th class=channel-sum><span tcl-text="target.volume"></span></th>
	         				<th class=channel-sum><span tcl-text="target.revenue"></span></th> 	
         				</tr>   
         			</thead>
         			<tbody  id="chan">
						<tr id="channellist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
     				</tbody>
         		</table>
     			<a  onclick="hideChan()" id="table-item-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showChan()" id="table-item-show" class="table-show" target="_self"></a>         		
     		</div> 
     		
     		<!--办事处目标-->
     		<p class="Headers-unified" id="OfficeTarget" style="margin:0;"><span tcl-text="target.office"></span>
     			<%-- <a href="<%=basePath%>download/officeTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importOfficeTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Pauleka" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="offices">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.offices"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         			
	         			</tr>
	         			<tr>         					
         					        					
         					<th class="offices-sum"><span tcl-text="target.volume"></span></th>
	         				<th class="offices-sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="offices-sum"><span tcl-text="target.volume"></span></th>
	         				<th class="offices-sum"><span tcl-text="target.revenue"></span></th> 	
         				</tr>   
         			</thead>
         			<tbody id="off">
         				<tr id="officelist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
     				</tbody>
         		</table>
     			<a  onclick="hideOffice()" id="table-offices-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showOffice()" id="table-offices-show" class="table-show" target="_self"></a>
     		</div>
     		
			<!--经营部目标-->
     		<p class="Headers-unified" id="RegionTarget" style="margin:0;"><span tcl-text="target.region"></span>
     			<%-- <a href="<%=basePath%>download/regionTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importRegTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Salesman" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="regions">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.regions"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         			         					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         			
	         			</tr>
	         			<tr>         					
         					<th class="regions-sum"><span tcl-text="target.volume"></span></th>
	         				<th class="regions-sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="regions-sum"><span tcl-text="target.volume"></span></th>
	         				<th class="regions-sum"><span tcl-text="target.revenue"></span></th> 			
         				</tr>   
         			</thead>
         			<tbody id="reg">          				
         				<tr id="regionlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr >
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	       				
         			</tbody>
         			
         		</table>
         		<a  onclick="hideRegion()" id="table-region-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showRegion()" id="table-region-show" class="table-show" target="_self"></a>
     		</div>
     		
			<!--分公司目标-->
     		<p class="Headers-unified" id="BranchTarget" style="margin:0;"><span tcl-text="target.country"></span>
     			<%-- <a href="<%=basePath%>download/branchTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importBranchTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Supervision" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="branch">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.countrys"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         			
	         			</tr>
	         			<tr>         					
         					<th class="branch_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.revenue"></span></th> 
         				</tr>   
         			</thead>
         			<tbody id="bran">          				
         				<tr id="branchlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	     	       				   				
         			</tbody>
         		
         		</table>
     			<a  onclick="hideBranch()" id="table-branch-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showBranch()" id="table-branch-show" class="table-show" target="_self"></a>
     		</div>
     		
			<!--导购员目标-->
 <%--     		<p class="Headers-unified" id="PromoterTarget" style="margin:0;">Promoter's Target
     			<a href="<%=basePath%>download/promoterTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importPromoterTarget()" plain="true"></a>
     		</p>
     		<!--表格-->
     		<div id="table-Busimana" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="promoter">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;">Promoter's</td>
	         				<td colspan="4">So Target</td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2">Basic Target</td>
	         				<td colspan="2">Challenge Target</td>
	         				
	         			</tr>
	         			<tr>         					
         					<th class="promoter_sum">Volume</th>
	         				<th class="promoter_sum">Revenue</th>
	         				<th class="promoter_sum">Volume</th>
	         				<th class="promoter_sum">Revenue</th> 		
         				</tr>   
         			</thead>
         			<tbody id="pro">          				
         				<tr id="promoterlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	   		   				
         			</tbody>
         			
         		</table>
     			<a href="" onclick="hidePromoter()" id="table-promoter-id" class="table-hidden" target="_self"></a>
     			<a href="#" onclick="showPromoter()" id="table-promoter-show" class="table-show" target="_self"></a>
     		</div> --%>
     		
			<!--督导目标-->
     		<p class="Headers-unified" id="MarketingSupervisorTarget" style="margin:0;"><span tcl-text="target.supervisor"></span>
     			<%-- <a href="<%=basePath%>download/supervisorTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importSupervisorTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Dealer" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="supervisor">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.supervisors"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				       					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         				
	         			</tr>
	         			<tr>         					
         					<th class="supervisor_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="supervisor_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="supervisor_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="supervisor_sum"><span tcl-text="target.revenue"></span></th> 		
         				</tr>   
         			</thead>
         			<tbody id="sup">          				
         				<tr id="supervisorlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	   				    				
         			</tbody>
         			
         		</table>
     			<a  onclick="hideSupervisor()" id="table-supervisor-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showSupervisor()" id="table-supervisor-show" class="table-show" target="_self"></a>
     		</div>
     		
			<!--业务员目标-->
     		<p class="Headers-unified" id="SalesmanTarget" style="margin:0;"><span tcl-text="target.salesman"></span>
     			<%-- <a href="<%=basePath%>download/salesmanTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importSalesmanTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Office" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="salesman">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.salesmans"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         				
	         			</tr>
	         			<tr>         					
         					<th class="salesman_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="salesman_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="salesman_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="salesman_sum"><span tcl-text="target.revenue"></span></th> 		
         				</tr>   
         			</thead>
         			<tbody id="sale">          				
         				<tr id="salesmanlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	   				    				
         			</tbody>
         			
         		</table>
     			<a  onclick="hideSalesman()" id="table-salesman-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showSalesman()" id="table-salesman-show" class="table-show" target="_self"></a>
     		</div>
     		
     		<!--业务经理目标-->
     		<p class="Headers-unified" id="BusinessManagerTarget" style="margin:0;"><span tcl-text="target.manager"></span>
     			<%-- <a href="<%=basePath%>download/businessManagerTarget.xlsx" class="Download"></a>
     			<a href="#" class="Up"  onclick="importManagerTarget()" plain="true"></a> --%>
     		</p>
     		<!--表格-->
     		<div id="table-Branch" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="managers">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.managers"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         			       					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         			
	         			</tr>
	         			<tr>         					
         					<th class="managers_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="managers_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="managers_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="managers_sum"><span tcl-text="target.revenue"></span></th> 			
         				</tr>   
         			</thead>
         			<tbody id="man">          				
         				<tr id="managerslist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	   			    				
         			</tbody>
         				
         		</table>
     			<a  onclick="hideManager()" id="table-manager-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showManager()" id="table-manager-show" class="table-show" target="_self"></a>
     		</div>
     		<%}else{%>
     			<!--分公司目标-->
     		<p class="Headers-unified" id="BranchTarget" style="margin:0;"><span tcl-text="target.country"></span>
     		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>Year</span>
     			<select id="year" style="font-size: 14px;"></select>
     		
     		<span>Month</span>
     			<select id="month" style="font-size: 14px;">
     			
     			</select>	
     		</p>

     		<!--表格-->
     		<div id="table-Supervision" class="Form-unified">
     			<table border="" cellspacing="" cellpadding="" id="branch">
         			<thead>
         				<tr>
         					<td rowspan="3" style="width: 15%;"><span tcl-text="target.countrys"></span></td>
	         				<td colspan="4"><span tcl-text="target.sotarget"></span></td>
	         				        					
	         			</tr>
	         			<tr>
	         				<td colspan="2"><span tcl-text="target.basic.target"></span></td>
	         				<td colspan="2"><span tcl-text="target.challenge.target"></span></td>
	         			
	         			</tr>
	         			<tr>         					
         					<th class="branch_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.revenue"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.volume"></span></th>
	         				<th class="branch_sum"><span tcl-text="target.revenue"></span></th> 
         				</tr>   
         			</thead>
         			<tbody id="bran">          				
         				<tr id="branchlist">
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>
         				<tr>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         					<td></td>
         				</tr>	     	       				   				
         			</tbody>
         		
         		</table>
     			<a  onclick="hideBranch()" id="table-branch-id" class="table-hidden" target="_self"></a>
     			<a  onclick="showBranch()" id="table-branch-show" class="table-show" target="_self"></a>
     		</div>
     		<%} %>
     		<div id="parents">
     		</div>
     		
     		<!--分页-->
     		<!--<div class="tcdPageCode"></div>
         	<script>
			    $(".tcdPageCode").createPage({
			        pageCount:100,
			        current:1,
			        backFn:function(p){
			            //console.log(p);
			        }
			    });
			</script>-->
       	</div>        
	
</body>
</html>
