var pType = '?pType=2';//空调类型

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
	        {field:'size',title:locale("product.list.th.horse"),width:100},
	        {field:'productType',title:locale("product.list.th.productType"),width:100},
	        {field:'catena',title:locale("product.list.th.line"),width:100},
	        {field:'description',title:locale("toolbar.edit"),width:160,formatter:opFormatter_product}
	    ]],
	    toolbar:'#producttb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	initWindow();
	initEditor();
	
});

function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}

function loadACSize(){
	$("#addProductWin input[name='size']").combobox({
		url:baseUrl + "platform/selectACAllSize.action",
		valueField:'size',    
	    textField:'size'
	});
};

function loadACClassification(){
	$("#addProductWin input[name='productType']").combobox({
		url:baseUrl + "platform/selectACAllClassification.action",
		valueField:'productTypeId',    
	    textField:'productTypeId'
	});
};

function loadACType(){
	$("#addProductWin input[name='productCatena']").combobox({
		url:baseUrl + "platform/selectACCatena.action",
		valueField:'productType',    
		textField:'productType'
	});
};


function initEditor(){
	//自定义按钮
	var _tools = "Cut,Copy,Paste,Pastetext,|,Blocktag,Fontface,FontSize,Bold,Italic,Underline," +
			"Strikethrough,FontColor,BackColor,SelectAll,Removeformat,|,Align,List,Outdent," +
			"Indent,|,Hr,Emot,Table,Source";
	$("#description").xheditor({tools:_tools});
	$("#descriptionView").xheditor({tools:_tools});
}
function initWindow(){
	loadACSize();
	loadACClassification();
	loadACType();
}
function showAddWin(){
	$("#isUdpateAd").val('add');
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

	function submitForm(){
		if(validate()){
			//添加or编辑
			var id=$('#editId').val();
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

//显示编辑窗口
function onEdit(index){
	$("#isUdpateAd").val('update');
	
	$('#addProductForm').form("reset");
	//获取并填充数据
	var row=$("#productListTable").datagrid('getRows')[index];
	row.productCatena = row.catena;
	row.acsize = row.size;
	row.gas = row.gasType;
	//储存修改前的型号
	$('#editProductId').val(row.modelName);
	
	$('#addProductForm').form('load',row);
	$('#addProductForm input[name=editId]').val(row.id);
	var pdfHtml = "<a href='"+row.filePath+"' class='easyui-linkbutton' plain='true'>"+row.fileName+"</a>";
	$('#uploadExcel_product_name').html(pdfHtml);
	$("#description").html(row.description);
	$("#utr").show();
	
	switchOnOff("edit",$("#addProductForm"),$("#submitBtn"));
	setReadOnly($('#addProductForm #productId'));
	$('#addProductForm #productId').textbox("disable");
	//显示窗口
	$('#addProductWin').window({title:locale("product.window.edit")});
	$('#addProductWin').window('center');
	$('#addProductWin').window('open');
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
	row.productCatena = row.catena;
	row.acsize = row.size;
	row.gas = row.gasType;
	$('#addProductForm').form('load',row);
	var pdfHtml = "<a href='"+row.filePath+"' class='easyui-linkbutton' plain='true'>"+row.fileName+"</a>";
	
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
