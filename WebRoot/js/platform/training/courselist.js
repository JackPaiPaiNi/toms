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
			//$("#selectLeft option:first").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在select前面加内容
			//$("#selectLeft option[value=3]").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在selectt指定某一行加内容
			userArray.push({"userLoginId":$(this).val(),"userName":$(this).html()});
			
		});
		
		var courseId=$('#addCourseForm input[name=editId]').val();
		var o={};
		o.userLoginId=JSON.stringify(userArray);
		o.courseId=courseId;
		if($.trim(courseId)!=''){
			$.ajax({
				url:baseUrl+"message/userIsReadCourse.action",
				type:"POST",
				data:o,
				success:function(data){
					var result = eval('('+data+')');
					var users="";
					if(result.rows.length > 0){
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
	
	//移除用户并且校验是否已阅读
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
			//$("#selectLeft option:first").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在select前面加内容
			//$("#selectLeft option[value=3]").before("<option value=" + $(this).val() + ">" + $(this).html() + "</option>"); //此种方法是在selectt指定某一行加内容
			userArray.push({"userLoginId":$(this).val(),"userName":$(this).html()});
			
		});
		
		var courseId=$('#addCourseForm input[name=editId]').val();
		var o={};
		o.userLoginId=JSON.stringify(userArray);
		o.courseId=courseId;
		if($.trim(courseId)!=''){
			$.ajax({
				url:baseUrl+"message/userIsReadCourse.action",
				type:"POST",
				data:o,
				success:function(data){
					var result = eval('('+data+')');
					var users="";
					if(result.rows.length > 0){
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
	
//	$("#correspondingCountryId").change(function(){
//		if($(this).val()!=''){
//			$("#selectLeft").empty();
//			getUserByRoleName($("#roleTypeName").val(),$("#correspondingCountryId").val());
//		}
//	});
//	
//	$("#roleTypeName").change(function(){
//		if($(this).val()!=''){
//			$("#selectLeft").empty();
//			getUserByRoleName($(this).val(),$("#correspondingCountryId").val());
//		}else if($(this).val()==""){
//			getUserByRoleName('',$("#correspondingCountryId").val());
//			$("#selectLeft").empty();
//		}
//	});
	
	$("#correspondingRegionId").change(function(){
		if($(this).val()=='-1' && $("#roleTypeName").val()=='-1'){
			$("#selectLeft").empty();
			selectAllUsers();
		}else if($(this).val()!='-1' && $("#correspondingCountryId").val()=='-1'){
			$("#selectLeft").empty();
			getUserByRegion($('#region').val());
		}else if($(this).val()=='-1' && $("#roleTypeName").val()!='-1'){
			$("#selectLeft").empty();
			getUserByCondition($("#roleTypeName").val());
		}else{
			$("#selectLeft").empty();
		}
	});
	
	
	$("#correspondingCountryId").change(function(){
		if($(this).val()!='-1' && $("#roleTypeName").val()!=''){
			if($("#roleTypeName").val()=='-1'){
				$("#selectLeft").empty();
				getUserByRoleName('',$("#correspondingCountryId").val());
			}else{
				$("#selectLeft").empty();
				getUserByRoleName($("#roleTypeName").val(),$("#correspondingCountryId").val());
			}
		}else if($(this).val() =='-1' && $('#correspondingRegionId').val()!='-1'){
			$("#selectLeft").empty();
			getUserByRegion($('#correspondingRegionId').val());
		}else if($("#roleTypeName").val()!=''){
			$("#selectLeft").empty();
			getUserByRoleName($("#roleTypeName").val(),$("#correspondingCountryId").val());
		}
	});
	
	$("#roleTypeName").change(function(){
		if($(this).val()!='-1' && $("#correspondingCountryId").val()=='-1'){
			$("#selectLeft").empty();
			getUserByCondition($(this).val());
//			getUserByConditions($('#correspondingRegionId').val(),$(this).val());
		}else if($(this).val()==''){
			$("#selectLeft").empty();
//			getUserByRoleName('',$("#country").val());
		}else if($(this).val()=='-1' && $("#correspondingCountryId").val()=='' && $('#correspondingRegionId').val()=='-1'){
			$("#selectLeft").empty();
			selectAllUsers();
		}else if($(this).val()=='-1' && $("#correspondingCountryId").val()==null && $('#correspondingRegionId').val()=='-1' ){
			$("#selectLeft").empty();
			selectAllUsers();
		}else if($(this).val()!='' && $("#correspondingCountryId").val()==null && $('#correspondingRegionId').val()=='-1'){
			$("#selectLeft").empty();
			getUserByCondition($(this).val());
		}else if($(this).val()=='-1' && $("#correspondingCountryId").val()!='-1'){
			$("#selectLeft").empty();
			getUserByRoleName('',$("#correspondingCountryId").val());
		}else if($(this).val()!='' && $("#correspondingCountryId").val()=='' && $('#correspondingRegionId').val()=='-1'){
			$("#selectLeft").empty();
			getUserByCondition($(this).val());
		}else{
			$("#selectLeft").empty();
			getUserByRoleName($(this).val(),$("#correspondingCountryId").val());
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
	
	loadlevel1Type();
	initLevel2Type();
	initLevel3Type();
	loadCourseDataList();
	
/***************************1.课程类别级联菜单Start*****************************************/	
	//加载一级栏目的方法
	function loadlevel1Type()
	{
		initLevel1Type();
		$("#column").removeAttr("disabled");
		$.ajax({
			url:baseUrl+"training/loadLevel1CourseType.action",
			type:"GET",
			success:function(data)
			{
				$.each(data.rows,function(n,value){					
					$("#column").append("<option value='"+value.typeId+"'>"+value.typeName+"</option>");
				});
			},
			dataType:"json"
		});
	}
	//加载二级栏目
	function loadlevel2Type()
	{
		var typeId=$("#column").val();
		if(typeId=='')
		{
			return;
		}
		$("#subcolumn").removeAttr("disabled");
		$.ajax({
		   url:baseUrl+"training/loadSubCourseType.action",
		   type:"POST",
		   data:{"typeId":typeId},
		   success:function(data){
			   $.each(data.rows,function(n,value){
				   $("#subcolumn").append("<option value='"+value.typeId+"'>"+value.typeName+"</option>");
			   })
		   },
		   dataType:"json"
		});
	}
	//加载三级栏目
	function loadlevel3Type()
	{
		var typeId=$("#subcolumn").val();
		if(typeId=='')
		{
			return;
		}
		$("#subtocolumn").removeAttr("disabled");
		$.ajax({
			url:baseUrl+"training/loadSubCourseType.action",
			type:"POST",
			data:{"typeId":typeId},
			success:function(data)
			{
				$.each(data.rows,function(n,value){
					   $("#subtocolumn").append("<option value='"+value.typeId+"'>"+value.typeName+"</option>");
				   })
			},
			dataType:"json"
		});
	}
	//初始化一级栏目列表
	function initLevel1Type()
	{
		
		$("#column").empty();
		$("#column").append("<option value=''>Please Select The Column</option>");
	}
	//初始化二级栏目列表
	function initLevel2Type()
	{
		$("#subcolumn").empty();
		$("#subcolumn").append("<option value=''>Please Select The Sub-Column</option>");
		$("#subcolumn").attr("disabled",true);
	}
	//初始化三级栏目列表
	function initLevel3Type()
	{
		$("#subtocolumn").empty();
		$("#subtocolumn").append("<option value=''>Please Select The Sub-Column</option>");
		$("#subtocolumn").attr("disabled",true);
	}
	//选择消息栏目
	$("#column").change(function(){
		if($("#column").val()=='')
		{
			initLevel2Type();
			initLevel3Type();
			return;
		}
		initLevel2Type();
		loadlevel2Type();
	})
	//选择子栏目		
	$("#subcolumn").change(function(){
		if($("#subcolumn")=='')
		{
			initLevel3Type();
			return;
		}
		initLevel3Type();
		loadlevel3Type();
	});
	
/***************************1.课程类别级联菜单End*****************************************/	
	
/***************************2.加载课程列表Start*************************************************/	
function loadCourseDataList()
{	
	initCourseDataList();
	var param={};
	param.typeId=$("#subtocolumn").val();
	param.keyword=$("#keyword").val();
//	var html='';
//	$.ajax({
//		url:baseUrl+"training/loadCourseListData.action",
//		type:"POST",
//		data:param,
//		success:function(data)
//		{
//			$.each(data.rows,function(n,value){			  
//				   $("#Query_Result").append("<tr>");
//				   $("#Query_Result").append("<td>"+value.typeId+"</td>");
//				   $("#Query_Result").append("<td>"+value.courseTitle+"</td>");
//				   $("#Query_Result").append("<td>"+value.createBy+"</td>");
//				   $("#Query_Result").append("<td>"+value.courseSummary+"</td>");
//				   $("#Query_Result").append("<td>"+value.messageType+"</td>");
//				   $("#Query_Result").append("<td>Saved</td>");
//				   $("#Query_Result").append("<td>"+value.createDate+"</td>");
//				   $("#Query_Result").append("<td>Edit</td>");
//				   $("#Query_Result").append("<td class='remove_tr'>Delete</td>");
//				   $("#Query_Result").append("</tr>")
//			   });	
//			$('#caption_i').text(data.total);
//		},
//		dataType:"json"
//	});
	
	
	$("#courselist").datagrid({
		title:locale("course.list.title"),
		url:baseUrl+"training/loadCourseListData.action",
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
		columns:[[
		          {field: 'courseId',  title:"courseId",width:50,hidden:true},
		          {field: 'partyName',  title: jsLocale.get('district.countryName'),width:50},
		          {field: 'typeId',  title: jsLocale.get('train.typeId'),width:50,hidden:true}, 
		            {field: 'courseTitle',  title: jsLocale.get('train.courseTitle'),width:80},  
		            {field: 'createBy',  title: jsLocale.get('train.CreateBy'),width:50,hidden:true},
		            {field: 'courseSummary',  title: jsLocale.get('train.courseSummary'),width:50},
		            {field: 'messageType',  title: jsLocale.get('train.datamessageTypeData'),width:50,formatter:messageTypeformatter},
		            {field: 'coverImgUrl',  title: jsLocale.get('train.cover'),width:50,formatter:imgFormatter},
		            {field: 'state',  title: jsLocale.get('train.state'),width:50,formatter:pushformatter},
		            {field: 'createDate',  title: jsLocale.get('train.CreateDate'),width:50,formatter:todate},
		            {field:'op',title:locale("toolbar.edit"),width:60,formatter:editFormatter},
		          ]],
		 toolbar:'#coursetb',
		 onHeaderContextMenu:onEasyGridHeadMenu,
		 onLoadSuccess:enableBt
	});
		$.ajax({
			url : baseUrl
					+ "training/loadCourseListData.action",
			type : "POST",
			data : param,
			success : function(data) {
				$('#caption_i').text(data.total);
			}

		});
		
}
initWindow();


function initCourseDataList(){
	$("#Query_Result").empty();
}
});

//初始化
function initWindow(){
	initReion();
//	initParty($("#correspondingRegionId"),$("#correspondingCountryId"),null);
	firstColumn();
//	initColumn();
	initCountry();
//	editColumn($("#levelOneTypeId"),$("#levelTwoTypeId"),$("#levelThreeTypeId"),null);
//	$(".tcladdwin input[name='correspondingPartyId']").combobox({
//		data:[{text:locale("train.course.all"),value:999}]
//	});
	$(".tcladdwin input[name='messageType']").combobox({
		data:[{text:locale("train.course.graphic"),value:1},{text:locale("train.course.article"),value:2},
		      {text:locale("train.course.video"),value:3}]
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
		return "Graphic";
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
	
	if(value == null)
	{
		return null;
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
	if($("#levelOneTypeId").val()==""){
		showMsg(locale("train.firse.column"));
		$("#levelOneTypeId").focus();
		return false;
	}
	
	if($("#levelTwoTypeId").val()==""){
		showMsg(locale("train.second.column"));
		$("#levelTwoTypeId").focus();
		return false;
	}
	
	if($("#levelThreeTypeId").val()==""){
		showMsg(locale("train.third.column"));
		$("#levelThreeTypeId").focus();
		return false;
	}
	
	
	if($.trim($("#courseTitle").val())==""){
		showMsg(locale("train.course.title"));
		$("#courseTitle").focus();
		return false;
		
	}
	
	if($.trim($("#courseSummary").val())==""){
		showMsg(locale("train.course.summary"));
		$("#courseSummary").focus();
		return false;
		
	}
	
	if($.trim(ue.getContent())=='')
	{
		showMsg(locale('train.course.content'));
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
//	editDistrict($("#correspondingRegionId"),$("#correspondingCountryId"),row);
//	$("#levelTwoTypeId").html("<option  value='"+row.levelTwoTypeId+"'>"+row.levelTwoTypeName+"</option>");
//	$("#levelThreeTypeId").html("<option  value='"+row.levelThreeTypeId+"'>"+row.levelThreeTypeName+"</option>");
	$('#addCourseForm input[name=editId]').val(row.courseId);
	$('#courseTitle').val(row.courseTitle);
	$('#courseSummary').val(row.courseSummary);
	$('#addBarcodeForm #barcode').textbox("disable");
	switchOnOff("edit",$("#addCourseForm"),$("#submitBtn"),row.createBy);
	loadPartyList(row.correspondingRegionId,row.correspondingCountryId);
	//显示窗口
	$('#addCourseWin').window({title:locale("barcode.window.edit")});
	$('#addCourseWin').window('center');
	$('#addCourseWin').window('open');
//	editor.html(row.courseContent);	//kingeditor获取文本内容
	UE.getEditor('editor').setContent(uncompileStr(row.courseContent));  //给UEditor编辑器赋值
	//填充级联菜单显示的数据
	editRegionCountry(row.correspondingRegionId,row.correspondingCountryId);
	editFirstSecond(row.levelOneTypeId,row.levelTwoTypeId);
	editSecondThird(row.levelTwoTypeId,row.levelThreeTypeId);
	
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

//编辑级联符值
//function editDistrict($country,$partyId,row){
//	var countryId = $country.combobox("getValue");
//	if(countryId=="999"){
//		$country.combobox("setText","All");
//		$partyId.combobox("disable");
//	}else{
//	if($country){
//		$partyId.combobox('reload',baseUrl + 'training/selectCountry.action?countryId='+countryId);
//		$partyId.combobox("setValue",row.correspondingCountryId);
//		$partyId.combobox("enable");
//	}
//	}
//}
function submitForm(){
	if($("#file").val()==''){
		if(validate()){
			$("#imgUrl").show();
			$("#submitBtn").attr("disabled","true");
			//添加or编辑
			var courseId=$('#addCourseForm input[name=editId]').val();
			if($.trim(courseId)!=""){
				//编辑
				var param = "";
				var userParam="";
				//获取右边框选中的所有用户
				var selectRight = $('#selectRight option');
				var userArray = [];
				var allUserLoginArray = [];
				var allUserStr = "";
				var allUserLogin="";
				var o = {};
				o.courseContents=compileStr(ue.getContent());
				o.levelOneTypeId=$("#levelOneTypeId").val();
				o.levelTwoTypeId=$("#levelTwoTypeId").val();
				o.levelThreeTypeId=$("#levelThreeTypeId").val();
				o.messageType=$("#messageType").val();
				o.correspondingRegionId=$("#correspondingRegionId").val();
				o.correspondingCountryId=$("#correspondingCountryId").val();
				o.courseSummary=encodeURIComponent($("#courseSummary").val());
//				o.roleTypeName=$("#roleTypeName").val();
				o.state=$("#state").val();
				o.coverImgUrl=$("#hidcover").val();
				o.courseTitle=encodeURIComponent($("#courseTitle").val());
				o.editId=courseId;
				for(var i=0;i<selectRight.length;i++)
				{
					var msgRoleId = selectRight.eq(i).attr("value");
					//allUserStr += msgRoleId+";";
					userArray[i] = {'userId':msgRoleId};
				}
//					param = allUserStr;
				
				if(userArray.length!=0){
//						for(var i=0;i<selectRight.length;i++)
//						{
//							var userLoginId = selectRight[i].innerText;
////							allUserLogin += userLoginId+";";
//							allUserLoginArray[i] = {'userId':userLoginId};
//						}
///					userParam+=allUserLogin;
					
					
					o.param = JSON.stringify(userArray);
					
//						o.userParam = JSON.stringify(allUserLoginArray);
					
//				if(param!='' && param!=null && userParam !='' && userParam!=null){
					$.ajax({
						url:baseUrl + "training/editCourse.action",
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
//						$('#addCourseForm').ajaxSubmit({
//							url:baseUrl + "training/editCourse.action",
//							data:o,
//							type:"POST",
//							success:function(data){
//								var result=eval('('+data+')');				
//								if(result.success){
//									$("#courselist").datagrid('reload');
//									$('#addCourseWin').window('close');
//									showMsg(locale("alert.success"));
//								}else{
//									showMsg(result.msg);
//								}
//							}
//						});
				}else{
					$("#imgUrl").hide();
					$("#submitBtn").removeAttr("disabled");
					alert("At Least One User !");
					$("#selectRight").focus();
				}			
			}
		}else{
			
		}
	}else{
		if(!check()){
			return;
		}
		
		if(validate()){
			$("#imgUrl").show();
			$("#submitBtn").attr("disabled","true");
			//添加or编辑
			var courseId=$('#addCourseForm input[name=editId]').val();
			if($.trim(courseId)!=""){
				//编辑
				var param = "";
				var userParam="";
				//获取右边框选中的所有用户
				var selectRight = $('#selectRight option');
				var userArray = [];
				var allUserLoginArray = [];
				var allUserStr = "";
				var allUserLogin="";
				for(var i=0;i<selectRight.length;i++)
				{
					var msgRoleId = selectRight.eq(i).attr("value");
					//allUserStr += msgRoleId+";";
					userArray[i] = {'userId':msgRoleId};
				}
//					param = allUserStr;
				
				if(userArray.length!=0){
					for(var i=0;i<selectRight.length;i++)
					{
						var userLoginId = selectRight[i].innerText;
//							allUserLogin += userLoginId+";";
						allUserLoginArray[i] = {'userId':userLoginId};
					}
///					userParam+=allUserLogin;
					
					var o = {};
					o.param = JSON.stringify(userArray);
					o.courseContents=compileStr(ue.getContent());
					o.levelOneTypeId=$("#levelOneTypeId").val();
					o.levelTwoTypeId=$("#levelTwoTypeId").val();
					o.levelThreeTypeId=$("#levelThreeTypeId").val();
					o.messageType=$("#messageType").val();
					o.correspondingRegionId=$("#correspondingRegionId").val();
					o.correspondingCountryId=$("#correspondingCountryId").val();
					o.courseSummary=encodeURIComponent($("#courseSummary").val());
					o.state=$("#state").val();
					o.coverImgUrl=$("#hidcover").val();
					o.courseTitle=encodeURIComponent($("#courseTitle").val());
					o.editId=courseId;
//						o.userParam = JSON.stringify(allUserLoginArray);
					
//				if(param!='' && param!=null && userParam !='' && userParam!=null){
					
					$.ajax({
						url:baseUrl + "training/editCourse.action",
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
//						url:baseUrl + "training/editCourse.action",
//						data:o,
//						type:"POST",
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


//删除课程
function onDelete(index){
	if(confirm(locale("alert.delete.confirm"))){
		var row = $("#courselist").datagrid('getRows')[index];		
		$.ajax({
			url:baseUrl + "training/deleteCourse.action",
			type:"POST",
			data:{"courseId":row.courseId},
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

//非总部999不可修改region和country
function loadPartyList(region,country){
	var userCountryId=countryId;//用户所在的国家
	if(userCountryId!="999")
	{	
		$("#correspondingRegionId").combobox({disabled: true});
		$.ajax({
			url:baseUrl+"training/selectRegion.action?partyId="+regionId,
			type:"POST",
		}).success(function(data){
		$("#correspondingRegionId").combobox("setValue",data.rows[0].partyName);
		
		});
		if(countryId!="999")
		{	
			$("#correspondingCountryId").combobox({disabled: true});
			$.ajax({
				url:baseUrl+"training/selectRegion.action?partyId="+country,
				type:"POST",
			}).success(function(data){
			$("#correspondingCountryId").combobox("setValue",data.rows[0].partyName);
		});
		}
		return;
	}else{
		if(roleName.indexOf("EMSC")==0){
			$("#correspondingRegionId").empty();
			$("#correspondingRegionId").append("<option value='1'>"+roleName.substring(0,4)+"</option>");
			$("#correspondingRegionId").val("1");
			$("#correspondingRegionId").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("BDSC")==0){
			$("#correspondingRegionId").empty();
			$("#correspondingRegionId").append("<option value='2'>"+roleName.substring(0,4)+"</option>");
			$("#correspondingRegionId").val("2");
			$("#correspondingCountryId").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("NASC")==0){
			$("#correspondingRegionId").empty();
			$("#correspondingRegionId").append("<option value='3'>"+roleName.substring(0,4)+"</option>");
			$("#correspondingRegionId").val("3");
			$("#correspondingRegionId").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("EUSC")==0){
			$("#correspondingRegionId").empty();
			$("#correspondingRegionId").append("<option value='14'>"+roleName.substring(0,4)+"</option>");
			$("#correspondingRegionId").val("14");
			$("#correspondingRegionId").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("SRSC")==0){
			$("#correspondingRegionId").empty();
			$("#correspondingRegionId").append("<option value='15'>"+roleName.substring(0,4)+"</option>");
			$("#correspondingRegionId").val("15");
			$("#correspondingRegionId").attr("disabled",true);
			
			loadCountryListData();
		}
	}
}

function loadCountryListData()
{
	var regionId=$("#correspondingRegionId").val();
	if(regionId=='0'||regionId=='-1')
	{
		return;
	}
	$("#correspondingCountryId").removeAttr("disabled");
	var param={};
	param.partyId=regionId;
	$.ajax({
		url:baseUrl+"party/loadCountryData.action",
		type:"POST",
		data:param,
		success:function(data){
//			$("#country").append("<option value='-1'>All</option>");
			$.each(data,function(n,value){
				$("#correspondingCountryId").append("<option value='"+value.partyId+"'>"+value.partyName+"</option>");
			});
		},
		dataType:"json"
	});
}

//function initParty($country,$partyId,$table){
//	if (!$country)
//		return;
//	$country.combobox({
//		url : baseUrl + 'training/selectAllParty.action?',
//		valueField : 'partyId',
//		textField : 'partyName',
//		onChange : function(newValue, oldValue) {
//			var countryId = $(this).combobox("getValue");
//			if ($partyId) {
//				$partyId.combobox("clear");
//				$partyId.combobox('reload', baseUrl
//						+ 'training/selectCountry.action?countryId='
//						+ countryId + '&random=' + Math.random());
//				$partyId.combobox("setValue", '');
//			}
//			if ($table) {
//				var qryParms = $table.datagrid("options").queryParams;
//				if (countryId == "") {
//					countryId = null;
//				}
//				qryParms.countryId = countryId;
//				qryParms.partyId = $partyId.combobox("getValue");
//				$table.datagrid({
//					queryParams : qryParms,
//				});// 重新加载
//			};
//		}
//	});
//	if ($partyId) {
//		$partyId.combobox({
//			valueField : 'partyId',
//			textField : 'partyName',
//			onChange : function(newValue, oldValue) {
//				return;
//			}
//		});
//	}
//}

function doSearch(){
	$("#searchBt").linkbutton("disable");
	var countryId=$("#countryName").val();
	var type=$("#column").val();
	var typeIdSub=$("#subcolumn").val();
	var typeIdSubto=$("#subtocolumn").val();
	var searchKey=$("#searchCourse").val();
	var Messagetype=$("#messagetype").val();
	$("#courselist").datagrid({
		queryParams:{
			partyId:countryId,
			typeId:type,
			typeIdSubId:typeIdSub,
			typeIdSubtoId:typeIdSubto,
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
        rvalue += "<img onclick=download(\""+strs[i]+"\") style='width:120px; height:60px;margin-left:3px;' src=" + strs[i] + " title='点击查看图片'/>";  
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

function editColumn($levelOneTypeId,$levelTwoTypeId,$levelThreeTypeId,$table){
	if(!$levelOneTypeId)
		return;
	var data=[];  //自定义一个空的数组存放集合
	$levelOneTypeId.combobox({
		url : baseUrl + 'training/getLevelOneTypeId.action?',
		valueField : 'typeId',
		textField : 'typeName',
		onChange : function(newValue, oldValue) {
			var typeId = $(this).combobox("getValue");
			if ($levelTwoTypeId) {
				$levelTwoTypeId.combobox("clear");
				$levelTwoTypeId.combobox('reload', baseUrl
						+ 'training/getLevelTwoOrthreeTypeId.action?typeId='
						+ typeId + '&random=' + Math.random());
				$levelTwoTypeId.combobox("setValue", '');
				
			}
			if ($levelThreeTypeId) {
				$levelThreeTypeId.combobox("clear");
				$levelThreeTypeId.combobox('reload', data);
				$levelThreeTypeId.combobox("setValue", '');
				
			}
			
			if ($table) {
				var qryParms = $table.datagrid("options").queryParams;
				if (typeId == "") {
					typeId = null;
				}
				qryParms.typeId = typeId;
				qryParms.typeId = $levelTwoTypeId.combobox("getValue");
				$table.datagrid({
					queryParams : qryParms,
				});// 重新加载
			};
		}
	});
	if ($levelTwoTypeId) {
		$levelTwoTypeId.combobox({
			valueField : 'typeId',
			textField : 'typeName',
			onChange : function(newValue, oldValue) {
				return;
			}
		});
	}
	
	if ($levelThreeTypeId) {
		$levelThreeTypeId.combobox({
			valueField : 'typeId',
			textField : 'typeName',
			onChange : function(newValue, oldValue) {
				return;
			}
		});
	}
	
	if ($levelTwoTypeId) {
		$levelTwoTypeId.combobox({
			valueField : 'typeId',
			textField : 'typeName',
			onChange : function(newValue, oldValue) {
				var levelTwoTypeId= $(this).combobox("getValue");
				
	
					
				if ($levelThreeTypeId) {
//					$levelThreeTypeId.combobox("clear");
					$levelThreeTypeId.combobox('reload', baseUrl
							+ 'training/getLevelthreeTypeId.action?typeId='
							+ levelTwoTypeId + '&random=' + Math.random());
//					$levelThreeTypeId.combobox("setValue", data);
				}
				
				
				
				if ($table) {
					var qryParms = $table.datagrid("options").queryParams;
					qryParms.typeId = $levelTwoTypeId.combobox("getValue");
					if (typeId == "") {
						typeId = null;
					}
					qryParms.typeId = typeId;
					qryParms.levelThreeTypeId = $levelThreeTypeId.combobox("getValue");
					$table.datagrid({
						queryParams : qryParms,
					});// 重新加载
				};
			}
		});
	}
	
	
}

//function initColumn(){
//	$.ajax({
//		url : baseUrl + 'training/getLevelOneTypeId.action?',
//		type:"POST",
//	}).success(function(result){
//		$("#levelOneTypeId").combobox({
//			data:result,
//			valueField : 'typeId',
//			textField : 'typeName',
//			onChange:function(){
//				var getLevelOneTypeId=$(this).combobox("getValue");
//				$.ajax({
//					url:baseUrl
//					+ 'training/getLevelTwoOrthreeTypeId.action?',
//					type:"POST",
//					data:{"typeId":getLevelOneTypeId},
//				}).success(function(result){
//					$("#levelTwoTypeId").combobox({
//						data:result,
//						valueField : 'typeId',
//						textField : 'typeName',
//						onChange:function(){
//							var getLevelTwoTypeId=$(this).combobox("getValue");
//							$.ajax({
//								url:baseUrl
//								+ 'training/getLevelthreeTypeId.action?',
//								type:"POST",
//								data:{"typeId":getLevelTwoTypeId},
//							}).success(function(result){
//								$("#levelThreeTypeId").combobox({
//									data:result,
//									valueField : 'typeId',
//									textField : 'typeName',
//								});
//							});
//						}
//					});
//				});
//			}
//		});
//	});
//}
//
function firstColumn(){
	var optStr="";
	$.ajax({
		url : baseUrl + 'training/getLevelOneTypeId.action?',
		type:"POST",
	}).success(function(result){
		$.each(result,function(i,n){
			optStr +="<option value='"+n.typeId+"'>"+n.typeName+"</option>";
		});
		$("#levelOneTypeId").html(optStr);
		$("#levelOneTypeId").change(function(){
			secondColumn($(this).val()== "" ? -1 : $(this).val());
		});
	});
}

function secondColumn(getLevelOneTypeId){
	var optStr="<option value=''></option>";
	$.ajax({
		url:baseUrl
		+ 'training/getLevelTwoOrthreeTypeId.action?',
		type:"POST",
		data:{"typeId":getLevelOneTypeId},
	}).success(function(result){
		$.each(result,function(i,n){
			optStr +="<option value='"+n.typeId+"'>"+n.typeName+"</option>";
		});
		$("#levelTwoTypeId").html(optStr);
		$("#levelTwoTypeId").change(function(){
			thirdColumn($(this).val()== "" ? -1 : $(this).val());
		});
	});
}

function thirdColumn(getLevelTwoTypeId){
	var optStr="<option value=''></option>";
	$.ajax({
		url:baseUrl
		+ 'training/getLevelthreeTypeId.action?',
		type:"POST",
		data:{"typeId":getLevelTwoTypeId},
	}).success(function(result){
		$.each(result,function(i,n){
			optStr +="<option value='"+n.typeId+"'>"+n.typeName+"</option>";
		});
		$("#levelThreeTypeId").html(optStr);
	});
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
			$("#correspondingRegionId").html(optStr);
			$("#correspondingRegionId").change(function(){
				initCountryByRegion($("#correspondingRegionId").val());	
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
			$("#correspondingCountryId").html(optStr);
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
				$("#correspondingCountryId").html(optStr);
			}
		}
	});
}

//验证是否有选择角色
function verifyRole(){
	var selectRight = $('#selectRight option');
	var userLoginId="";
	for(var i=0;i<selectRight.length;i++)
	{
		 userLoginId = selectRight[i].innerText;
	}
	if($("#roleTypeName").val()==""){
		alert("Please Select RoleType!");
		$("#roleTypeName").focus();
		return false;
	}else if(userLoginId==''){
		alert("Please Select Users from Left to Right!");
		$("#selectRight").focus();
		return false;
	}
	return true;
}

//填充数据
function editFirstSecond(first,second){
	var optStr="";
	$.ajax({
		url:baseUrl
		+ 'training/getLevelTwoOrthreeTypeId.action?typeId='+first,
		type:"POST",
		success:function(result){
			$.each(result,function(i,n){
				if(second==n.typeId){
					optStr +="<option selected='selected' value='"+n.typeId+"'>"+n.typeName+"</option>";
				}else{
					optStr +="<option  value='"+n.typeId+"'>"+n.typeName+"</option>";
				}
				$("#levelTwoTypeId").html(optStr);
				$("#levelTwoTypeId").change(function(){
					thirdColumn($(this).val()== "" ? -1 : $(this).val());
				});
			});			
		}
	});	
}

function editSecondThird(second,third){
	var optStr="";
	$.ajax({
		url:baseUrl
		+ 'training/getLevelTwoOrthreeTypeId.action?typeId='+second,
		type:"POST",
		success:function(result){
			$.each(result,function(i,n){
				if(third==n.typeId){
					optStr +="<option selected='selected' value='"+n.typeId+"'>"+n.typeName+"</option>";
				}else{
					optStr +="<option  value='"+n.typeId+"'>"+n.typeName+"</option>";
				}
				$("#levelThreeTypeId").html(optStr);
			});
		}
	});
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

//查看数据
//function onView(index){
//	var row=$("#courselist").datagrid('getRows')[index];
//	$('#addCourseForm').form('load',row);
//	switchOnOff("view",$("#addCourseForm"),$("#submitBtn"));
//	loadPartyList(row.correspondingRegionId,row.correspondingCountryId);
//	editRegionCountry(row.correspondingRegionId,row.correspondingCountryId);
//	
//	editFirstSecond(row.levelOneTypeId,row.levelTwoTypeId);
//	editSecondThird(row.levelTwoTypeId,row.levelThreeTypeId);
//	
//	UE.getEditor('editor').setContent(row.courseContent);  //给UEditor编辑器赋值
//	
//	//获取发送消息的用户
//	$.ajax({
//		url:baseUrl+"message/selectUserBycondition.action?msgRoleId="+row.msgRoleId,
//		type:"POST",
//		success:function(result){
//			var obj = eval('('+result+')');
//			var optStr="";
//			$.each(obj.rows,function(i,n){
//				optStr +="<option  value="+n.userLoginId+">"+n.userName+"</option>";
//			});
//			$("#selectRight").html(optStr);
//		}
//	});
//	$('#addCourseWin').window({title:locale("customer.window.view")}).window('center').window('open');
//}

//查看是否是创建课件本人可修改数据
function switchOnOff(switchSign,$form,$btn,createBy){
	if(switchSign=='view' || createBy!=my_login_id){
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


function editFormatter(value,item,index){
	return '<div class="tcl-btn-group">'+
	'<a href="javascript:void(0);" class="tcl-btn" onclick="onEdit('+index+')">'+
		'<i class="jicon jicon-edit"></i>'+locale("toolbar.edit")+'</a>'+
	'<a href="javascript:void(0);" class="tcl-btn tcl-btn-warning" onclick="onDelete(\''+index+'\')">'+
		'<i class="fa fa-arrow-right"></i>'+locale("toolbar.delete")+'</a></div>';
}

//字符串转换为十六进制
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



function selectToSelectAll(ele1,ele2){//select全部追加
	var selFrom=document.getElementById(ele1);
	var selTarget=document.getElementById(ele2);
	var optionArr=selFrom.children;
	for (var i=optionArr.length-1;i>=0;i--) {
		selTarget.appendChild(optionArr[0]);
	}
}