$(document).ready(function(){
	//	Saleman月份达成
	var salemanTotal = $('#test_Left1 table').width();
	var salemanTotal2 = $('#test_Right1 table').width();
    $('#test_Right1 table').css('width',salemanTotal);
    $('#test_Left1 table').css('width',salemanTotal2);
//  表格高度不够出现滚动条
	var salemanLefttablehi = $('#test_Left1 table').height();
	var salemantestLefthi = $('#test_Left1').height();
    if(salemanLefttablehi > salemantestLefthi){
    	$('#test_Right1').css('width','calc(100vw - 870px)');
    	$('#test_Left1').css('width','calc(100vw - 853px)');
    }
})
var timer = null;
//	Saleman月份达成
function moveLeft_Leftone(){
    $("#test_Right1").removeAttr("onScroll");
    $("#test_Right1").scrollLeft($("#test_Left1").scrollLeft());
    $("#test_Head1").removeAttr("onScroll");
    $("#test_Head1 tbody").scrollTop($("#test_Left1").scrollTop());
//     取消延迟预约。【重点】鼠标滚动过程中会多次触发本行代码，相当于不停的延迟执行下面的预约
    clearTimeout(timer);
 //     延迟恢复（预约）另一个DIV的滚动事件，并将本预约返回给变量[timer]
//  timer = setTimeout(function() {
//      $("#test_Right").attr("onScroll","moveLeft_Right();");
//  }, 300 );
}
