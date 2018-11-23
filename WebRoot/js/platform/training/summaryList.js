$(document).ready(function(){
	
	//点击全选到右边
	$('#toRightAll').click(function(){
		selectToSelectAll('selectLeft','selectRight');
	});	
	//点击全选到左边
	$('#toLeftAll').click(function(){
//		selectToSelectAll('selectRight','selectLeft');
		var userArray=[];
		$("#selectRight option").each(function(){
//			$("#selectLeft").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
			//$("#selectLeft option:first").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在select前面加内容
			//$("#selectLeft option[value=3]").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在selectt指定某一行加内容
			userArray.push({"userLoginId":$(this).val(),"userName":$(this).html()});
		});
		
		var summaryId=$('#addCourseForm input[name=editId]').val();
		var o = {};
		o.userLoginId=JSON.stringify(userArray);
		o.summaryId=summaryId;
		if($.trim(summaryId)!=''){
			$.ajax({
				url:baseUrl+"message/userIsReadSummary.action",
				type:"POST",
				data:o,
				success:function(result){
					var users="";
					if(result.rows.length>0){
						$(result.rows).each(function(i,n){
							users += n.userName +" , ";
						});
						showMsg(users + locale("train.course.remove.user"));
					}else{
						selectToSelectAll('selectRight','selectLeft');
					}
				}
			});
		}else{
			selectToSelectAll('selectRight','selectLeft');
		}
	});
	
	$("#toRight").click(function(){
		//校验至少要选择一个
		var selectLeft = $('#selectLeft option:selected');
		var userLoginId="";
		for(var i=0;i<selectLeft.length;i++)
		{
			 userLoginId = selectLeft[i].innerText;
		}
			if(userLoginId==""){
				showMsg(locale("msg.form.error.not"));
				$("#selectLeft").focus();
			}	
		
		$("#selectLeft option:selected").each(function(){
			$("#selectRight").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");
			$(this).remove();
		});
	});
	
	$("#toLeft").click(function(){
		
		//校验至少要选择一个
		var selectRight = $('#selectRight option:selected');
		var userLoginId="";
		for(var i=0;i<selectRight.length;i++)
		{
			 userLoginId = selectRight[i].innerText;
		}
			if(userLoginId==""){
				showMsg(locale("msg.form.error.not"));
				$("#selectRight").focus();
			}	
		
		var userArray=[];
		$("#selectRight option:selected").each(function(){
//			$("#selectLeft").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
			//$("#selectLeft option:first").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在select前面加内容
			//$("#selectLeft option[value=3]").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在selectt指定某一行加内容
			userArray.push({"userLoginId":$(this).val(),"userName":$(this).html()});
		});
		
		var summaryId=$('#addCourseForm input[name=editId]').val();
		var o = {};
		o.userLoginId=JSON.stringify(userArray);
		o.summaryId=summaryId;
		if($.trim(summaryId)!=''){
			$.ajax({
				url:baseUrl+"message/userIsReadSummary.action",
				type:"POST",
				data:o,
				success:function(result){
					var users="";
					if(result.rows.length>0){
						$(result.rows).each(function(i,n){
							users += n.userName +" , ";
						});
						showMsg(users + locale("train.course.remove.user"));
					}else{
						removeUser();
					}
				}
			});
		}else{
			removeUser();
		}
	});
	
//	$("#countryId").change(function(){
//		if($(this).val()!=''){
//			$("#selectLeft").empty();
//			getUserByRoleName($("#roleTypeName").val(),$("#countryId").val());
//		}
//	});
//	
//	$("#roleTypeName").change(function(){
//		if($(this).val()!=''){
//			$("#selectLeft").empty();
//			getUserByRoleName($(this).val(),$("#countryId").val());
//		}else if($(this).val()==''){
//			getUserByRoleName('',$("#countryId").val());
//		}
//	});
	
	$("#regionId").change(function(){
		if($(this).val()=='-1' && $("#roleTypeName").val()=='-1'){
			$("#selectLeft").empty();
			selectAllUsers();
		}else if($(this).val()!='-1' && $("#countryId").val()=='-1'){
			$("#selectLeft").empty();
			getUserByRegion($('#region').val());
		}else if($(this).val()=='-1' && $("#roleTypeName").val()!='-1'){
			$("#selectLeft").empty();
			getUserByCondition($("#roleTypeName").val());
		}else{
			$("#selectLeft").empty();
		}
	});
	
	
	$("#countryId").change(function(){
		if($(this).val()!='-1' && $("#roleTypeName").val()!=''){
			if($("#roleTypeName").val()=='-1'){
				$("#selectLeft").empty();
				getUserByRoleName('',$("#countryId").val());
			}else{
				$("#selectLeft").empty();
				getUserByRoleName($("#roleTypeName").val(),$("#countryId").val());
			}
		}else if($(this).val() =='-1' && $('#regionId').val()!='-1'){
			$("#selectLeft").empty();
			getUserByRegion($('#regionId').val());
		}else if($("#roleTypeName").val()!=''){
			$("#selectLeft").empty();
			getUserByRoleName($("#roleTypeName").val(),$("#countryId").val());
		}
	});
	
	$("#roleTypeName").change(function(){
		if($(this).val()!='-1' && $("#countryId").val()=='-1'){
			$("#selectLeft").empty();
			getUserByCondition($(this).val());
//			getUserByConditions($('#correspondingRegionId').val(),$(this).val());
		}else if($(this).val()==''){
			$("#selectLeft").empty();
//			getUserByRoleName('',$("#country").val());
		}else if($(this).val()=='-1' && $("#countryId").val()=='' && $('#regionId').val()=='-1' ){
			$("#selectLeft").empty();
			selectAllUsers();
		}else if($(this).val()=='-1' && $("#countryId").val()==null && $('#regionId').val()=='-1' ){
			$("#selectLeft").empty();
			selectAllUsers();
		}else if($(this).val()!='' && $("#countryId").val()==null && $('#regionId').val()=='-1'  ){
			$("#selectLeft").empty();
			getUserByCondition($(this).val());
		}else if($(this).val()=='-1' && $("#countryId").val()!='-1'){
			$("#selectLeft").empty();
			getUserByRoleName('',$("#countryId").val());
		}else{
			$("#selectLeft").empty();
			getUserByRoleName($(this).val(),$("#countryId").val());
		}
	});
	
	$(".side li a").hover(
		function(){
			$(this).addClass("jsih");
		},
		function(){
			$(this).removeClass("jsih");
		}
	);
	$('.side li a').click(function(){		
		$('.side li .kwdn').each(function(){
			$(this).removeClass('kwdn');
		});
		$(this).addClass('kwdn');
	});
	$('#PrototypeManagement>ul').hide();
	$('#PrototypeManagement>a').click(
		function(){
			$('#PrototypeManagement>ul').toggle();
		}
	);
	$('#training>ul').hide();
	$('#training>a').click(
		function(){
			$('#training>ul').toggle();
		}
	);
//	$('#Query_Condition').click(function(){
//		//$('#Query_Result').show();
//		//$('#caption_i').text('4');
//		loadCourseDataList();
//	});
	$(".remove_tr").click(function(){
        $(this).parents("tr").remove(); 
   });
	loadMessage();
	
/***************************1.课程类别级联菜单Start*****************************************/	

	
/***************************1.课程类别级联菜单End*****************************************/	
	
/***************************2.加载课程列表Start*************************************************/	

	$("#courselist").datagrid({
		title:locale("summary.list.title"),
		url:baseUrl+"training/loadSummaryListData.action",
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
		columns:[[
		          {field: 'summaryId',  title:"summaryId",width:50,hidden:true},
		          {field: 'partyName',  title: jsLocale.get('district.countryName'),width:50},
		          {field: 'typeId',  title: jsLocale.get('summary.typeId'),width:50,hidden:true}, 
		            {field: 'summaryTitle',  title: jsLocale.get('summary.title'),width:80},  
		            {field: 'summary',  title: jsLocale.get('summary.summary'),width:50},
		            {field: 'summaryType',  title: jsLocale.get('summary.messageType'),width:50,formatter:messageTypeformatter},
		            {field: 'coverUrl',  title: jsLocale.get('summary.cover'),width:50,formatter:imgFormatter},
		            {field: 'state',  title: jsLocale.get('summary.state'),width:50,formatter:pushformatter},
		            {field: 'createDate',  title: jsLocale.get('summary.createDate'),width:50,formatter:todate},
		            {field:'op',title:locale("toolbar.edit"),width:60,formatter:opFormatter},
		          ]],
		 toolbar:'#coursetb',
		 onHeaderContextMenu:onEasyGridHeadMenu,
		 onLoadSuccess:enableBt
	});
		
initWindow();

});


