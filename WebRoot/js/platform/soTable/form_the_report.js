$(document).ready(function() {
	
	getYearByReg();

});


function queryYearByBig(){//big年度
	var selectDateVal = $('#YearByBigSelectDate').val();
	var beginDate;
	var endDate;
	var y;
	if(!isStringNull(selectDateVal)){
		beginDate = new Date().getFullYear() + "-01-01";
		endDate = new Date().getFullYear() + "-12-31";
		y = new Date().getFullYear();
	}else{
		beginDate = selectDateVal + "-01-01";
		endDate = selectDateVal + "-12-31";
		y = selectDateVal;
	}
	
	var o = {};
	o.beginDate = beginDate;
	o.endDate = endDate;
	if(y >= 2018){
		o.big = 55;//尺寸大于48为big,调整只需改变big值
	}else{
		o.big = 48;//尺寸大于48为big,调整只需改变big值
	}
	
	o.series = 'C2';//c2市场占比
	o.coeff = $('#year_coun_big').is(":checked");
	loadingShowHides(false);
	$.ajax({
		url:baseUrl + "staPage/queryBigSaleInfo.action",
		type:"POST",
		data:o,
		success:function(data){
			loadingShowHides(true);
			$('#bigYearInfo').html(setBigInfoHead(selectDateVal) + "<tbody>" + setBigInfo(data.saleInfo,selectDateVal)+ setBigBottomHtml(data.month,data.series,selectDateVal) + "</tbody>");
			showRegionalName();
		}
	});
};

