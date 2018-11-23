
function loadREgMonthData(obj,whatTime){//加载数据
	loadRightData(obj,whatTime);
	loadLeftData(obj,whatTime);
};


function loadAcfoMonthData(obj,whatTime){//加载数据
	loadRightDataByAcfo(obj,whatTime);
	loadLeftDataByAcfo(obj,whatTime);
};

//tt
function loadSaleMonthGrowth(obj,beginDate,endDate,whatTime){//加载数据
	loadRightDataBySaleGro(obj,beginDate,endDate,whatTime);
	loadLeftDataBySaleGro(obj,beginDate,endDate,whatTime);
};




function loadCustomerWeekGrowth(obj,beginDate,endDate,whatTime){//加载数据
	loadRightDataByCustomerGro(obj,beginDate,endDate,whatTime);
	loadLeftDataByCustomerGro(obj,beginDate,endDate,whatTime);
};

function loadCustomerWeekGrowths(obj,beginDate,endDate,tab){//加载数据
	loadRightDataByCustomerGros(obj,beginDate,endDate,tab);
	loadLeftDataByCustomerGros(obj,beginDate,endDate,tab);
};



function loadSaleYearData(obj){//加载数据
	loadSaleDataByYear(obj);
};

function loadRegYearData(obj){//加载数据
	loadRegDataByYear(obj);
};

function loadAcfoYearData(obj){//加载数据
	loadAcfoDataByYear(obj);
};



function loadXCPYearData(obj,beginDate,endDate,whatHead){//加载数据

	loadLeftDataByXCPYear(obj);
	loadRightDataByXCPYear(obj,whatHead);
};
function loadCountryGroData(obj,beginDate,endDate,what){//加载数据

	loadRightDataByCountryGro(obj,beginDate,endDate,what);
};





function isStringNullAvaliableNumS(val){//字符串是否为空
	if(typeof(val) != "undefined" && val != '' && val != null){
		return val;
	}
	return 0;	
};



function loadRightData(obj,whatTime){//加载右边数据
	var Shop=0;
	var Fps=0;
	var SoQty=0;
	var SoAmt=0;
	var TargetAmt=0;
	var Ach=0;
	var Ave_FPS_Qty=0;
	var Ave_FPS_Amount=0;
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Shop)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].TargetAmt)+ "</td>";
		html += reachTheBackgroundColor(isStringNullAvaliableNum(by[i].Ach));
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Ave_FPS_Qty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Ave_FPS_Amount)+ "</td>";
		html += "</tr>";
		Shop+=parseInt(isStringNullAvaliableNumS(by[i].Shop));
		Fps+=parseInt(isStringNullAvaliableNumS(by[i].Fps));
		SoQty+=parseInt(isStringNullAvaliableNumS(by[i].SoQty));
		SoAmt+=parseInt(isStringNullAvaliableNumS(by[i].SoAmt));
		TargetAmt+=parseInt(isStringNullAvaliableNumS(by[i].TargetAmt));
	}

	if(TargetAmt!=0){
		Ach=Math.round(SoAmt/TargetAmt*100);
	}else{
		Ach=100;
	}
	if(Fps!=0){
		Ave_FPS_Qty=Math.round(SoQty/Fps);
		Ave_FPS_Amount=Math.round(SoAmt/Fps);
	}
	if(by.length>0){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Shop)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(TargetAmt)+ "</td>";
		html += "<td  '>" +isStringNullAvaliableNum(Ach)+ "%</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Ave_FPS_Qty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Ave_FPS_Amount)+ "</td>";
		html += "</tr>";
		
	}
	
	html += '</tbody>';
	if(whatTime=="RegMonth"){
		$('#monthRegRight').html("");
		$('#monthRegRight').html(loadRightHead() + html);
	}else if(whatTime=="RegWeek"){
		$('#weekRegRight').html("");
		$('#weekRegRight').html(loadRightHead() + html);
	}
	
	
}

function loadLeftData(obj,whatTime){//加载固定列数据
	

	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +isStringNullAvaliable(by[i].Rank)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].RegionalHead)+ "</td>";
		html += "<td id='setTitle'>" +isStringNullAvaliable(by[i].AREA)+ "</td>";
		
		html += "</tr>";

		
	}
	if(by.length>0){
		
		html += "<tr>";
		html += "<th></th>";
		html += "<th>TOTAL</th>";
		html += "<th></th>";
		html += "</tr>";
	}

	
	html += '</tbody>';
	if(whatTime=="RegMonth"){
		$('#monthRegLeft').html("");
		$('#monthRegLeft').html(loadLeftHead() + html);
	}else if(whatTime=="RegWeek"){
		$('#weekRegLeft').html("");
		$('#weekRegLeft').html(loadLeftHead() + html);
	}
	
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}



//右边表头
function loadRightHead(){
	

return '<thead>' +
'<tr>'+
'<th rowspan="2">No of Shops</th>'+
'<th rowspan="2">TV FPS</th>'+
'<th class="TVblue" colspan="2">TTL TV SO</th>'+
'<th class="lightBlue" rowspan="2">Basic TV Target</th>'+

'<th class="lightBlue" rowspan="2">Ach</th>'+
'<th class="lightBlue" colspan="2">Ave.SO per FPS</th>'+
'</tr>'+
'<tr>'+
'<th class="TVblue">Qty</th>'+
'<th class="TVblue">Amt</th>'+
'<th class="lightBlue">Qty</th>'+
'<th class="lightBlue">Amt</th>'+
'</tr>'+
'</thead>' ;
}


function loadLeftHead(){//获取固定表头
	return '<thead>' +
				'<th style="height: 42px;">RANK</th>'+
				'<th>Regional Head</th>'+
				'<th>AREA</th>'+
			'</thead>' ;
};























//AcfoByMonth

function loadRightDataByAcfo(obj,whatTime){//加载右边数据
	var Shop=0;
	var Fps=0;
	var SoQty=0;
	var SoAmt=0;
	var TargetAmt=0;
	var Ach=0;
	var Ave_FPS_Qty=0;
	var Ave_FPS_Amount=0;
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Shop)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].TargetAmt)+ "</td>";
		html += reachTheBackgroundColor(isStringNullAvaliableNum(by[i].Ach));
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Ave_FPS_Qty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Ave_FPS_Amount)+ "</td>";
		html += "</tr>";
		Shop+=parseInt(isStringNullAvaliableNumS(by[i].Shop));
		Fps+=parseInt(isStringNullAvaliableNumS(by[i].FPS));
		SoQty+=parseInt(isStringNullAvaliableNumS(by[i].SoQty));
		SoAmt+=parseInt(isStringNullAvaliableNumS(by[i].SoAmt));
		TargetAmt+=parseInt(isStringNullAvaliableNumS(by[i].TargetAmt));
	}

	if(TargetAmt!=0){
		Ach=Math.round(SoAmt/TargetAmt*100);
	}else{
		Ach=100;
	}
	if(Fps!=0){
		Ave_FPS_Qty=Math.round(SoQty/Fps);
		Ave_FPS_Amount=Math.round(SoAmt/Fps);
	}
	if(by.length>0){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Shop)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(TargetAmt)+ "</td>";
		html += "<td >" +isStringNullAvaliableNum(Ach)+ "%</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Ave_FPS_Qty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Ave_FPS_Amount)+ "</td>";
		html += "</tr>";
	}
	
	
	html += '</tbody>';
	if(whatTime=="AcfoMonth"){
		
		if(by.length == 0){
			setHideOrShow(true,'monthAcfoRightHead','monthAcfoRight');
		}else{
			setHideOrShow(false,'monthAcfoRightHead','monthAcfoRight');
			
			$('#monthAcfoRightHead').html(loadRightHeadByAcfo() );
			$('#monthAcfoRight').html( html);
			setScrollBarShift("test_Left","test_Right");
		}
		
	}else if(whatTime=="AcfoWeek"){
		
		if(by.length == 0){
			setHideOrShow(true,'weekAcfoRightHead','weekAcfoRight');
		}else{
			setHideOrShow(false,'weekAcfoRightHead','weekAcfoRight');
			$('#weekAcfoRightHead').html(loadRightHeadByAcfo() );
			$('#weekAcfoRight').html( html);
			setScrollBarShift("test_Left3","test_Right3");
		}
	}
}

