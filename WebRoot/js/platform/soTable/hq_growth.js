$(function(){
	initializationLoad();
});

/**
 * 初始化加载
 */
function initializationLoad(){
	yearTabFunctionCut();
};

/**
 * 年度选项卡切换
 */
function yearTabFunctionCut(){
	queryYearGrowth('Total');
	$('#year_tab li a').click(function(){
		queryYearGrowth($(this).html());
	});
};

/**
 * 半年度选项卡切换
 */
function halfAYearTabFunctionCut(){
	queryHalfAYearGrowth('Total');
	$('#half_a_year_tab li a').click(function(){
		queryHalfAYearGrowth($(this).html());
	});
};

/**
 * 季度选项卡切换
 */
function quarterTabFunctionCut(){
	queryQuarterGrowth('Total');
	$('#quarter_tab li a').click(function(){
		queryQuarterGrowth($(this).html());
	});
};

/**
 * 同比年度
 * @param tab
 */
function queryYearGrowth(tab){
	var selectObj = getGrowthDateAndCoeffSelect(tab,'_year');
	var selectDateVal = selectObj.selectDateVal;
	var startDate = isStringNull(selectDateVal) ? new Date().getFullYear() + "-01-01" : selectDateVal + "-01-01";
	var endDate = isStringNull(selectDateVal) ? new Date().getFullYear() + "-12-31" : selectDateVal + "-12-31";
	
	var o = {};
	o.startDate = startDate;
	o.endDate = endDate;
	o.coeff = selectObj.coeff ;
	o.timeType = 'YEAR';
	o.tab = tab;
	o.selectDateVal = isStringNull(selectDateVal) ? new Date().getFullYear() : selectDateVal;
	o.subXcp = selectObj.subXcp;
	o.seriesXcp = selectObj.seriesXcp;
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/queryStateTimeSalesBycountry.action",
		type:"POST",
		data:o,
		success:function(data){
			loadingShowGrows(true);
			var dataArr = sortByObjArr(data.data);
			loadYearHtmlJoint(dataArr,o);
		}
	});
};

/**
 * 同比半年度
 * @param tab
 */
function queryHalfAYearGrowth(tab){
	var selectObj = getGrowthDateAndCoeffSelect(tab,'_halfAyear');
	var selectDateVal = selectObj.selectDateVal;
	var startDate = isStringNull(selectDateVal) ? getHalfAYearSelectDate(new Date().getFullYear(),tab)[0] : getHalfAYearSelectDate(selectDateVal,tab)[0];
	var endDate = isStringNull(selectDateVal) ? getHalfAYearSelectDate(new Date().getFullYear(),tab)[1] : getHalfAYearSelectDate(selectDateVal,tab)[1];
	
	var o = {};
	o.startDate = startDate;
	o.endDate = endDate;
	o.coeff = selectObj.coeff ;
	o.timeType = 'HALF_A_YEAR';
	o.selectHAY = getHalfAYearSelectDate(new Date().getFullYear(),tab)[2];
	o.tab = tab;
	o.selectDateVal = isStringNull(selectDateVal) ? new Date().getFullYear() : selectDateVal;
	o.subXcp = selectObj.subXcp;
	o.seriesXcp = selectObj.seriesXcp;
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/queryStateTimeSalesBycountry.action",
		type:"POST",
		data:o,
		success:function(data){
			loadingShowGrows(true);
			var dataArr = sortByObjArr(data.data);
			loadHalfAYearHtmlJoint(dataArr,o);
		}
	});
};

/**
 * 同比季度
 * @param tab
 */
