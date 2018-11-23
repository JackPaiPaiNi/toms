/**
 * 仪表盘条形码加载隐藏或显示
 */
function salesRatioLoadTheBarcode(showAndHide){
	if('show' == showAndHide){
		$('#loadingAllSaleVolume').show();
		$('#loadingAllSaleRevenue').show();
	}else{
		$('#loadingAllSaleVolume').hide();
		$('#loadingAllSaleRevenue').hide();
	};
};

/**
 * 重点产品折线图
 * @param name
 * @param texts
 * @param yearArr
 * @param timeArr
 * @param showArr
 */
function loadCodeLineChar(name,texts,yearArr,timeArr,showArr){
	var myChart = echarts.init($("[name='"+name+"']")[0]); 
	option = {
	    title: {
	        text: texts
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:yearArr
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    toolbox: {
	        feature: {
	            saveAsImage: {}
	        },
	        show : true
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: timeArr
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: showArr
	};
	$('#loadingCodeSalesRank').hide();
	myChart.setOption(option); 
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

/**
 * 折线图
 * @param name
 * @param texts
 * @param yearArr
 * @param timeArr
 * @param showArr
 */
function loadLineChar(name,texts,yearArr,timeArr,showArr){
	var myChart = echarts.init($("[name='"+name+"']")[0]); 
	option = {
	    title: {
	        text: texts
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:yearArr
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    toolbox: {
	        feature: {
	            saveAsImage: {}
	        },
	        show : true
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: timeArr
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: showArr
	};
	myChart.setOption(option); 
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

/**
 * 加载折线图
 * @param name
 * @param texts
 * @param yearArr
 * @param timeArr
 * @param oneArr
 * @param twoArr
 */
function loadLineChart(name,texts,yearArr,timeArr,oneArr,twoArr){
	var myChart = echarts.init($("[name='"+name+"']")[0]); 
	option = {
	    title: {
	        text: texts
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	        data:yearArr
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    toolbox: {
	        feature: {
	            saveAsImage: {}
	        },
	        show : true
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: false,
	        data: timeArr
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: [
	        {
	            name:yearArr[0],
	            type:'line',
	            stack: '总量',
	            data:oneArr
	        },
	        {
	            name:yearArr[1],
	            type:'line',
	            stack: '总量',
	            data:twoArr
	        }
	    ]
	};
	myChart.setOption(option); 
	
	
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

/**
 * 仪表盘生成
 * @param name
 * @param numSele
 * @param volumeIsMoney
 */
function instrumentpanel(name,numSele,volumeIsMoney){
	var myChart = echarts.init($("[name='"+name+"']")[0]); 
	option = {
	    tooltip : {
	        formatter: "{a} <br/>{b} : {c}%"
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    series : [
	        {
	            name:'业务指标',
	            type:'gauge',
	            splitNumber: 10,       // 分割段数，默认为5
	            axisLine: {            // 坐标轴线
	                lineStyle: {       // 属性lineStyle控制线条样式
//	                    color: [[0.45, 'red'],[1.54, '#228b22']], //改变颜色
	                	color: [[0.295, 'red'],[0.454, 'orange'],[1.251, 'green']], 
	                    width: 8
	                }
	            },
	            radius: '100%',
	            axisTick: {            // 坐标轴小标记
	                splitNumber: 10,   // 每份split细分多少段
	                length :12,        // 属性length控制线长
	                lineStyle: {       // 属性lineStyle控制线条样式
	                    color: 'auto'
	                }
	            },
	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: 'auto'
	                }
	            },
	            splitLine: {           // 分隔线
	                show: true,        // 默认显示，属性show控制显示与否
	                length :30,         // 属性length控制线长
	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	                    color: 'auto'
	                }
	            },
	            pointer : {
	                width : 5
	            },
	            title : {
	                show : true,
	                offsetCenter: [0, '-40%'],       // x, y，单位px
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    fontWeight: 'bolder'
	                }
	            },
	            detail : {
	                formatter:'{value}%',
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: 'auto',
	                    fontWeight: 'bolder'
	                }
	            },
	            data:[{value: 50, name: volumeIsMoney}]
	        }
	    ]
	};
	salesRatioLoadTheBarcode('hide');
	option.series[0].data[0].value = numSele;
	myChart.setOption(option, true);
	
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

/**
 * 横圆柱体
 * @param byId
 * @param nameArray
 * @param weekDataArr
 * @param monthDataArr
 * @param quarterDataArr
 * @param yearDataArr
 * @param comment
 */
function nog(byId,nameArray,weekDataArr,monthDataArr,quarterDataArr,yearDataArr,comment){
	var myChart = echarts.init($("[name='"+byId+"']")[0]);
	option = {
	    title : {
	        text: comment,
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	    	selectedMode:'single',
	        data:['Week', 'Month','Quarter','Year']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType: {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    grid: {
	        left: '5%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'value',
	            boundaryGap : [0, 0.01]
	        }
	    ],
	    
	    yAxis : [
	        {
	            type : 'category',
	            data : nameArray
	        }
	    ],
	    series : [
	        {
	            name:'Week',
	            type:'bar',
	            data:weekDataArr
	        },
	        
	        {
	            name:'Month',
	            type:'bar',
	            data:monthDataArr
	        },
	       {
	            name:'Quarter',
	            type:'bar',
	            data:quarterDataArr
	        },
	        {
	            name:'Year',
	            type:'bar',
	            data:yearDataArr
	        }
	      
	    ]
	};
	$('#loadingAreaSales').hide();
	                    
	myChart.setOption(option);
		
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

/**
 * 区域商店销售进度横圆柱体
 * @param byId
 * @param nameArray
 * @param weekDataArr
 * @param comment
 * @param seriesName
 */
function areaSaleNog(byId,nameArray,weekDataArr,comment,seriesName){
	var myChart = echarts.init($("[name='"+byId+"']")[0]);
	option = {
	    title : {
	        text: comment,
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType: {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    
	    grid: {
	        left: '5%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'value',
	            boundaryGap : [0, 0.01]
	        }
	    ],
	    
	    yAxis : [
	        {
	            type : 'category',
	            data : nameArray
	        }
	    ],
	    series : [
	        {
	            name:seriesName,
	            type:'bar',
	            data:weekDataArr
	        }
	    ]
	};
	$('#loadingAreaSales').hide();
	                    
	myChart.setOption(option);
		
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}

/**
 * 销售百分比排名横圆柱体
 * @param byId
 * @param nameArray
 * @param weekDataArr
 * @param monthDataArr
 * @param quarterDataArr
 * @param yearDataArr
 * @param comment
 */
function salePercentageNog(byId,nameArray,weekDataArr,monthDataArr,quarterDataArr,yearDataArr,comment){
	var myChart = echarts.init($("[name='"+byId+"']")[0]);
	option = {
	    title : {
	        text: comment,
	       /* subtext: '数据来自网络'*/
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	    	selectedMode:'single',
	        data:['Week', 'Month','Quarter','Year']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType: {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
//	    calculable : true,
	    
	    grid: {
	        left: '5%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'value',
	            boundaryGap : [0, 0.01]
	        }
	    ],
	    
	    yAxis : [
	        {
	            type : 'category',
	            data : nameArray
	        }
	    ],
	    series : [
	        {
	            name:'Week',
	            type:'bar',
	            data:weekDataArr
	        },
	        
	        {
	            name:'Month',
	            type:'bar',
	            data:monthDataArr
	        },
	       {
	            name:'Quarter',
	            type:'bar',
	            data:quarterDataArr
	        },
	        {
	            name:'Year',
	            type:'bar',
	            data:yearDataArr
	        }
	      
	    ]
	};
	$('#loadingSalesRank').hide();
	                    
	myChart.setOption(option);
		
	window.addEventListener('resize', function () {
		myChart.resize();
	});
}


/**
 * 年份对比（只适合两条折现）
 * @param byId
 * @param headNameList
 * @param headDateArrty
 * @param tailDateArrty
 * @param headValueArray
 * @param tailValueArray
 */
function yearContrast(byId,headNameList,headDateArrty,tailDateArrty,headValueArray,tailValueArray){
	
	var myChart = echarts.init($("[name='"+byId+"']")[0]);
	var colors = ['#5793f3', '#d14a61', '#675bba'];
	

	option = {
	    color: colors,

	    tooltip: {
	        trigger: 'none',
	        axisPointer: {
	            type: 'cross'
	        }
	    },
	    legend: {
	        data:  headNameList //['2015 降水量', '2016 降水量']
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType: {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    grid: {
	        top: 70,
	        bottom: 50
	    },
	    xAxis: [
	        {
	            type: 'category',
	            axisTick: {
	                alignWithLabel: true
	            },
	            axisLine: {
	                onZero: false,
	                lineStyle: {
	                    color: colors[1]
	                }
	            },
	            axisPointer: {
	                label: {
	                    formatter: function (params) {
	                        return '降水量  ' + params.value
	                            + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
	                    }
	                }
	            },
	            data: headDateArrty//["2016-1", "2016-2", "2016-3", "2016-4", "2016-5", "2016-6", "2016-7", "2016-8", "2016-9", "2016-10", "2016-11", "2016-12"]
	        },
	        {
	            type: 'category',
	            axisTick: {
	                alignWithLabel: true
	            },
	            axisLine: {
	                onZero: false,
	                lineStyle: {
	                    color: colors[0]
	                }
	            },
	            axisPointer: {
	                label: {
	                    formatter: function (params) {
	                        return '降水量  ' + params.value
	                            + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
	                    }
	                }
	            },
	            data: tailDateArrty//["2015-1", "2015-2", "2015-3", "2015-4", "2015-5", "2015-6", "2015-7", "2015-8", "2015-9", "2015-10", "2015-11", "2015-12"]
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name:headNameList[0], //'2015 降水量',
	            type:'line',
	            xAxisIndex: 1,
	            smooth: true,
	            data: headValueArray//[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
	        },
	        {
	            name: headNameList[1], //'2016 降水量',
	            type:'line',
	            smooth: true,
	            data: tailValueArray//[3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3, 0.7]
	        }
	    ]
	};
	$('#loadingContrast').hide();
		myChart.setOption(option); 
		window.addEventListener('resize', function () {//自适应
			myChart.resize();
		});
}

function yearContrast(byId,headNameList,headDateArrty,tailDateArrty,headValueArray,tailValueArray){
	var myChart = echarts.init($("[name='"+byId+"']")[0]);
	var colors = ['#5793f3', '#d14a61', '#675bba'];

	option = {
	    color: colors,

	    tooltip: {
	        trigger: 'none',
	        axisPointer: {
	            type: 'cross'
	        }
	    },
	    toolbox: {
	        show: true,
	        feature: {
	            dataZoom: {
	                yAxisIndex: 'none'
	            },
	            dataView: {readOnly: false},
	            magicType: {type: ['line', 'bar']},
	            restore: {},
	            saveAsImage: {}
	        }
	    },
	    legend: {
	        data:  headNameList //['2015 降水量', '2016 降水量']
	    },
	    
	    grid: {
	        top: 70,
	        bottom: 50
	    },
	    xAxis: [
	        {
	            type: 'category',
	            axisTick: {
	                alignWithLabel: true
	            },
	            axisLine: {
	                onZero: false,
	                lineStyle: {
	                    color: colors[1]
	                }
	            },
	            axisPointer: {
	                label: {
	                    formatter: function (params) {
	                        return 'SalesVolume  ' + params.value
	                            + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
	                    }
	                }
	            },
	            data: headDateArrty//["2016-1", "2016-2", "2016-3", "2016-4", "2016-5", "2016-6", "2016-7", "2016-8", "2016-9", "2016-10", "2016-11", "2016-12"]
	        },
	        {
	            type: 'category',
	            axisTick: {
	                alignWithLabel: true
	            },
	            axisLine: {
	                onZero: false,
	                lineStyle: {
	                    color: colors[0]
	                }
	            },
	            axisPointer: {
	                label: {
	                    formatter: function (params) {
	                        return 'SalesVolume  ' + params.value
	                            + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
	                    }
	                }
	            },
	            data: tailDateArrty//["2015-1", "2015-2", "2015-3", "2015-4", "2015-5", "2015-6", "2015-7", "2015-8", "2015-9", "2015-10", "2015-11", "2015-12"]
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name:headNameList[0], //'2015 降水量',
	            type:'line',
	            xAxisIndex: 1,
	            smooth: true,
	            data: headValueArray//[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
	        },
	        {
	            name: headNameList[1], //'2016 降水量',
	            type:'line',
	            smooth: true,
	            data: tailValueArray//[3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3, 0.7]
	        }
	    ]
	};
	$('#loadingContrast').hide();
		myChart.setOption(option); 
		window.addEventListener('resize', function () {//自适应
			myChart.resize();
		});
}
 