//加载消息栏目
function loadMessage(){
	initLevel1Type();
	$.ajax({
		url:baseUrl+"training/getMessageType.action",
		type:"POST",
		success:function(result){
			var obj = eval('('+result+')');
			$.each(obj.rows,function(i,n){
				$("#column").append("<option value='"+n.typeId+"'>"+n.typeName+"</option>");
			});
		}
	});	
}

//初始化消息栏目
function initLevel1Type()
{
	$("#column").empty();
	$("#column").append("<option value=''>Please Select The Message Column</option>");
}

//初始化
function initWindow(){
	initReion();
	initCountry();
	initMsgColumn();

	$(".tcladdwin input[name='messageType']").combobox({
		data:[{text:locale("summary.image"),value:1},{text:locale("summary.article"),value:2},
		      {text:locale("summary.video"),value:3}]
	});
	
	$(".tcladdwin input[name='state']").combobox({
		data:[{text:locale("window.status.push"),value:1},{text:locale("window.status.normal"),value:0}]
	});
}


function enableBt(){
	$("#searchBt").linkbutton("enable");
//	initDataGridCells();
	if(!isAvailable()){
		//根据tcl-btn样式
		$(".tcl-btn").each(function(){
			$(this).remove();
		});
		$(".easyui-linkbutton").each(function(){
			var iconcls = $(this).attr("iconcls");
			if("icon-search"!=iconcls && "icon-redo"!=iconcls)
			{
				$(this).remove();
			}
		});
	}
}

