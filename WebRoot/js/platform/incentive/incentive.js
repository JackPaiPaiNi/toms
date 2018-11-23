/**
 * 提成设定
 */

$(function(){
	$("#modelmapList").datagrid({
		title:locale("incentive.list.title"),
		url:baseUrl + 'incentive/loadIncentiveList.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
		columns:[[
		          {field:'incentiveId',title:locale("incentive.list.th.id"),width:100,hidden:true},
		          {field:'partyName',title:locale("incentive.list.th.partyName"),width:100},
		          {field:'branchModel',title:locale("incentive.list.th.branchModel"),width:100},
//			      {field:'size',title:locale("incentive.list.th.size"),width:100},
			      {field:'retailPrice',title:locale("incentive.list.th.retailPrice"),width:100},
			      {field:'incentive',title:locale("incentive.list.th.incentive"),width:100},
//			      {field:'quantity',title:locale("incentive.list.th.quantity"),width:100},
			      {field:'date',title:locale("incentive.list.th.ctime"),width:100},
			      {field:'op',title:locale("toolbar.edit"),width:80,formatter:opFormatterModel}
		          ]],
	    toolbar:'#modelmaptb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	initWindow();
	
	
	//快速获取国家下拉菜单
	$("#selectQuertyPartyId").combobox({
		url:baseUrl + "platform/selectUserPartyCountry.action",
		valueField:'partyId',    
	    textField:'partyName',
		method:"post",
		onChange: function () {
			obj.search();
		}
	});
	
	//快速获取国家下拉菜单
//	$("#partyId").change(function(){
//		var countryId=$("#partyId").val();
//		$("#branchModel").combobox({
//			url:baseUrl + "incentive/selectBranchModel.action?countryId="+countryId,
//			valueField:'branchModel',    
//		    textField:'branchModel',
//			method:"post",
//		});
//	});
});

//全局变量
obj = {
	search:function(){
		$('#modelmapList').datagrid("load",{
			searchMsg:$.trim($("#searchMsg").val()),
			selectQuertyPartyId:$.trim($('#selectQuertyPartyId').combobox('getValue'))
		});
		$('#modelmapList').datagrid('clearSelections');
	}
};

//easygrid操作格式化
function opFormatterModel(value,item,index){
	return '<div class="tcl-btn-group">'+
	'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
	'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
	'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}

function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}

function initWindow(){
//	loadPartyCountryTree($("#addModelmapWin input[name=partyId]"));
	loadCountryName();
//	initCountry();
//	loadParameters('TCL_PRODUCT').success(function(result){
//		$("#addModelmapWin input[name='size']").combobox({
//			textField:"pvalue",
//			valueField:"pvalue",
//			data:result.SIZE
//		});
//	});
}

//新增
function showAddWin(){
//	$('#addModelmapForm #branchModel').textbox("enable");
	$('#addModelmapForm #price').textbox("enable");
	$('#addModelmapForm input[name=editId]').val("");
	
	$("#branchModel").empty();
	$('#addModelmapForm').form("reset");
	$('#addModelmapWin').window({title:locale("incentive.window.add")}).window('center').window('open');
}
//查询
function doSearch(){
	$("#searchBt").linkbutton("disable");
	$("#modelmapList").datagrid({
		queryParams:{keyword:$("#qryName").val(),selectQuertyPartyId:$.trim($('#selectQuertyPartyId').combobox('getValue'))}
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
				url:baseUrl + "incentive/updateIncentive.action?id="+id,
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#modelmapList").datagrid('reload');
						$('#addModelmapWin').window('close');
					}else if(json.msg){
						showMsg(locale("incentive.list.branchModel"));
						return;
					}else{
						showMsg(json.msg);
					}
				}
			});
		}else{
			$('#addModelmapForm').ajaxSubmit({
				url:baseUrl + "incentive/addIncentive.action",
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#modelmapList").datagrid('reload');
						$('#addModelmapWin').window('close');
					}else if(json.msg){
						showMsg(locale("incentive.list.branchModel"));
						return;
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
	var retailPrice = $("#retailPrice").val();
	var incentive = $("#incentive").val();
	var date =$("#date").val();
	var partyId=$("#partyId").combobox("getValue");
	var branchModel=$("#branchModel").combobox("getValue");
//	var quantity = $("#quantity").val();
	
	var pricePartn=/(^[1-9]([0-9]+)?(\.[0-9]{1,})?$)|(^(0){1}$)|(^[0-9]\.[0-9]{1,}?$)/;
	var patrn=/^[0-9]+$/;
	
	if(partyId=="" || partyId==null){
		showMsg(locale("incentive.list.incentive.countryName"));
		$("#partyId").focus();
		return false;
	}
	
	if(branchModel=="" || branchModel==null){
		showMsg(locale("incentive.list.incentive.branchModel"));
		$("#branchModel").focus();
		return false;
	}
	
	if(!pricePartn.test(retailPrice)){
		showMsg(locale("incentive.list.price.numb"));
		return false;
	}
	if(!pricePartn.test(incentive)){
		showMsg(locale("incentive.list.incentive.numb"));
		return false;
	}
	
	if(Number(retailPrice)<Number(incentive)){
		showMsg(locale("incentive.list.incentive.morethan"));
		return false;
	}
	
//	if(quantity!=""){
//		if(!patrn.test(quantity)){
//			showMsg(locale("incentive.list.quantity.numb"));
//			return false;
//		}
//	}
	
	if(date==""|| date==null){
		showMsg(locale("incentive.list.incentive.date"));
		$("#date").focus();
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
//	loadBranchModel($("#partyId"),$("#branchModel"),row.partyId);
	$('#addModelmapForm').form('load',row);
	$('#addModelmapForm input[name=editId]').val(row.id);
	editBranchModel(row.partyId,row.branchModel,row);
	//$('#addBarcodeForm #barcode').textbox("disable");
	//显示窗口
	$('#addModelmapWin').window({title:locale("incentive.window.edit")});
	$('#addModelmapWin').window('center');
	$('#addModelmapWin').window('open');
}

//删除
function onDelete(index){
	var row = $("#modelmapList").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "incentive/deleteIncentive.action",
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
	showImportWin(locale("modelmap.import.title"),baseUrl + "incentive/importIncentive.action");
}


//日期转换函数
//var todate = function(value,row,index) 
//{
//	
//	if(value == null)
//	{
//		return null;
//	}
//	var d = new Date();
//	d.setTime(value.time); 
//	return d.getFullYear()+"-"+(d.getMonth()+1);
//};

//加载国家列表
function loadCountryName(){
	$.ajax({
		url:baseUrl + "platform/selectUserPartyCountry.action",
		type:"POST",
		success:function(result){
			$("#partyId").combobox({
				data:result,
				valueField:'partyId',    
			    textField:'partyName',
			    onChange:function(newValue,oldValue){
			    	var countryId= $(this).combobox("getValue");
			    	$("#branchModel").combobox("clear");
			    	$("#branchModel").combobox('reload',baseUrl + 'incentive/selectBranchModel.action?countryId='+countryId);
			    	$("#branchModel").combobox("setValue","");
//			    	$.ajax({
//			    		url:baseUrl + "incentive/selectBranchModel.action",
//			    		type:"POST",
//			    		data:{"countryId":countryId},
//			    		success:function(result){
//			    			$("#branchModel").combobox({
//			    				data:result,
//			    				valueField:'branchModel',    
//			    			    textField:'branchModel',
//			    			});
//			    		}
//			    	});
			    }
			});
		}
	});
	
	$("#branchModel").combobox({
		valueField:'branchModel',
	    textField:'branchModel',
	    onChange :function(newValue,oldValue){
	    	return;
	    }
	});
}

//初始化国家
function initCountry(){
	var optStr="<option value=''></option>";
	$.ajax({
		url:baseUrl + "platform/selectUserPartyCountry.action",
		type:"POST",
		success:function(result){
			$.each(result,function(i,n){
				optStr+="<option value="+n.partyId+">"+n.partyName+"</option>";
			});
			$("#partyId").html(optStr);
			$("#partyId").change(function(){
				initBranchModel($("#partyId").val());	
			});
		}
	});
}
//联动分公司型号
function initBranchModel(countryId){
	var optStr="<option value=''></option>";
	$.ajax({
		url:baseUrl + "incentive/selectBranchModel.action",
		type:"POST",
		data:{"countryId":countryId},
		success:function(result){
			if(countryId!=null && countryId!=""){
				$.each(result,function(i,n){

				optStr+="<option 	value="+n.branchModel+">"+n.branchModel+"</option>";
					
				});
			}
			$("#branchModel").html(optStr);
		}
	});
}

//编辑分公司型号
function editBranchModel(countryId,branchModel,row){
	$("#branchModel").combobox('reload',baseUrl + 'incentive/selectBranchModel.action?countryId='+countryId);
	$("#branchModel").combobox("setValue",row.branchModel);
//	var optStr="<option value=''></option>";
//	$.ajax({
//		url:baseUrl + "incentive/selectBranchModel.action",
//		type:"POST",
//		data:{"countryId":countryId},
//		success:function(result){
//			if(countryId!=null && countryId!=""){
//				$.each(result,function(i,n){
//					if(branchModel==n.branchModel){
//						optStr+="<option selected='selected'	value="+n.branchModel+">"+n.branchModel+"</option>";
//					}else{
//						optStr+="<option 	value="+n.branchModel+">"+n.branchModel+"</option>";
//					}					
//				});
//			}
//			$("#branchModel").html(optStr);
//		}
//	});
}