function setHideOrShow(isHide,headId,rightId){
	if(isHide){
		$('#' + headId).parent().hide();
		$('#' + rightId).parent().hide();
	}else{
		$('#' + headId).parent().show();
		$('#' + rightId).parent().show();
	}
}

function loadLeftDataByAcfo(obj,whatTime){//加载固定列数据
	

	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +isStringNullAvaliable(by[i].Rank)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].Acfo)+ "</td>";
		html += "<td id='setTitle'>" +isStringNullAvaliable(by[i].Area)+ "</td>";
		
		html += "</tr>";

		
	}
	if(by.length>0){
		html += "<tr>";
		html += "<th style='border-top:0;'>TOTAL</th>";
		
		html += "</tr>";
	
	}

	html += '</tbody>';
	if(whatTime=="AcfoMonth"){
		if( by.length == 0){
			$('#monthAcfoLeft').parent().hide();
		}else{
			$('#monthAcfoLeft').parent().show();
			$('#monthAcfoLeft').html(loadLeftHeadByAcfo() + html);
			$("[id='setTitle']").mouseenter(function(){
				$(this).attr('title',$(this).html());
			});
		}
	}else if(whatTime=="AcfoWeek"){
		if( by.length == 0){
			$('#weekAcfoLeft').parent().hide();
		}else{
			$('#weekAcfoLeft').parent().show();
			$('#weekAcfoLeft').html(loadLeftHeadByAcfo() + html);
			$("[id='setTitle']").mouseenter(function(){
				$(this).attr('title',$(this).html());
			});
		}
	}
}



//右边表头
function loadRightHeadByAcfo(){
	

return '<thead>' +
'<tr>'+
'<th rowspan="2">No of Shops</th>'+
'<th rowspan="2">TV FPS</th>'+
'<th class="TVblue" colspan="2" style="width: 200px;">TTL TV SO</th>'+
'<th class="lightBlue" rowspan="2">Basic TV Target</th>'+

'<th class="lightBlue" rowspan="2">Ach</th>'+
'<th class="lightBlue" colspan="2" style="width: 200px;">Ave.SO per FPS</th>'+
'</tr>'+
'<tr>'+
'<th class="TVblue">Qty</th>'+
'<th class="TVblue">Amt</th>'+
'<th class="lightBlue">Qty</th>'+
'<th class="lightBlue">Amt</th>'+
'</tr>'+
'</thead>' ;
}


function loadLeftHeadByAcfo(){//获取固定表头
	return '<thead>' +
				'<th style="height: 44px;">RANK</th>'+
				'<th>Acfo</th>'+
				'<th >AREA</th>'+
			'</thead>' ;
};







function loadLeftHeadBySaleGro(){//获取固定表头

	return '<thead>' +
			'<tr class="bluegrey">'+
				'<th rowspan="2">RANK</th>'+
				'<th rowspan="2">Saleman</th>'+
				'</tr>'+
				'<tr></tr>'+
			'</thead>' ;
};


//右边表头
function loadRightHeadBySaleGro(beginDate){
	var date =beginDate.split("-");
	var toYear=parseInt(date[0])-1;
	var month=getENMonth(date[1]);
	return '<thead>' +
			'<tr>'+
			'<th class="lightBlue" colspan="2" style="width: 200px;">FPS</th>'+
			'<th class="lightBlue" colspan="3" style="width: 301px;">Total Flat Panel TV Quantity</th>'+
			'<th class="lightBlue" colspan="3" style="width: 301px;">Total Amount</th>'+
			'<th class="lightBlue" colspan="4" style="width: 401px;">Average sellout per fps</th>'+
			'</tr>'+
			
			'<tr>'+
			'	<th class="bluegrey">Yr-'+toYear+'</th>'+
			 '<th class="bluegrey">Yr-'+date[0]+'</th>'+
			 '<th class="bluegrey">'+month+' '+toYear+'</th>'+
			'<th class="bluegrey">'+month+' '+date[0]+'</th>'+
			'<th class="bluegrey">Growth</th>'+
			'<th class="bluegrey">'+month+' '+toYear+'</th>'+
			'<th class="bluegrey">'+month+' '+date[0]+'</th>'+
			'<th class="bluegrey">Growth</th>'+
			'<th class="bluegrey">'+toYear +' Ave.qty</th>'+
			'<th class="bluegrey">'+date[0]+' Ave.qty</th>'+
			'<th class="bluegrey">'+toYear +' Ave.amt</th>'+
			'<th class="bluegrey">'+date[0]+' Ave.amt</th>'+
			'</tr>'+
			
			'</thead>' ;
}




function loadRightHeadBySaleGroWeek(beginDate,endDate){
	var dateBeg =beginDate.split("-");
	var dateEnd =endDate.split("-");
	var toYearBeg=parseInt(dateBeg[0])-1;
	var monthBeg=getENMonth(dateBeg[1]);
	
	var toYearEnd=parseInt(dateEnd[0])-1;
	var monthEnd=getENMonth(dateEnd[1]);
	
	
	return '<thead>' +
			'<tr>'+
			'<th class="lightBlue" colspan="2" style="width: 200px;">FPS</th>'+
			'<th class="lightBlue" colspan="3" style="width: 301px;">Total Flat Panel TV Quantity</th>'+
			'<th class="lightBlue" colspan="3" style="width: 301px;">Total Amount</th>'+
			'<th class="lightBlue" colspan="4" style="width: 401px;">Average sellout per fps</th>'+
			'</tr>'+
			
			'<tr>'+
			'	<th class="bluegrey">Yr-'+toYearBeg+'</th>'+
			 '<th class="bluegrey">Yr-'+dateBeg[0]+'</th>'+
			 '<th class="bluegrey" id="setTitle">'+monthBeg+dateBeg[2]+","+toYearBeg+"-"+monthEnd+dateEnd[2]+","+toYearEnd+'</th>'+
			'<th class="bluegrey" id="setTitle">'+monthBeg+dateBeg[2]+","+dateBeg[0]+"-"+monthEnd+dateEnd[2]+","+dateEnd[0]+'</th>'+
			'<th class="bluegrey">Growth</th>'+
			 '<th class="bluegrey" id="setTitle">'+monthBeg+dateBeg[2]+","+toYearBeg+"-"+monthEnd+dateEnd[2]+","+toYearEnd+'</th>'+
			'<th class="bluegrey" id="setTitle">'+monthBeg+dateBeg[2]+","+dateBeg[0]+"-"+monthEnd+dateEnd[2]+","+dateEnd[0]+'</th>'+
			'<th class="bluegrey">Growth</th>'+
			'<th class="bluegrey">'+toYearEnd +' Ave.qty</th>'+
			'<th class="bluegrey">'+dateBeg[0]+' Ave.qty</th>'+
			'<th class="bluegrey">'+toYearEnd +' Ave.amt</th>'+
			'<th class="bluegrey">'+dateBeg[0]+' Ave.amt</th>'+
			'</tr>'+
			
			'</thead>' ;
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}





