

function loadData(obj,what){//加载数据
	loadDynamicHeaderHtml(obj,what);
	loadBottomData(obj,what);
	loadImmobilizationData(obj,what);
};

function loadBottomData(o,what){//底部数据
	var bg = o.data;
	var by = o.dataTwo;
	
	var html = "<tbody>";
	for(var i =0;i<bg.length;i++){
		html += "<tr>";
		for(var j =0;j<by.length;j++){
			if(bg[i].shopId == by[j].shopId){
				html += "<td>" + isStringNullAvaliable(by[i].date) + "</td>";
				html += "<td>" + isStringNullAvaliable(by[i].ACFO) + "</td>";
				html += "<td>" + isStringNullAvaliable(by[i].area) + "</td>";
				html += "<td>" + isStringNullAvaliable(by[i].SALESMAN) + "</td>";
				html += "<td>" + isStringNullAvaliable(by[i].shopClass) + "</td>";
				html += "<td  style='text-align:right;'>" + isStringNullAvaliableNum(by[i].LastTTLQty) + "</td>";
				html += "<td  style='text-align:right;'>" + isStringNullAvaliableNum(by[i].LastTTLAmt) + "</td>";
				html += "<td   style='text-align:right;'>" + isStringNullAvaliableNum(by[i].ToTTLQty) + "</td>";
				html += "<td   style='text-align:right;'>" + isStringNullAvaliableNum(by[i].ToTTLAmt) + "</td>";
				
				if(isStringNullAvaliableNum(by[i].GROWTH_QTY).indexOf("-")>=0){
					html += "<td   style='background-color: red;'>" + isStringNullAvaliableNum(by[i].GROWTH_QTY) + "</td>";
				}else{
					html += "<td   style='background-color: green;'>" + isStringNullAvaliableNum(by[i].GROWTH_QTY) + "</td>";
				}
				if(isStringNullAvaliableNum(by[i].GROWTH_AMT).indexOf("-")>=0){
					html += "<td  style='background-color: red;'>" + isStringNullAvaliableNum(by[i].GROWTH_AMT) + "</td>";
				}else{
					html += "<td  style='background-color: green;'>" + isStringNullAvaliableNum(by[i].GROWTH_AMT) + "</td>";

				}
				/*html += "<td>" + isStringNullAvaliable(by[i].basicTarget) + "</td>";
				html += "<td>" + isStringNullAvaliable(by[i].basicAch) + "</td>";
				html += "<td>" + isStringNullAvaliable(by[i].challengeSum) + "</td>";
				html += "<td>" + isStringNullAvaliable(by[i].challengeAch) + "</td>";*/
			}
		}
		//html += loadByDataCreateBotton(o,bg[i].shopId);
		
		
		
		html += "</tr>";
	}
	html += '</tbody>';
	$('#bydata'+what).html(html);
		var Lefttablehi = $('#test_Left table').height();
		var testLefthi = $('#test_Left').height();
	    if(Lefttablehi > testLefthi){
	    	$('#test_Right').css('width','calc(100vw - 987px)');
	    	$('#test_Left').css('width','calc(100vw - 970px)');
	    } 
		var Lefttablehi = $('#test_Left2 table').height();
		var testLefthi = $('#test_Left2').height();
	    if(Lefttablehi > testLefthi){
	    	$('#test_Right2').css('width','calc(100vw - 987px)');
	    	$('#test_Left2').css('width','calc(100vw - 970px)');
	    } 
		var Lefttablehi = $('#test_Left3 table').height();
		var testLefthi = $('#test_Left3').height();
	    if(Lefttablehi > testLefthi){
	    	$('#test_Right3').css('width','calc(100vw - 987px)');
	    	$('#test_Left3').css('width','calc(100vw - 970px)');
	    } 
		var Lefttablehi = $('#test_Left4 table').height();
		var testLefthi = $('#test_Left4').height();
	    if(Lefttablehi > testLefthi){
	    	$('#test_Right4').css('width','calc(100vw - 987px)');
	    	$('#test_Left4').css('width','calc(100vw - 970px)');
	    } 
	  
}

