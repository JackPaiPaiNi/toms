


function loadRegTotal(obj,beginDate,endDate,what){//加载数据

	loadRightDataByRegTotal(obj,beginDate,endDate,what);
};
function loadSaleTotal(obj,beginDate,endDate,what){//加载数据

	loadRightDataBySaleTotal(obj,beginDate,endDate,what);
};
function loadAcfoTotal(obj,beginDate,endDate,what){//加载数据

	loadRightDataByAcfoTotal(obj,beginDate,endDate,what);
};


function loadCountryTotal(obj,beginDate,endDate){//加载数据

	loadRightDataByCountryTotal(obj,beginDate,endDate);
};

function loadCountryUD(obj,beginDate,endDate,what){//加载数据

	loadRightDataByCountryUD(obj,beginDate,endDate,what);
};




function loadXCPYearData(obj,beginDate,endDate,whatHead){//加载数据
	loadLeftDataByXCPYear(obj);
	loadRightDataByXCPYear(obj,whatHead);
};

function loadCountryBig(obj,beginDate,endDate){//加载数据

	loadRightDataByCountryBig(obj,beginDate,endDate);
};




function loadMonthCountryTotal(obj,beginDate,endDate,what){//加载数据

	loadMonthDataByCountryTotal(obj,beginDate,endDate,what);
};



function loadXCPMonthData(obj,beginDate,endDate,whatHead){//加载数据
	if(whatHead=="XCP"){
		loadLeftDataByXCPMonth(obj);
	}else{
		loadLeftDataByBigMonth(obj);
	}
	
	loadRightDataByXCPMonth(obj,whatHead);
};




function loadMonthDataByReg(obj,beginDate,endDate,whatHead){//加载数据
	loadRightDataByRegMonth(obj,whatHead);
};

function loadMonthDataBySale(obj,beginDate,endDate,whatHead){//加载数据
	loadRightDataBySaleMonth(obj,whatHead);
};


function loadMonthDataByAcfo(obj,beginDate,endDate,whatHead){//加载数据
	loadRightDataByAcfoMonth(obj,whatHead);
};




function isStringNullAvaliableNumS(val){//字符串是否为空
	if(typeof(val) != "undefined" && val != '' && val != null){
		return val;
	}
	return 0;	
};






function setInitArr(length){
	var arr = [];
	for(var i =0;i<length;i++){
		arr[i] = 0;
	}
	return arr;
};







//年度增长

function loadRightDataByRegTotal(obj,beginDate,endDate,what){//加载右边数据

	var da=beginDate.split("-")[0];
	var beg=parseInt(da[0])-1;
	var end=endDate.split("-")[0];
	
	//第一行tr去年
	//第2行tr今年
	//第三行tr增长
	var html = "<tbody>";
	var by = obj.data;
	
	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		

	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}
	
	html += '</tbody>';
	if(what=="Total"){
		$('#RegTotalByYearleft').html("");
		$('#RegTotalByYearleft').html( loadLeftHeadByRegTotal(obj));
		$('#RegTotalByYearRight').html("");
		$('#RegTotalByYearRight').html( loadRegTotalByYear(obj)+html);
	}else if(what=="UD"){
		$('#RegUDByYearleft').html("");
		$('#RegUDByYearleft').html( loadLeftHeadByRegTotal(obj));
		$('#RegUDByYearRight').html("");
		$('#RegUDByYearRight').html( loadRegTotalByYear(obj)+html);
		
	}else if(what=="XCP"){
		$('#RegXCPByYearleft').html("");
		$('#RegXCPByYearleft').html( loadLeftHeadByRegTotal(obj));
		$('#RegXCPByYearRight').html("");
		$('#RegXCPByYearRight').html( loadRegTotalByYear(obj)+html);
		
	}else if(what=="Smart"){
		$('#RegSmartByYearleft').html("");
		$('#RegSmartByYearleft').html( loadLeftHeadByRegTotal(obj));
		$('#RegSmartByYearRight').html("");
		$('#RegSmartByYearRight').html( loadRegTotalByYear(obj)+html);
		
	}else if(what=="Big"){
		$('#RegBigByYearleft').html("");
		$('#RegBigByYearleft').html( loadLeftHeadByRegTotal(obj));
		$('#RegBigByYearRight').html("");
		$('#RegBigByYearRight').html( loadRegTotalByYear(obj)+html);
		
	}else if(what=="Curved"){
		$('#RegCurvedByYearleft').html("");
		$('#RegCurvedByYearleft').html( loadLeftHeadByRegTotal(obj));
		$('#RegCurvedByYearRight').html("");
		$('#RegCurvedByYearRight').html( loadRegTotalByYear(obj)+html);
		
	}
	
	
	
	

	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}







function loadLeftHeadByRegTotal(obj){//获取固定表头
	
	
	var by = obj.data;
	var html="<thead>";
	html+="<tr>";
	html+="<th style='height:42px;'> </th>";
	html+="<th style='height:42px;'>Regional Head</th>";
	html+="<th style='height:42px;'>AREA</th>";
	html+="</tr>";

	

	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +(i+1)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].RegionalHead)+ "</td>";
		html += "<td id='setTitle' style='white-space: nowrap;text-overflow: ellipsis;overflow: hidden;'>" +isStringNullAvaliable(by[i].AREA)+ "</td>";
		html += "</tr>";
		
	}
	
	
	
	
	if(by.length>0){
		html+="<tr>";
		html+="<th></th>";
		html+="<th>TOTAL</th>";
		html+="<th></th>";
		html+="</tr>";
	}
	html += "</thead>";

	
	return html;
};


//右边表头

