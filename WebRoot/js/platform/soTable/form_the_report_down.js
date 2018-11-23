
$(document).ready(function() {
	
	//getGroCountry();
	queryYearDataByRegi();
	selectPartyCus();
	
	$('#month_region_head_TAB li a').click(function(){
		queryMonthDataByRegi($(this).html());
	});
	$('#year_region_head_TAB li a').click(function(){
		queryYearDataByRegi($(this).html());
	});
	
	$('#chou_region_head_TAB li a').click(function(){
		if('Total' != $(this).html()){
			queryChouDataByRegi($(this).html());
		}else{
			queryWeekDataByRegi();
		}
	});
	
	
	$('#month_acfo_head_TAB li a').click(function(){
		qeryMonthDataByACFO($(this).html());
	});
	
	$('#week_acfo_head_TAB li a').click(function(){
		qeryWeekDataByACFO($(this).html());
	});
	
	$('#year_acfo_head_TAB li a').click(function(){
		qeryYearDataByACFO($(this).html());
	});
	
	
});

function getAcfoHeadTabDate(tab,time_dime){//获取月度督导选项卡选择时间、是否还原系数
	var o = {};
	var coeff;
	var selectDateVal;
	var xcpWhere;
	var big;
	if(!isStringNull(tab) || tab == 'ALL'){
		selectDateVal = $('#selectDateQueryAcfoTotal' + time_dime).val();
		coeff = $('#acfo_total_Check' + time_dime).is(":checked");
	}else if('Smart' == tab){
		selectDateVal = $('#selectDateQueryAcfoSmart' + time_dime).val();
		coeff = $('#acfo_Smart_Check' + time_dime).is(":checked");
	} else if('UD' == tab){
		selectDateVal = $('#selectDateQueryAcfoUD' + time_dime).val();
		coeff = $('#acfo_UD_Check' + time_dime).is(":checked");
	} else if('Big' == tab){
		selectDateVal = $('#selectDateQueryAcfoBig' + time_dime).val();
		coeff = $('#acfo_Big_Check' + time_dime).is(":checked");
		big = $('#ACFOMonthBigAttr' + time_dime).val();
	} else if('Curved' == tab){
		selectDateVal = $('#selectDateQueryAcfoCurved' + time_dime).val();
		coeff = $('#acfo_Curved_Check' + time_dime).is(":checked");
	} else if('X/C/P' == tab){
		var subXcp = $('#subACFOXcp' + time_dime).val();
		if(isStringNull(subXcp)){
			xcpWhere =subXcp;// " and pr.`product_line` like '%"+ subXcp +"%'";
		}else{
			var seriesXcp = $('#SeriesACFOXcp' + time_dime).val();
			if(isStringNull(seriesXcp)){
				xcpWhere =seriesXcp;// " and pr.`product_line` like '%"+ seriesXcp +"%'";
			}else{
				xcpWhere = "";//" and (pr.`product_line` like '%X%' or pr.`product_line` like '%C%' or pr.`product_line` like '%P%' ) ";
			}
		}
		
		selectDateVal = $('#selectDateQueryAcfoXCP' + time_dime).val();
		coeff = $('#acfo_XCP_Check' + time_dime).is(":checked");
	}
	o.xcpWhere = xcpWhere;
	o.coeff = coeff;
	o.selectDateVal = selectDateVal;
	o.big = big;
	return o;
}

