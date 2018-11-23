
$(function(){
	//回车键触发登录
	$("#username,#password").keydown(function(e){
		if(e.keyCode==13){
			$("#loginBut").click();
		}
	});
	//登录功能

	$("#loginBut").click(
			function()
			{

				var username = $.trim($("#username").val());
				
				var password = $("#password").val();
				if(null==username || ""==username){
					$("#username").focus();
					return;
				}else if(/[\'=*%;]/.test(username)){
					//showMsg(locale("login.wronginput"),locale("alert.error"),"error");
					return;
				}
				if(null==password || ""==password){
					$("#password").focus();
					return;
				}
				 
				//密码加密处理
				var _password = MD5(password);
				$("#password").val(_password);
				
				var param = {};
				param.name = username;
				param.password = _password;
				$(".login-status").show();$("#loginBut").addClass("disabled");
				
				var uidErrCoun =username + "userErrCoun";
				 var PinLoc = username+ "userPinLocTim";
			    var count = getCookie(uidErrCoun);
			    var lockTim = getCookie(PinLoc);
			  
			    
			    var str;
				if(language=="zh"){
					str="用户登录失败多次，已被锁定，请5分钟之后重新登录！";
				}else{
					str="User login failed many times, has been locked, please login again after 5 minutes!";
				}
			    if (lockTim == null || lockTim == "") {  
			    	if( count>=5){
			    		alert(str);
			    		 var expireDate = new Date();

			                expireDate.setTime(expireDate.getTime() + 5 * 60 * 1000);

			                setCookie(PinLoc, "userPinLocTim", expireDate.toGMTString(), "/");
			                deleteCookie(uidErrCoun, "/");
			    		return;
			    	}
			    }else{
			    	alert(str);
			    	return;
			    }
			  
			   
			    	
			    
				$.post(baseUrl + "main/login.action",
						param,
						function(data){
					   if("success"==data.msg)
					   {
						   window.location = baseUrl + "tomsIndexPage.action";
					
					   }
					   else{
						  
						  logInputFaile(username);
							
						   alert(data.msg);
						  
					   }
					   $(".login-status").hide();$("#loginBut").removeClass("disabled");
				},
						"json"
				);
				
				
			}
	); 
}
);





function logInputFaile(username) {

	var uidErrCoun =username + "userErrCoun";

    var PinLoc = username+ "userPinLocTim";

 

    var count = getCookie(uidErrCoun);

    var lockTim = getCookie(PinLoc);
    if (lockTim == null || lockTim == "") {                       //未锁定

        var expireDate = new Date();

        expireDate.setTime(expireDate.getTime() + 24 * 60 * 60 * 1000);

        if (count == null || count=="") {                  //第一次输入错误

            setCookie(uidErrCoun, 1, expireDate.toGMTString(), "/");

            return false;

        }

        else {

            var expireDate = new Date();

            expireDate.setTime(expireDate.getTime() + 24 * 60 * 60 * 1000);

            var count = getCookie(uidErrCoun);
            if (count <5) {                                //出错小于5次
                setCookie(uidErrCoun, ++count, expireDate.toGMTString(), "/");

                return false;

            }

            else {                                          //大于5次，锁定账号
                var expireDate = new Date();

                expireDate.setTime(expireDate.getTime() + 5 * 60 * 1000);

                setCookie(PinLoc, "userPinLocTim", expireDate.toGMTString(), "/");
                
                deleteCookie(uidErrCoun, "/");

                return true;

            }

        }

    }

    else {

        return true;

    }

}
