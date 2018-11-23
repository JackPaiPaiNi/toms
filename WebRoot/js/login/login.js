
$(function(){
	//初始化语言下拉选项
	loadParameters("TCL_TOMS","LANGUAGE",null).success(function(data){
		$("#languageCheck").combobox({
			width:"150px",
			data:data.LANGUAGE,
			valueField:"pkey", 
			textField:"pvalue",
			onLoadSuccess:function(){
				$('#languageCheck').combobox('setValue', language);
			},
			onSelect:function(record){
				var _language = record.pkey;
				var param = {};
				param.language = _language;
				$.post(
					baseUrl + "main/setLanguage.action",
					param,
					function(data,status)
					{
						location.reload();
					}
				);
			}
		});
	});
	
	
	
	
	//回车键触发登录
	$("#username,#password").keydown(function(e){
		if(e.keyCode==13)
		{
			$("#loginBut").click();
		}
	});
	//登录功能
	$("#loginBut").click(function(){
		var username = $.trim($("#username").val());
		var password = $("#password").val();
		if(null==username || ""==username){
			$("#username").focus();
			return;
		}else if(/[\'=*%;]/.test(username)){
			showMsg(locale("login.wronginput"),locale("alert.error"),"error");
			return;
		}
		if(null==password || ""==password){
			$("#password").focus();
			return;
		}
		var param = {};
		param.name = username;
		param.password = password;
		$.post(
			baseUrl + "main/login.action",
			param,
			function(data,status)
			{
				var _data = eval("("+data+")");
				if("success"==_data.msg)
				{
					window.location = baseUrl + "tomsIndexPage.action";
				}
				else
				{
					showMsg(_data.msg);
				}
			}
		);
	});
});