function loadByDataCreateBotton(obj,shopId){//根据数据生成底部信息
	var so = obj.soldData;
	var soAllSum = obj.sumTwo;
	var functArray = obj.Sold;//获取功能数据
	var funQty = 0;//每个功能数量总和
	var funAmt = 0;//每个功能金额总和
	var hml = "";
	
	//sold
	var functArrayIsNull = functArray.length != 0;
	if(functArrayIsNull){
		var funcTemp = functArray[0].spec;//储存第一个
		for(var i = 0;i < functArray.length;i ++){
			if(funcTemp == functArray[i].spec){
				var is = true;
				for(var j=0;j<so.length;j++){
					if(so[j].shop == shopId && 
							funcTemp == so[j].spec &&
								so[j].model == functArray[i].model){
						hml += "<td>" + isStringNullAvaliable(so[j].qty) + "</td>";
						funQty += so[j].qty;
						funAmt += so[j].amt;
						is = false;
						break;
					}
				}
				hml += loadNullHtml(is);
				
				if(functArray.length - i == 1){
					hml += "<td>" + isStringNullAvaliable(funQty) + "</td>";
					hml += "<td>" + isStringNullAvaliable(funAmt) + "</td>";
				}
			}else{
				
				hml += "<td>" + isStringNullAvaliable(funQty) + "</td>";
				hml += "<td>" + isStringNullAvaliable(funAmt) + "</td>";
				funQty = 0;
				funAmt = 0;
				funcTemp = functArray[i].spec;
				i --;
			}
		}
		
		var isAllNUll = true;
		for(var i = 0;i<soAllSum.length;i++){
			if(soAllSum[i].spec == 'soldSum' && soAllSum[i].shop == shopId){
				hml += "<td>"+ isStringNullAvaliable(soAllSum[i].qty) +"</td>";//so数量总和
				hml += "<td>"+ isStringNullAvaliable(soAllSum[i].amt) +"</td>";//so金额总和
				isAllNUll = false;
				break;
			}
		}
		if(isAllNUll){
			hml += "<td>-</td>";//so数量总和
			hml += "<td>-</td>";//so金额总和
		}
	}
	
	
	/*so = obj.stocksData;
	functArray = obj.Stocks;//获取功能数据
	var functArrayIsNull = functArray.length != 0;
	if(functArrayIsNull){
	var funcTemp = functArray[0].spec;//储存第一个
		//Stocks
		for(var i = 0;i < functArray.length;i ++){
			if(funcTemp == functArray[i].spec){
				var is = true;
				for(var j=0;j<so.length;j++){
					if(so[j].shop == shopId && 
							funcTemp == so[j].spec &&
								so[j].model == functArray[i].model){
						hml += "<td>" +so[j].qty+ "</td>";
						is = false;
						break;
					}
				}
				hml += loadNullHtml(is);
			}else{
				funcTemp = functArray[i].spec;
				i --;
			}
		}		
		
		
		isAllNUll = true;
		for(var i = 0;i<soAllSum.length;i++){
			if(soAllSum[i].spec == 'stocksSum' && soAllSum[i].shop == shopId){
				hml += "<td>"+ soAllSum[i].qty +"</td>";//so数量总和
				isAllNUll = false;
				break;
			}
		}
		if(isAllNUll){
			hml += "<td>-</td>";//st数量总和
		}
	}*/
		
	/*so = obj.displayData;
	functArray = obj.Display;//获取功能数据
	var functArrayIsNull = functArray.length != 0;
	if(functArrayIsNull){
		funcTemp = functArray[0].spec;//储存第一个
		//Stocks
		for(var i = 0;i < functArray.length;i ++){
			if(funcTemp == functArray[i].spec){
				var is = true;
				for(var j=0;j<so.length;j++){
					if(so[j].shop == shopId && 
							funcTemp == so[j].spec &&
								so[j].model == functArray[i].model){
						hml += "<td>" +so[j].qty+ "</td>";
						is = false;
						break;
					}
				}
				hml += loadNullHtml(is);
			}else{
				funcTemp = functArray[i].spec;
				i --;
			}
		}		
		isAllNUll = true;
		for(var i = 0;i<soAllSum.length;i++){
			if(soAllSum[i].spec == 'displaySum' && soAllSum[i].shop == shopId){
				hml += "<td>"+ soAllSum[i].qty +"</td>";//so数量总和
				isAllNUll = false;
				break;
			}
		}
		if(isAllNUll){
			hml += "<td>-</td>";//st数量总和
		}
	}*/
		
	return hml;

}

