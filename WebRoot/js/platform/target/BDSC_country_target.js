$(document).ready(function(){
	tabWid();
	$(".main tbody tr").hover(
		function(){
			$(this).addClass("highlight");
		},
		function(){
			$(this).removeClass("highlight");
		}
	);
	$(".main tbody tr").click(
		function(){
			$(this).addClass("selected");
			$(this).siblings().removeClass("selected");
		}
	);
	/*widthAndHeight('test_Right','test_Head');*/
	$(window).resize(function(){
		/*parent.location.reload();*/
		widthAndHeight('test_Right','test_Head');
		selectBDSCTarget($("#type").val());
		tabWid();
	});
	$('#addTabsMine').tabs({
        onSelect:function(Year){
        	selectBDSCTargetYear('1');
        }
    });
	$('#addTabsMineAc').tabs({
        onSelect:function(Year){
        	selectBDSCTargetYear('2');
        }
    });
});
function tabWid(){
	var addTabsMineW = $(window).width()- 316;
	var addTabsMineH = $(window).height()- 120;
	$('#addTabsMine').css('width',addTabsMineW);
	$('#addTabsMine').css('height',addTabsMineH);
}
//同步滚动,每个表格加载完成添加,季度表格不需要
function moveTopOrLeft(right,left){
	$("#" + left + " tbody").removeAttr("onScroll");
    $("#" + left + " tbody").scrollTop($("#" + right + " tbody").scrollTop());
    
}//同步滚动结束
//滚动条,每个表格加载完成添加,季度表格不需要
function widthAndHeight(right,left){//right右下正文，left左部固定
	$('#bloOrNo').css('height','22');
	//右部宽度
	var wiRi = $(window).width() - $("#" + left).width() - 340;
	$("#" + right).css('width',wiRi);
	 //头部固定高
	var topHeight = $("#" + right + " table thead").height();
	$("#" + left + " table thead").css('height',topHeight);
	
	//获取各部宽高
	var winHeight = $(window).height()-280;
	var e = $("#" + left + " table tbody").height();
	var b = $("#" + right + " table tbody").height();
	var f = $("#" + right + " table tbody").width();
	var c = $("#" + right).width();
	var d = $("#" + right + " table").width();
	var scrWidth = getScrollbarWidth();
	if(b>=winHeight){//出现y轴
		if(c == 0 || c == null){//右边没内容
			$('#bloOrNo').text('SO Target');
			$('#bloOrNoY').text('SO Target');
			/*$('.Left_Head').css('width','calc(100vw - 360px)');*/
		}else if(d>c){//出现x轴
			/*$('#addTabsMine').css('width',addTabsMineW).css('height',addTabsMineH);*/
			$("#" + right + " table tbody td").css('width','110');
			$(".lineHtmlW").css('width',d);
			$("#" + right + " table thead").css('width',d);
			$("#" + right + " table tbody").css('height',winHeight);
			$("#" + right + " table tbody").css('width',d + scrWidth);
		}else{//不出现x轴
			/*$('#addTabsMine').css('width',addTabsMineW).css('height',addTabsMineH);*/
			$("#" + right + " table tbody td").css('width','110');
			$(".lineHtmlW").css('width',d);
			$("#" + right + " table thead").css('width',d);
			$("#" + right + " table tbody").css('height',winHeight);
			$("#" + right + " table tbody").css('width',d + scrWidth);
		}
	}else if(b<winHeight){//不出现y轴
		if(c == 0 || c == null){//右边没内容
			$('#bloOrNo').text('SO Target');
			$('#bloOrNoY').text('SO Target');
			/*$('.Left_Head').css('width','calc(100vw - 360px)');*/
		}else if(d>c){//出现x轴
			/*$('#addTabsMine').css('width',addTabsMineW).css('height',addTabsMineH);*/
			$("#" + right + " table tbody td").css('width','110');
			$(".lineHtmlW").css('width',d);
			$("#" + right + " table thead").css('width',d);
			$("#" + right + " table tbody").css('width',d);
		}else{//不出现x轴
			/*$('#addTabsMine').css('width',addTabsMineW);
			$('#addTabsMine').css('height',addTabsMineH);*/
			$("#" + right + " table tbody td").css('width','110');
			$(".lineHtmlW").css('width',d);
			$("#" + right + " table thead").css('width',d);
			$("#" + right + " table tbody").css('width',d);
		}
	}
}//滚动条结束
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