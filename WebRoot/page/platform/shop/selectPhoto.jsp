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
	<title><s:text name='permission.labelkey.mdzpwh'/></title>
	<script src="<%=basePath%>js/platform/shop/photolist.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
</head>

<body>
	<table id="photoListTable"></table>
	<div id="stargettb" style="padding:2px 5px;">
		<a class="tcl-gridbar-separator"></a>
		<span tcl-text="photo.list.search.shop"></span>
		<input class="easyui-textbox" id="searchShop" style="width:110px">
		<span tcl-text="photo.list.search.date"></span>
		<input type="text" style="width:100px" class="easyui-datebox" name="searchDate" id="searchDate"/>
		
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearch();" tcl-text="toolbar.search">&nbsp;</a>
	</div>
	<div class="picview" closed="true">
		<img src=""/>
	</div>

	</div>
</body>
</html>