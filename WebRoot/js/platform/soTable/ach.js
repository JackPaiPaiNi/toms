
$(document).ready(function() {
	getXCPModel();
	getCurvedModel();
	//getGroCountry();
	//selectPartyCus();
	//getYearByReg();
	
	$('#channel_tab_wher li a').click(function(){
		if($(this).html() == 'Total'){
			getWeekGrowthByCustomer();
			selectPartyCus();
		}else{
			selectPartysCus($(this).html());
			getWeekGrowthByCustomers($(this).html());
		}
	});
});


function XCPByYear(beginDate,endDate){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	
	
	var whatHead;
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.what="yearXCP";
	o.check=$("#countryXCPCheck").is(':checked');
	var subXcp=$("#subXcp").val();
	var xcp=$("#xcp").val();
	if(subXcp!=null && subXcp!=""){
		o.line=subXcp;
		whatHead=subXcp+" series TTL SELLOUT/MONTHLY";
	}else{
		if(xcp!=null && xcp!=""){
			whatHead=xcp+" series TTL SELLOUT/MONTHLY";
			o.line=xcp;
		}else{
			whatHead="X/C/P series TTL SELLOUT/MONTHLY";
			o.line="";

		}
	}
	
	$("#yearXCPText").text(whatHead+"  series monthly sellout trend "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+endMonth[0]);
	
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadXCPYearData(data,beginDate,endDate,whatHead);
		   
		}
	});
	
}




function SaleByYear(beginDate,endDate){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	$("#saleByYearText").text("Cumulative sell-out  as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.check=$("#saleByYearCheck").is(':checked');
	o.what="yearSale";
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadSaleYearData(data);
		   
		}
	});
	
}



function CountryByGrowth(beginDate,endDate,whatTime){
	$("#loadingImportOne").show();
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	if(whatTime=="Total"){
		o.what="countryGrowth";
		o.check=$("#GroCountryCheck").is(':checked');
	}else if(whatTime=="UD"){
		o.what="monthCountryGrowthUD";
		o.CountryUDModelByGroMonth=$("#CountryUDModelByYear").val();
		o.check=$("#YearCountryUDCheck").is(':checked');
		//$("#YearCountryUDText").text("UD Growth performances   for the Year of   "+getENYear(endYear[1])+" "+endYear[0]);
	}else if(whatTime=="XCP"){
		o.what="monthCountryGrowthXCP";
		o.check=$("#YearCountryXCPCheck").is(':checked');
		var subXcp=$("#subXcpCountryYear").val();
		var xcp=$("#XcpCountryYear").val();
		var whatHead;
		if(subXcp!=null && subXcp!=""){
			o.line=subXcp;
			whatHead=subXcp;
		}else{
			if(xcp!=null && xcp!=""){
				whatHead=xcp;
				o.line=xcp;
			}else{
				whatHead="X/C/P ";
				o.line="";

			}
		}
		
		$("#YearCountryXCPText").text(whatHead+"  series Growth performances ");
		
	}else if(whatTime=="Smart"){
		o.what="monthCountryGrowthSmart";
		o.check=$("#YearCountrySmartCheck").is(':checked');
		//$("#YearCountrySmartText").text("Smart Growth performances  for the Year  "+getENYear(endYear[1])+" "+endYear[0]);
	}else if(whatTime=="Big"){
		o.what="monthCountryGrowthBig";
		//o.line="AND  pt.`size`>"+$("#CountryBigSizeByYear").val();
		o.check=$("#YearCountryBigCheck").is(':checked');
		//$("#YearCountryBigText").text("Big Growth performances  for the Year   "+getENYear(endYear[1])+" "+endYear[0]);
	}else if(whatTime=="Curved"){
		o.what="monthCountryGrowthCurved";
		o.CurvedModelCountryGroMonth=$("#CurvedModelCountryYear").val();
		o.check=$("#YearCountryCurvedCheck").is(':checked');
		//$("#YearCountryCurvedText").text("Curved Growth performances   for the Year  "+getENYear(endYear[1])+" "+endYear[0]);
	}
	
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImportOne").hide();
			loadCountryGroData(data,beginDate,endDate,whatTime);
		   
		}
	});
	
}




