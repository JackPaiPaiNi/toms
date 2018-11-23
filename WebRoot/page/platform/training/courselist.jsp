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
	<title>查询图文列表</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/training/courselist.css"/>
	<script type="text/javascript" src="<%=basePath%>js/platform/training/courselist.js"></script>
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
        	<p class="create">Select Course List</p>    <!--Create WeChat graphic-->
        	<!--查询条件-->
        	<div class="Query-Condition">
        		<span>Column</span>
        		<select id="column" name="column">
        			<option value="Markbusicou">市场业务课程</option>
     				<option value="Saleskilcou">销售技能课程</option>     				
     				<option value="Produknowlecour">产品知识课程</option>
     				<option value="Keywordquery">关键字查询</option>
        		</select>
        		<span>Keyword</span>
        		<input type="text" name="keyword" id="keyword" value="" />
        		<div class="inb">
        			<span>Sub-column</span>
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
        		<input type="button" name="" id="Query_Condition" value="Search" />
        	</div>
        	<!--查询结果-->
        	<p class="caption">Search Result<span>（<i id="caption_i"></i> Records）</span></p>
        	<table class="Query-Result">        		
        		<thead>
        			<tr>
        				<th>Column</th>
        				<th>Title</th>
        				<th>Author</th>
        				<th>Digest</th>
        				<th>Type</th>
        				<th>Status</th>
        				<th>Create Time</th>
        				<th style="width: 50px;">Edit</th>
        				<th style="width: 50px;">Delete</th>
        			</tr>
        		</thead>
        		<tbody id="Query_Result">
        			<tr>
        				<td>总部统一，销售政策</td>
        				<td>《百问百答》，问答整理</td>
        				<td>***</td>
        				<td>《百问百答》问答整理</td>
        				<td>图文</td>
        				<td>保存</td>
        				<td>09/09/2016 09:09:09</td>
        				<td>修改</td>
        				<td class="remove_tr">删除</td>
        			</tr>
        			<tr>
        				<td>总部统一，销售政策</td>
        				<td>《百问百答》，问答整理</td>
        				<td>***</td>
        				<td>《百问百答》问答整理</td>
        				<td>图文</td>
        				<td>保存</td>
        				<td>09/09/2016 09:09:09</td>
        				<td>修改</td>
        				<td  class="remove_tr">删除</td>
        			</tr>
        			<tr>
        				<td>总部统一，销售政策</td>
        				<td>《百问百答》，问答整理</td>
        				<td>***</td>
        				<td>《百问百答》问答整理</td>
        				<td>图文</td>
        				<td>保存</td>
        				<td>09/09/2016 09:09:09</td>
        				<td>修改</td>
        				<td  class="remove_tr">删除</td>
        			</tr>
        			<tr>
        				<td>总部统一，销售政策</td>
        				<td>《百问百答》，问答整理</td>
        				<td>***</td>
        				<td>《百问百答》问答整理</td>
        				<td>图文</td>
        				<td>保存</td>
        				<td>09/09/2016 09:09:09</td>
        				<td>Edit</td>
        				<td  class="remove_tr">Delete</td>
        			</tr>
        		</tbody>
        	</table>
       	</div>        
	</div>
</body>
</html>