function getInitArrayByLength(length){
	var a = [];
	for(var j =0;j<length;j++){
		a[j] = 0;
	}
	return a;
};
function showRegionalName(){//显示big完整类型
	$("[id='bigTVType']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}
function setBigBottomHtml(o,obj,selectDateVal){
	var bigBottomArr = getInitArrayByLength(getMonthCount(selectDateVal));
	var BottomHtml = "<tr class='bluegrey'><td colspan='3'>TTL SELLOUT/MOS.</td>";
	var allYearAnd = 0;
	for(var i = 0;i<o.length;i++){
		bigBottomArr[(o[i].month-1)] += accumulativeJudgment(o[i].ttlTVSOQty);
		allYearAnd += accumulativeJudgment(o[i].ttlTVSOQty);
	}
	
	for(var i = 0;i<bigBottomArr.length;i++){
		BottomHtml += setTDRight(bigBottomArr[i]);
	}
	BottomHtml += setTDRight(allYearAnd);
	BottomHtml += "</tr>";
	
	var allsAnd = 0;
	var bigSBottomArr = getInitArrayByLength(bigBottomArr.length);
	for(var i = 0;i<obj.length;i++){
		bigSBottomArr[(obj[i].month-1)] += accumulativeJudgment(obj[i].ttlTVSOQty);
		allsAnd += accumulativeJudgment(obj[i].ttlTVSOQty);
	}
	
	BottomHtml += "<tr><td colspan='3'>RATIO of C2</td>";
	
	for(var k = 0;k<bigSBottomArr.length;k++){
		BottomHtml += setTDRight((bigSBottomArr[k]/bigBottomArr[k]*100).toFixed(2));
	}
	BottomHtml += setTDRight((allsAnd/allYearAnd*100).toFixed(2));
	return BottomHtml += "</tr>";
}

function setBigInfo(obj,selectDateVal){
	var bigAllVal = getInitArrayByLength(getMonthCount(selectDateVal));
	var trHtml = "";
	for(var i = 0;i<obj.length;i++){
		trHtml += "<tr>";
		trHtml += "<td>"+ obj[i].size +"</td>";
		trHtml += "<td id='bigTVType'>"+ obj[i].func +"</td>";
		trHtml += "<td id='bigTVType'>"+ obj[i].model +"</td>";
		var monthVal = obj[i].monthVal;
		var rowAll = 0;
		for(var j =0;j<getMonthCount(selectDateVal);j++){
			trHtml += setTDRight(monthVal[j]);
			rowAll += accumulativeJudgment(monthVal[j]);
			bigAllVal[j] += accumulativeJudgment(monthVal[j]);
		}
		trHtml += setTDRight(rowAll);
		trHtml += "</tr>";
	};
	trHtml += "<tr><td colspan='3'>TOTAL</td>";
	var bigAllValAnd = 0;
	for(var i = 0;i<bigAllVal.length;i++){
		trHtml += setTDRight(bigAllVal[i]);
		bigAllValAnd += accumulativeJudgment(bigAllVal[i]);
	};
	trHtml += setTDRight(bigAllValAnd);
	return trHtml += "</tr>";
};

function setTDRight(val){//设置居右带千位符
	return "<td id='bigTVType';style='text-align:right'>"+ headDataToFixed(val) +"</td>";
};

function getMonthCount(selectDateVal){
	var length;
	if(!isStringNull(selectDateVal)){
		length = new Date().getMonth() + 1;
	}else{
		if(selectDateVal == new Date().getFullYear()){
			length = new Date().getMonth() + 1;
		}else{
			length = 12;
		}
	}
	return length;
};

function setBigInfoHead(selectDateVal){
	var length = getMonthCount(selectDateVal);
	var theadHtml = "<thead>" +
						"<tr>" +
							"<th>SIZE</th>" +
							"<th>TV TYPE</th>"+
							"<th>MODEL</th>";
	for(var i =0;i<length;i++){
		theadHtml += "<th>" + enMonth[i] + "</th>";
	}
	return theadHtml += "<th>TTL</th></tr></thead>";
};

function setYearByCPU(){//xcp年度达成
	var selectDateVal = $('#reservationAnnualCpu').val();
	var beginDate;
	var endDate;
	var whiles;
	if(!isStringNull(selectDateVal)){
		beginDate = new Date().getFullYear() + "-01-01";
		endDate = new Date().getFullYear() + "-12-31";
	}else{
		beginDate = selectDateVal + "-01-01";
		endDate = selectDateVal + "-12-31";
	}
	
	var selectCPU = $('#subYearXcpWeek').val();
	if(isStringNull(selectCPU)){
		whiles = selectCPU;
	}else{
		var xcpValues = $('#xcpsYear').val();
		if(isStringNull(xcpValues)){
			whiles = xcpValues;
		}else{
			whiles = "";
		}
	}
	loadingShowHides(false);
	$.ajax({
		url:baseUrl + "staPage/PSeriesSalesStatus.action",
		type:"POST",
		data:{'whiles':whiles,"beginDate":beginDate,'endDate':endDate, 'coeff':$('#year_region_xpc_Check').is(":checked")},
		success:function(data){
			var leftDataHtml = setWeekByCPUHeadHtml(data.TFL) + setWeekByCPULeftHtml(data.val);
			$('#yearByCPU').html(leftDataHtml);
			loadingShowHides(true);
			setYearRgionTitle();
		}
	});
};

function setYearRgionTitle(){
	var cupSeriesValue = "";
	var cpuSeries = $("#xcpsYear").val();
	var subXcp = $("#subYearXcpWeek").val();
	
	if(isStringNull(subXcp)){
		cupSeriesValue = subXcp;
	}else if(isStringNull(cpuSeries)){
		cupSeriesValue = cpuSeries;
	}else{
		cupSeriesValue = "X/C/P";
	}
	$('#year_Region').html(cupSeriesValue + " sellout and display in each Region");
};

function querySeriesOfSales(){//cpu周度达成
	var selectDateVal = $('#WeekByCP').val();
	var beginDate;
	var endDate;
	var whiles;
	if(!isStringNull(selectDateVal)){
		beginDate = Common_date.getMondayAndWeekend()[0];
		endDate = Common_date.getMondayAndWeekend()[1];
	}else{
		beginDate = selectDateVal.split(' - ')[0];
		endDate = selectDateVal.split(' - ')[1];
	}
	
	var selectCPU = $('#subXcpWeek').val();
	if(isStringNull(selectCPU)){
		whiles =selectCPU;// "pr.product_line LIKE '%"+ selectCPU +"%'";
	}else{
		var xcpValues = $('#xcps').val();
		if(isStringNull(xcpValues)){
			whiles = xcpValues;//"pr.product_line LIKE '%"+ xcpValues +"%'";
		}else{
			whiles ="";// "(pr.product_line  LIKE '%X%' OR pr.product_line  LIKE '%C%' OR pr.product_line  LIKE '%P%')"
		}
	}
	loadingShowHides(false);
	$.ajax({
		url:baseUrl + "staPage/PSeriesSalesStatus.action",
		type:"POST",
		data:{'whiles':whiles,"beginDate":beginDate,'endDate':endDate,'coeff':$('#week_xcp_Check').is(":checked")},
		success:function(data){
			var leftDataHtml = setWeekByCPUHeadHtml(data.TFL) + setWeekByCPULeftHtml(data.val);
			loadingShowHides(true);
			$('#WeekByCPU').html(leftDataHtml);
			cpuTitleShow(beginDate,endDate);
		}
	});
};

function cpuTitleShow(beginDate,endDate){
	var cupSeriesValue = "";
	var cpuSeries = $("#xcps").val();
	var subXcp = $("#subXcpWeek").val();
	
	if(isStringNull(subXcp)){
		cupSeriesValue = subXcp;
	}else if(isStringNull(cpuSeries)){
		cupSeriesValue = cpuSeries;
	}else{
		cupSeriesValue = "X/C/P";
	}
	var beginDateArr = beginDate.split('-');
	var endDateArr = endDate.split('-');
		$('#week_series_title').html(cupSeriesValue + " sellout /display RESULTS in September as of  "+ 
				 getENMonth(beginDateArr[1]) +" "+beginDateArr[2] +","+ beginDateArr[0] +
					" - "+  getENMonth(endDateArr[1]) +" " + endDateArr[2] +","+endDateArr[0]);
};

function loadingShowHideCPU(is,loading,right,left){//加载图标(同比)																																																																								
	if(is){
		$("#" + loading).hide();
		$("#" + right).show();
	}else{
		$("#" + loading).show();
		$("#" + right).hide();
	}
};

function getSubXcpWeek(){
	
	var line=$("#xcps").val();
	if(line==""){
		//line="AND (cfg.`PVALUE` LIKE '%X%' OR cfg.`PVALUE` LIKE '%C%' OR cfg.`PVALUE` LIKE '%P%')";
		//$('#subXcpWeek').html("<option value=''></option>");
		$('#subXcpWeek').html('');
		$('#subXcpWeek').html("<option value=''> </option>");
		return;
		
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
					$('#subXcpWeek').html('');
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].PVALUE+"'>"+STring[i].PVALUE+"</option>";
						
					}
						$('#subXcpWeek').html(option);
				
			}else{
						$('#subXcpWeek').html("<option value=''></option>");

			}
			
		   
		}
	});
	
}

