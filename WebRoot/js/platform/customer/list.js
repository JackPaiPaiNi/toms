/**
 * create by lill on 2016/04/13
 * 客户表CRUD
 */
$(function(){
	$("#customerListTable").datagrid({
		title:locale("customer.list.title"),
		url:baseUrl + 'platform/loadCustomerListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		nowrap:false,
		iconCls:'icon-man',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'customerId',title:"id",width:100,hidden:true},
	        {field:'channelType',title:locale("customer.list.th.channelType"),width:100,hidden:true},
	        {field:'customerName',title:locale("customer.list.th.name"),width:100},
	        {field:'partyName',title:locale("customer.list.th.party"),width:100},
	        {field:'createBy',title:locale("customer.list.th.creator"),width:100,hidden:true},
	        {field:'enterDateStr',title:locale("customer.list.th.enterdate"),width:100},
	        {field:'countryName',title:locale("district.countryName"),width:100,hidden:true},
	        {field:'provinceName',title:locale("district.provinceName"),width:100,hidden:true},
	        {field:'cityName',title:locale("district.cityName"),width:100,hidden:true},
	        {field:'countyName',title:locale("district.countyName"),width:100,hidden:true},
	        {field:'townName',title:locale("district.townName"),width:100,hidden:true},
	        {field:'customerCode',title:locale("customer.list.th.customerCode"),width:100,hidden:true},
	        {field:'channelTypeName',title:locale("customer.list.th.channelTypeName"),width:100,hidden:true},
	        {field:'detailAddress',title:locale("contact.address"),width:100},
	        {field:'contactName',title:locale("customer.list.th.contactName"),width:100},
	        {field:'phone',title:locale("contact.phone"),width:100},
	        {field:'email',title:locale("contact.email"),width:100},
	        {field:'status',title:locale("contact.status"),width:100,formatter:statusFormatter},
	        {field:'website',title:locale("contact.website"),width:100},
	        {field:'op',title:locale("toolbar.edit"),width:180,formatter:opFormatter_x}
	    ]],
	    toolbar:'#customertb',
	    onHeaderContextMenu:onEasyGridHeadMenu,
	    onLoadSuccess:enableBt
	});
	
	initWindow();
	
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
function initWindow(){
	initDistrictComponentsByCustomer($("#district_country"),$("#partyId"),$("#district_province"),$("#district_city"),$("#district_county"),$("#district_town"),null,my_login_id);
	//initDistrictComponents($("#district_country"),$("#district_province"),$("#district_city"),$("#district_county"),$("#district_town"),null,my_login_id);
	//initDistrictComponents($("#tcl_country"),$("#tcl_province"),null,null,null,$("#customerListTable"),"all");
	//加载机构树
//	loadPartyComboTree($("#partyId"));
	initCountry();
//	editCountry();
	$(".tcladdwin input[name='status']").combobox({
		data:[{text:locale("window.status.enable"),value:1,selected:true},{text:locale("window.status.disable"),value:0}]
	});
	
	//加载渠道类型
	loadParameters("TCL_CHANNEL").success(function(result){
		$("#channelType").combobox({
			textField:"pvalue",
			valueField:"pkey",
			data:result.CHANNEL
		});
	});
}

//加载区域数据
//function loadPartyCombo(countryId){
//	//var countryId = $("#countryId").combobox("getValue");
//	$.ajax({
//		url:baseUrl + "platform/loadPartyList.action",
//		type:"POST",
//		data:{"countryId":countryId},
//	}).success(function(data){
//		if(data.rows.length==1){
//			$("#partyId").combobox("setValue", data.rows[0].partyId).combobox("setText", data.rows[0].partyName);
//		}else{
//			data.rows.unshift({"partyName":"ALL","partyId":""});
//			$("#partyId").combobox({			
//				data:data.rows,
//				textField:"partyName",
//				valueField:"partyId",
//			});
//		}
//		
//		
//	});
//	}

