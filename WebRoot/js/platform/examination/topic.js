/**
 * 考试题目管理
 * lilinlu
 */
$(function(){
	$("#productListTable").datagrid({
		title:locale("topic.list.title"),
		url:baseUrl + 'examination/selectExamQuestions.action',
		pagination:true,
		rownumbers:true,
		singleSelect:true,
		boder:false,
		iconCls:'icon-large-icons',
		fitColumns:true,
		fit:true,
	    columns:[[
	        { field:'ck',checkbox:true },
	        {field:'exQuestions',title:locale("topic.list.attr.topic"),width:100},
	        {field:'fractions',title:locale("topic.list.attr.score"),width:40},
	        {field:'countryName',title:locale("learn.country"),width:50},
	        {field:'exQuestionsTypeShow',title:locale("topic.list.attr.type"),width:50},
	        {field:'categories',title:locale("topic.alert.topic.one"),width:100},
	        {field:'mediums',title:locale("topic.alert.topic.two"),width:80},
	        {field:'smaClass',title:locale("topic.alert.topic.three"),width:95},
	        {field:'description',title:locale("toolbar.edit"),width:125,formatter:opFormatter_examination}
	    ]],
	    toolbar:'#producttb',
	    singleSelect: false,
		selectOnCheck: true,
		checkOnSelect: true,
		onHeaderContextMenu:onEasyGridHeadMenu,
		onLoadSuccess:enableBt
		
	});
	initWindow();
});

function discernType(){//类型选择判断题或者未选择类型时，备选答案不能增加。
	if($('#cType').val() != 3 && $('#cType').val() != ""){
		addAltAnswers();
		hideJudge(true);
	}
}

function controlTypeSelect(){//控制类型选择，当备选答案超出三个时，单选题禁止选择。
	var count = 0;
	$("[name='input']").each(function(){
		if($(this).css('display') != "none"){
			count ++;
		}
	});
	
	if(count > 2){
		hideJudge(true);
	}else{
		hideJudge(false);
	}
}

function hideJudge(isTrue){//控制判断题可选择情况
	if(isTrue){
		$("#cType option[value='3']").attr('disabled','disabled');
	}else{
		$("#cType option[value='3']").removeAttr('disabled');
	}
}

function hiddenAnswers(){
	var inputDemo = $("[id='noneInput']");
	var isTwoAlternatives = true;
	for(var i = (inputDemo.length -1); i>= 0 ;i--){
		 var isCheck = $($(inputDemo[i]).children('input')[0]).is(':checked');
		 var isinpValNull = Common_util.isStringNullAvaliable($($(inputDemo[i]).children('input')[1]).val());
		 if(isCheck || isinpValNull){
			 showMsg(locale("topic.alert.alteAnsw.hiden"));
			 isTwoAlternatives = false;
			 break;
		 }else if($(inputDemo[i]).css('display') != "none"){
			$(inputDemo[i]).hide();
			controlTypeSelect();
			isTwoAlternatives = false;
			break;
		}
	};
	if(isTwoAlternatives){
		showMsg(locale("topic.alert.alteAnsw.min"));
	}
};

function addAltAnswers(){
	var inputDemo = $("[id='noneInput']");
	for(var i =0;i<inputDemo.length;i++){
		if($(inputDemo[i]).css('display') == "none"){
			$(inputDemo[i]).show();
			break;
		}
	};
};

function spellTheAzswer(){
	var divAll = $("[name='input']");
	$("[name='input']").each(function(){
		if($(this).css('display') != "none"){
			var inpArr = $(this).children('input');
			$(inpArr[1]).val($(inpArr[0]).val() +". "+ $(inpArr[1]).val());
		}
	});
};

function alteAnswIsNotNull(){//备选答案不能为空
	var num = ['A. ','B. ','C. ','D. ','E. ','F. ','G. '];
	var divAll = $("[name='input']");
	for(var i = 0;i<divAll.length;i++){
		if($(divAll[i]).css('display') != "none"){
			var inpVal = $($(divAll[i]).children('input')[1]).val();
			if(!Common_util.isStringNullAvaliable(inpVal)){
				showMsg(locale("topic.alert.alteAnsw.notnull"));
				return false;
			}else{
				if(inpVal.indexOf(num[i]) != 0){
					$($(divAll[i]).children('input')[1]).val(num[i] + inpVal);
				}
			}
		}
	}
	return true;
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
function initWindow(){
	loadBrHeading(); 
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
			$('#categories').html(optionStr);
			$('#categories').change(function(){
				loadMinoDefect($(this).val() == "" ? -1 : $(this).val());//加载中类
				loadEliminateMinor();//清除小类
			});
		}
	});
};

