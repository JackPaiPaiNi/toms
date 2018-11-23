$(document).ready(function(){
//	Saleman月份增长
	var salemanTotal = $('#test_Left1 table').width();
	var salemanTotal2 = $('#test_Right1 table').width();
    $('#test_Right1 table').css('width',salemanTotal);
    $('#test_Left1 table').css('width',salemanTotal2);
//  表格高度不够出现滚动条
	var salemanLefttablehi = $('#test_Left1 table').height();
	var salemantestLefthi = $('#test_Left1').height();
    if(salemanLefttablehi > salemantestLefthi){
    	$('#test_Right1').css('width','calc(100vw - 850px)');
    	$('#test_Left1').css('width','calc(100vw - 833px)');
    }

//	Acfo月份增长
	var tbodywi = $('#test_Left table').width();
	var tboodywi = $('#test_Right table').width();
    $('#test_Right table').css('width',tbodywi);
    $('#test_Left table').css('width',tboodywi);
//  表格高度不够出现滚动条
	var Lefttablehi = $('#test_Left table').height();
	var testLefthi = $('#test_Left').height();
    if(Lefttablehi > testLefthi){
    	$('#test_Right').css('width','calc(100vw - 850px)');
    	$('#test_Left').css('width','calc(100vw - 833px)');
    }
    
//	Saleman周增长
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
    
//	Acfo周增长
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
    
//	DEALER周增长
	var dealerWeekTotal = $('#test_Left4 table').width();
	var dealerWeekTotal2 = $('#test_Right4 table').width();
    $('#test_Right4 table').css('width',dealerWeekTotal);
    $('#test_Left4 table').css('width',dealerWeekTotal2);
//  表格高度不够出现滚动条
	var dealerWeekLeft = $('#test_Left4 table').height();
	var dealerWeekLeft2 = $('#test_Left4').height();
    if(dealerWeekLeft > dealerWeekLeft2){
    	$('#test_Right4').css('width','calc(100vw - 870px)');
    	$('#test_Left4').css('width','calc(100vw - 853px)');
    }
    
	var udMonTotal = $('#test_Left5 table').width();
	var udMonTotal2 = $('#test_Right5 table').width();
    $('#test_Right5 table').css('width',udMonTotal);
    $('#test_Left5 table').css('width',udMonTotal2);
	var udMonLeft = $('#test_Left5 table').height();
	var udMonLeft2 = $('#test_Left5').height();
    if(udMonLeft > udMonLeft2){
    	$('#test_Right5').css('width','calc(100vw - 870px)');
    	$('#test_Left5').css('width','calc(100vw - 853px)');
    }
    
    var xcMonTotal = $('#test_Left6 table').width();
	var xcMonTotal2 = $('#test_Right6 table').width();
    $('#test_Right6 table').css('width',xcMonTotal);
    $('#test_Left6 table').css('width',xcMonTotal2);
	var xcMonLeft = $('#test_Left6 table').height();
	var xcMonLeft2 = $('#test_Left6').height();
    if(xcMonLeft > xcMonLeft2){
    	$('#test_Right6').css('width','calc(100vw - 870px)');
    	$('#test_Left6').css('width','calc(100vw - 853px)');
    }
    
    var smMonTotal = $('#test_Left7 table').width();
	var smMonTotal2 = $('#test_Right7 table').width();
    $('#test_Right7 table').css('width',smMonTotal);
    $('#test_Left7 table').css('width',smMonTotal2);
	var smMonLeft = $('#test_Left7 table').height();
	var smMonLeft2 = $('#test_Left7').height();
    if(smMonLeft > smMonLeft2){
    	$('#test_Right7').css('width','calc(100vw - 870px)');
    	$('#test_Left7').css('width','calc(100vw - 853px)');
    }
    
    var bismMonTotal = $('#test_Left8 table').width();
	var bismMonTotal2 = $('#test_Right8 table').width();
    $('#test_Right8 table').css('width',bismMonTotal);
    $('#test_Left8 table').css('width',bismMonTotal2);
	var bismMonLeft = $('#test_Left8 table').height();
	var bismMonLeft2 = $('#test_Left8').height();
    if(bismMonLeft > bismMonLeft2){
    	$('#test_Right8').css('width','calc(100vw - 870px)');
    	$('#test_Left8').css('width','calc(100vw - 853px)');
    }
    
    var cuMonTotal = $('#test_Left9 table').width();
	var cuMonTotal2 = $('#test_Right9 table').width();
    $('#test_Right9 table').css('width',cuMonTotal);
    $('#test_Left9 table').css('width',cuMonTotal2);
	var cuMonLeft = $('#test_Left9 table').height();
	var cuMonLeft2 = $('#test_Left9').height();
    if(cuMonLeft > cuMonLeft2){
    	$('#test_Right9').css('width','calc(100vw - 870px)');
    	$('#test_Left9').css('width','calc(100vw - 853px)');
    }
    
    var udcuMonTotal = $('#test_Left10 table').width();
	var udcuMonTotal2 = $('#test_Right10 table').width();
    $('#test_Right10 table').css('width',udcuMonTotal);
    $('#test_Left10 table').css('width',udcuMonTotal2);
	var udcuMonLeft = $('#test_Left10 table').height();
	var udcuMonLeft2 = $('#test_Left10').height();
    if(udcuMonLeft > udcuMonLeft2){
    	$('#test_Right10').css('width','calc(100vw - 870px)');
    	$('#test_Left10').css('width','calc(100vw - 853px)');
    }
    
    var xcudcuMonTotal = $('#test_Left11 table').width();
	var xcudcuMonTotal2 = $('#test_Right11 table').width();
    $('#test_Right11 table').css('width',xcudcuMonTotal);
    $('#test_Left11 table').css('width',xcudcuMonTotal2);
	var xcudcuMonLeft = $('#test_Left11 table').height();
	var xcudcuMonLeft2 = $('#test_Left11').height();
    if(xcudcuMonLeft > xcudcuMonLeft2){
    	$('#test_Right11').css('width','calc(100vw - 870px)');
    	$('#test_Left11').css('width','calc(100vw - 853px)');
    }
    
    var smxcudcuMonTotal = $('#test_Left12 table').width();
	var smxcudcuMonTotal2 = $('#test_Right12 table').width();
    $('#test_Right12 table').css('width',smxcudcuMonTotal);
    $('#test_Left12 table').css('width',smxcudcuMonTotal2);
	var smxcudcuMonLeft = $('#test_Left12 table').height();
	var smxcudcuMonLeft2 = $('#test_Left12').height();
    if(smxcudcuMonLeft > smxcudcuMonLeft2){
    	$('#test_Right12').css('width','calc(100vw - 870px)');
    	$('#test_Left12').css('width','calc(100vw - 853px)');
    }
    
    var smudcuMonTotal = $('#test_Left13 table').width();
	var smxcudcuMonTotal2 = $('#test_Right13 table').width();
    $('#test_Right13 table').css('width',smudcuMonTotal);
    $('#test_Left13 table').css('width',smxcudcuMonTotal2);
	var smxcudcuMonLeft = $('#test_Left13 table').height();
	var smxcudcuMonLeft2 = $('#test_Left13').height();
    if(smxcudcuMonLeft > smxcudcuMonLeft2){
    	$('#test_Right13').css('width','calc(100vw - 870px)');
    	$('#test_Left13').css('width','calc(100vw - 853px)');
    }
    
    var cuxcudcuMonTotal = $('#test_Left14 table').width();
	var cusmxcudcuMonTotal2 = $('#test_Right14 table').width();
    $('#test_Right14 table').css('width',cuxcudcuMonTotal);
    $('#test_Left14 table').css('width',cusmxcudcuMonTotal2);
	var cusmxcudcuMonLeft = $('#test_Left14 table').height();
	var cusmxcudcuMonLeft2 = $('#test_Left14').height();
    if(cusmxcudcuMonLeft > cusmxcudcuMonLeft2){
    	$('#test_Right14').css('width','calc(100vw - 870px)');
    	$('#test_Left14').css('width','calc(100vw - 853px)');
    }
    
    var gjztMonTotal = $('#test_Left15 table').width();
	var gjzoMonTotal2 = $('#test_Right15 table').width();
    $('#test_Right15 table').css('width',gjztMonTotal);
    $('#test_Left15 table').css('width',gjzoMonTotal2);
	var gjztdcuMonLeft = $('#test_Left15 table').height();
	var gjztMonLeft2 = $('#test_Left15').height();
    if(gjztdcuMonLeft > gjztMonLeft2){
    	$('#test_Right15').css('width','calc(100vw - 570px)');
    	$('#test_Left15').css('width','calc(100vw - 553px)');
    }
    
    var gjudMonTotal = $('#test_Left16 table').width();
	var gjudMonTotal2 = $('#test_Right16 table').width();
    $('#test_Right16 table').css('width',gjudMonTotal);
    $('#test_Left16 table').css('width',gjudMonTotal2);
	var gjuddcuMonLeft = $('#test_Left16 table').height();
	var gjudMonLeft2 = $('#test_Left16').height();
    if(gjuddcuMonLeft > gjudMonLeft2){
    	$('#test_Right16').css('width','calc(100vw - 570px)');
    	$('#test_Left16').css('width','calc(100vw - 553px)');
    }
    
    var gjxcMonTotal = $('#test_Left17 table').width();
	var gjxcMonTotal2 = $('#test_Right17 table').width();
    $('#test_Right17 table').css('width',gjxcMonTotal);
    $('#test_Left17 table').css('width',gjxcMonTotal2);
	var gjxcdcuMonLeft = $('#test_Left17 table').height();
	var gjxcMonLeft2 = $('#test_Left17').height();
    if(gjxcdcuMonLeft > gjxcMonLeft2){
    	$('#test_Right17').css('width','calc(100vw - 570px)');
    	$('#test_Left17').css('width','calc(100vw - 553px)');
    }
    
    var gjsmMonTotal = $('#test_Left18 table').width();
	var gjsmMonTotal2 = $('#test_Right18 table').width();
    $('#test_Right18 table').css('width',gjsmMonTotal);
    $('#test_Left18 table').css('width',gjsmMonTotal2);
	var gjsmMonLeft = $('#test_Left18 table').height();
	var gjsmMonLeft2 = $('#test_Left18').height();
    if(gjsmMonLeft > gjsmMonLeft2){
    	$('#test_Right18').css('width','calc(100vw - 570px)');
    	$('#test_Left18').css('width','calc(100vw - 553px)');
    }
    
    var gjbiMonTotal = $('#test_Left19 table').width();
	var gjbiMonTotal2 = $('#test_Right19 table').width();
    $('#test_Right19 table').css('width',gjbiMonTotal);
    $('#test_Left19 table').css('width',gjbiMonTotal2);
	var gjbiMonLeft = $('#test_Left19 table').height();
	var gjbiMonLeft2 = $('#test_Left19').height();
    if(gjbiMonLeft > gjbiMonLeft2){
    	$('#test_Right19').css('width','calc(100vw - 570px)');
    	$('#test_Left19').css('width','calc(100vw - 553px)');
    }
    
    var gjcuMonTotal = $('#test_Left20 table').width();
	var gjcuMonTotal2 = $('#test_Right20 table').width();
    $('#test_Right20 table').css('width',gjcuMonTotal);
    $('#test_Left20 table').css('width',gjcuMonTotal2);
	var gjcuMonLeft = $('#test_Left20 table').height();
	var gjcuMonLeft2 = $('#test_Left20').height();
    if(gjcuMonLeft > gjcuMonLeft2){
    	$('#test_Right20').css('width','calc(100vw - 570px)');
    	$('#test_Left20').css('width','calc(100vw - 553px)');
    }
    
    var saudWeekTotal = $('#test_Left21 table').width();
	var saudWeekTotal2 = $('#test_Right21 table').width();
    $('#test_Right21 table').css('width',saudWeekTotal);
    $('#test_Left21 table').css('width',saudWeekTotal2);
	var saudWeekLeft = $('#test_Left21 table').height();
	var saudWeekLeft2 = $('#test_Left21').height();
    if(saudWeekLeft > saudWeekLeft2){
    	$('#test_Right21').css('width','calc(100vw - 870px)');
    	$('#test_Left21').css('width','calc(100vw - 853px)');
    }
    
    var saxcWeekTotal = $('#test_Left22 table').width();
	var saxcWeekTotal2 = $('#test_Right22 table').width();
    $('#test_Right22 table').css('width',saxcWeekTotal);
    $('#test_Left22 table').css('width',saxcWeekTotal2);
	var saxcWeekLeft = $('#test_Left22 table').height();
	var saxcWeekLeft2 = $('#test_Left22').height();
    if(saxcWeekLeft > saxcWeekLeft2){
    	$('#test_Right22').css('width','calc(100vw - 870px)');
    	$('#test_Left22').css('width','calc(100vw - 853px)');
    }
    
    var sasmWeekTotal = $('#test_Left23 table').width();
	var sasmWeekTotal2 = $('#test_Right23 table').width();
    $('#test_Right23 table').css('width',sasmWeekTotal);
    $('#test_Left23 table').css('width',sasmWeekTotal2);
	var sasmWeekLeft = $('#test_Left23 table').height();
	var sasmWeekLeft2 = $('#test_Left23').height();
    if(sasmWeekLeft > sasmWeekLeft2){
    	$('#test_Right23').css('width','calc(100vw - 870px)');
    	$('#test_Left23').css('width','calc(100vw - 853px)');
    }
    
    var sabiWeekTotal = $('#test_Left24 table').width();
	var sabiWeekTotal2 = $('#test_Right24 table').width();
    $('#test_Right24 table').css('width',sabiWeekTotal);
    $('#test_Left24 table').css('width',sabiWeekTotal2);
	var sabiWeekLeft = $('#test_Left24 table').height();
	var sabiWeekLeft2 = $('#test_Left24').height();
    if(sabiWeekLeft > sabiWeekLeft2){
    	$('#test_Right24').css('width','calc(100vw - 870px)');
    	$('#test_Left24').css('width','calc(100vw - 853px)');
    }
    
    var sacuWeekTotal = $('#test_Left25 table').width();
	var sacuWeekTotal2 = $('#test_Right25 table').width();
    $('#test_Right25 table').css('width',sacuWeekTotal);
    $('#test_Left25 table').css('width',sacuWeekTotal2);
	var sacuWeekLeft = $('#test_Left25 table').height();
	var sacuWeekLeft2 = $('#test_Left25').height();
    if(sacuWeekLeft > sacuWeekLeft2){
    	$('#test_Right25').css('width','calc(100vw - 870px)');
    	$('#test_Left25').css('width','calc(100vw - 853px)');
    }
    
    var aocuWeekTotal = $('#test_Left26 table').width();
	var aocuWeekTotal2 = $('#test_Right26 table').width();
    $('#test_Right26 table').css('width',aocuWeekTotal);
    $('#test_Left26 table').css('width',aocuWeekTotal2);
	var aocuWeekLeft = $('#test_Left26 table').height();
	var aocuWeekLeft2 = $('#test_Left26').height();
    if(aocuWeekLeft > aocuWeekLeft2){
    	$('#test_Right26').css('width','calc(100vw - 870px)');
    	$('#test_Left26').css('width','calc(100vw - 853px)');
    }
    
    var aoxcWeekTotal = $('#test_Left27 table').width();
	var aoxcWeekTotal2 = $('#test_Right27 table').width();
    $('#test_Right27 table').css('width',aoxcWeekTotal);
    $('#test_Left27 table').css('width',aoxcWeekTotal2);
	var aoxcWeekLeft = $('#test_Left27 table').height();
	var aoxcWeekLeft2 = $('#test_Left27').height();
    if(aoxcWeekLeft > aoxcWeekLeft2){
    	$('#test_Right27').css('width','calc(100vw - 870px)');
    	$('#test_Left27').css('width','calc(100vw - 853px)');
    }
    
    var aosmWeekTotal = $('#test_Left28 table').width();
	var aosmWeekTotal2 = $('#test_Right28 table').width();
    $('#test_Right28 table').css('width',aosmWeekTotal);
    $('#test_Left28 table').css('width',aosmWeekTotal2);
	var aosmWeekLeft = $('#test_Left28 table').height();
	var aosmWeekLeft2 = $('#test_Left28').height();
    if(aosmWeekLeft > aosmWeekLeft2){
    	$('#test_Right28').css('width','calc(100vw - 870px)');
    	$('#test_Left28').css('width','calc(100vw - 853px)');
    }
    
    var aobiWeekTotal = $('#test_Left29 table').width();
	var aobiWeekTotal2 = $('#test_Right29 table').width();
    $('#test_Right29 table').css('width',aobiWeekTotal);
    $('#test_Left29 table').css('width',aobiWeekTotal2);
	var aobiWeekLeft = $('#test_Left29 table').height();
	var aobiWeekLeft2 = $('#test_Left29').height();
    if(aobiWeekLeft > aobiWeekLeft2){
    	$('#test_Right29').css('width','calc(100vw - 870px)');
    	$('#test_Left29').css('width','calc(100vw - 853px)');
    }
    
    var aocuWeekTotal = $('#test_Left30 table').width();
	var aocuWeekTotal2 = $('#test_Right30 table').width();
    $('#test_Right30 table').css('width',aocuWeekTotal);
    $('#test_Left30 table').css('width',aocuWeekTotal2);
	var aocuWeekLeft = $('#test_Left30 table').height();
	var aobiWeekLeft2 = $('#test_Left30').height();
    if(aocuWeekLeft > aobiWeekLeft2){
    	$('#test_Right30').css('width','calc(100vw - 870px)');
    	$('#test_Left30').css('width','calc(100vw - 853px)');
    }
    
    var qdudWeekTotal = $('#test_Left31 table').width();
	var qdudWeekTotal2 = $('#test_Right31 table').width();
    $('#test_Right31 table').css('width',qdudWeekTotal);
    $('#test_Left31 table').css('width',qdudWeekTotal2);
	var qdudWeekLeft = $('#test_Left31 table').height();
	var qdudWeekLeft2 = $('#test_Left31').height();
    if(qdudWeekLeft > qdudWeekLeft2){
    	$('#test_Right31').css('width','calc(100vw - 870px)');
    	$('#test_Left31').css('width','calc(100vw - 853px)');
    }
    
    var qdxcWeekTotal = $('#test_Left32 table').width();
	var qdxcWeekTotal2 = $('#test_Right32 table').width();
    $('#test_Right32 table').css('width',qdxcWeekTotal);
    $('#test_Left32 table').css('width',qdxcWeekTotal2);
	var qdxcWeekLeft = $('#test_Left32 table').height();
	var qdxcWeekLeft2 = $('#test_Left32').height();
    if(qdxcWeekLeft > qdxcWeekLeft2){
    	$('#test_Right32').css('width','calc(100vw - 870px)');
    	$('#test_Left32').css('width','calc(100vw - 853px)');
    }
    
    var qusmWeekTotal = $('#test_Left33 table').width();
	var qusmWeekTotal2 = $('#test_Right33 table').width();
    $('#test_Right33 table').css('width',qusmWeekTotal);
    $('#test_Left33 table').css('width',qusmWeekTotal2);
	var qusmWeekLeft = $('#test_Left33 table').height();
	var qusmWeekLeft2 = $('#test_Left33').height();
    if(qusmWeekLeft > qusmWeekLeft2){
    	$('#test_Right33').css('width','calc(100vw - 870px)');
    	$('#test_Left33').css('width','calc(100vw - 853px)');
    }
    
    var qubiWeekTotal = $('#test_Left34 table').width();
	var qubiWeekTotal2 = $('#test_Right34 table').width();
    $('#test_Right34 table').css('width',qubiWeekTotal);
    $('#test_Left34 table').css('width',qubiWeekTotal2);
	var qubiWeekLeft = $('#test_Left34 table').height();
	var qubiWeekLeft2 = $('#test_Left34').height();
    if(qubiWeekLeft > qubiWeekLeft2){
    	$('#test_Right34').css('width','calc(100vw - 870px)');
    	$('#test_Left34').css('width','calc(100vw - 853px)');
    }
    
    var qucuWeekTotal = $('#test_Left35 table').width();
	var qucuWeekTotal2 = $('#test_Right35 table').width();
    $('#test_Right35 table').css('width',qucuWeekTotal);
    $('#test_Left35 table').css('width',qucuWeekTotal2);
	var qucuWeekLeft = $('#test_Left35 table').height();
	var qucuWeekLeft2 = $('#test_Left35').height();
    if(qucuWeekLeft > qucuWeekLeft2){
    	$('#test_Right35').css('width','calc(100vw - 870px)');
    	$('#test_Left35').css('width','calc(100vw - 853px)');
    }
    
    var qcuWeekTotal = $('#test_Left36 table').width();
	var qcuWeekTotal2 = $('#test_Right36 table').width();
    $('#test_Right36 table').css('width',qcuWeekTotal);
    $('#test_Left36 table').css('width',qcuWeekTotal2);
	var qcuWeekLeft = $('#test_Left36 table').height();
	var qcuWeekLeft2 = $('#test_Left36').height();
    if(qcuWeekLeft > qcuWeekLeft2){
    	$('#test_Right36').css('width','calc(100vw - 570px)');
    	$('#test_Left36').css('width','calc(100vw - 553px)');
    }
    
    var qcWeekTotal = $('#test_Left37 table').width();
	var qcWeekTotal2 = $('#test_Right37 table').width();
    $('#test_Right37 table').css('width',qcWeekTotal);
    $('#test_Left37 table').css('width',qcWeekTotal2);
	var qcWeekLeft = $('#test_Left37 table').height();
	var qcWeekLeft2 = $('#test_Left37').height();
    if(qcWeekLeft > qcWeekLeft2){
    	$('#test_Right37').css('width','calc(100vw - 570px)');
    	$('#test_Left37').css('width','calc(100vw - 553px)');
    }
    
    var qcWekTotal = $('#test_Left38 table').width();
	var qcWekTotal2 = $('#test_Right38 table').width();
    $('#test_Right38 table').css('width',qcWekTotal);
    $('#test_Left38 table').css('width',qcWekTotal2);
	var qcWekLeft = $('#test_Left38 table').height();
	var qcWekLeft2 = $('#test_Left38').height();
    if(qcWekLeft > qcWekLeft2){
    	$('#test_Right38').css('width','calc(100vw - 570px)');
    	$('#test_Left38').css('width','calc(100vw - 553px)');
    }
    
    var qcWkTotal = $('#test_Left39 table').width();
	var qcWkTotal2 = $('#test_Right39 table').width();
    $('#test_Right39 table').css('width',qcWkTotal);
    $('#test_Left39 table').css('width',qcWkTotal2);
	var qcWkLeft = $('#test_Left39 table').height();
	var qcWkLeft2 = $('#test_Left39').height();
    if(qcWkLeft > qcWekLeft2){
    	$('#test_Right39').css('width','calc(100vw - 570px)');
    	$('#test_Left39').css('width','calc(100vw - 553px)');
    }
    
    var qpcWkTotal = $('#test_Left40 table').width();
	var qpcWkTotal2 = $('#test_Right40 table').width();
    $('#test_Right40 table').css('width',qpcWkTotal);
    $('#test_Left40 table').css('width',qpcWkTotal2);
	var qpcWkLeft = $('#test_Left40 table').height();
	var qpcWkLeft2 = $('#test_Left40').height();
    if(qpcWkLeft > qpcWkLeft2){
    	$('#test_Right40').css('width','calc(100vw - 570px)');
    	$('#test_Left40').css('width','calc(100vw - 553px)');
    }
    
    var qpocWkTotal = $('#test_Left41 table').width();
	var qpocWkTotal2 = $('#test_Right41 table').width();
    $('#test_Right41 table').css('width',qpocWkTotal);
    $('#test_Left41 table').css('width',qpocWkTotal2);
	var qpocWkLeft = $('#test_Left41 table').height();
	var qpocWkLeft2 = $('#test_Left41').height();
    if(qpocWkLeft > qpocWkLeft2){
    	$('#test_Right41').css('width','calc(100vw - 570px)');
    	$('#test_Left41').css('width','calc(100vw - 553px)');
    }
    
    var qpocYearTotal = $('#test_Left42 table').width();
	var qpocYearTotal2 = $('#test_Right42 table').width();
    $('#test_Right42 table').css('width',qpocYearTotal);
    $('#test_Left42 table').css('width',qpocYearTotal2);
	var qpocYearLeft = $('#test_Left42 table').height();
	var qpocYearLeft2 = $('#test_Left42').height();
    if(qpocYearLeft > qpocYearLeft2){
    	$('#test_Right42').css('width','calc(100vw - 570px)');
    	$('#test_Left42').css('width','calc(100vw - 553px)');
    }
    
    var qpcYearTotal = $('#test_Left43 table').width();
	var qpcYearTotal2 = $('#test_Right43 table').width();
    $('#test_Right43 table').css('width',qpcYearTotal);
    $('#test_Left43 table').css('width',qpcYearTotal2);
	var qpcYearLeft = $('#test_Left43 table').height();
	var qpcYearLeft2 = $('#test_Left43').height();
    if(qpcYearLeft > qpcYearLeft2){
    	$('#test_Right43').css('width','calc(100vw - 570px)');
    	$('#test_Left43').css('width','calc(100vw - 553px)');
    }
    
    var qpycYearTotal = $('#test_Left44 table').width();
	var qpycYearTotal2 = $('#test_Right44 table').width();
    $('#test_Right44 table').css('width',qpycYearTotal);
    $('#test_Left44 table').css('width',qpycYearTotal2);
	var qpycYearLeft = $('#test_Left44 table').height();
	var qpycYearLeft2 = $('#test_Left44').height();
    if(qpycYearLeft > qpycYearLeft2){
    	$('#test_Right44').css('width','calc(100vw - 570px)');
    	$('#test_Left44').css('width','calc(100vw - 553px)');
    }
    
    var qpeycYearTotal = $('#test_Left45 table').width();
	var qpeycYearTotal2 = $('#test_Right45 table').width();
    $('#test_Right45 table').css('width',qpeycYearTotal);
    $('#test_Left45 table').css('width',qpeycYearTotal2);
	var qpeycYearLeft = $('#test_Left45 table').height();
	var qpeycYearLeft2 = $('#test_Left45').height();
    if(qpeycYearLeft > qpeycYearLeft2){
    	$('#test_Right45').css('width','calc(100vw - 570px)');
    	$('#test_Left45').css('width','calc(100vw - 553px)');
    }
    
    var qpetycYearTotal = $('#test_Left46 table').width();
	var qpetycYearTotal2 = $('#test_Right46 table').width();
    $('#test_Right46 table').css('width',qpetycYearTotal);
    $('#test_Left46 table').css('width',qpetycYearTotal2);
	var qpetycYearLeft = $('#test_Left46 table').height();
	var qpetycYearLeft2 = $('#test_Left46').height();
    if(qpetycYearLeft > qpetycYearLeft2){
    	$('#test_Right46').css('width','calc(100vw - 570px)');
    	$('#test_Left46').css('width','calc(100vw - 553px)');
    }
    
    var qpertycYearTotal = $('#test_Left47 table').width();
	var qpertycYearTotal2 = $('#test_Right47 table').width();
    $('#test_Right47 table').css('width',qpertycYearTotal);
    $('#test_Left47 table').css('width',qpertycYearTotal2);
	var qpertycYearLeft = $('#test_Left47 table').height();
	var qpertycYearLeft2 = $('#test_Left47').height();
    if(qpertycYearLeft > qpertycYearLeft2){
    	$('#test_Right47').css('width','calc(100vw - 570px)');
    	$('#test_Left47').css('width','calc(100vw - 553px)');
    }
    
    var qpeurtycYearTotal = $('#test_Left48 table').width();
	var qperutycYearTotal2 = $('#test_Right48 table').width();
    $('#test_Right48 table').css('width',qpeurtycYearTotal);
    $('#test_Left48 table').css('width',qperutycYearTotal2);
	var qpeurtycYearLeft = $('#test_Left48 table').height();
	var qpeurtycYearLeft2 = $('#test_Left48').height();
    if(qpeurtycYearLeft > qpeurtycYearLeft2){
    	$('#test_Right48').css('width','calc(100vw - 870px)');
    	$('#test_Left48').css('width','calc(100vw - 853px)');
    }
    
    var qperurtycYearTotal = $('#test_Left49 table').width();
	var qperrutycYearTotal2 = $('#test_Right49 table').width();
    $('#test_Right49 table').css('width',qperurtycYearTotal);
    $('#test_Left49 table').css('width',qperrutycYearTotal2);
	var qperurtycYearLeft = $('#test_Left49 table').height();
	var qperurtycYearLeft2 = $('#test_Left49').height();
    if(qperurtycYearLeft > qperurtycYearLeft2){
    	$('#test_Right49').css('width','calc(100vw - 870px)');
    	$('#test_Left49').css('width','calc(100vw - 853px)');
    }
    
    var qpedrurtycYearTotal = $('#test_Left50 table').width();
	var qpedrrutycYearTotal2 = $('#test_Right50 table').width();
    $('#test_Right50 table').css('width',qpedrurtycYearTotal);
    $('#test_Left50 table').css('width',qpedrrutycYearTotal2);
	var qperdurtycYearLeft = $('#test_Left50 table').height();
	var qperdurtycYearLeft2 = $('#test_Left50').height();
    if(qperdurtycYearLeft > qperdurtycYearLeft2){
    	$('#test_Right50').css('width','calc(100vw - 870px)');
    	$('#test_Left50').css('width','calc(100vw - 853px)');
    }
    
    var qprtycYearTotal = $('#test_Left51 table').width();
	var qrutycYearTotal2 = $('#test_Right51 table').width();
    $('#test_Right51 table').css('width',qprtycYearTotal);
    $('#test_Left51 table').css('width',qrutycYearTotal2);
	var qrtycYearLeft = $('#test_Left51 table').height();
	var qptycYearLeft2 = $('#test_Left51').height();
    if(qrtycYearLeft > qptycYearLeft2){
    	$('#test_Right51').css('width','calc(100vw - 870px)');
    	$('#test_Left51').css('width','calc(100vw - 853px)');
    }
    
    var qprotycYearTotal = $('#test_Left52 table').width();
	var qruotycYearTotal2 = $('#test_Right52 table').width();
    $('#test_Right52 table').css('width',qprotycYearTotal);
    $('#test_Left52 table').css('width',qruotycYearTotal2);
	var qrtoycYearLeft = $('#test_Left52 table').height();
	var qptoycYearLeft2 = $('#test_Left52').height();
    if(qrtoycYearLeft > qptoycYearLeft2){
    	$('#test_Right52').css('width','calc(100vw - 870px)');
    	$('#test_Left52').css('width','calc(100vw - 853px)');
    }
    
    var qpyrotycYearTotal = $('#test_Left53 table').width();
	var qryuotycYearTotal2 = $('#test_Right53 table').width();
    $('#test_Right53 table').css('width',qpyrotycYearTotal);
    $('#test_Left53 table').css('width',qryuotycYearTotal2);
	var qrtyoycYearLeft = $('#test_Left53 table').height();
	var qpytoycYearLeft2 = $('#test_Left53').height();
    if(qrtyoycYearLeft > qpytoycYearLeft2){
    	$('#test_Right53').css('width','calc(100vw - 870px)');
    	$('#test_Left53').css('width','calc(100vw - 853px)');
    }
    
    var aoyrotycYearTotal = $('#test_Left54 table').width();
	var aoyuotycYearTotal2 = $('#test_Right54 table').width();
    $('#test_Right54 table').css('width',aoyrotycYearTotal);
    $('#test_Left54 table').css('width',aoyuotycYearTotal2);
	var aotyoycYearLeft = $('#test_Left54 table').height();
	var aoytoycYearLeft2 = $('#test_Left54').height();
    if(aotyoycYearLeft > aoytoycYearLeft2){
    	$('#test_Right54').css('width','calc(100vw - 870px)');
    	$('#test_Left54').css('width','calc(100vw - 853px)');
    }
    
    var aorotycYearTotal = $('#test_Left55 table').width();
	var aouotycYearTotal2 = $('#test_Right55 table').width();
    $('#test_Right55 table').css('width',aorotycYearTotal);
    $('#test_Left55 table').css('width',aouotycYearTotal2);
	var aoyoycYearLeft = $('#test_Left55 table').height();
	var aotoycYearLeft2 = $('#test_Left55').height();
    if(aoyoycYearLeft > aotoycYearLeft2){
    	$('#test_Right55').css('width','calc(100vw - 870px)');
    	$('#test_Left55').css('width','calc(100vw - 853px)');
    }
    
    var aortycYearTotal = $('#test_Left56 table').width();
	var aootycYearTotal2 = $('#test_Right56 table').width();
    $('#test_Right56 table').css('width',aortycYearTotal);
    $('#test_Left56 table').css('width',aootycYearTotal2);
	var aoyocYearLeft = $('#test_Left56 table').height();
	var aotycYearLeft2 = $('#test_Left56').height();
    if(aoyocYearLeft > aotycYearLeft2){
    	$('#test_Right56').css('width','calc(100vw - 870px)');
    	$('#test_Left56').css('width','calc(100vw - 853px)');
    }
    
    var aooycYearTotal = $('#test_Left57 table').width();
	var aooycYearTotal2 = $('#test_Right57 table').width();
    $('#test_Right57 table').css('width',aooycYearTotal);
    $('#test_Left57 table').css('width',aooycYearTotal2);
	var aoycYearLeft = $('#test_Left57 table').height();
	var aotcYearLeft2 = $('#test_Left57').height();
    if(aoycYearLeft > aotcYearLeft2){
    	$('#test_Right57').css('width','calc(100vw - 870px)');
    	$('#test_Left57').css('width','calc(100vw - 853px)');
    }
    
    var aooyYearTotal = $('#test_Left58 table').width();
	var aooyYearTotal2 = $('#test_Right58 table').width();
    $('#test_Right58 table').css('width',aooyYearTotal);
    $('#test_Left58 table').css('width',aooyYearTotal2);
	var aoyYearLeft = $('#test_Left58 table').height();
	var aotYearLeft2 = $('#test_Left58').height();
    if(aoyYearLeft > aotYearLeft2){
    	$('#test_Right58').css('width','calc(100vw - 870px)');
    	$('#test_Left58').css('width','calc(100vw - 853px)');
    }
    
    var aooygYearTotal = $('#test_Left59 table').width();
	var aooygYearTotal2 = $('#test_Right59 table').width();
    $('#test_Right59 table').css('width',aooygYearTotal);
    $('#test_Left59 table').css('width',aooygYearTotal2);
	var aoygYearLeft = $('#test_Left59 table').height();
	var aotgYearLeft2 = $('#test_Left59').height();
    if(aoygYearLeft > aotgYearLeft2){
    	$('#test_Right59').css('width','calc(100vw - 870px)');
    	$('#test_Left59').css('width','calc(100vw - 853px)');
    }
    
    
})
//JS代码
var timer = null;
//	Saleman月份增长
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
function moveLeft_Left(){
    $("#test_Right").removeAttr("onScroll");
    $("#test_Right").scrollLeft($("#test_Left").scrollLeft());
    $("#test_Head").removeAttr("onScroll");
    $("#test_Head tbody").scrollTop($("#test_Left").scrollTop());
    clearTimeout(timer);
}
//	Saleman周增长
function moveLeft_Lefttwo(){
    $("#test_Right2").removeAttr("onScroll");
    $("#test_Right2").scrollLeft($("#test_Left2").scrollLeft());
    $("#test_Head2").removeAttr("onScroll");
    $("#test_Head2 tbody").scrollTop($("#test_Left2").scrollTop());
}
//  Acfo周增长
function moveLeft_Leftthir(){
    $("#test_Right3").removeAttr("onScroll");
    $("#test_Right3").scrollLeft($("#test_Left3").scrollLeft());
    $("#test_Head3").removeAttr("onScroll");
    $("#test_Head3 tbody").scrollTop($("#test_Left3").scrollTop());
    clearTimeout(timer);
}
//  DEALER周增长
function moveLeft_Leftfour(){
    $("#test_Right4").removeAttr("onScroll");
    $("#test_Right4").scrollLeft($("#test_Left4").scrollLeft());
    $("#test_Head4").removeAttr("onScroll");
    $("#test_Head4 tbody").scrollTop($("#test_Left4").scrollTop());
    clearTimeout(timer);
}