function setDataByAcfoHtml(tab,leftDataHtml,rightDataHtml,time_dime,endDate){
	var d = endDate.split('-')[0] * 1;
	var m = endDate.split('-')[1];
	
	if(!isStringNull(tab) || tab == 'Total'){
		setShowIsNull('ACFOTotalHead' + time_dime,'ACFOTotalLeft' + time_dime,'ACFOTotalRight' + time_dime,null);
		$('#ACFOTotalHead' + time_dime).html(leftDataHtml);
		$('#ACFOTotalLeft' + time_dime).html(rightDataHtml);
		$('#ACFOTotalRight' + time_dime).html(getACFORightHeadHtml(d,(d - 1),m));
		setScrollBarShift('ACFOTotalLeftDiv' + time_dime,'ACFOTotalRightDiv' + time_dime);
		widthlala('ACFOTotalLeftDiv' + time_dime,'ACFOTotalRightDiv' + time_dime);
	}else if('Smart' == tab){
		setShowIsNull('ACFOSmartHead' + time_dime,'ACFOSmartLeft' + time_dime,'ACFOSmartRight' + time_dime,null);
		$('#ACFOSmartHead' + time_dime).html(leftDataHtml);
		$('#ACFOSmartLeft' + time_dime).html(rightDataHtml);
		$('#ACFOSmartRight' + time_dime).html(getACFORightHeadHtml(d,(d - 1),m));
		setScrollBarShift('ACFOSmartLeftDiv' + time_dime,'ACFOSmartRightDiv' + time_dime);
		widthlala('ACFOSmartLeftDiv' + time_dime,'ACFOSmartRightDiv' + time_dime);
	}else if('UD' == tab){
		setShowIsNull('ACFOUDHead' + time_dime,'ACFOUDLeft' + time_dime,'ACFOUDRight' + time_dime,null);
		$('#ACFOUDHead' + time_dime).html(leftDataHtml);
		$('#ACFOUDLeft' + time_dime).html(rightDataHtml);
		$('#ACFOUDRight' + time_dime).html(getACFORightHeadHtml(d,(d - 1),m));
		setScrollBarShift('ACFOUDLeftDiv' + time_dime,'ACFOUDRightDiv' + time_dime);
		widthlala('ACFOUDLeftDiv' + time_dime,'ACFOUDRightDiv' + time_dime);
	}else if('Big' == tab){
		setShowIsNull('ACFOBigHead' + time_dime,'ACFOBigLeft' + time_dime,'ACFOBigRight' + time_dime,null);
		$('#ACFOBigHead' + time_dime).html(leftDataHtml);
		$('#ACFOBigLeft' + time_dime).html(rightDataHtml);
		$('#ACFOBigRight' + time_dime).html(getACFORightHeadHtml(d,(d - 1),m));
		setScrollBarShift('ACFOBigLeftDiv' + time_dime,'ACFOBigRightDiv' + time_dime);
		widthlala('ACFOBigLeftDiv' + time_dime,'ACFOBigRightDiv' + time_dime);
	} else if('Curved' == tab){
		setShowIsNull('ACFOCurvedHead' + time_dime,'ACFOCurvedLeft' + time_dime,'ACFOCurvedRight' + time_dime,null);
		$('#ACFOCurvedHead' + time_dime).html(leftDataHtml);
		$('#ACFOCurvedLeft' + time_dime).html(rightDataHtml);
		$('#ACFOCurvedRight' + time_dime).html(getACFORightHeadHtml(d,(d - 1),m));
		setScrollBarShift('ACFOCurvedLeftDiv' + time_dime,'ACFOCurvedRightDiv' + time_dime);
		widthlala('ACFOCurvedLeftDiv' + time_dime,'ACFOCurvedRightDiv' + time_dime);
	}else if('X/C/P' == tab){
		setShowIsNull('ACFOXCPHead' + time_dime,'ACFOXCPLeft' + time_dime,'ACFOXCPRight' + time_dime,null);
		$('#ACFOXCPHead' + time_dime).html(leftDataHtml);
		$('#ACFOXCPLeft' + time_dime).html(rightDataHtml);
		$('#ACFOXCPRight' + time_dime).html(getACFORightHeadHtml(d,(d - 1),m));
		setScrollBarShift('ACFOXCPLeftDiv' + time_dime,'ACFOXCPRightDiv' + time_dime);
		widthlala('ACFOXCPLeftDiv' + time_dime,'ACFOXCPRightDiv' + time_dime);
	}
	moveLeft_Left();
}

function qeryWeekDataByACFO(tab){
	
	var selectDateVal = getAcfoHeadTabDate(tab,'_week').selectDateVal;
	var beginDate;
	var endDate;
	
	if(!isStringNull(selectDateVal)){
		beginDate = Common_date.getMondayAndWeekend()[0];
		endDate = Common_date.getMondayAndWeekend()[1];
	}else{
		beginDate = selectDateVal.split(' - ')[0];
		endDate = selectDateVal.split(' - ')[1];
	}
	
	var o = {};
	o.beginDate = beginDate;
	o.engDate = endDate;
	o.coeff = getAcfoHeadTabDate(tab,'_week').coeff ;
	o.tab = tab;
	o.big = getAcfoHeadTabDate(tab,'_week').big;
	o.xcpWhere = getAcfoHeadTabDate(tab,'_week').xcpWhere;
	
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/regionalGrowthPerformance.action",
		type:"POST",
		data:o,
		success:function(data){
			var ACFOleftDataHtml = getLeftMonthHeadACFODataHtml() + setLeftACFODataHtml(data.data);
			var ACFOrightDataHtml =  setACFORightDataHtml(data.data);
			loadingShowGrows(true);
			setDataByAcfoHtml(tab,ACFOleftDataHtml,ACFOrightDataHtml,'_week',endDate);
		}
	});
}

function qeryMonthDataByACFO(tab){
	var selectDateVal = getAcfoHeadTabDate(tab,'_month').selectDateVal;
	var beginDate;
	var endDate;
	
	if(!isStringNull(selectDateVal)){
		beginDate = new Date().getFullYear() + "-" + (new Date().getMonth()+1) + "-01";
		endDate = getSelectDate(beginDate);
	}else{
		beginDate = selectDateVal + "-01";
		endDate = getSelectDate(beginDate);
	}
	
	var o = {};
	o.beginDate = beginDate;
	o.engDate = endDate;
	o.coeff = getAcfoHeadTabDate(tab,'_month').coeff ;
	o.tab = tab;
	o.big = getAcfoHeadTabDate(tab,'_month').big;
	o.xcpWhere = getAcfoHeadTabDate(tab,'_month').xcpWhere;
	
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/regionalGrowthPerformance.action",
		type:"POST",
		data:o,
		success:function(data){
			var ACFOleftDataHtml = getLeftMonthHeadACFODataHtml() + setLeftACFODataHtml(data.data);
			var ACFOrightDataHtml =  setACFORightDataHtml(data.data);
			loadingShowGrows(true);
			setDataByAcfoHtml(tab,ACFOleftDataHtml,ACFOrightDataHtml,'_month',endDate);
		}
	});
}

