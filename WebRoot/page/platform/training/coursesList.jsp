<%@page import="cn.tcl.platform.role.vo.Role"%>
<%@page import="cn.tcl.platform.role.service.IRoleService"%>
<%@page import="cn.tcl.platform.party.service.IPartyService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.tcl.common.WebPageUtil"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String roleId =WebPageUtil.getLoginedUser().getRoleId();
	String party = (String) request.getSession().getAttribute("loginUserId");
	String partyId=WebPageUtil.getLoginedUser().getPartyId();
	ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(application);
	IPartyService partyService=(IPartyService)context.getBean("partyService");
	IRoleService IRoleService = (IRoleService)context.getBean("roleService");
	String countryId=partyService.selectPartyById(partyId).getCountryId();
	
	Role role = IRoleService.getRoleName(roleId);
	String roleName="";
	if(role!=null && !role.equals("")){
		 roleName = role.getRoleName();
	}
	
	String countryName="HQ";
	String regionId="999";
	String regionName="HQ";
	if(!countryId.equals("999"))
	{
		regionId=partyService.selectPartyById(countryId).getParentPartyId();
		regionName=partyService.selectPartyById(regionId).getPartyName();
		countryName=partyService.selectPartyById(countryId).getPartyName();
		System.out.println(regionId+"========"+countryName+"==============="+regionName);
	}
	System.out.println(party+"------------------------userId");
%>

