$(function(){
	obj = {
		add:function(){
			var partyId = $("#partyTree").tree("getSelected").id;
			$("#partyForm").form("reset");
			$("#partyId").val("");
			$("#parentPartyId").val(partyId);
			setUnReadOnly($("#partyId"));
			$("#partyWindow").window('open');
		},
		edit:function(){
			var partyId = $("#partyTree").tree("getSelected").id;
			$("#partyForm").form({
        		onLoadSuccess:function(data)
        		{
        			$("#partyWindow").window('open');
        			setReadOnly($("#partyId"));
        		}
        	}).form('load',baseUrl+'platform/loadUpdatePartyData.action?partyId='+partyId);
		},
		del:function(){
			if(!confirm(jsLocale.get('party.del.tips')))
			{
				return;
			}
			var partyId = $("#partyTree").tree("getSelected").id;
			var param = {};
			param.partyId = partyId;
			$.post(
				baseUrl + "platform/deleteParty.action",
				param,
				function(data,status)
				{
					var msg = $.parseJSON(data).msg;
					if("success"==msg){
						showMsg(jsLocale.get('party.opt.success'));
						obj.refrash("parent");
					}
					else{
						showMsg(msg);
					}
				}
			);
		},
		refrash:function(flag){
			var selectedNode = $("#partyTree").tree("getSelected");
			var parentNode = $("#partyTree").tree("getParent",selectedNode.target);
			//如果flag==self刷新当前节点，其他刷新父节点
			if("self"==flag)
			{
				$("#partyTree").tree("reload",selectedNode.target);
			}
			else
			{
				$("#partyTree").tree("reload",parentNode.target);
			}
		},
		save:function(){
			if(!$("#partyForm").form("validate"))
			{
				return;
			}
			var formAction = "";
			if($("#partyId").next().find(".textbox-text").attr("readonly"))
			{
				formAction = baseUrl + 'platform/updateParty.action';
			}
			else
			{
				formAction = baseUrl + 'platform/insertParty.action';
			}
			$("#partyForm").form("submit",{
				url:formAction,
				onSubmit:function(){},
				success:function(data){
					var msg = $.parseJSON(data).msg;
					if("success"==msg){
						showMsg(jsLocale.get("party.opt.success"));
						obj.refrash("self");
						$("#partyWindow").window('close');
					}
					else{
						showMsg(msg);
					}
			    }
			});
		},
		close:function(){
			$("#partyWindow").window('close');
		}
	};
	
	//给窗口添加事件,满足form表单元素自适应
	$("#partyWindow").window({
		'onOpen':function(){
			setFormWidth($('#partyForm'));
		},
		'onResize':function(){
			setFormWidth($('#partyForm'));
		}
	});
	
	$("#partyTree").tree({
		url:baseUrl + "platform/loadPartyTreeNodeData.action",
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
			$('#partyTree').tree('select', node.target);
			
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
