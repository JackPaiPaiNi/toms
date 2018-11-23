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
	
});
var city=[
 ["终端上样 物料布置","助销道具 演示片源","产品升级 异常处理","总部统一 销售策略","活动通知","经验分享"],  //直辖市
 ["推广技能","终端技能","广宣技能","培训技能","督导技能","终端技能"],  //江苏省
 [],  //福建省
 [],  //广东省
 []  //甘肃省
];
 

function getCity(){
 //获得身省份和城市下拉列表框的引用
 var sltProvince=document.forms["theForm"].elements["province"];
 var sltCity=document.forms["theForm"].elements["city"];
 //得到对应于省份的城市列表数组
 var provinceCity=city[sltProvince.selectedIndex-1];
 
 //将城市下拉列表框清空，仅留第一个提示选项
 sltCity.length=1;
 //将相应省市的城市填充到城市选择框中
 for(var i=0;i<provinceCity.length;i++){
  //创建新的Option对象并将其添加到城市下拉列表框中
  sltCity[i+1]=new Option(provinceCity[i],provinceCity[i]);
 }
}
