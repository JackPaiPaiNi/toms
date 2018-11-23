
$(document).ready(function() {
	getYearCountryTotal();
	getXCPModel();
	getCurvedModel();
});



function RegTotalByYear(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	if(what=="Total"){
		o.what="yearRegTotal";
		o.check=$("#YearChainRegCheck").is(':checked');
		$("#YearChainRegText").text("TOTAL Sellout performances of Regional Head for the month of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);

	}else if(what=="UD"){
		o.what="yearRegUD";
		o.RegUDModelByYear=$("#RegUDModelByYear").val();
		o.check=$("#YearUDRegCheck").is(':checked');
		$("#YearUDRegText").text("UD sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="XCP"){
		o.what="yearRegXCP";
		o.check=$("#YearXCPRegCheck").is(':checked');
		var subXcp=$("#subXcpRegYear").val();
		var xcp=$("#XcpRegYear").val();
		var whatHead;
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
		
		$("#YearXCPRegText").text(whatHead+"  series monthly sellout trend "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+endMonth[0]);
		
	}else if(what=="Smart"){
		o.what="yearRegSmart";
		o.check=$("#YearSmartRegCheck").is(':checked');
		$("#YearSmartRegText").text("Smart sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="Big"){
		o.what="yearRegBig";
		//o.line="AND  pr.`size`>"+$("#RegBigSize").val();
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  pr.`size`>48 ";

		}else{
			o.line=" AND  pr.`size`>55 ";
		}
		o.check=$("#YearBigRegCheck").is(':checked');
		$("#YearBigRegText").text("Big sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="Curved"){
		o.what="yearRegCurved";
		o.CurvedModelRegYear=$("#CurvedModelRegYear").val();
		o.check=$("#YearCurvedRegCheck").is(':checked');
		$("#YearCurvedRegText").text("Curved sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadRegTotal(data,beginDate,endDate,what);
		   
		}
	});
	
}



function CountryTotalByYear(beginDate,endDate){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	$("#YearChainCountryText").text("TOTAL Sellout performances of Country for the month of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);

	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.check=$("#YearChainCountryCheck").is(':checked');
	o.what="yearCountryTotal";
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadCountryTotal(data,beginDate,endDate);
		   
		}
	});
	
}

function CountryUDByYear(beginDate,endDate){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	$("#YearUDCountryText").text("UD sell-out as  of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	
	var o = {};
	o.countryUDModelByYear=$("#countryUDModelByYear").val();
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.check=$("#YearUDCountryCheck").is(':checked');

	o.what="yearCountryUD";
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadCountryUD(data,beginDate,endDate,"UD");
		   
		}
	});
	
}

function CountryCurvedByYear(beginDate,endDate){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	$("#YearCurvedCountryText").text("Curved sell-out as  of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	
	var o = {};
	o.countryCurvedModelByYear=$("#countryCurvedModelByYear").val();
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.check=$("#YearCurvedCountryCheck").is(':checked');

	o.what="yearCountryCurved";
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadCountryUD(data,beginDate,endDate,"Curved");
		   
		}
	});
	
}


function CountrySmartByYear(beginDate,endDate){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	$("#YearCountrySmartText").text("Smart sell-out as  of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.check=$("#YearCountrySmartCheck").is(':checked');
	o.what="yearCountrySmart";
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadCountryUD(data,beginDate,endDate,"Smart");
		   
		}
	});
	
}


function CountryBigByYear(beginDate,endDate){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");

	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	//o.line="AND  pt.`size`>"+$("#countryBigSize").val();
	if(parseInt(begMonth[0])<=2017){
		o.line=" AND  pt.`size`>48 ";

	}else{
		o.line=" AND  pt.`size`>55 ";
	}
	o.check=$("#YearCountryBigCheck").is(':checked');
	o.what="yearCountryBig";
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadCountryBig(data,beginDate,endDate);
		   
		}
	});
	
}



function getYearCountryTotal(){
	var dateYear=$("#YearChainCountry").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		CountryTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last);

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		CountryTotalByYear( Common_date.getTheFirstDay()[0],str);
	}
}


function getYearCountryBig(){
	var dateYear=$("#YearCountryBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		CountryBigByYear( dateYear+"-01-01",dateYear+"-12-"+last);

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		CountryBigByYear( Common_date.getTheFirstDay()[0],str);
	}
}








