
function loadYearTotal(obj,beginDate,endDate,what,head){//加载数据
	loadYearTotalData(obj,beginDate,endDate,what,head);
};




function loadYearTotalHead(obj,beginDate,endDate,what,head){//加载表头
	var beg=beginDate.split("-");
	var html="";
	html+='<thead class="bluegrey">';
	html+='<tr>';
	html+='	<th rowspan="2">'+beg[0]+' Year '+head+' Sell-Out Growth Rate</th>';
	html+='	<th >'+beg[0]+' Year '+head+' Sell-Out </th>';
	html+='	<th colspan="2">'+beg[0]+' H1 '+head+' Sell-Out </th>';
	html+='	<th colspan="2">'+beg[0]+' H2 '+head+' Sell-Out </th>';
	html+='</tr>';
	html+='<tr>';
	html+='<th>Achieved</th>';	

	html+='<th>Achieved</th>';
	html+='<th>Growth Rate%</th>';  
	
	html+='<th>Achieved</th>';
	html+='<th>Growth Rate%</th>';  
	
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
	
	
	var saleQtyYearOBC=0;
	var saleQtyH1OBC=0;
	var saleQtyH2OBC=0;
	
	var counHtml="";
	for ( var i = 0; i < by.length; i++) {
		if(center!=null && center!="" && center==by[i].center){
			saleQtyYear+=by[i].saleQtyYear;
			saleQtyH1+=by[i].saleQtyH1;
			saleQtyH2+=by[i].saleQtyH2;
			
			saleQtyYearOBC+=by[i].saleQtyYear;
			saleQtyH1OBC+=by[i].saleQtyH1;
			saleQtyH2OBC+=by[i].saleQtyH2;

			counHtml+='<tr>';
			counHtml+='<td >'+isStringNullAvaliable(by[i].country)+'</td>';
			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyYear)+'</td>';
			
			if(beg[0]==tYear && tMonth<=6 ){
				counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			}else{
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			}
			
			counHtml+='<td >'+by[i].gro1+'</td>';


			
			
			if(beg[0]==tYear && tMonth>6 ){
				counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			}else{
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			}
			
			counHtml+=growthRateHQ(by[i].gro2);
			counHtml+='</tr>';
			
			if(by.length- i == 1){
				html+='<tr>';
				html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyYear)+'</th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1)+'</th>';
				html+='<th style="text-align:right";> </th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2)+'</th>';
				if(saleQtyH2==0){
					html+=growthRateHQ('-100%');

				}else if(saleQtyH1==0){
					html+=growthRateHQ('100%');

				}else{
					var gro=(saleQtyH2-saleQtyH1)/saleQtyH1;
					html+=growthRateHQ(Math.round(gro*100)+'%');

				}
				html+='</tr>';
			   
				html+=counHtml;
				
				
				html+='<tr>';
				html+='<th>OBC</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyYearOBC)+'</th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1OBC)+'</th>';
				html+='<th style="text-align:right";> </th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2OBC)+'</th>';
				if(saleQtyH2OBC==0){
					html+=growthRateHQ('-100%');

				}else if(saleQtyH1OBC==0){
					html+=growthRateHQ('100%');

				}else{
					var gro=(saleQtyH2OBC-saleQtyH1OBC)/saleQtyH1OBC;
					html+=growthRateHQ(Math.round(gro*100)+'%');

				}
				
				html+='</tr>';
				
			}
			
		}else{
			var ach=0;
			html+='<tr>';

			html+='<th>'+isStringNullAvaliable(by[i-1].center)+'</th>';
		
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyYear)+'</th>';
			

			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1)+'</th>';
			html+='<th style="text-align:right";> </th>';
			
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2)+'</th>';

			if(saleQtyH2==0){
				html+=growthRateHQ('-100%');

			}else if(saleQtyH1==0){
				html+=growthRateHQ('100%');

			}else{
				var gro=(saleQtyH2-saleQtyH1)/saleQtyH1;
				html+=growthRateHQ(Math.round(gro*100)+'%');

			}
			
			html+='</tr>';
			
			html+=counHtml;
		    
		    counHtml="";
			center =by[i].center;
			saleQtyYear=0;
			saleQtyH1=0;
			saleQtyH2=0;
			
			
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
	html+='	<th rowspan="2">'+beg[0]+headBig+' Sell-Out Growth Rate</th>';
	html+='	<th  >'+beg[0]+headBig+' Sell-Out </th>';
	html+='	<th colspan="2">'+beg[0]+headBig1+' Sell-Out </th>';
	html+='	<th colspan="2">'+beg[0]+headBig2+' Sell-Out </th>';
	html+='</tr>';
	html+='<tr>';
	
	html+='<th>Achieved</th>';	

	html+='<th>Achieved</th>';
	html+='<th>Growth Rate%</th>';  
	
	html+='<th>Achieved</th>';
	html+='<th>Growth Rate%</th>';  
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
	var  saleQtylast=0;
	
	var saleQtyHalfOBC=0;
	var saleQtyH1OBC=0;
	var saleQtyH2OBC=0;
	var  saleQtylastOBC=0;
	
	var counHtml="";
	for ( var i = 0; i < by.length; i++) {
		if(center!=null && center!="" && center==by[i].center){
			saleQtyHalf+=by[i].saleQtyYear;
			saleQtyH1+=by[i].saleQtyH1;
			saleQtyH2+=by[i].saleQtyH2;
			saleQtylast+=by[i].saleQtylast;
			
			saleQtyHalfOBC+=by[i].saleQtyYear;
			saleQtyH1OBC+=by[i].saleQtyH1;
			saleQtyH2OBC+=by[i].saleQtyH2;
			saleQtylastOBC+=by[i].saleQtylast;

			
			counHtml+='<tr>';
			counHtml+='<td >'+isStringNullAvaliable(by[i].country)+'</td>';

			counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyYear)+'</td>';
			


			if(beg[0]==tYear && ( (beg[1]<=tMonth && beg[1]<=3)  || (    9>=tMonth>=7  &&  9>=parseInt(beg[2]>=7))   ) ){
				counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			}else{
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH1)+'</td>';
			}
			

			if(beg[1]==1){
				counHtml+='<td > </td>';
			}else{
				counHtml+=growthRateHQ(by[i].gro1);

			}
			
			if(beg[0]==tYear && ((6>=tMonth>=4 &&  6>=beg[1]>=4)  || (tMonth>=10  && beg[1]>=10)) ){
				counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			}else{
				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQtyH2)+'</td>';
			}
		
			counHtml+=growthRateHQ(by[i].gro2);
			
			
			if(by.length- i == 1){
				html+='<tr>';
				html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyHalf)+'</th>';
			
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1)+'</th>';
				
				if(beg[1]==1){
					html+='<th > </th>';
				}else{
					if(saleQtyH1==0){
						html+=growthRateHQ('-100%');

					}else if(saleQtylast==0){
						html+=growthRateHQ('100%');

					}else{
						var gro=(saleQtyH1-saleQtylast)/saleQtylast;
						html+=growthRateHQ(Math.round(gro*100)+'%');
					}
					
				}
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2)+'</th>';
				
				
				
				if(saleQtyH1==0){
					html+=growthRateHQ('100%');

				}else if(saleQtyH2==0){
					html+=growthRateHQ('100%');

				}else{
					var gro2=(saleQtyH2-saleQtyH1)/saleQtyH1;
					html+=growthRateHQ(Math.round(gro2*100)+'%');
				}
				
				
				
				
				
				html+='</tr>';
				html+=counHtml;
				
				
				html+='<tr>';
				html+='<th>OBC</th>';
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyHalfOBC)+'</th>';
			
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1OBC)+'</th>';
				
				if(beg[1]==1){
					html+='<th > </th>';
				}else{
					if(saleQtyH1OBC==0){
						html+=growthRateHQ('-100%');

					}else if(saleQtylastOBC==0){
						html+=growthRateHQ('100%');

					}else{
						var gro=(saleQtyH1OBC-saleQtylastOBC)/saleQtylastOBC;
						html+=growthRateHQ(Math.round(gro*100)+'%');
					}
					
				}
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2OBC)+'</th>';
				
				
				
				if(saleQtyH1OBC==0){
					html+=growthRateHQ('100%');

				}else if(saleQtyH2OBC==0){
					html+=growthRateHQ('100%');

				}else{
					var gro2=(saleQtyH2OBC-saleQtyH1OBC)/saleQtyH1OBC;
					html+=growthRateHQ(Math.round(gro2*100)+'%');
				}
				
				
				
				
				
				html+='</tr>';
			}
			
		}else{
			var ach=0;
			html+='<tr>';
			html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyHalf)+'</th>';
		
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH1)+'</th>';
			
			
			if(beg[1]==1){
				html+='<th > </th>';
			}else{
				if(saleQtyH1==0){
					html+=growthRateHQ('-100%');

				}else if(saleQtylast==0){
					html+=growthRateHQ('100%');

				}else{
					var gro=(saleQtyH1-saleQtylast)/saleQtylast;
					html+=growthRateHQ(Math.round(gro*100)+'%');
				}
				
			}
			
			html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyH2)+'</th>';
			
			
			
			if(saleQtyH1==0){
				html+=growthRateHQ('100%');

			}else if(saleQtyH2==0){
				html+=growthRateHQ('100%');

			}else{
				var gro2=(saleQtyH2-saleQtyH1)/saleQtyH1;
				html+=growthRateHQ(Math.round(gro2*100)+'%');
			}
			
			
			
			
			html+='</tr>';
			html+=counHtml;
			
		    counHtml="";
			center =by[i].center;
			saleQtyHalf=0;
			saleQtyH1=0;
			saleQtyH2=0;
			saleQtylast=0;
			
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
			html+='	<th rowspan="2">'+beg[0]+' Q1 '+head+' Sell-Out Growth Rate</th>';
			html+='	<th >'+beg[0]+' Q1 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'01 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'02 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'03 '+head+' Sell-Out </th>';
		}else if(beg[1]=="4" || beg[1]==4){
			html+='	<th rowspan="2">'+beg[0]+' Q2 '+head+' Sell-Out Growth Rate</th>';
			html+='	<th >'+beg[0]+' Q2 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'04 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'05 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'06 '+head+' Sell-Out </th>';
		}else if(beg[1]=="7" || beg[1]==7){
			html+='	<th rowspan="2">'+beg[0]+' Q3 '+head+' Sell-Out Growth Rate</th>';
			html+='	<th >'+beg[0]+' Q3 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'07 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'08 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'09 '+head+' Sell-Out </th>';
		}else if(beg[1]=="10" || beg[1]==10){
			html+='	<th rowspan="2">'+beg[0]+' Q4 '+head+' Sell-Out Growth Rate</th>';
			html+='	<th >'+beg[0]+' Q4 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'10 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'11 '+head+' Sell-Out </th>';
			html+='	<th colspan="2">'+beg[0]+'12 '+head+' Sell-Out </th>';
		}
		
		
		html+='</tr>';
		html+='<tr>';
		html+='<th>Achieved</th>';	

		html+='<th>Achieved</th>';
		html+='<th>Growth Rate%</th>';  
		
		html+='<th>Achieved</th>';
		html+='<th>Growth Rate%</th>';  
		
		html+='<th>Achieved</th>';
		html+='<th>Growth Rate%</th>';  
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
		var saleQtylast=0;
		
		var saleQtyOBC=0;
		var saleQty1OBC=0;
		var saleQty2OBC=0;
		var saleQty3OBC=0;
		var saleQtylastOBC=0;
		
		var counHtml="";
		for ( var i = 0; i < by.length; i++) {
			if(center!=null && center!="" && center==by[i].center){
				saleQty+=by[i].saleQty;
				saleQty1+=by[i].saleQty1;
				saleQty2+=by[i].saleQty2;
				saleQty3+=by[i].saleQty3;
				saleQtylast+=by[i].saleQtylast;
				
				
				saleQtyOBC+=by[i].saleQty;
				saleQty1OBC+=by[i].saleQty1;
				saleQty2OBC+=by[i].saleQty2;
				saleQty3OBC+=by[i].saleQty3;
				saleQtylastOBC+=by[i].saleQtylast;
			
				
				counHtml+='<tr>';
				counHtml+='<td >'+isStringNullAvaliable(by[i].country)+'</td>';

				counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty)+'</td>';
				

				if(beg[0]==tYear && (tMonth==1 || tMonth==4 || tMonth==7 || tMonth==10) && tMonth==beg[1]){
					counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty1)+'</td>';

				}else{
					counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty1)+'</td>';
				}
				
				if(beg[1]==1){
					counHtml+='<td > </td>';

				}else{
					counHtml+=growthRateHQ(by[i].gro1);
				}
				
			
				
				if(beg[0]==tYear && (tMonth==2 || tMonth==5 || tMonth==8 || tMonth==11) && ((parseInt(beg[1])+1)==2 || (parseInt(beg[1])+1)==5 || (parseInt(beg[1])+1)==8 || (parseInt(beg[1])+1)==11)){
					counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty2)+'</td>';

				}else{
					counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty2)+'</td>';
				}
				
				
				counHtml+=growthRateHQ(by[i].gro2);
				
				
				if(beg[0]==tYear && (tMonth==3 || tMonth==6 || tMonth==9 || tMonth==12) && (end[1]==3 || end[1]==6 || end[1]==9 || end[1]==12)){
					counHtml+='<td style="background-color: yellow;color:#000;text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty3)+'</td>';

				}else{
					counHtml+='<td style="text-align:right";>'+isStringNullAvaliableNum(by[i].saleQty3)+'</td>';
				}
				
				counHtml+=growthRateHQ(by[i].gro3);
			
				
				
				if(by.length- i == 1){
					html+='<tr>';
					html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty)+'</th>';
					
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty1)+'</th>';
					
					if(beg[1]==1){
						html+='<td > </td>';

					}else{
						var gro=(saleQty1-saleQtylast)/saleQtylast;
						
						html+=growthRateHQ(Math.round(gro*100)+'%');
					}
					
					
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty2)+'</th>';
					
					var gro1=(saleQty2-saleQty1)/saleQty1;
					
					if(saleQty2==0){
						html+=growthRateHQ('-100%');
					}else if(saleQty1==0){
						html+=growthRateHQ('100%');
					}else{
						html+=growthRateHQ(Math.round(gro1*100)+'%');

					}
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty3)+'</th>';
					
					gro1=(saleQty3-saleQty2)/saleQty2;
					
					if(saleQty3==0){
						html+=growthRateHQ('-100%');
					}else if(saleQty2==0){
						html+=growthRateHQ('100%');
					}else{
						html+=growthRateHQ(Math.round(gro1*100)+'%');

					}
					html+='</tr>';
					
					
					html+=counHtml;
					html+='<tr>';
					html+='<th>OBC</th>';
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQtyOBC)+'</th>';
					
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty1OBC)+'</th>';
					
					if(beg[1]==1){
						html+='<td > </td>';

					}else{
						var gro=(saleQty1OBC-saleQtylastOBC)/saleQtylastOBC;
						
						html+=growthRateHQ(Math.round(gro*100)+'%');
					}
					
					
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty2OBC)+'</th>';
					
					var gro1=(saleQty2OBC-saleQty1OBC)/saleQty1OBC;
					
					if(saleQty2OBC==0){
						html+=growthRateHQ('-100%');
					}else if(saleQty1OBC==0){
						html+=growthRateHQ('100%');
					}else{
						html+=growthRateHQ(Math.round(gro1*100)+'%');

					}
					
					html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty3OBC)+'</th>';
					
					gro1=(saleQty3OBC-saleQty2OBC)/saleQty2OBC;
					
					if(saleQty3OBC==0){
						html+=growthRateHQ('-100%');
					}else if(saleQty2OBC==0){
						html+=growthRateHQ('100%');
					}else{
						html+=growthRateHQ(Math.round(gro1*100)+'%');

					}
					html+='</tr>';
					
				}
				
			}else{
				var ach=0;
				html+='<tr>';
				html+='<th>'+isStringNullAvaliable(by[i].center)+'</th>';
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty)+'</th>';
				
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty1)+'</th>';
				
				if(beg[1]==1){
					html+='<td > </td>';

				}else{
					var gro=(saleQty1-saleQtylast)/saleQtylast;
					
					html+=growthRateHQ(Math.round(gro*100)+'%');
				}
				
				
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty2)+'</th>';
				
				var gro1=(saleQty2-saleQty1)/saleQty1;
				
				if(saleQty2==0){
					html+=growthRateHQ('-100%');
				}else if(saleQty1==0){
					html+=growthRateHQ('100%');
				}else{
					html+=growthRateHQ(Math.round(gro1*100)+'%');

				}
				
				html+='<th style="text-align:right";>'+isStringNullAvaliableNum(saleQty3)+'</th>';
				
				gro1=(saleQty3-saleQty2)/saleQty2;
				
				if(saleQty3==0){
					html+=growthRateHQ('-100%');
				}else if(saleQty2==0){
					html+=growthRateHQ('100%');
				}else{
					html+=growthRateHQ(Math.round(gro1*100)+'%');

				}
				html+='</tr>';
				
			   
				
			    html+=counHtml;
			    counHtml="";
				center =by[i].center;
				saleQty=0;
				saleQty1=0;
				saleQty2=0;
				saleQty3=0;
				saleQtylast=0;
				
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
	
	
	function growthRateHQ(val){//增长率背景加载颜色
		if(('' + val).indexOf("-")>=0){
			return "<td  style='background-color: red;color:#fff;'>" + val + "</td>";
		}else{
			return "<td  style='background-color: green;color:#000;'>" +val + "</td>";
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
