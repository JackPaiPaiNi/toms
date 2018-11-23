<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="cn.tcl.common.WebPageUtil"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String partyName = WebPageUtil.getLoginedUser().getPartyName();
	String partyId = WebPageUtil.getLoginedUser().getPartyId();
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.ktgl'/></title>
	
	<script>
		var partyName = '<%=partyName%>';
		var partyId = '<%=partyId%>';
		//alert(partyName + partyId);
	</script>
	
	<script src="<%=basePath%>js/platform/examination/common_examination.js" ></script>
	<script src="<%=basePath%>js/platform/examination/topic.js" ></script>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<script src="<%=basePath%>js/xhEditor1.2.2/xheditor-1.2.2.min.js" ></script>
	<script src="<%=basePath%>js/xhEditor1.2.2/xheditor_lang/zh-cn.js" ></script>
	<style type="text/css">
		#addProductForm>div{
			margin: 0 34px;
			margin-top:10px;
		}
		#addProductForm>p{
			color: red;
			font-size:12px;
			margin-top: 10px;
			margin-bottom: 2px!important;
			text-indent: 74px;
		}
		#addProductForm>div>input[type='text']{
			width: -webkit-calc(100% - 140px);
			width: -moz-calc(100% - 140px);
			width: calc(100% - 140px);
		}
		#addProductForm>i.add_title{
			display: inline-block;
			width: 20px;
   	 		height: 20px;
			background: url(<%=basePath%>images/examination/zengjia.png) no-repeat center center;
			background-size: 18px 18px;
   			 cursor: pointer;
   			 float: right;
			 margin: 4px 110px 0px 0px;
		}
		#addProductForm>i.delete_title{
			display: inline-block;
			width: 20px;
   	 		height: 20px;
			background: url(<%=basePath%>images/examination/delete_title.png) no-repeat center center;
			background-size: 18px 18px;
   			 cursor: pointer;
   			 float: right;
			 margin: 4px 10px 0px 0px;
			 opacity: .5;
		}
		.type_qq>input{
			width: 144px!important;
		}
		.type_qq>label{
			width: 100px;
		}
		#add_tab>tbody>tr>td:nth-child(1){
			width:95px;
		}
		#add_tab>tbody>tr>td:nth-child(2){
			width:330px;
		}
		#add_tab>tbody>tr>td:nth-child(3){
			width:95px;
		}
		.alin{
			display: inline-block;
		    width: 46px;
		    text-align: right;
		}
		#productListTable td[field="exQuestions"]>div{
			width:200px;
		}
	</style>
</head>
<body>
	<table id="productListTable" name="gridTable"></table>
	
	<div id="producttb" style="padding:2px 5px;">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deletes();" tcl-text="toolbar.delete"></a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importProducts();"></a>
		
		<%if(WebPageUtil.isHQRole()){%>
	   		<a href="<%=basePath%>download/QuestionBanks.xlsx" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
		<%}else{%>
			<a href="<%=basePath%>download/QuestionBank.xlsx" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
		<%}%>
		
		<a class="tcl-gridbar-separator"></a>
		<span tcl-text="topic.list.attr.type"></span>
		<!-- <input class="easyui-textbox" id="qryName" style="width:110px"> -->
		<select id='qryName' name='cType' style="width: 154px;height: 23px;">
			<option id=''></option>
			<option  value='1' tcl-text="topic.list.attr.single"></option>
			<option  value='2' tcl-text="topic.list.attr.multiple"></option>
			<option  value='3' tcl-text="topic.list.attr.judge"></option>
		</select>
		
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();" id="searchBt" tcl-text="toolbar.search">&nbsp;</a>
        
	</div>
	<div id="addProductWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cpwh.add'/>" 
		data-options="width:800,height:500,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addProductForm" method="post" enctype="multipart/form-data">
	            	<input id='isUdpateAd' type="hidden" >
	            	<input id='historyId' type="hidden" name='id'>
	            	<input id='corAnswer' type="hidden" name='corAnswer'>
	            	<table id="add_tab" style="width: 100%;">
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="topic.list.attr.type"></span>:</td>
		            		<td><select id='cType' name='cType' style="width: 154px;height: 23px;">
		            				<option id=''></option>
		            				<option  value='1' tcl-text="topic.list.attr.single"></option>
		            				<option  value='2' tcl-text="topic.list.attr.multiple"></option>
		            				<option  value='3' tcl-text="topic.list.attr.judge"></option>
		            			</select>
		            		</td>
							<td><i class="required">*</i><span tcl-text="topic.list.attr.score"></span>:</td>
							<td><input type="text" style="width:154px" id="fractions"   name="fractions"/></td>
	            		</tr>
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="topic.alert.topic.one"></span>:</td>
		            		<td><select style="width:154px;height: 23px" id="categories"  name="categories"></select></td>
		            		<td><i class="required">*</i><span tcl-text="district.countryName"></span>:</td>
