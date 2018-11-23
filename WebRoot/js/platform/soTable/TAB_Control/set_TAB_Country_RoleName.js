$(function(){
	loadYearMenu();
});

function loadYearMenu(){
	var menuHtml = "";
	var isConfiTab = true;
	for(var i =0;i<TABArr.length;i++){
		if(TABArr[i].countryId == loginCountry){
			loadYearMenuEN(TABArr[i],language);
			isConfiTab = false;
			break;
		}
	}
	
	if(isConfiTab){//true：说明该国家没有配置选项卡名称、默认为使用菲律宾选项卡名称
		loadYearMenuEN(TABArr[0],language);
	}
};

function loadYearMenuEN(obj,language){
	if('en' == language){
		$('#year_reg_head').html(obj.en_region_head);
		$('#year_sale').html(obj.en_sale);
		$('#year_acfo').html(obj.en_ACFO);
		$('#year_country').html(obj.en_country);
		$('#year_region').html(obj.en_region);
	}else if('zh' == language){
		$('#year_reg_head').html(obj.zh_region_head);
		$('#year_sale').html(obj.zh_sale);
		$('#year_acfo').html(obj.zh_ACFO);
		$('#year_country').html(obj.zh_country);
		$('#year_region').html(obj.zh_region);
	}else{
		$('#year_reg_head').html(obj.ftzw_region_head);
		$('#year_sale').html(obj.ftzw_sale);
		$('#year_acfo').html(obj.ftzw_ACFO);
		$('#year_country').html(obj.ftzw_country);
		$('#year_region').html(obj.ftzw_region);
	}
};

function loadMonthMenu(){
	var menuHtml = "";
	var isConfiTab = true;
	for(var i =0;i<TABArr.length;i++){
		if(TABArr[i].countryId == loginCountry){
			loadMonthMenuEN(TABArr[i],language);
			isConfiTab = false;
			break;
		}
	}
	
	if(isConfiTab){//true：说明该国家没有配置选项卡名称、默认为使用菲律宾选项卡名称
		loadMonthMenuEN(TABArr[0],language);
	}
};

function loadMonthMenuEN(obj,language){
	if('en' == language){
		$('#month_regin_head').html(obj.en_region_head);
		$('#month_sale').html(obj.en_sale);
		$('#month_acfo').html(obj.en_ACFO);
	}else if('zh' == language){
		$('#month_regin_head').html(obj.zh_region_head);
		$('#month_sale').html(obj.zh_sale);
		$('#month_acfo').html(obj.zh_ACFO);
	}else{
		$('#month_regin_head').html(obj.ftzw_region_head);
		$('#month_sale').html(obj.ftzw_sale);
		$('#month_acfo').html(obj.ftzw_ACFO);
	}
};

function loadWeekMenu(){
	var menuHtml = "";
	var isConfiTab = true;
	for(var i =0;i<TABArr.length;i++){
		if(TABArr[i].countryId == loginCountry){
			loadWeekMenuEN(TABArr[i],language);
			isConfiTab = false;
			break;
		}
	}
	
	if(isConfiTab){//true：说明该国家没有配置选项卡名称、默认为使用菲律宾选项卡名称
		loadWeekMenuEN(TABArr[0],language);
	}
};

function loadWeekMenuEN(obj,language){
	if('en' == language){
		$('#week_region_head').html(obj.en_region_head);
		$('#week_sale').html(obj.en_sale);
		$('#week_acfo').html(obj.en_ACFO);
		$('#week_region').html(obj.en_region);
	}else if('zh' == language){
		$('#week_region_head').html(obj.zh_region_head);
		$('#week_sale').html(obj.zh_sale);
		$('#week_acfo').html(obj.zh_ACFO);
		$('#week_region').html(obj.zh_region);
	}else{
		$('#week_region_head').html(obj.ftzw_region_head);
		$('#week_sale').html(obj.ftzw_sale);
		$('#week_acfo').html(obj.ftzw_ACFO);
		$('#week_region').html(obj.ftzw_region);
	}
};
