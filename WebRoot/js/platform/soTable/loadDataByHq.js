
function loadYearTotal(obj,beginDate,endDate,what,head){//加载数据
	loadYearTotalData(obj,beginDate,endDate,what,head);
};




function loadYearTotalHead(obj,beginDate,endDate,what,head){//加载表头
	var beg=beginDate.split("-");
	var html="";
	html+='<thead class="bluegrey">';
	html+='<tr>';
	html+='	<th rowspan="2">'+beg[0]+' Year '+head+' Sell-Out</th>';
	html+='	<th colspan="3">'+beg[0]+' Year '+head+' Sell-Out</th>';
	html+='	<th colspan="3">'+beg[0]+' H1 '+head+' Sell-Out</th>';
	html+='	<th colspan="3">'+beg[0]+' H2 '+head+' Sell-Out</th>';
	html+='</tr>';
	html+='<tr>';
	html+='<th>Target</th>';	
	html+='<th>Achieved</th>';	
	html+='<th>Rate%</th>';
	html+='<th>Target</th>';	
	html+='<th>Achieved</th>';
	html+='<th>Rate%</th>';
	html+='<th>Target</th>';
	html+='<th>Achieved</th>';
	html+='<th>Rate%</th>';
	html+='</tr>';
	html+='</thead>';
	return html;
};

