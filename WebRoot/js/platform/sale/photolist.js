/**
 * 终端上传图片管理
 */
$(function(){
	$("#photoListTable").datagrid({
		title:locale("photo.list.title"),
		url:baseUrl + 'platform/loadTerminalPhotoListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		nowrap:false,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'photoId',title:locale("photo.list.th.photoId"),width:100,hidden:true},
	        {field:'userId',title:locale("sale.list.th.userId"),width:100,hidden:true},
	        {field:'shopId',title:locale("photo.list.th.shopId"),width:100,hidden:true},
	        {field:'partyId',title:locale("photo.list.th.patryId"),width:100,hidden:true},
	        {field:'customerId',title:locale("photo.list.th.customerId"),width:100,hidden:true},
	        {field:'latitude',title:locale("photo.list.th.latitude"),width:100,hidden:true},
	        {field:'longitude',title:locale("photo.list.th.longitude"),width:100,hidden:true},
	        {field:'serverId',title:locale("photo.list.th.serverId"),width:80,align:'center',formatter:picPathFormatter},
	        {field:'partyName',title:locale("photo.list.th.patryName"),width:80},
	        {field:'customerName',title:locale("photo.list.th.customerName"),width:80},
	        {field:'shopName',title:locale("photo.list.th.shopName"),width:80},
	        {field:'address',title:locale("photo.list.th.address"),width:100},
	        {field:'coordinate',title:locale("photo.list.th.longitudeandlatitude"),width:200},
	        {field:'datatime',title:locale("photo.list.th.datadate"),width:100},
	        {field:'userName',title:locale("photo.list.th.userName"),width:80},
	        {field:'status',title:locale("photo.list.th.status"),width:70,formatter:picStatus}
	    ]],
	    toolbar:'#stargettb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
	initWindow();
});
function enableBt(){
	$("#serachBt").linkbutton("enable");
}
function initWindow(){
	$(".picview").remove().appendTo("body");//把这家伙移到body下属，这样保证其定位坐标是依据于body的。
}
function doSearch(){
	$("#serachBt").linkbutton("disable");
	var searchParty = $("#searchParty").val();
	var searchCustomer = $("#searchCustomer").val();
	var searchShop = $("#searchShop").val();
	var searchDate = $("#searchDate").datebox("getValue");
	
	$("#photoListTable").datagrid({
		queryParams:{
			searchParty:searchParty,
			searchCustomer:searchCustomer,
			searchShop:searchShop,
			searchDate:searchDate
		}
	});
}
function picPathFormatter(value,item,index){
//	'+baseUrl +'
	return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.serverId+'"><img class="tcl-gridcell-thumbnail" src="/upload/images/topsale/'+item.serverId+'"/></a>';
}
function onPicHover(event){
	event=event||window.event;
	var $obj=$(event.currentTarget||event.target);//.attr('href')
	//显示大图片
	$(".picview img").attr("src",$obj.attr("href"));
	var top=$obj.find("img").offset().top-$obj.height();
	$(".picview").css({left:$obj.offset().left+$obj.width(),top:$obj.offset().top-$obj.height()}).show();
	if($(".picview").offset().top+$(".picview").height()>$(document).height()){
		//边界检测
		var updatetop=$(".picview").offset().top-($(".picview").offset().top+$(".picview").height()-$(document).height());
		$(".picview").css({
			top:updatetop>0?updatetop:0
		});
	}
}
function picBlur(event){
	$(".picview").hide();
}

function picStatus(value,item,index){
	if(value=="1"){
		return "<span style='color:blue;'>Success</span>";
	}else if(value=="-1"){
		return "<span style='color:red;'>Fail</span>";
	}
}