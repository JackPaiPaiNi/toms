/**
 * 门店管理
 */
$(function(){
	$("#shopListTable").datagrid({
		title:locale("target.name"),
		url:baseUrl + 'platform/loadPCTargetData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		nowrap:false,
		iconCls:'icon-large-table',
		fitColumns:true,
		fit:true,
	    columns:[[
	    	 {field:'countryName',title:locale("district.countryName"),width:100,hidden:true},
	        {field:'shopName',title:locale("shop.list.th.name"),width:100},
	        {field:'partyName',title:locale("shop.list.th.partyName"),width:100,hidden:true},
	        {field:'customerName',title:locale("shop.list.th.customer"),width:100},
	        {field:'userName',title:locale("shop.list.th.salers"),width:100},
	         {field:'quantity',title:locale("sale.list.th.quantity"),width:100},
	         {field:'amount',title:locale("sale.list.th.amount"),width:100},
	         {field:'date',title:locale("sample.list.th.datadate"),width:100},
	       ]],
	    toolbar:'#shoptb',
	    onHeaderContextMenu:onEasyGridHeadMenu,
	    onLoadSuccess:enableBt
	});
	initComponents();
});
function enableBt(){
	$("#searchBt").linkbutton("enable");
//	initDataGridCells();
	if(!isAvailable()){
		//根据tcl-btn样式
		$(".tcl-btn").each(function(){
			$(this).remove();
		});
		$(".easyui-linkbutton").each(function(){
			var iconcls = $(this).attr("iconcls");
			if("icon-search"!=iconcls && "icon-redo"!=iconcls)
			{
				$(this).remove();
			}
		});
	}
}
//创建WINDOW
function initComponents(){
	$('#addShopForm input[name=switchSign]').val("add");
	initDistrictComponentsByshop($("#countryId"),$("#partyId"),$("#provinceId"),$("#cityId"),$("#countyId"),$("#townId"),null,my_login_id,$("#level"));
	//initDistrictComponents($("#tcl_country"),null,null,null,null,$("#shopListTable"),"all");
	initCountry();
//	shopLevel($("#level"));
	shopLevel($("#tcl_level"));

	
	$(".tcladdwin input[name='status']").combobox({
		data:[{text:locale("window.status.enable"),value:1,selected:true},{text:locale("window.status.disable"),value:0}]
	});
	
}