function qeryYearDataByACFO(tab){
	var selectDateVal = getAcfoHeadTabDate(tab,'_year').selectDateVal;
	var beginDate;
	var endDate;
	
	if(!isStringNull(selectDateVal)){
		beginDate = new Date().getFullYear() + "-01-01";
		endDate = new Date().getFullYear() + "-12-31";
	}else{
		beginDate = selectDateVal + "-01-01";
		endDate = selectDateVal + "-12-31";
	}
	
	var o = {};
	o.beginDate = beginDate;
	o.engDate = endDate;
	o.coeff = getAcfoHeadTabDate(tab,'_year').coeff ;
	o.tab = tab;
	o.big = getAcfoHeadTabDate(tab,'_year').big;
	o.xcpWhere = getAcfoHeadTabDate(tab,'_year').xcpWhere;
	
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/regionalGrowthPerformance.action",
		type:"POST",
		data:o,
		success:function(data){
			var ACFOleftDataHtml = getLeftMonthHeadACFODataHtml() + setLeftACFODataHtml(data.data);
			var ACFOrightDataHtml =  setACFORightDataHtml(data.data);
			loadingShowGrows(true);
			setDataByAcfoHtml(tab,ACFOleftDataHtml,ACFOrightDataHtml,'_year',endDate);
		}
	});
}

function setACFORightDataHtml(obj){
	var tbodyHtml = "<tbody>";
	
	var allVal = [0,0,0,0,0,0,0,0,0,0,0,0];
	for(var i=0;i<obj.length;i++){
		allVal[0] += accumulativeJudgment(obj[i].FPS_YR_lastYear);
		allVal[1] += accumulativeJudgment(obj[i].YearFPS);
		allVal[2] += accumulativeJudgment(obj[i].TOTAL_TV_Qty_lastYear);
		allVal[3] += accumulativeJudgment(obj[i].YearQty);
		allVal[4] += accumulativeJudgment(obj[i].Qty_Growth);
		allVal[5] += accumulativeJudgment(obj[i].TOTAL_TV_Amt_lastYear);
		allVal[6] += accumulativeJudgment(obj[i].YearAmt);
		allVal[7] += accumulativeJudgment(obj[i].Amt_Growth);
		
		tbodyHtml += "<tr>" + setTDRight(obj[i].FPS_YR_lastYear);
		tbodyHtml += setTDRight(obj[i].YearFPS);
		tbodyHtml += setTDRight(obj[i].TOTAL_TV_Qty_lastYear);
		tbodyHtml += setTDRight(obj[i].YearQty);
		tbodyHtml += growthRate(obj[i].Qty_Growth * 100);
		tbodyHtml += setTDRight(obj[i].TOTAL_TV_Amt_lastYear);
		tbodyHtml += setTDRight(obj[i].YearAmt);
		tbodyHtml += growthRate(obj[i].Amt_Growth * 100);
		
		tbodyHtml += setTDRight(getAvg(accumulativeJudgment(obj[i].FPS_YR_lastYear),accumulativeJudgment(obj[i].TOTAL_TV_Qty_lastYear))); 
		tbodyHtml += setTDRight(getAvg(accumulativeJudgment(obj[i].YearFPS),accumulativeJudgment(obj[i].YearQty))); 
		tbodyHtml += setTDRight(getAvg(accumulativeJudgment(obj[i].FPS_YR_lastYear),accumulativeJudgment(obj[i].TOTAL_TV_Amt_lastYear))); 
		tbodyHtml += setTDRight(getAvg(accumulativeJudgment(obj[i].YearFPS),accumulativeJudgment(obj[i].YearAmt)))+ "</tr>";
		allVal[8] += getAvg(accumulativeJudgment(obj[i].FPS_YR_lastYear),accumulativeJudgment(obj[i].TOTAL_TV_Qty_lastYear));
		allVal[9] += getAvg(accumulativeJudgment(obj[i].YearFPS),accumulativeJudgment(obj[i].YearQty)); 
		allVal[10] += getAvg(accumulativeJudgment(obj[i].FPS_YR_lastYear),accumulativeJudgment(obj[i].TOTAL_TV_Amt_lastYear));
		allVal[11] += getAvg(accumulativeJudgment(obj[i].YearFPS),accumulativeJudgment(obj[i].YearAmt));
		
	}
	
	tbodyHtml += "<tr>";
	for(var i =0;i<allVal.length;i++){
		if(i == 4){
			tbodyHtml += growthRate(growthRateFormula(allVal[2],allVal[3]));
		}else if(i == 7){
			tbodyHtml += growthRate(growthRateFormula(allVal[5],allVal[6]));
		}else{
			tbodyHtml += setTDRight(allVal[i]);
		}
	}
	tbodyHtml += "</tr>";
	return tbodyHtml += "</tbody>";
};

