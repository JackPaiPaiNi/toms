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
	<title><s:text name='sample.target.title'/></title>
	<script>var my_login_id='<%=party%>';</script>
	<script src="<%=basePath%>js/platform/sample/sampleTarget.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
     <script type="text/javascript" src="<%=basePath%>js/platform/statement/daterangepicker.js"></script>
     <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
</head>
<body>
	<table id="samplesListTable"  name="gridTable">
	</table>
	
	
	
	<div id="stargettb" style="padding:2px 5px;">
	    <!-- <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importProducts();" tcl-text="toolbar.import"></a> -->
	    <div style="margin-bottom:5px">
			<a onclick="exportSampleTTemplate()"   href="#<%-- <%=basePath%>download/Sample Target.xlsx --%>" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importSampleTarget();"> <!-- tcl-text="toolbar.import"> --></a>
			<a onclick="exportTargetAch()"   href="#<%-- <%=basePath%>download/Sample Target.xlsx --%>" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="sampleTarget.targetDow"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="showSumDiv();" id="searchBt" tcl-text="sampleTarget.sum">&nbsp;</a>
		
		
		</div>
		<div>
		<span tcl-text="district.countryName"></span>:
	    	<select class="easyui-combobox" id="tcl_country" style="width:100px"  data-options="editable:false">
		    </select>
		    <span tcl-text="shop.list.search.party"></span>
			<input class="easyui-textbox" id="searchPatry" style="width:150px" data-options="editable:false">
		<span tcl-text="sample.list.th.datadate"></span>
		<input type="text" style="width:100px" onclick="WdatePicker({dateFmt:'yyyy-MM'})" name="beginDate" id="beginDate"/>
			<span tcl-text="sample.list.search.customer"></span>
			<input class="easyui-textbox" id="searchCustomer" style="width:150px" data-options="editable:false">
			<span tcl-text="sample.list.search.shop"></span>
			<input class="easyui-textbox" id="searchShop" style="width:150px" data-options="editable:false">
			<br>
			<span tcl-text="product.list.th.line"></span>
			<input class="easyui-combobox" id="searchLine" style="width:100px" data-options="editable:false" multiple="true"/>
			<%-- &nbsp;&nbsp;
			<span tcl-text="sample.list.search.hqMadel"></span>
			<input class="easyui-textbox" id="searchHqModel" style="width:100px">
			<span tcl-text="sample.list.search.model"></span>
			<input class="easyui-textbox" id="searchModel" style="width:100px">
			 --%><a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();" id="searchBt" tcl-text="toolbar.search">&nbsp;</a>
		</div>
	</div>
	
	
	
		<div id="addModelmapWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cpxhgx'/>" 
		data-options="width:600,height:200,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addSampleTargetForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            	<tr>
	            			<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
		            		<td><input type="text" id="countryId" class="easyui-textbox"  data-options="required:true,editable:false" name="countryId"/></td>
		            
		            		<td><i class="required">*</i><span tcl-text="shop.list.search.party"></span>:</td>
		            		<td><input type="text" id="partyId" class="easyui-textbox"  data-options="required:true,editable:false" name="partyId"/></td>
		            		</tr>
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="shop.list.th.shopName"></span>:</td>
		            		<td><input type="text" id="shopId" class="easyui-textbox "  data-options="required:true,editable:false" name="shopId"/></td>
		            		<td><i class="required">*</i><span tcl-text="modelmap.list.th.branchModel"></span>:</td>
		            		<td><input type="text" style="width:154px" class="easyui-textbox" id="model" editable="false"  data-options="required:true,editable:false" name="model"/></td>
	            		</tr>
	            		<tr>
	            			<td><i class="required">*</i><span tcl-text="sample.list.th.quantity"></span>:</td>
		            		<td><input type="text" id="quantity" class="easyui-textbox "  data-options="required:true" name="quantity"/></td>
		            		<td><i class="required">*</i><span tcl-text="sample.list.th.datadate"></span>:</td>
		            		<td><input  class="easyui-datebox "    type="text" style="width:154px" name="dataDate" editable="false" data-options="required:true,editable:false"/></td>
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
	
	<div id="sumDiv" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cpxhgx'/>" 
		data-options="width:570,height:360,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true
		">
		<div class="easyui-layout" data-options="fit:true"  >
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	        <table id="samplesSumListTable" name="gridTable"></table>
	        </div>
	        
	    </div>
	</div>
	
	
	
</body>
</html>