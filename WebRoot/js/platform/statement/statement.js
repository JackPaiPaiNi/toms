var select_personnel_or_area = "";
var loginPartyId = "";//登录partyId
var logincoutryId = "";
var emptySelection = '<option></option>';
var pType = '1';

/**
 * 隐藏此代码，防止报错
 * @returns {Array}
 */
$(function(){
	selectParty();
	businessCenterSelect();
	countrySelect();
	regionSelect();
	officeSelect();
	userSelect();
	//initFunc();
	//Common_util.queryStoreLevel('storeLevel');
	
	/*$('#instrument').change(function(){
		var saleTime = getCorrespondingDates($("#instrument").val());
		if('area' == select_personnel_or_area){
			load(true,headSelect(),'selecPartySalerTar');
		}else{
			load(true,getIdAndName()[0],'selectUserSale',getIdAndName()[0],$('#isSorS').val());
		}
	});*/
});

function initFunc(){//初始化启动函数
	manuallyChooseSearchs();
	infoLoadDrawing();
	codeProuWhereSelect();
	
	funcWhereChange();
	yearCompared();
	differentSizesWhereSelect();
	salesPerformanceWhereSelect();
	salesProgressWhereSelect();
	personnelEfficiencyWhereSelect();
};

/**
 * Personnel Efficiency(人员效率条件抉择)
 */
function personnelEfficiencyWhereSelect(){
	$('#autoNamesAttr').change(function(){
		selectSizeByFunction($(this).val());
	});
	
	$('#per_eff_date,#per_eff_voOrRe,#sizeFunction').change(function(){
		if('area' != select_personnel_or_area){
			if(!isStringNull($("#supervisor").val())){
				selectUserSaleNumber('Supervisor');
			}else if(commonSelectObj().isBusinessManager){
				selectUserSaleNumber('Region head');
			}else{
				selectUserSaleNumber('Salesman');
			}
		}else{
			selectUserSaleNumber('Supervisor');
		}
	});
};

/**
 * different sizes(Size大小抉择)
 */
function differentSizesWhereSelect(){
	$("#sizeSale").change(function(){
		differentSizeTV();
	});
};

/**
 * Rangking Of Sales Achieving Rate(销售业绩范围条件抉择)
 */
function salesPerformanceWhereSelect (){
	$('#sales_rank').change(function(){
		loadSalesRank(headSelect());
	});
};

/**
 * Sales Progress(销售进展条件抉择)
 */
function salesProgressWhereSelect(){
	$('#area,#salesRanking').change(function(){
		loadShopOrSupervisorByPartyId();
	});
};

/**
 * Different Time Periods Contrast In A Region(年份对比条件选择)
 */
function yearCompared (){
	$('#contrastRole,#analysis,#volOrRev').change(function(){
		loadContrastAnalysis();
	});
};

/**
 * Different Functions(功能显示条件选择)
 */
function funcWhereChange(){
	$("#selectFunctionSaleTime").change(function(){
		differentFunctionsTV();
	});
};

/**
 * 用户抉择
 */
function userSelect(){
	/*督导*/
	$("#supervisor").change(function(){
		$("#my_menu").hide();
	});
	
	/*业务员*/
	$('#business_Mana').click(function(){
		if($('#business_Mana').val() != ''){
			$("#my_menu").show();
		}
	});
};

/**
 * 办事处抉择
 */
function officeSelect (){
	$("#office").change(function(){
		$("#my_menu").hide();
		clear();
		intermediary();
	});
};

/**
 * 地区抉择
 */
function regionSelect(){
	$("#region").change(function(){
		if($(this).val() != ''){
			selectOffice($(this).val());
		}else{
			$("#office").html("");
		}
		$("#my_menu").hide();
		clear();
	});
};

/**
 * 国家抉择
 */
function countrySelect(){
	$("#country").change(function(){
		soType="admin";
		Common_util.queryStoreLevel('storeLevel');
		if($(this).val() != ''){
			selectRegion($(this).val());
		}else{
			$("#office").html("");
			$("#region").html("");
		}
		$("#office").html("");
		$("#my_menu").hide();
		clear();
	});
};

/**
 * 商业中心抉择
 */
function businessCenterSelect (){
	$("#center").change(function(){
		if($(this).val() != ''){
			loadCountry($(this).val());
		}else{
			$("#office").html("");
			$("#region").html("");
			$("#country").html("");
		}
		$("#office").html("");
		$("#region").html("");
		$("#my_menu").hide();
		clear();
	});
};

/**
 * 重点产品条件赛选型号
 */
function codeProuWhereSelect(){
	//重点产品属性
	$('#majorFunction').change(function(){
		selectSeries($(this).val());
		selectModel();
	});
	
	//重点产品系列、尺寸
	$('#majorSeries,#majorSize').change(function(){
		selectModel();
	});
};

function infoLoadDrawing(){
	$('#loadingContrast').hide();
	$('#loadingFunction').hide();
	$('#loadingSize').hide();
	$('#loadingAllSaleVolume').hide();
	$('#loadingAllSaleRevenue').hide();
	$('#loadingSalesRank').hide();
	$('#loadingAreaSales').hide();
	$('#loadingStaffEfficiency').hide();
	$('#loadingCodeSalesRank').hide();
	$('#loadingSModelSalealesRank').hide();
};

/**
 * 手动选择查询
 */
function manuallyChooseSearchs(){
	$("#searchs").click(function(){
		var isSelect = false;
		var skipUrl = "";
		var saleTime = getCorrespondingDates($("#instrument").val());
		var id = "";
		var name = "";
		var  role = "";
		var isSOrSal = "";
		
		if($("#supervisor").val() != "" && $("#supervisor").val() != null){//督导是否选择
			skipUrl = "selectUserSale";
			id = $("#supervisor").val();
			isSelect = true;
			select_personnel_or_area = 'personnel';
			name = $('#supervisor>option:selected').html();
			role = 'supervisor';
		}else if($("#business_Mana").attr('userId') != "" && $("#business_Mana").attr('userId') != null){//业务经理是否选择
			skipUrl = "selectUserSale";
			id = $("#business_Mana").attr("userId");
			name = $("#business_Mana").val();
			isSelect = true;
			role = 'Region head';
			select_personnel_or_area = 'personnel';
			isSOrSal = $('#isSorS').val();
		}else if($("#office").val() != "" && $("#office").val() != null){//办事处是否选择
			skipUrl = "selecPartySalerTar";
			id = $("#office").val();
			select_personnel_or_area = 'area';
			isSelect = true;
		}else if($("#region").val() != "" && $("#region").val() != null){//地区是否选择
			skipUrl = "selecPartySalerTar";
			id = $("#region").val();
			isSelect = true;
			select_personnel_or_area = 'area';
		}else if($("#country").val() != "" && $("#country").val() != null){//国家是否选择
			skipUrl = "selecPartySalerTar";
			id = $("#country").val();
			isSelect = true;
			select_personnel_or_area = 'area';
		};
		
		if(id == '' && loginCountry != 999){
			skipUrl = "selecPartySalerTar";
			id = loginCountry;
			isSelect = true;
			select_personnel_or_area = 'area';
		};
		
		differentFunctionsTV();//功能
		loadContrastAnalysis();//年份比较
		if(!isStringNull($("#supervisor").val())){
			selectUserSaleNumber('Supervisor');
		}else if(commonSelectObj().isBusinessManager){
			selectUserSaleNumber('Region head');
		}else{
			selectUserSaleNumber('Salesman');
		}
		
		if(id != ""){
			if(skipUrl == 'selectUserSale'){
				load(isSelect,id,skipUrl,id,isSOrSal);
				personageSalesRank();
				personalEfficiency(id,name);
				itemSaleModelP(id);
				
				$('.fr-1').hide();
				$('.fr-2').hide();
				$('.fr-3').hide();
				$('.fr-1-1').hide();
				$('.fr-2-1').hide();
				$('.fr-3-1').hide();
				$('#contrastRole').hide();
				$('#area').hide();
				$('#sales_rank').hide();
			}else{
				load(isSelect,id,skipUrl);
				loadSalesRank(headSelect());
				itemSaleModel(headSelect());
				
				$('.fr-1').show();
				$('.fr-2').show();
				$('.fr-3').show();
				$('.fr-1-1').show();
				$('.fr-2-1').show();
				$('.fr-3-1').show();
				$('#contrastRole').show();
				$('#area').show();
				$('#sales_rank').show();
			}
			loadShopOrSupervisorByPartyId();
			selectSizeBySeries();
			productAttr();
			querySizeByAll();
		}
	});
}

function getIdAndName (){
	var id = "";
	var name = "";
	
	if($("#supervisor").val() != "" && $("#supervisor").val() != null){
		id = $("#supervisor").val();
		name = $('#supervisor>option:selected').html();
	}else if($("#business_Mana").attr('userId') != "" && $("#business_Mana").attr('userId') != null){
		id = $("#business_Mana").attr("userId");
		name = $("#business_Mana").val();
	}
	
	return [id,name];
}

function personalEfficiency (id,name){
	$('#loadingStaffEfficiency').show();
	personnelEfficiency();
}

function selectUserSaleNumber(role){
	$('#loadingStaffEfficiency').show();
	personnelEfficiency(role);
}


function assignmentName(nameArray,obj,supervisorDataArr){
	for(var i = 0;i<obj.length;i++){
		nameArray[i] = obj[i].countryName;
		supervisorDataArr [i] = 0;
	}
}

function productAttr(){//产品属性
	$.ajax({
		url:baseUrl + "platform/selectAttr.action",
		type:"POST",
		success:function(data){
			var optionStr = "";
			var obj =  eval('(' + data + ')');
			if(data != '' && data != null && obj.rows != "" && obj.rows != null){
				for(var i=0;i<obj.rows.length;i++){
					optionStr += "<option value="+obj.rows[i].name+">"+obj.rows[i].name+"</option>";
				}
				selectSeries(obj.rows[0].name);
				$('#autoNamesAttr').html(optionStr);
				$('#majorFunction').html(optionStr);
				selectSizeByFunction(obj.rows[0].name);
				selectModel();
			}else{
				$('#majorSeries').html("");
				$('#majorSize').html("");
				remove_the_core_model();
			}
		}
	});
}

function remove_the_core_model(){
	$('#id_select').html("");
	$($('[role=menu]')[0]).html("");
	$($('[data-id=id_select] span')[0]).html('');
}

function selectSeries(attrId){//产品系列
	$.ajax({
		url:baseUrl + "platform/selectSeries.action",
		type:"POST",
		data:{'attrId':attrId,'partyId':getCountryId()/*headSelect()*/},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data != '' && data != null && obj.rows != "" && obj.rows != null){
				var optionStr = emptySelection;
				for(var i=0;i<obj.rows.length;i++){
					optionStr += "<option value="+obj.rows[i].name+">"+obj.rows[i].name+"</option>";
				}
				$('#majorSeries').html(optionStr);
			}else{
				$('#majorSize').html("");
				remove_the_core_model();
			}
		}
	});
}

function selectSizeBySeries(series){
	$.ajax({
		url:baseUrl + "platform/selectSizeBySeries.action",
		type:"POST",
		data:{'series':series},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data != '' && data != null && obj.rows != "" && obj.rows != null){
				var optionStr = emptySelection;
				for(var i=0;i<obj.rows.length;i++){
					optionStr += "<option value="+obj.rows[i].name+">"+obj.rows[i].name+"</option>";
				}
				selectModel();
				$('#majorSize').html(optionStr);
			}else{
				remove_the_core_model();
			}
		}
	});
}

function selectSizeByFunction(functions){
	$.ajax({
		url:baseUrl + "platform/selectSizeByFunction.action",
		type:"POST",
		data:{'function':functions},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data != '' && data != null && obj.rows != "" && obj.rows != null){
				var optionStr = "";
				for(var i=0;i<obj.rows.length;i++){
					optionStr += "<option value="+obj.rows[i].size+">"+obj.rows[i].size+"</option>";
				}
				$('#sizeFunction').html(optionStr);
				
				if(!isStringNull($("#supervisor").val())){
					selectUserSaleNumber('Supervisor');
				}else if(commonSelectObj().isBusinessManager){
					selectUserSaleNumber('Region head');
				}else{
					selectUserSaleNumber('Salesman');
				}
				//selectUserSaleNumber('Supervisor');
			}
		}
	});
}

var initializeCodeModel = true;

