function resize(){
	var banner_height=50;
	var content_height=$(window).height()-banner_height;
	var form_height=298;
	var padding_top=(content_height-form_height)/2;
	//$(".login-form").css("padding-top",padding_top+"px");
	$(".login-form form").css("top",padding_top+"px");
}
$(function(){
	resize();
	$(window).resize(function(){
		resize();
	});
	//回车键触发登录
	$("#username,#password").keydown(function(e){
		if(e.keyCode==13){
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
		//密码加密处理
		var _password = MD5(password);
		$("#password").val(_password);
		
		$(".login-status").show();$("#loginBut").addClass("disabled");
		var param = {};
		param.name = username;
		param.password = _password;
		$.post(
			baseUrl + "main/login.action",
			param,
			function(data,status){
				var _data = eval("("+data+")");
				if("success"==_data.msg){
					window.location = baseUrl + "tomsIndexPage.action";
				}
				else{
					alert(_data.msg);
				}
				$(".login-status").hide();$("#loginBut").removeClass("disabled");
			}
		);
	});
});