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
	<title><s:text name='permission.labelkey.incentive'/></title>
	<script src="<%=basePath%>js/platform/incentive/incentive.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	 <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
</head>
<body>
	<table id="modelmapList" name="gridTable"></table>
	<div id="modelmaptb" style="padding:2px 5px;">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	   	<a href="<%=basePath%>download/IncentiveInfo.xlsx" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importModelMap();"> <!-- tcl-text="toolbar.import"> --></a>
		<span tcl-text="incentive.list.countryId"></span>
		<input type="text" id="selectQuertyPartyId" name="selectQuertyPartyId"></input>
		<span tcl-text="modelmap.list.search.name"></span>
		<input class="easyui-textbox" id="qryName" style="width:110px">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="searchBt" onclick="doSearch();" tcl-text="toolbar.search">&nbsp;</a>
		<a class="tcl-gridbar-separator"></a>
	</div>
	
	<div id="addModelmapWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cpxhgx'/>" 
		data-options="width:600,height:300,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addModelmapForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
		            	<%-- 	<td><i class="required">*</i><span tcl-text="incentive.list.th.size"></span>:</td>
		            		<td><input type="text" style="width:154px" id="size" editable="false"  data-options="required:true,editable:false" name="size"/></td> --%>
	            			<td><i class="required">*</i><span tcl-text="incentive.list.th.partyName"></span>:</td>
		            		<td><input type="text" style="width:154px" id="partyId" name="partyId" editable="false" data-options="required:true,editable:false"/></td>
	            			<%-- <td><select  style="border:1px solid #EE9A49;width:150px;height:22px" id="partyId" name="partyId"></select></td> --%>
	            			<td><i class="required">*</i><span tcl-text="incentive.list.th.branchModel"></span>:</td>
		            		<td><input type="text" id="branchModel" class="easyui-combobox"  data-options="required:true" name="branchModel"/></td>
	            			<%-- <td><select  style="border:1px solid #EE9A49;width:150px;height:22px;" id="branchModel" name="branchModel"></select> --%>
	            				
	            			</td>
	            		</tr>
	            
	            		<tr>
	            			<td><i class="required">*</i><span tcl-text="incentive.list.th.retailPrice"></span>:</td>
		            		<td><input type="text" id="retailPrice" class="easyui-textbox easyui-validatebox"  data-options="required:true" name="retailPrice"/></td>
		            		<td><i class="required">*</i><span tcl-text="incentive.list.th.incentive"></span>:</td>
		            		<td><input type="text" style="width:154px" class="easyui-textbox easyui-validatebox" id="incentive"	name="incentive"  data-options="required:true"/></td>
	            		</tr>
	            		
	            		<%-- <tr>
		            		<td><i class="required">*</i><span tcl-text="incentive.list.th.quantity"></span>:</td>
		            		<td><input type="text" id="quantity" class="easyui-textbox easyui-validatebox"   name="quantity" data-options="required:true"/></td>
	            			
	            		</tr> --%>
	            		
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="incentive.list.th.ctime"></span>:</td>
		            		<td><input  id="date"  name="date" readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" /></td>
	            		</tr>
	            		<tr>
		            		<td><!-- <i class="required">*</i> --><span tcl-text="incentive.list.remark"></span>:</td>
		            		<td colspan="3" ><input type="text" style="width:420px;height:50px;" name="remark" class="easyui-textbox" data-options="multiline:true"/></td>
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