<!DOCTYPE HTML>
<html>
  <head>
    
    <title><s:text name="permission.labelkey.cklist"></s:text></title>
    
	
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<script> var my_login_id='<%=party%>'; var roleId="<%=roleId%>";</script>
	
	
	<%-- <link rel="stylesheet" href="<%=basePath%>kindeditor/themes/default/default.css" />
	<script charset="utf-8" src="<%=basePath%>kindeditor/kindeditor-min.js"></script>
	<script charset="utf-8" src="<%=basePath%>kindeditor/lang/zh_CN.js"></script> --%>
	
	<script type="text/javascript" src="<%=basePath%>ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=basePath%>ueditor/ueditor.all.js"></script>
	<script type="text/javascript" src="<%=basePath%>ueditor/ueditor.parse.js"></script>
	<script type="text/javascript" src="<%=basePath%>ueditor/lang/en/en.js"></script>
	<link rel="stylesheet" href="<%=basePath%>ueditor/themes/default/css/ueditor.css" type="text/css"></link>
	<script type="text/javascript" src="<%=basePath%>js/platform/training/courselist.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/platform/training/fileUpload.js"></script>
	<link rel="stylesheet" href="<%=basePath%>ueditor/third-party/video-js/video-js.css" type="text/css"></link>
	<script type="text/javascript" src="<%=basePath%>ueditor/third-party/video-js/video.js"></script>
	<link rel="stylesheet" href="<%=basePath%>css/training/coursesList.css" type="text/css"></link>
	
	<script>
		var regionId="<%=regionId%>";
		var countryId="<%=countryId%>";
		var regionName="<%=regionName%>";
		var countryName="<%=countryName%>";
		var roleName="<%=roleName%>";
		var ue = UE.getEditor('editor');		
	</script>	
  	<style type="text/css">
  		select{height: 22px;}
  		.tcladdwin form table tr td, .wintable tr td {
		    padding: 2px 0;
		}
  	</style>
 
  
  </head>
  
  <body>
	
	<table id="courselist"></table>
	<div id="coursetb" class="easyui-panel" style="background:#f2f6f8;border-bottom:0px;"  data-options="collapsible:false">
		<div class="main">        	
        	<!--查询条件-->
        	<div class="Query-Condition">
        		&nbsp;&nbsp;&nbsp;&nbsp;<span tcl-text="district.countryName"></span>
        		<select id="countryName">
        			
        		</select>
        		<span>&nbsp;&nbsp;&nbsp;&nbsp;Column</span>
        		<select id="column" name="column">
       			
        		</select>
        		
        		
        			<span>&nbsp;&nbsp;&nbsp;&nbsp;Sub-column</span>
	        		<select id="subcolumn" name="subcolumn">
	        			 
	        		</select>
	        		<select id="subtocolumn" name="subtocolumn">
						 	<option value="0">Please Select The Sub-Column</option>
					</select>
					<span>&nbsp;&nbsp;&nbsp;&nbsp;Message Type</span>
					<select id="messagetype" name="messagetype">
	     				<option value="1"><s:text name="train.msg.graphic"></s:text></option>
	     				<option value="2"><s:text name="train.msg.articl"></s:text></option>
	     				<option value="3"><s:text name="train.msg.video"></s:text></option> 				
	     			</select>
	     			<span>&nbsp;&nbsp;&nbsp;&nbsp;Keyword</span>
	     			<input class="easyui-textbox" id="searchCourse" style="width:110px">&nbsp;
        		<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search" id="searchBt" onclick="doSearch();" tcl-text="toolbar.search"></a>
        		<div class="tcl-gridbar-separator"></div>
        		
        	
        		
        	</div>
       	</div> 
	</div>
	
	<div id="dlg">
		<img  src="" id="simg">
	</div>
	<div id="addCourseWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cptmdzb.add'/>"
		data-options="width:900,height:540,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true" >
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;" >
	            <form id="addCourseForm" method="post" enctype="multipart/form-data"  accept="image/gif,image/jpeg,image/jpg,image/png,/image/bmp">
	            	<input type="hidden" name="editId"/>
	            	<table style="width: 100%;">
	            		<tr>
							<td><img id="imgUrl" src='<%=basePath%>images/training/loading.gif'/></td>
						</tr> 
	            		
	            		<tr>
					           <td ><i class="required">*</i><span style="color:red"></span><s:text name="train.oneColumn" />:</td>
				               <td><select style="width:174px"  id="levelOneTypeId" name="levelOneTypeId" <%-- data-options="required:true,editable:false,panelHeight:'auto'" --%>></select></td>	
				               <td ><i class="required">*</i><span style="color:red"></span><s:text name="train.twoColumn" />:</td>
				               <td><select style="width:174px"  id="levelTwoTypeId" name="levelTwoTypeId" <%-- data-options="required:true,editable:false,panelHeight:'auto'" --%>></select></td>					         			           		   
			           	</tr>
			           	<tr>
					           <td ><i class="required">*</i><span style="color:red"></span><s:text name="train.threeColumn" />:</td>
				               <td><select style="width:174px"  id="levelThreeTypeId" name="levelThreeTypeId" <%-- data-options="required:true,editable:false,panelHeight:'auto'" --%>></select></td>					         			           					           				         			           		   
			           	</tr>
	            		<tr>
					           <td ><i class="required">*</i><span style="color:red"></span><s:text name="train.messageType" />:</td>
				               <!-- <td><input type="text"  id="messageType" name="messageType" data-options="required:true,editable:false,panelHeight:'auto'"></input></td>	 -->				         
			           		   <td><select id="messageType" name="messageType" style="width:174px;height: 23px">
			           		   		<option value="1">Graphic</option>
			           		   		<option value="2">Article</option>
			           		   		<option value="3">Video</option>
			           		   		</select></td>
			           		   <td ><s:text name="train.coverImgUrl" />:</td>
			           		   <td><input  id="file" type="file" onchange="check()"  name="fileUploadTools.uploadFile" / style="display: inline-block;width:200px;"><input type="hidden" id="hidcover" name="coverImgUrl"/><input  id="coverupload" type="button" value="upload" onclick="upload()"><%-- <i style="display:inline-block;width:20px;height: 20px; background:url(<%=basePath%>/images/training/upload2.png) no-repeat center center;background-size: 12px 18px;cursor: pointer;"></i> --%></td>
			           	</tr>
			           	
			           	 <tr>
					          	
				         		<td ><%-- <i class="required">*</i><span style="color:red"></span> --%><s:text name="train.correspondingPartyId" />:</td>
				                <!-- <td><input type="text"  id="correspondingRegionId"  	name="correspondingRegionId"  class="easyui-combobox"	 data-options="required:true,editable:false"></input></td> -->
				                <td><select   id="correspondingRegionId"  	name="correspondingRegionId" style="width:174px"></select></td>
				                <td ><!-- <i class="required">*</i> --><s:text name="train.country" />:</td>
				                <!-- <td><input type="text"  id="correspondingCountryId"  name="correspondingCountryId" class="easyui-combobox"	 data-options="required:true,editable:false"></input></td> -->
				                <td><select   id="correspondingCountryId"  	name="correspondingCountryId" style="width:174px"></select></td>
				         </tr>  
				         
				         <%if(countryId.equals("999")){ %>
	            	 <%-- 	<tr>
	            	 		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
	            			<td><select type="text" style="width:154px;height: 23px" id="countryId" name="countryId"></select></td>
	            	 	</tr> --%>
	            	 	<tr id="role1">
	            	 		<td><i class="required">*</i>RoleType:</td>	            	 		
	            	 		<td>
			     			 <select id="roleTypeName" style="width:174px;height: 23px"	name="roleTypeName"  data-options="required:true,editable:false" editable="false">
				                	<option value=""><s:text name="msg.form.title.roleType.Select" /></option>
				                	<option value="-1">All</option>
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
							<td ><s:text name="train.state" />: </td>
					           <!-- <td><input type="text"  id="state"  name="state" panelHeight="auto"	data-options="required:false,editable:false"></input></td> -->
					           <td><select id="state"  name="state" style="width:174px;height: 23px">
					           		<option value="0">Normal</option>
					           		<option value="1">Pushed</option>
					           </select></td>					 
					 	</tr>
					 	<%}else{ %>	
					 		<tr id="role1">
					 		<%-- <td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
	            			<td><select type="text" style="width:154px;height: 23px" id="countryId" name="countryId"></select></td> --%>
	            	 		<td>RoleType:</td>	            	 		
	            	 		<td>
			     			 <select id="roleTypeName" style="width:174px;height: 23px"	name="roleTypeName"  data-options="required:true,editable:false" editable="false">
				                	<option value=""><s:text name="msg.form.title.roleType.Select" /></option>
				                	<option value="-1">All</option>
				                	<option value="SALES"><s:text name="msg.form.title.roleType.SALES" /></option>
				                	<option value="SUPERVISOR"><s:text name="msg.form.title.roleType.SUPERVISOR" /></option>
				                	<option value="PROM"><s:text name="msg.form.title.roleType.PROM" /></option>			                	
				                	<option value="BADMIN"><s:text name="msg.form.title.roleType.BADMIN" /></option>
				                	<option value="BLEADER"><s:text name="msg.form.title.roleType.BLEADER" /></option>
				                	<option value="BTEAMER"><s:text name="msg.form.title.roleType.BTEAMER" /></option>
				                	<option value="REGIONAL"><s:text name="msg.form.title.roleType.REGIONAL" /></option>
							 </select>
							</td>
							<td ><s:text name="train.state" />: </td>
					           <!-- <td><input type="text"  id="state"  name="state" panelHeight="auto"	data-options="required:false,editable:false"></input></td> -->
					           <td><select id="state"  name="state" style="width:174px;height: 23px">
					           		<option value="0">Normal</option>
					           		<option value="1">Pushed</option>
					           </select></td>					 
					 	</tr>
					 	<%} %>
					 	<tr id="role2">
					 		<td style="vertical-align: top;">All User:</td>
					 		<td colspan="3">
					 			<div class="inb select_from">
									<select size='10' multiple id="selectLeft">									
									</select>
								</div>
					 			<div class="inb" style="vertical-align: top;padding-top: 12px;">
									<input type="button" value=" &gt; " id="toRight" style="width:37px;"/><br />
									<input type="button" value=" &gt;&gt " id="toRightAll" style="margin-top:10px;width:37px;"/><br />
									<input type="button" value=" &lt; " id="toLeft" style="margin-top:10px;width:37px;"/><br />
									<input type="button" value=" &lt;&lt " id="toLeftAll" style="margin-top:10px;"/>
								</div>
								<div class="inb select_to">
									<select size='10' multiple id="selectRight">										
									</select>
								</div>
					 		</td>
					 	</tr>
				         
			           	<tr>
					           <td ><i class="required">*</i><span style="color:red"></span><s:text name="train.CourseTitle" />:</td>
				               <!-- <td colspan="2"><input  class="easyui-textbox" id="courseTitle" style="width:383px;height:40px;" class="easyui-textbox easyui-validatebox" name="courseTitle" data-options="multiline:true"></input></td> -->
				                <td colspan="2"><textarea   id="courseTitle" style="width:383px;height:40px;resize: none;"  name="courseTitle" data-options="multiline:true"></textarea></td>			         			           		   
			           	</tr>
			           	
			           	<tr>
			           			 <td ><i class="required">*</i><span style="color:red"></span><s:text name="train.CourseSummary" />: </td>
			           			<!-- <td colspan="2"><input   class="easyui-textbox" id="courseSummary" style="width:383px;height:40px;margin-top: 2px;" name="courseSummary" data-options="multiline:true"></input></td> -->
			           			<td colspan="2"><textarea rows="" cols="" id="courseSummary" style="width:383px;height:40px;margin-top: 2px;resize: none;" name="courseSummary" ></textarea></td>
			           	</tr>					         
			           		
			           	
					   <tr>		
					         	<td ><i class="required">*</i><span style="color:red"></span><s:text name="train.courseContent" />:</td>					           
					            <td colspan="3"><textarea id="editor" id="courseContent" name="courseContent"	style="width:700px;height:250px;margin-top: 2px;"></textarea></td>
					    </tr>
					    
	            	</table>
	            	
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a 	id="submitBtn"	class="btn btn-warning btn-sm" onclick="submitForm()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
	        	<a class="btn btn-warning btn-sm" onclick="clearForm()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
	</div>
	
  </body>
</html>

