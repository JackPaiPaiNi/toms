$(function(){
	getSelectYear();
	getSelectMonth();
	currentTime();
	$('#month').change(function(){
		var month = find($("#month").val());//月 
		var year  =	$("#year").val();//年
		var d  = 0;//日
		var datadate = year+"-"+month+"-"+d;
		onloadBranch(datadate);
	});
	
	$('#year').change(function(){
		var month = find($("#month").val());//月 
		var year  =	$("#year").val();//年
		var d  = 0;//日
		var datadate = year+"-"+month+"-"+d;
		onloadBranch(datadate);
	});
});
function onloadBranch(datadate){
	$.ajax({
		url : baseUrl + "platform/selectCountry.action",
		type : "POST",
		data : {
			"id" : my_login_id
		},
		success : function(data) {
			var obj = eval('(' + data + ')');
			if (data != "" && data != null) {
				$.ajax({
					dataType : "JSON",
					url : baseUrl + "platform/getOBCACBranchTarget.action",
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						$("#bran").find('tr').empty();
						var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							 tdStr += "<td>"+n.partyName+"</td>";
							 tdStr += "<td>"+splitK(n.quantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.hAmount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.hTzAmount,2)+"</td>";
							 tdStr += "</tr>";
//							 $("#bran").html(tdStr);
						});
						$("#branch").append(tdStr);
//						var demo = 	$("#bran tr");
//						for(var i = 0;i<demo.length;i++){
//							$(demo[i]).hide();
//						}
						onmouse();
						autoSumBranch();
					}
				});
			}
		}
	});
}

//导入数据
function importACtarget(val){
	//showImportWin(locale("store.import"),baseUrl + "platform/importACtarget.action");
	showTargetImportWin(locale("excel.target.import"),baseUrl + "platform/importACtarget.action?targetType=" + val);
}

function loadBranch(){
	var year  =	$("#year").val();//年
	var month = find($("#month").val());//月 
	var d  = 0;//日
	var datadate = year+"-"+month+"-"+d;
	$.ajax({
		url : baseUrl + "platform/selectCountry.action",
		type : "POST",
		data : {
			"id" : my_login_id
		},
		success : function(data) {
			var obj = eval('(' + data + ')');
			if (data != "" && data != null) {
				$.ajax({
					dataType : "JSON",
					url : baseUrl + "platform/getOBCACBranchTarget.action",
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
					 var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							 tdStr += "<td>"+n.partyName+"</td>";
							 tdStr += "<td>"+splitK(n.quantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.hAmount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.hTzAmount,2)+"</td>";
							 tdStr += "</tr>";
//							var row = $("#branchlist").clone();
//							row.find("td:eq(0)").html(n.partyName);
//							row.find("td:eq(1)").html(splitK(n.quantity));
//							row.find("td:eq(2)").html(splitK(n.amount));
//							row.find("td:eq(3)").html(splitK(n.tzQuantity));
//							row.find("td:eq(4)").html(splitK(n.tzAmount));
//							row.appendTo("#bran");
						});
						 $('#branch').append(tdStr);
						 onmouse();
						autoSumBranch();
					}
				});
			}
		}
	});
}


//分公司目标统计
function autoSumBranch(){
    var total_objs = new Object();
    $('.branch_sum').each(function(){
        var index = $('tr th').index(this)+1; //index(this)是当前文本下标，+1是返回来
        var total = 0;
        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
            var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
            total += Number(text);
        });
        total_objs[index] = total.toFixed(2);
    });
    var td_num = $('.branch_sum').parent('tr').find('th').length+1;
    
    var tds = '<tr style="color:red;">';
    for(var i=0;i<td_num;i++){
        var total = 'Total';
        if(total_objs[i]!=undefined){
            total = splitK(Number(total_objs[i]));
        }
        tds += '<td style="color: #c7254e;">'+ total + '</td>';
    }
    tds += '</tr>';
    $('.branch_sum').closest('table').append(tds);
    

}


//千字符分隔
function splitK(num) {
    var decimal = String(num).split('.')[1] || '';//小数部分
    var tempArr = [];
    var revNumArr = String(num).split('.')[0].split("").reverse();//倒序
    for (i in revNumArr){
      tempArr.push(revNumArr[i]);
      if((i+1)%3 == 0 && i != revNumArr.length-1){
        tempArr.push(',');
      }
    }
    var zs = tempArr.reverse().join('');//整数部分
    return decimal?zs+'.'+decimal:zs;
  }

//显示最近10年
function getSelectYear(){
	var date = new Date();
	var y = date.getFullYear();
	for (i = 0; i < 10; i++) {

		var oP = document.createElement("option");

		var oText = document.createTextNode(y);

		oP.appendChild(oText);

		oP.setAttribute("value", y);

		document.getElementById('year').appendChild(oP);

		y = y - 1;

		};
}

function getSelectMonth(){
	var date = new Date();
	var m = date.getMonth() + 1;
	var j = 1;
	for (i = 1; i < 13; i++) {

		var month = document.createElement("option");

		var monthText = document.createTextNode(j);

		month.appendChild(monthText);

		month.setAttribute("value", j);

		if (j == m) {

		month.setAttribute("selected", "selected");

		}

		;

		document.getElementById('month').appendChild(month);

		j = j + 1;

		};
}

function find(month){
	 var month = $('#month').val();  
     if (month.length == 1) {  
         month = "0" + month;  
     }  
    return month;
}

function currentTime(){
	var d = new Date(),str = '';
	 var year= d.getFullYear();
	 var month=d.getMonth()+1;
	 var day=0;
	 if(month<10)
		 {
		 month="0"+month;
		 }
	 str=year+"-"+month+"-"+day;
	loadBranch();
	}

//获取鼠标变色
function onmouse(){
	$(".main tbody tr").hover(
			function(){
				$(this).addClass("highlight");
			},
			function(){
				$(this).removeClass("highlight");
			}
		);
}

//格式化千位分隔，保留n位小数
function formatRevenue(s, n) { 
	n = n > 0 && n <= 20 ? n : 2; 
	s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
	var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1]; 
	t = ""; 
	for (i = 0; i < l.length; i++) { 
	t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
	} 
	return t.split("").reverse().join("") + "." + r; 
	} 