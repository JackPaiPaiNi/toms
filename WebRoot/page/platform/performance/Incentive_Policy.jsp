<%@page import="cn.tcl.common.WebPageUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String party = (String) request.getSession().getAttribute("loginUserId");
	String roleId =WebPageUtil.getLoginedUser().getRoleId();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><s:text name='permission.labelkey.inpo'/></title>
	<script>var my_login_id='<%=party%>';var roleId="<%=roleId%>";</script>
	<script src="<%=basePath%>js/platform/performance/Incentive_Policy.js" ></script>
	<script src="<%=basePath%>js/common.easyui.js" ></script>
		 <link rel="stylesheet" type="text/css" media="all" href="<%=basePath%>css/daterangepicker-bs3.css"/>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/moment.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/daterangepicker.js"></script>
      <script type="text/javascript" src="<%=basePath%>js/platform/statement/WdatePicker.js"></script>
	 <style type="text/css">
	 	.datagrid-wrap.panel-body{
	 		height:-moz-calc(100vh - 95px)!important;
	 		height:-webkit-calc(100vh - 95px)!important;
	 		height:calc(100vh - 95px)!important;
	 	}
	 	#customId{
	 		overflow: hidden;
	 	}
	 	.panel.datagrid.easyui-fluid{
	 		height:100%;
	 	}
	 </style>
</head>

