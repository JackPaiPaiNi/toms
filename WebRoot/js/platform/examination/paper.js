$(function(){
	$("#productListTable").datagrid({
		url:baseUrl + 'examination/selectPaperData.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	        {field:'headline',title:locale("paper.list.attr.headline"),width:100},
	        {field:'categories',title:locale("topic.alert.topic.one"),width:80},
	        {field:'mediums',title:locale("topic.alert.topic.two"),width:80},
	        {field:'smaClass',title:locale("topic.alert.topic.three"),width:80},
	        {field:'testTime',title:locale("paper.list.attr.time"),width:80},
	        {field:'sTime',title:locale("paper.list.attr.startTime"),width:70},
	        {field:'eTime',title:locale("paper.list.attr.endTime"),width:70},
	        {field:'QRCode',title:locale("paper.list.attr.qrcode"),width:125,formatter:qrCodeFormatter},
	        {field:'userName',title:locale("user.list.gridhead.createBy"),width:70},
	        {field:'createTime',title:locale("summary.createDate"),width:70},
	        {field:'partyName',title:locale("user.list.gridhead.partyName"),width:70},
	    ]],
	    toolbar:[{
	    	text:jsLocale.get('toolbar.exam.SQAdd'),
            iconCls:'icon-add',
            handler:function(){
            	showAddWin();
            }
        },'-',{  
            text:jsLocale.get('toolbar.exam.RQAdd'),  
            iconCls:'icon-add',  
            handler:function(){
            	showManualAddWin();
            }
        },'-',{  
            text:jsLocale.get('toolbar.delete'),  
            iconCls:'icon-remove',  
            handler:function(){
            	if(isSelectEd()){
            		onDelete();
            	};
            }
        },'-',{  
            text:jsLocale.get('toolbar.edit'),  
            iconCls:'icon-edit',  
            handler:function(){
            	if(isSelectEd()){
            		onEdit();
            	};
            }
        },'-',{  
            text:jsLocale.get('toolbar.editAee'),  
            iconCls:'icon-edit',  
            handler:function(){
            	if(isSelectEd()){
            		onEditDate();
            	};
            }
        },'-',{  
            text:jsLocale.get('toolbar.link'),  
            iconCls:'icon-large-line',  
            handler:function(){
            	linkGeneration();
            }
        },'-',{  
            text:jsLocale.get('paper.list.topic.preview'),  
            iconCls:'icon-search',  
            handler:function(){
            	if(isSelectEd()){
            		onDescriptionView();
            	};
            }
        }],
        onLoadSuccess: function (data) {
        	$('#searchBt').linkbutton({disabled:false}); 
    	}
	});
	initWindow();
});

function initWindow(){
	loadBrHeading();
	loadBrHeadingm();
	userMCSelect();
	examSelect();
	queryUserByCountyAndRole();
	clickLoadExamShow();
	examShowEmpty();
	loadCountry();
	selectToSelect();
};
function selectToSelect(){
	$('#toRightAll').click(function(){
		selectToSelectAll('selectLeft','selectRight');
	})	
	/*$('#toLeftAll').click(function(){
		
		selectToSelectAll('selectRight','selectLeft');
	})*/
	$("#toLeftAll").click(function(){
		//储存选中用户
		var userArr = [];
		$("#selectRight option").each(function(){
			userArr.push({"userId":$(this).val(),"userName":$(this).html()});
		});
		
		var id=$('#isUdpateAd').val();
		if($.trim(id)!="add"){
			
			//选中用户是否已经考试,如果已经参加考试,则不能移动
			$.ajax({
				url:baseUrl + "examination/selectUserIsExam.action",
				type:"POST",
				data:{"userId":JSON.stringify(userArr),"paperId":$('#historyId').val()},
				success:function(data){
					var result = eval('('+data+')');
					if(result != null){
						if(result.rows.length > 0){//result.rows[0].userName
							var users = "";
							$(result.rows).each(function(){
								users += (this.userName + ",");
							});
							showMsg(users + " " + locale("topic.alert.remove.user"));
						}else{
							selectToSelectAll('selectRight','selectLeft');
						}
					}
				}
			});
			
		}else{
			selectToSelectAll('selectRight','selectLeft');
		}
	
	});
	$('#toRight1All').click(function(){
		selectToSelectAll('selectLeft1','selectRight1');
	})	
	$('#toLeft1All').click(function(){
		selectToSelectAll('selectRight1','selectLeft1');
	})
	$('#toRight2All').click(function(){
		selectToSelectAll('selectLeft2','selectRight2');
	})	
	$('#toLeft2All').click(function(){
		selectToSelectAll('selectRight2','selectLeft2');
	})
	$('#toRight3All').click(function(){
		selectToSelectAll('selectLeft3','selectRight3');
	})	
	$('#toLeft3All').click(function(){
		selectToSelectAll('selectRight3','selectLeft3');
	})
	$('#mtoRightAll').click(function(){
		selectToSelectAll('mselectLeft','mselectRight');
	})	
	$('#mtoLeftAll').click(function(){
		selectToSelectAll('mselectRight','mselectLeft');
	})
}
function examShowEmpty(){//改变类型清空显示题目
	$('#categoriesId,#mediumss,#smaClasss').change(function(){
		setLeftHtml("","","");
		setRightHtml("","","");
	});
};

function clickLoadExamShow(){//点击加载数据
	$('#QueryButton').click(function(){
		loadExamShow();
	});
};

