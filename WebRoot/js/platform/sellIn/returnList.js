/**
 * 门店管理
 */
$(function(){
	$("#returnListTable").datagrid({
		title:locale("return.title"),
		url:baseUrl + 'platform/selectReturn.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		nowrap:false,
		iconCls:'icon-large-table',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'countryId',title:locale("district.countryId"),width:100,hidden:true},
	        {field:'countryName',title:locale("statement.country"),width:100},
	        {field:'partyName',title:locale("shop.list.th.party"),width:100},
	        {field:'customerCode',title:locale("customer.list.th.customerCode"),width:100},
	        {field:'customerName',title:locale("customer.list.th.name"),width:100},
	        {field:'productLine',title:locale("product.list.th.line"),width:100},
	        {field:'model',title:locale("barcode.list.th.model"),width:100},
	        {field:'quantity',title:locale("sale.list.th.quantity"),width:100},
	        {field:'datadate',title:locale("sample.list.th.datadate"),width:100}
	    ]],
	    toolbar:'#shoptb',
	    onHeaderContextMenu:onEasyGridHeadMenu,
	    onLoadSuccess:enableBt
	});
	initComponents();
});
function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}
//创建WINDOW
function initComponents(){
	initCountry();
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
	
	if($("#searchLine")){
		$("#searchLine").combobox("clear");
		$("#searchLine").combobox('reload',baseUrl + 'platform/selectLine.action');
		$("#searchLine").combobox("setValue",'');
	}
	
}

function initDataList(callback,shopId,customerId,partyId){
		var parm = "";
		if(shopId != null){
			parm += "&shopId="+shopId;
		}
		if(customerId != null){
			parm += "&customerId="+customerId;
		}

		if(partyId != null){
			parm += "&partyId="+partyId;
		}
		var url = baseUrl + "platform/loadSalersData.action?1=1"+parm;
		$.ajax({
			url: url
		}).success(function(data){
			if(data){
				$("#dl_bussinessers").datalist({
					data:data.bussinessers,
					checkbox: true,
				    singleSelect:false,
				    checkOnSelect:true,
				    lines: true
				});
				$("#dl_salers").datalist({
					data:data.salers,
				    checkbox: true,
				    singleSelect:false,
				    checkOnSelect:true,
				    lines: true
				});
				$("#dl_supervisors")
				.datalist({
					data:data.supervisors,
					checkbox: true,
				    singleSelect:false,
				    checkOnSelect:true,
				    lines: true
				});
				if(callback){
					callback();
				}
			}
		}).error(function(){
		});
}
function showAddWin(){
	showLoading();
	//loadPartyCombo($("#partyId"),$(".tcladdwin input[name='customer']").val());
	$('#addShopForm input[name=switchSign]').val("add");
	$('.tcladdwin input[name=editId]').val("");
	$('.tcladdwin form').form("reset");
	initDataList(function(){
		switchOnOff("add",$("#addShopForm"),$("#submitBtn"));
		concealLoading();
	},null,null,null);
	$("#status").combobox('setValue',1);
	$('#addShopWin').siblings(".panel-header").find(".panel-title").text(locale("shop.window.add"));
	$('#addShopWin').window('center').window('open');//.window({title:locale("shop.window.add")})
}
function validate(){
	return $("#addShopForm").form("validate");
}
function submitForm(){
	//添加or编辑
	var id=$('#addShopForm input[name=editId]').val();
	//把选择的业务员和促销员给传到后台处理。
	var sel_a=$("#dl_bussinessers").datalist('getChecked');//选中的业务员对象
	var sel_b=$("#dl_salers").datalist('getChecked');//选中的促销员对象
	var sel_c=$("#dl_supervisors").datalist('getChecked');//选中的督导员对象
	var bussinessers=[];var salers=[];var supervisors=[];
	$.each(sel_a,function(index,item){
		bussinessers.push(sel_a[index]["id"]);//业务员
	});
	$.each(sel_b,function(index,item){
		salers.push(sel_b[index]["id"]);//促销员
	});
	$.each(sel_c,function(index,item){
		supervisors.push(sel_c[index]["id"]);//促销员
	});
	
	/*if(bussinessers.length > 1){
		showMsg(locale("shop.validate.error.bussinessers"));
		return;
	}else if(supervisors.length > 1){
		showMsg(locale("shop.validate.error.supervisors"));
		return;
	}else{*/
		$('#addShopForm').form("submit",{
			url:baseUrl + 
			($.trim(id)!=""?"platform/editShop.action":"platform/addShop.action")
			+"?salers="+salers.join(';')
			+"&businessers="+bussinessers.join(';')
			+"&supervisors="+supervisors.join(';'),
			onSubmit: function(param){
				if(validate()){
					return true;
				}else{
					showMsg(locale("alert.validate.fail"));
					return false;
				}
			},
			success:function(data){
				if(data != ""){
					var result=eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#returnListTable").datagrid('reload');
						$('#addShopWin').window('close');
					}else{
						showMsg(result.msg);
					}
				}else{
					showMsg(locale(result.msg),locale("alert.title.error"),"error");
				}
			}
		});
	}
