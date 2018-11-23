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
	var Lefttablehi = $('#test_Left table').height();
	var testLefthi = $('#test_Left').height();
    if(Lefttablehi > testLefthi){
    	$('#test_Right').css('width','calc(100vw - 870px)');
    	$('#test_Left').css('width','calc(100vw - 853px)');
    }
    
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
    
//	年国家总体
	var stateYearTotal = $('#test_Left6 table').width();
	var stateYearTotal2 = $('#test_Right6 table').width();
    $('#test_Right6 table').css('width',stateYearTotal);
    $('#test_Left6 table').css('width',stateYearTotal2);
//  表格高度不够出现滚动条
	var stateYearLeft = $('#test_Left6 table').height();
	var stateYearLeft2 = $('#test_Left6').height();
    if(stateYearLeft > stateYearLeft2){
    	$('#test_Right6').css('width','calc(100vw - 870px)');
    	$('#test_Left6').css('width','calc(100vw - 853px)');
    }
    
//	年国家UD
	var udYearTotal = $('#test_Left7 table').width();
	var udYearTotal2 = $('#test_Right7 table').width();
    $('#test_Right7 table').css('width',udYearTotal);
    $('#test_Left7 table').css('width',udYearTotal2);
//  表格高度不够出现滚动条
	var udYearLeft = $('#test_Left7 table').height();
	var udYearLeft2 = $('#test_Left7').height();
    if(udYearLeft > udYearLeft2){
    	$('#test_Right7').css('width','calc(100vw - 870px)');
    	$('#test_Left7').css('width','calc(100vw - 853px)');
    }
    
    //	年国家Smart
	var smartYearTotal = $('#test_Left8 table').width();
	var smartYearTotal2 = $('#test_Right8 table').width();
    $('#test_Right8 table').css('width',smartYearTotal);
    $('#test_Left8 table').css('width',smartYearTotal2);
