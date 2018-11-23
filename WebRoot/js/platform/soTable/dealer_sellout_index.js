$(function(){
	getSelectMmodel();
});

//JS代码
var timer = null;
//左侧DIV的滚动事件
function moveLeft_Left(){
    $("#test_Right").removeAttr("onScroll");
    $("#test_Right").scrollLeft($("#test_Left").scrollLeft());
    $("#test_Head").removeAttr("onScroll");
    $("#test_Head tbody").scrollTop($("#test_Left").scrollTop());
//     取消延迟预约。【重点】鼠标滚动过程中会多次触发本行代码，相当于不停的延迟执行下面的预约
    clearTimeout(timer);
 //     延迟恢复（预约）另一个DIV的滚动事件，并将本预约返回给变量[timer]
//  timer = setTimeout(function() {
//      $("#test_Right").attr("onScroll","moveLeft_Right();");
//  }, 300 );
}

function getSelectDate(d){
	 var date=new Date(d);
	 var currentMonth=date.getMonth();
	 var nextMonth=++currentMonth;
	 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
	 var oneDay=1000*60*60*24;
	 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
};

function loadingShowHide(is){																																																																									
	if(is){
		$("#loadingImport").hide();
		
		$("#test_Head").show();
		$("#test_Right").show();
		$("#test_Left").show();
		
	}else{
		$("#loadingImport").show();
		
		$("#test_Head").hide();
		$("#test_Right").hide();
		$("#test_Left").hide();
	}
};

function getSelectMmodel(){
	loadingShowHide(false);
	var selectDateVal = $('#reservationMonthly').val();
	var beginDate;
	var endDate;
	if(!isStringNullAvaliable(selectDateVal)){
		beginDate = new Date().getFullYear() + "-" + (new Date().getMonth()+1) + "-01";
		endDate = getSelectDate(beginDate);
	}else{
		beginDate = selectDateVal + "-01";
		endDate = getSelectDate(beginDate);
	}
	//注
	//默认是设计时间
	//selectDateVal  等于本年本月
	$.ajax({
		url:baseUrl + "staPage/selectDealerSellout.action",
		type:"POST",
		data:{"beginDate":beginDate,"endDate":endDate},
		success:function(data){
			var ImmoInfoHtml = "<tbody>" + byDataLoadImmoInfo(data.data) + "</tbody>";//固定底部
			$('#fixedInformation').html(getImmoHead() + ImmoInfoHtml);//固定信息生成
			
			var moveInfoHtml = "";//移动数据
			if(isStringNullAvaliable(selectDateVal)){//移动表头数据
				moveInfoHtml += getRandomHead(selectDateVal.split('-')[0]);
			}else{
				moveInfoHtml += getRandomHead(new Date().getFullYear());//下拉框选择为空时时间默认为当年
			};
			
			$('#mobileHeadData').html(moveInfoHtml + loadMobileData(data.data)[1]);
			
			var loadMobileHtml = loadMobileData(data.data)[0];//底部信息
			
			loadingShowHide(true);
			
			$('#mobileData').html(loadMobileHtml);
			
		//  表格高度不够出现滚动条
			var Lefttablehi = $('#test_Left table').height();
			var testLefthi = $('#test_Left').height();
		    if(Lefttablehi > testLefthi){
		     	$('#test_Right').css('width','calc(100vw - 704px)');
		    	$('#test_Left').css('width','calc(100vw - 687px)');
		    }
		}
	});
};

function byDataLoadImmoInfo(obj){//根据info生成底部固定数据
	var html = '';
	$(obj).each(function(){
		var trHtml = '<tr>';
		html += "<td>" + isNull(this.REG) + "</td>";
		html += "<td>TV</td>";
		html += "<td>" + isNull(this.DEALER) + "</td>";
		trHtml = '</tr>';
		html += trHtml;
	});
	return html;
};


