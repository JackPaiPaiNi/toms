/**
 * fzl 
 * AC目标显示,Type_target类型 0为导入国家，1为渠道，2为门店
 */
$(function(){
	selectParty();
//	loadPromoter();
	getSelectYear();
	getSelectMonth();
	currentTime();
	$('#month').change(function(){
		var month = find($("#month").val());//月 
		var year  =	$("#year").val();//年
		var d  = 0;//日
		var datadate = year+"-"+month+"-"+d;
		onload(datadate);
		onloadChan(datadate);
		onloadOffice(datadate);
		onloadRegion(datadate);
		onloadBranch(datadate);
		onloadSupervisor(datadate);
		onloadSalesman(datadate);
		onloadManager(datadate);
	});
	
	
	$('#year').change(function(){
		var month = find($("#month").val());//月 
		var year  =	$("#year").val();//年
		var d  = 0;//日
		var datadate = year+"-"+month+"-"+d;
		onload(datadate);
		onloadChan(datadate);
		onloadOffice(datadate);
		onloadRegion(datadate);
		onloadBranch(datadate);
		onloadSupervisor(datadate);
		onloadSalesman(datadate);
		onloadManager(datadate);
	});
	
//	$("#selectSearch").click(function(){
//		var year ='';
//		var month='';
//		var day=0;
//		var select = '';
//		var datadate='';
//		if($("#month").val()!=null && $("#month").val()!='' 
//		&& $("#year").val!=null && $("#year").val!=''){
//			year=$("#year").val();
//			month=$("#month").val();
//			select=true;
//		    datadate = year+"-"+month+"-"+day;	
//		}
//		if(datadate!=null){
//			onload(select,datadate);
//		}
//	});	
	
	/*商业中心*/
	$("#center").change(function(){
		if($(this).val() != ''){
			loadCountry($(this).val());
		}else{
			$("#office").html("");
			$("#region").html("");
			$("#country").html("");
		}
		$("#office").html("");
		$("#region").html("");
		$("#my_menu").hide();
		//clear();
		//intermediary();
	});
	//国家抉择
	$("#country").change(function(){ 
		if($(this).val() != ''){
			selectRegion($(this).val());
		}else{
			$("#office").html("");
			$("#region").html("");
		}
		$("#office").html("");
		$("#my_menu").hide();
		//clear();
		//intermediary();
	});
	//地区抉择
	$("#region").change(function(){
		if($(this).val() != ''){
			selectOffice($(this).val());
		}else{
			$("#office").html("");
		}
		$("#my_menu").hide();
		//clear();
		//intermediary();
	});
	
	
	$("#office").change(function(){
		if($(this).val() != ''){
			selectDealer($(this).val());
		}else{
			$("#dealer").html("");
		}
	});
	
	$("#dealer").change(function(){
		if($(this).val() != ''){
			selectStore($(this).val());
		}else{
			$("#stores").html("");
		}
	});
	
	$("#stores").change(function(){
		if($(this).val() != ''){
			loadSales($(this).val());
		}else{
			$("#sales").html("");
		}
	});
	$("#stores").change(function(){
		if($(this).val() != ''){
			loadSaleManager($(this).val());
		}else{
			$("#saleManager").html("");
		}
	});
	$("#stores").change(function(){
		if($(this).val() != ''){
			loadSupervisors($(this).val());
		}else{
			$("#Sups").html("");
		}
	});
	$("#stores").change(function(){
		if($(this).val() != ''){
			loadProduct($(this).val());
		}else{
			$("#Pros").html("");
		}
	});
	
	//手动选择查询
	$("#searchs").click(function(){
		var isSelect = false;
		var skipUrl = "";
		var id = "";
		var Oid="";
		
		if($("#Sups").val() != "" && $("#Sups").val() != null){//督导是否选择
			skipUrl = "selectUserSale";
			id = $("#Sups").val();
			isSelect = true;
		}else if($("#saleManager").val() != "" && $("#saleManager").val() != null){//业务经理是否选择
			skipUrl = "selectUserSale";
			id = $("#saleManager").val();
			isSelect = true;
		}else if($("#stores").val() != "" && $("#stores").val() != null){//门店是否选择
			skipUrl = "selecPartySalerTar";
			id = $("#stores").val();
			isSelect = true;
		}else if($("#dealer").val() != "" && $("#dealer").val() != null){//经销商是否选择
			skipUrl = "selectUserSale";
			id = $("#dealer").val();
			isSelect = true;
		}else if($("#office").val() != "" && $("#office").val() != null){//办事处是否选择
			skipUrl = "chooseOffice";
			Oid = $("#office").val();
			isSelect = true;
		}else if($("#region").val() != "" && $("#region").val() != null){//地区是否选择
			skipUrl = "chooseRegion";
			id = $("#region").val();
			isSelect = true;
		}else if($("#country").val() != "" && $("#country").val() != null){//国家是否选择
			skipUrl = "selecPartySalerTar";
			id = $("#country").val();
			isSelect = true;
		}else if($("#center").val() != "" && $("#center").val() != null){//商业中心
			skipUrl = "selecPartySalerTar";
			id = $("#center").val();
			isSelect = true;
		}
		
		if( id!=""||Oid!=""){
			if( skipUrl=='chooseRegion'){
				loadSearch(isSelect,id,skipUrl);
				//loadSearchOffice(isSelect,Oid,skipUrl);	
			}else{
				//loadSearch(isSelect,id,skipUrl);
				loadSearchOffice(isSelect,Oid,skipUrl);
			}
		}
	});
});

