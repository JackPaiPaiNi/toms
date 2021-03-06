$(function(){
	loadMessage();
	loadSCPartyData();
	
	
	//点击全选到右边
	$('#toRightAll').click(function(){
		selectToSelectAll('selectLeft','selectRight');
	});	
	//点击全选到左边
	$('#toLeftAll').click(function(){
		selectToSelectAll('selectRight','selectLeft');
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
		
		$("#selectRight option:selected").each(function(){
			$("#selectLeft").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
			$(this).remove();
		});
	});
	
//	$("#country").change(function(){
//		if($(this).val()!=''){
//			$("#selectLeft").empty();
//			getUserByRoleName($("#roleTypeName").val(),$("#country").val());
//		}
//	});
//	
//	$("#roleTypeName").change(function(){
//		if($(this).val()!=''){
//			$("#selectLeft").empty();
//			getUserByRoleName($(this).val(),$("#country").val());
//		}else if($(this).val()==''){
//			$("#selectLeft").empty();
//			getUserByRoleName('',$("#country").val());
//		}
//	});
	
	$("#region").change(function(){
		if($(this).val()=='-1' && $("#roleTypeName").val()=='-1'){
			$("#selectLeft").empty();
			selectAllUsers();
		}else if($(this).val()!='-1' && $("#country").val()=='-1'){
			$("#selectLeft").empty();
			getUserByRegion($('#region').val());
		}else if($(this).val()=='-1' && $("#roleTypeName").val()!='-1'){
			$("#selectLeft").empty();
			getUserByCondition($("#roleTypeName").val());
		}else{
			$("#selectLeft").empty();
		}
	});
	
	
	$("#country").change(function(){
		if($(this).val()!='-1' && $("#roleTypeName").val()!=''){
			if($("#roleTypeName").val()=='-1'){
				$("#selectLeft").empty();
				getUserByRoleName('',$("#country").val());
			}else{
				$("#selectLeft").empty();
				getUserByRoleName($("#roleTypeName").val(),$("#country").val());
			}
		}else if($(this).val() =='-1' && $('#region').val()!='-1'){
			$("#selectLeft").empty();
			getUserByRegion($('#region').val());
		}else if($("#roleTypeName").val()!=''){
			$("#selectLeft").empty();
			getUserByRoleName($("#roleTypeName").val(),$("#country").val());
		}
	});
	
	$("#roleTypeName").change(function(){
		if($(this).val()!='-1' && $("#country").val()=='-1'){
			$("#selectLeft").empty();
			getUserByConditions($('#region').val(),$(this).val());
		}else if($(this).val()==''){
			$("#selectLeft").empty();
//			getUserByRoleName('',$("#country").val());
		}else if($(this).val()=='-1' && $("#country").val()=='0' && $('#region').val()=='-1'){
			$("#selectLeft").empty();
			selectAllUsers();
		}else if($(this).val()!='' && $("#country").val()=='0' && $('#region').val()=='-1'){
			$("#selectLeft").empty();
			getUserByCondition($(this).val());
		}else if($(this).val()=='-1'){
			$("#selectLeft").empty();
			getUserByRoleName('',$("#country").val());
		}else{
			$("#selectLeft").empty();
			getUserByRoleName($(this).val(),$("#country").val());
		}
	});
	
	//判断是否选择区域
	if($("#region").val()=='0'||$("#region").val()=='-1')
	{
		initCountryList();
	}
	
	//选择区域
	$("#region").change(function(){
		if($("#region").val()=='0'||$("#region").val()=='-1')
		{
			initCountryList();
			return;
		}
		initCountryList();
		loadCountryListData();
	});
	
	

	//封面图片的显示
	$("#coverimg").change(function(){
		if(!validateCoverImg()){
			return;
		}
		var file=this.files[0];
		var reader=new FileReader();
		reader.onload=function()
		{
			$("#Coverpreview").attr("src",reader.result);
		}
		if(file)
		{
			reader.readAsDataURL(file);
		}
		else{
			$("#Coverpreview").attr("src","");
		}
	});	


	//上传封面图片,图片地址保存在隐藏域中
	$("#coverupload").click(function(){
		var options={
			type:"POST",
			url:baseUrl+"updownload/fileUploadAction.action?flag=cover",
			success:function(data){
				if(!validateCoverImg()){
					return;
				}else{
					if(data.path!=""){
						$("#hidcover").val(data.path);
						$("#coverimg").attr("disabled","true");
						$("#coverupload").attr("disabled","true");
						alert("upload successful!");
					}
				}
				if(data.path==""){				
					alert("Please Select A Cover！");
					
				}
				
			}
		};
		$("#coverform").ajaxSubmit(options);
	});


	//上传附件,附件地址保存在隐藏域中
	//提交课程数据,保存至数据库
	$("#submit").click(function(){
		if(!validateData())
		{
			return;
		} 
		var param={};
		param.typeId=$("#column").val();
		param.roleTypeName=$("#roleTypeName").val();
		param.messageType=$("#messagetype").val();
		param.courseTitle=encodeURIComponent($("#title").val());
		param.courseSummary=encodeURIComponent($("#summary").val());
		param.coverImgUrl=$("#hidcover").val();
		param.courseContent=compileStr(ue.getContent());
		
		//获取右边框选中的所有用户
		var selectRight = $('#selectRight option');
		var allUserStr = "";
		var userArray = [];
		for(var i=0;i<selectRight.length;i++)
		{
			var userLoginId = selectRight.eq(i).attr("value");
//			allUserStr += userLoginId+";";
			userArray[i] = {'userId':userLoginId};
		}
//		if(""!=allUserStr && null!=allUserStr)
//		{
//			allUserStr = allUserStr.substring(0,allUserStr.length-1);
//		}
		param.allUserStr = JSON.stringify(userArray);
		
		
		var partyId,regionId,countryId;
		if($("#region").val()=='-1')
		{
			partyId='999';
			regionId='999';
			countryId='';
		}
		else
		{
			if($("#country").val()=='-1')
			{
				partyId=$("#region").val();
				regionId=$("#region").val();
				countryId='';
			}
			else
			{
				partyId=$("#country").val();
				regionId=$("#region").val();
				countryId=$("#country").val();
			}
		}
		param.partyId=partyId;
		param.regionId=regionId;
		param.countryId=countryId;
		$.post(baseUrl+"training/insertSummary.action",
		param,
		function(data){
		   if("success"==data.msg)
		   {
			  alert("success");
			  window.location = baseUrl + "training/loadSummaryListPage.action";
		   }
		   else{
			   alert(data.msg);
		   }
		},
		"json"
		); 
		/*var options={
			url:baseUrl+"training/CreateCourse.action",
			type:"POST",
			success:function(data)
			{
				if("success"==data.msg)
				   {
					  alert("success");
					  window.location = baseUrl + "training/loadCourseListPage.action";
				   }
				   else{
					   alert(data.msg);
				   }
			},
			dataType:"json"
		};
		
		$("#courseform").ajaxSubmit(options); */
	}); 
});