function examSelect(){//题目选择
	/*单选题选择*/
	$("#toRight1").click(function(){
		$("#selectLeft1 option:selected").each(function(){
			$("#selectRight1").append("<option title='"+ $(this).html() +"' value='" + $(this).val() + "'>" + $(this).html() + "</option>");
			$(this).remove();
		});
	});
	$("#toLeft1").click(function(){
		$("#selectRight1 option:selected").each(function(){
			$("#selectLeft1").append("<option title='"+ $(this).html() +"' value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
			$(this).remove();
		});
	});
	
	/*多选题选择*/
	$("#toRight2").click(function(){
		$("#selectLeft2 option:selected").each(function(){
			$("#selectRight2").append("<option title='"+ $(this).html() +"' value='" + $(this).val() + "'>" + $(this).html() + "</option>");
			$(this).remove();
		});
	});
	$("#toLeft2").click(function(){
		$("#selectRight2 option:selected").each(function(){
			$("#selectLeft2").append("<option title='"+ $(this).html() +"' value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
			$(this).remove();
		});
	});
	
	//判断题选择
	$("#toRight3").click(function(){
		$("#selectLeft3 option:selected").each(function(){
			$("#selectRight3").append("<option title='"+ $(this).html() +"' value='" + $(this).val() + "'>" + $(this).html() + "</option>");
			$(this).remove();
		});
	});
	$("#toLeft3").click(function(){
		$("#selectRight3 option:selected").each(function(){
			$("#selectLeft3").append("<option title='"+ $(this).html() +"' value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
			$(this).remove();
		});
	});
};

function queryUserByCountyAndRole(){//根据国家or角色加载用户
	
	$("#countryId").change(function(){
		if($(this).val()!=''){
			$("#selectLeft").empty();
			getUserByRoleName($("#roleTypeName").val(),$("#countryId").val());
		}
	});
	
	$("#roleTypeName").change(function(){
		if($(this).val()!=''){
			$("#selectLeft").empty();
			getUserByRoleName($(this).val(),$("#countryId").val());
		}else if($(this).val()==""){
			$("#selectLeft").empty();
			getUserByRoleName("",$("#countryId").val());
		}
	});
	
	$("#mcountryId").change(function(){
		if($(this).val()!=''){
			$("#mselectLeft").empty();
			getmUserByRoleName($("#mroleTypeName").val(),$("#mcountryId").val());
		}
	});
	
	$("#mroleTypeName").change(function(){
		if($(this).val()!==""){
			$("#mselectLeft").empty();
			getmUserByRoleName($(this).val(),$("#mcountryId").val());
		}else if($(this).val()==""){
			$("#mselectLeft").empty();
			getmUserByRoleName("",$("#mcountryId").val());
		}
	});
};

/**
 * 移除右边用户
 */
function removeDexter(){
	$("#selectRight option:selected").each(function(){
		$("#selectLeft").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
		$(this).remove();
	});
};

function userMCSelect(){/*用户选择*/
	$("#toRight").click(function(){
		$("#selectLeft option:selected").each(function(){
			$("#selectRight").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");
			$(this).remove();
		});
	});
	
	$("#toLeft").click(function(){
		//储存选中用户
		var userArr = [];
		$("#selectRight option:selected").each(function(){
			userArr.push({"userId":$(this).val(),"userName":$(this).html()});
		});
		
		var id=$('#isUdpateAd').val();
		if($.trim(id)!="add"){
			
			//选中用户是否已经考试,如果已经参加考试,则不能移动
			$.ajax({
				url:baseUrl + "examination/selectUserIsExam.action",
				type:"POST",
				data:{"userId":JSON.stringify(userArr),"paperId":$('#historyId').val()},
				success:function(data){
					var result = eval('('+data+')');
					if(result != null){
						if(result.rows.length > 0){//result.rows[0].userName
							var users = "";
							$(result.rows).each(function(){
								users += (this.userName + ",");
							});
							showMsg(users + " " + locale("topic.alert.remove.user"));
						}else{
							removeDexter();
						}
					}
				}
			});
			
		}else{
			removeDexter();
		}
	
	});
	/*手动增加*/
	$("#mtoRight").click(function(){
		$("#mselectLeft option:selected").each(function(){
			$("#mselectRight").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");
			$(this).remove();
		});
	});
	$("#mtoLeft").click(function(){
		$("#mselectRight option:selected").each(function(){
			$("#mselectLeft").append("<option value='" + $(this).val() + "'>" + $(this).html() + "</option>");//这个方法是默认在后面添加
			$(this).remove();
		});
	});
};

function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}


function initEditor(){
	//自定义按钮
	var _tools = "Cut,Copy,Paste,Pastetext,|,Blocktag,Fontface,FontSize,Bold,Italic,Underline," +
			"Strikethrough,FontColor,BackColor,SelectAll,Removeformat,|,Align,List,Outdent," +
			"Indent,|,Hr,Emot,Table,Source";
	$("#description").xheditor({tools:_tools});
	$("#descriptionView").xheditor({tools:_tools});
}

function loadBrHeading(){//加载大类
	var optionStr = "<option value=''></option>";
	$.ajax({
		url:baseUrl + "examination/selectSubclassCategoriesById.action?id=0",
		type:"POST",
		success:function(data){
			var result = eval('('+data+')');
			$(result).each(function(){
				optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
			});
			$('#categoriesId').html(optionStr);
			$('#categoriesId').change(function(){
				loadMinoDefect($(this).val() == "" ? -1 : $(this).val());
			});
		}
	});
};

function loadMinoDefect(id){//中类
	var optionStr = "<option value=''></option>";
	$.ajax({
		url:baseUrl + "examination/selectSubclassCategoriesById.action",
		type:"POST",
		data:{"id":id},
		success:function(data){
			var result = eval('('+data+')');
			$(result).each(function(){
				optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
			});
			$('#mediumss').html(optionStr);
			$('#mediumss').change(function(){
				loadSmaClass($(this).val() == "" ? -1 : $(this).val());
			});
		}
	});
};

function loadSmaClass(id){//小类
	var optionStr = "<option value=''></option>";
	$.ajax({
		url:baseUrl + "examination/selectSubclassCategoriesById.action",
		type:"POST",
		data:{"id":id},
		success:function(data){
			var result = eval('('+data+')');
			$(result).each(function(){
				optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
			});
			$('#smaClasss').html(optionStr);
		}
	});
};

function showManualAddWin(){
	resetOper();
	$("#isUdpateAd").val('addManual');
	setUnReadOnly($('#addManualProductForm #productId'));
	$('#addManualProductForm #productId').textbox("enable");
	$('#addManualProductForm').form("reset");
	switchOnOff("addManual",$("#addManualProductForm"),$("#submitBtn"));
	$('#manualAddProductWin').window({title:locale("paper.list.add")}).window('center').window('open');
	$("#status").combobox('setValue',1);
	loadManualCountry();
	setRightHtml1('','');
	setInputReadOnly();
}

function showAddWin(){
	resetOper();
	$("#isUdpateAd").val('add');
	setUnReadOnly($('#addProductForm #productId'));
	$('#addProductForm #productId').textbox("enable");
	$('#addProductForm').form("reset");
	switchOnOff("add",$("#addProductForm"),$("#submitBtn"));
	$('#addProductWin').window({title:locale("paper.list.add")}).window('center').window('open');
	$("#status").combobox('setValue',1);
	Common_util.loadCountry();
	setLeftHtml("","","");
	setRightHtml("","","");
	setRightHtml2('','');
	setInputReadOnly();
}

function loadExamShow(){//题目显示
	setLeftHtml("","","");
	setRightHtml("","","");
	var o = {};
	o.countryId = $('#countryId').val();
	o.categories = $('#categoriesId').val();
	o.mediumsId = $('#mediumss').val();
	o.smaClassId = $('#smaClasss').val();
	
	$.ajax({
		url:baseUrl + "examination/selectExamByTypeAndCountry.action",
		type:"POST",
		data:o,
		success:function(data){//1、单选;2、多选;3、判断
			var result = eval('('+data+')');
			var radioOption = "";
			var multiOption = "";
			var judgeOption = "";
			$(result.rows).each(function(){
				var a = 0;
				if(this.cType == 1){
					radioOption += setOptionHtml(this.id,this.exQuestions);
				}else if(this.cType == 2){
					multiOption += setOptionHtml(this.id,this.exQuestions);
				}else{
					judgeOption += setOptionHtml(this.id,this.exQuestions);
				}
			});
			setLeftHtml(judgeOption,radioOption,multiOption);
		}
	});
};

function setLeftHtml(judgeOption,radioOption,multiOption){
	$('#selectLeft1').html(judgeOption);
	$('#selectLeft2').html(radioOption);
	$('#selectLeft3').html(multiOption);
};
function setRightHtml(judgeOption,radioOption,multiOption){
	$('#selectRight1').html(judgeOption);
	$('#selectRight2').html(radioOption);
	$('#selectRight3').html(multiOption);
};

function setRightHtml1(left,right){
	$('#mselectLeft').html(left);
	$('#mselectRight').html(right);
};

function setRightHtml2(left,right){
	$('#selectLeft').html(left);
	$('#selectRight').html(right);
};

function setOptionHtml(val,htmlVal){
	return "<option value="+ val +" title='"+ htmlVal +"'>"+ htmlVal +"</option>";
};

function validate(){
	return $("#addProductForm").form("validate");
}

function resetOper(){//重置
	$('#mediumss').html("");
	$('#smaClasss').html("");
	$("#selectLeft").html("");
	$("#selectRight").html("");
}

function verifayBrHead(){//大类不能为空
	if(Common_util.isStringNullAvaliable($('#categoriesId').val())){
		return true;
	};
	showMsg(locale("topic.alert.broad.notnull"));
	return false;
}

function verifyAll(){
	return (verifayBrHead() && verifySTime() && verifyETime() && verifyTestTime()&& verifySTimeSmallETime() && verifyHead())? true : false;
}

function verifyHead(){//标题不能为空
	if(!Common_util.isStringNullAvaliable($('#headline').val())){
		showMsg(locale("topic.alert.headline.notnull"));
		return false;
	}
	return true;
};

function verifySTime(){//开始时间不能为空
	if(!Common_util.isStringNullAvaliable($("#sTime").datetimebox("getValue"))){
		showMsg(locale("topic.alert.sTime.notnull"));
		return false;
	}
	
	if($("#sTime").datetimebox("getValue") < new Date().Format('yyyy-MM-dd')){
		showMsg(locale("topic.alert.start.time"));
		return false;
	}
	return true;
}

function verifyETime(){//结束时间不能为空
	if(!Common_util.isStringNullAvaliable($("#eTime").datetimebox("getValue"))){
		showMsg(locale("topic.alert.eTime.notnull"));
		return false;
	}
	
	if($("#eTime").datetimebox("getValue") < new Date().Format('yyyy-MM-dd')){
		showMsg(locale("topic.alert.end.time"));
		return false;
	}
	return true;
}

function verifySTimeSmallETime(){//考试时间不能大于结束时间
	var isTrue =  $("#sTime").datetimebox("getValue") > $("#eTime").datetimebox("getValue");
	if(isTrue){
		showMsg(locale("topic.alert.Time.error"));
		return false;
	}
	return true;
}

/**
 * 旋转图和按钮同时控制
 * @param is
 */
function hidesAndSaveInvalid(is,name){
	if(is){
		loadingShowHides(false);//false 旋转图标显示
		buttonClickInvalid(name,true);//true:保存按钮失效
	}else{
		loadingShowHides(true);//true 隐藏旋转图标显示
		buttonClickInvalid(name,false);//false:保存按生效
	}
};

function verifyBrHead(){//题目类型不能全部为空、且输入值必须为正整数
	hidesAndSaveInvalid(true,'randomSub');//旋转图显示，提交按钮失效
	
	var id=$('#isUdpateAd').val();
	if($.trim(id) == "add"){
		var judNum = $('#selectRight1 option').length;
		var sinNum = $('#selectRight2 option').length;
		var muiUum = $('#selectRight3 option').length;
		var num = judNum + sinNum + muiUum;
		if(num == 0){
			hidesAndSaveInvalid(false,'randomSub');
			showMsg(locale("topic.alert.topType.notnull"));
			return false;
		}
		submitForm();
	}else{
		/**
		 * 修改之前查看试卷是否已经开始考试
		 */
		var o = {};
		o.pId = $('#historyId').val();
		o.start = new Date().Format('yyyy-MM-dd');
		$.ajax({
			url:baseUrl+"examination/isSpecifiedTime.action",
			type:"POST",
			data:o,
			success:function(data){
				var result = eval('('+data+')');
				if(result.success <= 0){
					submitForm();
				}else {
					submitFormByEndDate();
				}
			}
		});
	}
}

function submitFormByEndDate(){
	var staTime = $("#sTime").datetimebox("getValue")
	if(staTime <= (new Date().Format("yyyy-mm-dd"))){
		if(verifyETime() && verifySTimeSmallETime()){
			udpate();
		}else{
			hidesAndSaveInvalid(false,'randomSub');//旋转图隐藏，提交按钮生效
		}
	}
};

function verifyTestTime(){//考试时间为正整数 
	if(!Common_util.isStringNullAvaliable($('#testTime').val())){
		showMsg(locale("topic.alert.tTime.notnull"));
		return false;
	}
	
	if($('#testTime').val() > 120){//考试最长时间为两个小时,以分钟计算=120;
		showMsg(locale("topic.alert.test.longestTime"));
		return false;
	}
	
	if(!Common_util.isPositiveInteger($('#testTime').val()) || $('#testTime').val() == 0){
		showMsg(locale("topic.alert.testTime.integer"));
		return false;
	}
	return true;
}

//验证是否有选择角色
function verifyRole(){
	var selectRight = $('#selectRight option');
	var userLoginId="";
	for(var i=0;i<selectRight.length;i++)
	{
		 userLoginId = selectRight[i].innerText;
	}
	if($("#roleTypeName").val()=="0"){
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

function getExamIdArr(){
	var examIdStr = "";
	
	$('#selectRight1 option').each(function(){
		examIdStr += $(this).val()+";";
	});
	$('#selectRight2 option').each(function(){
		examIdStr += $(this).val()+";";
	});
	$('#selectRight3 option').each(function(){
		examIdStr += $(this).val()+";";
	});
	
	return Common_util.deletMantissa(examIdStr);
}

function udpate(){
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
			userArray[i] = {'userId':msgRoleId};
			
		}
		if(userArray.length!=0){
		for(var i=0;i<selectRight.length;i++)
		{
			var userLoginId = selectRight[i].innerText;
			allUserLoginArray[i] = {'userId':userLoginId};
		}
		$('#updateDate').val(new Date().Format('yyyy-MM-dd'));
		
		var o = {};
		o.param = JSON.stringify(userArray);
		o.userParam = JSON.stringify(allUserLoginArray);
		o.examIdStr = getExamIdArr();
	
		$('#addProductForm').ajaxSubmit({
			url:baseUrl + "examination/updatePaperData.action",
			data:o,
			type : 'post',
			success:function(data){
				hidesAndSaveInvalid(false,'randomSub');//旋转图隐藏，提交按钮生效
				concealLoading();
				var result = eval('('+data+')');
				if(result.success == true){
					$("#productListTable").datagrid('reload');
					$('#addProductWin').window('close');
					showMsg(locale("alert.success"));
				}else if(result.success == false){
					$("#productListTable").datagrid('reload');
					$('#addProductWin').window('close');
					showMsg(locale("topic.alert.paper.update.fals"));
				}else{
					concealLoading();
					if("TE_0001" == json.msg){
						showMsg(locale("product.alert.error_1"));
					}else{
						showMsg(locale("alert.error"));
					}
				}
				
				$('#productCatena').html('');
			}
		});
		
	}else{
		hidesAndSaveInvalid(false,'randomSub');//旋转图隐藏，提交按钮生效
		alert("At Least One User !");
		$("#selectRight").focus();
	}
};


function submitForm(){
	if(verifyAll()){
		//添加or编辑
		var id=$('#isUdpateAd').val();
		if($.trim(id)!="add"){
			udpate();
		}else{
			if(verifyRole()){
			var param = "";
			//获取右边框选中的所有用户
			var selectRight = $('#selectRight option');
			var userArray = [];
			var allUserStr = "";
			for(var i=0;i<selectRight.length;i++)
			{
				var userLoginId = selectRight.eq(i).attr("value");
				userArray[i] = {'userId':userLoginId};
			}
			
			var o = {};
			o.param = JSON.stringify(userArray);
			o.examIdStr = getExamIdArr();
			o.isAutomatic = 'true';
			o.createTime = new Date().Format('yyyy-MM-dd');
			
			$('#addProductForm').ajaxSubmit({
				url:baseUrl + "examination/insertPaperData.action",
				data:o,
				type : 'post',
				success:function(data){
					hidesAndSaveInvalid(false,'randomSub');//旋转图隐藏，提交按钮生效
					concealLoading();
					var json = eval('(' + data + ')');
					var falg = json.success;
					if(falg){
						showMsg(locale("alert.success"));
						$("#productListTable").datagrid('reload');
						$('#addProductWin').window('close');
					}else{
						concealLoading();
						if("TE_0001" == json.msg){
							showMsg(locale("product.alert.error_1"));
						}else{
							showMsg(locale("alert.error"));
							}
						}
					}
				});
			}else{
				hidesAndSaveInvalid(false,'randomSub');//旋转图隐藏，提交按钮生效
			}
		}
	}else{
		hidesAndSaveInvalid(false,'randomSub');//旋转图隐藏，提交按钮生效
	}
}

function clearForm(){
	$('#addProductWin').window('close');
}
function clearFormView(){
	$('#xhEditorView').window('close');
}
function clearFormView_code(){
	$('#xhEditorView_code').window('close');
}
function doSearch(){
	$("#searchBt").linkbutton("disable");
	$("#productListTable").datagrid({
		queryParams:{
			keyword:$("#qryName").val(),
			countryId:$("#countryIds").val()
		}
	});
}

//结束考试
function onEditDate(index){
	//var row=$("#productListTable").datagrid('getRows')[index];
	var row = $("#productListTable").datagrid('getSelections')[0];
	if(row.sTime > new Date().Format('yyyy-MM-dd')){
		showMsg(locale("topic.alert.paper.update.testSta"));
	}else{
		$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.finish.exem"),function(r){
		    if (r){
		    	var o = {};
		    	o.id = row.id;
		    	o.eTime = new Date().Format('yyyy-MM-dd');
		    	$.ajax({
		    		url:baseUrl+"examination/updateEndTime.action",
		    		type:"POST",
		    		data:o,
		    		success:function(data){
		    			var result = eval('('+data+')');
		    			if(result.success == true){
		    				showMsg(locale("alert.success"));
		    				$("#productListTable").datagrid('reload');
		    			}else if(result.success == false){
		    				showMsg(locale("topic.alert.paper.update.testEnd"));
		    				$("#productListTable").datagrid('reload');
		    			}else{
		    				showMsg(result.msg);
		    			}
		    		}
		    	});
		    }
		});
	}
}

//显示编辑窗口
function onEdit(index){
	resetOper();
	$("#isUdpateAd").val('update');
	$('#addProductForm').form("reset");
	//获取并填充数据
	var row = $("#productListTable").datagrid('getSelections')[0];
	
	$('#historyEndTime').val(row.eTime);
	
	row.categories = row.categoriesId;
	$('#historyId').val(row.id);
	$('#addProductForm').form('load',row);
	
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
	
	switchOnOff("edit",$("#addProductForm"),$("#submitBtn"));
	setReadOnly($('#addProductForm #productId'));
	$('#addProductForm #productId').textbox("disable");
	//显示窗口
	$('#addProductWin').window({title:locale("paper.list.edit")});
	$('#addProductWin').window('center');
	$('#addProductWin').window('open');
	Common_util.loadCountry(row.countryId);
	loadMinoDefectByONEdit(row.categoriesId,row.mediumsId,row.smaClassId);
	examEditShwo(row.id,row.categoriesId,row.mediumsId,row.smaClassId,row.countryId);
}

function examEditShwo(paperId,categoriesId,mediumsId,smaClassId,countryId){
	setLeftHtml("","","");
	setRightHtml("","","");
	var o = {};
	o.countryId = countryId;
	o.categoriesId = categoriesId;
	o.mediumss = mediumsId;
	o.smaClasss = smaClassId;
	o.paperId = paperId;
	
	$.ajax({
		url:baseUrl + "examination/selectExamSelectedInfo.action",
		type:"POST",
		data:o,
		success:function(data){//1、单选;2、多选;3、判断
			var result = eval('('+data+')');
			var rowsSele = result.rowsSele;
			var rowsFrus = result.rowsFrus;
			
			var radioOption = "";
			var multiOption = "";
			var judgeOption = "";
			$(rowsSele).each(function(){
				var a = 0;
				if(this.cType == 1){
					radioOption += setOptionHtml(this.id,this.exQuestions);
				}else if(this.cType == 2){
					multiOption += setOptionHtml(this.id,this.exQuestions);
				}else{
					judgeOption += setOptionHtml(this.id,this.exQuestions);
				}
			});
			setRightHtml(judgeOption,radioOption,multiOption);
			
			radioOption = "";
			multiOption = "";
			judgeOption = "";
			$(rowsFrus).each(function(){
				var a = 0;
				if(this.cType == 1){
					radioOption += setOptionHtml(this.id,this.exQuestions);
				}else if(this.cType == 2){
					multiOption += setOptionHtml(this.id,this.exQuestions);
				}else{
					judgeOption += setOptionHtml(this.id,this.exQuestions);
				}
			});
			setLeftHtml(judgeOption,radioOption,multiOption);
		}
	});
};

function loadMinoDefectByONEdit(maxId,medId,minId){//中类
	var optionStr = "<option value=''></option>";
	$.ajax({
		url:baseUrl + "examination/selectSubclassCategoriesById.action",
		type:"POST",
		data:{"id":maxId},
		success:function(data){
			var result = eval('('+data+')');
			
			if(Common_util.isStringNullAvaliable(medId)){
				$(result).each(function(){
					if(medId == this.id){
						optionStr += "<option selected='selected' value='"+this.id+"'>"+this.names+"</option>";
					}else{
						optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
					}
				});
				
				loadSmaClassONEdit(medId,minId);
			}else{
				$(result).each(function(){
					optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
				});
			}
			$('#mediumss').html(optionStr);
			$('#mediumss').change(function(){
				loadSmaClass($(this).val() == "" ? -1 : $(this).val());
			});
		}
	});
};

function loadSmaClassONEdit(medId,minId){//小类
	var optionStr = "<option value=''></option>";
	$.ajax({
		url:baseUrl + "examination/selectSubclassCategoriesById.action",
		type:"POST",
		data:{"id":medId},
		success:function(data){
			var result = eval('('+data+')');
			
			if(Common_util.isStringNullAvaliable(minId)){
				$(result).each(function(){
					if(minId == this.id){
						optionStr += "<option selected='selected' value='"+this.id+"'>"+this.names+"</option>";
					}else{
						optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
					}
				});
			}else{
				$(result).each(function(){
					optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
				});
			}
			$('#smaClasss').html(optionStr);
		}
	});
};

//导入数据
function importProducts(){
	showImportWin(locale("product.import"),baseUrl + "platform/importProduct.action" + pType);
}

//删除
function onDelete(index){
	var row = $("#productListTable").datagrid('getSelections')[0];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "examination/deletePaperData.action",
				type:"POST",
				data:{"id":row.id,'currentTime':new Date().Format('yyyy-MM-dd')},
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#productListTable").datagrid('reload');
					}else if(result.success == false){
						showMsg(locale("topic.alert.paper.delete.fals"));
					}else{
						showMsg(result.msg);
					}
				}
			});
	    }
	});
}

