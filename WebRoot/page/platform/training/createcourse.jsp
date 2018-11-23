<%@page import="cn.tcl.platform.role.vo.Role"%>
<%@page import="cn.tcl.platform.role.service.IRoleService"%>
<%@page import="cn.tcl.platform.party.vo.Party"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="cn.tcl.common.WebPageUtil" %>
<%@ page import="org.springframework.context.ApplicationContext" %> 
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="cn.tcl.platform.party.service.IPartyService" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String partyId=WebPageUtil.getLoginedUser().getPartyId();
	String roleId =WebPageUtil.getLoginedUser().getRoleId();
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
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title><s:text name="permission.labelkey.pxck"></s:text></title>
	
	<style>
		form {
			margin: 0;
		}
		textarea {
			display: block;
		}
	</style>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/training/createcourse.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>jquery-ui-1-11/themes/base/all.css"/>  

	<script type="text/javascript" src="<%=basePath%>js/platform/training/createcourse.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js"></script>
	<script type="text/javascript" src="<%=basePath%>jquery-ui-1-11/ui/core.js"></script>
	<script type="text/javascript" src="<%=basePath%>jquery-ui-1-11/ui/widget.js"></script>
	<script type="text/javascript" src="<%=basePath%>jquery-ui-1-11/ui/progressbar.js"></script>
<!--  	<link rel="stylesheet" type="text/css" href="<%=basePath%>jquery-ui-1-11/demos/demos.css"/> -->
	<script type="text/javascript" src="<%=basePath%>js/platform/training/fileUpload.js"></script>
	<%-- <link rel="stylesheet" href="<%=basePath%>kindeditor/themes/default/default.css" />
	<script charset="utf-8" src="<%=basePath%>kindeditor/kindeditor-min.js"></script>
	<script charset="utf-8" src="<%=basePath%>kindeditor/lang/zh_CN.js"></script> --%>
	<script type="text/javascript" src="<%=basePath%>ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="<%=basePath%>ueditor/ueditor.all.js"></script>
	<script type="text/javascript" src="<%=basePath%>ueditor/lang/en/en.js"></script>
	<link rel="stylesheet" href="<%=basePath%>ueditor/themes/default/css/ueditor.css" type="text/css"></link>
	<script>
		<%-- KindEditor.ready(function(K) {
			K.create('textarea[name="content"]', {
				autoHeightMode : true,
				allowImageUpload:true,
				imageUploadJson:"<%=basePath%>"+"kindeditor/jsp/upload_json2.jsp",
				fileManagerJson:"<%=basePath%>"+"kindeditor/jsp/file_manager_json.jsp",
				allowFileManager:true,
				afterCreate : function() {
					this.loadPlugin('autoheight');
					this.sync();
				},
				afterBlur:function(){
					this.sync();
				}
			});
		}); --%>
		
		var ue = UE.getEditor('editor',{
			UEDITOR_HOME_URL:"<%=basePath%>ueditor/"
		});
		var regionId="<%=regionId%>";
		var countryId="<%=countryId%>";
		var regionName="<%=regionName%>";
		var countryName="<%=countryName%>";
		var roleName="<%=roleName%>";
		
		function add()
		{
			var html="";
			html+="<tr>";
			html+="<td>File:</td>";
			html+="<td>";
			html+="<input type='file' name='file'></input>";
			html+="<input type='button' value='delete' onclick='del(this);'></input>";
			html+="<td></tr>";
			$("#tb").append(html);
		}
		
		function del(obj)
		{
		    var tr = obj.parentNode.parentNode;
		    //alert(tr.tagName);
		    //获取tr在table中的下标
	        var index = tr.rowIndex;
		    //删除
		    var tb = document.getElementById("tb");
		    tb.deleteRow(index);
		}
	</script>	
	<style type="text/css">
		.kwdn{
			border-bottom: 1px solid #dedede;
			border-top: 1px solid #dedede;
			background-color: #eeeeee;
		}
		.jsih{
			background-color: #eeeeee;
		}
		
	</style>