/*清除记录*/
function clear(){
	$("#regionlist").html("");
//	$("#salesman").html("");
//	$("#partyManager").html("");
//	$("#business_Mana").val("");
//	$("#business_Mana").attr("userId","");
//	$("#supervisor").html("");
}

function onload(datadate){	
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
					url : baseUrl + "platform/selectACStoreTargets.action",
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						$("#tbody").find('tr').empty();
						onmouse();
						var tdStr = "<tr>";				//新建tr
						$.each(data.rows, function(i, n) {
							tdStr += "<td>"+n.shopName+"</td>";
					           tdStr += "<td>"+splitK(n.quantity)+"</td>";
					           tdStr += "<td>"+formatRevenue(n.amount,2)+"</td>";
					           tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
					           tdStr += "<td>"+formatRevenue(n.tzAmount,2)+"</td>";
					           tdStr += "</tr>";
						});
						//$("#tbody").html(tdStr);  //在这里不能再用html替换
						$("#store").append(tdStr);
						var demo = 	$("#tbody tr");
						for(var i = 0;i<demo.length;i++){
							$(demo[i]).hide();
						}
						onmouse();
						autoSum();
					}
				});
			}
		}
	});
	
}

function onloadChan(datadate){
	$.ajax({
		url : baseUrl + "platform/selectCountry.action",
		type : "POST",
		data : {
			"id" : my_login_id
		},
		success : function(data) {
			var obj = eval('(' + data + ')');
			if (data != "" && data != null) {
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACChannelTarget.action";
				}else{
					url=baseUrl + "platform/selectACChannelTargets.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						$("#chan").find('tr').empty();	
						 var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							 tdStr += "<td>"+n.customerName+"</td>";
							 tdStr += "<td>"+splitK(n.quantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.amount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.tzAmount,2)+"</td>";
							 tdStr += "</tr>";
//						       $("#chan").html(tdStr);
						});
						$("#channel").append(tdStr);
						var demo = 	$("#chan tr");
						for(var i = 0;i<demo.length;i++){
							$(demo[i]).hide();
						}
						onmouse();
						autoSumChannel();
					}
				});
			}
		}
	});
}

function onloadOffice(datadate){
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
					url : baseUrl + "platform/selectACOfficeTargets.action",
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						$("#off").find('tr').empty();	
						 var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							 tdStr += "<td>"+n.partyName+"</td>";
							 tdStr += "<td>"+splitK(n.quantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.amount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.tzAmount,2)+"</td>";
							 tdStr += "</tr>";
//							$("#off").html(tdStr);	
						});
						$("#offices").append(tdStr);
						var demo = 	$("#off tr");
						for(var i = 0;i<demo.length;i++){
							$(demo[i]).hide();
						}
						onmouse();
						autoSumOffices();
					}
				});
			}
		}
	});
}

function onloadRegion(datadate){
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
					url : baseUrl + "platform/selectACRegionTargets.action",
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
					$("#reg").find('tr').empty();
					 var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							 tdStr += "<td>"+n.partyName+"</td>";
							 tdStr += "<td>"+splitK(n.quantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.amount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.tzAmount,2)+"</td>";
							 tdStr += "</tr>";
//							 $("#reg").html(tdStr);
						});
						$("#regions").append(tdStr);
						var demo = 	$("#reg tr");
						for(var i = 0;i<demo.length;i++){
							$(demo[i]).hide();
						}
						onmouse();
						autoSumRegions();
					}
				});
			}
		}
	});
}

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
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACBranchTarget.action";
				}else if(Type_target=="2"){
					url=baseUrl + "platform/selectACBranchTargets.action";
				}else{
					url=baseUrl + "platform/getACBranchTagetList.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
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
							 tdStr += "<td>"+formatRevenue(n.amount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.tzAmount,2)+"</td>";
							 tdStr += "</tr>";
//							 $("#bran").html(tdStr);
						});
						$("#branch").append(tdStr);
						var demo = 	$("#bran tr");
						for(var i = 0;i<demo.length;i++){
							$(demo[i]).hide();
						}
						onmouse();
						autoSumBranch();
					}
				});
			}
		}
	});
}

function onloadSupervisor(datadate){
	$.ajax({
		url : baseUrl + "platform/selectCountry.action",
		type : "POST",
		data : {
			"id" : my_login_id
		},
		success : function(data) {
			var obj = eval('(' + data + ')');
			if (data != "" && data != null) {
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACSupervisorTarget.action";
				}else{
					url=baseUrl + "platform/selectACSupervisorTargets.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						$("#sup").find('tr').empty();
					 var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							 tdStr += "<td>"+n.partyName+"</td>";
							 tdStr += "<td>"+splitK(n.quantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.amount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.tzAmount,2)+"</td>";
							 tdStr += "</tr>";
//							 $("#sup").html(tdStr);
						});
						$("#supervisor").append(tdStr);
						var demo = 	$("#sup tr");
						for(var i = 0;i<demo.length;i++){
							$(demo[i]).hide();
						}
						onmouse();
						autoSumSupervisor();
					}
				});
			}
		}
	});
}

function onloadSalesman(datadate){
	$.ajax({
		url : baseUrl + "platform/selectCountry.action",
		type : "POST",
		data : {
			"id" : my_login_id
		},
		success : function(data) {
			var obj = eval('(' + data + ')');
			if (data != "" && data != null) {
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACSalemanTarget.action";
				}else{
					url=baseUrl + "platform/selectACSalesmanTargets.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						$("#sale").find('tr').empty();
					 var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							 tdStr += "<td>"+n.partyName+"</td>";
							 tdStr += "<td>"+splitK(n.quantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.amount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.tzAmount,2)+"</td>";
							 tdStr += "</tr>";
//							 $("#sale").html(tdStr);
						});
						$("#salesman").append(tdStr);
						var demo = 	$("#sale tr");
						for(var i = 0;i<demo.length;i++){
							$(demo[i]).hide();
						}
						onmouse();
						autoSumSalesman();
					}
				});
			}
		}
	});
}