function getYearCountryCurved(){
	var dateYear=$("#YearCountryCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		CountryCurvedByYear( dateYear+"-01-01",dateYear+"-12-"+last);

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		CountryCurvedByYear( Common_date.getTheFirstDay()[0],str);
	}
}




function getYearCountryUD(){
	var dateYear=$("#YearUDCountry").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		CountryUDByYear( dateYear+"-01-01",dateYear+"-12-"+last);

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		CountryUDByYear( Common_date.getTheFirstDay()[0],str);
	}
}


function getYearCountrySmart(){
	var dateYear=$("#YearCountrySmart").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		CountrySmartByYear( dateYear+"-01-01",dateYear+"-12-"+last);

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		CountrySmartByYear( Common_date.getTheFirstDay()[0],str);
	}
}




function getYearRegTotal(){
	var dateYear=$("#YearChainReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		RegTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Total");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		RegTotalByYear( Common_date.getTheFirstDay()[0],str,"Total");
	}
}




function getYearRegUD(){
	var dateYear=$("#YearUDReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		RegTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"UD");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		RegTotalByYear( Common_date.getTheFirstDay()[0],str,"UD");
	}
}





function getYearRegXCP(){
	var dateYear=$("#YearXCPReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		RegTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"XCP");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		RegTotalByYear( Common_date.getTheFirstDay()[0],str,"XCP");
	}
}



function getYearRegSmart(){
	var dateYear=$("#YearSmartReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		RegTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Smart");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		RegTotalByYear( Common_date.getTheFirstDay()[0],str,"Smart");
	}
}

function getYearRegBig(){
	var dateYear=$("#YearBigReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		RegTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Big");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		RegTotalByYear( Common_date.getTheFirstDay()[0],str,"Big");
	}
}



function getYearRegCurved(){
	var dateYear=$("#YearCurvedReg").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		RegTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Curved");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		RegTotalByYear( Common_date.getTheFirstDay()[0],str,"Curved");
	}
}




function SaleTotalByYear(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");

	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	
	
	if(what=="Total"){
		o.what="yearSaleTotal";
		o.check=$("#YearChainSaleCheck").is(':checked');
		$("#YearChainSaleText").text("Cumulative sell-out  as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="UD"){
		o.what="yearSaleUD";
		o.SaleUDModelByYear=$("#SaleUDModelByYear").val();
		o.check=$("#YearUDSaleCheck").is(':checked');
		$("#YearUDSaleText").text("UD sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="XCP"){
		o.what="yearSaleXCP";
		o.check=$("#YearXCPSaleCheck").is(':checked');
		var subXcp=$("#subXcpSaleYear").val();
		var xcp=$("#XcpSaleYear").val();
		var whatHead;
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
		
		$("#YearXCPSaleText").text(whatHead+"  series monthly sellout trend "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+endMonth[0]);
		
	}else if(what=="Smart"){
		o.what="yearSaleSmart";
		o.check=$("#YearSmartSaleCheck").is(':checked');
		$("#YearSmartSaleText").text("Smart sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="Big"){
		o.what="yearSaleBig";
		//o.line="AND  pr.`size`>"+$("#SaleBigSize").val();
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  pr.`size`>48 ";

		}else{
			o.line=" AND  pr.`size`>55 ";
		}
		
		o.check=$("#YearBigSaleCheck").is(':checked');
		$("#YearBigSaleText").text("Big sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="Curved"){
		o.what="yearSaleCurved";
		o.CurvedModelSaleYear=$("#CurvedModelSaleYear").val();
		o.check=$("#YearCurvedSaleCheck").is(':checked');
		$("#YearCurvedSaleText").text("Curved sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadSaleTotal(data,beginDate,endDate,what);
		   
		}
	});
	
}


function AcfoTotalByYear(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	
	
	if(what=="Total"){
		o.check=$("#YearChainAcfoCheck").is(':checked');
		o.what="yearAcfoTotal";
		$("#YearChainAcfoText").text("Cumulative sell-out  as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="UD"){
		o.what="yearAcfoUD";
		o.AcfoUDModelByYear=$("#AcfoUDModelByYear").val();
		o.check=$("#YearUDAcfoCheck").is(':checked');
		$("#YearUDAcfoText").text("UD sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="XCP"){
		o.what="yearAcfoXCP";
		o.check=$("#YearXCPAcfoCheck").is(':checked');
		var subXcp=$("#subXcpAcfoYear").val();
		var xcp=$("#XcpAcfoYear").val();
		var whatHead;
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
		
		$("#YearXCPAcfoText").text(whatHead+"  series monthly sellout trend "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+endMonth[0]);
		
	}else if(what=="Smart"){
		o.what="yearAcfoSmart";
		o.check=$("#YearSmartAcfoCheck").is(':checked');
		$("#YearSmartAcfoText").text("Smart sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="Big"){
		o.what="yearAcfoBig";
		//o.line="AND  pr.`size`>"+$("#AcfoBigSize").val();
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  pr.`size`>48 ";

		}else{
			o.line=" AND  pr.`size`>55 ";
		}
		o.check=$("#YearBigAcfoCheck").is(':checked');
		$("#YearBigAcfoText").text("Big sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}else if(what=="Curved"){
		o.what="yearAcfoCurved";
		o.CurvedModelAcfoYear=$("#CurvedModelAcfoYear").val();
		o.check=$("#YearCurvedAcfoCheck").is(':checked');
		$("#YearCurvedAcfoText").text("Curved sell-out as of  "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+" "+begMonth[0]);
	}
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadAcfoTotal(data,beginDate,endDate,what);
		   
		}
	});
	
}

function getYearSaleTotal(){
	var dateYear=$("#YearChainSale").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Total");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleTotalByYear( Common_date.getTheFirstDay()[0],str,"Total");
	}
}



function getYearSaleUD(){
	var dateYear=$("#YearUDSale").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"UD");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleTotalByYear( Common_date.getTheFirstDay()[0],str,"UD");
	}
}

