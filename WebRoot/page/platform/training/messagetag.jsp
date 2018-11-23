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
	<title>消息标签</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/training/messagetag.css"/>
	<script type="text/javascript" src="<%=basePath%>js/platform/training/messagetag.js"></script>
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
        	<p class="create">Message Tags</p>
        	<!--查询条件-->
        	<div class="Query-Condition">
        		<input type="button" name="" id="" value="Create Tag" />
        	</div>
        	<!--查询结果-->
        	<table class="Query-Result">        		
        		<thead>
        			<tr>
        				<th>Tag Name</th>
        				<th>Remark</th>
        				<th>Creater</th>
        				<th>Operation</th>
        			</tr>
        		</thead>
        		<tbody id="Query_Result">
        			<tr>
        				<td>销司全员</td>
        				<td></td>
        				<td>***</td>
        				<td>
        					<a href="#">Add Personnel</a>
        					<a href="#">View Personnel</a>
        					<a href="#">Edit</a>
        					<a href="#">Delete</a>
        				</td>
        			</tr>
        			<tr>
        				<td>销司全员</td>
        				<td></td>
        				<td>***</td>
        				<td>
        					<a href="#">Add Personnel</a>
        					<a href="#">View Personnel</a>
        					<a href="#">Edit</a>
        					<a href="#">Delete</a>
        				</td>
        			</tr>
        			<tr>
        				<td>销司全员</td>
        				<td></td>
        				<td>***</td>
        				<td>
        					<a href="#">Add Personnel</a>
        					<a href="#">View Personnel</a>
        					<a href="#">Edit</a>
        					<a href="#">Delete</a>
        				</td>
        			</tr>
        			<tr>
        				<td>销司全员</td>
        				<td></td>
        				<td>***</td>
        				<td>
        					<a href="#">Add Personnel</a>
        					<a href="#">View Personnel</a>
        					<a href="#">Edit</a>
        					<a href="#">Delete</a>
        				</td>
        			</tr>
        		</tbody>
        	</table>
       	</div>        
	</div>
</body>
</html>
