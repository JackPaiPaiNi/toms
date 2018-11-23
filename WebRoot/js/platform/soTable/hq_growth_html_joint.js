/**
 * 加载年度拼接
 * @param dataArr,selectObj
 */
function loadYearHtmlJoint(dataArr,selectObj){
	var left = loadYearHtmlLeftHeadJoint(selectObj) + loadYearHtmlLeftBottomJoint(dataArr);
	var right = loadYearHtmlRightHeadJoint(selectObj) + loadYearHtmlRightBottomJoint(dataArr,selectObj);
	setYearHtml(selectObj.tab,left,right);
};

/**
 * 加载半年度拼接
 * @param dataArr,selectObj
 */
function loadHalfAYearHtmlJoint(dataArr,selectObj){
	var left = loadHalfAYearHtmlLeftHeadJoint(selectObj) + loadYearHtmlLeftBottomJoint(dataArr);
	var right = loadHalfAYearHtmlRightHeadJoint(selectObj) + loadYearHtmlRightBottomJoint(dataArr,selectObj);
	setHalfAYearHtml(selectObj.tab,left,right);
};

/**
 * 加载季度拼接
 * @param dataArr,selectObj
 */
function loadQuarterHtmlJoint(dataArr,selectObj){
	var left = loadHalfAYearHtmlLeftHeadJoint(selectObj) + loadYearHtmlLeftBottomJoint(dataArr);
	var right = loadQuarterHtmlRightHeadJoint(selectObj) + loadQuarterHtmlRightBottomJoint(dataArr,selectObj);
	setQuarterHtml(selectObj.tab,left,right);
};

/**
 * 初始化数据对象
 */
function initializeDataObj(){
	var o = {};
	o.last_year_qty = 0;
	o.this_year_qty = 0;
	o.last_year_H1 = 0;
	o.this_year_H1 = 0;
	o.last_year_H2 = 0;
	o.this_year_H2 = 0;
	return o;
};

/**
 * 初始化数据对象
 */
function initializeQuarterDataObj(){
	var o = {};
	o.last_year_qty = 0;
	o.this_year_qty = 0;
	o.last_Q1 = 0;
	o.this_Q1 = 0;
	o.last_Q2 = 0;
	o.this_Q2 = 0;
	o.last_Q3 = 0;
	o.this_Q3 = 0;
	return o;
};

/**
 * 加载年度右边底部拼接
 * @param dataArr
 */
function loadYearHtmlRightBottomJoint(dataArr,selectObj){
	if(dataArr.length <=0 ){
		return "</tbody>" +
			"</table>";
	}
	
	var allHtmlStr = "";//业务中心 AND 国家
	var countryHtmlStr = "";//国家Html
	
	var o = initializeDataObj();
	
	var center = dataArr[0].center;
	for(var i = 0;i < dataArr.length; i++){
		if(center == dataArr[i].center){
			countryHtmlStr += jointStrTDYear(dataArr[i],selectObj);
			
			o.last_year_qty += toRound(isNullReturnZero(dataArr[i].last_year_qty));
			o.this_year_qty += toRound(isNullReturnZero(dataArr[i].this_year_qty));
			
			o.last_year_H1 += toRound(isNullReturnZero(dataArr[i].last_year_H1));
			o.this_year_H1 += toRound(isNullReturnZero(dataArr[i].this_year_H1));
			
			o.last_year_H2 += toRound(isNullReturnZero(dataArr[i].last_year_H2));
			o.this_year_H2 += toRound(isNullReturnZero(dataArr[i].this_year_H2));
			
			if(dataArr.length - i == 1){
				o.year_growth = getGrowthRate(o.last_year_qty,o.this_year_qty);
				o.H1_growth = getGrowthRate(o.last_year_H1,o.this_year_H1);
				o.H2_growth = getGrowthRate(o.last_year_H2,o.this_year_H2);
				allHtmlStr += jointStrYear(o) + countryHtmlStr;
			}
		}else{
			o.year_growth = getGrowthRate(o.last_year_qty,o.this_year_qty);
			o.H1_growth = getGrowthRate(o.last_year_H1,o.this_year_H1);
			o.H2_growth = getGrowthRate(o.last_year_H2,o.this_year_H2);
			allHtmlStr += jointStrYear(o) + countryHtmlStr;
			center = dataArr[i].center;
			o = initializeDataObj();
			countryHtmlStr = "";
			i --;
		}
	}
	return allHtmlStr + "</tbody>" + "</table>";
};