function getSubXcpYear(){
	
	var line=$("#xcpsYear").val();
	if(line=="" || line==null){
		$('#subYearXcpWeek').html('');
	
		//line="AND (cfg.`PVALUE` LIKE '%X%' OR cfg.`PVALUE` LIKE '%C%' OR cfg.`PVALUE` LIKE '%P%')";
		$('#subYearXcpWeek').html("<option value=''></option>");
		return;
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
				$('#subXcpWeek').html('');
				var option="<option value=''>All</option>";
				for(var i=0;i<STring.length;i++){
					option+="<option value='"+STring[i].PVALUE+"'>"+STring[i].PVALUE+"</option>";
					
				}
				$('#subYearXcpWeek').html(option);
				
			}else{
				$('#subYearXcpWeek').html("<option value=''></option>");
				
			}
			
			
		}
	});
	
}

function setWeekByCPUHeadHtml(obj){
	var theadHtml = "<thead style='width: calc(100% - 17px);'><tr class='bluegrey'><th>MODEL</th>";
	for(var i =0;i<obj.length;i++){
		theadHtml += "<th>"+ obj[i].partyName +"</th>";
	} 
	return theadHtml += "<th>TOTAL</th></tr></thead>";
};

function getInitArrByLength(length){
	var arr = [];
	for(var i = 0;i<length;i++){
		arr[i] = 0;
	};
	return arr;
};