//右边表头
function loadRightHeadBySaleGroYear(beginDate,endDate){
	var dateBeg =beginDate.split("-");
	var dateEnd =endDate.split("-");
	var toYearBeg=parseInt(dateBeg[0])-1;
	var monthBeg=getENMonth(dateBeg[1]);
	
	var toYearEnd=parseInt(dateEnd[0])-1;
	var monthEnd=getENMonth(dateEnd[1]);
	return '<thead>' +
			'<tr>'+
			'<th class="lightBlue" colspan="2" style="width: 200px;">FPS</th>'+
			'<th class="lightBlue" colspan="3" style="width: 301px;">Total Flat Panel TV Quantity</th>'+
			'<th class="lightBlue" colspan="3" style="width: 301px;">Total Amount</th>'+
			'<th class="lightBlue" colspan="4" style="width: 401px;">Average sellout per fps</th>'+
			'</tr>'+
			
			'<tr>'+
			'	<th class="bluegrey">Yr-'+toYearBeg+'</th>'+
			 '<th class="bluegrey">Yr-'+dateBeg[0]+'</th>'+
			 '<th class="bluegrey">Yr-'+toYearBeg+'</th>'+
			'<th class="bluegrey">Yr-'+dateBeg[0]+'</th>'+
			'<th class="bluegrey">Growth</th>'+
			'<th class="bluegrey">Yr-'+toYearBeg+'</th>'+
			'<th class="bluegrey">Yr-'+dateBeg[0]+'</th>'+
			'<th class="bluegrey">Growth</th>'+
			'<th class="bluegrey">'+toYearBeg+' Ave.qty</th>'+
			'<th class="bluegrey">'+dateBeg[0]+' Ave.qty</th>'+
			'<th class="bluegrey">'+toYearBeg+' Ave.amt</th>'+
			'<th class="bluegrey">'+dateBeg[0]+' Ave.amt</th>'+
			'</tr>'+
			
			'</thead>' ;
}






function loadRightDataBySaleGro(obj,beginDate,endDate,whatTime){//加载右边数据
	var toFps=0;
	var laFps=0;
	var toSoQty=0;
	var toSoAmt=0;
	var laSoQty=0;
	var laSoAmt=0;
	var qtyGro=0;
	var amtGro=0;

	var laAvgQty=0;
	var toAvgQty=0;
	var laAvgAmt=0;
	var toAvgAmt=0;
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].laYear_FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].laYear_Qty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_Qty)+ "</td>";
		html += growthRate(isStringNullAvaliableNum(by[i].Qty_Growth).substr(0, isStringNullAvaliableNum(by[i].Qty_Growth).length-1));
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].laYear_Amt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_Amt)+ "</td>";
		html += growthRate(isStringNullAvaliableNum(by[i].Amt_Growth).substr(0, isStringNullAvaliableNum(by[i].Amt_Growth).length-1));
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].laYear_Aveqty_fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_Aveqty_fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].laYear_Aveamt_fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_Aveamt_fps)+ "</td>";
		html += "</tr>";
		toFps+=parseInt(isStringNullAvaliableNumS(by[i].toYear_FPS));
		laFps+=parseInt(isStringNullAvaliableNumS(by[i].laYear_FPS));
		toSoQty+=parseInt(isStringNullAvaliableNumS(by[i].toYear_Qty));
		toSoAmt+=parseInt(isStringNullAvaliableNumS(by[i].toYear_Amt));
		laSoQty+=parseInt(isStringNullAvaliableNumS(by[i].laYear_Qty));
		laSoAmt+=parseInt(isStringNullAvaliableNumS(by[i].laYear_Amt));
		
	}

	if(laSoQty!=0){
		var l=toSoQty-laSoQty;
		qtyGro=Math.round(parseFloat(l)/laSoQty*100);
	}else{
		qtyGro=100;
	}
	if(laSoAmt!=0){
		var l=toSoAmt-laSoAmt;
		amtGro=Math.round(parseFloat(l)/laSoAmt*100);
	}else{
		amtGro=100;
	}
	
	if(toFps!=0){
		toAvgQty=Math.round(toSoQty/toFps);
		toAvgAmt=Math.round(toSoAmt/toFps);
	}
	
	
	if(laFps!=0){
		laAvgQty=Math.round(laSoQty/laFps);
		laAvgAmt=Math.round(laSoAmt/laFps);
	}
	
	
	if(by.length>0){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laFps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toFps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laSoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toSoQty)+ "</td>";
		html += "<td  '>" +isStringNullAvaliableNum(qtyGro)+ "%</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laSoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toSoAmt)+ "</td>";
		html += "<td  >" +isStringNullAvaliableNum(amtGro)+ "%</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laAvgQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toAvgQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laAvgAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toAvgAmt)+ "</td>";
		html += "</tr>";
	}
	
	
	html += '</tbody>';
	
	if(whatTime=="SaleYearTotal"){
		setSaleDataHideOrShow(false,'growthSaleYearRightHead','growthSaleYearRight',loadRightHeadBySaleGroYear(beginDate,endDate),html);
		setScrollBarShift("test_Left48","test_Right48");
	}else if(whatTime=="SaleYearUD"){
		setSaleDataHideOrShow(false,'YearSaleUDRightHead','YearSaleUDRightData',loadRightHeadBySaleGroYear(beginDate,endDate),html);
		setScrollBarShift("test_Left49","test_Right49");
	}else if(whatTime=="SaleYearXCP"){
		setSaleDataHideOrShow(false,'YearSaleXCPRightHead','YearSaleXCPRightData',loadRightHeadBySaleGroYear(beginDate,endDate),html);
		setScrollBarShift("test_Left50","test_Right50");
	}else if(whatTime=="SaleYearSmart"){
		setSaleDataHideOrShow(false,'YearSaleSmartRightHead','YearSaleSmartRightData',loadRightHeadBySaleGroYear(beginDate,endDate),html);
		setScrollBarShift("test_Left51","test_Right51");
	}else if(whatTime=="SaleYearBig"){
		setSaleDataHideOrShow(false,'YearSaleBigRightHead','YearSaleBigRightData',loadRightHeadBySaleGroYear(beginDate,endDate),html);
		setScrollBarShift("test_Left52","test_Right52");
	}else if(whatTime=="SaleYearCurved"){
		setSaleDataHideOrShow(false,'YearSaleCurvedRightHead','YearSaleCurvedRightData',loadRightHeadBySaleGroYear(beginDate,endDate),html);
		setScrollBarShift("test_Left53","test_Right53");
	}
	
	else if(whatTime=="SaleMonthTotal"){
		setSaleDataHideOrShow(false,'growthSaleMonthRightHead','growthSaleMonthRight',loadRightHeadBySaleGro(beginDate),html);
		setScrollBarShift("test_Left1","test_Right1");
	}else if(whatTime=="SaleMonthUD"){
		setSaleDataHideOrShow(false,'monthSaleUDRightHead','monthSaleUDRightData',loadRightHeadBySaleGro(beginDate),html);
		setScrollBarShift("test_Left5","test_Right5");
	}else if(whatTime=="SaleMonthXCP"){
		setSaleDataHideOrShow(false,'monthSaleXCPRightHead','monthSaleXCPRightData',loadRightHeadBySaleGro(beginDate),html);
		setScrollBarShift("test_Left6","test_Right6");
	}else if(whatTime=="SaleMonthSmart"){
		setSaleDataHideOrShow(false,'monthSaleSmartRightHead','monthSaleSmartRightData',loadRightHeadBySaleGro(beginDate),html);
		setScrollBarShift("test_Left7","test_Right7");
	}else if(whatTime=="SaleMonthBig"){
		setSaleDataHideOrShow(false,'monthSaleBigRightHead','monthSaleBigRightData',loadRightHeadBySaleGro(beginDate),html);
		setScrollBarShift("test_Left8","test_Right8");
	}else if(whatTime=="SaleMonthCurved"){
		setSaleDataHideOrShow(false,'monthSaleCurvedRightHead','monthSaleCurvedRightData',loadRightHeadBySaleGro(beginDate),html);
		setScrollBarShift("test_Left9","test_Right9");
	}
	
	else if(whatTime=="SaleWeekTotal"){
		setSaleDataHideOrShow(false,'growthSaleWeekRightHead','growthSaleWeekRight',loadRightHeadBySaleGroWeek(beginDate,endDate),html);
		setScrollBarShift("test_Left2","test_Right2");
	}else if(whatTime=="SaleWeekUD"){
		setSaleDataHideOrShow(false,'WeekSaleUDRightHead','WeekSaleUDRightData',loadRightHeadBySaleGroWeek(beginDate,endDate),html);
		setScrollBarShift("test_Left21","test_Right21");
	}else if(whatTime=="SaleWeekXCP"){
		setSaleDataHideOrShow(false,'WeekSaleXCPRightHead','WeekSaleXCPRightData',loadRightHeadBySaleGroWeek(beginDate,endDate),html);
		setScrollBarShift("test_Left22","test_Right22");
	}else if(whatTime=="SaleWeekSmart"){
		setSaleDataHideOrShow(false,'WeekSaleSmartRightHead','WeekSaleSmartRightData',loadRightHeadBySaleGroWeek(beginDate,endDate),html);
		setScrollBarShift("test_Left23","test_Right23");
	}else if(whatTime=="SaleWeekBig"){
		setSaleDataHideOrShow(false,'WeekSaleBigRightHead','WeekSaleBigRightData',loadRightHeadBySaleGroWeek(beginDate,endDate),html);
		setScrollBarShift("test_Left24","test_Right24");
	}else if(whatTime=="SaleWeekCurved"){
		setSaleDataHideOrShow(false,'WeekSaleCurvedRightHead','WeekSaleCurvedRightData',loadRightHeadBySaleGroWeek(beginDate,endDate),html);
		setScrollBarShift("test_Left25","test_Right25");
	}
}

