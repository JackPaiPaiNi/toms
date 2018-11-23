<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@page import="cn.tcl.common.WebPageUtil"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String party = (String) request.getSession().getAttribute("loginUserId");
	String isCanEdit = WebPageUtil.canEdit() + "";
%>
<!DOCTYPE html>
<html>
	<head>
		<title><s:text name='permission.labelkey.yhwh'/></title>
		<script>var my_role_id='<%=party%>';</script>
		<script>var isCanEdit='<%=isCanEdit%>';</script>
		<script language="javascript" src="<%=basePath%>js/platform/user/userList.js" ></script>
	
		
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
				
				<div id="searchPanel" class="easyui-panel" style="background:#f2f6f8;border-bottom:0px;" title="<s:text name="login.list.title" />" data-options="iconCls:'icon-search',collapsible:false">
					<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importUser();"   tcl-text="user.list.but.importUser"  >  </a> 
	    	<a href="<%=basePath%>download/UserInfo.xlsx" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
	    	 
					<label for="searchMsg"><s:text name="party.form.title.countryId" /></label>
					<input type="text" id="selectQuertyPartyId" name="selectQuertyPartyId"></input>
					
					<label for="searchMsg"><s:text name="login.list.conditions" /></label><input name="searchMsg" id="searchMsg" type="text" class="easyui-textbox"></input>
					<a id="searchBut" href="javascript:obj.search()" class="easyui-linkbutton" iconCls="icon-search" plain="true" ><s:text name="login.list.querybut" /></a> 
				
				</div>
				
			</div>
			<div region="center">
				<table id="userListTable"></table>
			</div>
			
		</div>
		<!-- 表单 -->  
		<div id="userWindow" class="easyui-window tcladdwin" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false" title="<s:text name="login.list.form.title" />" icon="icon-save" style="width: 800px; height: 360px;background-color: #f2f6f8;">
		    <form id="userForm" method="post" fit="true" style="height:320px;">
		    	<div class="easyui-layout" data-options="fit:true">
		    		<div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
		    			<table class="dir" style="width: 100%;">
				        	<tr>
					        	<td align="right">  
					                <label><s:text name="login.list.form.userWorkNum" /></label>
				                </td>
				                <td>
				                	<input type="hidden" id="addOrEdit" name="addOrEdit"></input>
					                <input type="text" class="easyui-textbox" id="userWorkNum" name="userWorkNum"></input>
					            </td>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.userLoginId" /></label>
					            </td>
					            <td>  
					                <input type="text" class="easyui-textbox"  id="userLoginId" name="userLoginId" data-options="required:true"></input>
					                <input type="hidden" id="addUserLoginId" name="addUserLoginId" ></input>
					            </td>  
			           		</tr>
			           		<tr>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.userName" /></label>
					            </td>
					            <td>  
					                <input type="text" class="easyui-textbox" id="userName" name="userName" data-options="required:true"></input>  
					            </td>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.userLocale" /></label>
				                </td>
				                <td>  
					                <select id="userLocale" name="userLocale" editable="false" data-options="required:true,editable:false">
			            			</select>  
					            </td>
				            </tr>  
				            <tr>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.enabled" /></label>
					            </td>
					            <td>
					                <select id="enabled" name="enabled" class="easyui-combobox" editable="false" style="width:173px;" data-options="required:true,editable:false">
					                	<option value="1"><s:text name="login.list.form.enabled.y" /></option>
					                	<option value="0"><s:text name="login.list.form.enabled.n" /></option>
					                </select>
					            </td>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.disabledDateTime" /></label>
					            </td>
					            <td>  
					                <input type="text" class="easyui-datebox" id="disabledDateTime" name="disabledDateTime" data-options="required:true"></input>  
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
					                <label><span style="color:red">*</span><s:text name="login.list.form.userEmail" /></label>
				                </td>
				                <td colspan="3">
				                	<input type="hidden" id="loginType" name="loginType" value="LOCAL"></input>
					                <input type="text" class="easyui-textbox" id="userEmail" name="userEmail" data-options="required:true,validType:'email',width:'93%'"></input>  
					            </td>
				            </tr>
				            <tr>
					            <td align="right">  
					                <label><s:text name="login.list.form.userMcId" /></label>
				                </td>
				                <td>  
					                <input type="text" class="easyui-textbox" id="userMcId" name="userMcId"></input>  
					            </td>
					            <td align="right">  
					                <label><s:text name="login.list.form.userTelNum" /></label>
				                </td>
				                <td>  
					                <input type="text" class="easyui-textbox" id="userTelNum" name="userTelNum"></input>  
					            </td>
				            </tr>
				            <tr>
					            <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.partyId" /></label>
				                </td>
				                <td>  
					                <input type="text" class="easyui-textbox" editable="false" id="partyId" name="partyId" data-options="required:true,editable:false"></input>  
					            </td>
					            
					            
					             <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.party" /></label>
				                </td>
				                <td>  
					                <input type="text" class="easyui-textbox" editable="false" id="party" name="party" data-options="required:true,editable:false"></input>  
					            </td>
					            
					           
				            </tr>
				            <tr>
				              <td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.roleId" /></label>
				                </td>
				                <td>  
					                <input type="text" class="easyui-textbox" id="roleId" editable="false" name="roleId" data-options="required:true,editable:false"></input>  
					            </td>
					            
					            <td align="right">
					            	 <label><s:text name="login.list.form.roleId.level" /></label>
					            </td>
					             <td>  
					               <!--  <input type="text" class="easyui-textbox" editable="false" id="userLevel" name="userLevel" ></input> -->  
					            	 <select style="width:235px;height:22px;" id="userLevel" name="userLevel" >
					            	 </select>
					            </td>
					            
				            </tr>
				            <tr id="ptr">
				            
					          
				            	<td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.password" /></label>
					            </td>
					            <td>  
					            <!-- 隐藏旧微信 -->
					            	<input id = "newUserLoginId" value="" type="hidden" name="newUserLoginId">
					                <input type="password" class="easyui-textbox" id="password" name="password" data-options="required:true"></input>  
					            </td>
				            </tr>
				            <tr>
					            <td colspan="4" align="center">  
					                
					            </td>  
				            </tr>
				            
				        </table>
				        
		    		</div>
		    		<div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
		    			<a href="javascript:obj.save();" class="easyui-linkbutton" icon="icon-save"><s:text name="login.list.form.but.save" /></a>  &nbsp;
		                <a href="javascript:obj.closeWindow();" class="easyui-linkbutton" icon="icon-clear"><s:text name="login.list.form.but.close" /></a>
		    		</div>
	    		</div>
			</form>
		</div>  
		<!-- 修改密码 -->
		<div id="passwordWindow" class="easyui-window tcladdwin" data-options="closed:true,collapsible:false,minimizable:false,maximizable:false" title="<s:text name="login.list.form.title.password" />" icon="icon-save" style="width: 400px; height: 190px;background-color: #f2f6f8;">
		    <form id="passwordForm" method="post" fit="true" style="height:150px;">
		    	<div class="easyui-layout" data-options="fit:true">
		    		<div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
		    			<table class="dir" style="width: 100%;">
				            <tr>
				            	<td align="right">  
					                <label><span style="color:red">*</span><s:text name="login.list.form.password" /></label>
					            </td>
					            <td>  
					            	<input type="hidden" id="editUserLoginId" name="editUserLoginId" />
					                <input type="password" class="easyui-textbox" id="updatePassword" name="updatePassword" data-options="required:true"></input>  
					            </td>
				            </tr>
				            <tr>
					            <td colspan="4" align="center">  
					                
					            </td>  
				            </tr>
				        </table>
		    		</div>
		    		<div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
		    			<a href="javascript:obj.updatePassword();" class="easyui-linkbutton" icon="icon-save"><s:text name="login.list.form.but.save" /></a>  &nbsp;
		                <a href="javascript:obj.closeWindow2();" class="easyui-linkbutton" icon="icon-clear"><s:text name="login.list.form.but.close" /></a>
		    		</div>
	    		</div>
			</form>
		</div> 
	</body>
	
</html>