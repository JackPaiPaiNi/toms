<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.cpwh'/></title>
	<script src="<%=basePath%>js/easyui1.4/jquery.form.3.26.0.js" ></script>
	<script src="<%=basePath%>js/xhEditor1.2.2/xheditor-1.2.2.min.js" ></script>
	<script src="<%=basePath%>js/xhEditor1.2.2/xheditor_lang/zh-cn.js" ></script>
	<script src="<%=basePath%>js/platform/product/list.js" ></script>
	<style type="text/css">
		.tcladdwin form table tr td, .wintable tr td {
		    padding: 2px 0;
		}
	</style>
</head>
<body>
	<table id="productListTable" name="gridTable"></table>
	<div id="producttb" style="padding:2px 5px;">
	    <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	    <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="importProducts();"><!--  tcl-text="toolbar.import"> --></a>
	    <a href="<%=basePath%>download/TVProductsInfo.xlsx" class="easyui-linkbutton" iconCls="icon-arrow-down" plain="true" tcl-text="toolbar.downtemplate"></a>
		<a class="tcl-gridbar-separator"></a>
		<span tcl-text="product.search.proCatepory"></span>
      <select id = "sonId" sel = "1" style="border:1px solid #EE9A49;width:100px;height:22px;">   
          <option value='###'></option>   
      </select> 
      <select id="catId" sel='2' style="border:1px solid #EE9A49;width:100px;height:22px;">   
          <option value='###'></option>   
      </select> 
		<span tcl-text="product.search.name"></span>
		<input class="easyui-textbox" id="qryName" style="width:110px">
		
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch();" id="searchBt" tcl-text="toolbar.search">&nbsp;</a>
        
	</div>
	<div id="addProductWin" class="easyui-window tcladdwin" title="<s:text name='permission.labelkey.cpwh.add'/>" 
		data-options="width:800,height:500,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true">
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addProductForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" id = "editId" name="editId"/>
	            	<input type="hidden" id = "editProductId" name="editProductId"/>
	            	<table style="width: 100%;">
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="product.list.th.productId"></span>:</td>
		            		<td><input type="text" style="width:154px" id="modelName" class="easyui-textbox easyui-validatebox"  name="modelName" data-options="required:true"/></td>
							<td><i class="required">*</i><span tcl-text="product.list.th.productType"></span>:</td>
							<td>
								<select id = "sonType" name = "productTypeId" style="border:1px solid #EE9A49;width:153px;height:23px">	
	            				</select>
	            			</td>
	            	 	</tr>
	            	 	
	            	 	<tr>
	            	 		<td><span tcl-text="product.list.th.modelName"></span>:</td>
		            		<td id = 'tt'><input type="text" style="width:154px"  class="easyui-textbox easyui-validatebox"   name="productId"/></td>
	            	 	    <td><i class="required">*</i><span tcl-text="product.list.th.attr"></span>:</td>
	            			<td rowspan="4">
	            				<div style="border:1px solid #EE9A49;width:153px;height:140px" border="1" rules="rows">
							     	<table>
							     		<tr id ='tr1'></tr>
							     		<tr id ='tr2'></tr>
							     		<tr id ='tr3'></tr>
							     		<tr id ='tr4'></tr>
							     		<tr id ='tr5'></tr>
							     	</table>
						     	</div>
						 	</td>
	            	 	</tr>		
	            		
	            		<tr>
		            		<td><i class="required">*</i><span tcl-text="product.list.th.size"></span>:</td>
<!-- 		            		<td><input type="text" style="width:154px" id="size" name="size" editable="false" data-options="required:true,editable:false"/></td> -->
		            			<td><input type="text" style="width:154px" id="size" name="size" data-options="required:true,editable:false"/></td>
	            		</tr>
	            		
	            		<tr>
	            			<td><span tcl-text="product.list.th.display"></span>:</td>
		            		<td><input type="text" style="width:154px" id="display" name="display" editable="false" data-options="editable:false"/></td>
		            		
		            	</tr>
		            	
		            	<tr>
		            		<td><span tcl-text="product.list.th.ratio"></span>:</td>
		            		<td><input type="text" style="width:154px" id="ratio" name="ratio" editable="false" /></td>
		            	</tr>
		            	
	            		<tr>
		            		<td> <i class="required">*</i> <span tcl-text="product.list.th.os"></span>:</td>
		            		<td><input type="text" style="width:154px" id="os" name="os"  data-options="required:true,editable:false"/></td>
	            			
	            			<!-- 产品系列  -->
	            			 <td><span tcl-text="product.list.th.line"></span>:</td>
	            		<%-- 	<td>
								<select id = "productCatena" name = "productCatena" style="border:1px solid #EE9A49;width:153px;height:23px">	
	            				</select>
	            				</td> --%>
	            				<td><input type="text" style="width:154px" id="productCatena"  name="productCatena"/></td>
	
	            			  
	            		</tr>
	            		
	            		<!-- 上市时间  -->
	            		<%-- <tr>
	            			<td align="right"><span tcl-text="product.list.th.introductionDate"></span>:</td>
					        <td><input type="text" class="easyui-datebox" id="introduction" name="intDate" value="111"></input></td>
		            	</tr> --%>
	            		
	            		<tr>
		            		<td><span tcl-text="product.list.th.comments"></span>:</td>
		            		<td><input type="text" style="width:300px" id="comments" class="easyui-textbox easyui-validatebox"   name="comments"/></td>
		            	</tr> 
		            	
	            		<tr id="editorControlName">
	            			<td><span tcl-text="product.list.th.description"></span>:</td>
	            		</tr>
	            		
	            		<tr id="editorControl">
	            			<td colspan="4">
	            				<textarea id="description" name="description" class="xheditor" style="width: 100%"></textarea>
	            			</td>
	            		</tr>
	            		
	            	</table>
	            	<input id = 'catType' name="productSpecId" type="hidden">
	            	<input id = 'isUdpateAd' type="hidden">
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
	            	<table style="width: 100%;">
	            		<tr>
	            			<td>
	            				<textarea id="descriptionView" name="descriptionView" class="xheditor" style="width: 100%"></textarea>
	            			</td>
	            		</tr>
	            	</table>
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearFormView()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>
	        </div>
	    </div>
	</div>
</body>
</html>