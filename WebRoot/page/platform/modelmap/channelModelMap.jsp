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
	<title><s:text name='permission.labelkey.cpxhgx'/></title>
	<script src="<%=basePath%>js/platform/modelmap/channelModelMap.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
</head>
<body>
	<table id="modelmapList" name="gridTable"></table>
	<div id="modelmaptb" style="padding:2px 5px;">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	   	<a href="<%=basePath%>download/ChannelModelmapInfo.xls" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importModelMap();"> <!-- tcl-text="toolbar.import"> --></a>
	<!-- 	<a href="#" class="easyui-linkbutton" onclick="exportExcel();" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downPrice"></a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importModelPrice();" tcl-text="toolbar.importPrice" > ></a>
		 -->
		<a class="tcl-gridbar-separator"></a>
		<span tcl-text="modelmap.list.search.name"></span>
		<input class="easyui-textbox" id="qryName" style="width:110px">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="searchBt" onclick="doSearch();" tcl-text="toolbar.search">&nbsp;</a>
	</div>
	
	<div id="addModelmapWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cpxhgx'/>" 
		data-options="width:600,height:200,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addModelmapForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
	            			<td><i class="required">*</i><span tcl-text="modelmap.list.th.branchModel"></span>:</td>
		            		<td><input type="text" style="width:154px" id="branchModel" editable="false"  data-options="required:true,editable:false" name="branchModel"/></td>
	            	
		            		<td><i class="required">*</i><span tcl-text="modelmap.list.th.channelModel"></span>:</td>
		            		<td><input type="text" style="width:154px" id="channelModel" class="easyui-textbox easyui-validatebox"  data-options="required:true" name="channelModel"/></td>
		            	</tr>
	            		<tr>
<%-- 	            			<td><i class="required">*</i><span tcl-text="modelmap.list.th.price"></span>:</td> --%>
<!-- 		            		<td><input type="text" id="price" class="easyui-textbox easyui-validatebox"  data-options="required:true" name="price"/></td> -->
		            		<td><i class="required">*</i><span tcl-text="modelmap.list.th.partyName"></span>:</td>
		            		<td><input type="text" id='partyId' style="width:154px" name="partyId" editable="false" data-options="required:true,editable:false"/></td>
		            		<td><i class="required">*</i><span tcl-text="modelmap.list.th.customerName"></span>:</td>
		            		<td><input type="text" style="width:154px" id='customerId' class="easyui-tabs" name="customerId" editable="false" data-options="required:true,editable:false"/></td>
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