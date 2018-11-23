
$(document).ready(function() {
	getYearTotal();
});





function YearTotal(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	var o = {};
	var head=what;
	o.beginDate=beginDate;
	o.endDate=endDate;
	if(what=="Total"){
		o.what="yearTotalByHQChain";
		o.check=$("#YearTotalCheck").is(':checked');
		$("#YearTotalText").text(begMonth[0]+"  Sell-Out Growth Rate");
		
	}else if(what=="UD"){
		o.what="yearUDByHQChain";
		o.check=$("#YearUDCheck").is(':checked');
		$("#YearUDText").text(begMonth[0]+" UD Sell-Out Growth Rate");
	}else if(what=="XCP"){
		o.what="yearXCPByHQChain";
		o.check=$("#YearXCPCheck").is(':checked');
		var subXcp=$("#subXcpYear").val();
		var xcp=$("#XcpYear").val();
		
		if(subXcp!=null && subXcp!=""){
			o.line=subXcp;
			head=subXcp+" Series";
		}else{
			if(xcp!=null && xcp!=""){
				head=xcp+" Series";
				o.line=xcp;
			}else{
				head="X/C/P"+" Series";
				o.line="";

			}
		}
		
		$("#YearXCPText").text(begMonth[0]+"  "+head+"  Sell-Out Growth Rate");
		
	}else if(what=="Smart"){
		o.what="yearSmartByHQChain";
		o.check=$("#YearSmartCheck").is(':checked');
		$("#YearSmartText").text(begMonth[0]+" Smart  Sell-Out Growth Rate");
	}else if(what=="Big"){
		o.what="yearBigByHQChain";
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  sr.size>48 ";

		}else{
			o.line=" AND  sr.size>55 ";
		}
		o.check=$("#YearBigCheck").is(':checked');
		$("#YearBigText").text(begMonth[0]+" Big Sell-Out Growth Rate");
	}else if(what=="Curved"){
		o.what="yearCurvedByHQChain";
		o.check=$("#YearCurvedCheck").is(':checked');

		$("#yearCurvedText").text(begMonth[0]+" Curved Sell-Out Growth Rate");
	}
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadYearTotal(data,beginDate,endDate,what,head);

			
		}
	});
	
}











function HalfTotal(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	var o = {};
	var head=what;
	o.beginDate=beginDate;
	o.endDate=endDate;
	if(what=="Total"){
		o.what="HalfTotalByHQChain";
		o.check=$("#HalfTotalCheck").is(':checked');
		$("#HalfTotalText").text(begMonth[0]+"  Sell-Out Growth Rate");
		
	}else if(what=="UD"){
		o.what="HalfUDByHQChain";
		o.check=$("#HalfUDCheck").is(':checked');
		$("#HalfUDText").text(begMonth[0]+" UD Sell-Out Growth Rate");
	}else if(what=="XCP"){
		o.what="HalfXCPByHQChain";
		o.check=$("#HalfXCPCheck").is(':checked');
		var subXcp=$("#subXcpHalf").val();
		var xcp=$("#XcpHalf").val();
		
		if(subXcp!=null && subXcp!=""){
			o.line=subXcp;
			head=subXcp+" Series";
		}else{
			if(xcp!=null && xcp!=""){
				head=xcp+" Series";
				o.line=xcp;
			}else{
				head="X/C/P"+" Series";
				o.line="";

			}
		}
		
		$("#HalfXCPText").text(begMonth[0]+"  "+head+"  Sell-Out Growth Rate");
		
	}else if(what=="Smart"){
		o.what="HalfSmartByHQChain";
		o.check=$("#HalfSmartCheck").is(':checked');
		$("#HalfSmartText").text(begMonth[0]+" Smart  Sell-Out Growth Rate");
	}else if(what=="Big"){
		o.what="HalfBigByHQChain";
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  sr.size>48 ";

		}else{
			o.line=" AND  sr.size>55 ";
		}
		o.check=$("#HalfBigCheck").is(':checked');
		$("#HalfBigText").text(begMonth[0]+" Big Sell-Out Growth Rate");
	}else if(what=="Curved"){
		o.what="HalfCurvedByHQChain";
		o.check=$("#HalfCurvedCheck").is(':checked');

		$("#HalfCurvedText").text(begMonth[0]+" Curved Sell-Out Growth Rate");
	}
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			
			loadHalfTotal(data,beginDate,endDate,what,head);

			
		}
	});
	
}