function setSaleDataHideOrShow(isHide,left,right,leftHtmlData,rightHtmlData){
	if(isHide){
		$("#" + left).parent().hide();
		$("#" + right).parent().hide()
	}else{
		$("#" + left).parent().show();
		$("#" + right).parent().show();
		$('#' + left).html(leftHtmlData);
		$('#' + right).html(rightHtmlData);
	}
};

function loadLeftDataBySaleGro(obj,beginDate,endDate,whatTime){//加载固定列数据
	

	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +isStringNullAvaliable(by[i].RANK)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].performances_SALESMAN)+ "</td>";

		
		html += "</tr>";

		
	}
	if(by.length>0){
		html += "<tr>";
		html += "<th>TOTAL</th>";
		
		html += "</tr>";
	
	}

	html += '</tbody>';
	if(whatTime=="SaleYearTotal"){
		setYearSaleUDLeft('growthSaleYearLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleYearUD"){
		setYearSaleUDLeft('YearSaleUDLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleYearXCP"){
		setYearSaleUDLeft('YearSaleXCPLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleYearSmart"){
		setYearSaleUDLeft('YearSaleSmartLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleYearBig"){
		setYearSaleUDLeft('YearSaleBigLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleYearCurved"){
		setYearSaleUDLeft('YearSaleCurvedLeft',loadLeftHeadBySaleGro() + html);
	}
	
	else if(whatTime=="SaleMonthTotal"){
		setYearSaleUDLeft('growthSaleMonthLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleMonthUD"){
		setYearSaleUDLeft('monthSaleUDLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleMonthXCP"){
		setYearSaleUDLeft('monthSaleXCPLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleMonthSmart"){
		setYearSaleUDLeft('monthSaleSmartLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleMonthBig"){
		setYearSaleUDLeft('monthSaleBigLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleMonthCurved"){
		setYearSaleUDLeft('monthSaleCurvedLeft',loadLeftHeadBySaleGro() + html);
	}
	
	else if(whatTime=="SaleWeekTotal"){
		setYearSaleUDLeft('growthSaleWeekLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleWeekUD"){
		setYearSaleUDLeft('WeekSaleUDLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleWeekXCP"){
		setYearSaleUDLeft('WeekSaleXCPLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleWeekSmart"){
		setYearSaleUDLeft('WeekSaleSmartLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleWeekBig"){
		setYearSaleUDLeft('WeekSaleBigLeft',loadLeftHeadBySaleGro() + html);
	}else if(whatTime=="SaleWeekCurved"){
		setYearSaleUDLeft('WeekSaleCurvedLeft',loadLeftHeadBySaleGro() + html);
	}
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}

function setYearSaleUDLeft(leftId,leftDataHtml){
	$('#' + leftId).parent().show();
	$('#' + leftId).html(leftDataHtml);
};

function loadLeftHeadByCustomerGro(){//获取固定表头

	return '<thead>' +
			'<tr class="bluegrey">'+
				'<th rowspan="2">RANK</th>'+
				'<th rowspan="2">DEALER</th>'+
				'</tr>'+
				'<tr></tr>'+
			'</thead>' ;
};



function loadRightHeadByCustomerGroWeek(beginDate,endDate){
	var dateBeg =beginDate.split("-");
	var dateEnd =endDate.split("-");
	var toYearBeg=parseInt(dateBeg[0])-1;
	var monthBeg=getENMonth(dateBeg[1]);
	
	var toYearEnd=parseInt(dateEnd[0])-1;
	var monthEnd=getENMonth(dateEnd[1]);
	
	
	return '<thead>' +
			'<tr>'+
			'<th class="lightBlue" colspan="2" style="width: 200px;">FPS</th>'+
			'<th class="lightBlue" colspan="2" style="width: 200px;">Quantity</th>'+
			'<th class="lightBlue" colspan="2" style="width: 200px;">Amount</th>'+
			'<th class="lightBlue" colspan="2" style="width: 200px;">GROWTH</th>'+
			'<th class="lightBlue" colspan="4" style="width: 402px;">SELL-OUT EFFICIENSY / FPS</th>'+
			'</tr>'+
			
			'<tr>'+
			'<th class="bluegrey">Yr-'+toYearBeg+'</th>'+
			 '<th class="bluegrey">Yr-'+dateBeg[0]+'</th>'+
			 '<th class="bluegrey" id="setTitle">'+monthBeg+dateBeg[2]+","+toYearBeg+"-"+monthEnd+dateEnd[2]+","+toYearEnd+'</th>'+
			'<th class="bluegrey" id="setTitle">'+monthBeg+dateBeg[2]+","+dateBeg[0]+"-"+monthEnd+dateEnd[2]+","+dateEnd[0]+'</th>'+
			 '<th class="bluegrey" id="setTitle">'+monthBeg+dateBeg[2]+","+toYearBeg+"-"+monthEnd+dateEnd[2]+","+toYearEnd+'</th>'+
			'<th class="bluegrey" id="setTitle">'+monthBeg+dateBeg[2]+","+dateBeg[0]+"-"+monthEnd+dateEnd[2]+","+dateEnd[0]+'</th>'+
			'<th class="bluegrey">Qty</th>'+
			'<th class="bluegrey">Amt</th>'+
			'<th class="bluegrey">Yr-'+toYearBeg+'</th>'+
			 '<th class="bluegrey">Yr-'+dateBeg[0]+'</th>'+
			 '<th class="bluegrey">Yr-'+toYearBeg+'</th>'+
			 '<th class="bluegrey">Yr-'+dateBeg[0]+'</th>'+
			'</tr>'+
			
			'</thead>' ;
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}





function loadRightDataByCustomerGro(obj,beginDate,endDate,whatTime){//加载右边数据
	var toFps=0;
	var laFps=0;
	var toSoQty=0;
	var toSoAmt=0;
	var laSoQty=0;
	var laSoAmt=0;
	var qtyGro=0;
	var amtGro=0;
	var laFPSAVGQty=0;
	var toFPSAVGQty=0;
	var laFPSAVGAmt=0;
	var toFPSAVGAmt=0;
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_QTY)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_QTY)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_AMT)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_AMT)+ "</td>";
		html += growthRate(isStringNullAvaliableNum(by[i].GROWTH_QTY).substr(0, isStringNullAvaliableNum(by[i].GROWTH_QTY).length-1));
		html += growthRate(isStringNullAvaliableNum(by[i].GROWTH_AMOUNT).substr(0, isStringNullAvaliableNum(by[i].GROWTH_AMOUNT).length-1));
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_AVE_FPS_QTY)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_AVE_FPS_QTY)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_AVE_FPS_AMT)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_AVE_FPS_AMT)+ "</td>";

		html += "</tr>";
		toFps+=parseInt(isStringNullAvaliableNumS(by[i].toYear_FPS));
		laFps+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_FPS));
		toSoQty+=parseInt(isStringNullAvaliableNumS(by[i].toYear_QTY));
		toSoAmt+=parseInt(isStringNullAvaliableNumS(by[i].toYear_AMT));
		laSoQty+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_QTY));
		laSoAmt+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_AMT));
		/*laFPSAVGQty+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_AVE_FPS_QTY));
		toFPSAVGQty+=parseInt(isStringNullAvaliableNumS(by[i].toYear_AVE_FPS_QTY));
		laFPSAVGAmt+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_AVE_FPS_AMT));
		toFPSAVGAmt+=parseInt(isStringNullAvaliableNumS(by[i].toYear_AVE_FPS_AMT));*/
	}

	if(laSoQty!=0){
		var l=toSoQty-laSoQty;
		qtyGro=Math.round(parseFloat(l)/laSoQty*100);
	}else{
		qtyGro=100;
	}
	if(laSoAmt!=0){
		var l=toSoAmt-laSoAmt;
		amtGro=Math.round(parseFloat(l)/laSoAmt*100);
	}else{
		amtGro=100;
	}
	
	if(laFps!=0){
		laFPSAVGQty=Math.round(laSoQty/laFps);
		laFPSAVGAmt=Math.round(laSoAmt/laFps);
	}
	
	if(toFps!=0){
		toFPSAVGQty=Math.round(toSoQty/toFps);
		toFPSAVGAmt=Math.round(toSoAmt/toFps);	
	}
	if(by.length>0){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laFps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toFps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laSoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toSoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laSoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toSoAmt)+ "</td>";
		html += "<td  '>" +isStringNullAvaliableNum(qtyGro)+ "%</td>";
		html += "<td  >" +isStringNullAvaliableNum(amtGro)+ "%</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laFPSAVGQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toFPSAVGQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laFPSAVGAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toFPSAVGAmt)+ "</td>";
	
		html += "</tr>";
	}
	
	
	html += '</tbody>';
	
		$('#growthCustomerWeekRightHead').html(loadRightHeadByCustomerGroWeek(beginDate,endDate));
		$('#growthCustomerWeekRight').html( html);
		setScrollBarShift("test_Left4","test_Right4");
		
	
}

