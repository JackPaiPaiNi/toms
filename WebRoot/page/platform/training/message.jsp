<%@page import="cn.tcl.common.WebPageUtil"%>
<%@page import="cn.tcl.platform.party.service.IPartyService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String party = (String) request.getSession().getAttribute("loginUserId");
String partyId=WebPageUtil.getLoginedUser().getPartyId();
ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(application);
IPartyService partyService=(IPartyService)context.getBean("partyService");
String countryId=partyService.selectPartyById(partyId).getCountryId();
%>


<!DOCTYPE HTML>
<html>
  <head>

    
    <title><s:text name="permission.labelkey.msg"/></title>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
  	<script  src="<%=basePath%>js/platform/training/message.js"></script>
  	<script> var my_login_id="<%=party%>";</script>
	
  <link rel="stylesheet" href="<%=basePath%>css/training/message.css" type="text/css"></link>
  
  </head> 
  <body>
  	
  	<!-- 消息表 -->
    <table id="msglist"></table>    
    
    
    	<div id="addMsgWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cptmdzb.add'/>"
		data-options="width:700,height:540,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true" >
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;" >
	            <form id="addMsgForm" method="post" enctype="multipart/form-data"  accept="image/gif,image/jpeg,image/jpg,image/png,/image/bmp">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		
	            	 <%if(countryId.equals("999")){ %>
	            	 	<tr>
	            	 		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
	            			<td><select type="text" style="width:154px;height: 23px" id="countryId" name="countryId"></select></td>
	            	 		<td>RoleType:</td>	            	 		
	            	 		<td>
			     			 <select id="roleTypeName" style="width:200px;height: 23px"	name="roleTypeName"  data-options="required:true,editable:false" editable="false">
				                	<option value="0"><s:text name="msg.form.title.roleType.Select" /></option>
				                	<option value="SALES"><s:text name="msg.form.title.roleType.SALES" /></option>
				                	<option value="SUPERVISOR"><s:text name="msg.form.title.roleType.SUPERVISOR" /></option>
				                	<option value="PROM"><s:text name="msg.form.title.roleType.PROM" /></option>
			                		<option value="HLEADER"><s:text name="msg.form.title.roleType.HLEADER" /></option>
				                	<option value="HTEAMER"><s:text name="msg.form.title.roleType.HTEAMER" /></option>
				                	<option value="BADMIN"><s:text name="msg.form.title.roleType.BADMIN" /></option>
				                	<option value="BLEADER"><s:text name="msg.form.title.roleType.BLEADER" /></option>
				                	<option value="BTEAMER"><s:text name="msg.form.title.roleType.BTEAMER" /></option>
				                	<option value="REGIONAL"><s:text name="msg.form.title.roleType.REGIONAL" /></option>
							 </select>
							</td>					 
					 	</tr>
					 	<%}else{ %>	
					 		<tr>
					 		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
	            			<td><select type="text" style="width:154px;height: 23px" id="countryId" name="countryId"></select></td>
	            	 		<td>RoleType:</td>	            	 		
	            	 		<td>
			     			 <select id="roleTypeName" style="width:200px;height: 23px"	name="roleTypeName"  data-options="required:true,editable:false" editable="false">
				                	<option value="0"><s:text name="msg.form.title.roleType.Select" /></option>
				                	<option value="SALES"><s:text name="msg.form.title.roleType.SALES" /></option>
				                	<option value="SUPERVISOR"><s:text name="msg.form.title.roleType.SUPERVISOR" /></option>
				                	<option value="PROM"><s:text name="msg.form.title.roleType.PROM" /></option>			                	
				                	<option value="BADMIN"><s:text name="msg.form.title.roleType.BADMIN" /></option>
				                	<option value="BLEADER"><s:text name="msg.form.title.roleType.BLEADER" /></option>
				                	<option value="BTEAMER"><s:text name="msg.form.title.roleType.BTEAMER" /></option>
				                	<option value="REGIONAL"><s:text name="msg.form.title.roleType.REGIONAL" /></option>
							 </select>
							</td>					 
					 	</tr>
					 	<%} %>
					 	<tr>
					 		<td style="vertical-align: top;">All User:</td>
					 		<td>
					 			<div class="inb select_from">
									<select size='10' multiple id="selectLeft">									
									</select>
								</div>
					 			<div class="inb" style="vertical-align: top;padding-top: 50px;">
									<input type="button" value=" >> " id="toRight" /><br /><br />
									<input type="button" value=" << " id="toLeft" />
								</div>
								<div class="inb select_to">
									<select size='10' multiple id="selectRight" name="selectRight">										
									</select>
								</div>
					 		</td>
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
