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
//  表格高度不够出现滚动条
	var Lefttablehi = $('#test_Left table').height();
	var testLefthi = $('#test_Left').height();
    if(Lefttablehi > testLefthi){
    	$('#test_Right').css('width','calc(100vw - 950px)');
    	$('#test_Left').css('width','calc(100vw - 933px)');
    }
    
//	季 //	可拖动表格表头和内容宽度
	var tbodywitwo = $('#test_Left2 table').width();
	var tboodywitwo = $('#test_Right2 table').width();
    $('#test_Right2 table').css('width',tbodywitwo);
    $('#test_Left2 table').css('width',tboodywitwo);
//  表格高度不够出现滚动条
	var tablehiTwo = $('#test_Left2 table').height();
	var LefthiTwo = $('#test_Left2').height();
    if(tablehiTwo > LefthiTwo){
    	$('#test_Right2').css('width','calc(100vw - 960px)');
    	$('#test_Left2').css('width','calc(100vw - 943px)');
    }
    
//	月 //	可拖动表格表头和内容宽度
	var tbodywiTre = $('#test_Left3 table').width();
	var tboodywiTre = $('#test_Right3 table').width();
    $('#test_Right3 table').css('width',tbodywiTre);
    $('#test_Left3 table').css('width',tboodywiTre);
//  表格高度不够出现滚动条
	var LefttablehiTre = $('#test_Left3 table').height();
	var testLefthiTre = $('#test_Left3').height();
    if(LefttablehiTre > testLefthiTre){
    	$('#test_Right3').css('width','calc(100vw - 960px)');
    	$('#test_Left3').css('width','calc(100vw - 943px)');
    }
    
//	自定义 //	可拖动表格表头和内容宽度
	var tbodywiFor = $('#test_Left4 table').width();
	var tboodywiFor = $('#test_Right4 table').width();
    $('#test_Right4 table').css('width',tbodywiFor);
    $('#test_Left4 table').css('width',tboodywiFor);
//  表格高度不够出现滚动条
	var LefttablehiFor = $('#test_Left4 table').height();
	var testLefthiFor = $('#test_Left4').height();
    if(LefttablehiFor > testLefthiFor){
    	$('#test_Right4').css('width','calc(100vw - 960px)');
    	$('#test_Left4').css('width','calc(100vw - 943px)');
    }
});

//JS代码
var timer = null;
//左侧DIV的滚动事件
function moveLeft_Leftlalal(){
    $("#test_Right").removeAttr("onScroll");
    $("#test_Right").scrollLeft($("#test_Left").scrollLeft());
    $("#test_Head").removeAttr("onScroll");
    $("#test_Head tbody").scrollTop($("#test_Left").scrollTop());
//     取消延迟预约。【重点】鼠标滚动过程中会多次触发本行代码，相当于不停的延迟执行下面的预约
    clearTimeout(timer);
 //     延迟恢复（预约）另一个DIV的滚动事件，并将本预约返回给变量[timer]
//  timer = setTimeout(function() {
//      $("#test_Right").attr("onScroll","moveLeft_Right();");
//  }, 300 );
}
function moveLeft_Lefttwo(){
    $("#test_Right2").removeAttr("onScroll");
    $("#test_Right2").scrollLeft($("#test_Left2").scrollLeft());
    $("#test_Head2").removeAttr("onScroll");
    $("#test_Head2 tbody").scrollTop($("#test_Left2").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Lefttre(){
    $("#test_Right3").removeAttr("onScroll");
    $("#test_Right3").scrollLeft($("#test_Left3").scrollLeft());
    $("#test_Head3").removeAttr("onScroll");
    $("#test_Head3 tbody").scrollTop($("#test_Left3").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Leftfor(){
    $("#test_Right4").removeAttr("onScroll");
    $("#test_Right4").scrollLeft($("#test_Left4").scrollLeft());
    $("#test_Head4").removeAttr("onScroll");
    $("#test_Head4 tbody").scrollTop($("#test_Left4").scrollTop());
    clearTimeout(timer);
}