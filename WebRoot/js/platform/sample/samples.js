/**
 * 样机数据表管理
 */
$(function(){
	$("#samplesListTable").datagrid({
		title:locale("equipment.tool.title"),
		url:baseUrl + "platform/selectSample.action?classes=2",
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	              {field: 'branch',  title: locale("sample.list.th.partyName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	              {field: 'customer',  title: locale("sample.list.th.customerName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'shopName',  title: locale("sample.list.th.shopName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	              {field: 'hqModel',  title: locale("sample.list.th.hqModel"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'model',  title: locale("sample.list.th.model"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	              {field: 'quantity',  title: locale("sample.list.th.quantity"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'userId',  title: locale("sample.list.th.userName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'datadate',  title: locale("sample.list.th.datadate"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	              
	             
	    ]],
	    toolbar:'#stargettb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
	initWindow();
});
function initWindow(){
	
}
function enableBt(){
	$("#serachBt").linkbutton("enable");
}
function doSearch(){
	
	$("#serachBt").linkbutton("disable");
	var searchDate = $("#searchDate").datebox("getValue");
	var searchPatry = $("#searchPatry").val();
	var searchCustomer = $("#searchCustomer").val();
	var searchShop = $("#searchShop").val();
	var searchModel = $("#searchModel").val();
	var searchHqModel = $("#searchHqModel").val();
	$("#samplesListTable").datagrid({
		queryParams:{
			searchDate:searchDate,
			searchPatry:searchPatry,
			searchCustomer:searchCustomer,
			searchShop:searchShop,
			searchModel:searchModel,
			searchHqModel:searchHqModel
		}
	});
}

function exportExcel(){
	
	var page = $('#samplesListTable').datagrid('getPager').data("pagination").options.pageNumber;
	var rows = $('#samplesListTable').datagrid('getPager').data("pagination").options.pageSize;
	
	var searchDate = $("#searchDate").datebox("getValue");
	var searchPatry = $("#searchPatry").val();
	var searchCustomer = $("#searchCustomer").val();
	var searchShop = $("#searchShop").val();
	var searchModel = $("#searchModel").val();
	var searchHqModel = $("#searchHqModel").val();
	var valueStr = "searchDate="+searchDate+"&searchPatry="+searchPatry+"&searchCustomer="+searchCustomer+"&";
	var valueSt = "searchShop="+searchShop+"&searchModel="+searchModel+"&searchHqModel="+searchHqModel+"&";
	var valueS = "page="+page+"&rows="+rows+"";
	
	var url = baseUrl + "platform/xportSamplesExcel.action?"+valueStr+valueSt+valueS;
	location.href = url;
}