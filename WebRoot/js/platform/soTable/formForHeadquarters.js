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
	});
//	年季月周切换
	$(function(){
		$('#prizes').tabs();
	});
	$(function(){
		$('#tabs-1').tabs();
	});
	$(function(){
		$('#tabs_1_1,#tabs_1_2,#tabs_1_3,#tabs_1_4,#tabs_1_5').tabs();
	});
	$(function(){
		$('#tabs-2').tabs();
	});
	$(function(){
		$('#tabs_2_1,#tabs_2_2,#tabs_2_3,#tabs_2_4,#tabs_2_5').tabs();
	});
	$(function(){
		$('#tabs-3').tabs();
	});
	$(function(){
		$('#tabs_3_1,#tabs_3_2,#tabs_3_3,#tabs_3_4,#tabs_3_5').tabs();
	});
  /*  $('.selctPadTop button').click(function(){
    	$(this).next('p').html($($(this).parent().next().find('table thead tr th')[0]).html());
    })*/
//  $('.selctPadTop>p').html($($(this).parent().next().find('table thead tr th')[0]).html());
});


function mounthRirht(){
	var witTabs211 = $('#tabs_2_1_1>table').width();
	var wit2Tabs211 = $('#tabs_2_1_1').width();
	$('#tabs_2_1_1 .MRH').css('width',wit2Tabs211 - witTabs211 - 10);
}
function weekRirht(){
	var witTabs311 = $('#tabs_3_1_1>table').width();
	var wit2Tabs311 = $('#tabs_3_1_1').width();
	$('#tabs_3_1_1 .WRH').css('width',wit2Tabs311 - witTabs311 - 10);
}