//function loadPartyCombo(countryId){
////	var countryId = $("#countryId").combobox("getValue");
//	$.ajax({
//		url:baseUrl + "platform/getProvinceList.action",
//		type:"POST",
//		data:{"countryId":countryId},
//	}).success(function(data){
//		if(data.rows.length==1){
//			$("#district_province").combobox("setValue", data.rows[0].provinceId).combobox("setText", data.rows[0].province);
//		}else{
//			data.rows.unshift({"province":"ALL","provinceId":""});
//			$("#district_province").combobox({			
//				data:data.rows,
//				textField:"province",
//				valueField:"provinceId",
//			});
//		}
//		
//		
//	});
//	
//}

function initCountry(){
	$.ajax({
		url:baseUrl + "platform/onloadCountry.action",
		type:"POST",
		data:{"id":my_login_id},
	}).success(function(data){
		if(data.rows.length==1){
			$("#tcl_country").combobox("setValue", data.rows[0].countryId).combobox("setText", data.rows[0].countryName);
		}else{
			data.rows.unshift({"countryName":"All","countryId":""});
			$("#tcl_country").combobox({			
				data:data.rows,
				textField:"countryName",
				valueField:"countryId",
				onChange :function(){
		    	var countryId= $(this).combobox("getValue");
		    	$.ajax({
		    		url:baseUrl + "platform/selectProvince.action",
		    		type:"POST",
		    		data:{"countryId":countryId},
		    	}).success(function(data){
		    		if(data.rows.length==1){
		    			$("#tcl_province").combobox("setValue", data.rows[0].countryId).combobox("setText", data.rows[0].countryName);
		    		}else{
		    		$("#tcl_province").combobox({			
		    			data:data.rows,
		    			textField:"countryName",
		    			valueField:"countryId",
		    		});	
		    		}
		    	});
				}
			
			});
		}
		$.ajax({
			url : baseUrl + "platform/selectProvince.action",
			type : "POST",
			data : {
				"countryId" : data.rows[0].countryId
			},
		}).success(
				function(data) {
					if (data.rows.length == 1) {
						$("#tcl_province").combobox("setValue",
								data.rows[0].countryId).combobox("setText",
								data.rows[0].countryName);
					} else {
						$("#tcl_province").combobox({
							data : data.rows,
							textField : "countryName",
							valueField : "countryId",
						});
					}
				});
	});
}

//function initCountry(){
//	$.ajax({
//		url:baseUrl + "platform/onloadCountry.action",
//		type:"POST",
//		data:{"id":my_login_id},
//	}).success(function(data){
//		$("#tcl_country").combobox({			
//			data:data.rows,
//			textField:"countryName",
//			valueField:"countryId",
//			onChange :function(){
//		    	var countryId= $(this).combobox("getValue");
//		    	$.ajax({
//		    		url:baseUrl + "platform/selectProvince.action",
//		    		type:"POST",
//		    		data:{"countryId":countryId},
//		    	}).success(function(data){
//		    		$("#tcl_province").combobox({			
//		    			data:data.rows,
//		    			textField:"countryName",
//		    			valueField:"countryId",
//		    		});	
//		    	});
//			}
//		});
////		initProvice(data.rows[0].countryId);
//	});
//}
//
//function editCountry(){
//	$.ajax({
//		url:baseUrl + "platform/onloadCountry.action",
//		type:"POST",
//		data:{"id":my_login_id},
//	}).success(function(data){
//		$("#district_country").combobox({			
//			data:data.rows,
//			textField:"countryName",
//			valueField:"countryId",
//			onChange :function(){
//		    	var countryId= $(this).combobox("getValue");
//		    	$.ajax({
//		    		url:baseUrl + "platform/selectProvince.action",
//		    		type:"POST",
//		    		data:{"countryId":countryId},
//		    	}).success(function(data){
//		    		$("#district_province").combobox({			
//		    			data:data.rows,
//		    			textField:"countryName",
//		    			valueField:"countryId",
//		    		});	
//		    	});
//			}
//		});
////		initProvice(data.rows[0].countryId);
//	});
//}