function onloadManager(datadate){
	$.ajax({
		url : baseUrl + "platform/selectCountry.action",
		type : "POST",
		data : {
			"id" : my_login_id
		},
		success : function(data) {
			var obj = eval('(' + data + ')');
			if (data != "" && data != null) {
				if(Type_target=="1"){
					url=baseUrl + "platform/getACBussinessTarget.action";
				}else{
					url=baseUrl + "platform/selectACBusinessTargets.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						$("#man").find('tr').empty();
					 var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							 tdStr += "<td>"+n.partyName+"</td>";
							 tdStr += "<td>"+splitK(n.quantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.amount,2)+"</td>";
							 tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
							 tdStr += "<td>"+formatRevenue(n.tzAmount,2)+"</td>";
							 tdStr += "</tr>";
//							 $("#man").html(tdStr);
						});
						$("#managers").append(tdStr);
						var demo = 	$("#man tr");
						for(var i = 0;i<demo.length;i++){
							$(demo[i]).hide();
						}
						onmouse();
						autoSumManager();
					}
				});
			}
		}
	});
}
//function loadSearch(isSelect,id,skipUrl){
//	if(isSelect){
//		$.ajax({
//			url:baseUrl + "platform/"+skipUrl+".action",
//			type:"POST",
//			data:{"id":id},
//			success:function(data){
//				$("#reg").find('tr').empty();	//清空tr内容
//				var tdStr = "<tr>";				//新建tr
//	            $.each(data.rows, function(i, n){
//	           tdStr += "<td>"+n.partyName+"</td>";
//	           tdStr += "<td>"+splitK(n.quantity)+"</td>";
//	           tdStr += "<td>"+splitK(n.amount)+"</td>";
//	           tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
//	           tdStr += "<td>"+splitK(n.tzAmount)+"</td>";
//	           tdStr += "</tr>";
//		        $("#reg").html(tdStr);
//	            });  	  
//	            autoSumRegions(); 	
//			}
//		});
//	}
//}
//
//function loadSearchOffice(isSelect,id,skipUrl){
//	if(isSelect){
//		$.ajax({
//			url:baseUrl + "platform/"+skipUrl+".action",
//			type:"POST",
//			data:{"id":id},
//			success:function(data){
//				$("#off").find('tr').empty();	//清空tr内容
//				var tdStr = "<tr>";				//新建tr
//	            $.each(data.rows, function(i, n){
//	           tdStr += "<td>"+n.partyName+"</td>";
//	           tdStr += "<td>"+splitK(n.quantity)+"</td>";
//	           tdStr += "<td>"+splitK(n.amount)+"</td>";
//	           tdStr += "<td>"+splitK(n.tzQuantity)+"</td>";
//	           tdStr += "<td>"+splitK(n.tzAmount)+"</td>";
//	           tdStr += "</tr>";
//		        $("#off").html(tdStr);
//	            });  	  
//	            autoSumOffices(); 	
//			}
//		});
//	}
//}

//是否是总部用户
function selectParty(){
	$.ajax({
		url:baseUrl + "platform/selectCountry.action",
		type:"POST",
		data:{"id":my_login_id},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				if(obj.rows[0].countryId == '999'){//总部用户
					loadBusinessCenter();
				}else if(obj.rows[0].id == '999'){//业务中心用户
					loadCountry(obj.rows[0].countryId);
				}else if(obj.rows[0].countryId == obj.rows[0].assistCountryId){//国家用户
					selectRegion(obj.rows[0].countryId);
				}else{
					$.ajax({
						url:baseUrl + "platform/selectRegion.action",
						type:"POST",
						data:{"partyId":obj.rows[0].countryId},
						success:function(data){
							var obj =  eval('(' + data + ')');
							if(obj.rows.length != 0){//经营部用户
								selectOffice(obj.rows[0].countryId);
							}else{//办事处用户
								intermediary();
							}
						}
					});
				}
			}
		}
	});
}

/*加载商业中心*/
function loadBusinessCenter(){
	$.ajax({
		url:baseUrl + "platform/selectBusinessCenter.action",
		type:"POST",
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var partyManaStr = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					partyManaStr += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#center").html(partyManaStr);
			}else{
				$("#center").html("");
			}
		}
	});
}

//加载国家
function loadCountry(partyId){
	$.ajax({
		url:baseUrl + "platform/selectAllCountry+.action",
		type:"POST",
		data:{"id":partyId},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var parStrObj = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					parStrObj += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#country").html(parStrObj);
			}else{
				$("#country").html("");
			}
		}
	});
}

//加载地区
function selectRegion(partyId){
	$.ajax({
		url:baseUrl + "platform/selectAllCountry.action",
		type:"POST",
		data:{"id":partyId},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var regStrObj = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					regStrObj += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#region").html(regStrObj);
			}else{
				$("#region").html("");
			}
		}
	});
}


//加载办事处
function selectOffice(partyId){
	$.ajax({
		url:baseUrl + "platform/selectAllCountry.action",
		type:"POST",
		data:{"id":partyId},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null && obj.rows != "" && obj.rows != null){
				var regStrObj = "<option value=''>SELECT</option>";
				for(var i = 0;i<obj.rows.length;i++){
					regStrObj += "<option value="+obj.rows[i].countryId+">"+obj.rows[i].countryName+"</option>";
				}
				$("#office").html(regStrObj);
			}else{
				$("#office").html("");
			}
		}
	});
}


