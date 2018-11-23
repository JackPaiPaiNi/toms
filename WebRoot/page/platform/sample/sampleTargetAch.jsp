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
	<meta charset="utf-8">
	<title><s:text name='sample.targetAch.title'/></title>
	<script>var my_login_id='<%=party%>';</script>
	<script src="<%=basePath%>js/platform/sample/sampleTargetAch.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
     <script type="text/javascript" src="<%=basePath%>js/platform/statement/daterangepicker.js"></script>
     <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
</head>
<body>
	<table id="samplesListTable"  name="gridTable"></table>
	   
	<div id="stargettb" style="padding:2px 5px;">
	    <div style="margin-bottom:5px">
	  <a onclick="exportTargetAch()"   href="#<%-- <%=basePath%>download/Sample Target.xlsx --%>" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="sampleTarget.downloadStatements"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="showSumDiv();" id="searchBt" tcl-text="sampleTarget.sum">&nbsp;</a>
			
	  </div>
		<div>
			<span tcl-text="sample.list.th.datadate"></span>
		<input type="text" style="width:100px"  onclick="WdatePicker({dateFmt:'yyyy-MM'})" name="datadate" id="datadate"/>
			
		<span tcl-text="district.countryName"></span>:
	    	<select class="easyui-combobox" id="tcl_country" style="width:100px"  data-options="editable:false">
		    </select>
		    <span tcl-text="shop.list.search.party"></span>
			<input class="easyui-textbox" id="searchPatry" style="width:150px" data-options="editable:false">
	
			<span tcl-text="sample.list.search.customer"></span>
			<input class="easyui-textbox" id="searchCustomer" style="width:150px" data-options="editable:false">
			<span tcl-text="sample.list.search.shop"></span>
			<input class="easyui-textbox" id="searchShop" style="width:150px" data-options="editable:false">
			<%-- <span tcl-text="sample.list.search.hqMadel"></span>
			<input class="easyui-textbox" id="searchHqModel" style="width:100px"> --%>
			<br>
			<span tcl-text="product.list.th.line"></span>
			<input class="easyui-textbox" id="searchLine" style="width:100px" data-options="editable:false" multiple data-live-search="true">
			
			&nbsp;&nbsp;
			<%-- <span tcl-text="sample.list.search.model"></span>
			<input class="easyui-textbox" id="searchModel" style="width:100px"> --%>
			
			 <span tcl-text="sampleTarget.ach"></span><i>(<=)</i>
			<input  class="easyui-numberbox"  id="ach" style="width:100px"><i>%</i>
			&nbsp;&nbsp;
			<span tcl-text="sampleTarget.soAch"></span><i>(<=)</i>
			<input  class="easyui-numberbox"  id="soAch" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();" id="searchBt" tcl-text="toolbar.search">&nbsp;</a>
			
		</div>
	</div>
	
	<div    id="sumDiv"    class="" <%-- title="<s:text name='permission.labelkey.cpxhgx'/>"  --%>
		data-options="width:970,height:360,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true"  style="left:300">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;"  style="left:300">
	        <table id="samplesSumListTable" name="gridTable"></table>
	        </div>
	        
	    </div>
	</div>
		
</body>
</html>