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
	<title>统计汇总</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/training/statisticssummary.css"/>
	<script type="text/javascript" src="<%=basePath%>js/platform/training/statisticssummary.js"></script>
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
        	<p class="create">Statistics Summary</p>
        	<!--结果-->
        	<table class="Query-Result">        		
        		<thead>
        			<tr>
        				<th>Statistics Type</th>
        				<th>Description</th>
        				<th>Operation</th>
        			</tr>
        		</thead>
        		<tbody id="Query_Result">
        			<tr>
        				<td>Integral Summary</td>
        				<td></td>
        				<td>View</td>
        			</tr>
        			<tr>
        				<td>Summary Of Total Reading</td>
        				<td></td>
        				<td>View</td>
        			</tr>
        			<tr>
        				<td>Summary Of Daily Reading</td>
        				<td></td>
        				<td>View</td>
        			</tr>
        			<tr>
        				<td>Summary Of Weekly Reading</td>
        				<td></td>
        				<td>View</td>
        			</tr>
        			<tr>
        				<td>Summary Of Examination Results</td>
        				<td></td>
        				<td>View</td>
        			</tr>
        		</tbody>
        	</table>
       	</div>        
	</div>
</body>
</html>
