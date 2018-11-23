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
	<title><s:text name='permission.labelkey.sellCountry'/></title>
	<script src="<%=basePath%>js/platform/sale/countrySale.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
</head>
<body>
	<table id="saleListTable"></table>
	<div id="saletb" style="padding:2px 5px;">
	   <%--  <!--<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a> -->
	     <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importSales();" tcl-text="toolbar.import"></a>
		<a href="<%=basePath%>download/sales.xlsx" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
	 --%>	<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="exportExcel();" tcl-text="toolbar.export"></a>
		<a class="tcl-gridbar-separator"></a>
		
		<span tcl-text="sale.list.search.cdate"></span>
		<input type="text" style="width:100px" class="easyui-datebox" name="searchCdate" id="searchCdate"/>
		<span tcl-text="sale.list.search.ddate"></span>
		<input type="text" style="width:100px" class="easyui-datebox" name="searchDdate" id="searchDdate"/>
		<span tcl-text="sale.list.search.cparty"></span>
		<input class="easyui-textbox" id="searchCParty" style="width:100px">
		
		
		<span tcl-text="sale.list.th.hqMadel"></span>
		<input class="easyui-textbox" id="searchHModel" style="width:100px">
		
		<span tcl-text="sale.list.th.model"></span>
		<input class="easyui-textbox" id="searchBModel" style="width:100px">
		
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearch();" tcl-text="toolbar.search">&nbsp;</a>
	</div>
	<div id="addSaleWin" class="easyui-window tcladdwin" 
		data-options="title:'sale record',width:800,height:320,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addSaleForm" method="post" enctype="multipart/form-data">
	            	<!-- <input type="hidden" name="saleId"/> -->
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
	            			<%-- <td><i class="required">*</i><span tcl-text="sale.list.th.country"></span>:</td>
		            		<td><input type="text" id="country" style="width:154px" name="country"/></td> --%>
	            			<td><i class="required">*</i><span tcl-text="sale.list.th.shopName"></span>:</td>
		            		<td><input type="text" id="shopId" class="easyui-combobox" style="width:154px" name="shopId" editable="false" data-options="required:true,editable:false"/></td>
	            			<td><i class="required">*</i><span tcl-text="sale.list.th.salerName"></span>:</td>
		            		<td><input type="text" id="userId" style="width:154px" class="easyui-textbox easyui-validatebox"  data-options="required:true" name="userId"/></td>
	            		</tr>
	            		<tr>
	            			<td><i class="required">*</i><span tcl-text="sale.list.th.model"></span>:</td>
		            		<td><input type="text" id="model" name="model" style="width:154px" class="easyui-textbox easyui-validatebox"  data-options="required:true"/></td>
		            		<%-- <td><i class="required">*</i><span tcl-text="sale.list.th.hqMadel"></span>:</td>
		            		<td><input type="text" id="hqModel" name="hqModel" style="width:154px" class="easyui-textbox easyui-validatebox"  data-options="required:true"/></td> --%>
		            		<td><span tcl-text="sale.list.th.quantity"></span>:</td>
		            		<td><input type="text" id="quantity" style="width:154px" name="quantity"/></td>
	            		</tr>
	            		<tr>
	            			<td><span tcl-text="sale.list.th.amount"></span>:</td>
		            		<td><input type="text" id="amount" style="width:154px" name="amount"/></td>
		            		<td><span tcl-text="sale.list.th.datadate"></span>:</td>
		            		<td><input type="text" id="datadate" style="width:154px" name="datadate"/></td>
		            		
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="sale.list.th.remark"></span>:</td>
		            		<td colspan="3"><textarea type="text" id="remark" style="width:480px;height:70px;" name="remark"></textarea></td>
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