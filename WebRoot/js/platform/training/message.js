/**
 * 消息提醒
 */

$(function(){
	
	$("#toRight").click(function(){
		$("#selectLeft option:selected").each(function(){
			$("#selectRight").append("<option value=" + $(this).val() + ">" + $(this).html() + "</option>");
			$(this).remove();
		});
	});
	$("#toLeft").click(function(){
		$("#selectRight option:selected").each(function(){
			$("#selectLeft").append("<option value=" + $(this).val() + ">" + $(this).html() + "</option>");//这个方法是默认在后面添加
			//$("#selectLeft option:first").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在select前面加内容
			//$("#selectLeft option[value=3]").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在selectt指定某一行加内容
			$(this).remove();
		});
	});
	
	//全局变量
//	obj={
//		//加载编辑框的内容
//		loadMessageUserData:function(){
//			var param={};
//			var msgId=$("#msgId").val();
//			var searchUser = $("#searchUser").val();
//			param.msgId=msgId;
//			param.searchUser=searchUser;
//			$.post(
//				baseUrl+"message/getUserAll.action",
//				param,
//				function(result){
//					var msg=result.msg;
//					if(msg=="success"){
//					var rows=result.rows;
//					$("#selectLeft option").remove();
//					for(var i=0;i<rows.length;i++){
//						var row=rows[i];
//						var row = rows[i];
//						var userLoginId = row.userLoginId;
//						var existSelectRight = $("#selectRight option[value='"+userLoginId+"']");
//						if(existSelectRight && ""!=existSelectRight && null!=existSelectRight && existSelectRight.length>0)
//						{
//							continue;
//						}
//						else
//						{
//							var leftOrRight = row.leftOrRight;
//							var _option = "<option value='"+userLoginId+"'>"+row.userName+"</option>";
//							if("left"==leftOrRight)
//							{
//								$(_option).appendTo('#selectLeft');
//							}
//							else
//							{
//								$(_option).appendTo('#selectRight');
//								}
//							}
//						}
//					}
//				}
//			);
//		}	
//	};
	
	$("#msglist").datagrid({
		title:locale("message.list.title"),
		url:baseUrl + 'message/LoadMessageListData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-search',
		fitColumns:true,
		fit:true,
		columns:[[
		          {field: 'id',  title: jsLocale.get('message.id'),width:100,hidden:true}, 
		          {field: 'countryName',  title: jsLocale.get('message.countryId'),width:100}, 
		          {field: 'msgType',  title: jsLocale.get('message.type'),width:100,formatter:msgType}, 
		            {field: 'msgTitle',  title: jsLocale.get('message.title'),width:100},  
		            {field: 'msgComment',  title: jsLocale.get('message.comment'),width:100,hidden:true},
		            {field: 'createBy',  title: jsLocale.get('message.createBy'),width:100},
		            {field: 'createTime',  title: jsLocale.get('message.createtime'),width:110,formatter:todate},
		           /* {field:'op',title:locale("toolbar.edit"),width:60,formatter:opFormatter},*/
		            ]],
		  		onHeaderContextMenu:onEasyGridHeadMenu,
//		  		onLoadSuccess:enableBt
		  	});
//		initWindow();
});

//function enableBt(){
//	$("#searchBt").linkbutton("enable");
//	initDataGridCells();
//}


//日期转换函数
var todate = function(value,row,index) 
{
	
	if(value == null)
	{
		return null;
	}
	var d = new Date();
	d.setTime(value.time); 
	return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
};

//消息的类型
function msgType(value,row,index){
	if(value=="1"){
		return "Training Course";
	}else if(value=="2"){
		return "Examination";
	}
}