function getImmoHead(){//获取固定表头头部
	return "<thead>" +
				"<tr>" +
					"<th rowspan='3'>REG.</th>" +
					"<th rowspan='3'>RANK</th>" +
					"<th rowspan='3'>DEALER</th>" +
				"</tr>" +
				"<tr></tr>" +
				"<tr></tr>" +
				"<tr class='graphite'>" +
					"<th></th>" +
					"<th>TOTAL</th>" +
					"<th></th>" +
				"</tr>" +
			"</thead>";
};

function isPercentNull(val){
	if(isNull(val) == '-' || isNull("" + val).indexOf("NaN") >= 0){
		return '-';
	}else{
		return val + "%";
	}
};

function setHtmlColour(val){
	if(isNull(val) == '-' || isNull("" + val).indexOf("NaN") >= 0){
		return '-';
	}else{
		return growthRate(val);
	}
};

function loadMobileData(obj){//byData底部信息
	var htmls = [2];
	var html = "<tbody>";
	var headTotal = loadloadMobileDataTotal(obj);//累加储存头部信息
	$(obj).each(function(){
		var trHtml = '<tr>';
		
		html += setHtmlLeft(this.lastYear_NO_OF_SHOP,false);
		html += setHtmlLeft(this.lastYear_NO_OF_FPS,false);
		html += setHtmlLeft(this.lastYear_TOTAL_QTY_,false);
		html += setHtmlLeft(this.lastYear_TOTAL_AMOUNT,true);
		html += setHtmlLeft(this.toYear_NO_OF_SHOP,false);
		html += setHtmlLeft(this.toYear_NO_OF_FPS,false);
		html += setHtmlLeft(this.toYear_TTL_TV_SO_QTY,false);
		html += setHtmlLeft(this.toYear_TTL_TV_SO_AMT,true);
		html += setHtmlLeft(this.BASIC_TARGET,true) + "</td>";
		html += "<td>" + isPercentNull(this.ACH_) + "</td>";
		html += "<td>" + isPercentNull((this.toYear_TTL_TV_SO_QTY/headTotal[5]%100).toFixed(2)) + "</td>";
		html +=  setHtmlColour(this.GROWTH_QTY);
		html +=  setHtmlColour(this.GROWTH_AMOUNT);
		html += setHtmlLeft(this.lastYear_AVE_SO_FPS_QTY,false);
		html += setHtmlLeft(this.lastYear_AVE_SO_FPS_AMT,false);
		html += setHtmlLeft(this.toYear_AVE_SO_FPS_QTY,false);
		html += setHtmlLeft(this.toYear_AVE_SO_FPS_AMT,false);
		
		html += setHtmlLeft(this.SELL_OUT_EFFICIENCY_QTY,false);
		html += setHtmlLeft(this.SELL_OUT_EFFICIENCY_AMT,false);
		
		trHtml = '</tr>';
		html += trHtml;
	});
	
	/*total*/
	var totalHtml = "<tr class='graphite'>";
	for(var i =0;i<headTotal.length;i++){
		if(i == 9  || i == 10){
			totalHtml += "<th>"+ headTotal[i] +"%</th>";
		}else if( i == 11 || i== 12){
			totalHtml += setHtmlColour(headTotal[i]);
		}else{
			totalHtml += "<th style='text-align:right;'>"+ headDataToFixed(headTotal[i] * 1) +"</th>";
		}
	}

	totalHtml += "<tr>";
	html += "</tbody>";
	htmls[0] = html;
	htmls[1] = totalHtml;
	return htmls;
}

function headDataToFixed(num){//头部判断保留小数点。
	if((""+num).indexOf('.') >= 0){
		return toThousands(num.toFixed(2),2,1);
	}else{
		return toThousand(num);
	}
}

function isNum(value){//是否有值
	if(isStringNullAvaliable(value)){
		return value * 1;
	};
	return 0;
};