function loadYearTotalData(obj,beginDate,endDate,what,head){//加载数据
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var beg=beginDate.split("-");
	var tYear=d.getFullYear();
	var tMonth=(d.getMonth()+1);
	var by = obj.data;
	var html="";
	html+='<tbody>';
		
	var center="";
	if(by.length >=1){
		center=by[0].center;
	}
	
	var saleQtyYear=0;
	var saleQtyH1=0;
	var saleQtyH2=0;
	
	var TargetQtyYear=0;
	var TargetQtyH1=0;
	var TargetQtyH2=0;
	
	
	
	var saleQtyYearOBC=0;
	var saleQtyH1OBC=0;
	var saleQtyH2OBC=0;
	
	var TargetQtyYearOBC=0;
	var TargetQtyH1OBC=0;
	var TargetQtyH2OBC=0;
	
	var counHtml="";
	for ( var i = 0; i < by.length; i++) {
		if(center!=null && center!="" && center==by[i].center){
			saleQtyYear+=by[i].saleQtyYear;
			saleQtyH1+=by[i].saleQtyH1;
			saleQtyH2+=by[i].saleQtyH2;
			
			TargetQtyYear+=by[i].TargetQtyYear;
			TargetQtyH1+=by[i].TargetQtyH1;
			TargetQtyH2+=by[i].TargetQtyH2;
			
			
			saleQtyYearOBC+=by[i].saleQtyYear;
			saleQtyH1OBC+=by[i].saleQtyH1;
			saleQtyH2OBC+=by[i].saleQtyH2;
			
			TargetQtyYearOBC+=by[i].TargetQtyYear;
			TargetQtyH1OBC+=by[i].TargetQtyH1;
			TargetQtyH2OBC+=by[i].TargetQtyH2;
			
			
			counHtml+='<tr>';
			counHtml+='<td >'+isStringNullAvaliable(by[i].country)+'</td>';
			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQtyYear)+'</td>';
			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyYear)+'</td>';
			counHtml+=TheBackgroundColor(isStringNullAvaliableNum(by[i].achYear));
			
			
			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQtyH1)+'</td>';
			if(beg[0]==tYear && tMonth<=6 ){
				counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			}else{
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			}
			counHtml+='<td >'+isStringNullAvaliableNum(by[i].achH1)+'</td>';

			
			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQtyH2)+'</td>';
			
			if(beg[0]==tYear && tMonth>6 ){
				counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			}else{
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			}
			
			counHtml+='<td  >'+isStringNullAvaliableNum(by[i].achH2)+'</td>';
			
			
			if(by.length- i == 1){
				html+='<tr>';
				html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
				html+='<th  style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyYear)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyYear)+'</th>';
				if(TargetQtyYear==0 || TargetQtyYear=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyYear/TargetQtyYear*100);
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH1)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1)+'</th>';
				
				
				if(TargetQtyH1==0 || TargetQtyH1=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyH1/TargetQtyH1*100);
				}
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH2)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2)+'</th>';
				if(TargetQtyH2==0 || TargetQtyH2=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyH2/TargetQtyH2*100);
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
			    html+=counHtml;
			    
			    
			    html+='<tr>';
				html+='<th>OBC</th>';
				html+='<th  style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyYearOBC)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyYearOBC)+'</th>';
				if(TargetQtyYearOBC==0 || TargetQtyYearOBC=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyYearOBC/TargetQtyYearOBC*100);
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH1OBC)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1OBC)+'</th>';
				
				
				if(TargetQtyH1OBC==0 || TargetQtyH1OBC=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyH1OBC/TargetQtyH1OBC*100);
				}
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH2OBC)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2OBC)+'</th>';
				if(TargetQtyH2OBC==0 || TargetQtyH2OBC=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyH2OBC/TargetQtyH2OBC*100);
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
			}
			
		}else{
			var ach=0;
			html+='<tr>';
			html+='<th>'+isStringNullAvaliable(by[i-1].center)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyYear)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyYear)+'</th>';
			if(TargetQtyYear==0 || TargetQtyYear=="0"){
				ach=100;
			}else{
				ach=Math.round(saleQtyYear/TargetQtyYear*100);
			}
			
			html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH1)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1)+'</th>';
			
			
			if(TargetQtyH1==0 || TargetQtyH1=="0"){
				ach=100;
			}else{
				ach=Math.round(saleQtyH1/TargetQtyH1*100);
			}
			html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
			
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH2)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2)+'</th>';
			if(TargetQtyH2==0 || TargetQtyH2=="0"){
				ach=100;
			}else{
				ach=Math.round(saleQtyH2/TargetQtyH2*100);
			}
			
			html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
		    html+=counHtml;
		    counHtml="";
			center =by[i].center;
			saleQtyYear=0;
			saleQtyH1=0;
			saleQtyH2=0;
			TargetQtyYear=0;
			TargetQtyH1=0;
			TargetQtyH2=0;
			
			i--;
		}
		
		
	}
	
	
	html+='</tbody>';
	
	if(what=="Total"){
		$("#YearTotalData").html("");
		if(by.length >0){
			$("#YearTotalData").html(loadYearTotalHead(obj,beginDate,endDate,what,head)+html);
		}
	}else if(what=="UD"){
		$("#YearUDData").html("");
		if(by.length >0){
			$("#YearUDData").html(loadYearTotalHead(obj,beginDate,endDate,what,head)+html);
		}
	
	}else if(what=="XCP"){
		$("#YearXCPData").html("");
		if(by.length >0){
			$("#YearXCPData").html(loadYearTotalHead(obj,beginDate,endDate,what,head)+html);
		}

	}else if(what=="Smart"){
		$("#YearSmartData").html("");
		if(by.length >0){
			$("#YearSmartData").html(loadYearTotalHead(obj,beginDate,endDate,what,head)+html);
		}

		
	}else if(what=="Big"){
		$("#YearBigData").html("");

		if(by.length >0){
			$("#YearBigData").html(loadYearTotalHead(obj,beginDate,endDate,what,head)+html);
		}
		
	}else if(what=="Curved"){
		$("#YearCurvedData").html("");
		if(by.length >0){
			$("#YearCurvedData").html(loadYearTotalHead(obj,beginDate,endDate,what,head)+html);
		}
		
	}
	
};


































//===================================half=======================================================================





function loadHalfTotal(obj,beginDate,endDate,what,head){//加载数据
	loadHalfTotalData(obj,beginDate,endDate,what,head);
};




