Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

Common_util = {
	isStringNullAvaliable:function(string){//字符串是否为空
		return (typeof(string) != "undefined" && string != '' && string != null && string.trim().length != 0) ? true : false;
	},
	isPositiveInteger:function(intger){//是否为正整数
		var patrn=/^[0-9]+$/;
		return patrn.test(intger) ? true : false;
	},
	loadCountry:function(countrId){
		$.ajax({
			url:baseUrl + "examination/selectCountry.action",
			type:"POST",
			success:function(data){
				var htmlOption = "";
				var result = eval('('+data+')');
				$(result).each(function(){
					if(countrId == this.countryId){
						htmlOption += "<option selected='selected' value='" + this.countryId + "'>" + this.countryName + "</option>";
					}else{
						htmlOption += "<option value='" + this.countryId + "'>" + this.countryName + "</option>";
					}
				});
				$("#countryId").html(htmlOption);
			}
		});
	},
	deletMantissa:function(str){//删除尾数
		return Common_util.isStringNullAvaliable(str) ? str.substring(0,str.length-1):"";
	}
};

function buttonClickInvalid(name,is){//保存按钮点击失效
	if(is){
		$("[name='"+ name +"']").attr('disabled','disabled');
	}else{
		$("[name='"+ name +"']").removeAttr('disabled');
	};
};

function loadingShowHides(is){//加载图标																																																																					
	if(is){
		$("#loadingImport").hide();
	}else{
		$("#loadingImport").show();
	}
};
function selectToSelectAll(ele1,ele2){//select全部追加
	var selFrom=document.getElementById(ele1);
	var selTarget=document.getElementById(ele2);
	var optionArr=selFrom.children;
	for (var i=optionArr.length-1;i>=0;i--) {
		selTarget.appendChild(optionArr[0]);
	}
}