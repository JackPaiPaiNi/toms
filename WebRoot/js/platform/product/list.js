var pType = '?pType=1';//电视类型

/**
 * 产品信息管理
 * lilinlu
 */
$(function(){
	$("#productListTable").datagrid({
		title:locale("product.list.title"),
		url:baseUrl + 'platform/loadProductListData.action' + pType,
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'modelName',title:locale("product.list.th.productId"),width:100},
	        {field:'productTypeId',title:locale("product.list.th.productTypeId"),width:100},
	        {field:'productSpecId',title:locale("product.list.th.productSpecId"),width:100},
	        {field:'size',title:locale("product.list.th.size"),width:100},
	        {field:'os',title:locale("product.list.th.os"),width:100},
	        {field:'op',title:locale("toolbar.edit"),width:160,formatter:opFormatter_product}
	    ]],
	    toolbar:'#producttb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	initWindow();
	initEditor();
	getCategoryTwo("#fathe","#sonId","#catId",null);
	
});

function getCategoryTwo(fatherId,sonId,catId,isUpdateSelete){
	
	var typeOptionStr = "";
	var classOptionStr = "";
	var fatherI = $(fatherId).val();
	if(fatherId != '###'){
		$.ajax({
			url:baseUrl + "platform/selectCategoryByFatherIdTWo.action",
			type:"POST",
			success:function(data){
				var result = eval('('+data+')');
				if(isUpdateSelete != null){
					var typeOptionStr = "";
					var classOptionStr = "";
					var tr1 = "";
					var tr2 = "";
					var tr3 = "";
					var tr4 = "";
					var tr5 = "";
					var i = 1;
					$(result).each(function(){
						if(this.productId == 2){
							if(isUpdateSelete.productTypeId == this.productName){
								typeOptionStr += "<option selected=true value="+this.productName+">"+this.productName+"</option>";
							}else{
								typeOptionStr += "<option value="+this.productName+">"+this.productName+"</option>";
							}
						}else{
							if(i == 1){
								tr1 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
							}else if (i == 2){
								tr2 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
							}else if (i == 3){
								tr3 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
							}else if (i == 4){
								tr4 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
							}else if (i == 5){
								tr5 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
							}
							i++;
							if(i == 6){
								i = 1;
							}
						}
					});
					operating_public_category(tr1,tr2,tr3,tr4,tr5,typeOptionStr);
					var sqecIdStr = isUpdateSelete.productSpecId.split('/');
					var specDemo = $("[name='z']");
					for(var i= 0;i<sqecIdStr.length;i++){
						for(var j=0;j<specDemo.length;j++){
							if(specDemo[j].value.toLowerCase() == sqecIdStr[i].toLowerCase()){
								$(specDemo[j]).attr('checked','checked');
								break;
							}
						}
					}
					
					
					//id editId
					$("#editProductId").val(isUpdateSelete.modelName);
					$("#editId").val(isUpdateSelete.id);
					
					//产品系列
//					loadProductLine(isUpdateSelete.catena);
					
					
				}else{
//					loadProductLine('');
					if(fatherId != '#productType'){
						$(sonId).html("");
						$(catId).html("");
						var typeOptionStr = "<option value=''><span tcl-text='product.search.select'></option>";
						var classOptionStr = "<option value=''><span tcl-text='product.search.select'></option>";
						$(result).each(function(){
							if(this.productId == 2){
								typeOptionStr += "<option value="+this.productName+">"+this.productName+"</option>"
							}else{
								classOptionStr += "<option value="+this.productName+">"+this.productName+"</option>"
							}
						});
						$(sonId).html(typeOptionStr);
						$(catId).html(classOptionStr);
					}else{
						var typeOptionStr = "";
						var classOptionStr = "";
						var tr1 = "";
						var tr2 = "";
						var tr3 = "";
						var tr4 = "";
						var tr5 = "";
						var i = 1;
						$(result).each(function(){
							if(this.productId == 2){
								typeOptionStr += "<option value="+this.productName+">"+this.productName+"</option>";
							}else{
								if(i == 1){
									tr1 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
								}else if (i == 2){
									tr2 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
								}else if (i == 3){
									tr3 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
								}else if (i == 4){
									tr4 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
								}else if (i == 5){
									tr5 += "<td><input name='z' value= '"+this.productName+"' type='checkbox'></td><td>"+this.productName+"</td>";
								}
								i++;
								if(i == 6){
									i = 1;
								}
							}
						});
						operating_public_category(tr1,tr2,tr3,tr4,tr5,typeOptionStr);
					}				
				}
			}
		});
	}
}