/**
 * 季度右边底部拼接
 * @param dataArr
 */
function loadQuarterHtmlRightBottomJoint(dataArr,selectObj){
	if(dataArr.length <=0 ){
		return "</tbody>" +
		"</table>";
	}
	
	var allHtmlStr = "";//业务中心 AND 国家
	var countryHtmlStr = "";//国家Html
	
	var o = initializeQuarterDataObj();
	
	var center = dataArr[0].center;
	for(var i = 0;i < dataArr.length; i++){
		if(center == dataArr[i].center){
			countryHtmlStr += quarterJointStrTDYear(dataArr[i],selectObj);
			
			o.last_year_qty += toRound(isNullReturnZero(dataArr[i].last_year_qty));
			o.this_year_qty += toRound(isNullReturnZero(dataArr[i].this_year_qty));
			
			o.last_Q1 += toRound(isNullReturnZero(dataArr[i].last_Q1));
			o.this_Q1 += toRound(isNullReturnZero(dataArr[i].this_Q1));
			
			o.last_Q2 += toRound(isNullReturnZero(dataArr[i].last_Q2));
			o.this_Q2 += toRound(isNullReturnZero(dataArr[i].this_Q2));
			
			o.last_Q3 += toRound(isNullReturnZero(dataArr[i].last_Q3));
			o.this_Q3 += toRound(isNullReturnZero(dataArr[i].this_Q3));
			
			if(dataArr.length - i == 1){
				o.year_growth = getGrowthRate(o.last_year_qty,o.this_year_qty);
				o.Q1_growth = getGrowthRate(o.last_Q1,o.this_Q1);
				o.Q2_growth = getGrowthRate(o.last_Q2,o.this_Q2);
				o.Q3_growth = getGrowthRate(o.last_Q3,o.this_Q3);
				allHtmlStr += quarterJointStrYear(o) + countryHtmlStr;
			}
		}else{
			o.year_growth = getGrowthRate(o.last_year_qty,o.this_year_qty);
			o.Q1_growth = getGrowthRate(o.last_Q1,o.this_Q1);
			o.Q2_growth = getGrowthRate(o.last_Q2,o.this_Q2);
			o.Q3_growth = getGrowthRate(o.last_Q3,o.this_Q3);
			allHtmlStr += quarterJointStrYear(o) + countryHtmlStr;
			center = dataArr[i].center;
			o = initializeQuarterDataObj();
			countryHtmlStr = "";
			i --;
		}
	}
	return allHtmlStr + "</tbody>" + "</table>";
};

function jointStrYear(o){
	return "<tr>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_year_qty)) +"</th>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_year_qty)) +"</th>" +
				growthRate(o.year_growth) +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_year_H1)) +"</th>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_year_H1)) +"</th>" +
				growthRate(o.H1_growth) +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_year_H2)) +"</th>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_year_H2)) +"</th>" +
				growthRate(o.H2_growth) +
			"</tr>";
};

function jointStrTDYear(o,selectObj){
	var returnClasYellowArr = MarkCurrentTime(selectObj);
	return "<tr>" +
				"<td "+ returnClasYellowArr[0] +" style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_year_qty)) +"</td>" +
				"<td "+ returnClasYellowArr[1] +" style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_year_qty)) +"</td>" +
				growthRate(o.year_growth) +
				"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_year_H1)) +"</td>" +
				"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_year_H1)) +"</td>" +
				growthRate(o.H1_growth) +
				"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_year_H2)) +"</td>" +
				"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_year_H2)) +"</td>" +
				growthRate(o.H2_growth) +
			"</tr>";
};

/**
 * 标识当前数据(黄色)
 * @param selectObj
 */
