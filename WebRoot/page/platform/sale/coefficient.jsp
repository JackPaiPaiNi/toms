<%@page import="cn.tcl.common.WebPageUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String party = (String) request.getSession().getAttribute("loginUserId");
	int country =Integer.parseInt( WebPageUtil.getLoginedUser().getPartyId());;

%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.xswh'/></title>
	<script>
	var my_login_id='<%=party%>';
	var country='<%=country%>';
	</script>
	<script src="<%=basePath%>js/platform/sale/coefficient.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
</head>
<body>
	<table id="saleListTable"></table>
	<% if(country==999){
	
	%>
	<div id="saletb" style="padding:2px 5px;">
	
		    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	    <!--<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a> -->
<!-- 	   	<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="exportExcel();" tcl-text="toolbar.export"></a>
 -->		<a class="tcl-gridbar-separator"></a>
		
		<%-- <span tcl-text="sale.list.search.cdate"></span>
		<input type="text" style="width:100px" class="easyui-datebox" name="searchCdate" id="searchCdate"/>
		<span tcl-text="sale.list.search.ddate"></span>
		<input type="text" style="width:100px" class="easyui-datebox" name="searchDdate" id="searchDdate"/> --%>
		
		<span tcl-text="district.countryName"></span>:
	    	<select class="easyui-combobox" id="tcl_country" style="width:100px" editable="false" data-options="editable:false">
		    </select>		
		    <a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearch();" tcl-text="toolbar.search">&nbsp;</a>
	</div>
	<%
	
	}%>
	<div id="addSaleWin" class="easyui-window tcladdwin" 
		data-options="title:'sale record',width:800,height:300,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	          
	            <form id="addSaleForm" method="post" enctype="multipart/form-data">
	            	<!-- <input type="hidden" name="saleId"/> -->
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
	            			<%-- <td><i class="required">*</i><span tcl-text="sale.list.th.country"></span>:</td>
		            		<td><input type="text" id="country" style="width:154px" name="country"/></td> --%>
	            			<td><i class="required">*</i><span tcl-text="district.countryName" ></span>:</td>
		            		<td><input type="text" id="countryId" class="easyui-combobox" style="width:154px" name="countryId" editable="false" data-options="required:true,editable:false"/></td>
	            			 	
	            			 		</tr>
	            		<tr>
	            			<td><i class="required">*</i><span tcl-text="coefficient.all"></span>:</td>
		            		<td><input type="text" id="all" name="all" style="width:154px" class="easyui-numberbox easyui-validatebox"  data-options="required:true,min:0,precision:2"/></td>
		            		<%-- <td><i class="required">*</i><span tcl-text="sale.list.th.hqMadel"></span>:</td>
		            		<td><input type="text" id="hqModel" name="hqModel" style="width:154px" class="easyui-textbox easyui-validatebox"  data-options="required:true"/></td> --%>
	            			<td><i class="required">*</i><span tcl-text="coefficient.core"></span>:</td>
		            		<td><input type="text" id="core" style="width:154px" name="core"  data-options="required:true,min:0,precision:2" class="easyui-numberbox easyui-validatebox"/></td>
	            		</tr>
	            		
	            <%-- 	<tr>
	            	
	            		<td><i class="required">*</i><span tcl-text="coefficient.file"></span>:</td>
	            	
		            	<td>
		            	<input type="file" id="file" style="width:154px" name="file"  data-options="required:true"  class="easyui-textbox easyui-validatebox"/>
						<input type="hidden" id="hidcover"/>
						<input type="button" id="coverupload" value="upload"></input>
						</td>
						
	            	</tr>
	            	<tr>
	            	<td>
						<label>size:<span>2M</span> max,file forma:<span>gif，jpg，jpeg，png，bmp</span></label>	
	            	</td>
	            	
	            	</tr> --%>
	            	
	            <tr>  
	            <td>
    	 
            </td>  
        </tr>  
	            	</table>
	         
	            </form>	
	               	 <form id="coverform"  enctype="multipart/form-data"  >	
		     		<div class="Thecover" style="padding-left: 140px;">	
	            		<i class="required">*</i><span tcl-text="coefficient.file"></span>:</td>
						<input type="file" id="coverimg" name="fileUploadTools.uploadFile" value="upload" style="display: inline-block;"/>
						<input type="text"  id="file"/>
						<input type="button" id="coverupload" value="upload"></input>
						<br />
						<label style="text-indent: 40px;color:red;">size:<span>2M</span> max,file forma:<span>gif，jpg，jpeg，png，bmp，pdf</span></label>	
		     		</div>   
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