//加载经销商
function selectDealer(partyId){
	$.ajax({
		url:baseUrl + "platform/selectCustomer.action",
		type:"POST",
		data:{"partyId":partyId},
		success:function(msg){
			if(msg !=  "" && msg != null){
				var regStrObj = "<option value=''>SELECT</option>";
				for(var i = 0;i<msg.rows.length;i++){
					regStrObj += "<option value="+msg.rows[i].countryId+">"+msg.rows[i].countryName+"</option>";
				}
				$("#dealer").html(regStrObj);
			}else{
				$("#dealer").html("");
			}
		}
	});
}
////加载经销商
//function selectDealer(){
//	$.ajax({
//		url:baseUrl + "platform/selectCountry.action",
//		type:"POST",
//		data:{"id":my_login_id},
//		success:function(data){
//			var obj =  eval('(' + data + ')');
//			if(data !=  "" && data != null){
//				$.ajax({
//					url:baseUrl + "platform/selectCustomer.action",
//					type:"POST",
//					data:{"partyId":obj.rows[0].countryId},
//					success:function(msg){
//						if(msg !=  "" && msg != null){
//							var regStrObj = "<option value=''>SELECT</option>";
//							for(var i = 0;i<msg.rows.length;i++){
//								regStrObj += "<option value="+msg.rows[i].countryId+">"+msg.rows[i].countryName+"</option>";
//							}
//							$("#dealer").html(regStrObj);
//						}else{
//							$("#dealer").html("");
//						}
//					}
//				});
//			}
//		}
//	});
//	
//}

//加载门店
function selectStore(partyId){
	$.ajax({
		url:baseUrl + "platform/selectShop.action",
		type:"POST",
		data:{"partyId":partyId},
		success:function(data){
			//var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null){
				var regStrObj = "<option value=''>SELECT</option>";
				for(var i = 0;i<data.rows.length;i++){
					regStrObj += "<option value="+data.rows[i].countryId+">"+data.rows[i].countryName+"</option>";
				}
				$("#stores").html(regStrObj);
			}else{
				$("#stores").html("");
			}
		}
	});
}

///*加载督导、业务员和业务经理中介*/
//function intermediary(){
//	var center = $("#center").val();
//	var country = $("#country").val();
//	var region = $("#region").val(); 
//	var office = $("#office").val();
//	var dealer=$("dealer").val();
//	var store=$("store").val();
//	if(store != "" && store != null){
//		loadSupervisor(store);
//		loadSales(store);
//	}
//}


//加载业务员
function loadSales(value){
	var isNUll = false;
	$.ajax({
		url:baseUrl + "platform/selectSale.action",
		type:"POST",
		data:{"partyId":value,
			  "saleType":0
		},
		success:function(data){
			//var obj =  eval('(' + data + ')');
			var salesmanStr = "<option value=''>SELECT</option>";
			if(data !=  "" && data != null){
				isNUll = true;
				for(var i = 0;i<data.rows.length;i++){
					salesmanStr += "<option value="+data.rows[i].countryId+">"+data.rows[i].countryName+"</option>";
				}
			}
			$("#sales").html(salesmanStr);
			//loadSaleManager(value,isNUll);
		}
	});
}

//加载督导
function loadSupervisors(value){
	$.ajax({
		url:baseUrl + "platform/selectSale.action",
		type:"POST",
		data:{"partyId":value,
			  "saleType":2
		},
		success:function(data){
			//var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null ){
				var supervisorStr = "<option value=''>SELECT</option>";
				for(var i = 0;i<data.rows.length;i++){
					supervisorStr += "<option value="+data.rows[i].countryId+">"+data.rows[i].countryName+"</option>";
				}
				$("#Sups").html(supervisorStr);
			}
		}
	});
}

//加载区域经理 
function loadSaleManager(){
	$.ajax({
		url:baseUrl + "platform/selectCountry.action",
		type:"POST",
		data:{"id":my_login_id},
		success:function(data){
			var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null){
				$.ajax({
					url:baseUrl + "platform/selectManager.action",
					type:"POST",
					data:{"partyId":obj.rows[0].countryId},
					success:function(data){
						//var obj =  eval('(' + data + ')');
						var salesmanStr =  "<option value=''>SELECT</option>";
						if(data !=  "" && data != null){
							for(var i = 0;i<data.rows.length;i++){
								salesmanStr += "<option value="+data.rows[i].countryId+">"+data.rows[i].countryName+"</option>";
							}
						}
						$("#saleManager").html(salesmanStr);
					}
				});
			}
		}
	});
}

//下拉单品
function loadProduct(value){
	$.ajax({
		url:baseUrl + "platform/selectProduct.action",
		type:"POST",
		data:{"partyId":value},
		success:function(data){
			//var obj =  eval('(' + data + ')');
			if(data !=  "" && data != null ){
				var proStr = "<option value=''>SELECT</option>";
				for(var i = 0;i<data.rows.length;i++){
					proStr += "<option value="+data.rows[i].countryId+">"+data.rows[i].countryName+"</option>";
				}
				$("#Pros").html(proStr);
			}
		}
	});
}

