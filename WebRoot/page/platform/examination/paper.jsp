<%@page import="cn.tcl.platform.party.service.IPartyService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="cn.tcl.common.WebPageUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String partyId=WebPageUtil.getLoginedUser().getPartyId();
	ApplicationContext context=WebApplicationContextUtils.getWebApplicationContext(application);
	IPartyService partyService=(IPartyService)context.getBean("partyService");
	String countryId=partyService.selectPartyById(partyId).getCountryId();
	String language = WebPageUtil.getLanguage();
	String loginUserId = (String) request.getSession().getAttribute("loginUserId");
	String isHQRole = WebPageUtil.isHQRole() + "";
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.sjgl'/></title>
	
	<script>
			var language='<%=language%>';
			var my_login_id = '<%=loginUserId%>';
			var isHQRole = '<%=isHQRole%>';
	</script>
	
	<script src="<%=basePath%>js/platform/examination/common_examination.js" ></script>
	<script src="<%=basePath%>js/platform/examination/paper.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<script src="<%=basePath%>js/xhEditor1.2.2/xheditor-1.2.2.min.js" ></script>
	<script src="<%=basePath%>js/xhEditor1.2.2/xheditor_lang/zh-cn.js" ></script>
	<link rel="stylesheet" href="<%=basePath%>css/examination/examination.css"/>
	<style type="text/css">
	#add_box>tbody>tr>td:nth-child(1),#add_box>tbody>tr>td:nth-child(3){
		min-width:120px;
	}
	.l-btn{
		border:0!important;
	}
	.l-btn-plain{padding:0!important;}
	#QueryButton{
		color: #ffffff;
	    text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
	    background-color: #f7cc8f;
	    background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#EFC942), to(#E0A80B));
	    background-image: -webkit-linear-gradient(top, #EFC942, #E0A80B);
	    background-image: -o-linear-gradient(top, #EFC942, #E0A80B);
	    background-image: linear-gradient(to bottom, #EFC942, #E0A80B);
	    background-image: -moz-linear-gradient(top, #EFC942, #E0A80B);
	    background-repeat: repeat-x;
	    border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
	    border-radius: 4px;
	    border-width: 1px;
	}
	#searchPanel{
		font-size:9pt;
	}
	.datagrid-btable tr{
		height:61px;
	}
	.datagrid-cell{
		height:60px;
	}
	.leftToRightBut {
	    padding-top: 12px;
	}
	.leftToRightBut input[type="button"]{
		width:37px;
	}
	</style>
</head>
<body>

	<div class="easyui-layout" fit="true">
		<div region="north" style="height:58px;">
			<!-- 表头查询条件-->
			<div id="searchPanel" class="easyui-panel" style="background:#f2f6f8;border-bottom:0px;" title="<s:text name='paper.list'/>" data-options="iconCls:'icon-search',collapsible:false">
				
				<span tcl-text="user.list.gridhead.partyName"></span>
				<select id="countryIds" style="border:1px solid #CB9C70;width:154px;height: 23px;"></select>
				<a class="tcl-gridbar-separator"></a>
				<span tcl-text="paper.list.attr.headline"></span>
				<input class="easyui-textbox" style="width: 150px;" id="qryName" style="width:110px">
				<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();" id="searchBt" tcl-text="toolbar.search">&nbsp;</a>
			</div>
			
		</div>
		<div region="center">
			<table id="productListTable" name="gridTable"></table>
		</div>
		
	</div>

	<div id="addProductWin" class="easyui-window tcladdwin" title="<s:text name='paper.list.edit'/>" 
		data-options="width:800,height:540,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addProductForm" method="post" enctype="multipart/form-data">
	            	<input id='isUdpateAd' type="hidden" >
	            	<input id='historyId' type="hidden" name='id'>
	            	<input id='historyEndTime' type="hidden">
	            	<table id="add_box" style="width: 100%;">
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="paper.list.attr.headline"></span>:</td>
		            		<td colspan='3'><input type="text" style="width:555px" id="headline" class="  "  name="headline" /></td>
	            	 	</tr>
	            	 	<tr>
		            		<td><i class="required">*</i><span tcl-text="paper.list.attr.startTime"></span>:</td>
		            		<td><input type="text" style="width:154px" id="sTime"   class="easyui-datebox"   name="sTime" /></td>
	            	 		<td><i class="required">*</i><span tcl-text="paper.list.attr.endTime"></span>:</td>
		            		<td><input type="text" style="width:154px" id="eTime"    class="easyui-datebox"  name="eTime" /></td>
	            	 	</tr>
	            	 	
	            	 	 <%if(countryId.equals("999")){ %>
	            	 	<tr>
	            	 		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
	            			<td><select type="text" style="width:154px;height: 23px" id="countryId" name="countryId"></select></td>
	            	 		<td><i class="required">*</i>RoleType:</td>	            	 		
	            	 		<td>
			     			 <select id="roleTypeName" style="width:200px;height: 23px"	name="roleTypeName"  data-options="required:true,editable:false" editable="false">
				                	<option value=""><s:text name="msg.form.title.roleType.Select" /></option>
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
	            	 	<!-- <tr id="role1">
	            	 				 
					 	</tr> -->
					 	<%}else{ %>	
					 		<tr id="role1">
					 		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
	            			<td><select type="text" style="width:154px;height: 23px" id="countryId" name="countryId"></select></td>
	            	 		
	            	 		<td>RoleType:</td>	            	 		
	            	 		<td>
			     			 <select id="roleTypeName" style="width:155px;height: 23px"	name="roleTypeName"  data-options="required:true,editable:false" editable="false">
				                	<option value=""><s:text name="msg.form.title.roleType.Select" /></option>
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
					 	<tr id="role2">
					 		<td style="vertical-align: top;">All User:</td>
					 		<td colspan="3">
					 			<div class="inb select_from">
									<select class="selectLeftCss" size='10' multiple id="selectLeft">									
									</select>
								</div>
					 			<div class="inb" style="vertical-align: top;padding-top: 12px;">
					 				<input type="button" value=" &gt; " id="toRight" style="width:37px;"/><br />
					 				<input type="button" value=" &gt;&gt; " id="toRightAll"  style="margin-top:10px;width:37px;"/><br />
					 				<input type="button" value=" &lt; " id="toLeft" style="margin-top:10px;width:37px;"/><br />
									<input type="button" value=" &lt;&lt; " id="toLeftAll" style="margin-top:10px;"/>
									
									
								</div>
								<div class="inb select_to">
									<select class="selectRightCss" size='10' multiple id="selectRight">										
									</select>
								</div>
					 		</td>
					 	</tr>
	            	 	
	            		<tr>
	            	 		<td><i class="required">*</i><span tcl-text="paper.list.attr.time"></span>:</td>
		            		<td><input type="text" style="width:154px" id="testTime" class="  "  name="testTime" /></td>
		            		<td><i class="required">*</i><span tcl-text="topic.alert.topic.one"></span>:</td>
		            		<td><select style="width:154px;height: 23px" id="categoriesId"  name="categories"></select></td>
		            	</tr> 
		            	
	            		<tr>
		            		<td><span tcl-text="topic.alert.topic.two"></span>:</td>
							<td><select style="width:154px;height: 23px" id="mediumss" name="mediumsId"></select></td>
							<td><span tcl-text="topic.alert.topic.three"></span>:</td>
							<td><select style="width:154px;height: 23px" id="smaClasss" name="smaClassId"></select></td>
	            		</tr>
	            		<tr>
		            		<td><span tcl-text="topic.list.attr.topic"></span>:</td>
							<td><button type="button" id="QueryButton" tcl-text="toolbar.search"></button></td>
	            		</tr>
	            	 	
	            	 	<!-- 判断题 -->
	            		<tr>
		            		<td style="vertical-align: top;"><span tcl-text="paper.list.attr.judgement"></span>:</td>
		            		<!-- <td><input type="text" style="width:154px" id="judNum" class="  "  name="judNum" /></td> -->
		            		<td colspan="3">
					 			<div class="inb select_from">
									<select class="selectLeftCss" size='10' multiple id="selectLeft1">	
									</select>
								</div>
					 			<div class="inb leftToRightBut">
					 				<input type="button" value=" &gt; " id="toRight1"/><br />
					 				<input type="button" value=" &gt;&gt " id="toRight1All"  style="margin-top:10px;"/><br />
					 				<input type="button" value=" &lt; " id="toLeft1" style="margin-top:10px;"/><br />
									<input type="button" value=" &lt;&lt " id="toLeft1All" style="margin-top:10px;"/>
									
									
								</div>
								<div class="inb select_to">
									<select class="selectRightCss" size='10' multiple id="selectRight1">										
									</select>
								</div>
					 		</td>
	            	 	</tr>
	            	 	<!-- 单选题 -->
	            	 	<tr>
	            	 		<td style="vertical-align: top;"><span tcl-text="paper.list.attr.exChoice"></span>:</td>
		            		<td colspan="3">
					 			<div class="inb select_from">
									<select class="selectLeftCss" size='10' multiple id="selectLeft2">	
									</select>
								</div>
					 			<div class="inb leftToRightBut">
					 				<input type="button" value=" &gt; " id="toRight2"/><br />
					 				<input type="button" value=" &gt;&gt " id="toRight2All"  style="margin-top:10px;"/><br />
					 				<input type="button" value=" &lt; " id="toLeft2" style="margin-top:10px;"/><br />
									<input type="button" value=" &lt;&lt " id="toLeft2All" style="margin-top:10px;"/>
									
									
								</div>
								<div class="inb select_to">
									<select class="selectRightCss" size='10' multiple id="selectRight2">										
									</select>
								</div>
					 		</td>
	            	 	</tr>
	            	 	<!-- 多选题 -->
	            	 	<tr>
		            		<td style="vertical-align: top;"><span tcl-text="paper.list.attr.mulChoice"></span>:</td>
		            		<!-- <td><input type="text" style="width:154px" id="muiUum" class="  "  name="muiUum" /></td> -->
		            		<td colspan="3">
					 			<div class="inb select_from">
									<select class="selectLeftCss" size='10' multiple id="selectLeft3">
									</select>
								</div>
					 			<div class="inb leftToRightBut">
					 				<input type="button" value=" &gt; " id="toRight3" /><br />
					 				<input type="button" value=" &gt;&gt " id="toRight3All" style="margin-top:10px;"/><br />
					 				<input type="button" value=" &lt; " id="toLeft3" style="margin-top:10px;"/><br />
									<input type="button" value=" &lt;&lt " id="toLeft3All" style="margin-top:10px;"/>
									
									
								</div>
								<div class="inb select_to">
									<select class="selectRightCss" size='10' multiple id="selectRight3">										
									</select>
								</div>
					 		</td>
	            	 	</tr>
	            	</table>
	            	<input type="hidden" id='updateDate' name='currentTime'></input>
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="submitBtn" name='randomSub' class="btn btn-warning btn-sm" onclick="verifyBrHead()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearForm()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
		<div id="load-layout" style="position:fixed;width:100%;height:100%;top:0px;
				left:0px;opacity:0.4;background:#000;display:none;">
			<div align="center" style="position:absolute;left:49%;top:48%;width:31px;height:31px;">
				<img id="loadGif" src="">
			</div>
		</div>
	</div>
	
	<!-- 考卷手动增加列表 -->
	<div id="manualAddProductWin" class="easyui-window tcladdwin" title="<s:text name='paper.list.edit'/>" 
		data-options="width:800,height:540,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addManualProductForm" method="post" enctype="multipart/form-data">
	            	<input id='manualAddProductWinisUdpateAd' type="hidden" >
	            	<input id='historyId' type="hidden" name='id'>
	            	<table id="add_box" style="width: 100%;">
	            		
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="topic.alert.topic.one"></span>:</td>
		            		<td><select style="width:154px;height: 23px" id="mcategoriesId"  name="categories"></select></td>
		            	</tr> 
		            	
	            		<tr>
		            		<td><span tcl-text="topic.alert.topic.two"></span>:</td>
							<td><select style="width:154px;height: 23px" id="mmediumss" name="mediumsId"></select></td>
							<td><span tcl-text="topic.alert.topic.three"></span>:</td>
							<td><select style="width:154px;height: 23px" id="msmaClasss" name="smaClassId"></select></td>
	            		</tr>
	            		
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="paper.list.attr.headline"></span>:</td>
		            		<td colspan='3'><input type="text" style="width:555px" id="mheadline" class="  "  name="headline" /></td>
	            	 	</tr>
	            	 	
	            		<tr>
		            		<td><span tcl-text="paper.list.attr.judgement"></span>:</td>
		            		<td><input type="text" style="width:154px" id="mjudNum" class="  "  name="judNum" /></td>
	            	 		<td><span tcl-text="paper.list.attr.exChoice"></span>:</td>
		            		<td><input type="text" style="width:154px" id="msinNum" class="  "  name="sinNum" /></td>
	            	 	</tr>
	            	 	<tr>
		            		<td><span tcl-text="paper.list.attr.mulChoice"></span>:</td>
		            		<td><input type="text" style="width:154px" id="mmuiUum" class="  "  name="muiUum" /></td>
	            	 		<td><i class="required">*</i><span tcl-text="paper.list.attr.time"></span>:</td>
		            		<td><input type="text" style="width:154px" id="mtestTime" class="  "  name="testTime" /></td>
	            	 	</tr>
	            	 	<tr>
		            		<td><i class="required">*</i><span tcl-text="paper.list.attr.startTime"></span>:</td>
		            		<td><input type="text" style="width:154px" id="msTime"  class="easyui-datebox"  name="sTime" /></td>
	            	 		<td><i class="required">*</i><span tcl-text="paper.list.attr.endTime"></span>:</td>
		            		<td><input type="text" style="width:154px" id="meTime"   class="easyui-datebox"  name="eTime" /></td>
	            	 	</tr>
	            	 <%if(countryId.equals("999")){ %>
	            	 	<tr>
	            	 		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
	            			<td><select type="text" style="width:154px;height: 23px" id="mcountryId" name="countryId"></select></td>
	            	 		<td><i class="required">*</i>RoleType:</td>	            	 		
	            	 		<td>
			     			 <select id="mroleTypeName" style="width:200px;height: 23px"	name="roleTypeName"  data-options="required:true,editable:false" editable="false">
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
					 		<tr id="role1">
					 		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
	            			<td><select  style="width:154px;height: 23px" id="mcountryId" name="countryId"></select></td>
	            	 		<td>RoleType:</td>	            	 		
	            	 		<td>
			     			 <select id="mroleTypeName" style="width:155px;height: 23px"	name="roleTypeName"  data-options="required:true,editable:false" editable="false">
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
					 	<tr id="role2">
					 		<td style="vertical-align: top;">All User:</td>
					 		<td colspan="3">
					 			<div class="inb select_from">
									<select class='selectLeftCss' size='10' multiple id="mselectLeft">									
									</select>
								</div>
					 			<div class="inb" style="vertical-align: top;padding-top: 12px;">
					 				<input type="button" value=" &gt; " id="mtoRight" style="width:37px;"/><br />
					 				<input type="button" value=" &gt;&gt " id="mtoRightAll" style="margin-top:10px;width:37px;"/><br />
					 				<input type="button" value=" &lt; " id="mtoLeft" style="margin-top:10px;width:37px;"/><br />
									<input type="button" value=" &lt;&lt " id="mtoLeftAll" style="margin-top:10px;"/>
									
									
								</div>
								<div class="inb select_to">
									<select class='selectRightCss' size='10' multiple id="mselectRight">										
									</select>
								</div>
					 		</td>
					 	</tr>
	            	</table>
	            	<input type="hidden" id='updateDate' name='currentTime'></input>
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="submitBtn" name='manualSub' class="btn btn-warning btn-sm" onclick="verifyBrHeadm()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearFormm()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
		<div id="load-layout" style="position:fixed;width:100%;height:100%;top:0px;
				left:0px;opacity:0.4;background:#000;display:none;">
			<div align="center" style="position:absolute;left:49%;top:48%;width:31px;height:31px;">
				<img id="loadGif" src="">
			</div>
		</div>
	</div>
	
	
	
	<div id="dlg">
		<img  src="" id="simg">
	</div>
	<div id="xhEditorView" class="easyui-window tcladdwin" 
		data-options="width:800,height:500,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;padding-top:0;">
	            <form class="preview" id="xhEditorFrom" method="post" enctype="multipart/form-data">
		           <p class="preview_title">题目</p>
		           <div id = "preveiew_vive">
		           		<div  style="display: none ">
		           			<span id = 'sin_nub' style="font-size:16px;"></span>
		           			<p tcl-text="paper.list.attr.exChoicea"></p><span>：</span>
		           			<div id="sin"></div>
		           		</div>
		           		<div style="display: none ">
		           			<span id = 'min_nub' style="font-size:16px;"></span>
		           			<p tcl-text="paper.list.attr.mulChoicea"></p><span>：</span>
		           			<div id="min"></div>
		           		</div> 
		           		<div style="display: none ">
		           			<span id = 'jud_nub' style="font-size:16px;"></span>
		           			<p tcl-text="paper.list.attr.judgementa"></p><span>：</span>
		           			<div id="jud"></div>
		           		</div>
		           </div>
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearFormView()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
	</div>
	
	
	<div id="xhEditorView_code" class="easyui-window tcladdwin" 
		data-options="width:500,height:300,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;padding-top:0;">
	            <form class="preview"  method="post" enctype="multipart/form-data">
		           <p class="preview_title_code">题目</p>
		           <div style="height:150px;">
		           		<div>
		           			<div id="url" style="padding:10px;">
		           			</div>
		           		</div>
		           </div>
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearFormView_code()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
	</div>
	
	<div id='loadingImport' style='width:100px; height:100px;display: none;position: fixed;top: 50vh;left: 50vw;margin-top: -50px; margin-left: -50px;
					z-index: 9999999999999999999999999999;'>
		<table border='0'> 
			<tr>
				<td><img src='<%=basePath%>images/statement/import.gif' style='width: 100px;height: 100px;z-index: 10000;'/></td>
			</tr> 	 
		
		</table> 
	</div>
</body>
</html>