function loadNullHtml(is){
	if(is){
		return "<td>-</td>"; 
	}
	return "";
}

function loadDynamicHeaderHtml(obj,what){//动态表头
	var html = "";
	
	var funcHeadHtml = "";//功能显示
	var funcModelHtml = "";//功能型号
	//var funcModelDataHtml = "<tr>";//功能型号数据(tr)未结束
	var soldHtml = "";
	
	var soldData = obj.SoldSum;
	var ttl = obj.TTL;
	var headSum =   "<tr><td>-</td>" +
					"<td>-</td>" +
					"<td>-</td>" +
					"<td>-</td>" +
					"<td>-</td>" +
					"<td  style='text-align:right'>"+ isStringNullAvaliableNum(ttl[0].LASTTTLQty) +"</td>" +
					"<td  style='text-align:right'>"+ isStringNullAvaliableNum(ttl[0].LASTTTLAmt) +"</td>" +
					"<td style='text-align:right' >"+ isStringNullAvaliableNum(ttl[0].TOTTLQty) +"</td>" +
					"<td style='text-align:right'>"+ isStringNullAvaliableNum(ttl[0].TOTTLAmt) +"</td>" ;
				
					if(isStringNullAvaliableNum(ttl[0].GROWTH_QTY).indexOf("-")>=0){
						headSum += "<td   style='background-color:red;'>" + isStringNullAvaliableNum(ttl[0].GROWTH_QTY) + "</td>";
					}else{
						headSum += "<td  style='background-color: green;'>" + isStringNullAvaliableNum(ttl[0].GROWTH_QTY) + "</td>";
					}
					
	
					if(isStringNullAvaliableNum(ttl[0].GROWTH_AMT).indexOf("-")>=0){
						headSum += "<td  style='background-color: red;'>" + isStringNullAvaliableNum(ttl[0].GROWTH_AMT) + "</td>";
					}else{
						headSum += "<td  style='background-color: green;'>" + isStringNullAvaliableNum(ttl[0].GROWTH_AMT) + "</td>";
					}
					
					
					/*"<td>"+ isStringNullAvaliable(ttl[0].GROWTH_QTY) +"</td>"
					+
					"<td>"+ isStringNullAvaliable(ttl[0].GROWTH_AMT) +"</td>";*/
					/*"<td>"+ isStringNullAvaliable(ttl[0].basicTarget) +"</td>" +
					"<td>"+ isStringNullAvaliable(ttl[0].basicAch) +"</td>" +
					"<td>"+ isStringNullAvaliable(ttl[0].challengeSum) +"</td>" +
					"<td>"+ isStringNullAvaliable(ttl[0].challengeAch) +"</td>";*/
					
	
	/*var functArray = obj.Sold;//获取功能数据
	var functArrayIsNull = (functArray.length != 0);
	if(functArrayIsNull){
		var funcNum = 0;//记录某个功能个数
		var funcTemp = functArray[0].spec;//储存第一个
		
		//sold
		for(var i = 0;i < functArray.length;i ++){
			if(funcTemp == functArray[i].spec){
				funcModelHtml += "<th>" + isStringNullAvaliable(functArray[i].model) + "</th>";
				funcModelDataHtml += "<th>" + isStringNullAvaliable(functArray[i].price) + "</th>";
				soldHtml += "<th>Sold</th>";
				funcNum ++;
				if(functArray.length - i == 1){
					var o = {};
					o.funcNum = funcNum;
					o.funcName = functArray[i].spec;
					var spc = obj.specSum[0];
					headSum += loadSum(spc);
					
					commonLoad(o);
				}
				
				for(var j = 0;j<soldData.length;j++){
					if(soldData[j].spec == functArray[i].spec && soldData[j].model == functArray[i].model){
						headSum += "<td>"+ isStringNullAvaliable(soldData[j].qty) +"</td>";
						break;
					}
				}
				
			}else{
				var o = {};
				o.funcNum = funcNum;
				o.funcName = functArray[i - 1].spec;
				commonLoad(o);
				var spc = obj.specSum[0];
				headSum += loadSum(spc);
				
				funcTemp = functArray[i].spec;
				funcNum = 0;//功能个数清0
				
				i --;
			}
		}
		soldHtml += "<th>QTY</th><th>AMOUNT</th>";
		funcHeadHtml += getFuncSumHtml("GO LIVE/SMART TV SUB-TOTAL");
		
		headSum += "<td>"+ isStringNullAvaliable(spc.soldQty) +"</td>";
		headSum += "<td>"+ isStringNullAvaliable(spc.soldAmt) +"</td>";
	}
	*/
	// Stocks 
	/*functArray = obj.Stocks;//获取功能数据
	functArrayIsNull = (functArray.length != 0);
	if(functArrayIsNull){
		var funcNum = 0;//记录某个功能个数
		var funcTemp = functArray[0].spec;//储存第一个
		soldData = obj.StocksSum;
		
		for(var i = 0;i < functArray.length;i ++){
			if(funcTemp == functArray[i].spec){
				funcModelHtml += "<th>" + functArray[i].model + "</th>";
				funcModelDataHtml += "<th></th>";
				soldHtml += "<th>Stocks</th>";
				funcNum ++;
				if(functArray.length - i == 1){
					var o = {};
					o.funcNum = funcNum;
					o.funcName = functArray[i].spec;
					loadCommonLoad();
				}
				
				for(var j = 0;j<soldData.length;j++){
					if(soldData[j].spec == functArray[i].spec && soldData[j].model == functArray[i].model){
						headSum += "<td>"+ soldData[j].qty +"</td>";
						break;
					}
				}
			}else{
				var o = {};
				o.funcNum = funcNum;
				o.funcName = functArray[i - 1].spec;
				loadCommonLoad();
					
				funcTemp = functArray[i].spec;
				funcNum = 0;//功能个数清0
				i --;
			}
		}
		soldHtml += "<th>QTY</th>";
		funcHeadHtml += getFuncSumHtmls("FLAT PANEL TV TTL INVENTORY");
		
		headSum += "<td>"+ spc.stocksQty +"</td>";
	}*/
	 
	//  Display  
	/*functArray = obj.Display;//获取功能数据
	functArrayIsNull = (functArray.length != 0);
	if(functArrayIsNull){
		var funcNum = 0;//记录某个功能个数
		var funcTemp = functArray[0].spec;//储存第一个
		
		soldData = obj.DisplaySum;
		
		for(var i = 0;i < functArray.length;i ++){
			if(funcTemp == functArray[i].spec){
				funcModelHtml += "<th>" + functArray[i].model + "</th>";
				funcModelDataHtml += "<th></th>";
				soldHtml += "<th>Display</th>";
				funcNum ++;
				if(functArray.length - i == 1){
					var o = {};
					o.funcNum = funcNum;
					o.funcName = functArray[i].spec;
					loadCommonLoad();
					
				}
				
				for(var j = 0;j<soldData.length;j++){
					if(soldData[j].spec == functArray[i].spec && soldData[j].model == functArray[i].model){
						headSum += "<td>"+ soldData[j].qty +"</td>";
						break;
					}
				}
			}else{
				var o = {};
				o.funcNum = funcNum;
				o.funcName = functArray[i - 1].spec;
				loadCommonLoad();
				
				funcTemp = functArray[i].spec;
				funcNum = 0;//功能个数清0
				i --;
			}
		}
		soldHtml += "<th>QTY</th>";
		funcHeadHtml += getFuncSumHtmls("FLAT PANEL TV TTL DISPLAY");
		
		headSum += "<td>"+ spc.displayQty +"</td>";
	}*/
	
	//funcModelDataHtml += "</tr>";
	headSum += "</tr>";
	
	html += setHeaderFunctHtml(funcHeadHtml) + setHeaderMdoelHtml(funcModelHtml)/* + funcModelDataHtml */+ setSoldAndSumHtml(soldHtml) + headSum;
	$("#headr"+what).html(html);
	
	function commonLoad(o){
		funcHeadHtml += "<th class='kind' colspan=" + o.funcNum + " style='width: "+ (o.funcNum * 101 - 2) + "px;'>" + o.funcName + "</th>" + getFuncSumHtml(o.funcName + "SUB-TOTAL");
		soldHtml += "<th>QTY</th><th>AMOUNT</th>";
	}
	
	function loadCommonLoad(){
		funcHeadHtml += "<th class='kind' colspan=" + o.funcNum + " style='width: "+ (o.funcNum * 101 - 2) + "px;'>" + o.funcName + "</th>";
	};
	
	function loadSum(so){
		var spc = so;
		var spqty = ("spc." + o.funcName +"_qty").replace(/\ /g, "_");
		var spamt = ("spc." + o.funcName +"_amt").replace(/\ /g, "_");
		return "<td>"+ isStringNullAvaliable(parseJSONStr(spqty)) +"</td><td>"+ isStringNullAvaliable(parseJSONStr(spamt)) +"</td>";
	};
	
	function parseJSONStr(string){//解析字符串为JSON对象
		return eval('(' + string + ')');
	};
}

