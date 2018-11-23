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
		<title><s:text name='permission.labelkey.jswh'/></title>
		<script language="javascript" src="<%=basePath%>js/platform/role/roleList.js" ></script>
		<style type="text/css">
			#searchPanel{
				font-size:9pt;
			}
		</style>
	</head>
	<body>
		<div class="easyui-layout" fit="true">
			<div region="north" style="height:58px;">
				<!-- 表头查询条件 -->
				<div id="searchPanel" class="easyui-panel" style="background:#f2f6f8;border-bottom:0px;" title="<s:text name="role.grid.title" />" data-options="iconCls:'icon-search',collapsible:false">
					<label for="searchMsg"><s:text name="role.grid.conditions" /></label><input name="searchMsg" id="searchMsg" type="text" class="easyui-textbox"></input>
					<a id="searchBut" href="javascript:obj.search()" class="easyui-linkbutton" iconCls="icon-search" plain="true" ><s:text name="role.grid.querybut" /></a> 
				</div>
			</div>
		<div region="center">
			<table id="roleListTable"></table>
		</div>
		
		<!-- 表单 -->  
		<div id="roleWindow" class="easyui-window tcladmin" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false" title="<s:text name="role.grid.title" />" icon="icon-save" style="width: 650px; height: 440px;background-color: #f2f6f8;">
		    <form id="roleForm" method="post" style="height: 404px;">
		    	<div class="easyui-layout" data-options="fit:true">
		    		<div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
		    			 <table class="wintable" style="width: 100%;">
				        	<tr id="roleTypeRow">
					        	<td align="right" style="width:170px;">
					                <label><span style="color:red">*</span><s:text name="role.form.title.roleType" /></label>
				                </td>
				                <td>
					                <select id="roleType" name="roleType" class="easyui-combobox" data-options="required:true,editable:false" editable="false">
					                	<option value=""><s:text name="role.form.title.roleType.Select" /></option>
					                	<option value="SALES"><s:text name="role.form.title.roleType.SALES" /></option>
					                	<option value="SUPERVISOR"><s:text name="role.form.title.roleType.SUPERVISOR" /></option>
					                	<option value="PROM"><s:text name="role.form.title.roleType.PROM" /></option>