function loadRightDataByCustomerGros(obj,beginDate,endDate,tab){//加载右边数据
	var toFps=0;
	var laFps=0;
	var toSoQty=0;
	var toSoAmt=0;
	var laSoQty=0;
	var laSoAmt=0;
	var qtyGro=0;
	var amtGro=0;
	var laFPSAVGQty=0;
	var toFPSAVGQty=0;
	var laFPSAVGAmt=0;
	var toFPSAVGAmt=0;
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_QTY)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_QTY)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_AMT)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_AMT)+ "</td>";
		html += growthRate(isStringNullAvaliableNum(by[i].GROWTH_QTY).substr(0, isStringNullAvaliableNum(by[i].GROWTH_QTY).length-1));
		html += growthRate(isStringNullAvaliableNum(by[i].GROWTH_AMOUNT).substr(0, isStringNullAvaliableNum(by[i].GROWTH_AMOUNT).length-1));
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_AVE_FPS_QTY)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_AVE_FPS_QTY)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].lastYear_AVE_FPS_AMT)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].toYear_AVE_FPS_AMT)+ "</td>";

		html += "</tr>";
		toFps+=parseInt(isStringNullAvaliableNumS(by[i].toYear_FPS));
		laFps+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_FPS));
		toSoQty+=parseInt(isStringNullAvaliableNumS(by[i].toYear_QTY));
		toSoAmt+=parseInt(isStringNullAvaliableNumS(by[i].toYear_AMT));
		laSoQty+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_QTY));
		laSoAmt+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_AMT));
		/*laFPSAVGQty+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_AVE_FPS_QTY));
		toFPSAVGQty+=parseInt(isStringNullAvaliableNumS(by[i].toYear_AVE_FPS_QTY));
		laFPSAVGAmt+=parseInt(isStringNullAvaliableNumS(by[i].lastYear_AVE_FPS_AMT));
		toFPSAVGAmt+=parseInt(isStringNullAvaliableNumS(by[i].toYear_AVE_FPS_AMT));*/
	}

	if(laSoQty!=0){
		var l=toSoQty-laSoQty;
		qtyGro=Math.round(parseFloat(l)/laSoQty*100);
	}else{
		qtyGro=100;
	}
	if(laSoAmt!=0){
		var l=toSoAmt-laSoAmt;
		amtGro=Math.round(parseFloat(l)/laSoAmt*100);
	}else{
		amtGro=100;
	}
	
	if(laFps!=0){
		laFPSAVGQty=Math.round(laSoQty/laFps);
		laFPSAVGAmt=Math.round(laSoAmt/laFps);
	}
	
	if(toFps!=0){
		toFPSAVGQty=Math.round(toSoQty/toFps);
		toFPSAVGAmt=Math.round(toSoAmt/toFps);	
	}
	if(by.length>0){
		html += "<tr>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laFps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toFps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laSoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toSoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laSoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toSoAmt)+ "</td>";
		html += "<td  '>" +isStringNullAvaliableNum(qtyGro)+ "%</td>";
		html += "<td  >" +isStringNullAvaliableNum(amtGro)+ "%</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laFPSAVGQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toFPSAVGQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(laFPSAVGAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(toFPSAVGAmt)+ "</td>";
	
		html += "</tr>";
	}
	
	html += '</tbody>';
	
	if(tab == 'X/C/P'){
		$('#growthCustomerWeekRightHead_XCP' ).html(loadRightHeadByCustomerGroWeek(beginDate,endDate));
		$('#growthCustomerWeekRight_XCP' ).html( html);
		setScrollBarShift("test_Left4_XCP" ,"test_Right4_XCP" );
		widthlala('test_Left4_XCP','test_Right4_XCP');
	}else{
		$('#growthCustomerWeekRightHead_' + tab).html(loadRightHeadByCustomerGroWeek(beginDate,endDate));
		$('#growthCustomerWeekRight_' + tab).html( html);
		widthlala('test_Left4_' + tab,'test_Right4_' + tab);
		setScrollBarShift('test_Left4_' + tab,'test_Right4_' + tab);
	}
}