function RegByMonth(beginDate,endDate,whatTime){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.what="monthReg";
	if(whatTime=="RegYear"){
		o.check=$("#regByYearCheck").is(':checked');
	}else if(whatTime=="RegMonth"){
		o.check=$("#regByMonthCheck").is(':checked');
	}else{
		o.check=$("#regByWeekCheck").is(':checked');
	}
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			if(whatTime=="RegYear"){
				loadRegYearData(data);
				$("#regByYearText").text("Cumulative sell-out  as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
			}else{
				loadREgMonthData(data,whatTime);
				if(whatTime=="RegMonth"){
					$("#monthRegText").text("TOTAL Sellout performances of Regional Head for the month of "+getENMonth(endMonth[1])+" "+endMonth[0]);
				}else{
					$("#·weekRegText").text("Regional Head sellout updates as of  "+getENMonth(begMonth[1])+" "+begMonth[2]+","+begMonth[0]+" - "+getENMonth(endMonth[1])+" "+endMonth[2]+","+endMonth[0]);
				}

			}
		   
		}
	});
	
}


function  SaleByMonthGrowth(beginDate,endDate,whatTime){
	$("#loadingImportOne").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	if(whatTime=="SaleYearTotal"){
		o.check=$("#YearGrowthBySaleCheck").is(':checked');
		o.what="monthSaleGrowthTotal";
	}else if(whatTime=="SaleYearUD"){
		o.what="monthSaleGrowthUD";
		o.SaleUDModelByGroMonth=$("#SaleUDModelByYear").val();
		o.check=$("#YearSaleUDCheck").is(':checked');
		//$("#YearSaleUDText").text("UD Growth performances   for the Year of   "+getENYear(endYear[1])+" "+endYear[0]);
	}else if(whatTime=="SaleYearXCP"){
		o.what="monthSaleGrowthXCP";
		o.check=$("#YearSaleXCPCheck").is(':checked');
		var subXcp=$("#subXcpSaleYear").val();
		var xcp=$("#XcpSaleYear").val();
		var whatHead;
		if(subXcp!=null && subXcp!=""){
			o.line=subXcp;
			whatHead=subXcp;
		}else{
			if(xcp!=null && xcp!=""){
				whatHead=xcp;
				o.line=xcp;
			}else{
				whatHead="X/C/P ";
				o.line="";

			}
		}
		$("#YearSaleXCPText").text(whatHead+"  series Growth performances in every Salesman/Account");
		
	}else if(whatTime=="SaleYearSmart"){
		o.what="monthSaleGrowthSmart";
		o.check=$("#YearSaleSmartCheck").is(':checked');
		//$("#YearSaleSmartText").text("Smart Growth performances  for the Year  "+getENYear(endYear[1])+" "+endYear[0]);
	}else if(whatTime=="SaleYearBig"){
		o.what="monthSaleGrowthBig";
		//o.line="AND  pr.`size`>"+$("#SaleBigSizeByYear").val();
		o.check=$("#YearSaleBigCheck").is(':checked');
		//$("#YearSaleBigText").text("Big Growth performances  for the Year   "+getENYear(endYear[1])+" "+endYear[0]);
	}else if(whatTime=="SaleYearCurved"){
		o.what="monthSaleGrowthCurved";
		o.CurvedModelSaleGroMonth=$("#CurvedModelSaleYear").val();
		o.check=$("#YearSaleCurvedCheck").is(':checked');
		//$("#YearSaleCurvedText").text("Curved Growth performances   for the Year  "+getENYear(endYear[1])+" "+endYear[0]);
	}else if(whatTime=="SaleMonthTotal"){
		o.check=$("#MonthGrowthBySaleCheck").is(':checked');
		o.what="monthSaleGrowthTotal";
	}else if(whatTime=="SaleMonthUD"){
		o.what="monthSaleGrowthUD";
		o.SaleUDModelByGroMonth=$("#SaleUDModelByMonth").val();
		o.check=$("#monthSaleUDCheck").is(':checked');
		//$("#monthSaleUDText").text("UD Growth performances   for the month of   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(whatTime=="SaleMonthXCP"){
		o.what="monthSaleGrowthXCP";
		o.check=$("#monthSaleXCPCheck").is(':checked');
		var subXcp=$("#subXcpSaleMonth").val();
		var xcp=$("#XcpSaleMonth").val();
		var whatHead;
		if(subXcp!=null && subXcp!=""){
			o.line=subXcp;
			whatHead=subXcp;
		}else{
			if(xcp!=null && xcp!=""){
				whatHead=xcp;
				o.line=xcp;
			}else{
				whatHead="X/C/P  ";
				o.line="";

			}
		}
		
		$("#monthSaleXCPText").text(whatHead+"  series Growth performances in every Salesman/Account");
		
	}else if(whatTime=="SaleMonthSmart"){
		o.what="monthSaleGrowthSmart";
		o.check=$("#monthSaleSmartCheck").is(':checked');
		//$("#monthSaleSmartText").text("Smart Growth performances  for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(whatTime=="SaleMonthBig"){
		o.what="monthSaleGrowthBig";
		//o.line="AND  pr.`size`>"+$("#SaleBigSizeByMonth").val();
		o.check=$("#monthSaleBigCheck").is(':checked');
		//$("#monthSaleBigText").text("Big Growth performances  for the month   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(whatTime=="SaleMonthCurved"){
		o.what="monthSaleGrowthCurved";
		o.CurvedModelSaleGroMonth=$("#CurvedModelSaleMonth").val();
		o.check=$("#monthSaleCurvedCheck").is(':checked');
		//$("#monthSaleCurvedText").text("Curved Growth performances   for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}
	
	
	
	else if(whatTime=="SaleWeekTotal"){
		o.check=$("#GrowthBySaleWeekCheck").is(':checked');
		o.what="monthSaleGrowthTotal";
	}else if(whatTime=="SaleWeekUD"){
		o.what="monthSaleGrowthUD";
		o.SaleUDModelByGroMonth=$("#SaleUDModelByWeek").val();
		o.check=$("#WeekSaleUDCheck").is(':checked');
		//$("#WeekSaleUDText").text("UD Growth performances   for the Week of   "+getENWeek(endWeek[1])+" "+endWeek[0]);
	}else if(whatTime=="SaleWeekXCP"){
		o.what="monthSaleGrowthXCP";
		o.check=$("#WeekSaleXCPCheck").is(':checked');
		var subXcp=$("#subXcpSaleWeek").val();
		var xcp=$("#XcpSaleWeek").val();
		var whatHead;
		if(subXcp!=null && subXcp!=""){
			o.line=subXcp;
			whatHead=subXcp;
		}else{
			if(xcp!=null && xcp!=""){
				whatHead=xcp;
				o.line=xcp;
			}else{
				whatHead="X/C/P ";
				o.line="";

			}
		}
		
		$("#WeekSaleXCPText").text(whatHead+"  series Growth performances in every Salesman/Account");
		
	}else if(whatTime=="SaleWeekSmart"){
		o.what="monthSaleGrowthSmart";
		o.check=$("#WeekSaleSmartCheck").is(':checked');
		//$("#WeekSaleSmartText").text("Smart Growth performances  for the Week  "+getENWeek(endWeek[1])+" "+endWeek[0]);
	}else if(whatTime=="SaleWeekBig"){
		o.what="monthSaleGrowthBig";
		//o.line="AND  pr.`size`>"+$("#SaleBigSizeByWeek").val();
		o.check=$("#WeekSaleBigCheck").is(':checked');
		//$("#WeekSaleBigText").text("Big Growth performances  for the Week   "+getENWeek(endWeek[1])+" "+endWeek[0]);
	}else if(whatTime=="SaleWeekCurved"){
		o.what="monthSaleGrowthCurved";
		o.CurvedModelSaleGroMonth=$("#CurvedModelSaleWeek").val();
		o.check=$("#WeekSaleCurvedCheck").is(':checked');
		//$("#WeekSaleCurvedText").text("Curved Growth performances   for the Week  "+getENWeek(endWeek[1])+" "+endWeek[0]);
	}
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImportOne").hide();
			loadSaleMonthGrowth(data,beginDate,endDate,whatTime);
			
		   
		}
	});
	
}