function loadRegTotalByYear(obj){
	  var month = ["Jan.", "Feb.", "Mar.", "Apr.", "May.", "June.", "July.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."];
	  var by = obj.data;
	  var mon=by.length > 0 ? by[0].arrQty:[];
	  
	var html="";
	
	html+= '<thead>' ;
	html+=		'<tr>';
			for ( var int = 0; int < mon.length; int++) {
				html+='<th colspan="2">'+month[int]+'</th>';
			  }
			html+='<th colspan="2">Total</th>';
			html+=	'</tr>';
			
			html+=		'<tr>';
			for ( var int = 0; int < mon.length; int++) {
				html+='<th>QTY</th>';
				html+='<th>AMT</th>';
			  }
			html+='<th>QTY</th>';
			html+='<th>AMT</th>';
			html+=	'</tr>';
			
			
			html+='</thead>' ;
			return html;
}



//年度增长

function loadRightDataBySaleTotal(obj,beginDate,endDate,what){//加载右边数据

	var da=beginDate.split("-")[0];
	var beg=parseInt(da[0])-1;
	var end=endDate.split("-")[0];
	
	//第一行tr去年
	//第2行tr今年
	//第三行tr增长
	var html = "<tbody>";
	var by = obj.data;
	
	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		

	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}
	
	html += '</tbody>';
	
	
	if(what=="Total"){

		$('#YearChainSaleLeft').html("");
		$('#YearChainSaleRightHead').html("");
		$('#YearChainSaleRightData').html("");
		if(by.length>0){
			$('#YearChainSaleLeft').html(loadLeftHeadBySaleTotal(obj));
			$('#YearChainSaleRightHead').html(loadSaleTotalByYear(obj));
			$('#YearChainSaleRightData').html(html);
			
			setScrollBarShift("test_Left1","test_Right1");
		}
		

		
	}else if(what=="UD"){
		
		$('#SaleUDByYearleft').html("");
		$('#SaleUDByYearRightHead').html("");
		$('#SaleUDByYearRightData').html("");
		
		if(by.length>0){
			$('#SaleUDByYearleft').html(loadLeftHeadBySaleTotal(obj));
			$('#SaleUDByYearRightHead').html(loadSaleTotalByYear(obj));
			$('#SaleUDByYearRightData').html(html);
			setScrollBarShift("test_Left2","test_Right2");
		}
		
	}else if(what=="XCP"){
		$('#SaleXCPByYearleft').html("");
		$('#SaleXCPByYearRightHead').html("");
		$('#SaleXCPByYearRightData').html("");
		
		if(by.length>0){
			$('#SaleXCPByYearleft').html(loadLeftHeadBySaleTotal(obj));
			$('#SaleXCPByYearRightHead').html(loadSaleTotalByYear(obj));
			$('#SaleXCPByYearRightData').html(html);
			setScrollBarShift("test_Left3","test_Right3");
		}
		
	}else if(what=="Smart"){
		$('#SaleSmartByYearleft').html("");
		$('#SaleSmartByYearRightHead').html("");
		$('#SaleSmartByYearRightData').html("");
		
		if(by.length>0){
			$('#SaleSmartByYearleft').html(loadLeftHeadBySaleTotal(obj));
			$('#SaleSmartByYearRightHead').html(loadSaleTotalByYear(obj));
			$('#SaleSmartByYearRightData').html(html);
			setScrollBarShift("test_Left4","test_Right4");
		}
		
	}else if(what=="Big"){
		$('#SaleBigByYearleft').html("");
		$('#SaleBigByYearRightHead').html("");
		$('#SaleBigByYearRightData').html("");
	
		if(by.length>0){
			$('#SaleBigByYearleft').html(loadLeftHeadBySaleTotal(obj));
			$('#SaleBigByYearRightHead').html(loadSaleTotalByYear(obj));
			$('#SaleBigByYearRightData').html(html);
			setScrollBarShift("test_Left5","test_Right5");
		}
		
		
	}else if(what=="Curved"){
		$('#SaleCurvedByYearleft').html("");
		$('#SaleCurvedByYearRightHead').html("");
		$('#SaleCurvedByYearRightData').html("");
		
		if(by.length>0){
			$('#SaleCurvedByYearleft').html(loadLeftHeadBySaleTotal(obj));
			$('#SaleCurvedByYearRightHead').html(loadSaleTotalByYear(obj));
			$('#SaleCurvedByYearRightData').html(html);
			setScrollBarShift("test_Left6","test_Right6");
		}
		
		
	}
	
	

	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}







function loadLeftHeadBySaleTotal(obj){//获取固定表头
	
	
	var by = obj.data;
	var html="<thead>";
	html+="<tr>";
	html+="<th rowspan='2'> </th>";
	html+="<th rowspan='2'>Salesman</th>";
	html+="</tr>";
	html+="<tr></tr>";
	
	html += "</thead>";
	
	html += "<tbody>";
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +(i+1)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].userName)+ "</td>";
		html += "</tr>";
		
	}
	
	
	
	
	if(by.length>0){
		html+="<tr>";
		html+="<th>TOTAL</th>";
		html+="</tr>";
	}
	html += "</tbody>";

	
	return html;
};


//右边表头

function loadSaleTotalByYear(obj){
	  var month = ["Jan.", "Feb.", "Mar.", "Apr.", "May.", "June.", "July.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."];
	  var by = obj.data;
	  var html="";
	  if(by.length>0){
		  var  mon=by[0].arrQty;

			html+= '<thead>' ;
			html+=		'<tr>';
					for ( var int = 0; int < mon.length; int++) {
						html+='<th colspan="2" style="width: 200px;">'+month[int]+'</th>';
					  }
					html+='<th colspan="2" style="width: 200px;">Total</th>';
					html+=	'</tr>';
					
					html+=		'<tr>';
					for ( var int = 0; int < mon.length; int++) {
						html+='<th>QTY</th>';
						html+='<th>AMT</th>';
					  }
					html+='<th>QTY</th>';
					html+='<th>AMT</th>';
					html+=	'</tr>';
					
					
					html+='</thead>' ;
	  }
	 
	  
	
	
			return html;
}




















//年度增长