function initDataList(callback,shopId,customerId,partyId){
		var parm = "";
		if(shopId != null){
			parm += "&shopId="+shopId;
		}
		if(customerId != null){
			parm += "&customerId="+customerId;
		}


			parm += "&partyId="+partyId;
		
		var url = baseUrl + "platform/loadSalersData.action?1=1"+parm;
		$.ajax({
			url: url
		}).success(function(data){
			if(data){
				$("#dl_bussinessers").datalist({
					data:data.bussinessers,
					checkbox: true,
				    singleSelect:true,
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
				    singleSelect:true,
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
	//清除选中行
	$("#shopListTable").datagrid('clearSelections');
	//搜索框数据清空
	$("#searchBus").textbox('setValue','');
	$("#searchSup").textbox('setValue','');
	$("#searchSal").textbox('setValue','');
	//按钮启用
	$("#searchBusBt").linkbutton({disabled:false});
	$("#searchSupBt").linkbutton({disabled:false});
	$("#searchSalBt").linkbutton({disabled:false});
	
	showLoading();
	//loadPartyCombo($("#partyId"),$(".tcladdwin input[name='customer']").val());
	$('#addShopForm input[name=switchSign]').val("add");
	$('.tcladdwin input[name=editId]').val("");
	$('.tcladdwin form').form("reset");
//	initDataList(function(){
		switchOnOff("add",$("#addShopForm"),$("#submitBtn"));
		concealLoading();
//	},null,null,null);
	$("#status").combobox('setValue',1);
	$('#addShopWin').siblings(".panel-header").find(".panel-title").text(locale("shop.window.add"));
	$('#addShopWin').window('center').window('open');//.window({title:locale("shop.window.add")})
	clearInitData();
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
							$("#shopListTable").datagrid('reload');
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
	$("#searchBt").linkbutton("disable");
	var customerName = $("#customerName").val();
	var shopName = $("#shopName").val();
	var userName = $("#userName").val();
	var date = $("#date").val();

	$("#shopListTable").datagrid({
		queryParams:{customerName:customerName,shopName:shopName,userName:userName,date:date}
	});
}

function doSearchBus(){
	var param = {};
	var searchBus=$("#searchBus").val();
	var partyId=$("#countryId").combobox("getValue");
	param.searchBus = searchBus;
	var shopId='';
	var row=$("#shopListTable").datagrid('getSelections');
	for ( var j = 0; j < row.length; j++) {
		shopId=row[j].shopId;
	}
	$.post(
		baseUrl + "platform/SalersData.action?shopId="+shopId+"&partyId="+partyId,
		param,
		function(data){
			$.ajax({
				url:baseUrl + "platform/getShopUserRelations.action?shopId="+shopId+"&partyId="+partyId
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
				if(partyId!=''){
					if(data){
						$("#dl_bussinessers").datalist({
							data:data.bussinessers,
							checkbox: true,
						    singleSelect:false,
						    checkOnSelect:true,
						    lines: true
						});
					}
				}else{
					
				}
			}
		);
}

function doSearchSal(){
	var param = {};
	var searchSal=$("#searchSal").val();
	var partyId=$("#countryId").combobox("getValue");
	param.searchSal = searchSal;
	var shopId='';
			var row=$("#shopListTable").datagrid('getSelections');
			for ( var j = 0; j < row.length; j++) {
				shopId=row[j].shopId;
			}
			$.post(
					baseUrl + "platform/SalersData.action?shopId="+shopId+"&partyId="+partyId,
					param,
					function(data){
						$.ajax({
							url:baseUrl + "platform/getShopUserRelations.action?shopId="+shopId+"&partyId="+partyId
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
			if(partyId!=''){
				if(data){
					$("#dl_salers").datalist({
						data:data.salers,
						checkbox: true,
					    singleSelect:false,
					    checkOnSelect:true,
					    lines: true
					});
				}
			}else{
				
			}
		}
	);
}


function doSearchSup(){
	var param = {};
	var searchSup=$("#searchSup").val();
	var partyId=$("#countryId").combobox("getValue");
	param.searchSup = searchSup;
	var shopId='';
	var row=$("#shopListTable").datagrid('getSelections');
	for ( var j = 0; j < row.length; j++) {
		shopId=row[j].shopId;
	}
	$.post(
		baseUrl + "platform/SalersData.action?partyId="+partyId+"&partyId="+partyId,
		param,
		function(data){
			$.ajax({
				url:baseUrl + "platform/getShopUserRelations.action?shopId="+shopId+"&partyId="+partyId
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
			if(partyId!=''){
				if(data){
					$("#dl_supervisors").datalist({
						data:data.supervisors,
						checkbox: true,
					    singleSelect:false,
					    checkOnSelect:true,
					    lines: true
					});
				}
			}else{
				
			}
		}
	);
}




function autoSelect(countryId,shopId,callback){
//	$("#dl_bussinessers").datalist('clearChecked');
//	$("#dl_salers").datalist('clearChecked');
//	$("#dl_supervisors").datalist('clearChecked');
	$.ajax({
		url:baseUrl + "platform/getShopUserRelations.action?shopId="+shopId+"&partyId="+countryId
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
	
	//搜索框数据清空
	$("#searchBus").textbox('setValue','');
	$("#searchSup").textbox('setValue','');
	$("#searchSal").textbox('setValue','');
	//按钮启用
	$("#searchBusBt").linkbutton({disabled:false});
	$("#searchSupBt").linkbutton({disabled:false});
	$("#searchSalBt").linkbutton({disabled:false});
	
	showLoading();
	$('#addShopForm input[name=switchSign]').val("edit");
	//获取并填充数据
	var row=$("#shopListTable").datagrid('getRows')[index];
	$('#addShopForm').form('load',row);
	editDistrictComponents($("#countryId"),$("#partyId"),$("#provinceId"),$("#cityId"),$("#countyId"),$("#townId"),$("#level"),row);
	$('#addShopForm input[name=editId]').val(row.shopId);
	$('#addShopForm input[name=hisLoac]').val(row.location);
	//$("#customer").combobox('setValue',row.customerId);
	//$("#partyId").combobox("setValue", row.partyId).combobox("setText", row.partyName);

	//$("#partyId").combobox('setValue',row.partyName);
	//显示窗口.window({title:locale("shop.window.edit")})
	$('#addShopWin').siblings(".panel-header").find(".panel-title").text(locale("shop.window.edit"));
	$('#addShopWin').window('center').window('open');
	editInitDataListByCondition(function(){
		autoSelect(row.countryId,row.shopId,function(){
			switchOnOff("edit",$("#addShopForm"),$("#submitBtn"));
			concealLoading();
		});
	},row.shopId,row.customerId,row.countryId);
	
}
//删除门店
function onDelete(index){
	//获取并填充数据
	var row=$("#shopListTable").datagrid('getRows')[index];
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
	    				$("#shopListTable").datagrid('reload');
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
function importPCTarget(){
	showImportWin(locale("shop.import"),baseUrl + "platform/importPCTarget.action");
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
	//清空文本框的值
	$("#searchBus").textbox('setValue','');
	$("#searchSup").textbox('setValue','');
	$("#searchSal").textbox('setValue','');
	//按钮禁用
	$("#searchBusBt").linkbutton({disabled:true});
	$("#searchSupBt").linkbutton({disabled:true});
	$("#searchSalBt").linkbutton({disabled:true});
	
	showLoading();
	$('#addShopForm input[name=switchSign]').val("view");
	//获取并填充数据
	var row=$("#shopListTable").datagrid('getRows')[index];
	$('#addShopForm').form('load',row);
	editDistrictComponents($("#countryId"),$("#partyId"),$("#provinceId"),$("#cityId"),$("#countyId"),$("#townId"),$("#level"),row);
	//$("#customer").combobox('setValue',row.customerId).combobox("setText", row.customerName);
	//$("#partyId").combobox("setValue", row.partyId).combobox("setText", row.partyName);
	//$("#partyId").combobox('setValue',row.partyName);
	//显示窗口.window({title:locale("shop.window.edit")})
	$('#addShopWin').siblings(".panel-header").find(".panel-title").text(locale("shop.window.view"));
	initDataListByCondition(function(){
		autoSelect(row.countryId,row.shopId,function(){
			switchOnOff("view",$("#addShopForm"),$("#submitBtn"));
			concealLoading();
		});
	},row.shopId,row.customerId,row.countryId);
	$('#addShopWin').window('center').window('open');
}

function switchOnOff(switchSign,$form,$subBtn){
	if("view" == switchSign){
		$subBtn.hide();
//		$form.find('input,select').attr('readonly',true);
		$form.find('input,select').attr('disabled',true);
		$form.find("input[editable='false']").each(function(){
	    	$(this).combobox("disable");
		});
	}else{
		$subBtn.show();
//		$form.find('input,select').attr('readonly',false);
		$form.find('input,select').attr('disabled',false);
		$form.find("input[editable='false']").each(function(){
	    	$(this).combobox("enable");
		});
	}
}

function viewDataListInit(callback,shopId,customerId,countryId){
	var parm = "";
	if(shopId != null){
		parm += "&shopId="+shopId;
	}
	if(customerId != null){
		parm += "&customerId="+customerId;
	}
	if(countryId !=null){
		parm += "&partyId="+countryId;
	}
	var url = baseUrl + "platform/loadSalersData.action?1=1"+parm;
	
	$.ajax({
		url: url
	}).success(function(data){
		if(data){
			$("#dl_bussinessers").datalist({
				data:data.bussinessers,
				checkbox: true,
			    singleSelect:true,
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
			    singleSelect:true,
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














function initDistrictComponentsByshop($country,$partyId,$province,$city,$county,$town,$table,_searchFlag,$level){
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
	    	initDataList(null,null,null,countryId);
	    	if($partyId){
	    		$partyId.combobox("clear");
	    		$partyId.combobox('reload',baseUrl + 'platform/loadPartyListData.action?countryId='+countryId+'&random='+Math.random());
	    		$partyId.combobox("setValue",'');
	    	}
	    	
	    	
	    	if($("#customer")){
	    		$("#customer").combobox("clear");
	    		$("#customer").combobox('reload',baseUrl + 'platform/loadCustomerByParty.action?countryId='+countryId+'&random='+Math.random());
	    		$("#customer").combobox("setValue",'');
	    		
	    	}
	    	
	    	if($city){
	    		$city.combobox('clear');
	    		$city.combobox('reload',baseUrl + 'platform/loadCityList.action?countryId='+countryId+'&random='+Math.random());
	    		$city.combobox("setValue",'');
	    	}
	    	
	    	if($level){
	    		$level.combobox("clear");
	    		$level.combobox('reload',baseUrl + 'platform/selectLevelBycountry.action?countryId='+countryId+'&random='+Math.random());
	    		$level.combobox("setValue",'');
	    		
	    	}
	    		if($province){
	    		//add 20160526 huangqitai 
	    		//增加  重新选择下拉属性值, 级联的下级下拉框 重置
	    		$province.combobox("clear");
	    		$province.combobox('reload',baseUrl + 'platform/getProvinceList.action?countryId='+countryId+'&random='+Math.random()+"&"+conStr);
	    		$province.combobox("setValue",'');
	    		if($city){
	    			$city.combobox("clear");
	    			$city.combobox("loadData",data);
	    			$city.combobox("setValue",'');
	    			if($county){
	    				$county.combobox("clear");
	    				$county.combobox("loadData",data);
	    				$county.combobox("setValue",'');
	    				if($town){
	    					$town.combobox("clear");
	    					$town.combobox("loadData",data);
	    					$town.combobox("setValue",'');
	    				}
	    			}
	    		}
	    	}
	    	if($table){
	    		var qryParms=$table.datagrid("options").queryParams;
	    		if(countryId == ""){
	    			countryId = null;
	    		}
	    		qryParms.countryId = countryId;
	    		qryParms.provinceId = $province.combobox("getValue");
	    		qryParms.partyId = $partyId.combobox("getValue");
	    		qryParms.customerId = $("#customer").combobox("getValue");
	    		qryParms.level=$level.combobox("getValue");
	    		$table.datagrid({queryParams:qryParms});//重新加载
	    	}
	    }	
	});
	

	if($("#customer")){
		$("#customer").combobox({
			textField:"customerName",
			valueField:"customerId",
		    onChange :function(newValue,oldValue){
				var sign = $('#addShopForm input[name=switchSign]').val();
				var _customer = $("#customer").combobox("getValue");
//				var _party = $("#partyId").combobox("getValue");
				var _party = $("#countryId").combobox("getValue");
				if("" == _customer || null == _customer){
					initDataList(function(){
						switchOnOff("add",$("#addShopForm"),$("#submitBtn"));
					},null,null,null);
				}else {
					if("view" == sign){
						var _shop = $('#addShopForm input[name=editId]').val();
						viewDataListInit(function(){
							autoSelect(_party,_shop,function(){
								switchOnOff("view",$("#addShopForm"),$("#submitBtn"));
							});
						},_shop,_customer,_party);
					}else if("add" == sign){
						initDataList(function(){
							switchOnOff("add",$("#addShopForm"),$("#submitBtn"));
						},null,_customer,_party);
					}else if("edit" == sign){
						var _shop = $('#addShopForm input[name=editId]').val();
						initDataList(function(){
							autoSelect(_party,_shop,function(){
								switchOnOff("edit",$("#addShopForm"),$("#submitBtn"));
							});
						},_shop,_customer,_party);
					}
				}
		    }
		});
		
		
	}
	
	if($partyId){
		$partyId.combobox({
			valueField:'partyId',
		    textField:'partyName',
		    onChange :function(newValue,oldValue){
		    	return;
		    }
		});
	}
	
	if($province){
		$province.combobox({
			valueField:'provinceId',
		    textField:'provinceName',
		    onChange :function(newValue,oldValue){
		    	var provinceId= $(this).combobox("getValue");
		    	if($city){
		    		//add 20160526 huangqitai 
		    		//增加  重新选择下拉属性值, 级联的下级下拉框 重置
		    		$city.combobox("clear");
		    		$city.combobox('reload',baseUrl + 'platform/getCityList.action?provinceId='+provinceId+'&random='+Math.random()+"&"+conStr);
		    		$city.combobox("setValue",'');
	    			if($county){
	    				$county.combobox("clear");
	    				$county.combobox("loadData",data);
	    				$county.combobox("setValue",'');
	    				if($town){
	    					$town.combobox("clear");
	    					$town.combobox("loadData",data);
	    					$town.combobox("setValue",'');
	    				}
	    			}
		    	}
		    	if($table){
		    		var qryParms=$table.datagrid("options").queryParams;
		    		qryParms.countryId = $country.combobox("getValue");
		    		if(provinceId == ""){
		    			provinceId = null;
		    		}
		    		qryParms.provinceId = provinceId;
		    		$table.datagrid({queryParams:qryParms});//重新加载
		    	}
		    }
		});
	}
	if($city){
		$city.combobox({
			valueField:'cityId',
		    textField:'cityName',
		    onChange :function(newValue,oldValue){
		    	var cityId= $(this).combobox("getValue");
		    	if($county){
		    		//add 20160526 huangqitai 
		    		//增加  重新选择下拉属性值, 级联的下级下拉框 重置
		    		$county.combobox("clear");
		    		$county.combobox('reload',baseUrl + 'platform/getCountyList.action?cityId='+cityId+'&random='+Math.random()+"&"+conStr);
		    		$county.combobox("setValue",'');
    				if($town){
    					$town.combobox("clear");
    					$town.combobox("loadData",data);
    					$town.combobox("setValue",'');
    				}
		    	}
		    }
		});
	}
	if($county){
		$county.combobox({
			valueField:'countyId',
		    textField:'countyName',
		    onChange :function(newValue,oldValue){
		    	var countyId= $(this).combobox("getValue");
		    	if($town){
		    		//add 20160526 huangqitai 
		    		//增加  重新选择下拉属性值, 级联的下级下拉框 重置
		    		$town.combobox("clear");
		    		$town.combobox('reload',baseUrl + 'platform/getTownList.action?countyId='+countyId+'&random='+Math.random()+"&"+conStr);
		    		$town.combobox("setValue",'');
		    	}
		    }
		});
	}
	if($town){
		$town.combobox({
			valueField:'townId',
		    textField:'townName',
		    onChange :function(newValue,oldValue){
		    	return;
		    }
		});
	}
	
	if($level){
		$level.combobox({
			valueField:'id',
		    textField:'value',
		    onChange :function(newValue,oldValue){
		    	return;
		    }
		});
	}
}


//门店等级
function shopLevel($obj){
	$.ajax({
		url:baseUrl+"platform/selectShopLevel.action",
		type:"POST",
		data:{"partyId":my_login_id},
		success:function(result){
		result.rows.unshift({"value":"All","id":""});
		$obj.combobox({
			data:result.rows,
			valueField:"id",
			textField:"value",
		});
	}
	});	
}

function levelformatter(value,item,index){
	if(value=="1"){
		return "A";
	}else if(value=="2"){
		return "B";
	}else if(value=="3"){
		return 'C';
	}else if(value=="4"){
		return 'D';
	}else if(item.level=="5"){
		return 'S';
	}else if(item.level=="6"){
		return 'AA';
	}	
}

//格式化按钮
function Formatter_x(value,item,index){
	return '<div class="tcl-btn-group">'+
		'<a href="javascript:void(0);" class="tcl-view" onclick="onView(\''+index+'\')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.view")+'</a>'+
		'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
		'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
			'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}

//校验用户是否有编辑/删除功能
function isAvailable()
{	
	if(null!=roleId && ""!=roleId){
		if((roleId.substring(0,6)).indexOf("HADMIN")=='0'){
			return  true;
		}else if( (roleId.substring(0,6)).indexOf("BADMIN")=='0'){
			return  true;
		}
		else{
			return false;
		}
	}
}


//清空easyui加载的数据
function clearInitData(){
	if($('#dl_salers').length>0 && $('#dl_bussinessers').length>0 && $('#dl_supervisors').length>0){
		$('#dl_salers').datagrid('loadData', { total: 0, rows: [] }); 
		$('#dl_bussinessers').datagrid('loadData', { total: 0, rows: [] }); 
		$('#dl_supervisors').datagrid('loadData', { total: 0, rows: [] }); 
	}
}

//初使化根据国家/渠道条件
function editInitDataListByCondition(callback,shopId,customerId,countryId){
	var param="";
	if(countryId!=null){
		param+="&partyId="+countryId;
	}
	$.ajax({
			url:baseUrl+"platform/loadSalersData.action?1=1"+param+"&shopId="+shopId,
		
	}).success(function(data){
		if(data){
			$("#dl_salers").datalist({
				data:data.salers,
				checkbox: true,
			    singleSelect:false,
			    checkOnSelect:true,
			    lines: true
			});
			$("#dl_bussinessers").datalist({
				data:data.bussinessers,
				checkbox: true,
			    singleSelect:true,
			    checkOnSelect:true,
			    lines: true
			   
			});			
			$("#dl_supervisors").datalist({
				data:data.supervisors,
			    checkbox: true,
			    singleSelect:true,
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


//初使化根据国家/渠道条件
function initDataListByCondition(callback,shopId,customerId,countryId){
	var param="";
	if(countryId!=null){
		param+="&partyId="+countryId;
	}
	$.ajax({
			url:baseUrl+"platform/loadSalersData.action?1=1"+param+"&shopId="+shopId,
		
	}).success(function(data){
		if(data){
			$("#dl_salers").datalist({
				data:data.salers,
				checkbox: true,
			    singleSelect:false,
			    checkOnSelect:false,
			    lines: true
			});
			$("#dl_bussinessers").datalist({
				data:data.bussinessers,
				checkbox: true,
			    singleSelect:true,
			    checkOnSelect:false,//选中的行,false,选中复选框
			    lines: true
			   
			});			
			$("#dl_supervisors").datalist({
				data:data.supervisors,
			    checkbox: true,
			    singleSelect:true,
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