//easyui格式化messageType
function messageTypeformatter(value,item,index){
	if(value=="1"){
		return "Image";
	}else if(value=="2"){
		return "Article";
	}else if(value=="3"){
		return 'Video';
	}	
}

function pushformatter(value,item,index){
	if(item.state=="0"){
		return "<span style='color:blue;'>Normal</span>";
	}else if(item.state=="1"){
		return "<span style='color:red;'>Pushed</span>";
	}
}

//日期转换函数
var todate = function(value,row,index) 
{
	
	if(value == '')
	{
		return '';
	}
	var d = new Date();
	d.setTime(value.time); 
	return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
};

//关闭新增框
function clearForm(){
	$('#addCourseWin').window('close');
}

function validate(){
//	if($("#countryId").val()==""){
//		alert("This field is required.");
//		$("#countryId").focus();
//		return false;
//	}
	
	if($.trim($("#summaryTitle").val())==""){
		showMsg(locale("summary.titles"));
		$("#summaryTitle").focus();
		return false;
		
	}
	
	if($.trim($("#summary").val())==""){
		showMsg(locale("summary.summarys"));
		$("#summary").focus();
		return false;
		
	}
	
	if($.trim(ue.getContent())=='')
	{
		showMsg(locale('summary.contents'));
		$("#editor").focus();
		return false;
	}
	
	return $("#addCourseForm").form("validate");
	return true;
}

//清空左右角色框
function clearLeftAndRight(){
	$("#selectLeft").html("");
	$("#selectRight").html("");
}

//显示编辑框
function onEdit(index){
	$("#imgUrl").hide();
	clearLeftAndRight();
	$("#submitBtn").removeAttr("disabled");
	//清空form表单数据
	$('#addCourseForm').form("reset");
	//获取并填充数据
	var row=$("#courselist").datagrid('getRows')[index];
	$('#addCourseForm').form('load',row);

	$('#addCourseForm input[name=editId]').val(row.summaryId);
	$('#addBarcodeForm #barcode').textbox("disable");
	loadPartyList(row.regionId,row.countryId);
	switchOnOff($("#addCourseForm"),$("#submitBtn"),row.createBy);
	
	$('#summaryTitle').val(row.summaryTitle);
	$('#summary').val(row.summary);
	
	//显示窗口
	$('#addCourseWin').window({title:locale("barcode.window.edit")});
	$('#addCourseWin').window('center');
	$('#addCourseWin').window('open');
	
	UE.getEditor('editor').setContent(uncompileStr(row.summaryContent));  //给UEditor编辑器赋值
	//填充级联菜单显示的数据
	editRegionCountry(row.regionId,row.countryId);
	//获取发送消息的用户
	$.ajax({
		url:baseUrl+"message/selectUserBycondition.action?msgRoleId="+row.msgRoleId,
		type:"POST",
		success:function(result){
			var obj = eval('('+result+')');
			var optStr="";
			$.each(obj.rows,function(i,n){
				optStr +="<option  value='"+n.userLoginId+"'>"+n.userName+"</option>";
			});
			$("#selectRight").html(optStr);
		}
	});
}


