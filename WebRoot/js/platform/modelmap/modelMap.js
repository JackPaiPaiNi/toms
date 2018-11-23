/**
 * 总部产品型号与分公司产品型号关系表
 * hqt
 */

$(function(){
	$("#modelmapList").datagrid({
		title:locale("modelmap.list.title"),
		url:baseUrl + 'platform/loadModelMapListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
		columns:[[
		          {field:'id',title:locale("modelmap.list.th.id"),width:100,hidden:true},
		          {field:'partyId',title:locale("modelmap.list.th.partyId"),width:100,hidden:true},
		          {field:'branchModel',title:locale("modelmap.list.th.branchModel"),width:100},
			      {field:'hqModel',title:locale("modelmap.list.th.hqModel"),width:100},
			      {field:'price',title:locale("modelmap.list.th.price"),width:100},
			      {field:'partyName',title:locale("modelmap.list.th.partyName"),width:100},
			      {field:'ctime',title:locale("modelmap.list.th.ctime"),width:100},
			      {field:'op',title:locale("toolbar.edit"),width:80,formatter:opFormatterModel}
		          ]],
	    toolbar:'#modelmaptb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	initWindow();
});

//easygrid操作格式化
function opFormatterModel(value,item,index){
	return '<div class="tcl-btn-group">'+
		"<a href='javascript:void(0);' class='tcl-btn' onclick='verifySaleMappingByBranchModel(3,"+index+")'>"+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
		"<a href='javascript:void(0);' class='tcl-btn tcl-btn-warning' onclick='verifySaleMappingByBranchModel(4,"+index+")'>"+
			'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}

function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}

function initWindow(){
	loadPartyCountryTree($("#addModelmapWin input[name=partyId]"));
	
	//-------------------加载 总部产品----------------------
	$("#hqModel").combobox({
		url:baseUrl + "platform/getHQProductParm.action",
		valueField:'modelName',    
	    textField:'modelName',
		method:"post"
	});
}

//新增
function showAddWin(){
	$('#addModelmapForm #branchModel').textbox("enable");
	$('#addModelmapForm #price').textbox("enable");
	$('#addModelmapForm input[name=editId]').val("");
	
	$('#addModelmapForm').form("reset");
	$('#addModelmapWin').window({title:locale("modelmap.window.add")}).window('center').window('open');
}
//查询
function doSearch(){
	$("#searchBt").linkbutton("disable");
	$("#modelmapList").datagrid({
		queryParams:{keyword:$("#qryName").val()}
	});
}
//新增保存
function submitForm(){
	if(validate() ){
		
		if(!inputNum()){
			return;
		}
		
		//添加or编辑
		var id=$('#addModelmapForm input[name=editId]').val();
		if($.trim(id)!=""){
			//编辑
			$('#addModelmapForm').ajaxSubmit({
				url:baseUrl + "platform/editModelMap.action?id="+id,
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#modelmapList").datagrid('reload');
						$('#addModelmapWin').window('close');
					}else{
						showMsg(json.msg);
					}
				}
			});
		}else{
			$('#addModelmapForm').ajaxSubmit({
				url:baseUrl + "platform/addModelMap.action",
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#modelmapList").datagrid('reload');
						$('#addModelmapWin').window('close');
					}else{
						showMsg(json.msg);
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
	$('#addModelmapWin').window('close');
}

/**
 * 只能输入数字
 * @param num
 */
function inputNum(){
	var num = $("#price").val();
	var patrn=/^[0-9]+$/;
	if(!patrn.test(num)){
		showMsg(locale("modelmap.import.price.numb"));
		return false;
	}
	return true;
};

function validate(){
	return $("#addModelmapForm").form("validate");
}
//显示编辑窗口
function onEdit(index){
	//获取并填充数据
	var row=$("#modelmapList").datagrid('getRows')[index];
	$('#addModelmapForm').form('load',row);
	$('#addModelmapForm input[name=editId]').val(row.id);
	//$('#addBarcodeForm #barcode').textbox("disable");
	//显示窗口
	$('#addModelmapWin').window({title:locale("modelmap.window.edit")});
	$('#addModelmapWin').window('center');
	$('#addModelmapWin').window('open');
}

//删除
function onDelete(index){
	var row = $("#modelmapList").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "platform/deleteModelMap.action",
				type:"POST",
				data:{"id":row.id},
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#modelmapList").datagrid('reload');
					}else{
						showMsg(json.msg);
					}
				}
			});
	    }
	});
}

//导入
function importModelMap(){
	showImportWin(locale("modelmap.import.title"),baseUrl + "platform/importModelMap.action");
}

//导入
function importModelPrice(){
	showImportWin(locale("modelmap.import.price"),baseUrl + "platform/importModelPrice.action");
}



function exportExcel(){
	var url = baseUrl + "platform/exportModelExcel.action";
	location.href = url;
}

/**
 * 分公司型号是否存在对应销售数据
 */
function verifySaleMappingByBranchModel(operType,index){
	var row = $("#modelmapList").datagrid('getRows')[index];
	$.ajax({
		url:baseUrl + "platform/selectSaleMappingBybranchModel.action",
		type:"POST",
		data:{"branchModel":row.branchModel},
		success:function(data){
			var json = eval('(' + data + ')');
			var n = json.msg;
			if(n > 0){
				showMsg(locale("modelmap.operation.error.relevance"));
			}else{
				if(operType == 3){//修改操作
					onEdit(index);
				}else if(operType == 4){//删除操作
					onDelete(index);
				}
			}
		}
	});
};

