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



//校验菜单值

var isFalse = true;
function addMenu(){
	
	var menuName = $("#permissionName").val();
	
	if("" == menuName || null == menuName){
		showMsg(jsLocale.get('permission.check.permissionName'));
		return isFalse = false;
	}else if(menuName != "" && menuName != null){
		var menArr = $("#supMenu option");
		for(var i = 0 ;i<menArr.length;i++){
			if(menArr[i].innerHTML == menuName){
				showMsg(jsLocale.get('permission.check.permissionNew'));
				isFalse =false;
				return false;
			}
		}
		endTest();
	};
}

var perName = "";
function updateMenu(){
	
	var menuName = $("#permissionName").val();
	if("" == menuName || null == menuName){
		showMsg(jsLocale.get('permission.check.permissionName'));
		return isFalse = false;
		
	}else if(perName == menuName){
		endTest();
	}else if(menuName != "" && menuName != null){
		var menArr = $("#supMenu option");
		for(var i = 0 ;i<menArr.length;i++){
			if(menArr[i].innerHTML == menuName){
				showMsg(jsLocale.get('permission.check.permissionNew'));
				isFalse =false;
				return false;
			}
		}
		
		endTest();
	};
	
}

function endTest(){
	var labelKey = $("#labelKey").val();
	var menuUrl = $("#menuUrl").val();
	var menuNub = $("#menuNub").val();
	if("" == labelKey || labelKey == null){
		showMsg(jsLocale.get('permission.check.permissionIKey'));
		return isFalse = false;
	};
	if("" == menuUrl || menuUrl == null){
		showMsg(jsLocale.get('permission.check.url'));
		return isFalse = false;
	};
	
	if('' == menuNub || menuNub == null){
		showMsg(jsLocale.get('permission.check.nub'));
	}else{
		var patrn=/^[0-9]+$/;
		if(!patrn.test(menuNub)){
			showMsg(jsLocale.get('Please enter the correct integer values!'));
		}
	}
}

function loadSupData(){
	//加载父类数据
	
		$.post(
				baseUrl + "platform/selectPermissionIdAndName.action",
				function(data,status)
				{
					$("#supMenu").html("");
				var SupDataValue = eval("("+data+")")	
				var optionStr = "";
				
				for(var i = 0; i< SupDataValue.length;i ++){
					optionStr += "<option value = "+SupDataValue[i].permissionId+">"+SupDataValue[i].permissionName+"</option>";
				}
				$("#supMenu").html(optionStr);
				}
			);
}

//form表单value验证
function formValidate(addOrEdit){
	debugger
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
		var passwordRegExp = new RegExp("^[a-zA-Z]{1}[a-zA-Z0-9]*$");
		if(!passwordRegExp.test(loginPassword)){
			showMsg(jsLocale.get('user.list.form.save.error.loginPassword'));
			falg = false;
			return falg;
		}
	}
	return falg;
}