function getACFORightHeadHtml(thisYear,lastYear,month){
	return "<thead>" +
				"<tr>" +
					"<th class='lightBlue' colspan='2' style='width: 200px;'>FPS</th>" +
					"<th class='lightBlue' colspan='3' style='width: 301px;'>Total Flat Panel TV Quantity</th>" +
					"<th class='lightBlue' colspan='3' style='width: 301px;'>Total Amount</th>" +
					"<th class='lightBlue' colspan='4' style='width: 402px;'>SELL-OUT EFFICIENSY / FPS</th>"+
				"</tr>" +
				"<tr>" +
					"<th class='bluegrey'>Yr-"+ lastYear +"</th>" +
					"<th class='bluegrey'>Yr-"+ thisYear +"</th>" +
					"<th class='bluegrey'>" + getENMonth(month) + " "+ lastYear +"</th>" +
					"<th class='bluegrey'>" + getENMonth(month) + " "+ thisYear +"</th>" +
					"<th class='bluegrey'>Growth</th>" +
					"<th class='bluegrey'>" + getENMonth(month) + " "+ lastYear +"</th>" +
					"<th class='bluegrey'>" + getENMonth(month) + " "+ thisYear +"</th>" +
					"<th class='bluegrey'>Growth</th>" +
					"<th class='bluegrey'>"+ lastYear +" Ave.qty</th>" +
					"<th class='bluegrey'>"+ thisYear +" Ave.qty</th>" +
					"<th class='bluegrey'>"+ lastYear +" Ave.amt</th>" +
					"<th class='bluegrey'>"+ thisYear +" Ave.amt</th>" +
				"</tr>" +
			"</thead>";
};

function setLeftACFODataHtml(obj){
	var html = '<tbody>';
	for(var i=0;i<obj.length;i++){
		html += "<tr><td>"+ (i + 1) +"</td>";
		html += "<td>"+ obj[i].ACFO +"</td></tr>";
	}
	html += "<tr>";
	html += "<td>TOTAL</td></tr>";
	return html += '</tbody>';
};

function getLeftMonthHeadACFODataHtml(){
	return "<thead>" +
				"<tr class='bluegrey'>" +
					"<th rowspan='2'>RANK</th>" +
					"<th rowspan='2'>Acfo</th>" +
				"</tr>" +
				"<tr></tr>" +
			"</thead>";
};


function queryWeekDataByRegi(){
	loadWeekMenu();//加载周度选项卡
	var selectDateVal = $('#regionWeekData').val();
	var beginDate;
	var endDate;
	if(!isStringNull(selectDateVal)){
		beginDate = Common_date.getMondayAndWeekend()[0];
		endDate = Common_date.getMondayAndWeekend()[1];
	}else{
		beginDate = selectDateVal.split(' - ')[0];
		endDate = selectDateVal.split(' - ')[1];
	}
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/regionalGrowthPerformanceByPartyId.action",
		type:"POST",
		data:{"beginDate":beginDate,'engDate':endDate,'coeff':$('#week_region_head_Check').is(":checked")},
		success:function(data){
			var leftDataHtml = getLeftHeadHTML() + setLeftDataHtml(data.data);
			
			var d = endDate.split('-')[0] * 1;
			var m = beginDate.split('-')[1];
			var rightDataHtml = getRightHeadHtml(d,(d - 1),m) + setRightDataHtml(data.data);
			loadingShowGrows(true);
			$('#weekRegLeftGro').html(leftDataHtml);
			$('#weekRegRightGro').html(rightDataHtml);
			//setScrollBarShift('weekRegLeftGro','weekRegRightGro');
			showRegionalName();
		}
	});
}

//chou --> regional head
function queryChouDataByRegi(tab){
	var selectDateVal = getRegionHeadTabDate(tab,'_week').selectDateVal ;
	var beginDate;
	var endDate;
	
	if(!isStringNull(selectDateVal)){
		beginDate = Common_date.getMondayAndWeekend()[0];
		endDate = Common_date.getMondayAndWeekend()[1];
	}else{
		beginDate = selectDateVal.split(' - ')[0];
		endDate = selectDateVal.split(' - ')[1];
	}
	
	var o = {};
	o.beginDate = beginDate;
	o.engDate = endDate;
	o.coeff = getRegionHeadTabDate(tab,'_week').coeff ;
	o.tab = tab;
	o.big = $('#regiHeadBigAttr_week').val();
	o.xcpWhere = getRegionHeadTabDate(tab,'_week').xcpWhere;
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/regionalGrowthPerformanceByPartyId.action",
		type:"POST",
		data:o,
		success:function(data){
			var leftDataHtml = getLeftHeadHTML() + setLeftDataHtml(data.data);
			var d = endDate.split('-')[0] * 1;
			var m = endDate.split('-')[1];
			var rightDataHtml = getRightHeadHtml(d,(d - 1),m) + setRightDataHtml(data.data);
			loadingShowGrows(true);
			setDataByRegiHtml(tab,leftDataHtml,rightDataHtml,'_week');
			showRegionalName();
		}
	});
}

