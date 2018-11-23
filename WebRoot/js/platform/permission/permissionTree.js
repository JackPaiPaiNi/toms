$(function(){
	obj = {
		add:function(){
			var permissionId = $("#permissionTree").tree("getSelected").id;
			$("#permissionForm").form("reset");
			$("#permissionId").val("");
			$("#parentPermissionId").val(permissionId);
			setUnReadOnly($("#permissionId"));
			$("#permissionWindow").window('open');
		},
		edit:function(){
			var permissionId = $("#permissionTree").tree("getSelected").id;
			$("#permissionForm").form({
        		onLoadSuccess:function(data)
        		{
        			$("#permissionWindow").window('open');
        			setReadOnly($("#permissionId"));
        		}
        	}).form('load',baseUrl+'platform/loadUpdatePermissionData.action?permissionId='+permissionId);
		},
		del:function(){
			if(!confirm(jsLocale.get('permission.del.tips')))
			{
				return;
			}
			var permissionId = $("#permissionTree").tree("getSelected").id;
			var param = {};
			param.permissionId = permissionId;
			$.post(
				baseUrl + "platform/deletePermission.action",
				param,
				function(data,status)
				{
					var msg = $.parseJSON(data).msg;
					if("success"==msg){
						showMsg(jsLocale.get('permission.opt.success'));
						obj.refrash("parent");
					}
					else{
						showMsg(msg);
					}
				}
			);
		},
		refrash:function(flag){
			var selectedNode = $("#permissionTree").tree("getSelected");
			var parentNode = $("#permissionTree").tree("getParent",selectedNode.target);
			//如果flag==self刷新当前节点，其他刷新父节点
			if("self"==flag)
			{
				$("#permissionTree").tree("reload",selectedNode.target);
			}
			else
			{
				$("#permissionTree").tree("reload",parentNode.target);
			}
		},
		save:function(){
			if(!$("#permissionForm").form("validate"))
			{
				return;
			}
			
			var formAction = "";
			if($("#permissionId").next().find(".textbox-text").attr("readonly"))
			{
				formAction = baseUrl + 'platform/updatePermission.action';
			}
			else
			{
				formAction = baseUrl + 'platform/insertPermission.action';
			}
			$("#permissionForm").form("submit",{
				url:formAction,
				onSubmit:function(){},
				success:function(data){
					var msg = $.parseJSON(data).msg;
					if("success"==msg){
						showMsg(jsLocale.get('permission.opt.success'));
						obj.refrash("self");
						$("#permissionWindow").window('close');
					}
					else{
						showMsg(msg);
					}
			    }
			});
		},
		close:function(){
			$("#permissionWindow").window('close');
		}
	};
	
	//给窗口添加事件,满足form表单元素自适应
	$("#permissionWindow").window({
		'onOpen':function(){
			setFormWidth($('#permissionForm'));
		},
		'onResize':function(){
			setFormWidth($('#permissionForm'));
		}
	});
	
	$("#permissionTree").tree({
		url:baseUrl + "platform/loadPermissionTreeNodeData.action",
		method:"post",
		animate:true,
		lines:true,
		dnd:false,
		onBeforeLoad:function(node,param){
			if(null==node)
			{
				param.node = context.TREE_ROOT;
			}
			else
			{
				param.node = node.id;
			}
		},
		onContextMenu:function(e,node){
			e.preventDefault();
			$('#permissionTree').tree('select', node.target);
			
			if(typeof(canEdit)!="undefined" && "true"==canEdit)
			{
				//系统管理员或分公司管理员，拥有编辑权限
				// 显示快捷菜单
				$('#mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
			}
		}
	});
});