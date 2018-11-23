/**
 * 门店上传图片管理
 */
$(function(){
	$("#photoListTable").datagrid({
		title:locale("photoStore.list.title"),
		url:baseUrl + 'platform/loadShopPhotosListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		nowrap:false,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	            {field:'userId',title:locale("shop.list.th.userId"),width:100,hidden:true},
	  	        {field:'shopId',title:locale("shop.list.th.shopId"),width:100,hidden:true},
	  	        {field:'shopName',title:locale("shop.list.th.shopName"),width:200},
	  	        {field:'serverId',title:locale("shop.list.th.serverId"),width:100,hidden:true},
	  	        {field:'overall',title:locale("shop.list.th.overall"),width:200,align:'center',formatter:picOverAll},
	  	        {field:'middle',title:locale("shop.list.th.middle"),width:200,align:'center',formatter:picMiddle},
	  	        {field:'lf',title:locale("shop.list.th.lf"),width:200,align:'center',formatter:picLeft},
	  	        {field:'rg',title:locale("shop.list.th.rg"),width:200,align:'center',formatter:picRight},
	  	        {field:'otherOne',title:locale("shop.list.th.otherOne"),width:200,align:'center',formatter:picOtherOne},
	  	        {field:'otherTwo',title:locale("shop.list.th.otherTwo"),width:200,align:'center',formatter:picOtherTwo},
	  	        {field:'upTime',title:locale("shop.list.th.upTime"),width:200},
	  	        {field:'remarks',title:locale("shop.list.th.remarks"),width:200},
	       
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
	var searchShop = $("#searchShop").val();
	var searchDate = $("#searchDate").datebox("getValue");
	
	$("#photoListTable").datagrid({
		queryParams:{
			searchShop:searchShop,
			searchDate:searchDate
		}
	});
}
function picOverAll(value,item,index){
//	'+baseUrl +'
	if(item.overall==null  ||  item.overall==""){
	return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.overall+'"></a>';
	
	}else  if(item.overall!=null  &&  item.overall!=""){
	return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.overall+'"><img class="tcl-gridcell-thumbnail" src="/upload/images/topsale/'+item.overall+'"/></a>';	
	}
}
function picMiddle(value,item,index){
	if(item.middle==null  ||  item.middle==""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.middle+'"></a>';

	}else  if(item.middle!=null  &&  item.middle!=""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.middle+'"><img class="tcl-gridcell-thumbnail" src="/upload/images/topsale/'+item.middle+'"/></a>';

	}
}
function picLeft(value,item,index){
	if(item.lf==null  ||  item.lf==""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.lf+'"></a>';

	}else  if(item.lf!=null  &&  item.lf!=""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.lf+'"><img class="tcl-gridcell-thumbnail" src="/upload/images/topsale/'+item.lf+'"/></a>';

	}
}
function picRight(value,item,index){
	if(item.rg==null  ||  item.rg==""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.rg+'"></a>';

	}else  if(item.rg!=null  &&  item.rg!=""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.rg+'"><img class="tcl-gridcell-thumbnail" src="/upload/images/topsale/'+item.rg+'"/></a>';

	}
}
function picOtherOne(value,item,index){
	if(item.otherOne==null  ||  item.otherOne==""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.otherOne+'"></a>';

	}else  if(item.otherOne!=null  &&  item.otherOne!=""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.otherOne+'"><img class="tcl-gridcell-thumbnail" src="/upload/images/topsale/'+item.otherOne+'"/></a>';

	}
}
function picOtherTwo(value,item,index){
	if(item.otherTwo==null  ||  item.otherTwo==""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.otherTwo+'"></a>';

	}else  if(item.otherTwo!=null  &&  item.otherTwo!=""){
		return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="/upload/images/topsale/'+item.otherTwo+'"><img class="tcl-gridcell-thumbnail" src="/upload/images/topsale/'+item.otherTwo+'"/></a>';

	}
//	'+baseUrl +'
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


