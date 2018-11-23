<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<title><s:text name='permission.labelkey.cdwh'/></title>
		<script language="javascript" src="<%=basePath%>js/platform/permission/permissionTree.js" ></script>
		<style type="text/css">
			
		</style>
	</head>
	<body>
		<!-- 树控件 -->
		<ul id="permissionTree"></ul>
		
		<!-- 树右键菜单 -->
		<div id="mm" class="easyui-menu" style="width:120px;">
			<div onclick="obj.add()" data-options="iconCls:'icon-add'"><s:text name="permission.menu.addPermission" /></div>
			<div onclick="obj.edit()" data-options="iconCls:'icon-edit'"><s:text name="permission.menu.modifyPermission" /></div>
			<div onclick="obj.del()" data-options="iconCls:'icon-remove'"><s:text name="permission.menu.delPermission" /></div>
			<div onclick="obj.refrash()" data-options="iconCls:'icon-reload'"><s:text name="permission.menu.reflashPermission" /></div>
		</div>
		
		<!-- 菜单表单 -->
		<div id="permissionWindow" class="easyui-window tcladdwin" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false" title="<s:text name="permission.tree.title" />" icon="icon-save" style="width: 600px; height: 340px;background-color: #f2f6f8;">
		    <form id="permissionForm" method="post">
		        <table class="dir" style="width: 100%;">
		        	<tr style="display:none;">
			        	<td align="right">  
			                <label><s:text name="permission.from.title.permissionId" /></label>
		                </td>
		                <td>
		                	<input type="hidden" id="parentPermissionId" name="parentPermissionId"></input>
			                <input type="text" class="easyui-textbox" id="permissionId" name="permissionId"></input>
			            </td>
			        </tr>
			        <tr>
			        	<td align="right">  
			                <label><span style="color:red">*</span><s:text name="permission.from.title.permissionName" /></label>
		                </td>
		                <td>
			                <input type="text" class="easyui-textbox" id="permissionName" name="permissionName" data-options="required:true"></input>
			            </td>
			        </tr>
			        <tr>
			        	<td align="right">  
			                <label><span style="color:red">*</span><s:text name="permission.from.title.labelKey" /></label>
		                </td>
		                <td>
			                <input type="text" class="easyui-textbox" id="labelKey" name="labelKey" data-options="required:true"></input>
			            </td>
			        </tr>
			        <tr>
			        	<td align="right">  
			                <label><span style="color:red">*</span><s:text name="permission.form.title.isMenu" /></label>
		                </td>
		                <td>
		                	<select id="isMenu" name="isMenu" class="easyui-combobox" data-options="required:true">
			                	<option value="1" selected="selected"><s:text name="permission.form.isMenu.y" /></option>
			                	<option value="0"><s:text name="permission.form.isMenu.n" /></option>
			                </select>
			            </td>
			        </tr>
	           		<tr>
			            <td align="right">  
			                <label><s:text name="permission.form.title.permissionCode" /></label>
			            </td>
			            <td>  
			                <input type="text" class="easyui-textbox" id="permissionCode" name="permissionCode"></input>
			            </td>  
	           		</tr>
	           		<tr>
			            <td align="right">  
			                <label><s:text name="permission.form.title.permissionUrl" /></label>
			            </td>
			            <td>  
			                <input type="text" class="easyui-textbox" id="permissionUrl" name="permissionUrl"></input>  
			            </td> 
			        </tr>
	           		<tr> 
			            <td align="right">  
			                <label><s:text name="permission.form.title.buttonId" /></label>
			            </td>
			            <td>  
			                <input type="text" class="easyui-textbox" id="buttonId" name="buttonId"></input>    
			            </td> 
		            </tr>  
		            <tr>
			            <td align="right">  
			                <label><s:text name="permission.form.title.comments" /></label>
			            </td>
			            <td>
			                <input type="text" class="easyui-textbox" id="comments" name="comments" multiline=true style="height:60px"></input>  
			            </td>
		            </tr>
		            <tr>
			            <td colspan="2"  style="text-align:center;">  
			                <a href="javascript:obj.save();" class="easyui-linkbutton" icon="icon-save"><s:text name="permission.form.but.submit" /></a>  &nbsp;
			                <a href="javascript:obj.close();" class="easyui-linkbutton" icon="icon-clear"><s:text name="permission.form.but.close" /></a>
			            </td>  
		            </tr>
		        </table>
			</form>
		</div>
	</body>
</html>