<%-- 					                	<option value="HADMIN"><s:text name="role.form.title.roleType.HADMIN" /></option>
 --%>					                	<option value="HLEADER"><s:text name="role.form.title.roleType.HLEADER" /></option>
					                	<option value="HTEAMER"><s:text name="role.form.title.roleType.HTEAMER" /></option>
					                	<option value="BADMIN"><s:text name="role.form.title.roleType.BADMIN" /></option>
					                	<option value="BLEADER"><s:text name="role.form.title.roleType.BLEADER" /></option>
					                	<option value="BTEAMER"><s:text name="role.form.title.roleType.BTEAMER" /></option>
					                	<option value="REGIONAL"><s:text name="role.form.title.roleType.REGIONAL" /></option>
					                </select>
					            </td>
					        </tr>
					        <tr style="display:none;">
					        	<td align="right">
					                <label><span style="color:red">*</span><s:text name="role.form.title.roleid" /></label>
				                </td>
				                <td>
				                	<input type="hidden" id="addOrEdit" name="addOrEdit"></input>
					                <input type="hidden" id="roleId" name="roleId"></input>
					            </td>
					        </tr>
					        <tr>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="role.form.title.rolename" /></label>
					            </td>
					            <td>  
					                <input type="text" class="easyui-textbox" id="roleName" name="roleName" data-options="required:true"></input>
					                <input type="hidden" id="roleNameFalg" name="roleNameFalg" value="T" />
					                <input type="hidden" id="_roleName" name="_roleName" />
					            </td>  
			           		</tr>
			           		<tr>
					            <td align="right">  
					                <label><s:text name="role.form.title.details" /></label>
					            </td>
					            <td>  
					                <input type="text" id="searchUser" name="searchUser"></input>
					            </td>  
			           		</tr>
				            <tr>
					            <td align="center" colspan=4>  
					                <div style="width:91%;padding-left:85px;">
								        <div style="width:40%;height:200px;float:left;">
						                	<label style="float: left;margin-left: 15px;"><s:text name="role.form.title.allUser" /></label>
							          		<select multiple="true" id="selectLeft" ondblclick="obj.leftDBClick(this)" class="roundText" style="width:177px;height:200px;">
							          		</select>
								        </div>
								        <div style="width:9%;height:200px;float:left;margin-left:15px;margin-top: 15px;">
							        		<span>
							          			<input id="add" type="button" class="easyui-linkbutton" onclick="obj.add()" style="width:45px;height:25px;margin-top:15px;" value=">"/>
								          	</span><br />
								          	<span>
								          		<input id="add_all" type="button" class="easyui-linkbutton" onclick="obj.addAll()" style="width:45px;height:25px;margin-top:15px;" value=">>"/>
								          	</span> <br />
								          	<span>
								          		<input id="remove" type="button" class="easyui-linkbutton" onclick="obj.remove()" style="width:45px;height:25px;margin-top:15px;" value="<"/>
								          	</span><br />
								          	<span>
								          		<input id="remove_all" type="button" class="easyui-linkbutton" onclick="obj.removeAll()" style="width:45px;height:25px;margin-top:15px;" value="<<"/>
								          	</span> 
								        </div>
							        	<div style="width:40%;height:200px;float:left;margin-left:15px;">
									        <label style="float: left;margin-left: 15px;"><s:text name="role.form.title.roleUser" /></label>
								          	<select multiple="multiple" id="selectRight" ondblclick="obj.rightDBClick(this)" class="roundText" style="width: 177px;height:200px;">
								          	</select>
								        </div>
						      		</div>
					            </td>
				            </tr>
				          
				        </table>
		    		</div>
		    		<div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
		    			 <a href="javascript:obj.save();" class="easyui-linkbutton" icon="icon-save"><s:text name="role.form.but.submit" /></a>  &nbsp;
		                <a href="javascript:obj.close('roleWindow');" class="easyui-linkbutton" icon="icon-clear"><s:text name="role.form.but.close" /></a>
		    		</div>
		    	</div>
		       
			</form>
		</div> 
		
		<!-- 菜单 -->  
		<div id="permissionWindow" class="easyui-window tcladdwin" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false" title="<s:text name="role.grid.but.permission" />" icon="icon-save" style="width: 320px; height: 450px;background-color: #f2f6f8;">
			<table class="dir" style="width:100%;">
				<tr>
		            <td>
		            	<div style="height:370px;overflow-y:auto;">
		            		<input type="hidden" id="permissionRoleId" name="permissionRoleId"></input>
		            		<ul id="permissionTree"></ul>
		            	</div> 
		            </td>
           		</tr>
           		<tr>
		            <td style="text-align:center;">  
		               	<a href="javascript:obj.savePermission();" class="easyui-linkbutton" icon="icon-save"><s:text name="role.form.but.submit" /></a>  &nbsp;
            			<a href="javascript:obj.close('permissionWindow');" class="easyui-linkbutton" icon="icon-clear"><s:text name="role.form.but.close" /></a>
		            </td>
           		</tr>
           	</table>
		</div>
		
		<!-- 机构 -->  
		<div id="partyWindow" class="easyui-window tcladdwin" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false" title="<s:text name="role.grid.but.party" />" icon="icon-save" style="width: 320px; height: 450px;background-color: #f2f6f8;">
			<table class="dir" style="width:100%;">
				<tr>
		            <td>
		            	<div style="height:370px;overflow-y:auto;">
		            		<input type="hidden" id="partyRoleId" name="partyRoleId"></input>
		            		<ul id="partyTree"></ul>
		            	</div> 
		            </td>
           		</tr>
           		<tr>
		            <td align="center">  
		               	<a href="javascript:obj.saveParty();" class="easyui-linkbutton" icon="icon-save"><s:text name="role.form.but.submit" /></a>  &nbsp;
            			<a href="javascript:obj.close('partyWindow');" class="easyui-linkbutton" icon="icon-clear"><s:text name="role.form.but.close" /></a>
		            </td>
           		</tr>
           	</table>
		</div>
	</body>
</html>