function isStringNullAvaliable(val){//字符串是否为空
	if(typeof(val) != "undefined" && val != '' && val != null){
		return val;
	}
	return '-';	
};

function isStringNullAvaliableNum(val){//字符串是否为空
	if(typeof(val) != "undefined" && val != '' && val != null){
		return toThousands(val);
	}
	return '-';	
};


function setHeaderFunctHtml(functHtml){//动态设计表头（功能）
	return "<tr>" +
				"<th rowspan='3'>DATE OF HIRED</th>" +
				"<th rowspan='3'>ACFO</th>" +
				"<th rowspan='3'>AREA</th>" +
				"<th rowspan='3'>SALESMAN</th>" +
				"<th rowspan='3'>SHOP CLASS</th>" +
				"<th colspan='2' style='width: 200px;'>Last Year.</th>"
				+
				"<th colspan='2' style='width: 200px;'>This Year.</th>"
				+
				"<th rowspan='2'  style='width: 200px;' colspan='2' >GROWTH</th>"
				/*+
				"<th rowspan='3'>GROWTH_AMT</th>"*/
				+ functHtml +
		"</tr>";
};
   
function setHeaderMdoelHtml(modelHtml){//动态设计表头（型号）
	return "<tr>" +
				"<th class='TVblue' colspan='2' style='width: 200px;'>TTL TV SO</th>" +
				"<th class='TVblue' colspan='2' style='width: 200px;'>TTL TV SO</th>" +
				/*"<th class='TVblue' rowspan='3'>BASIC TARGET</th>" +
				"<th class='TVblue' rowspan='3'>Ach.</th>" +
				"<th class='TVblue' rowspan='3' >CHALLENGE TARGET</th>" +
				"<th class='TVblue' rowspan='3'>Ach.</th>" + modelHtml +*/
				
			"</tr>";
}