function submitForm(){
	if($("#file").val()==''){
		if(validate()){
			$("#imgUrl").show();
			$("#submitBtn").attr("disabled","true");
			//添加or编辑
			var summaryId=$('#addCourseForm input[name=editId]').val();
			if($.trim(summaryId)!=""){
				//编辑
				var param = "";
				var userParam="";
				var o = {};
				//获取右边框选中的所有用户
				var selectRight = $('#selectRight option');
				var allUserStr = "";
				var userArray = [];
				for(var i=0;i<selectRight.length;i++)
				{
					var msgRoleId = selectRight.eq(i).attr("value");
//					allUserStr += msgRoleId+";";
					userArray[i]={"userId":msgRoleId};
				}
//				param = allUserStr;
				
				if(userArray.length!=0){
					o.param = JSON.stringify(userArray);
					o.courseContents=compileStr(ue.getContent());
					o.regionId=$("#regionId").val();
					o.countryId=$("#countryId").val();
					o.typeId=$("#typeId").val();
					o.summaryType=$("#summaryType").val();
					o.coverUrl=$("#hidcover").val();
					o.state=$("#state").val();
					o.summaryTitle=encodeURIComponent($("#summaryTitle").val());
					o.summary=encodeURIComponent($("#summary").val());
					o.editId=summaryId;
					
					$.ajax({
						url:baseUrl + "training/updateSummary.action",
						data:o,
						type:"POST",
						success:function(data){
							$("#imgUrl").hide();
							var result=eval('('+data+')');	
							if(result.success){
								$("#courselist").datagrid('reload');
								$('#addCourseWin').window('close');
								showMsg(locale("alert.success"));
							}else{
								showMsg(result.msg);
							}
						}
					});
//					$('#addCourseForm').ajaxSubmit({
//						url:baseUrl + "training/updateSummary.action",
//						data:o,
//						success:function(data){
//							var result=eval('('+data+')');				
//							if(result.success){
//								$("#courselist").datagrid('reload');
//								$('#addCourseWin').window('close');
//								showMsg(locale("alert.success"));
//							}else{
//								showMsg(result.msg);
//							}
//						}
//					});
				}else{
					$("#imgUrl").hide();
					$("#submitBtn").removeAttr("disabled");
					alert("At Least One User !");
					$("#selectRight").focus();
				}			
			}
		}
	}else{
		if(!check()){
			return;
		}
		if(validate()){
			$("#imgUrl").show();
			$("#submitBtn").attr("disabled","true");
			//添加or编辑
			var summaryId=$('#addCourseForm input[name=editId]').val();
			if($.trim(summaryId)!=""){
				//编辑
				var param = "";
				var userParam="";
				var o = {};
				//获取右边框选中的所有用户
				var selectRight = $('#selectRight option');
				var allUserStr = "";
				var userArray = [];
				for(var i=0;i<selectRight.length;i++)
				{
					var msgRoleId = selectRight.eq(i).attr("value");
//					allUserStr += msgRoleId+";";
					userArray[i]={"userId":msgRoleId};
				}
//				param = allUserStr;
				
				if(userArray.length!=0){
					o.param = JSON.stringify(userArray);
					o.courseContents=compileStr(ue.getContent());
					o.regionId=$("#regionId").val();
					o.countryId=$("#countryId").val();
					o.typeId=$("#typeId").val();
					o.summaryType=$("#summaryType").val();
					o.coverUrl=$("#hidcover").val();
					o.state=$("#state").val();
					o.summaryTitle=encodeURIComponent($("#summaryTitle").val());
					o.summary=encodeURIComponent($("#summary").val());
					o.editId=summaryId;
					
					$.ajax({
						url:baseUrl + "training/updateSummary.action",
						data:o,
						type:"POST",
						success:function(data){
							$("#imgUrl").hide();
							var result=eval('('+data+')');	
							if(result.success){
								$("#courselist").datagrid('reload');
								$('#addCourseWin').window('close');
								showMsg(locale("alert.success"));
							}else{
								showMsg(result.msg);
							}
						}
					});
					
//					$('#addCourseForm').ajaxSubmit({
//						url:baseUrl + "training/updateSummary.action",
//						data:o,
//						success:function(data){
//							var result=eval('('+data+')');				
//							if(result.success){
//								$("#courselist").datagrid('reload');
//								$('#addCourseWin').window('close');
//								showMsg(locale("alert.success"));
//							}else{
//								showMsg(result.msg);
//							}
//						}
//					});
				}else{
					$("#imgUrl").hide();
					$("#submitBtn").removeAttr("disabled");
					alert("At Least One User !");
					$("#selectRight").focus();
				}			
			}
		}
	}
}