function setTDRight(val){//设置居右带千位符
	return "<td style='text-align:right'>"+ headDataToFixedDown(val) +"</td>";
};

function headDataToFixedDown(num){//判断识别是否存在小数点。(千位符)
	if(typeof(num) == "string"){
		num = num * 1;
	}
	if((""+num).indexOf('.') >= 0){
		return formatNumber(num.toFixed(2),0,1);
	}else{
		return toThousands(num);
	}
}

function setWeekByCPULeftHtml(obj){
	var tbodyHtml = "<tbody>";
	var lineAllVal ;
	var rowAllValAll = 0;
	if(obj.length <= 0){
		lineAllVal = getInitArrByLength(0);
	}else{
		lineAllVal = getInitArrByLength(obj[0].val.length);
	}
	
	for(var i =0;i<obj.length;i++){
		var rowAllVal = 0;//行累加
		tbodyHtml +="<tr><td>"+ obj[i].model +"</td>";
		var valArr = obj[i].val;
		for(var j = 0;j < valArr.length;j++){
			tbodyHtml += setTDRight(valArr[j]);
			rowAllVal += accumulativeJudgment(valArr[j]);
			lineAllVal[j] += valArr[j];
		}
		tbodyHtml += setTDRight(rowAllVal) +"</tr>";
		rowAllValAll += rowAllVal;
	}
	tbodyHtml += "<tr><td>TOTAL Sellout</td>";
	for(var i=0;i<lineAllVal.length;i++){
		tbodyHtml += setTDRight(lineAllVal[i]);
	}
	tbodyHtml += setTDRight(rowAllValAll) +"</tr>";
	
	return tbodyHtml += "</tbody>";
}