function showAddWin(){
	showLoading();
//	$("#customerListTable").datagrid('clearSelections');//清除选中行
	$("#searchBus").textbox('setValue','');
	$("#searchProm").textbox('setValue','');
	$("#searchSup").textbox('setValue','');
	$("#customerListTable").datagrid('reload');//清除选中行

	
	//按钮启用
	$("#searchBusBt").linkbutton({disabled:false});
	$("#searchPromBt").linkbutton({disabled:false});
	$("#searchSupBt").linkbutton({disabled:false});
	
	$('#addCustomerForm input[name=editId]').val("");
	$('#addCustomerForm').form("reset");
	
//	switchOnOff("add",$("#addCustomerForm"),$("#submitBtn"));
//	initDateList(function(){
		switchOnOff("add",$("#addCustomerForm"),$("#submitBtn"));
		concealLoading();
//	},null);
	$('#addCustomerWin').window({title:locale("customer.window.add")}).window('center').window('open');
	$("#status").combobox('setValue',1);
	
	clearInitData();

}
function validate(){
	return $("#addCustomerForm").form("validate");
}
//function submitForm(){
//	if(validate()){
//		//添加or编辑
//		var id=$('#addCustomerForm input[name=editId]').val();
//		if($.trim(id)!=""){
//			$('#addCustomerForm').form("submit",{
//				url:baseUrl + "platform/editCustomer.action",
//				success:function(data){
//					if(data != ""){
//						var result=eval('('+data+')');
//						if(result.success){
//							$("#customerListTable").datagrid('reload');
//							showMsg(locale("alert.success"));
//							$('#addCustomerWin').window('close');
//						}else{
//							showMsg(result.msg);
//						}
//					}else{
//						showMsg(locale(result.msg),locale("alert.title.error"),"error");
//					}
//				}
//			});
//		}else{
//			$('#addCustomerForm').form("submit",{
//				url:baseUrl + "platform/addCustomer.action",
//				success:function(data){
//					if(data != ""){
//						var result = eval('('+data+')');
//						if(result.success){
//							showMsg(locale("alert.success"));
//							$("#customerListTable").datagrid('reload');
//							$('#addCustomerWin').window('close');
//						}else{
//							showMsg(result.msg);
//						}
//					}else{
//						showMsg(locale(result.msg),locale("alert.title.error"),"error");
//					}
//				}
//			});
//		}
//		
//	}else{
//		showMsg(locale("alert.validate.fail"));
//	}
//}
function clearForm(){
	$('#addCustomerWin').window('close');
}
function doSearch(){
	$("#searchBt").linkbutton("disable");
	var country = $("#tcl_country").combobox("getValue");
	var province = $("#tcl_province").combobox("getValue");
	var searchParty = $("#searchParty").val();
	var searchCustomer = $("#searchCustomer").val();
	$("#customerListTable").datagrid({
		queryParams:{
			searchCustomer:searchCustomer,
			searchParty:searchParty,
			countryId:country,
			provinceId:province
		}
	});
}
//显示编辑窗口
function onEdit(index){
	showLoading();
	$("#searchBus").textbox('setValue','');
	$("#searchProm").textbox('setValue','');
	$("#searchSup").textbox('setValue','');
	//按钮启用
	$("#searchBusBt").linkbutton({disabled:false});
	$("#searchPromBt").linkbutton({disabled:false});
	$("#searchSupBt").linkbutton({disabled:false});
	
	//获取并填充数据
	var row=$("#customerListTable").datagrid('getRows')[index];
	$('#addCustomerForm input[name=editId]').val(row.customerId);
	$('#addCustomerForm').form('load',row);
	editDistrictComponents($("#district_country"),$("#district_province"),$("#district_city"),$("#district_county"),$("#district_town"),row);
	editDataList(function(){
		autoSelect(row.countryId,row.customerId,function(){
			switchOnOff("edit",$("#addCustomerForm"),$("#submitBtn"));
			concealLoading();
		});
	},row.countryId,row.customerId);
	
//	switchOnOff("edit",$("#addCustomerForm"),$("#submitBtn"));
	//显示窗口
	$('#addCustomerWin').window({title:locale("customer.window.edit")}).window('center').window('open');
}
//导入数据
function importCustomers(){
	showImportWin(locale("customer.import"),baseUrl + "platform/importCustomer.action");
}

