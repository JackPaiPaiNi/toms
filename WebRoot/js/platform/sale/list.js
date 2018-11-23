/**
 * 销售数据表管理
 */
$(function(){
	$("#saleListTable").datagrid({
		title:locale("sale.list.title"),
		url:baseUrl + 'platform/loadSaleListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'saleId',title:locale("sale.list.th.saleId"),width:100,hidden:true},
	        {field:'userId',title:locale("sale.list.th.userId"),width:100,hidden:true},
	        {field:'shopId',title:locale("photo.list.th.shopId"),width:100,hidden:true},
	        {field:'remark',title:locale("sale.list.th.remark"),width:100,hidden:true},
	        {field:'ctime',title:locale("sale.list.th.ctime"),width:100,hidden:true},
	        {field:'partyId',title:locale("sale.list.th.partyId"),width:100,hidden:true},
	        {field:'customerId',title:locale("sale.list.th.customerId"),width:100,hidden:true},
	        {field:'country',title:locale("sale.list.th.country"),width:100},
	        {field:'partyName',title:locale("sale.list.th.partyName"),width:100},
	        {field:'customerName',title:locale("sale.list.th.customerName"),width:100},
	        {field:'shopName',title:locale("sale.list.th.shopName"),width:100},
	        {field:'hqModel',title:locale("sale.list.th.hqMadel"),width:100},
	        {field:'model',title:locale("sale.list.th.model"),width:100},
	        {field:'quantity',title:locale("sale.list.th.quantity"),width:70},
	        {field:'amount',title:locale("sale.list.th.amount"),width:100},
	        {field:'datadate',title:locale("sale.list.th.datadate"),width:100},
	        {field:'userName',title:locale("sale.list.th.salerName"),width:100},
	        {field:'op',title:locale("toolbar.edit"),width:80,formatter:tbrForDate}
	    ]],
	    toolbar:'#saletb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
	initWindow();
});
function enableBt(){
	$("#searchBt").linkbutton("enable");
}
/*function initWindow(){
	$.getJSON(baseUrl + "platform/loadSalersData.action",function(data){
		$("#userId").combobox({
			data:data
		})
	});
}*/
function initWindow(){
	initLoadShopAndUser($("#shopId"));
}

function showAddWin(){
	$('#addSaleForm #productId').textbox("enable");
	$('#addSaleForm input[name=editId]').val("");
	
	$('#addSaleForm').form("reset");
	$('#addSaleWin').window({title:locale("sale.window.add")}).window('center').window('open');
}
function doSearch(){
	$("#searchBt").linkbutton("disable");
	
	var searchCdate = $("#searchCdate").datebox("getValue");
	var searchDdate = $("#searchDdate").datebox("getValue");
	var searchParty = $("#searchParty").val();
	var searchCParty = $("#searchCParty").val();
	var searchCustomer = $("#searchCustomer").val();
	var searchShop = $("#searchShop").val();
	
	var searchHModel = $("#searchHModel").val();
	var searchBModel = $("#searchBModel").val();
	
	$("#saleListTable").datagrid({
		queryParams:{
			searchCdate:searchCdate,
			searchDdate:searchDdate,
			searchParty:searchParty,
			searchCParty:searchCParty,
			searchCustomer:searchCustomer,
			searchShop:searchShop,
			searchHModel:searchHModel,
			searchBModel:searchBModel
		}
	});
}


//关闭新增框
function clearForm(){
	$('#addSaleWin').window('close');
}

function validate(){
	return $("#addSaleForm").form("validate");
}

//显示编辑窗口
function onEdit(index){
	//获取并填充数据
	var row=$("#saleListTable").datagrid('getRows')[index];
	$('#addSaleForm input[name=editId]').val(row.saleId);
	$('#addSaleForm').form('load',row);
	editLoadShopAndUser($("#shopId"),row);
	//显示窗口
	$('#addSaleWin').window({title:locale("sale.window.edit")});
	$('#addSaleWin').window('center');
	$('#addSaleWin').window('open');
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
		}
	});
}

//新增保存
function submitForm(){
	if(validate()){
		//添加or编辑
		var id=$('#addSaleForm input[name=editId]').val();
		if($.trim(id)!=""){
			//编辑
			$('#addSaleForm').ajaxSubmit({
				url:baseUrl + "platform/editSale.action?id="+id,
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						$("#saleListTable").datagrid('reload');
						$('#addSaleWin').window('close');
						showMsg(locale("alert.success"));
					}else {
						showMsg(result.msg);
					}
				}
			});
		}else{
			$('#addSaleForm').ajaxSubmit({
				url:baseUrl + "platform/addSale.action",
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#saleListTable").datagrid('reload');
						$('#addSaleWin').window('close');
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

//删除
function onDelete(index){
	var row = $("#saleListTable").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "platform/deleteSale.action",
				type:"POST",
				data:{"id":row.saleId},
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#saleListTable").datagrid('reload');
					}else{
						showMsg(result.msg);
					}
				}
			});
	    }
	});
}

function clearForm(){
	$('#addSaleWin').window('close');
}

function exportExcel(){
	var searchCdate = $("#searchCdate").datebox("getValue");
	var searchDdate = $("#searchDdate").datebox("getValue");
	var searchParty = $("#searchParty").val();
	var searchCParty = $("#searchCParty").val();
	var searchCustomer = $("#searchCustomer").val();
	var searchShop = $("#searchShop").val();
	
	var searchHModel = $("#searchHModel").val();
	var searchBModel = $("#searchBModel").val();
	var str="?searchCdate="+searchCdate;
	str+="&searchDdate="+searchDdate;
	str+="&searchParty="+searchParty;
	str+="&searchCParty="+searchCParty;
	str+="&searchCustomer="+searchCustomer;
	str+="&searchShop="+searchShop;
	str+="&searchHModel="+searchHModel;
	str+="&searchBModel="+searchBModel;
		
	var url = baseUrl + "platform/exportSaleExcel.action"+str;
	location.href = url;
}

function editLoadShopAndUser($shopTree,row){
	if($shopTree){
		var shopId= $shopTree.combobox("getValue");
//		$userTree.combobox('reload',baseUrl + "platform/getUserList.action?shopId="+shopId+"&random="+Math.random());
//		$userTree.combobox("setValue",row.userId);
	}
}

//导入数据
function importSales(){
	showImportWin(locale("sale.import"),baseUrl + "platform/importSales.action");
}













//公用的easygrid操作格式化
function tbrForDate(value,item,index){
	var beginTimes = item.ctime.split('-');
	beginTimes=beginTimes[0]+beginTimes[1]+beginTimes[2];
	beginTimes=beginTimes.split(':');
	beginTimes=beginTimes[0]+beginTimes[1]+beginTimes[2];
	beginTimes=beginTimes.split(' ');
	beginTimes=beginTimes[0]+beginTimes[1];
	

	if(new Date().Format("yyyyMMddhhmmss.S") - parseFloat(beginTimes)>1000000){  
		return null;
	}{
		return '<div class="tcl-btn-group">'+
		'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
		'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
			'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
	}
	
	
}


Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};