//导入数据
function importACtarget(val){
	showImportWin(locale("excel.target.import"),baseUrl + "platform/importACtarget.action?targetType=" + val);
}
//function importChannelTarget(){
//	showImportWin(locale("channel.import"),baseUrl + "platform/importChannel.action");
//}
//function importOfficeTarget(){
//	showImportWin(locale("office.import"),baseUrl + "platform/importOffice.action");
//}
//function importRegTarget(){
//	showImportWin(locale("reg.import"),baseUrl + "platform/importReg.action");
//}
//function importBranchTarget(){
//	showImportWin(locale("branch.import"),baseUrl + "platform/importBranch.action");
//}
//function importPromoterTarget(){
//	showImportWin(locale("promoter.import"),baseUrl + "platform/importRole.action");
//}
//function importSupervisorTarget(){
//	showImportWin(locale("supervisor.import"),baseUrl + "platform/importRole.action");
//}
//function importSalesmanTarget(){
//	showImportWin(locale("salesman.import"),baseUrl + "platform/importRole.action");
//}
//function importManagerTarget(){
//	showImportWin(locale("manager.import"),baseUrl + "platform/importRole.action");
//}


function load(){
	var month = find($("#month").val());//月 
	var year  =	$("#year").val();//年
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
					url : baseUrl + "platform/selectACStoreTargets.action",
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						$.each(data.rows, function(i, n) {
							var row = $("#storelist").clone();
							row.find("td:eq(0)").html(n.shopName);
							row.find("td:eq(1)").html(splitK(n.quantity));
							row.find("td:eq(2)").html(formatRevenue(n.amount,2));
							row.find("td:eq(3)").html(splitK(n.tzQuantity));
							row.find("td:eq(4)").html(formatRevenue(n.tzAmount,2));
							row.appendTo("#tbody");
						});
						onmouse();
						autoSum();
					}
				});
			}
		}
	});
}

function loadChannel(){
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
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACChannelTarget.action";
				}else{
					url=baseUrl + "platform/selectACChannelTargets.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
						// var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							// tdStr += "<td>"+n.customerName+"</td>";
							// tdStr += "<td>"+n.quantity+"</td>";
							// tdStr += "<td>"+n.amount+"</td>";
							// tdStr += "<td>"+n.tzQuantity+"</td>";
							// tdStr += "<td>"+n.tzAmount+"</td>";
							// tdStr += "</tr>";
							var row = $("#channellist").clone();
							row.find("td:eq(0)").html(n.customerName);
							row.find("td:eq(1)").html(splitK(n.quantity));
							row.find("td:eq(2)").html(formatRevenue(n.amount,2));
							row.find("td:eq(3)").html(splitK(n.tzQuantity));
							row.find("td:eq(4)").html(formatRevenue(n.tzAmount,2));
							row.appendTo("#chan");
						});
						//		 $('#channel').append(tdStr);
						onmouse();
						autoSumChannel();
					}
				});
			}
		}
	});
}

function loadOffice(){
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
					url : baseUrl + "platform/selectACOfficeTargets.action",
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
					// var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							// tdStr += "<td>"+n.partyName+"</td>";
							// tdStr += "<td>"+n.quantity+"</td>";
							// tdStr += "<td>"+n.amount+"</td>";
							// tdStr += "<td>"+n.tzQuantity+"</td>";
							// tdStr += "<td>"+n.tzAmount+"</td>";
							// tdStr += "</tr>";
							var row = $("#officelist").clone();
							row.find("td:eq(0)").html(n.partyName);
							row.find("td:eq(1)").html(splitK(n.quantity));
							row.find("td:eq(2)").html(formatRevenue(n.amount,2));
							row.find("td:eq(3)").html(splitK(n.tzQuantity));
							row.find("td:eq(4)").html(formatRevenue(n.tzAmount,2));
							row.appendTo("#off");

						});
						// $('#offices').append(tdStr);
						onmouse();
						autoSumOffices();
					}
				});
			}
		}
	});
}

function loadRegion(){
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
					url : baseUrl + "platform/selectACRegionTargets.action",
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
					// var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							// tdStr += "<td>"+n.partyName+"</td>";
							// tdStr += "<td>"+n.quantity+"</td>";
							// tdStr += "<td>"+n.amount+"</td>";
							// tdStr += "<td>"+n.tzQuantity+"</td>";
							// tdStr += "<td>"+n.tzAmount+"</td>";
							// tdStr += "</tr>";
							var row = $("#regionlist").clone();
							row.find("td:eq(0)").html(n.partyName);
							row.find("td:eq(1)").html(splitK(n.quantity));
							row.find("td:eq(2)").html(formatRevenue(n.amount,2));
							row.find("td:eq(3)").html(splitK(n.tzQuantity));
							row.find("td:eq(4)").html(formatRevenue(n.tzAmount,2));
							row.appendTo("#reg");
						});
						// $('#regions').append(tdStr);
						onmouse();
						autoSumRegions();
					}
				});
			}
		}
	});
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
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACBranchTarget.action";
				}else if(Type_target=="2"){
					url=baseUrl + "platform/selectACBranchTargets.action";
				}else{
					url=baseUrl + "platform/getACBranchTagetList.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
					// var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							// tdStr += "<td>"+n.partyName+"</td>";
							// tdStr += "<td>"+n.quantity+"</td>";
							// tdStr += "<td>"+n.amount+"</td>";
							// tdStr += "<td>"+n.tzQuantity+"</td>";
							// tdStr += "<td>"+n.tzAmount+"</td>";
							// tdStr += "</tr>";
							var row = $("#branchlist").clone();
							row.find("td:eq(0)").html(n.partyName);
							row.find("td:eq(1)").html(splitK(n.quantity));
							row.find("td:eq(2)").html(formatRevenue(n.amount,2));
							row.find("td:eq(3)").html(splitK(n.tzQuantity));
							row.find("td:eq(4)").html(formatRevenue(n.tzAmount,2));
							row.appendTo("#bran");
						});
						// $('#branch').append(tdStr);
						onmouse();
						autoSumBranch();
					}
				});
			}
		}
	});
}