//year --> regional head
function getRegionHeadTabDate(tab,time_dime){//获取月度区域经理选项卡选择时间、是否还原系数
	var o = {};
	var coeff;
	var selectDateVal;
	var xcpWhere;
	if(!isStringNull(tab) || tab == 'Total'){
		selectDateVal = $('#queryYearDataByregin' + time_dime).val();
		coeff = $('#region_head_Check' + time_dime).is(":checked");
	}else if('Smart' == tab){
		selectDateVal = $('#queRegHeSmartDate' + time_dime).val();
		coeff = $('#region_head_smart_Check' + time_dime).is(":checked");
	} else if('UD' == tab){
		selectDateVal = $('#queRegHeUDDate' + time_dime).val();
		coeff = $('#region_head_UD_Check' + time_dime).is(":checked");
	} else if('Big' == tab){
		selectDateVal = $('#queRegHeBigDate' + time_dime).val();
		coeff = $('#region_head_big_Check' + time_dime).is(":checked");
	} else if('Curved' == tab){
		selectDateVal = $('#queRegHeCurvedDate' + time_dime).val();
		coeff = $('#region_head_Curved_Check' + time_dime).is(":checked");
	} else if('X/C/P' == tab){
		var subXcp = $('#subXcp' + time_dime).val();
		if(isStringNull(subXcp)){
			xcpWhere =subXcp;// " and pr.`product_line` like '%"+ subXcp +"%'"
		}else{
			var seriesXcp = $('#SeriesXcp' + time_dime).val();
			if(isStringNull(seriesXcp)){
				xcpWhere =seriesXcp;// " and pr.`product_line` like '%"+ seriesXcp +"%'"
			}else{
				xcpWhere ="";
				//" and (pr.`product_line` like '%X%' or pr.`product_line` like '%C%' or pr.`product_line` like '%P%' ) "
			}
		}
		
		selectDateVal = $('#queRegHeXCPDate' + time_dime).val();
		coeff = $('#mongth_region_head_xcp_Check' + time_dime).is(":checked");
	}
	o.xcpWhere = xcpWhere;
	o.coeff = coeff;
	o.selectDateVal = selectDateVal;
	return o;
}

function queryYearDataByRegi(tab){
	//loadMonthMenu();//加载年度选项卡
	var selectDateVal = getRegionHeadTabDate(tab,'_year').selectDateVal ;
	var beginDate;
	var endDate;
	
	if(!isStringNull(selectDateVal)){
		beginDate = new Date().getFullYear() + "-01-01";
		endDate = new Date().getFullYear() + "-12-31";
	}else{
		beginDate = selectDateVal + "-01-01";
		endDate = selectDateVal + "-12-31";
	}
	
	var o = {};
	o.beginDate = beginDate;
	o.engDate = endDate;
	o.coeff = getRegionHeadTabDate(tab,'_year').coeff ;
	o.tab = tab;
	o.big = $('#regiHeadBigAttr_year').val();
	o.xcpWhere = getRegionHeadTabDate(tab,'_year').xcpWhere;
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/regionalGrowthPerformanceByPartyId.action",
		type:"POST",
		data:o,
		success:function(data){
			var leftDataHtml = getLeftHeadHTML() + setLeftDataHtml(data.data);
			var d = endDate.split('-')[0] * 1;
			var m = endDate.split('-')[1];
			var rightDataHtml = getRightHeadHtml(d,(d - 1),m) + setRightDataHtml(data.data);
			loadingShowGrows(true);
			setDataByRegiHtml(tab,leftDataHtml,rightDataHtml,'_year');
			showRegionalName();
		}
	});
}