function operating_public_category (tr1,tr2,tr3,tr4,tr5,typeOptionStr){
	$($("#tr1")[0]).html("");
	$($("#tr2")[0]).html("");
	$($("#tr3")[0]).html("");
	$($("#tr4")[0]).html("");
	$($("#tr5")[0]).html("");
	$($("#tr1")[0]).html(tr1);
	$($("#tr2")[0]).html(tr2);
	$($("#tr3")[0]).html(tr3);
	$($("#tr4")[0]).html(tr4);
	$($("#tr5")[0]).html(tr5);
	$("#sonType").html(typeOptionStr);
}

function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}

//加载产品系列
function loadProductLine(/*isUpdate*/){
	$.ajax({
		url:baseUrl + "platform/selectLine.action",
		type:"POST",
		success:function(data){
			var msg = $.parseJSON(data);
			$("#productCatena").combobox({			
				data:msg,
				textField:"catena",
				valueField:"catena",
			});
			$('#productCatena').combobox({editable:false});
			/*if(msg != "" && msg != null){
				
				var optionStr = "";
				if('' != isUpdate){
					for(var i = 0;i<msg.length;i++){
						if(isUpdate.toLowerCase() == msg[i].catena.toLowerCase()){
							optionStr += "<option selected=true value="+msg[i].catena+">"+msg[i].catena+"</option>";
						}else{
							optionStr += "<option value = "+msg[i].catena+">"+msg[i].catena+"</option>";
						}
					}
				}else{
					for(var i = 0;i<msg.length;i++){
						optionStr += "<option value = "+msg[i].catena+">"+msg[i].catena+"</option>";
					}
				}
				$("#productCatena").html(optionStr);
			}*/
		}
	});
}

function initEditor(){
	//自定义按钮
	var _tools = "Cut,Copy,Paste,Pastetext,|,Blocktag,Fontface,FontSize,Bold,Italic,Underline," +
			"Strikethrough,FontColor,BackColor,SelectAll,Removeformat,|,Align,List,Outdent," +
			"Indent,|,Hr,Emot,Table,Source";
	$("#description").xheditor({tools:_tools});
	$("#descriptionView").xheditor({tools:_tools});
}
function initWindow(){
	//加载机构树
	loadPartyCountryTree($("#addProductWin input[name=partyId]"));
	//加载下拉框
	loadParameters("TCL_PRODUCT").success(function(result){
		$("#addProductWin input[name=productType]").combobox({
			textField:"pvalue",
			valueField:"pkey",
			data:result.TYPE
		});
		$("#addProductWin input[name='display']").combobox({
			textField:"pvalue",
			valueField:"pvalue",
			data:result.DISPLAY
		});
		$("#addProductWin input[name='size']").combobox({
			textField:"pvalue",
			valueField:"pvalue",
			data:result.SIZE
		});
		$("#addProductWin input[name='brandId']").combobox({
			textField:"pvalue",
			valueField:"pvalue",
			data:result.BRAND
		});
		$("#addProductWin input[name='interFace']").combobox({
			textField:"pvalue",
			valueField:"pvalue",
			data:result.INTERFACE
		});
		$("#addProductWin input[name='network']").combobox({
			textField:"pvalue",
			valueField:"pvalue",
			data:result.NETWORK
		});
		$("#addProductWin input[name='os']").combobox({
			textField:"pvalue",
			valueField:"pvalue",
			data:result.OS
		});
		$("#addProductWin input[name='ratio']").combobox({
			textField:"pvalue",
			valueField:"pvalue",
			data:result.SCALE
		});
		$("#addProductWin input[name='colorId']").combobox({
			textField:"pvalue",
			valueField:"pvalue",
			data:result.COLOR
		});
		$("#addProductWin input[name='status']").combobox({
			data:[{text:locale("window.status.enable"),value:1,"selected":true},{text:locale("window.status.disable"),value:0}]
		});
	});
	
	loadProductLine();
}
function showAddWin(){
	$("#isUdpateAd").val('add');
	getCategoryTwo("#productType","#sonType","#catType",null);
	setUnReadOnly($('#addProductForm #productId'));
	$('#addProductForm #productId').textbox("enable");
	$('#addProductForm input[name=editId]').val("");
	$('#addProductForm').form("reset");
	$("#description").html("");
	$("#utr").hide();
	switchOnOff("add",$("#addProductForm"),$("#submitBtn"));
	$('#addProductWin').window({title:locale("product.window.add")}).window('center').window('open');
	$("#status").combobox('setValue',1);
}
function validate(){
	return $("#addProductForm").form("validate");
}