//编辑级联框赋值
function editDistrictComponents($country,$province,$city,$county,$town,row){
	if($country){
		var countryId = $country.combobox("getValue");
		$province.combobox('reload',baseUrl + 'platform/getProvinceList.action?countryId='+countryId);
		$province.combobox("setValue",row.provinceId);
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

//删除
function onDelete(index){
	var row = $("#customerListTable").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "platform/deleteCustomer.action",
				type:"POST",
				data:{"id":row.customerId},
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#customerListTable").datagrid('reload');
					}else{
						showMsg(result.msg);
					}
				}
			});
	    }
	});
}
//查看
function onView(index){
	showLoading();
	//清空文本框的值
	$("#searchBus").textbox('setValue','');
	$("#searchProm").textbox('setValue','');
	$("#searchSup").textbox('setValue','');
	//按钮禁用
	$("#searchBusBt").linkbutton({disabled:true});
	$("#searchPromBt").linkbutton({disabled:true});
	$("#searchSupBt").linkbutton({disabled:true});
	
	var row=$("#customerListTable").datagrid('getRows')[index];
	$('#addCustomerForm').form('load',row);
	editDistrictComponents($("#district_country"),$("#district_province"),$("#district_city"),$("#district_county"),$("#district_town"),row);
	viewDateList(function(){
		autoSelect(row.countryId,row.customerId,function(){
			switchOnOff("view",$("#addCustomerForm"),$("#submitBtn"));
			concealLoading();
		});
	},row.countryId,row.customerId);
	//显示窗口
	$('#addCustomerWin').window({title:locale("customer.window.view")}).window('center').window('open');
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


function downName(){
	var url = baseUrl + "platform/ExportDealerName.action";
	location.href = url;
}


function initDistrictComponentsByCustomer($country,$partyId,$province,$city,$county,$town,$table,_searchFlag){
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
	    	initDateList(countryId,null);
	    	if($partyId){
	    		$partyId.combobox("clear");
	    		$partyId.combobox('reload',baseUrl + 'platform/loadPartyList.action?countryId='+countryId+'&random='+Math.random());
	    		$partyId.combobox("setValue",'');
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
	    		$table.datagrid({queryParams:qryParms});//重新加载
	    	}
	    }	
	});
	
	
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


//初使化业务员，督导
function initDateList(countryId,customerId){
	var param="";
	if(countryId!=null){
		param+="&partyId="+countryId;
	}
	$.ajax({
			url:baseUrl+"platform/loadCustomerSalesDataByConditons.action?1=1"+param+"&customerId="+customerId,
		
	}).success(function(data){
		if(data){
			$("#dl_sales").datalist({
				data:data.salers,
				checkbox: true,
			    singleSelect:true,
			    checkOnSelect:true,
			    lines: true
			});
			$("#dl_pro").datalist({
				data:data.pro,
				checkbox: true,
			    singleSelect:false,//选择多行
			    checkOnSelect:true,//选中的行,false,选中复选框
			    lines: true
			   
			});			
			$("#dl_sup").datalist({
				data:data.sup,
			    checkbox: true,
			    singleSelect:true,
			    checkOnSelect:true,
			    lines: true
			});
			

		}
	}).error(function(){
		
	});
}