function setDataByRegiHtml(tab,leftDataHtml,rightDataHtml,time_dime){
	if(!isStringNull(tab) || tab == 'Total'){
		$('#leftDataHtml' + time_dime).html(leftDataHtml);
		$('#rightDataHtml' + time_dime).html(rightDataHtml);
		//setScrollBarShift('leftDataHtml'+ time_dime,'rightDataHtml' + time_dime);
	}else if('Smart' == tab){
		$('#queRegHeSmartLeft' + time_dime).html(leftDataHtml);
		$('#queRegHeSmartRight' + time_dime).html(rightDataHtml);
		//setScrollBarShift('queRegHeSmartLeft' + time_dime,'queRegHeSmartRight' + time_dime);
	}else if('UD' == tab){
		$('#queRegHeUDLeft' + time_dime).html(leftDataHtml);
		$('#queRegHeUDRight' + time_dime).html(rightDataHtml);
		//setScrollBarShift('queRegHeUDLeft' + time_dime,'queRegHeUDRight' + time_dime);
	}else if('Big' == tab){
		$('#queRegHeBigLeft' + time_dime).html(leftDataHtml);
		$('#queRegHeBigRight' + time_dime).html(rightDataHtml);
		//setScrollBarShift('queRegHeBigLeft' + time_dime,'queRegHeBigRight' + time_dime);
	} else if('Curved' == tab){
		$('#queRegHeCurvedLeft' + time_dime).html(leftDataHtml);
		$('#queRegHeCurvedRight' + time_dime).html(rightDataHtml);
		//setScrollBarShift('queRegHeCurvedLeft' + time_dime,'queRegHeCurvedRight' + time_dime);
	}else if('X/C/P' == tab){
		$('#queRegHeXCPLeft' + time_dime).html(leftDataHtml);
		$('#queRegHeXCPRight' + time_dime).html(rightDataHtml);
		//setScrollBarShift('queRegHeXCPLeft' + time_dime,'queRegHeXCPRight' + time_dime);
	}
}

//month --> regional head
function getMonthRegionHeadTabDate(tab){//获取月度区域经理选项卡选择时间、是否还原系数
	var o = {};
	var coeff;
	var selectDateVal;
	var xcpWhere;
	if(!isStringNull(tab) || tab == 'Total'){
		selectDateVal = $('#queryYearDataByregin').val();
		coeff = $('#mongth_region_head_Check').is(":checked");
	}else if('Smart' == tab){
		selectDateVal = $('#queMonRegHeSmartDatedd').val();
		coeff = $('#mongth_region_head_smart_Check').is(":checked");
	} else if('UD' == tab){
		selectDateVal = $('#queMonRegHeUDDate').val();
		coeff = $('#mongth_region_head_UD_Check').is(":checked");
	} else if('Big' == tab){
		selectDateVal = $('#queMonRegHeBigDate').val();
		coeff = $('#mongth_region_head_big_Check').is(":checked");
	} else if('Curved' == tab){
		selectDateVal = $('#queMonRegHeCurvedDate').val();
		coeff = $('#mongth_region_head_Curved_Check').is(":checked");
	} else if('X/C/P' == tab){
		var subXcp = $('#subXcp').val();
		if(isStringNull(subXcp)){
			xcpWhere =subXcp;// " and pr.`product_line` like '%"+ subXcp +"%'";
		}else{
			var seriesXcp = $('#SeriesXcp').val();
			if(isStringNull(seriesXcp)){
				xcpWhere =seriesXcp;// " and pr.`product_line` like '%"+ seriesXcp +"%'";
			}else{
				xcpWhere ="";// " and (pr.`product_line` like '%X%' or pr.`product_line` like '%C%' or pr.`product_line` like '%P%' ) ";
			}
		}
		
		selectDateVal = $('#queMonRegHeXCPDate').val();
		coeff = $('#mongth_region_head_xcp_Check').is(":checked");
	}
	o.xcpWhere = xcpWhere;
	o.coeff = coeff;
	o.selectDateVal = selectDateVal;
	return o;
}

function queryMonthDataByRegi(tab){
	loadMonthMenu();//加载月度选项卡
	var selectDateVal = getMonthRegionHeadTabDate(tab).selectDateVal ;
	var beginDate;
	var endDate;
	
	if(!isStringNull(selectDateVal)){
		beginDate = new Date().getFullYear() + "-" + (new Date().getMonth()+1) + "-01";
		endDate = getSelectDate(beginDate);
	}else{
		beginDate = selectDateVal + "-01";
		endDate = getSelectDate(beginDate);
	}
	
	var o = {};
	o.beginDate = beginDate;
	o.engDate = endDate;
	o.coeff = getMonthRegionHeadTabDate(tab).coeff ;
	o.tab = tab;
	o.big = $('#regiHeadMonthBigAttr').val();
	o.xcpWhere = getMonthRegionHeadTabDate(tab).xcpWhere;
	o.time_dime = 'month';
	loadingShowGrows(false);
	$.ajax({
		url:baseUrl + "staPage/regionalGrowthPerformanceByPartyId.action",
		type:"POST",
		data:o,
		success:function(data){
			var leftDataHtml = getLeftHeadHTML() + setLeftDataHtml(data.data);
			var d = endDate.split('-')[0] * 1;
			var m = endDate.split('-')[1];
			var rightDataHtml = getRightHeadHtml(d,(d - 1),m) + setRightDataHtml(data.data);
			loadingShowGrows(true);
			setMonthDataByRegiHtml(tab,leftDataHtml,rightDataHtml);
			showRegionalName();
		}
	});
}