//非总部999不可修改region和country
function loadPartyList(region,country){
	var userCountryId=countryId;//用户所在的国家
	if(userCountryId!="999")
	{	
		$("#regionId").combobox({disabled: true});
		$.ajax({
			url:baseUrl+"training/selectRegion.action?partyId="+regionId,
			type:"POST",
		}).success(function(data){
		$("#regionId").combobox("setValue",data.rows[0].partyName);
		
		});
		if(countryId!="999")
		{	
			$("#countryId").combobox({disabled: true});
			$.ajax({
				url:baseUrl+"training/selectRegion.action?partyId="+country,
				type:"POST",
			}).success(function(data){
			$("#countryId").combobox("setValue",data.rows[0].partyName);
		});
		}
		return;
	}else{
		if(roleName.indexOf("EMSC")==0){
			$("#regionId").empty();
			$("#regionId").append("<option value='1'>"+roleName.substring(0,4)+"</option>");
			$("#regionId").val("1");
			$("#regionId").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("BDSC")==0){
			$("#regionId").empty();
			$("#regionId").append("<option value='2'>"+roleName.substring(0,4)+"</option>");
			$("#regionId").val("2");
			$("#regionId").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("NASC")==0){
			$("#regionId").empty();
			$("#regionId").append("<option value='3'>"+roleName.substring(0,4)+"</option>");
			$("#regionId").val("3");
			$("#regionId").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("EUSC")==0){
			$("#regionId").empty();
			$("#regionId").append("<option value='14'>"+roleName.substring(0,4)+"</option>");
			$("#regionId").val("14");
			$("#regionId").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("SRSC")==0){
			$("#regionId").empty();
			$("#regionId").append("<option value='15'>"+roleName.substring(0,4)+"</option>");
			$("#regionId").val("15");
			$("#regionId").attr("disabled",true);
			
			loadCountryListData();
		}
	}
}


function loadCountryListData()
{
	var regionId=$("#regionId").val();
	if(regionId=='0'||regionId=='-1')
	{
		return;
	}
	$("#countryId").removeAttr("disabled");
	var param={};
	param.partyId=regionId;
	$.ajax({
		url:baseUrl+"party/loadCountryData.action",
		type:"POST",
		data:param,
		success:function(data){
//			$("#country").append("<option value='-1'>All</option>");
			$.each(data,function(n,value){
				$("#countryId").append("<option value='"+value.partyId+"'>"+value.partyName+"</option>");
			});
		},
		dataType:"json"
	});
}


function doSearch(){
	$("#searchBt").linkbutton("disable");
	var countryId=$("#countryName").val();
	var type=$("#column").val();
	var searchKey=$("#searchCourse").val();
	var Messagetype=$("#messagetype").val();
	$("#courselist").datagrid({
		queryParams:{
			partyId:countryId,
			typeId:type,
			MessagetypeId:Messagetype,
			searchKey:searchKey
		}
	});
}




//图片添加路径  
function imgFormatter(value,row,index){
     if('' != value && null != value){  
    var strs = new Array(); //定义一数组   
    if(value.substr(value.length-1,1)==","){  
        value=value.substr(0,value.length-1);  
    }  
        strs = value.split(","); //字符分割   
  var rvalue ="";            
    for (i=0;i<strs.length ;i++ ){   
        rvalue += "<img onclick=download(\""+strs[i]+"\") style='width:120px; height:60px;margin-left:3px;' src=" + strs[i] + " title='Click to View The Picture'/>";  
        }   
    return  rvalue;        
     }  
    }  