function getYearSaleXCP(){
	var dateYear=$("#YearXCPSale").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"XCP");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleTotalByYear( Common_date.getTheFirstDay()[0],str,"XCP");
	}
}


function getYearSaleSmart(){
	var dateYear=$("#YearSmartSale").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Smart");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleTotalByYear( Common_date.getTheFirstDay()[0],str,"Smart");
	}
}

function getYearSaleBig(){
	var dateYear=$("#YearBigSale").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Big");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleTotalByYear( Common_date.getTheFirstDay()[0],str,"Big");
	}
}






function getYearSaleCurved(){
	var dateYear=$("#YearCurvedSale").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		SaleTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Curved");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		SaleTotalByYear( Common_date.getTheFirstDay()[0],str,"Curved");
	}
}



function getYearAcfoTotal(){
	var dateYear=$("#YearChainAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		AcfoTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Total");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		AcfoTotalByYear( Common_date.getTheFirstDay()[0],str,"Total");
	}
}







function getYearAcfoUD(){
	var dateYear=$("#YearUDAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		AcfoTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"UD");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		AcfoTotalByYear( Common_date.getTheFirstDay()[0],str,"UD");
	}
}

function getYearAcfoXCP(){
	var dateYear=$("#YearXCPAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		AcfoTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"XCP");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		AcfoTotalByYear( Common_date.getTheFirstDay()[0],str,"XCP");
	}
}


function getYearAcfoSmart(){
	var dateYear=$("#YearSmartAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		AcfoTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Smart");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		AcfoTotalByYear( Common_date.getTheFirstDay()[0],str,"Smart");
	}
}

function getYearAcfoBig(){
	var dateYear=$("#YearBigAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		AcfoTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Big");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		AcfoTotalByYear( Common_date.getTheFirstDay()[0],str,"Big");
	}
}






function getYearAcfoCurved(){
	var dateYear=$("#YearCurvedAcfo").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		AcfoTotalByYear( dateYear+"-01-01",dateYear+"-12-"+last,"Curved");

	}else{
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		AcfoTotalByYear( Common_date.getTheFirstDay()[0],str,"Curved");
	}
}




/*
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
		SaleByMonthGrowth(beginDate,endDate,'SaleMonth');

	}else{
		SaleByMonthGrowth(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'SaleMonth');

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
		SaleByMonthGrowth(beginDate,endDate,'SaleWeek');

	}else{
		SaleByMonthGrowth(Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],'SaleWeek');
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


*/


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