/**
 * 清除小类内容
 */
function loadEliminateMinor(){
	$('#smaClasss').html("");
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

function showAddWin(){
	hideJudge(false);
	resetOper();
	inputTopic();
	$("#isUdpateAd").val('add');
	setUnReadOnly($('#addProductForm #productId'));
	$('#addProductForm #productId').textbox("enable");
	$('#addProductForm input[name=editId]').val("");
	$('#addProductForm').form("reset");
	$("#utr").hide();
	switchOnOff("add",$("#addProductForm"),$("#submitBtn"));
	$('#addProductWin').window({title:locale("topic.list.add")}).window('center').window('open');
	$("#status").combobox('setValue',1);
	Common_util.loadCountry();
};

function deletes (){
	var selectedRow = $("#productListTable").datagrid('getSelections');
	if(selectedRow.length == 0){
		showMsg(locale("topic.alert.deletes.error"));
		return;
	};
	
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm.selected"),function(r){
		if(r){
			var ids = "";
			$(selectedRow).each(function(){
				ids += this.id + ',';
			});
			$.ajax({
				url:baseUrl + "examination/onDeleteExamQuestions.action",
				type:"POST",
				data:{"ids":ids},
				success:function(data){
					var result = eval('('+data+')');
					if(result.success == 'true'){
						showMsg(locale("alert.success"));
						$("#productListTable").datagrid('reload');
					}else if(result.success == 'false'){
						showMsg(locale("alert.error.deleting"));
					}else{
						var linelength = result.success;
						showMsg(locale("topic.alert.topic.line") + (linelength.substr(0,(linelength.length-1))) + locale("topic.error.topic.line"));
						$("#productListTable").datagrid('reload');
					}
				}
			});
		}
	});
};

function inputTopic(){//选择框默认保留两个，其余影藏。
	$("[id='noneInput']").each(function(){
		$(this).hide();
	});
};

function validateType(){//题目类型
	if(!Common_util.isStringNullAvaliable($('#cType').val())){
		showMsg(locale("topic.alert.topic.notnull"));
		return false;
	}
	return true;
};

function validateScore(){//分值
	var patrn=/^[0-9]+$/;
	var inpValue = $('#fractions').val();
	if(inpValue.length >=6){
		showMsg(locale("topic.alert.fractions.long"));
		return false;
	}else if(!Common_util.isStringNullAvaliable(inpValue)){
		showMsg(locale("topic.alert.fractions.notnull"));
		return false;
	}else if(!patrn.test(inpValue) || inpValue == 0){
		showMsg(locale("topic.alert.fractions.integer"));
		return false;
	}
	return true;
};

function validateBroadHea(){//大类
	var inpuValue = $('#categories').val();
	if(!Common_util.isStringNullAvaliable(inpuValue)){
		showMsg(locale("topic.alert.broad.notnull"));
		return false;
	}
	return true;
};

function validateQuestions(){//主题
	if(!Common_util.isStringNullAvaliable($('#exQuestions').val())){
		showMsg(locale("topic.alert.topicHead.notnull"));
		return false;
	}
	return true;
};

function validateCorAnswerSelect(){//正确答案不能为空
	var selNum = 0;
	var divObj = $("[name='input']");
	for(var i=0;i<divObj.length;i++){
		if($($(divObj[i]).children('input')[0]).is(':checked')){
			selNum ++;
		}
	};
	if(selNum == 0){
		showMsg(locale("topic.list.attr.correctnessSel"));
		return false;
	}else{
		if(($('#cType').val() == 1 || $('#cType').val() == 3) && selNum != 1){
			showMsg(locale("topic.alert.alteAnsw.sole"));
			return false;
		}
		return true;
	}
};