function  QuarterTotal(beginDate,endDate,what){
	$("#loadingImport").show();
	var begMonth=beginDate.split("-");
	var endMonth=endDate.split("-");
	var o = {};
	var head=what;
	o.beginDate=beginDate;
	o.endDate=endDate;
	if(what=="Total"){
		o.what="QuarterTotalByHQChain";
		o.check=$("#QuarterTotalCheck").is(':checked');
		$("#QuarterTotalText").text(begMonth[0]+"  Sell-Out Growth Rate");
		
	}else if(what=="UD"){
		o.what="QuarterUDByHQChain";
		o.check=$("#QuarterUDCheck").is(':checked');
		$("#QuarterUDText").text(begMonth[0]+" UD Sell-Out Growth Rate");
	}else if(what=="XCP"){
		o.what="QuarterXCPByHQChain";
		o.check=$("#QuarterXCPCheck").is(':checked');
		var subXcp=$("#subXcpQuarter").val();
		var xcp=$("#XcpQuarter").val();
		
		if(subXcp!=null && subXcp!=""){
			o.line=subXcp;
			head=subXcp+" Series";
		}else{
			if(xcp!=null && xcp!=""){
				head=xcp+" Series";
				o.line=xcp;
			}else{
				head="X/C/P"+" Series";
				o.line="";

			}
		}
		
		$("#QuarterXCPText").text(begMonth[0]+"  "+head+"  Sell-Out Growth Rate");
		
	}else if(what=="Smart"){
		o.what="QuarterSmartByHQChain";
		o.check=$("#QuarterSmartCheck").is(':checked');
		$("#QuarterSmartText").text(begMonth[0]+" Smart  Sell-Out Growth Rate");
	}else if(what=="Big"){
		o.what="QuarterBigByHQChain";
		if(parseInt(begMonth[0])<=2017){
			o.line=" AND  sr.size>48 ";

		}else{
			o.line=" AND  sr.size>55 ";
		}
		o.check=$("#QuarterBigCheck").is(':checked');
		$("#QuarterBigText").text(begMonth[0]+" Big Sell-Out Growth Rate");
	}else if(what=="Curved"){
		o.what="QuarterCurvedByHQChain";
		o.check=$("#QuarterCurvedCheck").is(':checked');

		$("#QuarterCurvedText").text(begMonth[0]+" Curved Sell-Out Growth Rate");
	}
	
	$.ajax({
		url:baseUrl + "platform/selectGrandTTL.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#loadingImport").hide();
			loadQuarterTotal(data,beginDate,endDate,what,head);

			
		}
	});
	
}









function getQuarterTotal(){
	var dateQua=$("#QuarterTotal").val();
	var qua=$("#QuarterTotalQ").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var yearT=new Date().getFullYear();
	if(dateQua!=null && dateQua!=""){
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=dateQua+"-01-01";
				var last=getLastDay(dateQua,3);
				endDate=dateQua+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=dateQua+"-04-01";
				var last=getLastDay(dateQua,6);
				endDate=dateQua+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=dateQua+"-07-01";
				var last=getLastDay(dateQua,9);
				endDate=dateQua+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=dateQua+"-10-01";
				var last=getLastDay(dateQua,12);
				endDate=dateQua+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'Total');
		}else{
			QuarterTotal(getQua(dateQua)[0],getQua(dateQua)[1],'Total');
		}
		
		

	}else{
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=yearT+"-01-01";
				var last=getLastDay(yearT,3);
				endDate=yearT+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=yearT+"-04-01";
				var last=getLastDay(yearT,6);
				endDate=yearT+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=yearT+"-07-01";
				var last=getLastDay(yearT,9);
				endDate=yearT+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=yearT+"-10-01";
				var last=getLastDay(yearT,12);
				endDate=yearT+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'Total');
		}else{
			QuarterTotal(Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1],'Total');
		}

	}
}