function loadloadMobileDataTotal(o){//加载底部信息Total
	
	var headTotal = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
	$(o).each(function(){
		headTotal[0] += isNum(this.lastYear_NO_OF_SHOP);
		headTotal[1] += isNum(this.lastYear_NO_OF_FPS);
		headTotal[2] += isNum(this.lastYear_TOTAL_QTY_);
		headTotal[3] += isNum(this.lastYear_TOTAL_AMOUNT);
		headTotal[4] += isNum(this.toYear_NO_OF_SHOP);
		headTotal[5] += isNum(this.toYear_NO_OF_FPS);
		headTotal[6] += isNum(this.toYear_TTL_TV_SO_QTY);
		headTotal[7] += isNum(this.toYear_TTL_TV_SO_AMT);
		headTotal[8] += isNum(this.BASIC_TARGET);
		headTotal[9] += isNum(this.ACH_);
		headTotal[10] = 100;
		headTotal[11] += isNum(this.GROWTH_QTY);
		headTotal[12] += isNum(this.GROWTH_AMOUNT);
		headTotal[13] += isNum(this.lastYear_AVE_SO_FPS_QTY);
		headTotal[14] += isNum(this.lastYear_AVE_SO_FPS_AMT);
		headTotal[15] += isNum(this.toYear_AVE_SO_FPS_QTY);
		headTotal[16] += isNum(this.toYear_AVE_SO_FPS_AMT);
		headTotal[17] += isNum(this.SELL_OUT_EFFICIENCY_QTY);
		headTotal[18] += isNum(this.SELL_OUT_EFFICIENCY_AMT);
	});

	return headTotal;
};

function getRandomHead(year){//获取移动表头
	//$('#reservationMonthly').val().split('-')[0] -1
	return "<tr>" +
			"<th colspan='4' style='width: 402px;'>Nov." +(year -1)+ "</th> " +
			"<th class='ivory' colspan='4' style='width: 402px;'>Nov."+ year+"</th>" +
			"<th rowspan='3'>BASIC TARGET</th>" +
			"<th rowspan='3'>ACH.</th>" +
			"<th rowspan='3'>MARKET SHARE</th>" +
			"<th colspan='2' style='width: 200px;'>GROWTH</th>" +
			"<th colspan='2' style='width: 200px;'>" +(year -1)+ " AVE.SO/FPS</th>" +
			"<th colspan='2' style='width: 200px;'>"+ year+" AVE.SO/FPS</th>" +
			"<th colspan='2' style='width: 200px;'>SELL-OUT EFFICIENCY</th>" +
		"</tr>" +            
		"<tr>" +                      
			"<th class='' rowspan='2' >NO. OF SHOP</th>" +
			"<th class='' rowspan='2'>NO. OF FPS</th>" +
			"<th class='' rowspan='2'>TOTAL QTY.</th>" +
			"<th class='' rowspan='2' >TOTAL AMOUNT</th>" +
			"<th class='' rowspan='2'>NO. OF SHOP</th>" +
			"<th class='' rowspan='2'>NO. OF FPS</th>" +
			"<th class='cyan' rowspan='2' colspan='2' style='width: 200px;'>TTL TV SO</th>" +
			"<th rowspan='2'>QTY</th>" +
			"<th rowspan='2'>AMOUNT</th>" +
			"<th rowspan='2'>QTY</th>" +
			"<th rowspan='2'>AMT</th>" +
			"<th rowspan='2'>QTY</th>" +
			"<th rowspan='2'>AMT</th>" +
			"<th rowspan='2'>QTY</th>" +
			"<th rowspan='2'>AMT</th>" +
		"</tr>" +
		"<tr></tr>";
};

function setHtmlLeft(val,isMoney){
	if(isNull(val) == "-"){
		return "<td>-</td>";
	}else{
		if(isMoney){//true为金额
			return "<td style='text-align:right;'>" + headDataToFixed(val * 1) + "</td>";
		}else{
			return "<td style='text-align:right;'>" + headDataToFixed(val * 1) + "</td>";
		}
	}
}

function isNull (val){//识别值是否为空
	if(isStringNullAvaliable(val)){
		return val;
	}
	return "-";
};