function  CustomerByWeekGrowth(beginDate,endDate,whatTime){
	$("#loadingImportOne").show();
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.party=$("#DealerName").val();
	o.what="WeekCustomerGrowth";
	o.check=$("#GrowthByCustomerCheck").is(':checked');
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImportOne").hide();
			loadCustomerWeekGrowth(data,beginDate,endDate,whatTime);
			
		}
	});
	
}




function AcfoByMonth(beginDate,endDate,whatTime){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.what="monthAcfo";
	if(whatTime=="AcfoYear"){
		o.check=$("#acfoByYearCheck").is(':checked');
	}else if(whatTime=="AcfoWeek"){
		o.check=$("#acfoByWeekCheck").is(':checked');
	}else{
		o.check=$("#acfoByMonthCheck").is(':checked');
	}

	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			if(whatTime=="AcfoYear"){
				loadAcfoYearData(data);
				$("#AcfoByYearText").text("Cumulative sell-out  as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);

			}else{
				loadAcfoMonthData(data,whatTime);
				
				if(whatTime=="AcfoWeek"){
					$("#weekAcfoText").text("Acfo/Area sellout updates as of "+getENMonth(begMonth[1])+" "+begMonth[2]+","+begMonth[0]+" - "+getENMonth(endMonth[1])+" "+endMonth[2]+","+endMonth[0]);
				}else{
					$("#monthAcfoText").text("Total Sellout performances of ACFO for the month of "+getENMonth(begMonth[1])+" "+begMonth[0]);
					
				}

			}
			
			
		}
	});
	
}