function moveLeft_Left5(){
    $("#test_Right5").removeAttr("onScroll");
    $("#test_Right5").scrollLeft($("#test_Left5").scrollLeft());
    $("#test_Head5").removeAttr("onScroll");
    $("#test_Head5 tbody").scrollTop($("#test_Left5").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left6(){
    $("#test_Right6").removeAttr("onScroll");
    $("#test_Right6").scrollLeft($("#test_Left6").scrollLeft());
    $("#test_Head6").removeAttr("onScroll");
    $("#test_Head6 tbody").scrollTop($("#test_Left6").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left7(){
    $("#test_Right7").removeAttr("onScroll");
    $("#test_Right7").scrollLeft($("#test_Left7").scrollLeft());
    $("#test_Head7").removeAttr("onScroll");
    $("#test_Head7 tbody").scrollTop($("#test_Left7").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left8(){
    $("#test_Right8").removeAttr("onScroll");
    $("#test_Right8").scrollLeft($("#test_Left8").scrollLeft());
    $("#test_Head8").removeAttr("onScroll");
    $("#test_Head8 tbody").scrollTop($("#test_Left8").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left9(){
    $("#test_Right9").removeAttr("onScroll");
    $("#test_Right9").scrollLeft($("#test_Left9").scrollLeft());
    $("#test_Head9").removeAttr("onScroll");
    $("#test_Head9 tbody").scrollTop($("#test_Left9").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left10(){
    $("#test_Right10").removeAttr("onScroll");
    $("#test_Right10").scrollLeft($("#test_Left10").scrollLeft());
    $("#test_Head10").removeAttr("onScroll");
    $("#test_Head10 tbody").scrollTop($("#test_Left10").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left11(){
    $("#test_Right11").removeAttr("onScroll");
    $("#test_Right11").scrollLeft($("#test_Left11").scrollLeft());
    $("#test_Head11").removeAttr("onScroll");
    $("#test_Head11 tbody").scrollTop($("#test_Left11").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left12(){
    $("#test_Right12").removeAttr("onScroll");
    $("#test_Right12").scrollLeft($("#test_Left12").scrollLeft());
    $("#test_Head12").removeAttr("onScroll");
    $("#test_Head12 tbody").scrollTop($("#test_Left12").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left13(){
    $("#test_Right13").removeAttr("onScroll");
    $("#test_Right13").scrollLeft($("#test_Left13").scrollLeft());
    $("#test_Head13").removeAttr("onScroll");
    $("#test_Head13 tbody").scrollTop($("#test_Left13").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left14(){
    $("#test_Right14").removeAttr("onScroll");
    $("#test_Right14").scrollLeft($("#test_Left14").scrollLeft());
    $("#test_Head14").removeAttr("onScroll");
    $("#test_Head14 tbody").scrollTop($("#test_Left14").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left15(){
    $("#test_Right15").removeAttr("onScroll");
    $("#test_Right15").scrollLeft($("#test_Left15").scrollLeft());
//  $("#test_Head15").removeAttr("onScroll");
//  $("#test_Head15 tbody").scrollTop($("#test_Left15").scrollTop());
//  clearTimeout(timer);
}
function moveLeft_Left16(){
    $("#test_Right16").removeAttr("onScroll");
    $("#test_Right16").scrollLeft($("#test_Left16").scrollLeft());
//  $("#test_Head16").removeAttr("onScroll");
//  $("#test_Head16 tbody").scrollTop($("#test_Left16").scrollTop());
//  clearTimeout(timer);
}
function moveLeft_Left17(){
    $("#test_Right17").removeAttr("onScroll");
    $("#test_Right17").scrollLeft($("#test_Left17").scrollLeft());
//  $("#test_Head17").removeAttr("onScroll");
//  $("#test_Head17 tbody").scrollTop($("#test_Left17").scrollTop());
//  clearTimeout(timer);
}
function moveLeft_Left18(){
    $("#test_Right18").removeAttr("onScroll");
    $("#test_Right18").scrollLeft($("#test_Left18").scrollLeft());
//  $("#test_Head18").removeAttr("onScroll");
//  $("#test_Head18 tbody").scrollTop($("#test_Left18").scrollTop());
//  clearTimeout(timer);
}
function moveLeft_Left19(){
    $("#test_Right19").removeAttr("onScroll");
    $("#test_Right19").scrollLeft($("#test_Left19").scrollLeft());
//  $("#test_Head19").removeAttr("onScroll");
//  $("#test_Head19 tbody").scrollTop($("#test_Left19").scrollTop());
//  clearTimeout(timer);
}
function moveLeft_Left20(){
    $("#test_Right20").removeAttr("onScroll");
    $("#test_Right20").scrollLeft($("#test_Left20").scrollLeft());
//  $("#test_Head20").removeAttr("onScroll");
//  $("#test_Head20 tbody").scrollTop($("#test_Left20").scrollTop());
//  clearTimeout(timer);
}
function moveLeft_Left21(){
    $("#test_Right21").removeAttr("onScroll");
    $("#test_Right21").scrollLeft($("#test_Left21").scrollLeft());
    $("#test_Head21").removeAttr("onScroll");
    $("#test_Head21 tbody").scrollTop($("#test_Left21").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left22(){
    $("#test_Right22").removeAttr("onScroll");
    $("#test_Right22").scrollLeft($("#test_Left22").scrollLeft());
    $("#test_Head22").removeAttr("onScroll");
    $("#test_Head22 tbody").scrollTop($("#test_Left22").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left23(){
    $("#test_Right23").removeAttr("onScroll");
    $("#test_Right23").scrollLeft($("#test_Left23").scrollLeft());
    $("#test_Head23").removeAttr("onScroll");
    $("#test_Head23 tbody").scrollTop($("#test_Left23").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left24(){
    $("#test_Right24").removeAttr("onScroll");
    $("#test_Right24").scrollLeft($("#test_Left24").scrollLeft());
    $("#test_Head24").removeAttr("onScroll");
    $("#test_Head24 tbody").scrollTop($("#test_Left24").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left25(){
    $("#test_Right25").removeAttr("onScroll");
    $("#test_Right25").scrollLeft($("#test_Left25").scrollLeft());
    $("#test_Head25").removeAttr("onScroll");
    $("#test_Head25 tbody").scrollTop($("#test_Left25").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left26(){
    $("#test_Right26").removeAttr("onScroll");
    $("#test_Right26").scrollLeft($("#test_Left26").scrollLeft());
    $("#test_Head26").removeAttr("onScroll");
    $("#test_Head26 tbody").scrollTop($("#test_Left26").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left27(){
    $("#test_Right27").removeAttr("onScroll");
    $("#test_Right27").scrollLeft($("#test_Left27").scrollLeft());
    $("#test_Head27").removeAttr("onScroll");
    $("#test_Head27 tbody").scrollTop($("#test_Left27").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left28(){
    $("#test_Right28").removeAttr("onScroll");
    $("#test_Right28").scrollLeft($("#test_Left28").scrollLeft());
    $("#test_Head28").removeAttr("onScroll");
    $("#test_Head28 tbody").scrollTop($("#test_Left28").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left29(){
    $("#test_Right29").removeAttr("onScroll");
    $("#test_Right29").scrollLeft($("#test_Left29").scrollLeft());
    $("#test_Head29").removeAttr("onScroll");
    $("#test_Head29 tbody").scrollTop($("#test_Left29").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left30(){
    $("#test_Right30").removeAttr("onScroll");
    $("#test_Right30").scrollLeft($("#test_Left30").scrollLeft());
    $("#test_Head30").removeAttr("onScroll");
    $("#test_Head30 tbody").scrollTop($("#test_Left30").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left31(){
    $("#test_Right31").removeAttr("onScroll");
    $("#test_Right31").scrollLeft($("#test_Left31").scrollLeft());
    $("#test_Head31").removeAttr("onScroll");
    $("#test_Head31 tbody").scrollTop($("#test_Left31").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left32(){
    $("#test_Right32").removeAttr("onScroll");
    $("#test_Right32").scrollLeft($("#test_Left32").scrollLeft());
    $("#test_Head32").removeAttr("onScroll");
    $("#test_Head32 tbody").scrollTop($("#test_Left32").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left33(){
    $("#test_Right33").removeAttr("onScroll");
    $("#test_Right33").scrollLeft($("#test_Left33").scrollLeft());
    $("#test_Head33").removeAttr("onScroll");
    $("#test_Head33 tbody").scrollTop($("#test_Left33").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left34(){
    $("#test_Right34").removeAttr("onScroll");
    $("#test_Right34").scrollLeft($("#test_Left34").scrollLeft());
    $("#test_Head34").removeAttr("onScroll");
    $("#test_Head34 tbody").scrollTop($("#test_Left34").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left35(){
    $("#test_Right35").removeAttr("onScroll");
    $("#test_Right35").scrollLeft($("#test_Left35").scrollLeft());
    $("#test_Head35").removeAttr("onScroll");
    $("#test_Head35 tbody").scrollTop($("#test_Left35").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left36(){
    $("#test_Right36").removeAttr("onScroll");
    $("#test_Right36").scrollLeft($("#test_Left36").scrollLeft());
    $("#test_Head36").removeAttr("onScroll");
    $("#test_Head36 tbody").scrollTop($("#test_Left36").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left37(){
    $("#test_Right37").removeAttr("onScroll");
    $("#test_Right37").scrollLeft($("#test_Left37").scrollLeft());
    $("#test_Head37").removeAttr("onScroll");
    $("#test_Head37 tbody").scrollTop($("#test_Left37").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left38(){
    $("#test_Right38").removeAttr("onScroll");
    $("#test_Right38").scrollLeft($("#test_Left38").scrollLeft());
    $("#test_Head38").removeAttr("onScroll");
    $("#test_Head38 tbody").scrollTop($("#test_Left38").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left39(){
    $("#test_Right39").removeAttr("onScroll");
    $("#test_Right39").scrollLeft($("#test_Left39").scrollLeft());
    $("#test_Head39").removeAttr("onScroll");
    $("#test_Head39 tbody").scrollTop($("#test_Left39").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left40(){
    $("#test_Right40").removeAttr("onScroll");
    $("#test_Right40").scrollLeft($("#test_Left40").scrollLeft());
    $("#test_Head40").removeAttr("onScroll");
    $("#test_Head40 tbody").scrollTop($("#test_Left40").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left41(){
    $("#test_Right41").removeAttr("onScroll");
    $("#test_Right41").scrollLeft($("#test_Left41").scrollLeft());
    $("#test_Head41").removeAttr("onScroll");
    $("#test_Head41 tbody").scrollTop($("#test_Left41").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left42(){
    $("#test_Right42").removeAttr("onScroll");
    $("#test_Right42").scrollLeft($("#test_Left42").scrollLeft());
    $("#test_Head42").removeAttr("onScroll");
    $("#test_Head42 tbody").scrollTop($("#test_Left42").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left43(){
    $("#test_Right43").removeAttr("onScroll");
    $("#test_Right43").scrollLeft($("#test_Left43").scrollLeft());
    $("#test_Head43").removeAttr("onScroll");
    $("#test_Head43 tbody").scrollTop($("#test_Left43").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left44(){
    $("#test_Right44").removeAttr("onScroll");
    $("#test_Right44").scrollLeft($("#test_Left44").scrollLeft());
    $("#test_Head44").removeAttr("onScroll");
    $("#test_Head44 tbody").scrollTop($("#test_Left44").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left45(){
    $("#test_Right45").removeAttr("onScroll");
    $("#test_Right45").scrollLeft($("#test_Left45").scrollLeft());
    $("#test_Head45").removeAttr("onScroll");
    $("#test_Head45 tbody").scrollTop($("#test_Left45").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left46(){
    $("#test_Right46").removeAttr("onScroll");
    $("#test_Right46").scrollLeft($("#test_Left46").scrollLeft());
    $("#test_Head46").removeAttr("onScroll");
    $("#test_Head46 tbody").scrollTop($("#test_Left46").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left47(){
    $("#test_Right47").removeAttr("onScroll");
    $("#test_Right47").scrollLeft($("#test_Left47").scrollLeft());
    $("#test_Head47").removeAttr("onScroll");
    $("#test_Head47 tbody").scrollTop($("#test_Left47").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left48(){
    $("#test_Right48").removeAttr("onScroll");
    $("#test_Right48").scrollLeft($("#test_Left48").scrollLeft());
    $("#test_Head48").removeAttr("onScroll");
    $("#test_Head48 tbody").scrollTop($("#test_Left48").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left49(){
    $("#test_Right49").removeAttr("onScroll");
    $("#test_Right49").scrollLeft($("#test_Left49").scrollLeft());
    $("#test_Head49").removeAttr("onScroll");
    $("#test_Head49 tbody").scrollTop($("#test_Left49").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left50(){
    $("#test_Right50").removeAttr("onScroll");
    $("#test_Right50").scrollLeft($("#test_Left50").scrollLeft());
    $("#test_Head50").removeAttr("onScroll");
    $("#test_Head50 tbody").scrollTop($("#test_Left50").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left51(){
    $("#test_Right51").removeAttr("onScroll");
    $("#test_Right51").scrollLeft($("#test_Left51").scrollLeft());
    $("#test_Head51").removeAttr("onScroll");
    $("#test_Head51 tbody").scrollTop($("#test_Left51").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left52(){
    $("#test_Right52").removeAttr("onScroll");
    $("#test_Right52").scrollLeft($("#test_Left52").scrollLeft());
    $("#test_Head52").removeAttr("onScroll");
    $("#test_Head52 tbody").scrollTop($("#test_Left52").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left53(){
    $("#test_Right53").removeAttr("onScroll");
    $("#test_Right53").scrollLeft($("#test_Left53").scrollLeft());
    $("#test_Head53").removeAttr("onScroll");
    $("#test_Head53 tbody").scrollTop($("#test_Left53").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left54(){
    $("#test_Right54").removeAttr("onScroll");
    $("#test_Right54").scrollLeft($("#test_Left54").scrollLeft());
    $("#test_Head54").removeAttr("onScroll");
    $("#test_Head54 tbody").scrollTop($("#test_Left54").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left55(){
    $("#test_Right55").removeAttr("onScroll");
    $("#test_Right55").scrollLeft($("#test_Left55").scrollLeft());
    $("#test_Head55").removeAttr("onScroll");
    $("#test_Head55 tbody").scrollTop($("#test_Left55").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left56(){
    $("#test_Right56").removeAttr("onScroll");
    $("#test_Right56").scrollLeft($("#test_Left56").scrollLeft());
    $("#test_Head56").removeAttr("onScroll");
    $("#test_Head56 tbody").scrollTop($("#test_Left56").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left57(){
    $("#test_Right57").removeAttr("onScroll");
    $("#test_Right57").scrollLeft($("#test_Left57").scrollLeft());
    $("#test_Head57").removeAttr("onScroll");
    $("#test_Head57 tbody").scrollTop($("#test_Left57").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left58(){
    $("#test_Right58").removeAttr("onScroll");
    $("#test_Right58").scrollLeft($("#test_Left58").scrollLeft());
    $("#test_Head58").removeAttr("onScroll");
    $("#test_Head58 tbody").scrollTop($("#test_Left58").scrollTop());
    clearTimeout(timer);
}
function moveLeft_Left59(){
    $("#test_Right59").removeAttr("onScroll");
    $("#test_Right59").scrollLeft($("#test_Left59").scrollLeft());
    $("#test_Head59").removeAttr("onScroll");
    $("#test_Head59 tbody").scrollTop($("#test_Left59").scrollTop());
    clearTimeout(timer);
}


function widthlala(left,right){
	var tbodywiFor = $('#' + left + ' table').width();
	var tboodywiFor = $('#' + right + ' table').width();
    $('#' + right + ' table').css('width',tbodywiFor);
    $('#' + left + ' table').css('width',tboodywiFor);
}
function moveLeft_LeftAcfo(left,right,head){
    $("#" + right).removeAttr("onScroll");
    $("#" + right).scrollLeft($("#" + left).scrollLeft());
    $("#" + head).removeAttr("onScroll");
    $("#" + head + ' tbody').scrollTop($("#" + left).scrollTop());
    clearTimeout(timer);
}