function bb(a){
	 $("#"+a.id).html(moment().subtract('days', 29).format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));
	 $("#"+a.id).daterangepicker({minDate: '2000-01-01',whatRole:a.id});
}

$(document).ready(function() {
	
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
	    $(window).resize(function(){
	    	setScrollBarShift("test_Left","test_Right");
	    	setScrollBarShift("test_Left1","test_Right1");
	    	setScrollBarShift("test_Left2","test_Right2");
	    	setScrollBarShift("test_Left3","test_Right3");
	    	setScrollBarShift("test_Left4","test_Right4");
	    	setScrollBarShift("test_Left5","test_Right5");
	    	setScrollBarShift("test_Left6","test_Right6");
	    	setScrollBarShift("test_Left7","test_Right7");
	    	setScrollBarShift("test_Left8","test_Right8");
	    	setScrollBarShift("test_Left9","test_Right9");
	    	setScrollBarShift("test_Left10","test_Right10");
	    	setScrollBarShift("test_Left11","test_Right11");
	    	setScrollBarShift("test_Left12","test_Right12");
	    	setScrollBarShift("test_Left13","test_Right13");
	    	setScrollBarShift("test_Left14","test_Right14");
	    	setScrollBarShift("test_Left15","test_Right15");
	    	setScrollBarShift("test_Left16","test_Right16");
	    	setScrollBarShift("test_Left17","test_Right17");
	    	setScrollBarShift("test_Left18","test_Right18");
	    	setScrollBarShift("test_Left19","test_Right19");
	    	setScrollBarShift("test_Left20","test_Right20");
	    	setScrollBarShift("test_Left21","test_Right21");
	    	setScrollBarShift("test_Left22","test_Right22");
	    	setScrollBarShift("test_Left23","test_Right23");
	    	setScrollBarShift("test_Left24","test_Right24");
	    	setScrollBarShift("test_Left25","test_Right25");
	    	setScrollBarShift("test_Left26","test_Right26");
	    	setScrollBarShift("test_Left27","test_Right27");
	    	setScrollBarShift("test_Left28","test_Right28");
	    	setScrollBarShift("test_Left29","test_Right29");
	    	setScrollBarShift("test_Left30","test_Right30");
	    	setScrollBarShift("test_Left31","test_Right31");
	    	setScrollBarShift("test_Left32","test_Right32");
	    	setScrollBarShift("test_Left33","test_Right33");
	    	setScrollBarShift("test_Left34","test_Right34");
	    	setScrollBarShift("test_Left35","test_Right35");
	    	setScrollBarShift("test_Left36","test_Right36");
	    	setScrollBarShift("test_Left37","test_Right37");
	    	setScrollBarShift("test_Left38","test_Right38");
	    	setScrollBarShift("test_Left39","test_Right39");
	    	setScrollBarShift("test_Left40","test_Right40");
	    	setScrollBarShift("test_Left41","test_Right41");
	    	setScrollBarShift("test_Left42","test_Right42");
	    	setScrollBarShift("test_Left43","test_Right43");
	    	setScrollBarShift("test_Left44","test_Right44");
	    	setScrollBarShift("test_Left45","test_Right45");
	    	setScrollBarShift("test_Left46","test_Right46");
	    	setScrollBarShift("test_Left47","test_Right47");
	    	setScrollBarShift("test_Left48","test_Right48");
	    	setScrollBarShift("test_Left49","test_Right49");
	    	setScrollBarShift("test_Left50","test_Right50");
	    });
	 
});


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
	return 0;	
};



function getLastDay(year, month){    
    var dt = new Date(year, month - 1, '01');    
    dt.setDate(1);    
    dt.setMonth(dt.getMonth() + 1);    
    cdt = new Date(dt.getTime()-1000*60*60*24);    
    return cdt.getDate();  
    //alert(cdt.getFullYear()+"年"+(Number(cdt.getMonth())+1)+"月月末日期:"+cdt.getDate()+"日");     
}  

