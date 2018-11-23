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
	<title><s:text name='permission.labelkey.xsmbgl'/></title>
	<script src="<%=basePath%>js/platform/sale/targetlist.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
</head>
<body>
	<table id="saleTargetListTable"></table>
	<div id="stargettb" style="padding:2px 5px;">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	   <!-- <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importProducts();" tcl-text="toolbar.import"></a> -->
		<a class="tcl-gridbar-separator"></a>
		<span tcl-text="saletarget.list.search.party"></span>
		<input class="easyui-textbox" id="searchParty" style="width:110px">
		<span tcl-text="saletarget.list.search.customer"></span>
		<input class="easyui-textbox" id="searchCustomer" style="width:110px">
		<span tcl-text="saletarget.list.search.shop"></span>
		<input class="easyui-textbox" id="searchShop" style="width:110px">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();" id="searchBt" tcl-text="toolbar.search">&nbsp;</a>
	</div>
	
	<div id="addsaleTargetWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.xsmbgl.add'/>"
		data-options="width:600,height:260,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addsaleTargetForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="saletarget.list.th.shop"></span>:</td>
		            		<td><input type="text" id="shopId" class="easyui-combobox" style="width:154px" name="shopId" editable="false" data-options="required:true,editable:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="sale.list.th.quantity"></span>:</td>
		            		<td><input type="text"id="quantity" class="easyui-textbox" name="quantity"/></td>
		            		
		            		<td><span tcl-text="sale.list.th.amount"></span>:</td>
		            		<td><input type="text" class="easyui-textbox" id="amount" name="amount"/></td>
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