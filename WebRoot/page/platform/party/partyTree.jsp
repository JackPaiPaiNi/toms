<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<title><s:text name='permission.labelkey.jgwh'/></title>
		<script language="javascript" src="<%=basePath%>js/platform/party/partyTree.js" ></script>
		<style type="text/css">
			
		</style>
	</head>
	<body>
		<!-- 树控件 -->
		<ul id="partyTree"></ul>
		
		<!-- 树右键菜单 -->
		<div id="mm" class="easyui-menu" style="width:120px;">
			<div onclick="obj.add()" data-options="iconCls:'icon-add'"><s:text name="party.menu.addParty" /></div>
			<div onclick="obj.edit()" data-options="iconCls:'icon-edit'"><s:text name="party.menu.modifyParty" /></div>
			<div onclick="obj.del()" data-options="iconCls:'icon-remove'"><s:text name="party.menu.delParty" /></div>
			<div onclick="obj.refrash()" data-options="iconCls:'icon-reload'"><s:text name="party.menu.reflashParty" /></div>
		</div>
		
		<!-- 菜单表单 -->
		<div id="partyWindow" class="easyui-window tcladdwin" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false" title="<s:text name="party.tree.title" />" icon="icon-save" style="width: 600px; height: 400px;background-color: #f2f6f8;">
		    <form id="partyForm" method="post">
		        <table class="dir" style="width: 100%;">
		        	<tr style="display:none;">
			        	<td align="right">  
			                <label><s:text name="party.form.title.partyId" /></label>
		                </td>
		                <td>
		                	<input type="hidden" id="parentPartyId" name="parentPartyId"></input>
			                <input type="text" class="easyui-textbox" id="partyId" name="partyId"></input>
			            </td>
			        </tr>
			        <tr>
			        	<td align="right">  
			                <label><span style="color:red">*</span><s:text name="party.form.title.partyName" /></label>
		                </td>
		                <td>
			                <input type="text" class="easyui-textbox" id="partyName" name="partyName" data-options="required:true"></input>
			            </td>
			        </tr>
			        <tr>
			        	<td align="right">  
			                <label><span style="color:red">*</span><s:text name="party.form.title.federalTaxId" /></label>
		                </td>
		                <td>
			                <input type="text" class="easyui-textbox" id="federalTaxId" name="federalTaxId" data-options="required:true"></input>
			            </td>
			        </tr>
	           		<tr>
			            <td align="right">  
			                <label><span style="color:red">*</span><s:text name="party.form.title.status" /></label>
			            </td>
			            <td>  
			                <select id="status" name="status" class="easyui-combobox" data-options="required:true">
			                	<option value="1" selected="selected"><s:text name="party.form.status.y" /></option>
			                	<option value="0"><s:text name="party.form.status.n" /></option>
			                </select>
			            </td>  
	           		</tr>
	           		<tr>
			            <td align="right">  
			                <label><s:text name="party.form.title.isConutry" /></label>
			            </td>
			            <td>
			            	<input id="countryId" type="hidden" name="countryId" />  
			                <select id="isCountry" name="isCountry" class="easyui-combobox" data-options="required:true">
			                	<option value="1" selected="selected"><s:text name="party.form.isCountry.y" /></option>
			                	<option value="0"><s:text name="party.form.isCountry.n" /></option>
			                </select>
			            </td>  
	           		</tr>
	           		<tr>
			            <td align="right">  
			                <label><s:text name="party.form.title.groupNameAbbr" /></label>
			            </td>
			            <td>  
			                <input type="text" class="easyui-textbox" id="groupNameAbbr" name="groupNameAbbr"></input>  
			            </td> 
			        </tr>
	           		<tr> 
			            <td align="right">  
			                <label><s:text name="party.form.title.partyIdLayer" /></label>
			            </td>
			            <td>  
			                <input type="text" class="easyui-textbox" id="partyIdLayer" name="partyIdLayer"></input>    
			            </td> 
		            </tr>  
		            <tr>
			            <td align="right">  
			                <label><s:text name="party.form.title.comments" /></label>
			            </td>
			            <td>
			                <input type="text" class="easyui-textbox" id="comments" name="comments" multiline=true style="height:60px"></input>  
			            </td>
		            </tr>
		            <tr>
			            <td colspan="2" style="text-align:center;">  
			                <a href="javascript:obj.save();" class="easyui-linkbutton" icon="icon-save"><s:text name="party.form.but.submit" /></a>  &nbsp;
			                <a href="javascript:obj.close();" class="easyui-linkbutton" icon="icon-clear"><s:text name="party.form.but.close" /></a>
			            </td>  
		            </tr>
		        </table>
			</form>
		</div>
	</body>
</html>