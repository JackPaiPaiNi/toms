<%@page import="cn.tcl.common.WebPageUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String party = (String) request.getSession().getAttribute("loginUserId");
	String roleId =WebPageUtil.getLoginedUser().getRoleId();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.pctarget'/></title>
	<script>var my_login_id='<%=party%>';var roleId="<%=roleId%>";</script>
	<script src="<%=basePath%>js/platform/performance/PC_Target.js" ></script>
	<script src="<%=basePath%>js/common.easyui.js" ></script>
	 <link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
</head>

<body>
	<table id="shopListTable" name="gridTable"></table>
	<div id="shoptb" style="padding:2px 5px;">
		<div style="margin-bottom:5px">

	    	<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importPCTarget();" tcl-text="promoter.import" ></a> 
	    	<a href="<%=basePath%>download/Promoters Target.xlsx" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="target.downloadtemplate"></a>
	    	
	    	<a class="tcl-gridbar-separator"></a>
    	</div>
    	<div>
    	 <span tcl-text="sample.list.th.datadate"></span>:
    
    	<input  id="date"   readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" />
    	
	    	<%-- <span tcl-text="district.countryName"></span>:
	    	<input class="easyui-textbox" id="countryName" style="width:110px"> --%>
		    <span tcl-text="shop.list.search.customer"></span>: <input class="easyui-textbox" id="customerName" style="width:110px">
		    <span tcl-text="shop.list.th.name"></span>: <input class="easyui-textbox" id="shopName" style="width:110px">
		     <span tcl-text="target.promoter"></span>: <input class="easyui-textbox" id="userName" style="width:110px">
		   
		    <a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearch();" tcl-text="toolbar.search"></a>
		</div>
	</div>
	<div id="addShopWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.mdwh.add'/>" style="width:820px;height:515px"
		data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true" >
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addShopForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<input type="hidden" name="hisLoac"/>
	            	<input type="hidden" name="shopId"/>
	            	<input type="hidden" name="switchSign"/>
	            	<table style="width: 100%;">
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="shop.list.th.name"></span>:</td>
		            		<td><input type="text" name="shopName" class="easyui-textbox easyui-validatebox" data-options="required:true"/></td>
		            		<td><i class="required"></i><span tcl-text="shop.list.th.enterdate"></span>:</td>
		            		<td><input type="text" style="width:154px" name="enterDateStr" editable="false" class="easyui-datebox" data-options="editable:false"/></td>
		            		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
		            		<td><input type="text" id="countryId" style="width:154px" name="countryId" editable="false" data-options="required:true,editable:false"  /></td>
	            		</tr>
	            		<tr>
	            			<td><i class="required"></i><span tcl-text="shop.list.th.customer"></span>:</td>
		            		<td><input type="text" style="width:153px" id="customer" name="customer" editable="false" data-options="editable:false"/></td>
	            			<td><i class="required">*</i><span tcl-text="shop.list.th.party"></span>:</td>
		            		<td><input type="text" style="width:153px" id="partyId" name="partyId" editable="false" data-options="required:true,editable:false"/></td>
		            		<td><!-- <i class="required">*</i> --><span tcl-text="district.provinceName"></span>:</td>
		            		<td><input type="text" id="provinceId" style="width:154px" name="provinceId" editable="false" data-options="editable:false"/></td>
	            		</tr>
	            		<tr>
	            			<td><span tcl-text="district.cityName"></span>:</td>
		            		<td><input type="text" id="cityId" style="width:154px" name="cityId" editable="false" data-options="editable:false"/></td>
		            		<td><span tcl-text="district.countyName"></span>:</td>
		            		<td><input type="text" id="countyId" style="width:154px" name="countyId" editable="false" data-options="editable:false"/></td>
		            		<td><span tcl-text="district.townName"></span>:</td>
		            		<td><input type="text" id="townId" style="width:154px" name="townId" editable="false" data-options="editable:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><!-- <i class="required">*</i> --><span tcl-text="contact.address"></span>:</td>
		            		<td><input type="text" name="detailAddress" class="easyui-textbox" data-options="required:false"/></td>
		            		<td><!-- <i class="required">*</i> --><span tcl-text="shop.list.th.lng"></span>:</td>
		            		<td><input type="text" name="lng" class="easyui-textbox" data-options="required:false"/></td>
		            		<td><!-- <i class="required">*</i> --><span tcl-text="shop.list.th.lat"></span>:</td>
		            		<td><input type="text" name="lat" class="easyui-textbox" data-options="required:false"/></td>
	            		</tr>
	            		<tr>
		            		<td><i class="required"></i><span tcl-text="contact.contactName"></span>:</td>
		            		<!-- <td><input type="text" name="contactName" class="easyui-textbox" data-options="required:true"/></td> -->
		            		<td><input type="text" name="contactName" class="easyui-textbox" /></td>
		            		<td><i class="required"></i><span tcl-text="contact.phone"></span>:</td>
		            		<!-- <td><input type="text" name="phone" class="easyui-textbox" data-options="required:true"/></td> -->
		            		<td><input type="text" name="phone" class="easyui-textbox"/></td>
		            		<td><i class="required"></i><span tcl-text="contact.email"></span>:</td>
		            		<!-- <td><input type="text" name="email" class="easyui-textbox easyui-validatebox" data-options="required:true,validType:'email',validateOnBlur:true"/></td> -->
		            		<td><input type="text" name="email" class="easyui-textbox easyui-validatebox" data-options="validType:'email',validateOnBlur:true"/></td>
	            		</tr>
	            		<tr>
		            		<%-- <td><span tcl-text="shop.list.th.comments"></span>:</td>
		            		<td colspan="3"><input type="text" name="comments" style="width:403px;" class="easyui-textbox"/></td> --%>
		            		<td><span tcl-text="contact.status"></span>:</td>
		            		<td><input type="text" id="status" name="status" style="width:154px" editable="false" data-options="editable:false,panelHeight:'auto'"/></td>
	            			<td><span tcl-text="contact.level"></span>:</td>
	            			<td><input type="text" id="level"  style="width:154px" name="level" editable="false" data-options="editable:false,panelHeight:'auto'"/></td>
	            			
	            			<td><i class="required"></i><span tcl-text="contact.location"></span>:</td>
		            		<td><input type="text" id="location" name="location" class="easyui-textbox" style="width:154px"/></td>
		            		
	            		</tr>
	            	</table>
	            	
	            	<div  class="easyui-layout" style="height:200px;width:766px;">
	            		<div  data-options="region:'west',split:false,collapsible:false" title="<s:text name='shop.list.th.bussinessers'/>" style="width:256px;position:position:absolute;z-index:998">
							<div  style="position:absolute;top:1px;right:10px;z-index:999;"><input class="easyui-textbox" id="searchBus" style="width:110px">
							<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBusBt" plain="true" onclick="doSearchBus();" tcl-text="toolbar.search"></a>
							</div>
							<div id="dl_bussinessers" ></div>
						</div>

						<div data-options="region:'east',split:false,collapsible:false" title="<s:text name='shop.list.th.supervisors'/>" style="width:256px">
							<div style="position:absolute;top:1px;right:10px;z-index:999;"><input class="easyui-textbox" id="searchSup" style="width:100px">
							<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchSupBt" plain="true" onclick="doSearchSup();" tcl-text="toolbar.search"></a>
							</div>
							<div id="dl_supervisors"></div>
						</div>
	            		<div data-options="region:'center'" title="<s:text name='shop.list.th.salers'/>">
							<div style="position:absolute;top:1px;right:10px;z-index:999;"><input class="easyui-textbox" id="searchSal" style="width:110px">
							<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchSalBt" plain="true" onclick="doSearchSal();" tcl-text="toolbar.search"></a>
							</div>
							<div id="dl_salers"></div>
						</div>
	            	</div>
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="submitBtn" class="btn btn-warning btn-sm" onclick="submitForm()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearForm()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>&nbsp;&nbsp;
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