//view显示业务员，督导
function viewDateList(callback,countryId,customerId){
	var param="";
	if(countryId!=null){
		param+="&partyId="+countryId;
	}
	$.ajax({
			url:baseUrl+"platform/loadCustomerSalesData.action?"+param+"&customerId="+customerId,
		
	}).success(function(data){
		if(data){
			$("#dl_sales").datalist({
				data:data.salers,
				checkbox: true,
			    singleSelect:true,
			    checkOnSelect:false,
			    lines: true
			});
			$("#dl_pro").datalist({
				data:data.pro,
				checkbox: true,
			    singleSelect:false, //选择多行
			    checkOnSelect:false, //复选框选中或取消复某一行,false,选中或取消只能行径复选框
			    lines: true
			});		
			$("#dl_sup").datalist({
				data:data.sup,
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

function submitForm(){
	if(validate()){
		//添加or编辑
		var id=$('#addCustomerForm input[name=editId]').val();
		var sel_a=$("#dl_sales").datalist('getChecked');//获取选中的对象
		var sel_b=$("#dl_sup").datalist('getChecked');
		var sel_c=$("#dl_pro").datalist('getChecked');
		var sals=[]; 
		var sups=[];
		var pros=[];
		var salParam="";
		var supsParam="";
		var prosParam="";
		
		$.each(sel_a,function(i,n){
			sals.push(sel_a[i]["id"]); //业务员
			salParam +=sals+";";
		});
		$.each(sel_b,function(i,n){
			sups.push(sel_b[i]["id"]); //督导
			supsParam +=sups+";";
		});
		$.each(sel_c,function(i,n){
			pros.push(sel_c[i]["id"]); //促销员
//			prosParam +=pros+";";
		});
		
		if($.trim(id)!=""){
			$('#addCustomerForm').form("submit",{
				url:baseUrl + "platform/editCustomer.action?salParam="+salParam+
				"&prosParam="+pros.join(';')+"&supsParam="+supsParam,
				success:function(data){
					if(data != ""){
						var result=eval('('+data+')');
						if(result.success){
							$("#customerListTable").datagrid('reload');
							showMsg(locale("alert.success"));
							$('#addCustomerWin').window('close');
						}else{
							showMsg(result.msg);
						}
					}else{
						showMsg(locale(result.msg),locale("alert.title.error"),"error");
					}
				}
			});
		}else{
			$('#addCustomerForm').form("submit",{
				url:baseUrl + "platform/addCustomer.action?salParam="+salParam+
				"&prosParam="+pros.join(';')+"&supsParam="+supsParam,
				success:function(data){
					if(data != ""){
						var result = eval('('+data+')');
						if(result.success){
							showMsg(locale("alert.success"));
							$("#customerListTable").datagrid('reload');
							$('#addCustomerWin').window('close');
						}else{
							showMsg(result.msg);
						}
					}else{
						showMsg(locale(result.msg),locale("alert.title.error"),"error");
					}
				}
			});
		}
		
	}else{
		showMsg(locale("alert.validate.fail"));
	}
}

//编辑时获取已选用户信息
function autoSelect(countryId,customerId,callback){
	var param ="";
	var para = "";
	if(countryId!=null && countryId!=""){
		param +="partyId="+countryId;
	}
	if(customerId!=null && customerId!=""){
		para +="&customerId="+customerId;
	}
	$.ajax({
		url:baseUrl+"platform/getCustomerUserRelations.action?"+param
		+para
	}).success(function(list){
		if(list.length>0){
			$.each(list,function(index,next){
				if(next.salerType==0){
					//业务员，item.userLoginId
					var rows = $("#dl_sales").datalist('getRows');
					for(i=0;i<rows.length;i++){
						if(rows[i].id==next.userLoginId){
							$("#dl_sales").datalist('checkRow',i);
							break;
						}
					}
				}else if(next.salerType==1){
					var rows = $("#dl_pro").datalist('getRows');
					for(i=0;i<rows.length;i++){
						if(rows[i].id==next.userLoginId){
							$("#dl_pro").datalist('checkRow',i);
							break;
						}
					}
				}else if(next.salerType==2){
					var rows = $("#dl_sup").datalist('getRows');
					for(i=0;i<rows.length;i++){
						if(rows[i].id==next.userLoginId){
							$("#dl_sup").datalist('checkRow',i);
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
	
//	$.ajax({
//		url:baseUrl+ 'platform/getCustomerUserRelations.action?customerId='+customerId,
//		type:"POST",
//		success:function(list){
//				if(list.length>0){
//					$.each(list,function(index,next){
//						if(next.salerType==0){
//							//业务员，item.userLoginId
//							var rows = $("#dl_sales").datalist('getRows');
//							for(i=0;i<=rows.length;i++){
//								if(rows[i].id==next.userLoginId){
//									$("#dl_sales").datalist('checkRow',i);
//									break;
//								}
//							}
//						}else if(next.salerType==2){
//							var rows = $("#dl_sup").datalist('getRows');
//							for(i=0;i<=rows.length;i++){
//								if(rows[i].id==next.userLoginId){
//									$("#dl_sup").datalist('checkRow',i);
//									break;
//								}
//							}
//						}
//					});
//				}
//				if(callback){
//					callback();
//				}
//			}
//	});
}

//渠道对应关系业务员用户名查询
function doSearchBus(){
	var param={};
	var searchBus=$("#searchBus").val();
	param.searchBus=searchBus;
	var customerId="";
	var partyId=$("#district_country").combobox("getValue");
	var row=$("#customerListTable").datagrid('getSelections');
	for ( var j = 0; j < row.length; j++) {
		customerId=row[j].customerId;
	}
	//如果未选择国家时
	if(partyId!=""){
		$.post(
				baseUrl+"platform/loadCustomerSalesData.action?partyId="+partyId+"&customerId="+customerId,
				param,
				function(data){			
						$.ajax({
							url:baseUrl+"platform/getCustomerUserRelations.action?partyId="+partyId+"&customerId="+customerId
						}).success(function(list){
							if(list.length>0){
								$.each(list,function(index,next){
									if(next.salerType==0){
										//业务员，item.userLoginId
										var rows = $("#dl_sales").datalist('getRows');
										for(i=0;i<rows.length;i++){
											if(rows[i].id==next.userLoginId){
												$("#dl_sales").datalist('checkRow',i);
												break;
											}
										}
									}
								});
							}
						});	
						
						if(data){
							$("#dl_sales").datalist({
								data:data.salers,
								checkbox: true,
							    singleSelect:true,
							    checkOnSelect:true,
							    lines: true
							});    
						}
					
				}
			);
	}else{
		$.post(
				baseUrl+"platform/loadCustomerSalesData.action?partyId="+partyId+"&customerId="+customerId,
				param,
				function(data){			
						$.ajax({
							url:baseUrl+"platform/getCustomerUserRelations.action?partyId="+partyId+"&customerId="+customerId
						}).success(function(list){
							if(list.length>0){
								$.each(list,function(index,next){
									if(next.salerType==0){
										//业务员，item.userLoginId
										var rows = $("#dl_sales").datalist('getRows');
										for(i=0;i<rows.length;i++){
											if(rows[i].id==next.userLoginId){
												$("#dl_sales").datalist('checkRow',i);
												break;
											}
										}
									}
								});
							}
						});										
				}
			);
	}
}

//渠道对应关系促俏员用户名查询
function doSearchProm(){
	var param={};
	var searchprom=$("#searchProm").val();
	var partyId=$("#district_country").combobox("getValue");
	param.searchprom=searchprom;
	var customerId="";
	var row=$("#customerListTable").datagrid('getSelections');
	for ( var j = 0; j < row.length; j++) {
		customerId=row[j].customerId;
	}
	if(partyId!=""){
		$.post(
				baseUrl+"platform/loadCustomerSalesData.action?partyId="+partyId+"&customerId="+customerId,
				param,
				function(data){
						$.ajax({
							url:baseUrl+"platform/getCustomerUserRelations.action?partyId="+partyId+"&customerId="+customerId
						}).success(function(list){
							if(list.length>0){
								$.each(list,function(index,next){
									if(next.salerType==1){
										//业务员，item.userLoginId
										var rows = $("#dl_pro").datalist('getRows');
										for(i=0;i<rows.length;i++){
											if(rows[i].id==next.userLoginId){
												$("#dl_pro").datalist('checkRow',i);
												break;
											}
										}
									}
								});
							}
						});	
						
					if(data){
						$("#dl_pro").datalist({
							data:data.pro,
							checkbox: true,
						    singleSelect:false,
						    checkOnSelect:true,
						    lines: true
						});    
					}			
				}
			);
	}else{
		$.post(
				baseUrl+"platform/loadCustomerSalesData.action?partyId="+partyId+"&customerId="+customerId,
				param,
				function(data){
						$.ajax({
							url:baseUrl+"platform/getCustomerUserRelations.action?partyId="+partyId+"&customerId="+customerId
						}).success(function(list){
							if(list.length>0){
								$.each(list,function(index,next){
									if(next.salerType==1){
										//业务员，item.userLoginId
										var rows = $("#dl_pro").datalist('getRows');
										for(i=0;i<rows.length;i++){
											if(rows[i].id==next.userLoginId){
												$("#dl_pro").datalist('checkRow',i);
												break;
											}
										}
									}
								});
							}
						});	
		
				}
			);
	}
}

//渠道对应关系督导用户名查询
function doSearchSup(){
	var param={};
	var searchSup=$("#searchSup").val();
	var partyId=$("#district_country").combobox("getValue");
	param.searchSup=searchSup;
	var customerId ="";
	var row=$("#customerListTable").datagrid('getSelections');
	for ( var j = 0; j < row.length; j++) {
		customerId=row[j].customerId;
	}
	if(partyId!=""){
		$.post(
				baseUrl+"platform/loadCustomerSalesData.action?partyId="+partyId+"&customerId="+customerId,
				param,
				function(data){
						$.ajax({
							url:baseUrl+"platform/getCustomerUserRelations.action?partyId="+partyId+"&customerId="+customerId
						}).success(function(list){
							if(list.length>0){
								$.each(list,function(index,next){
									if(next.salerType==2){
										//业务员，item.userLoginId
										var rows = $("#dl_sup").datalist('getRows');
										for(i=0;i<rows.length;i++){
											if(rows[i].id==next.userLoginId){
												$("#dl_sup").datalist('checkRow',i);
												break;
											}
										}
									}
								});
							}
						});	
						
					if(data){
						$("#dl_sup").datalist({
							data:data.sup,
							checkbox: true,
						    singleSelect:true,
						    checkOnSelect:true,
						    lines: true
						});    
					}			
				}
			);
	}else{
		$.post(
				baseUrl+"platform/loadCustomerSalesData.action?partyId="+partyId+"&customerId="+customerId,
				param,
				function(data){
						$.ajax({
							url:baseUrl+"platform/getCustomerUserRelations.action?partyId="+partyId+"&customerId="+customerId
						}).success(function(list){
							if(list.length>0){
								$.each(list,function(index,next){
									if(next.salerType==2){
										//业务员，item.userLoginId
										var rows = $("#dl_sup").datalist('getRows');
										for(i=0;i<rows.length;i++){
											if(rows[i].id==next.userLoginId){
												$("#dl_sup").datalist('checkRow',i);
												break;
											}
										}
									}
								});
							}
						});									
				}
			);
	}
}


function showLoading(){
	var url = baseUrl + "images/login/loading.gif";
	$("#loadGif").attr("src",url);
	$('#load-layout').show();
}

function concealLoading(){
	$('#load-layout').hide();
}

//编辑加载数据
function editDataList(callback,partyId,customerId){
	var param="";
	if(partyId!=null){
		param+="&partyId="+partyId;
	}
	$.ajax({
			url:baseUrl+"platform/loadCustomerSalesData.action?1=1"+param+"&customerId="+customerId,
		
	}).success(function(data){
		if(data){
			$("#dl_sales").datalist({
				data:data.salers,
				checkbox: true,
			    singleSelect:true,
			    checkOnSelect:true,
			    lines: true
			});
			$("#dl_pro").datalist({
				data:data.pro,
				checkbox: true,
			    singleSelect:false,//选择多行
			    checkOnSelect:true,//复选框选中或取消复某一行,false,选中或取消只能行径复选框
			    lines: true
			   
			});			
			$("#dl_sup").datalist({
				data:data.sup,
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

//清空easyui加载的数据
function clearInitData(){
	if($('#dl_sales').length>0 && $('#dl_pro').length>0 && $('#dl_sup').length){
		$('#dl_sales').datagrid('loadData', { total: 0, rows: [] }); 
		$('#dl_pro').datagrid('loadData', { total: 0, rows: [] }); 
		$('#dl_sup').datagrid('loadData', { total: 0, rows: [] }); 
	}
}
