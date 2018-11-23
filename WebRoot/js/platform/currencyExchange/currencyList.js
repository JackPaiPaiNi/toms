/**
 * 汇率转换
 */

$(function(){	
	$("#selectQuertyPartyId").combobox({
		url:baseUrl + "platform/selectUserPartyCountry.action",
		valueField:'partyId',    
	    textField:'partyName',
		method:"post",
		onChange: function () {
			obj.search();
		}
	});
	
	$("#currencyList").datagrid({
//		fit:true,
//		iconCls:'icon-large-icons',
//		fitColumns:true,
//		resizeHandle:'both',
//	//	autoRowHeight:false,
//		striped:true,
//		method:'post',
		url:baseUrl + 'platform/loadExchangeList.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
//		idField:'countryName',
//		pagination:true,
//		rownumbers:true,
//		singleSelect:true,
//		sortName:'dataDate',
//		sortOrder:'desc',
//		remoteSort:true,
//		pageNumber:1,
//		pageSize:10,
//		pageList:[10,20,30,40,50,100],
//		queryParams:{},
		columns:[[
		          {field: 'id',  title: jsLocale.get('exchange.id'),width:100,hidden:true}, 
		          {field: 'countryId',  title: jsLocale.get('exchange.countryId'),width:100,hidden:true}, 
		            {field: 'countryName',  title: jsLocale.get('exchange.countryName'),width:100},  
		            {field: 'exchange',  title: jsLocale.get('exchange.currency'),width:100},
//		            {field: 'ctime',  title: jsLocale.get('exchange.ctime'),width:100,hidden:true},
		            {field: 'dataDate',  title: jsLocale.get('exchange.dataData'),width:110},
//		            {field:'op',title:locale("toolbar.edit"),width:80,formatter:opFormatter},
		          ]],
	    toolbar:'#barcodetb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	initWindow();
});

function initWindow(){
//	loadPartyCountryTree($("#addCurrencyWin input[name=countryId]"));
	$("#countryId").combobox({
		url:baseUrl + "platform/selectUserPartyCountry.action",
		valueField:'partyId',    
	    textField:'partyName',
		method:"post"
	});
}

//全局变量
obj = {
	search:function(){
		$('#currencyList').datagrid("load",{
			searchMsg:$.trim($("#searchMsg").val()),
			selectQuertyPartyId:$.trim($('#selectQuertyPartyId').combobox('getValue'))
		});
		$('#currencyList').datagrid('clearSelections');
	}
};

//日期转换函数
var todate = function(value,row,index) 
{
	
	if(value == null)
	{
		return null;
	}
	var d = new Date();
	d.setTime(value.time); 
	return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
};

function enableBt(){
	$("#barcodetb").linkbutton("enable");
	initDataGridCells();
}
//新增
//function showAddWin(){
//	$('#addCurrecncyForm #countryId').textbox("enable");
//	$('#addCurrecncyForm input[name=editId]').val("");
//	
//	$('#addCurrecncyForm').form("reset");
//	$('#addCurrencyWin').window({title:locale("exchange.list.but.add")}).window('center').window('open');
//}

//新增保存
//function submitForm(){
//	if(validate()){
//		//添加or编辑
//		var id=$('#addCurrecncyForm input[name=editId]').val();
//		if($.trim(id)!=""){
//			//编辑
//			$('#addCurrecncyForm').ajaxSubmit({
//				url:baseUrl + "platform/editExchange.action?id="+id,
//				success:function(data){
//					var result=eval('('+data+')');
//					if(result.success){
//						$("#currencyList").datagrid('reload');
//						$('#addCurrencyWin').window('close');
//						showMsg(locale("alert.success"));
//					}else{
//						showMsg(result.msg);
//					}
//				}
//			});
//		}else{
//			$('#addCurrecncyForm').ajaxSubmit({
//				url:baseUrl + "platform/saveExchange.action",
//				success:function(data){
//					var result=eval('('+data+')');
//					if(result.success){
//						showMsg(locale("alert.success"));
//						$("#currencyList").datagrid('reload');
//						$('#addCurrencyWin').window('close');
//					}else{
//						showMsg(result.msg);
//					}
//				}
//			});
//		}
//	}else{
//		showMsg(locale("alert.validate.fail"));
//	}
//	
//}
////关闭新增框
//function clearForm(){
//	$('#addCurrencyWin').window('close');
//}
//
//function validate(){
//	return $("#addCurrecncyForm").form("validate");
//}
////显示编辑窗口
//function onEdit(index){
//	//获取并填充数据
//	var row=$("#currencyList").datagrid('getRows')[index];
//	$('#addCurrecncyForm').form('load',row);
//	$('#addCurrecncyForm input[name=editId]').val(row.id);
//	//$('#addBarcodeForm #barcode').textbox("disable");
//	//显示窗口
//	$('#addCurrencyWin').window({title:locale("barcode.window.edit")});
//	$('#addCurrencyWin').window('center');
//	$('#addCurrencyWin').window('open');
//}
//
////删除
//function onDelete(index){
//	if(confirm(locale("alert.delete.confirm"))){
//		var row = $("#currencyList").datagrid('getRows')[index];
//		$.ajax({
//			url:baseUrl + "platform/delectExchange.action",
//			type:"POST",
//			data:{"id":row.id},
//			success:function(data){
//				var result=eval('('+data+')');
//				if(result.success){
//					showMsg(locale("alert.success"));
//					$("#currencyList").datagrid('reload');
//				}else{
//					showMsg(result.msg);
//				}
//			}
//		});
//	}
//}