function selectModel(){
	
	var o = {};
	o.size = $('#majorSize').val();
	o.series = $('#majorSeries').val();
	o.functions = $('#majorFunction').val();
	o.country = getCountryId();
	$.ajax({
		url:baseUrl + "platform/selectModelBy.action",
		type:"POST",
		data:o,
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data != '' && data != null && obj.rows != "" && obj.rows != null){
				var optionStr = "";
				if(initializeCodeModel){
					for(var i=0;i<obj.rows.length;i++){
						optionStr += "<option>"+obj.rows[i].name+"</option>";
					}
					$('#id_select').html(optionStr);
					$('#id_select').selectpicker({'selectedText': 'cat'});
					
					initializeCodeModel = false;
				}else{
					var opStr = "";
					for(var i=0;i<obj.rows.length;i++){
						optionStr += "<li id = 'liId' rel="+i+" class>" +
								     "<a tabindex='0' class style>" +
								     "<span class='text'>"+obj.rows[i].name+"</span>" +
								     "<i class='glyphicon glyphicon-ok check-mark'></i>" +
								     "</a></li>";
						opStr += "<option>"+obj.rows[i].name+"</option>";
					}
					$('#id_select').html(opStr);
					$($('[role=menu]')[0]).html(optionStr);
					$('[id=liId]').click(function(){
						
						if($(this).children('a').children('i').css('display') == 'none'){
							
							$(this).children('a').children('i').css('position','absolute');
							$(this).children('a').children('i').css('right','40px');
							$(this).children('a').children('i').css('display','inline-block');
						}else{
							$(this).children('a').children('i').css('display','none');
						}
					});
				}
			}else{
				$('#id_select').html("");
				$($('[role=menu]')[0]).html("");
				$($('[data-id=id_select] span')[0]).html('');
			}
		}
	});
}

function querySizeByAll(){
//	$.ajax({
//		url:baseUrl + "platform/queryAllSize.action",
//		type:"POST",
//		success:function(data){
//			var obj =  eval('(' + data + ')');
//			if(data != '' && data != null && obj.rows != "" && obj.rows != null){
//				var optionStr = "";
//				for(var i=0;i<obj.rows.length;i++){
//					optionStr += "<option>"+obj.rows[i].NAME+"</option>";
//				}
//				$('#id_size').html(optionStr);
//				$('#id_size').selectpicker({'selectedText': 'cat'});
//				
//			}
//		}
//	});
	
	$("#id_size").combobox({
		url:baseUrl + "platform/queryAllSize.action",
		valueField:'NAME',    
	    textField:'NAME'
	});
}


function salesRatioDate(datestr){
	var saleTime = '';
	var startTime = '';
	var endTime = '';
	if('week' == datestr){
		saleTime = Common_date.getMondayAndWeekend()[0];
		startTime = new Date().Format("yyyy-MM")+"-01";
		endTime = new Date(startTime);
		endTime.setMonth(endTime.getMonth() +1);
		endTime.setDate(0);
		endTime = endTime.Format("yyyy-MM-dd");
	}else if('month' == datestr){
		saleTime = Common_date.getEarlierMonth();
		startTime = new Date().Format("yyyy-MM")+"-01";
		endTime = new Date(startTime);
		endTime.setMonth(endTime.getMonth() +1);
		endTime.setDate(0);
		endTime = endTime.Format("yyyy-MM-dd");
	}else if('quarter' == datestr){
		saleTime = Common_date.getQuarterDate()[0];
		startTime = Common_date.getQuarterDate()[0];
		endTime = Common_date.getQuarterDate()[1];
	}else if('year' == datestr){
		saleTime = Common_date.getTheFirstDay()[0];
		startTime = Common_date.getTheFirstDay()[0];
		endTime = Common_date.getTheFirstDay()[1];
	};
	
	return [saleTime,startTime,endTime];
};

function toThousands(num) {//千位符
    return (num || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
};

function formatNumber(num,cent,isThousand) {  //千位符
    
    // 检查传入数值为数值类型  
     if(isNaN(num))  
      num = "0";  
   
    // 获取符号(正/负数)  
    sign = (num == (num = Math.abs(num)));  
   
    num = Math.floor(num*Math.pow(10,cent)+0.50000000001); // 把指定的小数位先转换成整数.多余的小数位四舍五入  
    cents = num%Math.pow(10,cent);       // 求出小数位数值  
    num = Math.floor(num/Math.pow(10,cent)).toString();  // 求出整数位数值  
    cents = cents.toString();        // 把小数位转换成字符串,以便求小数位长度  
   
    // 补足小数位到指定的位数  
    while(cents.length<cent)  
     cents = "0" + cents;  
   
    if(isThousand) {  
     // 对整数部分进行千分位格式化.  
     for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
      num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  
    }  
   
    if (cent > 0)  
     return (((sign)?'':'-') + num + '.' + cents);  
    else  
     return (((sign)?'':'-') + num);  
   }  

function load(isSelect,id,skipUrl,userId,isSOrSal){
	salesRatioLoadTheBarcode('show');
	if(isSelect){
		var selectDateArr = salesRatioDate($("#instrument").val());
		var o = {};
		o.id = id;
		o.saleTime = selectDateArr[0];
		o.userId = userId;
		o.country = getPartyId();
		o.isSOrSal = isSOrSal;
		o.startTime = selectDateArr[1];
		o.endTime = selectDateArr[2];
		o.isSelectCountry = commonSelectObj().isSelectCountry;
		o.pType = pType;
		o.level = $("#storeLevel").val();
		
		$.ajax({
			url:baseUrl + "platform/"+skipUrl+".action",
			type:"POST",
			data:o,
			success:function(data){
				if(data !=  "" && data != null){
					var obj =  eval('(' + data + ')');
					
					var volumeSale = (obj.rows[0].volume/obj.coefficient[0].allCoeffinient).toFixed(0);
					$("#volumeSale").html(formatNumber(volumeSale,2,1));
					var volumeSat = (obj.rows[1].volume).toFixed(0);
					$("#volumeSat").html(formatNumber(volumeSat,2,1));
					
					if(volumeSale != 0 && volumeSat != 0){
						var num = volumeSale/volumeSat;
						instrumentpanel("amount",(num * 100).toFixed(3),'Volume');
					}else{
						instrumentpanel("amount",0,'Volume');
					}
					
					var revenueSale = (obj.rows[0].revenue/obj.coefficient[0].allCoeffinient).toFixed(3);
					$("#revenueSale").html(formatNumber(revenueSale,2,1));
					var revenueSat = (obj.rows[1].revenue).toFixed(3);
					$("#revenueSat").html(formatNumber(revenueSat,2,1));
					
					if(revenueSale != 0 && revenueSat != 0){
						var num = revenueSale/revenueSat;
						instrumentpanel("money",(num*100).toFixed(3),'Revenue');
					}else{
						instrumentpanel("money",0,'Revenue');
					}
				}
			}
		});
	}
}

function loadShopOrSupervisorByPartyId(){//区域渠道商店销售 数量
	LoadSalesProgress();
}

function initializeTheArr(arr,length){
	for(var i = 0;i<length;i++){
		arr[i] = 0;
	}
}

function loadPersonageAreaSaleNumber(obj,id,weekDataArr,monthDataArr,quarterDataArr,yearDataArr){

	for(var i=0;i<obj.rows.length;i++){
		//-----------周
		if(new Date(obj.rows[i].dataDate).getTime() >= new Date(getMondayAndWeekend()[0]).getTime() && new Date(obj.rows[i].dataDate).getTime() <= new Date(getMondayAndWeekend()[1]).getTime()){
			weekDataArr[0] += obj.rows[i].volume; 
		}
		
		//----------月
		if((obj.rows[i].dataDate).indexOf(new Date().Format('MM')) == 5 ){
			monthDataArr[0] += obj.rows[i].volume; 
		}
		
		//----------季度
		if(new Date(obj.rows[i].dataDate).getTime() >= new Date(getQuarterDate(new Date().getMonth()+1)[0]).getTime() && new Date(obj.rows[i].dataDate).getTime() <= new Date(getQuarterDate(new Date().getMonth()+ 1)[1]).getTime()){
			quarterDataArr[0] += obj.rows[i].volume; 
		}
		
		//----------年
		yearDataArr[0] += obj.rows[i].volume; 
	}
}

function loadAreaSaleNumber(obj,idArray,weekDataArr,monthDataArr,quarterDataArr,yearDataArr){
	initializeTheArr(weekDataArr,idArray.length);
	initializeTheArr(monthDataArr,idArray.length);
	initializeTheArr(quarterDataArr,idArray.length);
	initializeTheArr(yearDataArr,idArray.length);
	
	for(var i=0;i<obj.rows.length;i++){
		//-----------周
		if(new Date(obj.rows[i].dataDate).getTime() >= new Date(getMondayAndWeekend()[0]).getTime() && new Date(obj.rows[i].dataDate).getTime() <= new Date(getMondayAndWeekend()[1]).getTime()){
			for(var j=0;j<idArray.length;j++){
				if(idArray[j] == obj.rows[i].id){
					weekDataArr[j] += obj.rows[i].volume; 
					break;
				}
			}
		}
		
		//----------月
		if((obj.rows[i].dataDate).indexOf(new Date().Format('MM')) == 5 ){
			for(var j=0;j<idArray.length;j++){
				if(idArray[j] == obj.rows[i].id){
					monthDataArr[j] += obj.rows[i].volume; 
					break;
				}
			}
		}
		
		//----------季度
		
		if(new Date(obj.rows[i].dataDate).getTime() >= new Date(getQuarterDate(new Date().getMonth()+1)[0]).getTime() && new Date(obj.rows[i].dataDate).getTime() <= new Date(getQuarterDate(new Date().getMonth()+ 1)[1]).getTime()){
			for(var j=0;j<idArray.length;j++){
				if(idArray[j] == obj.rows[i].id){
					quarterDataArr[j] += obj.rows[i].volume; 
					break;
				}
			}
		}
		
		//----------年
		for(var j=0;j<idArray.length;j++){
			if(idArray[j] == obj.rows[i].id){
				yearDataArr[j] += obj.rows[i].volume; 
				break;
			}
		}
	}
}

function getQuarterDate(month){//获取本季度
	if(month >= 1 && month <= 3){
		return [new Date().getFullYear()+"-01-01",new Date().getFullYear()+"-03-31"];
	}else if(month >= 4 && month <= 6){
		return [new Date().getFullYear()+"-04-01",new Date().getFullYear()+"-06-30"];
	}else if(month >= 7 && month <= 9){
		return [new Date().getFullYear()+"-07-01",new Date().getFullYear()+"-09-30"];
	}else if(month >= 10 && month <= 12){
		return [new Date().getFullYear()+"-10-01",new Date().getFullYear()+"-12-31"];
	}
}

function getMondayAndWeekend(){//获取本周周一和本周周末
	var getDays = function ()
    {
        var now = new Date;
        var day = now.getDay ();
        var week = "7123456";
        var first = 0 - week.indexOf (day);
        var f = new Date;
        f.setDate (f.getDate () + first);
        var last = 6 - week.indexOf (day);
        var l = new Date;
        l.setDate (l.getDate () + last);
        return [
                f, l
        ];
    }
	
	var startTime = "";
	var engTime = "";
	
	
	var t = getDays()[0];
	t.setDate(getDays()[0].getDate()+1);
	t.setMonth(getDays()[0].getMonth());
	t.setFullYear(getDays()[0].getFullYear());
	
	startTime = t.Format("yyyy-MM-dd");
	
	t = getDays()[1];
	t.setDate(getDays()[1].getDate()+1);
	t.setMonth(getDays()[1].getMonth());
	t.setFullYear(getDays()[1].getFullYear());
	
	engTime = t.Format("yyyy-MM-dd");
	
	return [startTime,engTime];
}

function headSelect(){
	var id = "";
	if($("#office").val() != "" && $("#office").val() != null){//办事处是否选择
		id = $("#office").val();
	}else if($("#region").val() != "" && $("#region").val() != null){//地区是否选择
		id = $("#region").val();
	}else if($("#country").val() != "" && $("#country").val() != null){//国家是否选择
		id = $("#country").val();
	}
	if(id == ''){
		return loginPartyId;
	}
	return id;
}

var initializeSaleCodeModel = true;

function itemSaleModelP(userId){
	
	$.ajax({
		url:baseUrl + "platform/selectItemSaleInfo.action",
		type:"POST",
		data:{"userId":userId,'tDate':new Date().getFullYear()+"-01-01",'isSorS':$('#isSorS').val(),'pType':pType},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.model != "" && obj.model != null){
				var optionStr = "";
				
				if(initializeSaleCodeModel){
					
					for(var i = 0;i<obj.model.length;i++){
						optionStr += "<option>"+obj.model[i].name+"</option>";
						
					}
					$('#id_item').html(optionStr);
					$('#id_item').selectpicker({'selectedText': 'cat'});
					
					initializeSaleCodeModel = false;
				}else{
					var opStr = "";
					for(var i = 0;i<obj.model.length;i++){
						optionStr += "<li id = 'liIdS' rel="+i+" class>" +
								     "<a tabindex='0' class style>" +
								     "<span class='text'>"+obj.model[i].name+"</span>" +
								     "<i class='glyphicon glyphicon-ok check-mark'></i>" +
								     "</a></li>";
						
						opStr += "<option>"+obj.model[i].name+"</option>";
					}
					$('#id_item').html(opStr);
					$($('[role=menu]')[1]).html(optionStr);
					$('#id_item').selectpicker({'selectedText': 'cat'});
					
					$('[id=liIdS]').click(function(){
						
						if($(this).children('a').children('i').css('display') == 'none'){
							
							$(this).children('a').children('i').css('position','absolute');
							$(this).children('a').children('i').css('right','40px');
							$(this).children('a').children('i').css('display','inline-block');
						}else{
							$(this).children('a').children('i').css('display','none');
						}
					});
				}
			}else{
				$('#id_item').html("");
				$($('[role=menu]')[1]).html("");
				$($('[data-id=id_item] span')[0]).html('');
			}
		}
	});
}


