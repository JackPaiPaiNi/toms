$(document).ready(function(){
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
	
	$('#Query_select').change(function(){
		var Query_select = $('#Query_select').val();
		if(Query_select == '1'){
			$('#Query_Result').show();
			$('#caption_i').text('4');
		}
	});
	$('#Select_message').click(function(){
		$('#Messselection').show();
	});
	$('#Messselection_close').click(function(){
		$('#Messselection').hide();
	});
	$('#Message_selection').click(function(){
		$('#cssraindemo_ty').show();
	});
//	选中消息
	$('.cssraindemo2 input[type="checkbox"]:checked').parents('tr').addClass('selected');
	$('.cssraindemo2 tbody tr').click(
	  	function() {
	   	if ($(this).hasClass('selected')) {
	    	$(this).removeClass('selected');
	   	 	$(this).find('input[type="checkbox"]').removeAttr('checked');
	    } else {
	    $(this).addClass('selected');
	    $(this).find('input[type="checkbox"]').attr('checked','checked');
	   }
	});
});