function getQuarterUD(){
	var dateQua=$("#QuarterUD").val();
	var qua=$("#QuarterUDQ").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var yearT=new Date().getFullYear();
	if(dateQua!=null && dateQua!=""){
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=dateQua+"-01-01";
				var last=getLastDay(dateQua,3);
				endDate=dateQua+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=dateQua+"-04-01";
				var last=getLastDay(dateQua,6);
				endDate=dateQua+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=dateQua+"-07-01";
				var last=getLastDay(dateQua,9);
				endDate=dateQua+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=dateQua+"-10-01";
				var last=getLastDay(dateQua,12);
				endDate=dateQua+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'UD');
		}else{
			QuarterTotal(getQua(dateQua)[0],getQua(dateQua)[1],'UD');
		}
		
		

	}else{
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=yearT+"-01-01";
				var last=getLastDay(yearT,3);
				endDate=yearT+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=yearT+"-04-01";
				var last=getLastDay(yearT,6);
				endDate=yearT+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=yearT+"-07-01";
				var last=getLastDay(yearT,9);
				endDate=yearT+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=yearT+"-10-01";
				var last=getLastDay(yearT,12);
				endDate=yearT+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'UD');
		}else{
			QuarterTotal(Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1],'UD');
		}

	}
}





function getQuarterXCP(){
	var dateQua=$("#QuarterXCP").val();
	var qua=$("#QuarterXCPQ").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var yearT=new Date().getFullYear();
	if(dateQua!=null && dateQua!=""){
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=dateQua+"-01-01";
				var last=getLastDay(dateQua,3);
				endDate=dateQua+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=dateQua+"-04-01";
				var last=getLastDay(dateQua,6);
				endDate=dateQua+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=dateQua+"-07-01";
				var last=getLastDay(dateQua,9);
				endDate=dateQua+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=dateQua+"-10-01";
				var last=getLastDay(dateQua,12);
				endDate=dateQua+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'XCP');
		}else{
			QuarterTotal(getQua(dateQua)[0],getQua(dateQua)[1],'XCP');
		}
		
		

	}else{
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=yearT+"-01-01";
				var last=getLastDay(yearT,3);
				endDate=yearT+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=yearT+"-04-01";
				var last=getLastDay(yearT,6);
				endDate=yearT+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=yearT+"-07-01";
				var last=getLastDay(yearT,9);
				endDate=yearT+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=yearT+"-10-01";
				var last=getLastDay(yearT,12);
				endDate=yearT+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'XCP');
		}else{
			QuarterTotal(Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1],'XCP');
		}

	}
}









function getQuarterSmart(){
	var dateQua=$("#QuarterSmart").val();
	var qua=$("#QuarterSmartQ").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var yearT=new Date().getFullYear();
	if(dateQua!=null && dateQua!=""){
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=dateQua+"-01-01";
				var last=getLastDay(dateQua,3);
				endDate=dateQua+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=dateQua+"-04-01";
				var last=getLastDay(dateQua,6);
				endDate=dateQua+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=dateQua+"-07-01";
				var last=getLastDay(dateQua,9);
				endDate=dateQua+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=dateQua+"-10-01";
				var last=getLastDay(dateQua,12);
				endDate=dateQua+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'Smart');
		}else{
			QuarterTotal(getQua(dateQua)[0],getQua(dateQua)[1],'Smart');
		}
		
		

	}else{
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=yearT+"-01-01";
				var last=getLastDay(yearT,3);
				endDate=yearT+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=yearT+"-04-01";
				var last=getLastDay(yearT,6);
				endDate=yearT+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=yearT+"-07-01";
				var last=getLastDay(yearT,9);
				endDate=yearT+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=yearT+"-10-01";
				var last=getLastDay(yearT,12);
				endDate=yearT+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'Smart');
		}else{
			QuarterTotal(Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1],'Smart');
		}

	}
}