function loadHalfTotalHead(obj,beginDate,endDate,what,head){//加载表头

	var beg=beginDate.split("-");
	var headBig;
	var headBig1;
	var headBig2;
	if(beg[1]=="1" || beg[1]==1){
		headBig=" H1 "+head;
		headBig1=" Q1 "+head;
		headBig2=" Q2 "+head;
	}else{
		headBig=" H2 "+head;
		headBig1=" Q3 "+head;
		headBig2=" Q4 "+head;
	}
	var html="";
	html+='<thead class="bluegrey">';
	html+='<tr>';
	html+='	<th rowspan="2">'+beg[0]+headBig+' Sell-Out</th>';
	html+='	<th colspan="3">'+beg[0]+headBig+' Sell-Out</th>';
	html+='	<th colspan="3">'+beg[0]+headBig1+' Sell-Out</th>';
	html+='	<th colspan="3">'+beg[0]+headBig2+' Sell-Out</th>';
	html+='</tr>';
	html+='<tr>';
	html+='<th>Target</th>';	
	html+='<th>Achieved</th>';	
	html+='<th>Rate%</th>';
	html+='<th>Target</th>';	
	html+='<th>Achieved</th>';
	html+='<th>Rate%</th>';
	html+='<th>Target</th>';
	html+='<th>Achieved</th>';
	html+='<th>Rate%</th>';
	html+='</tr>';
	html+='</thead>';
	return html;
};