function showLoading(){
	var url = baseUrl + "images/login/loading.gif";
	$("#loadGif").attr("src",url);
	$('#load-layout').show();
}

function concealLoading(){
	$('#load-layout').hide();
}

//查看描述
function onDescriptionView(index){
	$('#xhEditorFrom').form("reset");
	var row = $("#productListTable").datagrid('getSelections')[0];
	var preveiew = "";
	$(".preview_title").html(row.headline);
	var sin = "";
	var sinCount = 1;
	var min = "";
	var minCount = 1;
	var jud = "";
	var judCount = 1;
	$.ajax({
		url:baseUrl + "examination/selectTopicByPaperId.action",
		type:"POST",
		data:{"id":row.id},
		success:function(data){
			var result = eval('('+data+')');
			for(var i=0;i<result.length;i++){
				var arr = [result[i].alAnswersA,result[i].alAnswersB,result[i].alAnswersC,result[i].alAnswersD,result[i].alAnswersE,result[i].alAnswersF,result[i].alAnswersG];
				if(result[i].cType == 1){//单选
					sin += "<div><p style='white-space:inherit;word-wrap:break-word;word-break:break-all;'>" + (sinCount ++) +". " + result[i].exQuestions + "</p>"//组建题目
					for(var j=0;j<arr.length;j++){//组建答案
						if(Common_util.isStringNullAvaliable(arr[j])){
							sin += "<p style='white-space:inherit;word-wrap:break-word;word-break:break-all;'><input type='radio'/>" + arr[j] + "</p>"
						}
					}
					sin += "</div>"
				}else if(result[i].cType == 2){//多选
					min += "<div><p style='white-space:inherit;word-wrap:break-word;word-break:break-all;'>" + (minCount ++) +". " + result[i].exQuestions + "</p>"//组建题目
					for(var j=0;j<arr.length;j++){//组建答案
						if(Common_util.isStringNullAvaliable(arr[j])){
							min += "<p style='white-space:inherit;word-wrap:break-word;word-break:break-all;'><input type='radio'/>" + arr[j] + "</p>"
						}
					}
					min += "</div>"
				}else{//判断
					jud += "<div><p style='white-space:inherit;word-wrap:break-word;word-break:break-all;'>" + (judCount ++) +". " + result[i].exQuestions + "</p>"//组建题目
					for(var j=0;j<arr.length;j++){//组建答案
						if(Common_util.isStringNullAvaliable(arr[j])){
							jud += "<p style='white-space:inherit;word-wrap:break-word;word-break:break-all;'><input type='radio'/>" + arr[j] + "</p>"
						}
					}
					jud += "</div>"
				}
			}
			
			var num = 1;
			if(sin != null && sin != ''){
				$("#sin").html(sin);
				$("#sin").parent('div').show();
				$('#sin_nub').html(getArabMaxNumber(num));
				num ++;
			}else{
				$("#sin").html("");
				$("#sin").parent('div').hide();
			}
			
			if(min != null && min != ''){
				$("#min").html(min);
				$("#min").parent('div').show();
				$('#min_nub').html(getArabMaxNumber(num));
				num ++;
			}else{
				$("#min").html("");
				$("#min").parent('div').hide();
			}
			
			if(jud != null &&  jud != ''){
				$("#jud").html(jud);
				$("#jud").parent('div').show();
				$('#jud_nub').html(getArabMaxNumber(num));
				num ++;
			}else{
				$("#jud").html("");
				$("#jud").parent('div').hide();
			}
			
			$('#xhEditorView').window({title:locale("product.window.descriptionView")});
			$('#xhEditorView').window('center');
			$('#xhEditorView').window('open');
		}
	});
}

