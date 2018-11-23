<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>多消息发送</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/training/sendmultimessages.css"/>
	<script type="text/javascript" src="<%=basePath%>js/platform/training/sendmultimessages.js"></script>
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
        <div class="main">
        	<p class="create">Send Multiple Messages</p>
        	<div class="Send-application">
        		<p class="Send-application-p">Select The Application To Send</p>
        		<span>The Application To Send:</span>
        		<select name="">
        			<option value=""></option>
        			<option value="">销司门户</option>
        		</select>
        		<i></i>
        		<span class="hint">this option indicates that you want the following message to be emitted from which wechat enterprise number's application,so 
the corresponding user can receive a message in that application</span>
        	</div>
        	<div class="Message-sender">
        		<p class="Message-sender-p">The Target Party Or Personnel Send To:</p>
        		<div>Click To Select Party Or Personnel</div>
        		<input type="button" name="" id="Select_message" value="Select Message" />
        		<input type="button" name="" id="" value="Send" />
        		<fieldset id="">
        			<input type="checkbox" id="YesORno">
    				<label for="YesORno">Encrypt or not</label>
        		</fieldset>
        		<p class="TopArticle">Current Top Article:<span></span></p>
        	</div>
       	</div> 
       	<div class="Message-selection" id="Messselection">
       		<div>
       			<p>Select Message <i id="Messselection_close"></i></p>
       			<span>Column</span>
       			<select name="">
       				<option value=""></option>
       				<option value="">产品知识课程</option>
       			</select>
       			<span>Keyword</span>
       			<input type="text" name="" id="" value="" />
       			<div class="inb dfghj">
       				<span>Sub-column</span>
	       			<select name="">
	       				<option value=""></option>
	       			</select>
	       			<select name="">
	       				<option value=""></option>
	       			</select>
       			</div>
       			<input type="button" name="" id="Message_selection" value="Search" />
       			<table class="cssraindemo2">
       				<thead>
       					<tr>
       						<th></th>
       						<th>Column</th>
       						<th>Title</th>
       						<th>Author</th>
       						<th>Digest</th>
       						<th>Type</th>
       						<th>Status</th>
       						<th>Create Time</th>
       					</tr>
       				</thead>
       				<tbody id="cssraindemo_ty">
       					<tr>
       						<td><input type="checkbox" name="" value="" /></td>
       						<td>薪资福利管理</td>
       						<td>2017年产假的规定</td>
       						<td></td>
       						<td></td>
       						<td>新闻</td>
       						<td>确认</td>
       						<td>09/09/2017 09:09:09</td>
       					</tr>
       					<tr>
       						<td><input type="checkbox" name="" value="" /></td>
       						<td>经验分享</td>
       						<td>临门一脚，促单议价技巧！</td>
       						<td></td>
       						<td>临门一脚，促单议价技巧！</td>
       						<td>图文</td>
       						<td>保存</td>
       						<td>09/09/2017 09:09:09</td>
       					</tr>
       				</tbody>
       			</table>
       		</div>
       	</div>
	</div>
</body>
</html>
