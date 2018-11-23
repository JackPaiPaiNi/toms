function loadYearTotal(obj, beginDate, endDate, what, head) {// 加载数据
	loadYearTotalData(obj, beginDate, endDate, what, head);
};

function loadYearTotalHead(obj, beginDate, endDate, what, head) {// 加载表头
	var beg = beginDate.split("-");
	var html = "";
	html += '<thead class="bluegrey">';
	html += '<tr>';
	html += '	<th rowspan="2">' + beg[0] + ' Year ' + head + ' Sell-Out</th>';
	html += '	<th colspan="3">' + beg[0] + ' Year ' + head + ' Sell-Out</th>';
	html += '	<th colspan="3">' + beg[0] + ' Q1 ' + head + ' Sell-Out</th>';
	html += '	<th colspan="3">' + beg[0] + ' Q2 ' + head + ' Sell-Out</th>';
	html += '	<th colspan="3">' + beg[0] + ' Q3 ' + head + ' Sell-Out</th>';
	html += '	<th colspan="3">' + beg[0] + ' Q4 ' + head + ' Sell-Out</th>';
	html += '</tr>';
	html += '<tr>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '</tr>';
	html += '</thead>';
	return html;
};

function loadYearTotalData(obj, beginDate, endDate, what, head) {// 加载数据
	var d = new Date();
	var str = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
	var beg = beginDate.split("-");
	var tYear = d.getFullYear();
	var tMonth = (d.getMonth() + 1);
	var by = obj.data;
	var html = "";
	html += '<tbody>';

	var center = "";
	if (by.length >= 1) {
		center = by[0].center;
	}

	var saleQtyYear = 0;
	var saleQtyQ1 = 0;
	var saleQtyQ2 = 0;
	var saleQtyQ3 = 0;
	var saleQtyQ4 = 0;

	var TargetQtyYear = 0;
	var TargetQtyQ1 = 0;
	var TargetQtyQ2 = 0;

	var TargetQtyQ3 = 0;
	var TargetQtyQ4 = 0;

	var saleQtyYearOBC = 0;
	var saleQtyQ1OBC = 0;
	var saleQtyQ2OBC = 0;
	var saleQtyQ3OBC = 0;
	var saleQtyQ4OBC = 0;

	var TargetQtyYearOBC = 0;
	var TargetQtyQ1OBC = 0;
	var TargetQtyQ2OBC = 0;
	var TargetQtyQ3OBC = 0;
	var TargetQtyQ4OBC = 0;

	var counHtml = "";
	for (var i = 0; i < by.length; i++) {
		if (center != null && center != "" && center == by[i].center) {
			saleQtyYear += by[i].saleQtyYear;
			saleQtyQ1 += by[i].saleQtyQ1;
			saleQtyQ2 += by[i].saleQtyQ2;
			saleQtyQ3 += by[i].saleQtyQ3;
			saleQtyQ4 += by[i].saleQtyQ4;

			TargetQtyYear += by[i].TargetQtyYear;
			TargetQtyQ1 += by[i].TargetQtyQ1;
			TargetQtyQ2 += by[i].TargetQtyQ2;
			TargetQtyQ3 += by[i].TargetQtyQ3;
			TargetQtyQ4 += by[i].TargetQtyQ4;

			saleQtyYearOBC += by[i].saleQtyYear;
			saleQtyQ1OBC += by[i].saleQtyQ1;
			saleQtyQ2OBC += by[i].saleQtyQ2;
			saleQtyQ3OBC += by[i].saleQtyQ3;
			saleQtyQ4OBC += by[i].saleQtyQ4;

			TargetQtyYearOBC += by[i].TargetQtyYear;
			TargetQtyQ1OBC += by[i].TargetQtyQ1;
			TargetQtyQ2OBC += by[i].TargetQtyQ2;
			TargetQtyQ3OBC += by[i].TargetQtyQ3;
			TargetQtyQ4OBC += by[i].TargetQtyQ4;

			counHtml += '<tr>';
			counHtml += '<td >' + isStringNullAvaliable(by[i].country)
					+ '</td>';
			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQtyYear) + '</td>';
			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].saleQtyYear) + '</td>';
			counHtml += TheBackgroundColor(isStringNullAvaliableNum(by[i].achYear));

			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQtyQ1) + '</td>';
			if (beg[0] == tYear && tMonth >= 1 && tMonth <= 3) {
				counHtml += '<td style="background-color: yellow;color:#000;text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQtyQ1) + '</td>';
			} else {
				counHtml += '<td style="text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQtyQ1) + '</td>';
			}
			counHtml += TheBackgroundColor( isStringNullAvaliableNum(by[i].achQ1))
					;

			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQtyQ2) + '</td>';

			if (beg[0] == tYear && tMonth >= 4 && tMonth <= 6) {
				counHtml += '<td style="background-color: yellow;color:#000;text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQtyQ2) + '</td>';
			} else {
				counHtml += '<td style="text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQtyQ2) + '</td>';
			}

			counHtml +=TheBackgroundColor( isStringNullAvaliableNum(by[i].achQ2));

			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQtyQ3) + '</td>';

			if (beg[0] == tYear && tMonth >= 7 && tMonth <= 9) {
				counHtml += '<td style="background-color: yellow;color:#000;text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQtyQ3) + '</td>';
			} else {
				counHtml += '<td style="text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQtyQ3) + '</td>';
			}

			counHtml +=TheBackgroundColor( isStringNullAvaliableNum(by[i].achQ3));

			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQtyQ4) + '</td>';

			if (beg[0] == tYear && tMonth >= 10 && tMonth <= 12) {
				counHtml += '<td style="background-color: yellow;color:#000;text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQtyQ4) + '</td>';
			} else {
				counHtml += '<td style="text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQtyQ4) + '</td>';
			}

			counHtml +=TheBackgroundColor(isStringNullAvaliableNum(by[i].achQ4));

			if (by.length - i == 1) {
				html += '<tr>';
				html += '<th>' + isStringNullAvaliable(by[i].center) + '</th>';
				html += '<th  style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQtyYear) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQtyYear) + '</th>';
				if (TargetQtyYear == 0 || TargetQtyYear == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQtyYear / TargetQtyYear * 100);
				}

				html += TheBackgroundColor( isStringNullAvaliableNum(ach) );

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQtyQ1) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQtyQ1) + '</th>';

				if (TargetQtyQ1 == 0 || TargetQtyQ1 == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQtyQ1 / TargetQtyQ1 * 100);
				}
				html += TheBackgroundColor(isStringNullAvaliableNum(ach)) ;

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQtyQ2) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQtyQ2) + '</th>';
				if (TargetQtyQ2 == 0 || TargetQtyQ2 == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQtyQ2 / TargetQtyQ2 * 100);
				}

				html +=TheBackgroundColor( isStringNullAvaliableNum(ach)) ;
				
				
				html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(TargetQtyQ3) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(saleQtyQ3) + '</th>';
			if (TargetQtyQ3 == 0 || TargetQtyQ3 == "0") {
				ach = 100;
			} else {
				ach = Math.round(saleQtyQ3 / TargetQtyQ3 * 100);
			}

			html +=TheBackgroundColor( isStringNullAvaliableNum(ach) );
			
			
			html += '<th style="text-align:right";>'
				+ isStringNullAvaliableNum(TargetQtyQ4) + '</th>';
		html += '<th style="text-align:right";>'
				+ isStringNullAvaliableNum(saleQtyQ4) + '</th>';
		if (TargetQtyQ4 == 0 || TargetQtyQ4 == "0") {
			ach = 100;
		} else {
			ach = Math.round(saleQtyQ4 / TargetQtyQ4 * 100);
		}

		html += TheBackgroundColor( isStringNullAvaliableNum(ach) );
				html += counHtml;

				html += '<tr>';
				html += '<th>BDSC</th>';
				html += '<th  style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQtyYearOBC) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQtyYearOBC) + '</th>';
				if (TargetQtyYearOBC == 0 || TargetQtyYearOBC == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQtyYearOBC / TargetQtyYearOBC * 100);
				}

				html += TheBackgroundColor( isStringNullAvaliableNum(ach) );

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQtyQ1OBC) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQtyQ1OBC) + '</th>';

				if (TargetQtyQ1OBC == 0 || TargetQtyQ1OBC == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQtyQ1OBC / TargetQtyQ1OBC * 100);
				}
				html +=TheBackgroundColor( isStringNullAvaliableNum(ach) );

				html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(TargetQtyQ2OBC) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(saleQtyQ2OBC) + '</th>';

			if (TargetQtyQ2OBC == 0 || TargetQtyQ2OBC == "0") {
				ach = 100;
			} else {
				ach = Math.round(saleQtyQ2OBC / TargetQtyQ2OBC * 100);
			}
			html +=TheBackgroundColor( isStringNullAvaliableNum(ach) );
			
			html += '<th style="text-align:right";>'
				+ isStringNullAvaliableNum(TargetQtyQ3OBC) + '</th>';
		html += '<th style="text-align:right";>'
				+ isStringNullAvaliableNum(saleQtyQ3OBC) + '</th>';

		if (TargetQtyQ3OBC == 0 || TargetQtyQ3OBC == "0") {
			ach = 100;
		} else {
			ach = Math.round(saleQtyQ3OBC / TargetQtyQ3OBC * 100);
		}
		html +=  TheBackgroundColor( isStringNullAvaliableNum(ach) );
		
		
		html += '<th style="text-align:right";>'
			+ isStringNullAvaliableNum(TargetQtyQ4OBC) + '</th>';
	html += '<th style="text-align:right";>'
			+ isStringNullAvaliableNum(saleQtyQ4OBC) + '</th>';

	if (TargetQtyQ4OBC == 0 || TargetQtyQ4OBC == "0") {
		ach = 100;
	} else {
		ach = Math.round(saleQtyQ4OBC / TargetQtyQ4OBC * 100);
	}
	html +=TheBackgroundColor( isStringNullAvaliableNum(ach) );
			}

		} else {
			var ach = 0;
			html += '<tr>';
			html += '<th>' + isStringNullAvaliable(by[i - 1].center) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(TargetQtyYear) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(saleQtyYear) + '</th>';
			if (TargetQtyYear == 0 || TargetQtyYear == "0") {
				ach = 100;
			} else {
				ach = Math.round(saleQtyYear / TargetQtyYear * 100);
			}

			html += TheBackgroundColor( isStringNullAvaliableNum(ach) ) ;

			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(TargetQtyQ1) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(saleQtyQ1) + '</th>';

			if (TargetQtyQ1 == 0 || TargetQtyQ1 == "0") {
				ach = 100;
			} else {
				ach = Math.round(saleQtyQ1 / TargetQtyQ1 * 100);
			}
			html += TheBackgroundColor( isStringNullAvaliableNum(ach) );

			
			html += '<th style="text-align:right";>'
				+ isStringNullAvaliableNum(TargetQtyQ2) + '</th>';
		html += '<th style="text-align:right";>'
				+ isStringNullAvaliableNum(saleQtyQ2) + '</th>';

		if (TargetQtyQ2 == 0 || TargetQtyQ2 == "0") {
			ach = 100;
		} else {
			ach = Math.round(saleQtyQ2 / TargetQtyQ2 * 100);
		}
		html += TheBackgroundColor( isStringNullAvaliableNum(ach) );

		
		html += '<th style="text-align:right";>'
			+ isStringNullAvaliableNum(TargetQtyQ3) + '</th>';
	html += '<th style="text-align:right";>'
			+ isStringNullAvaliableNum(saleQtyQ3) + '</th>';

	if (TargetQtyQ3 == 0 || TargetQtyQ3 == "0") {
		ach = 100;
	} else {
		ach = Math.round(saleQtyQ3 / TargetQtyQ3 * 100);
	}
	html += TheBackgroundColor( isStringNullAvaliableNum(ach) );

	
	html += '<th style="text-align:right";>'
		+ isStringNullAvaliableNum(TargetQtyQ4) + '</th>';
html += '<th style="text-align:right";>'
		+ isStringNullAvaliableNum(saleQtyQ4) + '</th>';

if (TargetQtyQ4 == 0 || TargetQtyQ4 == "0") {
	ach = 100;
} else {
	ach = Math.round(saleQtyQ4 / TargetQtyQ4 * 100);
}
html +=TheBackgroundColor( isStringNullAvaliableNum(ach) );

			html += counHtml;
			counHtml = "";
			center = by[i].center;
			saleQtyYear = 0;
			saleQtyQ1 = 0;
			saleQtyQ2 = 0;
			saleQtyQ3 = 0;
			saleQtyQ4 = 0;
			
			TargetQtyYear = 0;
			TargetQtyQ1 = 0;
			TargetQtyQ2 = 0;
			TargetQtyQ3 = 0;
			TargetQtyQ4 = 0;
			
			i--;
		}

	}

	html += '</tbody>';

	if (what == "Total") {
		$("#YearTotalData").html("");
		if (by.length > 0) {
			$("#YearTotalData").html(
					loadYearTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}
	} else if (what == "UD") {
		$("#YearUDData").html("");
		if (by.length > 0) {
			$("#YearUDData").html(
					loadYearTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	} else if (what == "XCP") {
		$("#YearXCPData").html("");
		if (by.length > 0) {
			$("#YearXCPData").html(
					loadYearTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	} else if (what == "Smart") {
		$("#YearSmartData").html("");
		if (by.length > 0) {
			$("#YearSmartData").html(
					loadYearTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	} else if (what == "Big") {
		$("#YearBigData").html("");

		if (by.length > 0) {
			$("#YearBigData").html(
					loadYearTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	} else if (what == "Curved") {
		$("#YearCurvedData").html("");
		if (by.length > 0) {
			$("#YearCurvedData").html(
					loadYearTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	}

};

// ===================================Quarter=======================================================================

function loadQuarterTotal(obj, beginDate, endDate, what, head) {// 加载数据
	loadQuarterTotalData(obj, beginDate, endDate, what, head);
};

function loadQuarterTotalHead(obj, beginDate, endDate, what, head) {// 加载表头

	var beg = beginDate.split("-");
	var html = "";
	html += '<thead class="bluegrey">';
	html += '<tr>';

	if (beg[1] == "1" || beg[1] == 1) {
		html += '	<th rowspan="2">' + beg[0] + ' Q1 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + ' Q1 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '01 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '02 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '03 ' + head + ' Sell-Out</th>';
	} else if (beg[1] == "4" || beg[1] == 4) {
		html += '	<th rowspan="2">' + beg[0] + ' Q2 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + ' Q2 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '04 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '05 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '06 ' + head + ' Sell-Out</th>';
	} else if (beg[1] == "7" || beg[1] == 7) {
		html += '	<th rowspan="2">' + beg[0] + ' Q3 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + ' Q3 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '07 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '08 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '09 ' + head + ' Sell-Out</th>';
	} else if (beg[1] == "10" || beg[1] == 10) {
		html += '	<th rowspan="2">' + beg[0] + ' Q4 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + ' Q4 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '10 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '11 ' + head + ' Sell-Out</th>';
		html += '	<th colspan="3">' + beg[0] + '12 ' + head + ' Sell-Out</th>';
	}

	html += '</tr>';
	html += '<tr>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '<th>Target</th>';
	html += '<th>Achieved</th>';
	html += '<th>Rate%</th>';
	html += '</tr>';
	html += '</thead>';
	return html;
};

function loadQuarterTotalData(obj, beginDate, endDate, what, head) {// 加载数据
	var d = new Date();
	var str = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
	var beg = beginDate.split("-");
	var end = endDate.split("-");
	var tYear = d.getFullYear();
	var tMonth = (d.getMonth() + 1);
	var qua=$("#QuarterTotalQ").val();
	 var isQ=getQ(tMonth)==qua;
	var by = obj.data;
	var html = "";
	html += '<tbody>';

	var center = "";
	if (by.length >= 1) {
		center = by[0].center;
	}

	var saleQty = 0;
	var saleQty1 = 0;
	var saleQty2 = 0;
	var saleQty3 = 0;

	var TargetQty = 0;
	var TargetQty1 = 0;
	var TargetQty2 = 0;
	var TargetQty3 = 0;

	var saleQtyOBC = 0;
	var saleQty1OBC = 0;
	var saleQty2OBC = 0;
	var saleQty3OBC = 0;

	var TargetQtyOBC = 0;
	var TargetQty1OBC = 0;
	var TargetQty2OBC = 0;
	var TargetQty3OBC = 0;

	var counHtml = "";
	for (var i = 0; i < by.length; i++) {
		if (center != null && center != "" && center == by[i].center) {
			saleQty += by[i].saleQty;
			saleQty1 += by[i].saleQty1;
			saleQty2 += by[i].saleQty2;
			saleQty3 += by[i].saleQty3;

			TargetQty += by[i].TargetQty;
			TargetQty1 += by[i].TargetQty1;
			TargetQty2 += by[i].TargetQty2;
			TargetQty3 += by[i].TargetQty3;

			saleQtyOBC += by[i].saleQty;
			saleQty1OBC += by[i].saleQty1;
			saleQty2OBC += by[i].saleQty2;
			saleQty3OBC += by[i].saleQty3;

			TargetQtyOBC += by[i].TargetQty;
			TargetQty1OBC += by[i].TargetQty1;
			TargetQty2OBC += by[i].TargetQty2;
			TargetQty3OBC += by[i].TargetQty3;

			counHtml += '<tr>';
			counHtml += '<td >' + isStringNullAvaliable(by[i].country)
					+ '</td>';
			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQty) + '</td>';
			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].saleQty) + '</td>';
			counHtml += TheBackgroundColor(isStringNullAvaliableNum(by[i].ach));

			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQty1) + '</td>';
			if (beg[0] == tYear
					&& (tMonth == 1 || tMonth == 4 || tMonth == 7 || tMonth == 10)
					&& isQ) {
				counHtml += '<td style="background-color: yellow;color:#000;text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQty1) + '</td>';

			} else {
				counHtml += '<td style="text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQty1) + '</td>';
			}
			counHtml +=TheBackgroundColor( isStringNullAvaliableNum(by[i].ach1))
					;

			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQty2) + '</td>';

			if (beg[0] == tYear
					&& (tMonth == 2 || tMonth == 5 || tMonth == 8 || tMonth == 11)
					&& isQ) {
				counHtml += '<td style="background-color: yellow;color:#000;text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQty2) + '</td>';

			} else {
				counHtml += '<td style="text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQty2) + '</td>';
			}

			counHtml += TheBackgroundColor( isStringNullAvaliableNum(by[i].ach2))
					;

			counHtml += '<td style="text-align:right";>'
					+ isStringNullAvaliableNum(by[i].TargetQty3) + '</td>';

			if (beg[0] == tYear
					&& (tMonth == 3 || tMonth == 6 || tMonth == 9 || tMonth == 12)
					&& isQ) {
				counHtml += '<td style="background-color: yellow;color:#000;text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQty3) + '</td>';

			} else {
				counHtml += '<td style="text-align:right";>'
						+ isStringNullAvaliableNum(by[i].saleQty3) + '</td>';
			}
			counHtml += TheBackgroundColor( isStringNullAvaliableNum(by[i].ach3))
				;

			if (by.length - i == 1) {
				html += '<tr>';
				html += '<th>' + isStringNullAvaliable(by[i].center) + '</th>';
				html += '<th  style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQty) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQty) + '</th>';
				if (TargetQty == 0 || TargetQty == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQty / TargetQty * 100);
				}

				html +=TheBackgroundColor( isStringNullAvaliableNum(ach)) ;

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQty1) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQty1) + '</th>';
				if (TargetQty1 == 0 || TargetQty1 == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQty1 / TargetQty1 * 100);
				}

				html +=TheBackgroundColor( isStringNullAvaliableNum(ach));

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQty2) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQty2) + '</th>';
				if (TargetQty2 == 0 || TargetQty2 == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQty2 / TargetQty2 * 100);
				}

				html +=TheBackgroundColor( isStringNullAvaliableNum(ach));

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQty3) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQty3) + '</th>';
				if (TargetQty3 == 0 || TargetQty3 == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQty3 / TargetQty3 * 100);
				}

				html += TheBackgroundColor( isStringNullAvaliableNum(ach)) ;

				html += counHtml;

				html += '<tr>';
				html += '<th>BDSC</th>';
				html += '<th  style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQtyOBC) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQtyOBC) + '</th>';
				if (TargetQtyOBC == 0 || TargetQtyOBC == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQtyOBC / TargetQtyOBC * 100);
				}

				html += TheBackgroundColor( isStringNullAvaliableNum(ach)) ;

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQty1OBC) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQty1OBC) + '</th>';
				if (TargetQty1OBC == 0 || TargetQty1OBC == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQty1OBC / TargetQty1OBC * 100);
				}

				html +=TheBackgroundColor( isStringNullAvaliableNum(ach)) ;

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQty2OBC) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQty2OBC) + '</th>';
				if (TargetQty2OBC == 0 || TargetQty2OBC == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQty2OBC / TargetQty2OBC * 100);
				}

				html +=  TheBackgroundColor( isStringNullAvaliableNum(ach));

				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(TargetQty3OBC) + '</th>';
				html += '<th style="text-align:right";>'
						+ isStringNullAvaliableNum(saleQty3OBC) + '</th>';
				if (TargetQty3OBC == 0 || TargetQty3OBC == "0") {
					ach = 100;
				} else {
					ach = Math.round(saleQty3OBC / TargetQty3OBC * 100);
				}

				html += TheBackgroundColor( isStringNullAvaliableNum(ach));
			}

		} else {
			var ach = 0;
			html += '<tr>';
			html += '<th>' + isStringNullAvaliable(by[i].center) + '</th>';
			html += '<th  style="text-align:right";>'
					+ isStringNullAvaliableNum(TargetQty) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(saleQty) + '</th>';
			if (TargetQty == 0 || TargetQty == "0") {
				ach = 100;
			} else {
				ach = Math.round(saleQty / TargetQty * 100);
			}

			html += TheBackgroundColor( isStringNullAvaliableNum(ach));

			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(TargetQty1) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(saleQty1) + '</th>';
			if (TargetQty1 == 0 || TargetQty1 == "0") {
				ach = 100;
			} else {
				ach = Math.round(saleQty1 / TargetQty1 * 100);
			}

			html +=TheBackgroundColor( isStringNullAvaliableNum(ach));

			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(TargetQty2) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(saleQty2) + '</th>';
			if (TargetQty2 == 0 || TargetQty2 == "0") {
				ach = 100;
			} else {
				ach = Math.round(saleQty2 / TargetQty2 * 100);
			}

			html += TheBackgroundColor( isStringNullAvaliableNum(ach));

			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(TargetQty3) + '</th>';
			html += '<th style="text-align:right";>'
					+ isStringNullAvaliableNum(saleQty3) + '</th>';
			if (TargetQty3 == 0 || TargetQty3 == "0") {
				ach = 100;
			} else {
				ach = Math.round(saleQty3 / TargetQty3 * 100);
			}
			html += TheBackgroundColor( isStringNullAvaliableNum(ach));

			html += counHtml;
			counHtml = "";
			center = by[i].center;
			saleQty = 0;
			saleQty1 = 0;
			saleQty2 = 0;
			saleQty3 = 0;

			TargetQty = 0;
			TargetQty1 = 0;
			TargetQty2 = 0;
			TargetQty3 = 0;

			i--;
		}

	}

	html += '</tbody>';

	if (what == "Total") {
		$("#QuarterTotalData").html("");
		if (by.length > 0) {
			$("#QuarterTotalData").html(
					loadQuarterTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}
	} else if (what == "UD") {
		$("#QuarterUDData").html("");
		if (by.length > 0) {
			$("#QuarterUDData").html(
					loadQuarterTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	} else if (what == "XCP") {
		$("#QuarterXCPData").html("");
		if (by.length > 0) {
			$("#QuarterXCPData").html(
					loadQuarterTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	} else if (what == "Smart") {
		$("#QuarterSmartData").html("");
		if (by.length > 0) {
			$("#QuarterSmartData").html(
					loadQuarterTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	} else if (what == "Big") {
		$("#QuarterBigData").html("");

		if (by.length > 0) {
			$("#QuarterBigData").html(
					loadQuarterTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	} else if (what == "Curved") {
		$("#QuarterCurvedData").html("");
		if (by.length > 0) {
			$("#QuarterCurvedData").html(
					loadQuarterTotalHead(obj, beginDate, endDate, what, head)
							+ html);
		}

	}

};

function TheBackgroundColor(num) {// 达成率背景加载颜色
	if (typeof (num) == "string") {
		if (num.indexOf("%") >= 0) {
			num = num.substr(0, num.length - 1);
		}

		if (typeof (num) == "string") {
			num = num * 1;
		}
	}
	//超过95%为绿色，70%-94%为黄色，69%以下为红色
	if (num >=95) {
		return "<td  style='background-color: green;color:#fff;text-align:center;'>"
				+ num + "%</td>";
	} else if(num<95 && num>69){
		return "<td   style='background-color: yellow;color:#000;text-align:center;'>" + num + "%</td>";

	}else{
		return "<td   style='background-color: red;color:#fff;text-align:center;'>" + num + "%</td>";
	}
};
function getQ(month){
	if(month>=1 && month<=3){
		return "Q1"
	}else 	if(month>=4 && month<=6){
		return "Q2"
	}else 	if(month>=7 && month<=9){
		return "Q3"
	}else 	if(month>=10 && month<=12){
		return "Q4"
	}
}