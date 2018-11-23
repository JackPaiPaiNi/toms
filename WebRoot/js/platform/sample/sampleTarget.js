/**
 * 样机数据表管理
 */
$(function(){
	$("#samplesListTable").datagrid({
		title:locale("sample.target.list"),
		url:baseUrl + "platform/selectSampleTarget.action",
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	              {field: 'countryName',  title: locale("district.countryName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	              {field: 'partyName',  title: locale("shop.list.search.party"),sortable:true,resizable:true,halign:'center',align:'left',width:50},
	              {field: 'partyId',  title: locale("shop.list.search.party"),sortable:true,resizable:true,halign:'center',align:'left',width:30,hidden:true},
	              {field: 'countryId',  title: locale("district.countryId"),sortable:true,resizable:true,halign:'center',align:'left',width:30,hidden:true},
	              {field: 'customerName',  title: locale("sample.list.th.customerName"),sortable:true,resizable:true,halign:'center',align:'left',width:50},  
	              {field: 'shopName',  title: locale("sample.list.th.shopName"),sortable:true,resizable:true,halign:'center',align:'left',width:50},
	              {field: 'shopId',  title: locale("shop.list.th.id"),sortable:true,resizable:true,halign:'center',align:'left',width:30,hidden:true},
	              {field: 'productLine',  title: locale("product.list.th.line"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	             /* {field: 'hqModel',  title: locale("sample.list.th.hqModel"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'model',  title: locale("sample.list.th.model"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	         */     {field: 'quantity',  title: locale("sampleTarget.sampleTarget"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	            //  {field: 'targetTTL',  title: locale("sampleTarget.sampleTargetTTL"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'minDate',  title: locale("sampleTarget.first"),sortable:true,resizable:true,halign:'center',align:'left',width:50},  
	              /*{field: 'userName',  title: locale("sample.list.th.userName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'dataDate',  title: locale("sample.list.th.datadate"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	*/		    //  {field:'op',title:locale("toolbar.edit"),width:80,formatter:opFormatter}
	             
	    ]],
	    toolbar:'#stargettb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
	
	
	
	
	initWindow();
	initCountry();
});
function initWindow(){
	init($("#countryId"),$("#partyId"),$("#shopId"),$("#model"),null,my_login_id);
	init($("#tcl_country"),$("#searchPatry"),$("#searchShop"),null,null,my_login_id);
	
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
	var beginDate ="";
	var endDate = "";
	if($("#beginDate").val()!=null && $("#beginDate").val()!=""){
		beginDate = $("#beginDate").val()+"-01";
	}
	if($("#beginDate").val()!=null && $("#beginDate").val()!=""){
		endDate = $("#beginDate").val()+"-31";
	}
	var searchPatry = $("#searchPatry").combobox("getValue");
	var searchCustomer = $("#searchCustomer").combobox("getValue");
	var searchShop = $("#searchShop").combobox("getValue");
	var searchModel = $("#searchModel").val();
	var searchHqModel = $("#searchHqModel").val();
	var searchLine = $("#searchLine").combobox("getValues");
	var line=JSON.stringify(searchLine);
	line=line.substring(1,line.length-1);
	
	 if( line=="\"\""){
		 line="";
	 }
	 line=line.replace("\"\",", "");
	 
	 line=line.replace(/\"/g, "");
	$("#samplesListTable").datagrid({
		queryParams:{
			searchCountry:searchCountry,
			beginDate:beginDate,
			endDate:endDate,
			searchPatry:searchPatry,
			searchCustomer:searchCustomer,
			searchShop:searchShop,
			searchModel:searchModel,
			searchHqModel:searchHqModel,
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
			searchModel:searchModel,
			searchHqModel:searchHqModel,
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


function importSampleTarget(){
	showImportWin(locale("sample.target.import"),baseUrl + "platform/importSampleTarget.action");
}


function exportSampleTTemplate(){
	var url = baseUrl + "platform/exportSampleTTemplate.action";
	location.href = url;
}








//新增
function showAddWin(){
	$('#addSampleTargetForm #branchModel').textbox("enable");
	$('#addSampleTargetForm #price').textbox("enable");
	$('#addSampleTargetForm input[name=editId]').val("");
	
	$('#addSampleTargetForm').form("reset");
	$('#addModelmapWin').window({title:locale("sampleTarget.window.add")}).window('center').window('open');
}


//新增
function showSumDiv(){
	$("#samplesSumListTable").datagrid({
/*		title:locale("sample.target.list"),
*/		url:baseUrl + "platform/selectSampleTargetSumListByLine.action",
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	                  {field: 'productLine',  title: locale("product.list.th.line"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	             /* {field: 'hqModel',  title: locale("sample.list.th.hqModel"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'model',  title: locale("sample.list.th.model"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	         */     {field: 'quantity',  title: locale("sample.list.th.quantity"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	         //     {field: 'targetTTL',  title: locale("sampleTarget.sampleTargetTTL"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'minDate',  title: locale("sampleTarget.first"),sortable:true,resizable:true,halign:'center',align:'left',width:50},  
	              /*{field: 'userName',  title: locale("sample.list.th.userName"),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
	              {field: 'dataDate',  title: locale("sample.list.th.datadate"),sortable:true,resizable:true,halign:'center',align:'left',width:30},
	*/		      
	             
	    ]]
	});

	$('#sumDiv').window({
		left: ($(window).width() - 450) * 0.5,
		title:locale("sampleTarget.sum")}).window('center').window('open');
}

//新增保存
function submitForm(){
	if(validate()){
		//添加or编辑
		var id=$('#addSampleTargetForm input[name=editId]').val();
		if($.trim(id)!=""){
			//编辑
			$('#addSampleTargetForm').ajaxSubmit({
				url:baseUrl + "platform/editSampleTargetById.action?id="+id,
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#samplesListTable").datagrid('reload');
						$('#addModelmapWin').window('close');
					}else{
						showMsg(json.msg);
					}
				}
			});
		}else{
			$('#addSampleTargetForm').ajaxSubmit({
				url:baseUrl + "platform/addModelMap.action",
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#samplesListTable").datagrid('reload');
						$('#addModelmapWin').window('close');
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
	return $("#addSampleTargetForm").form("validate");
}
//显示编辑窗口
function onEdit(index){
	//获取并填充数据
	var row=$("#samplesListTable").datagrid('getRows')[index];
	$('#addSampleTargetForm').form('load',row);
	$('#addSampleTargetForm input[name=editId]').val(row.id);
	//$('#addBarcodeForm #barcode').textbox("disable");
	//显示窗口
	$('#addModelmapWin').window({title:locale("sampleTarget.window.edit")});
	$('#addModelmapWin').window('center');
	$('#addModelmapWin').window('open');
}

//删除
function onDelete(index){
	var row = $("#samplesListTable").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "platform/deleteSampleTarget.action",
				type:"POST",
				data:{"id":row.id},
				success:function(data){
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#samplesListTable").datagrid('reload');
					}else{
						showMsg(json.msg);
					}
				}
			});
	    }
	});
}

function init($country,$partyId,$shopId,$model,$table,_searchFlag){
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
			
	    	
	    	if($model){
	    		$model.combobox("clear");
	    		$model.combobox('reload',baseUrl + 'platform/loadModelByParty.action?countryId='+countryId+'&random='+Math.random());
	    		$model.combobox("setValue",'');
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
	
	if($model){
		$model.combobox({
			valueField:'model',
		    textField:'model',
		    onChange :function(newValue,oldValue){
		    	return;
		    }
		});
	}
	
	
}

function exportTargetAch(){
	var searchCountry = $("#tcl_country").combobox("getValue");
	var beginDate ="";
	var endDate = "";
	if($("#beginDate").val()!=null && $("#beginDate").val()!=""){
		beginDate = $("#beginDate").val()+"-01";
	}
	if($("#beginDate").val()!=null && $("#beginDate").val()!=""){
		endDate = $("#beginDate").val()+"-31";
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
	var str="?searchCountry="+searchCountry+"&beginDate="+beginDate+"&endDate="+endDate
	+"&searchPatry="+searchPatry+"&searchCustomer="+searchCustomer+"&searchShop="+searchShop
	+"&searchLine="+line;
	var url = baseUrl + "platform/exportSampleTarget.action"+str;
	
	
	
	location.href = url;
}