//function loadPromoter(){
//
//	$.ajax({
//		dataType:"JSON",
//		url:baseUrl + "platform/loadPromoterData.action",
//		type:"POST",
//		success:function(data){//data返回数据
////			var tdStr = "<tr>";
//            $.each(data.rows, function(i, n){
////           tdStr += "<td>"+n.partyName+"</td>";
////           tdStr += "<td>"+n.quantity+"</td>";
////           tdStr += "<td>"+n.amount+"</td>";
////           tdStr += "<td>"+n.tzQuantity+"</td>";
////           tdStr += "<td>"+n.tzAmount+"</td>";
////           tdStr += "</tr>";
//            var row=$("#promoterlist").clone();
//        	row.find("td:eq(0)").html(n.partyName);
//        	row.find("td:eq(1)").html(n.quantity);
//        	row.find("td:eq(2)").html(n.amount);
//        	row.find("td:eq(3)").html(n.tzQuantity);
//        	row.find("td:eq(4)").html(n.tzAmount);
//        	row.appendTo("#pro");	
//            });  
////		 $('#promoter').append(tdStr);
//		 autoSumPromoter();
//		}
//	});
//}

function loadSupervisor(){
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
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACSupervisorTarget.action";
				}else{
					url=baseUrl + "platform/selectACSupervisorTargets.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
					// var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							// tdStr += "<td>"+n.partyName+"</td>";
							// tdStr += "<td>"+n.quantity+"</td>";
							// tdStr += "<td>"+n.amount+"</td>";
							// tdStr += "<td>"+n.tzQuantity+"</td>";
							// tdStr += "<td>"+n.tzAmount+"</td>";
							// tdStr += "</tr>";
							var row = $("#supervisorlist").clone();
							row.find("td:eq(0)").html(n.partyName);
							row.find("td:eq(1)").html(splitK(n.quantity));
							row.find("td:eq(2)").html(formatRevenue(n.amount,2));
							row.find("td:eq(3)").html(splitK(n.tzQuantity));
							row.find("td:eq(4)").html(formatRevenue(n.tzAmount,2));
							row.appendTo("#sup");
						});
						// $('#supervisor').append(tdStr);
						onmouse();
						autoSumSupervisor();
					}
				});
			}
		}
	});
}

function loadSalesman(){
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
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACSalemanTarget.action";
				}else{
					url=baseUrl + "platform/selectACSalesmanTargets.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
					// var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							// tdStr += "<td>"+n.partyName+"</td>";
							// tdStr += "<td>"+n.quantity+"</td>";
							// tdStr += "<td>"+n.amount+"</td>";
							// tdStr += "<td>"+n.tzQuantity+"</td>";
							// tdStr += "<td>"+n.tzAmount+"</td>";
							// tdStr += "</tr>";
							var row = $("#salesmanlist").clone();
							row.find("td:eq(0)").html(n.partyName);
							row.find("td:eq(1)").html(splitK(n.quantity));
							row.find("td:eq(2)").html(formatRevenue(n.amount,2));
							row.find("td:eq(3)").html(splitK(n.tzQuantity));
							row.find("td:eq(4)").html(formatRevenue(n.tzAmount,2));
							row.appendTo("#sale");
						});
						// $('#salesman').append(tdStr);
						onmouse();
						autoSumSalesman();
					}
				});
			}
		}
	});
}

function loadManager(){
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
				var url="";
				if(Type_target=="1"){
					url=baseUrl + "platform/getACBussinessTarget.action";
				}else{
					url=baseUrl + "platform/selectACBusinessTargets.action";
				}
				$.ajax({
					dataType : "JSON",
					url : url,
					type : "POST",
					data : {
						"partyId" : obj.rows[0].countryId,"datadate":datadate
					},
					success : function(data) {// data返回数据
					// var tdStr = "<tr>";
						$.each(data.rows, function(i, n) {
							// tdStr += "<td>"+n.partyName+"</td>";
							// tdStr += "<td>"+n.quantity+"</td>";
							// tdStr += "<td>"+n.amount+"</td>";
							// tdStr += "<td>"+n.tzQuantity+"</td>";
							// tdStr += "<td>"+n.tzAmount+"</td>";
							// tdStr += "</tr>";
							var row = $("#managerslist").clone();
							row.find("td:eq(0)").html(n.partyName);
							row.find("td:eq(1)").html(splitK(n.quantity));
							row.find("td:eq(2)").html(formatRevenue(n.amount,2));
							row.find("td:eq(3)").html(splitK(n.tzQuantity));
							row.find("td:eq(4)").html(formatRevenue(n.tzAmount,2));
							row.appendTo("#man");
						});
						// $('#managers').append(tdStr);
						onmouse();
						autoSumManager();
					}
				});
			}
		}
	});
}

//统计更新数量和金额
//if($('.store-sum').length>0){
//    autoSum();
//}
//if($('.channel-sum').length>0){
//	autoSumChannel();
//}
//if($('.offices-sum').length>0){
//	autoSumChannel();
//}
//if($('.regions-sum').length>0){
//	autoSumChannel();
//}
//if($('.branch_sum').length>0){
//	autoSumChannel();
//}
//if($('.promoter_sum').length>0){
//	autoSumManager();
//}
//if($('.supervisor_sum').length>0){
//	autoSumManager();
//}
//if($('.salesman_sum').length>0){
//	autoSumManager();
//}
//if($('.managers_sum').length>0){
//	autoSumManager();
//}

