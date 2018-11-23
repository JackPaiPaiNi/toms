/**
 * 样机数据表管理
 */
$(function(){
	$("#samplesListTable").datagrid({
		title:locale("sample.list.title"),
		url:baseUrl + 'platform/loadSampleDeviceListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'sampleId',title:locale("sample.list.th.sampleId"),width:100,hidden:true},
	        {field:'userId',title:locale("sale.list.th.userId"),width:100,hidden:true},
	        {field:'shopId',title:locale("photo.list.th.shopId"),width:100,hidden:true},
	        {field:'customerId',title:locale("sample.list.th.customerId"),width:100,hidden:true},
	        {field:'remark',title:locale("sample.list.th.remark"),width:100,hidden:true},
	        {field:'partyId',title:locale("sample.list.th.partyId"),width:100,hidden:true},
	        {field:'partyName',title:locale("sample.list.th.partyName"),width:100},
	        {field:'customerName',title:locale("sample.list.th.customerName"),width:100},
	        {field:'shopName',title:locale("sample.list.th.shopName"),width:100},
	        {field:'hqModel',title:locale("sample.list.th.hqModel"),width:100},
	        {field:'model',title:locale("sample.list.th.model"),width:100},
	        {field:'quantity',title:locale("sample.list.th.quantity"),width:100},
	        {field:'userName',title:locale("sample.list.th.userName"),width:100},
	        {field:'datadate',title:locale("sample.list.th.datadate"),width:100}
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
	var url = baseUrl + "platform/exportSamplesExcel.action";
	location.href = url;
}