function XCPByYear(beginDate,endDate){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	
	
	var whatHead;
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	o.what="yearXCP";
	o.check=$("#reservationAnnualXCPCheck").is(':checked');
	
	var subXcp=$("#subXcp").val();
	var xcp=$("#Xcp").val();
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
	
	$("#yearXCPText").text(whatHead+"  series monthly sellout trend "+getENMonth(begMonth[1])+" - "+getENMonth(endMonth[1])+""+endMonth[0]);
	
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


function getGroCountry(){
	var dateYear=$("#reservationAnnualGro").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			CountryByGrowth( Common_date.getTheFirstDay()[0],str);
		}else{
			CountryByGrowth( dateYear+"-01-01",dateYear+"-12-"+last);

		}

	}else{
		CountryByGrowth( Common_date.getTheFirstDay()[0],str);
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



function getXCPModel(){
	
		
	$.ajax({
		url:baseUrl + "platform/selectXCPModel.action",
		type:"POST",
		success:function(data){
			var STring = $.parseJSON(data);
			if(STring!=null&&STring.length>0){
					$('#countryUDModelByYear').html('');
					$('#RegUDModelByYear').html('');
					$('#SaleUDModelByYear').html('');
					$('#AcfoUDModelByYear').html('');
					
					
					$('#countryUDModelByMonth').html('');
					$('#RegUDModelByMonth').html('');
					$('#SaleUDModelByMonth').html('');
					$('#AcfoUDModelByMonth').html('');
					
					
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].model+"'>"+STring[i].model+"</option>";
						
					}
					
					$('#countryUDModelByYear').html(option);
					$('#RegUDModelByYear').html(option);
					$('#SaleUDModelByYear').html(option);
					$('#AcfoUDModelByYear').html(option);
					
					
					$('#countryUDModelByMonth').html(option);
					$('#RegUDModelByMonth').html(option);
					$('#SaleUDModelByMonth').html(option);
					$('#AcfoUDModelByMonth').html(option);
					
			}else{
						$('#countryUDModelByYear').html("<option value=''></option>");
						$('#RegUDModelByYear').html("<option value=''></option>");
						$('#SaleUDModelByYear').html("<option value=''></option>");
						$('#AcfoUDModelByYear').html("<option value=''></option>");
						
						$('#countryUDModelByMonth').html("<option value=''></option>");
						$('#RegUDModelByMonth').html("<option value=''></option>");
						$('#SaleUDModelByMonth').html("<option value=''></option>");
						$('#AcfoUDModelByMonth').html("<option value=''></option>");
						
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
					$('#countryCurvedModelByYear').html('');
					$('#CurvedModelRegYear').html('');
					$('#CurvedModelSaleYear').html('');
					$('#CurvedModelAcfoYear').html('');
					
					
					$('#countryCurvedModelByMonth').html('');
					$('#CurvedModelRegMonth').html('');
					$('#CurvedModelSaleMonth').html('');
					$('#CurvedModelAcfoMonth').html('');
					
					
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].model+"'>"+STring[i].model+"</option>";
						
					}
						$('#countryCurvedModelByYear').html(option);
						$('#CurvedModelRegYear').html(option);
						$('#CurvedModelSaleYear').html(option);
						$('#CurvedModelAcfoYear').html(option);
						
						$('#countryCurvedModelByMonth').html(option);
						$('#CurvedModelRegMonth').html(option);
						$('#CurvedModelSaleMonth').html(option);
						$('#CurvedModelAcfoMonth').html(option);
				
			}else{
						$('#countryCurvedModelByYear').html("<option value=''></option>");
						$('#CurvedModelRegYear').html("<option value=''></option>");
						$('#CurvedModelSaleYear').html("<option value=''></option>");
						$('#CurvedModelAcfoYear').html("<option value=''></option>");
						
						$('#countryCurvedModelByMonth').html("<option value=''></option>");
						$('#CurvedModelRegMonth').html("<option value=''></option>");
						$('#CurvedModelSaleMonth').html("<option value=''></option>");
						$('#CurvedModelAcfoMonth').html("<option value=''></option>");

			}
			
		   
		}
	});
	
}





function getSubXcp(target){
	var tarId=target.id;
	var line=$(target).val();
	if(line==null || line==""){
		$('#sub'+tarId).html("");
		$('#sub'+tarId).html("<option value=''></option>");
		return;
		
		//line="AND (cfg.`PVALUE` LIKE '%X%' OR cfg.`PVALUE` LIKE '%C%' OR cfg.`PVALUE` LIKE '%P%')";
	}else {
		line="AND cfg.`PVALUE` LIKE '%"+line+"%'";
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
					$('#sub'+tarId).html('');
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].PVALUE+"'>"+STring[i].PVALUE+"</option>";
						
					}
						$('#sub'+tarId).html(option);
				
			}else{
						$('#sub'+tarId).html("<option value=''></option>");

			}
			
		   
		}
	});
	
}






























