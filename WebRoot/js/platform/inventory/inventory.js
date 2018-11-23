/**
 * 库存数据表管理
 */
$(function(){
	$("#inventoryListTable").datagrid({
		title:locale("inventory.list.title"),
		url:baseUrl + 'platform/loadInventoryListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-man',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'inventoryId',title:locale("inventory.list.th.inventoryId"),width:100,hidden:true},
	        {field:'userId',title:locale("sale.list.th.userId"),width:100,hidden:true},
	        /*{field:'shopId',title:locale("photo.list.th.shopId"),width:100,hidden:true},*/
	        {field:'customerId',title:locale("inventory.list.th.customerId"),width:100,hidden:true},
	        {field:'remark',title:locale("inventory.list.th.remark"),width:100,hidden:true},
	        {field:'partyId',title:locale("inventory.list.th.partyId"),width:100,hidden:true},
	        {field:'partyName',title:locale("inventory.list.th.partyName"),width:100},
	        /*{field:'customerName',title:locale("inventory.list.th.customerName"),width:100},*/
	        {field:'shopName',title:locale("inventory.list.th.shopName"),width:100},
	        {field:'hqModel',title:locale("inventory.list.th.hqModel"),width:100},
	        {field:'model',title:locale("inventory.list.th.model"),width:100},
	        {field:'quantity',title:locale("inventory.list.th.quantity"),width:100},
	        /*{field:'userName',title:locale("inventory.list.th.userName"),width:100},*/
	        {field:'datadate',title:locale("inventory.list.th.datadate"),width:100}
	    ]],
	    toolbar:'#stargettb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
	/*initWindow();*/
});
function initWindow(){
	
}
function enableBt(){
	$("#serachBt").linkbutton("enable");
}
function doSearch(){
	$("#serachBt").linkbutton("disable");
	
	//var searchDate = $("#searchDate").datebox("getValue");
	var searchPatry = $("#searchPatry").val();
	//var searchCustomer = $("#searchCustomer").val();
	var searchShop = $("#searchShop").val();
	var searchModel = $("#searchModel").val();
	var searchHqModel = $("#searchHqModel").val();
	$("#inventoryListTable").datagrid({
		queryParams:{
			//searchDate:searchDate,
			searchPatry:searchPatry,
			//searchCustomer:searchCustomer,
			searchShop:searchShop,
			searchModel:searchModel,
			searchHqModel:searchHqModel
		}
	});
}
	//导入数据
function importInventory(){
	showImportWin(locale("inventory.import"),baseUrl + "platform/importInventory.action");
}