function getFuncSumHtml(str){
	return "<th class='safety' colspan='2' rowspan='3' style='width: 200px;'>" + str + "</th>";
};

function getFuncSumHtmls(str){
	return "<th class='safety'  rowspan='3'>"+ str +"</th>";
};



function setSoldAndSumHtml(soldHtml){
	return "<tr>" +
				"<th class='TVblue'>QTY</th>" +
				"<th class='TVblue'>AMOUNT</th>" + 
				"<th class='TVblue'>QTY</th>" +
				"<th class='TVblue'>AMOUNT</th>"+
				"<th class='TVblue'>QTY</th>" +
				"<th class='TVblue'>AMT</th>"  + soldHtml +
			"</tr>";
};

function loadImmobilizationData(obj,what){//加载固定表头
	
	//alert(html);

	var html = "<tbody>";
	var by = obj.data;
	for(var i =0;i<by.length;i++){
		html += "<tr>";
		html += "<td>" +isStringNullAvaliable(by[i].reg)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].type)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].dealer)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].branch)+ "</td>";
		html += "<td>" +isStringNullAvaliable(by[i].PROMODISER)+ "</td>";
		html += "</tr>";
	}
	html += '</tbody>';
	//alert(html);
	$('#fixation'+what).html(getImmoHead() + html);
}
function getImmoHead(){//获取固定表头
	
	return '<thead>' +
				'<tr>' +
					'<th colspan="6">SELL-OUT INFORMATION SHEET</th>' +
				'</tr>' +
				'<tr>' +
					'<th>REG.</th>' +
					'<th>TYPE</th>' +
					'<th>DEALER</th>' +
					'<th>BRANCH</th>' +
					'<th>PROMODISER NAME</th>' +
				'</tr>' +
				/*'<tr>' +
					'<th></th>' +
					'<th></th>' +
					'<th></th>' +
					'<th></th>' +
					'<th></th>' +
				'</tr>' +*/
			'</thead>' ;
};