function loadRightDataByAcfoTotal(obj,beginDate,endDate,what){//加载右边数据

	var da=beginDate.split("-")[0];
	var beg=parseInt(da[0])-1;
	var end=endDate.split("-")[0];
	
	//第一行tr去年
	//第2行tr今年
	//第三行tr增长
	var html = "<tbody>";
	var by = obj.data;
	
	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		

	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}
	
	html += '</tbody>';
	
	
	
	if(what=="Total"){

		$('#YearChainAcfoLeft').html("");
		$('#YearChainAcfoRightHead').html("");
		$('#YearChainAcfoRightData').html("");
		if(by.length>0){
			$('#YearChainAcfoLeft').html(loadLeftHeadByAcfoTotal(obj));
			$('#YearChainAcfoRightHead').html(loadAcfoTotalByYear(obj));
			$('#YearChainAcfoRightData').html(html);
			
			setScrollBarShift("test_Left7","test_Right7");
		}
		


		
	}else if(what=="UD"){
		
		$('#AcfoUDByYearleft').html("");
		$('#AcfoUDByYearRightHead').html("");
		$('#AcfoUDByYearRightData').html("");
		
		if(by.length>0){
			$('#AcfoUDByYearleft').html(loadLeftHeadByAcfoTotal(obj));
			$('#AcfoUDByYearRightHead').html(loadAcfoTotalByYear(obj));
			$('#AcfoUDByYearRightData').html(html);
			setScrollBarShift("test_Left8","test_Right8");
		}
		
	}else if(what=="XCP"){
		$('#AcfoXCPByYearleft').html("");
		$('#AcfoXCPByYearRightHead').html("");
		$('#AcfoXCPByYearRightData').html("");
		if(by.length>0){
			$('#AcfoXCPByYearleft').html(loadLeftHeadByAcfoTotal(obj));
			$('#AcfoXCPByYearRightHead').html(loadAcfoTotalByYear(obj));
			$('#AcfoXCPByYearRightData').html(html);
			setScrollBarShift("test_Left9","test_Right9");
		}
		
	}else if(what=="Smart"){
		$('#AcfoSmartByYearleft').html("");
		$('#AcfoSmartByYearRightHead').html("");
		$('#AcfoSmartByYearRightData').html("");
		if(by.length>0){
			$('#AcfoSmartByYearleft').html(loadLeftHeadByAcfoTotal(obj));
			$('#AcfoSmartByYearRightHead').html(loadAcfoTotalByYear(obj));
			$('#AcfoSmartByYearRightData').html(html);
			setScrollBarShift("test_Left10","test_Right10");
		}
	
	}else if(what=="Big"){
		$('#AcfoBigByYearleft').html("");
		$('#AcfoBigByYearRightHead').html("");
		$('#AcfoBigByYearRightData').html("");
		if(by.length>0){
			$('#AcfoBigByYearleft').html(loadLeftHeadByAcfoTotal(obj));
			$('#AcfoBigByYearRightHead').html(loadAcfoTotalByYear(obj));
			$('#AcfoBigByYearRightData').html(html);
			setScrollBarShift("test_Left11","test_Right11");
		}
		
		
	}else if(what=="Curved"){
		$('#AcfoCurvedByYearleft').html("");
		$('#AcfoCurvedByYearRightHead').html("");
		$('#AcfoCurvedByYearRightData').html("");
		if(by.length>0){
			$('#AcfoCurvedByYearleft').html(loadLeftHeadByAcfoTotal(obj));
			$('#AcfoCurvedByYearRightHead').html(loadAcfoTotalByYear(obj));
			$('#AcfoCurvedByYearRightData').html(html);
			setScrollBarShift("test_Left12","test_Right12");
		}
		
		
	}
	
	
	
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}







function loadLeftHeadByAcfoTotal(obj){//获取固定表头
	
	
	var by = obj.data;
	var html="<thead>";
	html+="<tr>";
	html+="<th rowspan='2'> </th>";
	html+="<th rowspan='2'>Acfo</th>";
	/*html+="<th rowspan='2'>AREA</th>";*/
	html+="</tr>";
	html+="<tr></tr>";
	
	html += "</thead>";
	
	html += "<tbody>";
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +(i+1)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].userName)+ "</td>";
/*		html += "<td>" +isStringNullAvaliable(by[i].area)+ "</td>";
*/		html += "</tr>";
		
	}
	
	
	
	
	if(by.length>0){
		html+="<tr>";
		html+="<th>TOTAL</th>";
		html+="</tr>";
	}
	html += "</tbody>";

	
	return html;
};


//右边表头

function loadAcfoTotalByYear(obj){
	  var month = ["Jan.", "Feb.", "Mar.", "Apr.", "May.", "June.", "July.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."];
	  var by = obj.data;
	  if(by.length>0){
		  var  mon=by[0].arrQty;
		  var html="";
			
			html+= '<thead>' ;
			html+=		'<tr>';
					for ( var int = 0; int < mon.length; int++) {
						html+='<th colspan="2" style="width: 200px;">'+month[int]+'</th>';
					  }
					html+='<th colspan="2" style="width: 200px;">Total</th>';
					html+=	'</tr>';
					
					html+=		'<tr>';
					for ( var int = 0; int < mon.length; int++) {
						html+='<th>QTY</th>';
						html+='<th>AMT</th>';
					  }
					html+='<th>QTY</th>';
					html+='<th>AMT</th>';
					html+=	'</tr>';
					
					
					html+='</thead>' ;
					return html;
	  }
	  
	
}





function loadRightDataByCountryTotal(obj,beginDate,endDate){//加载右边数据

	var da=beginDate.split("-")[0];
	var beg=parseInt(da[0])-1;
	var end=endDate.split("-")[0];
	
	//第一行tr去年
	//第2行tr今年
	//第三行tr增长
	var html = "<tbody>";
	var by = obj.data;
	
	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		
		/*
	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}*/
	
	html += '</tbody>';
	$('#YearChainCountryData').html("");
	if(by.length>0){
		$('#YearChainCountryData').html( loadRegTotalByYear(obj)+html);
	}

	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}


