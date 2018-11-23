$(function(){
	//全局变量
	obj = {
		//移到右边
	    add:function() {
	    	//获取选中的选项，删除并追加给对方
	        $('#selectLeft option:selected').appendTo('#selectRight');
	    },
	    //移到左边
	    remove:function() {
	        $('#selectRight option:selected').appendTo('#selectLeft');
	    },
	    //全部移到右边
	    addAll:function() {
	        //获取全部的选项,删除并追加给对方
	        $('#selectLeft option').appendTo('#selectRight');
	    },
	    //全部移到左边
	    removeAll:function() {
	        $('#selectRight option').appendTo('#selectLeft');
	    },
	    //双击选项
	    leftDBClick:function(me){
	        //获取全部的选项,删除并追加给对方
	        $("option:selected",me).appendTo('#selectRight'); //追加给对方
	    },
	    //双击选项
	    rightDBClick:function(me){
	       $("option:selected",me).appendTo('#selectLeft');
	    },
	    //清除左右选择框内容
	    clearLeftAndRight:function(){
	    	$("#searchUser").searchbox("setValue","");
	    	$("#selectLeft option").remove();
			$("#selectRight option").remove();
	    },
	    //加载表单编辑选择框内容
	    loadRoleUserData:function()
		{
	    	var roleId = $("#roleId").val();
	    	var searchUser = $("#searchUser").val();
			var param = {};
			param.roleId = roleId;
			param.searchUser = searchUser;
			$.post(
				baseUrl + "platform/loadRoleUserData.action",
				param,
				function(data,status)
				{
					var msg = data.msg;
					if("success"==msg){
						var rows = data.rows;
						$("#selectLeft option").remove();
						for(var i=0;i<rows.length;i++)
						{
							var row = rows[i];
							var userLoginId = row.userLoginId;
							var existSelectRight = $("#selectRight option[value='"+userLoginId+"']");
							if(existSelectRight && ""!=existSelectRight && null!=existSelectRight && existSelectRight.length>0)
							{
								continue;
							}
							else
							{
								var leftOrRight = row.leftOrRight;
								var _option = "<option value='"+userLoginId+"'>"+row.userName+"</option>"
								if("left"==leftOrRight)
								{
									$(_option).appendTo('#selectLeft');
								}
								else
								{
									$(_option).appendTo('#selectRight');
								}
							}
						}
					}
					else{
						showMsg(msg);
					}
				}
			);
		},
	    //查询列表
		search:function(){
			$('#roleListTable').datagrid("load",{
				searchMsg:$.trim($("#searchMsg").val())
			});
		},
		//关闭表单
		close:function(win){
			$("#"+win).window('close');
		},
		//保存表单
		save:function(){
			if(!$("#roleForm").form("validate"))
			{
				return;
			}
			var addOrEdit = $("#addOrEdit").val();
			if(!validateForm(addOrEdit)){
				return;
			}
			var formAction = "";
			if("update" == addOrEdit)
			{
				formAction = baseUrl + 'platform/updateRole.action';
			}
			else
			{
				var roleType = $("#roleType").combobox("getValue");
				if(null==roleType || ""==roleType)
				{
					return;
				}
				formAction = baseUrl + 'platform/insertRole.action';
			}
			
			$("#roleForm").form("submit",{
				url:formAction,
				onSubmit:function(param){
					var selectRight = $('#selectRight option');
					var allUserStr = "";
					for(var i=0;i<selectRight.length;i++)
					{
						var userLoginId = selectRight.eq(i).attr("value");
						allUserStr += userLoginId+";";
					}
					if(""!=allUserStr && null!=allUserStr)
					{
						allUserStr = allUserStr.substring(0,allUserStr.length-1);
					}
					param.allUserStr = allUserStr;
				},
				success:function(data){
					var msg = $.parseJSON(data).msg;
					if("success"==msg){
						showMsg(jsLocale.get('role.list.form.save.success'));
						obj.search();
						$("#roleWindow").window('close');
					}
					else if("exists"==msg){
						showMsg(jsLocale.get('role.list.form.save.exist'));
					}
					else{
						showMsg(msg);
					}
			    }
			});
		},
		//保存菜单权限
		savePermission:function(){
			var checkedNodes = $("#permissionTree").tree("getChecked");
			var uncheckedNodes = $("#permissionTree").tree("getChecked","unchecked");
			var indeterminateNodes = $("#permissionTree").tree("getChecked","indeterminate");
			
			var checkedNodesParam = initTreeCheckData(checkedNodes);
			var uncheckedNodesParam = initTreeCheckData(uncheckedNodes);
			var indeterminateNodesParam = initTreeCheckData(indeterminateNodes);
			
			var param = {};
			param.permissionRoleId = $("#permissionRoleId").val();
			param.checkedNodesParam = checkedNodesParam;
			param.uncheckedNodesParam = uncheckedNodesParam;
			param.indeterminateNodesParam = indeterminateNodesParam;
			$.post(
				baseUrl + "platform/insertRolePermission.action",
				param,
				function(data,status)
				{
					var msg = data.msg;
					if("success"==msg)
					{
						showMsg(jsLocale.get('role.list.permission.success'));
						$("#permissionTree").tree("reload");
						obj.close('permissionWindow');
					}
					else
					{
						showMsg(msg);
					}
				}
			);
		},
		//保存机构权限
		saveParty:function(){
			var checkedNodes = $("#partyTree").tree("getChecked");
			var checkedNodesParam = initTreeCheckData(checkedNodes);
			var param = {};
			param.permissionRoleId = $("#partyRoleId").val();
			param.checkedNodesParam = checkedNodesParam;
			$.post(
				baseUrl + "platform/insertRoleParty.action",
				param,
				function(data,status)
				{
					var msg = data.msg;
					if("success"==msg)
					{
						showMsg(jsLocale.get('role.list.party.success'));
						$("#partyTree").tree("reload");
						obj.close('partyWindow');
					}
					else
					{
						showMsg(msg);
					}
				}
			);
		}
	};
	
//	$("input",$("#roleName").next("span")).blur(function(){
//		var name = $("#roleName").textbox("getValue");
//		$.ajax({
//			url:baseUrl + "platform/isRoleNameSame.action",
//			type:"POST",
//			data:{"roleName":name},
//			success:function(data){
//				if(data.roleNum == "1"){
//					alert(jsLocale.get("role.list.form.errorMsg"));
//				}else{
//					alert(jsLocale.get("role.list.form.successMsg"));
//				}
//			}
//		});
//	});
	
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
	
	//给窗口添加事件,满足form表单元素自适应
	$("#roleWindow").window({
		'onOpen':function(){
			setFormWidth($('#roleForm'));
		},
		'onResize':function(){
			setFormWidth($('#roleForm'));
		}
	});
	
	//初始化表格
	$('#roleListTable').datagrid({
		fit:true,
		//title:'分组列表',
		iconCls:'icon-search',
		fitColumns:true,
		resizeHandle:'both',
		autoRowHeight:false,
		striped:true,
		method:'post',
		url:baseUrl + 'platform/loadRoleDataForGrid.action',
		idField:'roleId',
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
	        {field: 'roleId',  title: jsLocale.get('role.list.gridhead.roleId'),sortable:true,resizable:true,halign:'center',align:'left',width:70,hidden:true},
            {field: 'roleName',  title: jsLocale.get('role.list.gridhead.roleName'),sortable:true,resizable:true,halign:'center',align:'left',width:150},  
            {field: 'roleType',  title: jsLocale.get('role.list.gridhead.roleType'),sortable:true,resizable:true,halign:'center',align:'left',width:80},  
            {field: 'createBy',  title: jsLocale.get('role.list.gridhead.createBy'),sortable:true,resizable:true,halign:'center',align:'left',width:100},
            {field: 'createDate', title: jsLocale.get('role.list.gridhead.createDate'),sortable:true,resizable:true,halign:'center',align:'left',width:80,formatter:todate},
            {field: 'updateBy',  title: jsLocale.get('role.list.gridhead.updateBy'),sortable:true,resizable:true,halign:'center',align:'left',width:100},
            {field: 'updateDate', title: jsLocale.get('role.list.gridhead.updateDate'),sortable:true,resizable:true,halign:'center',align:'left',width:80,formatter:todate},
        ]],
        toolbar:[{
            text:jsLocale.get('role.list.but.addRole'), 
            iconCls:'icon-add',  
            handler:function(){
            	$('#roleForm').form('reset');
            	$("#roleId").val("");
            	$('#roleForm').form({
            		onLoadSuccess:function(data){
            			obj.clearLeftAndRight();
            			obj.loadRoleUserData();
            			$("#roleTypeRow").show();
            			setUnReadOnly($("#roleId"));
            		}
            	}).form("load",{addOrEdit:null});
                $("#roleWindow").window('open');  
            }
        },'-',{  
            text:jsLocale.get('role.list.but.modifyRole'),  
            iconCls:'icon-edit',  
            handler:function(){
            	var selectedRow = $("#roleListTable").datagrid('getSelected');
            	if(null==selectedRow)
            	{
            		showMsg(jsLocale.get('role.list.selectTips'));
            		return;
            	}
            	var roleId = selectedRow.roleId;
            	var _roleName = selectedRow.roleName;
            	$("#roleForm").form({
            		onLoadSuccess:function(data)
            		{
            			setReadOnly($("#roleId"));
            			$("#_roleName").val(_roleName);
            			$("#roleTypeRow").hide();
            			obj.clearLeftAndRight();
            			obj.loadRoleUserData();
            			$("#roleWindow").window('open');
            		}
            	}).form('load',baseUrl+'platform/loadUpdateRoleData.action?roleId='+roleId);
            	emptySelectedInfo();
            }
        },'-',{  
            text:jsLocale.get('role.list.but.deleteRole'),  
            iconCls:'icon-remove',  
            handler:function(){
            	if(isSelectingData()){
	            	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm.selected"),function(r){
	            		if(r){
			            	deleteRole();
	            		}
	            	});
            	};
            }
        },'-',{  
            text:jsLocale.get('role.list.but.configPermision'),  
            iconCls:'icon-edit',  
            handler:function(){
            	var selectedRow = $("#roleListTable").datagrid('getSelected');
            	if(null==selectedRow)
            	{
            		showMsg(jsLocale.get('role.list.selectTips'));
            		return;
            	}
            	var roleId = selectedRow.roleId;
            	$("#permissionRoleId").val(roleId);
            	
            	//初始化菜单树
            	$("#permissionTree").tree({
            		url : baseUrl + "platform/selectRolePermission.action",
            		method:"post",
            		checkbox:true,
            		cascadeCheck: true,
            		lines:true,
            		valueField: 'id',
            		textField: 'text',
            		onBeforeLoad:function(node, param){
            			param.roleId = roleId;
            		}
            	});
            	
            	$("#permissionWindow").window("open");
            	emptySelectedInfo();
            }
        },'-',{  
            text:jsLocale.get('role.list.but.configParty'),  
            iconCls:'icon-edit',  
            handler:function(){
            	var selectedRow = $("#roleListTable").datagrid('getSelected');
            	if(null==selectedRow)
            	{
            		showMsg(jsLocale.get('role.list.selectTips'));
            		return;
            	}
            	var roleId = selectedRow.roleId;
            	$("#partyRoleId").val(roleId);
            	
            	//初始化菜单树
            	$("#partyTree").tree({
            		url : baseUrl + "platform/selectRoleParty.action",
            		method:"post",
            		checkbox:true,
            		cascadeCheck: true,
            		lines:true,
            		valueField: 'id',
            		textField: 'text',
            		onBeforeLoad:function(node, param){
            			param.roleId = roleId;
            		}
            	});
            	
            	$("#partyWindow").window("open");
            	emptySelectedInfo();
            }
        }],
        onLoadSuccess: function (data) {
    		initDataGridToolBar();
    	}
    });
	
	//初始化表单的用户搜索框
	$("#searchUser").searchbox({
		width:200,
		searcher:function(value,name){ 
			obj.loadRoleUserData();
		}
	});
	
	//角色删除
	function deleteRole(){
		var selectedRow = $("#roleListTable").datagrid('getSelected');
		$.ajax({
			url:baseUrl + "platform/deleteRole.action",
			type:"POST",
			data:{"roleId":selectedRow.roleId},
			async: false,
			success:function(data){
				if(data.success){
					showMsg(locale("alert.success"));
					emptySelectedInfo();
					$("#roleListTable").datagrid('reload');
				}else{
					showMsg(result.msg);
				}
			}
		});
	};
	
	/**
	 * 清空选中信息
	 */
	function emptySelectedInfo(){
		$('#roleListTable').datagrid("unselectAll");
	};
	
	//是否选择处理数据
	function isSelectingData(){
		var selectedRow = $("#roleListTable").datagrid('getSelected');
    	if(null==selectedRow)
    	{
    		showMsg(jsLocale.get('role.list.selectTips'));
    		return false;
    	}
    	return true;
	}
	
	//组装选中函数
	function initTreeCheckData(nodes)
	{
		var rolePermissionIds = "";
		//组装半选中节点ID
		for(var i=0;i<nodes.length;i++)
		{
			var node = nodes[i];
			if(i==nodes.length-1)
			{
				rolePermissionIds += node.id;
			}
			else
			{
				rolePermissionIds += node.id + ";";
			}
		}
		return rolePermissionIds;
	}
});

//校验
function validateForm(addOrEdit){
	var falg = true;
	if(!"update" == addOrEdit){
		var roleType = $("#roleType").combobox("getValue");
		if("" == roleType){
			showMsg(jsLocale.get("role.list.form.errorMsg_0001"));
			return false;
		}
	}
	var roleName = $("#roleName").textbox("getValue");
	var _roleName = $("#_roleName").val();
	if("" == roleName){
		showMsg(jsLocale.get("role.list.form.errorMsg_0002"));
		return false;
	}else{
		$.ajax({
			url:baseUrl + "platform/isRoleNameSame.action",
			type:"POST",
			data:{"roleName":roleName},
			async: false,
			success:function(data){
				if("update" == addOrEdit){
					if(data.roleNum == "0"){
						falg = true;
					}else{
						if(data.roleName == _roleName){
							falg = true;
						}else{
							showMsg(jsLocale.get("role.list.form.errorMsg"));
							falg = false;
						}
					}
				}else{
					if(data.roleNum != "0"){
						showMsg(jsLocale.get("role.list.form.errorMsg"));
						falg = false;
					}else{
						falg = true;
					}
				}
			}
		});
		return falg;
	}
	return true;
}