function getYearByReg(){
	var dateYear=$("#reservationAnnualReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		RegByMonth( dateYear+"-01-01",dateYear+"-12-"+last,"RegYear");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		RegByMonth( Common_date.getTheFirstDay()[0],str,"RegYear");
	}
}


function getYearByAcfo(){
	var dateYear=$("#reservationAnnualAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		AcfoByMonth( dateYear+"-01-01",dateYear+"-12-"+last,"AcfoYear");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		AcfoByMonth( Common_date.getTheFirstDay()[0],str,"AcfoYear");
	}
}




function getMonthByReg(){
	loadMonthMenu();//月度选项卡 
	var dateMonth=$("#reservationMonthlyReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		RegByMonth(beginDate,endDate,'RegMonth');

	}else{
		RegByMonth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'RegMonth');

	}
	
}

function getWeekByReg(){
	loadWeekMenu();//加载周度选项卡
	var dateWeek=$("#reservationWeeklyReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		RegByMonth(beginDate,endDate,'RegWeek');

	}else{
		RegByMonth(Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],'RegWeek');
	}
}





function getMonthByAcfo(){
	var dateMonth=$("#reservationMonthlyAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		AcfoByMonth(beginDate,endDate,'AcfoMonth');

	}else{
		AcfoByMonth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'AcfoMonth');

	}
	
}




function getWeekByAcfo(){
	
	
	var dateWeek=$("#reservationWeeklyAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		AcfoByMonth(beginDate,endDate,'AcfoWeek');

	}else{
		AcfoByMonth(Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],'AcfoWeek');
	}
	
}


function getMonthGrowthBySale(){
	var dateMonth=$("#reservationMonthlySaleGro").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		SaleByMonthGrowth(beginDate,endDate,'SaleMonthTotal');

	}else{
		SaleByMonthGrowth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'SaleMonthTotal');

	}
	
}



function getMonthBySaleUD(){
	var dateMonth=$("#monthSaleUD").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		SaleByMonthGrowth(beginDate,endDate,'SaleMonthUD');

	}else{
		SaleByMonthGrowth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'SaleMonthUD');

	}
	
}

function getMonthBySaleXCP(){
	var dateMonth=$("#monthSaleXCP").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		SaleByMonthGrowth(beginDate,endDate,'SaleMonthXCP');

	}else{
		SaleByMonthGrowth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'SaleMonthXCP');

	}
	
}


function getMonthBySaleSmart(){
	var dateMonth=$("#monthSaleSmart").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		SaleByMonthGrowth(beginDate,endDate,'SaleMonthSmart');

	}else{
		SaleByMonthGrowth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'SaleMonthSmart');

	}
	
}



function getMonthBySaleBig(){
	var dateMonth=$("#monthSaleBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		SaleByMonthGrowth(beginDate,endDate,'SaleMonthBig');

	}else{
		SaleByMonthGrowth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'SaleMonthBig');

	}
	
}



function getMonthBySaleCurved(){
	var dateMonth=$("#monthSaleCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		SaleByMonthGrowth(beginDate,endDate,'SaleMonthCurved');

	}else{
		SaleByMonthGrowth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'SaleMonthCurved');

	}
	
}












function getYearGrowthBySale(){
	var dateYear=$("#reservationYearlySaleGro").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleByMonthGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"SaleYearTotal");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getTheFirstDay()[0],str,"SaleYearTotal");
	}
	
}



function getYearBySaleUD(){
	var dateYear=$("#YearSaleUD").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleByMonthGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"SaleYearUD");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getTheFirstDay()[0],str,"SaleYearUD");
	}
	
	
}