//门店目标统计
function autoSum(){
    var total_objs = new Object();
    $('.store-sum').each(function(){
        var index = $('tr th').index(this)+1;	//遍历th对应td的值，index 从0开始
        var total = 0;
        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
            var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
            //alert(text);
            total += Number(text);
        });
        total_objs[index] = total.toFixed(2);
    });
    var td_num = $('.store-sum').parent('tr').find('th').length+1;
    
    var tds = '<tr style="color:red;">';
    for(var i=0;i<td_num;i++){
        var total = 'Total';
        if(total_objs[i]!=undefined){
            total = splitK(Number(total_objs[i]));
        }
        tds += '<td style="color: #c7254e;">'+ total + '</td>';
    }
    tds += '</tr>';
    $('.store-sum').closest('table').append(tds);
    

}

//渠道目标统计
function autoSumChannel(){
    if(Type_target=="1"){
    	var total_objs = new Object();
        $('.channel-sum').each(function(){
            var index = $('tr th').index(this)+1; //index(this)是当前文本下标，-3是返回来
            var total = 0;
            $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
                var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
                total += Number(text);
            });
            total_objs[index] = total.toFixed(2);
        });
        var td_num = $('.channel-sum').parent('tr').find('th').length+1;
        
        var tds = '<tr style="color:red;">';
        for(var i=0;i<td_num;i++){
            var total = 'Total';
            if(total_objs[i]!=undefined){
                total = splitK(Number(total_objs[i]));
            }
            tds += '<td style="color: #c7254e;">'+ total + '</td>';
        }
        tds += '</tr>';
        $('.channel-sum').closest('table').append(tds);
    }else{
    	var total_objs = new Object();
        $('.channel-sum').each(function(){
            var index = $('tr th').index(this)-3; //index(this)是当前文本下标，-3是返回来
            var total = 0;
            $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
                var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
                total += Number(text);
            });
            total_objs[index] = total.toFixed(2);
        });
        var td_num = $('.channel-sum').parent('tr').find('th').length+1;
        
        var tds = '<tr style="color:red;">';
        for(var i=0;i<td_num;i++){
            var total = 'Total';
            if(total_objs[i]!=undefined){
                total = splitK(Number(total_objs[i]));
            }
            tds += '<td style="color: #c7254e;">'+ total + '</td>';
        }
        tds += '</tr>';
        $('.channel-sum').closest('table').append(tds);
    }
    

}

//办事处目标统计
function autoSumOffices(){
    var total_objs = new Object();
    $('.offices-sum').each(function(){
        var index = $('tr th').index(this)-7; //index(this)是当前文本下标，-7是返回来
        var total = 0;
        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
            var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
            total += Number(text);
        });
        total_objs[index] = total.toFixed(2);
    });
    var td_num = $('.offices-sum').parent('tr').find('th').length+1;
    
    var tds = '<tr style="color:red;">';
    for(var i=0;i<td_num;i++){
        var total = 'Total';
        if(total_objs[i]!=undefined){
            total = splitK(Number(total_objs[i]));
        }
        tds += '<td style="color: #c7254e;">'+ total + '</td>';
    }
    tds += '</tr>';
    $('.offices-sum').closest('table').append(tds);
    

}


//区域目标统计
function autoSumRegions(){
    var total_objs = new Object();
    $('.regions-sum').each(function(){
        var index = $('tr th').index(this)-11; //index(this)是当前文本下标，-11是返回来
        var total = 0;
        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
            var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
            total += Number(text);
        });
        total_objs[index] = total.toFixed(2);
    });
    var td_num = $('.regions-sum').parent('tr').find('th').length+1;
    
    var tds = '<tr style="color:red;">';
    for(var i=0;i<td_num;i++){
        var total = 'Total';
        if(total_objs[i]!=undefined){
            total = splitK(Number(total_objs[i]));
        }
        tds += '<td style="color: #c7254e;">'+ total + '</td>';
    }
    tds += '</tr>';
    $('.regions-sum').closest('table').append(tds);
    

}