function queryQuarterGrowth(tab){
	var selectObj = getGrowthDateAndCoeffSelect(tab,'_quarter');
	var selectDateVal = selectObj.selectDateVal;
	var startDate = isStringNull(selectDateVal) ? getQuarterSelectDate(new Date().getFullYear(),tab)[0] : getQuarterSelectDate(selectDateVal,tab)[0];
	var endDate = isStringNull(selectDateVal) ? getQuarterSelectDate(new Date().getFullYear(),tab)[1] : getQuarterSelectDate(selectDateVal,tab)[1];
	
	var o = {};
	o.startDate = startDate;
	o.endDate = endDate;
	o.coeff = selectObj.coeff ;
	o.timeType = 'QUARTER';
	o.selectHAY = getQuarterSelectDate(new Date().getFullYear(),tab)[2];
	o.tab = tab;
	o.selectDateVal = isStringNull(selectDateVal) ? new Date().getFullYear() : selectDateVal;
	o.subXcp = selectObj.subXcp;
	o.seriesXcp = selectObj.seriesXcp;
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/queryStateTimeSalesBycountry.action",
		type:"POST",
		data:o,
		success:function(data){
			loadingShowGrows(true);
			var dataArr = sortByObjArr(data.data);
			loadQuarterHtmlJoint(dataArr,o);
		}
	});
};

/**
 * 半年度选择返回时间
 * @param h
 */
function getHalfAYearSelectDate(h,tab){
	if(tab == 'Total'){
		return getHalfAYearSelectDateStrByYear(h,$('#total_halfAyear_select').val());
	}else if('Smart' == tab){
		return getHalfAYearSelectDateStrByYear(h,$('#smart_halfAyear_select').val());
	} else if('UD' == tab){
		return getHalfAYearSelectDateStrByYear(h,$('#ud_halfAyear_select').val());
	} else if('Big' == tab){
		return getHalfAYearSelectDateStrByYear(h,$('#big_halfAyear_select').val());
	} else if('Curved' == tab){
		return getHalfAYearSelectDateStrByYear(h,$('#curved_halfAyear_select').val());
	} else if('X/C/P' == tab){
		return getHalfAYearSelectDateStrByYear(h,$('#xcp_halfAyear_select').val());
	}
};

/**
 * 半年度时间截取
 * @param d
 * @returns {Array}
 */
function getHalfAYearSelectDateStrByYear(d,yearStyle){
	if('H1' == yearStyle){
		return [d + '-01-01', d + '-06-30',yearStyle];
	}else{
		return [d + '-07-01', d + '-12-31',yearStyle];
	}
};

/**
 * 季度选择返回时间
 * @param h
 */
function getQuarterSelectDate(h,tab){
	if(tab == 'Total'){
		return getQuarterSelectDateStrByYear(h,$('#total_quarter_select').val());
	}else if('Smart' == tab){
		return getQuarterSelectDateStrByYear(h,$('#smart_quarter_select').val());
	} else if('UD' == tab){
		return getQuarterSelectDateStrByYear(h,$('#ud_quarter_select').val());
	} else if('Big' == tab){
		return getQuarterSelectDateStrByYear(h,$('#big_quarter_select').val());
	} else if('Curved' == tab){
		return getQuarterSelectDateStrByYear(h,$('#curved_quarter_select').val());
	} else if('X/C/P' == tab){
		return getQuarterSelectDateStrByYear(h,$('#xcp_quarter_select').val());
	}
};

/**
 * 季度时间截取
 * @param d
 * @returns {Array}
 */
function getQuarterSelectDateStrByYear(d,yearStyle){
	if('Q1' == yearStyle){
		return [d + '-01-01', d + '-03-31',yearStyle];
	}else if('Q2' == yearStyle){
		return [d + '-04-01', d + '-06-30',yearStyle];
	}else if('Q3' == yearStyle){
		return [d + '-07-01', d + '-09-30',yearStyle];
	}else{
		return [d + '-10-01', d + '-12-31',yearStyle];
	}
};

/**
 * 同比表格限制信息
 * @param tab
 * @param time_dime
 * @returns {___anonymous1184_1185}
 */
