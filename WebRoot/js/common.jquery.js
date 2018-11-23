/**
 * 项目通用JS方法
 */

//设置JS端的常用变量
var context = {
	TREE_ROOT:'root'
};
/**
  * 设置元素只读
  * @param obj 要设置只读的jquery对象
  */
function setReadOnly(obj)
{
	obj.parent().find(".textbox-text").attr("readonly","readonly");
	obj.parent().find(".textbox-text").addClass("myReadOnly");
}

/**
  * 设置元素不为只读
  * @param obj 要设置不为只读的jquery对象
  */
function setUnReadOnly(obj)
{
	obj.parent().find(".textbox-text").removeAttr("readonly");
	obj.parent().find(".textbox-text").removeClass("myReadOnly");
}
/**
 * 设置表单元素的宽度
 */
function setFormWidth(form)
{
	form.find("input[type='text'],textarea").each(function(index,element){
		if($(element).is(":visible"))
		{
			var span = $(element).parent();
			var td = span.parent();
			span.css("width",td.width()*0.85);
			$(element).css("width",td.width()*0.85);
		}
	});
}
/**
 * 中文编码转码
 */
function encodeZh(param)
{
	return encodeURI(encodeURI(param));
}

/**
 * 字符串替换，替换全部
 */
function ReplaceAll(str, sptr, sptr1)
{
    while (str.indexOf(sptr) >= 0)
    {
       str = str.replace(sptr, sptr1);
    }
    return str;
 }

/**
 * 加载配置参数
 * pdomain:模块区域
 * ptype：模块属性
 * pkey:　属性键
 */
function loadParameters(pdomain,ptype,pkey){
	var url=baseUrl + "main/loadParameter.action?pdomain="+pdomain+
			(ptype?"&ptype="+ptype:"")+
			(pkey?"&pkey="+pkey:""); 
	return $.ajax({
		type:"POST",
		url:url
	});
}

////加载区域数据
//function loadPartyCombo($obj){
//	$obj.combobox({
//		url:baseUrl + "platform/loadPartyListData.action",
//		valueField:'partyId',    
//	    textField:'partyName',
//		method:"post"
//	});
//}

//加载机构数据
function loadPartyComboTree($obj){
	$obj.combobox({
		url:baseUrl + "platform/selectUserParty.action",
		valueField:'partyId',    
	    textField:'partyName',
		method:"post"
	});
}

//加载所属国家
function loadPartyCountryTree($obj){
	$obj.combobox({
		url:baseUrl + "platform/selectUserPartyCountry.action",
		valueField:'partyId',    
	    textField:'partyName',
		method:"post"
	});
}

//获取国际化资源
jsLocale.get=function(key)  
{  
    if (typeof(jsLocale) != 'undefined')  
    {  
        if (typeof(jsLocale[key]) != 'undefined')  
        {  
            return jsLocale[key];  
        }  
        else  
        {  
            return key;  
        }                 
    }  
    else  
    {  
        return key;  
    };  
}
//查找this(默认是全局对象)中的属性
//此方法和直接使用jsLocale的属性方法效果一样，但使用面更广一些
function findProperty(key){
	return this[key]||"";
}
//调用方式：
//locale("user.list.gridhead.createBy")
var locale=function(key){
	return findProperty.apply(jsLocale,arguments);
}
//国际化批处理
$(function(){
	//所有文本
	$("[tcl-text]").each(function(index,item){
		$(item).text(locale($(item).attr("tcl-text")));
	});
	//{{product.list.th.productName}}
	$("#side-menu li").each(function(index,item){
		$li=$(item);
		var href=$li.find(">a").attr("href");
		if(href!="#" && document.location.href.indexOf(href)>=0){
			var $ul=$li.parent();
			if($ul.hasClass("nav-second-level")){
				$ul.addClass("in");
			}
			$li.addClass("active");
		}
	})
});
//将 Date 转化为指定格式的String  
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.format = function(fmt)   {   
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
}  
//公用的easygrid日期格式化
function dateFormatter(value,item,index){
	if(value && value.time){
		return new Date(value.time).format("yyyy-MM-dd");
	}else{
		return "";
	}	
}
//公用的easygrid状态格式化：1启用，0停用
function statusFormatter(value,item,index){
	if(value=="1"){
		return "Enabled";
	}else{
		return '<span class="required">Disabled</span>';
	}	
}
//公用的easygrid操作格式化
function opFormatter(value,item,index){
	return '<div class="tcl-btn-group">'+
		'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
		'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
			'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}
//包含 查看按钮
function opFormatter_x(value,item,index){
	return '<div class="tcl-btn-group">'+
		'<a href="javascript:void(0);" class="tcl-view" onclick="onView(\''+index+'\')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.view")+'</a>'+
		'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
		'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
			'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}
//产品维护 特殊处理
//增加查看 描述按钮
function opFormatter_product(value,item,index){
	return '<div class="tcl-btn-group">'+
	'<a href="javascript:void(0);" class="tcl-DescriptionView" onclick="onDescriptionView(\''+index+'\')">'+
	'<i class="jicon jicon-edit"></i>'+locale("toolbar.DescriptionView")+'</a>'+
	'<a href="javascript:void(0);" class="tcl-view" onclick="onView(\''+index+'\')">'+
	'<i class="jicon jicon-edit"></i>'+locale("toolbar.view")+'</a>'+
	'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
	'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
	'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
	'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}

//试卷维护 特殊处理
function opFormatter_paper(value,item,index){
	return '<div class="tcl-btn-group">'+
	'<a href="javascript:void(0);" class="tcl-DescriptionView" style="padding:4px 6px;" onclick="onDescriptionView(\''+index+'\')">'+
	'<i class="jicon jicon-edit"></i>'+locale("paper.list.topic.preview")+'</a>'+
	'<a href="javascript:void(0);" class="tcl-btn" style="padding:4px 6px;" onclick="onEditDate('+index+')">'+
	'<i class="jicon jicon-edit"></i>'+locale("toolbar.editAee")+'</a>'+
	'<a href="javascript:void(0);" class="tcl-btn" style="padding:4px 6px;" onclick="onEdit('+index+')">'+
	'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
	'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" style="padding:4px 6px;" onclick="onDelete(\''+index+'\')">'+
	'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}

//产品维护 特殊处理
//增加查看 描述按钮
function opFormatter_examination(value,item,index){
	return '<div class="tcl-btn-group">'+
	'<a href="javascript:void(0);" class="tcl-DescriptionView" onclick="onDescriptionView(\''+index+'\')">'+
	'<i class="jicon jicon-edit"></i>'+locale("toolbar.alt.Answers")+'</a>'+
	'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
	'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a></div>';+
	'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
	'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}