<body>

	<div class="easyui-tabs" style="width:100%;height:100%;">
		<div title='<s:text name="policy.list.incentive"></s:text>' style="padding:2px 5px;" >
			<table id="shopListTable" name="gridTable" ></table>
			<div id="shoptb" style="padding:2px 5px;">
				<div style="margin-bottom:5px">
			    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWinOne();" tcl-text="toolbar.add"></a>
			    	&nbsp;&nbsp;
			    	
			    	
			    	<a class="tcl-gridbar-separator"></a>
		    	</div>
		    	<div>
		    		<span tcl-text="policy.list.startDate"></span>:
			    <input  id="tcl_startDate" readonly name="tcl_startDate" type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" />
			    	
			    	<span tcl-text="user.list.gridhead.disabledDateTime"></span>:
			    <input  id="date" readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" />
			    
				    
				    <a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearchOne();" tcl-text="toolbar.search"></a>
				</div>
			</div>
		
			<div id="addShopWin" class="easyui-window tcladdwin" title="Add" style="height:250px"
				data-options="width:800,height:300,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true" >
				<div class="easyui-layout" data-options="fit:true">
			        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
			            <form id="addShopForm" method="post" enctype="multipart/form-data">
			            	<input type="hidden" name="editId"/>
			            	<input type="hidden" name="switchSign"/>
			            	<table style="width: 100%;">
			            		<tr>
									<td><i class="required">*</i><span tcl-text="policy.list.startDate"></span>:</td>
				            		<td><input  id="startDate" name="startDate"  readonly  type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" /></td>
				
			            		  	<td><i class="required">*</i><span tcl-text="user.list.gridhead.disabledDateTime"></span>:</td>
				            		<td><input  id="expirationDate" name="expirationDate"  readonly  type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" /></td>
				
				            		
				                   </tr>
			            		<tr>
			            			<td><i class="required">*</i><span tcl-text="inpo.quantity.Completion"></span>:(>=)</td>
				            		<td><input type="text" id="qtyCompletionRate" name="qtyCompletionRate" class="easyui-numberbox easyui-validatebox" data-options="required:true"/>%</td>
			            			
			            			<td><i class="required">*</i><span tcl-text="inpo.amount.Completion"></span>:(>=)</td>
				            		<td><input type="text" id="amtCompletionRate" name="amtCompletionRate" style="width:154px" class="easyui-numberbox easyui-validatebox"  data-options="required:true"  />%</td>
			       					
			            		</tr>
			            		
			            		<tr>
			            			<td><i class="required"></i><span tcl-text="inpo.complete"></span>:</td>
				            		<td><input type="text" name="amtReWard" style="width:153px" id="complete" class="easyui-numberbox easyui-validatebox"   data-options="required:true"/></td>
			            		</tr>
			            	</table>
			            	
			           
			            </form>
			        </div>
			        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
			        	<a id="submitBtn" class="btn btn-warning btn-sm" onclick="submitFormOne()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
			        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearFormOne()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>&nbsp;&nbsp;
			        </div>
			    </div>
			</div>
		</div>
		<div title="<s:text name="policy.list.key.series"></s:text>" style="padding:2px 5px;">
			<table id="shopListTableTwo" name="gridTable"></table>
		<div id="shoptbTwo" style="padding:2px 5px;">
		<div style="margin-bottom:5px">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWinTwo();" tcl-text="toolbar.add"></a>
	    	&nbsp;&nbsp;
	    	
	    	
	    	<a class="tcl-gridbar-separator"></a>
    	</div>
    	<div>
    	<span tcl-text="policy.list.startDate"></span>:
			<input  id="tcl_startDateTwo" readonly name="tcl_startDateTwo" type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" />
    	
	    	<span tcl-text="user.list.gridhead.disabledDateTime"></span>:
	    <input  id="dateTwo" readonly name="reservatioTwo" type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" />
	    
		    
		    <a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearchTwo();" tcl-text="toolbar.search"></a>
		</div>
	</div>
	
		<div id="addShopWinTwo" class="easyui-window tcladdwin" title="Add" style="height:250px"
		data-options="width:800,height:300,closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true" >
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addShopFormTwo" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<input type="hidden" name="switchSign"/>
	            	<table style="width: 100%;">
	            		<tr>
									<td><i class="required">*</i><span tcl-text="policy.list.startDate"></span>:</td>
				            		<td><input  id="startDateTwo" name="startDateTwo"  readonly  type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" /></td>
				
			            		  	<td><i class="required">*</i><span tcl-text="user.list.gridhead.disabledDateTime"></span>:</td>
				            		<td><input  id="expirationDateTwo" name="expirationDateTwo"  readonly  type="text" onclick="WdatePicker({dateFmt:'MM-yyyy'})" /></td>
				
				            		
				                   </tr>
			            		<tr>
			            			<td><i class="required">*</i><span tcl-text="inpo.quantity.Completion"></span>:(>=)</td>
				            		<td><input type="text" id="qtyCompletionRateTwo" name="qtyCompletionRateTwo" class="easyui-numberbox easyui-validatebox" data-options="required:true"/>%</td>
			            			
			            			<td><i class="required">*</i><span tcl-text="inpo.productLine"></span>:</td>
				            		<td><select style="border:1px solid #EE9A49;width:150px;height:22px;" name="productLine"  id="productLine"></select></td>
			       					
			            		</tr>
			            		
			            		<tr>
			            			<td><i class="required">*</i><span tcl-text="inpo.complete"></span>:</td>
				            		<td><input type="text" name="amtReWardTwo" style="width:153px" id="completeTwo" class="easyui-numberbox easyui-validatebox"   data-options="required:true"/></td>
			            			
			            				            			
			            		</tr>
	            	
	            	</table>
	            	
	           
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="submitBtn" class="btn btn-warning btn-sm" onclick="submitFormTwo()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearFormTwo()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>&nbsp;&nbsp;
	        </div>
	    </div>
		</div>
		</div>
		<div title="<s:text name="policy.list.rank"></s:text>" style="padding:2px 5px;">
			<table id="shopListTableThree" name="gridTable"></table>
		<div id="shoptbThree" style="padding:2px 5px;">
		<div style="margin-bottom:5px">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddWin();" tcl-text="toolbar.add"></a>
	    	&nbsp;&nbsp;
	    	
	    	
	    	<a class="tcl-gridbar-separator"></a>
    	</div>
    	<div>
	    	<span tcl-text="user.list.gridhead.disabledDateTime"></span>:
	    <input  id="date" readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" />
	    
		    
		    <a href="#" class="easyui-linkbutton" iconCls="icon-search" id="searchBt" plain="true" onclick="doSearch();" tcl-text="toolbar.search"></a>
		</div>
	</div>
	
		<div id="addShopWinThree" class="easyui-window tcladdwin" title="Add" style="height:250px"
		data-options="closed:true,collapsible:false,minimizable:false,maximizable:false,modal:true" >
		<div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center',split:false,border:false" style="padding:15px 10px;">
	            <form id="addShopForm" method="post" enctype="multipart/form-data">
	            	<input type="hidden" name="editId"/>
	            	<input type="hidden" name="hisLoac"/>
	            	<input type="hidden" name="shopId"/>
	            	<input type="hidden" name="switchSign"/>
	            	<table style="width: 100%;">
	            		<tr>
	
	            		  	<td><i class="required"></i><span tcl-text="user.list.gridhead.disabledDateTime"></span>:</td>
		            		<td>    	<input  id="date" readonly name="reservatio" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" /></td>
		
		            		<td><i class="required">*</i><span tcl-text="product.list.th.line"></span>:(>=)</td>
		            		<td><input type="text" id="line" class="easyui-numberbox easyui-validatebox" data-options="required:true"/>%</td>
		                   </tr>
	            		<tr>
	            			<td><i class="required">*</i><span tcl-text="inpo.amount.Completion"></span>:(>=)</td>
		            		<td><input type="text" id="amount" style="width:154px" class="easyui-numberbox easyui-validatebox"  data-options="required:true"  />%</td>
	       					
	            			<td><i class="required"></i><span tcl-text="inpo.complete"></span>:</td>
		            		<td><input type="text" style="width:153px" id="complete" class="easyui-numberbox easyui-validatebox"   data-options="required:true"/></td>
	            		</tr>
	            	
	            	</table>
	            	
	           
	            </form>
	        </div>
	        <div data-options="region:'south',split:false,border:false" style="text-align:center;padding:5px;height:50px;">
	        	<a id="submitBtn" class="btn btn-warning btn-sm" onclick="submitForm()"><i class="glyphicon glyphicon-ok"></i><span tcl-text="window.btn.save"></span></a>&nbsp;&nbsp;
	        	<a id="clearBtn" class="btn btn-warning btn-sm" onclick="clearForm()"><i class="glyphicon glyphicon-remove"></i><span tcl-text="window.btn.cancel"></span></a>&nbsp;&nbsp;
	        </div>
	    </div>
		</div>
		</div>
	</div>


		<div id="load-layout" style="position:fixed;width:100%;height:100%;top:0px;
				left:0px;opacity:0.4;background:#000;display:none;">
			<div align="center" style="position:absolute;left:49%;top:48%;width:31px;height:31px;">
				<img id="loadGif" src="">
			</div>
		</div>
	</div>
</body>
</html>