function getYearBySaleXCP(){
	var dateYear=$("#YearSaleXCP").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleByMonthGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"SaleYearXCP");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getTheFirstDay()[0],str,"SaleYearXCP");
	}
	
	
}


function getYearBySaleSmart(){
	var dateYear=$("#YearSaleSmart").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleByMonthGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"SaleYearSmart");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getTheFirstDay()[0],str,"SaleYearSmart");
	}
	
}



function getYearBySaleBig(){
	var dateYear=$("#YearSaleBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleByMonthGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"SaleYearBig");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getTheFirstDay()[0],str,"SaleYearBig");
	}
	
}



function getYearBySaleCurved(){
	var dateYear=$("#YearSaleCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleByMonthGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"SaleYearCurved");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getTheFirstDay()[0],str,"SaleYearCurved");
	}
	
	
}

function getWeekGrowthBySale(){

	var dateWeek=$("#reservationWeeklySaleGro").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		SaleByMonthGrowth(beginDate,endDate,'SaleWeekTotal');

	}else{
		SaleByMonthGrowth(Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],'SaleWeekTotal');
	}
	
}





function getWeekBySaleUD(){
	var dateWeek=$("#WeekSaleUD").val();
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		SaleByMonthGrowth(beginDate,endDate,"SaleWeekUD");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],"SaleWeekUD");
	}
	
	
}

function getWeekBySaleXCP(){
	var dateWeek=$("#WeekSaleXCP").val();
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		SaleByMonthGrowth(beginDate,endDate,"SaleWeekXCP");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],"SaleWeekXCP");
	}
	
}


function getWeekBySaleSmart(){
	var dateWeek=$("#WeekSaleSmart").val();
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		SaleByMonthGrowth(beginDate,endDate,"SaleWeekSmart");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],"SaleWeekSmart");
	}
}



function getWeekBySaleBig(){
	var dateWeek=$("#WeekSaleBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		SaleByMonthGrowth(beginDate,endDate,"SaleWeekBig");

	}else{
		var d = new Date();
		//var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],"SaleWeekBig");
	}
}



function getWeekBySaleCurved(){
	var dateWeek=$("#WeekSaleCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		SaleByMonthGrowth(beginDate,endDate,"SaleWeekCurved");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByMonthGrowth( Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],"SaleWeekCurved");
	}
	
	
}














function getYearByCountryUD(){
	var dateYear=$("#YearCountryUD").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			CountryByGrowth( Common_date.getTheFirstDay()[0],str,"UD");
		}else{
			CountryByGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"UD");

		}

	}else{
		CountryByGrowth( Common_date.getTheFirstDay()[0],str,"UD");
	}
	
	
}

function getYearByCountryXCP(){
	var dateYear=$("#YearCountryXCP").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			CountryByGrowth( Common_date.getTheFirstDay()[0],str,"XCP");
		}else{
			CountryByGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"XCP");

		}

	}else{
		CountryByGrowth( Common_date.getTheFirstDay()[0],str,"XCP");
	}
	
}


function getYearByCountrySmart(){
	var dateYear=$("#YearCountrySmart").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			CountryByGrowth( Common_date.getTheFirstDay()[0],str,"Smart");
		}else{
			CountryByGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"Smart");

		}

	}else{
		CountryByGrowth( Common_date.getTheFirstDay()[0],str,"Smart");
	}
}



function getYearByCountryBig(){
	var dateYear=$("#YearCountryBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			CountryByGrowth( Common_date.getTheFirstDay()[0],str,"Big");
		}else{
			CountryByGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"Big");

		}

	}else{
		CountryByGrowth( Common_date.getTheFirstDay()[0],str,"Big");
	}
}



function getYearByCountryCurved(){
	var dateYear=$("#YearCountryCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			CountryByGrowth( Common_date.getTheFirstDay()[0],str,"Curved");
		}else{
			CountryByGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"Curved");

		}

	}else{
		CountryByGrowth( Common_date.getTheFirstDay()[0],str,"Curved");
	}
	
	
}



function getWeekGrowthByCustomer(){

	var dateWeek=$("#reservationWeeklyCustomerGro").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		CustomerByWeekGrowth(beginDate,endDate,'CustomerWeek');
	}else{
		CustomerByWeekGrowth(Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],'CustomerWeek');
	}
	
}