//====================================Month Table===========================================


function getMonthByCountryTotal(){
	loadMonthMenu();//生成月度选项卡
	var dateMonth=$("#monthCountryTotal").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		CountryByMonthTotal(beginDate,endDate,'Total');

	}else{
		CountryByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Total');

	}
	
}



function getMonthByCountryUD(){
	var dateMonth=$("#monthCountryUD").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		CountryByMonthTotal(beginDate,endDate,'UD');

	}else{
		CountryByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'UD');

	}
	
}


function getMonthByCountrySmart(){
	var dateMonth=$("#monthCountrySmart").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		CountryByMonthTotal(beginDate,endDate,'Smart');

	}else{
		CountryByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Smart');

	}
	
}

function getMonthByCountryCurved(){
	var dateMonth=$("#monthCountryCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		CountryByMonthTotal(beginDate,endDate,'Curved');

	}else{
		CountryByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Curved');

	}
	
}


function getMonthByCountryXCP(){
	var dateMonth=$("#monthCountryXCP").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		CountryByMonthTotal(beginDate,endDate,'XCP');

	}else{
		CountryByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'XCP');

	}
	
}



function getMonthByCountryBig(){
	var dateMonth=$("#monthCountryBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		CountryByMonthTotal(beginDate,endDate,'Big');

	}else{
		CountryByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Big');

	}
	
}




function CountryByMonthTotal(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");

	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	
	
	if(what=="Total"){
		o.what="monthCountryTotal";
		o.check=$("#monthCountryTotalCheck").is(':checked');
		$("#monthCountryTotalText").text("Cumulative sell-out as of  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="UD"){
		o.what="monthCountryUD";
		o.countryUDModelByMonth=$("#countryUDModelByMonth").val();
		o.check=$("#monthCountryUDCheck").is(':checked');
		$("#monthCountryUDText").text("UD sell-out as of  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="XCP"){
		o.what="monthCountryXCP";
		o.check=$("#monthCountryXCPCheck").is(':checked');
		var subXcp=$("#subXcpCountryMonth").val();
		var xcp=$("#XcpCountryMonth").val();
		var whatHead;
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
		
		$("#monthCountryXCPText").text(whatHead+"  series monthly sellout trend  "+getENMonth(endMonth[1])+" "+endMonth[0]);
		
	}else if(what=="Smart"){
		o.what="monthCountrySmart";
		o.check=$("#monthCountrySmartCheck").is(':checked');
		$("#monthCountrySmartText").text("Smart sell-out as of   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="Big"){
		o.what="monthCountryBig";
		//o.line="AND  pr.`size`>"+$("#CountryBigSizeByMonth").val();
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  pr.`size`>48 ";

		}else{
			o.line=" AND  pr.`size`>55 ";
		}
		o.check=$("#monthCountryBigCheck").is(':checked');
		$("#monthCountryBigText").text("Big sell-out as of   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="Curved"){
		o.what="monthCountryCurved";
		o.countryCurvedModelByMonth=$("#countryCurvedModelByMonth").val();
		o.check=$("#monthCountryCurvedCheck").is(':checked');
		$("#monthCountryCurvedText").text("Curved sell-out as of  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			if(what=="XCP" ||what=="Big" ){
			loadXCPMonthData(data,beginDate,endDate,what);
			}else{
				loadMonthCountryTotal(data,beginDate,endDate,what);
			}
		   
		}
	});
	
}




					//=======================================Regional Head==========================
function getMonthByRegTotal(){
	var dateMonth=$("#monthRegTotal").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		RegByMonthTotal(beginDate,endDate,'Total');

	}else{
		RegByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Total');

	}
	
}

function getMonthByRegUD(){
	var dateMonth=$("#monthRegUD").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		RegByMonthTotal(beginDate,endDate,'UD');

	}else{
		RegByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'UD');

	}
	
}