function getQuarterBig(){
	var dateQua=$("#QuarterBig").val();
	var qua=$("#QuarterBigQ").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var yearT=new Date().getFullYear();
	if(dateQua!=null && dateQua!=""){
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=dateQua+"-01-01";
				var last=getLastDay(dateQua,3);
				endDate=dateQua+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=dateQua+"-04-01";
				var last=getLastDay(dateQua,6);
				endDate=dateQua+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=dateQua+"-07-01";
				var last=getLastDay(dateQua,9);
				endDate=dateQua+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=dateQua+"-10-01";
				var last=getLastDay(dateQua,12);
				endDate=dateQua+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'Big');
		}else{
			QuarterTotal(getQua(dateQua)[0],getQua(dateQua)[1],'Big');
		}
		
		

	}else{
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=yearT+"-01-01";
				var last=getLastDay(yearT,3);
				endDate=yearT+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=yearT+"-04-01";
				var last=getLastDay(yearT,6);
				endDate=yearT+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=yearT+"-07-01";
				var last=getLastDay(yearT,9);
				endDate=yearT+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=yearT+"-10-01";
				var last=getLastDay(yearT,12);
				endDate=yearT+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'Big');
		}else{
			QuarterTotal(Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1],'Big');
		}

	}
}







function getQuarterCurved(){
	var dateQua=$("#QuarterCurved").val();
	var qua=$("#QuarterCurvedQ").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var yearT=new Date().getFullYear();
	if(dateQua!=null && dateQua!=""){
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=dateQua+"-01-01";
				var last=getLastDay(dateQua,3);
				endDate=dateQua+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=dateQua+"-04-01";
				var last=getLastDay(dateQua,6);
				endDate=dateQua+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=dateQua+"-07-01";
				var last=getLastDay(dateQua,9);
				endDate=dateQua+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=dateQua+"-10-01";
				var last=getLastDay(dateQua,12);
				endDate=dateQua+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'Curved');
		}else{
			QuarterTotal(getQua(dateQua)[0],getQua(dateQua)[1],'Curved');
		}
		
		

	}else{
		if(qua!=null && qua!=""){
			if(qua=="Q1"){
				beginDate=yearT+"-01-01";
				var last=getLastDay(yearT,3);
				endDate=yearT+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=yearT+"-04-01";
				var last=getLastDay(yearT,6);
				endDate=yearT+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=yearT+"-07-01";
				var last=getLastDay(yearT,9);
				endDate=yearT+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=yearT+"-10-01";
				var last=getLastDay(yearT,12);
				endDate=yearT+"-12-"+last;
			}
			QuarterTotal(beginDate,endDate,'Curved');
		}else{
			QuarterTotal(Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1],'Curved');
		}

	}
}





function getHalfTotal(){
	var dateYear=$("#HalfTotal").val();
	var dateYearWhat=$("#HalfTotalWhat").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var  currMonth=d.getMonth()+1;
	if(dateYear!=null && dateYear!=""){
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"Total");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"Total");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"Total");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"Total");
			}
		}

	}else{
		
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"Total");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"Total");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"Total");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"Total");
			}
		}
	}
}







function getHalfUD(){
	var dateYear=$("#HalfUD").val();
	var dateYearWhat=$("#HalfUDWhat").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var  currMonth=d.getMonth()+1;
	if(dateYear!=null && dateYear!=""){
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"UD");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"UD");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"UD");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"UD");
			}
		}

	}else{
		
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"UD");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"UD");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"UD");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"UD");
			}
		}
	}
}





function getHalfXCP(){
	var dateYear=$("#HalfXCP").val();
	var dateYearWhat=$("#HalfXCPWhat").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var  currMonth=d.getMonth()+1;
	if(dateYear!=null && dateYear!=""){
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"XCP");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"XCP");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"XCP");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"XCP");
			}
		}

	}else{
		
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"XCP");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"XCP");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"XCP");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"XCP");
			}
		}
	}
}







function getHalfSmart(){
	var dateYear=$("#HalfSmart").val();
	var dateYearWhat=$("#HalfSmartWhat").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var  currMonth=d.getMonth()+1;
	if(dateYear!=null && dateYear!=""){
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"Smart");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"Smart");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"Smart");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"Smart");
			}
		}

	}else{
		
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"Smart");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"Smart");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"Smart");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"Smart");
			}
		}
	}
}








