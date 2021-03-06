/**
 * 学习情况统计
 */
$(function(){
	loadlevel1Type();
	initLevel2Type();
	initLevel3Type();
	loadSCPartyData();	
	//选择消息栏目
	
	$("#region").change(function(){
		if($("#region").val()=='')
		{
			initCountryList();
			return;
		}
		initCountryList();
		loadCountryListData();
	});
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
	
	if(countryId=="304"){
		$("#learnList").datagrid({
			title:jsLocale.get("test.title"),
			url:baseUrl+"examination/selectRusExaminationSituation.action",
			pagination:true,
			rownumbers:true,
			singleSelect:true,
			boder:false,
			iconCls:'icon-large-icons',
			fitColumns:true,
			fit:true,
			columns:[[
			          {field:'countryName',title:jsLocale.get("learn.country"),width:50},
			          {field:'userId',title:jsLocale.get("learn.userlogin"),width:30},
			          {field:'userName',title:jsLocale.get("learn.username"),width:40},
			          {field:'wechat',title:jsLocale.get("learn.userwc"),width:50},
			          {field:'customerName',title:jsLocale.get("learn.customerName"),width:50},
			          {field:'roleName',title:jsLocale.get("learn.rolename"),width:100},
			          {field:'workNum',title:jsLocale.get("learn.userworknum"),width:30},
			          {field:'subTimes',title:jsLocale.get("test.sub.visit"),width:80,formatter:todate},
			          {field:'title',title:jsLocale.get("test.coursetitle"),width:120},
			          {field:'score',title:jsLocale.get("learn.operationScore"),width:30},
			]],
			 toolbar:'#coursetb',
			 onHeaderContextMenu:onEasyGridHeadMenu,
			 onLoadSuccess:enableBt
		});
	}else{
		$("#learnList").datagrid({
			title:jsLocale.get("test.title"),
			url:baseUrl+"examination/selectExaminationSituation.action",
			pagination:true,
			rownumbers:true,
			singleSelect:true,
			boder:false,
			iconCls:'icon-large-icons',
			fitColumns:true,
			fit:true,
			columns:[[
			          {field:'countryName',title:jsLocale.get("learn.country"),width:50},
			          {field:'userId',title:jsLocale.get("learn.userlogin"),width:30},
			          {field:'userName',title:jsLocale.get("learn.username"),width:40},
			          {field:'wechat',title:jsLocale.get("learn.userwc"),width:50},
			          {field:'roleName',title:jsLocale.get("learn.rolename"),width:100},
			          {field:'workNum',title:jsLocale.get("learn.userworknum"),width:30},
			          {field:'subTimes',title:jsLocale.get("test.sub.visit"),width:80,formatter:todate},
			          {field:'title',title:jsLocale.get("test.coursetitle"),width:120},
			          {field:'score',title:jsLocale.get("learn.operationScore"),width:30},
			]],
			 toolbar:'#coursetb',
			 onHeaderContextMenu:onEasyGridHeadMenu,
			 onLoadSuccess:enableBt
		});
	}
});

//全局变量
obj = {
	search:function(){
		$('#learnList').datagrid("load",{
			searchMsg:$.trim($("#searchMsg").val()),
			selectQuertyPartyId:$.trim($('#selectQuertyPartyId').combobox('getValue'))
		});
		$('#learnList').datagrid('clearSelections');
	}
};

function enableBt(){
	$("#searchBt").linkbutton("enable");
	initDataGridCells();
}

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

function doSearch(){
	$("#searchBt").linkbutton("disable");
	var levelOne=$("#column").val();
	var levelTwo=$("#subcolumn").val();
	var levelThree=$("#subtocolumn").val();
	var region=$("#region").val();
	var country=$("#country").val();
	var startdate=$(".startdate").val();
	var enddate=$(".enddate").val();
	
	$("#learnList").datagrid({
		queryParams:{
			levelOneTypeId:levelOne,
			levelTwoTypeId:levelTwo,
			levelThreeTypeId:levelThree,
			region:region,
			country:country,
			startdate:startdate,
			enddate:enddate
		}
	});
}

//日期转换函数
var todate = function(value,row,index) 
{
	
	if(value == "")
	{
		return "";
	}
	var d = new Date(value);
	
	return d.format("yyyy-MM-dd hh:mm:ss");
};

//状态格式化
function stateFormatter(value,row,index){
	if(row.readState=='1'){
		return "<Span style='color:blue;'> Reading </Span>";
	}
}



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
			$("#region").append("<option value='999'>OBC</option>");
			$("#region").append("<option value='1'>"+roleName.substring(0,4)+"</option>");
