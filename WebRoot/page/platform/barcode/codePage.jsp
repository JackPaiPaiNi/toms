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
	<title><s:text name='permission.labelkey.cptmdzb'/></title>
	<script src="<%=basePath%>js/platform/barcode/codePage.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
</head>
<body>
	<table id="barcodeList"></table>
	<div id="barcodetb" style="padding:2px 5px;">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importBarcode();"> <!-- tcl-text="toolbar.import"> --></a> 
		<a class="tcl-gridbar-separator"></a>
		<span tcl-text="barcode.list.search.name"></span>
		<input class="easyui-textbox" id="qryName" style="width:110px">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearch();" tcl-text="toolbar.search">&nbsp;</a>
	</div>
	<div id="addBarcodeWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cptmdzb.add'/>"
		data-options="width:600,height:160,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addBarcodeForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="barcode.list.th.barcode"></span>:</td>
		            		<td><input type="text" id="barcode" class="easyui-textbox easyui-validatebox"  data-options="required:true" name="barcode"/></td>
		            		<td><i class="required">*</i><span tcl-text="barcode.list.th.model"></span>:</td>
		            		<td><input type="text" id="hqModel" class="easyui-textbox easyui-validatebox"  data-options="required:true" name="hqModel"/></td>
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