function getHalfBig(){
	var dateYear=$("#HalfBig").val();
	var dateYearWhat=$("#HalfBigWhat").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var  currMonth=d.getMonth()+1;
	if(dateYear!=null && dateYear!=""){
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"Big");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"Big");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"Big");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"Big");
			}
		}

	}else{
		
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"Big");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"Big");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"Big");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"Big");
			}
		}
	}
}








function getHalfCurved(){
	var dateYear=$("#HalfCurved").val();
	var dateYearWhat=$("#HalfCurvedWhat").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var  currMonth=d.getMonth()+1;
	if(dateYear!=null && dateYear!=""){
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"Curved");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"Curved");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(dateYear,6);
				HalfTotal(dateYear+"-01-01",dateYear+"-06-"+last,"Curved");
			}else{
				last=getLastDay(dateYear,12);
				HalfTotal(dateYear+"-07-01",dateYear+"-12-"+last,"Curved");
			}
		}

	}else{
		
		var last;
		if(dateYearWhat!=null && dateYearWhat!=""){
			if(dateYearWhat=="H1"){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"Curved");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"Curved");
			}
		}else{
			if(currMonth<=6){
				last=getLastDay(d.getFullYear(),6);
				HalfTotal(d.getFullYear()+"-01-01",d.getFullYear()+"-06-"+last,"Curved");
			}else{
				last=getLastDay(d.getFullYear(),12);
				HalfTotal(d.getFullYear()+"-07-01",d.getFullYear()+"-12-"+last,"Curved");
			}
		}
	}
}








function getYearTotal(){
	var dateYear=$("#YearTotal").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			YearTotal( Common_date.getTheFirstDay()[0],str,"Total");
		}else{
			YearTotal( dateYear+"-01-01",dateYear+"-12-"+last,"Total");

		}

	}else{
		YearTotal( Common_date.getTheFirstDay()[0],str,"Total");
	}
}




function getYearUD(){
	var dateYear=$("#YearUD").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			YearTotal( Common_date.getTheFirstDay()[0],str,"UD");
		}else{
			YearTotal( dateYear+"-01-01",dateYear+"-12-"+last,"UD");

		}

	}else{
		YearTotal( Common_date.getTheFirstDay()[0],str,"UD");
	}
}

function getYearXCP(){
	var dateYear=$("#YearXCP").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			YearTotal( Common_date.getTheFirstDay()[0],str,"XCP");
		}else{
			YearTotal( dateYear+"-01-01",dateYear+"-12-"+last,"XCP");

		}

	}else{
		YearTotal( Common_date.getTheFirstDay()[0],str,"XCP");
	}
}




function getYearSmart(){
	var dateYear=$("#YearSmart").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			YearTotal( Common_date.getTheFirstDay()[0],str,"Smart");
		}else{
			YearTotal( dateYear+"-01-01",dateYear+"-12-"+last,"Smart");

		}

	}else{
		YearTotal( Common_date.getTheFirstDay()[0],str,"Smart");
	}
}




function getYearBig(){
	var dateYear=$("#YearBig").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			YearTotal( Common_date.getTheFirstDay()[0],str,"Big");
		}else{
			YearTotal( dateYear+"-01-01",dateYear+"-12-"+last,"Big");

		}

	}else{
		YearTotal( Common_date.getTheFirstDay()[0],str,"Big");
	}
}





function getYearCurved(){
	var dateYear=$("#YearCurved").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();

	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
		if(d.getFullYear()==dateYear){
			YearTotal( Common_date.getTheFirstDay()[0],str,"Curved");
		}else{
			YearTotal( dateYear+"-01-01",dateYear+"-12-"+last,"Curved");

		}

	}else{
		YearTotal( Common_date.getTheFirstDay()[0],str,"Curved");
	}
}





function getQua(dateQua){
	var month = new Date().getMonth() + 1;
	if(month >= 1 && month <= 3){
		return [dateQua+"-01-01",dateQua+"-03-31"];
	}else if(month >= 4 && month <= 6){
		return [dateQua+"-04-01",dateQua+"-06-31"];
	}else if(month >= 7 && month <= 9){
		return [dateQua+"-07-01",dateQua+"-09-31"];
	}else if(month >= 10 && month <= 12){
		return [dateQua+"-10-01",dateQua+"-12-31"];
	}
}