function getCountryId(){
	var countryId = $('#country').val();
	if(countryId != null && countryId != ''){
		return countryId;
	}else{
		return loginPartyId;
	}
}


function itemSaleModel(partyId){
	$.ajax({
		url:baseUrl + "platform/selectItemSaleInfo.action",
		type:"POST",
		data:{"partyId":partyId,'tDate':new Date().getFullYear()+"-01-01",'isSorS':$('#isSorS').val(),'country':getPartyId(),'pType':pType},
		success:function(data){
			
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.model != "" && obj.model != null){
				var optionStr = "";
				if(initializeSaleCodeModel){
					
					for(var i = 0;i<obj.model.length;i++){
						optionStr += "<option>"+obj.model[i].name+"</option>";
					}
					$('#id_item').html(optionStr);
					$('#id_item').selectpicker({'selectedText': 'cat'});
					initializeSaleCodeModel = false;
				}else{
					var opStr = "";
					for(var i = 0;i<obj.model.length;i++){
						optionStr += "<li id = 'liIdS' rel="+i+" class>" +
								     "<a tabindex='0' class style>" +
								     "<span class='text'>"+obj.model[i].name+"</span>" +
								     "<i class='glyphicon glyphicon-ok check-mark'></i>" +
								     "</a></li>";
						
						opStr += "<option>"+obj.model[i].name+"</option>";
					}
					$('#id_item').html(opStr);
					$($('[role=menu]')[1]).html(optionStr);
					$('#id_item').selectpicker({'selectedText': 'cat'});
					
					$('[id=liIdS]').click(function(){
						
						if($(this).children('a').children('i').css('display') == 'none'){
							
							$(this).children('a').children('i').css('position','absolute');
							$(this).children('a').children('i').css('right','40px');
							$(this).children('a').children('i').css('display','inline-block');
						}else{
							$(this).children('a').children('i').css('display','none');
						}
					});
				}
			}else{
				$('#id_item').html("");
				$($('[role=menu]')[1]).html("");
				$($('[data-id=id_item] span')[0]).html('');
			}
		}
	});
}

function loadItemSaleInfo(value){
	if(value == 1){
		$('#loadingCodeSalesRank').show();
		coreProducts();
	}else{
		$('#loadingSModelSalealesRank').show();
		loadItemSold();
	}
}

function initSelectData(oneArr,twoArr,leng){
	for(var i =0;i<leng;i++){
		oneArr[i] = 0;
		twoArr[i] = 0;
	}
}

function selectWeek(rows,oneArr,twoArr,threeArr,fourArr,fiveArr,yearArr,timeArr){
	
	initSelectData(oneArr,twoArr,threeArr,fourArr,fiveArr,7);
	
	for(var i = 0;i<rows.length;i++){
		if(rows[i].dataDate.indexOf(yearArr[0]) > -1){
			for(var j = 0;j<timeArr.length;j++){
				if(timeArr[j].indexOf(rows[i].dataDate.substr(5))> -1){
					oneArr [j] = rows[i].volume;
					break;
				}
			}
		}else if(rows[i].dataDate.indexOf(yearArr[1]) > -1){
			for(var j = 0;j<timeArr.length;j++){
				if(timeArr[j].indexOf(rows[i].dataDate.substr(5))> -1){
					twoArr [j] = rows[i].volume;
					break;
				}
			}
		}else if(rows[i].dataDate.indexOf(yearArr[2]) > -1){
			for(var j = 0;j<timeArr.length;j++){
				if(timeArr[j].indexOf(rows[i].dataDate.substr(5))> -1){
					threeArr [j] = rows[i].volume;
					break;
				}
			}
		}else if(rows[i].dataDate.indexOf(yearArr[3]) > -1){
			for(var j = 0;j<timeArr.length;j++){
				if(timeArr[j].indexOf(rows[i].dataDate.substr(5))> -1){
					fourArr [j] = rows[i].volume;
					break;
				}
			}
		}else if(rows[i].dataDate.indexOf(yearArr[4]) > -1){
			for(var j = 0;j<timeArr.length;j++){
				if(timeArr[j].indexOf(rows[i].dataDate.substr(5)) > -1){
					fiveArr [j] = rows[i].volume;
					break;
				}
			}
		}
	}
}

function personageSalesRank(){
	$('#loadingSalesRank').show();
	var year = getCorrespondingDates("year");//年
	var quarter = getCorrespondingDates("quarter");//季度
	var month = getCorrespondingDates("month");//月
	var week = getCorrespondingDates("week");//周
	
	var targMonStart = new Date().Format("yyyy-MM")+"-01";
	var targMonEnd = new Date(targMonStart);
	targMonEnd.setMonth(targMonEnd.getMonth() +1);
	targMonEnd.setDate(0);
	targMonEnd = targMonEnd.Format("yyyy-MM-dd");
	
	var targQuarterStart = Common_date.getQuarterDate()[0];
	var targQuarterEnd = Common_date.getQuarterDate()[1];
	
	var targYearStart = Common_date.getTheFirstDay()[0];
	var targYearEnd = Common_date.getTheFirstDay()[1];
	
	var type = isStringNull($('#supervisor').val())? 0 : 2;
	
	var o = {};
	o.partyId = headSelect();
	o.type = type;
	o.year = year;
	o.quarter = quarter;
	o.saleType = 'personage';
	o.targMonStart = targMonStart;
	o.targMonEnd = targMonEnd;
	o.targQuarterStart = targQuarterStart;
	o.targQuarterEnd = targQuarterEnd;
	o.targYearStart = targYearStart;
	o.targYearEnd = targYearEnd;
	o.month = month;
	o.week = week;
	o.country = getPartyId();
	o.isSelectCountry = commonSelectObj().isSelectCountry;
	o.userId = getIdAndName()[0];
	o.userName = getIdAndName()[1];
	o.country = getPartyId();
	o.isSorS = $('#isSorS').val();
	o.pType = pType;
	o.level = $("#storeLevel").val();
	var nameArray = [];
	var weekDataArr = [];
	var weekDataArrIndex = 0;
	var monthDataArr = [];
	var monthDataArrIndex = 0;
	var quarterDataArr = [];
	var quarterDataArrIndex = 0;
	var yearDataArr = [];
	var yearDataArrIndex = 0;
	$.ajax({
		url:baseUrl + "platform/selectSaleRank.action",
		type:"POST",
		data:o,
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				
				var arrayLength = obj.rows.length / 4;//每个数组平均长度
				var index = 0;//总数据下标
				
				for(i = 0; i<arrayLength; i++){
					nameArray[i] = obj.rows[index].countryName;//收集名称
					
					if(obj.rows[index].target != 0 && obj.rows[index].volume != 0){//年数据
						
						var num = obj.rows[index].volume/obj.rows[index].target/*/obj.coefficient[0].allCoeffinient*/;
						yearDataArr[i] = (num * 100).toFixed(3);
					}else{
						yearDataArr[i] = 0;
					}
					index ++;
				}
				
				for(i = 0; i<arrayLength; i++){
					
					if(obj.rows[index].target != 0 && obj.rows[index].volume != 0){//季度
						var num = obj.rows[index].volume/obj.rows[index].target/*/obj.coefficient[0].allCoeffinient*/;
						quarterDataArr[i] = (num * 100).toFixed(3);
					}else{
						quarterDataArr[i] = 0;
					}
					index ++;
				}
				
				for(i = 0; i<arrayLength; i++){
					
					if(obj.rows[index].target != 0 && obj.rows[index].volume != 0){//月
						var num = obj.rows[index].volume/obj.rows[index].target/*/obj.coefficient[0].allCoeffinient*/;
						monthDataArr[i] = (num * 100).toFixed(3);
					}else{
						monthDataArr[i] = 0;
					}
					index ++;
				}
				
				for(i = 0; i<arrayLength; i++){
					
					if(obj.rows[index].target != 0 && obj.rows[index].volume != 0){//周
						var num = obj.rows[index].volume/obj.rows[index].target/*/obj.coefficient[0].allCoeffinient*/;
						weekDataArr[i] = (num * 100).toFixed(3);
					}else{
						weekDataArr[i] = 0;
					}
					index ++;
				}
			}
			salePercentageNog('sales_rank',nameArray,weekDataArr,monthDataArr,quarterDataArr,yearDataArr,'Sales Percentage（%）');
		}
	});
}

function loadSalesRank(partyId){//销售百分比
	$('#loadingSalesRank').show();
	var value = $("#sales_rank").val();
	var selectPartyURL = "";
	var type = "";
	if(value == 'businessManager'){//业务经理
		type = '-1';
	}else if(value == 'salesman'){//业务员
		type = '0';
	}else if(value == 'supervisor'){//督导
		type = '2';
	}
	
	var year = getCorrespondingDates("year");//年
	var quarter = getCorrespondingDates("quarter");//季度
	var month = getCorrespondingDates("month");//月
	var week = getCorrespondingDates("week");//周
	
	var targMonStart = new Date().Format("yyyy-MM")+"-01";
	var targMonEnd = new Date(targMonStart);
	targMonEnd.setMonth(targMonEnd.getMonth() +1);
	targMonEnd.setDate(0);
	targMonEnd = targMonEnd.Format("yyyy-MM-dd");
	
	var targQuarterStart = Common_date.getQuarterDate()[0];
	var targQuarterEnd = Common_date.getQuarterDate()[1];
	
	var targYearStart = Common_date.getTheFirstDay()[0];
	var targYearEnd = Common_date.getTheFirstDay()[1];
	
	var nameArray = [];
	var weekDataArr = [];
	var weekDataArrIndex = 0;
	var monthDataArr = [];
	var monthDataArrIndex = 0;
	var quarterDataArr = [];
	var quarterDataArrIndex = 0;
	var yearDataArr = [];
	var yearDataArrIndex = 0;
	
	var o = {};
	o.partyId = partyId;
	o.saleType = type;
	o.year = year;
	o.quarter = quarter;
	o.targMonStart = targMonStart;
	o.targMonEnd = targMonEnd;
	o.targQuarterStart = targQuarterStart;
	o.targQuarterEnd = targQuarterEnd;
	o.targYearStart = targYearStart;
	o.targYearEnd = targYearEnd;
	o.month = month;
	o.week = week;
	o.country = getPartyId();
	o.isSelectCountry = commonSelectObj().isSelectCountry;
	o.pType = pType;
	o.level = $("#storeLevel").val();
	$.ajax({
		url:baseUrl + "platform/selectSaleRank.action",
		type:"POST",
		data:o,
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				
				var arrayLength = obj.rows.length / 4;//每个数组平均长度
				var index = 0;//总数据下标
				
				for(i = 0; i<arrayLength; i++){
					nameArray[i] = obj.rows[index].countryName;//收集名称
					
					if(obj.rows[index].target != 0 && obj.rows[index].volume != 0){//年数据
						var num = obj.rows[index].volume/obj.rows[index].target/*/obj.coefficient[0].allCoeffinient*/;
						yearDataArr[i] = (num * 100).toFixed(3);
					}else{
						yearDataArr[i] = 0;
					}
					index ++;
				}
				
				for(i = 0; i<arrayLength; i++){
					
					if(obj.rows[index].target != 0 && obj.rows[index].volume != 0){//季度
						var num = obj.rows[index].volume/obj.rows[index].target/*/obj.coefficient[0].allCoeffinient*/;
						quarterDataArr[i] = (num *100).toFixed(3);
					}else{
						quarterDataArr[i] = 0;
					}
					index ++;
				}
				
				for(i = 0; i<arrayLength; i++){
					
					if(obj.rows[index].target != 0 && obj.rows[index].volume != 0){//月
						var num = obj.rows[index].volume/obj.rows[index].target/*/obj.coefficient[0].allCoeffinient*/;
						monthDataArr[i] = (num * 100).toFixed(3);
					}else{
						monthDataArr[i] = 0;
					}
					index ++;
				}
				
				for(i = 0; i<arrayLength; i++){
					
					if(obj.rows[index].target != 0 && obj.rows[index].volume != 0){//周
						var num = obj.rows[index].volume/obj.rows[index].target/*/obj.coefficient[0].allCoeffinient*/;
						weekDataArr[i] = (num *100).toFixed(3);
					}else{
						weekDataArr[i] = 0;
					}
					index ++;
				}
			}
			salePercentageNog('sales_rank',nameArray,weekDataArr,monthDataArr,quarterDataArr,yearDataArr,'Sales Percentage（%）');
		}
	});
}

/*操作业务员显示值*/
function getDivAttr(id,name){
	$("#business_Mana").attr("userId",id);
	$("#business_Mana").val(name);
	$("#my_menu").hide();
};

/*业务员插件生成*/
function loadUser(){
	var myMenu;
	myMenu = new SDMenu("my_menu");
	myMenu.init();
	var firstSubmenu = myMenu.submenus[0];
	myMenu.expandMenu(firstSubmenu);
}