/**
 * get阿拉伯数字
 * @param n
 * @returns
 */
function getArabMaxNumber (n){
	var nArr;
	if('en' == language){
		nArr = ['I. ','II. ','III. ','IV. ','V. ']
	}else{
		nArr = ['一. ','二. ','三. ','四. ','五. ']
	}
	return nArr[n - 1]
};

function switchOnOff(switchSign,$form,$subBtn){
	if("view" == switchSign){
		$subBtn.hide();
		$form.find('input,select').attr('readonly',true);
		$form.find('input,select').attr('disabled',true); 	
		$("#editorControlName").hide();
		$("#editorControl").hide();
		$form.find("input[editable='false']").each(function(){
	    	$(this).combobox("disable");
		});
	}else{
		$subBtn.show();
		$form.find('input,select').attr('readonly',false);
		$form.find('input,select').attr('disabled',false);
		$("#editorControlName").show();
		$("#editorControl").show();
		$form.find("input[editable='false']").each(function(){
	    	$(this).combobox("enable");
		});
	}
}

//根据角色和国家获取用户名
function getmUserByRoleName(roleName,country){
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
//					if($("#mselectRight").find("option").text().indexOf(userName)==-1){
					if($("#mselectRight").find("option").attr('value')  != userLoginId){
						_optStr+="<option value='"+userLoginId+"'>"+userName+"</option>";
					}
				}
			}
			$("#mselectLeft").html(_optStr);
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