function setMonthDataByRegiHtml(tab,leftDataHtml,rightDataHtml){
	if(!isStringNull(tab) || tab == 'Total'){
		$('#leftDataHtml').html(leftDataHtml);
		$('#rightDataHtml').html(rightDataHtml);
		//setScrollBarShift('leftDataHtml','rightDataHtml');
	}else if('Smart' == tab){
		$('#queMonRegHeSmartLeft').html(leftDataHtml);
		$('#queMonRegHeSmartRight').html(rightDataHtml);
		//setScrollBarShift('queMonRegHeSmartLeft','queMonRegHeSmartRight');
	}else if('UD' == tab){
		$('#queMonRegHeUDLeft').html(leftDataHtml);
		$('#queMonRegHeUDRight').html(rightDataHtml);
		//setScrollBarShift('queMonRegHeUDLeft','queMonRegHeUDRight');
	}else if('Big' == tab){
		$('#queMonRegHeBigLeft').html(leftDataHtml);
		$('#queMonRegHeBigRight').html(rightDataHtml);
		//setScrollBarShift('queMonRegHeBigLeft','queMonRegHeBigRight');
	} else if('Curved' == tab){
		$('#queMonRegHeCurvedLeft').html(leftDataHtml);
		$('#queMonRegHeCurvedRight').html(rightDataHtml);
		//setScrollBarShift('queMonRegHeCurvedLeft','queMonRegHeCurvedRight');
	}else if('X/C/P' == tab){
		$('#queMonRegHeXCPLeft').html(leftDataHtml);
		$('#queMonRegHeXCPRight').html(rightDataHtml);
		//setScrollBarShift('queMonRegHeXCPLeft','queMonRegHeXCPRight');
	}
}