function getYearSale(){
	var dateYear=$("#reservationAnnualSale").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleByYear( dateYear+"-01-01",dateYear+"-12-"+last);

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleByYear( Common_date.getTheFirstDay()[0],str);
	}
}

function getYearXCP(){
	var dateYear=$("#reservationAnnualXCP").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			XCPByYear( Common_date.getTheFirstDay()[0],str);
		}else{
			XCPByYear( dateYear+"-01-01",dateYear+"-12-"+last);

		}

	}else{
		XCPByYear( Common_date.getTheFirstDay()[0],str);
	}
}



function getGroCountry(){
	loadYearMenu();//加载增长年度选项卡
	var dateYear=$("#reservationAnnualGro").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			CountryByGrowth( Common_date.getTheFirstDay()[0],str,"Total");
		}else{
			CountryByGrowth( dateYear+"-01-01",dateYear+"-12-"+last,"Total");

		}

	}else{
		CountryByGrowth( Common_date.getTheFirstDay()[0],str,"Total");
	}
}


function selectPartyCus(){
	
	$.ajax({
		url:baseUrl + "platform/selectPartyCus.action",
		type:"POST",
		success:function(data){
			var STring = $.parseJSON(data);
			if(STring!=null&&STring.length>0){
					$('#DealerName').html('');
					var option="<option value=''></option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].PARTY_ID+"'>"+STring[i].PARTY_NAME+"</option>";
						
					}
						$('#DealerName').html(option);
					
				
			}else{
						$('#DealerName').html("<option value=''></option>");

			}
			
			
		   
		}
	});

	
}



function getSubXcp(){
	
	var line=$("#xcp").val();
	if( line==""){
		$('#subXcp').html('');
		//line="AND (cfg.`PVALUE` LIKE '%X%' OR cfg.`PVALUE` LIKE '%C%' OR cfg.`PVALUE` LIKE '%P%')";
		$('#subXcp').html("<option value=''></option>");
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
					$('#subXcp').html('');
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].PVALUE+"'>"+STring[i].PVALUE+"</option>";
						
					}
						$('#subXcp').html(option);
				
			}else{
						$('#subXcp').html("<option value=''></option>");

			}
			
		   
		}
	});
	
}



















function getXCPModel(){
	
		
	$.ajax({
		url:baseUrl + "platform/selectXCPModel.action",
		type:"POST",
		success:function(data){
			var STring = $.parseJSON(data);
			if(STring!=null&&STring.length>0){
					$('#CountryUDModelByYear').html('');
					$('#SaleUDModelByYear').html('');
					$('#SaleUDModelByMonth').html('');
					$('#SaleUDModelByWeek').html('');
					
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].model+"'>"+STring[i].model+"</option>";
						
					}
					
					$('#CountryUDModelByYear').html(option);
					$('#SaleUDModelByYear').html(option);
					$('#SaleUDModelByMonth').html(option);
					$('#SaleUDModelByWeek').html(option);
					
			}else{
						$('#CountryUDModelByYear').html("<option value=''></option>");
						$('#SaleUDModelByYear').html("<option value=''></option>");
						$('#SaleUDModelByMonth').html("<option value=''></option>");
						$('#SaleUDModelByWeek').html("<option value=''></option>");
						
			}
			
		   
		}
	});
	
}
function getCurvedModel(){
	
	
	$.ajax({
		url:baseUrl + "platform/selectCurvedModel.action",
		type:"POST",
		success:function(data){
			var STring = $.parseJSON(data);
			if(STring!=null&&STring.length>0){
					$('#CurvedModelCountryYear').html('');
					$('#CurvedModelSaleYear').html('');
					$('#CurvedModelSaleMonth').html('');
					$('#CurvedModelSaleWeek').html('');
					
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].model+"'>"+STring[i].model+"</option>";
						
					}
					
						$('#CurvedModelCountryYear').html(option);
						$('#CurvedModelSaleYear').html(option);
						$('#CurvedModelSaleMonth').html(option);
						$('#CurvedModelSaleWeek').html(option);
				
			}else{
						$('#CurvedModelCountryYear').html("<option value=''></option>");
						$('#CurvedModelSaleYear').html("<option value=''></option>");
						$('#CurvedModelSaleMonth').html("<option value=''></option>");
						$('#CurvedModelSaleWeek').html("<option value=''></option>");

			}
			
		   
		}
	});
	
}

//--------------------------------------------渠道后期补齐--------------------------------------