/*}*/
function clearForm(){
	$('#addShopWin').window('close');
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
	var searchLine = $("#searchLine").combobox("getValues");
	var line=JSON.stringify(searchLine);
	line=line.substring(1,line.length-1);
	 
	$("#returnListTable").datagrid({
		queryParams:{
			searchCountry:searchCountry,
			beginDate:beginDate,
			endDate:endDate,
			searchPatry:searchPatry,
			searchCustomer:searchCustomer,
			searchLine:line
		}
	});
	
}

function doSearchBus(){
	var param = {};
	var searchBus=$("#searchBus").val();
	param.searchBus = searchBus;
	$.post(
		baseUrl + "platform/SalersData.action",
		param,
		function(data){
			var shopId='';
			var row=$("#returnListTable").datagrid('getSelections');
			for ( var j = 0; j < row.length; j++) {
				shopId=row[j].shopId;
			}
			$.ajax({
				url:baseUrl + "platform/getShopUserRelations?shopId="+shopId
			}).success(function(list){
				if(list && list.length>0){
					$.each(list,function(index,item){
						if(item.salerType==0){
							var rows=$("#dl_bussinessers").datalist('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].id==item.userLoginId){
									$("#dl_bussinessers").datalist('checkRow',i);
									break;
								}
							}
						}
					});
				}
			});
			if(data){
				$("#dl_bussinessers").datalist({
					data:data.bussinessers,
					checkbox: true,
				    singleSelect:false,
				    checkOnSelect:true,
				    lines: true
				});
			}
		}
		);
}

function doSearchSal(){
	var param = {};
	var searchSal=$("#searchSal").val();
	param.searchSal = searchSal;
	var shopId='';
			var row=$("#returnListTable").datagrid('getSelections');
			for ( var j = 0; j < row.length; j++) {
				shopId=row[j].shopId;
			}
			$.post(
					baseUrl + "platform/SalersData.action?shopId="+shopId,
					param,
					function(data){
						$.ajax({
							url:baseUrl + "platform/getShopUserRelations?shopId="+shopId
						}).success(function(list){
							if(list && list.length>0){
								$.each(list,function(index,item){
									if(item.salerType==1){
										var rows=$("#dl_salers").datalist('getRows');
										for(var i=0;i<rows.length;i++){
											if(rows[i].id==item.userLoginId){
												$("#dl_salers").datalist('checkRow',i);
												break;
											}
										}
									}
								});
							}
						});
			if(data){
				$("#dl_salers").datalist({
					data:data.salers,
					checkbox: true,
				    singleSelect:false,
				    checkOnSelect:true,
				    lines: true
				});
			}
		}
		);
}


function doSearchSup(){
	var param = {};
	var searchSup=$("#searchSup").val();
	param.searchSup = searchSup;
	$.post(
		baseUrl + "platform/SalersData.action",
		param,
		function(data){
			var shopId='';
			var row=$("#returnListTable").datagrid('getSelections');
			for ( var j = 0; j < row.length; j++) {
				shopId=row[j].shopId;
			}
			$.ajax({
				url:baseUrl + "platform/getShopUserRelations?shopId="+shopId
			}).success(function(list){
				if(list && list.length>0){
					$.each(list,function(index,item){
						if(item.salerType==2){
							var rows=$("#dl_supervisors").datalist('getRows');
							for(var i=0;i<rows.length;i++){
								if(rows[i].id==item.userLoginId){
									$("#dl_supervisors").datalist('checkRow',i);
									break;
								}
							}
						}
					});
				}
			});
			if(data){
				$("#dl_supervisors").datalist({
					data:data.supervisors,
					checkbox: true,
				    singleSelect:false,
				    checkOnSelect:true,
				    lines: true
				});
			}
		}
		);
}