function loadRightDataByCountryUD(obj,beginDate,endDate,what){//加载右边数据

	var da=beginDate.split("-")[0];
	var beg=parseInt(da[0])-1;
	var end=endDate.split("-")[0];
	
	//第一行tr去年
	//第2行tr今年
	//第三行tr增长
	var html = "<tbody>";
	var by = obj.data;
	
	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		
		/*
	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}*/
	
	html += '</tbody>';
	if(what=="UD"){
		$('#YearUDCountryData').html("");
		if(by.length>0){
			$('#YearUDCountryData').html( loadRegTotalByYear(obj)+html);

		}
	}else if(what=="Smart"){
		$('#YearCountrySmartData').html("");
		if(by.length>0){
			$('#YearCountrySmartData').html( loadRegTotalByYear(obj)+html);
		}
	}else if(what=="Curved"){
		$('#YearCountryCurvedData').html("");
		if(by.length>0){
			$('#YearCountryCurvedData').html( loadRegTotalByYear(obj)+html);
		}
	}
	

	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}





function loadRightDataByXCPYear(obj,whatHead){//加载右边数据
	var html = "<tbody>";
	var by = obj.data;
	var totleVal;
	if(by.length <= 0){
		totleVal = setInitArr(0);
	}else{
		totleVal = setInitArr(by[0].arr.length);
	}
	 
	for(var i =0;i<by.length;i++){
		var arr=by[i].arr;
		var modelTotal=0;
		html += "<tr>";
		for ( var int = 0; int < arr.length; int++) {
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arr[int])+ "</td>";	
			totleVal[int] += accumulativeJudgment(arr[int]);
			modelTotal+=accumulativeJudgment(arr[int]);
		}
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(modelTotal)+ "</td>";	
		html += "</tr>";
		
	}
	
	
	if(by.length>0){
		var sum=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleVal.length; int2++) {
			sum+=accumulativeJudgment(totleVal[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleVal[int2])+ "</td>";
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sum)+ "</td>";

		html += "</tr>";
	}
	
	html += '</tbody>';
	$('#yearXCPRightHead').html("");
	$('#yearXCPRight').html("");
	
	if(by.length>0){
		$('#yearXCPRightHead').html(loadRightHeadByXCPYear(obj,whatHead));
		$('#yearXCPRight').html(html);
		setScrollBarShift("test_Left13","test_Right13");
	}
}


function loadLeftDataByXCPYear(obj){//加载固定列数据
	

	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td id='setTitle'>" +isStringNullAvaliable(by[i].model)+ "</td>";
		html += "</tr>";

	}
	if(by.length>0){
		
		html += "<tr>";
		html += "<td>TOTAL</td>";
		html += "</tr>";
	}

	
	html += '</tbody>';
	$('#yearXCPLeft').html("");
	if(by.length > 0){
		$('#yearXCPLeft').html(loadLeftHeadByXCPYear() + html);
		
		$("[id='setTitle']").mouseenter(function(){
			$(this).attr('title',$(this).html());
		});
	}
}





function loadLeftHeadByXCPYear(){//获取固定表头
	
	
	return '<thead>' +
				'<tr>'+
				'<th rowspan="2">Model</th>'+
				'</tr>'+
				'<tr></tr>'+
			'</thead>' ;
};








//右边表头
function loadRightHeadByXCPYear(obj,whatHead){
	  var month = ["Jan.", "Feb.", "Mar.", "Apr.", "May.", "June.", "July.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."];
	  var by = obj.data;
	  var mon=by[0].arr;
	  
	var html="";
	var cos= mon.length+1;
	var wid=101*cos-3;
	html+= '<thead class="bluegrey">' ;
	html+=	'<tr>';
	html+='<th colspan="'+cos+'" style="width: '+wid+'px;" id="whatXCP">'+whatHead+'</th>';
	html+=		'</tr>';
			
	html+=		'<tr>';
			for ( var int = 0; int < mon.length; int++) {
				html+='<th class="bluegrey">'+month[int]+'</th>';
			  }
			html+='<th class="bluegrey">TTL</th>';
			html+=	'</tr>';
			
			html+='</thead>' ;
			return html;
}

















//====================================countryBig====================================

function loadRightDataByCountryBig(obj,beginDate,endDate){//加载右边数据

	var da=beginDate.split("-")[0];
	var beg=parseInt(da[0])-1;
	var end=endDate.split("-")[0];
	
	//第一行tr去年
	//第2行tr今年
	//第三行tr增长
	var html = "<tbody>";
	var by = obj.data;
	
	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		

	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}
	
	html += '</tbody>';
	$('#YearCountryBigLeft').html("");
	$('#YearCountryBigRightHead').html("");
	$('#YearCountryBigRightData').html("");
	if(by.length>0){
		$('#YearCountryBigLeft').html(loadLeftHeadByCountryBig(obj));
		$('#YearCountryBigRightHead').html(loadCountryBigByYear(obj));
		$('#YearCountryBigRightData').html(html);
	}
	
	
	setScrollBarShift("test_Left14","test_Right14");

	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}







function loadLeftHeadByCountryBig(obj){//获取固定表头
	
	var by = obj.data;
	var html="<thead>";
	html+="<tr>";
	html+="<th rowspan='2'>SIZE</th>";
	html+="<th rowspan='2'>TV TYPE</th>";
	html+="<th rowspan='2'>MODEL</th>";
	html+="</tr>";
	html+="<tr></tr>";
	
	html += "</thead>";
	
	html += "<tbody>";
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +isStringNullAvaliable(by[i].size)+ "</td>";
		html += "<td id='setTitle'>" +isStringNullAvaliable(by[i].spec)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].model)+ "</td>";
		html += "</tr>";
		
	}
	
	
	
	
	if(by.length>0){
		html+="<tr>";
		html+="<th style='border-top:0;'>TOTAL</th>";
		html+="</tr>";
	}
	html += "</tbody>";

	
	return html;
};


//右边表头