//这里需要自己定义一个div   来创建一个easyui的弹窗展示图片
function download(img){ 
    var simg =  img;  
    $('#dlg').dialog({  
        title: '预览',  
        width: 1000,  
        height:600,  
        resizable:true,  
        closed: false,  
        cache: false,  
        modal: true  
    });  
    $("#simg").attr("src",simg);  
      
}     

//上传图片
function upload(){
	if($("#file").val()==""){
		alert("Please Select the Cover Image !");
		return false;
	}
	$("#addCourseForm").ajaxSubmit({
		type:"POST",
		url:baseUrl+"updownload/fileUploadAction.action?flag=cover",
		success:function(data){
			if(!check()){
				return;
			}else{
//				$("#hidcover").val(data.path);
//				alert("upload successful");
				if(data.path!=""){
					$("#hidcover").val(data.path);
					$("#file").attr("disabled","true");
					$("#coverupload").attr("disabled","true");
					alert("upload successful!");
				}
			}
		}
	});
	return true;
}

//上传图片大小
function check(){
	var img=document.getElementById("file").value.toLowerCase().split('.');		//用"."分隔上传的图片字符串
	if(img[img.length-1]=='gif'||img[img.length-1]=='jpg'
		||img[img.length-1]=='bmp'||img[img.length-1]=='png'||img[img.length-1]=='jpeg'){ //判断上传图片格式	
	}else{
		alert("please select the image of *.jpg, *.gif, *.bmp, *.png, *.jpeg");
		return false;
	}
	var imgSize=document.getElementById("file").files[0].size;
	if(imgSize>=1024*1024*2){
		 alert("The picture size is within 2M, for:"+imgSize/(1024*1024)+"M");
		 return false;
	}
		return true;
}


//初始化国家列表根据用户所在的国家
function initCountry(){
	var url="";
	if(countryId=='999'){
		url=baseUrl+"training/getPartyList.action";
	}else{
		url=baseUrl+"training/getBranchPartyList.action";
	}
	$.ajax({
		url:url,
		type:"POST",
		data:{"userLoginId":my_login_id},
		success:function(result){
			var obj = eval('('+result+')');
			var optStr="";
			$.each(obj.rows,function(i,n){
//				optStr+="<option  value='"+n.partyId+"'>"+n.partyName+"</option>";
				if(obj.rows.length==1){
				optStr+="<option  value='"+n.partyId+"'>"+n.partyName+"</option>";
				}else{
				optStr+="<option  value='"+n.partyId+"'>"+n.partyName+"</option>";	
				}
			});
			$("#countryName").html(optStr);
		}
	});
}

//根据角色和国家获取用户名
function getUserByRoleName(roleName,country){
	$.ajax({
		dataType : "JSON",
		url:baseUrl+"message/getUserByRoleName.action",
		data:{"roleName":roleName,"countryId":country},
		success:function(result){
			var rows = result.rows;
			if(rows!=null && rows.length>0){
				var _optStr="";
				for(var i=0;i<rows.length;i++){
					var row = rows[i];
					var userName=row.userName;
					var userLoginId=row.userLoginId;
					if($("#selectRight").find("option[value='"+userLoginId+"']").size()!=1){
					_optStr+="<option value='"+userLoginId+"'>"+userName+"</option>";
					}
				}
			}
			$("#selectLeft").html(_optStr);
		}
	});
}
//初始化创建课件所在的区域
function initReion(){
	var optStr="";
	$.ajax({
		url:baseUrl + "training/selectAllParty.action",
		type:"POST",
		success:function(result){
			$.each(result,function(i,n){
				optStr+="<option value="+n.partyId+">"+n.partyName+"</option>";
			});
			$("#regionId").html(optStr);
			$("#regionId").change(function(){
				initCountryByRegion($("#regionId").val());	
			});
		}
	});
}

//初使化课件所在的国家并且联动
function initCountryByRegion(region){
	var optStr="<option value=''></option>";
	$.ajax({
		url:baseUrl + "training/selectCountry.action?countryId="+region,
		type:"POST",
		success:function(result){
			$.each(result,function(i,n){
				optStr+="<option value="+n.partyId+">"+n.partyName+"</option>";
			});
			$("#countryId").html(optStr);
		}
	});
}

