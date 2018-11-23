<%@page import="cn.tcl.common.WebPageUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String roleId =WebPageUtil.getLoginedUser().getRoleId();
	String party = (String) request.getSession().getAttribute("loginUserId");
%>
<!DOCTYPE html>
<html>
<head>
	<title><s:text name='permission.labelkey.khwh'/></title>
	<script>var my_login_id='<%=party%>'; var roleId="<%=roleId%>";</script>
	<script src="<%=basePath%>js/platform/customer/list.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
</head>
<body>
	<table id="customerListTable" name="gridTable"></table>
	<div id="customertb" style="padding:2px 5px;">
		<div style="margin-bottom:5px">
			<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="showAddWin();" tcl-text="toolbar.add"></a>
		    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-save" onclick="importCustomers();"> <!-- tcl-text="toolbar.import"> --></a> 
		    <a href="<%=basePath%>download/Customers.xlsx" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
		    <a href="#" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true"  onclick="downName();" tcl-text="toolbar.downCusName"></a>
		    
		    <div class="tcl-gridbar-separator"></div>
		</div>
		<div>
		    
	    	<span tcl-text="district.countryName"></span>
	    	<select class="easyui-combobox" id="tcl_country"  style="width:100px" editable="false" data-options="editable:false">
		   <!--  <option tcl-text="shop.list.country"></option> -->
		    </select>
			<span tcl-text="customer.list.search.party"></span>: 
		    <input class="easyui-textbox" id="searchParty" style="width:110px">
		    <span tcl-text="district.provinceName"></span>:
		    <select class="easyui-combobox" id="tcl_province" style="width:100px" editable="false" data-options="editable:false">
		    </select>
		    <span tcl-text="customer.list.search.customer"></span>: 
		    <input class="easyui-textbox" id="searchCustomer" style="width:110px">&nbsp;
		    <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search" id="searchBt" onclick="doSearch();" tcl-text="toolbar.search"></a>
	    </div>
	</div>
	<div id="addCustomerWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.khwh.add'/>" 
	 data-options="width:820,height:570,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addCustomerForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="customer.list.th.name"></span>:</td>
		            		<td><input type="text" class="easyui-textbox easyui-validatebox" data-options="required:true" name="customerName"/></td>
		            		<td><i class="required"></i><span tcl-text="customer.list.th.enterdate"></span>:</td>
		            		<td><input type="text" name="enterDateStr" editable="false" class="easyui-datebox" data-options="required:false,editable:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><i class="required"></i><span tcl-text="customer.list.th.aname"></span>:</td>
		            		<td colspan="3"><input type="text" style="width:420px;" name="aname" class="easyui-textbox" data-options="required:false"/></td>
	            		</tr>
	            		<tr>
	            			<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
		            		<td><input type="text" id="district_country" class="easyui-combobox" style="width:154px" name="countryId" editable="false" data-options="required:true,editable:false"/></td>
		            		<td><i class="required"></i><span tcl-text="customer.list.th.party"></span>:</td>
		            		<td><input type="text" style="width:154px" class="easyui-combobox" name="partyId" id="partyId" editable="false" data-options="editable:false,required:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="customer.list.th.customerCode"></span>:</td>
		            		<td><input type="text" name="customerCode" id="customerCode" class="easyui-textbox" data-options="required:true"/></td>
		            		<td><!-- <i class="required">*</i> --><span tcl-text="customer.list.th.channelTypeName"></span>:</td>
		            		<td><input type="text" id="channelType" class="easyui-combobox" style="width:154px" name="channelType" editable="false" data-options="required:true,editable:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><i class="required"></i><span tcl-text="district.provinceName"></span>:</td>
		            		<td><input type="text" id="district_province" class="easyui-combobox" style="width:154px" name="provinceId" editable="false" data-options="required:false,editable:false"/></td>
		            		<td><span tcl-text="district.cityName"></span>:</td>
		            		<td><input type="text" id="district_city" class="easyui-combobox" style="width:154px" name="cityId" editable="false" data-options="editable:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="district.countyName"></span>:</td>
		            		<td><input type="text" id="district_county" class="easyui-combobox" style="width:154px" name="countyId" editable="false" data-options="editable:false"/></td>
		            		<td><span tcl-text="district.townName"></span>:</td>
		            		<td><input type="text" id="district_town" class="easyui-combobox" style="width:154px" name="townId" editable="false" data-options="editable:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><!-- <i class="required">*</i> --><span tcl-text="contact.address"></span>:</td>
		            		<td colspan="3"><input type="text" style="width:420px;" name="detailAddress" class="easyui-textbox" data-options="required:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><i class="required"></i><span tcl-text="contact.contactName"></span>:</td>
		            		<td><input type="text" name="contactName" class="easyui-textbox easyui-validatebox" /></td>
		            		<td><i class="required"></i><span tcl-text="contact.phone"></span>:</td>
		            		<td><input type="text" name="phone" class="easyui-textbox" /></td>
	            		</tr>
	            		<tr>
		            		<td><i class="required"></i><span tcl-text="contact.email"></span>:</td>
		            		<td><input type="text" name="email" class="easyui-textbox" /></td>
		            		<td><i class="required">*</i><span tcl-text="contact.status"></span>:</td>
		            		<td><input type="text" id="status" style="width:154px" name="status" editable="false" data-options="required:true,editable:false,panelHeight:'auto'"/></td>
	            		</tr>
	            		<tr>
		            		<td><i class="required"></i><span tcl-text="contact.website"></span>:</td>
		            		<td colspan="3"><input type="text" name="website" style="width:420px;" class="easyui-textbox" data-options="required:false"/></td>
	            		</tr>
	            	</table>
	            	
	            	<div  class="easyui-layout" style="height:200px;width:766px;margin: 0 auto;">
	            		<div  data-options="region:'west',split:false,collapsible:false" title="<s:text name='shop.list.th.bussinessers'/>" style="width:256px;position:position:absolute;z-index:998">
							<div  style="position:absolute;top:1px;right:10px;z-index:999;"><input class="easyui-textbox" id="searchBus" style="width:110px">
							<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBusBt" plain="true" onclick="doSearchBus();" tcl-text="toolbar.search"></a>
							</div>
							<div id="dl_sales" ></div>
						</div>
	            		<div data-options="region:'center'" title="<s:text name='shop.list.th.salers'/>">
							<div style="position:absolute;top:1px;right:10px;z-index:999;"><input class="easyui-textbox" id="searchProm" style="width:102px">
							<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchPromBt" plain="true" onclick="doSearchProm();" tcl-text="toolbar.search"></a>
							</div>
							<div id="dl_pro"></div>
						</div>
						
						<div data-options="region:'east',split:false,collapsible:false" title="<s:text name='shop.list.th.supervisors'/>" style="width:256px">
							<div style="position:absolute;top:1px;right:10px;z-index:999;"><input class="easyui-textbox" id="searchSup" style="width:100px">
							<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchSupBt" plain="true" onclick="doSearchSup();" tcl-text="toolbar.search"></a>
							</div>
							<div id="dl_sup"></div>
						</div>
	            	</div>
	            	
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="submitBtn" class="btn btn-warning btn-sm" iconCls="icon-save" onclick="submitForm()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
	        	<a id="clearBtn" class="btn btn-warning btn-sm" iconCls="icon-cancel" onclick="clearForm()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
	    
	    <div id="load-layout" style="position:fixed;width:100%;height:100%;top:0px;
				left:0px;opacity:0.4;background:#000;display:none;">
			<div align="center" style="position:absolute;left:49%;top:48%;width:31px;height:31px;">
				<img id="loadGif" src="">
			</div>
		</div>
	</div>
</body>
</html>