function getPartyId(){
	return $("#country").val();
}

//是否是总部用户
function selectParty(){
	$.ajax({
		url:baseUrl + "platform/selectCountry.action",
		type:"POST",
		data:{"id":my_login_id},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				if(obj.rows[0].countryId == '999'){//总部用户
					loadBusinessCenter();
				}else if(obj.rows[0].id == '999'){//业务中心用户
					loadCountry(obj.rows[0].countryId);
				}else if(obj.rows[0].countryId == obj.rows[0].assistCountryId){//国家用户
					selectRegion(obj.rows[0].countryId);
				}else{
					$.ajax({
						url:baseUrl + "platform/selectRegion.action",
						type:"POST",
						data:{"partyId":obj.rows[0].countryId},
						success:function(data){
							var obj =  eval('(' + data + ')');
							if(obj.rows.length != 0){//经营部用户
								selectOffice(obj.rows[0].countryId);
							}else{//办事处用户
								intermediary();
							}
						}
					});
				}
				loginPartyId = obj.rows[0].countryId;
				logincoutryId = obj.rows[0].countryId;
				//初始化创建ture 只运行一次
				/*if(obj.rows[0].countryId != '999' && obj.rows[0].id != '999'){//总部用户或商业中心用户需选择国家才查询
					load(true,obj.rows[0].countryId,'selecPartySalerTar');//初始化仪表盘
					loadSalesRank(obj.rows[0].countryId);//初始化销售百分比
					loadShopOrSupervisorByPartyId();
					loadContrastAnalysis();//loadRegionalComparison(obj.rows[0].countryId);//年份对比
					differentFunctionsTV();//differentFunctionsTV(obj.rows[0].countryId);//功能对比
					itemSaleModel(obj.rows[0].countryId);
					select_personnel_or_area = 'area';
					productAttr();
					selectSizeBySeries();
					querySizeByAll();
				}*/
			}
		}
	});
}

/*加载商业中心*/
function loadBusinessCenter(){
	$.ajax({
		url:baseUrl + "platform/selectBusinessCenter.action",
		type:"POST",
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var partyManaStr = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					partyManaStr += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#center").html(partyManaStr);
			}else{
				$("#center").html("");
			}
		}
	});
}

//加载国家
function loadCountry(partyId){
	$.ajax({
		url:baseUrl + "platform/selectAllCountry.action",
		type:"POST",
		data:{"id":partyId},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var parStrObj = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					parStrObj += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#country").html(parStrObj);
			}else{
				$("#country").html("");
			}
		}
	});
}

//加载地区
function selectRegion(partyId){
	$.ajax({
		url:baseUrl + "platform/selectAllCountry.action",
		type:"POST",
		data:{"id":partyId},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var regStrObj = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					regStrObj += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#region").html(regStrObj);
			}else{
				$("#region").html("");
			}
		}
	});
}

//加载办事处
function selectOffice(partyId){
	$.ajax({
		url:baseUrl + "platform/selectAllCountry.action",
		type:"POST",
		data:{"id":partyId},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var regStrObj = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					regStrObj += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#office").html(regStrObj);
			}else{
				$("#office").html("");
			}
		}
	});
}

/*加载督导、业务员和业务经理中介*/
function intermediary(){
	var center = $("#center").val();
	var country = $("#country").val();
	var region = $("#region").val(); 
	var office = $("#office").val();
	if(office != "" && office != null){
		loadSupervisor(office);
		loadSalesman(office);
	}
}

//加载督导
function loadSupervisor(value){
	$.ajax({
		url:baseUrl + "platform/selectSalerType.action",
		type:"POST",
		data:{"partyId":value,
			  "saleType":2,
			  'isCountry':commonSelectObj().isSelectCountry
		},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var supervisorStr = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					supervisorStr += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#supervisor").html(supervisorStr);
			}
		}
	});
}

//加载业务员
function loadSalesman(value){
	var isNUll = false;
	$.ajax({
		url:baseUrl + "platform/selectSalerType.action",
		type:"POST",
		data:{"partyId":value,
			  "saleType":0,
			  'isCountry':commonSelectObj().isSelectCountry
		},
		success:function(data){
			var obj =  eval('(' + data + ')');
			var salesmanStr = "<span>Salesman</span>"/*+
				"<a href='#' isSupOrSal='sal'  id='jmsanchez'>Joseph Sanchez</a>";//-----------------------test
				isNUll = true*/;//-----------------------test;
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				isNUll = true;
				for(var i = 0;i<obj.rows.length;i++){
					salesmanStr += "<a href='#' isSupOrSal='sal' id="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</a>";
				}
			}
			$("#salesman").html(salesmanStr);
			loadPartyManager(value,isNUll);
		}
	});
}

//加载区域经理 
function loadPartyManager (value,isNUll){
	$.ajax({
		url:baseUrl + "platform/selectPartyManage.action",
		type:"POST",
		data:{"partyId":value,'isCountry':commonSelectObj().isSelectCountry},
		success:function(data){
			var obj =  eval('(' + data + ')');
			var salesmanStr = "<span>RegionalManager</span>"/*+
					"<a href='#' isSupOrSal='sup'  id='jmsanchez'>Joseph Sanchez</a>";//-----------------------test
					isNUll = true*/;//-----------------------test
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				isNUll = true;
				for(var i = 0;i<obj.rows.length;i++){
					salesmanStr += "<a href='#' isSupOrSal='sup'  id="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</a>";
				}
			}
			$("#partyManager").html(salesmanStr);
			
			if(isNUll){
				$("#business_Mana").val("SELECT");
				loadUser();
				$("#my_menu a").click(function(){
					$("#business_Mana").val($(this).html());
					$('#business_Mana').attr('userId',$(this).attr('id'));
					$("#my_menu").hide();
					$('#isSorS').val($(this).attr('isSupOrSal'));
				});
			}else{
				clear();
			}
		}
	});
}

/*清理督导、业务员*/
function clear(){
	$("#salesman").html("");
	$("#partyManager").html("");
	$("#business_Mana").val("");
	$("#business_Mana").attr("userId","");
	$("#supervisor").html("");
}


