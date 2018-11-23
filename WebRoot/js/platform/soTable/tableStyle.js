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

	
//	Acfo月份达成 
	var tbodywi = $('#test_Left table').width();
	var tboodywi = $('#test_Right table').width();
    $('#test_Right table').css('width',tbodywi);
    $('#test_Left table').css('width',tboodywi);
//  表格高度不够出现滚动条
	/*var Lefttablehi = $('#test_Left table').height();
	var testLefthi = $('#test_Left').height();
    if(Lefttablehi > testLefthi){
    	$('#test_Right').css('width','calc(100vw - 870px)');
    	$('#test_Left').css('width','calc(100vw - 853px)');
    }*/
    
//	Saleman周达成
	var salemanWeekTotal = $('#test_Left2 table').width();
	var salemanWeekTotal2 = $('#test_Right2 table').width();
    $('#test_Right2 table').css('width',salemanWeekTotal);
    $('#test_Left2 table').css('width',salemanWeekTotal2);
//  表格高度不够出现滚动条
	var salemanWeekLeft = $('#test_Left2 table').height();
	var salemanWeekLeft2 = $('#test_Left2').height();
    if(salemanWeekLeft > salemanWeekLeft2){
    	$('#test_Right2').css('width','calc(100vw - 870px)');
    	$('#test_Left2').css('width','calc(100vw - 853px)');
    }
    
//	Acfo周达成
	var acfoWeekTotal = $('#test_Left3 table').width();
	var acfoWeekTotal2 = $('#test_Right3 table').width();
    $('#test_Right3 table').css('width',acfoWeekTotal);
    $('#test_Left3 table').css('width',acfoWeekTotal2);
//  表格高度不够出现滚动条
	var acfoWeekLeft = $('#test_Left3 table').height();
	var acfoWeekLeft2 = $('#test_Left3').height();
    if(acfoWeekLeft > acfoWeekLeft2){
    	$('#test_Right3').css('width','calc(100vw - 870px)');
    	$('#test_Left3').css('width','calc(100vw - 853px)');
    }
    
//	年国家xcp
	var xcpYearTotal = $('#test_Left4 table').width();
	var xcpYearTotal2 = $('#test_Right4 table').width();
    $('#test_Right4 table').css('width',xcpYearTotal);
    $('#test_Left4 table').css('width',xcpYearTotal2);
//  表格高度不够出现滚动条
	var xcpYearLeft = $('#test_Left4 table').height();
	var xcpYearLeft2 = $('#test_Left4').height();
    if(xcpYearLeft > xcpYearLeft2){
    	$('#test_Right4').css('width','calc(100vw - 870px)');
    	$('#test_Left4').css('width','calc(100vw - 853px)');
    }
    
//	月UD整体Acfo
	var udMounthTotal = $('#test_Left5 table').width();
	var udMounthTotal2 = $('#test_Right5 table').width();
    $('#test_Right5 table').css('width',udMounthTotal);
    $('#test_Left5 table').css('width',udMounthTotal2);
//  表格高度不够出现滚动条
	var udMounthLeft = $('#test_Left5 table').height();
	var udMounthLeft2 = $('#test_Left5').height();
    if(udMounthLeft > udMounthLeft2){
    	$('#test_Right5').css('width','calc(100vw - 870px)');
    	$('#test_Left5').css('width','calc(100vw - 853px)');
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
//  Acfo月份达成
function moveLeft_Left(){
    $("#test_Right").removeAttr("onScroll");
    $("#test_Right").scrollLeft($("#test_Left").scrollLeft());
    $("#test_Head").removeAttr("onScroll");
    $("#test_Head tbody").scrollTop($("#test_Left").scrollTop());
    clearTimeout(timer);
}
//	Saleman周达成
function moveLeft_Lefttwo(){
    $("#test_Right2").removeAttr("onScroll");
    $("#test_Right2").scrollLeft($("#test_Left2").scrollLeft());
    $("#test_Head2").removeAttr("onScroll");
    $("#test_Head2 tbody").scrollTop($("#test_Left2").scrollTop());
}
//  Acfo周达成
function moveLeft_Leftthir(){
    $("#test_Right3").removeAttr("onScroll");
    $("#test_Right3").scrollLeft($("#test_Left3").scrollLeft());
    $("#test_Head3").removeAttr("onScroll");
    $("#test_Head3 tbody").scrollTop($("#test_Left3").scrollTop());
    clearTimeout(timer);
}
//年国家XCP单品
function moveLeft_Leftfou(){
    $("#test_Right4").removeAttr("onScroll");
    $("#test_Right4").scrollLeft($("#test_Left4").scrollLeft());
    $("#test_Head4").removeAttr("onScroll");
    $("#test_Head4 tbody").scrollTop($("#test_Left4").scrollTop());
    clearTimeout(timer);
}
//月UD整体Acfo
function moveLeft_Leftfive(){
    $("#test_Right5").removeAttr("onScroll");
    $("#test_Right5").scrollLeft($("#test_Left5").scrollLeft());
    $("#test_Head5").removeAttr("onScroll");
    $("#test_Head5 tbody").scrollTop($("#test_Left5").scrollTop());
    clearTimeout(timer);
}