//分公司目标统计
function autoSumBranch(){
    if(Type_target=="1"){
    	var total_objs = new Object();
        $('.branch_sum').each(function(){
            var index = $('tr th').index(this)-3; //index(this)是当前文本下标，-15是返回来
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
    }else if(Type_target=="2"){
    	var total_objs = new Object();
        $('.branch_sum').each(function(){
            var index = $('tr th').index(this)-15; //index(this)是当前文本下标，-15是返回来
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
    }else{
    	var total_objs = new Object();
        $('.branch_sum').each(function(){
            var index = $('tr th').index(this)+1; //index(this)是当前文本下标，-15是返回来
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
    

}

//导购员目标统计
//function autoSumPromoter(){
//    var total_objs = new Object();
//    $('.promoter_sum').each(function(){
//        var index = $('tr th').index(this)-19; //index(this)是当前文本下标，-19是返回来
//        var total = 0;
//        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
//            var text = $(this).find('td').eq(index).text();
//            total += Number(text);
//        });
//        total_objs[index] = total;
//    });
//    var td_num = $('.promoter_sum').parent('tr').find('th').length+1;
//    
//    var tds = '<tr style="color:red;">';
//    for(var i=0;i<td_num;i++){
//        var total = 'Total';
//        if(total_objs[i]!=undefined){
//            total = Number(total_objs[i]);
//        }
//        tds += '<td style="color: #c7254e;">'+ total + '</td>';
//    }
//    tds += '</tr>';
//    $('.promoter_sum').closest('table').append(tds);
//    
//
//}

//督导目标统计
function autoSumSupervisor(){
    if(Type_target=="1"){
    	var total_objs = new Object();
        $('.supervisor_sum').each(function(){
            var index = $('tr th').index(this)-7; //index(this)是当前文本下标，-23是返回来
            var total = 0;
            $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
                var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
                total += Number(text);
            });
            total_objs[index] = total.toFixed(2);
        });
        var td_num = $('.supervisor_sum').parent('tr').find('th').length+1;
        
        var tds = '<tr style="color:red;">';
        for(var i=0;i<td_num;i++){
            var total = 'Total';
            if(total_objs[i]!=undefined){
                total = splitK(Number(total_objs[i]));
            }
            tds += '<td style="color: #c7254e;">'+ total + '</td>';
        }
        tds += '</tr>';
        $('.supervisor_sum').closest('table').append(tds);
    }else{
    	var total_objs = new Object();
        $('.supervisor_sum').each(function(){
            var index = $('tr th').index(this)-19; //index(this)是当前文本下标，-23是返回来
            var total = 0;
            $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
                var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
                total += Number(text);
            });
            total_objs[index] = total.toFixed(2);
        });
        var td_num = $('.supervisor_sum').parent('tr').find('th').length+1;
        
        var tds = '<tr style="color:red;">';
        for(var i=0;i<td_num;i++){
            var total = 'Total';
            if(total_objs[i]!=undefined){
                total = splitK(Number(total_objs[i]));
            }
            tds += '<td style="color: #c7254e;">'+ total + '</td>';
        }
        tds += '</tr>';
        $('.supervisor_sum').closest('table').append(tds);
    }
    

}

//业务员目标统计
function autoSumSalesman(){
   if(Type_target=="1"){
	   var total_objs = new Object();
	    $('.salesman_sum').each(function(){
	        var index = $('tr th').index(this)-11; //index(this)是当前文本下标，-27是返回来
	        var total = 0;
	        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
	            var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
	            total += Number(text);
	        });
	        total_objs[index] = total.toFixed(2);
	    });
	    var td_num = $('.salesman_sum').parent('tr').find('th').length+1;
	    
	    var tds = '<tr style="color:red;">';
	    for(var i=0;i<td_num;i++){
	        var total = 'Total';
	        if(total_objs[i]!=undefined){
	            total = splitK(Number(total_objs[i]));
	        }
	        tds += '<td style="color: #c7254e;">'+ total + '</td>';
	    }
	    tds += '</tr>';
	    $('.salesman_sum').closest('table').append(tds);
   }else{
	   var total_objs = new Object();
	    $('.salesman_sum').each(function(){
	        var index = $('tr th').index(this)-23; //index(this)是当前文本下标，-27是返回来
	        var total = 0;
	        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
	            var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");
	            total += Number(text);
	        });
	        total_objs[index] = total.toFixed(2);
	    });
	    var td_num = $('.salesman_sum').parent('tr').find('th').length+1;
	    
	    var tds = '<tr style="color:red;">';
	    for(var i=0;i<td_num;i++){
	        var total = 'Total';
	        if(total_objs[i]!=undefined){
	            total = splitK(Number(total_objs[i]));
	        }
	        tds += '<td style="color: #c7254e;">'+ total + '</td>';
	    }
	    tds += '</tr>';
	    $('.salesman_sum').closest('table').append(tds);
   }
    

}

//业务经理目标统计
function autoSumManager(){
   if(Type_target=="1"){
	   var total_objs = new Object();
	    $('.managers_sum').each(function(){
	        var index = $('tr th').index(this)-15; //index(this)是当前文本下标，-31是返回来
	        var total = 0;
	        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
	            var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");//千位符转化
	            total =total+ Number(text);
	        });
	        total_objs[index] = total.toFixed(2);
	    });
	    var td_num = $('.managers_sum').parent('tr').find('th').length+1;
	    
	    var tds = '<tr style="color:red;">';
	    for(var i=0;i<td_num;i++){
	        var total = 'Total';
	        if(total_objs[i]!=undefined){
	            total = splitK(Number(total_objs[i]));
	        }
	        tds += '<td style="color: #c7254e;">'+ total + '</td>';
	    }
	    tds += '</tr>';
	    $('.managers_sum').closest('table').append(tds);
   }else{
	   var total_objs = new Object();
	    $('.managers_sum').each(function(){
	        var index = $('tr th').index(this)-27; //index(this)是当前文本下标，-31是返回来
	        var total = 0;
	        $(this).parent('tr').parent('thead').next('tbody').children('tr').each(function(){
	            var text = $(this).find('td').eq(index).text().replace(/[^0-9.]/ig,"");//千位符转化
	            total =total+ Number(text);
	        });
	        total_objs[index] = total.toFixed(2);
	    });
	    var td_num = $('.managers_sum').parent('tr').find('th').length+1;
	    
	    var tds = '<tr style="color:red;">';
	    for(var i=0;i<td_num;i++){
	        var total = 'Total';
	        if(total_objs[i]!=undefined){
	            total = splitK(Number(total_objs[i]));
	        }
	        tds += '<td style="color: #c7254e;">'+ total + '</td>';
	    }
	    tds += '</tr>';
	    $('.managers_sum').closest('table').append(tds);
   }
    

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
	load();
	loadChannel();
	loadOffice();
	loadRegion();
	loadBranch();
	loadSupervisor();
	loadSalesman();
	loadManager();
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