//编辑初始化国家列表
function editRegionCountry(region,country){
	var optStr="";
	$.ajax({
		url:baseUrl + "training/selectCountry.action?countryId="+region,
		type:"POST",
		success:function(result){
			if(region!='' && region!=null){
				$.each(result,function(i,n){
					if(country==n.partyId){
						optStr+="<option selected='selected'	value="+n.partyId+">"+n.partyName+"</option>";
					}else{
						optStr+="<option 	value="+n.partyId+">"+n.partyName+"</option>";
					}
				});
				$("#countryId").html(optStr);
			}
		}
	});
}

//验证是否有选择角色
//function verifyRole(){
//	var selectRight = $('#selectRight option');
//	var userLoginId="";
//	for(var i=0;i<selectRight.length;i++)
//	{
//		 userLoginId = selectRight[i].innerText;
//	}
//	if($("#roleTypeName").val()=="0"){
//		alert("Please Select RoleType!");
//		$("#roleTypeName").focus();
//		return false;
//	}else if(userLoginId==''){
//		alert("Please Select Users from Left to Right!");
//		$("#selectRight").focus();
//		return false;
//	}
//	return true;
//}


//初始化消息列
function initMsgColumn(){
	$.ajax({
		url:baseUrl+"training/getMessageType.action",
		type:"POST",
		success:function(result){
			var obj = eval('('+result+')');
			var html="";
			$.each(obj.rows,function(i,n){
				html+="<option 	value='"+n.typeId+"'>"+n.typeName+"</option>";
			});
			$("#typeId").html(html);
		}
	});	
}

//删除
function onDelete(index){
	if(confirm(locale("alert.delete.confirm"))){
		var row = $("#courselist").datagrid('getRows')[index];
		$.ajax({
			url:baseUrl + "training/deleteSummary.action",
			type:"POST",
			data:{"summaryId":row.summaryId},
			success:function(data){
				var result=eval('('+data+')');
				if(result.success){
					showMsg(locale("alert.success"));
					$("#courselist").datagrid('reload');
				}else if(result.num){
					showMsg(locale("alert.false"));
				}else{
					showMsg(result.msg);
				}
			}
		});
	}
}

//查看是否是创建课件本人可修改数据
function switchOnOff($form,$btn,createBy){
	if(createBy!=my_login_id){
		$btn.hide();
		$form.find('input,select').attr('readonly',true);
		$form.find('input,select').attr('disabled',true);
		$form.find("input[editable='false']").each(function(){ 
	    	$(this).combobox("disable");
		}); 
	}else{
		$btn.show();
		$form.find('input,select').attr('readonly',false);
		$form.find('input,select').attr('disabled',false);
		$form.find("input[editable='false']").each(function(){ 
	    	$(this).combobox("enable");
		}); 
	}
}

//校验用户是否有编辑/删除功能
function isAvailable()
{	
	if(null!=roleId && ""!=roleId){
		if((roleId.substring(0,6)).indexOf("HADMIN")=='0'){
			return  true;
		}else if( (roleId.substring(0,6)).indexOf("BADMIN")=='0'){
			return  true;
		}else if(roleId.substring(0,7).indexOf("HLEADER")=='0'){
			return  true;
		}else if(roleId.substring(0,7).indexOf("BLEADER")=='0'){
			return  true;
		}
		else{
			return false;
		}
	}
}


////字符串转换为十六进制
//function stringToHex(str){
//	if(str === "")
//		return "";
//	var hexCharCode = [];
//	hexCharCode.push("0x");
//	for(var i = 0; i < str.length; i++) {
//		hexCharCode.push((str.charCodeAt(i)).toString(16));
//		}
//	return hexCharCode.join("");
//}
//
////十六进制转换为字符串
//function hexToString(str){
//	var trimedStr = str.trim();
//	var rawStr = trimedStr.substr(0,2).toLowerCase() === "0x"? trimedStr.substr(2) :trimedStr;
//	var len = rawStr.length;
//	if(len % 2 !== 0) {
//		alert("Illegal Format ASCII Code!");
//		return "";
//	}
//	var curCharCode;
//	var resultStr = [];
//	for(var i = 0; i < len;i = i + 2) {
//		curCharCode = parseInt(rawStr.substr(i, 2), 16); // ASCII Code Value
//		resultStr.push(String.fromCharCode(curCharCode));
//	}
//	return resultStr.join("");
//}