function loadCountryBigByYear(obj){
	  var month = ["Jan.", "Feb.", "Mar.", "Apr.", "May.", "June.", "July.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."];
	  var by = obj.data;
	  var mon;
	  if(by.length>0){
		  mon=by[0].arrQty;
	  }
	  
	var html="";
	
	html+= '<thead>' ;
	html+=		'<tr>';
			for ( var int = 0; int < mon.length; int++) {
				html+='<th colspan="2" style="width: 200px;">'+month[int]+'</th>';
			  }
			html+='<th colspan="2" style="width: 200px;">Total</th>';
			html+=	'</tr>';
			
			html+=		'<tr>';
			for ( var int = 0; int < mon.length; int++) {
				html+='<th>QTY</th>';
				html+='<th>AMT</th>';
			  }
			html+='<th>QTY</th>';
			html+='<th>AMT</th>';
			html+=	'</tr>';
			
			
			html+='</thead>' ;
			return html;
}










//======================Month Table==========================


//年度增长

function loadMonthDataByCountryTotal(obj,beginDate,endDate,what){//加载右边数据

	var da=beginDate.split("-")[0];
	var beg=parseInt(da[0])-1;
	var end=endDate.split("-")[0];
	
	//第一行tr去年
	//第2行tr今年
	//第三行tr增长
	var html = "<tbody>";
	var by = obj.data;
	html += "<tr>";
		for(var i =0;i<by.length;i++){
		
			var qtyTotal=0;
			var amtTotal=0;
			
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].arrQty)+ "</td>";	
					qtyTotal+=accumulativeJudgment(by[i].arrQty);
					
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].arrAmt)+ "</td>";	
					amtTotal+=accumulativeJudgment(by[i].arrAmt);
	
	
		
			
		
			
		}
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
		html += "</tr>";
	html += '</tbody>';
	
	
	if(what=="Total"){

		$('#monthCountryTotalData').html("");
		if(by.length>0){
			$('#monthCountryTotalData').html(loadCountryMonthTotalByHead(obj)+html);

		}
		
	}else if(what=="UD"){
		$('#monthCountryUDData').html("");
		if(by.length>0){
			$('#monthCountryUDData').html(loadCountryMonthTotalByHead(obj)+html);

		}
	}else if(what=="XCP"){
		$('#SaleXCPByYearleft').html("");
		$('#SaleXCPByYearRightHead').html("");
		$('#SaleXCPByYearRightData').html("");
		if(by.length>0){
			$('#SaleXCPByYearleft').html(loadLeftHeadBySaleTotal(obj));
			$('#SaleXCPByYearRightHead').html(loadSaleTotalByYear(obj));
			$('#SaleXCPByYearRightData').html(html);
			setScrollBarShift("test_Left3","test_Right3");
		}
		
	}else if(what=="Smart"){
		$('#monthCountrySmartData').html("");
		if(by.length>0){
			$('#monthCountrySmartData').html(loadCountryMonthTotalByHead(obj)+html);

		}
	}else if(what=="Big"){
		$('#SaleBigByYearleft').html("");
		$('#SaleBigByYearRightHead').html("");
		$('#SaleBigByYearRightData').html("");
		if(by.length>0){
			$('#SaleBigByYearleft').html(loadLeftHeadBySaleTotal(obj));
			$('#SaleBigByYearRightHead').html(loadSaleTotalByYear(obj));
			$('#SaleBigByYearRightData').html(html);
			setScrollBarShift("test_Left5","test_Right5");
		}
		
		
	}else if(what=="Curved"){
		$('#monthCountryCurvedData').html("");
		if(by.length>0){
			$('#monthCountryCurvedData').html(loadCountryMonthTotalByHead(obj)+html);

		}
		
	}
	
	

	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}




function loadCountryMonthTotalByHead(obj){
	  var by = obj.data;
	  
	var html="";
	
	

	html+= '<thead>' ;
	html+=		'<tr>';
			for ( var int = 0; int < by.length; int++) {
				html+='<th colspan="2" >'+isStringNullAvaliable(by[int].week)+'</th>';
			  }
			html+='<th colspan="2" >Total</th>';
			html+=	'</tr>';
			
			html+=		'<tr>';
			
			for ( var int = 0; int < by.length; int++) {
				html+='<th class="lightBlue">Qty</th>';
				html+='<th class="lightBlue">Amt</th>';
			  }
			html+='<th>QTY</th>';
			html+='<th>AMT</th>';
			html+=	'</tr>';
			
			html+='</thead>' ;
			return html;
}







function loadRightDataByXCPMonth(obj,whatHead){//加载右边数据
	
	
	var html = "<tbody>";
	var by = obj.data;

	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		

	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}
	
	html += '</tbody>';
	if(whatHead=="XCP"){
		$('#monthCountryXCPRightHead').html("");
		$('#monthCountryXCPRightData').html("");
		$('#monthCountryXCPRightHead').html(loadRightHeadByXCPMonth(obj,whatHead));
		$('#monthCountryXCPRightData').html(html);
		setScrollBarShift("test_Left33","test_Right33");
	}else{
		$('#monthCountryBigRightHead').html("");
		$('#monthCountryBigRightData').html("");
		$('#monthCountryBigRightHead').html(loadRightHeadByXCPMonth(obj,whatHead));
		$('#monthCountryBigRightData').html(html);
		setScrollBarShift("test_Left34","test_Right34");

	}
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
	
}


function loadLeftDataByXCPMonth(obj){//加载固定列数据
	

	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td id='setTitle'>" +isStringNullAvaliable(by[i].model)+ "</td>";
		html += "</tr>";

	}
	if(by.length>0){
		
		html += "<tr>";
		html += "<td>TOTAL</td>";
		html += "</tr>";
	}

	
	html += '</tbody>';
	$('#monthCountryXCPleft').html("");
	if(by.length>0){
		$('#monthCountryXCPleft').html(loadLeftHeadByXCPMonth() + html);
	}
	
	
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}





function loadLeftHeadByXCPMonth(){//获取固定表头
	
	
	return '<thead>' +
				'<tr>'+
				'<th rowspan="2">Model</th>'+
				'</tr>'+
				'<tr></tr>'+
			'</thead>' ;
};







