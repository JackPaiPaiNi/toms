/**
 * 依赖于easyui的公共方法,此文件必须在easyui后面引用
 */
function onEasyGridHeadMenu(e,field){
	e.preventDefault(); 
	var fields=$(this).datagrid('getColumnFields');
	if($("#header_menu").length==0){
		$("<div id='header_menu'></div>").appendTo('body');
		$("#header_menu").html("");
		var grid=$(this);
		for ( var i = 0; i < fields.length; i++) { 
			var fildOption = $(this).datagrid('getColumnOption', fields[i]);
			if (!fildOption.hidden) {
				//加到菜单中
				$('<div iconCls="icon-ok" field="' + fields[i] + '">'+fildOption.title+'</div>').appendTo("#header_menu");
			}else{
				$('<div iconCls="icon-empty" field="' + fields[i] + '">'+fildOption.title+'</div>').appendTo("#header_menu");
			}
		}
		$("#header_menu").menu({
			left:e.pageX,
			top:e.pageY,
			onClick:function(item){
				var field = $(item.target).attr('field'); 
				if (item.iconCls == 'icon-ok'){
					$(this).menu('setIcon', { target : item.target, iconCls : 'icon-empty' }); 
					grid.datagrid('hideColumn', field); 
				}else{
					$(this).menu('setIcon', { target : item.target, iconCls : 'icon-ok' }); 
					grid.datagrid('showColumn', field); 
				}
			}
		});
	}			
	$("#header_menu").menu("show",{
		left:e.pageX,
		top:e.pageY
	});
}
//提示信息
function showMsg(msg,title,type){
	title=title||locale("alert.title")
	type=type||"info";
	$.messager.alert(title,msg,type);
}
//国家省市县乡五级数据联动:参数均是easyui.combobox
//_searchFlag 参数 添加 全部选项  参数值 all 添加,  null 是不添加
function initDistrictComponents($country,$province,$city,$county,$town,$table,_searchFlag){
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
	    	$("#partyId").combobox("clear");
	    	loadPartyCombo(countryId);
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
	    		$table.datagrid({queryParams:qryParms});//重新加载
	    	}
	    }	
	});
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
