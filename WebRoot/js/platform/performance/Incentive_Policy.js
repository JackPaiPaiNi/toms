/**
 * 绩效管理
 */
$(function(){

	$("#shopListTable").datagrid({
//		title:locale("shop.list.title"),
		url:baseUrl + 'incentive/loadPolicyList.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		nowrap:false,
		iconCls:'icon-large-table',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'startDate',title:locale("policy.list.startDate"),width:100},      
	        {field:'expirationDate',title:locale("user.list.gridhead.disabledDateTime"),width:100},
	        {field:'qtyCompletionRate',title:locale("inpo.quantity.Completion"),width:100},
	        {field:'amtCompletionRate',title:locale("inpo.amount.Completion"),width:100},
	        {field:'amtReWard',title:locale("inpo.complete"),width:100},
	        {field:'userId',title:locale("policy.creatBy"),width:100},
	      
	        {field:'op',title:locale("toolbar.edit"),width:130,formatter:Formatter_One}
	    ]],
	    toolbar:'#shoptb',
	    onHeaderContextMenu:onEasyGridHeadMenu,
	    onLoadSuccess:enableBt
	});
	
	$("#shopListTableTwo").datagrid({
//		title:locale("shop.list.title"),
		url:baseUrl + 'incentive/loadPolicyListByProduct.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		nowrap:false,
		iconCls:'icon-large-table',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'startDate',title:locale("policy.list.startDate"),width:100},  
	        {field:'expirationDate',title:locale("user.list.gridhead.disabledDateTime"),width:100},
	        {field:'productLine',title:locale("product.list.th.line"),width:100},
	        {field:'qtyCompletionRate',title:locale("inpo.quantity.Completion"),width:100},
	        {field:'amtReWard',title:locale("inpo.complete"),width:100},
	        {field:'userId',title:locale("policy.creatBy"),width:100},
	      
	        {field:'op',title:locale("toolbar.edit"),width:130,formatter:Formatter_Two}
	    ]],
	    toolbar:'#shoptbTwo',
	    onHeaderContextMenu:onEasyGridHeadMenu,
	    onLoadSuccess:enableBt
	});
	
	$("#shopListTableThree").datagrid({
//		title:locale("shop.list.title"),
//		url:baseUrl + 'platform/loadShopListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		nowrap:false,
		iconCls:'icon-large-table',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'ExpirationDate',title:locale("user.list.gridhead.disabledDateTime"),width:100},
	        {field:'line',title:locale("product.list.th.line"),width:100},
	        {field:'amtCompletion',title:locale("inpo.amount.Completion"),width:100},
	        {field:'complete',title:locale("inpo.complete"),width:100},
	      
	        {field:'op',title:locale("toolbar.edit"),width:130,formatter:Formatter_One}
	    ]],
	    toolbar:'#shoptbThree',
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
	ProductLine();	
}

//个人奖励
function showAddWinOne(){
	$('#addShopForm input[name=switchSign]').val("add");
	$('.tcladdwin input[name=editId]').val("");
	$('.tcladdwin form').form("reset");

	$('#addShopWin').siblings(".panel-header").find(".panel-title").text(locale("policy.window.add"));
	$('#addShopWin').window('center').window('open');
}

//重点产品奖励
function showAddWinTwo(){
	$('#addShopFormTwo input[name=switchSign]').val("add");
	$('.tcladdwin input[name=editId]').val("");
	$('.tcladdwin form').form("reset");

	$('#addShopWinTwo').siblings(".panel-header").find(".panel-title").text(locale("policy.window.add"));
	$('#addShopWinTwo').window('center').window('open');
}