function linkGeneration(){//二维码链接生成
	var selectedRow = $("#productListTable").datagrid('getSelections');
	if(selectedRow.length == 0){
		showMsg(locale("topic.alert.paper.LinksToGenerate"));
		return;
	};
	//操作显示连接
	$(".preview_title_code").html(selectedRow[0].headline);
	
	$("#url").html(selectedRow[0].codeUrl);
	
	$('#xhEditorView_code').window({title:locale("product.window.descriptionView")});
	$('#xhEditorView_code').window('center');
	$('#xhEditorView_code').window('open');
}

//图片添加路径  
function qrCodeFormatter(value,row,index){
     if('' != value && null != value){  
    var strs = new Array(); //定义一数组   
    if(value.substr(value.length-1,1)==","){  
        value=value.substr(0,value.length-1);  
    }  
        strs = value.split(","); //字符分割   
  var rvalue ="";            
    for (i=0;i<strs.length ;i++ ){   
        rvalue += "<img onclick=download(\""+strs[i]+"\") style='width:120px; height:60px;margin-left:3px;' src=" + strs[i] + " title='Please click to view the qr code image'/>";  
        }   
    return  rvalue;        
     }  
    }  


//这里需要自己定义一个div   来创建一个easyui的弹窗展示图片
function download(img){ 
    var simg =  img;  
    $('#dlg').dialog({  
        title: locale("toolbar.preview"),  
        width: 320,  
        height:350,  
        resizable:true,  
        closed: false,  
        cache: false,  
        modal: true  
    });  
    $("#simg").attr("src",simg);  
      
}   


