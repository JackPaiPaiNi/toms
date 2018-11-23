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
	$('.search_SI div ul li').click(function(){
		$(this).children("input[type='checkbox']").click();
	})
	
//	年季月周切换
	$(function(){
		$('#prizes').tabs();
	})
//	年 //	可拖动表格表头和内容宽度
	var tbodywi = $('#test_Left table').width();
	var tboodywi = $('#test_Right table').width();
    $('#test_Right table').css('width',tbodywi);
    $('#test_Left table').css('width',tboodywi);

});