function MarkCurrentTime(selectObj){
	if('YEAR' == selectObj.timeType){
		return [returnClasYellow(selectObj.selectDateVal - 1),returnClasYellow(selectObj.selectDateVal)];
	}else if('HALF_A_YEAR' == selectObj.timeType){
		return [returnHalfAYearClasYellow(selectObj.selectDateVal - 1,selectObj.selectHAY),returnHalfAYearClasYellow(selectObj.selectDateVal,selectObj.selectHAY)];
	}else{
		return [returnQuarterClasYellow(selectObj.selectDateVal - 1,selectObj.selectHAY),returnQuarterClasYellow(selectObj.selectDateVal,selectObj.selectHAY)];
	}
};

function returnClasYellow(num){
	if(num == (new Date().getFullYear())){
		return "class='yellow';";
	}
	return "";
};

function returnHalfAYearClasYellow(num,selectHAY){
	if(num == (new Date().getFullYear()) && selectHAY == returnCurrentHalfYear()){
		return "class='yellow';";
	}
	return "";
};

function returnQuarterClasYellow(num,selectHAY){
	if(num == (new Date().getFullYear()) && selectHAY == returnCurrentquarter()){
		return "class='yellow';";
	}
	return "";
};

function returnCurrentHalfYear(){
	if((new Date().getMonth() + 1) < 7){
		return "H1";
	}else{
		return "H2";
	}
};

function returnCurrentquarter(){
	if((new Date().getMonth() + 1) < 4){
		return "Q1";
	}else if((new Date().getMonth() + 1) < 7){
		return "Q2";
	}else if((new Date().getMonth() + 1) < 10){
		return "Q3";
	}else {
		return "Q4";
	}
};

function quarterJointStrYear(o){
	return "<tr>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_year_qty)) +"</th>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_year_qty)) +"</th>" +
				growthRate(o.year_growth) +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_Q1)) +"</th>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_Q1)) +"</th>" +
				growthRate(o.Q1_growth) +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_Q2)) +"</th>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_Q2)) +"</th>" +
				growthRate(o.Q2_growth) +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_Q3)) +"</th>" +
				"<th style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_Q3)) +"</th>" +
				growthRate(o.Q3_growth) +
			"</tr>";
};
function quarterJointStrTDYear(o,selectObj){
	var returnClasYellowArr = MarkCurrentTime(selectObj);
	return "<tr>" +
	"<td "+ returnClasYellowArr[0] +" style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_year_qty)) +"</td>" +
	"<td "+ returnClasYellowArr[1] +" style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_year_qty)) +"</td>" +
	growthRate(o.year_growth) +
	"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_Q1)) +"</td>" +
	"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_Q1)) +"</td>" +
	growthRate(o.Q1_growth) +
	"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_Q2)) +"</td>" +
	"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_Q2)) +"</td>" +
	growthRate(o.Q2_growth) +
	"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.last_Q3)) +"</td>" +
	"<td style='text-align: right;'>"+ toThous(isNullReturnZero(o.this_Q3)) +"</td>" +
	growthRate(o.Q3_growth) +
	"</tr>";
};

/**
 * 加载年度右边头部拼接
 * @param selectObj
 * @returns {String}
 */
function loadYearHtmlRightHeadJoint(selectObj){
	return "<table>" +
				"<thead>" +
					"<tr>" +
						"<th colspan='3'>"+ selectObj.selectDateVal +" VS "+ ((selectObj.selectDateVal) - 1) +"  Sell-Out</th>" +
						"<th colspan='3'>"+ selectObj.selectDateVal +" H1 VS "+ ((selectObj.selectDateVal) - 1) +" H1 Sell-Out</th>" +
						"<th colspan='3'>"+ selectObj.selectDateVal +" H2 VS "+ ((selectObj.selectDateVal) - 1) +" H2 Sell-Out</th>" +
					"</tr>" +
					"<tr>" +
						"<th>"+ ((selectObj.selectDateVal) - 1) +" Achieved</th>" +
						"<th>"+ selectObj.selectDateVal +" Achieved</th>" +
						"<th>Growth Rate%</th>" +
						"<th>"+ ((selectObj.selectDateVal) - 1) +" H1 Achieved</th>" +
						"<th>"+ selectObj.selectDateVal +" H1 Achieved</th>" +
						"<th>Growth Rate%</th>" +
						"<th>"+ ((selectObj.selectDateVal) - 1) +" H2 Achieved</th>" +
						"<th>"+ selectObj.selectDateVal +" H2 Achieved</th>" +
						"<th>Growth Rate%</th>" +
					"</tr>" +
				"</thead>" +
				"<tbody>";
};