//-------------------------手动创建试卷-------------------------

function clearFormm(){
	$('#manualAddProductWin').window('close');
}

function verifyBrHeadm(){//题目类型不能全部为空、且输入值必须为正整数
	hidesAndSaveInvalid(true,'manualSub');//旋转图显示，提交按钮失效
	var judNum = $('#mjudNum').val();
	var sinNum = $('#msinNum').val();
	var muiUum = $('#mmuiUum').val();
	if(!Common_util.isStringNullAvaliable(judNum) &&
			!Common_util.isStringNullAvaliable(sinNum) &&
				!Common_util.isStringNullAvaliable(muiUum)){
		hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
		showMsg(locale("topic.alert.topType.notnull"));
		return false;
	}
	
	if(!Common_util.isStringNullAvaliable(judNum) &&
			!Common_util.isStringNullAvaliable(sinNum) &&
				!Common_util.isStringNullAvaliable(muiUum)){
		hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
		showMsg(locale("topic.alert.topType.notnull"));
		return false;
	}
	
	if(judNum == 0 && sinNum == 0 && muiUum == 0){
		hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
		showMsg(locale("topic.alert.topType.notnull"));
		return false;
	}
	
	judNum = judNum == '' ? 0 : judNum;
	sinNum = sinNum == '' ? 0 : sinNum;
	muiUum = muiUum == '' ? 0 : muiUum;
	
	if(!Common_util.isPositiveInteger(judNum) ||
			!Common_util.isPositiveInteger(sinNum) ||
				 !Common_util.isPositiveInteger(muiUum)){
		hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
		showMsg(locale("topic.alert.topType.integer"));
		return false;
	}
	verifyTopicjudNumm();
}