//  表格高度不够出现滚动条
	var smartYearLeft = $('#test_Left8 table').height();
	var smartYearLeft2 = $('#test_Left8').height();
    if(smartYearLeft > smartYearLeft2){
    	$('#test_Right8').css('width','calc(100vw - 870px)');
    	$('#test_Left8').css('width','calc(100vw - 853px)');
    }
    
	var curveYearTotal = $('#test_Left9 table').width();
	var curveYearTotal2 = $('#test_Right9 table').width();
    $('#test_Right9 table').css('width',curveYearTotal);
    $('#test_Left9 table').css('width',curveYearTotal2);
	var curveYearLeft = $('#test_Left9 table').height();
	var curveYearLeft2 = $('#test_Left9').height();
    if(curveYearLeft > curveYearLeft2){
    	$('#test_Right9').css('width','calc(100vw - 870px)');
    	$('#test_Left9').css('width','calc(100vw - 853px)');
    }
    
    var udMouTotal = $('#test_Left10 table').width();
	var udMouTotal2 = $('#test_Right10 table').width();
    $('#test_Right10 table').css('width',udMouTotal);
    $('#test_Left10 table').css('width',udMouTotal2);
	var udMouLeft = $('#test_Left10 table').height();
	var udMouLeft2 = $('#test_Left10').height();
    if(udMouLeft > udMouLeft2){
    	$('#test_Right10').css('width','calc(100vw - 870px)');
    	$('#test_Left10').css('width','calc(100vw - 853px)');
    }
    
    var xcpMouTotal = $('#test_Left11 table').width();
	var xcpMouTotal2 = $('#test_Right11 table').width();
    $('#test_Right11 table').css('width',xcpMouTotal);
    $('#test_Left11 table').css('width',xcpMouTotal2);
	var xcpMouLeft = $('#test_Left11 table').height();
	var xcpMouLeft2 = $('#test_Left11').height();
    if(xcpMouLeft > xcpMouLeft2){
    	$('#test_Right11').css('width','calc(100vw - 870px)');
    	$('#test_Left11').css('width','calc(100vw - 853px)');
    }
    
    var smaMouTotal = $('#test_Left12 table').width();
	var smaMouTotal2 = $('#test_Right12 table').width();
    $('#test_Right12 table').css('width',smaMouTotal);
    $('#test_Left12 table').css('width',smaMouTotal2);
	var smaMouLeft = $('#test_Left12 table').height();
	var smaMouLeft2 = $('#test_Left12').height();
    if(smaMouLeft > smaMouLeft2){
    	$('#test_Right12').css('width','calc(100vw - 870px)');
    	$('#test_Left12').css('width','calc(100vw - 853px)');
    }
    
    var bigMouTotal = $('#test_Left13 table').width();
	var bigMouTotal2 = $('#test_Right13 table').width();
    $('#test_Right13 table').css('width',bigMouTotal);
    $('#test_Left13 table').css('width',bigMouTotal2);
	var bigMouLeft = $('#test_Left13 table').height();
	var bigMouLeft2 = $('#test_Left13').height();
    if(bigMouLeft > bigMouLeft2){
    	$('#test_Right13').css('width','calc(100vw - 870px)');
    	$('#test_Left13').css('width','calc(100vw - 853px)');
    }
    
    var curMouTotal = $('#test_Left14 table').width();
	var curMouTotal2 = $('#test_Right14 table').width();
    $('#test_Right14 table').css('width',curMouTotal);
    $('#test_Left14 table').css('width',curMouTotal2);
	var curMouLeft = $('#test_Left14 table').height();
	var curMouLeft2 = $('#test_Left14').height();
    if(curMouLeft > curMouLeft2){
    	$('#test_Right14').css('width','calc(100vw - 870px)');
    	$('#test_Left14').css('width','calc(100vw - 853px)');
    }
    
    var xcpafMouTotal = $('#test_Left15 table').width();
	var xcpafMouTotal2 = $('#test_Right15 table').width();
    $('#test_Right15 table').css('width',xcpafMouTotal);
    $('#test_Left15 table').css('width',xcpafMouTotal2);
	var xcpafMouLeft = $('#test_Left15 table').height();
	var xcpafMouLeft2 = $('#test_Left15').height();
    if(xcpafMouLeft > xcpafMouLeft2){
    	$('#test_Right15').css('width','calc(100vw - 870px)');
    	$('#test_Left15').css('width','calc(100vw - 853px)');
    }
    
    var smaafMouTotal = $('#test_Left16 table').width();
	var smapafMouTotal2 = $('#test_Right16 table').width();
    $('#test_Right16 table').css('width',smaafMouTotal);
    $('#test_Left16 table').css('width',smapafMouTotal2);
	var smapafMouLeft = $('#test_Left16 table').height();
	var smaafMouLeft2 = $('#test_Left16').height();
    if(smapafMouLeft > smaafMouLeft2){
    	$('#test_Right16').css('width','calc(100vw - 870px)');
    	$('#test_Left16').css('width','calc(100vw - 853px)');
    }
    
    var bigafMouTotal = $('#test_Left17 table').width();
	var bigpafMouTotal2 = $('#test_Right17 table').width();
    $('#test_Right17 table').css('width',bigafMouTotal);
    $('#test_Left17 table').css('width',bigpafMouTotal2);
	var bigpafMouLeft = $('#test_Left17 table').height();
	var bigafMouLeft2 = $('#test_Left17').height();
    if(bigpafMouLeft > bigafMouLeft2){
    	$('#test_Right17').css('width','calc(100vw - 870px)');
    	$('#test_Left17').css('width','calc(100vw - 853px)');
    }
    
    var curafMouTotal = $('#test_Left18 table').width();
	var curpafMouTotal2 = $('#test_Right18 table').width();
    $('#test_Right18 table').css('width',curafMouTotal);
    $('#test_Left18 table').css('width',curpafMouTotal2);
	var curpafMouLeft = $('#test_Left18 table').height();
	var curafMouLeft2 = $('#test_Left18').height();
    if(curpafMouLeft > curafMouLeft2){
    	$('#test_Right18').css('width','calc(100vw - 870px)');
    	$('#test_Left18').css('width','calc(100vw - 853px)');
    }
    
    var urafMouTotal = $('#test_Left19 table').width();
	var urpafMouTotal2 = $('#test_Right19 table').width();
    $('#test_Right19 table').css('width',urafMouTotal);
    $('#test_Left19 table').css('width',urpafMouTotal2);
	var urpafMouLeft = $('#test_Left19 table').height();
	var urafMouLeft2 = $('#test_Left19').height();
    if(urpafMouLeft > urafMouLeft2){
    	$('#test_Right19').css('width','calc(100vw - 870px)');
    	$('#test_Left19').css('width','calc(100vw - 853px)');
    }
    
    var udafMouTotal = $('#test_Left20 table').width();
	var udpafMouTotal2 = $('#test_Right20 table').width();
    $('#test_Right20 table').css('width',udafMouTotal);
    $('#test_Left20 table').css('width',udpafMouTotal2);
	var udpafMouLeft = $('#test_Left20 table').height();
	var udafMouLeft2 = $('#test_Left20').height();
    if(udpafMouLeft > udafMouLeft2){
    	$('#test_Right20').css('width','calc(100vw - 870px)');
    	$('#test_Left20').css('width','calc(100vw - 853px)');
    }
    
    var xcpafMouTotal = $('#test_Left21 table').width();
	var xcppafMouTotal2 = $('#test_Right21 table').width();
    $('#test_Right21 table').css('width',xcpafMouTotal);
    $('#test_Left21 table').css('width',xcppafMouTotal2);
	var xcppafMouLeft = $('#test_Left21 table').height();
	var xcpafMouLeft2 = $('#test_Left21').height();
    if(xcppafMouLeft > xcpafMouLeft2){
    	$('#test_Right21').css('width','calc(100vw - 870px)');
    	$('#test_Left21').css('width','calc(100vw - 853px)');
    }
    
    var smafMouTotal = $('#test_Left22 table').width();
	var smppafMouTotal2 = $('#test_Right22 table').width();
    $('#test_Right22 table').css('width',smafMouTotal);
    $('#test_Left22 table').css('width',smppafMouTotal2);
	var smpafMouLeft = $('#test_Left22 table').height();
	var smafMouLeft2 = $('#test_Left22').height();
    if(smpafMouLeft > smafMouLeft2){
    	$('#test_Right22').css('width','calc(100vw - 870px)');
    	$('#test_Left22').css('width','calc(100vw - 853px)');
    }
    
    var cuafMouTotal = $('#test_Left23 table').width();
	var cuppafMouTotal2 = $('#test_Right23 table').width();
    $('#test_Right23 table').css('width',cuafMouTotal);
    $('#test_Left23 table').css('width',cuppafMouTotal2);
	var cupafMouLeft = $('#test_Left23 table').height();
	var cuafMouLeft2 = $('#test_Left23').height();
    if(cupafMouLeft > cuafMouLeft2){
    	$('#test_Right23').css('width','calc(100vw - 870px)');
    	$('#test_Left23').css('width','calc(100vw - 853px)');
    }
    
    var udWeekTotal = $('#test_Left24 table').width();
	var udWeekTotal2 = $('#test_Right24 table').width();
    $('#test_Right24 table').css('width',udWeekTotal);
    $('#test_Left24 table').css('width',udWeekTotal2);
	var udWeekLeft = $('#test_Left24 table').height();
	var udWeekLeft2 = $('#test_Left24').height();
    if(udWeekLeft > udWeekLeft2){
    	$('#test_Right24').css('width','calc(100vw - 870px)');
    	$('#test_Left24').css('width','calc(100vw - 853px)');
    }
    
    var xcWeekTotal = $('#test_Left25 table').width();
	var xcWeekTotal2 = $('#test_Right25 table').width();
    $('#test_Right25 table').css('width',xcWeekTotal);
    $('#test_Left25 table').css('width',xcWeekTotal2);
	var xcWeekLeft = $('#test_Left25 table').height();
	var xcWeekLeft2 = $('#test_Left25').height();
    if(xcWeekLeft > xcWeekLeft2){
    	$('#test_Right25').css('width','calc(100vw - 870px)');
    	$('#test_Left25').css('width','calc(100vw - 853px)');
    }
    
    var smWeekTotal = $('#test_Left26 table').width();
	var smWeekTotal2 = $('#test_Right26 table').width();
    $('#test_Right26 table').css('width',smWeekTotal);
    $('#test_Left26 table').css('width',smWeekTotal2);
	var smWeekLeft = $('#test_Left26 table').height();
	var smWeekLeft2 = $('#test_Left26').height();
    if(smWeekLeft > smWeekLeft2){
    	$('#test_Right26').css('width','calc(100vw - 870px)');
    	$('#test_Left26').css('width','calc(100vw - 853px)');
    }
    
    var biWeekTotal = $('#test_Left27 table').width();
	var biWeekTotal2 = $('#test_Right27 table').width();
    $('#test_Right27 table').css('width',biWeekTotal);
    $('#test_Left27 table').css('width',biWeekTotal2);
	var biWeekLeft = $('#test_Left27 table').height();
	var biWeekLeft2 = $('#test_Left27').height();
    if(biWeekLeft > biWeekLeft2){
    	$('#test_Right27').css('width','calc(100vw - 870px)');
    	$('#test_Left27').css('width','calc(100vw - 853px)');
    }
    
    var cuWeekTotal = $('#test_Left28 table').width();
	var cuWeekTotal2 = $('#test_Right28 table').width();
    $('#test_Right28 table').css('width',cuWeekTotal);
    $('#test_Left28 table').css('width',cuWeekTotal2);
	var cuWeekLeft = $('#test_Left28 table').height();
	var cuWeekLeft2 = $('#test_Left28').height();
    if(cuWeekLeft > cuWeekLeft2){
    	$('#test_Right28').css('width','calc(100vw - 870px)');
    	$('#test_Left28').css('width','calc(100vw - 853px)');
    }
    
    var uWeekTotal = $('#test_Left29 table').width();
	var uWeekTotal2 = $('#test_Right29 table').width();
    $('#test_Right29 table').css('width',uWeekTotal);
    $('#test_Left29 table').css('width',uWeekTotal2);
	var uWeekLeft = $('#test_Left29 table').height();
	var uWeekLeft2 = $('#test_Left29').height();
    if(uWeekLeft > uWeekLeft2){
    	$('#test_Right29').css('width','calc(100vw - 870px)');
    	$('#test_Left29').css('width','calc(100vw - 853px)');
    }
    
    var xcpuWeekTotal = $('#test_Left30 table').width();
	var xcpuWeekTotal2 = $('#test_Right30 table').width();
    $('#test_Right30 table').css('width',xcpuWeekTotal);
    $('#test_Left30 table').css('width',xcpuWeekTotal2);
	var xcpuWeekLeft = $('#test_Left30 table').height();
	var xcpuWeekLeft2 = $('#test_Left30').height();
    if(xcpuWeekLeft > xcpuWeekLeft2){
    	$('#test_Right30').css('width','calc(100vw - 870px)');
    	$('#test_Left30').css('width','calc(100vw - 853px)');
    }
    
    var puWeekTotal = $('#test_Left31 table').width();
	var puWeekTotal2 = $('#test_Right31 table').width();
    $('#test_Right31 table').css('width',puWeekTotal);
    $('#test_Left31 table').css('width',puWeekTotal2);
	var puWeekLeft = $('#test_Left31 table').height();
	var puWeekLeft2 = $('#test_Left31').height();
    if(puWeekLeft > puWeekLeft2){
    	$('#test_Right31').css('width','calc(100vw - 870px)');
    	$('#test_Left31').css('width','calc(100vw - 853px)');
    }
    
    var bpuWeekTotal = $('#test_Left32 table').width();
	var bpuWeekTotal2 = $('#test_Right32 table').width();
    $('#test_Right32 table').css('width',bpuWeekTotal);
    $('#test_Left32 table').css('width',bpuWeekTotal2);
	var bpuWeekLeft = $('#test_Left32 table').height();
	var bpuWeekLeft2 = $('#test_Left32').height();
    if(bpuWeekLeft > bpuWeekLeft2){
    	$('#test_Right32').css('width','calc(100vw - 870px)');
    	$('#test_Left32').css('width','calc(100vw - 853px)');
    }
    
    var cubpuWeekTotal = $('#test_Left33 table').width();
	var cubpuWeekTotal2 = $('#test_Right33 table').width();
    $('#test_Right33 table').css('width',cubpuWeekTotal);
    $('#test_Left33 table').css('width',cubpuWeekTotal2);
	var cubpuWeekLeft = $('#test_Left33 table').height();
	var cubpuWeekLeft2 = $('#test_Left33').height();
    if(cubpuWeekLeft > cubpuWeekLeft2){
    	$('#test_Right33').css('width','calc(100vw - 870px)');
    	$('#test_Left33').css('width','calc(100vw - 853px)');
    }
    
    var ciuWeekTotal = $('#test_Left34 table').width();
	var ciuWeekTotal2 = $('#test_Right34 table').width();
    $('#test_Right34 table').css('width',ciuWeekTotal);
    $('#test_Left34 table').css('width',ciuWeekTotal2);
	var ciuWeekLeft = $('#test_Left34 table').height();
	var ciuWeekLeft2 = $('#test_Left34').height();
    if(ciuWeekLeft > ciuWeekLeft2){
    	$('#test_Right34').css('width','calc(100vw - 870px)');
    	$('#test_Left34').css('width','calc(100vw - 853px)');
    }
    
    var cieuWeekTotal = $('#test_Left35 table').width();
	var cieuWeekTotal2 = $('#test_Right35 table').width();
    $('#test_Right35 table').css('width',cieuWeekTotal);
    $('#test_Left35 table').css('width',cieuWeekTotal2);
	var cieuWeekLeft = $('#test_Left35 table').height();
	var cieuWeekLeft2 = $('#test_Left35').height();
    if(cieuWeekLeft > cieuWeekLeft2){
    	$('#test_Right35').css('width','calc(100vw - 870px)');
    	$('#test_Left35').css('width','calc(100vw - 853px)');
    }
    
    var cieuWewekTotal = $('#test_Left36 table').width();
	var cieuWewekTotal2 = $('#test_Right36 table').width();
    $('#test_Right36 table').css('width',cieuWewekTotal);
    $('#test_Left36 table').css('width',cieuWewekTotal2);
	var cieuWewekLeft = $('#test_Left36 table').height();
	var cieuWewekLeft2 = $('#test_Left36').height();
    if(cieuWewekLeft > cieuWewekLeft2){
    	$('#test_Right36').css('width','calc(100vw - 870px)');
    	$('#test_Left36').css('width','calc(100vw - 853px)');
    }
    
    var cieuWewekTyotal = $('#test_Left37 table').width();
	var cieuWewekTyotal2 = $('#test_Right37 table').width();
    $('#test_Right37 table').css('width',cieuWewekTyotal);
    $('#test_Left37 table').css('width',cieuWewekTyotal2);
	var cieuWewekyLeft = $('#test_Left37 table').height();
	var cieuWewekyLeft2 = $('#test_Left37').height();
    if(cieuWewekyLeft > cieuWewekyLeft2){
    	$('#test_Right37').css('width','calc(100vw - 870px)');
    	$('#test_Left37').css('width','calc(100vw - 853px)');
    }
    
     var cieuWeweekTyotal = $('#test_Left38 table').width();
	var cieuWeweekTyotal2 = $('#test_Right38 table').width();
    $('#test_Right38 table').css('width',cieuWeweekTyotal);
    $('#test_Left38 table').css('width',cieuWeweekTyotal2);
	var cieuWeweekyLeft = $('#test_Left38 table').height();
	var cieuWeweekyLeft2 = $('#test_Left38').height();
    if(cieuWeweekyLeft > cieuWeweekyLeft2){
    	$('#test_Right38').css('width','calc(100vw - 870px)');
    	$('#test_Left38').css('width','calc(100vw - 853px)');
    }
    
    var ciuWeweekTyotal = $('#test_Left39 table').width();
	var ciuWeweekTyotal2 = $('#test_Right39 table').width();
    $('#test_Right39 table').css('width',ciuWeweekTyotal);
    $('#test_Left39 table').css('width',ciuWeweekTyotal2);
	var ciuWeweekyLeft = $('#test_Left39 table').height();
	var ciuWeweekyLeft2 = $('#test_Left39').height();
    if(ciuWeweekyLeft > ciuWeweekyLeft2){
    	$('#test_Right39').css('width','calc(100vw - 870px)');
    	$('#test_Left39').css('width','calc(100vw - 853px)');
    }
    
    var cieuWweekTyotal = $('#test_Left40 table').width();
	var cieuWweekTyotal2 = $('#test_Right40 table').width();
    $('#test_Right40 table').css('width',cieuWweekTyotal);
    $('#test_Left40 table').css('width',cieuWweekTyotal2);
	var cieuWweekyLeft = $('#test_Left40 table').height();
	var cieuWweekyLeft2 = $('#test_Left40').height();
    if(cieuWweekyLeft > cieuWweekyLeft2){
    	$('#test_Right40').css('width','calc(100vw - 870px)');
    	$('#test_Left40').css('width','calc(100vw - 853px)');
    }
})
var timer = null;
//	Saleman月份达成
function moveLeft_Leftone(){
    $("#test_Right1").removeAttr("onScroll");
    $("#test_Right1").scrollLeft($("#test_Left1").scrollLeft());
    $("#test_Head1").removeAttr("onScroll");
    $("#test_Head1 tbody").scrollTop($("#test_Left1").scrollTop());
    clearTimeout(timer);
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
//国家年总体
function moveLeft_Leftsix(){
    $("#test_Right6").removeAttr("onScroll");
    $("#test_Right6").scrollLeft($("#test_Left6").scrollLeft());
    $("#test_Head6").removeAttr("onScroll");
    $("#test_Head6 tbody").scrollTop($("#test_Left6").scrollTop());
}
//国家年UD
function moveLeft_Leftseven(){
    $("#test_Right7").removeAttr("onScroll");
    $("#test_Right7").scrollLeft($("#test_Left7").scrollLeft());
    $("#test_Head7").removeAttr("onScroll");
    $("#test_Head7 tbody").scrollTop($("#test_Left7").scrollTop());
}
//国家年Smart
function moveLeft_Lefteight(){
    $("#test_Right8").removeAttr("onScroll");
    $("#test_Right8").scrollLeft($("#test_Left8").scrollLeft());
    $("#test_Head8").removeAttr("onScroll");
    $("#test_Head8 tbody").scrollTop($("#test_Left8").scrollTop());
}
//国家年Curve
function moveLeft_Leftnine(){
    $("#test_Right9").removeAttr("onScroll");
    $("#test_Right9").scrollLeft($("#test_Left9").scrollLeft());
    $("#test_Head9").removeAttr("onScroll");
    $("#test_Head9 tbody").scrollTop($("#test_Left9").scrollTop());
}
//月
function moveLeft_Leftten(){
    $("#test_Right10").removeAttr("onScroll");
    $("#test_Right10").scrollLeft($("#test_Left10").scrollLeft());
    $("#test_Head10").removeAttr("onScroll");
    $("#test_Head10 tbody").scrollTop($("#test_Left10").scrollTop());
}
function moveLeft_Left11(){
    $("#test_Right11").removeAttr("onScroll");
    $("#test_Right11").scrollLeft($("#test_Left11").scrollLeft());
    $("#test_Head11").removeAttr("onScroll");
    $("#test_Head11 tbody").scrollTop($("#test_Left11").scrollTop());
}
function moveLeft_Left12(){
    $("#test_Right12").removeAttr("onScroll");
    $("#test_Right12").scrollLeft($("#test_Left12").scrollLeft());
    $("#test_Head12").removeAttr("onScroll");
    $("#test_Head12 tbody").scrollTop($("#test_Left12").scrollTop());
}
function moveLeft_Left13(){
    $("#test_Right13").removeAttr("onScroll");
    $("#test_Right13").scrollLeft($("#test_Left13").scrollLeft());
    $("#test_Head13").removeAttr("onScroll");
    $("#test_Head13 tbody").scrollTop($("#test_Left13").scrollTop());
}
function moveLeft_Left14(){
    $("#test_Right14").removeAttr("onScroll");
    $("#test_Right14").scrollLeft($("#test_Left14").scrollLeft());
    $("#test_Head14").removeAttr("onScroll");
    $("#test_Head14 tbody").scrollTop($("#test_Left14").scrollTop());
}
function moveLeft_Left15(){
    $("#test_Right15").removeAttr("onScroll");
    $("#test_Right15").scrollLeft($("#test_Left15").scrollLeft());
    $("#test_Head15").removeAttr("onScroll");
    $("#test_Head15 tbody").scrollTop($("#test_Left15").scrollTop());
}
function moveLeft_Left16(){
    $("#test_Right16").removeAttr("onScroll");
    $("#test_Right16").scrollLeft($("#test_Left16").scrollLeft());
    $("#test_Head16").removeAttr("onScroll");
    $("#test_Head16 tbody").scrollTop($("#test_Left16").scrollTop());
}
function moveLeft_Left17(){
    $("#test_Right17").removeAttr("onScroll");
    $("#test_Right17").scrollLeft($("#test_Left17").scrollLeft());
    $("#test_Head17").removeAttr("onScroll");
    $("#test_Head17 tbody").scrollTop($("#test_Left17").scrollTop());
}
function moveLeft_Left18(){
    $("#test_Right18").removeAttr("onScroll");
    $("#test_Right18").scrollLeft($("#test_Left18").scrollLeft());
    $("#test_Head18").removeAttr("onScroll");
    $("#test_Head18 tbody").scrollTop($("#test_Left18").scrollTop());
}
function moveLeft_Left19(){
    $("#test_Right19").removeAttr("onScroll");
    $("#test_Right19").scrollLeft($("#test_Left19").scrollLeft());
    $("#test_Head19").removeAttr("onScroll");
    $("#test_Head19 tbody").scrollTop($("#test_Left19").scrollTop());
}
function moveLeft_Left20(){
    $("#test_Right20").removeAttr("onScroll");
    $("#test_Right20").scrollLeft($("#test_Left20").scrollLeft());
    $("#test_Head20").removeAttr("onScroll");
    $("#test_Head20 tbody").scrollTop($("#test_Left20").scrollTop());
}
function moveLeft_Left21(){
    $("#test_Right21").removeAttr("onScroll");
    $("#test_Right21").scrollLeft($("#test_Left21").scrollLeft());
    $("#test_Head21").removeAttr("onScroll");
    $("#test_Head21 tbody").scrollTop($("#test_Left21").scrollTop());
}
function moveLeft_Left22(){
    $("#test_Right22").removeAttr("onScroll");
    $("#test_Right22").scrollLeft($("#test_Left22").scrollLeft());
    $("#test_Head22").removeAttr("onScroll");
    $("#test_Head22 tbody").scrollTop($("#test_Left22").scrollTop());
}
function moveLeft_Left23(){
    $("#test_Right23").removeAttr("onScroll");
    $("#test_Right23").scrollLeft($("#test_Left23").scrollLeft());
    $("#test_Head23").removeAttr("onScroll");
    $("#test_Head23 tbody").scrollTop($("#test_Left23").scrollTop());
}
function moveLeft_Left24(){
    $("#test_Right24").removeAttr("onScroll");
    $("#test_Right24").scrollLeft($("#test_Left24").scrollLeft());
    $("#test_Head24").removeAttr("onScroll");
    $("#test_Head24 tbody").scrollTop($("#test_Left24").scrollTop());
}
function moveLeft_Left25(){
    $("#test_Right25").removeAttr("onScroll");
    $("#test_Right25").scrollLeft($("#test_Left25").scrollLeft());
    $("#test_Head25").removeAttr("onScroll");
    $("#test_Head25 tbody").scrollTop($("#test_Left25").scrollTop());
}
function moveLeft_Left26(){
    $("#test_Right26").removeAttr("onScroll");
    $("#test_Right26").scrollLeft($("#test_Left26").scrollLeft());
    $("#test_Head26").removeAttr("onScroll");
    $("#test_Head26 tbody").scrollTop($("#test_Left26").scrollTop());
}
function moveLeft_Left27(){
    $("#test_Right27").removeAttr("onScroll");
    $("#test_Right27").scrollLeft($("#test_Left27").scrollLeft());
    $("#test_Head27").removeAttr("onScroll");
    $("#test_Head27 tbody").scrollTop($("#test_Left27").scrollTop());
}
function moveLeft_Left28(){
    $("#test_Right28").removeAttr("onScroll");
    $("#test_Right28").scrollLeft($("#test_Left28").scrollLeft());
    $("#test_Head28").removeAttr("onScroll");
    $("#test_Head28 tbody").scrollTop($("#test_Left28").scrollTop());
}
function moveLeft_Left29(){
    $("#test_Right29").removeAttr("onScroll");
    $("#test_Right29").scrollLeft($("#test_Left29").scrollLeft());
    $("#test_Head29").removeAttr("onScroll");
    $("#test_Head29 tbody").scrollTop($("#test_Left29").scrollTop());
}
function moveLeft_Left30(){
    $("#test_Right30").removeAttr("onScroll");
    $("#test_Right30").scrollLeft($("#test_Left30").scrollLeft());
    $("#test_Head30").removeAttr("onScroll");
    $("#test_Head30 tbody").scrollTop($("#test_Left30").scrollTop());
}
function moveLeft_Left31(){
    $("#test_Right31").removeAttr("onScroll");
    $("#test_Right31").scrollLeft($("#test_Left31").scrollLeft());
    $("#test_Head31").removeAttr("onScroll");
    $("#test_Head31 tbody").scrollTop($("#test_Left31").scrollTop());
}
function moveLeft_Left32(){
    $("#test_Right32").removeAttr("onScroll");
    $("#test_Right32").scrollLeft($("#test_Left32").scrollLeft());
    $("#test_Head32").removeAttr("onScroll");
    $("#test_Head32 tbody").scrollTop($("#test_Left32").scrollTop());
}
function moveLeft_Left33(){
    $("#test_Right33").removeAttr("onScroll");
    $("#test_Right33").scrollLeft($("#test_Left33").scrollLeft());
    $("#test_Head33").removeAttr("onScroll");
    $("#test_Head33 tbody").scrollTop($("#test_Left33").scrollTop());
}
function moveLeft_Left34(){
    $("#test_Right34").removeAttr("onScroll");
    $("#test_Right34").scrollLeft($("#test_Left34").scrollLeft());
    $("#test_Head34").removeAttr("onScroll");
    $("#test_Head34 tbody").scrollTop($("#test_Left34").scrollTop());
}
function moveLeft_Left35(){
    $("#test_Right35").removeAttr("onScroll");
    $("#test_Right35").scrollLeft($("#test_Left35").scrollLeft());
    $("#test_Head35").removeAttr("onScroll");
    $("#test_Head35 tbody").scrollTop($("#test_Left35").scrollTop());
}
function moveLeft_Left36(){
    $("#test_Right36").removeAttr("onScroll");
    $("#test_Right36").scrollLeft($("#test_Left36").scrollLeft());
    $("#test_Head36").removeAttr("onScroll");
    $("#test_Head36 tbody").scrollTop($("#test_Left36").scrollTop());
}
function moveLeft_Left37(){
    $("#test_Right37").removeAttr("onScroll");
    $("#test_Right37").scrollLeft($("#test_Left37").scrollLeft());
    $("#test_Head37").removeAttr("onScroll");
    $("#test_Head37 tbody").scrollTop($("#test_Left37").scrollTop());
}
function moveLeft_Left38(){
    $("#test_Right38").removeAttr("onScroll");
    $("#test_Right38").scrollLeft($("#test_Left38").scrollLeft());
    $("#test_Head38").removeAttr("onScroll");
    $("#test_Head38 tbody").scrollTop($("#test_Left38").scrollTop());
}
function moveLeft_Left39(){
    $("#test_Right39").removeAttr("onScroll");
    $("#test_Right39").scrollLeft($("#test_Left39").scrollLeft());
    $("#test_Head39").removeAttr("onScroll");
    $("#test_Head39 tbody").scrollTop($("#test_Left39").scrollTop());
}
function moveLeft_Left40(){
    $("#test_Right40").removeAttr("onScroll");
    $("#test_Right40").scrollLeft($("#test_Left40").scrollLeft());
    $("#test_Head40").removeAttr("onScroll");
    $("#test_Head40 tbody").scrollTop($("#test_Left40").scrollTop());
}