$(function(){
	//全局变量
	obj = {
		search:function(){
			debugger
			$('#userListTable').datagrid("load",{
				searchMsg:$.trim($("#searchMsg").val()),
				superiorName:$.trim($("#superiorName").val())
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
			debugger
			if($("#isAddorUpdate").val()=="add"){
				addMenu();
				if(isFalse){
					$("#userForm").form("submit",{
						url:baseUrl + 'platform/insertPermission.action',
						success:function(data){
							var msg = $.parseJSON(data).msg;
							if("success"==msg){
								showMsg(jsLocale.get('user.list.form.save.success'));
								obj.search();
								$("#userWindow").window('close');
							}
						}
					});
				}
			}else{
				updateMenu();
				if(isFalse){
					$("#userForm").form("submit",{
						url:baseUrl + 'platform/updatePermission.action',
						success:function(data){
							var msg = $.parseJSON(data).msg;
							if("success"==msg){
								showMsg(jsLocale.get('user.list.form.opt.success'));
								obj.search();
								$("#userWindow").window('close');
							}
						}
					});
				}
			}
		},
		
		enableOrDisableUser:function(id)
		{
			if(confirm(jsLocale.get("permission.del.tips"))){
				$.post(
						baseUrl + "platform/deletePermission.action?permissionId="+id,
						function(data,status)
						{
							var msg = $.parseJSON(data).msg;
							if("success"==msg){
								showMsg(jsLocale.get('alert.success'));
								obj.search();
							}
							else{
								showMsg(msg);
							}
						}
					);
			}
		}
	};
	
	
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
	
	//加载机构数据
	loadPartyCountryTree($("#partyId"));
	
	//加载角色数据
	$("#roleId").combobox({
		url:baseUrl + "platform/selectRoleForSelect.action",
		valueField:'roleId',
	    textField:'roleName',
		method:"post"
//			,multiple:"true"
	});
	
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
		url:baseUrl + 'platform/loadSelectAllPermissionData.action',
		idField:'permissionId',
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
	        {field: 'permissionId',  title: jsLocale.get('permission.list.gridhead.permissionId'),sortable:true,resizable:true,halign:'center',align:'left',width:30},
            {field: 'parentPermissionId',  title: jsLocale.get('permission.list.gridhead.permissionSName'),sortable:true,resizable:true,halign:'center',align:'left',width:80},  
            {field: 'permissionName',  title: jsLocale.get('permission.list.gridhead.permissionName'),sortable:true,resizable:true,halign:'center',align:'left',width:80},
            {field: 'labelKey',  title: jsLocale.get('permission.list.gridhead.LABEL_KEY'),sortable:true,resizable:true,halign:'center',align:'left',width:80},  
            {field: 'permissionSeq',  title: jsLocale.get('permission.list.gridhead.PERMISSION_SEQ'),sortable:true,resizable:true,halign:'center',align:'left',width:30},  
            {field: 'ismen',  title: jsLocale.get('permission.list.gridhead.IS_MENU'),sortable:true,resizable:true,halign:'center',align:'left',width:40},
            {field: 'comments',  title: jsLocale.get('permission.list.gridhead.COMMENTS'),sortable:true,resizable:true,halign:'center',align:'left',width:50},
            {field: 'permissionCode', title: jsLocale.get('permission.list.gridhead.PERMISSION_CODE'),sortable:true,resizable:true,halign:'center',align:'left',width:60},
            {field: 'permissionUrl',  title: jsLocale.get('permission.list.gridhead.PERMISSION_URL'),sortable:true,resizable:true,halign:'center',align:'left',width:80},
            {field: 'buttonId', title: jsLocale.get('permission.list.gridhead.BUTTON_ID'),sortable:true,resizable:true,halign:'center',align:'left',width:50},
        ]],
        toolbar:[{
        	text:jsLocale.get('permission.list.but.addpermission'),
            iconCls:'icon-add',
            handler:function(){
            	$('#userForm').form('reset');
            	$('#userForm').form({
            		onLoadSuccess:function(data){
            			debugger
            			setUnReadOnly($("#userWorkNum"));
                    	setUnReadOnly($("#userLoginId"));
                    	$("#userLoginId").textbox("enable");
                    	$("#ptr").show();
            		}
            	}).form("load",{addOrEdit:null});
                $("#userWindow").window('open');  
                loadSupData();
                $("#isAddorUpdate").val("add");
                isFalse = true;
            }
        }/*,'-',{
            text:jsLocale.get('user.list.but.addLdapUser'),  
            iconCls:'icon-add',  
            handler:function(){
            	
            }
        }*/,'-',{  
            text:jsLocale.get('permission.list.but.editpermission'),  
            iconCls:'icon-edit',  
            handler:function(){
            	var selectedRow = $("#userListTable").datagrid('getSelected');
            	if(null==selectedRow)
            	{
            		showMsg(jsLocale.get('user.list.but.modifyUser.checkUser'));
            		return;
            	}
            	
            	
            	$("#isAddorUpdate").val("update");
            	isFalse = true;
            	loadSupData();
            	var permissionId = selectedRow.permissionId;
            	$("#userForm").form({
            		onLoadSuccess:function(data)
            		{
            			setReadOnly($("#userLoginId"));
            			$("#addUserLoginId").val(permissionId);
            			$("#userLoginId").textbox("disable");
            			$("#ptr").hide();
            			$("#userWindow").window('open');
            			perName = $("#permissionName").val();
            		}
            	}).form('load',baseUrl+'platform/loadUpdatePermissionData.action?permissionId='+permissionId);
            }
        },'-',{  
                text:jsLocale.get('permission.list.but.deletepermission'),  
                iconCls:'icon-no',  
                handler:function(){
                	debugger
                	var selectedRow = $("#userListTable").datagrid('getSelected');
                	if(null==selectedRow){
                		showMsg(jsLocale.get('permission.list.delete.Select.prompt'));
                		return;
                	}
                	var permissionId = selectedRow.permissionId;
                	obj.enableOrDisableUser(permissionId);
                }
        }],
    onLoadSuccess: function (data) {
    		initDataGridToolBar();
    	}
    });
});