//			$("#region").val("1");	
//			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("BDSC")==0){
			$("#region").append("<option value='999'>OBC</option>");
			$("#region").append("<option value='2'>"+roleName.substring(0,4)+"</option>");
//			$("#region").val("2");
//			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("NASC")==0){
			$("#region").append("<option value='999'>OBC</option>");
			$("#region").append("<option value='3'>"+roleName.substring(0,4)+"</option>");
//			$("#region").val("3");
//			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("EUSC")==0){
			$("#region").append("<option value='999'>OBC</option>");
			$("#region").append("<option value='14'>"+roleName.substring(0,4)+"</option>");
//			$("#region").val("14");
//			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else if(roleName.indexOf("SRSC")==0){
			$("#region").append("<option value='999'>OBC</option>");
			$("#region").append("<option value='15'>"+roleName.substring(0,4)+"</option>");
//			$("#region").val("15");
//			$("#region").attr("disabled",true);
			
			loadCountryListData();
		}else{
			$.ajax({
				url:baseUrl+"party/loadSCPartyData.action",
				type:"GET",
				success:function(data){
					/*$("#region").append("<option value='-1'>All</option>");*/
					$.each(data,function(n,value){
						$("#region").append("<option value='"+value.partyId+"'>"+value.partyName+"</option>");
					}); 
				},
				dataType:"json"
			});
		}
	}
	
}
function loadCountryListData()
{
	var regionId=$("#region").val();
	if(regionId==''||regionId=='-1')
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
			/*$("#country").append("<option value='-1'>All</option>");*/
			$.each(data,function(n,value){
				$("#country").append("<option value='"+value.partyId+"'>"+value.partyName+"</option>");
			});
		},
		dataType:"json"
	});
}
//初始化业务区域列表
function initRegionList()
{
	$("#region").empty();
	$("#region").append("<option value=''>Please Select The Region</option>");
}
//初始化国家列表
function initCountryList()
{
	$("#country").empty();
	$("#country").append("<option value=''>Please Select The Country</option>");
	$("#country").attr("disabled",true);
}

//导出学习统计
function exportPaperExcel(){
	var levelOneTypeId=$("#column").val();
	var levelTwoTypeId=$("#subcolumn").val();
	var levelThreeTypeId=$("#subtocolumn").val();
	var region=$("#region").val();
	var country=$("#country").val();
	var startdate=$(".startdate").val();
	var enddate=$(".enddate").val();
	
	var grid = $('#learnList');  
	var options = grid.datagrid('getPager').data("pagination").options;  
	var page = options.pageNumber; //目前显示页
	var rows = options.pageSize;//条数
	
	var url=baseUrl+"examination/exportExamTestExcel.action?levelOneTypeId="+levelOneTypeId+
					(levelTwoTypeId?"&levelTwoTypeId="+levelTwoTypeId:"")+
					(levelThreeTypeId?"&levelThreeTypeId="+levelThreeTypeId:"")+
					(region?"&region="+region:"")+
					(country?"&country="+country:"")+
					(startdate?"&startdate="+startdate:"")+
					(enddate?"&enddate="+enddate:"")+
					("&page="+page)+
					("&rows="+rows);
	location.href = url;
}

function exportRusPaperExcel(){
	var levelOneTypeId=$("#column").val();
	var levelTwoTypeId=$("#subcolumn").val();
	var levelThreeTypeId=$("#subtocolumn").val();
	var region=$("#region").val();
	var country=$("#country").val();
	var startdate=$(".startdate").val();
	var enddate=$(".enddate").val();
	
	var grid = $('#learnList');  
	var options = grid.datagrid('getPager').data("pagination").options;  
	var page = options.pageNumber; //目前显示页
	var rows = options.pageSize;//条数
	
	var url=baseUrl+"examination/exportRusExamTestExcel.action?levelOneTypeId="+levelOneTypeId+
					(levelTwoTypeId?"&levelTwoTypeId="+levelTwoTypeId:"")+
					(levelThreeTypeId?"&levelThreeTypeId="+levelThreeTypeId:"")+
					(region?"&region="+region:"")+
					(country?"&country="+country:"")+
					(startdate?"&startdate="+startdate:"")+
					(enddate?"&enddate="+enddate:"")+
					("&page="+page)+
					("&rows="+rows);
	location.href = url;
}