function getRegionHeadTabDateChannel(tab){//获取月度区域经理选项卡选择时间、是否还原系数
	var o = {};
	var coeff;
	var selectDateVal;
	var xcpWhere;
	var region;
	if('Smart' == tab){
		selectDateVal = $('#reservationWeeklyCustomerGro_Smart').val();
		coeff = $('#GrowthByCustomerCheck_Smart').is(":checked");
		region = $('#DealerName_Smart').val();
	} else if('UD' == tab){
		selectDateVal = $('#reservationWeeklyCustomerGro_UD').val();
		coeff = $('#GrowthByCustomerCheck_UD').is(":checked");
		region = $('#DealerName_UD').val();
	} else if('Big' == tab){
		selectDateVal = $('#reservationWeeklyCustomerGro_Big').val();
		coeff = $('#GrowthByCustomerCheck_Big').is(":checked");
		region = $('#DealerName_Big').val();
		big = $('#Channel_big_week').val();
	} else if('Curved' == tab){
		selectDateVal = $('#reservationWeeklyCustomerGro_Curved').val();
		coeff = $('#GrowthByCustomerCheck_Curved').is(":checked");
		region = $('#DealerName_Curved').val();
	} else if('X/C/P' == tab){
		var subXcp = $('#subXcpChannelWeek').val();
		if(isStringNull(subXcp)){
			xcpWhere = subXcp;//" and pr.`product_line` like '%"+ subXcp +"%'";
		}else{
			var seriesXcp = $('#SeriesXcpChannelWeek').val();
			if(isStringNull(seriesXcp)){
				xcpWhere =seriesXcp;// " and pr.`product_line` like '%"+ seriesXcp +"%'";
			}else{
				xcpWhere ="";// " and (pr.`product_line` like '%X%' or pr.`product_line` like '%C%' or pr.`product_line` like '%P%' ) ";
			}
		}
		selectDateVal = $('#reservationWeeklyCustomerGro_XCP').val();
		coeff = $('#GrowthByCustomerCheck_XCP').is(":checked");
		region = $('#DealerName_XCP').val();
	}
	o.xcpWhere = xcpWhere;
	o.coeff = coeff;
	o.selectDateVal = selectDateVal;
	o.region = region;
	return o;
}

function getWeekGrowthByCustomers(tab){
	$("#loadingImportOne").show();
	var selectDateVal = getRegionHeadTabDateChannel(tab).selectDateVal ;
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
	o.endDate = endDate;
	if(tab == 'X/C/P'){
		o.party=$("#DealerName_XCP").val();
	}else{
		o.party=$("#DealerName_" + tab).val();
	}
	o.check = getRegionHeadTabDateChannel(tab).coeff ;
	o.what="WeekCustomerGrowth";
	o.tab = tab;
	o.big = $('#together_Channel_big_week').val();
	o.xcpWhere = getRegionHeadTabDateChannel(tab).xcpWhere;
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImportOne").hide();
			loadCustomerWeekGrowths(data,beginDate,endDate,tab);
		}
	});
	
}

function selectPartysCus(t){
	var tab;
	if(t == 'X/C/P'){
		tab = 'XCP';
	}else{
		tab = t;
	}
	
	$.ajax({
		url:baseUrl + "platform/selectPartyCus.action",
		type:"POST",
		success:function(data){
			var STring = $.parseJSON(data);
			if(STring!=null&&STring.length>0){
					$('#DealerName_' + tab).html('');
					var option="<option value=''></option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].PARTY_ID+"'>"+STring[i].PARTY_NAME+"</option>";
					}
						$('#DealerName_' + tab).html(option);
			}else{
						$('#DealerName_' + tab).html("<option value=''></option>");
			}
		}
	});
}

function SeriesXcpchannel_week(target){
	var line=$(target).val();
	if(line==null || line==""){
		//line="AND (cfg.`PVALUE` LIKE '%X%' OR cfg.`PVALUE` LIKE '%C%' OR cfg.`PVALUE` LIKE '%P%')";
		$('#subXcpChannelWeek').html('');
		$('#subXcpChannelWeek').html("<option value=''> </option>");
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
					$('#subXcpChannelWeek' ).html('');
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].PVALUE+"'>"+STring[i].PVALUE+"</option>";
						
					}
						$('#subXcpChannelWeek').html(option);
				
			}else{
						$('#subXcpChannelWeek' ).html("<option value=''></option>");

			}
			
		   
		}
	});
	
}







