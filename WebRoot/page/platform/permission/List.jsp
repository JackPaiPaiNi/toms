<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<title><s:text name='permission.labelkey.yhwh'/></title>
		  <script language="javascript" src="<%=basePath%>js/platform/permission/category.js" ></script> 
		<style type="text/css">
			#searchPanel{
				font-size:9pt;
			}
		</style>
	</head>
	<body>
		<div class="easyui-layout" fit="true">
			<div region="north" style="height:58px;">
				<!-- 表头查询条件-->
				<div id="searchPanel" class="easyui-panel" style="background:#f2f6f8;border-bottom:0px;" title="<s:text name="permission.list" />" data-options="iconCls:'icon-search',collapsible:false">
					<label for="searchMsg"><s:text name="permission.list.queryName" /></label><input name="searchMsg" id="searchMsg" type="text" class="easyui-textbox"></input>
					<a id="searchBut" href="javascript:obj.search()" class="easyui-linkbutton" iconCls="icon-search" plain="true" ><s:text name="permission.list.but.query" /></a> 
				</div> 
			</div>
			<div region="center">
				<table id="userListTable"></table>
			</div>
		</div>
		<!-- 表单 -->  
		<div id="userWindow" class="easyui-window tcladdwin" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false" title="<s:text name="permission.tree.title" />" icon="icon-save" style="width: 600px; height: 360px;background-color: #f2f6f8;">
		    <form id="userForm" method="post" fit="true" style="height:320px;">
		    	<div class="easyui-layout" data-options="fit:true">
		    		<div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
		    			<table class="dir" style="width: 100%;">
				        	<tr>
					        	<td align="right">  
					                <label><span style="color:red">*</span><s:text name="permission.from.title.permissionName" /></label>
				                </td>                    
				                <td>
					                <input type="text" class="easyui-textbox" id="permissionName" name="permissionName"></input>
					            </td>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="permission.the.superior.category" /></label>
				                </td>
				                <td>  
				                <!--  <select   style="border:1px solid #EE9A49;">    -->
					                <select style="border:1px solid #EE9572; width:195px;height:22px" id="supMenu" name="parentPermissionId">
					               	 
			            			</select>  
					            </td>
			           		</tr>
			           		
				            <tr>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="permission.form.title.isMenu" /></label>
					            </td>
					            <td>
					                <select id="isMenu" name="isMenu" class="easyui-combobox" editable="false" style="width:173px;">
					                	<option value="1"><s:text name="permission.form.isMenu.y" /></option>
					                	<option value="0"><s:text name="permission.form.isMenu.n" /></option>
					                </select>
					            </td>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="permission.category.number" /></label>
				                </td>                    
				                <td>
					                <input type="text" class="easyui-textbox" id="menuNub" name="permissionSeq"></input>
					            </td>
				            </tr>
				            <!-- 
				            <tr>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.loginType" /></label>
				                </td>
					            <td>
					                <select id="loginType" name="loginType" class="easyui-combobox" style="width:173px;" data-options="required:true,readonly:true,hasDownArrow:false">
					                	<option value="LOCAL"><s:text name="login.list.form.loginType.LOCAL" /></option>
					                	<option value="LDAP"><s:text name="login.list.form.loginType.LDAP" /></option>
					                </select>
					            </td>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.userEmail" /></label>
				                </td>
				                <td>  
					                <input type="text" class="easyui-textbox" id="userEmail" name="userEmail" data-options="required:true,validType:'email'"></input>  
					            </td>
				            </tr>
				             -->
				             <tr>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="permission.label.key" /></label>
				                </td>
				                <td colspan="3">
					                <input type="text" class="easyui-textbox" id="labelKey" name="labelKey"></input>  
					            </td>
				            </tr>
				            <tr>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="permission.form.title.permissionUrl" /></label>
				                </td>
				                <td colspan="3">
					                <input type="text" class="easyui-textbox" id="menuUrl" name="permissionUrl" ></input>  
					            </td>
				            </tr>
				            <tr>
					            <td align="right">  
					                <label><s:text name="permission.form.title.comments" /></label>
				                </td>
				                <td colspan="3">
					                <input type="text" class="easyui-textbox" id="menuDes" name="comments"></input>  
					            </td>
				            </tr>
				            <tr>
					            <td align="right">  
					                <label><s:text name="permission.form.title.permissionCode" /></label>
				                </td>
				                <td>  
					                <input type="text" class="easyui-textbox" id="menuPho" name="permissionCode"></input>  
					            </td>
					            <td align="right">  
					                <label><s:text name="permission.form.title.buttonId" /></label>
				                </td>
				                <td>  
					                <input type="text" class="easyui-textbox" id="pageMenu" name="buttonId"></input>  
					            </td>
				            </tr>
				            
				           
				        </table>
				        <input id = "isAddorUpdate" value = "" type="hidden"></input>
				        <input id = "permissionId" name = "permissionId" type="hidden">
		    		</div>
		    		<div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
		    			<a href="javascript:obj.save();" class="easyui-linkbutton" icon="icon-save"><s:text name="permission.form.but.submit" /></a>  &nbsp;
		                <a href="javascript:obj.closeWindow();" class="easyui-linkbutton" icon="icon-clear"><s:text name="permission.form.but.close" /></a>
		    		</div>
	    		</div>
		        
			</form>
		</div>  
	</body>
</html>