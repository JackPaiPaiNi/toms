var select_personnel_or_area = "";
var loginPartyId = "";//登录partyId
var logincoutryId = "";
var emptySelection = '<option></option>';
var pType = '1';

var initializeCodeModel = true;




$(document).ready(function() {
	selectLine();
	
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
	    
	    
	/*loadBusinessCenter();
	//国家抉择
	$("#country").change(function(){ 
		//selectModel();
	});
	
	商业中心
	$("#center").change(function(){
		if($(this).val() != ''){
			loadCountry($(this).val());
		}else{
			$("#country").html("");
		}
		
	});*/
	
  
    
    //初始化表格[日期为本周本月本季度本年开始结束]
    selectSellInByHq(Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],'week');
    selectSellInByHq(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'month');
    selectSellInByHq(Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1],'quarter');
    selectSellInByHq( Common_date.getTheFirstDay()[0],Common_date.getTheFirstDay()[1],'year');
   
 });


/*加载产品系列*/
function selectLine(){
	$.ajax({
		url:baseUrl + 'platform/selectLine.action',
		type:"POST",
		success:function(data){
			var obj = eval("("+data+")");
			$('#line').html('');
			if(data != '' && data != null ){
			
				var str="";
				for(var i=0;i<obj.length;i++){
					str+="<option value="+obj[i].catena+">"+obj[i].catenaName+"</option>";
				}
			}
				$('#line').html(str);
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





//双日历控件
function bb(){
		 $('#reservationWeekly').html(moment().subtract('days', 29).format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));
		 $('#reservationWeekly').daterangepicker({minDate: '2016-01-01'});
			}
	

//拼接表格
function selectSellInByHq(beginDate,endDate,whatTime){
	var  div="";
	if(whatTime=="year"){
		div="year";
	}else if(whatTime=="quarter"){
		div="quarter";
	}else if(whatTime=="month"){
		div="month";
	}else if(whatTime=="week"){
		div="week";
	}
	var center=$("#center").val();
	var country=$("#country").val();
	var line=$("#line").val();
	var o = {};
	o.beginDate=beginDate;
	o.endDate=endDate;
	if(center!=null  &&center!=""){
		o.center=center;
	}
	
	if(country!=null  &&country!=""){
		o.country=country;
	}
	if(line!=null  &&line!=""){
		o.line=line;
	}
	
	
	$.ajax({
		url:baseUrl + "platform/selectSellInByHq.action",
		type:"POST",
		data:o,
		success:function(data){
			$("#"+div).html('');
	
			if(data.country != '' && data.country != null){
		
				var model = data.model;
				var party = data.country;
				var sum = data.sum;
				var data = data.data;


				var theadHtml = "";
				loadParty();
				function loadParty(){//拼接业务中心与国家
					theadHtml = "<thead class='fixedThead'><tr class='backgray'>" +
							"<th style='text-align: center'; width: 10px' rowspan='3' ></th>" +
						    "<th style='text-align: center';  width: 10px' rowspan='3' ></th>" +
								   "</tr>";	
					var partyCentreHtml = "<tr id= 'partyCentre'>";//区域中心（tr未结尾）
					var partyHtml = "<tr>";		//国家(tr未结尾)
					var partyCentreId = party[0].centerName;//储存第一个商业中心ID
					var countryCount = 0;//国家个数
					for(var i = 0;i < party.length ;i ++){
						if(party[i].centerName == partyCentreId){
							partyHtml += "<th style='width: 80px;text-align: center'>" + party[i].countryName + "</th>";
							countryCount ++;
							
							if(i == party.length-1){//识别最后一个加入
								partyCentreHtml += "<th style='text-align: center'  colspan="+ countryCount +">"+ party[i].centerName +"</th>" +
									"<th class='totalback' style='width: 80px;' rowspan='2' >SUB TOTAL</th>";
							}
							
						}else{
							countryCount = countryCount + "";
							partyCentreId = party[i].centerName;
							partyCentreHtml += "<th style='text-align: center'  colspan="+ countryCount +">"+ party[i-1].centerName + "</th>" +
									"<th class='totalback' style='width: 80px;' rowspan='2' >SUB TOTAL</th>";
							countryCount = 0;//区域国家个数清0
							i --;
						}
					};
					partyCentreHtml += "</tr>";
					partyHtml += "</tr>";
					theadHtml += partyCentreHtml + partyHtml + "</thead>";
				};

				var tbodyHtmls = "";
				loadData();
				function loadData(){//拼接SI数据
					var centerLineAllHtml = "<tr><td class='totalback' colspan='2'>SUB TOTAL</td>";
					tbodyHtmls = "<tbody class='scrollTbody'>" + loadCountryAll();
					var series = model[0].line;
					for(var i = 0;i < model.length;i ++){
						if(series == model[i].line){
							tbodyHtmls += "<tr><td>"+ model[i].line +"</td><td>"+ model[i].model +"</td>";
							var partyCentreId = party[0].centerName;
							for(var j = 0;j < party.length; j ++){
								if(party[j].centerName == partyCentreId){
									var isNull = true; 
									for( var k = 0;k < data.length;k ++){
										if(data[k].model == model[i].model && party[j].countryName == data[k].countryName){
											tbodyHtmls += "<td>"+ data[k].quantity +"</td>";
											isNull = false;
											break;
										}
									}
									if(isNull){
										tbodyHtmls += "<td>"+ 0 +"</td>";
									}
									
									if(j == party.length-1){//识别最后一个加入
										var isNUll = true; 
										for(var m = 0 ;m<sum.length;m++){//拼接整个业务中心的和
											if(sum[m].total == (party[j].centerName +"_"+ model[i].model +"_CenterModel")){
												tbodyHtmls += "<td>"+ sum[m].qty +"</td>";
												isNUll =  false;
												break;
											}
										}
										if(isNUll){
											tbodyHtmls += "<td>"+ 0 +"</td>";
										};
									}
									
								}else{
									var isNUll = true; 
									for(var m = 0 ;m<sum.length;m++){//拼接业务中心型号的和
										if(sum[m].total == (party[j-1].centerName +"_"+ model[i].model +"_CenterModel")){
											tbodyHtmls += "<td>"+ sum[m].qty +"</td>";
											isNUll =  false;
											break;
										}
									}
									if(isNUll){
										tbodyHtmls += "<td>"+ 0 +"</td>";
									};
									partyCentreId = party[j].centerName;
									j --;
								}
							}
							
							if(i == model.length - 1){
								var partyCentreId = party[0].centerName;
								for(var l = 0;l<party.length;l++){
									if(party[l].centerName == partyCentreId){
										var isNull = true; 
										for( var k = 0;k < sum.length;k ++){
											if(sum[k].total == party[l].countryName + "_" + model[i].line +"_CountryLine"){
												centerLineAllHtml += "<td>"+ sum[k].qty +"</td>";
												isNull = false;
												break;
											}
										}
										if(isNull){
											centerLineAllHtml += "<td>"+ 0 +"</td>";
										}
										
										if(l == party.length-1){//识别最后一个加入
											var isNUll = true; 
											for(var m = 0 ;m<sum.length;m++){//拼接整个业务中心的和
												if(sum[m].total == (party[l].centerName +"_"+ model[i].line +"_CenterLine")){
													centerLineAllHtml += "<td>"+ sum[m].qty +"</td>";
													isNUll =  false;
													break;
												}
											}
											if(isNUll){
												centerLineAllHtml += "<td>"+ 0 +"</td>";
											};
										}
										
									}else{
										var isNUll = true; 
										for(var m = 0 ;m<sum.length;m++){//拼接业务中心型号的和
											if(sum[m].total == (party[l-1].centerName +"_"+ model[i].line +"_CenterLine")){
												centerLineAllHtml += "<td>"+ sum[m].qty +"</td>";
												isNUll =  false;
												break;
											}
										}
										if(isNUll){
											centerLineAllHtml += "<td>"+ 0 +"</td>";
										};
										partyCentreId = party[l].centerName;
										l --;
									}
								}
								tbodyHtmls += centerLineAllHtml+"</tr>";
							}
						}else{
							var partyCentreId = party[0].centerName;
							for(var l = 0;l<party.length;l++){
								if(party[l].centerName == partyCentreId){
									var isNull = true; 
									for( var k = 0;k < sum.length;k ++){
										if(sum[k].total == party[l].countryName + "_" + model[i-1].line +"_CountryLine"){
											centerLineAllHtml += "<td>"+ sum[k].qty +"</td>";
											isNull = false;
											break;
										}
									}
									if(isNull){
										centerLineAllHtml += "<td>"+ 0 +"</td>";
									}
									
									if(l == party.length-1){//识别最后一个加入
										var isNUll = true; 
										for(var m = 0 ;m<sum.length;m++){//拼接整个业务中心的和
											if(sum[m].total == (party[l].centerName +"_"+ model[i-1].line +"_CenterLine")){
												centerLineAllHtml += "<td>"+ sum[m].qty +"</td>";
												isNUll =  false;
												break;
											}
										}
										if(isNUll){
											centerLineAllHtml += "<td>"+ 0 +"</td>";
										};
									}
									
								}else{
									var isNUll = true; 
									for(var m = 0 ;m<sum.length;m++){//拼接业务中心型号的和
										if(sum[m].total == (party[l-1].centerName +"_"+ model[i-1].line +"_CenterLine")){
											centerLineAllHtml += "<td>"+ sum[m].qty +"</td>";
											isNUll =  false;
											break;
										}
									}
									if(isNUll){
										centerLineAllHtml += "<td>"+ 0 +"</td>";
									};
									partyCentreId = party[l].centerName;
									l --;
								}
							}
							tbodyHtmls += centerLineAllHtml+"</tr>";
							centerLineAllHtml = "<tr><td class='totalback' colspan='2'>SUB TOTAL</td>";
							series = model[i].line;
							i --;
						}
					}
				};

				function loadCountryAll(){
					var partyCentreId = party[0].centerName;
					var countryAllHtml = "<td class='totalback' colspan='2'>TOTAL</td>";
					for(var j = 0;j < party.length; j ++){
						if(party[j].centerName == partyCentreId){
							var isNUll = true;
							for( var k = 0;k < sum.length;k ++){//拼接国家的和
								if(sum[k].total == (party[j].countryName + "_Country")){
									countryAllHtml += "<td>"+ sum[k].qty +"</td>";
									isNUll =  false;
									break;
								}
							}
							if(isNUll){
								countryAllHtml += "<td>"+ 0 +"</td>";
							}
							if(j == party.length-1){//识别最后一个加入
								var isNUll = true; 
								for(var i = 0 ;i<sum.length;i++){//拼接整个业务中心的和
									if(sum[i].total == (party[j].centerName + "_Center")){
										countryAllHtml += "<td>"+ sum[i].qty +"</td>";
										isNUll =  false;
										break;
									}
								}
								if(isNUll){
									countryAllHtml += "<td>"+ 0 +"</td>";
								};
							}
						}else{
							var isNUll = true; 
							for(var i = 0 ;i<sum.length;i++){//拼接整个业务中心的和
								if(sum[i].total == (party[j-1].centerName + "_Center")){
									countryAllHtml += "<td>"+ sum[i].qty +"</td>";
									isNUll =  false;
									break;
								}
							}
							if(isNUll){
								countryAllHtml += "<td>"+ 0 +"</td>";
							};
							partyCentreId = party[j].centerName;
							j --;
						}
					}
					return countryAllHtml;
				};
				
				$("#"+div).html(theadHtml + tbodyHtmls);
			
		}
		}
	});
	
	function isMap(data,countryName, model){
		  for(var i = 0, len = data.data.length; i < len; i++){
		    if(data.data[i].model==model 
					&& data.data[i].countryName==countryName ){
		      return true;
		    }
		  }
		  return false;
		}
	
}

function getData(obj,time){
	var date=$(obj).val();
	if(date!=null && date!=""){
		var spDate = new Array();
		spDate=date.split(" - ");
		var beginDate=spDate[0];
		var endDate=spDate[1];
		if(time=="year"){
			beginDate=date+"-01-01";
			var last=getLastDay(date,12);
			endDate=date+"-12-"+last;
		}else if(time=="quarter"){
			spDate=date.split("-");
			var year=spDate[0];
			var qua=spDate[1];
			if(qua=="Q1"){
				beginDate=year+"-01-01";
				var last=getLastDay(year,3);
				endDate=year+"-03-"+last;
			}else if(qua=="Q2"){
				beginDate=year+"-04-01";
				var last=getLastDay(year,6);
				endDate=year+"-06-"+last;
			}else if(qua=="Q3"){
				beginDate=year+"-07-01";
				var last=getLastDay(year,9);
				endDate=year+"-09-"+last;
			}else	if(qua=="Q4"){
				beginDate=year+"-10-01";
				var last=getLastDay(year,12);
				endDate=year+"-12-"+last;
			}
			
		}else if(time=="month"){
			beginDate=date+"-01";
			var days=date.split("-");
			var last=getLastDay(days[0],days[1]);
			endDate=date+"-"+last;
			
		}else if(time=="week"){
			beginDate=beginDate;
			endDate=endDate;
		}
		selectSellInByHq(beginDate,endDate,time);
		
	}
}

function  weekV(obj,week){
	var flag=true;
	var date = $("#reservationWeekly").val();
	var dateArr = date.split(' - ');
	if(new Date(dateArr[0]).getDay() != 6 && new Date(dateArr[0]).getDay() != 1){
		alert('Start time must be on Saturday or on Monday');
		flag=false;
	}else{
		if(new Date(dateArr[1]).getDay() != 5 && new Date(dateArr[1]).getDay() != 0){
			alert('End time must be on Friday or Sunday');
			flag=false;
		}else{
			if((new Date(dateArr[1]).getTime() - new Date(dateArr[0]).getTime())/(24 * 60 * 60 * 1000) != 6){
				alert('Not a week');
				flag=false;
			}
		}
	}
	if(flag==true){
		 getdata(obj,week);
	}
}
function getLastDay(year, month){    
    var dt = new Date(year, month - 1, '01');    
    dt.setDate(1);    
    dt.setMonth(dt.getMonth() + 1);    
    cdt = new Date(dt.getTime()-1000*60*60*24);    
    return cdt.getDate();  
    //alert(cdt.getFullYear()+"年"+(Number(cdt.getMonth())+1)+"月月末日期:"+cdt.getDate()+"日");     
}  



function getWeek(){
	var dateWeek=$("#reservationWeekly").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateWeek!=null && dateWeek!=""){
		spDate=dateWeek.split(" - ");
		beginDate=spDate[0];
		endDate=spDate[1];
		selectSellInByHq(beginDate,endDate,'week');

	}else{
		selectSellInByHq(Common_date.getMondayAndWeekend()[0],Common_date.getMondayAndWeekend()[1],'week');
	}
}

function getMonth(){
	var dateMonth=$("#reservationMonthly").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	if(dateMonth!=null && dateMonth!=""){
		beginDate=dateMonth+"-01";
		var days=dateMonth.split("-");
		var last=getLastDay(days[0],days[1]);
		endDate=dateMonth+"-"+last;
	    selectSellInByHq(beginDate,endDate,'month');

	}else{
	    selectSellInByHq(Common_date.getEarlierMonth(),Common_date.getLaterMonth(),'month');

	}
	
}
function getQua(){
	var dateQua=$("#reservationQuarterly").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateQua!=null && dateQua!=""){
		spDate=dateQua.split("-");
		var year=spDate[0];
		var qua=spDate[1];
		if(qua=="Q1"){
			beginDate=year+"-01-01";
			var last=getLastDay(year,3);
			endDate=year+"-03-"+last;
		}else if(qua=="Q2"){
			beginDate=year+"-04-01";
			var last=getLastDay(year,6);
			endDate=year+"-06-"+last;
		}else if(qua=="Q3"){
			beginDate=year+"-07-01";
			var last=getLastDay(year,9);
			endDate=year+"-09-"+last;
		}else	if(qua=="Q4"){
			beginDate=year+"-10-01";
			var last=getLastDay(year,12);
			endDate=year+"-12-"+last;
		}
	    selectSellInByHq(beginDate,endDate,'quarter');

	}else{
	    selectSellInByHq(Common_date.getQuarterDate()[0],Common_date.getQuarterDate()[1],'quarter');

	}
}
function getYear(){
	var dateYear=$("#reservationAnnual").val();
	var spDate = new Array();
	var beginDate;
	var endDate;
	
	if(dateYear!=null && dateYear!=""){
		var last=getLastDay(dateYear,12);
	    selectSellInByHq( dateYear+"-01-01",dateYear+"-12-"+last,'year');

	}else{
	    selectSellInByHq( Common_date.getTheFirstDay()[0],Common_date.getTheFirstDay()[1],'year');
	}
}

function  searchData(){
	getWeek();
	getMonth();
	getQua();
	getYear();
}




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



function parseJSONStr(string){//解析字符串为JSON对象
	return eval('(' + string + ')');
};