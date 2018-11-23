<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='sample.list.table'/></title>
	<script src="<%=basePath%>js/platform/sample/samples.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
</head>
<body>
	<table id="samplesListTable"></table>
	<div id="stargettb" style="padding:2px 5px;">
	    <!-- <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importProducts();" tcl-text="toolbar.import"></a> -->
	    <div style="margin-bottom:5px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="exportExcel();" tcl-text="toolbar.export"></a>
			<a class="tcl-gridbar-separator"></a>
		</div>
		<div>
			<span tcl-text="sample.list.search.date"></span>
			<input type="text" style="width:100px" class="easyui-datebox" name="searchDate" id="searchDate"/>
			<span tcl-text="sample.list.search.party"></span>
			<input class="easyui-textbox" id="searchPatry" style="width:100px">
			<span tcl-text="sample.list.search.customer"></span>
			<input class="easyui-textbox" id="searchCustomer" style="width:100px">
			<span tcl-text="sample.list.search.shop"></span>
			<input class="easyui-textbox" id="searchShop" style="width:100px">
			<span tcl-text="sample.list.search.hqMadel"></span>
			<input class="easyui-textbox" id="searchHqModel" style="width:100px">
			<span tcl-text="sample.list.search.model"></span>
			<input class="easyui-textbox" id="searchModel" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();" id="searchBt" tcl-text="toolbar.search">&nbsp;</a>
		</div>
	</div>
</body>
</html>