function loadHalfTotalData(obj,beginDate,endDate,what,head){//加载数据
	var d = new Date();
	var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var beg=beginDate.split("-");
	var tYear=d.getFullYear();
	var tMonth=(d.getMonth()+1);
	
	var by = obj.data;
	var html="";
	html+='<tbody>';
		
	var center="";
	if(by.length >=1){
		center=by[0].center;
	}
	
	var saleQtyHalf=0;
	var saleQtyH1=0;
	var saleQtyH2=0;
	
	var TargetQtyHalf=0;
	var TargetQtyH1=0;
	var TargetQtyH2=0;
	
	
	var saleQtyHalfOBC=0;
	var saleQtyH1OBC=0;
	var saleQtyH2OBC=0;
	
	var TargetQtyHalfOBC=0;
	var TargetQtyH1OBC=0;
	var TargetQtyH2OBC=0;
	
	var counHtml="";
	for ( var i = 0; i < by.length; i++) {
		if(center!=null && center!="" && center==by[i].center){
			saleQtyHalf+=by[i].saleQtyYear;
			saleQtyH1+=by[i].saleQtyH1;
			saleQtyH2+=by[i].saleQtyH2;
			
			TargetQtyHalf+=by[i].TargetQtyYear;
			TargetQtyH1+=by[i].TargetQtyH1;
			TargetQtyH2+=by[i].TargetQtyH2;
			
			
			
			
			saleQtyHalfOBC+=by[i].saleQtyYear;
			saleQtyH1OBC+=by[i].saleQtyH1;
			saleQtyH2OBC+=by[i].saleQtyH2;
			
			TargetQtyHalfOBC+=by[i].TargetQtyYear;
			TargetQtyH1OBC+=by[i].TargetQtyH1;
			TargetQtyH2OBC+=by[i].TargetQtyH2;
			
			
			counHtml+='<tr>';
			counHtml+='<td >'+isStringNullAvaliable(by[i].country)+'</td>';
			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQtyYear)+'</td>';
			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyYear)+'</td>';
			counHtml+=TheBackgroundColor(isStringNullAvaliableNum(by[i].achYear));

			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQtyH1)+'</td>';

			if(beg[0]==tYear && ( (beg[1]<=tMonth && beg[1]<=3)  || (    9>=tMonth>=7  &&  9>=parseInt(beg[2]>=7))   ) ){
				counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			}else{
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			}
			
			//counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			counHtml+='<td >'+isStringNullAvaliableNum(by[i].achH1)+'</td>';
			
			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQtyH2)+'</td>';
			if(beg[0]==tYear && ((6>=tMonth>=4 &&  6>=beg[1]>=4)  || (tMonth>=10  && beg[1]>=10)) ){
				counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			}else{
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			}
			//counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			counHtml+='<td  >'+isStringNullAvaliableNum(by[i].achH2)+'</td>';
			
			
			if(by.length- i == 1){
			
			    
			    
			    
			    html+='<tr>';
				html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
				html+='<th  style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyHalf)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyHalf)+'</th>';
				if(TargetQtyHalf==0 || TargetQtyHalf=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyHalf/TargetQtyHalf*100);	
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH1)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1)+'</th>';
				
				if(TargetQtyH1==0 || TargetQtyH1=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyH1/TargetQtyH1*100);
				}
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH2)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2)+'</th>';
				if(TargetQtyH2==0 || TargetQtyH2=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyH2/TargetQtyH2*100);
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				
				
				
				
				
				
				
				html+=counHtml;
				
				
				
				html+='<tr>';
				html+='<th>OBC</th>';
				html+='<th  style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyHalfOBC)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyHalfOBC)+'</th>';
				if(TargetQtyHalfOBC==0 || TargetQtyHalfOBC=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyHalfOBC/TargetQtyHalfOBC*100);	
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH1OBC)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1OBC)+'</th>';
				
				if(TargetQtyH1OBC==0 || TargetQtyH1OBC=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyH1OBC/TargetQtyH1OBC*100);
				}
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH2OBC)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2OBC)+'</th>';
				if(TargetQtyH2OBC==0 || TargetQtyH2OBC=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQtyH2OBC/TargetQtyH2OBC*100);
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
			}
			
		}else{
			var ach=0;
			html+='<tr>';
			html+='<th>'+isStringNullAvaliable(by[i-1].center)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyHalf)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyHalf)+'</th>';
			if(TargetQtyHalf==0 || TargetQtyHalf=="0"){
				ach=100;
			}else{
				ach=Math.round(saleQtyHalf/TargetQtyHalf*100);	
			}
			
			html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH1)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1)+'</th>';
			
			if(TargetQtyH1==0 || TargetQtyH1=="0"){
				ach=100;
			}else{
				ach=Math.round(saleQtyH1/TargetQtyH1*100);
			}
			html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
			
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyH2)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2)+'</th>';
			if(TargetQtyH2==0 || TargetQtyH2=="0"){
				ach=100;
			}else{
				ach=Math.round(saleQtyH2/TargetQtyH2*100);
			}
			
			html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
		    html+=counHtml;
		    counHtml="";
			center =by[i].center;
			saleQtyHalf=0;
			saleQtyH1=0;
			saleQtyH2=0;
			TargetQtyHalf=0;
			TargetQtyH1=0;
			TargetQtyH2=0;
			
			i--;
		}
		
		
	}
	
	
	html+='</tbody>';
	
	if(what=="Total"){
		$("#HalfTotalData").html("");
		if(by.length >0){
			$("#HalfTotalData").html(loadHalfTotalHead(obj,beginDate,endDate,what,head)+html);
		}
	}else if(what=="UD"){
		$("#HalfUDData").html("");
		if(by.length >0){
			$("#HalfUDData").html(loadHalfTotalHead(obj,beginDate,endDate,what,head)+html);
		}
	
	}else if(what=="XCP"){
		$("#HalfXCPData").html("");
		if(by.length >0){
			$("#HalfXCPData").html(loadHalfTotalHead(obj,beginDate,endDate,what,head)+html);
		}

	}else if(what=="Smart"){
		$("#HalfSmartData").html("");
		if(by.length >0){
			$("#HalfSmartData").html(loadHalfTotalHead(obj,beginDate,endDate,what,head)+html);
		}

		
	}else if(what=="Big"){
		$("#HalfBigData").html("");

		if(by.length >0){
			$("#HalfBigData").html(loadHalfTotalHead(obj,beginDate,endDate,what,head)+html);
		}
		
	}else if(what=="Curved"){
		$("#HalfCurvedData").html("");
		if(by.length >0){
			$("#HalfCurvedData").html(loadHalfTotalHead(obj,beginDate,endDate,what,head)+html);
		}
		
	}
};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	//===================================Quarter=======================================================================





	function loadQuarterTotal(obj,beginDate,endDate,what,head){//加载数据
		loadQuarterTotalData(obj,beginDate,endDate,what,head);
	};




	function loadQuarterTotalHead(obj,beginDate,endDate,what,head){//加载表头

		var beg=beginDate.split("-");
		var html="";
		html+='<thead class="bluegrey">';
		html+='<tr>';
		
		if(beg[1]=="1" || beg[1]==1){
			html+='	<th rowspan="2">'+beg[0]+' Q1 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+' Q1 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'01 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'02 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'03 '+head+' Sell-Out</th>';
		}else if(beg[1]=="4" || beg[1]==4){
			html+='	<th rowspan="2">'+beg[0]+' Q2 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+' Q2 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'04 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'05 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'06 '+head+' Sell-Out</th>';
		}else if(beg[1]=="7" || beg[1]==7){
			html+='	<th rowspan="2">'+beg[0]+' Q3 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+' Q3 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'07 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'08 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'09 '+head+' Sell-Out</th>';
		}else if(beg[1]=="10" || beg[1]==10){
			html+='	<th rowspan="2">'+beg[0]+' Q4 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+' Q4 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'10 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'11 '+head+' Sell-Out</th>';
			html+='	<th colspan="3">'+beg[0]+'12 '+head+' Sell-Out</th>';
		}
		
		
		html+='</tr>';
		html+='<tr>';
		html+='<th>Target</th>';	
		html+='<th>Achieved</th>';	
		html+='<th>Rate%</th>';
		html+='<th>Target</th>';	
		html+='<th>Achieved</th>';
		html+='<th>Rate%</th>';
		html+='<th>Target</th>';
		html+='<th>Achieved</th>';
		html+='<th>Rate%</th>';
		html+='<th>Target</th>';
		html+='<th>Achieved</th>';
		html+='<th>Rate%</th>';
		html+='</tr>';
		html+='</thead>';
		return html;
	};

	function loadQuarterTotalData(obj,beginDate,endDate,what,head){//加载数据
		var d = new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
		var beg=beginDate.split("-");
		var end=endDate.split("-");
		var tYear=d.getFullYear();
		var tMonth=(d.getMonth()+1);
		var by = obj.data;
		var html="";
		html+='<tbody>';
			
		var center="";
		if(by.length >=1){
			center=by[0].center;
		}
		
		var saleQty=0;
		var saleQty1=0;
		var saleQty2=0;
		var saleQty3=0;
		
		var TargetQty=0;
		var TargetQty1=0;
		var TargetQty2=0;
		var TargetQty3=0;
		
		
		
		var saleQtyOBC=0;
		var saleQty1OBC=0;
		var saleQty2OBC=0;
		var saleQty3OBC=0;
		
		var TargetQtyOBC=0;
		var TargetQty1OBC=0;
		var TargetQty2OBC=0;
		var TargetQty3OBC=0;
		
		var counHtml="";
		for ( var i = 0; i < by.length; i++) {
			if(center!=null && center!="" && center==by[i].center){
				saleQty+=by[i].saleQty;
				saleQty1+=by[i].saleQty1;
				saleQty2+=by[i].saleQty2;
				saleQty3+=by[i].saleQty3;
				
				TargetQty+=by[i].TargetQty;
				TargetQty1+=by[i].TargetQty1;
				TargetQty2+=by[i].TargetQty2;
				TargetQty3+=by[i].TargetQty3;
				
				
				
				saleQtyOBC+=by[i].saleQty;
				saleQty1OBC+=by[i].saleQty1;
				saleQty2OBC+=by[i].saleQty2;
				saleQty3OBC+=by[i].saleQty3;
				
				TargetQtyOBC+=by[i].TargetQty;
				TargetQty1OBC+=by[i].TargetQty1;
				TargetQty2OBC+=by[i].TargetQty2;
				TargetQty3OBC+=by[i].TargetQty3;
				
				counHtml+='<tr>';
				counHtml+='<td >'+isStringNullAvaliable(by[i].country)+'</td>';
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQty)+'</td>';
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty)+'</td>';
				counHtml+=TheBackgroundColor(isStringNullAvaliableNum(by[i].ach));

				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQty1)+'</td>';
				if(beg[0]==tYear && (tMonth==1 || tMonth==4 || tMonth==7 || tMonth==10) && tMonth==beg[1]){
					counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty1)+'</td>';

				}else{
					counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty1)+'</td>';
				}
				counHtml+='<td  >'+isStringNullAvaliableNum(by[i].ach1)+'</td>';
				
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQty2)+'</td>';
				
				if(beg[0]==tYear && (tMonth==2 || tMonth==5 || tMonth==8 || tMonth==11) && ((parseInt(beg[1])+1)==2 || (parseInt(beg[1])+1)==5 || (parseInt(beg[1])+1)==8 || (parseInt(beg[1])+1)==11)){
					counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty2)+'</td>';

				}else{
					counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty2)+'</td>';
				}
				
				counHtml+='<td  >'+isStringNullAvaliableNum(by[i].ach2)+'</td>';
				
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].TargetQty3)+'</td>';
				
				if(beg[0]==tYear && (tMonth==3 || tMonth==6 || tMonth==9 || tMonth==12) && (end[1]==3 || end[1]==6 || end[1]==9 || end[1]==12)){
					counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty3)+'</td>';

				}else{
					counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty3)+'</td>';
				}
				counHtml+='<td  >'+isStringNullAvaliableNum(by[i].ach3)+'</td>';
			
				
				
				if(by.length- i == 1){
					html+='<tr>';
					html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
					html+='<th  style="text-align:right";>'+isStringNullAvaliableNum(TargetQty)+'</th>';
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty)+'</th>';
					if(TargetQty==0 || TargetQty=="0"){
						ach=100;
					}else{
						ach=Math.round(saleQty/TargetQty*100);
					}
					
					html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty1)+'</th>';
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty1)+'</th>';
					if(TargetQty1==0 || TargetQty1=="0"){
						ach=100;
					}else{
						ach=Math.round(saleQty1/TargetQty1*100);
					}
					
					
					html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty2)+'</th>';
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty2)+'</th>';
					if(TargetQty2==0 || TargetQty2=="0"){
						ach=100;
					}else{
						ach=Math.round(saleQty2/TargetQty2*100);
					}
					
					html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				   
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty3)+'</th>';
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty3)+'</th>';
					if(TargetQty3==0 || TargetQty3=="0"){
						ach=100;
					}else{
						ach=Math.round(saleQty3/TargetQty3*100);
					}
				
					html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				   
					
					html+=counHtml;
					
					
					html+='<tr>';
					html+='<th>OBC</th>';
					html+='<th  style="text-align:right";>'+isStringNullAvaliableNum(TargetQtyOBC)+'</th>';
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyOBC)+'</th>';
					if(TargetQtyOBC==0 || TargetQtyOBC=="0"){
						ach=100;
					}else{
						ach=Math.round(saleQtyOBC/TargetQtyOBC*100);
					}
					
					html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty1OBC)+'</th>';
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty1OBC)+'</th>';
					if(TargetQty1OBC==0 || TargetQty1OBC=="0"){
						ach=100;
					}else{
						ach=Math.round(saleQty1OBC/TargetQty1OBC*100);
					}
					
					
					html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty2OBC)+'</th>';
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty2OBC)+'</th>';
					if(TargetQty2OBC==0 || TargetQty2OBC=="0"){
						ach=100;
					}else{
						ach=Math.round(saleQty2OBC/TargetQty2OBC*100);
					}
					
					html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				   
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty3OBC)+'</th>';
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty3OBC)+'</th>';
					if(TargetQty3OBC==0 || TargetQty3OBC=="0"){
						ach=100;
					}else{
						ach=Math.round(saleQty3OBC/TargetQty3OBC*100);
					}
				
					html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				}
				
			}else{
				var ach=0;
				html+='<tr>';
				html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
				html+='<th  style="text-align:right";>'+isStringNullAvaliableNum(TargetQty)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty)+'</th>';
				if(TargetQty==0 || TargetQty=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQty/TargetQty*100);
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';

				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty1)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty1)+'</th>';
				if(TargetQty1==0 || TargetQty1=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQty1/TargetQty1*100);
				}
				
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty2)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty2)+'</th>';
				if(TargetQty2==0 || TargetQty2=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQty2/TargetQty2*100);
				}
				
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
			   
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(TargetQty3)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty3)+'</th>';
				if(TargetQty3==0 || TargetQty3=="0"){
					ach=100;
				}else{
					ach=Math.round(saleQty3/TargetQty3*100);
				}
				html+='<th >'+isStringNullAvaliableNum(ach)+'%</th>';
			   
				
			    html+=counHtml;
			    counHtml="";
				center =by[i].center;
				saleQty=0;
				saleQty1=0;
				saleQty2=0;
				saleQty3=0;
				
				TargetQty=0;
				TargetQty1=0;
				TargetQty2=0;
				TargetQty3=0;
				
				i--;
			}
			
			
		}
		
		
		html+='</tbody>';
		
		if(what=="Total"){
			$("#QuarterTotalData").html("");
			if(by.length >0){
				$("#QuarterTotalData").html(loadQuarterTotalHead(obj,beginDate,endDate,what,head)+html);
			}
		}else if(what=="UD"){
			$("#QuarterUDData").html("");
			if(by.length >0){
				$("#QuarterUDData").html(loadQuarterTotalHead(obj,beginDate,endDate,what,head)+html);
			}
		
		}else if(what=="XCP"){
			$("#QuarterXCPData").html("");
			if(by.length >0){
				$("#QuarterXCPData").html(loadQuarterTotalHead(obj,beginDate,endDate,what,head)+html);
			}

		}else if(what=="Smart"){
			$("#QuarterSmartData").html("");
			if(by.length >0){
				$("#QuarterSmartData").html(loadQuarterTotalHead(obj,beginDate,endDate,what,head)+html);
			}

			
		}else if(what=="Big"){
			$("#QuarterBigData").html("");

			if(by.length >0){
				$("#QuarterBigData").html(loadQuarterTotalHead(obj,beginDate,endDate,what,head)+html);
			}
			
		}else if(what=="Curved"){
			$("#QuarterCurvedData").html("");
			if(by.length >0){
				$("#QuarterCurvedData").html(loadQuarterTotalHead(obj,beginDate,endDate,what,head)+html);
			}
			
		}
		
	};
	
	
	
	
	
	
	function TheBackgroundColor(num){//达成率背景加载颜色
		if(typeof(num) == "string"){
			if(num.indexOf("%")>=0){
				num=num.substr(0, num.length-1);
			}
			
			if(typeof(num) == "string"){
				num = num * 1;
			}
		}
		
		if(num >100){
			return "<td  style='background-color: green;color:#fff;text-align:center;'>" + num + "%</td>";
		}else{
			return "<td  style='text-align:center;'>" + num + "%</td>";
		}
	};