/**
 * 加载半年度右边头部拼接
 * @param selectObj
 * @returns {String}
 */
function loadHalfAYearHtmlRightHeadJoint(selectObj){
	var sh;
	var eh;
	if(selectObj.selectHAY == 'H1'){
		sh = 'Q1';
		eh = 'Q2';
	}else{
		sh = 'Q3';
		eh = 'Q4';
	}
	
	return "<table>" +
	"<thead>" +
	"<tr>" +
	"<th colspan='3'>"+ selectObj.selectDateVal +" "+ selectObj.selectHAY +" VS "+ ((selectObj.selectDateVal) - 1) +" "+ selectObj.selectHAY +"  Sell-Out</th>" +
	"<th colspan='3'>"+ selectObj.selectDateVal +" " +sh+ " VS "+ ((selectObj.selectDateVal) - 1) +" " +sh+ " Sell-Out</th>" +
	"<th colspan='3'>"+ selectObj.selectDateVal +" " +eh+ " VS "+ ((selectObj.selectDateVal) - 1) +" " +eh+ " Sell-Out</th>" +
	"</tr>" +
	"<tr>" +
	"<th>"+ ((selectObj.selectDateVal) - 1) +" "+ selectObj.selectHAY +" Achieved</th>" +
	"<th>"+ selectObj.selectDateVal +" "+ selectObj.selectHAY +" Achieved</th>" +
	"<th>Growth Rate%</th>" +
	"<th>"+ ((selectObj.selectDateVal) - 1) +" " +sh+ " Achieved</th>" +
	"<th>"+ selectObj.selectDateVal +" " +sh+ " Achieved</th>" +
	"<th>Growth Rate%</th>" +
	"<th>"+ ((selectObj.selectDateVal) - 1) +" " +eh+ " Achieved</th>" +
	"<th>"+ selectObj.selectDateVal +" " +eh+ " Achieved</th>" +
	"<th>Growth Rate%</th>" +
	"</tr>" +
	"</thead>" +
	"<tbody>";
};

/**
 * 加载季度右边头部拼接
 * @param selectObj
 * @returns {String}
 */
function loadQuarterHtmlRightHeadJoint(selectObj){
	var oy;
	var twy;
	var thy;
	if(selectObj.selectHAY == 'Q1'){
		oy = '01';
		twy = '02';
		thy = '03';
	}else if(selectObj.selectHAY == 'Q2'){
		oy = '04';
		twy = '05';
		thy = '06';
	}else if(selectObj.selectHAY == 'Q3'){
		oy = '07';
		twy = '08';
		thy = '09';
	}else{
		oy = '10';
		twy = '11';
		thy = '12';
	}
	
	return "<table>" +
	"<thead>" +
	"<tr>" +
	"<th colspan='3'>"+ selectObj.selectDateVal +" "+ selectObj.selectHAY +" VS "+ ((selectObj.selectDateVal) - 1) +" "+ selectObj.selectHAY +"  Sell-Out</th>" +
	"<th colspan='3'>"+ selectObj.selectDateVal +" " +oy+ " VS "+ ((selectObj.selectDateVal) - 1) +" " +oy+ " Sell-Out</th>" +
	"<th colspan='3'>"+ selectObj.selectDateVal +" " +twy+ " VS "+ ((selectObj.selectDateVal) - 1) +" " +twy+ " Sell-Out</th>" +
	"<th colspan='3'>"+ selectObj.selectDateVal +" " +thy+ " VS "+ ((selectObj.selectDateVal) - 1) +" " +thy+ " Sell-Out</th>" +
	"</tr>" +
	"<tr>" +
	"<th>"+ ((selectObj.selectDateVal) - 1) +" "+ selectObj.selectHAY +" Achieved</th>" +
	"<th>"+ selectObj.selectDateVal +" "+ selectObj.selectHAY +" Achieved</th>" +
	"<th>Growth Rate%</th>" +
	"<th>"+ ((selectObj.selectDateVal) - 1) +" " +oy+ " Achieved</th>" +
	"<th>"+ selectObj.selectDateVal +" " +oy+ " Achieved</th>" +
	"<th>Growth Rate%</th>" +
	"<th>"+ ((selectObj.selectDateVal) - 1) +" " +twy+ " Achieved</th>" +
	"<th>"+ selectObj.selectDateVal +" " +twy+ " Achieved</th>" +
	"<th>Growth Rate%</th>" +
	"<th>"+ ((selectObj.selectDateVal) - 1) +" " +thy+ " Achieved</th>" +
	"<th>"+ selectObj.selectDateVal +" " +thy+ " Achieved</th>" +
	"<th>Growth Rate%</th>" +
	"</tr>" +
	"</thead>" +
	"<tbody>";
};

