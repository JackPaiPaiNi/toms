<%@page import="cn.tcl.platform.role.vo.Role"%>
<%@page import="cn.tcl.platform.role.service.IRoleService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.tcl.platform.party.service.IPartyService"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.tcl.common.WebPageUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String partyId=WebPageUtil.getLoginedUser().getPartyId();
String roleId =WebPageUtil.getLoginedUser().getRoleId();
	ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(application);
	IPartyService partyService=(IPartyService)context.getBean("partyService");
	IRoleService IRoleService = (IRoleService)context.getBean("roleService");
	String countryId=partyService.selectPartyById(partyId).getCountryId();
	
	Role role = IRoleService.getRoleName(roleId);
	String roleName="";
	if(role!=null && !role.equals("")){
		 roleName = role.getRoleName();
	}
	
	String countryName="HQ";
	String regionId="999";
	String regionName="HQ";
	if(!countryId.equals("999"))
	{
		regionId=partyService.selectPartyById(countryId).getParentPartyId();
		regionName=partyService.selectPartyById(regionId).getPartyName();
		countryName=partyService.selectPartyById(countryId).getPartyName();
	}

%>


<!DOCTYPE html>
<html>
  <head>   
    <title><s:text name='permission.labelkey.ksqkhz'></s:text></title>
    <script type="text/javascript" src="<%=basePath%>js/platform/examination/test_statistics.js"></script>    
   <script type="text/javascript"  src="<%=basePath%>My97DatePicker/WdatePicker.js"></script>
   
    <style type="text/css">
  		select{height: 22px;}
  	</style>
<script>
		var regionId="<%=regionId%>";
		var countryId="<%=countryId%>";
		var regionName="<%=regionName%>";
		var countryName="<%=countryName%>";
		var roleName="<%=roleName%>";
</script>
  </head>  
  <body>
     <table id="learnList"></table>
     	<div id="coursetb" class="easyui-panel" 	style="background:#f2f6f8;border-bottom:0px;"  data-options="collapsible:false">
		<div class="main">        	
        	<!--查询条件-->
        	<div class="Query-Condition">
        		<span>&nbsp;&nbsp;&nbsp;&nbsp;Column</span>
        		<select id="column" name="column">
        		</select>
        		
        		
        			<span>&nbsp;&nbsp;&nbsp;&nbsp;Sub-column</span>
	        		<select id="subcolumn" name="subcolumn">
	        			 <option value="0">Please Select The Sub-Column</option>
	        		</select>
	        		<select id="subtocolumn" name="subtocolumn">
						 	<option value="0">Please Select The Sub-Column</option>
					</select>
        			 Start:<input class="startdate" type="text" onFocus="WdatePicker({lang:'en',dateFmt:'yyyy-MM-dd HH:mm:ss'})">To:<input class="enddate" type="text" onFocus="WdatePicker({lang:'en',dateFmt:'yyyy-MM-dd HH:mm:ss'})">  
        		<!-- 业务区域,国家 -->
        		<%if(countryId.equals("999")){ %>
	     		<div class="regioncountry">&nbsp;&nbsp;&nbsp;&nbsp;Region-Country
	     			<select id="region" name="region">
	     				<option value="0">Please Select The Business Center</option>	
	     				<option value="-1">All</option>  				
	     			</select>   			
	     			<select id="country" name="country">
	     				<option value="">Please Select The Country</option>
	     			</select>
	     		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search" id="searchBt" onclick="doSearch();" tcl-text="toolbar.search"></a>
	     		<div class="tcl-gridbar-separator"></div>
	     		<a href="#" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" onclick="exportRusPaperExcel();"   tcl-text="test.downLearnList" ></a> 
	     		</div>
	     		<%}else if(countryId.equals("304")){ %>
	     		<div class="regioncountry" style="display:none;">&nbsp;&nbsp;&nbsp;&nbsp;Region-Country
	     			<select id="region" name="region">
	     				<option value="0">Please Select The Business Center</option>	
	     			</select>   			
	     			<select id="country" name="country">
	     				<option value="">Please Select The Country</option>
	     			</select>	     			
	     		</div>
	     		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search" id="searchBt" onclick="doSearch();" tcl-text="toolbar.search"></a>
	     		<div class="tcl-gridbar-separator"></div>
	     		<a href="#" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" onclick="exportRusPaperExcel();"   tcl-text="test.downLearnList" ></a> 
	     		<%}else{ %>
	     			<div class="regioncountry" style="display:none;">&nbsp;&nbsp;&nbsp;&nbsp;Region-Country
	     			<select id="region" name="region">
	     				<option value="0">Please Select The Business Center</option>	
	     			</select>   			
	     			<select id="country" name="country">
	     				<option value="">Please Select The Country</option>
	     			</select>	     			
	     		</div>
	     		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search" id="searchBt" onclick="doSearch();" tcl-text="toolbar.search"></a>
	     		<div class="tcl-gridbar-separator"></div>
	     		<a href="#" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" onclick="exportRusPaperExcel();"   tcl-text="test.downLearnList" ></a>
	     		<%} %>
        	</div>
       	</div> 
	</div>
  </body>
</html>