//加载消息栏目
function loadMessage(){
	initLevel1Type();
	$("#column").removeAttr("disabled");
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
	$("#column").append("<option value='0'>Please Select The Column</option>");
}

//初始化区域列表
function initRegionList()
{
	$("#region").empty();
	$("#region").append("<option value='0'>Please Select The Region</option>");
}
//初始化国家列表
function initCountryList()
{
	$("#country").empty();
	$("#country").append("<option value='0'>Please Select The Country</option>");
	$("#country").attr("disabled",true);
}

//加载区域
function loadSCPartyData()
{
	initRegionList();
	if(regionId!="999")
	{
		$("#region").append("<option value='"+regionId+"'>"+regionName+"</option>");
		$("#region").val(regionId);
		$("#region").attr("disabled",true);
		if(countryId!="999")
		{
			$("#country").append("<option value='"+countryId+"'>"+countryName+"</option>");
			$("#country").val(countryId);
			$("#country").attr("disabled",true);
		}
		return;
	}else{
		if(roleName.indexOf("EMSC")==0){
			$("#region").append("<option value='1'>"+roleName.substring(0,4)+"</option>");
			$("#region").val("1");
			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("BDSC")==0){
			$("#region").append("<option value='2'>"+roleName.substring(0,4)+"</option>");
			$("#region").val("2");
			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("NASC")==0){
			$("#region").append("<option value='3'>"+roleName.substring(0,4)+"</option>");
			$("#region").val("3");
			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("EUSC")==0){
			$("#region").append("<option value='14'>"+roleName.substring(0,4)+"</option>");
			$("#region").val("14");
			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("SRSC")==0){
			$("#region").append("<option value='15'>"+roleName.substring(0,4)+"</option>");
			$("#region").val("15");
			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}
	}
	$.ajax({
		url:baseUrl+"party/loadSCPartyData.action",
		type:"GET",
		success:function(data){
			$("#region").append("<option value='-1'>All</option>");
			$.each(data,function(n,value){
				$("#region").append("<option value='"+value.partyId+"'>"+value.partyName+"</option>");
			}); 
		},
		dataType:"json"
	});
}