/**
 * 加载年度左边底部拼接
 * @param dataArr
 */
function loadYearHtmlLeftBottomJoint(dataArr){
	if(dataArr.length <=0 ){
		return "</tbody>" +
			"</table>";
	}
	
	var allHtmlStr = "";//业务中心 AND 国家
	var countryHtmlStr = "";//国家Html
	var center = dataArr[0].center;
	for(var i = 0;i < dataArr.length; i++){
		if(center == dataArr[i].center){
			countryHtmlStr += "<tr><td>"+ dataArr[i].country +"</td></tr>";
			if(dataArr.length - i == 1){
				allHtmlStr += "<tr><th>"+ dataArr[i].center +"</th></tr>" + countryHtmlStr;
			}
		}else{
			allHtmlStr += "<tr><th>"+ dataArr[i-1].center +"</th></tr>" + countryHtmlStr;
			center = dataArr[i].center;
			countryHtmlStr = "";
			i --;
		}
	}
	return allHtmlStr + "</tbody>" + "</table>";
};

/**
 * 加载年度左边头部拼接
 * @param selectObj
 * @returns {String}
 */
function loadYearHtmlLeftHeadJoint(selectObj){
	return "<table>" +
				"<thead>" +
					"<tr>" +
						"<th rowspan='2'>"+ selectObj.selectDateVal +" VS "+ ((selectObj.selectDateVal) - 1) +" Sell-Out</th>" +
					"</tr>" +
				"</thead>" +
				"<tbody>";
};

/**
 * 加载半年度左边头部拼接
 * @param selectObj
 * @returns {String}
 */
function loadHalfAYearHtmlLeftHeadJoint(selectObj){
	return "<table>" +
				"<thead>" +
					"<tr>" +
						"<th rowspan='2'>"+ selectObj.selectDateVal +" "+ selectObj.selectHAY +" VS "+ ((selectObj.selectDateVal) - 1) +" "+ selectObj.selectHAY +" Sell-Out</th>" +
					"</tr>" +
				"</thead>" +
			"<tbody>";
};

/**
 * set季度页面显示
 * @param tab
 * @param left
 * @param right
 */
function setQuarterHtml(tab,left,right){
	if('Total' == tab){
		$('#QuarterTotalLeft').html(left);
		$('#QuarterTotalRight').html(right);
		moveX('QuarterTotalRight','QuarterTotalLeft');
	}else if('UD' == tab){
		$('#QuarterUDLeft').html(left);
		$('#QuarterUDRight').html(right);
		moveX('QuarterUDRight','QuarterUDLeft');
	}else if('Smart' == tab){
		$('#QuarterSmartLeft').html(left);
		$('#QuarterSmartRight').html(right);
		moveX('QuarterSmartRight','QuarterSmartLeft');
	}else if('Big' == tab){
		$('#QuarterBigLeft').html(left);
		$('#QuarterBigRight').html(right);
		moveX('QuarterBigRight','QuarterBigLeft');
	}else if('Curved' == tab){
		$('#QuarterCurvedLeft').html(left);
		$('#QuarterCurvedRight').html(right);
		moveX('QuarterCurvedRight','QuarterCurvedLeft');
	}else if('X/C/P' == tab){
		$('#QuarterXCPLeft').html(left);
		$('#QuarterXCPRight').html(right);
		moveX('QuarterXCPRight','QuarterXCPLeft');
	}
	setSotitle();
};