//右边表头
function loadRightHeadByXCPMonth(obj,whatHead){
	  var by = obj.data;
	  var html="";
	  if(by.length>0){
		  var week=by[0].week;
		  html+= '<thead>' ;
			html+=		'<tr>';
					for ( var int = 0; int < week.length; int++) {
						html+='<th colspan="2" style="width: 200px;" >'+isStringNullAvaliable(week[int])+'</th>';
					  }
					html+='<th colspan="2"  style="width: 200px;">Total</th>';
					html+=	'</tr>';
					
					html+=		'<tr>';
					
					
					for ( var int = 0; int < week.length; int++) {
						html+='<th class="lightBlue">Qty</th>';
						html+='<th class="lightBlue">Amt</th>';
					  }
					html+='<th>QTY</th>';
					html+='<th>AMT</th>';
					html+=	'</tr>';
					
					html+= '</thead>' ;
	  }
	 
		
		
		

		
		
				return html;
				}

function loadLeftDataByBigMonth(obj){//获取固定表头
						
						var by = obj.data;
						var html="<thead>";
						html+="<tr>";
						html+="<th rowspan='2'>SIZE</th>";
						html+="<th rowspan='2'>TV TYPE</th>";
						html+="<th rowspan='2'>MODEL</th>";
						html+="</tr>";
						html+="<tr></tr>";
						
						html += "</thead>";
						
						html += "<tbody>";
						for(var i =0;i<by.length;i++){
							html += "<tr>";
							html += "<td>" +isStringNullAvaliable(by[i].size)+ "</td>";
							html += "<td id='setTitle'>" +isStringNullAvaliable(by[i].spec)+ "</td>";
							html += "<td>" +isStringNullAvaliable(by[i].model)+ "</td>";
							html += "</tr>";
							
						}
						
						
						
						
						if(by.length>0){
							html+="<tr>";
							html+="<th style='border-top:0;'>TOTAL</th>";
							html+="</tr>";
						}
						html += "</tbody>";

						
					
				$('#monthCountryBigleft').html("");
				if(by.length>0){
					$('#monthCountryBigleft').html( html);
				}
				
				
				
				$("[id='setTitle']").mouseenter(function(){
					$(this).attr('title',$(this).html());
				});
				
}




//=================================================Regional Head=============================================================================================





//右边表头
function loadRightHeadByRegMonth(obj,whatHead){
	  var by = obj.data;
	  var html="";
	  if(by.length>0){
		  var week=by[0].week;
		  html+= '<thead>' ;
			html+=		'<tr>';
					for ( var int = 0; int < week.length; int++) {
						html+='<th colspan="2" >'+isStringNullAvaliable(week[int])+'</th>';
					  }
					html+='<th colspan="2" >Total</th>';
					html+=	'</tr>';
					
					html+=		'<tr>';
					
					
					for ( var int = 0; int < week.length; int++) {
						html+='<th class="lightBlue">Qty</th>';
						html+='<th class="lightBlue">Amt</th>';
					  }
					html+='<th>QTY</th>';
					html+='<th>AMT</th>';
					html+=	'</tr>';
					
					html+= '</thead>' ;
	  }
	 
		
		
				return html;
}

function loadLeftDataByRegMonth(obj){//获取固定表头
						
						var by = obj.data;
						var html="<thead>";
						html+="<tr>";
						html+="<th style='height: 42px;'>RANK</th>";
						html+="<th style='height: 42px;'>Regional Head</th>";
						html+="<th style='height: 42px;'>AREA</th>";
						html+="</tr>";
						html+="<tr></tr>";
						
						html += "</thead>";
						
						html += "<tbody>";
						for(var i =0;i<by.length;i++){
							html += "<tr>";
							html += "<td>" +(i+1)+ "</td>";
							html += "<td >" +isStringNullAvaliable(by[i].RegionalHead)+ "</td>";
							html += "<td id='setTitle' style='white-space: nowrap;text-overflow: ellipsis;overflow: hidden;'>" +isStringNullAvaliable(by[i].AREA)+ "</td>";
							html += "</tr>";
							
						}
						
						
						
						
						if(by.length>0){
							html+="<tr>";
							html+="<th></th>";
							html+="<th>TOTAL</th>";
							html+="<th></th>";
							html+="</tr>";
						}
						html += "</tbody>";
			
				return  html;
}


function loadRightDataByRegMonth(obj,whatHead){//加载右边数据
	
	
	var html = "<tbody>";
	var by = obj.data;

	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		

	if(by.length>0){
		
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}
	
	html += '</tbody>';
	if(whatHead=="Total"){
		$('#monthRegTotalLeft').html("");
		$('#monthRegTotalRight').html("");
		$('#monthRegTotalLeft').html( loadLeftDataByRegMonth(obj));
		$('#monthRegTotalRight').html(loadRightHeadByRegMonth(obj,whatHead)+html);
	}else if(whatHead=="UD"){
		$('#monthRegUDLeft').html("");
		$('#monthRegUDRight').html("");
		$('#monthRegUDLeft').html( loadLeftDataByRegMonth(obj));
		$('#monthRegUDRight').html(loadRightHeadByRegMonth(obj,whatHead)+html);
	}else if(whatHead=="XCP"){
		$('#monthRegXCPLeft').html("");
		$('#monthRegXCPRight').html("");
		$('#monthRegXCPLeft').html( loadLeftDataByRegMonth(obj));
		$('#monthRegXCPRight').html(loadRightHeadByRegMonth(obj,whatHead)+html);
	}else if(whatHead=="Smart"){
		$('#monthRegSmartLeft').html("");
		$('#monthRegSmartRight').html("");
		$('#monthRegSmartLeft').html( loadLeftDataByRegMonth(obj));
		$('#monthRegSmartRight').html(loadRightHeadByRegMonth(obj,whatHead)+html);
	}else if(whatHead=="Big"){
		$('#monthRegBigLeft').html("");
		$('#monthRegBigRight').html("");
		$('#monthRegBigLeft').html( loadLeftDataByRegMonth(obj));
		$('#monthRegBigRight').html(loadRightHeadByRegMonth(obj,whatHead)+html);
	}else if(whatHead=="Curved"){
		$('#monthRegCurvedLeft').html("");
		$('#monthRegCurvedRight').html("");
		$('#monthRegCurvedLeft').html( loadLeftDataByRegMonth(obj));
		$('#monthRegCurvedRight').html(loadRightHeadByRegMonth(obj,whatHead)+html);
	}
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
	
}