<!-- 							<td><input type="text" style="width:154px" id="countryId" name="countryId" /></td> -->
	            			<td><select type="text" style="width:154px;height: 23px" id="countryId" name="countryId"></select></td>
		            	</tr> 
	            		<tr>
		            		<td><span tcl-text="topic.alert.topic.two"></span>:</td>
							<td><select style="width:154px;height: 23px" id="mediumss" name="mediumss"></select></td>
							<td><span tcl-text="topic.alert.topic.three"></span>:</td>
							<td><select type="text" style="width:154px;height: 23px" id="smaClasss" name="smaClasss"></select></td>
	            		</tr>
	            	</table>
	            	
	            	<div><i class="required" style="padding: 0 4px 0 13px;">*</i>
	            		<span tcl-text="topic.list.attr.topic" style="display: inline-block;width: 34px;text-align: right;">:</span>
	            		<input type="text" id='exQuestions' name='exQuestions' style="width: -webkit-calc(100% - 120px);width: -moz-calc(100% - 120px);width: calc(100% - 120px);"/>
	            	</div>
	            	<p tcl-text="topic.list.attr.commentaries" style="display: inline-block;"></p><input onclick='checkBoxChecked();' id='checkedInput' type="checkbox" checked readonly="readonly" style="margin-left: 10px;margin-top: 0px;vertical-align: middle;"/>
	            	<div name='input' style="margin-top: 0px;">
	            	  <i class="required">*</i>
	            		<span class="alin" tcl-text="topic.list.attr.optionsA">:</span>
	            		<input type="checkbox" value='A' style="margin: 0;vertical-align: middle;"/>
	            		<input type="text" id='alAnswersA' name='alAnswersA'/>
	            	</div>
	            	<div name='input'>
	            		<i class="required">*</i>
	            		<span class="alin" tcl-text="topic.list.attr.optionsB">:</span>
	            		<input type="checkbox" value='B' style="margin: 0;vertical-align: middle;"/>
	            		<input type="text" id='alAnswersB' name='alAnswersB' />
	            	</div>
	            	<div name='input' id='noneInput' style="display: none;">
	            		<i class="required">*</i>
	            		<span class="alin" tcl-text="topic.list.attr.optionsC">:</span>
	            		<input type="checkbox" value='C' style="margin: 0;vertical-align: middle;"/>
	            		<input type="text" id='alAnswersC' name='alAnswersC'/>
	            	</div>
	            	<div name='input' id='noneInput' style="display: none;">
	            		<i class="required">*</i>
	            		<span class="alin" tcl-text="topic.list.attr.optionsD">:</span>
	            		<input type="checkbox" value='D' style="margin: 0;vertical-align: middle;"/>
	            		<input type="text" id='alAnswersD' name='alAnswersD'/>
	            	</div>
	            	<div name='input' id='noneInput' style="display: none;">
	            		<i class="required">*</i>
	            		<span class="alin" tcl-text="topic.list.attr.optionsE">:</span>
	            		<input type="checkbox" value='E' style="margin: 0;vertical-align: middle;"/>
	            		<input type="text" id='alAnswersE' name='alAnswersE'/>
	            	</div>
	            	<div name='input' id='noneInput' style="display: none;">
	            		<i class="required">*</i>
	            		<span class="alin" tcl-text="topic.list.attr.optionsF">:</span>
	            		<input type="checkbox" value='F' style="margin: 0;vertical-align: middle;"/>
	            		<input type="text" id='alAnswersF' name='alAnswersF'/>
	            	</div>
	            	<div name='input' id='noneInput' style="display: none;">
	            		<i class="required">*</i>
	            		<span class="alin" tcl-text="topic.list.attr.optionsG">:</span>
	            		<input type="checkbox" value='G' style="margin: 0;vertical-align: middle;"/>
	            		<input type="text" id='alAnswersG' name='alAnswersG'/>
	            	</div>
	            	
	            	<i class="add_title" onclick='discernType();'></i>
	            	<i class="delete_title" onclick='hiddenAnswers();'></i> 
	            	<div  style="display: ;margin-top: 50px;">
	            		<span tcl-text="paper.list.attr.analysis" style="display: inline-block;width: 58px;text-align: right;"></span>
	            		<textarea  id='analysis' name='analysis' style="margin: 0;vertical-align: middle;resize: none;width: -webkit-calc(100% - 120px);width: -moz-calc(100% - 120px);width: calc(100% - 120px);height: 6pc;overflow-x: hidden;"></textarea>
	            	</div>
	            	
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="submitBtn" class="btn btn-warning btn-sm" onclick="submitForm()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
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
	
	<div id="xhEditorView" class="easyui-window tcladdwin" 
		data-options="width:800,height:300,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="xhEditorFrom" method="post" enctype="multipart/form-data">
		            <div  style="padding: 0px 30px;" >
		            	<p style="font-size: 18px;color: blue;hight:100%;" tcl-text="topic.list.attr.correctness"></p>
		            		<div id='correctAnswer'></div>
		            </div>
		            <div style="padding: 0px 30px;">
		            	<p style="font-size: 18px;color: red;hight:100%;" tcl-text="topic.list.attr.error"></p>
		            		<div id='wrongAnswer' ></div>
		            </div>
		            <p tcl-text="paper.list.attr.analysis" style="font-size: 18px;color: #2a2a2a;hight:100%;padding-left:30px;"></p>
		            <div id='analysisView' style="padding: 6px;margin: 0 auto;word-wrap: break-word!important;white-space: inherit!important;word-break: break-all!important;padding-left:52px;padding-right: 30px;">
		            </div>
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearFormView()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
	</div>
</body>
</html>