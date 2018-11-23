/**
 * 销售数据表管理
 */
$(function(){


	$("#coverupload").click(function(){
		var options={
			type:"POST",
			url:baseUrl+"updownload/fileUploadAction.action?flag=coeff",
			success:function(data){
				$("#file").val(data.path);
				alert("upload successful!");
			}
		};
		$("#coverform").ajaxSubmit(options);
	});
	$("#saleListTable").datagrid({
		title:locale("coefficient.me"),
		url:baseUrl + 'platform/selectCoefficient.action?isUsing=YES',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	  	     {field:'file',title:locale("coefficient.file"),width:100,formatter:showFile},

	        {field:'country',title:locale("sale.list.th.country"),width:100,hidden:true},
	        {field:'countryName',title:locale("sale.list.th.country"),width:100},
	        {field:'all',title:locale("coefficient.all"),width:100},
	        {field:'core',title:locale("coefficient.core"),width:100},
	        {field:'user',title:locale("sale.list.th.salerName"),width:80},
	        {field:'ctime',title:locale("sale.list.th.ctime"),width:90},
	        {field:'isUsing',title:locale("coefficient.isUsing"),width:50,formatter: function (value, row, index) {  
               
                var res = '<font style="color:RED;">'+row.isUsing+'</font>';  

                return res;  
            }  
        },  
        {field:'ww',title:locale("coefficient.his"),width:100,formatter: function (value, row, index) {  
            
            var res = '<a href="javascript:void(0)"  onclick="showList('+row.country+')">Historical</a>';  

            return res;  
        }  
        },
	        /*,
	        {field:'op',title:locale("toolbar.edit"),width:80,formatter:tbrForDate}*/
	    ]],
	    toolbar:'#saletb',
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	
	initWindow();
});
function enableBt(){
	$("#searchBt").linkbutton("enable");
}

function initWindow(){
	initCountry();
}


function initCountry(){	
	$.ajax({
		url:baseUrl + "platform/onloadCountry.action",
		type:"POST",
		data:{"id":my_login_id},
	}).success(function(data){
		if(data.rows.length==1){
			$("#tcl_country").combobox("setValue", data.rows[0].countryId).combobox("setText", data.rows[0].countryName);

		}else{
			
			data.rows.unshift({"countryName":"ALL","countryId":""});
			$("#tcl_country").combobox({			
				data:data.rows,
				textField:"countryName",
				valueField:"countryId",
			});
		
		}
		
		
	});
	
	$.ajax({
		url:baseUrl + "platform/onloadCountry.action",
		type:"POST",
		data:{"id":my_login_id},
	}).success(function(data){
		if(data.rows.length==1){
			$("#countryId").combobox("setValue", data.rows[0].countryId).combobox("setText", data.rows[0].countryName);

		}else{
			$("#countryId").combobox({			
				data:data.rows,
				textField:"countryName",
				valueField:"countryId",
			});

		
		}
		
		
	});
	
}


function showAddWin(){
	$('#addSaleForm #productId').textbox("enable");
	$('#addSaleForm input[name=editId]').val("");
	
	$('#addSaleForm').form("reset");
	$('#addSaleWin').window({title:locale("coefficient.window.add")}).window('center').window('open');
}
function doSearch(){
	$("#searchBt").linkbutton("disable");
	

	var country = $("#tcl_country").combobox("getValue");

	
	$("#saleListTable").datagrid({
		queryParams:{
			countryId:country
		}
	});
}


//关闭新增框
function clearForm(){
	$('#addSaleWin').window('close');
}

function validate(){
	return $("#addSaleForm").form("validate");
}

//显示编辑窗口
function onEdit(index){
	//获取并填充数据
	var row=$("#saleListTable").datagrid('getRows')[index];
	$('#addSaleForm input[name=editId]').val(row.saleId);
	$('#addSaleForm').form('load',row);
	editLoadShopAndUser($("#shopId"),row);
	//显示窗口
	$('#addSaleWin').window({title:locale("sale.window.edit")});
	$('#addSaleWin').window('center');
	$('#addSaleWin').window('open');
}



//新增保存
function submitForm(){
	
	var files=$("#file").val();
	
	if(files.indexOf("/var/www/topsale")>-1){
		files=files.replace("/var/www/topsale","");
	}
	//alert(files.indexOf("D:\\test")>-1);
	/*if(files.indexOf("D:\\test")>-1){
		files=files.replace("D:\\test","http:\\obctop.tcl.com.cn");
	}
	alert(files);*/
	
	if(files==null || files==""){
		alert("Please upload files!");
		return;
	}
	
	
	var  file=encodeURI(files);

	file=encodeURI(file);

	if(validate()){
		//添加or编辑
		var id=$('#addSaleForm input[name=editId]').val();
		if($.trim(id)!=""){
			//编辑
			$('#addSaleForm').ajaxSubmit({
				url:baseUrl + "platform/editSale.action?id="+id,
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						$("#saleListTable").datagrid('reload');
						$('#addSaleWin').window('close');
						showMsg(locale("alert.success"));
					}else {
						showMsg(result.msg);
					}
				}
			});
		}else{
			$('#addSaleForm').ajaxSubmit({
				url:baseUrl + "platform/addCoefficient.action?file="+file,
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#saleListTable").datagrid('reload');
						$('#addSaleWin').window('close');
					}else{
						showMsg(result.msg);
					}
				}
			});
		}
	}else{
		showMsg(locale("alert.validate.fail"));
	}
}