//=================================================SalesMan=============================================================================================





//右边表头
function loadRightHeadBySaleMonth(obj,whatHead){
	  var by = obj.data;
	  var html="";
	  if(by.length>0){
		  var week=by[0].week;
		  html+= '<thead>' ;
			html+=		'<tr>';
					for ( var int = 0; int < week.length; int++) {
						html+='<th   colspan="2" style="width: 200px;" >'+isStringNullAvaliable(week[int])+'</th>';
					  }
					html+='<th  colspan="2" style="width: 200px;" >Total</th>';
					html+=	'</tr>';
					
					html+=		'<tr>';
					
					
					for ( var int = 0; int < week.length; int++) {
						html+='<th class="lightBlue">Qty</th>';
						html+='<th class="lightBlue">Amt</th>';
					  }
					html+='<th>QTY</th>';
					html+='<th>AMT</th>';
					html+=	'</tr>';
					
					html+= '</thead>' ;
	  }
	 
		
		
				return html;
}

function loadLeftDataBySaleMonth(obj){//获取固定表头
						
						var by = obj.data;
						var html="<thead>";
						html+="<tr>";
						html+="<th  rowspan='2' > </th>";
						html+="<th  rowspan='2' >Salesman</th>";
						html+="</tr>";
						html+="<tr></tr>";
						
						html += "</thead>";
						
						html += "<tbody>";
						for(var i =0;i<by.length;i++){
							html += "<tr>";
							html += "<td>" +(i+1)+ "</td>";
							html += "<td >" +isStringNullAvaliable(by[i].userName)+ "</td>";
							html += "</tr>";
							
						}
						
						
						
						
						if(by.length>0){
							html+="<tr>";
							html+="<th>TOTAL</th>";
							html+="</tr>";
						}
						html += "</tbody>";
			
				return  html;
}


function loadRightDataBySaleMonth(obj,whatHead){//加载右边数据
	
	
	var html = "<tbody>";
	var by = obj.data;

	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		

	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}
	
	html += '</tbody>';
	if(whatHead=="Total"){
		$('#monthSaleTotalLeft').html("");
		$('#monthSaleTotalRightHead').html("");
		$('#monthSaleTotalRightData').html("");
		if(by.length>0){
			$('#monthSaleTotalLeft').html( loadLeftDataBySaleMonth(obj));
			$('#monthSaleTotalRightHead').html(loadRightHeadBySaleMonth(obj,whatHead));
			$('#monthSaleTotalRightData').html(html);
			
			setScrollBarShift("test_Left21","test_Right21");
		}
		
	}else if(whatHead=="UD"){
		$('#monthSaleUDLeft').html("");
		$('#monthSaleUDRightHead').html("");
		$('#monthSaleUDRightData').html("");

		if(by.length>0){
			$('#monthSaleUDLeft').html( loadLeftDataBySaleMonth(obj));
			$('#monthSaleUDRightHead').html(loadRightHeadBySaleMonth(obj,whatHead));
			$('#monthSaleUDRightData').html(html);
			setScrollBarShift("test_Left22","test_Right22");
		}
		
		
	}else if(whatHead=="XCP"){
		$('#monthSaleXCPLeft').html("");
		$('#monthSaleXCPRightHead').html("");
		$('#monthSaleXCPRightData').html("");
		
		if(by.length>0){
			$('#monthSaleXCPLeft').html( loadLeftDataBySaleMonth(obj));
			$('#monthSaleXCPRightHead').html(loadRightHeadBySaleMonth(obj,whatHead));
			$('#monthSaleXCPRightData').html(html);
			setScrollBarShift("test_Left23","test_Right23");
		}
		
	}else if(whatHead=="Smart"){
		$('#monthSaleSmartLeft').html("");
		$('#monthSaleSmartRightHead').html("");
		$('#monthSaleSmartRightData').html("");
		
		if(by.length>0){
			$('#monthSaleSmartLeft').html( loadLeftDataBySaleMonth(obj));
			$('#monthSaleSmartRightHead').html(loadRightHeadBySaleMonth(obj,whatHead));
			$('#monthSaleSmartRightData').html(html);
			setScrollBarShift("test_Left24","test_Right24");
		}
		
	}else if(whatHead=="Big"){
		$('#monthSaleBigLeft').html("");
		$('#monthSaleBigRightHead').html("");
		$('#monthSaleBigRightData').html("");
		
		if(by.length>0){
			$('#monthSaleBigLeft').html( loadLeftDataBySaleMonth(obj));
			$('#monthSaleBigRightHead').html(loadRightHeadBySaleMonth(obj,whatHead));
			$('#monthSaleBigRightData').html(html);
			setScrollBarShift("test_Left25","test_Right25");
		}
		
	}else if(whatHead=="Curved"){
		$('#monthSaleCurvedLeft').html("");
		$('#monthSaleCurvedRightHead').html("");
		$('#monthSaleCurvedRightData').html("");
		
		if(by.length>0){
			$('#monthSaleCurvedLeft').html( loadLeftDataBySaleMonth(obj));
			$('#monthSaleCurvedRightHead').html(loadRightHeadBySaleMonth(obj,whatHead));
			$('#monthSaleCurvedRightData').html(html);
			setScrollBarShift("test_Left26","test_Right26");
		}
		
	}
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
	
}














//=================================================Acfo=============================================================================================