//编辑消息
function onEdit(index){
	var row = $("#msglist").datagrid('getRows')[index];
	$('#addMsgForm').form('load',row);
	$('#addMsgForm input[name=editId]').val(row.id);
	//截取msg.roleId的值
	var roleIdValue=(row.msgRoleId).substring((row.msgRoleId).indexOf("_")-10,(row.msgRoleId).indexOf("_"));
	if(roleIdValue=='SALES'){
		$("#roleTypeName").html("<option value='SALES'>"+jsLocale.get('msg.form.title.roleType.SALES')+"</option>");
	}else if(roleIdValue=='SUPERVISOR'){
		$("#roleTypeName").html("<option value='SUPERVISOR'>"+jsLocale.get('msg.form.title.roleType.SUPERVISOR')+"</option>");
	}else if(roleIdValue=='PROM'){
		$("#roleTypeName").html("<option value='PROM'>"+jsLocale.get('msg.form.title.roleType.PROM')+"</option>");
	}else if(roleIdValue=='HLEADER'){
		$("#roleTypeName").html("<option value='HLEADER'>"+jsLocale.get('msg.form.title.roleType.HLEADER')+"</option>");
	}else if(roleIdValue=="HTEAMER"){
		$("#roleTypeName").html("<option value='HTEAMER'>"+jsLocale.get('msg.form.title.roleType.HTEAMER')+"</option>");
	}else if(roleIdValue=='BADMIN'){
		$("#roleTypeName").html("<option value='BADMIN'>"+jsLocale.get('msg.form.title.roleType.BADMIN')+"</option>");
	}else if(roleIdValue=='BLEADER'){
		$("#roleTypeName").html("<option value='BLEADER'>"+jsLocale.get('msg.form.title.roleType.BLEADER')+"</option>");
	}else if(roleIdValue=='BTEAMER'){
		$("#roleTypeName").html("<option value='BTEAMER'>"+jsLocale.get('msg.form.title.roleType.BTEAMER')+"</option>");
	}else if(roleIdValue=="REGIONAL"){
		$("#roleTypeName").html("<option value='REGIONAL'>"+jsLocale.get('msg.form.title.roleType.REGIONAL')+"</option>");
	}
	
	
	//获取发送消息的用户
	$.ajax({
		url:baseUrl+"message/selectUserBycondition.action?msgRoleId="+row.msgRoleId,
		type:"POST",
		success:function(result){
			var obj = eval('('+result+')');
			var optStr="";
			$.each(obj.rows,function(i,n){
				optStr +="<option  value=''>"+n.userLoginId+"</option>";
			});
			$("#selectRight").html(optStr);
		}
	});
	
	$("#countryId").html("<option value=''>"+row.countryName+"</option>");
	
	$('#addMsgWin').window({title:locale("barcode.window.edit")});
	$('#addMsgWin').window('center');
	$('#addMsgWin').window('open');
	
	$("#selectLeft").html("");
}

//删除信息
function onDelete(index){
	if(confirm(locale("alert.delete.confirm"))){
		var row = $("#msglist").datagrid('getRows')[index];
		$.ajax({
			url:baseUrl + "message/deleteMessage.action",
			type:"POST",
			data:{"id":row.id},
			success:function(data){
				var result=eval('('+data+')');
				if(result.success){
					showMsg(locale("alert.success"));
					$("#msglist").datagrid('reload');
				}else{
					showMsg(result.msg);
				}
			}
		});
	}
}

function clearForm(){
	$("#addMsgWin").window('close');
}

function validate(){
	return $("#addMsgForm").form("validate");
}

function submitForm(){
	if(validate()){
		//添加or编辑
		var msgId=$('#addMsgForm input[name=editId]').val();
		if($.trim(msgId)!=""){
			
			var selectRight = $('#selectRight option');
			var allUserStr = "";
			for(var i=0;i<selectRight.length;i++)
			{
				var userLoginId = selectRight[i].innerText;
				allUserStr += userLoginId+";";
			}
			param= allUserStr;
			//编辑
			$('#addMsgForm').ajaxSubmit({
				url:baseUrl + "message/updateMessage.action?id="+msgId,
				data:{"allUserStr":allUserStr},
				success:function(data){
					var result=eval('('+data+')');				
					if(result.success){
						$("#msglist").datagrid('reload');
						$('#addMsgWin').window('close');
						showMsg(locale("alert.success"));
					}else{
						showMsg(result.msg);
					}
				}
			});
		}
	}else{
		showMsg(locale("alert.validate.fail"));
	}
	
}