var enMonth = ['Jan','Feb','Mar','Apr','May','June','July','Aug','Sept','Oct','Nov','Dec']; 

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


function getSelectDate(d){
	 var date=new Date(d);
	 var currentMonth=date.getMonth();
	 var nextMonth=++currentMonth;
	 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
	 var oneDay=1000*60*60*24;
	 return new Date(nextMonthFirstDay-oneDay).Format("yyyy-MM-dd");
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

function isStringNull(val){
	if(typeof(val) != "undefined" && val != '' && val != null){
		return true;
	}
	return false;
};

function accumulativeJudgment(num){//累加识别
	if(!isStringNull(num)){
		return 0;
	}
	if(typeof(num) == "string"){
		return (num * 1);
	}
	return num;
};
function getScrollbarWidth() {
    var oP = document.createElement('p'),
        styles = {
            width: '100px',
            height: '100px',
            overflowY: 'scroll'
        }, i, scrollbarWidth;
    for (i in styles) oP.style[i] = styles[i];
    document.body.appendChild(oP);
    scrollbarWidth = oP.offsetWidth - oP.clientWidth;
    oP.remove();
    return scrollbarWidth;
}
function setScrollBarShift(left,right){//滚动条移位
	$("#"+ left).scrollTop('0');
	$("#"+ left).scrollLeft('0');
	var a = $("#"+ left +" table").height();
	/*var b = $("#"+ left).height() - getScrollbarWidth();*/
	var b = $(document).height() - 320 - getScrollbarWidth();
	var e = $("#"+ left).width();
	/*alert(b)*/
    if(a > b){
     	$("#"+ right).css('width',e - getScrollbarWidth());
    }else{
    	$("#"+ right).css('width',e);
    }
};
/*function setScrollBarShift(left,right){//滚动条移位
	var Lefttablehi = $("#"+ left +" table").height();
	var testLefthi = $("#"+ left).height() - getScrollbarWidth();
    if(Lefttablehi > testLefthi){
     	$("#"+ right).css('width','calc(100vw - 844px)');
    	$("#"+ left).css('width','calc(100vw - 827px)');
    }else{
    	$("#"+ right).css('width','calc(100vw - 827px)');
    	$("#"+ left).css('width','calc(100vw - 827px)');
    }
};*/
function setWidthBarShift(left,right){//表头和表身宽度相等
	var Lefttablehi = $("#"+ left +" table").width();
	var testLefthi = $("#"+ right +" table").width();
	$("#"+ left +" table").css("width",testLefthi);
	$("#"+ right +" table").css("width",Lefttablehi);
};

function headDataToFixed(num){//判断识别是否存在小数点。(千位符)
	if(typeof(num) == "string"){
		num = num * 1;
	}
	if((""+num).indexOf('.') >= 0){
		return formatNumber(num.toFixed(2),0,1);
	}else{
		return toThousands(num);
	}
}
 

function growthRate(val){//增长率背景加载颜色
	if(('' + val).indexOf("-")>=0){
		return "<td  style='background-color: red;color:#fff;'>" + (val *1).toFixed(0) + "%</td>";
	}else{
		return "<td  style='background-color: green;color:#000;'>" + (val *1).toFixed(0) + "%</td>";
	}
};

function loadingShowHides(is){//加载图标(达成)																																																																						
	if(is){
		$("#loadingImport").hide();
	}else{
		$("#loadingImport").show();
	}
};

function loadingShowGrows(is){//加载图标(增长)	
	if(is){
		$("#loadingImportOne").hide();
	}else{
		$("#loadingImportOne").show();
	}
}

function loadingShowHide(is,loading,head,right,left){//加载图标(达成)																																																																						
	if(is){
		$("#" + loading).hide();
		
		$("#" + head).show();
		$("#" + right).show();
		$("#" + left).show();
		
	}else{
		$("#" + loading).show();
		
		$("#" + head).hide();
		$("#" + right).hide();
		$("#" + left).hide();
	}
};

function loadingShowHideSyn(is,loading,right,left){//加载图标(同比)																																																																								
	if(is){
		$("#" + loading).hide();
		
		$("#" + right).show();
		$("#" + left).show();
		
	}else{
		$("#" + loading).show();
		
		$("#" + right).hide();
		$("#" + left).hide();
	}
};

function reachTheBackgroundColor(num){//达成率背景加载颜色
	if(typeof(num) == "string"){
		if(num.indexOf("%")>=0){
			num=num.substr(0, num.length-1);
		}
		
		if(typeof(num) == "string"){
			num = num * 1;
		}
	}
	
	if(num < 60){
		return "<td  style='background-color: red;color:#fff;text-align:center;'>" + num + "%</td>";
	}else if(num >= 60 && num < 90){
		return "<td  style='background-color: yellow;color:#000;text-align:center;'>" + num + "%</td>";
	}else{
		return "<td  style='background-color: green;color:#000;text-align:center;'>" + num + "%</td>";
	}
};

function getManualSelectDate(id,type){//根据id获取插件时间
	var o = {};
	var dateArr = $('#' + id).val();
	if(type == "month"){//月度处理
		if(isStringNull(dateArr)){
			o.year = dateArr.split('-')[0];
			o.month = getENMonth(dateArr.split('-')[1]);
		}else{
			o.year = new Date().getFullYear();
			o.month = getENMonth(new Date().getMonth() + 1);
		};
	}else if(type == "year"){//年度处理
		if(isStringNull(dateArr)){
			o.year = dateArr;
		}else{
			o.year = new Date().getFullYear();
		};
	}else{//
		
	}//准确到天时
	return o;
};






function getENMonth(mon){
	  var month = ["Jan.", "Feb.", "Mar.", "Apr.", "May.", "June.", "July.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."];
	if(mon==01){
		return month[0];
	}
	if(mon==02){
		return month[1];
	}
	if(mon==03){
		return month[2];
	}
	if(mon==04){
		return month[3];
	}
	if(mon==05){
		return month[4];
	}
	if(mon==06){
		return month[5];
	}
	if(mon==07){
		return month[6];
	}
	if(mon==08){
		return month[7];
	}
	if(mon==09){
		return month[8];
	}
	if(mon==10){
		return month[9];
	}
	if(mon==11){
		return month[10];
	}
	if(mon==12){
		return month[11];
	}
}

function setHtmlNull(id1,id2,id3,id4){//多标签清空
	id1 != null?$('#' + id1).html(''):'';
	id2 != null?$('#' + id2).html(''):'';
	id3 != null?$('#' + id3).html(''):'';
	id4 != null?$('#' + id4).html(''):'';
};

function fatherHtmlShow(id1,id2,id3,id4){//父类标签显示
	id1 != null?$('#' + id1).parent().show():'';
	id2 != null?$('#' + id2).parent().show():'';
	id3 != null?$('#' + id3).parent().show():'';
	id4 != null?$('#' + id4).parent().show():'';
};





function getSubXcpGro(target){
	var tarId=target.id;
	var line=$(target).val();
	$('#subModel').html('');
	if(line==null || line==""){
		$('#sub'+tarId).html('');
		$('#sub'+tarId).html("<option value=''> </option>");
		return;
		//line="AND (cfg.PVALUE LIKE '%X%' OR cfg.PVALUE LIKE '%C%' OR cfg.PVALUE LIKE '%P%')";
	}else {
		line=line;
	}
	var o = {};
	o.line=line;
	$.ajax({
		url:baseUrl + "platform/selectXCPLine.action",
		type:"POST",
		data:o,
		success:function(data){
			var STring = $.parseJSON(data);
			if(STring!=null&&STring.length>0){
					$('#sub'+tarId).html('');
					var option="<option value=''>All</option>";
					for(var i=0;i<STring.length;i++){
						option+="<option value='"+STring[i].PVALUE+"'>"+STring[i].PVALUE+"</option>";
						
					}
						$('#sub'+tarId).html(option);
				
			}else{
						$('#sub'+tarId).html("<option value=''></option>");

			}
			
		   
		}
	});
	
}