//删除
function onDelete(index){
	var row = $("#saleListTable").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "platform/deleteSale.action",
				type:"POST",
				data:{"id":row.saleId},
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#saleListTable").datagrid('reload');
					}else{
						showMsg(result.msg);
					}
				}
			});
	    }
	});
}

function clearForm(){
	$('#addSaleWin').window('close');
}

function exportExcel(){
	var url = baseUrl + "platform/exportSaleExcel.action";
	location.href = url;
}

function editLoadShopAndUser($shopTree,row){
	if($shopTree){
		var shopId= $shopTree.combobox("getValue");
//		$userTree.combobox('reload',baseUrl + "platform/getUserList.action?shopId="+shopId+"&random="+Math.random());
//		$userTree.combobox("setValue",row.userId);
	}
}

//导入数据
function importSales(){
	showImportWin(locale("sale.import"),baseUrl + "platform/importSales.action");
}













//公用的easygrid操作格式化
function tbrForDate(value,item,index){
	
	

	if(country==999){  
		return '<div class="tcl-btn-group">'+
		'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
			'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
		'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
			'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
	}{
		return null;
	}
	
	
}
function showFile(value,item,index){
	var upFileName = item.file;
	var index1=upFileName.lastIndexOf(".");
	var index2=upFileName.length;
	var suffix=upFileName.substring(index1+1,index2);//后缀名
	var AllImgExt=".jpg.jpeg.gif.bmp.png";//全部图片格式类型 
	
	
	if(AllImgExt.indexOf(suffix)!=-1){
		if(item.file==null  ||  item.file==""){
			return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="'+item.file+'"></a>';

		}else  if(item.file!=null  &&  item.file!=""){
			return '<a onmouseenter="onPicHover(event)" onmouseleave="picBlur(event)" target="_blank" href="'+item.file+'"><img style="width:25px; height:25px;" class="tcl-gridcell-thumbnail" src="'+item.file+'"/></a>';

		}
		
	}else{
		if(item.file==null  ||  item.file==""){
			return ;
			
			}else  if(item.file!=null  &&  item.file!=""){
			return '<a href="javascript:downloadFile(\''+item.file+'\')">Download</a>';	
			}
	}
	
	
}



function downloadFile(file){
	window.open(file);
}
function onPicHover(event){
	event=event||window.event;
	var $obj=$(event.currentTarget||event.target);//.attr('href')
	//显示大图片
	$(".picview img").attr("src",$obj.attr("href"));
	var top=$obj.find("img").offset().top-$obj.height();
	$(".picview").css({left:100,top:$obj.offset().top-$obj.height()-50}).show();
	if($(".picview").offset().top+$(".picview").height()>$(document).height()){
		//边界检测
		var updatetop=$(".picview").offset().top-($(".picview").offset().top+$(".picview").height()-$(document).height());
		$(".picview").css({
			top:updatetop>0?updatetop:0
		});
	}
}
function picBlur(event){
	$(".picview").hide();
}



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


function showList(country){
	$("#samplesSumListTable").datagrid({
/*		title:locale("coefficient.me"),
*/		url:baseUrl + 'platform/selectCoefficient.action?countryId='+country,
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	  	     {field:'file',title:locale("coefficient.file"),width:150,formatter:showFile},
	        {field:'countryName',title:locale("sale.list.th.country"),width:100},
	        {field:'all',title:locale("coefficient.all"),width:100},
	        {field:'core',title:locale("coefficient.core"),width:100},
	        {field:'user',title:locale("sale.list.th.salerName"),width:100},
	        {field:'ctime',title:locale("sale.list.th.ctime"),width:100},
	        {field:'isUsing',title:locale("coefficient.isUsing"),width:50,formatter: function (value, row, index) {  
                var res = '<font style="color:RED;">'+row.isUsing+'</font>';  

                return res;  
            }  
        },  
	        /*,
	        {field:'op',title:locale("toolbar.edit"),width:80,formatter:tbrForDate}*/
	    ]],
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
	});
	$('#sumDiv').window({
		left: ($(window).width() - 450) * 0.5,
		title:locale("coefficient.his")}).window('center').window('open');
}