</head>
<body>
	<div class="parent">   
        <!--主要内容-->
       <!--   <form id="courseform" enctype="multipart/form-data">-->
	        <div class="main">
	        	<p class="create">Create Training Course</p>    <!--Create WeChat graphic-->
	        	<!--消息栏目-->
	     		<div class="messagecolumn" style="margin-top: 40px;">Message Column
	     			<select id="column" name="column">
	     				<option value="Markbusicou">市场业务课程</option>
	     				<option value="Saleskilcou">销售技能课程</option>     				
	     				<option value="Produknowlecour">产品知识课程</option>
	     				<option value="Keywordquery">关键字查询</option>
	     			</select>
	     		</div>
	     		<!--子栏目-->
	     		<div class="subtopic">Sub-Column
						<select id="subcolumn" name="subcolumn">
							 <option value="0">Please Select The Sub-Column</option>
							 <option value="ywjn">业务技能</option>
							 <option value="scjn">市场技能</option>
							 <option value="scywfc">市场业务风采</option>
							 <option value="alfx">案例分享</option>
							 <option value="hydt">行业动态</option>
							  <option value="scddlc">市场督导流程</option>
						</select>
						<select id="subtocolumn" name="subtocolumn">
						 	<option value="0">Please Select The Sub-Column</option>
						</select>
	     		</div>
	     		<!--消息类型-->
	     		<div class="Messagetype">Message Type
	     			<select id="messagetype" name="messagetype">
	     				<option value="1"><s:text name="train.msg.graphic"></s:text></option>
	     				<option value="2"><s:text name="train.msg.articl"></s:text></option>
	     				<option value="3"><s:text name="train.msg.video"></s:text></option>      				
	     			</select>
	     		</div>
	     		<!-- 业务区域,国家 -->
	     		<div class="regioncountry">Region-Country
	     			<select id="region" name="region">
	     				<option value="0">Please Select The Business Center</option>	
	     				<option value="-1">All</option>  				
	     			</select>   			
	     			<select id="country" name="country">
	     				<option value="0">Please Select The Country</option>
	     				<!-- <option value="-1">All</option> -->
	     			</select>
	     		</div>
	     		
	     		<!-- 角色类型 -->
	     		<div class="roleType">Role-Type
	     		<%if(countryId.equals("999")){ %>
	     			 <select id="roleTypeName" name="roleTypeName"  data-options="required:true,editable:false" editable="false">
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
					 <%}else{ %>
					 	 <select id="roleTypeName" name="roleTypeName"  data-options="required:true,editable:false" editable="false">
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
					 <%} %>
	     		</div>
	     			<!--左右互移-->
					<div class="leftToright">
						<div>
							<span style="vertical-align: top;margin-left: 156px;">All User</span>
							<span style="vertical-align: top;margin-left: 166px;">Accepted User</span>
						</div>
						
						<div class="inb select_from" style="margin-left: 154px;">
							<select size='10' multiple id="selectLeft">
							</select>
						</div>
						
						<div class="inb" style="vertical-align: top;padding-top: 12px;">
							<input type="button" value=" &gt; " id="toRight" style="width:28px;"/><br />
							<input type="button" value=" &gt;&gt " id="toRightAll" style="margin-top:10px;width:28px;"/><br />
							<input type="button" value=" &lt; " id="toLeft" style="margin-top:10px;width:28px;"/><br />
							<input type="button" value=" &lt;&lt " id="toLeftAll" style="margin-top:10px;"/>
							
							
						</div>
						<div class="inb select_to">
							<select size='10' multiple id="selectRight">
							</select>
						</div>
					</div>
	     		<div>
	     		<!--标题-->
	     		<p class="Thetitle">Title
	     			<input type="text" name="title" id="title" value="" />
	     		</p>
	     		</div>
	     		<!--封面-->
	     		<div>   
	     		<form id="coverform"  enctype="multipart/form-data">	
		     		<div class="Thecover">	
		     			<label>Cover</label>
						<input type="file" id="coverimg" name="fileUploadTools.uploadFile"/>
						<input type="hidden" id="hidcover"/>
						<input type="button" id="coverupload" value="upload"/>
						<br />
						<label>size:<span>2M</span> max,file forma:<span>gif，jpg，jpeg，png，bmp</span></label>	
		     		</div>   
	     		</form> 
	     		</div> 			
	     		<!--摘要-->
	     		<div class="Inthispaper">
	     			<label>Digest</label>
	     			<!--  <input type="text" name="summary" id="summary" value="" />-->
	     			<textarea rows="2" cols="" id="summary" name="summary" ></textarea>
	     		</div>
  			    <!-- 附件 -->
	     		<!-- <div class="Theattachment">
		     		<form id="attachmentform" enctype="multipart/form-data">		     			
	     				<label>Attachment</label>
	     				<input type="hidden" id="hidattach" name="attachment"/>
	     				<table>
							<tr>
								<td id="fileForm"><br />
									<div id="progressbar" style="width:100%;height: 10px;"></div>
								    <br />
									<div id="progressDetail" class="demo-description" >			      
										
									</div>
									<br/>
									<input type="file" name="fileUploadTools.uploadFile" multiple="multiple">
								</td>
								<td width="30px">
								 <a href="javascript:insertFile()">添加附件</a>
				
									<input type="button" value="upload" id="attachupload"/>
									<input type="button" value="add" onclick="javascript:insertFile()"/> 
								</td>
							</tr>
						</table>	     			
		     		</form>
	     		</div> -->
	     		<!--内容-->
	     		<div class="content">
	     			<label for="">Content</label>
					<!-- <textarea id="content" name="content" style="width:680px;height:200px;"></textarea> -->
					<textarea id="editor" name="content" style="width:680px;height:200px;margin-left: 64px;" ></textarea>
					<%-- <script id="editor" name="content" type="text/plain" style="width:680px;height:200px;margin-left: 64px;"></script> --%>
				</div>

				<!--封面展示-->
				<img src=""  alt="Cover Preview" class="Coverpreview" id="Coverpreview">
				<!--  <input type="submit" name="submit" id="submit" value="Submit" />-->
				<input type="button" name="submit" id="submit" value="Submit"/>
	       	</div> 
       <!-- </form> -->
	</div>
	
</body>

</html>