function getMonthByRegXCP(){
	var dateMonth=$("#monthRegXCP").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		RegByMonthTotal(beginDate,endDate,'XCP');

	}else{
		RegByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'XCP');

	}
	
}


function getMonthByRegSmart(){
	var dateMonth=$("#monthRegSmart").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		RegByMonthTotal(beginDate,endDate,'Smart');

	}else{
		RegByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Smart');

	}
	
}



function getMonthByRegBig(){
	var dateMonth=$("#monthRegBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		RegByMonthTotal(beginDate,endDate,'Big');

	}else{
		RegByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Big');

	}
	
}



function getMonthByRegCurved(){
	var dateMonth=$("#monthRegCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		RegByMonthTotal(beginDate,endDate,'Curved');

	}else{
		RegByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Curved');

	}
	
}


function RegByMonthTotal(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");

	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	
	
	if(what=="Total"){
		o.what="monthRegTotal";
		o.check=$("#monthRegTotalCheck").is(':checked');
		$("#monthRegTotalText").text("TOTAL Sellout performances   for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="UD"){
		o.what="monthRegUD";
		o.RegUDModelByMonth=$("#RegUDModelByMonth").val();
		o.check=$("#monthRegUDCheck").is(':checked');
		$("#monthRegUDText").text("UD Sellout performances  for the month of   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="XCP"){
		o.what="monthRegXCP";
		o.check=$("#monthRegXCPCheck").is(':checked');
		var subXcp=$("#subXcpRegMonth").val();
		var xcp=$("#XcpRegMonth").val();
		var whatHead;
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
		
		$("#monthRegXCPText").text(whatHead+"  series Sellout performances for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
		
	}else if(what=="Smart"){
		o.what="monthRegSmart";
		o.check=$("#monthRegSmartCheck").is(':checked');
		$("#monthRegSmartText").text("Smart Sellout performances  for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="Big"){
		o.what="monthRegBig";
		//o.line="AND  pr.`size`>"+$("#RegBigSizeByMonth").val();
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  pr.`size`>48 ";

		}else{
			o.line=" AND  pr.`size`>55 ";
		}
		o.check=$("#monthRegBigCheck").is(':checked');
		$("#monthRegBigText").text("Big Sellout performances  for the month   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="Curved"){
		o.what="monthRegCurved";
		o.CurvedModelRegMonth=$("#CurvedModelRegMonth").val();
		o.check=$("#monthRegCurvedCheck").is(':checked');
		$("#monthRegCurvedText").text("Curved Sellout performances   for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadMonthDataByReg(data,beginDate,endDate,what);
		   
		}
	});
	
}




//==========================================SalesMan Month========================================================




function getMonthBySaleTotal(){
	var dateMonth=$("#monthSaleTotal").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		SaleByMonthTotal(beginDate,endDate,'Total');

	}else{
		SaleByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Total');

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
		SaleByMonthTotal(beginDate,endDate,'UD');

	}else{
		SaleByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'UD');

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
		SaleByMonthTotal(beginDate,endDate,'XCP');

	}else{
		SaleByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'XCP');

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
		SaleByMonthTotal(beginDate,endDate,'Smart');

	}else{
		SaleByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Smart');

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
		SaleByMonthTotal(beginDate,endDate,'Big');

	}else{
		SaleByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Big');

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
		SaleByMonthTotal(beginDate,endDate,'Curved');

	}else{
		SaleByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Curved');

	}
	
}