function loadLeftDataByCustomerGro(obj,beginDate,endDate,whatTime){//加载固定列数据
	

	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +isStringNullAvaliable(by[i].RANK)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].DEALER)+ "</td>";

		
		html += "</tr>";

		
	}
	if(by.length>0){
		html += "<tr>";
		html += "<th></th>";
		html += "<th>TOTAL</th>";
		html += "<th></th>";
		
		html += "</tr>";
	
	}

	html += '</tbody>';
	
	$('#growthCustomerWeekLeft').html(loadLeftHeadByCustomerGro() + html);
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}

function loadLeftDataByCustomerGros(obj,beginDate,endDate,tab){//加载固定列数据
	
	
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +isStringNullAvaliable(by[i].RANK)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].DEALER)+ "</td>";
		
		
		html += "</tr>";
		
		
	}
	if(by.length>0){
		html += "<tr>";
		html += "<th>TOTAL</th>";
		html += "</tr>";
		
	}
	
	html += '</tbody>';
	
	if(tab == 'X/C/P'){
		$('#growthCustomerWeekLeft_XCP').html(loadLeftHeadByCustomerGro() + html);
	}else{
		$('#growthCustomerWeekLeft_' +tab).html(loadLeftHeadByCustomerGro() + html);
	}
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}
function loadLeftDataByCustomerGro(obj,beginDate,endDate,whatTime){//加载固定列数据
	
	
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +isStringNullAvaliable(by[i].RANK)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].DEALER)+ "</td>";
		
		
		html += "</tr>";
		
		
	}
	if(by.length>0){
		html += "<tr>";
		html += "<th>TOTAL</th>";
		
		html += "</tr>";
		
	}
	
	html += '</tbody>';
	
	$('#growthCustomerWeekLeft').html(loadLeftHeadByCustomerGro() + html);
	
	$("[id='setTitle']").mouseenter(function(){
		$(this).attr('title',$(this).html());
	});
}






//年度业务员达成

function loadSaleDataByYear(obj){//加载右边数据
	
	
	/*'<th>RANK</th>'+
	'<th>Salesman</th>'+
	'<th>Fps count</th>'+
	'<th>TTL SO QTY</th>'+
	'<th>TTL SO amt</th>'+
	'<th>BASIC TARGET</th>'+
	'<th>Achievement</th>'+*/
	var Shop=0;
	var Fps=0;
	var SoQty=0;
	var SoAmt=0;
	var TargetAmt=0;
	var Ach=0;
	var Ave_FPS_Qty=0;
	var Ave_FPS_Amount=0;
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td  >" +isStringNullAvaliableNum(by[i].RANK)+ "</td>";
		html += "<td  >" +isStringNullAvaliable(by[i].Saleman)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SO_Qty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SO_Amt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Target)+ "</td>";
		html += reachTheBackgroundColor(isStringNullAvaliableNum(by[i].Ach));
		html += "</tr>";
		//Shop+=parseInt(isStringNullAvaliableNumS(by[i].Shop));
		Fps+=parseInt(isStringNullAvaliableNumS(by[i].FPS));
		SoQty+=parseInt(isStringNullAvaliableNumS(by[i].SO_Qty));
		SoAmt+=parseInt(isStringNullAvaliableNumS(by[i].SO_Amt));
		TargetAmt+=parseInt(isStringNullAvaliableNumS(by[i].Target));
	}

	if(TargetAmt!=0){
		Ach=Math.round(SoAmt/TargetAmt*100);
	}else{
		Ach=100;
	}
	/*if(Fps!=0){
		Ave_FPS_Qty=Math.round(SoQty/Fps);
		Ave_FPS_Amount=Math.round(SoAmt/Fps);
	}*/
	if(by.length>0){
		html += "<tr><td></td><td>Total</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(TargetAmt)+ "</td>";
		html += "<td  '>" +isStringNullAvaliableNum(Ach)+ "%</td>";
		html += "</tr>";
		
	}
	
	html += '</tbody>';
	$('#saleByYear').html(loadHeadBySaleYear() + html);
	tabs_1_2_1();
	
}



function loadHeadBySaleYear(){//获取固定表头

	return '<thead>' +
			'<tr class="bluegrey">'+
				'<th>RANK</th>'+
				'<th>Salesman</th>'+
				'<th>Fps count</th>'+
				'<th>TTL SO QTY</th>'+
				'<th>TTL SO amt</th>'+
				'<th>BASIC TARGET</th>'+
				'<th>Achievement</th>'+
				'</tr>'+
			'</thead>' ;
};






//年度区域经理达成

