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
	$('.selctPadTop>p').html($($(this).parent().next('.tableArea').find('.leftTabel table thead tr th')[0]).html());
    
//	$('.selctPadTop>p').html($($(this).parent().next().find('.leftTabel table thead tr th')[0]).html());
	var a = $('.rightTabel thead').height();
	$('.leftTabel thead th').css('height',a);
	$('.leftTabel thead th').css('min-height','42px');
	
	setSotitle();
	moveX('YearTotalRight','YearTotalLeft');
//	屏幕变化添加事件
	$(window).resize(function(){
		
		moveX('YearTotalRight','YearTotalLeft');
	})
});
function leftTabelThHeight(right,left){
	var e = $('#' + right + ' thead').height();
	$('#' + left + ' thead th').css('height',e);
	$('#' + left + ' thead th').css('min-height','42px');
}
//表格标题,每个表格加载完成添加
function setSotitle(){
	$('.selctPadTop>p').each(function(){
		$(this).html($($(this).parent().siblings('.tableArea').find('.leftTabel table thead tr th')[0]).html());
	});
}//表格标题结束
//同步滚动,每个表格加载完成添加,季度表格不需要
function moveTop(right,left){
    $("#" + left).removeAttr("onScroll");
    $("#" + left).scrollTop($("#" + right).scrollTop());
}//同步滚动结束
//X轴滚动条,每个表格加载完成添加,屏幕变化也添加,季度表格不需要
function moveX(right,left){
	//alert(getScrollbarWidth())
	leftTabelThHeight(right,left);
	var a = $("#" + right).height();
	var b = $("#" + right + " table").height();
	var c = $("#" + right).width();
	var d = $("#" + right + " table").width();
	if(c < d){
		$("#" + left).css("max-height",a - getScrollbarWidth()+ 1);
	}else{
		$("#" + left).css("max-height",a);
	}
}//X轴滚动结束
//获取滚动条宽度
function getScrollbarWidth() {
    var oP = document.createElement('p'),
        styles = {
            width: '100px',
            height: '100px',
            overflowY: 'scroll'
        }, i, scrollbarWidth;
    for (i in styles) oP.style[i] = styles[i];
    document.body.appendChild(oP);
    scrollbarWidth = oP.offsetWidth - oP.clientWidth;
    oP.remove();
    return scrollbarWidth;
}