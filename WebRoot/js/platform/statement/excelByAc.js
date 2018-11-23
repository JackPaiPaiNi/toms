
var soType;

$(function(){
	getSoType();
});

function  getSoType(){
	
	$.ajax({
		url:baseUrl + "platform/selectType.action",
		
		 dataType: "json", 
		 type:"POST",
		 cache : false,
		success:function(data){
		        if(data.success=="true")
	        	{	$("#upTe").html('');
	        	var strs;
	        	if(data.msg==0 || data.msg=="0"){
	        		$(".ReportDownload").html('');
	        		soType="country";
	        		$("#upTe").html(
	        	
	         	'	<span class="KeyUpload"  tcl-text="statement.upload.country">Upload(with country) </span>'+
	         	'	<a class="aKeyUpload" href="#"  onclick="importPHSales(\'country\')"  plain="true" ></a> ')
	         	
	         	strs='<hr/>'+
	     			'<li><a href="javascript:exportTemplate(\'country\')"  plain="true"  >Template(with country)</a></li>';
			     	
	        	}else if(data.msg==1 || data.msg=="1"){
	        		soType="customer";
	        		$("#upTe").html(
	    		         	'	<span class="KeyUpload"  tcl-text="statement.upload.customer">Upload(with customers) </span>'+
	    		         		'<a class="aKeyUpload" href="#"  onclick="importPHSales(\'customer\')"  plain="true" ></a> ')
	        		strs='<hr/>'+
	     			'<li><a href="javascript:exportTemplate(\'customer\')"  plain="true"  >Template(with customer)</a></li>';
			 
	        	}else if(data.msg==2 || data.msg=="2"){
	        		soType="shop";
	        		$("#upTe").html(
	    		         	'	<span class="KeyUpload" tcl-text="statement.upload"><!--一键上传-->Upload(with stores)</span> '+
	    		         	'	<a class="aKeyUpload" href="#"  onclick="importPHSales(\'shop\')"  plain="true" ></a> '+
	    		        	'	<span class="KeyUpload" tcl-text="statement.upload"><!--一键上传-->Upload local templates(with stores)</span> '+
	    		         	'	<a class="aKeyUpload" href="#"  onclick="importPHSales(\'local\')"  plain="true" ></a> ')
	    		         	strs='<hr/>'+
			     			'<li><a href="javascript:exportTemplate(\'shop\')"  plain="true"  >Template(with stores)</a></li>';
					 
	        	
	        	}else if(data.msg==3 || data.msg=="3"){
	        		$(".ReportDownload").html('');
	        		$("#upTe").html(
	    		         	'	<span class="KeyUpload" tcl-text="statement.upload"><!--一键上传-->Upload(with stores)</span> '+
	    		         	'	<a class="aKeyUpload" href="#"  onclick="importPHSales(\'shop\')"  plain="true" ></a> '+
	    		         	'	<span class="KeyUpload"  tcl-text="statement.upload.customer">Upload(with customers) </span>'+
	    		         		'<a class="aKeyUpload" href="#"  onclick="importPHSales(\'customer\')"  plain="true" ></a> ')
	    		         	strs=
	    		         		'<hr/>'+
				     			'<li><a href="javascript:exportTemplate(\'shop\')"  plain="true"  >Template(with stores)</a></li>'+
			     			'<li><a href="javascript:exportTemplate(\'customer\')"  plain="true"  >Template(with customers)</a></li>';
	        	
	        	}else if(data.msg=="admin"){
	        		if(partyList.indexOf("\'2\'") != -1 && isAdmin!="true"){
	        			$(".ReportDownload").html('');
		        		soType="country";
	        		}else{
	        			soType="admin";
	        		}
	        		//$(".ReportDownload").html('');
	        		$("#upTe").html(
	    		         	'	<span class="KeyUpload" tcl-text="statement.upload"><!--一键上传-->Upload(with stores)</span> '+
	    		         	'	<a class="aKeyUpload" href="#"  onclick="importPHSales(\'shop\')"  plain="true" ></a> '+
	    		         	'	<span class="KeyUpload"  tcl-text="statement.upload.customer">Upload(with customers)</span>'+
	    		         		'<a class="aKeyUpload" href="#"  onclick="importPHSales(\'customer\')"  plain="true" ></a> '+
	    		         	
	    		         	'	<span class="KeyUpload"  tcl-text="statement.upload.country">Upload(with country) </span>'+
	    		         	'	<a class="aKeyUpload" href="#"  onclick="importPHSales(\'country\')"  plain="true" ></a> '
	    		         	
	    		         		  
	    		         		  
	    		         	)
	    		         	strs='<hr/>'+
			     			'<li><a href="javascript:exportTemplate(\'country\')"  plain="true"  >Template(with country)</a></li>'+
	    		         		'<hr/>'+
				     			'<li><a href="javascript:exportTemplate(\'shop\')"  plain="true"  >Template(with stores)</a></li>'+
				     			'<hr/>'+
			     			'<li><a href="javascript:exportTemplate(\'customer\')"  plain="true"  >Template(with customers)</a></li>';
	        	
	        	}
	        	/*if(country==16 || country=="16"){
	         		strs='<li><a href="<%=basePath%>download/TV SO data upload.xlsx"  plain="true"  >Template</a></li>';
	         		
	         	}*/
	        	$("#panel").html('');
	        	$("#panel").html(strs);
	        	}
		        
		}
	});

}

