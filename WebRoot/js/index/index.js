
$(function(){

	 $.ajax({
			url:baseUrl + "platform/passwordExpired.action",
			type:"POST"
		}).success(function(data){
		if(data!=null && data!=""){
			if(data.msg!="NO"){
				var txt= data.msg;
				window.wxc.xcConfirm(txt, window.wxc.xcConfirm.typeEnum.info);
				
				//alert(data.msg);
			}
			
		}
		
				
			
		});
	
});
function loginOut() {
	$("#loginout").click(function() {
		window.location = baseUrl + "loginOut.action";
	});
}

