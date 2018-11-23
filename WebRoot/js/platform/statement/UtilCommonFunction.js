	var emptySelectio = "<option value=''>All</option>";

	Common_util = {
		queryStoreLevel:function(objId){//根据国家查询门店等级
			$.ajax({
				url:baseUrl + "platform/storeLevelQuery.action",
				type:"POST",
				data:{'country':isHQRole == 'true' ? getCountryId():loginCountry},
				success:function(data){
					var obj =  eval('(' + data + ')');
					var optionStr = emptySelectio;
					for(var i = 0;i < obj.length;i ++){
						optionStr += "<option value='"+obj[i].id+"'>"+obj[i].levels+"</option>";
					}
					$('#'+objId).html(optionStr);
				}
			});
		}
	};



