/**
 * 总部产品型号与分公司产品型号关系表
 * hqt
 */

$(function(){
	$("#modelmapList").datagrid({
		title:locale("channelmodelmap.list.title"),
		url:baseUrl + 'platform/loadChannelModelMapListData.action',
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
			      {field:'channelModel',title:locale("modelmap.list.th.cModel"),width:100},
			      {field:'price',title:locale("modelmap.list.th.price"),width:100,hidden:true},
			      {field:'partyName',title:locale("modelmap.list.th.partyName"),width:100},
			      {field:'customerName',title:locale("modelmap.list.th.customerName"),width:100},
			      {field:'ctime',title:locale("modelmap.list.th.ctime"),width:100},
			      {field:'op',title:locale("toolbar.edit"),width:80,formatter:opFormatter}
		          ]],
	    toolbar:'#modelmaptb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	initWindow();
});
function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}

function initWindow(){
	initDistrictComponentsByModel($("#addModelmapWin input[name=partyId]"),$("#addModelmapWin input[name=customerId]"),'admin');
	//-------------------加载 总部产品----------------------
	$("#branchModel").combobox({
		url:baseUrl + "platform/selectChennalModelByPartyId.action",
		valueField:'branchModel',    
	    textField:'branchModel',
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
	if(validate()){
		//添加or编辑
		var id=$('#addModelmapForm input[name=editId]').val();
		if($.trim(id)!=""){
			//编辑
			$('#addModelmapForm').ajaxSubmit({
				url:baseUrl + "platform/udpateChannelModelById.action?id="+id,
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg == true){
						showMsg(locale("alert.success"));
						$("#modelmapList").datagrid('reload');
						$('#addModelmapWin').window('close');
					}else if('custModelError' == falg){
						
						showMsg(locale("channelModelmap.alert.modelExist"));
						$("#modelmapList").datagrid('reload');
					}else if('branModelError' == falg){
						
						showMsg(locale("branchCModelmap.alert.modelExist"));
						$("#modelmapList").datagrid('reload');
					}else{
						showMsg(json.msg);
					}
				}
			});
		}else{
			$('#addModelmapForm').ajaxSubmit({
				url:baseUrl + "platform/insertChannelModel.action",
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg == 'true'){
						showMsg(locale("alert.success"));
						$("#modelmapList").datagrid('reload');
						$('#addModelmapWin').window('close');
					}else if('custModelError' == falg){
						
						showMsg(locale("channelModelmap.alert.modelExist"));
						$("#modelmapList").datagrid('reload');
					}else if('branModelError' == falg){
						
						showMsg(locale("branchCModelmap.alert.modelExist"));
						$("#modelmapList").datagrid('reload');
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

function validate(){
	return $("#addModelmapForm").form("validate");
}
//显示编辑窗口
function onEdit(index){
	//获取并填充数据
	var row=$("#modelmapList").datagrid('getRows')[index];
/*	$("[name='customerId']").combobox({
			url:baseUrl + "platform/getCustomerByCountry.action?countryId=" + row.partyId,
			valueField:'customerId',
		    textField:'customerName',
	 });*/
	//$("[name='customerId']").combobox('reload',baseUrl + "platform/getCustomerByCountry.action?countryId=" + row.partyId);
	//$("[name='customerId']").combobox("setValue",row.customerId);
	$('#addModelmapForm').form('load',row);
	editDistrictComponents($("#partyId"),$("#customerId"),row);
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
				url:baseUrl + "platform/deleteChannelModel.action",
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
	showImportWin(locale("modelmap.import.title"),baseUrl + "platform/importChannelModelMap.action");
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
 * 国家、渠道级联
 * @param $country
 * @param $partyId
 * @param _searchFlag
 */
function initDistrictComponentsByModel($country,$partyId,_searchFlag){
	if(!$country)
		return;//至少得有国家一级
	//自定义 一个 空的数据  用于置空级联数据
	var conStr = null;
	if(_searchFlag != null && _searchFlag == "all"){
		conStr = "_searchFlag=all";
	}else{
		conStr = "_searchFlag="+_searchFlag+"";
	}
	
	$country.combobox({
		url:baseUrl + 'platform/getCountryList.action?'+conStr,
	    valueField:'countryId',
	    textField:'countryName',
	    onChange :function(newValue,oldValue){
	    	var countryId= $(this).combobox("getValue");
	    	if($partyId){
	    		$partyId.combobox("clear");
	    		$partyId.combobox('reload',baseUrl + "platform/getCustomerByCountry.action?countryId=" + countryId);
	    	}
	    }	
	});
	
	if($partyId){
		$partyId.combobox({
			url:baseUrl + 'platform/getCountryList.action?'+conStr,
			valueField:'customerId',
		    textField:'customerName',
		    onChange :function(newValue,oldValue){
		    	return;
		    }
		});
	}
}

//编辑级联框赋值
function editDistrictComponents($country,$partyId,row){
	if($country){
		var countryId = $country.combobox("getValue");
		$partyId.combobox('reload',baseUrl + "platform/getCustomerByCountry.action?countryId=" + countryId);
		$partyId.combobox("setValue",row.customerId);
	}
}