function autoSelect(shopId,callback){
	$("#dl_bussinessers").datalist('clearChecked');
	$("#dl_salers").datalist('clearChecked');
	$("#dl_supervisors").datalist('clearChecked');
	$.ajax({
		url:baseUrl + "platform/getShopUserRelations?shopId="+shopId
	}).success(function(list){
		if(list && list.length>0){
			$.each(list,function(index,item){
				if(item.salerType==0){
					//业务员，item.userLoginId
					var rows=$("#dl_bussinessers").datalist('getRows');//本来应该使用getRowIndex，但此方法不起作用，怀疑是easyui的坑
					for(var i=0;i<rows.length;i++){
						if(rows[i].id==item.userLoginId){
							$("#dl_bussinessers").datalist('checkRow',i);
							break;
						}
					}
				}else if(item.salerType==1){
					//促销员
					var rows=$("#dl_salers").datalist('getRows');
					for(var i=0;i<rows.length;i++){
						if(rows[i].id==item.userLoginId){
							$("#dl_salers").datalist('checkRow',i);
							break;
						}
					}
				}else if(item.salerType=2){
					//督导员
					var rows=$("#dl_supervisors").datalist('getRows');
					for(var i=0;i<rows.length;i++){
						if(rows[i].id==item.userLoginId){
							$("#dl_supervisors").datalist('checkRow',i);
							break;
						}
					}
				}
			});
		}
		if(callback){
			callback();
		}
	});
}
//显示编辑窗口
function onEdit(index){
	showLoading();
	$('#addShopForm input[name=switchSign]').val("edit");
	//获取并填充数据
	var row=$("#returnListTable").datagrid('getRows')[index];
	$('#addShopForm').form('load',row);
	editDistrictComponents($("#countryId"),$("#partyId"),$("#provinceId"),$("#cityId"),$("#countyId"),$("#townId"),$("#level"),row);
	$('#addShopForm input[name=editId]').val(row.shopId);
	//$("#customer").combobox('setValue',row.customerId);
	//$("#partyId").combobox("setValue", row.partyId).combobox("setText", row.partyName);

	//$("#partyId").combobox('setValue',row.partyName);
	//显示窗口.window({title:locale("shop.window.edit")})
	$('#addShopWin').siblings(".panel-header").find(".panel-title").text(locale("shop.window.edit"));
	$('#addShopWin').window('center').window('open');
	initDataList(function(){
		autoSelect(row.shopId,function(){
			switchOnOff("edit",$("#addShopForm"),$("#submitBtn"));
			concealLoading();
		});
	},row.shopId,row.customerId,row.partyId);
	
}
//删除门店
function onDelete(index){
	//获取并填充数据
	var row=$("#returnListTable").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
	    	$.ajax({
	    		url:baseUrl + "platform/deleteShop.action",
	    		type:"POST",
	    		data:{shopId:row.shopId}
	    	}).success(function(ret){
	    		if(ret != null){
	    			var result = eval('('+ret+')');
	    			if(result.success){
	    				showMsg(locale("alert.success"),locale("alert.title"),"info");
	    				$("#returnListTable").datagrid('reload');
	    			}else{
	    				showMsg(locale("alert.error"),locale("alert.title"),"info");
	    			}
	    		}else{
	    			showMsg(locale("alert.error"),locale("alert.title"),"info");
	    		}
	    	});
	    }
	});
	
}
//导入数据
function importReturn(){
	showImportWin(locale("import.return"),baseUrl + "platform/importReturn.action");
}




