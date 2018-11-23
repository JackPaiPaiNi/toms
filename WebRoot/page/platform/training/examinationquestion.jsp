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
	<title>考题管理</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/training/examinationquestion.css"/>
	<script type="text/javascript" src="<%=basePath%>js/platform/training/examinationquestion.js"></script>
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
        	<p class="create">Examination Questions Management</p>
        	<!--查询条件-->
        	<div class="Query-Condition">
        		<span>Product Type</span>
        		<select name="" id="Query_select">
        			<option value=""></option>
        			<option value="1">H9700</option>        			
        		</select>
        		<input type="button" name="" id="" value="Download The Test To Import The Excel Template" />
        	</div>
        	<!--查询结果-->
        	<p class="caption">Search Result <span>（<i id="caption_i"></i>Records）</span></p>
        	<table class="Query-Result">        		
        		<thead>
        			<tr>
        				<th>Product Name</th>
        				<th>Operation</th>
        				<th>Import</th>
        			</tr>
        		</thead>
        		<tbody id="Query_Result" style="display: none;">
        			<tr>
        				<td>H9700</td>
        				<td>编辑考题</td>
        				<td>导入考题</td>
        			</tr>
        			<tr>
        				<td>H9700</td>
        				<td>编辑考题</td>
        				<td>导入考题</td>
        			</tr>
        			<tr>
        				<td>H9700</td>
        				<td>编辑考题</td>
        				<td>导入考题</td>
        			</tr>
        			<tr>
        				<td>H9700</td>
        				<td>编辑考题</td>
        				<td>导入考题</td>
        			</tr>
        		</tbody>
        	</table>
       	</div>        
	</div>
</body>
</html>