function validate(){
	if(validateType() && validateScore() && validateBroadHea() && validateQuestions() && validateCorAnswerSelect() && alteAnswIsNotNull()){
		return true;
	}else{
		false;
	}
}

	function submitForm(){
		theCorrectAnswer();
		if(validate()){
			//添加or编辑
			var id=$('#isUdpateAd').val();
			if($.trim(id)!="add"){
				showLoading();
				//编辑
				$('#addProductForm').ajaxSubmit({
					url:baseUrl + "examination/updateExamQuestionsById.action",
					success:function(data){
						concealLoading();
						var result = eval('('+data+')');
						if(result.success){
							$("#productListTable").datagrid('reload');
							$('#addProductWin').window('close');
							showMsg(locale("alert.success"));
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
			showLoading();
			//spellTheAzswer();
			$('#addProductForm').ajaxSubmit({
				url:baseUrl + "examination/insertExamQuestions.action",
				success:function(data){
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
		}
	}
}
	
function theCorrectAnswer(){//操作正确答案
	$('#corAnswer').val("");
	var arr = $("form [type='checkbox']:checked");
	if(arr.length >= 2){
		var corAnswer = arr[1].value;
		for(var i = 2;i < arr.length;i ++){
			corAnswer += ('/'+ arr[i].value);
		}
		$('#corAnswer').val(corAnswer);
	}
}

function clearForm(){
	$('#addProductWin').window('close');
}
function clearFormView(){
	$('#xhEditorView').window('close');
}
function doSearch(){
	$("#searchBt").linkbutton("disable");
	
	var sel = '';
	if($("#sonId").val()!= '###' && $("#catId").val()!= '###'){
		sel = '12';
	}else if ($("#sonId").val() == '###' && $("#catId").val() == '###'){
		sel = '';
	}else if ($("#sonId").val()!= '###'){
		sel = $("#sonId").attr("sel");
	}else if ($("#catId").val() != '###'){
		sel = $("#catId").attr("sel");
	}
	
	$("#productListTable").datagrid({
		queryParams:{
			cType:$("#qryName").val()
		}
	});
}

//显示编辑窗口
function onEdit(index){
	hideJudge(false);
	resetOper();
	$("#isUdpateAd").val('update');
	$('#addProductForm').form("reset");
	//获取并填充数据
	var row=$("#productListTable").datagrid('getRows')[index];
	//$('#mediumss').html("<option  value='"+row.mediumsId+"'>"+row.mediums+"</option>");
	//$('#smaClasss').html("<option  value='"+row.smaClassId+"'>"+row.smaClass+"</option>");
	
	row.categories = row.categoriesId;
	editCorrectAnswer(row.id);
	editAlAnswers(row);
	$('#historyId').val(row.id);
	$('#addProductForm').form('load',row);
	switchOnOff("edit",$("#addProductForm"),$("#submitBtn"));
	setReadOnly($('#addProductForm #productId'));
	//显示窗口
	$('#addProductWin').window({title:locale("topic.list.edit")});
	$('#addProductWin').window('center');
	$('#addProductWin').window('open');
	
	controlTypeSelect();
	Common_util.loadCountry(row.countryId);
	loadMinoDefectByONEdit(row.categoriesId,row.mediumsId,row.smaClassId);
}

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

function resetOper(){//重置
	$("[name='input']").each(function(){
		$($(this).children('input')[0]).attr('checked',false);
	});
	$("[id='noneInput']").each(function(){
		$(this).hide();
	});
	$('#mediumss').html("");
	$('#smaClasss').html("");
}

function editAlAnswers(row){
	var num = 0;
	var arr = [row.alAnswersA,row.alAnswersB,row.alAnswersC,row.alAnswersD,row.alAnswersE,row.alAnswersF,row.alAnswersG];
	for(var i=0;i<arr.length;i++){
		if(Common_util.isStringNullAvaliable(arr[i])){
			num ++;
		}
	}
	
	for(var i=0;i<num - 2;i++){
		addAltAnswers();
	}
}

function editCorrectAnswer(id){
	$.ajax({
		url:baseUrl + "examination/selectCorrectAnswerById.action",
		type:"POST",
		data:{"id":id},
		success:function(data){
			var result = eval('('+data+')');
			var divAll = $("[name='input']");
			
			for(var i=0;i<divAll.length;i++){
				var inpArr = $(divAll[i]).children('input');
				for(var j=0;j<result.length;j++){
					if($(inpArr[0]).val() == result[j].corAnswer){
						$(inpArr[0]).prop("checked",true);
						break;
					}
				}
			}
		}
	});
};

//删除
function onDelete(index){
	var row = $("#productListTable").datagrid('getRows')[index];
	$.messager.confirm(locale("alert.delete.confirm.title"),locale("alert.delete.confirm"),function(r){
	    if (r){
			$.ajax({
				url:baseUrl + "examination/deleteExamQuestions.action",
				type:"POST",
				data:{"id":row.id},
				success:function(data){
					var result = eval('('+data+')');
					if(result.success){
						showMsg(locale("alert.success"));
						$("#productListTable").datagrid('reload');
					}else{
						showMsg(locale("alert.error.deleting"));
					}
				}
			});
	    }
	});
}

//查看备选答案
function onDescriptionView(index){
	setDescriptionViewString("","","");
	$('#xhEditorFrom').form("reset");
	var row=$("#productListTable").datagrid('getRows')[index];
	var arr = [row.alAnswersA,row.alAnswersB,row.alAnswersC,row.alAnswersD,row.alAnswersE,row.alAnswersF,row.alAnswersG];
	
	$.ajax({
		url:baseUrl + "examination/selectCorrectAnswerById.action",
		type:"POST",
		data:{"id":row.id},
		success:function(data){
			var correctAnswer = "";
			var wrongAnswer = "";
			var result = eval('('+data+')');
			for(var i = 0;i < arr.length;i ++){
				if(Common_util.isStringNullAvaliable(arr[i])){
					var alteNumb =  arr[i].substr(0,1).toUpperCase();
					var isExist = true;
					for(var j=0;j < result.length;j ++){
						var answerNumb =  result[j].corAnswer.toUpperCase();
						if(alteNumb == answerNumb){
							correctAnswer += "<p style='word-wrap: break-word!important;white-space: inherit!important;word-break: break-all!important;padding-left:20px;'>"+ arr[i] +"</p>"
							isExist = false;
							break;
						}
					}
					if(isExist){
						wrongAnswer += "<p style='word-wrap: break-word!important;white-space: inherit!important;word-break: break-all!important;padding-left:20px;'>"+ arr[i] +"</p>"
					}
				}
			}
			setDescriptionViewString(row.analysis,correctAnswer,wrongAnswer);
		}
	});
	
	$('#xhEditorView').window({title:locale("product.window.descriptionView")});
	$('#xhEditorView').window('center');
	$('#xhEditorView').window('open');
}

function setDescriptionViewString(analysis,correctAnswer,wrongAnswer){
	$('#analysisView').html(analysis);
	$('#correctAnswer').html(correctAnswer);
	$('#wrongAnswer').html(wrongAnswer);
};

function showLoading(){
	var url = baseUrl + "images/login/loading.gif";
	$("#loadGif").attr("src",url);
	$('#load-layout').show();
}

function concealLoading(){
	$('#load-layout').hide();
}

//导入数据
function importProducts(){
	showImportWin(locale("examination.questions.import"),baseUrl + "examination/importQuestion.action");
}

/*//查看
function onView(index){
	
	$('#addProductForm').form("reset");
	//获取并填充数据
	var row=$("#productListTable").datagrid('getRows')[index];
	row.acsize = row.size;
	row.gas = row.gasType;
	$('#addProductForm').form('load',row);
	var pdfHtml = "<a href='"+row.filePath+"' class='easyui-linkbutton' plain='true'>"+row.fileName+"</a>";
	
	$('#uploadExcel_product_name').html(pdfHtml);
	$('#addProductForm #productId').textbox("disable");
	$("#description").html(row.description);
	$("#utr").show();
	switchOnOff("view",$("#addProductForm"),$("#submitBtn"));
	//显示窗口
	$('#addProductWin').window({title:locale("product.window.view")});
	$('#addProductWin').window('center');
	$('#addProductWin').window('open');
}*/

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

function checkBoxChecked(){//复选框无法取消选中
	$("#checkedInput").prop("checked",true);
};