//编辑级联框赋值
function editDistrictComponents($country,$partyId,$province,$city,$county,$town,$level,row){
	if($country){
		var countryId = $country.combobox("getValue");
		$province.combobox('reload',baseUrl + 'platform/getProvinceList.action?countryId='+countryId);
		$province.combobox("setValue",row.provinceId);
		
		$partyId.combobox('reload',baseUrl + 'platform/loadPartyListData.action?countryId='+countryId);
		$partyId.combobox("setValue",row.partyId);
		
		$("#customer").combobox('reload',baseUrl + 'platform/loadCustomerByParty.action?countryId='+countryId+'');
		$("#customer").combobox("setValue",row.customerId);
		
		if($province){
			var provinceId = $province.combobox("getValue");
			$city.combobox('reload',baseUrl + 'platform/getCityList.action?provinceId='+provinceId);
			$city.combobox("setValue",row.cityId);
			if($city){
				var cityId = $city.combobox("getValue");
				$county.combobox('reload',baseUrl + 'platform/getCountyList.action?cityId='+cityId);
				$county.combobox("setValue",row.countyId);
				if($county){
					var countyId = $county.combobox("getValue");
					$town.combobox('reload',baseUrl + 'platform/getTownList.action?countyId='+countyId);
					$town.combobox("setValue",row.townId);
				}
			}
		}
	}
}

//查看
function onView(index){
	showLoading();
	$('#addShopForm input[name=switchSign]').val("view");
	//获取并填充数据
	var row=$("#returnListTable").datagrid('getRows')[index];
	$('#addShopForm').form('load',row);
	editDistrictComponents($("#countryId"),$("#partyId"),$("#provinceId"),$("#cityId"),$("#countyId"),$("#townId"),$("#level"),row);
	//$("#customer").combobox('setValue',row.customerId).combobox("setText", row.customerName);
	//$("#partyId").combobox("setValue", row.partyId).combobox("setText", row.partyName);
	//$("#partyId").combobox('setValue',row.partyName);
	//显示窗口.window({title:locale("shop.window.edit")})
	$('#addShopWin').siblings(".panel-header").find(".panel-title").text(locale("shop.window.view"));
	viewDataListInit(function(){
		autoSelect(row.shopId,function(){
			switchOnOff("view",$("#addShopForm"),$("#submitBtn"));
			concealLoading();
		});
	},row.shopId,row.customerId,row.partyId);
	$('#addShopWin').window('center').window('open');
}

function switchOnOff(switchSign,$form,$subBtn){
	if("view" == switchSign){
		$subBtn.hide();
		$form.find('input,select').attr('readonly',true);
		$form.find('input,select').attr('disabled',true);
		$form.find("input[editable='false']").each(function(){
	    	$(this).combobox("disable");
		});
	}else{
		$subBtn.show();
		$form.find('input,select').attr('readonly',false);
		$form.find('input,select').attr('disabled',false);
		$form.find("input[editable='false']").each(function(){
	    	$(this).combobox("enable");
		});
	}
}

function viewDataListInit(callback,shopId,customerId){
	var parm = "";
	if(shopId != null){
		parm += "&shopId="+shopId;
	}
	if(customerId != null){
		parm += "&customerId="+customerId;
	}
	var url = baseUrl + "platform/loadSalersData.action?1=1"+parm;
	
	$.ajax({
		url: url
	}).success(function(data){
		if(data){
			$("#dl_bussinessers").datalist({
				data:data.bussinessers,
				checkbox: true,
			    singleSelect:false,
			    checkOnSelect:false,
			    lines: true
			});
			$("#dl_salers").datalist({
				data:data.salers,
			    checkbox: true,
			    singleSelect:false,
			    checkOnSelect:false,
			    lines: true
			});
			$("#dl_supervisors")
			.datalist({
				data:data.supervisors,
				checkbox: true,
			    singleSelect:false,
			    checkOnSelect:false,
			    lines: true
			});
			if(callback){
				callback();
			}
		}
	}).error(function(){
	});
}

function showLoading(){
	var url = baseUrl + "images/login/loading.gif";
	$("#loadGif").attr("src",url);
	$('#load-layout').show();
}

function concealLoading(){
	$('#load-layout').hide();
}


function downName(){
	var url = baseUrl + "platform/ExportShopName.action";
	location.href = url;
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







//格式化按钮
function Formatter_x(value,item,index){
	return '<div class="tcl-btn-group">'+
		'<a href="javascript:void(0);" class="tcl-view" onclick="onView(\''+index+'\')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.view")+'</a>'+
		'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a></div>';
}