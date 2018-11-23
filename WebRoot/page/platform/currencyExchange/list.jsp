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
	<title><s:text name='permission.labelkey.hlzhgl'/></title>
	<script src="<%=basePath%>js/platform/currencyExchange/exchanges.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
		<style type="text/css">
			#searchPanel{
				font-size:9pt;
			}
		</style>
</head>
<body>
	<table id="currencyList"></table>
	<div id="barcodetb" class="easyui-panel" style="background:#f2f6f8;border-bottom:0px;" title="<s:text name="exchange.list.title" />" data-options="iconCls:'icon-search',collapsible:false">
		<!-- 表头查询条件-->	
		<div region="north" style="height:58px;" id="searchPanel" class="easyui-panel" style="background:#f2f6f8;border-bottom:0px;" title="<s:text name="exchange.list.title" />" data-options="iconCls:'icon-search',collapsible:false">
		<label for="searchMsg"><s:text name="exchange.form.title.countryId" /></label>
		<input type="text" id="selectQuertyPartyId" name="selectQuertyPartyId"></input>		
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	   <%--  <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importBarcode();"> <!-- tcl-text="toolbar.import"> --></a> 
		<a class="tcl-gridbar-separator"></a>
		<span tcl-text="barcode.list.search.name"></span>
		<input class="easyui-textbox" id="qryName" style="width:110px">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearch();" tcl-text="toolbar.search">&nbsp;</a> --%>
	</div>
	</div>
	<div id="addCurrencyWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cptmdzb.add'/>"
		data-options="width:540,height:200,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addCurrecncyForm" method="post" enctype="multipart/form-data" style="height:80px;">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
					        	<td ><i class="required">*</i><span style="color:red"></span><s:text name="exchange.form.title.countryId" />:</td>
				                <td><input type="text" class="easyui-textbox" id="countryId" class="easyui-textbox easyui-validatebox" name="countryId" data-options="required:true,editable:false"></input></td>
					            <td ><i class="required">*</i><span style="color:red"></span><s:text name="exchange.list.form.currency" />: </td>
					            <td><input type="text" class="easyui-textbox"  id="exchange" class="easyui-textbox easyui-validatebox" name="exchange" data-options="required:true"></input></td> 
			           	</tr>
			            <tr>
					            <td ><label><span style="color:red">*</span><s:text name="exchange.list.form.datadate" /></label> </td>
					            <td><input type="text" class="easyui-datebox"  name="dataDate" data-options="required:true,editable:false"></input></td>			       
				         </tr>  
	            	</table>
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a class="btn btn-warning btn-sm" onclick="submitForm()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
	        	<a class="btn btn-warning btn-sm" onclick="clearForm()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
	</div>
	
	
</body>
</html>