function verifyTopicjudNumm(){//判断题数量超出
	var judNum = $('#mjudNum').val() == '' ? 0 : $('#mjudNum').val();
	$.ajax({
		url:baseUrl + "examination/selectTopicTypeCount.action?countryId=" + $('#mcountryId').val(),
		type:"POST",
		data:{"type":3,'categoriesId':$('#mcategoriesId').val(),'mediumss':$('#mmediumss').val(),'smaClasss':$('#msmaClasss').val()},
		success:function(data){
			var result = eval('('+data+')');
			if(judNum > result[0]){
				hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
				showMsg(locale("topic.alert.judNum.beyond"));
				return false;
			}else{
				verifyTopicSinNumm();
			}
		}
	});
};

function verifyTopicSinNumm(){//单选题数量超出
	var sinNum = $('#msinNum').val() == '' ? 0 : $('#msinNum').val();
	$.ajax({
		url:baseUrl + "examination/selectTopicTypeCount.action?countryId=" + $('#mcountryId').val(),
		type:"POST",
		data:{"type":1,'categoriesId':$('#mcategoriesId').val(),'mediumss':$('#mmediumss').val(),'smaClasss':$('#msmaClasss').val()},
		success:function(data){
			var result = eval('('+data+')');
			if(sinNum > result[0]){
				hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
				showMsg(locale("topic.alert.sinNum.beyond"));
				return false;
			}else{
				verifyTopicMuiUumm();
			}
		}
	});
};

function verifyTopicMuiUumm(){//多选题数量超出
	var muiUum = $('#mmuiUum').val() == '' ? 0 : $('#mmuiUum').val();
	$.ajax({
		url:baseUrl + "examination/selectTopicTypeCount.action?countryId=" + $('#mcountryId').val(),
		type:"POST",
		data:{"type":2,'categoriesId':$('#mcategoriesId').val(),'mediumss':$('#mmediumss').val(),'smaClasss':$('#msmaClasss').val()},
		success:function(data){
			var result = eval('('+data+')');
			if(muiUum > result[0]){
				hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
				showMsg(locale("topic.alert.muiUum.beyond"));
				return false;
			}else{
				msubmitForm();
			}
		}
	});
};


function msubmitForm(){
	if(verifyAllm() && verifyRolem()){
		
	var param = "";
	//获取右边框选中的所有用户
	var selectRight = $('#mselectRight option');
	var userArray = [];
	var allUserStr = "";
	for(var i=0;i<selectRight.length;i++)
	{
		var userLoginId = selectRight.eq(i).attr("value");
		userArray[i] = {'userId':userLoginId};
	}
	
	var o = {};
	o.param = JSON.stringify(userArray);
	o.createTime = new Date().Format('yyyy-MM-dd');
	$('#addManualProductForm').ajaxSubmit({
		url:baseUrl + "examination/insertPaperData.action?param="+param,
		data:o,
		type : 'post',
		success:function(data){
			hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
			concealLoading();
			var json = eval('(' + data + ')');
			var falg = json.success;
			if(falg){
				showMsg(locale("alert.success"));
				$("#productListTable").datagrid('reload');
				$('#manualAddProductWin').window('close');
			}else{
				concealLoading();
				if("TE_0001" == json.msg){
					showMsg(locale("product.alert.error_1"));
				}else{
					showMsg(locale("alert.error"));
					}
				}
			}
		});
	}else{
		hidesAndSaveInvalid(false,'manualSub');//旋转图隐藏，提交按钮生效
	}
}