//右边表头
function loadRightHeadByAcfoMonth(obj,whatHead){
	  var by = obj.data;
	  var html="";
	  if(by.length>0){
		  var week=by[0].week;
		  html+= '<thead>' ;
			html+=		'<tr>';
					for ( var int = 0; int < week.length; int++) {
						html+='<th   colspan="2" style="width: 200px;" >'+isStringNullAvaliable(week[int])+'</th>';
					  }
					html+='<th  colspan="2" style="width: 200px;">Total</th>';
					html+=	'</tr>';
					
					html+=		'<tr>';
					
					
					for ( var int = 0; int < week.length; int++) {
						html+='<th class="lightBlue">Qty</th>';
						html+='<th class="lightBlue">Amt</th>';
					  }
					html+='<th>QTY</th>';
					html+='<th>AMT</th>';
					html+=	'</tr>';
					
					html+= '</thead>' ;
	  }
	 
		
		
				return html;
}

function loadLeftDataByAcfoMonth(obj){//获取固定表头
						
					var by = obj.data;
					var html="<thead>";
					html+="<tr>";
					html+="<th   rowspan='2' > </th>";
					html+="<th  rowspan='2' >Acfo</th>";
					html+="</tr>";
					html+="<tr></tr>";
					
					html += "</thead>";
					
					html += "<tbody>";
					for(var i =0;i<by.length;i++){
						html += "<tr>";
						html += "<td>" +(i+1)+ "</td>";
						html += "<td >" +isStringNullAvaliable(by[i].userName)+ "</td>";
						html += "</tr>";
						
					}
					
					
					
					
					if(by.length>0){
						html+="<tr>";
						html+="<th>TOTAL</th>";
						html+="</tr>";
					}
					html += "</tbody>";
				
				return  html;
}


function loadRightDataByAcfoMonth(obj,whatHead){//加载右边数据
	
	
	var html = "<tbody>";
	var by = obj.data;

	var totleValQty;
	var totleValAmt;
	if(by.length <= 0){
		totleValQty = setInitArr(0);
		totleValAmt = setInitArr(0);
	}else{
		totleValQty = setInitArr(by[0].arrQty.length);
		totleValAmt = setInitArr(by[0].arrAmt.length);
	}
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					totleValQty[int] += accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
							totleValAmt[x] += accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		

	if(by.length>0){
		var sumQty=0;
		var sumAmt=0;
		html += "<tr>";
		for ( var int2 = 0; int2 < totleValQty.length; int2++) {
			sumQty+=accumulativeJudgment(totleValQty[int2]);
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValQty[int2])+ "</td>";
			for ( var x = 0; x < totleValAmt.length; x++) {
				if(x==int2){
					sumAmt+=accumulativeJudgment(totleValAmt[x]);
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(totleValAmt[x])+ "</td>";	
					
				}
				
			}
		}
		
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(sumAmt)+ "</td>";
		html += "</tr>";
	}
	
	html += '</tbody>';
	if(whatHead=="Total"){
		$('#monthAcfoTotalLeft').html("");
		$('#monthAcfoTotalRightHead').html("");
		$('#monthAcfoTotalRightData').html("");
		if(by.length>0){
			$('#monthAcfoTotalLeft').html( loadLeftDataByAcfoMonth(obj));
			$('#monthAcfoTotalRightHead').html(loadRightHeadByAcfoMonth(obj,whatHead));
			$('#monthAcfoTotalRightData').html(html);
			
			setScrollBarShift("test_Left27","test_Right27");
		}
		
	}else if(whatHead=="UD"){
		$('#monthAcfoUDLeft').html("");
		$('#monthAcfoUDRightHead').html("");
		$('#monthAcfoUDRightData').html("");

		if(by.length>0){
			$('#monthAcfoUDLeft').html( loadLeftDataByAcfoMonth(obj));
			$('#monthAcfoUDRightHead').html(loadRightHeadByAcfoMonth(obj,whatHead));
			$('#monthAcfoUDRightData').html(html);
			setScrollBarShift("test_Left28","test_Right28");
		}
		
		
	}else if(whatHead=="XCP"){
		$('#monthAcfoXCPLeft').html("");
		$('#monthAcfoXCPRightHead').html("");
		$('#monthAcfoXCPRightData').html("");
		
		if(by.length>0){
			$('#monthAcfoXCPLeft').html( loadLeftDataByAcfoMonth(obj));
			$('#monthAcfoXCPRightHead').html(loadRightHeadByAcfoMonth(obj,whatHead));
			$('#monthAcfoXCPRightData').html(html);
			setScrollBarShift("test_Left29","test_Right29");
		}
		
		
	}else if(whatHead=="Smart"){
		$('#monthAcfoSmartLeft').html("");
		$('#monthAcfoSmartRightHead').html("");
		$('#monthAcfoSmartRightData').html("");
		
		if(by.length>0){
			$('#monthAcfoSmartLeft').html( loadLeftDataByAcfoMonth(obj));
			$('#monthAcfoSmartRightHead').html(loadRightHeadByAcfoMonth(obj,whatHead));
			$('#monthAcfoSmartRightData').html(html);
			setScrollBarShift("test_Left30","test_Right30");
		}
		
		
	}else if(whatHead=="Big"){
		$('#monthAcfoBigLeft').html("");
		$('#monthAcfoBigRightHead').html("");
		$('#monthAcfoBigRightData').html("");
		
		if(by.length>0){
			$('#monthAcfoBigLeft').html( loadLeftDataByAcfoMonth(obj));
			$('#monthAcfoBigRightHead').html(loadRightHeadByAcfoMonth(obj,whatHead));
			$('#monthAcfoBigRightData').html(html);
			setScrollBarShift("test_Left31","test_Right31");
		}
		
	}else if(whatHead=="Curved"){
		$('#monthAcfoCurvedLeft').html("");
		$('#monthAcfoCurvedRightHead').html("");
		$('#monthAcfoCurvedRightData').html("");
		
		if(by.length>0){
			$('#monthAcfoCurvedLeft').html( loadLeftDataByAcfoMonth(obj));
			$('#monthAcfoCurvedRightHead').html(loadRightHeadByAcfoMonth(obj,whatHead));
			$('#monthAcfoCurvedRightData').html(html);
			setScrollBarShift("test_Left32","test_Right32");
		}
		
	}
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
	
}

