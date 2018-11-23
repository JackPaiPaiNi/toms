$(document).ready(function(){
	//隐藏数据
	$.each($("#store tr"), function(i){
		    if(i > 2){
		        this.style.display = 'none';
		 
		   }
		});
	$.each($("#channel tr"), function(i){
	    if(i > 2){
	        this.style.display = 'none';
	 
	   }
	});
	$.each($("#offices tr"), function(i){
	    if(i > 2){
	        this.style.display = 'none';
	 
	   }
	});
	$.each($("#regions tr"), function(i){
	    if(i > 2){
	        this.style.display = 'none';
	 
	   }
	});
	$.each($("#branch tr"), function(i){
	    if(i > 2){
	        this.style.display = 'none';
	 
	   }
	});
//	$.each($("#promoter tr"), function(i){
//	    if(i > 2){
//	        this.style.display = 'none';
//	 
//	   }
//	});
	$.each($("#supervisor tr"), function(i){
	    if(i > 2){
	        this.style.display = 'none';
	 
	   }
	});
	$.each($("#salesman tr"), function(i){
	    if(i > 2){
	        this.style.display = 'none';
	 
	   }
	});
	$.each($("#managers tr"), function(i){
	    if(i > 2){
	        this.style.display = 'none';
	 
	   }
	});
	
	$(".main tbody tr").click(
		function(){
			$(this).addClass("selected");
			$(this).siblings().removeClass("selected");
		}
	);
	
	
	var demo = 	$("#tbody tr");
	for(var i = 2;i<demo.length;i++){
		$(demo[i]).hide();
	}
	$("#table-store-id").hide();
	
	var demo = 	$("#chan tr");
	for(var i = 2;i<demo.length;i++){
		$(demo[i]).hide();
	}
	$("#table-item-id").hide();
	
	var demo = 	$("#off tr");
	for(var i = 2;i<demo.length;i++){
		$(demo[i]).hide();
	}
	$("#table-offices-id").hide();
	
	var demo = 	$("#reg tr");
	for(var i = 2;i<demo.length;i++){
		$(demo[i]).hide();
	}
	$("#table-region-id").hide();
	
	var demo = 	$("#bran tr");
	for(var i = 2;i<demo.length;i++){
		$(demo[i]).hide();
	}
	$("#table-branch-id").hide();
	
//	var demo = 	$("#pro tr");
//	for(var i = 2;i<demo.length;i++){
//		$(demo[i]).hide();
//	}
//	$("#table-promoter-id").hide();
	
	var demo = 	$("#sup tr");
	for(var i = 2;i<demo.length;i++){
		$(demo[i]).hide();
	}
	$("#table-supervisor-id").hide();
	
	var demo = 	$("#sale tr");
	for(var i = 2;i<demo.length;i++){
		$(demo[i]).hide();
	}
	$("#table-salesman-id").hide();
	
	var demo = 	$("#man tr");
	for(var i = 2;i<demo.length;i++){
		$(demo[i]).hide();
	}
	$("#table-manager-id").hide();
});



function sho(){
		$("#table-store-id").toggle();
		$("#table-store-show").toggle();
		var demo1 = $("#tbody tr");
		for(var i = 2;i<demo1.length;i++){
			$(demo1[i]).show();
		}
	};
	
function hid(){
		$("#table-store-id").toggle();
		$("#table-store-show").toggle();
		var demo1 = $("#tbody tr");
		for(var i = 2;i<demo1.length-1;i++){
			$(demo1[i]).hide();
		}
	};

	
function showChan(){
	$("#table-item-id").toggle();
	$("#table-item-show").toggle();
	var demo1 = $("#chan tr");
	for(var i = 2;i<demo1.length;i++){
		$(demo1[i]).show();
	}
}

function hideChan(){
	$("#table-item-id").toggle();
	$("#table-item-show").toggle();
	var demo1 = $("#chan tr");
	for(var i = 2;i<demo1.length-1;i++){
		$(demo1[i]).hide();
	}
}

function showOffice(){
	$("#table-offices-id").toggle();
	$("#table-offices-show").toggle();
	var demo1 = $("#off tr");
	for(var i = 2;i<demo1.length;i++){
		$(demo1[i]).show();
	}
}

function hideOffice(){
	$("#table-offices-id").toggle();
	$("#table-offices-show").toggle();
	var demo1 = $("#off tr");
	for(var i = 2;i<demo1.length-1;i++){
		$(demo1[i]).hide();
	}
}

function showRegion(){
	$("#table-region-id").toggle();
	$("#table-region-show").toggle();
	var demo1 = $("#reg tr");
	for(var i = 2;i<demo1.length;i++){
		$(demo1[i]).show();
	}
}

function hideRegion(){
	$("#table-region-id").toggle();
	$("#table-region-show").toggle();
	var demo1 = $("#reg tr");
	for(var i = 2;i<demo1.length-1;i++){
		$(demo1[i]).hide();
	}
}

function showBranch(){
	$("#table-branch-id").toggle();
	$("#table-branch-show").toggle();
	var demo1 = $("#bran tr");
	for(var i = 2;i<demo1.length;i++){
		$(demo1[i]).show();
	}
}

function hideBranch(){
	$("#table-branch-id").toggle();
	$("#table-branch-show").toggle();
	var demo1 = $("#bran tr");
	for(var i = 2;i<demo1.length-1;i++){
		$(demo1[i]).hide();
	}
}

//function showPromoter(){
//	$("#table-promoter-id").toggle();
//	$("#table-promoter-show").toggle();
//	var demo1 = $("#pro tr");
//	for(var i = 2;i<demo1.length;i++){
//		$(demo1[i]).show();
//	}
//}
//
//function hidePromoter(){
//	$("#table-promoter-id").toggle();
//	$("#table-promoter-show").toggle();
//	var demo1 = $("#pro tr");
//	for(var i = 2;i<demo1.length;i++){
//		$(demo1[i]).show();
//	}
//}

function showSupervisor(){
	$("#table-supervisor-id").toggle();
	$("#table-supervisor-show").toggle();
	var demo1 = $("#sup tr");
	for(var i = 2;i<demo1.length;i++){
		$(demo1[i]).show();
	}
}

function hideSupervisor(){
	$("#table-supervisor-id").toggle();
	$("#table-supervisor-show").toggle();
	var demo1 = $("#sup tr");
	for(var i = 2;i<demo1.length-1;i++){
		$(demo1[i]).hide();
	}
}

function showSalesman(){
	$("#table-salesman-id").toggle();
	$("#table-salesman-show").toggle();
	var demo1 = $("#sale tr");
	for(var i = 2;i<demo1.length;i++){
		$(demo1[i]).show();
	}
}

function hideSalesman(){
	$("#table-salesman-id").toggle();
	$("#table-salesman-show").toggle();
	var demo1 = $("#sale tr");
	for(var i = 2;i<demo1.length-1;i++){
		$(demo1[i]).hide();
	}
}

function showManager(){
	$("#table-manager-id").toggle();
	$("#table-manager-show").toggle();
	var demo1 = $("#man tr");
	for(var i = 2;i<demo1.length;i++){
		$(demo1[i]).show();
	}
}

function hideManager(){
	$("#table-manager-id").toggle();
	$("#table-manager-show").toggle();
	var demo1 = $("#man tr");
	for(var i = 2;i<demo1.length-1;i++){
		$(demo1[i]).hide();
	}
}