function verifyAllm(){
	return (verifayBrHeadm() && verifySTimem() && verifyETimem() && verifyTestTimem()&& verifySTimeSmallETimem() && verifyHeadm())? true : false;
}

function verifayBrHeadm(){//大类不能为空
	if(Common_util.isStringNullAvaliable($('#mcategoriesId').val())){
		return true;
	};
	showMsg(locale("topic.alert.broad.notnull"));
	return false;
}

function verifySTimem(){//开始时间不能为空
	if(!Common_util.isStringNullAvaliable($("#msTime").datetimebox("getValue"))){
		showMsg(locale("topic.alert.sTime.notnull"));
		return false;
	}
	
	if($("#msTime").datetimebox("getValue") < new Date().Format('yyyy-MM-dd')){
		showMsg(locale("topic.alert.start.time"));
		return false;
	}
	return true;
}

function verifyETimem(){//结束时间不能为空
	if(!Common_util.isStringNullAvaliable($("#meTime").datetimebox("getValue"))){
		showMsg(locale("topic.alert.eTime.notnull"));
		return false;
	}
	
	if($("#meTime").datetimebox("getValue") < new Date().Format('yyyy-MM-dd')){
		showMsg(locale("topic.alert.end.time"));
		return false;
	}
	return true;
}

function verifyTestTimem(){//考试时间为正整数 
	if(!Common_util.isStringNullAvaliable($('#mtestTime').val())){
		showMsg(locale("topic.alert.tTime.notnull"));
		return false;
	}
	
	if($('#mtestTime').val() > 120){//考试最长时间为两个小时,以分钟计算=120;
		showMsg(locale("topic.alert.test.longestTime"));
		return false;
	}
	
	if(!Common_util.isPositiveInteger($('#mtestTime').val()) || $('#mtestTime').val() == 0){
		showMsg(locale("topic.alert.testTime.integer"));
		return false;
	}
	return true;
}

function verifySTimeSmallETimem(){//考试时间不能大于结束时间
	var isTrue =  $("#msTime").datetimebox("getValue") > $("#meTime").datetimebox("getValue");
	if(isTrue){
		showMsg(locale("topic.alert.Time.error"));
		return false;
	}
	return true;
}

function verifyHeadm(){//标题不能为空
	if(!Common_util.isStringNullAvaliable($('#mheadline').val())){
		showMsg(locale("topic.alert.headline.notnull"));
		return false;
	}
	return true;
};

//验证是否有选择角色
function verifyRolem(){
	var selectRight = $('#mselectRight option');
	var userLoginId="";
	for(var i=0;i<selectRight.length;i++)
	{
		 userLoginId = selectRight[i].innerText;
	}
	if($("#mroleTypeName").val()=="0"){
		alert("Please Select RoleType!");
		$("#mroleTypeName").focus();
		return false;
	}else if(userLoginId==''){
		alert("Please Select Users from Left to Right!");
		$("#mselectRight").focus();
		return false;
	}
	return true;
}

function loadBrHeadingm(){//加载大类
	var optionStr = "<option value=''></option>";
	$.ajax({
		url:baseUrl + "examination/selectSubclassCategoriesById.action?id=0",
		type:"POST",
		success:function(data){
			var result = eval('('+data+')');
			$(result).each(function(){
				optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
			});
			$('#mcategoriesId').html(optionStr);
			$('#mcategoriesId').change(function(){
				loadMinoDefectm($(this).val() == "" ? -1 : $(this).val());
			});
		}
	});
};

function loadMinoDefectm(id){//中类
	var optionStr = "<option value=''></option>";
	$.ajax({
		url:baseUrl + "examination/selectSubclassCategoriesById.action",
		type:"POST",
		data:{"id":id},
		success:function(data){
			var result = eval('('+data+')');
			$(result).each(function(){
				optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
			});
			$('#mmediumss').html(optionStr);
			$('#mmediumss').change(function(){
				loadSmaClassm($(this).val() == "" ? -1 : $(this).val());
			});
		}
	});
};

function loadSmaClassm(id){//小类
	var optionStr = "<option value=''></option>";
	$.ajax({
		url:baseUrl + "examination/selectSubclassCategoriesById.action",
		type:"POST",
		data:{"id":id},
		success:function(data){
			var result = eval('('+data+')');
			$(result).each(function(){
				optionStr += "<option value='"+this.id+"'>"+this.names+"</option>";
			});
			$('#msmaClasss').html(optionStr);
		}
	});
};

function loadManualCountry(){
	$.ajax({
		url:baseUrl + "examination/selectCountry.action",
		type:"POST",
		success:function(data){
			var htmlOption = "";
			var result = eval('('+data+')');
			$(result).each(function(){
				htmlOption += "<option value='" + this.countryId + "'>" + this.countryName + "</option>";
			});
			$("#mcountryId").html(htmlOption);
		}
	});
}

/**
 * 时间选择只读
 */
function setInputReadOnly(){
	$(".datebox :text").attr("readonly","readonly");
};

/**
 * 是否选择操作数据
 */
function isSelectEd(){
	var selectedRow = $("#productListTable").datagrid('getSelections');
	if(selectedRow.length == 0){
		showMsg(locale("topic.alert.paper.LinksToGenerate"));
		return false;
	};
	return true;
};

/**
 * 加载国家
 * @param countrId
 */
function loadCountry (countrId){
	$.ajax({
		url:baseUrl + "examination/selectCountry.action",
		type:"POST",
		success:function(data){
			var htmlOption = (isHQRole == "true") ? "<option value=''></option>" : "";
			
			var result = eval('('+data+')');
			$(result).each(function(){
				if(countrId == this.countryId){
					htmlOption += "<option selected='selected' value='" + this.countryId + "'>" + this.countryName + "</option>";
				}else{
					htmlOption += "<option value='" + this.countryId + "'>" + this.countryName + "</option>";
				}
			});
			$("#countryIds").html(htmlOption);
		}
	});
};