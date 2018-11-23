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
	<title><s:text name='permission.labelkey.yjgl'/></title>
	<script src="<%=basePath%>js/platform/sale/samples.js" ></script>
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
	<div id="addProductWin" class="easyui-window tcladdwin" 
		data-options="title:'添加产品',width:600,height:460,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addProductForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="product.list.th.productId"></span>:</td>
		            		<td><input type="text" id="productId" class="easyui-textbox easyui-validatebox"  data-options="required:true" name="productId"/></td>
		            		<td><i class="required">*</i><span tcl-text="product.list.th.productName"></span>:</td>
		            		<td><input type="text" name="productName" class="easyui-textbox easyui-validatebox"  data-options="required:true"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="product.list.th.productType"></span>:</td>
		            		<td><input type="text" style="width:154px" name="productType"/></td>
		            		<td><span tcl-text="product.list.th.size"></span>:</td>
		            		<td><input type="text" style="width:154px" name="size"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="product.list.th.display"></span>:</td>
		            		<td><input type="text" style="width:154px" name="display"/></td>
		            		<td><span tcl-text="product.list.th.ratio"></span>:</td>
		            		<td><input type="text" style="width:154px" name="scale"/></td>
	            		</tr>
		            		<tr><td><span tcl-text="product.list.th.brandId"></span>:</td>
		            		<td><input type="text" style="width:154px" name="brand"/></td>
		            		<td><span tcl-text="product.list.th.colorId"></span>:</td>
		            		<td><input type="text" style="width:154px" name="color"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="product.list.th.power"></span>:</td>
		            		<td><input type="text" name="power" class="easyui-textbox"/></td>
		            		<td><span tcl-text="product.list.th.powerOn"></span>:</td>
		            		<td><input type="text" name="powerOn" class="easyui-textbox"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="product.list.th.powerWait"></span>:</td>
		            		<td><input type="text" name="powerWait" class="easyui-textbox"/></td>
		            		<td><span tcl-text="product.list.th.netweight"></span>:</td>
		            		<td><input type="text" name="netweight" class="easyui-textbox"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="product.list.th.weight"></span>（含包装箱）:</td>
		            		<td><input type="text" name="weight" class="easyui-textbox"/></td>
		            		<td><span tcl-text="product.list.th.weightInclude"></span>:</td>
		            		<td><input type="text" name="weightInclude" class="easyui-textbox"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="product.list.th.interFace"></span>:</td>
		            		<td><input type="text" name="interFace" class="easyui-textbox"/></td>
		            		<td><span tcl-text="product.list.th.network"></span>:</td>
		            		<td><input type="text" name="network" class="easyui-textbox"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="product.list.th.os"></span>:</td>
		            		<td><input type="text" name="os" class="easyui-textbox"/></td>
		            		<td><span tcl-text="product.list.th.filePath"></span>:</td>
		            		<td><input type="text" name="uploadExcel" class="easyui-filebox"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="product.list.th.partyId"></span>:</td>
		            		<td><input type="text" style="width:154px" name="party" /></td>
		            		<td><span tcl-text="product.list.th.status"></span>:</td>
		            		<td><input type="text" style="width:154px" name="status"/></td>
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