//对字符串进行加密
function compileStr(code){          
	  var c=String.fromCharCode(code.charCodeAt(0)+code.length);  
	 for(var i=1;i<code.length;i++)  
	  {        
	   c+=String.fromCharCode(code.charCodeAt(i)+code.charCodeAt(i-1));  
	 }     
	 return escape(c);  
	 
} 


//字符串进行解密   
function uncompileStr(code){        
 code=unescape(code);        
 var c=String.fromCharCode(code.charCodeAt(0)-code.length);        
 for(var i=1;i<code.length;i++)  
 {        
  c+=String.fromCharCode(code.charCodeAt(i)-c.charCodeAt(i-1));        
 }        
 return c;  
 }

//移除用户
function removeUser(){
	$("#selectRight option:selected").each(function(){
		$("#selectLeft").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
		$(this).remove();
	});
}


function selectToSelectAll(ele1,ele2){//select全部追加
	var selFrom=document.getElementById(ele1);
	var selTarget=document.getElementById(ele2);
	var optionArr=selFrom.children;
	var userArray=[];
	for (var i=optionArr.length-1;i>=0;i--) {
		userArray.push({"userLoginId":optionArr[i].value,"userName":optionArr[i].text});
		selTarget.appendChild(optionArr[0]);
	}
}


//选择国家all时候,查询所有用户
function selectAllUsers(){
	$.ajax({
		dataType : "JSON",
		url:baseUrl+"message/selectAllUsers.action",
		success:function(result){
			var rows = result.rows;
			if(rows!=null && rows.length>0){
				var _optStr="";
				for(var i=0;i<rows.length;i++){
					var row = rows[i];
					var userName=row.userName;
					var userLoginId=row.userLoginId;
					if($("#selectRight").find("option[value='"+userLoginId+"']").size()!=1){
						_optStr+="<option value='"+userLoginId+"'>"+userName+"</option>";
					}
					
				}
			}
			$("#selectLeft").html(_optStr);
		}
	});
}

//根据业务区域，国家选择all
function getUserByRegion(regionId){
	$.ajax({
		dataType : "JSON",
		url:baseUrl+"message/getUserByRegion.action",
		data:{"regionId":regionId},
		success:function(result){
			var rows = result.rows;
			if(rows!=null && rows.length>0){
				var _optStr="";
				for(var i=0;i<rows.length;i++){
					var row = rows[i];
					var userName=row.userName;
					var userLoginId=row.userLoginId;
					if($("#selectRight").find("option[value='"+userLoginId+"']").size()!=1){
						_optStr+="<option value='"+userLoginId+"'>"+userName+"</option>";
					}
					
				}
			}
			$("#selectLeft").html(_optStr);
		}
	});
}



//根据角色类型获取用户
function getUserByCondition(roleName){
	$.ajax({
		dataType : "JSON",
		url:baseUrl+"message/getUserByCondition.action",
		data:{"roleName":roleName},
		success:function(result){
			var rows = result.rows;
			if(rows!=null && rows.length>0){
				var _optStr="";
				for(var i=0;i<rows.length;i++){
					var row = rows[i];
					var userName=row.userName;
					var userLoginId=row.userLoginId;
					if($("#selectRight").find("option[value='"+userLoginId+"']").size()!=1){
						_optStr+="<option value='"+userLoginId+"'>"+userName+"</option>";
					}
					
				}
			}
			$("#selectLeft").html(_optStr);
		}
	});
}



//选择业务中心，国家为all条件时
function getUserByConditions(regionId,roleName){
	$.ajax({
		dataType : "JSON",
		url:baseUrl+"message/getUserByCon.action",
		data:{"regionId":regionId,"roleName":roleName},
		success:function(result){
			var rows = result.rows;
			if(rows!=null && rows.length>0){
				var _optStr="";
				for(var i=0;i<rows.length;i++){
					var row = rows[i];
					var userName=row.userName;
					var userLoginId=row.userLoginId;
					if($("#selectRight").find("option[value='"+userLoginId+"']").size()!=1){
						_optStr+="<option value='"+userLoginId+"'>"+userName+"</option>";
					}
					
				}
			}
			$("#selectLeft").html(_optStr);
		}
	});
}