function getCorrespondingDate(instrument){
	var myDate = new Date();
	if(instrument == "year"){//年
		myDate.setFullYear(myDate.getFullYear()-1);
	}else if(instrument == "quarter"){//季度
		myDate.setMonth(myDate.getMonth()-3);
	}else if(instrument == "month"){//月
		myDate.setMonth(myDate.getMonth() -1);
	}else if(instrument == "week"){//周
		myDate.setDate(myDate.getDate()-7);
	}
	return myDate.Format("yyyy-MM-dd");
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



function getdata(obj){
	var date=$(obj).prev("input").val();
	if(date==null || date==""){
		alert("Please select a date!");
	}else{
		var level=$("#storeLevel").val();
		var spDate = new Array();
		spDate=date.split(" - ");
		var beginDate=spDate[0];
		var endDate=spDate[1];
		var id=obj.id;
		var str="";
		var center=$("#center").val();
		var country=$("#country").val();
		var region=$("#region").val();
		var  office=$("#office").val();
		var business_Mana=$("#business_Mana").val();
		var supervisor=$("#supervisor").val();
		if(soType=='admin'){
			if(country==null || country==""){
				alert("Please choose the country !");
				return;
			}else{
				$.ajax({
					dataType:"json",
					url: "platform/selectType.action",
					data: {
						 "country":country
					 }, 
					success:function(data){
					        if(data.success=="true")
				        	{
					        	if(data.msg==0 || data.msg=="0"){
					        		alert("The country's SO import type does not support downloading");
					        		return;
					        	}else if(data.msg==1 || data.msg=="1"){
					        		soType="customer";
					        	}else if(data.msg==2 || data.msg=="2"){
					        		soType="shop";
					        	}else if(data.msg==3 || data.msg=="3"){
					        		alert("The country's SO import type does not support downloading");
					        		return;
					        	}else if(data.msg=="admin"){
					        		alert("The country's SO import type does not support downloading");
					        		return;
					        	}
					        	if(id=="daily"){
									if(center!="" && center!=null){
										str+="&center="+center;
									}
									if(country!="" && country!=null){
										str+="&country="+country;
									} 
									if(office!=""  &&office!=null){
										str+="&office="+office;
									}
									if(region!=""  && region!=null ){
										str+="&region="+region;
									}
									
								/*	if(level!=""  && level!=null ){
										//str+="&level="+level;
									}*/
								
							
									var url ;
									if(soType=="shop"){
						        		url =   baseUrl+ "platform/exportDailyByAc.action?date="+date+str+"";

									}else if(soType=="customer"){
						        		url =  baseUrl+ "platform/exportDailyByAcCustomer.action?date="+date+str+"";

									}
							
									location.href = url;

								}else if(id=="weekly"){
									if(center!="" && center!=null){
										str+="&center="+center;
									}
									if(country!="" && country!=null){
										str+="&country="+country;
									} 
									if(office!=""  &&office!=null){
										str+="&office="+office;
									}if(region!=""  && region!=null ){
										str+="&region="+region;
									}
									/*if(level!=""  && level!=null ){
										//str+="&level="+level;
									}*/
									
								
							
									var url ;
							
									if(soType=="shop"){
						        		url =baseUrl + "platform/exportWeeklyByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}else if(soType=="customer"){
						        		url =  baseUrl + "platform/exportWeeklyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}
							
									
									location.href = url;
									
								}else if(id=="monthly"){
									beginDate=date+"-01";
									var days=date.split("-");
									var last=getLastDay(days[0],days[1]);
									endDate=date+"-"+last;
									if(center!="" && center!=null){
										str+="&center="+center;
									}
									if(country!="" && country!=null){
										str+="&country="+country;
									} 
									if(office!=""  &&office!=null){
										str+="&office="+office;
									}if(region!=""  && region!=null ){
										str+="&region="+region;
									}
									if(level!=""  && level!=null ){
										//str+="&level="+level;
									}
									
									var url ;
									
									if(soType=="shop"){
						        		url =baseUrl + "platform/exportMonthlyByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}else if(soType=="customer"){
						        		url =baseUrl + "platform/exportMonthlyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}
							
									
									location.href = url;
									
								}else if(id=="quarterly"){
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
									str+="&qua="+qua;
									
									if(center!="" && center!=null){
										str+="&center="+center;
									}
									if(country!="" && country!=null){
										str+="&country="+country;
									} 
									if(office!=""  &&office!=null){
										str+="&office="+office;
									}if(region!=""  && region!=null ){
										str+="&region="+region;
									}
									if(level!=""  && level!=null ){
										//str+="&level="+level;
									}
									var url ;
									
									if(soType=="shop"){
						        		url =baseUrl + "platform/exportQuarterlyByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}else if(soType=="customer"){
						        		url =baseUrl + "platform/exportQuarterlyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}
									
									location.href = url;
									
								
								}else if(id=="annual"){
									beginDate=date+"-01-01";
									var last=getLastDay(date,12);
									endDate=date+"-12-"+last;
									
									if(center!="" && center!=null){
										str+="&center="+center;
									}
									if(country!="" && country!=null){
										str+="&country="+country;
									} 
									if(office!=""  &&office!=null){
										str+="&office="+office;
									}if(region!=""  && region!=null ){
										str+="&region="+region;
									}
									if(level!=""  && level!=null ){
										//str+="&level="+level;
									}
									
									var url ;
									
									if(soType=="shop"){
						        		url = baseUrl + "platform/exportAnnualByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}else if(soType=="customer"){
						        		url = baseUrl + "platform/exportAnnualByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}
									
									
									
									location.href = url;
									
								
								}else if(id="custom"){
									if(center!="" && center!=null){
										str+="&center="+center;
									}
									if(country!="" && country!=null){
										str+="&country="+country;
									} 
									if(office!=""  &&office!=null){
										str+="&office="+office;
									}if(region!=""  && region!=null ){
										str+="&region="+region;
									}
									if(level!=""  && level!=null ){
										//str+="&level="+level;
									}
									
									
									var url ;
									
									if(soType=="shop"){
						        		url = baseUrl + "platform/exportCustomByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}else if(soType=="customer"){
						        		url = baseUrl + "platform/exportCustomByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

									}
									
									
									location.href = url;
									
								}
					        	
				        	}
					       
					}
				});
			}
		}else{
			//需要判断他是日报表还是月报表
			if(id=="daily"){
				if(center!="" && center!=null){
					str+="&center="+center;
				}
				if(country!="" && country!=null){
					str+="&country="+country;
				} 
				if(office!=""  &&office!=null){
					str+="&office="+office;
				}if(region!=""  && region!=null ){
					str+="&region="+region;
				}
				if(level!=""  && level!=null ){
					//str+="&level="+level;
				}
				
				var url ;
				if(soType=="shop"){
	        		url =baseUrl+ "platform/exportDailyByAc.action?date="+date+str+"";

				}else if(soType=="customer"){
	        		url = baseUrl+ "platform/exportDailyByAcCustomer.action?date="+date+str+"";

				}
				
		/*		var url =  "http://121.96.67.248:7081/"baseUrl+ "platform/exportDaily.action?beginDate="+beginDate+"&endDate="+endDate+str+"";
		*/		//var url =  /*"http://121.96.67.248:7081/"*/baseUrl+ "platform/exportDailyByAcCustomer.action?date="+date+str+"";
			
				location.href = url;

			}else if(id=="weekly"){
				if(center!="" && center!=null){
					str+="&center="+center;
				}
				if(country!="" && country!=null){
					str+="&country="+country;
				} 
				if(office!=""  &&office!=null){
					str+="&office="+office;
				}if(region!=""  && region!=null ){
					str+="&region="+region;
				}
				if(level!=""  && level!=null ){
					//str+="&level="+level;
				}
				

				var url ;
				if(soType=="shop"){
	        		url = baseUrl + "platform/exportWeeklyByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}else if(soType=="customer"){
	        		url =  baseUrl + "platform/exportWeeklyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}
				
				//var url = baseUrl + "platform/exportWeeklyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";
				location.href = url;
				
			}else if(id=="monthly"){
				beginDate=date+"-01";
				var days=date.split("-");
				var last=getLastDay(days[0],days[1]);
				endDate=date+"-"+last;
				if(center!="" && center!=null){
					str+="&center="+center;
				}
				if(country!="" && country!=null){
					str+="&country="+country;
				} 
				if(office!=""  &&office!=null){
					str+="&office="+office;
				}if(region!=""  && region!=null ){
					str+="&region="+region;
				}
				if(level!=""  && level!=null ){
					//str+="&level="+level;
				}
				
				var url ;
				if(soType=="shop"){
	        		url = baseUrl + "platform/exportMonthlyByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}else if(soType=="customer"){
	        		url =baseUrl + "platform/exportMonthlyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}
				
				
				
				//var url = baseUrl + "platform/exportMonthlyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";
				location.href = url;
				
			}else if(id=="quarterly"){
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
				str+="&qua="+qua;
				
				if(center!="" && center!=null){
					str+="&center="+center;
				}
				if(country!="" && country!=null){
					str+="&country="+country;
				} 
				if(office!=""  &&office!=null){
					str+="&office="+office;
				}if(region!=""  && region!=null ){
					str+="&region="+region;
				}
				if(level!=""  && level!=null ){
					//str+="&level="+level;
				}
				
				var url ;
				if(soType=="shop"){
	        		url =  baseUrl + "platform/exportQuarterlyByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}else if(soType=="customer"){
	        		url = baseUrl + "platform/exportQuarterlyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}
				
				
				
				
				//var url = baseUrl + "platform/exportQuarterlyByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";
				location.href = url;
				
			
			}else if(id=="annual"){
				beginDate=date+"-01-01";
				var last=getLastDay(date,12);
				endDate=date+"-12-"+last;
				
				if(center!="" && center!=null){
					str+="&center="+center;
				}
				if(country!="" && country!=null){
					str+="&country="+country;
				} 
				if(office!=""  &&office!=null){
					str+="&office="+office;
				}if(region!=""  && region!=null ){
					str+="&region="+region;
				}
				if(level!=""  && level!=null ){
					//str+="&level="+level;
				}
				var url ;
				if(soType=="shop"){
	        		url =  baseUrl + "platform/exportAnnualByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}else if(soType=="customer"){
	        		url =  baseUrl + "platform/exportAnnualByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}
				
				
				
				
			//	var url = baseUrl + "platform/exportAnnualByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";
				location.href = url;
				
			
			}else if(id="custom"){
				if(center!="" && center!=null){
					str+="&center="+center;
				}
				if(country!="" && country!=null){
					str+="&country="+country;
				} 
				if(office!=""  &&office!=null){
					str+="&office="+office;
				}if(region!=""  && region!=null ){
					str+="&region="+region;
				}
				if(level!=""  && level!=null ){
					//str+="&level="+level;
				}
				
				var url ;
				if(soType=="shop"){
	        		url =  baseUrl + "platform/exportCustomByAc.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}else if(soType=="customer"){
	        		url =  baseUrl + "platform/exportCustomByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";

				}
				
				//var url = baseUrl + "platform/exportCustomByAcCustomer.action?beginDate="+beginDate+"&endDate="+endDate+str+"";
				location.href = url;
				
			}
		}
		
		
	}
	

	function exportExcel(obj){
		var date=$(obj).prev("input").val();
		var spDate = new Array();
		spDate=date.split(" - ");
		var center=$("#center").val();
		var country=$("#country").val();
		var region=$("#region").val();
		var  office=$("#office").val();
		var business_Mana=$("#business_Mana").val();
		var supervisor=$("#supervisor").val();
		var beginDate=spDate[0];
		var endDate=spDate[1];
		var id=obj.id;
		var str="";
		if(center!="" && center!=null){
			str+="&center="+center;
		}
		if(country!="" && country!=null){
			str+="&country="+country;
		} 
		if(office!=""  &&office!=null){
			str+="&office="+office;
		}if(region!=""  && region!=null ){
			str+="&region="+region;
		}
		var url = baseUrl + "platform/exportSaleExcelAction.action?beginDate="+beginDate+"&endDate="+endDate+str+"";
		location.href = url;
	}



	function exportVolumeAch(){
		var center=$("#center").val();
		var country=$("#country").val();
		var region=$("#region").val();
		var  office=$("#office").val();
		var business_Mana=$("#business_Mana").val();
		var supervisor=$("#supervisor").val();
		var instrument=$("#instrument").val();
		var str="";
		var  beginDate=getCorrespondingDate(instrument);
		if(center!="" && center!=null){
			str+="&center="+center;
		}
		if(country!="" && country!=null){
			str+="&country="+country;
		} 
		if(office!=""  &&office!=null){
			str+="&office="+office;
		}if(region!=""  && region!=null ){
			str+="&region="+region;
		}
		var url = baseUrl + "platform/exportVolumeAch.action?instrument="+instrument+"&beginDate="+beginDate+str+"";
		location.href = url;
		
	}
	
	
}


function exportValueAch(){
	var center=$("#center").val();
	var country=$("#country").val();
	var region=$("#region").val();
	var  office=$("#office").val();
	var business_Mana=$("#business_Mana").val();
	var supervisor=$("#supervisor").val();
	var instrument=$("#instrument").val();
	var str="";
	var  beginDate=getCorrespondingDate(instrument);
	if(center!="" && center!=null){
		str+="&center="+center;
	}
	if(country!="" && country!=null){
		str+="&country="+country;
	} 
	if(office!=""  &&office!=null){
		str+="&office="+office;
	}if(region!=""  && region!=null ){
		str+="&region="+region;
	}
	var url = baseUrl + "platform/exportValueAch.action?instrument="+instrument+"&beginDate="+beginDate+str+"";
	location.href = url;
	
}



function exportAchievingRate(){
	
	var center=$("#center").val();
	var country=$("#country").val();
	var region=$("#region").val();
	var  office=$("#office").val();
	var business_Mana=$("#business_Mana").val();
	var supervisor=$("#supervisor").val();
	var sales_rank=$("#sales_rank").val();
	var instrument=$("#instrument").val();
	
	var str="";
	var  beginDate=getCorrespondingDate(instrument);
	if(center!="" && center!=null){
		str+="&center="+center;
	}
	if(country!="" && country!=null){
		str+="&country="+country;
	} 
	if(office!=""  &&office!=null){
		str+="&office="+office;
	}if(region!=""  && region!=null ){
		str+="&region="+region;
	}
	
	var url="";
	if(sales_rank=="businessManager"){
		url = baseUrl + "platform/exportAchievingRateByBM.action?instrument="+instrument+"&beginDate="+beginDate+str+"";
	}
	if(sales_rank=="salesman"){
		url = baseUrl + "platform/exportAchievingRateBySM.action?instrument="+instrument+"&beginDate="+beginDate+str+"";
	}
	if(sales_rank=="supervisor"){
		url = baseUrl + "platform/exportAchievingRateBySV.action?instrument="+instrument+"&beginDate="+beginDate+str+"";
	}
	
	location.href = url;

}

function  weekV(obj){
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
		 getdata(obj);
	}
}



function  setCore(){
	var arr_v = new Array(); 
	$('input[name="core"]:checked').each(function(){
		 arr_v.push($(this).val()); 
	});
	$.ajax({
		url:baseUrl + "platform/setCoreProduct.action?core="+arr_v+"",
		type:"POST",
		success:function(data){
			alert("Success!");
		}
	});
}




function loadLine(){
	$.ajax({
		url:baseUrl + "platform/selectAllCore.action",
		type:"POST",
		success:function(data){
			var array=["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R"
			          ,"S","T","U","V","W","X","Y","Z"];
		$.each($.parseJSON(data), function(i, item){
			var s=i.charAt(0);
			for ( var int = 0; int < array.length; int++) {
				if(s==""+array[int]+""){
					$("#series"+array[int]+"").append(item);
				}
			}
			
		});
		 }		 
	});
}


function  seletCore(){
	$.ajax({
		url:baseUrl + "platform/selectCore.action",
		type:"POST",
		success:function(data){
		var core = new Array(); 
		core=data.split(",");
		for (var int = 0; int < core.length; int++) {
		$('input[name="core"]').each(function(){
			if($(this).val()==core[int]){
				$(this).attr("checked",'checked');
			}		
		});
		
		}
	
		}
	});
	
}

function importSales(){
	showImportWin(locale("sale.import"),baseUrl + "platform/importExcelAction.action");
}


function importPHSales(what){
	
	showImportWin(locale("sale.import"),baseUrl + "platform/importPHExcelAction.action?what="+what);
}




function exportTemplate(what){
	var url = baseUrl + "platform/exportTemplate.action?spec=2&what="+what;
	location.href = url;
}


function getLastDay(year, month){    
    var dt = new Date(year, month - 1, '01');    
    dt.setDate(1);    
    dt.setMonth(dt.getMonth() + 1);    
    cdt = new Date(dt.getTime()-1000*60*60*24);    
    return cdt.getDate();  
    //alert(cdt.getFullYear()+"年"+(Number(cdt.getMonth())+1)+"月月末日期:"+cdt.getDate()+"日");     
}  


$(document).ready(function(){
	  $("#flip").click(function(){
	    $("#panel").toggle();
	  });
	   $("#panel>li>a").click(function(){
	    $("#panel").toggle();
	  });
	   $('.aBulkimport').click(function(){
			$('.Bulk_upload_content').show();
		})
		$('.Shut_down').click(function(){
			$('.Bulk_upload_content').hide();
		});
	   

		$('.Error_message').click(function(){
			$('.Error_message').hide();
		});
		var  i =0;
		$('.Add').click(function () {
			var country="country"+i;
			var shop="shopId"+i;
			var date="date"+i;
			var model="model"+i;
			var qty="qty"+i;
			var amt="amt"+i;
			var type="type"+i;
	       	$('#tbody').append('<tr><td><input type="text"  onblur=verifyCountry("'+country+'","'+i+'")  id="'+country+'" /><i></i></td>'
	       			+'<td><input type="text"  onblur=verifyShop("'+shop+'")  id="'+shop+'" /><i></i></td>'
	       			+'<td><input type="text"   onblur=verifyDate("'+date+'")  id="'+date+'" /><i></i></td>'
	       			+'<td><input type="text"    onblur=verifyType("'+type+'")  id="'+type+'"/><i></i></td>'
	       			+'<td><input type="text"    onblur=verifyModel("'+model+'","'+i+'")  id="'+model+'"/><i></i></td>'
	       			+'<td><input type="text"   onblur=verifyQty("'+qty+'")  id="'+qty+'" /><i></i></td>'
	       			+'<td><input type="text"    onblur=verifyAmt("'+amt+'")  id="'+amt+'"/><i></i></td>'
	       			+'<td><i></i></td></tr>');

	       	aaaa();
	       	i++;
		});
		aaaa();
	   
		
		

		

	});

function aaaa(){
	$('.Bulk_upload_tb table tbody tr td:last-child i').click(function () {
      $(this).parent().parent().remove();
  });
}

function verifyCountry(country,i){
	$("#"+country).next().removeClass();
	 
	var str="what=country&country="+$("#"+country).val();
	if($("#"+country).val()==null || $("#"+country).val()==""){
		//alert("Please enter the Country");
		$("#"+country).next().addClass('match_error');
		if($("#model"+i).val()!=null && $("#model"+i).val()!=""){
			$("#model"+i).val('');
			$("#model"+i).next().addClass('match_error');
		}
		return;
	}
	$.ajax({
		url:baseUrl + "platform/importSoVerify.action?"+str,
		type:"POST",
		success:function(data){
			if(data!=null && data!=""){
				$("#"+country).next().removeClass();
				if(data.msg=="true"){
					$("#"+country).next().addClass('Match_correct');
					if($("#model"+i).val()!=null && $("#model"+i).val()!=""){
						$("#model"+i).val('');
						$("#model"+i).next().addClass('match_error');
					}
				}else{
					//alert("The Country is wrong");
					$("#"+country).next().addClass('match_error');
					if($("#model"+i).val()!=null && $("#model"+i).val()!=""){
						$("#model"+i).val('');
						$("#model"+i).next().addClass('match_error');
					}
				}
				
			}
		}
	});
	
	
}

function verifyShop(shopId){
	$("#"+shopId).next().removeClass();

	var str="what=shop&shop="+$("#"+shopId).val();

	if($("#"+shopId).val()==null || $("#"+shopId).val()==""){
		//	alert("Please enter the Shop");
		$("#"+shopId).next().addClass('match_error');
		return;
	}
	$.ajax({
		url:baseUrl + "platform/importSoVerify.action?"+str,
		type:"POST",
		success:function(data){
			$("#"+shopId).next().removeClass();
			if(data!=null && data!=""){
				if(data.msg=="true"){
					$("#"+shopId).next().addClass('Match_correct');
				}else{
					//alert("The Shop is wrong");
					$("#"+shopId).next().addClass('match_error');
				}
				
			}
		}
	});
	
}



function verifyType(type){
	$("#"+type).next().removeClass();
	var types=$("#"+type).val();
	if($("#"+type).val()==null || $("#"+type).val()==""){
		$("#"+type).next().addClass('match_error');
		return;
	}else{
		if(types.toLowerCase() =="sold"  || types.toLowerCase() =="stocks"  || types.toLowerCase()=="display"){
			$("#"+type).next().addClass('Match_correct');

		}else {
			$("#"+type).next().addClass('match_error');

		}
	}
	
}


function verifyDate(date){
	$("#"+date).next().removeClass();
	var dates=$("#"+date).val();
	if($("#"+date).val()==null || $("#"+date).val()==""){
		//	alert("Please enter the Date");
		$("#"+date).next().addClass('match_error');
		return;
	}else{
		var r = new RegExp("^(0?[1-9]||[1-2][1-9]||3[0-1])/(0?[1-9]||1[0-2])/[1-2]\\d{3}$");
		if(r.test(dates)){
			var da=dates.split("/");
			
			var thetime =da[2]+"-"+da[1]+"-"+da[0];
			var   d=new   Date(Date.parse(thetime .replace(/-/g,"/")));
			var   curDate=new   Date();
			if(d >curDate){
			$("#"+date).next().addClass('match_error');
			}else{
				$("#"+date).next().addClass('Match_correct');

			}
			

			
		}else{
			$("#"+date).next().addClass('match_error');
		}
		
		
		
		
		
		
	}
	
}

function verifyModel(model,i){
	$("#"+model).next().removeClass();
	var country=$("#country"+i).val();
	var models=$("#"+model).val();

	
	if(country==null  || country==""){
		//alert("Please enter the Country");
		$("#"+model).next().addClass('match_error');
		return;
	}else{
		if($("#country"+i).next().hasClass('match_error')){
			//alert("The Country is wrong");
			return;
		}
		
	}
	
	
	if(models==null  || models==""){
		//alert("Please enter the Model");
		$("#"+model).next().addClass('match_error');
		return;
	}
	var str="what=model&model="+models+"&country="+country;
	
	$.ajax({
		url:baseUrl + "platform/importSoVerify.action?"+str,
		type:"POST",
		success:function(data){
			if(data!=null && data!=""){
				$("#"+model).next().removeClass();
				if(data.msg=="true"){
					$("#"+model).next().addClass('Match_correct');
				}else{
					//alert("The Model is wrong");
					$("#"+model).next().addClass('match_error');
				}
				
			}
		}
	});
}

function verifyQty(qty){
	$("#"+qty).next().removeClass();
	
	if($("#"+qty).val()==null  || $("#"+qty).val()==""){
		//alert("Please enter the Quantity");
		$("#"+qty).next().addClass('match_error');
		return;
	}
	
	var quantity=$("#"+qty).val().replace(/[^0-9.]/ig,"");
		var reg = /^\d*$/ ;
		if( !reg.test(quantity)  || quantity==0 ){
			//alert('Please enter the correct number.');
		$("#"+qty).val('');
		$("#"+qty).next().addClass('match_error');
		}else{
			$("#"+qty).next().addClass('Match_correct');
			$("#"+qty).val(toThousands($("#"+qty).val().replace(/[^0-9.]/ig,"")));
		}
		

}

function verifyAmt(amt){
	$("#"+amt).next().removeClass();
	
	if($("#"+amt).val()==null  || $("#"+amt).val()==""){
		//alert("Please enter the Price");
		$("#"+amt).next().addClass('match_error');
		return;
	}
	
	
	var price=$("#"+amt).val().replace(/[^0-9.]/ig,"");
	if(price==0 ){
	//alert('Please enter the correct number.');
	$("#"+amt).val('');
	$("#"+amt).next().addClass('match_error');
	}else{
		
		 var reg = /.*\..*/;
			if(reg.test(price)){
				$("#"+amt).val(formatNumber(price,2,1));
				$("#"+amt).next().addClass('Match_correct');

			}else{
				$("#"+amt).val(toThousands(price));
				$("#"+amt).next().addClass('Match_correct');

			}
			
	/*	$("#"+amt).next().addClass('Match_correct');
		$("#"+amt).val(toThousands($("#"+amt).val().replace(/[^0-9.]/ig,"")));*/
	}
	
	
}



function toThousands(num) {
  var num = (num || 0).toString(), result = '';
  while (num.length > 3) {
      result = ',' + num.slice(-3) + result;
      num = num.slice(0, num.length - 3);
  }
  if (num) { result = num + result; }
  return result;
}
function formatNumber(num,cent,isThousand) {  
  
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

function importByPH(){
	if($(".Bulk_upload_tb table tbody tr td i").hasClass('match_error')){
	alert("The data is not validated, please check the data!");	
	}else{
		var trList = $("#tbody").children("tr");
		if(trList.size()<=0){
			alert("Please enter data!");
			return;
		}
		var list=[];
		  for (var i=0;i<trList.length;i++) {
			 var obj={};
		    var tdArr = trList.eq(i).find("td");
		    var country = tdArr.eq(0).find("input").val();//门店
		    var shop = tdArr.eq(1).find("input").val();//门店
		    var date = tdArr.eq(2).find("input").val();//日期
		    var type = tdArr.eq(3).find("input").val();//  型号
		    
		    var model = tdArr.eq(4).find("input").val();//  型号
		    var quantity = tdArr.eq(5).find("input").val().replace(/[^0-9.]/ig,"");// 数量
		    var price = tdArr.eq(6).find("input").val().replace(/[^0-9.]/ig,"");//  单价
		    var  amount=quantity*price;
		 
		      obj.country=country;
			  obj.shop=shop;
			  obj.date=date;
			  obj.type=type;
			  obj.model=model;
			  obj.qty=quantity;
			  if(country==null || country==""
				  ||   shop==null || shop==""
					  ||   date==null || date==""
						  || 	  type==null || type==""
							  || 		  model==null || model==""
								  || 		  quantity==null || quantity==""
			  
			  ){
				  alert("The data is not validated, please check the data!");	
				  return;
			  }
			  
		    if(type.toLowerCase()=="sold"){
		    	if(price==null || price==""
					  ){
					  alert("The data is not validated, please check the data!");	
		    		return;
		    	}
			  obj.price=price;
			  obj.amt=amount.toFixed(2);
		  }
		  
		  list.push(obj);

		  
		  }
		  var json = JSON.stringify(list);
		  $("#loadingImportByInput").show();
			$.ajax({
				url:baseUrl + "platform/importByInput.action",
				 data: {
					 "list":json
				 },  
				 dataType: "json", 
				success:function(data){
					 $("#loadingImportByInput").hide();
				        if(data.success=="true")
			        	{
			        		alert("Uploaded successfully");
			        		 $("#tbody").html("");
			        	}
				        else
			        	{
				        	$("#msg").html('');
				        		var _data = ReplaceAll(ReplaceAll(data.msg,"&lt;","<"),"&gt;",">")
					        	$("#msg").html(_data);
					        	$(".Error_message").show();
			        	}
				}
			});
		
	}
	

}       