function queryWeekBySale(){//业务员周度查看
	
	var selectDateVal = $('#reservationWeeklySale').val();
	var beginDate;
	var endDate;
	var whiles;
	if(!isStringNull(selectDateVal)){
		beginDate = Common_date.getMondayAndWeekend()[0];
		endDate = Common_date.getMondayAndWeekend()[1];
	}else{
		beginDate = selectDateVal.split(' - ')[0];
		endDate = selectDateVal.split(' - ')[1];
	}
	loadingShowHides(false);
	$.ajax({
		url:baseUrl + "staPage/monthlySalesPerformance.action",
		type:"POST",
		data:{"beginDate":beginDate,'endDate':endDate,'coeff':$('#week_sale_Check').is(":checked")},
		success:function(data){
			var leftDataHtml = getSalesmanMonthQuarterlyStaticHead() + setSalesmanMonthQuarterlyLeftData(data.data);
			var rightDataHtml = setSalesmanMonthQuarterlyRightData(data.data);
			
			loadingShowHides(true);
			$('#salesmanWeekQuarterlyLeftData').html(leftDataHtml);
			$('#test_Right2').html(salesmanWeekQuarterlyLeftDataHead());
			$('#salesmanWeekQuarterlyRightData').html(rightDataHtml);
			setScrollBarShift('test_Left2','test_Right2');
			setWidthBarShift('test_Left2','test_Right2');
			var beginDateArr = beginDate.split('-');
			var endDateArr = endDate.split('-');
			$('#week_sale_title').html("Salesman/ Account sellout updates as of "+ 
					" "+ getENMonth(beginDateArr[1]) +" "+beginDateArr[2] +","+ beginDateArr[0] +
						" - "+" "+ getENMonth(endDateArr[1])+ endDateArr[2]  +"," + endDateArr[0]);
		}
	});
};
function salesmanWeekQuarterlyLeftDataHead(){
	return "<table>"+
				"<thead>"+
					"<tr>"+
						"<th rowspan='2'>No of Shops</th>"+
						"<th rowspan='2'>TV FPS</th>"+
						"<th class='TVblue' colspan='2' style='width: 200px;'>TTL TV SO</th>"+
						"<th class='lightBlue' rowspan='2'>Basic TV Target</th>"+
						"<th class='lightBlue' rowspan='2'>Ach</th>"+
						"<th class='lightBlue' colspan='2' style='width: 200px;'>Ave.SO per FPS</th>"+
					"</tr>"+
					"<tr>"+
						"<th class='TVblue'>Qty</th>"+
						"<th class='TVblue'>Amt</th>"+
						"<th class='lightBlue'>Qty</th>"+
						"<th class='lightBlue'>Amt</th>"+
					"</tr>"+
				"</thead>"+
			"</table>";
}
function loadSalesmanMonthQuarterly(){//业务员月度查看
	/*setHtmlNull(salesmanMonthQuarterlyLeftData,salesmanMonthQuarterlyRightData,null,null);*/
	
	var selectDateVal = $('#salesmanMonthQuarterlyLeftDate').val();
	var beginDate;
	var endDate;
	if(!isStringNull(selectDateVal)){
		beginDate = new Date().getFullYear() + "-" + (new Date().getMonth()+1) + "-01";
		endDate = getSelectDate(beginDate);
	}else{
		beginDate = selectDateVal + "-01";
		endDate = getSelectDate(beginDate);
	}
	loadingShowHides(false);
	$.ajax({
		url:baseUrl + "staPage/monthlySalesPerformance.action",
		type:"POST",
		data:{"beginDate":beginDate,'endDate':endDate,'coeff':$('#month_sale_Check').is(":checked")},
		success:function(data){
			var leftDataHtml = getSalesmanMonthQuarterlyStaticHead() + setSalesmanMonthQuarterlyLeftData(data.data);
			var rightDataHtml = setSalesmanMonthQuarterlyRightData(data.data);
			loadingShowHides(true);
			$('#salesmanMonthQuarterlyLeftData').html(leftDataHtml);
			$('#test_Right1').html(setSalesmanMonthQuarterlyRightDataHead());
			$('#salesmanMonthQuarterlyRightData').html(rightDataHtml);
			setScrollBarShift('test_Left1','test_Right1');
			setWidthBarShift('test_Left1','test_Right1');
			
			var o = getManualSelectDate('salesmanMonthQuarterlyLeftDate','month');
			$('#month_sale_title').html("Total Sellout performances of Salesman for the month of " + o.month + " " + o.year);
		}
	});
};
function setSalesmanMonthQuarterlyRightDataHead(){
	return "<table>" +
			"<thead>" +
			"<tr>" +
			"<th rowspan='2'>No of Shops</th>" +
				"<th rowspan='2'>TV FPS</th>" +
				"<th class='TVblue' colspan='2' style='width: 200px;'>TTL TV SO</th>" +
				"<th class='lightBlue' rowspan='2'>Basic TV Target</th>" +
				"<th class='lightBlue' rowspan='2'>Ach</th>" +
				"<th class='lightBlue' colspan='2' style='width: 200px;'>Ave.SO per FPS</th>" +
			"</tr>" +
			"<tr>" +
				"<th class='TVblue'>Qty</th>" +
				"<th class='TVblue'>Amt</th>" +
				"<th class='lightBlue'>Qty</th>" +
				"<th class='lightBlue'>Amt</th>" +
			"</tr>" +
		"</thead>" +
	"</table>";
}
//业务员月度右边动态数据
function setSalesmanMonthQuarterlyRightData(obj){
	var rightHtml = "<tbody>";
	var allValArr = [0,0,0,0,0,0,0,0];
	
	for(var i = 0;i < obj.length;i ++){
		allValArr[0] += accumulativeJudgment(obj[i].No_of_Shops);
		allValArr[1] += accumulativeJudgment(obj[i].NO_of_FPS);
		allValArr[2] += accumulativeJudgment(obj[i].TTL_TV_SO_Qty);
		allValArr[3] += accumulativeJudgment(obj[i].TTL_TV_SO_Amt);
		allValArr[4] += accumulativeJudgment(obj[i].Basic_TV_Target.toFixed(2));
		allValArr[5] += accumulativeJudgment((isStringNullAvaliable(obj[i].Ach)?(obj[i].Ach * 1).toFixed(2):0));
		allValArr[6] += accumulativeJudgment(obj[i].Ave_SO_per_FPS_Qty);
		allValArr[7] += accumulativeJudgment(obj[i].Ave_SO_per_FPS_Amount);
		
		rightHtml += "<tr><td>"+ headDataToFixed(obj[i].No_of_Shops) +"</td>";
		rightHtml += "<td>"+ headDataToFixed(obj[i].NO_of_FPS) +"</td>";
		rightHtml += "<td>"+ headDataToFixed(obj[i].TTL_TV_SO_Qty) +"</td>";
		rightHtml += "<td>"+ headDataToFixed(obj[i].TTL_TV_SO_Amt) +"</td>";
		rightHtml += "<td>"+ headDataToFixed(obj[i].Basic_TV_Target.toFixed(2)) +"</td>";
		rightHtml += reachTheBackgroundColor(isStringNullAvaliable(obj[i].Ach)?(obj[i].Ach * 1).toFixed(2):0);
		rightHtml += "<td>"+ headDataToFixed(obj[i].Ave_SO_per_FPS_Qty) +"</td>";
		rightHtml += "<td>"+ headDataToFixed(obj[i].Ave_SO_per_FPS_Amount) +"</td></tr>";
	}
	
	for(var i = 0;i < allValArr.length;i ++){
		if(i == 5){
			rightHtml += reachTheBackgroundColor(growthRateFormula(allValArr[3],allValArr[4]));
		}else{
			rightHtml += "<td>"+ headDataToFixed(allValArr[i]) +"</td>";
		}
	}
	
	return rightHtml += "</tbody>";
};

function growthRateFormula(lastVal,thisVal){
	var adHoc = 0;
	if(thisVal == 0 || lastVal == 0){
		adHoc = 0;
	}else{
		adHoc = (lastVal / thisVal * 100).toFixed(2);
	}
	return adHoc;
};

//业务员月度左边动态数据
function setSalesmanMonthQuarterlyLeftData(obj){
	var leftHtml = "<tbody>";
	for(var i = 0;i < obj.length;i ++){
		leftHtml += "<tr><td>"+ (i+1) +"</td>";
		leftHtml += "<td>"+ obj[i].Saleman +"</td>";
		//leftHtml += "<td>MIN</td></tr>";
	}
	return leftHtml += "<tr>" +
							"<td></td>" +
							"<td>TOTAL</td>" +
//							"<td></td>" +
						"</tr>" +
							"</tbody>";
};

//业务员月度的静态表头
function getSalesmanMonthQuarterlyStaticHead (){
	return "<thead>" +
				"<tr>" +
					"<th rowspan='2'>RANK</th>" +
					"<th rowspan='2'>Saleman</th>" +
					//"<th rowspan='2'>Account</th>" +
				"</tr>" +
				"<tr></tr>" +
			"</thead>";
};