function loadRegDataByYear(obj){//加载右边数据
	
	
	/*'<th>RANK</th>'+
	'<th>Salesman</th>'+
	'<th>Fps count</th>'+
	'<th>TTL SO QTY</th>'+
	'<th>TTL SO amt</th>'+
	'<th>BASIC TARGET</th>'+
	'<th>Achievement</th>'+*/
	var Shop=0;
	var Fps=0;
	var SoQty=0;
	var SoAmt=0;
	var TargetAmt=0;
	var Ach=0;
	var Ave_FPS_Qty=0;
	var Ave_FPS_Amount=0;
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td  >" +isStringNullAvaliableNum(by[i].Rank)+ "</td>";
		html += "<td  >" +isStringNullAvaliable(by[i].RegionalHead)+ "</td>";
		html += "<td  >" +isStringNullAvaliable(by[i].AREA)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].Fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].TargetAmt)+ "</td>";
		html += reachTheBackgroundColor(isStringNullAvaliableNum(by[i].Ach));
		html += "</tr>";
		//Shop+=parseInt(isStringNullAvaliableNumS(by[i].Shop));
		Fps+=parseInt(isStringNullAvaliableNumS(by[i].Fps));
		SoQty+=parseInt(isStringNullAvaliableNumS(by[i].SoQty));
		SoAmt+=parseInt(isStringNullAvaliableNumS(by[i].SoAmt));
		TargetAmt+=parseInt(isStringNullAvaliableNumS(by[i].TargetAmt));
	}

	if(TargetAmt!=0){
		Ach=Math.round(SoAmt/TargetAmt*100);
	}else{
		Ach=100;
	}
	/*if(Fps!=0){
		Ave_FPS_Qty=Math.round(SoQty/Fps);
		Ave_FPS_Amount=Math.round(SoAmt/Fps);
	}*/
	if(by.length>0){
		html += "<tr><td></td><td>Total</td><td></td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(TargetAmt)+ "</td>";
		html += "<td  '>" +isStringNullAvaliableNum(Ach)+ "%</td>";
		html += "</tr>";
		
	}
	
	html += '</tbody>';
	$('#regByYear').html(loadHeadByRegYear() + html);
	
	
}



function loadHeadByRegYear(){//获取固定表头

	return '<thead>' +
			'<tr class="bluegrey">'+
				'<th>RANK</th>'+
				'<th>Regional Head</th>'+
				'<th>Region</th>'+
				'<th>Fps count</th>'+
				'<th>TTL SO QTY</th>'+
				'<th>TTL SO amt</th>'+
				'<th>BASIC TARGET</th>'+
				'<th>Achievement</th>'+
				'</tr>'+
			'</thead>' ;
};




//年度督导达成

function loadAcfoDataByYear(obj){//加载右边数据
	
	
	/*'<th>RANK</th>'+
	'<th>Salesman</th>'+
	'<th>Fps count</th>'+
	'<th>TTL SO QTY</th>'+
	'<th>TTL SO amt</th>'+
	'<th>BASIC TARGET</th>'+
	'<th>Achievement</th>'+*/
	var Shop=0;
	var Fps=0;
	var SoQty=0;
	var SoAmt=0;
	var TargetAmt=0;
	var Ach=0;
	var Ave_FPS_Qty=0;
	var Ave_FPS_Amount=0;
	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td  >" +isStringNullAvaliableNum(by[i].Rank)+ "</td>";
		html += "<td  >" +isStringNullAvaliable(by[i].Acfo)+ "</td>";
		html += "<td  >" +isStringNullAvaliable(by[i].Area)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].FPS)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(by[i].TargetAmt)+ "</td>";
		html += reachTheBackgroundColor(isStringNullAvaliableNum(by[i].Ach));
		html += "</tr>";
		//Shop+=parseInt(isStringNullAvaliableNumS(by[i].Shop));
		Fps+=parseInt(isStringNullAvaliableNumS(by[i].FPS));
		SoQty+=parseInt(isStringNullAvaliableNumS(by[i].SoQty));
		SoAmt+=parseInt(isStringNullAvaliableNumS(by[i].SoAmt));
		TargetAmt+=parseInt(isStringNullAvaliableNumS(by[i].TargetAmt));
	}

	if(TargetAmt!=0){
		Ach=Math.round(SoAmt/TargetAmt*100);
	}else{
		Ach=100;
	}
	/*if(Fps!=0){
		Ave_FPS_Qty=Math.round(SoQty/Fps);
		Ave_FPS_Amount=Math.round(SoAmt/Fps);
	}*/
	if(by.length>0){
		html += "<tr><td></td><td>Total</td><td></td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(Fps)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoQty)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(SoAmt)+ "</td>";
		html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(TargetAmt)+ "</td>";
		html += "<td  '>" +isStringNullAvaliableNum(Ach)+ "%</td>";
		html += "</tr>";
		
	}
	
	html += '</tbody>';
	$('#AcfoByYear').html(loadHeadByAcfoYear() + html);
	tabs_1_3_1();
	
}




function loadHeadByAcfoYear(){//获取固定表头

	return '<thead>' +
			'<tr class="bluegrey">'+
				'<th>RANK</th>'+
				'<th>Acfo</th>'+
				'<th>Area</th>'+
				'<th>Fps count</th>'+
				'<th>TTL SO QTY</th>'+
				'<th>TTL SO amt</th>'+
				'<th>BASIC TARGET</th>'+
				'<th>Achievement</th>'+
				'</tr>'+
			'</thead>' ;
};


