function getGrowthDateAndCoeffSelect(tab,time_dime){
	var o = {};
	var coeff;
	var selectDateVal;
	if(tab == 'Total'){
		selectDateVal = $('#totalGrowthDate' + time_dime).val();
		coeff = $('#totalGrowthCoeff' + time_dime).is(":checked");
	}else if('Smart' == tab){
		selectDateVal = $('#smartGrowthDate' + time_dime).val();
		coeff = $('#smartGrowthCoeff' + time_dime).is(":checked");
	} else if('UD' == tab){
		selectDateVal = $('#uDGrowthDate' + time_dime).val();
		coeff = $('#uDrowthCoeff' + time_dime).is(":checked");
	} else if('Big' == tab){
		selectDateVal = $('#bigGrowthDate' + time_dime).val();
		coeff = $('#bigGrowthCoeff' + time_dime).is(":checked");
	} else if('Curved' == tab){
		selectDateVal = $('#curvedGrowthDate' + time_dime).val();
		coeff = $('#curvedGrowthCoeff' + time_dime).is(":checked");
	} else if('X/C/P' == tab){
		var subXcp = $('#subXcp' + time_dime).val();
		var seriesXcp = $('#SeriesXcp' + time_dime).val();
		o.subXcp = subXcp;
		o.seriesXcp = seriesXcp;
		selectDateVal = $('#xCPGrowthDate' + time_dime).val();
		coeff = $('#xCPGrowthCoeff' + time_dime).is(":checked");
	}
	o.coeff = coeff;
	o.selectDateVal = selectDateVal;
	return o;
};

/**
 * 字符串是否为空或null
 * @param string
 * @returns
 */
function isStringNull (string){
	return (typeof(string) != "undefined" && string != "" && string != null) ? false : true;
};

/**
 * 总部年度同比加载图片
 * @param is
 */
function loadingShowGrows(is){
	if(is){
		$("#loadingHQGrandImport").hide();
	}else{
		$("#loadingHQGrandImport").show();
	}
}

/**
 * XCP加载
 * @param target
 * @param time_dime
 */
function SeriesXcp(target,time_dime){
	var line=$(target).val();
	if(line==null || line==""){
		$('#subXcp'+time_dime).html('');
		$('#subXcp'+time_dime).html("<option value=''> </option>");
		return;
	}else {
		line=line;
	}
	var o = {};
	o.line=line;
	$.ajax({
		url:baseUrl + "platform/selectXCPLine.action",
		type:"POST",
		data:o,
		success:function(data){
			var STring = $.parseJSON(data);
			if(STring!=null&&STring.length>0){
				$('#subXcp' + time_dime).html('');
				var option="<option value=''>All</option>";
				for(var i=0;i<STring.length;i++){
					option+="<option value='"+STring[i].PVALUE+"'>"+STring[i].PVALUE+"</option>";
				}
				$('#subXcp' + time_dime).html(option);
			}else{
				$('#subXcp' + time_dime).html("<option value=''></option>");
			}
		}
	});
}

/**
 * 整合排序数组
 * @param arr
 * @param attr
 * @returns
 */
function sortByObjArr(dataArr){
	if(dataArr.length == 0){
		return [];
	}
	var allArr = [];
	var arr = [];
	var center = dataArr[0].center;
	for(var i = 0;i < dataArr.length; i++){
		if(center == dataArr[i].center){
			arr.push(dataArr[i]);
			if(dataArr.length - i == 1){
				arr = sortByObjArrAttr(arr);
				$(arr).each(function(){
					allArr.push(this);
				});
			}
		}else{
			arr = sortByObjArrAttr(arr);
			$(arr).each(function(){
				allArr.push(this);
			});
			center = dataArr[i].center;
			arr = [];
			i --;
		}
	}
	return allArr;
};

/**
 * 根据对象数组属性排序
 * @param arr
 * @param attr
 * @returns
 */
function sortByObjArrAttr(arr){
	arr.sort(function(a,b){  
        return b.year_growth - a.year_growth;  
    })  
    return arr;
};