function SaleByMonthTotal(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");

	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	
	
	if(what=="Total"){
		o.what="monthSaleTotal";
		o.check=$("#monthSaleTotalCheck").is(':checked');
		$("#monthSaleTotalText").text("TOTAL Sellout performances for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="UD"){
		o.what="monthSaleUD";
		o.SaleUDModelByMonth=$("#SaleUDModelByMonth").val();
		o.check=$("#monthSaleUDCheck").is(':checked');
		$("#monthSaleUDText").text("UD Sellout performances   for the month of   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="XCP"){
		o.what="monthSaleXCP";
		o.check=$("#monthSaleXCPCheck").is(':checked');
		var subXcp=$("#subXcpSaleMonth").val();
		var xcp=$("#XcpSaleMonth").val();
		var whatHead;
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
		
		$("#monthSaleXCPText").text(whatHead+"  series Sellout performances  for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
		
	}else if(what=="Smart"){
		o.what="monthSaleSmart";
		o.check=$("#monthSaleSmartCheck").is(':checked');
		$("#monthSaleSmartText").text("Smart Sellout performances  for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="Big"){
		o.what="monthSaleBig";
		//o.line="AND  pr.`size`>"+$("#SaleBigSizeByMonth").val();
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  pr.`size`>48 ";

		}else{
			o.line=" AND  pr.`size`>55 ";
		}
		o.check=$("#monthSaleBigCheck").is(':checked');
		$("#monthSaleBigText").text("Big Sellout performances  for the month   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="Curved"){
		o.what="monthSaleCurved";
		o.CurvedModelSaleMonth=$("#CurvedModelSaleMonth").val();
		o.check=$("#monthSaleCurvedCheck").is(':checked');
		$("#monthSaleCurvedText").text("Curved Sellout performances   for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadMonthDataBySale(data,beginDate,endDate,what);
		   
		}
	});
	
}














//=========================================Acfo========================================================




function getMonthByAcfoTotal(){
	var dateMonth=$("#monthAcfoTotal").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		AcfoByMonthTotal(beginDate,endDate,'Total');

	}else{
		AcfoByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Total');

	}
	
}

function getMonthByAcfoUD(){
	var dateMonth=$("#monthAcfoUD").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		AcfoByMonthTotal(beginDate,endDate,'UD');

	}else{
		AcfoByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'UD');

	}
	
}

function getMonthByAcfoXCP(){
	var dateMonth=$("#monthAcfoXCP").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		AcfoByMonthTotal(beginDate,endDate,'XCP');

	}else{
		AcfoByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'XCP');

	}
	
}


function getMonthByAcfoSmart(){
	var dateMonth=$("#monthAcfoSmart").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		AcfoByMonthTotal(beginDate,endDate,'Smart');

	}else{
		AcfoByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Smart');

	}
	
}



function getMonthByAcfoBig(){
	var dateMonth=$("#monthAcfoBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		AcfoByMonthTotal(beginDate,endDate,'Big');

	}else{
		AcfoByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Big');

	}
	
}



function getMonthByAcfoCurved(){
	var dateMonth=$("#monthAcfoCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
		AcfoByMonthTotal(beginDate,endDate,'Curved');

	}else{
		AcfoByMonthTotal(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'Curved');

	}
	
}


function AcfoByMonthTotal(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");

	
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	
	
	if(what=="Total"){
		o.what="monthAcfoTotal";
		o.check=$("#monthAcfoTotalCheck").is(':checked');
		$("#monthAcfoTotalText").text("TOTAL Sellout performances  for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="UD"){
		o.what="monthAcfoUD";
		o.AcfoUDModelByMonth=$("#AcfoUDModelByMonth").val();
		o.check=$("#monthAcfoUDCheck").is(':checked');
		$("#monthAcfoUDText").text("UD Sellout performances for the month of   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="XCP"){
		o.what="monthAcfoXCP";
		o.check=$("#monthAcfoXCPCheck").is(':checked');
		var subXcp=$("#subXcpAcfoMonth").val();
		var xcp=$("#XcpAcfoMonth").val();
		var whatHead;
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
		
		$("#monthAcfoXCPText").text(whatHead+"  series Sellout performances for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
		
	}else if(what=="Smart"){
		o.what="monthAcfoSmart";
		o.check=$("#monthAcfoSmartCheck").is(':checked');
		$("#monthAcfoSmartText").text("Smart Sellout performances  for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="Big"){
		o.what="monthAcfoBig";
		//o.line="AND  pr.`size`>"+$("#AcfoBigSizeByMonth").val();
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  pr.`size`>48 ";

		}else{
			o.line=" AND  pr.`size`>55 ";
		}
		o.check=$("#monthAcfoBigCheck").is(':checked');
		$("#monthAcfoBigText").text("Big Sellout performances for the month   "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}else if(what=="Curved"){
		o.what="monthAcfoCurved";
		o.CurvedModelAcfoMonth=$("#CurvedModelAcfoMonth").val();
		o.check=$("#monthAcfoCurvedCheck").is(':checked');
		$("#monthAcfoCurvedText").text("Curved Sellout performances  for the month  "+getENMonth(endMonth[1])+" "+endMonth[0]);
	}
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadMonthDataByAcfo(data,beginDate,endDate,what);
		   
		}
	});
	
}