/**
 * 校验产品属性是否勾选
 * @returns {Boolean}
 */
function validateAttr(){
	var pAttr = $("[name='productSpecId']").val();
	if(pAttr == null || pAttr == ''){
		showMsg(locale("product.list.attr.notSelect"));
		return false;
	}
	return true;
};

function submitForm(){
	
	alertAddProductClass();
	if(validate()){
		
		if(!validateAttr()){
			return;
		};
		
		//添加or编辑
		var id=$('#editId').val();
		alertAddProductClass();
		if($.trim(id)!=""){
			showLoading();
			//编辑
			$('#addProductForm').ajaxSubmit({
				url:baseUrl + "platform/editProduct.action" + pType +"id="+id,
				success:function(data){
					concealLoading();
					var result = eval('('+data+')');
					if(result.success){
						$("#productListTable").datagrid('reload');
						$('#addProductWin').window('close');
						showMsg(locale("alert.success"));
					}else{
						concealLoading();
						if("TE_0001" == json.msg){
							showMsg(locale("product.alert.error_1"));
						}else{
							showMsg(locale("alert.error"));
						}
					}
					
					$('#productCatena').html('');
				}
			});
		}else{
			showLoading();
			$('#addProductForm').ajaxSubmit({
				url:baseUrl + "platform/addProduct.action" + pType,
				success:function(data){
					concealLoading();
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#productListTable").datagrid('reload');
						$('#addProductWin').window('close');
					}else{
						concealLoading();
						if("TE_0001" == json.msg){
							showMsg(locale("product.alert.error_1"));
						}else{
							showMsg(locale("alert.error"));
						}
					}
				}
			});
		}
	}else{
		showMsg(locale("alert.validate.fail"));
	}
}

function alertAddProductClass(){
	
	var spec = "";
	var specDemo = $("[name='z']");
	for(var i = 0;i<specDemo.length;i++){
		if(specDemo[i].checked == true){
			spec += specDemo[i].value;
			for(var j = i +1 ;j<specDemo.length;j++){
				if(specDemo[j].checked == true){
					spec += "/";
					break;
				}
			}
		}
	}
	$("#catType").val(spec);
}

function clearForm(){
	$('#addProductWin').window('close');
}
function clearFormView(){
	$('#xhEditorView').window('close');
}
function doSearch(){
	$("#searchBt").linkbutton("disable");
	
	var sel = '';
	if($("#sonId").val()!= '###' && $("#catId").val()!= '###'){
		sel = '12';
	}else if ($("#sonId").val() == '###' && $("#catId").val() == '###'){
		sel = '';
	}else if ($("#sonId").val()!= '###'){
		sel = $("#sonId").attr("sel");
	}else if ($("#catId").val() != '###'){
		sel = $("#catId").attr("sel");
	}
	
	$("#productListTable").datagrid({
		
		queryParams:{
			keyword:$("#qryName").val(),
			fatherId:$("#fatherId").val(),
			sonId:$("#sonId").val(),
			catId:$("#catId").val(),
			sel:sel
			
		}
	});
}

function test(){
	
	var typeOptionStr = "";
	var classOptionStr = "";
	var tr1 = "";
	var tr2 = "";
	var tr3 = "";
	var tr4 = "";
	var i = 1;
	
	$(result).each(function(){
		if(this.productId == 2){
			typeOptionStr += "<option value="+this.productName+">"+this.productName+"</option>";
			
		}else{
			
			if(i == 1){
				tr1 += "<td>"+this.productName+"</td><td><input name='z' value= '"+this.productName+"' type='checkbox'></td>";
			}else if (i == 2){
				tr2 += "<td>"+this.productName+"</td><td><input name='z' value= '"+this.productName+"' type='checkbox'></td>";
			}else if (i == 3){
				tr3 += "<td>"+this.productName+"</td><td><input name='z' value= '"+this.productName+"' type='checkbox'></td>";
			}else if (i == 4){
				tr4 += "<td>"+this.productName+"</td><td><input name='z' value= '"+this.productName+"' type='checkbox'></td>";
			}
			i++;
			if(i == 5){
				i = 1;
			}
		}
	});
	
	$($("#tr1")[0]).html("");
	$($("#tr2")[0]).html("");
	$($("#tr3")[0]).html("");
	$($("#tr4")[0]).html("");
	
	$($("#tr1")[0]).html(tr1);
	$($("#tr2")[0]).html(tr2);
	$($("#tr3")[0]).html(tr3);
	$($("#tr4")[0]).html(tr4);
	
	$("#sonType").html(typeOptionStr);
}