function setInitArr(length){
	var arr = [];
	for(var i =0;i<length;i++){
		arr[i] = 0;
	}
	return arr;
};


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
	
	if(by.length == 0){//数据为空时,隐藏table,使页面样式整洁。
		$('#yearXCPRightHead').parent().hide();
		$('#yearXCPRight').parent().hide();
	}else{
		$('#yearXCPRightHead').parent().show();
		$('#yearXCPRight').parent().show();
		$('#yearXCPRightHead').html(loadRightHeadByXCPYear(obj,whatHead));
		$('#yearXCPRight').html(html);
		setScrollBarShift("test_Left4","test_Right4");
		setWidthBarShift("test_Left4","test_Right4");
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
	
	if(by.length == 0){//数据为空时,隐藏table,使页面样式整洁。
		$('#yearXCPLeft').parent().hide();
	}else{
		$('#yearXCPLeft').parent().show();
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
	  var mon=(by != null && by != "")?by[0].arr:[];
	  
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
















//年度增长

function loadRightDataByCountryGro(obj,beginDate,endDate,whatTime){//加载右边数据

	var da=beginDate.split("-")[0];
	var beg=parseInt(da)-1;
	var end=endDate.split("-")[0];
	//第一行tr去年
	//第2行tr今年
	//第三行tr增长
	var html = "<tbody>";
	var by = obj.data;
	
	if(by.length>1){
		for(var i =0;i<by.length;i++){
			var arrQty=by[i].arrQty;
			var arrAmt=by[i].arrAmt;
			var qtyTotal=0;
			var amtTotal=0;
			html += "<tr>";
				for ( var int = 0; int < arrQty.length; int++) {
					html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
					qtyTotal+=accumulativeJudgment(arrQty[int]);
					for ( var x = 0; x < arrAmt.length; x++) {
						if(x==int){
							html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
							amtTotal+=accumulativeJudgment(arrAmt[x]);
						}
						
					}
				}
			
			
		
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
		}
		
	}else if(by.length==1){
		var arrQty=by[0].arrQty;
		var arrAmt=by[0].arrAmt;
		var qtyTotal=0;
		var amtTotal=0;
		if(by[0].year==beg){
			html += "<tr>";
			for ( var int = 0; int < arrQty.length; int++) {
				html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
				qtyTotal+=accumulativeJudgment(arrQty[int]);
				for ( var x = 0; x < arrAmt.length; x++) {
					if(x==int){
						html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
						amtTotal+=accumulativeJudgment(arrAmt[x]);
					}
					
				}
			}
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
			
			html += "<tr>";
			for ( var x = 0; x < 26; x++) {
				html += "<td  style='text-align:right;'>0</td>";
			}
			
			
			html += "</tr>";
		}else{
			html += "<tr>";
			for ( var x = 0; x <26; x++) {
				html += "<td  style='text-align:right;'>0</td>";
			}
		
			html += "</tr>";
			html += "<tr>";
			for ( var int = 0; int < arrQty.length; int++) {
				html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrQty[int])+ "</td>";	
				qtyTotal+=accumulativeJudgment(arrQty[int]);
				for ( var x = 0; x < arrAmt.length; x++) {
					if(x==int){
						html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(arrAmt[x])+ "</td>";	
						amtTotal+=accumulativeJudgment(arrAmt[x]);
					}
					
				}
			}
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(qtyTotal)+ "</td>";
			html += "<td  style='text-align:right;'>" +isStringNullAvaliableNum(amtTotal)+ "</td>";
			html += "</tr>";
		}
	}
	 
	
	
	

	
	
	if(by.length>1){
		html += "<tr>";
		var arrQty1=by[0].arrQty;
		var arrQty2=by[1].arrQty;
		var arrAmt1=by[0].arrAmt;
		var arrAmt2=by[1].arrAmt;
		
		var toQtyTTL=0;
		var laQtyTTL=0;
		var toAmtTTL=0;
		var laQmtTTL=0;
		for ( var a = 0; a < 12; a++) {
			var layearQty=parseInt(isStringNullAvaliableNumS(arrQty1[a]));
			var toyearQty=parseInt(isStringNullAvaliableNumS(arrQty2[a]));
			var layearAmt=parseInt(isStringNullAvaliableNumS(arrAmt1[a]));
			var toyearAmt=parseInt(isStringNullAvaliableNumS(arrAmt2[a]));
			toQtyTTL+=toyearQty;
			laQtyTTL+=layearQty;
			toAmtTTL+=toyearAmt;
			laQmtTTL+=layearAmt;
			if(layearQty!=0){
				var ach=Math.round((toyearQty-layearQty)/layearQty*100);
				html +=growthRate(ach);
				
			}else{
				html += growthRate(100);
			}
			if(layearAmt!=0){
				var ach=Math.round((toyearAmt-layearAmt)/layearAmt*100);
				html += growthRate(ach);
				
			}else{
				html +=growthRate(100);
			}
			}
		
		if(laQtyTTL!=0){
			var ach=Math.round((toQtyTTL-laQtyTTL)/laQtyTTL*100);
			html +=growthRate(ach);
			
		}else{
			html +=growthRate(100);
		}
		if(laQmtTTL!=0){
			var ach=Math.round((toAmtTTL-laQmtTTL)/laQmtTTL*100);
			html += growthRate(ach);
			
		}else{
			html += growthRate(100);
		}
		

		html += "</tr>";
	}else if(by.length==1){
		html += "<tr>";
		if(by[0].year==beg){
			for ( var x = 0; x < 26; x++) {
				html += growthRate(-100);
			}
			
		}else{
			for ( var x = 0; x < 26; x++) {
				html += growthRate(100);
			}
		}
		html += "</tr>";
	}
	
	html += '</tbody>';
	
	if(whatTime=="Total"){
		$('#growthByCountryLeft').html("");
		$('#growthByCountryRight').html("");
		if(by.length>0){
			$('#growthByCountryLeft').html( loadLeftHeadByCountryGro(beginDate));
			$('#growthByCountryRight').html( loadCountryGroYear(obj)+html);
		}
		
		
	}else if(whatTime=="UD"){
		$('#UDgrowthByCountryLeft').html("");
		$('#UDgrowthByCountryRight').html("");
		if(by.length>0){
			$('#UDgrowthByCountryLeft').html( loadLeftHeadByCountryGro(beginDate));
			$('#UDgrowthByCountryRight').html( loadCountryGroYear(obj)+html);
		}
		
	}else if(whatTime=="XCP"){
		$('#XCPgrowthByCountryLeft').html("");
		$('#XCPgrowthByCountryRight').html("");
		if(by.length>0){
			$('#XCPgrowthByCountryLeft').html( loadLeftHeadByCountryGro(beginDate));
			$('#XCPgrowthByCountryRight').html( loadCountryGroYear(obj)+html);

		}
	}else if(whatTime=="Smart"){
		$('#SmartgrowthByCountryLeft').html("");
		$('#SmartgrowthByCountryRight').html("");
		if(by.length>0){
			$('#SmartgrowthByCountryLeft').html( loadLeftHeadByCountryGro(beginDate));
			$('#SmartgrowthByCountryRight').html( loadCountryGroYear(obj)+html);

		}
	}else if(whatTime=="Big"){
		$('#BiggrowthByCountryLeft').html("");
		$('#BiggrowthByCountryRight').html("");
		
		if(by.length>0){
			$('#BiggrowthByCountryLeft').html( loadLeftHeadByCountryGro(beginDate));
			$('#BiggrowthByCountryRight').html( loadCountryGroYear(obj)+html);

		}
	}else if(whatTime=="Curved"){
		$('#CurvedgrowthByCountryLeft').html("");
		$('#CurvedgrowthByCountryRight').html("");
		
		if(by.length>0){
			$('#CurvedgrowthByCountryLeft').html( loadLeftHeadByCountryGro(beginDate));
			$('#CurvedgrowthByCountryRight').html( loadCountryGroYear(obj)+html);

		}
	}
	
	

	
}







function loadLeftHeadByCountryGro(beginDate){//获取固定表头
	var date =beginDate.split("-");
	var toYear=parseInt(date[0])-1;
	
	return '<thead>' +
				'<tr><th style="height: 42px;"></th></tr>'+
				'<tr><th>'+toYear+'</th></tr>'+
				'<tr><th>'+date[0]+'</th></tr>'+
				'<tr><th>Growth rate	</th></tr>'+
			'</thead>' ;
};


//右边表头

function loadCountryGroYear(obj){
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
				html+='<th colspan="2">'+month[int]+'</th>';
			  }
			html+='<th colspan="2">Total</th>';
			html+=	'</tr>';
			
			html+=		'<tr>';
			for ( var int = 0; int < mon.length; int++) {
				html+='<th>QTY</th>';
				html+='<th>AMOUNT</th>';
			  }
			html+='<th>QTY</th>';
			html+='<th>AMOUNT</th>';
			html+=	'</tr>';
			
			
			html+='</thead>' ;
			return html;
}