function showRegionalName(){//显示区域完整名称
	$("[id='mothRegioShow']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}
function setRightDataHtml(obj){//拼接右边信息HTMl
	var html = "<tbody>";
	
	var allVal = [0,0,0,0,0,0,0,0,0,0,0,0];
	for(var i=0;i<obj.length;i++){
		allVal[0] += accumulativeJudgment(obj[i].FPS_YR_LASTYEAR);
		allVal[1] += accumulativeJudgment(obj[i].FPS_YR_THISYEAR);
		allVal[2] += accumulativeJudgment(obj[i].TT_QTY_LASTYEAR);
		allVal[3] += accumulativeJudgment(obj[i].TT_QTY_THISYEAR);
		allVal[4] += accumulativeJudgment(obj[i].TT_QTY_GROWTH);
		allVal[5] += accumulativeJudgment(obj[i].TT_AMT_LASTYEAR);
		allVal[6] += accumulativeJudgment(obj[i].TT_AMT_THISYEAR);
		allVal[7] += accumulativeJudgment(obj[i].TT_AMT_GROWTH);
		
		html += "<tr>" + setTDRight(obj[i].FPS_YR_LASTYEAR);
		html += setTDRight(obj[i].FPS_YR_THISYEAR);
		html += setTDRight(obj[i].TT_QTY_LASTYEAR);
		html += setTDRight(obj[i].TT_QTY_THISYEAR);
		html += growthRate(obj[i].TT_QTY_GROWTH * 100);
		html += setTDRight(obj[i].TT_AMT_LASTYEAR);
		html += setTDRight(obj[i].TT_AMT_THISYEAR);
		html += growthRate(obj[i].TT_AMT_GROWTH * 100); 
		
		html += setTDRight(getAvg(accumulativeJudgment(obj[i].FPS_YR_LASTYEAR),accumulativeJudgment(obj[i].TT_QTY_LASTYEAR))); 
		html += setTDRight(getAvg(accumulativeJudgment(obj[i].FPS_YR_THISYEAR),accumulativeJudgment(obj[i].TT_QTY_THISYEAR))); 
		html += setTDRight(getAvg(accumulativeJudgment(obj[i].FPS_YR_LASTYEAR),accumulativeJudgment(obj[i].TT_AMT_LASTYEAR))); 
		html += setTDRight(getAvg(accumulativeJudgment(obj[i].FPS_YR_THISYEAR),accumulativeJudgment(obj[i].TT_AMT_THISYEAR)))+ "</tr>";
		allVal[8] += getAvg(accumulativeJudgment(obj[i].FPS_YR_LASTYEAR),accumulativeJudgment(obj[i].TT_QTY_LASTYEAR));
		allVal[9] += getAvg(accumulativeJudgment(obj[i].FPS_YR_THISYEAR),accumulativeJudgment(obj[i].TT_QTY_THISYEAR)); 
		allVal[10] += getAvg(accumulativeJudgment(obj[i].FPS_YR_LASTYEAR),accumulativeJudgment(obj[i].TT_AMT_LASTYEAR));
		allVal[11] += getAvg(accumulativeJudgment(obj[i].FPS_YR_THISYEAR),accumulativeJudgment(obj[i].TT_AMT_THISYEAR));
	}
	html += "<tr>";
	for(var i =0;i<allVal.length;i++){
		if(i == 4){
			html += growthRate(growthRateFormula(allVal[2],allVal[3]));
		}else if(i == 7){
			html += growthRate(growthRateFormula(allVal[5],allVal[6]));
		}else{
			html += setTDRight(allVal[i]);
		}
	}
	html += "</tr>";
	
	return  html += "</tbody>";
};

function growthRateFormula(lastVal,thisVal){
	var adHoc = 0;
	if(thisVal != 0 && lastVal== 0){
		adHoc = 100;
	}else if(thisVal == 0 && lastVal == 0){
		adHoc = 0;
	}else if(thisVal == 0 && lastVal != 0){
		adHoc = -100;
	}else{
		adHoc = ((thisVal - lastVal) / thisVal * 100).toFixed(2);
	}
	return adHoc;
};

function getAvg(num,val){
	if(num == 0 || val == 0){
		return 0;
	}else{
		return (val/num);
	}
};

function setTDRight(val){//设置居右带千位符
	return "<td style='text-align:right'>"+ headDataToFixed(val) +"</td>";
};

function getRightHeadHtml(thisYear,lastYear,month){//获取右边头部HTML
	return "<thead>" + 
				"<tr>" +
					"<th class='TVblue' colspan='2'>FPS</th>" +
					"<th class='TVblue' colspan='3'>Total Flat Panel TV Quantity</th>" +
					"<th class='TVblue' colspan='3'>Total Amount </th>" +
					"<th class='TVblue' colspan='4'>SELL-OUT EFFICIENSY / FPS</th>"+
				"</tr>" +
				"<tr>" +
					"<th class='bluegrey'>Yr-"+ lastYear +"</th>" +
					"<th class='bluegrey'>Yr-"+ thisYear +"</th>" +
					"<th class='bluegrey'>"+ getENMonth(month) +" "+ lastYear +"</th>" +
					"<th class='bluegrey'>"+ getENMonth(month) +" "+ thisYear +"</th>" +
					"<th class='bluegrey'>Growth</th>" +
					"<th class='bluegrey'>"+ getENMonth(month) +" "+ lastYear +"</th>" +
					"<th class='bluegrey'>"+ getENMonth(month) +" "+ thisYear +"</th>" +
					"<th class='bluegrey'>Growth</th>" +
					"<th class='bluegrey'>"+ lastYear +" Ave.qty</th>" +
					"<th class='bluegrey'>"+ thisYear +" Ave.qty</th>" +
					"<th class='bluegrey'>"+ lastYear +" Ave.amt</th>" +
					"<th class='bluegrey'>"+ thisYear +" Ave.amt</th>" +
				"</tr>" +
			"</thead>";
};

function setLeftDataHtml(obj){//拼接左边信息HTMl
	var html = "<tbody>";
	for(var i=0;i<obj.length;i++){
		html += "<tr><td>"+ (i+1) +"</td>";
		html += "<td>"+ obj[i].REGION +"</td>";
		html += "<td id='mothRegioShow'>"+ obj[i].REGIONAL_HEAD +"</td></tr>";
	}
	return html += "<tr>" +
						"<td></td>" +
						"<td>TOTAL</td>" +
						"<td></td>" +
					"</tr>" +
					"</tbody>";
};

function getLeftHeadHTML(){//获取左边头部HTML
	return "<thead>" +
				"<tr class='bluegrey'>" +
					"<th style='height: 42px;width: 60px;'>RANK</th>" +
					"<th style='height: 42px;'>Regional Head</th>" +
					"<th style='height: 42px;'>REGION</th>" +
				"</tr>" +
			"</thead>";
}


function SeriesACFOXcp(target,time_dime){
	var line=$(target).val();
	if(line==null || line==""){
		$('#subACFOXcp'+time_dime).html('');
		$('#subACFOXcp'+time_dime).html("<option value=''> </option>");
		return;
		//line="AND (cfg.`PVALUE` LIKE '%X%' OR cfg.`PVALUE` LIKE '%C%' OR cfg.`PVALUE` LIKE '%P%')";
	}else {
		line=line;//"AND cfg.`PVALUE` LIKE '%"+line+"%'"
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
					$('#subACFOXcp' + time_dime).html('');
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].PVALUE+"'>"+STring[i].PVALUE+"</option>";
						
					}
						$('#subACFOXcp' + time_dime).html(option);
				
			}else{
						$('#subACFOXcp' + time_dime).html("<option value=''></option>");

			}
			
		   
		}
	});
	
}

function setShowIsNull(head,left,right,min){
	fatherHtmlShow(head,left,right,min);
	setHtmlNull(head,left,right,min);
};

function SeriesXcp(target,time_dime){
	var line=$(target).val();
	if(line==null || line==""){
		$('#subXcp'+time_dime).html('');
		$('#subXcp'+time_dime).html("<option value=''> </option>");
		return;
		//line="AND (cfg.`PVALUE` LIKE '%X%' OR cfg.`PVALUE` LIKE '%C%' OR cfg.`PVALUE` LIKE '%P%')";
	}else {
		line=line;//"AND cfg.`PVALUE` LIKE '%"+line+"%'"
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