function nogT(byId,nameArray,supervisorDataArr,comment,name){
	var myChart = echarts.init($("[name='"+byId+"']")[0]);
	option = {
	    title : {
	        text: comment,
	       /* subtext: '数据来自网络'*/
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType: {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    grid: {
	        left: '5%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'value',
	            boundaryGap : [0, 0.01]
	        }
	    ],
	    
	    yAxis : [
	        {
	            type : 'category',
	            data : nameArray
	        }
	    ],
	    series : [
	        {
	            name:name,
	            type:'bar',
	            data:supervisorDataArr
	        }
	    ]
	};
	$('#loadingStaffEfficiency').hide();
	myChart.setOption(option);
	
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

function loadFunctionLineChar(name,texts,yearArr,timeArr,showArr,selectedObj){//功能折线图
	var myChart = echarts.init($("[name='"+name+"']")[0]); 
	option = {
	    title: {
	        text: texts
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:yearArr,
	        selected:selectedObj
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    toolbox: {
	        feature: {
	            saveAsImage: {}
	        },
	        show : true
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: timeArr
	    },
	    yAxis: {
	        type: 'value',
//	        minInterval: 1
	        
	    },
	    
	    series: showArr
	};
	
	$('#loadingFunction').hide();
	
	myChart.setOption(option); 
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

function loadSizeLineChar(name,texts,yearArr,timeArr,showArr,o){//尺寸折线图
	var myChart = echarts.init($("[name='"+name+"']")[0]); 
	option = {
	    title: {
	        text: texts
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:yearArr,
	        selected: o
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    toolbox: {
	        feature: {
	            saveAsImage: {}
	        },
	        show : true
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: timeArr
	    },
	    yAxis: {
	        type: 'value',
//	        minInterval:1
	    },
	    series: showArr
	};
	
	$('#loadingSize').hide();
	
	myChart.setOption(option); 
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

function loadModelSaleLineChar(name,texts,yearArr,timeArr,showArr){//型号销售折现图（图2）
	var myChart = echarts.init($("[name='"+name+"']")[0]); 
	option = {
	    title: {
	        text: texts
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:yearArr
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    toolbox: {
	        feature: {
	            saveAsImage: {}
	        },
	        show : true
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: timeArr
	    },
	    yAxis: {
	        type: 'value',
//	        minInterval: 1
	    },
	    series: showArr
	};
	$('#loadingSModelSalealesRank').hide();
	myChart.setOption(option); 
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}



function getCorrespondingDates(instrument){
	var myDate = new Date();
	if(instrument == "year"){//年
		return new Date().getFullYear()+"-01-01";
	}else if(instrument == "quarter"){//季度
		return getQuarterDate(new Date().getMonth()+1)[0];
	}else if(instrument == "month"){//月
		return new Date().Format('yyyy-MM')+'-01';
	}else if(instrument == "week"){//周
		return getMondayAndWeekend()[0];
	}
}

function getRecentlyTime(instrument,timeArr){
	var myDate = new Date();
	if(instrument == "year"){//年
		getYearTime(timeArr);
	}else if(instrument == "quarter"){//季度
		return getQuarterStartTime(timeArr);
	}else if(instrument == "month"){//月
		getMont(timeArr);
	}else if(instrument == "week"){//周
		return getWeekOne();
	}
}

function getMont(timeArr){
	
	var myDate = new Date();
	var t = new Date();
	t.setMonth(t.getMonth() + 1);
	t.setDate(0);
	myDate.setDate(1);
	timeArr[0] = myDate.Format('MM-dd');
	for(var i=1;i<t.getDate();i++){
		myDate.setDate(myDate.getDate() + 1);
		timeArr[i] = myDate.Format('MM-dd');
	}
}

function getYearTime(timeArr){
	timeArr[0] = "-01-";
	timeArr[1] = "-02-";
	timeArr[2] = "-03-";
	timeArr[3] = "-04-";
	timeArr[4] = "-05-";
	timeArr[5] = "-06-";
	timeArr[6] = "-07-";
	timeArr[7] = "-08-";
	timeArr[8] = "-09-";
	timeArr[9] = "-10-";
	timeArr[10] = "-11-";
	timeArr[11] = "-12-";
}

function getQuarterStartTime(timeArr){
	timeArr[0] = 'Q1';
	timeArr[1] = 'Q2';
	timeArr[2] = 'Q3';
	timeArr[3] = 'Q4';
}

function getWeekOne(){
	var myDate = new Date();
	if(myDate.getDay() == 0){
		myDate.setDate(myDate.getDate()-6);
		return myDate.Format("yyyy-MM-dd");
	}else if(myDate.getDay() == 2){
		myDate.setDate(myDate.getDate()-1);
		return myDate.Format("yyyy-MM-dd");
	}else if(myDate.getDay() == 3){
		myDate.setDate(myDate.getDate()-2);
		return myDate.Format("yyyy-MM-dd");
	}else if(myDate.getDay() == 4){
		myDate.setDate(myDate.getDate()-3);
		return myDate.Format("yyyy-MM-dd");
	}else if(myDate.getDay() == 5){
		myDate.setDate(myDate.getDate()-4);
		return myDate.Format("yyyy-MM-dd");
	}else if(myDate.getDay() == 6){
		myDate.setDate(myDate.getDate()-5)
		return myDate.Format("yyyy-MM-dd");
	}else{
		return myDate.Format("yyyy-MM-dd");
	}
}

//时间格式化
Date.prototype.Format = function(fmt){ //author: meizz   
	var o = {   
		"M+" : this.getMonth()+1,                 //月份   
		"d+" : this.getDate(),                    //日   
		"h+" : this.getHours(),                   //小时   
		"m+" : this.getMinutes(),                 //分   
		"s+" : this.getSeconds(),                 //秒   
		"q+" : Math.floor((this.getMonth()+3)/3), //季度   
		"S"  : this.getMilliseconds()             //毫秒   
    };   
  if(/(y+)/.test(fmt))   
      fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
      for(var k in o)   
          if(new RegExp("("+ k +")").test(fmt))   
              fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
      return fmt;   
};

Common_date = {
		getCurrentTime:function(){//获取当前时间(yyyy-MM-dd)
			return new Date().Format("yyyy-MM-dd");
		},
		getEarlierMonth:function(){//获取本月一号(yyyy-MM-dd)
			return new Date().Format("yyyy-MM-")+"01";
		},
		getLaterMonth:function(){//获取本月最后一天(yyyy-MM-dd)
			 var date=new Date();
			 var currentMonth=date.getMonth();
			 var nextMonth=++currentMonth;
			 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
			 var oneDay=1000*60*60*24;
			 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
		},
		getTheFirstDay:function(){//获取本年年初一和年末(yyyy-MM-dd)
			return [new Date().Format("yyyy-")+"01-01",new Date().Format("yyyy-")+"12-31"];
		},
		getQuarterDate:function(){//获取本季度初和末时间[初,末](yyyy-MM-dd)
			var month = new Date().getMonth() + 1;
			if(month >= 1 && month <= 3){
				return [new Date().getFullYear()+"-01-01",new Date().getFullYear()+"-03-31"];
			}else if(month >= 4 && month <= 6){
				return [new Date().getFullYear()+"-04-01",new Date().getFullYear()+"-06-30"];
			}else if(month >= 7 && month <= 9){
				return [new Date().getFullYear()+"-07-01",new Date().getFullYear()+"-09-30"];
			}else if(month >= 10 && month <= 12){
				return [new Date().getFullYear()+"-10-01",new Date().getFullYear()+"-12-31"];
			}
		},
		getMondayAndWeekend:function(){//获取本周周一和本周周末日期[初,末](yyyy-MM-dd)
			var startTime = "";
			var engTime = "";
			var d=new Date();
			var weekday = d.getDay();
			if(weekday == 0){
				engTime = d.Format("yyyy-MM-dd");
				d.setDate(d.getDate() - 6);
				startTime = d.Format("yyyy-MM-dd");
			}else if(weekday == 1){
				startTime = d.Format("yyyy-MM-dd");
				d.setDate(d.getDate() + 6);
				engTime = d.Format("yyyy-MM-dd");
			}else if(weekday == 2){
				d.setDate(d.getDate() - 1);
				startTime = d.Format("yyyy-MM-dd");
				d.setDate(d.getDate() + 6);
				engTime = d.Format("yyyy-MM-dd");
			}else if(weekday == 3){
				d.setDate(d.getDate() - 2);
				startTime = d.Format("yyyy-MM-dd");
				d.setDate(d.getDate() + 6);
				engTime = d.Format("yyyy-MM-dd");
			}else if(weekday == 4){
				d.setDate(d.getDate() - 3);
				startTime = d.Format("yyyy-MM-dd");
				d.setDate(d.getDate() + 6);
				engTime = d.Format("yyyy-MM-dd");
			}else if(weekday == 5){
				d.setDate(d.getDate() - 4);
				startTime = d.Format("yyyy-MM-dd");
				d.setDate(d.getDate() + 6);
				engTime = d.Format("yyyy-MM-dd");
			}else if(weekday == 6){
				d.setDate(d.getDate() - 5);
				startTime = d.Format("yyyy-MM-dd");
				d.setDate(d.getDate() + 6);
				engTime = d.Format("yyyy-MM-dd");
			};
			return [startTime,engTime];
		}
	}

// ------------------------------优化---------------------------------

/*
 * 公共区域
 */
function getSelectArea(){//获取选择的区域
	var selectValue = "";//选择区域partyId
	var isSelectCountry = '';//选择的区域类型
	
	if(!isStringNull($("#office").val())){//办事处
		selectValue = $("#office").val();
		isSelectCountry = false;
	}else if(!isStringNull($("#region").val())){//地区
		selectValue = $("#region").val();
		isSelectCountry = false;
	}else if(!isStringNull($("#country").val())){//国家
		selectValue = $("#country").val();
		isSelectCountry = true;
	}
	var o = {};
	o.selectValue = selectValue;
	o.isSelectCountry = isSelectCountry;
	return o;
};

function getQueryCountry(){//获取查询的国家
	return $("#country").val();
};

function isStringNull (string){//字符串是否为空或null
	return (string != "" && string != null) ? false : true;
}

function jointPartyId(obj){//拼接partyId
	var partyStr = '';
	if(obj.length > 0){
		partyStr += "'"+obj[0].countryId+"'";
		for(var i=1;i<obj.length;i++){
			partyStr += ",'"+obj[i].countryId+"'";
		}
	}else{
		partyStr = "''";
	}
	return partyStr;
}

function commonSelectObj(){
	var isSelectUser = false;//是否选择用户
	var userName = '';
	var selectValue = '';//选择值
	var isBusinessManager = false;//是否为业务经理
	var isSelectArea = false;//是否选择区域
	var isSelectCountry = false;//是否选择国家
	
	if(!isStringNull($("#supervisor").val())){//督导
		isSelectUser = true;
		selectValue = $("#supervisor").val();
		userName = $("#supervisor option:selected").html();
	}else if(!isStringNull($('#business_Mana').attr('userId'))){//业务员 或区域经理
		isSelectUser = true;
		selectValue = $('#business_Mana').attr('userId');
		userName = $('#business_Mana').val();
		isBusinessManager = $('#isSorS').val() == 'sup'?true : false;
	}else{
		if(!isStringNull($("#office").val())){//办事处
			selectValue = $("#office").val();
			isSelectArea = true;
		}else if(!isStringNull($("#region").val())){//经营部
			selectValue = $("#region").val();
			isSelectArea = true;
		}else if(!isStringNull($("#country").val())){//国家
			isSelectCountry = true;
			selectValue = $("#country").val();
			isSelectArea = true;
		}else{
			if(isHQRole != 'true'){
				isSelectCountry = true;
				selectValue = loginCountry;
				isSelectArea = true;
			}
		}
	}
	var o = {};
	o.isSelectUser = isSelectUser;
	o.selectValue = selectValue;
	o.isBusinessManager = isBusinessManager;
	o.isSelectArea = isSelectArea;
	o.isSelectCountry = isSelectCountry;
	o.userName = userName;
	return o;
};

Common_obj ={
		isStringNull:function (string){//字符串是否为空或null
			return (string != "" && string != null) ? false : true;
		},
		getMonthArray:function(){//获取月份数字
			return [1,2,3,4,5,6,7,8,9,10,11,12];
		},
		getMonthInitializeArray:function(){//12个月初始化
			return [0,0,0,0,0,0,0,0,0,0,0,0];
		},
		getQuarterArray:function(){//获取月份数字
			return [1,2,3,4];
		},
		getQuarterInitializeArray:function(){//4个季度初始化
			return [0,0,0,0];
		}
	};


//---------------------------------------------------------------------

/**
 * single product (单品销售)
 */
function loadItemSold(){
	var selectInfoObj = commonSelectObj();
	if(selectInfoObj.isSelectUser){
		var o = new Object();
		if(selectInfoObj.isBusinessManager){
			o.manager = commonSelectObj().selectValue;
			managerAreaJoIntParty(o);
		}else{
			o.userId =  commonSelectObj().selectValue;
			personalAreaRequest(o);
		}
	}else{
		loadItemPartyIdJoint(selectInfoObj);
	}
};	
	
function loadItemPartyIdJoint(selectInfoObj){
	var o = {};
	o.partyId = selectInfoObj.selectValue;
	o.isCountry = selectInfoObj.isSelectCountry;
	o.sType = "SELECTSUBDOMAINBYAREA";
	itemSoldAreaRequestDate(o);
};
	
function personalAreaRequest(o){
	o.startTime = Common_date.getTheFirstDay()[0];
	o.endTime = Common_date.getTheFirstDay()[1];
	o.pType = pType;
	o.level = $("#storeLevel").val();
	var json = JSON.stringify(o);
	var	url= baseUrl + "platform/personalItemSalesInfo.action";
	$.ajax({type: "post",
		url:url,
		data: o,
		success:function(msg){
			var jsonObj = $.parseJSON(msg);
			itemToCalculate(jsonObj.rows);
		}
	});
}
	
function managerAreaJoIntParty(o){
	o.userId = o.manager;
	o.sType = "SELECTPARTYIDBYUSERMANAGER";
	itemSoldAreaRequestDate(o);
}
	
function itemSoldAreaRequestDate(o){
	o.startTime = Common_date.getTheFirstDay()[0];
	o.endTime = Common_date.getTheFirstDay()[1];
	o.pType = pType;
	o.level = $("#storeLevel").val();
	var json = JSON.stringify(o);
	
	var	url= baseUrl + "platform/regionalSalesItemInfo.action";
	$.ajax({type: "post",
		url:url,
		data: o,
		success:function(msg){
			var jsonObj = $.parseJSON(msg);
			itemToCalculate(jsonObj.rows);
		}
	});
}

function itemToCalculate(jsonObj){
	var seriesArr = [];
	var selectTime = [];
	var modelList = [];
	var tiemModel = $('[data-id=id_item]').attr('title');
	if("Nothing selected" != tiemModel && '' != tiemModel){
		modelList = tiemModel.split(', ');
	}
	
	if($('#single').val() == 'month'){
		selectTime = Common_obj.getMonthArray();
		for(var i=0;i<modelList.length;i++){
			var medium = Common_obj.getMonthInitializeArray();
			for(var j=0;j<jsonObj.length;j++){
				var infoModel = modelList[i].toLowerCase();
				var contrastModel = jsonObj[j].modelName.toLowerCase();
				if(infoModel.indexOf(contrastModel) >= 0){
					for(var k=0;k<selectTime.length;k++){
						if(jsonObj[j].dataDate == selectTime[k]){
							medium[k] += jsonObj[j].quan;
							break;
						}
					}
				}
			}
			seriesArr.push({type:'line',name:modelList[i],stack:'总量',data:medium});
		}
	}else{
		selectTime = Common_obj.getQuarterArray();
		for(var i=0;i<modelList.length;i++){
			var medium = Common_obj.getQuarterInitializeArray();
			for(var j=0;j<jsonObj.length;j++){
				var infoModel = modelList[i].toLowerCase();
				var contrastModel = jsonObj[j].modelName.toLowerCase();
				if(infoModel.indexOf(contrastModel) >= 0){
					if(jsonObj[j].dataDate >= 1 && jsonObj[j].dataDate <= 3){
						medium[0] += jsonObj[j].quan;
					}else if(jsonObj[j].dataDate >= 4 && jsonObj[j].dataDate <= 6){
						medium[1] += jsonObj[j].quan;
					}else if(jsonObj[j].dataDate >= 7 && jsonObj[j].dataDate <= 9){
						medium[2] += jsonObj[j].quan;
					}else if(jsonObj[j].dataDate >= 10 && jsonObj[j].dataDate <= 12){
						medium[3] += jsonObj[j].quan;
					}
				}
			}
			seriesArr.push({type:'line',name:modelList[i],stack:'总量',data:medium});
		}
	}
	loadModelSaleLineChar('item','',modelList,selectTime,seriesArr);
}

	/**
	 * 重点产品
	 */
	function coreProducts(){
		
		var selectInfoObj = commonSelectObj();
		if(selectInfoObj.isSelectUser){
			var o = new Object();
			if(selectInfoObj.isBusinessManager){
				o.manager = commonSelectObj().selectValue;
				coreProductsManagerAreaJoIntParty(o);
			}else{
				o.userId =  commonSelectObj().selectValue;
				coreProductsPersonalAreaRequest(o);
			}
		}else{
			loadCoreProductsPartyIdJoint(selectInfoObj);
		}
	};	
	
	function loadCoreProductsPartyIdJoint(selectInfoObj){
		var o = {};
		o.partyId = selectInfoObj.selectValue;
		o.isCountry = selectInfoObj.isSelectCountry;
		o.sType = "SELECTSUBDOMAINBYAREA"
		coreProductsAreaRequestDate(o);
	};
	
	function coreProductsPersonalAreaRequest(o){
		o.startTime = Common_date.getTheFirstDay()[0];
		o.endTime = Common_date.getTheFirstDay()[1];
		o.country = getCountryId();
		o.pType = pType;
		o.level = $("#storeLevel").val();
		var	url=baseUrl + "platform/personalSalesOfKeyProductsInfo.action";
		$.ajax({type: "post",
			url:url,
			data: o,
			success:function(msg){
				var jsonObj = $.parseJSON(msg);
				coreProductsItemToCalculate(jsonObj.rows);
			}
		});
	}
	
	
	function coreProductsManagerAreaJoIntParty(o){
		o.userId = o.manager;
		o.sType = "SELECTPARTYIDBYUSERMANAGER"
		coreProductsAreaRequestDate(o);
	}
	
	function coreProductsAreaRequestDate(o){
		o.startTime = Common_date.getTheFirstDay()[0];
		o.endTime = Common_date.getTheFirstDay()[1];
		o.country = getCountryId();
		o.pType = pType;
		o.level = $("#storeLevel").val();
		var json = JSON.stringify(o);
		var	url=baseUrl + "platform/regionalSalesOfKeyProductsInfo.action";
		$.ajax({type: "post",
			url:url,
			data: o,
			success:function(msg){
				var jsonObj = $.parseJSON(msg);
				coreProductsItemToCalculate(jsonObj.rows);
			}
		});
	}
	
	function coreProductsItemToCalculate(jsonObj){
		var seriesArr = [];
		var selectTime = [];
		var tiemModel = $('[data-id=id_select]').attr('title');
		var modelList = [];
		if("Nothing selected" != tiemModel && '' != tiemModel){
			modelList = tiemModel.split(', ');
		}
		
		if($('#codeTdate').val() == 'month'){
			selectTime = Common_obj.getMonthArray();
			for(var i=0;i<modelList.length;i++){
				var medium = Common_obj.getMonthInitializeArray();
				for(var j=0;j<jsonObj.length;j++){
					var infoModel = modelList[i].toLowerCase();
					var contrastModel = jsonObj[j].modelName.toLowerCase();
					if(infoModel.indexOf(contrastModel) >= 0){
						for(var k=0;k<selectTime.length;k++){
							if(jsonObj[j].dataDate == selectTime[k]){
								medium[k] += jsonObj[j].quan;
								break;
							}
						}
					}
				}
				seriesArr.push({type:'line',name:modelList[i],stack:'总量',data:medium});
			}
		}else{
			selectTime = Common_obj.getQuarterArray();
			for(var i=0;i<modelList.length;i++){
				var medium = Common_obj.getQuarterInitializeArray();
				for(var j=0;j<jsonObj.length;j++){
					var infoModel = modelList[i].toLowerCase();
					var contrastModel = jsonObj[j].modelName.toLowerCase();
					if(infoModel.indexOf(contrastModel) >= 0){
						if(jsonObj[j].dataDate >= 1 && jsonObj[j].dataDate <= 3){
							medium[0] += jsonObj[j].quan;
						}else if(jsonObj[j].dataDate >= 4 && jsonObj[j].dataDate <= 6){
							medium[1] += jsonObj[j].quan;
						}else if(jsonObj[j].dataDate >= 7 && jsonObj[j].dataDate <= 9){
							medium[2] += jsonObj[j].quan;
						}else if(jsonObj[j].dataDate >= 10 && jsonObj[j].dataDate <= 12){
							medium[3] += jsonObj[j].quan;
						}
					}
				}
				seriesArr.push({type:'line',name:modelList[i],stack:'总量',data:medium});
			}
		}
		loadCodeLineChar('code','',modelList,selectTime,seriesArr);
	}

/**
 * Different Time Periods Contrast In A Region(年份对比图表)
 */
function loadContrastAnalysis(){
	$('#loadingContrast').show();
	var selectInfoObj = commonSelectObj();
	if(selectInfoObj.isSelectUser){
		var o = {};
		o.userList = selectInfoObj.selectValue;
		if(selectInfoObj.isBusinessManager){
			calculatingYearSalesData(o,'queryRoleOfSalesYear');
		}else{
			loadRegionalManager(o);
		}
	}else{
		loadAnalysisPartyIdJoint(selectInfoObj);
	}
};
	
function loadAnalysisPartyIdJoint(selectInfoObj){
	var o = {};
	o.partyId = selectInfoObj.selectValue;
	o.isCountry = selectInfoObj.isSelectCountry;
	o.sType = "SELECTSUBDOMAINBYAREA";
	calculatingYearSalesData(o,'queryManagerOfSalesYear');
};
	
function LoadSupeAndSaleCustomerInformation(o){
	var json = JSON.stringify(o);
  	var	url = baseUrl + "platform/selectPartySupeAndSale.action";
	$.ajax({type: "post",
   		url:url,
   		data: o,
   		success:function(msg){
   			var jsonObj = $.parseJSON(msg);
   			loadUserJoint(jsonObj.rows);
    	}
	});
};



function loadUserJoint(idList){
	var userList = '';
	var o = {};
	
	if(idList.length > 0){
		userList += "'"+idList[0].userId+"'";
		for(var i=1;i<idList.length;i++){
			userList += ",'"+idList[i].userId+"'";
		}
	}else{
		userList = "''";
	}
	o.userList = userList;
	if($('#contrastRole').val() == 'region'){
		loadRegionalManager(o);
	}else{
		calculatingYearSalesData(o,'queryRoleOfSalesYear');
	}
};
	
function loadRegionalManager(o){
	o.userId = o.userList;
	o.sType = "SELECTPARTYIDBYUSERMANAGER";
	calculatingYearSalesData(o,'queryManagerOfSalesYear');
};


function calculatingYearSalesData(o,urlSection){
	 o.endTime = new Date().getFullYear();
	 o.startTime = new Date().getFullYear() - 1;
	 o.pType = pType;
	 o.level = $("#storeLevel").val();
	 var json = JSON.stringify(o);
	 	var	url = baseUrl + "platform/"+urlSection+".action";
		$.ajax({type: "post",
	   		url:url,
	   		data: o,
	   		success:function(msg){
	   			var jsonObj = $.parseJSON(msg);
	   			if($('#analysis').val() == 'year'){
	   				bintageContrastOnAMonthlyBasis(jsonObj.rows);
	   			}else{
	   				yearCompareQuarterly(jsonObj.rows);
	   			}
	    	}
 	});
};

function yearCompareQuarterly(jsonObj){
	var headNameList = [new Date().getFullYear()-1+"",new Date().getFullYear()+""];
	var headDateArrty = Common_obj.getQuarterArray();
	var tailDateArrty = Common_obj.getQuarterArray();
	var headValueArray = Common_obj.getQuarterInitializeArray();
	var tailValueArray = Common_obj.getQuarterInitializeArray();
	
	if($('#volOrRev').val() == 'Volume'){
		for(var i=0;i<jsonObj.length;i++){
			if(headNameList[0] == jsonObj[i].yearT){
				if(jsonObj[i].dataDate >= 1 && jsonObj[i].dataDate <= 3){
					headValueArray[0] += jsonObj[i].quan; 
				}else if(jsonObj[i].dataDate >= 4 && jsonObj[i].dataDate <= 6){
					headValueArray[1] += jsonObj[i].quan; 
				}else if(jsonObj[i].dataDate >= 7 && jsonObj[i].dataDate <= 9){
					headValueArray[2] += jsonObj[i].quan; 
				}else if(jsonObj[i].dataDate >= 10 && jsonObj[i].dataDate <= 12){
					headValueArray[3] += jsonObj[i].quan; 
				}
			}else{
				if(jsonObj[i].dataDate >= 1 && jsonObj[i].dataDate <= 3){
					tailValueArray[0] += jsonObj[i].quan; 
				}else if(jsonObj[i].dataDate >= 4 && jsonObj[i].dataDate <= 6){
					tailValueArray[1] += jsonObj[i].quan; 
				}else if(jsonObj[i].dataDate >= 7 && jsonObj[i].dataDate <= 9){
					tailValueArray[2] += jsonObj[i].quan; 
				}else if(jsonObj[i].dataDate >= 10 && jsonObj[i].dataDate <= 12){
					tailValueArray[3] += jsonObj[i].quan; 
				}
			}
		}
	}else{
		if(isHQRole == 'true'){
			for(var i=0;i<jsonObj.length;i++){
				if(headNameList[0] == jsonObj[i].yearT){
					if(jsonObj[i].dataDate >= 1 && jsonObj[i].dataDate <= 3){
						headValueArray[0] += jsonObj[i].HAmou; 
					}else if(jsonObj[i].dataDate >= 4 && jsonObj[i].dataDate <= 6){
						headValueArray[1] += jsonObj[i].HAmou; 
					}else if(jsonObj[i].dataDate >= 7 && jsonObj[i].dataDate <= 9){
						headValueArray[2] += jsonObj[i].HAmou; 
					}else if(jsonObj[i].dataDate >= 10 && jsonObj[i].dataDate <= 12){
						headValueArray[3] += jsonObj[i].HAmou; 
					}
				}else{
					if(jsonObj[i].dataDate >= 1 && jsonObj[i].dataDate <= 3){
						tailValueArray[0] += jsonObj[i].HAmou; 
					}else if(jsonObj[i].dataDate >= 4 && jsonObj[i].dataDate <= 6){
						tailValueArray[1] += jsonObj[i].HAmou; 
					}else if(jsonObj[i].dataDate >= 7 && jsonObj[i].dataDate <= 9){
						tailValueArray[2] += jsonObj[i].HAmou; 
					}else if(jsonObj[i].dataDate >= 10 && jsonObj[i].dataDate <= 12){
						tailValueArray[3] += jsonObj[i].HAmou; 
					}
				}
			}
		}else{
			for(var i=0;i<jsonObj.length;i++){
				if(headNameList[0] == jsonObj[i].yearT){
					if(jsonObj[i].dataDate >= 1 && jsonObj[i].dataDate <= 3){
						headValueArray[0] += jsonObj[i].amou; 
					}else if(jsonObj[i].dataDate >= 4 && jsonObj[i].dataDate <= 6){
						headValueArray[1] += jsonObj[i].amou; 
					}else if(jsonObj[i].dataDate >= 7 && jsonObj[i].dataDate <= 9){
						headValueArray[2] += jsonObj[i].amou; 
					}else if(jsonObj[i].dataDate >= 10 && jsonObj[i].dataDate <= 12){
						headValueArray[3] += jsonObj[i].amou; 
					}
				}else{
					if(jsonObj[i].dataDate >= 1 && jsonObj[i].dataDate <= 3){
						tailValueArray[0] += jsonObj[i].amou; 
					}else if(jsonObj[i].dataDate >= 4 && jsonObj[i].dataDate <= 6){
						tailValueArray[1] += jsonObj[i].amou; 
					}else if(jsonObj[i].dataDate >= 7 && jsonObj[i].dataDate <= 9){
						tailValueArray[2] += jsonObj[i].amou; 
					}else if(jsonObj[i].dataDate >= 10 && jsonObj[i].dataDate <= 12){
						tailValueArray[3] += jsonObj[i].amou; 
					}
				}
			}
		}
	}
	
	var unit = "";
	if($('#volOrRev').val() == 'Revenue'){
		unit = "Year compared(Revenue)"
	}else{
		unit = "Year compared(Volume)"
	}
	yearContrast('analysis',headNameList,headDateArrty,tailDateArrty,headValueArray,tailValueArray);
}

function bintageContrastOnAMonthlyBasis(jsonObj){
	var headNameList = [new Date().getFullYear()-1+"",new Date().getFullYear()+""];
	var headDateArrty = Common_obj.getMonthArray();
	var tailDateArrty = Common_obj.getMonthArray();
	var headValueArray = Common_obj.getMonthInitializeArray();
	var tailValueArray = Common_obj.getMonthInitializeArray();
	
	if($('#volOrRev').val() == 'Volume'){
		for(var i=0;i<jsonObj.length;i++){
			if(headNameList[0] == jsonObj[i].yearT){
				for(var j=0;j<headDateArrty.length;j++){
					if(headDateArrty[j] == jsonObj[i].dataDate){
						headValueArray[j] += jsonObj[i].quan;
						break;
					}
				}
			}else{
				for(var j=0;j<headDateArrty.length;j++){
					if(headDateArrty[j] == jsonObj[i].dataDate){
						tailValueArray[j] += jsonObj[i].quan;
						break;
					}
				}
			}
		}
	}else{
		if(isHQRole == 'true'){
			for(var i=0;i<jsonObj.length;i++){
				if(headNameList[0] == jsonObj[i].yearT){
					for(var j=0;j<headDateArrty.length;j++){
						if(headDateArrty[j] == jsonObj[i].dataDate){
							headValueArray[j] += jsonObj[i].HAmou;
							break;
						}
					}
				}else{
					for(var j=0;j<headDateArrty.length;j++){
						if(headDateArrty[j] == jsonObj[i].dataDate){
							tailValueArray[j] += jsonObj[i].HAmou;
							break;
						}
					}
				}
			}
		}else{
			for(var i=0;i<jsonObj.length;i++){
				if(headNameList[0] == jsonObj[i].yearT){
					for(var j=0;j<headDateArrty.length;j++){
						if(headDateArrty[j] == jsonObj[i].dataDate){
							headValueArray[j] += jsonObj[i].amou;
							break;
						}
					}
				}else{
					for(var j=0;j<headDateArrty.length;j++){
						if(headDateArrty[j] == jsonObj[i].dataDate){
							tailValueArray[j] += jsonObj[i].amou;
							break;
						}
					}
				}
			}
		}
	}
	yearContrast('analysis',headNameList,headDateArrty,tailDateArrty,headValueArray,tailValueArray);
}

/**
 * 尺寸
 */
function differentSizeTV(){
	$('#loadingSize').show();
	var selectInfoObj = commonSelectObj();
	
	if(selectInfoObj.isSelectUser){
		if(selectInfoObj.isBusinessManager){
			selectCountryByUserIdSize(selectInfoObj.selectValue);
		}else{
			loadUserSaleSizeLine(selectInfoObj.selectValue);
		}
	}else{
		loadSizePartyIdJoint(selectInfoObj);
	}
};
	
function loadUserSaleSizeLine(userId){
	var o = {};
	o.userId = userId;
	o.startTime = Common_date.getTheFirstDay()[0];
	o.endTime = Common_date.getTheFirstDay()[1];
	o.pType = pType;
	o.level = $("#storeLevel").val();
	$.ajax({
		url:baseUrl + "platform/queryUserSizeSaleInfo.action",
		type:"POST",
		data:o,
		success:function(data){
			var obj =  eval('(' + data + ')');
			queryAllSize(obj.rows);
		}
	});
};
	
function selectCountryByUserIdSize(userId){
	var o = {};
	o.userId = userId;
	o.sType = "SELECTCOUNTRYBYUSERID";
	loadSizeSaleLine(o);
};
	
function loadSizePartyIdJoint(selectInfoObj){
	var o = {};
	o.partyId = selectInfoObj.selectValue;
	o.isCountry = selectInfoObj.isSelectCountry;
	o.sType = "SELECTSUBDOMAINBYAREA";
	loadSizeSaleLine(o);
};
	
function loadSizeSaleLine(o){
	o.startTime = Common_date.getTheFirstDay()[0];
	o.endTime = Common_date.getTheFirstDay()[1];
	o.pType = pType;
	o.level = $("#storeLevel").val();
	$.ajax({
		url:baseUrl + "platform/querySizeSaleInfo.action",
		type:"POST",
		data:o,
		success:function(data){
			var obj =  eval('(' + data + ')');
			queryAllSize(obj.rows);
		}
	});
};

function queryAllSize(jsonObj){
	var allFun = [];
	var modelList = $('#id_size').combobox('getValues');
	for(var i = 0;i<modelList.length;i++){
		 var o = {};
		 o.NAME = modelList[i];
		 allFun[i] = o;
	 };
	calculateAllSize(jsonObj,allFun);
};
	
	function calculateAllSize(obj,allFun){
		var seriesArr = [];
		var selectName = [];
		var selectStyle = $('#sizeSale').val();
		var selectTime = [];
		if('year' == selectStyle){
			selectTime = Common_obj.getMonthArray();
			for(var i=0;i<allFun.length;i++){
				selectName[i] = allFun[i].NAME;
				var temporaryArr = Common_obj.getMonthInitializeArray();
				for(var j=0;j<obj.length;j++){
					if(obj[j].func.indexOf(allFun[i].NAME) >= 0){
						for(var k = 0;k<selectTime.length;k++){
							if(obj[j].t == selectTime[k]){
								temporaryArr[k] += obj[j].quan;
								break;
							}
						}
					}
				}
				seriesArr.push({type:'line',name:allFun[i].NAME,stack:'总量',data:temporaryArr});
			}
			
		}else if('quarter'){
			selectTime = Common_obj.getQuarterArray();
			for(var i=0;i<allFun.length;i++){
				selectName[i] = allFun[i].NAME;
				var temporaryArr = Common_obj.getQuarterInitializeArray();
				for(var j=0;j<obj.length;j++){
					if(obj[j].func.indexOf(allFun[i].NAME) >= 0){
						if(obj[j].t >= 1 && obj[j].t <= 3){
							temporaryArr[0] += obj[j].quan; 
						}else if(obj[j].t >= 4 && obj[j].t <= 6){
							temporaryArr[1] += obj[j].quan;
						}else if(obj[j].t >= 7 && obj[j].t <= 9){
							temporaryArr[2] += obj[j].quan;
						}else if(obj[j].t >= 10 && obj[j].t <= 12){
							temporaryArr[3] += obj[j].quan;
						}
					}
				}
				seriesArr.push({type:'line',name:allFun[i].NAME,stack:'总量',data:temporaryArr});
			}
		}
		loadSizeLineChar('size','',selectName,selectTime,seriesArr/*,eval('(' +  hideAttr + ')')*/);
	}


/**
 * Different Functions(功能图表)
 */
function differentFunctionsTV(){
	$('#loadingFunction').show();
	var selectInfoObj = commonSelectObj();
	if(selectInfoObj.isSelectUser){
		if(selectInfoObj.isBusinessManager){
			selectCountryByUserId(selectInfoObj.selectValue);
		}else{
			loadUserSaleLine(selectInfoObj.selectValue);
		}
	}else{
		loadFunctionPartyIdJoint(selectInfoObj);
	}
};
	
function selectCountryByUserId(userId){
	var o = {};
	o.userId = userId;
	o.sType = "SELECTCOUNTRYBYUSERID";
	loadFunctionSaleLine(o);
};
	
function loadUserSaleLine(userId){
	var o = {};
	o.userId = userId;
	o.startTime = Common_date.getTheFirstDay()[0];
	o.endTime = Common_date.getTheFirstDay()[1];
	o.pType = pType;
	o.level = $("#storeLevel").val();
	$.ajax({
		url:baseUrl + "platform/queryUsesrFunctionSaleInfo.action",
		type:"POST",
		data:o,
		success:function(data){
			var obj =  eval('(' + data + ')');
			queryAllFunction(obj.rows);
		}
	});
};

	
function loadFunctionPartyIdJoint(selectInfoObj){
	var o = {};
	o.partyId = selectInfoObj.selectValue;
	o.isCountry = selectInfoObj.isSelectCountry;
	o.sType = "SELECTSUBDOMAINBYAREA";
	loadFunctionSaleLine(o);
};

	
function loadFunctionSaleLine(o){
	o.startTime = Common_date.getTheFirstDay()[0];
	o.endTime = Common_date.getTheFirstDay()[1];
	o.pType = pType;
	o.level = $("#storeLevel").val();
	$.ajax({
		url:baseUrl + "platform/queryFunctionSaleInfo.action",
		type:"POST",
		data:o,
		success:function(data){
			var obj =  eval('(' + data + ')');
			queryAllFunction(obj.rows);
		}
	});
};

function queryAllFunction(jsonObj){
	$.ajax({
		url:baseUrl + "platform/queryAllFunction.action",
		type:"POST",
		success:function(data){
			var obj =  eval('(' + data + ')');
			calculateAllFunction(jsonObj,obj.rows);
		}
	});
};

function calculateAllFunction(obj,allFun){
	var seriesArr = [];
	var selectName = [];
	var selectStyle = $("#selectFunctionSaleTime").val();
	var selectTime = [];
	if('year' == selectStyle){
		selectTime = Common_obj.getMonthArray();
		for(var i=0;i<allFun.length;i++){
			selectName[i] = allFun[i].name;
			var temporaryArr = Common_obj.getMonthInitializeArray();
			for(var j=0;j<obj.length;j++){
				var infoModel = obj[j].func.toLowerCase();
				var contrastModel = allFun[i].name.toLowerCase();
				if(infoModel.indexOf(contrastModel) >= 0){
					for(var k = 0;k<selectTime.length;k++){
						if(obj[j].t == selectTime[k]){
							temporaryArr[k] += obj[j].quan;
							break;
						}
					}
				}
			}
			seriesArr.push({type:'line',name:allFun[i].name,stack:'总量',data:temporaryArr});
		}
		
	}else if('quarter'){
		selectTime = Common_obj.getQuarterArray();
		for(var i=0;i<allFun.length;i++){
			selectName[i] = allFun[i].name;
			var temporaryArr = Common_obj.getQuarterInitializeArray();
			for(var j=0;j<obj.length;j++){
				var infoModel = obj[j].func.toLowerCase();
				var contrastModel = allFun[i].name.toLowerCase();
				if(infoModel.indexOf(contrastModel) >= 0){
					if(obj[j].t >= 1 && obj[j].t <= 3){
						temporaryArr[0] += obj[j].quan; 
					}else if(obj[j].t >= 4 && obj[j].t <= 6){
						temporaryArr[1] += obj[j].quan;
					}else if(obj[j].t >= 7 && obj[j].t <= 9){
						temporaryArr[2] += obj[j].quan;
					}else if(obj[j].t >= 10 && obj[j].t <= 12){
						temporaryArr[3] += obj[j].quan;
					}
				}
			}
			seriesArr.push({type:'line',name:allFun[i].name,stack:'总量',data:temporaryArr});
		}
	}
	loadFunctionLineChar('function1','',selectName,selectTime,seriesArr,{});
}
	
	/*
	 * -------------------------------销售进展---------------------------->
	 */
		function LoadSalesProgress(){
			
			var selectInfoObj = commonSelectObj();
			var o = new Object();
			if(selectInfoObj.isSelectUser){
				$('#area').hide();
				if(selectInfoObj.isBusinessManager){
					o.manager = selectInfoObj.selectValue;
					o.userName = selectInfoObj.userName;
					loadPersonalManagerSellingJointPartyId(o);
				}else{
					o.userId =  selectInfoObj.selectValue;
					o.userName = selectInfoObj.userName;
					loadPersonalRoleSelling(o);
				}
			}else{
				$('#area').show();
				salesProgressJointPartyByArea(selectInfoObj);
			};
		}
		
		function salesProgressJointPartyByArea(selectInfoObj){
			var isRegion = '';
			if($('#area').val() == 'region'){
				isRegion = true;
			}else{
				isRegion = false;
			}
			selectInfoObj.isRegion = isRegion;
			if($('#area').val() == 'region'){
				LoadAreaSalesProgress(selectInfoObj);
			}else{
				if($('#area').val() == 'stores'){
					loadShopSalesProgress(selectInfoObj);
				}else{
					loadChannelSalesProgress(selectInfoObj);
				}
			};
		};
		
		function loadPersonalManagerSellingJointPartyId(o){
			o.userId = o.manager;
			o.sType = "SELECTPARTYIDBYUSERMANAGER";
			loadPersonalManagerSelling(o);
			/*var json = JSON.stringify(o);
			$.ajax({type: "post",
				url:baseUrl + "platform/selectPartyIdByUserManager.action",
				data: o,
				success:function(msg){
					var jsonObj = $.parseJSON(msg).rows;
					var partyListStr = '';
					if(jsonObj.length > 0){
						 partyListStr += "'"+jsonObj[0].partyId+"'";
						 for(var i = 1;i<jsonObj.length;i++){
							 partyListStr += ",'"+jsonObj[i].partyId+"'";
						 }
					 }else{
						 partyListStr = "''";
					 }
					o.partyList = partyListStr;
					
				}
			});*/
		}
		
		function loadPersonalManagerSelling(o){
			o.startTime = getSalesProgressSelectDate()[0];
			o.endTime = getSalesProgressSelectDate()[1];
			o.pType = pType;
			o.level = $("#storeLevel").val();
			var json = JSON.stringify(o);
			$.ajax({type: "post",
				url:baseUrl + "platform/eachSingleAreaSalesQuantity.action",
				data: o,
				success:function(msg){
					var jsonObj = $.parseJSON(msg).rows;
					var seriesList = [0];
					var yAxis_date = [];
					yAxis_date[0] = o.userName;
					seriesList[0] = jsonObj[0].quan;
					salesVolumeGraphicsShow(yAxis_date,seriesList);
				}
			});
		}
		
		function loadPersonalRoleSelling(o){
			o.startTime = getSalesProgressSelectDate()[0];
			o.endTime = getSalesProgressSelectDate()[1];
			o.pType = pType;
			o.level = $("#storeLevel").val();
			var json = JSON.stringify(o);
			$.ajax({type: "post",
				url:baseUrl + "platform/eachRoleSalesQuantity.action",
				data: o,
				success:function(msg){
					var jsonObj = $.parseJSON(msg).rows;
					var seriesList = [0];
					var yAxis_date = [];
					yAxis_date[0] = o.userName;
					seriesList[0] = jsonObj[0].quan;
					salesVolumeGraphicsShow(yAxis_date,seriesList);
				}
			});
		}
		
		//门店销售
		function loadShopSalesProgress(selectInfoObj){
			var o = {};
			
			o.partyId = selectInfoObj.selectValue;
			o.isCountry = selectInfoObj.isSelectCountry;
			o.isRegion = selectInfoObj.isRegion;
			
//			o.countryList = partyListStr;
			o.level = $("#storeLevel").val();
			var json = JSON.stringify(o);
			
			$.ajax({type: "post",
				url:baseUrl + "platform/selectShopByPartyList.action",
				data: o,
				success:function(msg){
					var jsonObj = $.parseJSON(msg);
					loadShopSalesProgressCalculate(jsonObj.rows);
				}
			});
		}
		
		//门店销售计算
		function loadShopSalesProgressCalculate(resultData){
			var o = {};
			o.startTime = getSalesProgressSelectDate()[0];
			o.endTime = getSalesProgressSelectDate()[1];
			o.pType = pType;
			var json = JSON.stringify(o);
			$.ajax({type: "post",
				url:baseUrl + "platform/eachStoresSalesQuantity.action",
				data: o,
				success:function(msg){
					var jsonObj = $.parseJSON(msg);
					jsonObj = jsonObj.rows;
					var yAxis_date = [];
					var seriesList = [];
					initializeArray(seriesList,resultData.length);
					for(var i=0;i<resultData.length;i++){
						yAxis_date[i] = resultData[i].shopName;
						for(var j=0;j<jsonObj.length;j++){
							if(resultData[i].shopId == jsonObj[j].shopId){
								seriesList[i] = jsonObj[j].quan;
								break;
							}
						}
					}
					salesVolumeGraphicsShow(yAxis_date,seriesList);
				}
			});
		}
		
		//渠道销售
		function loadChannelSalesProgress(selectInfoObj){
			var o = {};
			
			o.partyId = selectInfoObj.selectValue;
			o.isCountry = selectInfoObj.isSelectCountry;
			o.isRegion = selectInfoObj.isRegion;
			
//			o.countryList = partyListStr;
			var json = JSON.stringify(o);
			$.ajax({type: "post",
				url:baseUrl + "platform/selectCustomerByPartyList.action",
				data: o,
				success:function(msg){
					var jsonObj = $.parseJSON(msg);
					loadChannelSalesProgressCalculate(jsonObj.rows);
				}
			});
		}
		
		//渠道销售计算
		function loadChannelSalesProgressCalculate(resultData){
			var o = {};
			o.startTime = getSalesProgressSelectDate()[0];
			o.endTime = getSalesProgressSelectDate()[1];
			o.pType = pType;
			var json = JSON.stringify(o);
			$.ajax({type: "post",
				url:baseUrl + "platform/eachChannelsSalesQuantity.action",
				data: o,
				success:function(msg){
					var jsonObj = $.parseJSON(msg);
					jsonObj = jsonObj.rows;
					var yAxis_date = [];
					var seriesList = [];
					initializeArray(seriesList,resultData.length);
					for(var i=0;i<resultData.length;i++){
						yAxis_date[i] = resultData[i].customerName;
						for(var j=0;j<jsonObj.length;j++){
							if(resultData[i].customerId == jsonObj[j].customerId){
								seriesList[i] = jsonObj[j].quan;
								break;
							}
						}
					}
					salesVolumeGraphicsShow(yAxis_date,seriesList);
				}
			});
		}
		
		//区域销售
		function LoadAreaSalesProgress(selectInfoObj){
			
			//"partyId":selectInfoObj.selectValue,'isCountry':selectInfoObj.isSelectCountry,'isRegion':isRegion
			var o = {};
			o.partyId = selectInfoObj.selectValue;
			o.isCountry = selectInfoObj.isSelectCountry;
			o.isRegion = selectInfoObj.isRegion;
			
			o.startTime = getSalesProgressSelectDate()[0];
			o.endTime = getSalesProgressSelectDate()[1];
			o.pType = pType;
			o.level = $("#storeLevel").val();
			//o.partyList = partyListStr;
			var url = baseUrl + "platform/eachRegionalSalesQuantity.action";
			$.ajax({type: "post",
				url:url,
				data: o,
				success:function(msg){
					var jsonObj = $.parseJSON(msg);
					jsonObj = jsonObj.rows;
					var objLength = jsonObj == null ? 0:jsonObj.length;
					var yAxis_date = [];
					var seriesList = [];
					initializeArray(seriesList,objLength);
					for(var i=0;i<objLength;i++){
						yAxis_date[i] = jsonObj[i].partyName;
						seriesList[i] = jsonObj[i].quan;
					}
					salesVolumeGraphicsShow(yAxis_date,seriesList);
				}
			});
		}
		
		
		
		
		function salesVolumeGraphicsShow(yAxis_date,seriesList){
			
			for (var i = 0; i < seriesList.length; i++) {
				for (var j = 0; j < seriesList.length - 1; j++) {
					if(seriesList[j] < seriesList[j + 1]){
						var medium = seriesList[j];
						seriesList[j] = seriesList[j + 1];
						seriesList[j + 1] = medium;
						
						var  mediumName = yAxis_date[j];
						yAxis_date[j] = yAxis_date[j + 1];
						yAxis_date[j + 1] = mediumName;
					}
				}
			}
			
			if(yAxis_date.length > 16){
				yAxis_date = yAxis_date.slice(0, 15);
				seriesList = seriesList.slice(0, 15);
			};
			
			areaSaleNog('channel_store',yAxis_date,seriesList,'',$('#salesRanking').val());
		};
		
		function getSalesProgressSelectDate(){
			var date = $('#salesRanking').val();
			if('week' == date){
				return [Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1]];
			}else if('month' == date){
				return [Common_date.getEarlierMonth(),Common_date.getLaterMonth()];
			}else if('quarter' == date){
				return [Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1]];
			}else if('year' == date){
				return [Common_date.getTheFirstDay()[0],Common_date.getTheFirstDay()[1]];
			}
		}
	
	
	/*
	 * 用户效率
	 */
	function personnelEfficiency(roleStyle){
		
		var selectInfoObj = commonSelectObj();
		if(selectInfoObj.isSelectUser){
			
			var o = {};
			var userList = [];
			var userObj = {};
			
			if(roleStyle == 'Region head'){
				o.roleStyle = roleStyle;
				o.userId = selectInfoObj.selectValue;
				o.userName =  $('#business_Mana').val();
				o.isUser = "TRUE";
				userObj.userId = selectInfoObj.selectValue;
				userObj.userName = $('#business_Mana').val();
			}else if(roleStyle == 'Salesman'){
				userObj.userId = selectInfoObj.selectValue;
				userObj.userName = $('#business_Mana').val();
				o.isUser = "TRUE";
				o.roleStyle = roleStyle;
				o.userId = selectInfoObj.selectValue;
				o.userName =  $('#business_Mana').val();
			}else if(roleStyle == 'Supervisor'){
				o.isUser = "TRUE";
				o.roleStyle = roleStyle;
				o.userId = selectInfoObj.selectValue;
				o.userName =  $('#supervisor option:selected').html();
				userObj.userId = selectInfoObj.selectValue;
				userObj.userName = $('#supervisor option:selected').html();
			}
			userList.push(userObj);
			personnelEfficiencyRoleRealize(o,userList);
		}else{
			
			if(roleStyle == 'Salesman'){
				$('.fr-1').css('background-color','#E0E0E0');
				$('.fr-2').css('background-color','#E0E0E0');
				$('.fr-3').css('background-color','#C23531');
			}else if(roleStyle == 'Region head'){
				$('.fr-1').css('background-color','#C23531');
				$('.fr-2').css('background-color','#E0E0E0');
				$('.fr-3').css('background-color','#E0E0E0');
			}else if(roleStyle == 'Supervisor'){
				$('.fr-1').css('background-color','#E0E0E0');
				$('.fr-2').css('background-color','#C23531');
				$('.fr-3').css('background-color','#E0E0E0');
			}
			
			loadEfficiencyPartyIdJoint(selectInfoObj,roleStyle);
		}
	};
	
	
	function loadEfficiencyPartyIdJoint(selectInfoObj,roleStyle){
		var o = {};
		o.partyId = selectInfoObj.selectValue;
		o.isCountry = selectInfoObj.isSelectCountry;
		o.sType = "SELECTSUBDOMAINBYAREA";
		efficiencySoldAreaRequestDate(o,roleStyle);
		
		/*$.ajax({
			url:baseUrl + "platform/selectSubdomainByArea.action",
			type:"POST",
			data:{"partyId":selectInfoObj.selectValue,'isCountry':selectInfoObj.isSelectCountry},
			success:function(data){
				var obj =  eval('(' + data + ')');
				var partyList = jointPartyId(obj.rows);
				efficiencySoldAreaRequestDate(partyList,roleStyle);
			}
		});*/
	};
	
	function efficiencySoldAreaRequestDate(o,roleStyle){
		o.roleStyle = roleStyle;
		var urlParagraph = '';
		if(roleStyle == 'Region head'){
			personnelEfficiencyRoleRealize(o,null);
			return;
		}else{
			if(roleStyle == 'Supervisor'){
				o.saleType = 2
			}else{
				o.saleType = 0
			}//selectPartySupeAndSaless
			urlParagraph = 'selectPartySupeAndSalessAnd';
		}
		var json = JSON.stringify(o);
		var url = baseUrl + "platform/"+urlParagraph+".action";
		$.ajax({type: "post",
			url:url,
			data: o,
			success:function(msg){
				var jsonObj = $.parseJSON(msg);
				personnelEfficiencyRoleRealize(o,jsonObj.rows);
			}
		});
	}
	
	function personnelEfficiencyRoleRealize(o,resultDate){
		o.attr = $('#autoNamesAttr').val();
		o.size = $('#sizeFunction').val();
		o.pType = pType;
		o.level = $("#storeLevel").val();
		if($('#per_eff_date').val() == 'month'){
			o.startTime = Common_date.getEarlierMonth();
			o.endTime = Common_date.getLaterMonth();
		}else{
			o.startTime = Common_date.getQuarterDate()[0];
			o.endTime = Common_date.getQuarterDate()[1];
		}
		
		if(o.roleStyle == 'Region head'){
			personnelEfficiencyManagerCalculations(o,resultDate);
		}else{
			personnelEfficiencySupOrSalCalculationsShow(o,resultDate);
		}
	}
	
	/*
	 * -------------------------------region
	 */
	function personnelEfficiencyManagerCalculations(o,resultDate){
		
		var json = JSON.stringify(o);
		var url = baseUrl + "platform/selectPartyIdByUserManagerAnd.action";
		$.ajax({type: "post",
			url:url,
			data: o,
			success:function(msg){
				var jsonObj = $.parseJSON(msg);
				personnelEfficiencyManagerCalculationsShow(o,jsonObj.busRows,jsonObj.rows);
			}
		});
	}
	
	function personnelEfficiencyManagerCalculationsShow(ob,resultDate,obj){
		var url = baseUrl + "platform/personalManagerSalesData.action";
		$.ajax({type: "post",
			url:url,
			data: ob,
			success:function(msg){
				var jsonObj = $.parseJSON(msg);
				jsonObj = jsonObj.rows;
				var yAxis_date = [];
				var mediumList = [];
				var regionList = [];
				initializeArray(regionList,resultDate.length);
				
				for(var i= 0;i<jsonObj.length;i++){
					for(var j=0;j<obj.length;j++){
						if(jsonObj[i].partyId == obj[j].partyId){
							var o = {};
							o.userId = obj[j].userId;
							o.quan = jsonObj[i].quan;
							o.amou = jsonObj[i].amou;
							o.HAmou = jsonObj[i].HAmou;
							mediumList.push(o);
							break;
						}
					}
				}
				
				if($('#per_eff_voOrRe').val() == 'Volume'){
					for(var i = 0;i<resultDate.length;i++){
						yAxis_date[i] = resultDate[i].userName;
						for(var j=0;j<mediumList.length;j++){
							if(mediumList[j].userId == resultDate[i].userId){
								regionList[i] += mediumList[j].quan;
								break;
							}
						}
					}
				}else{
					if(isHQRole == 'true'){
						for(var i = 0;i<resultDate.length;i++){
							yAxis_date[i] = resultDate[i].userName;
							for(var j=0;j<mediumList.length;j++){
								if(mediumList[j].userId == resultDate[i].userId){
									regionList[i] += mediumList[j].HAmou;
									break;
								}
							}
						}
					}else{
						for(var i = 0;i<resultDate.length;i++){
							yAxis_date[i] = resultDate[i].userName;
							for(var j=0;j<mediumList.length;j++){
								if(mediumList[j].userId == resultDate[i].userId){
									regionList[i] += mediumList[j].amou;
									break;
								}
							}
						}
					}
				}
				nogT('role_sale_info',yAxis_date,regionList,'',ob.roleStyle);
			}
		});
	}
	
	/*
	 * -------------------------------supOrSale
	 */
	function personnelEfficiencySupOrSalCalculationsShow(o,resultDate){
		var json = JSON.stringify(o);
		var url = baseUrl + "platform/personalSalesData.action";
		$.ajax({type: "post",
			url:url,
			data: o,
			success:function(msg){
				var jsonObj = $.parseJSON(msg);
				jsonObj = jsonObj.rows;
				var yAxis_date = [];
				var regionList = [];
				initializeArray(regionList,resultDate.length);
				if($('#per_eff_voOrRe').val() == 'Volume'){
					for(var i=0;i<resultDate.length;i++){
						yAxis_date[i] = resultDate[i].userName;
						for(var j=0;j<jsonObj.length;j++){
							if(resultDate[i].userId == jsonObj[j].userId){
								regionList[i] += jsonObj[j].quan;
								break;
							}
						}
					}
				}else{
					if(isHQRole == 'true'){
						for(var i=0;i<resultDate.length;i++){
							yAxis_date[i] = resultDate[i].userName;
							for(var j=0;j<jsonObj.length;j++){
								if(resultDate[i].userId == jsonObj[j].userId){
									regionList[i] += jsonObj[j].HAmou;
									break;
								}
							}
						}
					}else{
						for(var i=0;i<resultDate.length;i++){
							yAxis_date[i] = resultDate[i].userName;
							for(var j=0;j<jsonObj.length;j++){
								if(resultDate[i].userId == jsonObj[j].userId){
									regionList[i] += jsonObj[j].amou;
									break;
								}
							}
						}
					}
				}
				nogT('role_sale_info',yAxis_date,regionList,'',o.roleStyle);
			}
		});
	}
	
	function initializeArray(arr,length){
		for(var i= 0;i<length;i++){
			arr[i] = 0;
		}
	}
	
	function aa(){
		  $('#reservationGeneral').html(moment().subtract('days', 29).format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));
		  $('#reservationGeneral').daterangepicker({minDate: '2016-01-01'});
		    
	}
	
	function bb(){
		 $('#reservationWeekly').html(moment().subtract('days', 29).format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));
		 $('#reservationWeekly').daterangepicker({minDate: '2016-01-01'});
	}
	
$(document).ready(function() {
	loadLine();
    $("input[name='reservation']").on("click",function(e){
    	$("input[name='reservation']").each(function(){
    		var id=$(this).attr("id");
            if(e.target != this  && 
            		id!="reservationMonthly" && id!="reservationQuarterly" && id!="reservationAnnual"){
            	$(this).daterangepicker('hide');
            }
        });
    });
    $("input[name='reservatio']").on("click",function(e){
    	$("input[name='reservation']").daterangepicker('hide');
    });
 });