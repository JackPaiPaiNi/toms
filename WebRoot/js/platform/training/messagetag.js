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
//	$('.main table tbody tr').click(
//	  function() {
//	  	$(this).addClass('selected');
//	    $(this).siblings().removeClass('selected');	   
//	  }
//	 );
});

