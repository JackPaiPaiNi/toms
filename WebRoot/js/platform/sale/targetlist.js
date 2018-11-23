/**
 * 销售数据表管理
 */
$(function(){
	$("#saleTargetListTable").datagrid({
		title:locale("saletarget.list.title"),
		url:baseUrl + 'platform/loadSaleTargetListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'saleTargetId',title:locale("sale.list.th.saleTargetId"),width:100,hidden:true},
	        {field:'shopId',title:locale("saletarget.list.th.shopId"),width:100,hidden:true},
//	        {field:'userId',title:locale("sale.list.th.userId"),width:100,hidden:true},
	        {field:'customerId',title:locale("saletarget.list.th.customerId"),width:100,hidden:true},
	        {field:'partyId',title:locale("saletarget.list.th.partyId"),width:100,hidden:true},
//	        {field:'userName',title:locale("sale.list.th.userName"),width:100,hidden:true},
	        {field:'partyName',title:locale("saletarget.list.th.partyName"),width:100},
	        {field:'customerName',title:locale("saletarget.list.th.customerName"),width:100},
	        {field:'shopName',title:locale("saletarget.list.th.shop"),width:100},
	        {field:'quantity',title:locale("sale.list.th.quantity"),width:100},
	        {field:'amount',title:locale("sale.list.th.amount"),width:100},
	        {field:'op',title:locale("toolbar.edit"),width:80,formatter:opFormatter}
	    ]],
	    toolbar:'#stargettb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
	initWindow();
});
function enableBt(){
	$("#serachBt").linkbutton("enable");
	initDataGridCells();
}
function initWindow(){
	initLoadShopAndUser($("#shopId"));
}
function doSearch(){
	$("#serachBt").linkbutton("disable");
	var searchParty = $("#searchParty").val();
	var searchCustomer = $("#searchCustomer").val();
	var searchShop = $("#searchShop").val();
	$("#saleTargetListTable").datagrid({
		queryParams:{searchParty:searchParty,searchCustomer:searchCustomer,searchShop:searchShop}
	});
}

//新增
function showAddWin(){
	$('#addsaleTargetForm input[name=editId]').val("");
	
	$('#addsaleTargetForm').form("reset");
	$('#addsaleTargetWin').window({title:locale("saletarget.window.add")}).window('center').window('open');
}

//新增保存
function submitForm(){
	if(validate()){
		if(numberValidation()){
			//添加or编辑
			var id=$('#addsaleTargetForm input[name=editId]').val();
			if($.trim(id)!=""){
				//编辑
				$('#addsaleTargetForm').ajaxSubmit({
					url:baseUrl + "platform/editSaleTarget.action?id="+id,
					success:function(data){
						var result = eval('('+data+')');
						if(result.success){
							$("#saleTargetListTable").datagrid('reload');
							$('#addsaleTargetWin').window('close');
							showMsg(locale("alert.success"));
						}else{
							showMsg(result.msg);
						}
					}
				});
			}else{
				$('#addsaleTargetForm').ajaxSubmit({
					url:baseUrl + "platform/addSaleTarget.action",
					success:function(data){
						var json = eval('(' + data + ')');
						var falg = json.success;
						if(falg){
							showMsg(locale("alert.success"));
							$("#saleTargetListTable").datagrid('reload');
							$('#addsaleTargetWin').window('close');
						}else{
							showMsg(json.msg);
						}
					}
				});
			}
		}
	}else{
		showMsg(locale("alert.validate.fail"));
	}
}

//关闭新增框
function clearForm(){
	$('#addsaleTargetWin').window('close');
}

function validate(){
	return $("#addsaleTargetForm").form("validate");
}

//显示编辑窗口
function onEdit(index){
	//获取并填充数据
	var row=$("#saleTargetListTable").datagrid('getRows')[index];
	$('#addsaleTargetForm input[name=editId]').val(row.saleTargetId);
	$('#addsaleTargetForm').form('load',row);
	editLoadShopAndUser($("#shopId"),row);
	//显示窗口
	$('#addsaleTargetWin').window({title:locale("saletarget.window.edit")});
	$('#addsaleTargetWin').window('center');
	$('#addsaleTargetWin').window('open');
}

//删除
function onDelete(index){
	var row = $("#saleTargetListTable").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "platform/deleteSaleTarget.action",
				type:"POST",
				data:{"id":row.saleTargetId},
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#saleTargetListTable").datagrid('reload');
					}else{
						showMsg(result.msg);
					}
				}
			});
	    }
	});
}

//校验数字
function numberValidation(){
	var falg = true;
	var quantityRegExp = new RegExp("^[0-9]*$");
	var quantityValue = $("#quantity").val();
	if(!quantityRegExp.test(quantityValue)){
		showMsg(jsLocale.get('saletarget.list.form.error.quantity'));
		falg = false;
		return falg;
	}
	
	var amountRegExp = new RegExp("^[0-9]+(\.[0-9]+)?$");
	var amountValue = $("#amount").val();
	if(!amountRegExp.test(amountValue)){
		showMsg(jsLocale.get('saletarget.list.form.error.amount'));
		falg = false;
		return falg;
	}
	
	return falg;
}

//门店、用户联动
function initLoadShopAndUser($shopTree){
	if(!$shopTree) return;
	
	$shopTree.combobox({
		url:baseUrl + "platform/getShopList.action",
		valueField:'shopId',    
	    textField:'shopName',
		onChange:function(newValue,oldValue){
			var shopId= $(this).combobox("getValue");
//			if($userTree){
//				$userTree.combobox("clear");
//				$userTree.combobox('reload',baseUrl + "platform/getUserList.action?shopId="+shopId+"&random="+Math.random());
//				$userTree.combobox("setValue",'');
//			}
		}
	});
//	if($userTree){
//		$userTree.combobox({
//			valueField:'userLoginId',
//		    textField:'userName',
//		    onChange :function(newValue,oldValue){
//		    	return;
//		    }
//		});
//	}
}

function editLoadShopAndUser($shopTree,row){
	if($shopTree){
		var shopId= $shopTree.combobox("getValue");
//		$userTree.combobox('reload',baseUrl + "platform/getUserList.action?shopId="+shopId+"&random="+Math.random());
//		$userTree.combobox("setValue",row.userId);
	}
}

