//重写setText 方法  
//解决 textbox组件切出去再切回来 值会被清空
//20160531 add huangqitai
$.extend($.fn.textbox.methods, {
	setText : function(jq, value) {
		return jq.each(function() {
			var opts = $(this).textbox('options');
			var input = $(this).textbox('textbox');
			value = value == undefined ? '' : String(value);
			
			if ($(this).textbox('getText') != value) {
			input.val(value);
			}
			opts.value = value;
			if (!input.is(':focus')) {
				if (value) {
				input.removeClass('textbox-prompt');
				} else {
				input.val(opts.prompt).addClass('textbox-prompt');
				}
			}
			$(this).textbox('validate');
		});
	}
});

function formValidate(addOrEdit){
	var falg = true;
	//验证用户名不能为中文
	var loginUser = $("#userLoginId").val();
	var userRegExp = new RegExp("[\u4e00-\u9fa5]");
	if(userRegExp.test(loginUser)){
		showMsg(jsLocale.get('user.list.form.save.error.loginUser'));
		falg = false;
		return falg;
	}else if("" == $.trim(loginUser)){
		showMsg(jsLocale.get('user.list.form.save.error_1.loginUser'));
		falg = false;
		return falg;
	}
	if("update" != addOrEdit){
		//验证密码复杂性   密码规则： 至少6位密码,字母和数组的组合,以字母开头
		var loginPassword = $("#password").val();
		var passwordRegExp = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,1000}$/);
		if(!passwordRegExp.test(loginPassword)){
			showMsg(jsLocale.get('user.list.form.save.error.loginPassword'));
			falg = false;
			return falg;
		}
	}
	return falg;
}

function editSub(formAction){
	$("#userForm").form("submit",{
		url:formAction,
		onSubmit:function(){},
		success:function(data){
			var msg = $.parseJSON(data).msg;
			if("success"==msg){
				showMsg(jsLocale.get('user.list.form.save.success'));
				obj.search();
				$("#userWindow").window('close');
			}
			else if("exists"==msg){
				$("#password").textbox("setValue","");//当用户已存在,则需要重新输入密码
				showMsg(jsLocale.get('user.list.form.save.exist'));
			}else{
				showMsg(msg);
			}
		}
	});
}


	
function loadRoleData(partyId){	
	//加载角色数据
	$("#roleId").combobox({
		url:baseUrl + "platform/selectRoleByPartyId.action?partyId="+partyId+"",
		valueField:'roleId',
	    textField:'roleName',
		method:"post"
	});
}