function validate(){
	return $("#addShopForm").form("validate");
	return $("#addShopFormTwo").form("validate");
}
function submitFormOne(){
	if(validate()){
		if(!isCheck()){
			return;
		}
		//添加or编辑
		var id=$('#addShopForm input[name=editId]').val();

			$('#addShopForm').form("submit",{
				url:baseUrl + 
				($.trim(id)!=""?"incentive/updatePolicy.action?id="+id:"incentive/addPolicy.action"),
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
	}else{
		showMsg(locale("alert.validate.fail"));
	  }
	}
	

function submitFormTwo(){
	if(!isCheckTwo()){
		return;
	}
	//添加or编辑
	var id=$('#addShopFormTwo input[name=editId]').val();

		$('#addShopFormTwo').form("submit",{
			url:baseUrl + 
			($.trim(id)!=""?"incentive/updatePolicyByProduct.action?id="+id:"incentive/addPolicyByProduct.action"),
			success:function(data){
				if(data != ""){
					var result=eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#shopListTableTwo").datagrid('reload');
						$('#addShopWinTwo').window('close');
					}else{
						showMsg(result.msg);
					}
				}else{
					showMsg(locale(result.msg),locale("alert.title.error"),"error");
				}
			}
		});
	}

function clearFormOne(){
	$('#addShopWin').window('close');
}

function clearFormTwo(){
	$('#addShopWinTwo').window('close');
}

function doSearchOne(){
	$("#searchBt").linkbutton("disable");
	var startDate = $("#tcl_startDate").val();
	var expirationDate = $("#date").val();
	$("#shopListTable").datagrid({
		queryParams:{expirationDate:expirationDate,
					startDate:startDate,	}
	});
}

function doSearchTwo(){
	$("#searchBt").linkbutton("disable");
	var startDate = $("#tcl_startDateTwo").val();
	var expirationDate = $("#dateTwo").val();
	$("#shopListTableTwo").datagrid({
		queryParams:{expirationDate:expirationDate,
					startDate:startDate,	}
	});
}


//显示编辑窗口
function onEdit(index){
	$('#addShopForm input[name=switchSign]').val("edit");
	//获取并填充数据
	var row=$("#shopListTable").datagrid('getRows')[index];
	$('#addShopForm').form('load',row);

	$('#addShopForm input[name=editId]').val(row.id);
	
	$('#addShopWin').siblings(".panel-header").find(".panel-title").text(locale("policy.list.edit"));
	$('#addShopWin').window('center').window('open');	
}


//显示编辑窗口
function onEditTwo(index){
	$('#addShopFormTwo input[name=switchSign]').val("edit");
	//获取并填充数据
	var row=$("#shopListTableTwo").datagrid('getRows')[index];
	$('#addShopFormTwo').form('load',row);

	$('#addShopFormTwo input[name=editId]').val(row.id);
	
	$('#addShopWinTwo').siblings(".panel-header").find(".panel-title").text(locale("policy.list.edit"));
	$('#addShopWinTwo').window('center').window('open');	
}

function showLoading(){
	var url = baseUrl + "images/login/loading.gif";
	$("#loadGif").attr("src",url);
	$('#load-layout').show();
}







//格式化按钮
function Formatter_One(value,item,index){
	return '<div class="tcl-btn-group">'+
		'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a></div>';
}

function Formatter_Two(value,item,index){
	return '<div class="tcl-btn-group">'+
		'<a href="javascript:void(0);" class="tcl-btn" onclick="onEditTwo('+index+')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a></div>';
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

//校验
function isCheck(){
	var startDate = $("#startDate").val();
	var expirationDate = $("#expirationDate").val();
	var qtyCompletionRate=$("#qtyCompletionRate").val();
	var amtCompletionRate=$("#amtCompletionRate").val();
	var amtReWard=$("#complete").val();
	
	if(startDate==null || startDate ==""){
		showMsg(locale("policy.list.startDate.error"));
		return false;
	}
	
	if(expirationDate==null || expirationDate==""){
		showMsg(locale("policy.list.expirationDate.error"));
		return false;
	}
	
	if(startDate>expirationDate){
		showMsg(locale("policy.list.morethan"));
		return false;
	}
	
	if(qtyCompletionRate==null || qtyCompletionRate==""){
		showMsg(locale("policy.list.qtyCompletionRate"));
		return false;
	}
	
	if(amtCompletionRate==null || amtCompletionRate==""){
		showMsg(locale("policy.list.amtCompletionRate"));
		return false;
	}
	
	if(amtReWard==null || amtReWard==""){
		showMsg(locale("policy.list.amtReWard"));
		return false;
	}
	
	return true;
}

//校验
function isCheckTwo(){
	var startDate = $("#startDateTwo").val();
	var expirationDate = $("#expirationDateTwo").val();
	var qtyCompletionRateTwo=$("#qtyCompletionRateTwo").val();
	var productLine=$("#productLine option:selected").val();
	var amtReWard=$("#completeTwo").val();
	
	if(startDate==null || startDate ==""){
		showMsg(locale("policy.list.startDate.error"));
		return false;
	}
	
	if(expirationDate==null || expirationDate==""){
		showMsg(locale("policy.list.expirationDate.error"));
		return false;
	}
	
	if(startDate>expirationDate){
		showMsg(locale("policy.list.morethan"));
		return false;
	}
	
	if(qtyCompletionRateTwo==null || qtyCompletionRateTwo==""){
		showMsg(locale("policy.list.qtyCompletionRate"));
		return false;
	}
	
	if(productLine==null || productLine==""){
		showMsg(locale("policy.list.productLine"));
		return false;
	}
	
	if(amtReWard==null || amtReWard==""){
		showMsg(locale("policy.list.amtReWard"));
		return false;
	}
	
	return true;
}

//----------------------------------------------个人重点产品奖励--------------------------
function ProductLine(){
	var optStr="<option value=''></option>";
	$.ajax({
		url:baseUrl + "incentive/selectProductLine.action",
		type:"POST",
		success:function(result){
			$.each(result.rows,function(i,n){
				optStr+="<option value="+n.catena+">"+n.catena+"</option>";
			});
			$("#productLine").html(optStr);
		}
	});
}