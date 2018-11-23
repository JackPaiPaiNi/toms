/**
 * 产品条码列表
 * hqt
 */

$(function(){
	$("#barcodeList").datagrid({
		title:locale("barcode.list.title"),
		url:baseUrl + 'platform/loadBarcodeListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
		columns:[[
		          {field:'id',title:locale("barcode.list.th.id"),width:100,hidden:true},
		          {field:'barcode',title:locale("barcode.list.th.barcode"),width:100},
			      {field:'hqModel',title:locale("barcode.list.th.model"),width:100},
			      {field:'ctime',title:locale("barcode.list.th.ctime"),width:100},
			      {field:'op',title:locale("toolbar.edit"),width:80,formatter:opFormatter}
		          ]],
	    toolbar:'#barcodetb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
});
function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}
//新增
function showAddWin(){
	$('#addBarcodeForm #barcode').textbox("enable");
	$('#addBarcodeForm input[name=editId]').val("");
	
	$('#addBarcodeForm').form("reset");
	$('#addBarcodeWin').window({title:locale("barcode.window.add")}).window('center').window('open');
}
//查询
function doSearch(){
	$("#searchBt").linkbutton("disable");
	$("#barcodeList").datagrid({
		queryParams:{keyword:$("#qryName").val()}
	});
}
//新增保存
function submitForm(){
	if(validate()){
		//添加or编辑
		var id=$('#addBarcodeForm input[name=editId]').val();
		if($.trim(id)!=""){
			//编辑
			$('#addBarcodeForm').ajaxSubmit({
				url:baseUrl + "platform/editBarcode.action?id="+id,
				success:function(data){
					var result=eval('('+data+')');
					if(result.success){
						$("#barcodeList").datagrid('reload');
						$('#addBarcodeWin').window('close');
						showMsg(locale("alert.success"));
					}else{
						showMsg(result.msg);
					}
				}
			});
		}else{
			$('#addBarcodeForm').ajaxSubmit({
				url:baseUrl + "platform/addBarcode.action",
				success:function(data){
					var result=eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#barcodeList").datagrid('reload');
						$('#addBarcodeWin').window('close');
					}else{
						showMsg(result.msg);
					}
				}
			});
		}
	}else{
		showMsg(locale("alert.validate.fail"));
	}
	
}
//关闭新增框
function clearForm(){
	$('#addBarcodeWin').window('close');
}

function validate(){
	return $("#addBarcodeForm").form("validate");
}
//显示编辑窗口
function onEdit(index){
	//获取并填充数据
	var row=$("#barcodeList").datagrid('getRows')[index];
	$('#addBarcodeForm').form('load',row);
	$('#addBarcodeForm input[name=editId]').val(row.id);
	//$('#addBarcodeForm #barcode').textbox("disable");
	//显示窗口
	$('#addBarcodeWin').window({title:locale("barcode.window.edit")});
	$('#addBarcodeWin').window('center');
	$('#addBarcodeWin').window('open');
}

//删除
function onDelete(index){
	if(confirm(locale("alert.delete.confirm"))){
		var row = $("#barcodeList").datagrid('getRows')[index];
		$.ajax({
			url:baseUrl + "platform/deleteBarcode.action",
			type:"POST",
			data:{"id":row.id},
			success:function(data){
				var result=eval('('+data+')');
				if(result.success){
					showMsg(locale("alert.success"));
					$("#barcodeList").datagrid('reload');
				}else{
					showMsg(result.msg);
				}
			}
		});
	}
}