$(function(){
	//加载机构数据
	$("#partyId").combobox({
		url:baseUrl + "platform/selectUserPartyCountry.action",
		valueField:'partyId',    
	    textField:'partyName',
		method:"post",
		onChange: function () {
			loadRoleData($('#partyId').combobox('getValue'));
			var countryId=$("#partyId").combobox('getValue');
			$("#party").combobox("clear");
			$("#party").combobox({
				url: baseUrl + 'platform/loadPartyListByUser.action?countryId='+countryId+'',
				valueField:'partyId',
			    textField:'partyName',
			    onChange :function(newValue,oldValue){
			    	loadRoleData($('#party').combobox('getValue'));
			    }
			});
			$("#party").combobox("setValue",'');
    		
		}
	});
	
	if($("#party")){
		$("#party").combobox({
			valueField:'partyId',
		    textField:'partyName',
		    onChange :function(newValue,oldValue){
		    	return;
		    }
		});
	}
	
	
	$("#selectQuertyPartyId").combobox({
		url:baseUrl + "platform/selectUserPartyCountry.action",
		valueField:'partyId',    
	    textField:'partyName',
		method:"post",
		onChange: function () {
			obj.search();
		}
	});
	
	//全局变量
	obj = {
		search:function(){
			$('#userListTable').datagrid("load",{
				searchMsg:$.trim($("#searchMsg").val()),
				selectQuertyPartyId:$.trim($('#selectQuertyPartyId').combobox('getValue'))
			});
			$('#userListTable').datagrid('clearSelections');
		},
		reset:function(){
			var addOrEdit = $("#addOrEdit").val();
			var userWorkNum = $("#userWorkNum").val();
			var userLoginId = $("#userLoginId").val();
			$('#userForm').form('reset');
			if("update" == addOrEdit)
			{
				$("#userForm").form({
					onLoadSuccess:function(data){}
				}).form('load',{
					addOrEdit : addOrEdit,
					userWorkNum : userWorkNum,
					userLoginId : userLoginId
				});
			}
		},
		closeWindow:function(){
			$("#userWindow").window('close');
		},
		closeWindow2:function(){
			$("#passwordWindow").window('close');
		},
		save:function(){
			
			if(!$("#userForm").form("validate")){
				return;
			}
			var addOrEdit = $("#addOrEdit").val();
			var formAction = "";
			if(formValidate(addOrEdit)){
				
				if("update" == addOrEdit){
					$.post(
							baseUrl + "platform/selectUserLoginId.action",
							function(data,status)
							{
								var msg = $.parseJSON(data).msg;
								var isQualified = false;
								if($('#newUserLoginId').val() == $('#userLoginId').val()){
									isQualified = true;
								}else if("0"==msg){
									isQualified = true;
								}
								
								if(!isQualified){
									showMsg(jsLocale.get('user.list.but.userloginid'));
									return;
								}
								formAction = baseUrl + 'platform/editUserLogin.action';
								editSub(formAction);
							}
						);
				} else{
					formAction = baseUrl + 'platform/insertUserLogin.action';
					//把密码转成密文并且附加到页面上
					var password = $("#password").val();
					var _password = MD5(password);
					$("#password").textbox("setValue",_password);
					editSub(formAction);
				}
			}
		},
		updatePassword:function(){
			//验证密码复杂性   密码规则： 至少6位密码,字母和数组的组合,以字母开头
			var loginPassword = $("#updatePassword").val();
			var passwordRegExp = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[^]{8,1000}$");
			if(!passwordRegExp.test(loginPassword)){
				showMsg(jsLocale.get('user.list.form.save.error.loginPassword'));
			}
			else{
				var _password = MD5(loginPassword);
				$("#updatePassword").textbox("setValue",_password);
				$("#passwordForm").form("submit",{
					url:baseUrl + 'platform/updateUserPassword.action',
					onSubmit:function(){},
					success:function(data){
						var msg = $.parseJSON(data).msg;
						if("success"==msg){
							showMsg(jsLocale.get('user.list.form.save.success'));
							obj.search();
							$("#passwordWindow").window('close');
						}
						else if("exists"==msg){
							showMsg(jsLocale.get('user.list.form.save.exist'));
						}
						else{
							showMsg(msg);
						}
					}
				});
			}
		},
		deleteUser:function(id){
			var param = {};		
			param.userLoginId = id;
			$.post(
				baseUrl + "platform/deleteUserLogin.action",
				param,
				function(data,status)
				{
					var msg = $.parseJSON(data).msg;
					if("success"==msg){
						showMsg(jsLocale.get('user.list.form.opt.success'));
						obj.search();
					}
					else{
						showMsg(msg);
					}
				}
			);
		
			
		},
		enableOrDisableUser:function(flag,id)
		{
			//flag=1启用，flag=0禁用,id要禁用/启用的用户Id
			var param = {};
			param.enabled = flag;
			param.userLoginId = id;
			$.post(
				baseUrl + "platform/enableOrDisableUser.action",
				param,
				function(data,status)
				{
					var msg = $.parseJSON(data).msg;
					if("success"==msg){
						showMsg(jsLocale.get('user.list.form.opt.success'));
						obj.search();
					}
					else{
						showMsg(msg);
					}
				}
			);
		}
	};
	
	//加载本模块的所有配置参数
	loadParameters("TCL_TOMS","LANGUAGE",null).success(function(data){
		$("#userLocale").combobox({
			data:data.LANGUAGE,
			valueField:"pkey", 
			textField:"pvalue"
		});
	});
	
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
	
	//转换汉字--是否可用
	var toenabled = function(value,row,index) 
    {
		if(value == "1")
		{
			return jsLocale.get('user.list.form.enable.y');
		}
		else
		{
			return jsLocale.get('user.list.form.enable.n');
		}
	};
	
	//转换登陆方式
	var tologinType = function(value,row,index) 
    {
		if(value == "LDAP")
		{
			return jsLocale.get('user.list.form.loginType.LDAP');
		}
		else
		{
			return jsLocale.get('user.list.form.loginType.LOCAL');
		}
	};
	
	
	//给窗口添加事件,满足form表单元素自适应
	$("#userWindow").window({
		'onOpen':function(){
			setFormWidth($('#userForm'));
		},
		'onResize':function(){
			setFormWidth($('#userForm'));
		}
	});
	
	//初始化表格
	$('#userListTable').datagrid({
		fit:true,
		iconCls:'icon-search',
		fitColumns:true,
		resizeHandle:'both',
		autoRowHeight:false,
		striped:true,
		method:'post',
		url:baseUrl + 'platform/loadUserLoginGridData.action',
		idField:'userLoginId',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		sortName:'createDate',
		sortOrder:'desc',
		remoteSort:true,
		pageNumber:1,
		pageSize:10,
		pageList:[10,20,30,40,50,100],
		queryParams:{},
		columns:[[
	        {field: 'userWorkNum',  title: jsLocale.get('user.list.gridhead.userWorkNum'),sortable:true,resizable:true,halign:'center',align:'left',width:70},
            {field: 'userLoginId',  title: jsLocale.get('user.list.gridhead.userLoginId'),sortable:true,resizable:true,halign:'center',align:'left',width:100},  
            {field: 'loginType',  title: jsLocale.get('user.list.gridhead.loginType'),sortable:true,resizable:true,hidden:true,halign:'center',align:'left',width:70,formatter:tologinType},
            {field: 'userName',  title: jsLocale.get('user.list.gridhead.userName'),sortable:true,resizable:true,halign:'center',align:'left',width:110},
            {field: 'level',  title: jsLocale.get('user.list.gridhead.level'),sortable:true,resizable:true,halign:'center',align:'left',width:110,formatter:toLevel}, 
            {field: 'password',  title: jsLocale.get('user.list.gridhead.password'),sortable:true,resizable:true,hidden:true,halign:'center',align:'left',width:100},  
            {field: 'passwordHint',  title: jsLocale.get('user.list.gridhead.passwordHint'),sortable:true,resizable:true,hidden:true,halign:'center',align:'left',width:100},
            {field: 'enabled',  title: jsLocale.get('user.list.gridhead.enabled'),sortable:true,resizable:true,halign:'center',align:'left',width:70,formatter:toenabled},
            {field: 'disabledDateTime', title: jsLocale.get('user.list.gridhead.disabledDateTime'),sortable:true,resizable:true,halign:'center',align:'left',width:80,formatter:todate},
            {field: 'createBy',  title: jsLocale.get('user.list.gridhead.createBy'),sortable:true,resizable:true,halign:'center',align:'left',width:100},
            {field: 'createDate', title: jsLocale.get('user.list.gridhead.createDate'),sortable:true,resizable:true,halign:'center',align:'left',width:80,formatter:todate},
            {field: 'roleName', title: jsLocale.get('user.list.gridhead.roleName'),sortable:true,resizable:true,halign:'center',align:'left',width:160},
            {field: 'partyName', title: jsLocale.get('user.list.gridhead.partyName'),sortable:true,resizable:true,halign:'center',align:'left',width:90},
            {field: 'userEmail',  title: jsLocale.get('user.list.gridhead.userEmail'),sortable:true,resizable:true,halign:'center',align:'left',width:130},
            {field: 'userMcId',  title: jsLocale.get('user.list.gridhead.userMcId'),sortable:true,resizable:true,halign:'center',align:'left',width:90},
            {field: 'userTelNum',  title: jsLocale.get('user.list.gridhead.userTelNum'),sortable:true,resizable:true,halign:'center',align:'left',width:90},
            {field: 'userLocaleDesc',  title: jsLocale.get('user.list.gridhead.userLocaleDesc'),sortable:true,resizable:true,halign:'center',align:'left',width:90}
        ]],
        toolbar:[{
        	text:jsLocale.get('user.list.but.addLocalUser'),
            iconCls:'icon-add',
            handler:function(){
            	$('#userForm').form('reset');
            	$('#userForm').form({
            		onLoadSuccess:function(data){
            			setUnReadOnly($("#userWorkNum"));
                    	setUnReadOnly($("#userLoginId"));
                    	$("#userLoginId").textbox("enable");
                    	$("#ptr").show();
            		}
            	}).form("load",{addOrEdit:null});
                $("#userWindow").window('open');  
            }
        }/*,'-',{
            text:jsLocale.get('user.list.but.addLdapUser'),  
            iconCls:'icon-add',  
            handler:function(){
            	
            }
        }*/,'-',{  
            text:jsLocale.get('user.list.but.modifyUser'),  
            iconCls:'icon-edit',  
            handler:function(){
            	
            	if(isCanEdit == 'true'){
            		setUnReadOnly($("#userLoginId"));
            	}else{
            		setReadOnly($("#userLoginId"));
            	}
            	var selectedRow = $("#userListTable").datagrid('getSelected');
            	$("#newUserLoginId").val(selectedRow.userLoginId);
            	if(null==selectedRow)
            	{
            		showMsg(jsLocale.get('user.list.but.modifyUser.checkUser'));
            		return;
            	}
            	var userLoginId = selectedRow.userLoginId;
            	$("#userForm").form({
            		onLoadSuccess:function(data)
            		{
//            			setReadOnly($("#userWorkNum"));
//            			setReadOnly($("#userLoginId"));
            			$("#addUserLoginId").val(userLoginId);
            			$("#userLoginId").textbox("disable");
            			$("#ptr").hide();
            			$("#userWindow").window('open');
            		}
            	}).form('load',baseUrl+'platform/loadUpdateUserLoginData.action?userLoginId='+userLoginId);
            }
        },'-',{  
        	text:jsLocale.get('user.list.but.delete'),
            iconCls:'icon-remove',
            handler:function(){
            	var selectedRow = $("#userListTable").datagrid('getSelected');
            	if(null==selectedRow)
            	{
            		showMsg(jsLocale.get('user.list.but.deleteUser.checkUser'));
            		return;
            	}
            	//var userLoginId = selectedRow.userLoginId;
            	//var enabled = selectedRow.enabled;
             	/*if("1"==enabled)
         		{
             		showMsg(jsLocale.get('user.list.but.enableUser.hasEnable'));
             		return;
         		}*/
             	var userLoginId = selectedRow.userLoginId;
             	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm.selected"),function(r){
             		if (r){
             			obj.deleteUser(userLoginId);
             		}
             	});
                }
        },'-',{  
        	text:jsLocale.get('user.list.but.modifyPassword'),
            iconCls:'icon-edit',
            handler:function(){
            	
            	
            	
            	var selectedRow = $("#userListTable").datagrid('getSelected');
            	if(null==selectedRow)
            	{
            		showMsg(jsLocale.get('user.list.but.modifyPassword.checkUser'));
            		return;
            	}
            	var userLoginId = selectedRow.userLoginId;
            	$('#passwordForm').form('reset');
            	$('#passwordForm').form({
            		onLoadSuccess:function(data){
                    	$("#editUserLoginId").val(userLoginId);
            		}
            	}).form("load",{addOrEdit:null});
                $("#passwordWindow").window('open');  
            }
        },'-',{  
        	 text:jsLocale.get('user.list.but.enableUser'),  
             iconCls:'icon-ok',  
             handler:function(){
             	var selectedRow = $("#userListTable").datagrid('getSelected');
             	if(null==selectedRow)
             	{
             		showMsg(jsLocale.get('user.list.but.enableUser.checkUser'));
             		return;
             	}
             	var enabled = selectedRow.enabled;
             	if("1"==enabled)
         		{
             		showMsg(jsLocale.get('user.list.but.enableUser.hasEnable'));
             		return;
         		}
             	var userLoginId = selectedRow.userLoginId;
             	obj.enableOrDisableUser("1",userLoginId);
                }
        },'-',{  
        	text:jsLocale.get('user.list.but.disableUser'),  
            iconCls:'icon-no',  
            handler:function(){
            	var selectedRow = $("#userListTable").datagrid('getSelected');
            	if(null==selectedRow)
            	{
            		showMsg(jsLocale.get('user.list.but.disableUser.checkUser'));
            		return;
            	}
            	var enabled = selectedRow.enabled;
            	if("0"==enabled)
        		{
            		showMsg(jsLocale.get('user.list.but.disableUser.hasDisable'));
            		return;
        		}
            	var userLoginId = selectedRow.userLoginId;
            	obj.enableOrDisableUser("0",userLoginId);
            }
        }/*,'-',{  
        text:jsLocale.get('user.list.but.importUser'),  
        iconCls:'icon-save',  
        handler:function(){
        	showImportWin(jsLocale.get("user.list.but.importUser"),baseUrl+"/platform/importUser.action");
        }
    }*/],
    onLoadSuccess: function (data) {
    		initDataGridToolBar();
    	}
    });
	
	loadUserLevel();
});


function importUser(){
	showImportWin(locale("user.list.but.importUser"),baseUrl + "platform/importUser.action");
}

//加载角色级别
function loadUserLevel(){
	var optStr="<option value=''></option>";
	$.ajax({
		url:baseUrl+"platform/selectUserLevel.action",
		type:"POST",
		success:function(result){
			var obj=eval('('+result+')');
//		obj.rows.unshift({"value":"NOT","id":""});
//		$("#userLevel").combobox({
//			data:obj.rows,
//			valueField:"id",
//			textField:"value",
//		});
		$.each(obj.rows,function(i,n){
			optStr+="<option value="+n.id+">"+n.value+"</option>";
			$("#userLevel").html(optStr);
		});
		}
	});	
}

function toLevel(value,row,index){
	if(row.level=="1"){
		return "one-star";
	}else if (row.level=="2"){
		return "two-star";
	}else if(row.level=="3"){
		return "three-star";
	}else if(row.level=="4"){
		return "four-star";
	}else if(row.level=="5"){
		return "five-star";
	}
}