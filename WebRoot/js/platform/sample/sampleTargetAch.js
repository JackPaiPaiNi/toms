/**
 * 样机数据表管理
 */
var nowDate = new Date();
		
		var y = nowDate.getFullYear();
	
		var m = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1)
	
	 	: nowDate.getMonth() + 1;
	
		var d = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate
	
	 	.getDate();
	
	 	var dateStr = y + "-" + m + "-" + d; 
//?beginDate="+y + "-" + m + "-01"+"&endDate="+dateStr+"
$(function(){
	$("#samplesListTable").datagrid({
		title:locale("sample.targetAch.list"),
		url:baseUrl + "platform/selectSampleAch.action",
		pagination:false,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	              {field: 'countryName',  title: locale("district.countryName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	              {field: 'partyName',  title: locale("shop.list.search.party"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	              {field: 'partyId',  title: locale("shop.list.search.party"),sortable:true,resizable:true,halign:'center',align:'left',width:30,hidden:true},
	              {field: 'countryId',  title: locale("district.countryId"),sortable:true,resizable:true,halign:'center',align:'left',width:30,hidden:true},
	              {field: 'customerName',  title: locale("sample.list.th.customerName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'shopName',  title: locale("sample.list.th.shopName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	              {field: 'shopId',  title: locale("shop.list.th.id"),sortable:true,resizable:true,halign:'center',align:'left',width:30,hidden:true},
	              {field: 'productLine',  title: locale("product.list.th.line"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'sampleTarget',  title: locale("sampleTarget.sampleTarget"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	              {field: 'sampleQty',  title: locale("sampleTarget.sampleQty"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	              {field: 'ach',  title: locale("sampleTarget.ach"),sortable:true,resizable:true,halign:'center',align:'right',width:30},
	            //  {field: 'lastQty',  title: locale("sampleTarget.changeQty"),sortable:true,resizable:true,halign:'center',align:'right',width:30},

	              
	              
	              //  {field: 'sampleTargetTTL',  title: locale("sampleTarget.sampleTargetTTL"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	            //  {field: 'sampleQtyTTL',  title: locale("sampleTarget.sampleQtyTTL"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	              {field: 'changeQty',  title: locale("sampleTarget.changeQty"),sortable:true,resizable:true,halign:'center',align:'right',width:30},
/*	              {field: 'maxQty',  title: locale("sampleTarget.maxQty"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
*/	              {field: 'saleQty',  title: locale("sampleTarget.saleQty"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	              {field: 'soAch',  title: locale("sampleTarget.soAch"),sortable:true,resizable:true,halign:'center',align:'right',width:30}, 
	              {field: 'dataDate',  title: locale("sample.list.th.datadate"),sortable:true,resizable:true,halign:'center',align:'left',width:30}
	             
	    ]],
	    toolbar:'#stargettb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
	initWindow();
	initCountry();
});


//新增
function showSumDiv(){
	$("#samplesSumListTable").datagrid({
		url:baseUrl + "platform/selectSampleAchSumListByLine.action",
		pagination:false,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	              {field: 'productLine',  title: locale("product.list.th.line"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'sampleTarget',  title: locale("sampleTarget.sampleTarget"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	              {field: 'sampleQty',  title: locale("sampleTarget.sampleQty"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	              {field: 'ach',  title: locale("sampleTarget.ach"),sortable:true,resizable:true,halign:'center',align:'right',width:30},
	              {field: 'changeQty',  title: locale("sampleTarget.changeQty"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	              {field: 'saleQty',  title: locale("sampleTarget.saleQty"),sortable:true,resizable:true,halign:'center',align:'right',width:30},  
	              {field: 'soAch',  title: locale("sampleTarget.soAch"),sortable:true,resizable:true,halign:'center',align:'right',width:30}, 
	              {field: 'dataDate',  title: locale("sample.list.th.datadate"),sortable:true,resizable:true,halign:'center',align:'left',width:30}
	             
	 
	    ]]
	});

	$('#sumDiv').window({
	    left:300,
		title:locale("sampleTarget.sum")}).window('open');
}


function initWindow(){
	init($("#tcl_country"),$("#searchPatry"),$("#searchShop"),null,my_login_id);
	
	if($("#searchLine")){
		$("#searchLine").combobox({
			textField:"catenaName",
			valueField:"catena",
		    onChange :function(newValue,oldValue){
			return ;
		    }
		});
		
		
	}
	
	if($("#searchLine")){
		$("#searchLine").combobox("clear");
		$("#searchLine").combobox('reload',baseUrl + 'platform/selectLine.action');
		$("#searchLine").combobox("setValue",'');
	}
	

	
}
function enableBt(){
	$("#serachBt").linkbutton("enable");
}
function doSearch(){
	$("#serachBt").linkbutton("disable");
	var searchCountry = $("#tcl_country").combobox("getValue");
	var datadate = $("#datadate").val();
	var beginDate="";
	var endDate="";
	if(datadate!=null && datadate!=""){
		var days=datadate.split("-");
		beginDate=days[0]+"-"+days[1]+"-01";
		endDate=days[0]+"-"+days[1]+"-"+getLastDay(days[0],days[1]);
	}else{
		beginDate=y + "-" + m + "-01";
		endDate=dateStr;
	}
	var searchPatry = $("#searchPatry").combobox("getValue");
	var searchCustomer = $("#searchCustomer").combobox("getValue");
	var searchShop = $("#searchShop").combobox("getValue");
	//var searchModel = $("#searchModel").val();
	//var searchHqModel = $("#searchHqModel").val();
	var searchLine = $("#searchLine").combobox("getValues");
	var line=JSON.stringify(searchLine);
	line=line.substring(1,line.length-1);
	 if( line=="\"\""){
		 line="";
	 }
	 line=line.replace("\"\",", "");
	 
	 line=line.replace(/\"/g, "");
	
	var ach= $("#ach").val();
	var soAch= $("#soAch").val();
	$("#samplesListTable").datagrid({
		queryParams:{
			searchCountry:searchCountry,
			beginDate:beginDate,
			endDate:endDate,
			searchPatry:searchPatry,
			searchCustomer:searchCustomer,
			searchShop:searchShop,
			//searchModel:searchModel,
			ach:ach,
			soAch:soAch,
			//searchHqModel:searchHqModel,
			searchLine:line
		}
	});
	
	
	$("#samplesSumListTable").datagrid({
		queryParams:{
			searchCountry:searchCountry,
			beginDate:beginDate,
			endDate:endDate,
			searchPatry:searchPatry,
			searchCustomer:searchCustomer,
			searchShop:searchShop,
			ach:ach,
			soAch:soAch,
			searchLine:line
		}
	});
}


function initCountry(){	
	$.ajax({
		url:baseUrl + "platform/onloadCountry.action",
		type:"POST",
		data:{"id":my_login_id},
	}).success(function(data){
		if(data.rows.length==1){
			$("#tcl_country").combobox("setValue", data.rows[0].countryId).combobox("setText", data.rows[0].countryName);
		}else{
			data.rows.unshift({"countryName":"ALL","countryId":""});
			$("#tcl_country").combobox({			
				data:data.rows,
				textField:"countryName",
				valueField:"countryId",
			});
		}
		
		
	});
}




function getLastDay(year, month){    
    var dt = new Date(year, month - 1, '01');    
    dt.setDate(1);    
    dt.setMonth(dt.getMonth() + 1);    
    cdt = new Date(dt.getTime()-1000*60*60*24);    
    return cdt.getDate();  
    //alert(cdt.getFullYear()+"年"+(Number(cdt.getMonth())+1)+"月月末日期:"+cdt.getDate()+"日");     
}  


function exportTargetAch(){
	var searchCountry = $("#tcl_country").combobox("getValue");
	var datadate = $("#datadate").val();
	var beginDate="";
	var endDate="";
	if(datadate!=null && datadate!=""){
		var days=datadate.split("-");
		beginDate=days[0]+"-"+days[1]+"-01";
		endDate=days[0]+"-"+days[1]+"-"+getLastDay(days[0],days[1]);
	}else{
		beginDate=y + "-" + m + "-01";
		endDate=dateStr;
	}
	var searchPatry = $("#searchPatry").combobox("getValue");
	var searchCustomer = $("#searchCustomer").combobox("getValue");
	var searchShop = $("#searchShop").combobox("getValue");
	var searchLine = $("#searchLine").combobox("getValues");
	var line=JSON.stringify(searchLine);
	line=line.substring(1,line.length-1);	
	 if( line=="\"\""){
		 line="";
	 }
	 line=line.replace("\"\",", "");
	 
	 line=line.replace(/\"/g, "");
	
	//var searchModel = $("#searchModel").val();
	var soAch= $("#soAch").val();
	var ach= $("#ach").val();
	var str="?searchCountry="+searchCountry+"&beginDate="+beginDate+"&endDate="+endDate
	+"&searchPatry="+searchPatry+"&searchCustomer="+searchCustomer+"&searchShop="+searchShop
	+"&soAch="+soAch+"&ach="+ach+"&searchLine="+line;

	var url = baseUrl + "platform/exportTargetAch.action"+str;
	
	
	
	location.href = url;
}




function init($country,$partyId,$shopId,$table,_searchFlag){
	if(!$country)
		return;//至少得有国家一级
	
	//自定义 一个 空的数据  用于置空级联数据
	var data = [];
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
	    		$partyId.combobox('reload',baseUrl + 'platform/loadPartyListData.action?countryId='+countryId+'&random='+Math.random());
	    		$partyId.combobox("setValue",'');
	    	}
	    	var	partyId= $partyId.combobox("getValue");
	    	
	    	if($shopId){
	    		$shopId.combobox("clear");
	    		$shopId.combobox('reload',baseUrl + 'platform/loadShopByParty.action?countryId='+countryId+'&partyId='+partyId+'&random='+Math.random());
	    		$shopId.combobox("setValue",'');
	    	}
			
	    	
	    	
	    	
	    	if($("#searchCustomer")){
	    		$("#searchCustomer").combobox("clear");
	    		$("#searchCustomer").combobox('reload',baseUrl + 'platform/loadCustomerByParty.action?countryId='+countryId+'&random='+Math.random());
	    		$("#searchCustomer").combobox("setValue",'');
	    		
	    	}
	    		
	    	
	    
	    	if($table){
	    		var qryParms=$table.datagrid("options").queryParams;
	    		if(countryId == ""){
	    			countryId = null;
	    		}
	    		qryParms.countryId = countryId;
	    		qryParms.partyId = $partyId.combobox("getValue");
	    		$table.datagrid({queryParams:qryParms});//重新加载
	    	}
	    }	
	});
	

	
	if($partyId){
		$partyId.combobox({
			valueField:'partyId',
		    textField:'partyName',
		    onChange :function(newValue,oldValue){
		    	var countryId= $country.combobox("getValue");
		    	var	partyId= $partyId.combobox("getValue");
		    	if($shopId){
		    		$shopId.combobox("clear");
		    		$shopId.combobox('reload',baseUrl + 'platform/loadShopByParty.action?countryId='+countryId+'&partyId='+partyId+'&random='+Math.random());
		    		$shopId.combobox("setValue",'');
		    	}
		    	

		    }
		});
	}
	
	if($shopId){
		$shopId.combobox({
			valueField:'shopId',
		    textField:'shopName',
		    onChange :function(newValue,oldValue){
		    	return;
		    }
		});
	}
	
	
	if($("#searchCustomer")){
		$("#searchCustomer").combobox({
			textField:"customerName",
			valueField:"customerId",
		    onChange :function(newValue,oldValue){
			return ;
		    }
		});
		
		
	}
	

	
	
}