/**
 * set半年度页面显示
 * @param tab
 * @param left
 * @param right
 */
function setHalfAYearHtml(tab,left,right){
	if('Total' == tab){
		$('#HalfyearTotalLeft').html(left);
		$('#HalfyearTotalRight').html(right);
		moveX('HalfyearTotalRight','HalfyearTotalLeft');
	}else if('UD' == tab){
		$('#HalfyearUDLeft').html(left);
		$('#HalfyearUDRight').html(right);
		moveX('HalfyearUDRight','HalfyearUDLeft');
	}else if('Smart' == tab){
		$('#HalfyearSmartLeft').html(left);
		$('#HalfyearSmartRight').html(right);
		moveX('HalfyearSmartRight','HalfyearSmartLeft');
	}else if('Big' == tab){
		$('#HalfyearBigLeft').html(left);
		$('#HalfyearBigRight').html(right);
		moveX('HalfyearBigRight','HalfyearBigLeft');
	}else if('Curved' == tab){
		$('#HalfyearCurvedLeft').html(left);
		$('#HalfyearCurvedRight').html(right);
		moveX('HalfyearCurvedRight','HalfyearCurvedLeft');
	}else if('X/C/P' == tab){
		$('#HalfyearXCPLeft').html(left);
		$('#HalfyearXCPRight').html(right);
		moveX('HalfyearXCPRight','HalfyearXCPLeft');
	}
	setSotitle();
};

/**
 * set年度页面显示
 * @param tab
 * @param left
 * @param right
 */
function setYearHtml(tab,left,right){
	if('Total' == tab){
		$('#YearTotalLeft').html(left);
		$('#YearTotalRight').html(right);
		moveX('YearTotalRight','YearTotalLeft');
	}else if('UD' == tab){
		$('#YearUdLeft').html(left);
		$('#YearUdRight').html(right);
		moveX('YearUdRight','YearUdLeft');
	}else if('Smart' == tab){
		$('#YearSmartLeft').html(left);
		$('#YearSmartRight').html(right);
		moveX('YearSmartRight','YearSmartLeft');
	}else if('Big' == tab){
		$('#YearBigLeft').html(left);
		$('#YearBigRight').html(right);
		moveX('YearBigRight','YearBigLeft');
	}else if('Curved' == tab){
		$('#YearCurvedLeft').html(left);
		$('#YearCurvedRight').html(right);
		moveX('YearCurvedRight','YearCurvedLeft');
	}else if('X/C/P' == tab){
		$('#YearXCPLeft').html(left);
		$('#YearXCPRight').html(right);
		moveX('YearXCPRight','YearXCPLeft');
	}
	setSotitle();
};

/**
 * 字符串为null则return 0
 * @param num
 */
function isNullReturnZero(num){
	
	if(typeof(num) == "undefined"){
		return 0;
	}
	if(typeof(num) == "string"){
		return !isStringNull(num) ? (num * 1):0;
	}
	return num;
}

/**
 * 计算增长率(今年-去年)/去年*100
 * @param lastQty
 * @param thisQty
 */
function getGrowthRate(lastQty,thisQty){
	if(lastQty == 0 && thisQty == 0){
		return "0";
	}else if(lastQty == 0){
		return "100";
	}else if(thisQty == 0){
		return "-100";
	}else{
		return (thisQty - lastQty) / lastQty * 100;
	}
}

/**
 * 增长率背景加载颜色
 * @param val
 * @returns {String}
 */
function growthRate(val){
	if((('') + val).indexOf("%") >= 0){
		val = val.substr(0,(('') + val).length -1);
	}
	
	if(('' + val).indexOf("-")>=0){
		return "<td  style='background-color: red;color:#fff;'>" + (val *1).toFixed(0) + "%</td>";
	}else{
		return "<td  style='background-color: green;color:#000;'>" + (val *1).toFixed(0) + "%</td>";
	}
};

/**
 * 四舍五入取整
 * @param num
 */
function toRound(num){
	return typeof(num) == "string" ? Math.round(num * 1) : Math.round(num);
};

/**
 * 整数千位符显示
 * @param num
 * @returns
 */
function toThous(num) {
	num = toRound(num);
    return (num || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
};