//加载国家
function loadCountryListData()
{
	var regionId=$("#region").val();
	if(regionId=='0'||regionId=='-1')
	{
		return;
	}
	$("#country").removeAttr("disabled");
	var param={};
	param.partyId=regionId;
	$.ajax({
		url:baseUrl+"party/loadCountryData.action",
		type:"POST",
		data:param,
		success:function(data){
//			$("#country").append("<option value='-1'>All</option>");
			$.each(data,function(n,value){
				$("#country").append("<option value='"+value.partyId+"'>"+value.partyName+"</option>");
			});
		},
		dataType:"json"
	});
}

//验证信息
function validateData(){
	if($('#column').val()=='0'){
		alert("Please Select The Column!");
		$("#column").focus();
		return false;
	}
	
	//验证是否有选择区域
	if($("#region").val()=='0')
	{
		alert('Please Select The Region!');
		$("#region").focus();
		return false;
	}
	//验证是否有选择国家
	if($("#region").val()!="-1"&& $("#country").val()=='0')
	{
		alert('Please Select The Country!');
		$("#country").focus();
		return false;
	}
	//验证是否有选择角色
//	if($("#roleTypeName").val()==""){
//		alert("Please Select RoleType!");
//		$("#roleTypeName").focus();
//		return false;
//	}
		
	var selectRight = $('#selectRight option');
	var userLoginId="";
	for(var i=0;i<selectRight.length;i++)
	{
		 userLoginId = selectRight[i].innerText;
	}
		if(userLoginId==""){
			alert("Please Select Accepted User to Right!");
			$("#selectRight").focus();
			return false;
		}	
	
	//验证Title是否有填写
	if($.trim($("#title").val())=='')
	{
		alert('Please Input The Title!');
		$("#title").focus();
		return false;
	}
	//验证封面图片是否有上传
	if($("#hidcover").val()=='')
	{
		alert('Please Select The Cover And Click The Upload!');
		$("#coverimg").focus();
		return false;
	}
	//验证封面图片
	if(!validateCoverImg())
	{
		$("#coverimg").focus();
		return false;
	}
	//验证是否有填写摘要
	if($.trim($("#summary").val())=='')
	{
		alert('Please Input The Digest!');
		$("#summary").focus();
		return false;
	}
	//验证是否有填写内容
	
	if($.trim(ue.getContent())=='')
	{
		alert('Please Enter The Content!');
		$("#editor").focus();
		return false;
	}
	return true;
}

//根据选择角色或国家显示用户
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

//验证上传封面
function validateCoverImg()
{
	var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
	var target=document.getElementById("coverimg");
	var fileSize=0;
	var fileMaxSize=1024*1024*2;
	var filepath=$("#coverimg").val();
	var extStart=filepath.lastIndexOf(".");
	var ext=filepath.substring(extStart,filepath.length).toUpperCase();
	if(ext!=".GIF" && ext !=".JPG" && ext!=".JPEG" && ext!=".PNG" && ext !=".BMP"){
		alert("Only One Of The Following File Formats Is Supported!");
		return false;
	}
	if(isIE&&!target.files){
		var fileSystem=new ActiveXObject("Scripting.FileSystemObject"); 
		if(!fileSystem.FileExists(filepath)){ 
			alert("the image doesn't exist,please upload again!"); 
			return false; 
		} 
		fileSize=fileSystem.GetFile(filepath).Size;
	}
	else
	{
		fileSize=target.files[0].size;
	}
	if(fileSize>fileMaxSize)
	{
		alert("the cover have a maximum size of 2M!");
		return false;
	}
	return true;
}


//字符串转换为十六进制
//function compileStr(code){          
//	if(str === "")
//		return "";
//	var hexCharCode = [];
//	hexCharCode.push("0x");
//	for(var i = 0; i < str.length; i++) {
//		hexCharCode.push((str.charCodeAt(i)).toString(16));
//		}
//	return hexCharCode.join("");
//	 
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


function selectToSelectAll(ele1,ele2){//select全部追加
	var selFrom=document.getElementById(ele1);
	var selTarget=document.getElementById(ele2);
	var optionArr=selFrom.children;
	for (var i=optionArr.length-1;i>=0;i--) {
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