//显示编辑窗口
function onEdit(index){
	$("#isUdpateAd").val('update');
	
	$('#addProductForm').form("reset");
	//获取并填充数据
	var row=$("#productListTable").datagrid('getRows')[index];
	$('#addProductForm').form('load',row);
	$('#addProductForm input[name=editId]').val(row.modelName);
	var pdfHtml = "<a href='"+row.filePath+"' class='easyui-linkbutton' plain='true'>"+row.fileName+"</a>";
	$('#uploadExcel_product_name').html(pdfHtml);
	$("#description").html(row.description);
	$("#utr").show();
	
	
	getCategoryTwo("#fathe","#sonId","#catId",row);
	
	$("#productCatena").combobox("setValue",row.catena);
	switchOnOff("edit",$("#addProductForm"),$("#submitBtn"));
	setReadOnly($('#addProductForm #productId'));
	$('#addProductForm #productId').textbox("disable");
	//显示窗口
	$('#addProductWin').window({title:locale("product.window.edit")});
	$('#addProductWin').window('center');
	$('#addProductWin').window('open');
	
	//上市日期
	/*
	var myDate = new Date(row.introductionDate.time);
	var date = (myDate.getFullYear()+"")+"-"+(myDate.getMonth()+1)+""+"-"+myDate.getDate()+"";
	$("#introduction").val(date);*/
}

//导入数据
function importProducts(){
	showImportWin(locale("product.import"),baseUrl + "platform/importProduct.action" + pType);
}

//删除
function onDelete(index){
	var row = $("#productListTable").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "platform/deleteProduct.action",
				type:"POST",
				data:{"productId":row.modelName},
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#productListTable").datagrid('reload');
					}else{
						showMsg(result.msg);
					}
				}
			});
	    }
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

//查看
function onView(index){
	
	$('#addProductForm').form("reset");
	//获取并填充数据
	var row=$("#productListTable").datagrid('getRows')[index];
	$('#addProductForm').form('load',row);
	var pdfHtml = "<a href='"+row.filePath+"' class='easyui-linkbutton' plain='true'>"+row.fileName+"</a>";
	
	getCategoryTwo("#fathe","#sonId","#catId",row);
	
	$('#uploadExcel_product_name').html(pdfHtml);
	$('#addProductForm #productId').textbox("disable");
	$("#description").html(row.description);
	$("#utr").show();
	switchOnOff("view",$("#addProductForm"),$("#submitBtn"));
	//显示窗口
	$('#addProductWin').window({title:locale("product.window.view")});
	$('#addProductWin').window('center');
	$('#addProductWin').window('open');
}
//查看描述
function onDescriptionView(index){
	$('#xhEditorFrom').form("reset");
	var row=$("#productListTable").datagrid('getRows')[index];
	$("#descriptionView").html(row.description);
	$('#xhEditorView').window({title:locale("product.window.descriptionView")});
	$('#xhEditorView').window('center');
	$('#xhEditorView').window('open');
}

function switchOnOff(switchSign,$form,$subBtn){
	if("view" == switchSign){
		$subBtn.hide();
		$form.find('input,select').attr('readonly',true);
		$form.find('input,select').attr('disabled',true); 	
		$("#editorControlName").hide();
		$("#editorControl").hide();
		$form.find("input[editable='false']").each(function(){
	    	$(this).combobox("disable");
		});
	}else{
		$subBtn.show();
		$form.find('input,select').attr('readonly',false);
		$form.find('input,select').attr('disabled',false);
		$("#editorControlName").show();
		$("#editorControl").show();
		$form.find("input[editable='false']").each(function(){
	    	$(this).combobox("enable");
		});
	}
}
